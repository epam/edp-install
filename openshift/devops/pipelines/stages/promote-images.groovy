def run(vars) {
    script {
        openshift.withCluster() {
            openshift.withProject() {
                vars.images.each() { imageName->
                    vars.targetTags.each() { tagName->
                        openshift.tag("${vars.sourceProject}/${imageName}:${vars.sourceTag}","${vars.targetProject}/${imageName}:${tagName}")
                    }
                }
            }
        }
    }
}

return this;