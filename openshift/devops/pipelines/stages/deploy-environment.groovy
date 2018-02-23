/**
 * Checkout from gerrit.  Parameters are defined by gerrit trigger
 * @param vars object with all pipeline variables
 */
import groovy.json.*
import hudson.FilePath

def run(vars, commonLib) {
    def versionFile = new FilePath(Jenkins.getInstance().getComputer(env['NODE_NAME']).getChannel(), "${vars.devopsRoot}/version.json").readToString()
    vars['edpCockpitVersion'] = new JsonSlurperClassic().parseText(versionFile).get('edp-cockpit')

    dir("${vars.workDir}/openshift/devops/pipelines/oc_templates") {
        openshift.withCluster() {

            //TODO Remove this when central logging will be implemented
            try {
                openshift.selector("job/edp-deploy-${vars.ocProjectNameSuffix}").delete()
                println("[JENKINS][DEBUG] Job edp-deploy-${vars.ocProjectNameSuffix} has been deleted")
            }
            catch (Exception ex) {
                println("[JENKINS][DEBUG] Job edp-deploy-${vars.ocProjectNameSuffix} not found")
            }

            try {
                def job = openshift.newApp("--param=GERRIT_DATA_CAPACITY=1Gi",
                        "--param=GERRIT_DB_CAPACITY=1Gi",
                        "--param=GERRIT_JOB_VERSION=${vars.externalDockerRegistry}/${vars.imageProject}/gerrit-job:${vars.tagVersion}",
                        "--param=JENKINS_VOLUME_CAPACITY=1Gi",
                        "--param=JENKINS_MAVEN_CACHE_VOLUME_CAPACITY=1Gi",
                        "--param=JENKINS_FRONTEND_IMAGE=${vars.externalDockerRegistry}/${vars.imageProject}/ui-slave:${vars.tagVersion}",
                        "--param=NEXUS_VOLUME_CAPACITY=1Gi",
                        "--param=SONAR_VOLUME_CAPACITY=1Gi",
                        "--param=SONAR_DB_CAPACITY=1Gi",
                        "--param=EDP_VERSION=${vars.externalDockerRegistry}/${vars.imageProject}/edp-install:${vars.tagVersion}",
                        "--param=EDP_COCKPIT_VERSION=${vars.externalDockerRegistry}/${vars.imageProject}/edp-cockpit:${vars.edpCockpitVersion}",
                        "--param=PROJECTS_SUFFIX=${vars.ocProjectNameSuffix}", "-f edp-install.yaml").narrow('job').object()
                timeout(30) {
                    created = false
                    while (!job.status.succeeded && job.status.succeeded < 1) {
                        job = openshift.selector("job/edp-deploy-${vars.ocProjectNameSuffix}").object()
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
                //openshift.selector("job/edp-deploy-${vars.ocProjectNameSuffix}").delete()
            }
        }
    }
    this.result = "success"
}

return this;
