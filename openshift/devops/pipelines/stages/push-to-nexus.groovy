
def run(vars) {
    dir("${vars.workDir}") {
        if (vars.serviceVersion.contains("SNAPSHOT"))
            vars.nexusRepository = "http://nexus:8081/repository/edp-snapshots"
        sh "mvn deploy -DaltDeploymentRepository=nexus::default::${vars.nexusRepository} -DskipTests=true --settings ${vars.devopsRoot}/${vars.mavenSettings}"
    }
    this.result = "success"
}

return this;
