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

import org.apache.commons.lang.RandomStringUtils

//Define common variables
vars = [:]
commonLib = null
nexusLib = null
buildToolLib = null

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
        def workspace = "${WORKSPACE.replaceAll("@.*", "")}@script"

        dir("${workspace}") {
            libPath = "${vars.pipelinesPath}/libs"
            stash name: 'devops', includes: "${vars.pipelinesPath}/**", useDefaultExcludes: false
            commonLib = load "${libPath}/common.groovy"
            commonLib.getConstants(vars)
            nexusLib = load "${libPath}/nexus.groovy"
        }

        vars['promoteImage'] = true
        if (vars["${vars.envSettingsKey}"])
            vars['targetProject'] = "${vars["${vars.envSettingsKey}"][0].name}-meta"
        else {
            println("[JENKINS][WARNING] There are no environments were added to the project, we won't promote image after build config\r\n" +
                    "[JENKINS][WARNING] If your like to promote your images please add environment via your cockpit panel")
            vars['promoteImage'] = false
        }
        vars['serviceBranch'] = env.GERRIT_BRANCH ? env.GERRIT_BRANCH : env.SERVICE_BRANCH
        vars['gerritPatchsetNumber'] = env.GERRIT_PATCHSET_NUMBER ? env.GERRIT_PATCHSET_NUMBER : 0
        vars['gerritChangeNumber'] = env.GERRIT_CHANGE_NUMBER ? env.GERRIT_CHANGE_NUMBER : 0
        vars["itemMap"] = commonLib.getItemMap(vars.gerritProject, vars.appSettingsKey)
        if (!vars["itemMap"])
            commonLib.failJob("[JENKINS][ERROR] Application ${vars.gerritProject} is not found in configmap" +
                    " ${vars.configMapName} key ${vars.appSettingsKey} please check")
        vars['itemMap']['type'] = BUSINESS_APPLICATION_TYPE

        def buildToolLibFile = new File("${workspace}/${libPath}/${vars.itemMap.build_tool.toLowerCase()}.groovy")
        if (buildToolLibFile.exists()) {
            buildToolLib = load "${buildToolLibFile}"
        }
    }
}

node(vars.itemMap.build_tool.toLowerCase()) {
    vars['devopsRoot'] = new File("/tmp/${RandomStringUtils.random(10, true, true)}")
    vars['stagesRoot'] = "${vars.devopsRoot}/${vars.pipelinesPath}/stages"
    if (buildToolLib)
        buildToolLib.getConstants()
    try {
        dir("${vars.devopsRoot}") {
            unstash 'devops'
        }
    } catch (Exception ex) {
        commonLib.failJob("[JENKINS][ERROR] Devops repository unstash has failed. Reason - ${ex}")
    }
    vars['workDir'] = "${WORKSPACE}/${RandomStringUtils.random(10, true, true)}"

    commonLib.getDebugInfo(vars)

    commonLib.runStage(vars, "checkout","CHECKOUT")
    commonLib.runStage(vars, "get-app-version","GET-APP-VERSION")
    currentBuild.displayName = "${vars.businissAppVersion}-${vars.serviceBranch}"
    currentBuild.description = "Name: ${vars.itemMap.name}\r\nLanguage: ${vars.itemMap.language}" +
            "\r\nBuild tool: ${vars.itemMap.build_tool}\r\nFramework: ${vars.itemMap.framework}"

    commonLib.runStage(vars, "compile","COMPILE")
    commonLib.runStage(vars, "tests", "TESTS")
    commonLib.runStage(vars, "sonar", "SONAR")
    commonLib.runStage(vars, "sonar-cleanup", "SONAR-CLEANUP")
    commonLib.runStage(vars, "build", "BUILD")
    commonLib.runStage(vars, "push-to-nexus", "PUSH-TO-NEXUS")
    commonLib.runStage(vars, "run-build-config", "RUN-BUILD-CONFIG")
    commonLib.runStage(vars, "git-tag", "GIT-TAG")
}