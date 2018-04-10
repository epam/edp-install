def run(vars) {
    sh "rm -rf ${vars.workDir}*"
    dir("${vars.workDir}") {
        checkout([$class                           : 'GitSCM', branches: [[name: "${vars.serviceBranch}"]],
                  doGenerateSubmoduleConfigurations: false, extensions: [],
                  submoduleCfg                     : [],
                  userRemoteConfigs                : [[credentialsId: "${vars.gerritCredentials}",
                                                       url: "${vars.gitApplicationUrl}"]]])
    }
    this.result = "success"
}

return this;
