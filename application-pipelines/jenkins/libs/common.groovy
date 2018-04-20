import groovy.json.*
import org.apache.commons.lang.RandomStringUtils
import hudson.FilePath

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
        echo "${ex.getMessage()}"
        failJob("[JENKINS][ERROR] Execution stage ${stageName} for the service ${vars.itemMap.name} has been failed")
    }
}

def getConstants(vars) {
    BUSINESS_APPLICATION_TYPE="business"
    AUTOTEST_APPLICATION_TYPE="autotest"

    DEFAULT_OPERATIONS_TIMEOUT = "30"
    DEFAULT_GERRIT_AUTOUSER = "jenkins"
    DEFAULT_GERRIT_HOST = "gerrit"
    DEFAULT_GERRIT_CREDENTIALS = "jenkins"

    vars['workDir'] = "${WORKSPACE}/${RandomStringUtils.random(10, true, true)}"
    vars['operationsTimeout'] = env.OPERATIONS_TIMEOUT ? OPERATIONS_TIMEOUT : DEFAULT_OPERATIONS_TIMEOUT

    vars['configMapName'] = 'project-settings'
    vars['envSettingsKey'] = 'env.settings.json'
    vars['appSettingsKey'] = 'app.settings.json'
    vars['atSettingsKey'] = 'auto-test.settings.json'

    vars['gerritCredentials'] = env.GERRIT_CREDENTIALS ? GERRIT_CREDENTIALS : DEFAULT_GERRIT_CREDENTIALS
    vars['gerritAutoUser'] = env.GERRIT_AUTOUSER ? GERRIT_AUTOUSER : DEFAULT_GERRIT_AUTOUSER
    vars['gerritHost'] = env.GERRIT_HOST ? env.GERRIT_HOST : DEFAULT_GERRIT_HOST
    vars['gerritProject'] = env.GERRIT_PROJECT ? env.GERRIT_PROJECT : env.GERRIT_PROJECT_NAME
    vars['gerritSshPort'] = sh(
            script: "oc get svc gerrit -o jsonpath='{.spec.ports[?(@.name==\"ssh\")].targetPort}'",
            returnStdout: true
    ).trim()
    vars['gitApplicationUrl'] = "ssh://${vars.gerritAutoUser}@${vars.gerritHost}:${vars.gerritSshPort}/${vars.gerritProject}"

    vars['sonarRoute'] = sh(
            script: "oc get route sonar -o jsonpath='{.spec.host}'",
            returnStdout: true
    ).trim()

    vars["wildcard"] = sh(
            script: "oc get cm user-settings -o jsonpath='{.data.dns_wildcard}'",
            returnStdout: true
    ).trim()

    [vars.envSettingsKey, vars.appSettingsKey, vars.atSettingsKey].each() { key ->
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
    vars['nexusMavenRepositoryUrl'] = "http://nexus:8081/repository/maven"
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
    println("[JENKINS][DEGUG] Pipeline's variables:\n${debugOutput}")
}

return this;