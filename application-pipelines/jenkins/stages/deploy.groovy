def run(vars) {
    openshift.withCluster() {
        if (!openshift.selector("project", vars.deployProject).exists())
            openshift.newProject(vars.deployProject)
        vars.get(vars.appSettingsKey).each() { application ->
            def template = openshift.withProject() {
                openshift.selector('template', application.name).object()
            }

            openshift.withProject(vars.deployProject) {
                openshift.create(openshift.process(template, "-p", "APP_VERSION=${application.version}"))
            }
        }
    }
    this.result = "success"
}

return this;