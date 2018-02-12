/**
 * Checkout from gerrit.  Parameters are defined by gerrit trigger
 * @param vars object with all pipeline variables
 */
def run(vars) {
        withCredentials([sshUserPrivateKey(credentialsId: 'gerrit-key', keyFileVariable: 'key', passphraseVariable: '', usernameVariable: 'git_user')]) {
            //sh "eval `ssh-agent`"
            //sh "ssh-add ${key}"
            sh """
                eval `ssh-agent`
                ssh-add ${key}
                whoami
                pwd
                ls -al ~
                ssh -o StrictHostKeyChecking=no jenkins@${GERRIT_HOST}:${GERRIT_PORT}
                git remote -v
                git version
                git checkout -b 0.1.${vars.RCnum}-${vars.prefix}
                git push origin 0.1.${vars.RCnum}-${vars.prefix}            """
        }

}

return this;
