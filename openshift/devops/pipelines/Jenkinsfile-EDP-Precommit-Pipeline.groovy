import groovy.json.*
import org.apache.commons.lang.RandomStringUtils

PIPELINES_PATH_DEFAULT = "openshift/devops/pipelines"
DOCKER_REGISTRY_DEFAULT = "docker-registry-default.main.edp.projects.epam.com"
AUTOUSER_DEFAULT = "jenkins"
EMAIL_RECIPIENTS_DEFAULT = "SpecialEPMD-EDPcoreteam@epam.com"
CREDENTIALS_DEFAULT = "gerrit-key"

tmpDir = RandomStringUtils.random(10, true, true)
vars = [:]
commonLib = null

node("master") {
    vars['pipelinesPath'] = env.PIPELINES_PATH ? PIPELINES_PATH : PIPELINES_PATH_DEFAULT

    def workspace = "${WORKSPACE.replaceAll("@.*", "")}@script"
    dir("${workspace}") {
        stash name: 'data', includes: "**", useDefaultExcludes: false
        commonLib = load "${vars.pipelinesPath}/libs/common.groovy"
    }
}

node("ansible-slave") {
    vars['devopsRoot'] = new File("/tmp/${tmpDir}")

    try {
        dir("${vars.devopsRoot}") {
            unstash 'data'
        }
    } catch (Exception ex) {
        commonLib.failJob("[JENKINS][ERROR] Devops repository unstash has failed. Reason - ${ex}")
    }

    vars['externalDockerRegistry'] = env.DOCKER_REGISTRY ? DOCKER_REGISTRY : DOCKER_REGISTRY_DEFAULT
    vars['emailRecipients'] = env.EMAIL_RECIPIENTS ? EMAIL_RECIPIENTS : EMAIL_RECIPIENTS_DEFAULT
    vars['autoUser'] = env.AUTOUSER ? AUTOUSER : AUTOUSER_DEFAULT
    vars['credentials'] = env.CREDENTIALS ? CREDENTIALS : CREDENTIALS_DEFAULT
    vars['branch'] = GERRIT_BRANCH
    vars['gerritChange'] = "change-${GERRIT_CHANGE_NUMBER}-${GERRIT_PATCHSET_NUMBER}"
    vars['workDir'] = "${WORKSPACE}/${tmpDir}"
    vars['gitUrl'] = "ssh://${vars.autoUser}@${GERRIT_HOST}:${GERRIT_PORT}/${GERRIT_PROJECT}"
    vars['ocProjectNameSuffix'] = "mr-${GERRIT_CHANGE_NUMBER}-${GERRIT_PATCHSET_NUMBER}"
    vars['tagVersion'] = "snapshot-${vars.ocProjectNameSuffix}"
    vars['imageProject'] = "infra"

    currentBuild.displayName = "${currentBuild.displayName}-${vars.branch}(${vars.gerritChange})"
    currentBuild.description = """Branch: ${vars.branch}
Patchset: ${vars.gerritChange}
"""

    dir("${vars.devopsRoot}/${vars.pipelinesPath}/stages/") {
        try {
            stage("CHECKOUT") {
                stage = load "gerrit-checkout.groovy"
                stage.run(vars)
            }

            stage("BUILD") {
                stage = load "build.groovy"
                stage.run(vars)
            }

            stage("DEPLOY PROJECT") {
                stage = load "deploy-environment.groovy"
                stage.run(vars, commonLib)
            }

            stage("MANUAL APPROVE") {
                commonLib.sendEmail("${GERRIT_CHANGE_OWNER_EMAIL},${vars.emailRecipients}", "[EDP][JENKINS] Precommit pipeline is waiting for manual approve", "approve")
                input "Is everything ok with environment ${vars.ocProjectNameSufffix}?"
            }
            currentBuild.displayName = "${currentBuild.displayName}-APPROVED"
        }
        catch (Exception ex) {
            println("[JENKINS][ERROR] Exception - ${ex}")
            println "[JENKINS][ERROR] Trace: ${ex.getStackTrace().collect { it.toString() }.join('\n')}"
            currentBuild.displayName = "${currentBuild.displayName}-FAILED"
            currentBuild.result = 'FAILURE'
        }
        finally {
            stage = load "delete-environment.groovy"
            stage.run(vars)
        }
    }
}