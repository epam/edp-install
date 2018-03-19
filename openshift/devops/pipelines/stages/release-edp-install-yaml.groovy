import hudson.FilePath

def run(vars) {
    def edpInstallYamlFile = new FilePath(Jenkins.getInstance().getComputer(env['NODE_NAME']).getChannel(), "${vars.edpInstallTemplate}")
    def data = readYaml file: "${vars.edpInstallTemplate}"

    data['objects']['spec']['template']['spec']['containers'][0][0]['image'] = "${vars.externalDockerRegistry}:${vars.externalDockerRegistryPort}/${vars.releaseProject}/edp-install:${vars.edpInstallVersion}"
    data['objects']['spec']['template']['spec']['containers'][0][0]['command'].removeAll { it.contains('version') }
    data['objects']['spec']['template']['spec']['containers'][0][0]['command'].removeAll { it.contains('image') }
    data['objects']['spec']['template']['spec']['containers'][0][0]['command'].addAll([
            "-e gerrit_job_version=${vars.externalDockerRegistry}:${vars.externalDockerRegistryPort}/${vars.releaseProject}/edp-gerrit-job:${vars.edpInstallVersion}",
            "-e cockpit_version=${vars.externalDockerRegistry}:${vars.externalDockerRegistryPort}/${vars.releaseProject}/edp-cockpit:${vars.edpCockpitVersion}",
            "-e jenkins_frontend_image=${vars.externalDockerRegistry}:${vars.externalDockerRegistryPort}/${vars.releaseProject}/edp-ui-slave:${vars.edpInstallVersion}",
            "-e jenkins_backend_image=openshift/jenkins-slave-maven-centos7"
    ])

    def indexToRemove = []
    for (i = 0; i < data.parameters.size(); i++) {
        if (data['parameters'][i]['name'].contains("VERSION") || data['parameters'][i]['name'].contains("IMAGE")) {
            indexToRemove.push(data.parameters[i])
        }
    }
    data['parameters'].removeAll(indexToRemove as Object[])

    edpInstallYamlFile.delete()
    writeYaml file: "${vars.edpInstallTemplate}", data: data
    println("[JENKINS][DEBUG] EDP-Install YAML is ready to be upload to nexus:\r\n${edpInstallYamlFile.readToString()}")

    this.result = "success"
}

return this;