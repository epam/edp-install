//Function that used to run different stages during execution
void runStage(name, vars) {
    def source = null
    def fileList = []
    def stageIsSkipped = true
    try {
        exists = fileExists "${vars.serviceType}.groovy"
        if (exists) {
            source = load "${vars.serviceType}.groovy"
            println("[JENKINS] Stage is found, we will use ${vars.serviceType}.groovy file")
            stageIsSkipped = false
        } else {
            fileList = sh(
                    script: "ls",
                    returnStdout: true
            ).trim().tokenize()
            println("[JENKINS] FILElIST - ${fileList}")
            fileList.each { file ->
                def typesList = []
                typesList = file.replaceAll('.groovy', '').tokenize('_')
                if (vars.serviceType in typesList) {
                    println("[JENKINS] STAGE IS FOUND, WE WILL USE ${file} FILE")
                    source = load "${file}"
                    stageIsSkipped = false
                    return true
                }
            }
        }

        if (stageIsSkipped) {
            echo "[JENKINS] STAGE ${name} WAS SKIPPED"
            return
        }

        if (!name)
            source.run(vars)
        else {
            stage(name) {
                source.run(vars)
            }
        }
    }
    catch (Exception ex) {
        echo "${ex.getMessage()}"
        failJob("[JENKINS][ERROR] ${name}_STAGE: For the service ${vars.serviceType} has been failed")
    }
}

def getConstants(vars) {
    DEFAULT_OPERATIONS_TIMEOUT = "30"
    DEFAULT_GERRIT_AUTOUSER = "jenkins"
    DEFAULT_GERRIT_HOST = "gerrit"

    vars['devopsRoot'] = new File("/tmp/${RandomStringUtils.random(10, true, true)}")
    vars['workDir'] = "${WORKSPACE}/${RandomStringUtils.random(10, true, true)}"
    vars['operationsTimeout'] = env.OPERATIONS_TIMEOUT ? OPERATIONS_TIMEOUT : DEFAULT_OPERATIONS_TIMEOUT

    vars['gerritAutoUser'] = env.GERRIT_AUTOUSER ? GERRIT_AUTOUSER : DEFAULT_GERRIT_AUTOUSER
    vars['gerritHost'] = env.GERRIT_HOST ? env.GERRIT_HOST : DEFAULT_GERRIT_HOST

    vars['mavenSettings'] = "${vars.pipelinesPath}/settings/maven/settings.xml"
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