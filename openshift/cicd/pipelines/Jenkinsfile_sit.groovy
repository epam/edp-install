// By design the pipeline can only keep records of Serializable objects
// If you still need to keep an intermediate variable with a non serializable object, you need to hide it into a method and annotate this method with @NonCPS
// See https://cloudbees.zendesk.com/hc/en-us/articles/204972960-The-pipeline-even-if-successful-ends-with-java-io-NotSerializableException
import org.apache.commons.lang.RandomStringUtils
import groovy.json.*

//Define common variables
tmpDir = RandomStringUtils.random(10, true, true)
vars = [:]
commonLib = null

node("master") {
    if (!env.PIPELINES_PATH)
        error("[JENKINS][ERROR] PIPELINES_PATH variable is not defined, please check.")

    vars['pipelinesPath'] = PIPELINES_PATH

    def workspace = "${WORKSPACE.replaceAll("@", "")}@script"
    dir("${workspace}") {
        stash name: 'data', includes: "${vars.pipelinesPath}/**", useDefaultExcludes: false
        commonLib = load "${vars.pipelinesPath}/libs/common.groovy"
    }
    if (!env.SERVICE_TYPE || !env.GERRIT_PROJECT_NAME)
        commonLib.failJob("[JENKINS][ERROR] SERVICE_TYPE or GERRIT_PROJECT_NAME parameter is not defined, please check.")

    vars['serviceType'] = SERVICE_TYPE
    println("[JENKINS][DEBUG] Service type to build - ${vars.serviceType}")
}

node("backend") {
    stage("INITIALIZATION") {
        vars['devopsRoot'] = new File("/tmp/${tmpDir}")
        vars['serviceDir'] = "${WORKSPACE}/services"
        vars['mavenSettings'] = "${vars.pipelinesPath}/settings/maven/settings.xml"

        vars['appPath'] = env.APP_PATH ? APP_PATH : 'openshift/cicd/examples/3-tier-app'
        vars['autoUser'] = env.AUTOUSER ? AUTOUSER : "Auto_EPMC-JAVA_VCS"
        vars['credentials'] = env.CREDENTIALS ? CREDENTIALS : "gerrit-key"
        vars['serviceBranch'] = env.GERRIT_REFNAME ? GERRIT_REFNAME : SERVICE_BRANCH
        vars['gerritHost'] = env.GERRIT_HOST ? GERRIT_HOST : "gerrit"
        vars['gerritProject'] = GERRIT_PROJECT_NAME
        vars['gitMicroservicesUrl'] = "ssh://${vars.autoUser}@${vars.gerritHost}:29418/${vars.gerritProject}"

        vars['sitProject'] = env.SIT_PROJECT ? SIT_PROJECT : "sit"
        vars['qaProject'] = env.QA_PROJECT ? QA_PROJECT : "qa"

        println("[JENKINS][DEBUG] VARIABLES - ${vars}")

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

    dir("${vars.devopsRoot}/${vars.pipelinesPath}/stages/gerrit-checkout-git/") { commonLib.runStage("CHECKOUT", vars) }
    vars['imageTag'] = 'latest'
    vars['projects'] = ["${vars.sitProject}"]
    dir("${vars.devopsRoot}/${vars.pipelinesPath}/stages/deploy/") { commonLib.runStage("DEPLOY LATEST TAG", vars) }

    try {
        dir("${vars.devopsRoot}/${vars.pipelinesPath}/stages/integration-tests/") { commonLib.runStage("INTEGRATION-TESTS", vars) }
        currentBuild.displayName = "${currentBuild.displayName}-PASSED"
        vars['sourceProject'] = vars.sitProject
        vars['targetProject'] = vars.qaProject
        dir("${vars.devopsRoot}/${vars.pipelinesPath}/stages/tag-image/") { commonLib.runStage(null, vars) }

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
        vars['projects'] = ["${vars.sitProject}"]
        dir("${vars.devopsRoot}/${vars.pipelinesPath}/stages/deploy/") { commonLib.runStage("DEPLOY STABLE TAG", vars) }
    }
}
