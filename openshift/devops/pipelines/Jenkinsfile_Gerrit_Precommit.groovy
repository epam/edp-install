import groovy.json.*
import org.apache.commons.lang.RandomStringUtils

PIPELINES_PATH_DEFAULT = "openshift/devops/pipelines"

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
    vars['credentials'] = env.CREDENTIALS ? CREDENTIALS : "gerrit-key"
    vars['branch'] = GERRIT_BRANCH
    vars['gerritChange'] = "change-${GERRIT_CHANGE_NUMBER}-${GERRIT_PATCHSET_NUMBER}"
    vars['workDir'] = "${WORKSPACE}/repository"
    vars['gitUrl'] = "ssh://${vars.autoUser}@${GERRIT_HOST}:${GERRIT_PORT}/${GERRIT_PROJECT}"
    vars['ocProjectName'] = "cicd-post-review-${vars.gerritChange}"

    currentBuild.displayName = "${currentBuild.displayName}-${vars.branch}(${vars.gerritChange})"
    currentBuild.description = """Branch: ${vars.branch}
Patchset: ${vars.gerritChange}
"""

    dir("${vars.devopsRoot}/${vars.pipelinesPath}/stages/") {
        stage("CHECKOUT") {
            stage = load "gerrit-checkout.groovy"
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
        }
        finally {
            stage("DELETE PROJECT") {
                stage = load "delete-environment.groovy"
                stage.run(vars)
            }
        }
    }
}
