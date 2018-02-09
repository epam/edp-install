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
    }
    this.result = "success"
}

return this;
