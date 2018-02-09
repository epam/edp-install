import groovy.json.*
import org.apache.commons.lang.RandomStringUtils

PIPELINES_PATH_DEFAULT = "openshift/devops/pipelines"
GERRIT_HOST = 'gerrit'
GERRIT_PORT = '30001'
GERRIT_PROJECT = 'edp'

tmpDir = RandomStringUtils.random(10, true, true)
vars = [:]

node("master") {
    vars['pipelinesPath'] = env.PIPELINES_PATH ? PIPELINES_PATH : PIPELINES_PATH_DEFAULT

    def workspace = "${WORKSPACE.replaceAll("@", "")}@script"
    dir("${workspace}") {
        stash name: 'data', includes: "**", useDefaultExcludes: false
    }
}

node("ansible-slave") {
    vars['devopsRoot'] = new File("/tmp/${tmpDir}")

    try {
        dir("${vars.devopsRoot}") {
            unstash 'data'
        }
    } catch (Exception ex) {
        error("[JENKINS][ERROR] Devops repository unstash has failed. Reason - ${ex}")
    }

    vars['autoUser'] = env.AUTOUSER ? AUTOUSER : "jenkins"
    vars['workDir'] = "${WORKSPACE}/repository"
    vars['ocProjectName'] = "release-${RELEASE_NUMBER}-test"

    vars['credentials'] = env.CREDENTIALS ? CREDENTIALS : "gerrit-key"
    vars['gitUrl'] = "ssh://${vars.autoUser}@${GERRIT_HOST}:${GERRIT_PORT}/${GERRIT_PROJECT}"
    vars['branch'] = '0.1.${RELEASE_NUMBER}-RC'
    vars['prefix']='RELEASE'
    vars['RCnum']=RELEASE_NUMBER

    currentBuild.displayName = "${currentBuild.displayName}-${vars.branch}(${vars.gerritChange})"
    currentBuild.description = """Branch: ${vars.branch}
Patchset: ${vars.gerritChange}
"""

    println("pn - ${vars.ocProjectName}")
    println("branch - ${vars.branch}")
    dir("${vars.devopsRoot}/${vars.pipelinesPath}/stages/") {
        stage("CHECKOUT") {
            stage = load "git-checkout.groovy"
            stage.run(vars)
        }

        stage("DEPLOY PROJECT") {
            stage = load "deploy-environment.groovy"
            stage.run(vars)
        }

        try {
            stage("MANUAL APPROVE") {
                input "Is everything ok with environment ${vars.ocProjectName}?"
            }
            currentBuild.displayName = "${currentBuild.displayName}-APPROVED"
        }
        catch (Exception ex) {
            currentBuild.displayName = "${currentBuild.displayName}-FAILED"
            currentBuild.result = 'FAILURE'
        }
        finally {
            stage("DELETE PROJECT") {
                stage = load "delete-environment.groovy"
                stage.run(vars)
            }
        }
        stage("CREATE BRANCH") {
            stage = load "create-branch1.groovy"
            stage.run(vars)
        }
    }
}

/*node("master") {
    vars['devopsRoot'] = "${workspace}@script"
    vars['devopsRoot'] = "${WORKSPACE.replaceAll("@", "")}@script"

    dir("${vars.devopsRoot}/${vars.pipelinesPath}/stages/") {
        stage("CREATE BRANCH") {
            stage = load "create-branch.groovy"
            stage.run(vars)
        }
    }
}*/

