def run(vars) {
    vars['pomServiceVersion'] = sh(
            script: """
                        mvn org.apache.maven.plugins:maven-help-plugin:2.1.1:evaluate -Dexpression=project.version|grep -Ev '(^\\[|Download\\w+:)'
                    """,
            returnStdout: true
    ).trim()
    this.result = "success"
}

return this;