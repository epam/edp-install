import groovy.json.*
import org.apache.commons.lang.RandomStringUtils

PIPELINES_PATH_DEFAULT = "openshift/devops/pipelines"
AUTOUSER_DEFAULT = "jenkins"
EMAIL_RECIPIENTS_DEFAULT = "Alexander_Morozov@epam.com"
CREDENTIALS_DEFAULT = "gerrit-key"

tmpDir = RandomStringUtils.random(10, true, true)
vars = [:]

vars['html_body'] = """<html>
        <body>
          <H3>Dear Colleague(s),</H3>
          <div align="left">
            Jenkins build job ${BUILD_URL} is waiting for your approve, please check.<br>
          </div>
          <hr>
        </body>
        <footer> This message has been generated automatically by <a href="${JENKINS_URL}">EDP Jenkins CI</a>. Please do not reply on this message.
        </html>
        """

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

    vars['email_recipients'] = env.EMAIL_RECIPIENTS ? EMAIL_RECIPIENTS : EMAIL_RECIPIENTS_DEFAULT
    vars['autoUser'] = env.AUTOUSER ? AUTOUSER : AUTOUSER_DEFAULT
    vars['credentials'] = env.CREDENTIALS ? CREDENTIALS : CREDENTIALS_DEFAULT
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
                emailext to: "${vars.email_recipients}", subject: "[EDP][JENKINS] Precommit pipeline is waiting for manual approve", body: vars.html_body, mimeType: "text/html"
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
    }
}
