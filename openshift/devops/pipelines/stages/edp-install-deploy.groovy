def run(vars, commonLib) {
    openshift.withCluster() {
        openshift.withProject("${vars.dockerImageProject}") {
            //TODO Remove this when central logging will be implemented
            try {
                openshift.selector("job/edp-deploy${vars.ocProjectNameSuffix}").delete()
                println("[JENKINS][DEBUG] Job edp-deploy${vars.ocProjectNameSuffix} has been deleted")
            }
            catch (Exception ex) {
                println("[JENKINS][DEBUG] Job edp-deploy${vars.ocProjectNameSuffix} not found, we are going to create it")
            }

            try {
                def job = openshift.newApp("--param=GERRIT_DATA_CAPACITY=1Gi",
                        "--param=GERRIT_DB_CAPACITY=1Gi",
                        "--param=GERRIT_JOB_VERSION=${vars.externalDockerRegistry}/${vars.dockerImageProject}/edp-gerrit-job:${vars.edpInstallVersion}",
                        "--param=GITLAB_OAUTH_APP_ID=1793958fcbf67ba9498781e84b8beee375420796e5ee4510c5baf7b7265566d0",
                        "--param=GITLAB_OAUTH_APP_SECRET=d9921be13a9980798997b28bd9b6a2fa68a33fa9fa19f466d569e48293b1b584",
                        "--param=JENKINS_VOLUME_CAPACITY=1Gi",
                        "--param=JENKINS_MAVEN_CACHE_VOLUME_CAPACITY=1Gi",
                        "--param=JENKINS_FRONTEND_IMAGE=${vars.externalDockerRegistry}/${vars.dockerImageProject}/edp-ui-slave:${vars.edpInstallVersion}",
                        "--param=NEXUS_VOLUME_CAPACITY=1Gi",
                        "--param=SONAR_VOLUME_CAPACITY=1Gi",
                        "--param=SONAR_DB_CAPACITY=1Gi",
                        "--param=EDP_VERSION=${vars.externalDockerRegistry}/${vars.dockerImageProject}/edp-install:${vars.edpInstallVersion}",
                        "--param=EDP_COCKPIT_VERSION=${vars.externalDockerRegistry}/${vars.dockerImageProject}/edp-cockpit:${vars.edpCockpitVersion}",
                        "--param=DNS_WILDCARD=main.edp.projects.epam.com",
                        "--param=PROJECTS_SUFFIX=${vars.ocProjectNameSuffix}", "-f ${vars.edpInstallTemplate}").narrow('job').object()
                timeout(vars.operationsTimeout.toInteger()) {
                    created = false
                    while (!job.status.succeeded && job.status.succeeded < 1) {
                        job = openshift.selector("job/edp-deploy${vars.ocProjectNameSuffix}").object()
                        println("[JENKINS][DEBUG] Job hasn't finished yet. Current job status - ${job.status}")
                        sleep(60)
                    }
                }
            }
            catch (Exception ex) {
                println("[JENKINS][DEBUG] Job edp-deploy${vars.ocProjectNameSuffix} hasn't been finished with 30 min, it will be deleted")
                openshift.selector("job/edp-deploy${vars.ocProjectNameSuffix}").delete()
                if (env.GERRIT_CHANGE_OWNER_EMAIL)
                    commonLib.sendEmail("${GERRIT_CHANGE_OWNER_EMAIL}", "[EDP][JENKINS] Deploy stage for EDP-INSTALL has been failed. Reason - ${ex}", "failed")
                commonLib.failJob("[JENKINS][ERROR] Deploy stage for EDP-INSTALL has been failed. Reason - ${ex}")
            }
            finally {
                //TODO Start to use this code when central logging will be implemented
                //openshift.selector("job/edp-deploy${vars.ocProjectNameSuffix}").delete()
            }
        }
    }
    this.result = "success"
}

return this;
