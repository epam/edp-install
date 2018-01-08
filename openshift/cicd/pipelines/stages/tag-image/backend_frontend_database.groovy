def run(vars) {
    script {
        openshift.withCluster() {
            openshift.withProject() {
                openshift.tag("${vars.sourceProject}/${vars.serviceType}:latest","${vars.sourceProject}/${vars.serviceType}:stable")
                if (vars.targetProject == "uat")
                    openshift.tag("${vars.sourceProject}/${vars.serviceType}:latest","${vars.targetProject}/${vars.serviceType}:stable")
                else
                    openshift.tag("${vars.sourceProject}/${vars.serviceType}:latest","${vars.targetProject}/${vars.serviceType}:latest")
            }
        }
    }
}

return this;