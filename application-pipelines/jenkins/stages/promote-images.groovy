def run(vars) {
    openshift.withCluster() {
        openshift.withProject() {
            sh "oc -n ${vars.targetProject} policy add-role-to-group registry-viewer system:unauthenticated"
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