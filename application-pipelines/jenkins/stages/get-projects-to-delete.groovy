def run(vars) {
    vars['projectsToDelete'] = sh(
            script: "oc get project --no-headers " +
                    "| egrep \'${vars.pipelineProject}-[0-9]+\' " +
                    "| grep -v ${vars.deployProject} " +
                    "| awk \'{print \$1}\'",
            returnStdout: true
    ).trim().tokenize('\n')
    println("[JENKINS][DEBUG] - Found suffixes - ${vars.projectsToDelete}")
    this.result = "success"
}

return this;