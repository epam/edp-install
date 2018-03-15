import groovy.json.*

PIPELINES_PATH_DEFAULT = "openshift/devops/pipelines"

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
    stage("INITIALIZATION") {
        commonLib.getConstants(vars)
        try {
            dir("${vars.devopsRoot}") {
                unstash 'data'
            }
        } catch (Exception ex) {
            commonLib.failJob("[JENKINS][ERROR] Devops repository unstash has failed. Reason - ${ex}")
        }

        vars['ocProjectNameSuffix'] = "release-env"
        vars['version'] = '0.1'
        vars['rcNumber'] = RC_NUMBER
        vars['branch'] = "${vars.version}.${RC_NUMBER}-RC"
        vars['prefix'] = "RELEASE"
        vars['edpInstallVersion'] = "${vars.version}.${rcNumber}-release"

        currentBuild.displayName = "${currentBuild.displayName}-${vars.branch}"
        currentBuild.description = "Branch: ${vars.branch}"
        commonLib.getDebugInfo(vars)
    }

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
                vars['edpInstallTemplate'] = "${vars.workDir}/openshift/devops/pipelines/oc-templates/edp-install.yaml"
                stage = load "edp-install-deploy.groovy"
                stage.run(vars, commonLib)
            }

            stage("MANUAL APPROVE") {
                commonLib.sendEmail("${GERRIT_CHANGE_OWNER_EMAIL},${vars.emailRecipients}", "[EDP][JENKINS] Precommit pipeline is waiting for manual approve", "approve")
                input "Is everything ok with environment ${vars.ocProjectNameSuffix}?"
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
            currentBuild.result = 'FAILURE'
        }
        finally {
            stage = load "delete-environment.groovy"
            stage.run(vars)
        }
    }
}