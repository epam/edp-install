// By design the pipeline can only keep records of Serializable objects
// If you still need to keep an intermediate variable with a non serializable object, you need to hide it into a method and annotate this method with @NonCPS
// See https://cloudbees.zendesk.com/hc/en-us/articles/204972960-The-pipeline-even-if-successful-ends-with-java-io-NotSerializableException
import org.apache.commons.lang.RandomStringUtils
import groovy.json.*

//Define common variables
tmpDir = RandomStringUtils.random(10, true, true)
vars = [:]
vars['npmRegistry'] = "http://nexus:8081/repository/npm-all/"
commonLib = null
PIPELINES_PATH_DEFAULT = "openshift/devops/pipelines"

node("master") {
    vars['pipelinesPath'] = env.PIPELINES_PATH ? PIPELINES_PATH : PIPELINES_PATH_DEFAULT
    vars['devopsRoot'] = "${workspace}@script"

    dir("${vars.devopsRoot}/${vars.pipelinesPath}/stages/") {
        stage("CHECKOUT") {
            stage = load "gerrit-checkout.groovy"
            stage.run(vars)
        }

        stage("FIND LAST RC") {
            stage = load "deploy-environment.groovy"
            stage.run(vars)
        }

        stage("CREATE BRANCH") {
            stage = load "deploy-environment.groovy"
            stage.run(vars)
        }

        stage("PUSH TO GERRIT") {
            stage = load "deploy-environment.groovy"
            stage.run(vars)
        }

    }

}

