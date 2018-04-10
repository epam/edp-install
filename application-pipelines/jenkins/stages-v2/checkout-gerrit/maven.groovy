def run(vars) {
    sh "rm -rf ${vars.workDir}*"
    dir("${vars.workDir}") {
        checkout([$class                           : 'GitSCM', branches: [[name: "${vars.gerritChange}"]],
                  doGenerateSubmoduleConfigurations: false, extensions: [],
                  submoduleCfg                     : [],
                  userRemoteConfigs                : [[refspec: "${GERRIT_REFSPEC}:${vars.gerritChange}",
                                                       credentialsId: "${vars.gerritCredentials}",
                                                       url    : "${vars.gitApplicationUrl}"]]])
    }
    this.result = "success"
}

return this;
