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
        vars['devopsRoot'] = "${WORKSPACE.replaceAll("@", "")}@script"
        vars['pipelinesPath'] = "${vars.devopsRoot}/${PIPELINES_PATH}"

        openshift.withProject() {
            def matcher = (JOB_NAME =~ /.*\\/${openshift.project()}-(.*)-deploy-pipeline/)
            vars['environment'] = matcher[0][1]
            vars['deployProject'] = "${vars.environment}-${env.BUILD_NUMBER}"
        }

        commonLib = load "${vars.pipelinesPath}/libs/common.groovy"
        commonLib.getConstants(vars)

        vars.get(vars.appSettingsKey).each() { application ->
            if (env["${application.name.toUpperCase().replaceAll("-", "_")}_VERSION"])
                application['version'] = env["${application.name.toUpperCase().replaceAll("-", "_")}_VERSION"]
            else
                application['version'] = "latest"
        }

        commonLib.getDebugInfo(vars)
        currentBuild.displayName = "${currentBuild.displayName}-${vars.environment}"
    }

    stage("DEPLOY") {
        stage = load "${vars.pipelinesPath}/stages/deploy.groovy"
        stage.run(vars)
    }

    vars["${vars.envSettingsKey}"]["${vars['environment']}"]['quality-gates'].each() { qualityGate ->
        stage(qualityGate['step-name']) {
            try {
                switch (qualityGate.type) {
                    case "autotests":
                        vars['atProject'] = qualityGate.project
                        stage = load "${vars.pipelinesPath}/stages/automation-tests.groovy"
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
                    stage = load "${vars.pipelinesPath}/stages/delete-environment.groovy"
                    stage.run(vars)
                }
                currentBuild.description = "${currentBuild.description}\r\nStage ${qualityGate['step-name']} has been failed"
                commonLib.failJob("[JENKINS][ERROR] Stage ${qualityGate['step-name']} has been failed. Reason - ${ex}")
            }
        }
        currentBuild.description = "${currentBuild.description}\r\nStage ${qualityGate['step-name']} has been passed"
    }

    stage("PROMOTE") {
        vars['sourceTag'] = "latest"
        vars['targetTags'] = [vars.sourceTag]
        vars['targetProject'] = vars["${vars.envSettingsKey}"]["${vars['environment']}"]['promotion']['env-to-promote']
        vars['sourceProject'] = vars.environment
        stage = load "${vars.pipelinesPath}/stages/promote.groovy"
        stage.run(vars)
    }

    if (envSettingsMap.get('delete-trigger').type == "successful-quality-gates") {
        stage("DELETE PROJECTS") {
            stage = load "${vars.pipelinesPath}/stages/get-projects-to-delete.groovy"
            stage.run(vars)

            stage = load "${vars.pipelinesPath}/stages/delete-environment.groovy"
            stage.run(vars)
        }
    }
}