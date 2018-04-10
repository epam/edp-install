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

        commonLib = load "${vars.pipelinesPath}/libs/common.groovy"
        commonLib.getConstants(vars)

        vars['devopsRoot'] = "${WORKSPACE.replaceAll("@", "")}@script"
        vars['environment'] = JOB_NAME.split('-')[0]
        vars['deployProject'] = "${vars.environment}-${env.BUILD_NUMBER}"

        vars.get(vars.appSettingsKey).each() { application ->
            application['version'] = env["${application.name}_VERSION"] != null ? env["${application.name}_VERSION"] : "latest"
        }

        commonLib.getDebugInfo(vars)
        currentBuild.displayName = "${currentBuild.displayName}-${vars.environment}"
    }

    stage("DEPLOY") {
        stage = load "${vars.devopsRoot}/stages/deploy.groovy"
        stage.run(vars)
    }

    vars["${vars.envSettingsKey}"]["${vars['environment']}"]['quality-gates'].each() { qualityGate ->
        stage(qualityGate['step_name']) {
            try {
                switch (qualityGate.type) {
                    case "autotests":
                        vars['atProject'] = qualityGate.project
                        stage = load "${vars.devopsRoot}/stages/automation-tests.groovy"
                        stage.run(vars)
                        break
                    case "manual":
                        input "Is everything OK on project ${vars.deployProject}?"
                        break
                }
            }
            catch (Exception ex) {
                if (qualityGate.type == "manual") {
                    vars['projectsToDelete'] = ["${vars.deployProject}"]
                    stage = load "delete-environment.groovy"
                    stage.run(vars)
                }
                currentBuild.description = "${currentBuild.description}\r\nTest ${qualityGate.project} has been failed"
                commonLib.failJob("[JENKINS][ERROR] Automation test ${qualityGate.project} has been failed. Reason - ${ex}")
            }
        }
        currentBuild.description = "${currentBuild.description}\r\nTest ${qualityGate.project} has been passed"
    }

    stage("PROMOTE") {
        vars['sourceTag'] = "latest"
        vars['targetTags'] = [vars.sourceTag]
        vars['targetProject'] = vars["${vars.envSettingsKey}"]["${vars['environment']}"]['promotion']['env-to-promote']
        vars['sourceProject'] = vars.environment
        stage = load "promote.groovy"
        stage.run(vars)
    }

    if (envSettingsMap.get('delete-trigger').type == "successful-quality-gates") {
        stage("DELETE PROJECTS") {
            stage = load "get-projects-to-delete.groovy"
            stage.run(vars)

            stage = load "delete-environment.groovy"
            stage.run(vars)
        }
    }
}