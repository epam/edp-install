/**
 * Checkout from gerrit.  Parameters are defined by gerrit trigger
 * @param vars object with all pipeline variables
 */
def run(vars) {
    dir("${vars.workDir}") {
        ['edp-cicd-', 'edp-cockpit-', 'edp-sit-', 'edp-qa-', 'edp-uat-'].each {
            try {
                sh "oc delete project ${it}${vars.ocProjectNameSuffix}"
                sh "oc -n ${it}${vars.ocProjectNameSuffix} get pods | grep -v NAME | awk '{print \$1}' | xargs oc -n ${it}${vars.ocProjectNameSuffix} delete pod --force --grace-period=0"
            } catch (Exception ex) {
                println("[JENKINS][ERROR] Exception - ${ex}")
            }
        }

    }
    this.result = "success"
}

return this;
