/**
 * Checkout from gerrit.  Parameters are defined by gerrit trigger
 * @param vars object with all pipeline variables
 */
def run(vars) {
    dir("${vars.workDir}") {
        sh "oc delete project ${vars.ocProjectName}"
    }
    this.result = "success"
}

return this;
