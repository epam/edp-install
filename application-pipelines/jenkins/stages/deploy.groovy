def run(vars) {
    openshift.withCluster() {
        if (!openshift.selector("project", vars.deployProject).exists()) {
            openshift.newProject(vars.deployProject)
            sh "oc adm policy add-role-to-user admin admin -n ${vars.deployProject}"
        }
        vars.get(vars.appSettingsKey).each() { application ->
            if (!checkImageExists(application) || !checkTemplateExists(application))
                return

            if (application.need_database)
                sh "oc adm policy add-scc-to-user anyuid -z ${application.name} -n ${vars.deployProject}"

            openshift.withProject(vars.deployProject) {
                openshift.create(openshift.process(readFile(file: "${vars.deployTemplatesPath}/${application.name}.yaml"),
                        "-p APP_IMAGE=${vars.pipelineProject}/${application.name}",
                        "-p APP_VERSION=${application.version}",
                        "-p NAMESPACE=${vars.deployProject}")
                )
            }

            checkDeployment(application)
        }
        vars.get(vars.svcSettingsKey).each() { service ->
            if (!checkTemplateExists(service))
                return

            sh "oc adm policy add-scc-to-user anyuid -z ${service.name} -n ${vars.deployProject}"
            openshift.withProject(vars.deployProject) {
                openshift.create(openshift.process(readFile(file: "${vars.deployTemplatesPath}/${service.name}.yaml"),
                        "-p SERVICE_IMAGE=${service.image}",
                        "-p SERVICE_VERSION=${service.version}")
                )
            }
            checkDeployment(service)
        }
    }
    this.result = "success"
}

def checkImageExists(object) {
    def imageExists = sh(
            script: "oc -n ${vars.pipelineProject} get is ${object.name} --no-headers | awk '{print \$1}'",
            returnStdout: true
    ).trim()
    if (imageExists == "") {
        println("[JENKINS][WARNING] Image stream ${object.name} doesn't exist in the project ${vars.pipelineProject}\r\n" +
                "Deploy will be skipped")
        object['deployed'] = false
        return false
    }

    def tagExist = sh(
            script: "oc -n ${vars.pipelineProject} get is ${object.name} -o jsonpath='{.spec.tags[?(@.name==\"${object.version}\")].name}'",
            returnStdout: true
    ).trim()
    if (tagExist == "") {
        println("[JENKINS][WARNING] Image stream ${object.name} with tag ${object.version} doesn't exist in the project ${vars.pipelineProject}\r\n" +
                "Deploy will be skipped")
        object['deployed'] = false
        return false
    }
    object['deployed'] = true
    return true
}

def checkTemplateExists(object) {
    def templateFile = new File("${vars.deployTemplatesPath}/${object.name}.yaml")
    if (!templateFile.exists()) {
        println("[JENKINS][WARNING] Template file for ${object.name} doesn't exist in ${vars.deployTemaplatesDirectory} in devops repository\r\n" +
                "Deploy will be skipped")
        object['deployed'] = false
        return false
    }

    object['deployed'] = true
    return true
}

def checkDeployment(object) {
    println("[JENKINS][DEBUG] Validate deployment - ${object.name} in ${vars.deployProject}")
    try {
        openshiftVerifyDeployment apiURL: '', authToken: '', depCfg: "${object.name}",
                namespace: "${vars.deployProject}", replicaCount: '1', verbose: 'false',
                verifyReplicaCount: 'true', waitTime: '600', waitUnit: 'sec'
        println("[JENKINS][DEBUG] Service ${object.name} in project ${vars.deployProject} deployed")
    }
    catch (Exception verifyDeploymentException) {
        commonLib.failJob("[JENKINS][ERROR] ${object.name} deploy have been failed. Reason - ${verifyDeploymentException}")
    }

}
return this;