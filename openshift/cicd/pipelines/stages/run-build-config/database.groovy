def run(vars) {
    script {
        openshift.withCluster() {
            openshift.withProject() {
                if (!openshift.selector("buildconfig", "${vars.serviceType}").exists())
                    openshift.newBuild("--name=${vars.serviceType}", "--image-stream=oc-liquibase-postgresql", "--binary=true")
                openshift.selector("bc", "${vars.serviceType}").startBuild("--from-file=${vars.serviceDir}/target/oc-petclinic-db-evolution-1.0-SNAPSHOT.tar.gz", "--wait=true")
                openshift.tag("ci-cd/${vars.serviceType}:latest","sit/${vars.serviceType}:latest")
            }
        }
    }
}

return this;