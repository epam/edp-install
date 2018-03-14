def run(vars) {
    dir("${vars.workDir}") {
        withCredentials([sshUserPrivateKey(credentialsId: "${vars.credentials}", keyFileVariable: 'key', passphraseVariable: '', usernameVariable: 'git_user')]) {
            sh """
                eval `ssh-agent`
                ssh-add ${key}
                ssh-keyscan -p ${vars.gerritSshPort} ${vars.gerritHost} >> ~/.ssh/known_hosts
                git config --global user.email auto_epmc-java_vcs@epam.com
                git config --global user.name auto_epmc-java_vcs
                git tag -a ${vars.gitTag} -m 'Tag is added automatically by auto_epmc-java_vcs user'
                git push --tags"""
        }
    }
    this.result="success"
}

return this;
