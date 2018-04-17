def run(vars) {
    vars['projectsToDelete'] = sh(
            script: "oc get project --output=custom-columns=NAME:.metadata.name --no-headers | egrep \'${vars.pipelineProject}-[0-9]+\' | grep -v ${vars.deployProject}",
            returnStdout: true
    ).trim().tokenize('\n')
    println("[JENKINS][DEBUG] - Found suffixes - ${vars.projectsToDelete}")
    this.result = "success"
}

return this;