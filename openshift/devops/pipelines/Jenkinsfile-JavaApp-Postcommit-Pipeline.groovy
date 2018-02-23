import org.apache.commons.lang.RandomStringUtils

//Define common variables
tmpDir = RandomStringUtils.random(10, true, true)
vars = [:]
commonLib = null

PIPELINES_PATH_DEFAULT = "openshift/devops/pipelines"
AUTOUSER_DEFAULT = "jenkins"
EMAIL_RECIPIENTS_DEFAULT = "SpecialEPMD-EDPcoreteam@epam.com"
CREDENTIALS_DEFAULT = "gerrit-key"
DEFAULT_GERRIT_HOST = "gerrit"
DEFAULT_GERRIT_SSH_PORT = "30001"
DEFAULT_SIT_PROJECT_NAME = "edp-sit"
DEFAULT_CICD_PROJECT_NAME = "infra"

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
        vars['devopsRoot'] = new File("/tmp/${tmpDir}")
        try {
            dir("${vars.devopsRoot}") {
                unstash 'data'
            }
        } catch (Exception ex) {
            commonLib.failJob("[JENKINS][ERROR] Devops repository unstash has failed. Reason - ${ex}")
        }

        vars['autoUser'] = env.AUTOUSER ? AUTOUSER : AUTOUSER_DEFAULT
        vars['credentials'] = env.CREDENTIALS ? CREDENTIALS : CREDENTIALS_DEFAULT
        vars['workDir'] = "${WORKSPACE}/${tmpDir}"
        vars['mavenSettings'] = "${vars.pipelinesPath}/settings/maven/settings.xml"
        vars['branch'] = env.GERRIT_REFNAME ? env.GERRIT_REFNAME : env.SERVICE_BRANCH
        vars['nexusRepository'] = "http://nexus:8081/repository/edp-releases"

        vars['gerritHost'] = env.GERRIT_HOST ? env.GERRIT_HOST : DEFAULT_GERRIT_HOST
        vars['gerritSshPort'] = env.GERRIT_SSH_PORT ? env.GERRIT_SSH_PORT : DEFAULT_GERRIT_SSH_PORT
        vars['gerritProject'] = env.GERRIT_PROJECT ? env.GERRIT_PROJECT : env.GERRIT_PROJECT_NAME
        vars['sitProject'] = env.SIT_PROJECT ? SIT_PROJECT : DEFAULT_SIT_PROJECT_NAME
        vars['cicdProject'] = env.cicd_PROJECT ? cicd_PROJECT : DEFAULT_CICD_PROJECT_NAME
        vars['gitUrl'] = env.GILAB_REPO ? GILAB_REPO : "ssh://${vars.autoUser}@${vars.gerritHost}:${vars.gerritSshPort}/${vars.gerritProject}"

        println("[JENKINS][DEBUG] VARIABLES - ${vars}")

        currentBuild.displayName = "${currentBuild.number}-${vars.branch}"

        currentBuild.description = """Branch: ${vars.branch}"""
    }
    dir("${vars.devopsRoot}/${vars.pipelinesPath}/stages/") {
        stage("CHECKOUT") {
            stage = load "git-checkout.groovy"
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
        }
    }
}