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
        if (!env.RECREATE_ENVIRONMENT || !env.DEPLOY_COCKPIT) {
            commonLib.failJob("[JENKINS][ERROR] Some mandatory variables is not defined, please check. Reason - ${ex}")
        }
        commonLib.getConstants(vars)

        vars['recreateEnvironment'] = Boolean.parseBoolean(RECREATE_ENVIRONMENT)
        vars['deployCockpit'] = Boolean.parseBoolean(DEPLOY_COCKPIT)
        vars['edpInstallVersion'] = "SNAPSHOT"
        vars['edpCockpitVersion'] = "SNAPSHOT"
        vars['dockerImageProject'] = vars.sitProject
        vars['ocProjectNameSuffix'] = "sit-env"

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
        stage("PULL EDP-INSTALL TEMPLATE") {
            vars['artifact'] = [:]
            vars['artifact']['version'] = vars.edpInstallVersion
            vars['artifact']['id'] = "edp-install"
            vars['artifact']['packaging'] = "yaml"
            stage = load "pull-single-artifact-from-nexus.groovy"
            stage.run(vars)
            vars['edpInstallTemplate'] = "${vars.workDir}/edp-install.yaml"
        }
        stage("INSTALL EDP") {
            if (vars.recreateEnvironment) {
                stage = load "delete-environment.groovy"
                stage.run(vars)

                stage = load "edp-install-deploy.groovy"
                stage.run(vars, commonLib)
            }
        }
        stage("DEPLOY COCKPIT") {
            if (vars.deployCockpit && !vars.recreateEnvironment) {
                stage = load "edp-cockpit-deploy.groovy"
                stage.run(vars, commonLib)
            }
        }
        stage("INTEGRATION TEST") {
            try {
                println("[JENKINS][DEBUG] Here will be integration test")

                vars['images'] = ["edp-install", "gerrit-job", "ui-slave", "edp-cockpit"]
                vars['sourceProject'] = vars.sitProject
                vars['sourceTag'] = "SNAPSHOT"
                vars['targetProject'] = vars.qaProject
                vars['targetTag'] = "STABLE"
                stage = load "tag-image.groovy"
                stage.run(vars)
            }
            catch (Exception ex) {
                println("[JENKINS][DEBUG] Integration tests have been failed")
            }
        }
    }
}
