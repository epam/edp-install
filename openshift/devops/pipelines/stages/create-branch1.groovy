/**
 * Checkout from gerrit.  Parameters are defined by gerrit trigger
 * @param vars object with all pipeline variables
 */
def run(vars) {
        withCredentials([sshUserPrivateKey(credentialsId: 'gerrit-key', keyFileVariable: 'key', passphraseVariable: '', usernameVariable: 'git_user')]) {
                // some block
                println("${key}")
        }

}

return this;
