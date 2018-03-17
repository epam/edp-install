import org.apache.commons.lang.RandomStringUtils

def getBody(bodyType) {
    def greeting = """<html>
            <body>
                <H4>Dear Colleague(s),</H4>
                <div align="left">
                    This is notificaton from Jenkins <a href="${JENKINS_URL}/job/${JOB_NAME}">build job</a><br>
                    Reason:<br>
                </div>
            """
    def textMessage
    switch (bodyType) {
        case "approve":
            textMessage = """
            <div align="left">
                Job is waiting for your approve, please check<br>
            </div>
            """
        case "failed":
            textMessage = """
            <div align="left">
                Job has been failed please check<br>
            </div>
            """
    }
    def footer = """
            <hr>
            </body>
            <footer> This message has been generated automatically by <a href="${JENKINS_URL}">EDP Jenkins CI</a>. Please do not reply on this message.
            </html>
            """
    return(greeting + textMessage + footer)
}

def getConstants(vars) {
    DEFAULT_OPERATIONS_TIMEOUT = "30"
    DEFAULT_DOCKER_REGISTRY = "docker-registry-default.main.edp.projects.epam.com"
    DEFAULT_DOCKER_REGISTRY_PORT = "30212"
    DEFAULT_EMAIL_RECIPIENTS = "SpecialEPMD-EDPcoreteam@epam.com"
    DEFAULT_GERRIT_AUTOUSER = "jenkins"
    DEFAULT_GERRIT_CREDENTIALS = "gerrit-key"
    DEFAULT_GERRIT_HOST = "gerrit"
    DEFAULT_GERRIT_SSH_PORT = "30001"
    DEFAULT_SIT_PROJECT_NAME = "sit"
    DEFAULT_QA_PROJECT_NAME = "qa"
    DEFAULT_UAT_PROJECT_NAME = "uat"
    DEFAULT_IMAGE_PROJECT_NAME = "infra"
    DEFAULT_TEST_SUITES = "testng-smoke-suite.xml"

    vars['devopsRoot'] = new File("/tmp/${RandomStringUtils.random(10, true, true)}")
    vars['workDir'] = "${WORKSPACE}/${RandomStringUtils.random(10, true, true)}"
    vars['operationsTimeout'] = env.OPERATIONS_TIMEOUT ? OPERATIONS_TIMEOUT : DEFAULT_OPERATIONS_TIMEOUT

    vars['testSuites'] = env.TEST_SUITES ? TEST_SUITES : DEFAULT_TEST_SUITES

    vars['externalDockerRegistry'] = env.DOCKER_REGISTRY ? DOCKER_REGISTRY : DEFAULT_DOCKER_REGISTRY
    vars['externalDockerRegistryPort'] = env.DOCKER_REGISTRY_PORT ? DOCKER_REGISTRY_PORT : DEFAULT_DOCKER_REGISTRY_PORT
    vars['emailRecipients'] = env.EMAIL_RECIPIENTS ? EMAIL_RECIPIENTS : DEFAULT_EMAIL_RECIPIENTS
    vars['gerritAutoUser'] = env.GERRIT_AUTOUSER ? GERRIT_AUTOUSER : DEFAULT_GERRIT_AUTOUSER
    vars['credentials'] = env.GERRIT_CREDENTIALS ? GERRIT_CREDENTIALS : DEFAULT_GERRIT_CREDENTIALS

    vars['gerritHost'] = env.GERRIT_HOST ? env.GERRIT_HOST : DEFAULT_GERRIT_HOST
    vars['gerritSshPort'] = env.GERRIT_SSH_PORT ? env.GERRIT_SSH_PORT : DEFAULT_GERRIT_SSH_PORT
    vars['gerritProject'] = env.GERRIT_PROJECT ? env.GERRIT_PROJECT : env.GERRIT_PROJECT_NAME
    vars['gitUrl'] = "ssh://${vars.gerritAutoUser}@${vars.gerritHost}:${vars.gerritSshPort}/${vars.gerritProject}"
    vars['gitLabApiUrl'] = "https://git.epam.com/api/v4"

    vars['sitProject'] = env.SIT_PROJECT ? SIT_PROJECT : DEFAULT_SIT_PROJECT_NAME
    vars['qaProject'] = env.QA_PROJECT ? QA_PROJECT : DEFAULT_QA_PROJECT_NAME
    vars['uatProject'] = env.UAT_PROJECT ? UAT_PROJECT : DEFAULT_UAT_PROJECT_NAME
    vars['dockerImageProject'] = env.IMAGE_PROJECT ? IMAGE_PROJECT : DEFAULT_IMAGE_PROJECT_NAME

    vars['mavenSettings'] = "${vars.pipelinesPath}/settings/maven/settings.xml"
    vars['mavenEdpGroup'] = "com.epam.edp"
    vars['nexusRepository'] = "http://nexus:8081/repository/edp"
    vars['nexusGroupRepository'] = "http://nexus:8081/repository/maven-public"
}
void getDebugInfo(vars) {
    def debugOutput = ""
    vars.keySet().each{ key ->
        debugOutput = debugOutput + "${key}=${vars["${key}"]}\n"
    }
    println("[JENKINS][DEGUG] Pipeline's variables:\n${debugOutput}")
}

void sendEmail(recipients, subject, bodyType) {
    body = getBody(bodyType)
    emailext to: "${recipients}", subject: "${subject}", body: body, mimeType: "text/html"
}

void failJob(failMessage) {
    println(failMessage)
    currentBuild.displayName = "${currentBuild.displayName}-FAILED"
    currentBuild.result = 'FAILURE'
    error failMessage
}

return this;