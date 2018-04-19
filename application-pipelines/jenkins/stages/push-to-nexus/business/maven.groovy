def run(vars) {
    dir("${vars.workDir}") {
        vars['pomVersion'] = sh(
                script: """
                        mvn org.apache.maven.plugins:maven-help-plugin:2.1.1:evaluate -Dexpression=project.version|grep -Ev '(^\\[|Download\\w+:)'
                    """,
                returnStdout: true
        ).trim().toLowerCase()
        println("[JENKINS][DEBUG] Pom version - ${vars.pomVersion}")
        def nexusRepositoryUrl = vars.pomVersion.contains("snapshot") ? "${vars.nexusMavenRepositoryUrl}-snapshots" : "${vars.nexusMavenRepositoryUrl}-releases"

        sh "mvn deploy -DskipTests=true -DaltDeploymentRepository=nexus::default::${nexusRepositoryUrl}  --settings ${vars.devopsRoot}/${vars.mavenSettings}"
    }
    this.result = "success"
}

return this;
