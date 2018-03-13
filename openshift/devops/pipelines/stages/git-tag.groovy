def run(vars) {
    dir("${vars.workDir}") {
        sh "git config --global user.email \"${vars.gerritAutoUser}\""
        sh "git config --global user.name \"${vars.gerritAutoUser}\""
        sh "git tag -a ${vars.gitTag} -m 'Tag is added automatically by ${vars.gerritAutoUser} user'"
        sh "git push --tags"
    }
    this.result="success"
}

return this;
