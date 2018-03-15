def run(vars) {
    script {
        openshift.withCluster() {
            openshift.withProject() {
                vars.images.each() { imageName ->
                    vars.targetTags.each() { tagName ->
                        vars.targetProjects.each() { targetProject ->
                            openshift.tag("${vars.sourceProject}/${imageName}:${vars.sourceTag}", "${targetProject}/${imageName}:${tagName}")
                        }
                    }
                }
            }
        }
    }
}

return this;