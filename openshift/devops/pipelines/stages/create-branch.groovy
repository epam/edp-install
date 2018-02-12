/**
 * Checkout from gerrit.  Parameters are defined by gerrit trigger
 * @param vars object with all pipeline variables
 */
def run(vars) {
    dir("${vars.workDir}") {
        withCredentials([sshUserPrivateKey(credentialsId: "${vars.credentials}", keyFileVariable: 'key', passphraseVariable: '', usernameVariable: 'git_user')]) {
            sh """
                eval `ssh-agent`
                ssh-add ${key}
                ssh-keyscan -p ${GERRIT_PORT} ${GERRIT_HOST} >> ~/.ssh/known_hosts
                git checkout -b ${vars.version}.${vars.rcNumber}-${vars.prefix}
                git push origin ${vars.version}.${vars.rcNumber}-${vars.prefix}"""
        }
    }
}

return this;
