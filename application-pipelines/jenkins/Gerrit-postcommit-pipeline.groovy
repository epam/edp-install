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
        vars['promoteImage'] = true
        if (vars["${vars.envSettingsKey}"])
            vars['targetProject'] = vars["${vars.envSettingsKey}"][0].name
        else {
            println("[JENKINS][WARNING] There are no environments were added to the project, we won't promote image after build config\r\n" +
                    "[JENKINS][WARNING] If your like to promote your images please add environment via your cockpit panel")
            vars['promoteImage'] = false
        }
        vars['serviceBranch'] = env.GERRIT_BRANCH ? env.GERRIT_BRANCH : env.SERVICE_BRANCH
        vars["applicationMap"] = commonLib.getApplicationMap(vars.gerritProject)
        if (!vars["applicationMap"])
            commonLib.failJob("[JENKINS][ERROR] Application ${vars.gerritProject} is not found in configmap" +
                    " ${vars.configMapName} key ${vars.appSettingsKey} please check")
    }
}

node(vars.applicationMap.tool.toLowerCase()) {
    vars['devopsRoot'] = new File("/tmp/${RandomStringUtils.random(10, true, true)}")
    try {
        dir("${vars.devopsRoot}") {
            unstash 'devops'
        }
    } catch (Exception ex) {
        commonLib.failJob("[JENKINS][ERROR] Devops repository unstash has failed. Reason - ${ex}")
    }
    vars['workDir'] = "${WORKSPACE}/${RandomStringUtils.random(10, true, true)}"

    commonLib.getDebugInfo(vars)
    currentBuild.displayName = "${currentBuild.number}-${vars.serviceBranch}"
    currentBuild.description = "Name: ${vars.applicationMap.name}\r\nLanguage: ${vars.applicationMap.language}" +
            "\r\nBuild tool: ${vars.applicationMap.tool}\r\nFramework: ${vars.applicationMap.framework}"

    dir("${vars.devopsRoot}/${vars.pipelinesPath}/stages/checkout/") { commonLib.runStage("CHECKOUT", vars) }
    dir("${vars.devopsRoot}/${vars.pipelinesPath}/stages/compile/") { commonLib.runStage("COMPILE", vars) }
    dir("${vars.devopsRoot}/${vars.pipelinesPath}/stages/unit-tests/") { commonLib.runStage("UNIT-TESTS", vars) }
    dir("${vars.devopsRoot}/${vars.pipelinesPath}/stages/sonar/") { commonLib.runStage("SONAR", vars) }
    dir("${vars.devopsRoot}/${vars.pipelinesPath}/stages/build/") { commonLib.runStage("BUILD", vars) }
    dir("${vars.devopsRoot}/${vars.pipelinesPath}/stages/push-to-nexus/") { commonLib.runStage("PUSH-TO-NEXUS", vars) }
    dir("${vars.devopsRoot}/${vars.pipelinesPath}/stages/run-build-config/") { commonLib.runStage("RUN-BUILD-CONFIG", vars) }
}