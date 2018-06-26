/* Copyright 2018 EPAM Systems.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at
http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.

See the License for the specific language governing permissions and
limitations under the License. */

import hudson.FilePath

//Define common variables
vars = [:]
vars['updatedApplicaions'] = []
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
        dir("${vars.pipelinesPath}") {
            stash name: 'mavenSettings', includes: "**/settings/**", useDefaultExcludes: false
        }

        openshift.withProject() {
            def matcher = (JOB_NAME =~ /.*\\/${openshift.project()}-(.*)-deploy-pipeline/)
            vars['pipelineProject'] = matcher[0][1]
            vars['metaProject'] = "${vars.pipelineProject}-meta"
            vars['deployProject'] = "${vars.pipelineProject}"
        }

        commonLib = load "${vars.pipelinesPath}/libs/common.groovy"
        commonLib.getConstants(vars)

        if (commonLib.getBuildCause() != "Image change") {
            def parameters = [string(
                    defaultValue: '',
                    description: "Project suffix for ${vars.pipelineProject} project where services will be deployed, leave blank to deploy to ${vars.pipelineProject}(default) project",
                    name: "PROJECT_SUFFIX",
                    trim: true)]
            vars.get(vars.appSettingsKey).each() { application ->
                application['tags'] = ['noImageExists']
                def imageStreamExists = sh(
                        script: "oc -n ${vars.metaProject} get is ${application.name} --no-headers | awk '{print \$1}'",
                        returnStdout: true
                ).trim()
                if (imageStreamExists != "")
                    application['tags'] = sh(
                            script: "oc -n ${vars.metaProject} get is ${application.name} -o jsonpath='{range .spec.tags[*]}{.name}{\"\\n\"}{end}'",
                            returnStdout: true
                    ).trim().tokenize()

                parameters.add(choice(choices: "${application.tags.join('\n')}", description: '', name: "${application.name.toUpperCase().replaceAll("-", "_")}_VERSION"))
            }
            vars['userInput'] = input id: 'userInput', message: 'Provide the following information', parameters: parameters

            if (vars.userInput["PROJECT_SUFFIX"] && vars.userInput["PROJECT_SUFFIX"] != "")
                vars['deployProject'] = "${vars.deployProject}-${vars.userInput["PROJECT_SUFFIX"]}"
        }

        vars.get(vars.appSettingsKey).each() { application ->
            if (vars.userInput && vars.userInput["${application.name.toUpperCase().replaceAll("-", "_")}_VERSION"])
                application['version'] = vars.userInput["${application.name.toUpperCase().replaceAll("-", "_")}_VERSION"]
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
    }

    if (vars.updatedApplicaions.isEmpty()) {
        println("[JENKINS][DEBUG] There are no application that have been updated, pipeline has stopped")
        return
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
                currentBuild.description = "${currentBuild.description}\r\nStage ${qualityGate['step-name']} has been failed"
                commonLib.failJob("[JENKINS][ERROR] Stage ${qualityGate['step-name']} has been failed. Reason - ${ex}")
            }
        }
        currentBuild.description = "${currentBuild.description}\r\nStage ${qualityGate['step-name']} has been passed"
    }

    if (vars.userInput && vars.userInput["PROJECT_SUFFIX"] && vars.userInput["PROJECT_SUFFIX"] != "") {
        println("[JENKINS][WARNING] Promote images from custom projects is prohibited and will be skipped")
        return
    }

    stage("PROMOTE IMAGES") {
        vars['targetProject'] = "${vars.projectMap.promotion.get('env-to-promote')}-meta"
        vars['sourceProject'] = vars.metaProject
        if (vars.targetProject) {
            stage = load "${vars.pipelinesPath}/stages/promote-images.groovy"
            stage.run(vars)
        } else
            println("[JENKINS][WARNING] There are no environments specified to promote images, promotion was skipped")
    }
}