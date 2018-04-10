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
            vars['targetProject'] = vars["${vars.envSettingsKey}"].keySets()[0]
        else {
            println("[JENKINS][WARNING] There are no environments were added to the project, we won't promote image after build config\r\n" +
                    "[JENKINS][WARNING] If your like to promote your images please add environment via your cockpit panel")
            vars['promoteImage'] = false
        }
        vars['serviceBranch'] = env.GERRIT_BRANCH ? env.GERRIT_BRANCH : env.SERVICE_BRANCH
        vars["application"] = commonLib.getApplicationMap(vars.gerritProject)
        if (!vars["application"])
            commonLib.failJob("[JENKINS][ERROR] Application ${vars.gerritProject} is not found in configmap" +
                    " ${vars.configMapName} key ${vars.appSettingsKey} please check")
    }
}

node(vars.application.tool.toLowerCase()) {
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
    currentBuild.description = "Name: ${vars.application.name}\r\nLanguage: ${vars.application.language}" +
            "\r\nBuild tool: ${vars.application.tool}\r\nFramework: ${vars.application.framework}"

    dir("${vars.devopsRoot}/${vars.pipelinesPath}/stages-v2/checkout/") { commonLib.newRunStage("CHECKOUT", vars) }
    dir("${vars.devopsRoot}/${vars.pipelinesPath}/stages-v2/compile/") { commonLib.newRunStage("COMPILE", vars) }
    dir("${vars.devopsRoot}/${vars.pipelinesPath}/stages-v2/unit-tests/") { commonLib.newRunStage("UNIT-TESTS", vars) }
    dir("${vars.devopsRoot}/${vars.pipelinesPath}/stages-v2/sonar/") { commonLib.newRunStage("SONAR", vars) }
    dir("${vars.devopsRoot}/${vars.pipelinesPath}/stages-v2/build/") { commonLib.newRunStage("BUILD", vars) }
    dir("${vars.devopsRoot}/${vars.pipelinesPath}/stages-v2/push-to-nexus/") { commonLib.newRunStage("PUSH-TO-NEXUS", vars) }
    dir("${vars.devopsRoot}/${vars.pipelinesPath}/stages-v2/run-build-config/") { commonLib.newRunStage("RUN-BUILD-CONFIG", vars) }
}