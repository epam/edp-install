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
        vars['itemMap'] = commonLib.getItemMap(vars.gerritProject, vars.appSettingsKey)
        if (!vars.itemMap) {
            println("[JENKINS][DEBUG] Application ${vars.gerritProject} " +
                    "is not found in ${vars.appSettingsKey} key, trying to find in ${vars.atSettingsKey}")
            vars['itemMap'] = commonLib.getItemMap(vars.gerritProject, vars.atSettingsKey)
            if (!vars.itemMap)
                commonLib.failJob("[JENKINS][ERROR] Auto test project ${vars.gerritProject} " +
                        "is not found in ${vars.atSettingsKey} key, will fail job")
            else
                vars['itemMap']['type'] = AUTOTEST_APPLICATION_TYPE
        }
        else
            vars['itemMap']['type'] = BUSINESS_APPLICATION_TYPE
    }
}

node(vars.itemMap.build_tool.toLowerCase()) {
    vars['devopsRoot'] = new File("/tmp/${RandomStringUtils.random(10, true, true)}")
    vars['stagesRoot'] = "${vars.devopsRoot}/${vars.pipelinesPath}/stages"
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
    currentBuild.description = "Name: ${vars.itemMap.name}\r\nBuild tool: ${vars.itemMap.build_tool}"

    commonLib.runStage(vars, "checkout-gerrit","CHECKOUT")
    commonLib.runStage(vars, "compile","COMPILE")
    commonLib.runStage(vars, "tests", "TESTS")
    vars['serviceBranch'] = vars.gerritChange
    commonLib.runStage(vars, "sonar-gerrit", "SONAR-GERRIT")
    commonLib.runStage(vars, "sonar", "SONAR")
}