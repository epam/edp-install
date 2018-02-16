def run(vars) {
    dir("${vars.workDir}") {
        script {
        openshift.withCluster() {
            openshift.withProject() {
                if (!openshift.selector("buildconfig", "edp").exists())
                    openshift.newBuild("--name=edp", "--dockerfile='FROM openshift/origin\nRUN yum install -y git ansible\nWORKDIR /opt/edp/\nCOPY openshift /opt/edp/'")
                openshift.selector("bc", "edp").startBuild("--from-dir=.", "--wait=true")
                openshift.tag("infra/edp:latest","${vars.imageProject}/edp:${vars.tagVersion}")
            }
        }
    }
    }
    this.result = "success"
}

return this;
