/**
 * Checkout from gerrit.  Parameters are defined by gerrit trigger
 * @param vars object with all pipeline variables
 */
def run(vars) {
    sh "rm -rf ${vars.workDir}*"
    def failed=false
    try {
        dir("${vars.workDir}") {
            checkout([$class                           : 'GitSCM', branches: [[name: "${vars.prefix}"]],
                      doGenerateSubmoduleConfigurations: false, extensions: [],
                      submoduleCfg                     : [],
                      userRemoteConfigs                : [[credentialsId: "${vars.credentials}",
                                                           url    : "${vars.gitUrl}"]]])
        }
    } catch (Exception ex) {
        failed=true
    }
    if (!failed) {
        currentBuild.displayName = "${currentBuild.displayName}-FAILED"
        currentBuild.result = 'FAILURE'
        error("[JENKINS][ERROR] Release branch already exists")
    }

    failed=false
    try {
        dir("${vars.workDir}") {
            checkout([$class                           : 'GitSCM', branches: [[name: "${vars.branch}"]],
                      doGenerateSubmoduleConfigurations: false, extensions: [],
                      submoduleCfg                     : [],
                      userRemoteConfigs                : [[credentialsId: "${vars.credentials}",
                                                           url    : "${vars.gitUrl}"]]])
        }
    } catch (Exception ex) {
        currentBuild.displayName = "${currentBuild.displayName}-FAILED"
        currentBuild.result = 'FAILURE'
        error("[JENKINS][ERROR] RC branch doesn't exist")

    }

    this.result = "success"


}

return this;
