import groovy.json.*

vars = [:]
commonLib = null

node("master") {
    def workspace = "/tmp/workspace/${JOB_NAME}"
    vars['devopsRoot'] = "${workspace}@script"
    commonLib = load "${vars.devopsRoot}/infrastructure/pipelines/libs/common.groovy"

    if (!env.SERVICE_TYPE)
        commonLib.failJob("[JENKINS][ERROR] vars.serviceType wasn't provided, please check")

    vars['serviceType'] = SERVICE_TYPE

    currentBuild.displayName = "${currentBuild.displayName}-${vars.serviceType}"
    vars['imageTag'] = 'latest'
    vars['projects'] = ['qa']
    dir("${vars.devopsRoot}/infrastructure/pipelines/stages/deploy/") { commonLib.runStage("DEPLOY LATEST TAG", vars) }


    try {
        stage("QA APPROVEMENT") {
            input "Ready to update stable version of ${vars.serviceType} service on QA env?"
            vars['sourceProject'] = "qa"
            vars['targetProject'] = "uat"
            dir("${vars.devopsRoot}/infrastructure/pipelines/stages/tag-image/") { commonLib.runStage(null, vars) }
            currentBuild.displayName = "${currentBuild.displayName}-APPROVED"

            vars['imageTag'] = 'stable'
            vars['projects'] = ['uat']
            dir("${vars.devopsRoot}/infrastructure/pipelines/stages/deploy/") { commonLib.runStage(null, vars)  }
        }


    }
    catch (Exception ex) {
        println("[JENKINS][DEBUG] Promote image for ${vars.serviceType} from latest to stable hasn't approved by QA")
        currentBuild.displayName = "${currentBuild.displayName}-NOT-APPROVED"
    }
    finally {
        vars['imageTag'] = 'stable'
        vars['projects'] = ['qa']
        dir("${vars.devopsRoot}/infrastructure/pipelines/stages/deploy/") {commonLib.runStage("DEPLOY STABLE TAG", vars)}
    }
}
