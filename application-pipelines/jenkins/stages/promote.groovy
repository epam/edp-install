def run(vars) {
    openshift.withCluster() {
        openshift.withProject() {
            vars.get(vars.appSettingsKey).each() { application ->
                vars.targetTags.each() { tagName ->
                    openshift.tag("${vars.sourceProject}/${application.name}:${vars.sourceTag}", "${vars.targetProject}/${application.name}:${tagName}")
                }
            }
        }
    }
    this.result = "success"
}

return this;