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

node("master") {
    if (!env.PIPELINES_PATH)
        error("[JENKINS][ERROR] PIPELINES_PATH variable is not defined, please check.")

    vars['pipelinesPath'] = PIPELINES_PATH

    def workspace = "${WORKSPACE.replaceAll("@", "")}@script"
    dir("${workspace}") {
        stash name: 'data', includes: "${vars.pipelinesPath}/**", useDefaultExcludes: false
        commonLib = load "${vars.pipelinesPath}/libs/common.groovy"
    }
    if (!env.SERVICE_TYPE)
        commonLib.failJob("[JENKINS][ERROR] SERVICE_TYPE variable is not defined, please check.")

    vars['serviceType'] = env.SERVICE_TYPE
    println("[JENKINS][DEBUG] Service type to build - ${vars.serviceType}")
}

buildNode = "backend"
if (vars.serviceType == "frontend")
    buildNode = "frontend"

node("${buildNode}") {
    stage("INITIALIZATION") {
        vars['serviceDir'] = "${WORKSPACE}/services"
        vars['devopsRoot'] = new File("/tmp/${tmpDir}")
        vars['mavenSettings'] = "${vars.pipelinesPath}/settings/maven/settings.xml"

        vars['appPath'] = env.APP_PATH ? APP_PATH : 'openshift/cicd/examples/3-tier-app'
        vars['autoUser'] = env.AUTOUSER ? AUTOUSER : "Auto_EPMC-JAVA_VCS"
        vars['credentials'] = env.CREDENTIALS ? CREDENTIALS : "gerrit-key"
        vars['serviceBranch'] = env.GERRIT_REFNAME ? env.GERRIT_REFNAME : env.SERVICE_BRANCH
        vars['gerritHost'] = env.GERRIT_HOST ? env.GERRIT_HOST : "gerrit"
        vars['gerritSshPort'] = env.GERRIT_SSH_PORT ? env.GERRIT_SSH_PORT : "29418"
        vars['gerritProject'] = env.GERRIT_PROJECT ? env.GERRIT_PROJECT : env.GERRIT_PROJECT_NAME
        vars['sitProject'] = env.SIT_PROJECT ? SIT_PROJECT : "sit"

        vars['gitMicroservicesUrl'] = env.GILAB_REPO ? GILAB_REPO : "ssh://${vars.autoUser}@${vars.gerritHost}:${vars.gerritSshPort}/${vars.gerritProject}"

        println("[JENKINS][DEBUG] VARIABLES - ${vars}")

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

    dir("${vars.devopsRoot}/${vars.pipelinesPath}/stages/gerrit-checkout-git/") { commonLib.runStage("CHECKOUT", vars) }
    dir("${vars.devopsRoot}/${vars.pipelinesPath}/stages/compile/") { commonLib.runStage("COMPILE", vars) }
    dir("${vars.devopsRoot}/${vars.pipelinesPath}/stages/unit-test/") { commonLib.runStage("UNIT-TESTS", vars) }
    dir("${vars.devopsRoot}/${vars.pipelinesPath}/stages/sonar/") { commonLib.runStage("SONAR", vars) }
    dir("${vars.devopsRoot}/${vars.pipelinesPath}/stages/sonar-check/") { commonLib.runStage("SONAR-CHECK", vars) }
    dir("${vars.devopsRoot}/${vars.pipelinesPath}/stages/build/") { commonLib.runStage("BUILD", vars) }
    dir("${vars.devopsRoot}/${vars.pipelinesPath}/stages/push/") { commonLib.runStage("PUSH-TO-NEXUS", vars) }
    dir("${vars.devopsRoot}/${vars.pipelinesPath}/stages/run-build-config/") { commonLib.runStage("RUN-BUILD-CONFIG", vars) }

    vars['sourceProject'] = "ci-cd"
    vars['targetProject'] = vars.sitProject
    dir("${vars.devopsRoot}/${vars.pipelinesPath}/stages/tag-image/") { commonLib.runStage("TAG-IMAGE", vars) }

    build job: 'SIT-Deploy-Pipeline', wait: false, parameters: [
            string(name: "SERVICE_TYPE", value: "${vars.serviceType}")
    ]
}