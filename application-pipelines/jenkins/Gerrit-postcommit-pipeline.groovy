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
        vars["itemMap"] = commonLib.getItemMap(vars.gerritProject, vars.appSettingsKey)
        if (!vars["itemMap"])
            commonLib.failJob("[JENKINS][ERROR] Application ${vars.gerritProject} is not found in configmap" +
                    " ${vars.configMapName} key ${vars.appSettingsKey} please check")
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
        commonLib.failJob("[JENKINS][ERROR] Devops repository unstash has failed. Reason - ${ex}")
    }
    vars['workDir'] = "${WORKSPACE}/${RandomStringUtils.random(10, true, true)}"

    commonLib.getDebugInfo(vars)
    currentBuild.displayName = "${currentBuild.number}-${vars.serviceBranch}"
    currentBuild.description = "Name: ${vars.itemMap.name}\r\nLanguage: ${vars.itemMap.language}" +
            "\r\nBuild tool: ${vars.itemMap.build_tool}\r\nFramework: ${vars.itemMap.framework}"

    commonLib.runStage(vars, "checkout","CHECKOUT")
    commonLib.runStage(vars, "compile","COMPILE")
    commonLib.runStage(vars, "tests", "TESTS")
    commonLib.runStage(vars, "sonar", "SONAR")
    commonLib.runStage(vars, "build", "BUILD")
    commonLib.runStage(vars, "push-to-nexus", "PUSH-TO-NEXUS")
    commonLib.runStage(vars, "run-build-config", "RUN-BUILD-CONFIG")
}