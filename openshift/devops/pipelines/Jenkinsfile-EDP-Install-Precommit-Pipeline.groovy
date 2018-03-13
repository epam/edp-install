import groovy.json.*
import hudson.FilePath

PIPELINES_PATH_DEFAULT = "openshift/devops/pipelines"

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
    stage("INITIALIZATION") {
        commonLib.getConstants(vars)
        try {
            dir("${vars.devopsRoot}") {
                unstash 'data'
            }
        } catch (Exception ex) {
            commonLib.failJob("[JENKINS][ERROR] Devops repository unstash has failed. Reason - ${ex}")
        }

        vars['gerritChange'] = "change-${GERRIT_CHANGE_NUMBER}-${GERRIT_PATCHSET_NUMBER}"
        vars['ocProjectNameSuffix'] = "mr-${GERRIT_CHANGE_NUMBER}-${GERRIT_PATCHSET_NUMBER}"
        vars['edpInstallVersion'] = "${vars.ocProjectNameSuffix}"

        currentBuild.displayName = "${currentBuild.displayName}-${GERRIT_BRANCH}(${vars.gerritChange})"
        currentBuild.description = "Branch: ${GERRIT_BRANCH}\r\nOwner: ${GERRIT_CHANGE_OWNER_EMAIL}"
        commonLib.getDebugInfo(vars)
    }

    dir("${vars.devopsRoot}/${vars.pipelinesPath}/stages/") {
        try {
            stage("CHECKOUT") {
                stage = load "gerrit-checkout.groovy"
                stage.run(vars)
            }

            stage("BUILD") {
                stage = load "edp-install-build.groovy"
                stage.run(vars)

                vars['sourceProject'] = vars.dockerImageProject
                vars['sourceTag'] = "latest"
                vars['targetProjects'] = [vars.dockerImageProject]
                vars['targetTags'] = [vars.edpInstallVersion]

                stage = load "promote-images.groovy"
                stage.run(vars)
            }

            stage("DEPLOY EDP") {
                vars['edpCockpitVersion'] = "master"
                vars['edpInstallTemplate'] = "${vars.workDir}/openshift/devops/pipelines/oc-templates/edp-install.yaml"
                stage = load "edp-install-deploy.groovy"
                stage.run(vars, commonLib)
            }

            stage("RUN AUTOTESTS") {
                stage = load "java-run-autotests.groovy"
                stage.run(vars)
            }

            stage("MANUAL APPROVE") {
                commonLib.sendEmail(
                        "${GERRIT_CHANGE_OWNER_EMAIL},${vars.emailRecipients}",
                        "[EDP][JENKINS] Precommit pipeline is waiting for manual approve", "approve")
                timeout(vars.operationsTimeout.toInteger()) {
                    input "Is everything ok with environment ${vars.ocProjectNameSufffix}?"
                }
            }
            currentBuild.displayName = "${currentBuild.displayName}-APPROVED"
        }
        catch (Exception ex) {
            println "[JENKINS][ERROR] Trace: ${ex.getStackTrace().collect { it.toString() }.join('\n')}"
            currentBuild.result = 'FAILURE'
            error "[JENKINS][ERROR] Exception - ${ex}"
        }
        finally {
            vars['ocProjectNameSuffixes']=["$vars.ocProjectNameSuffix"]
            stage = load "delete-environment.groovy"
            stage.run(vars)
        }
    }
}