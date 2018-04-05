def run(vars) {
    vars.projectsToDelete.each() { projectName ->
        openshiftDeleteResourceByKey apiURL: '', authToken: '', keys: "${projectName}", namespace: '', types: 'project', verbose: 'false'
        sleep(10)
        sh("oc -n ${projectName} delete pod --all --force --grace-period=0")
    }
    this.result = "success"
}

return this;