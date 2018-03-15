//Define common variables
vars = [:]
vars['artifact'] = [:]
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
            vars['artifact']['repository'] = "${vars.nexusRepository}-snapshots"
            stage = load "push-to-nexus.groovy"
            stage.run(vars)
        }

        stage("BUILD") {
            vars['edpJavaAppVersion'] = "${vars.pomServiceVersion}-${BUILD_NUMBER}"
            println("[JENKINS][DEBUG] We are going to build docker image for ${vars.gerritProject} with version ${vars.edpJavaAppVersion}")
            stage = load "java-docker-build.groovy"
            stage.run(vars)

            vars['images'] = [vars.gerritProject]
            vars['sourceProject'] = vars.dockerImageProject
            vars['sourceTag'] = "latest"
            vars['targetProjects'] = [vars.dockerImageProject, vars.sitProject]
            vars['targetTags'] = [vars.edpJavaAppVersion, "master"]

            stage = load "promote-images.groovy"
            stage.run(vars)
        }

        stage("GIT-TAG") {
            vars['gitTag'] = vars.edpJavaAppVersion
            stage = load "git-tag.groovy"
            stage.run(vars)
        }

        build job: 'EDP-SIT-Deploy', wait: false, parameters: []
    }
}