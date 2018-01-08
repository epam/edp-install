// By design the pipeline can only keep records of Serializable objects
// If you still need to keep an intermediate variable with a non serializable object, you need to hide it into a method and annotate this method with @NonCPS
// See https://cloudbees.zendesk.com/hc/en-us/articles/204972960-The-pipeline-even-if-successful-ends-with-java-io-NotSerializableException
import org.apache.commons.lang.RandomStringUtils
import groovy.json.*

//Define common variables
tmpDir = RandomStringUtils.random(10, true, true)
vars = [:]
vars['npmRegistry']="http://nexus:8081/repository/npm-all/"
commonLib = null

//GLOBAL CONSTANTS
DEFAULT_AUTOUSER = "Auto_EPMC-JAVA_VCS"

def getServiceType() {
    getChangeApiUrl = "http://${GERRIT_HOST}:8080/a/changes/?q=${GERRIT_CHANGE_ID}&o=CURRENT_REVISION&o=CURRENT_FILES"
    def authString
    withCredentials([usernamePassword(credentialsId: 'auto_epmc-java_vcs_gerrit-http', passwordVariable: 'PASSWORD', usernameVariable: 'USERNAME')]) {
        authString = "${USERNAME}:${PASSWORD}".getBytes().encodeBase64().toString()
    }
    def connection = getChangeApiUrl.toURL().openConnection()
    connection.setRequestProperty("Authorization", "Basic ${authString}")
    def parsedJson = new JsonSlurperClassic().parseText(connection.content.text.replaceAll('\\)]}\'', '').trim())

    def paths = parsedJson[0].revisions[GERRIT_PATCHSET_REVISION].files.keySet()
    echo "[JENKINS][DEBUG] Changed paths - ${paths}"

    def match = paths.join("\n") =~ /^(backend|frontend|database)\/.*/
    return match[0][1]
}

node("master") {
    def workspace = "/tmp/workspace/${JOB_NAME}"
    dir("${workspace}@script") {
        stash name: 'data', includes: "infrastructure/**", useDefaultExcludes: false
        commonLib = load "infrastructure/pipelines/libs/common.groovy"
    }
    vars['serviceType'] = getServiceType()
    println("[JENKINS][DEBUG] Service type to build - ${vars.serviceType}")
}

buildNode = "backend"
if (vars.serviceType == "frontend")
    buildNode = "frontend"

node("${buildNode}") {
    stage("INITIALIZATION") {
        //DEFINE VARIABLES FROM JENKINS JOB PARAMETERS
        vars['autoUser'] = env.AUTOUSER != null ? AUTOUSER : DEFAULT_AUTOUSER
        vars['devopsRoot'] = new File("/tmp/${tmpDir}")
        vars['serviceBranch'] = GERRIT_BRANCH
        vars['gerritChange'] = "change-${GERRIT_CHANGE_NUMBER}-${GERRIT_PATCHSET_NUMBER}"
        vars['serviceDir'] = "${WORKSPACE}/services"
        vars['gitMicroservicesUrl'] = "ssh://${vars.autoUser}@${GERRIT_HOST}:${GERRIT_PORT}/${GERRIT_PROJECT}"

        //Download DevOps scripts
        try {
            dir("${vars.devopsRoot}") {
                unstash 'data'
            }
        } catch (Exception ex) {
            commonLib.failJob("[JENKINS][ERROR] Devops repository unstash has failed. Reason - ${ex}")
        }

        currentBuild.displayName = "${currentBuild.number}-${vars.serviceBranch}(${vars.gerritChange})"

        currentBuild.description = """Service: ${vars.serviceType}
Branch: ${vars.serviceBranch}
"""
    }

    dir("${vars.devopsRoot}/infrastructure/pipelines/stages/gerrit-checkout/") { commonLib.runStage("CHECKOUT", vars) }
    dir("${vars.devopsRoot}/infrastructure/pipelines/stages/compile/") { commonLib.runStage("COMPILE", vars) }
    dir("${vars.devopsRoot}/infrastructure/pipelines/stages/unit-test/") { commonLib.runStage("UNIT-TESTS", vars) }
    dir("${vars.devopsRoot}/infrastructure/pipelines/stages/sonar-preview/") { commonLib.runStage("SONAR-PREVIEW", vars) }
    dir("${vars.devopsRoot}/infrastructure/pipelines/stages/post-to-gerrit/") { commonLib.runStage("POST-TO-GERRIT", vars) }
    dir("${vars.devopsRoot}/infrastructure/pipelines/stages/sonar/") { commonLib.runStage("SONAR", vars) }
    dir("${vars.devopsRoot}/infrastructure/pipelines/stages/sonar-check/") { commonLib.runStage("SONAR-CHECK", vars) }
}