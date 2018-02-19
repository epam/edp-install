/**
 * Checkout from gerrit.  Parameters are defined by gerrit trigger
 * @param vars object with all pipeline variables
 */
def run(vars, commonLib) {
    dir("${vars.workDir}/openshift/devops/pipelines/oc_templates") {
        openshift.withCluster() {

            //TODO Remove this when central logging will be implemented
            try {
                openshift.selector("job/edp-deploy-${vars.ocProjectName}").delete()
                println("[JENKINS][DEBUG] Job edp-deploy-${vars.ocProjectName} has been deleted")
            }
            catch (Exception ex) {
                println("[JENKINS][DEBUG] Job edp-deploy-${vars.ocProjectName} not found")
            }

            try {
                def job = openshift.newApp("--param=GERRIT_DATA_CAPACITY=1Gi",
                        "--param=GERRIT_DB_CAPACITY=1Gi",
                        "--param=JENKINS_VOLUME_CAPACITY=1Gi",
                        "--param=JENKINS_MAVEN_CACHE_VOLUME_CAPACITY=1Gi",
                        "--param=NEXUS_VOLUME_CAPACITY=1Gi",
                        "--param=SONAR_VOLUME_CAPACITY=1Gi",
                        "--param=SONAR_DB_CAPACITY=1Gi",
                        "--param=EDP_VERSION=${vars.externalDockerRegistry}/${vars.imageProject}/edp:${vars.tagVersion}",
                        "--param=PROJECTS_PREFIX=${vars.ocProjectName}", "-f edp-install.yaml").narrow('job').object()
                timeout(30) {
                    created = false
                    while (!job.status.succeeded && job.status.succeeded < 1) {
                        job = openshift.selector("job/edp-deploy-${vars.ocProjectName}").object()
                        println("[JENKINS][DEBUG] Job hasn't finished yet. Current job status - ${job.status}")
                        sleep(60)
                    }
                }
            }
            catch (Exception ex) {
                commonLib.sendEmail("${GERRIT_CHANGE_OWNER_EMAIL}", "[EDP][JENKINS] Deploy stage has been failed. Reason - ${ex}", "failed")
                commonLib.failJob("[JENKINS][ERROR] Deploy stage has been failed. Reason - ${ex}")
            }
            finally {
                //TODO Start to use this code when central logging will be implemented
                //openshift.selector("job/edp-deploy-${vars.ocProjectName}").delete()
            }
        }
    }
    this.result = "success"
}

return this;
