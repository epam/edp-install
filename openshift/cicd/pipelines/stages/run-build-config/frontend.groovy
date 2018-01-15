def run(vars) {
    script {
        openshift.withCluster() {
            openshift.withProject() {
                if (!openshift.selector("buildconfig", "${vars.serviceType}").exists())
                    openshift.newBuild("--name=${vars.serviceType}", "--image-stream=s2i-nginx:latest", "--binary=true", "-e NGINX_STATIC_DIR=public")
                openshift.selector("bc", "${vars.serviceType}").startBuild("--from-dir=${vars.serviceDir}", "--wait=true")
                openshift.tag("ci-cd/${vars.serviceType}:latest","sit/${vars.serviceType}:latest")
            }
        }
    }
}

return this;