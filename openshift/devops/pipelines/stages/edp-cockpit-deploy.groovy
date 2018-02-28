def run(vars, commonLib) {
    def appName = "edp-cockpit"
    try {
        openshiftVerifyDeployment apiURL: '', authToken: '', depCfg: "${appName}", namespace: "${appName}-${vars.ocProjectNameSuffix}", replicaCount: '1', verbose: 'false', verifyReplicaCount: 'true', waitTime: '10', waitUnit: 'sec'
        println("[JENKINS][DEBUG] Application ${appName} in project ${appName}-${vars.ocProjectNameSuffix} is exist, we are going to set new version")
        sh "oc -n ${appName}-${vars.ocProjectNameSuffix} set image deploymentconfig/${appName} ${appName}=${vars.dockerImageProject}/${appName}:${vars.edpCockpitVersion} --source=imagestreamtag"
    }
    catch (Exception verifyDeploymentException) {
        println("[JENKINS][DEBUG] Exception - ${verifyDeploymentException}")
        openshift.withCluster() {
            openshift.withProject("${vars.dockerImageProject}") {
                //TODO Remove this when central logging will be implemented
                try {
                    openshift.selector("job/edp-deploy-${vars.ocProjectNameSuffix}").delete()
                    println("[JENKINS][DEBUG] Job edp-deploy-${vars.ocProjectNameSuffix} has been deleted")
                }
                catch (Exception ex) {
                    println("[JENKINS][DEBUG] Job edp-deploy-${vars.ocProjectNameSuffix} not found")
                }

                try {
                    def job = openshift.newApp("--param=APPS_TO_INSTALL=${appName}",
                            "--param=EDP_COCKPIT_VERSION=${vars.externalDockerRegistry}/${vars.dockerImageProject}/${appName}:${vars.edpCockpitVersion}",
                            "--param=PROJECTS_SUFFIX=${vars.ocProjectNameSuffix}", "-f ${vars.edpInstallTemplate}").narrow('job').object()
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
                    if (env.GERRIT_CHANGE_OWNER_EMAIL)
                        commonLib.sendEmail("${GERRIT_CHANGE_OWNER_EMAIL}", "[EDP][JENKINS] Deploy stage for EDP-COCKPIT has been failed. Reason - ${ex}", "failed")
                    commonLib.failJob("[JENKINS][ERROR] Deploy stage for EDP-COCKPIT has been failed. Reason - ${ex}")
                }
                finally {
                    //TODO Start to use this code when central logging will be implemented
                    //openshift.selector("job/edp-deploy-${vars.ocProjectNameSuffix}").delete()
                }
            }
        }
    }
    this.result = "success"
}

return this;
