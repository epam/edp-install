import groovy.json.*
import org.apache.commons.lang.RandomStringUtils

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
    if (!env.SERVICE_TYPE)
        commonLib.failJob("[JENKINS][ERROR] SERVICE_TYPE variable is not defined, please check.")

    vars['serviceType'] = env.SERVICE_TYPE
    println("[JENKINS][DEBUG] Service type to build - ${vars.serviceType}")
}

node("backend") {
    vars['devopsRoot'] = new File("/tmp/${tmpDir}")

    vars['qaProject'] = env.QA_PROJECT ? QA_PROJECT : "qa"
    vars['uatProject'] = env.UAT_PROJECT ? UAT_PROJECT : "uat"

    try {
        dir("${vars.devopsRoot}") {
            unstash 'data'
        }
    } catch (Exception ex) {
        commonLib.failJob("[JENKINS][ERROR] Devops repository unstash has failed. Reason - ${ex}")
    }

    currentBuild.displayName = "${currentBuild.displayName}-${vars.serviceType}"
    vars['imageTag'] = 'latest'
    vars['projects'] = ["${vars.qaProject}"]
    dir("${vars.devopsRoot}/${vars.pipelinesPath}/stages/deploy/") { commonLib.runStage("DEPLOY LATEST TAG", vars) }


    try {
        stage("QA APPROVEMENT") {
            input "Ready to update stable version of ${vars.serviceType} service on QA env?"
            vars['sourceProject'] = vars.qaProject
            vars['targetProject'] = vars.uatProject
            dir("${vars.devopsRoot}/${vars.pipelinesPath}/stages/tag-image/") { commonLib.runStage(null, vars) }
            currentBuild.displayName = "${currentBuild.displayName}-APPROVED"

            vars['imageTag'] = 'stable'
            vars['projects'] = ["${vars.uatProject}"]
            dir("${vars.devopsRoot}/${vars.pipelinesPath}/stages/deploy/") { commonLib.runStage(null, vars)  }
        }


    }
    catch (Exception ex) {
        println("[JENKINS][DEBUG] Promote image for ${vars.serviceType} from latest to stable hasn't approved by QA")
        currentBuild.displayName = "${currentBuild.displayName}-NOT-APPROVED"
    }
    finally {
        vars['imageTag'] = 'stable'
        vars['projects'] = ["${vars.uatProject}"]
        dir("${vars.devopsRoot}/${vars.pipelinesPath}/stages/deploy/") {commonLib.runStage("DEPLOY STABLE TAG", vars)}
    }
}
