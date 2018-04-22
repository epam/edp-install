import groovy.json.*
import org.apache.commons.lang.RandomStringUtils

void runStage(name, vars) {
    def source = null
    def fileList = []
    def typesList = []
    def stageIsSkipped = true
    def applicationTool = vars.applicationMap.build_tool.toLowerCase()
    try {
        exists = fileExists "${applicationTool}.groovy"
        if (exists) {
            source = load "${applicationTool}.groovy"
            println("[JENKINS] Stage is found, we will use ${applicationTool}.groovy file")
            stageIsSkipped = false
        } else {
            fileList = sh(
                    script: "ls",
                    returnStdout: true
            ).trim().tokenize()
            println("[JENKINS][DEBUG] Filelist - ${fileList}")
            fileList.each { file ->
                typesList = file.replaceAll('.groovy', '').tokenize('_')
                if (applicationTool in typesList) {
                    println("[JENKINS][DEBUG] Stage is found, we will use ${file} file")
                    source = load "${file}"
                    stageIsSkipped = false
                    return true
                }
            }
        }

        if (stageIsSkipped) {
            echo "[JENKINS][DEBUG] STAGE ${name} WAS SKIPPED"
            return
        }

        if (!name)
            source.run(vars)
        else
            stage(name) { source.run(vars) }
    }
    catch (Exception ex) {
        echo "${ex.getMessage()}"
        failJob("[JENKINS][ERROR] ${name}_STAGE: For the service ${vars.applicationMap.name} has been failed")
    }
}

def getConstants(vars) {
    DEFAULT_OPERATIONS_TIMEOUT = "30"
    DEFAULT_GERRIT_AUTOUSER = "jenkins"
    DEFAULT_GERRIT_HOST = "gerrit"
    DEFAULT_GERRIT_CREDENTIALS = "jenkins"

    vars['workDir'] = "${WORKSPACE}/${RandomStringUtils.random(10, true, true)}"
    vars['operationsTimeout'] = env.OPERATIONS_TIMEOUT ? OPERATIONS_TIMEOUT : DEFAULT_OPERATIONS_TIMEOUT

    vars['configMapName'] = 'project-settings'
    vars['envSettingsKey'] = 'env.settings.json'
    vars['appSettingsKey'] = 'app.settings.json'

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

    [vars.envSettingsKey, vars.appSettingsKey].each() { key ->
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

def getApplicationMap(name) {
    for (applicationItem in vars["${vars.appSettingsKey}"]) {
        if (applicationItem.name == name)
            return(applicationItem)
    }
    return(null)
}

def getEnvironmentMap(name) {
    for (environmentItem in vars["${vars.envSettingsKey}"]) {
        if (environmentItem.name == name)
            return(environmentItem)
    }
    return(null)
}

void failJob(failMessage) {
    println(failMessage)
    currentBuild.displayName = "${currentBuild.displayName}-FAILED"
    currentBuild.result = 'FAILURE'
    error failMessage
}

void getDebugInfo(vars) {
    def debugOutput = ""
    vars.keySet().each{ key ->
        debugOutput = debugOutput + "${key}=${vars["${key}"]}\n"
    }
    println("[JENKINS][DEGUG] Pipeline's variables:\n${debugOutput}")
}

return this;