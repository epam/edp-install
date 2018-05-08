def run(vars) {
    openshift.withCluster() {
        if (!openshift.selector("project", vars.deployProject).exists()) {
            openshift.newProject(vars.deployProject)
            sh "oc adm policy add-role-to-user admin admin -n ${vars.deployProject}"
        }
        vars.get(vars.appSettingsKey).each() { application ->
            if (!checkImageExists(application))
                return

            def template = openshift.withProject() {
                openshift.selector('template', application.name).object()
            }

            if (application.need_database)
                sh "oc adm policy add-scc-to-user anyuid -z ${application.name} -n ${vars.deployProject}"

            openshift.withProject(vars.deployProject) {
                openshift.create(openshift.process(template,
                        "-p APP_IMAGE=${vars.pipelineProject}/${application.name}",
                        "-p APP_VERSION=${application.version}",
                        "-p NAMESPACE=${vars.deployProject}")
                )
            }

            println("[JENKINS][DEBUG] Testing deployment - ${application.name} in ${vars.deployProject}")
            try {
                openshiftVerifyDeployment apiURL: '', authToken: '', depCfg: "${application.name}",
                        namespace: "${vars.deployProject}", replicaCount: '1', verbose: 'false',
                        verifyReplicaCount: 'true', waitTime: '600', waitUnit: 'sec'
                println("[JENKINS][DEBUG] Application ${application.name} in project ${vars.deployProject} deployed")
            }
            catch (Exception verifyDeploymentException) {
                commonLib.failJob("[JENKINS][ERROR] ${application.name} deploy have been failed. Reason - ${verifyDeploymentException}")
            }

        }
    }
    this.result = "success"
}

def checkImageExists(application) {
    def imageExists = sh(
            script: "oc -n ${vars.pipelineProject} get is ${application.name} --no-headers | awk '{print \$1}'",
            returnStdout: true
    ).trim()
    if (imageExists == "") {
        println("[JENKINS][WARNING] Image stream ${application.name} doesn't exist in the project ${vars.pipelineProject}\r\n" +
                "Deploy will be skipped")
        application['deployed'] = false
        return false
    }

    def tagExist = sh(
            script: "oc -n ${vars.pipelineProject} get is ${application.name} -o jsonpath='{.spec.tags[?(@.name==\"${application.version}\")].name}'",
            returnStdout: true
    ).trim()
    if (tagExist == "") {
        println("[JENKINS][WARNING] Image stream ${application.name} with tag ${application.version} doesn't exist in the project ${vars.pipelineProject}\r\n" +
                "Deploy will be skipped")
        application['deployed'] = false
        return false
    }
    application['deployed'] = true
    return true
}

return this;