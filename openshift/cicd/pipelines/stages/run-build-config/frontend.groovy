def run(vars) {
    script {
        openshift.withCluster() {
            openshift.withProject() {
                if (!openshift.selector("buildconfig", "${vars.serviceType}").exists())
                    openshift.newBuild("--name=${vars.serviceType}", "--image-stream=nodejs-6-centos7", "--binary=true")
                openshift.selector("bc", "${vars.serviceType}").startBuild("--wait=true")
                openshift.tag("ci-cd/${vars.serviceType}:latest","sit/${vars.serviceType}:latest")
            }
        }
    }
}

return this;