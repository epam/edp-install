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

        vars['branch'] = env.GERRIT_BRANCH ? GERRIT_BRANCH : env.SERVICE_BRANCH

        currentBuild.displayName = "${currentBuild.number}-${vars.branch}"
        currentBuild.description = "Branch: ${vars.branch}"
        commonLib.getDebugInfo(vars)

    }
    dir("${vars.devopsRoot}/${vars.pipelinesPath}/stages/") {
        stage("CHECKOUT") {
            stage = load "git-checkout.groovy"
            stage.run(vars)

            stage = load "get-pom-version.groovy"
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

        stage("POST-TO-SONAR") {
            stage = load "post-to-sonar.groovy"
            stage.run(vars)
        }

        stage("SONAR-QG-CHECK") {
            stage = load "sonar-qg-check.groovy"
            stage.run(vars)
        }

        stage("PUSH-TO-NEXUS") {
            stage = load "push-to-nexus.groovy"
            stage.run(vars)
        }

        stage("DOCKER-BUILD") {
            stage = load "java-docker-build.groovy"
            stage.run(vars)

            vars['images'] = ["${vars.gerritProject}"]
            vars['sourceProject'] = vars.dockerImageProject
            vars['sourceTag'] = "SNAPSHOT"
            vars['targetProject'] = vars.sitProject
            vars['targetTag'] = "SNAPSHOT"
            stage = load "tag-image.groovy"
            stage.run(vars)
        }

        build job: 'EDP-SIT-Deploy', wait: false, parameters: []
    }
}