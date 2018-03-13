PIPELINES_PATH_DEFAULT = "openshift/devops/pipelines"

vars = [:]
vars['artifact'] = [:]
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
        vars['edpInstallVersion'] = "master"
        vars['edpCockpitVersion'] = "master"
        vars['dockerImageProject'] = vars.sitProject
        vars['ocProjectNameSuffix'] = "sit-${env.BUILD_NUMBER}"
        vars['workDir'] = vars.devopsRoot

        try {
            dir("${vars.devopsRoot}") {
                unstash 'data'
            }
        } catch (Exception ex) {
            commonLib.failJob("[JENKINS][ERROR] Devops repository unstash has failed. Reason - ${ex}")
        }

        commonLib.getDebugInfo(vars)
    }

    dir("${vars.devopsRoot}/${vars.pipelinesPath}/stages/") {
        stage("EXTRACT INSTALL VERSION") {
            vars['artifact']['id'] = "edp-install"
            stage = load "extract-version-from-json.groovy"
            stage.run(vars)
            vars['edpInstallVersion'] = vars.artifact.version
        }

        stage("PULL EDP-INSTALL TEMPLATE") {
            vars['artifact']['packaging'] = "yaml"
            stage = load "pull-single-artifact-from-nexus.groovy"
            stage.run(vars)
            vars['edpInstallTemplate'] = "${vars.workDir}/edp-install.yaml"
        }

        stage("EXTRACT COCKPIT VERSION") {
            vars['artifact']['id'] = "edp-cockpit"
            stage = load "extract-version-from-json.groovy"
            stage.run(vars)
            vars['edpCockpitVersion'] = vars.artifact.version
        }

        stage("INSTALL EDP") {
            stage = load "edp-install-deploy.groovy"
            stage.run(vars, commonLib)
        }

        stage("INTEGRATION TESTS") {
            try {
                stage = load "java-run-autotests.groovy"
                stage.run(vars)

            }
            catch (Exception ex) {
                error "[JENKINS][DEBUG] Integration tests have been failed"
            }
        }

        vars['images'] = ["edp-install", "edp-ui-slave", "edp-gerrit-job"]
        vars['sourceTag'] = 'master'
        vars['targetTags'] = [vars.sourceTag, vars.edpInstallVersion]
        vars['targetProjects'] = [vars.qaProject]
        vars['sourceProject'] = vars.sitProject

        stage = load "promote-images.groovy"
        stage.run(vars)

        vars['images'] = ["edp-cockpit"]
        vars['sourceTag'] = 'master'
        vars['targetTags'] = [vars.sourceTag, vars.edpCockpitVersion]
        vars['targetProjects'] = [vars.qaProject]
        vars['sourceProject'] = vars.sitProject

        stage = load "promote-images.groovy"
        stage.run(vars)

        // Integration tests passed so we should delete all except last
        vars['projectMask'] = "sit"

        stage = load "filter-projects.groovy"
        stage.run(vars)
        // Integration tests passed so we should delete all except last
        stage = load "delete-environment.groovy"
        stage.run(vars)

    }
}
