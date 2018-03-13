def run(vars) {
    dir("${vars.workDir}") {
        vars['ocProjectNameSuffixes'] = sh(
                script: "oc get project | awk \'{print \$1}\' | egrep \'.*-$vars.projectMask-[0-9]+\' | grep -v $vars.ocProjectNameSuffix | awk -F - \'{print \$3\"-\"\$4}\'",
                returnStdout: true
        ).trim()
        vars['ocProjectNameSuffixes']=vars.ocProjectNameSuffixes.tokenize('\n').unique()
        println("[JENKINS][DEBUG] - Found suffixes - ${vars.ocProjectNameSuffixes}")
    }
    this.result = "success"
}

return this;