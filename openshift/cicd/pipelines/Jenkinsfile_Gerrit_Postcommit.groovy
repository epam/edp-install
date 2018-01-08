// By design the pipeline can only keep records of Serializable objects
// If you still need to keep an intermediate variable with a non serializable object, you need to hide it into a method and annotate this method with @NonCPS
// See https://cloudbees.zendesk.com/hc/en-us/articles/204972960-The-pipeline-even-if-successful-ends-with-java-io-NotSerializableException
import org.apache.commons.lang.RandomStringUtils
import groovy.json.*

//Define common variables
tmpDir = RandomStringUtils.random(10, true, true)
vars = [:]
vars['npmRegistry']="http://nexus:8081/repository/npm-all/"
vars['npmSnapshotRegistry']="http://nexus:8081/repository/npm-private-snapshots/"
commonLib = null

//GLOBAL CONSTANTS
DEFAULT_AUTOUSER = "Auto_EPMC-JAVA_VCS"

node("master") {
    def workspace = "/tmp/workspace/${JOB_NAME}"
    dir("${workspace}@script") {
        stash name: 'data', includes: "infrastructure/**", useDefaultExcludes: false
        commonLib = load "infrastructure/pipelines/libs/common.groovy"
    }
    if (!env.SERVICE_TYPE)
        failJob("[JENKINS][ERROR] SERVICE_TYPE variable is not defined, please check.")

    vars['serviceType'] = env.SERVICE_TYPE
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
        vars['serviceBranch'] = env.GERRIT_REFNAME ? env.GERRIT_REFNAME : env.SERVICE_BRANCH
        vars['serviceDir'] = "${WORKSPACE}/services"
        vars['gerritHost'] = env.GERRIT_HOST ? env.GERRIT_HOST : "gerrit"
        vars['gerritProject'] = env.GERRIT_PROJECT ? env.GERRIT_PROJECT : env.GERRIT_PROJECT_NAME
        vars['gitMicroservicesUrl'] = "ssh://${vars.autoUser}@${vars.gerritHost}:29418/${vars.gerritProject}"

        println("[JENKINS][DEBUG] VARIABLES - ${vars}")

        //Download DevOps scripts
        try {
            dir("${vars.devopsRoot}") {
                unstash 'data'
            }
        } catch (Exception ex) {
            commonLib.failJob("[JENKINS][ERROR] Devops repository unstash has failed. Reason - ${ex}")
        }

        currentBuild.displayName = "${currentBuild.number}-${vars.serviceBranch}"

        currentBuild.description = """Service: ${vars.serviceType}
Branch: ${vars.serviceBranch}
"""
    }

    dir("${vars.devopsRoot}/infrastructure/pipelines/stages/gerrit-checkout-git/") { commonLib.runStage("CHECKOUT", vars) }
    dir("${vars.devopsRoot}/infrastructure/pipelines/stages/compile/") { commonLib.runStage("COMPILE", vars) }
    dir("${vars.devopsRoot}/infrastructure/pipelines/stages/unit-test/") { commonLib.runStage("UNIT-TESTS", vars) }
    dir("${vars.devopsRoot}/infrastructure/pipelines/stages/sonar/") { commonLib.runStage("SONAR", vars) }
    dir("${vars.devopsRoot}/infrastructure/pipelines/stages/sonar-check/") { commonLib.runStage("SONAR-CHECK", vars) }
    dir("${vars.devopsRoot}/infrastructure/pipelines/stages/build/") { commonLib.runStage("BUILD", vars) }
    dir("${vars.devopsRoot}/infrastructure/pipelines/stages/push/") { commonLib.runStage("PUSH-TO-NEXUS", vars) }
    dir("${vars.devopsRoot}/infrastructure/pipelines/stages/run-build-config/") { commonLib.runStage("RUN-BUILD-CONFIG", vars) }

    vars['sourceProject'] = "ci-cd"
    vars['targetProject'] = "sit"
    dir("${vars.devopsRoot}/infrastructure/pipelines/stages/tag-image/") { commonLib.runStage("TAG-IMAGE", vars) }

    build job: 'SIT-Deploy-Pipeline', wait: false, parameters: [
            string(name: "SERVICE_TYPE", value: "${vars.serviceType}")
    ]
}