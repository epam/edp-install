PIPELINES_PATH_DEFAULT = "openshift/devops/pipelines"

vars = [:]
vars['artifact'] = [:]
commonLib = null

@NonCPS
def check_version() {
    def matcher = (env["EDP-PLATFORM_VERSION"] =~ /^(\d+\.\d+)\.\d+$/)
    if (matcher.count < 1)
        commonLib.failJob("[JENKINS][ERROR] Incorrect version(${env["EDP-PLATFORM_VERSION"]}) format for EDP_PLATFORM_VERSION. Correct example - X.X.X.")
    else
        vars['edpReleaseVersion'] = matcher[0][1].replace(".","-")
}

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
        if (!env["EDP-PLATFORM_VERSION"])
            commonLib.failJob("[JENKINS][ERROR] Parameter EDP_PLATFORM_VERSION is mandatory to specify, please check it.")

        this.check_version()

        vars['edpPlatformVersion'] = env["EDP-PLATFORM_VERSION"]
        vars['edpInstallVersion'] = env["EDP-INSTALL_VERSION"] ? env["EDP-INSTALL_VERSION"] : "master"
        vars['edpCockpitVersion'] = env["EDP-COCKPIT_VERSION"] ? env["EDP-COCKPIT_VERSION"] : "master"
        vars['dockerImageProject'] = vars.uatProject
        vars['releaseProject'] = "release-${vars.edpReleaseVersion}"
        vars['ocProjectNameSuffix'] = "${vars.uatProject}-${BUILD_NUMBER}"
        vars['workDir'] = vars.devopsRoot

        try {
            dir("${vars.devopsRoot}") {
                unstash 'data'
            }
        } catch (Exception ex) {
            commonLib.failJob("[JENKINS][ERROR] Devops repository unstash has failed. Reason - ${ex}")
        }
        commonLib.getDebugInfo(vars)
        currentBuild.displayName = "${currentBuild.number}-${vars.edpPlatformVersion}"
    }

    dir("${vars.devopsRoot}/${vars.pipelinesPath}/stages/") {
        stage("PULL EDP-INSTALL TEMPLATE") {
            vars['artifact']['id'] = "edp-install"
            stage = load "extract-version-from-json.groovy"
            stage.run(vars)
            vars['edpInstallVersion'] = vars.artifact.version

            vars['artifact']['packaging'] = "yaml"
            stage = load "pull-single-artifact-from-nexus.groovy"
            stage.run(vars)
            vars['edpInstallTemplate'] = "${vars.workDir}/edp-install.yaml"
        }

        stage("INSTALL EDP") {
            vars['artifact']['id'] = "edp-cockpit"
            stage = load "extract-version-from-json.groovy"
            stage.run(vars)
            vars['edpCockpitVersion'] = vars.artifact.version

            stage = load "edp-install-deploy.groovy"
            stage.run(vars, commonLib)
        }

        stage("INTEGRATION TESTS") {
            try {
                stage = load "java-run-autotests.groovy"
                stage.run(vars)
                currentBuild.description = "SIT test has been passed"
            }
            catch (Exception ex) {
                error "[JENKINS][ERROR] Integration tests have been failed"
            }
        }

        stage("MANUAL APPROVE") {
            commonLib.sendEmail("${vars.emailRecipients}","[EDP][JENKINS] UAT pipeline #${BUILD_NUMBER} is waiting for manual approve", "approve")
            timeout(vars.operationsTimeout.toInteger()) {
                try {
                    input "Is everything ok with environment ${vars.ocProjectNameSuffix}?"
                    currentBuild.displayName = "${currentBuild.displayName}-APPROVED"
                    currentBuild.description = "${currentBuild.description}\r\nManual approve has been passed"
                }
                catch (Exception ex) {
                    vars['ocProjectNameSuffixes']=[vars.ocProjectNameSuffix]
                    stage = load "delete-environment.groovy"
                    stage.run(vars)
                    error "[JENKINS][ERROR] Manual approve has not been passed"
                }
            }
        }

        stage("RELEASE ARTIFACTS") {
            sh "oc new-project ${vars.releaseProject} \
                --display-name=\"EDP Release ${vars.edpReleaseVersion.replace("-",".")}\" \
                --description=\"Release artifacts for EDP Platform version ${vars.edpReleaseVersion.replace("-",".")}\""
            sh "oc -n ${vars.releaseProject} adm policy add-role-to-user admin admin"

            vars['images'] = ["edp-install", "edp-ui-slave", "edp-gerrit-job"]
            vars['sourceTag'] = vars.edpInstallVersion
            vars['sourceProject'] = vars.uatProject
            vars['targetTags'] = [vars.sourceTag]
            vars['targetProjects'] = [vars.releaseProject]
            stage = load "promote-images.groovy"
            stage.run(vars)

            vars['images'] = ["edp-cockpit"]
            vars['sourceTag'] = vars.edpCockpitVersion
            vars['sourceProject'] = vars.uatProject
            vars['targetTags'] = [vars.sourceTag]
            vars['targetProjects'] = [vars.releaseProject]
            stage = load "promote-images.groovy"
            stage.run(vars)

            vars['artifact']['repository'] = "${vars.nexusRepository}-releases"
            vars['artifact']['version'] = vars.edpInstallVersion
            vars['artifact']['id'] = "edp-install"
            vars['artifact']['path'] = "${vars.workDir}/edp-install.yaml"
            stage = load "push-single-artifact-to-nexus.groovy"
            stage.run(vars)
        }

        vars['projectMask'] = vars.uatProject
        stage = load "filter-projects.groovy"
        stage.run(vars)
        stage = load "delete-environment.groovy"
        stage.run(vars)
    }
}
