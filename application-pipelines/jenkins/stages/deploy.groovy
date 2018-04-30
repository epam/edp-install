def run(vars) {
    openshift.withCluster() {
        if (!openshift.selector("project", vars.deployProject).exists()) {
            openshift.newProject(vars.deployProject)
            sh "oc adm policy add-role-to-user admin admin -n ${vars.deployProject}"
        }
        vars.get(vars.appSettingsKey).each() { application ->
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

return this;