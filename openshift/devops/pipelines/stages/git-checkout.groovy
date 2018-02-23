/**
 * Checkout from gerrit.  Parameters are defined by gerrit trigger
 * @param vars object with all pipeline variables
 */
def run(vars) {
    sh "rm -rf ${vars.workDir}*"

    dir("${vars.workDir}") {
        checkout([$class                           : 'GitSCM', branches: [[name: "${vars.branch}"]],
                  doGenerateSubmoduleConfigurations: false, extensions: [],
                  submoduleCfg                     : [],
                  userRemoteConfigs                : [[credentialsId: "${vars.credentials}",
                                                       url    : "${vars.gitUrl}"]]])

        vars['serviceVersion'] = sh(
                script: """
                        mvn org.apache.maven.plugins:maven-help-plugin:2.1.1:evaluate -Dexpression=project.version|grep -Ev '(^\\[|Download\\w+:)'
                    """,
                returnStdout: true
        ).trim()
    }
    this.result = "success"
}

return this;
