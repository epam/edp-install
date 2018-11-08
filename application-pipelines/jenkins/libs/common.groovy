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
import hudson.FilePath
import groovy.json.*

void runStage(vars, stageDirectory, stageName) {
    def applicationType = vars.itemMap.type
    def applicationBuildTool = vars.itemMap.build_tool.toLowerCase()
    try {
        def stageExecutionFile = new FilePath(Jenkins.getInstance().getComputer(env['NODE_NAME']).getChannel(),
                "${vars.stagesRoot}/${stageDirectory}/${applicationType}/${applicationBuildTool}.groovy")
        if (stageExecutionFile.exists()) {
            println("[JENKINS][DEBUG] Stage execution file for stage ${stageName} for type ${applicationType}" +
                    " and build tool ${applicationBuildTool} has been found")
            dir("${vars.stagesRoot}/${stageDirectory}/${applicationType}") {
                def source = load "${applicationBuildTool}.groovy"
                if (!stageName)
                    source.run(vars)
                else
                    stage(stageName) { source.run(vars) }
            }
        }
        else
            println("[JENKINS][DEBUG] Stage execution file for stage ${stageName} for type ${applicationType}" +
                    " and build tool ${applicationBuildTool} has not been found. Stage ${stageName} is skiped.")
    }
    catch (Exception ex) {
        failJob("[JENKINS][ERROR] Execution stage ${stageName} for the service ${vars.itemMap.name} has been failed." +
                " Reason - ${ex.getMessage()}")
    }
}

def getConstants(vars) {
    BUSINESS_APPLICATION_TYPE="business"
    AUTOTEST_APPLICATION_TYPE="autotest"

    DEFAULT_OPERATIONS_TIMEOUT = "30"
    DEFAULT_GERRIT_AUTOUSER = "jenkins"
    DEFAULT_GERRIT_HOST = "gerrit"
    DEFAULT_GERRIT_CREDENTIALS = "jenkins"
    DEFAULT_DEPLOY_TEMPLATES_DIRECTORY = "deploy-templates"
    DEFAULT_NEXUS_AUTOUSER = "jenkins"

    vars['workDir'] = "${WORKSPACE}/${RandomStringUtils.random(10, true, true)}"
    vars['deployTemplatesDirectory'] = env.DEPLOY_TEMPLATES_DIRECTORY ? DEPLOY_TEMPLATES_DIRECTORY : DEFAULT_DEPLOY_TEMPLATES_DIRECTORY
    vars['operationsTimeout'] = env.OPERATIONS_TIMEOUT ? OPERATIONS_TIMEOUT : DEFAULT_OPERATIONS_TIMEOUT

    vars['configMapName'] = 'project-settings'
    vars['envSettingsKey'] = 'env.settings.json'
    vars['appSettingsKey'] = 'app.settings.json'
    vars['atSettingsKey'] = 'auto-test.settings.json'
    vars['svcSettingsKey'] = 'service.settings.json'

    vars['gerritCredentials'] = env.GERRIT_CREDENTIALS ? GERRIT_CREDENTIALS : DEFAULT_GERRIT_CREDENTIALS
    vars['gerritAutoUser'] = env.GERRIT_AUTOUSER ? GERRIT_AUTOUSER : DEFAULT_GERRIT_AUTOUSER
    vars['gerritHost'] = env.GERRIT_HOST ? env.GERRIT_HOST : DEFAULT_GERRIT_HOST
    vars['gerritProject'] = env.GERRIT_PROJECT ? env.GERRIT_PROJECT : env.GERRIT_PROJECT_NAME
    vars['gerritSshPort'] = sh(
            script: "oc get svc gerrit -o jsonpath='{.spec.ports[?(@.name==\"ssh\")].targetPort}'",
            returnStdout: true
    ).trim()
    vars['nexusAutoUser'] = env.NEXUS_AUTOUSER ? NEXUS_AUTOUSER : DEFAULT_NEXUS_AUTOUSER
    vars['gitApplicationUrl'] = "ssh://${vars.gerritAutoUser}@${vars.gerritHost}:${vars.gerritSshPort}/${vars.gerritProject}"

    vars['sonarRoute'] = sh(
            script: "oc get route sonar -o jsonpath='{.spec.host}'",
            returnStdout: true
    ).trim()

    vars["wildcard"] = sh(
            script: "oc get cm user-settings -o jsonpath='{.data.dns_wildcard}'",
            returnStdout: true
    ).trim()

    vars["projectPrefix"] = sh(
            script: "oc get cm user-settings -o jsonpath='{.data.edp_name}'",
            returnStdout: true
    ).trim()

    vars['jenkinsToken'] = sh(
            script: "oc -n ${vars.projectPrefix}-edp-cicd get secret jenkins-token " +
                    "--template='{{ index .data \"token\" }}' | base64 --decode",
            returnStdout: true
    ).trim()

    [vars.envSettingsKey, vars.appSettingsKey, vars.atSettingsKey, vars.svcSettingsKey].each() { key ->
        try {
            def settingsJson = sh(
                    script: "oc get cm ${vars.configMapName} --template='{{ index .data \"${key}\" }}'",
                    returnStdout: true
            ).trim()
            vars["${key}"] = new JsonSlurperClassic().parseText(settingsJson)
        }
        catch (Exception ex) {
            failJob("[JENKINS][ERROR] Can't load project settings from config map ${vars.configMapName} key ${key}. Reason - ${ex}")
        }
    }

    vars['mavenSettings'] = "${vars.pipelinesPath}/settings/maven/settings.xml"
    vars['gradleInitScript'] = "${vars.pipelinesPath}/settings/gradle/init.gradle"
    vars['nexusMavenRepositoryUrl'] = "http://nexus:8081/repository/maven"

    vars['npmGroupRegistry']="http://nexus:8081/repository/npm-all/"
    vars['npmInternalRegistry']="http://nexus:8081/repository/npm-internal/"

    vars['nugetInternalRegistry'] = "http://nexus:8081/repository/nuget-hosted/"
    vars['nexusScripts'] = [:]
    vars['nexusScripts']['getNugetToken'] = "get-nuget-token"
    vars['nexusScriptsPath'] = "${vars.pipelinesPath}/files/nexus"

}

def getItemMap(name, configMapKey) {
    for (item in vars["${configMapKey}"]) {
        if (item.name == name)
            return (item)
    }
    return (null)
}

void failJob(failMessage) {
    println(failMessage)
    currentBuild.displayName = "${currentBuild.displayName}-FAILED"
    currentBuild.result = 'FAILURE'
    error failMessage
}

void getDebugInfo(vars) {
    def debugOutput = ""
    vars.keySet().each { key ->
        debugOutput = debugOutput + "${key}=${vars["${key}"]}\n"
    }
    println("[JENKINS][DEBUG] Pipeline's variables:\n${debugOutput}")
}

def getBuildCause() {
    def buildCause = sh(
            script: "oc get build ${vars.deployProject}-deploy-pipeline-${BUILD_NUMBER} -o jsonpath='{.spec.triggeredBy[0].message}'",
            returnStdout: true
    ).trim()
    return(buildCause)
}

return this;