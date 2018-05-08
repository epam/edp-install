def run(vars) {
    openshift.withCluster() {
        openshift.withProject() {
            sh "oc -n ${vars.targetProject} policy add-role-to-group registry-viewer system:unauthenticated"
            vars.get(vars.appSettingsKey).each() { application ->
                if(!application.deployed) {
                    println("[JENKINS][WARNING] Image ${application.name} hasn't been promoted since there is no image in ${vars.pipelineProject} project")
                    return
                }
                vars.targetTags.each() { tagName ->
                    openshift.tag("${vars.sourceProject}/${application.name}:${vars.sourceTag}", "${vars.targetProject}/${application.name}:${tagName}")
                }
            }
        }
    }
    this.result = "success"
}

return this;