def run(vars) {
    script {
        openshift.withCluster() {
            openshift.withProject() {
                vars.images.each() { imageName->
                    openshift.tag("${vars.sourceProject}/${imageName}:${vars.sourceTag}","${vars.targetProject}/${imageName}:${vars.targetTag}")
                }
            }
        }
    }
}

return this;