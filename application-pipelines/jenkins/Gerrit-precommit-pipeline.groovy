import org.apache.commons.lang.RandomStringUtils

//Define common variables
vars = [:]
commonLib = null

def checkEnvVariables(envVariable) {
    if (!env["${envVariable}"])
        error("[JENKINS][ERROR] ${envVariable} is mandatory to be specified, please check.")
}

node("master") {
    stage("INITIALIZATION") {
        ["PIPELINES_PATH"].each() { variable ->
            this.checkEnvVariables(variable)
        }
        vars['pipelinesPath'] = PIPELINES_PATH

        def workspace = "${WORKSPACE.replaceAll("@", "")}@script"
        dir("${workspace}") {
            stash name: 'devops', includes: "${vars.pipelinesPath}/**", useDefaultExcludes: false
            commonLib = load "${vars.pipelinesPath}/libs/common.groovy"
        }
        commonLib.getConstants(vars)

        vars['serviceBranch'] = GERRIT_BRANCH
        vars['gerritChange'] = "change-${GERRIT_CHANGE_NUMBER}-${GERRIT_PATCHSET_NUMBER}"
        vars["applicationMap"] = commonLib.getApplicationMap(vars.gerritProject)
        if (!vars["applicationMap"])
            commonLib.failJob("[JENKINS][ERROR] Application ${vars.gerritProject} is not found in configmap" +
                    " ${vars.configMapName} key ${vars.appSettingsKey} please check")
    }
}

node(vars.applicationMap.build_tool.toLowerCase()) {
    vars['devopsRoot'] = new File("/tmp/${RandomStringUtils.random(10, true, true)}")
    try {
        dir("${vars.devopsRoot}") {
            unstash 'devops'
        }
    } catch (Exception ex) {
        commonLib.failJob("[JENKINS][ERROR] Devops repository unstash has been failed. Reason - ${ex}")
    }
    vars['workDir'] = "${WORKSPACE}/${RandomStringUtils.random(10, true, true)}"

    commonLib.getDebugInfo(vars)
    currentBuild.displayName = "${currentBuild.number}-${vars.serviceBranch}(${vars.gerritChange})"
    currentBuild.description = "Name: ${vars.applicationMap.name}\r\nLanguage: ${vars.applicationMap.language}" +
            "\r\nBuild tool: ${vars.applicationMap.build_tool}\r\nFramework: ${vars.applicationMap.framework}"

    dir("${vars.devopsRoot}/${vars.pipelinesPath}/stages/checkout-gerrit/") { commonLib.runStage("CHECKOUT", vars) }
    dir("${vars.devopsRoot}/${vars.pipelinesPath}/stages/compile/") { commonLib.runStage("COMPILE", vars) }
    dir("${vars.devopsRoot}/${vars.pipelinesPath}/stages/unit-tests/") { commonLib.runStage("UNIT-TESTS", vars) }
    vars['serviceBranch'] = vars.gerritChange
    dir("${vars.devopsRoot}/${vars.pipelinesPath}/stages/sonar-gerrit/") { commonLib.runStage("SONAR-GERRIT", vars) }
    dir("${vars.devopsRoot}/${vars.pipelinesPath}/stages/sonar/") { commonLib.runStage("SONAR", vars) }
}