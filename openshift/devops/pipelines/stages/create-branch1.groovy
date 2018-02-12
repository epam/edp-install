/**
 * Checkout from gerrit.  Parameters are defined by gerrit trigger
 * @param vars object with all pipeline variables
 */
def run(vars) {
    dir("${vars.workDir}") {
        withCredentials([sshUserPrivateKey(credentialsId: "${CREDENTIALS}", keyFileVariable: 'key', passphraseVariable: '', usernameVariable: 'git_user')]) {
            sh """
                eval `ssh-agent`
                ssh-add ${key}
                ssh-keyscan -p ${GERRIT_PORT} ${GERRIT_HOST} >> ~/.ssh/known_hosts
                git checkout -b ${vars.prefix}
                git push origin ${vars.prefix}"""
        }
    }
}

return this;
