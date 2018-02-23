// By design the pipeline can only keep records of Serializable objects
// If you still need to keep an intermediate variable with a non serializable object, you need to hide it into a method and annotate this method with @NonCPS
// See https://cloudbees.zendesk.com/hc/en-us/articles/204972960-The-pipeline-even-if-successful-ends-with-java-io-NotSerializableException
import groovy.json.*

//Define common variables
vars = [:]
PIPELINES_PATH_DEFAULT = "openshift/devops/pipelines"
GERRIT_HOST = 'gerrit'
GERRIT_PORT = '30001'
GERRIT_PROJECT = 'edp'


node("master") {
    vars['pipelinesPath'] = env.PIPELINES_PATH ? PIPELINES_PATH : PIPELINES_PATH_DEFAULT
    vars['devopsRoot'] = "${WORKSPACE}@script"
    vars['autoUser'] = env.AUTOUSER ? AUTOUSER : "jenkins"
    vars['credentials'] = env.CREDENTIALS ? CREDENTIALS : "gerrit-key"
    vars['gitUrl'] = "ssh://${vars.autoUser}@${GERRIT_HOST}:${GERRIT_PORT}/${GERRIT_PROJECT}"
    vars['branch'] = 'master'
    vars['RC'] = ""
    vars['rcNumber']=0
    vars['prefix']='RC'
    vars['version']='0.1'
    vars['workDir'] = "${WORKSPACE}/${tmpDir}"

    dir("${vars.devopsRoot}/${vars.pipelinesPath}/stages/") {
        stage("CHECKOUT") {
            stage = load "git-checkout.groovy"
            stage.run(vars)
        }

        stage("FIND LAST RC") {
            stage = load "find-rc.groovy"
            stage.run(vars)
        }

        stage("CREATE BRANCH") {
            stage = load "create-branch.groovy"
            stage.run(vars)
        }

    }

}

