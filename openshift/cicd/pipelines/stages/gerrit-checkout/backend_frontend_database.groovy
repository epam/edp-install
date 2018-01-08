/**
 * Checkout from gerrit.  Parameters are defined by gerrit trigger
 * @param vars object with all pipeline variables
 */
def run(vars) {
    sh "rm -rf ${vars.serviceDir}*"
    dir("${vars.serviceDir}") {
        checkout([$class                           : 'GitSCM', branches: [[name: "${vars.gerritChange}"]],
                  doGenerateSubmoduleConfigurations: false, extensions: [],
                  submoduleCfg                     : [],
                  userRemoteConfigs                : [[refspec: "${GERRIT_REFSPEC}:${vars.gerritChange}",
                                                       credentialsId: 'auto_epmc-java_vcs',
                                                       url    : "${vars.gitMicroservicesUrl}"]]])
        vars.serviceDir = "${vars.serviceDir}/${vars.serviceType}"
    }
    this.result = "success"
}

return this;
