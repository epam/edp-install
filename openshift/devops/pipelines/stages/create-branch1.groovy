/**
 * Checkout from gerrit.  Parameters are defined by gerrit trigger
 * @param vars object with all pipeline variables
 */
def run(vars) {
        withCredentials([sshUserPrivateKey(credentialsId: 'gerrit-key', keyFileVariable: 'key', passphraseVariable: '', usernameVariable: 'git_user')]) {
                // some block
                println("key - ${key}")
            //sh "eval `ssh-agent`"
            //sh "ssh-add ${key}"
            sh """
                eval `ssh-agent`
                ssh-add ${key}
                whoami
                pwd
                ls -al ~
                cp ${key} ~/.ssh/
                git remote -v
                git version
                git checkout -b 0.1.${vars.RCnum}-${vars.prefix}
                git push origin 0.1.${vars.RCnum}-${vars.prefix}
            """

        }

}

return this;
