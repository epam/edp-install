
def run(vars) {
    dir("${vars.workDir}") {
        sh "mvn deploy -DaltDeploymentRepository=nexus::default::${vars.artifact.repository} -DskipTests=true --settings ${vars.devopsRoot}/${vars.mavenSettings}"
    }
    this.result = "success"
}

return this;
