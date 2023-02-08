# Upgrade EDP v2.7 to 2.8

This section provides the details on the EDP upgrade to 2.8.4. Explore the actions and requirements below.

!!! Note
    Kiosk is implemented and mandatory for EDP v.2.8.4 and is optional for EDP v.2.9.0 and higher.

To upgrade EDP to 2.8.4, take the following steps:

1. Deploy and configure Kiosk (create a Service Account, Account, and ClusterRoleBinging) according to the [Set Up Kiosk](install-kiosk.md#installation) documentation.

  * Update the *spec* field in the Kiosk space:

        apiVersion: tenancy.kiosk.sh/v1alpha1
        kind: Space
        metadata:
          name: <edp-project>
        spec:
          account: <edp-project>-admin

  * Create RoleBinding (required for namespaces created before using Kiosk):

    !!! Note
        In the **uid** field under the **ownerReferences** in the Kubernetes manifest, indicate the Account Custom Resource ID from *accounts.config.kiosk.sh*<br>
        `kubectl get account <edp-project>-admin -o=custom-columns=NAME:.metadata.uid --no-headers=true`

    <details>
    <summary><b>View: rolebinding-kiosk.yaml</b></summary>

        apiVersion: rbac.authorization.k8s.io/v1
        kind: RoleBinding
        metadata:
          generateName: <edp-project>-admin-
          namespace: <edp-project>
          ownerReferences:
          - apiVersion: config.kiosk.sh/v1alpha1
            blockOwnerDeletion: true
            controller: true
            kind: Account
            name: <edp-project>-admin
            uid: ''
        roleRef:
          apiGroup: rbac.authorization.k8s.io
          kind: ClusterRole
          name: kiosk-space-admin
        subjects:
        - kind: ServiceAccount
          name: <edp-project>
          namespace: security

    </details>

        kubectl create -f rolebinding-kiosk.yaml


2. With Amazon Elastic Container Registry to store the images, there are two options:
  * Enable IRSA and create AWS IAM Role for Kaniko image builder. Please refer to the [IAM Roles for Kaniko Service Accounts](kaniko-irsa.md) section for the details.
  * The Amazon Elastic Container Registry Roles can be stored in an [instance profile](https://docs.aws.amazon.com/IAM/latest/UserGuide/id_roles_use_switch-role-ec2_instance-profiles.html).

3. Update Custom Resource Definitions by applying all the necessary CRD to the cluster with the command below:

      kubectl apply -f https://raw.githubusercontent.com/epam/edp-cd-pipeline-operator/release/2.8/deploy-templates/crds/edp_v1alpha1_cdpipeline_crd.yaml
      kubectl apply -f https://raw.githubusercontent.com/epam/edp-codebase-operator/release/2.8/deploy-templates/crds/edp_v1alpha1_codebase_crd.yaml
      kubectl apply -f https://raw.githubusercontent.com/epam/edp-codebase-operator/release/2.8/deploy-templates/crds/edp_v1alpha1_cd_stage_deploy_crd.yaml
      kubectl apply -f https://raw.githubusercontent.com/epam/edp-jenkins-operator/release/2.8/deploy-templates/crds/v2_v1alpha1_jenkinsjobbuildrun_crd.yaml
      kubectl apply -f https://raw.githubusercontent.com/epam/edp-jenkins-operator/release/2.8/deploy-templates/crds/v2_v1alpha1_cdstagejenkinsdeployment_crd.yaml
      kubectl apply -f https://raw.githubusercontent.com/epam/edp-jenkins-operator/release/2.8/deploy-templates/crds/v2_v1alpha1_jenkinsjob_crd.yaml
      kubectl apply -f https://raw.githubusercontent.com/epam/edp-nexus-operator/release/2.8/deploy-templates/crds/edp_v1alpha1_nexus_crd.yaml
      kubectl apply -f https://raw.githubusercontent.com/epam/edp-keycloak-operator/release/1.8/deploy-templates/crds/v1_v1alpha1_keycloakauthflow_crd.yaml

4. With Amazon Elastic Container Registry to store and Kaniko to build the images, add the **kanikoRoleArn** parameter to the values before starting the update process. This parameter is indicated in AWS Roles once IRSA is enabled and AWS IAM Role is created for Kaniko. The value should look as follows:

      kanikoRoleArn: arn:aws:iam::<AWS_ACCOUNT_ID>:role/AWSIRSA‹CLUSTER_NAME›‹EDP_NAMESPACE›Kaniko

5. To upgrade EDP to the v.2.8.4, run the following command:

      helm upgrade edp epamedp/edp-install -n <edp-namespace> --values values.yaml --version=2.8.4

  !!! Note
      To verify the installation, it is possible to test the deployment before applying it to the cluster with:<br>
      `helm upgrade edp epamedp/edp-install -n <edp-namespace> --values values.yaml --version=2.8.4  --dry-run`

6. Remove the following Kubernetes resources left from the previous EDP installation (it is optional):

      kubectl delete cm luminatesec-conf -n <edp-namespace>
      kubectl delete sa edp edp-perf-operator -n <edp-namespace>
      kubectl delete deployment perf-operator -n <edp-namespace>
      kubectl delete clusterrole edp-<edp-namespace> edp-perf-operator-<edp-namespace>
      kubectl delete clusterrolebinding edp-<edp-namespace> edp-perf-operator-<edp-namespace>
      kubectl delete rolebinding edp-<edp-namespace> edp-perf-operator-<edp-namespace>-admin -n <edp-namespace>
      kubectl delete perfserver epam-perf -n <edp-namespace>
      kubectl delete services.v2.edp.epam.com postgres rabbit-mq -n <edp-namespace>

7. Update the CI and CD Jenkins job provisioners:<br>

  !!! Note
      Please refer to the [Manage Jenkins CI Pipeline Job Provisioner](manage-jenkins-ci-job-provision.md) section for the details.

   <details>
   <Summary><b>View: Default CI provisioner template for EDP 2.8.4</b></Summary>

```java
/* Copyright 2021 EPAM Systems.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at
http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.

See the License for the specific language governing permissions and
limitations under the License. */

import groovy.json.*
import jenkins.model.Jenkins
import hudson.model.*

Jenkins jenkins = Jenkins.instance
def stages = [:]
def jiraIntegrationEnabled = Boolean.parseBoolean("${JIRA_INTEGRATION_ENABLED}" as String)
def commitValidateStage = jiraIntegrationEnabled ? ',{"name": "commit-validate"}' : ''
def createJIMStage = jiraIntegrationEnabled ? ',{"name": "create-jira-issue-metadata"}' : ''
def buildTool = "${BUILD_TOOL}"
def goBuildStage = buildTool.toString() == "go" ? ',{"name": "build"}' : ',{"name": "compile"}'

stages['Code-review-application'] = '[{"name": "gerrit-checkout"}' + "${commitValidateStage}" + goBuildStage +
 ',{"name": "tests"},[{"name": "sonar"},{"name": "dockerfile-lint"},{"name": "helm-lint"}]]'
stages['Code-review-library'] = '[{"name": "gerrit-checkout"}' + "${commitValidateStage}" +
 ',{"name": "compile"},{"name": "tests"},' +
        '{"name": "sonar"}]'
stages['Code-review-autotests'] = '[{"name": "gerrit-checkout"},{"name": "get-version"}' + "${commitValidateStage}" +
 ',{"name": "tests"},{"name": "sonar"}' + "${createJIMStage}" + ']'
stages['Code-review-default'] = '[{"name": "gerrit-checkout"}' + "${commitValidateStage}" + ']'
stages['Code-review-library-terraform'] = '[{"name": "gerrit-checkout"}' + "${commitValidateStage}" +
 ',{"name": "terraform-lint"}]'
stages['Code-review-library-opa'] = '[{"name": "gerrit-checkout"}' + "${commitValidateStage}" +
 ',{"name": "tests"}]'
stages['Code-review-library-codenarc'] = '[{"name": "gerrit-checkout"}' + "${commitValidateStage}" +
 ',{"name": "sonar"},{"name": "build"}]'

stages['Build-library-maven'] = '[{"name": "checkout"},{"name": "get-version"},{"name": "compile"},' +
        '{"name": "tests"},{"name": "sonar"},{"name": "build"},{"name": "push"}' + "${createJIMStage}" + ',{"name": "git-tag"}]'
stages['Build-library-npm'] = stages['Build-library-maven']
stages['Build-library-gradle'] = stages['Build-library-maven']
stages['Build-library-dotnet'] = '[{"name": "checkout"},{"name": "get-version"},{"name": "compile"},' +
        '{"name": "tests"},{"name": "sonar"},{"name": "push"}' + "${createJIMStage}" + ',{"name": "git-tag"}]'
stages['Build-library-python'] = '[{"name": "checkout"},{"name": "get-version"},{"name": "compile"},' +
        '{"name": "tests"},{"name": "sonar"},{"name": "push"}' + "${createJIMStage}" + ',{"name": "git-tag"}]'
stages['Build-library-terraform'] = '[{"name": "checkout"},{"name": "get-version"},{"name": "terraform-lint"}' +
 ',{"name": "terraform-plan"},{"name": "terraform-apply"}' + "${createJIMStage}" + ',{"name": "git-tag"}]'
stages['Build-library-opa'] = '[{"name": "checkout"},{"name": "get-version"}' +
 ',{"name": "tests"}' + "${createJIMStage}" + ',{"name": "git-tag"}]'
stages['Build-library-codenarc'] = '[{"name": "checkout"},{"name": "get-version"},{"name": "sonar"},{"name": "build"}' +
    "${createJIMStage}" + ',{"name": "git-tag"}]'


stages['Build-application-maven'] = '[{"name": "checkout"},{"name": "get-version"},{"name": "compile"},' +
        '{"name": "tests"},[{"name": "sonar"}],{"name": "build"},{"name": "build-image-kaniko"},' +
        '{"name": "push"}' + "${createJIMStage}" + ',{"name": "git-tag"}]'
stages['Build-application-npm'] = stages['Build-application-maven']
stages['Build-application-gradle'] = stages['Build-application-maven']
stages['Build-application-dotnet'] = '[{"name": "checkout"},{"name": "get-version"},{"name": "compile"},' +
        '{"name": "tests"},[{"name": "sonar"}],{"name": "build-image-kaniko"},' +
        '{"name": "push"}' + "${createJIMStage}" + ',{"name": "git-tag"}]'
stages['Build-application-go'] = '[{"name": "checkout"},{"name": "get-version"},{"name": "tests"},{"name": "sonar"},' +
                                '{"name": "build"},{"name": "build-image-kaniko"}' +
                                "${createJIMStage}" + ',{"name": "git-tag"}]'
stages['Build-application-python'] = '[{"name": "checkout"},{"name": "get-version"},{"name": "compile"},' +
                                '{"name": "tests"},{"name": "sonar"},' +
                                '{"name": "build-image-kaniko"},{"name": "push"}' + "${createJIMStage}" +
                                ',{"name": "git-tag"}]'

stages['Create-release'] = '[{"name": "checkout"},{"name": "create-branch"},{"name": "trigger-job"}]'

def defaultBuild = '[{"name": "checkout"}' + "${createJIMStage}" + ']'

def codebaseName = "${NAME}"
def gitServerCrName = "${GIT_SERVER_CR_NAME}"
def gitServerCrVersion = "${GIT_SERVER_CR_VERSION}"
def gitCredentialsId = "${GIT_CREDENTIALS_ID ? GIT_CREDENTIALS_ID : 'gerrit-ciuser-sshkey'}"
def repositoryPath = "${REPOSITORY_PATH}"
def defaultBranch = "${DEFAULT_BRANCH}"

def codebaseFolder = jenkins.getItem(codebaseName)
if (codebaseFolder == null) {
    folder(codebaseName)
}

createListView(codebaseName, "Releases")
createReleasePipeline("Create-release-${codebaseName}", codebaseName, stages["Create-release"], "create-release.groovy",
        repositoryPath, gitCredentialsId, gitServerCrName, gitServerCrVersion, jiraIntegrationEnabled, defaultBranch)

if (buildTool.toString().equalsIgnoreCase('none')) {
    return true
}

if (BRANCH) {
    def branch = "${BRANCH}"
    def formattedBranch = "${branch.toUpperCase().replaceAll(/\\//, "-")}"
    createListView(codebaseName, formattedBranch)

    def type = "${TYPE}"
    def crKey = getStageKeyName(buildTool)
    createCiPipeline("Code-review-${codebaseName}", codebaseName, stages[crKey], "code-review.groovy",
            repositoryPath, gitCredentialsId, branch, gitServerCrName, gitServerCrVersion)

    def buildKey = "Build-${type}-${buildTool.toLowerCase()}".toString()
    if (type.equalsIgnoreCase('application') || type.equalsIgnoreCase('library')) {
        def jobExists = false
        if("${formattedBranch}-Build-${codebaseName}".toString() in Jenkins.instance.getAllItems().collect{it.name})
            jobExists = true

        createCiPipeline("Build-${codebaseName}", codebaseName, stages.get(buildKey, defaultBuild), "build.groovy",
                repositoryPath, gitCredentialsId, branch, gitServerCrName, gitServerCrVersion)

        if(!jobExists)
          queue("${codebaseName}/${formattedBranch}-Build-${codebaseName}")
    }
}

def createCiPipeline(pipelineName, codebaseName, codebaseStages, pipelineScript, repository, credId, watchBranch, gitServerCrName, gitServerCrVersion) {
    pipelineJob("${codebaseName}/${watchBranch.toUpperCase().replaceAll(/\\//, "-")}-${pipelineName}") {
        logRotator {
            numToKeep(10)
            daysToKeep(7)
        }
        triggers {
            gerrit {
                events {
                    if (pipelineName.contains("Build"))
                        changeMerged()
                    else
                        patchsetCreated()
                }
                project("plain:${codebaseName}", ["plain:${watchBranch}"])
            }
        }
        definition {
            cpsScm {
                scm {
                    git {
                        remote {
                            url(repository)
                            credentials(credId)
                        }
                        branches("${watchBranch}")
                        scriptPath("${pipelineScript}")
                    }
                }
                parameters {
                    stringParam("GIT_SERVER_CR_NAME", "${gitServerCrName}", "Name of Git Server CR to generate link to Git server")
                    stringParam("GIT_SERVER_CR_VERSION", "${gitServerCrVersion}", "Version of GitServer CR Resource")
                    stringParam("STAGES", "${codebaseStages}", "Consequence of stages in JSON format to be run during execution")
                    stringParam("GERRIT_PROJECT_NAME", "${codebaseName}", "Gerrit project name(Codebase name) to be build")
                    stringParam("BRANCH", "${watchBranch}", "Branch to build artifact from")
                }
            }
        }
    }
}

def getStageKeyName(buildTool) {
    if (buildTool.toString().equalsIgnoreCase('terraform')) {
        return "Code-review-library-terraform"
    }
    if (buildTool.toString().equalsIgnoreCase('opa')) {
        return "Code-review-library-opa"
    }
    if (buildTool.toString().equalsIgnoreCase('codenarc')) {
        return "Code-review-library-codenarc"
    }
    def buildToolsOutOfTheBox = ["maven","npm","gradle","dotnet","none","go","python"]
    def supBuildTool = buildToolsOutOfTheBox.contains(buildTool.toString())
    return supBuildTool ? "Code-review-${TYPE}" : "Code-review-default"
}

def createReleasePipeline(pipelineName, codebaseName, codebaseStages, pipelineScript, repository, credId,
 gitServerCrName, gitServerCrVersion, jiraIntegrationEnabled, defaultBranch) {
    pipelineJob("${codebaseName}/${pipelineName}") {
        logRotator {
            numToKeep(14)
            daysToKeep(30)
        }
        definition {
            cpsScm {
                scm {
                    git {
                        remote {
                            url(repository)
                            credentials(credId)
                        }
                        branches("${defaultBranch}")
                        scriptPath("${pipelineScript}")
                    }
                }
                parameters {
                    stringParam("STAGES", "${codebaseStages}", "")
                    if (pipelineName.contains("Create-release")) {
                        stringParam("JIRA_INTEGRATION_ENABLED", "${jiraIntegrationEnabled}", "Is Jira integration enabled")
                        stringParam("GERRIT_PROJECT", "${codebaseName}", "")
                        stringParam("RELEASE_NAME", "", "Name of the release(branch to be created)")
                        stringParam("COMMIT_ID", "", "Commit ID that will be used to create branch from for new release. If empty, HEAD of master will be used")
                        stringParam("GIT_SERVER_CR_NAME", "${gitServerCrName}", "Name of Git Server CR to generate link to Git server")
                        stringParam("GIT_SERVER_CR_VERSION", "${gitServerCrVersion}", "Version of GitServer CR Resource")
                        stringParam("REPOSITORY_PATH", "${repository}", "Full repository path")
                        stringParam("DEFAULT_BRANCH", "${defaultBranch}", "Default repository branch")
                    }
                }
            }
        }
    }
}

def createListView(codebaseName, branchName) {
    listView("${codebaseName}/${branchName}") {
        if (branchName.toLowerCase() == "releases") {
            jobFilters {
                regex {
                    matchType(MatchType.INCLUDE_MATCHED)
                    matchValue(RegexMatchValue.NAME)
                    regex("^Create-release.*")
                }
            }
        } else {
            jobFilters {
                regex {
                    matchType(MatchType.INCLUDE_MATCHED)
                    matchValue(RegexMatchValue.NAME)
                    regex("^${branchName}-(Code-review|Build).*")
                }
            }
        }
        columns {
            status()
            weather()
            name()
            lastSuccess()
            lastFailure()
            lastDuration()
            buildButton()
        }
    }
}

```
   </details>

  !!! Note
      Please refer to the [Manage Jenkins CD Pipeline Job Provisioner](manage-jenkins-cd-job-provision.md) page for the details.

   <details>
   <Summary><b>View: Default CD provisioner template for EDP 2.8.4</b></Summary>

```java
/* Copyright 2021 EPAM Systems.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at
http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.

See the License for the specific language governing permissions and
limitations under the License. */

import groovy.json.*
import jenkins.model.Jenkins

Jenkins jenkins = Jenkins.instance

def pipelineName = "${PIPELINE_NAME}-cd-pipeline"
def stageName = "${STAGE_NAME}"
def qgStages = "${QG_STAGES}"
def gitServerCrVersion = "${GIT_SERVER_CR_VERSION}"
def gitCredentialsId = "${GIT_CREDENTIALS_ID}"
def sourceType = "${SOURCE_TYPE}"
def libraryURL = "${LIBRARY_URL}"
def libraryBranch = "${LIBRARY_BRANCH}"
def autodeploy = "${AUTODEPLOY}"
def scriptPath = "Jenkinsfile"
def containerDeploymentType = "container"
def deploymentType = "${DEPLOYMENT_TYPE}"

def stages = buildStages(deploymentType, containerDeploymentType, qgStages)

def codebaseFolder = jenkins.getItem(pipelineName)
if (codebaseFolder == null) {
    folder(pipelineName)
}

if (deploymentType == containerDeploymentType) {
    createContainerizedCdPipeline(pipelineName, stageName, stages, scriptPath, sourceType,
            libraryURL, libraryBranch, gitCredentialsId, gitServerCrVersion,
            autodeploy)
} else {
    createCustomCdPipeline(pipelineName, stageName)
}

def buildStages(deploymentType, containerDeploymentType, qgStages) {
    return deploymentType == containerDeploymentType
    ? '[{"name":"init","step_name":"init"},{"name":"deploy","step_name":"deploy"},' + qgStages + ',{"name":"promote-images-ecr","step_name":"promote-images"}]'
    : ''
}

def createContainerizedCdPipeline(pipelineName, stageName, stages, pipelineScript, sourceType, libraryURL, libraryBranch, libraryCredId, gitServerCrVersion, autodeploy) {
    pipelineJob("${pipelineName}/${stageName}") {
        if (sourceType == "library") {
            definition {
                cpsScm {
                    scm {
                        git {
                            remote {
                                url(libraryURL)
                                credentials(libraryCredId)
                            }
                            branches("${libraryBranch}")
                            scriptPath("${pipelineScript}")
                        }
                    }
                }
            }
        } else {
            definition {
                cps {
                    script("@Library(['edp-library-stages', 'edp-library-pipelines']) _ \n\nDeploy()")
                    sandbox(true)
                }
            }
        }
        properties {
          disableConcurrentBuilds()
        }
        parameters {
            stringParam("GIT_SERVER_CR_VERSION", "${gitServerCrVersion}", "Version of GitServer CR Resource")
            stringParam("STAGES", "${stages}", "Consequence of stages in JSON format to be run during execution")

            if (autodeploy?.trim() && autodeploy.toBoolean()) {
                stringParam("AUTODEPLOY", "${autodeploy}", "Is autodeploy enabled?")
                stringParam("CODEBASE_VERSION", null, "Codebase versions to deploy.")
            }
        }
    }
}

def createCustomCdPipeline(pipelineName, stageName) {
    pipelineJob("${pipelineName}/${stageName}") {
        properties {
          disableConcurrentBuilds()
        }
    }
}

```
   </details>

   * It is also necessary to add the string parameter **DEPLOYMENT_TYPE** to the CD provisioner:<br>
     - Go to *job-provisions* - > *cd* -> *default* -> *configure*;<br>
     - *Add Parameter* - > *String parameter*;<br>
     - *Name* -> *DEPLOYMENT_TYPE*

8. Update Jenkins pipelines and stages to the new release tag:
  * In Jenkins, go to **Manage Jenkins** -> **Configure system** -> Find the **Global Pipeline Libraries** menu.
  * Change the **Default version** for *edp-library-stages* from *build/2.8.0-RC.6* to *build/2.9.0-RC.5*
  * Change the **Default version** for *edp-library-pipelines* from *build/2.8.0-RC.4* to *build/2.9.0-RC.3*

9. Update the *edp-admin-console* Custom Resource in the **KeycloakClient** Custom Resource Definition:<br>

   <details>
   <Summary><b>View: keycloakclient.yaml</b></Summary>

```yaml

kind: KeycloakClient
apiVersion: v1.edp.epam.com/v1alpha1
metadata:
  name: edp-admin-console
  namespace: <edp-namespace>
spec:
  advancedProtocolMappers: false
  attributes: null
  audRequired: true
  clientId: admin-console-client
  directAccess: true
  public: false
  secret: admin-console-client
  serviceAccount:
    enabled: true
    realmRoles:
      - developer
  targetRealm: <keycloak-edp-realm>
  webUrl: >-
    https://edp-admin-console-example.com

```
   </details>

      kubectl apply -f keycloakclient.yaml

10. Remove the *admin-console-client* client ID in the *edp-namespace-main* realm in Keycloak, restart the *keycloak-operator* pod and check that the new KeycloakClient is created with the **confidential** access type.

  !!! Note
      If "Internal error" occurs, regenerate the *admin-console-client* secret in the *Credentials* tab in Keycloak and update the *admin-console-client* secret key "clientSecret" and "password".

11. Update image versions for the Jenkins agents in the *ConfigMap*:

      kubectl edit configmap jenkins-slaves -n <edp-namespace>

   * The versions of the images should be:

        epamedp/edp-jenkins-dotnet-21-agent:1.0.2
        epamedp/edp-jenkins-dotnet-31-agent:1.0.2
        epamedp/edp-jenkins-go-agent:1.0.3
        epamedp/edp-jenkins-gradle-java11-agent:2.0.2
        epamedp/edp-jenkins-gradle-java8-agent:1.0.2
        epamedp/edp-jenkins-helm-agent:1.0.6
        epamedp/edp-jenkins-maven-java11-agent:2.0.3
        epamedp/edp-jenkins-maven-java8-agent:1.0.2
        epamedp/edp-jenkins-npm-agent:2.0.2
        epamedp/edp-jenkins-python-38-agent:2.0.3
        epamedp/edp-jenkins-terraform-agent:2.0.4

   * Add new Jenkins agents under the *data* field:

   <details>
   <Summary><b>View</b></Summary>

```yaml
data:
  codenarc-template: |-
    <org.csanchez.jenkins.plugins.kubernetes.PodTemplate>
      <inheritFrom></inheritFrom>
      <name>codenarc</name>
      <namespace></namespace>
      <privileged>false</privileged>
      <alwaysPullImage>false</alwaysPullImage>
      <instanceCap>2147483647</instanceCap>
      <slaveConnectTimeout>100</slaveConnectTimeout>
      <idleMinutes>5</idleMinutes>
      <activeDeadlineSeconds>0</activeDeadlineSeconds>
      <label>codenarc</label>
      <serviceAccount>jenkins</serviceAccount>
      <nodeSelector>beta.kubernetes.io/os=linux</nodeSelector>
      <nodeUsageMode>NORMAL</nodeUsageMode>
      <workspaceVolume class="org.csanchez.jenkins.plugins.kubernetes.volumes.workspace.EmptyDirWorkspaceVolume">
        <memory>false</memory>
      </workspaceVolume>
      <volumes/>
      <containers>
        <org.csanchez.jenkins.plugins.kubernetes.ContainerTemplate>
          <name>jnlp</name>
          <image>epamedp/edp-jenkins-codenarc-agent:1.0.0</image>
          <privileged>false</privileged>
          <alwaysPullImage>false</alwaysPullImage>
          <workingDir>/tmp</workingDir>
          <command></command>
          <args>${computer.jnlpmac} ${computer.name}</args>
          <ttyEnabled>false</ttyEnabled>
          <resourceRequestCpu></resourceRequestCpu>
          <resourceRequestMemory></resourceRequestMemory>
          <resourceLimitCpu></resourceLimitCpu>
          <resourceLimitMemory></resourceLimitMemory>
          <envVars>
            <org.csanchez.jenkins.plugins.kubernetes.model.KeyValueEnvVar>
              <key>JAVA_TOOL_OPTIONS</key>
              <value>-XX:+UnlockExperimentalVMOptions -Dsun.zip.disableMemoryMapping=true</value>
            </org.csanchez.jenkins.plugins.kubernetes.model.KeyValueEnvVar>
          </envVars>
          <ports/>
        </org.csanchez.jenkins.plugins.kubernetes.ContainerTemplate>
      </containers>
      <envVars/>
      <annotations/>
      <imagePullSecrets/>
      <podRetention class="org.csanchez.jenkins.plugins.kubernetes.pod.retention.Default"/>
    </org.csanchez.jenkins.plugins.kubernetes.PodTemplate>
  opa-template: |-
    <org.csanchez.jenkins.plugins.kubernetes.PodTemplate>
      <inheritFrom></inheritFrom>
      <name>opa</name>
      <namespace></namespace>
      <privileged>false</privileged>
      <alwaysPullImage>false</alwaysPullImage>
      <instanceCap>2147483647</instanceCap>
      <slaveConnectTimeout>100</slaveConnectTimeout>
      <idleMinutes>5</idleMinutes>
      <activeDeadlineSeconds>0</activeDeadlineSeconds>
      <label>opa</label>
      <serviceAccount>jenkins</serviceAccount>
      <nodeSelector>beta.kubernetes.io/os=linux</nodeSelector>
      <nodeUsageMode>NORMAL</nodeUsageMode>
      <workspaceVolume class="org.csanchez.jenkins.plugins.kubernetes.volumes.workspace.EmptyDirWorkspaceVolume">
        <memory>false</memory>
      </workspaceVolume>
      <volumes/>
      <containers>
        <org.csanchez.jenkins.plugins.kubernetes.ContainerTemplate>
          <name>jnlp</name>
          <image>epamedp/edp-jenkins-opa-agent:1.0.1</image>
          <privileged>false</privileged>
          <alwaysPullImage>false</alwaysPullImage>
          <workingDir>/tmp</workingDir>
          <command></command>
          <args>${computer.jnlpmac} ${computer.name}</args>
          <ttyEnabled>false</ttyEnabled>
          <resourceRequestCpu></resourceRequestCpu>
          <resourceRequestMemory></resourceRequestMemory>
          <resourceLimitCpu></resourceLimitCpu>
          <resourceLimitMemory></resourceLimitMemory>
          <envVars>
            <org.csanchez.jenkins.plugins.kubernetes.model.KeyValueEnvVar>
              <key>JAVA_TOOL_OPTIONS</key>
              <value>-XX:+UnlockExperimentalVMOptions -Dsun.zip.disableMemoryMapping=true</value>
            </org.csanchez.jenkins.plugins.kubernetes.model.KeyValueEnvVar>
          </envVars>
          <ports/>
        </org.csanchez.jenkins.plugins.kubernetes.ContainerTemplate>
      </containers>
      <envVars/>
      <annotations/>
      <imagePullSecrets/>
      <podRetention class="org.csanchez.jenkins.plugins.kubernetes.pod.retention.Default"/>
    </org.csanchez.jenkins.plugins.kubernetes.PodTemplate>

```
   </details>

  * Restart the Jenkins pod.

12. Update compatible plugins in Jenkins and install additional plugins:<br>
  * Go to **Manage Jenkins** -> **Manage Plugins** -> Select **Compatible** -> Click **Download now and install after restart**
  * Install the following additional plugins (click the **Available** plugins tab in Jenkins):
    - [Groovy Postbuild](https://plugins.jenkins.io/groovy-postbuild/)
    - [CloudBees AWS Credentials](https://plugins.jenkins.io/aws-credentials/)
    - [Badge](https://plugins.jenkins.io/badge/)
    - [Timestamper](https://plugins.jenkins.io/timestamper/)

13. Add the annotation `deploy.edp.epam.com/previous-stage-name: ''` (it should be empty if the CD pipeline contains one stage) to each Custom Resource in the Custom Resource Definition **Stage**, for example:

  * List all Custom Resources in **Stage**:<br>
      `kubectl get stages.v2.edp.epam.com -n <edp-namespace>`

  * Edit resources:<br>
      `kubectl edit stages.v2.edp.epam.com <cd-stage-name> -n <edp-namespace>`

        apiVersion: v2.edp.epam.com/v1alpha1
        kind: Stage
        metadata:
          annotations:
            deploy.edp.epam.com/previous-stage-name: ''

  !!! Note
      If a pipeline contains several stages, add a previous stage name indicated in the EDP Admin Console to the annotation, for example:<br>
      `deploy.edp.epam.com/previous-stage-name: 'dev'`.

15. Execute script to align _CDPipeline_ resources to the new API ([jq command-line JSON processor](https://stedolan.github.io/jq/download/) is required):

      pipelines=$( kubectl get cdpipelines -n <edp-namespace> -ojson | jq -c '.items[]' )
      for p in $pipelines; do
          echo "$p" | \
          jq '. | .spec.inputDockerStreams = .spec.input_docker_streams | del(.spec.input_docker_streams) | .spec += { "deploymentType": "container" } ' | \
          kubectl apply -f -
      done

16. Update the database in the *edp-db* pod in the edp-namespace:

  * Log in to the pod:<br>

        kubectl exec -i -t -n <edp-namespace> edp-db-<pod> -c edp-db "--" sh -c "(bash || ash || sh)"

  * Log in to the Postgress DB (where "admin" is the user the secret was created for):<br>

        psql edp-db <admin>;
        SET search_path to '<edp-namespace>';
        UPDATE cd_pipeline SET deployment_type = 'container';

17. Add `"AUTODEPLOY":"true/false","DEPLOYMENT_TYPE":"container"` to every Custom Resource in `jenkinsjobs.v2.edp.epam.com`:<br>

   * Edit Kubernetes resources:

        kubectl get jenkinsjobs.v2.edp.epam.com -n <edp-namespace>

        kubectl edit jenkinsjobs.v2.edp.epam.com <cd-pipeline-name> -n <edp-namespace>

   * Alternatively, use this script to update all the necessary jenkinsjobs Custom Resources:

        edp_namespace=<epd_namespace>
        for stages in $(kubectl get jenkinsjobs -o=name -n $edp_namespace); do kubectl get $stages -n $edp_namespace -o yaml | grep -q "container" && echo -e "\n$stages is already updated" || kubectl get $stages -n $edp_namespace -o yaml | sed 's/"GIT_SERVER_CR_VERSION"/"AUTODEPLOY":"false","DEPLOYMENT_TYPE":"container","GIT_SERVER_CR_VERSION"/g' | kubectl apply -f -; done

   * Make sure the edited resource looks as follows:

        job:
          config: '{"AUTODEPLOY":"false","DEPLOYMENT_TYPE":"container","GIT_SERVER_CR_VERSION":"v2","PIPELINE_NAME":"your-pipeline-name","QG_STAGES":"{\"name\":\"manual\",\"step_name\":\"your-step-name\"}","SOURCE_TYPE":"default","STAGE_NAME":"your-stage-name"}'
          name: job-provisions/job/cd/job/default

   * Restart the Jenkins operator pod and wait until the CD job provisioner in Jenkins creates the updated pipelines.

## Possible Issues

1. SonarQube fails during the CI pipeline run. The previous builds of SonarQube used the latest version of the [**OpenID Connect Authentication for SonarQube**](https://github.com/vaulttec/sonar-auth-oidc) plugin. Version 2.1.0 of this plugin may have issues with the connection, so it is necessary to [downgrade it](https://docs.sonarqube.org/latest/setup/install-plugin/) in order to get rid of errors in the pipeline. Take the following steps:

  * Log in to the Sonar pod:<br>

        kubectl exec -i -t -n <edp-namespace> sonar-<pod> -c sonar "--" sh -c "(bash || ash || sh)"

  * Run the command in the Sonar container:<br>

        rm extensions/plugins/sonar-auth-oidc-plugin*

  * Install the **OpenID Connect Authentication for SonarQube** plugin v2.0.0:<br>

        curl -L  https://github.com/vaulttec/sonar-auth-oidc/releases/download/v2.0.0/sonar-auth-oidc-plugin-2.0.0.jar --output extensions/plugins/sonar-auth-oidc-plugin-2.0.0.jar

  * Restart the SonarQube pod;

2. The Helm lint checker in EDP 2.8.4 has some additional rules. There can be issues with it during the Code Review pipeline in Jenkins for applications that were transferred from previous EDP versions to EDP 2.8.4. To fix this, add the following annotation to the `Chart.yaml` file:<br>
  * Go to the Git repository -> Choose the application -> Edit the `deploy-templates/Chart.yaml` file.
  * It is necessary to add the following lines to the bottom of the `Chart.yaml` file:

        home: https://github.com/your-repo.git
        sources:
          - https://github.com/your-repo.git
        maintainers:
          - name: DEV Team

  * Add **a new line character** at the end of the last line. Please be aware it is important.

### Related Articles

* [Set Up Kiosk](install-kiosk.md)
* [IAM Roles for Kaniko Service Accounts](kaniko-irsa.md)
* [Manage Jenkins CI Pipeline Job Provisioner](manage-jenkins-ci-job-provision.md)
* [Manage Jenkins CD Pipeline Job Provisioner](manage-jenkins-cd-job-provision.md)
