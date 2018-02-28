/**
 * Checkout from gerrit.  Parameters are defined by gerrit trigger
 * @param vars object with all pipeline variables
 */
def run(vars) {
    dir("${vars.workDir}") {
        ['edp-cicd-', 'edp-cockpit-', 'edp-sit-', 'edp-qa-', 'edp-uat-'].each { projectName ->
            try {
                openshiftDeleteResourceByKey apiURL: '', authToken: '', keys: "${projectName}${vars.ocProjectNameSuffix}", namespace: '', types: 'project', verbose: 'false'
                sleep(10)
                sh "for project in \$(oc get project | grep .*${vars.ocProjectNameSuffix} | grep Terminating | awk '{print \$1}'); \
                    do oc -n \$project get pods | grep -v NAME | awk '{print \$1}' | xargs oc -n \$project delete pod --force --grace-period=0; \
                    done"
            } catch (Exception ex) {
                println("[JENKINS][ERROR] Exception - ${ex}")
            }
        }

    }
    this.result = "success"
}

return this;
