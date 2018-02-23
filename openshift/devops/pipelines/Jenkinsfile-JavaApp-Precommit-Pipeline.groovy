import org.apache.commons.lang.RandomStringUtils

//Define common variables
tmpDir = RandomStringUtils.random(10, true, true)
vars = [:]
commonLib = null

PIPELINES_PATH_DEFAULT = "openshift/devops/pipelines"
AUTOUSER_DEFAULT = "jenkins"
EMAIL_RECIPIENTS_DEFAULT = "SpecialEPMD-EDPcoreteam@epam.com"
CREDENTIALS_DEFAULT = "gerrit-key"

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
        vars['branch'] = GERRIT_BRANCH
        vars['gerritChange'] = "change-${GERRIT_CHANGE_NUMBER}-${GERRIT_PATCHSET_NUMBER}"
        vars['gitUrl'] = "ssh://${vars.autoUser}@${GERRIT_HOST}:${GERRIT_PORT}/${GERRIT_PROJECT}"

        currentBuild.displayName = "${currentBuild.number}-${vars.branch}(${vars.gerritChange})"

        currentBuild.description = """Branch: ${vars.branch}"""
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