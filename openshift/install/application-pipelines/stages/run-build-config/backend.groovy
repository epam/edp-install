def run(vars) {
    script {
        openshift.withCluster() {
            openshift.withProject() {
                if (!openshift.selector("buildconfig", "${vars.serviceType}").exists())
                    openshift.newBuild("--name=${vars.serviceType}", "--image-stream=s2i-java", "--binary=true")
                openshift.selector("bc", "${vars.serviceType}").startBuild("--from-dir=${vars.serviceDir}/target", "--wait=true")
                openshift.tag("ci-cd/${vars.serviceType}:latest","sit/${vars.serviceType}:latest")
            }
        }
    }
}

return this;