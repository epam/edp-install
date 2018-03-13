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

        vars['branch'] = env.GERRIT_BRANCH ? GERRIT_BRANCH : env.SERVICE_BRANCH

        currentBuild.displayName = "${currentBuild.number}-${vars.branch}"
        currentBuild.description = "Branch: ${vars.branch}"
        commonLib.getDebugInfo(vars)
    }

    dir("${vars.devopsRoot}/${vars.pipelinesPath}/stages/") {
        stage("CHECKOUT") {
            stage = load "git-checkout.groovy"
            stage.run(vars)

            def versionFile = new FilePath(Jenkins.getInstance().getComputer(env['NODE_NAME']).getChannel(), "${vars.workDir}/version.json").readToString()
            vars['edpInstallVersion'] = "${new JsonSlurperClassic().parseText(versionFile).get('edp-install')}-${BUILD_NUMBER}"
        }

        stage("BUILD") {
            stage = load "edp-install-build.groovy"
            stage.run(vars)

            vars['sourceProject'] = vars.dockerImageProject
            vars['sourceTag'] = vars.edpInstallVersion
            vars['targetProject'] = vars.sitProject
            vars['targetTag'] = vars.edpInstallVersion
            stage = load "tag-image.groovy"
            stage.run(vars)
        }

        stage("PUSH-TO-NEXUS") {
            vars['artifact'] = [:]
            vars['artifact']['repository'] = "${vars.nexusRepository}-snapshots"
            vars['artifact']['version'] = vars.edpInstallVersion
            vars['artifact']['id'] = "edp-install"
            vars['artifact']['path'] = "${vars.workDir}/openshift/devops/pipelines/oc_templates/edp-install.yaml"
            stage = load "push-single-artifact-to-nexus.groovy"
            stage.run(vars)
        }

        build job: 'EDP-SIT-Deploy', wait: false, parameters: []
    }
}