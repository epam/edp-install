import groovy.json.*
import org.apache.commons.lang.RandomStringUtils

PIPELINES_PATH_DEFAULT = "openshift/devops/pipelines"
DOCKER_REGISTRY_DEFAULT = "docker-registry-default.main.edp.projects.epam.com"
GERRIT_HOST = 'gerrit'
GERRIT_PORT = '30001'
GERRIT_PROJECT = 'edp'
EMAIL_RECIPIENTS_DEFAULT = "SpecialEPMD-EDPcoreteam@epam.com"


tmpDir = RandomStringUtils.random(10, true, true)
vars = [:]
commonLib = null

node("master") {
    vars['pipelinesPath'] = env.PIPELINES_PATH ? PIPELINES_PATH : PIPELINES_PATH_DEFAULT

    def workspace = "${WORKSPACE.replaceAll("@", "")}@script"
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
    vars['autoUser'] = env.AUTOUSER ? AUTOUSER : "jenkins"
    vars['workDir'] = "${WORKSPACE}/${tmpDir}"
    vars['ocProjectName'] = "release-env"
    vars['credentials'] = env.CREDENTIALS ? CREDENTIALS : "gerrit-key"
    vars['gitUrl'] = "ssh://${vars.autoUser}@${GERRIT_HOST}:${GERRIT_PORT}/${GERRIT_PROJECT}"
    vars['version'] = '0.1'
    vars['rcNumber'] = RC_NUMBER
    vars['branch'] = "${vars.version}.${RC_NUMBER}-RC"
    vars['prefix'] = "RELEASE"
    vars['tagVersion'] = "${vars.version}.${rcNumber}-release"
    vars['imageProject'] = "release"

    currentBuild.displayName = "${currentBuild.displayName}-${vars.branch}"
    currentBuild.description = """Branch: ${vars.branch}
"""

    println("pn - ${vars.ocProjectName}")
    println("branch - ${vars.branch}")

    dir("${vars.devopsRoot}/${vars.pipelinesPath}/stages/") {
        try {
            stage("CHECK BRANCHES") {
                stage = load "check-branches.groovy"
                stage.run(vars)
            }

            stage("CHECKOUT") {
                stage = load "git-checkout.groovy"
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
                input "Is everything ok with environment ${vars.ocProjectName}?"
            }
            currentBuild.displayName = "${currentBuild.displayName}-APPROVED"

            stage("CREATE BRANCH") {
                stage = load "create-branch.groovy"
                stage.run(vars)
            }
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