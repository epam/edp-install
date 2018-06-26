/* Copyright 2018 EPAM Systems.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at
http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.

See the License for the specific language governing permissions and
limitations under the License. */

import org.apache.commons.lang.RandomStringUtils

def run(vars) {
    openshift.withCluster() {
        if (!openshift.selector("project", vars.deployProject).exists()) {
            openshift.newProject(vars.deployProject)
            sh "oc adm policy add-role-to-user admin admin -n ${vars.deployProject}"
        }
            vars.get(vars.svcSettingsKey).each() { service ->
                deployTemplatesPath = "${vars.devopsRoot}/${vars.deployTemplatesDirectory}"
                if (!checkTemplateExists(service, deployTemplatesPath))
                    return

                sh "oc adm policy add-scc-to-user anyuid -z ${service.name} -n ${vars.deployProject}"
                sh("oc -n ${vars.deployProject} process -f ${deployTemplatesPath}/${service.name}.yaml " +
                    "-p SERVICE_IMAGE=${service.image} " +
                    "-p SERVICE_VERSION=${service.version} " +
                    "--local=true -o json | oc -n ${vars.deployProject} apply -f -")
                checkDeployment(service, 'service')
             }
        vars.get(vars.appSettingsKey).each() { application ->
            application['currentDeploymentVersion'] = getDeploymentVersion(application)
            if (!checkImageExists(application))
                return

            if (application.version =~ "stable|latest") {
                application['version'] = getNumericVersion(application)
                if (!application.version)
                    return
                }
            appDir = "${WORKSPACE}/${RandomStringUtils.random(10, true, true)}"
            deployTemplatesPath = "${appDir}/${vars.deployTemplatesDirectory}"
            sh "rm -rf ${appDir}*"
            dir("${appDir}") {
                deployTemplates(application)
            }
        }
        println("[JENKINS][DEBUG] Applications that have been updated - ${vars.updatedApplicaions}")
    }
    this.result = "success"
}

def deployTemplates(application) {
    // Checkout app
    gitApplicationUrl = "ssh://${vars.gerritAutoUser}@${vars.gerritHost}:${vars.gerritSshPort}/${application.name}"

        checkout([$class                           : 'GitSCM', branches: [[name: "**"]],
                  doGenerateSubmoduleConfigurations: false, extensions: [],
                  submoduleCfg                     : [],
                  userRemoteConfigs                : [[credentialsId: "${vars.gerritCredentials}",
                                                       refspec      : "refs/tags/${application.version}",
                                                       url          : "${gitApplicationUrl}"]]])
    if(!checkTemplateExists(application, deployTemplatesPath))
        return
    if (application.need_database)
            sh "oc adm policy add-scc-to-user anyuid -z ${application.name} -n ${vars.deployProject}"

        if (!application.currentDeploymentVersion) {
            sh("oc -n ${vars.deployProject} process -f ${deployTemplatesPath}/${application.name}.yaml " +
                    "-p APP_VERSION=${application.version} " +
                    "-p NAMESPACE=${vars.deployProject} " +
                    "-p IS_NAMESPACE=${vars.metaProject} " +
                    "--local=true -o json | oc -n ${vars.deployProject} apply -f -")
        } else {
            def currentTag = sh(
                    script: "oc -n ${vars.deployProject} get dc ${application.name} -o jsonpath='{.spec.triggers[?(@.type==\"ImageChange\")].imageChangeParams.from.name}' | awk -F: '{print \$2}'",
                    returnStdout: true
            ).trim()
            if (currentTag == application.version && vars.currentDeploymentVersion != 0) {
                println("[JENKINS][DEBUG] Deployment config ${application.name} has been already deployed with version ${application.version}")
                return
            }
            sh("oc -n ${vars.deployProject} process -f ${deployTemplatesPath}/${application.name}.yaml " +
                    "-p APP_VERSION=${application.version} " +
                    "-p NAMESPACE=${vars.deployProject} " +
                    "-p IS_NAMESPACE=${vars.metaProject} " +
                    "--local=true -o json | oc -n ${vars.deployProject} apply set-last-applied -f -")
        }
        sh("oc -n ${vars.deployProject} rollout latest dc/${application.name}")
        checkDeployment(application, 'application')
}
def getDeploymentVersion(application) {
    def deploymentExists = sh(
            script: "oc -n ${vars.deployProject} get dc ${application.name} --no-headers | awk '{print \$1}'",
            returnStdout: true
    ).trim()
    if (deploymentExists == "") {
        println("[JENKINS][WARNING] Deployment config ${application.name} doesn't exist in the project ${vars.deployProject}\r\n" +
                "[JENKINS][WARNING] We will roll it out")
        return null
    }
    def version = sh(
            script: "oc -n ${vars.deployProject} get dc ${application.name} -o jsonpath=\'{.status.latestVersion}\'",
            returnStdout: true
    ).trim().toInteger()
    return (version)
}

def getNumericVersion(application) {
    def hash = sh(
            script: "oc -n ${vars.metaProject} get is ${application.name} -o jsonpath=\'{@.spec.tags[?(@.name==\"${application.version}\")].from.name}\'",
            returnStdout: true
    ).trim()
    def tags = sh(
            script: "oc -n ${vars.metaProject} get is ${application.name} -o jsonpath=\'{@.spec.tags[?(@.from.name==\"${hash}\")].name}\'",
            returnStdout: true
    ).trim().tokenize()
    tags.removeAll { it == "latest" }
    tags.removeAll { it == "stable" }
    println("[JENKINS][DEBUG] Application ${application.name} has the following numeric tag, which corresponds to tag ${application.version} - ${tags}")
    switch (tags.size()) {
        case 0:
            println("[JENKINS][WARNING] Application ${application.name} has no numeric version for tag ${application.version}\r\n" +
                    "[JENKINS][WARNING] Deploy will be skipped")
            return null
            break
        case 1:
            return (tags[0])
            break
        default:
            println("[JENKINS][WARNING] Application ${application.name} has more than one numeric tag for tag ${application.version}\r\n" +
                    "[JENKINS][WARNING] We will use the first one")
            return (tags[0])
            break
    }
}

def checkImageExists(object) {
    def imageExists = sh(
            script: "oc -n ${vars.metaProject} get is ${object.name} --no-headers | awk '{print \$1}'",
            returnStdout: true
    ).trim()
    if (imageExists == "") {
        println("[JENKINS][WARNING] Image stream ${object.name} doesn't exist in the project ${vars.metaProject}\r\n" +
                "[JENKINS][WARNING] Deploy will be skipped")
        return false
    }

    def tagExist = sh(
            script: "oc -n ${vars.metaProject} get is ${object.name} -o jsonpath='{.spec.tags[?(@.name==\"${object.version}\")].name}'",
            returnStdout: true
    ).trim()
    if (tagExist == "") {
        println("[JENKINS][WARNING] Image stream ${object.name} with tag ${object.version} doesn't exist in the project ${vars.metaProject}\r\n" +
                "[JENKINS][WARNING] Deploy will be skipped")
        return false
    }
    return true
}

def checkTemplateExists(object, deployTemplatesPath) {
    def templateFile = new File("${deployTemplatesPath}/${object.name}.yaml")
    if (!templateFile.exists()) {
        println("[JENKINS][WARNING] Template file for ${object.name} doesn't exist in ${deployTemplatesPath} in devops repository\r\n" +
                "[JENKINS][WARNING] Deploy will be skipped")
        return false
    }
    return true
}

def checkDeployment(object, type) {
    println("[JENKINS][DEBUG] Validate deployment - ${object.name} in ${vars.deployProject}")
    try {
        openshiftVerifyDeployment apiURL: '', authToken: '', depCfg: "${object.name}",
                namespace: "${vars.deployProject}", replicaCount: '1', verbose: 'false',
                verifyReplicaCount: 'true', waitTime: '600', waitUnit: 'sec'
        println("[JENKINS][DEBUG] Deployment ${object.name} in project ${vars.deployProject} has been rolled out")
        if (type == 'application' && getDeploymentVersion(object) != object.currentDeploymentVersion)
            vars.updatedApplicaions.push(object)
    }
    catch (Exception verifyDeploymentException) {
        if (type == "application" && object.currentDeploymentVersion != 0) {
            println("[JENKINS][WARNING] Rolling out of ${object.name} with version ${object.version} has been failed.\r\n" +
                    "Rollback to the previous version")
            sh("oc -n ${vars.deployProject} rollout undo dc ${object.name}")
            openshiftVerifyDeployment apiURL: '', authToken: '', depCfg: "${object.name}",
                    namespace: "${vars.deployProject}", replicaCount: '1', verbose: 'false',
                    verifyReplicaCount: 'true', waitTime: '600', waitUnit: 'sec'
        } else
            commonLib.failJob("[JENKINS][ERROR] ${object.name} deploy has been failed. Reason - ${verifyDeploymentException}")
    }

}

return this;