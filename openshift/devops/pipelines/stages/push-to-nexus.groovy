
def run(vars) {
    dir("${vars.workDir}") {
        def nexusRepository = vars.pomServiceVersion.contains("SNAPSHOT") ? "${vars.nexusRepository}-snapshots" : "${vars.nexusRepository}-releases"
                sh "mvn deploy -DaltDeploymentRepository=nexus::default::${vars.nexusRepository} -DskipTests=true --settings ${vars.devopsRoot}/${vars.mavenSettings}"
    }
    this.result = "success"
}

return this;
