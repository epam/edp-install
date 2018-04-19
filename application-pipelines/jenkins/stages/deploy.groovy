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

            openshift.withProject(vars.deployProject) {
                openshift.create(openshift.process(template,
                        "-p APP_IMAGE=${vars.pipelineProject}/${application.name}",
                        "-p APP_VERSION=${application.version}",
                        "-p NAMESPACE=${vars.deployProject}")
                )
            }
        }
    }
    this.result = "success"
}

return this;