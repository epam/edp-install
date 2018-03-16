def run(vars) {
    dir("${vars.workDir}") {
        vars['hash'] = sh(
                script: "oc get is ${vars.artifact.id} -n ${vars.dockerImageProject} -o jsonpath=\'{@.spec.tags[?(@.name==\"master\")].from.name}\'",
                returnStdout: true
        ).trim()
        vars['tags'] = sh(
                script: "oc get is ${vars.artifact.id} -n ${vars.dockerImageProject} -o jsonpath=\'{@.spec.tags[?(@.from.name==\"${vars.hash}\")].name}\'",
                returnStdout: true
        ).trim().tokenize()
        // Test results count
        println("[JENKINS][DEBUG] - Found tags - ${vars.tags}")
        if(vars.tags.size()!=2) {
            error "[JENKINS][ERROR] - Wrong number of tags to master branch"
        }
        vars['artifact']['version']=(vars.tags[0]!="master") ? vars.tags[0] : vars.tags[1]
        println("[JENKINS][DEBUG] - Found numeric tag - ${vars.artifact.version}")
    }
    this.result = "success"
}

return this;