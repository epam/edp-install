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
            Jenkins <a href="${JENKINS_URL}/job/${JOB_NAME}">build job</a> is waiting for your approve, please check.<br>
          </div>
          <hr>
        </body>
        <footer> This message has been generated automatically by <a href="${JENKINS_URL}">EDP Jenkins CI</a>. Please do not reply on this message.
        </html>
        """

node("ansible-slave") {
    vars['pipelinesPath'] = env.PIPELINES_PATH ? PIPELINES_PATH : PIPELINES_PATH_DEFAULT
    vars['devopsRoot'] = new File("/tmp/${tmpDir}")

    vars['email_recipients'] = env.EMAIL_RECIPIENTS ? EMAIL_RECIPIENTS : EMAIL_RECIPIENTS_DEFAULT
    vars['autoUser'] = env.AUTOUSER ? AUTOUSER : AUTOUSER_DEFAULT
    vars['credentials'] = env.CREDENTIALS ? CREDENTIALS : CREDENTIALS_DEFAULT
    vars['branch'] = GERRIT_BRANCH
    vars['gerritChange'] = "change-${GERRIT_CHANGE_NUMBER}-${GERRIT_PATCHSET_NUMBER}"
    vars['workDir'] = "${vars.devopsRoot}"
    vars['gitUrl'] = "ssh://${vars.autoUser}@${GERRIT_HOST}:${GERRIT_PORT}/${GERRIT_PROJECT}"
    vars['ocProjectName'] = "cicd-post-review-${vars.gerritChange}"

    currentBuild.displayName = "${currentBuild.displayName}-${vars.branch}(${vars.gerritChange})"
    currentBuild.description = """Branch: ${vars.branch}
Patchset: ${vars.gerritChange}
"""

    stage("CHECKOUT") {
        try {
            dir("${vars.devopsRoot}") {
                checkout([$class                           : 'GitSCM', branches: [[name: "${vars.gerritChange}"]],
                          doGenerateSubmoduleConfigurations: false, extensions: [],
                          submoduleCfg                     : [],
                          userRemoteConfigs                : [[refspec      : "${GERRIT_REFSPEC}:${vars.gerritChange}",
                                                               credentialsId: "${vars.credentials}",
                                                               url          : "${vars.gitUrl}"]]])
            }
        } catch (Exception ex) {
            error("[JENKINS][ERROR] Devops repository checkout has been failed. Reason - ${ex}")
        }
    }

    dir("${vars.devopsRoot}/${vars.pipelinesPath}/stages/") {

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
