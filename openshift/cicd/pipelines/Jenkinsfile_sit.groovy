// By design the pipeline can only keep records of Serializable objects
// If you still need to keep an intermediate variable with a non serializable object, you need to hide it into a method and annotate this method with @NonCPS
// See https://cloudbees.zendesk.com/hc/en-us/articles/204972960-The-pipeline-even-if-successful-ends-with-java-io-NotSerializableException
import org.apache.commons.lang.RandomStringUtils
import groovy.json.*
import groovy.xml.StreamingMarkupBuilder
import groovy.util.XmlNodePrinter
import org.codehaus.groovy.tools.xml.DomToGroovy
import groovy.xml.XmlUtil

//Define common variables
tmpDir = RandomStringUtils.random(10, true, true)
vars = [:]
commonLib = null

//GLOBAL CONSTANTS
DEFAULT_AUTOUSER = "Auto_EPMC-JAVA_VCS"
DEFAULT_TEST_MODE = false
DEFAULT_DEVOPS_REPOSITORY = "git@git.epam.com:epmc-java/javacc-microservices-taxi.git"
DEFAULT_BUILD_TOOL = "ansible"
DEFAULT_GERRIT_REFNAME = "vv/test"
DEFAULT_GERRIT_HOST = "gerrit"
DEFAULT__GERRIT_PORT = ""
DEFAULT_GERRIT_PROJECT= "oc-petclinic"

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

node("backend") {
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

        currentBuild.displayName = "${currentBuild.displayName}-${vars.serviceType}"

        currentBuild.description = """Service: ${vars.serviceType}
Branch: ${vars.serviceBranch}
"""
    }

    dir("${vars.devopsRoot}/infrastructure/pipelines/stages/gerrit-checkout-git/") { commonLib.runStage("CHECKOUT", vars) }
    vars['imageTag'] = 'latest'
    vars['projects'] = ['sit']
    dir("${vars.devopsRoot}/infrastructure/pipelines/stages/deploy/") { commonLib.runStage("DEPLOY LATEST TAG", vars) }

    try {
        dir("${vars.devopsRoot}/infrastructure/pipelines/stages/integration-tests/") { commonLib.runStage("INTEGRATION-TESTS", vars) }
        currentBuild.displayName = "${currentBuild.displayName}-PASSED"
        vars['sourceProject'] = "sit"
        vars['targetProject'] = "qa"
        dir("${vars.devopsRoot}/infrastructure/pipelines/stages/tag-image/") { commonLib.runStage(null, vars) }

        build job: 'QA-Deploy-Pipeline', wait: false, parameters: [
                string(name: "SERVICE_TYPE", value: "${vars.serviceType}")
        ]
    }
    catch (Exception ex) {
        println("[JENKINS][DEBUG] Integration test for ${vars.serviceType} has been failed")
        currentBuild.displayName = "${currentBuild.displayName}-NOT-PASSED"
    }
    finally {
        vars['imageTag'] = 'stable'
        vars['projects'] = ['sit']
        dir("${vars.devopsRoot}/infrastructure/pipelines/stages/deploy/") { commonLib.runStage("DEPLOY STABLE TAG", vars) }
    }
}
