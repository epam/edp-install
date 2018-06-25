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
        vars['deployTemplatesPath'] = "${vars.devopsRoot}/${vars.deployTemaplatesDirectory}"

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
    }

    if(vars.updatedApplicaions.isEmpty()) {
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

    stage("PROMOTE IMAGES") {
        vars['targetProject'] = "${vars.projectMap.promotion.get('env-to-promote')}-meta"
        vars['sourceProject'] = vars.metaProject
        if (vars.targetProject) {
            stage = load "${vars.pipelinesPath}/stages/promote-images.groovy"
            stage.run(vars)
        }
        else
            println("[JENKINS][WARNING] There are no environments specified to promote images, promotion was skipped")
    }
}