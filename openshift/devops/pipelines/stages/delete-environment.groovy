/**
 * Checkout from gerrit.  Parameters are defined by gerrit trigger
 * @param vars object with all pipeline variables
 */
def run(vars) {
    dir("${vars.workDir}") {
        sh "oc delete project ${vars.ocProjectName}"
        sh "oc -n ${vars.ocProjectName} get pods | grep -v NAME | awk '{print \$1}' | xargs oc -n ${vars.ocProjectName} delete pod --force --grace-period=0"
    }
    this.result = "success"
}

return this;
