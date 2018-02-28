//Define common variables
vars = [:]
commonLib = null

PIPELINES_PATH_DEFAULT = "openshift/devops/pipelines"

node("master") {
    vars['pipelinesPath'] = env.PIPELINES_PATH ? PIPELINES_PATH : PIPELINES_PATH_DEFAULT

    def workspace = "${WORKSPACE.replaceAll("@.*", "")}@script"
    dir("${workspace}") {
        stash name: 'data', includes: "**", useDefaultExcludes: false
        commonLib = load "${vars.pipelinesPath}/libs/common.groovy"
    }
}

node("java") {
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

        currentBuild.displayName = "${currentBuild.number}-${GERRIT_BRANCH}(${vars.gerritChange})"
        currentBuild.description = "Branch: ${GERRIT_BRANCH}\r\nOwner: ${GERRIT_CHANGE_OWNER_EMAIL}"
        commonLib.getDebugInfo(vars)
    }

    dir("${vars.devopsRoot}/${vars.pipelinesPath}/stages/") {
        stage("CHECKOUT") {
            stage = load "gerrit-checkout.groovy"
            stage.run(vars)
        }

        stage("COMPILE") {
            stage = load "java-compile.groovy"
            stage.run(vars)
        }

        stage("UNIT-TEST") {
            stage = load "java-unit-tests.groovy"
            stage.run(vars)
        }

        stage("SONAR-PREVIEW") {
            vars['branch'] = vars.gerritChange
            stage = load "java-sonar-preview.groovy"
            stage.run(vars)
        }

        stage("POST-TO-GERRIT") {
            stage = load "post-to-gerrit.groovy"
            stage.run(vars)
        }

        stage("POST-TO-SONAR") {
            stage = load "post-to-sonar.groovy"
            stage.run(vars)
        }

        stage("SONAR-QG-CHECK") {
            stage = load "sonar-qg-check.groovy"
            stage.run(vars)
        }
    }
}