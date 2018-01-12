/**
 * Checkout from gerrit.  Parameters are defined by gerrit trigger
 * @param vars object with all pipeline variables
 */
def run(vars) {
    sh "rm -rf ${vars.serviceDir}*"
    dir("${vars.serviceDir}") {
        checkout([$class                           : 'GitSCM', branches: [[name: "${vars.serviceBranch}"]],
                  doGenerateSubmoduleConfigurations: false, extensions: [],
                  submoduleCfg                     : [],
                  userRemoteConfigs                : [[credentialsId: "${vars.credentials}",
                                                       url    : "${vars.gitMicroservicesUrl}"]]])
        vars.serviceDir = "${vars.serviceDir}/${vars.appPath}/${vars.serviceType}"
    }
    this.result = "success"
}

return this;
