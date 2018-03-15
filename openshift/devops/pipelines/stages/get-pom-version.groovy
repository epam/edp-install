def run(vars) {
    dir("${vars.workDir}") {
        vars['pomServiceVersion'] = sh(
                script: """
                        mvn org.apache.maven.plugins:maven-help-plugin:2.1.1:evaluate -Dexpression=project.version|grep -Ev '(^\\[|Download\\w+:)'
                    """,
                returnStdout: true
        ).trim()
        println("[JENKINS][DEBUG] Pom version has been found - ${vars.pomServiceVersion}")
    }
    this.result = "success"
}

return this;