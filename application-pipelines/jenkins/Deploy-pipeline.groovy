import hudson.FilePath

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
            vars['pipelineProject'] = matcher[0][1]
            vars['deployProject'] = "${vars.pipelineProject}-${env.BUILD_NUMBER}"
        }

        commonLib = load "${vars.pipelinesPath}/libs/common.groovy"
        commonLib.getConstants(vars)

        vars.get(vars.appSettingsKey).each() { application ->
            if (env["${application.name.toUpperCase().replaceAll("-", "_")}_VERSION"])
                application['version'] = env["${application.name.toUpperCase().replaceAll("-", "_")}_VERSION"]
            else
                application['version'] = "latest"
        }

        vars["projectMap"] = commonLib.getItemMap(vars.pipelineProject, vars.envSettingsKey)
        if (!vars["projectMap"])
            commonLib.failJob("[JENKINS][ERROR] Environment ${vars.pipelineProject} is not found in configmap" +
                    " ${vars.configMapName} key ${vars.envSettingsKey} please check")

        commonLib.getDebugInfo(vars)
        currentBuild.displayName = "${currentBuild.displayName}-${vars.deployProject}"
    }

    stage("DEPLOY") {
        stage = load "${vars.pipelinesPath}/stages/deploy.groovy"
        stage.run(vars)

        stage = load "${vars.pipelinesPath}/stages/adjust-routes.groovy"
        stage.run(vars, "-latest", "create")
    }

    vars.projectMap.get('quality-gates').each() { qualityGate ->

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
                stage = load "${vars.pipelinesPath}/stages/adjust-routes.groovy"
                stage.run(vars, "-latest", "delete")

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

    stage = load "${vars.pipelinesPath}/stages/adjust-routes.groovy"
    stage.run(vars, "-latest", "delete")

    stage("PROMOTE IMAGES") {
        vars['sourceTag'] = "latest"
        vars['targetTags'] = [vars.sourceTag]
        vars['targetProject'] = vars.projectMap.promotion.get('env-to-promote')
        vars['sourceProject'] = vars.pipelineProject
        if (vars.targetProject) {
            stage = load "${vars.pipelinesPath}/stages/promote-images.groovy"
            stage.run(vars)
        }
        else
            println("[JENKINS][WARNING] There are no environments specified to promote images, promotion was skipped")
    }

    if (vars.projectMap.get('delete-trigger') == "successful-quality-gates") {
        stage("DELETE PROJECTS") {
            stage = load "${vars.pipelinesPath}/stages/get-projects-to-delete.groovy"
            stage.run(vars)

            stage = load "${vars.pipelinesPath}/stages/delete-environment.groovy"
            stage.run(vars)
        }
    }

    stage = load "${vars.pipelinesPath}/stages/adjust-routes.groovy"
    stage.run(vars, "-stable", "create")
}