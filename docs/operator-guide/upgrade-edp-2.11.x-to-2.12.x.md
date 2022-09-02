# Upgrade EDP v.2.11.x to v.2.12.x

!!! Warning
    Please make a backup of your EDP environment before the upgrade procedure.

This section provides the details on the EDP upgrade from the v.2.11.x to the v.2.12.x. Explore the actions and requirements below.

!!! Notes
    * EDP now supports Kubernetes 1.22: Ingress Resources use `networking.k8s.io/v1`, and Ingress Operators use CustomResourceDefinition `apiextensions.k8s.io/v1`.
    * EDP Team now delivers its own Gerrit Docker image: [epamedp/edp-gerrit](https://hub.docker.com/r/epamedp/edp-gerrit/tags). It is based on the [openfrontier Gerrit Docker image](https://github.com/openfrontier/docker-gerrit/).

1. EDP now uses DefectDojo as a SAST tool. It is mandatory to [deploy DefectDojo](./install-defectdojo.md) before updating EDP to v.2.12.x. 

2. Update Custom Resource Definitions (CRDs). Run the following command, to apply all necessary CRDs to the cluster:

      kubectl apply -f https://raw.githubusercontent.com/epam/edp-admin-console-operator/release/2.12/deploy-templates/crds/v2.edp.epam.com_adminconsoles.yaml
      kubectl apply -f https://raw.githubusercontent.com/epam/edp-cd-pipeline-operator/release/2.12/deploy-templates/crds/v2.edp.epam.com_cdpipelines.yaml
      kubectl apply -f https://raw.githubusercontent.com/epam/edp-codebase-operator/release/2.13/deploy-templates/crds/v2.edp.epam.com_cdstagedeployments.yaml
      kubectl apply -f https://raw.githubusercontent.com/epam/edp-jenkins-operator/release/2.12/deploy-templates/crds/v2.edp.epam.com_cdstagejenkinsdeployments.yaml
      kubectl apply -f https://raw.githubusercontent.com/epam/edp-codebase-operator/release/2.13/deploy-templates/crds/v2.edp.epam.com_codebasebranches.yaml
      kubectl apply -f https://raw.githubusercontent.com/epam/edp-codebase-operator/release/2.13/deploy-templates/crds/v2.edp.epam.com_codebaseimagestreams.yaml
      kubectl apply -f https://raw.githubusercontent.com/epam/edp-codebase-operator/release/2.13/deploy-templates/crds/v2.edp.epam.com_codebases.yaml
      kubectl apply -f https://raw.githubusercontent.com/epam/edp-component-operator/release/0.12/deploy-templates/crds/v1.edp.epam.com_edpcomponents.yaml
      kubectl apply -f https://raw.githubusercontent.com/epam/edp-gerrit-operator/release/2.12/deploy-templates/crds/v2.edp.epam.com_gerritgroupmembers.yaml
      kubectl apply -f https://raw.githubusercontent.com/epam/edp-gerrit-operator/release/2.12/deploy-templates/crds/v2.edp.epam.com_gerritgroups.yaml
      kubectl apply -f https://raw.githubusercontent.com/epam/edp-gerrit-operator/release/2.12/deploy-templates/crds/v2.edp.epam.com_gerritmergerequests.yaml
      kubectl apply -f https://raw.githubusercontent.com/epam/edp-gerrit-operator/release/2.12/deploy-templates/crds/v2.edp.epam.com_gerritprojectaccesses.yaml
      kubectl apply -f https://raw.githubusercontent.com/epam/edp-gerrit-operator/release/2.12/deploy-templates/crds/v2.edp.epam.com_gerritprojects.yaml
      kubectl apply -f https://raw.githubusercontent.com/epam/edp-gerrit-operator/release/2.12/deploy-templates/crds/v2.edp.epam.com_gerritreplicationconfigs.yaml
      kubectl apply -f https://raw.githubusercontent.com/epam/edp-gerrit-operator/release/2.12/deploy-templates/crds/v2.edp.epam.com_gerrits.yaml
      kubectl apply -f https://raw.githubusercontent.com/epam/edp-codebase-operator/release/2.13/deploy-templates/crds/v2.edp.epam.com_gitservers.yaml
      kubectl apply -f https://raw.githubusercontent.com/epam/edp-codebase-operator/release/2.13/deploy-templates/crds/v2.edp.epam.com_gittags.yaml
      kubectl apply -f https://raw.githubusercontent.com/epam/edp-codebase-operator/release/2.13/deploy-templates/crds/v2.edp.epam.com_imagestreamtags.yaml
      kubectl apply -f https://raw.githubusercontent.com/epam/edp-jenkins-operator/release/2.12/deploy-templates/crds/v2.edp.epam.com_jenkins.yaml
      kubectl apply -f https://raw.githubusercontent.com/epam/edp-jenkins-operator/release/2.12/deploy-templates/crds/v2.edp.epam.com_jenkinsagents.yaml
      kubectl apply -f https://raw.githubusercontent.com/epam/edp-jenkins-operator/release/2.12/deploy-templates/crds/v2.edp.epam.com_jenkinsauthorizationrolemappings.yaml
      kubectl apply -f https://raw.githubusercontent.com/epam/edp-jenkins-operator/release/2.12/deploy-templates/crds/v2.edp.epam.com_jenkinsauthorizationroles.yaml
      kubectl apply -f https://raw.githubusercontent.com/epam/edp-jenkins-operator/release/2.12/deploy-templates/crds/v2.edp.epam.com_jenkinsfolders.yaml
      kubectl apply -f https://raw.githubusercontent.com/epam/edp-jenkins-operator/release/2.12/deploy-templates/crds/v2.edp.epam.com_jenkinsjobbuildruns.yaml
      kubectl apply -f https://raw.githubusercontent.com/epam/edp-jenkins-operator/release/2.12/deploy-templates/crds/v2.edp.epam.com_jenkinsjobs.yaml
      kubectl apply -f https://raw.githubusercontent.com/epam/edp-jenkins-operator/release/2.12/deploy-templates/crds/v2.edp.epam.com_jenkinsscripts.yaml
      kubectl apply -f https://raw.githubusercontent.com/epam/edp-jenkins-operator/release/2.12/deploy-templates/crds/v2.edp.epam.com_jenkinsserviceaccounts.yaml
      kubectl apply -f https://raw.githubusercontent.com/epam/edp-jenkins-operator/release/2.12/deploy-templates/crds/v2.edp.epam.com_jenkinssharedlibraries.yaml
      kubectl apply -f https://raw.githubusercontent.com/epam/edp-codebase-operator/release/2.13/deploy-templates/crds/v2.edp.epam.com_jiraissuemetadatas.yaml
      kubectl apply -f https://raw.githubusercontent.com/epam/edp-codebase-operator/release/2.13/deploy-templates/crds/v2.edp.epam.com_jiraservers.yaml
      kubectl apply -f https://raw.githubusercontent.com/epam/edp-keycloak-operator/release/1.12/deploy-templates/crds/v1.edp.epam.com_keycloakauthflows.yaml
      kubectl apply -f https://raw.githubusercontent.com/epam/edp-keycloak-operator/release/1.12/deploy-templates/crds/v1.edp.epam.com_keycloakclients.yaml
      kubectl apply -f https://raw.githubusercontent.com/epam/edp-keycloak-operator/release/1.12/deploy-templates/crds/v1.edp.epam.com_keycloakclientscopes.yaml
      kubectl apply -f https://raw.githubusercontent.com/epam/edp-keycloak-operator/release/1.12/deploy-templates/crds/v1.edp.epam.com_keycloakrealmcomponents.yaml
      kubectl apply -f https://raw.githubusercontent.com/epam/edp-keycloak-operator/release/1.12/deploy-templates/crds/v1.edp.epam.com_keycloakrealmgroups.yaml
      kubectl apply -f https://raw.githubusercontent.com/epam/edp-keycloak-operator/release/1.12/deploy-templates/crds/v1.edp.epam.com_keycloakrealmidentityproviders.yaml
      kubectl apply -f https://raw.githubusercontent.com/epam/edp-keycloak-operator/release/1.12/deploy-templates/crds/v1.edp.epam.com_keycloakrealmrolebatches.yaml
      kubectl apply -f https://raw.githubusercontent.com/epam/edp-keycloak-operator/release/1.12/deploy-templates/crds/v1.edp.epam.com_keycloakrealmroles.yaml
      kubectl apply -f https://raw.githubusercontent.com/epam/edp-keycloak-operator/release/1.12/deploy-templates/crds/v1.edp.epam.com_keycloakrealms.yaml
      kubectl apply -f https://raw.githubusercontent.com/epam/edp-keycloak-operator/release/1.12/deploy-templates/crds/v1.edp.epam.com_keycloakrealmusers.yaml
      kubectl apply -f https://raw.githubusercontent.com/epam/edp-keycloak-operator/release/1.12/deploy-templates/crds/v1.edp.epam.com_keycloaks.yaml
      kubectl apply -f https://raw.githubusercontent.com/epam/edp-nexus-operator/release/2.12/deploy-templates/crds/v2.edp.epam.com_nexuses.yaml
      kubectl apply -f https://raw.githubusercontent.com/epam/edp-nexus-operator/release/2.12/deploy-templates/crds/v2.edp.epam.com_nexususers.yaml
      kubectl apply -f https://raw.githubusercontent.com/epam/edp-perf-operator/release/2.12/deploy-templates/crds/v2.edp.epam.com_perfdatasourcegitlabs.yaml
      kubectl apply -f https://raw.githubusercontent.com/epam/edp-perf-operator/release/2.12/deploy-templates/crds/v2.edp.epam.com_perfdatasourcejenkinses.yaml
      kubectl apply -f https://raw.githubusercontent.com/epam/edp-perf-operator/release/2.12/deploy-templates/crds/v2.edp.epam.com_perfdatasourcesonars.yaml
      kubectl apply -f https://raw.githubusercontent.com/epam/edp-perf-operator/release/2.12/deploy-templates/crds/v2.edp.epam.com_perfservers.yaml
      kubectl apply -f https://raw.githubusercontent.com/epam/edp-sonar-operator/release/2.12/deploy-templates/crds/v2.edp.epam.com_sonargroups.yaml
      kubectl apply -f https://raw.githubusercontent.com/epam/edp-sonar-operator/release/2.12/deploy-templates/crds/v2.edp.epam.com_sonarpermissiontemplates.yaml
      kubectl apply -f https://raw.githubusercontent.com/epam/edp-sonar-operator/release/2.12/deploy-templates/crds/v2.edp.epam.com_sonars.yaml
      kubectl apply -f https://raw.githubusercontent.com/epam/edp-cd-pipeline-operator/release/2.12/deploy-templates/crds/v2.edp.epam.com_stages.yaml

3. Set the required parameters. For details, please refer to the [values.yaml](https://github.com/epam/edp-install/blob/release/2.12/deploy-templates/values.yaml) file.

   * In version v.2.12.x, EDP contains Gerrit `v3.6.1`. According to the [Official Gerrit Upgrade flow](https://www.gerritcodereview.com/3.6.html#offline-upgrade), a user must initially upgrade to Gerrit `v3.5.2`, and then upgrade to `v3.6.1`. Therefore, define the `gerrit-operator.gerrit.version=3.5.2` value in the edp-install `values.yaml` file.
   
   * Two more components are available with the new functionality:

     * [`edp-argocd-operator`](./argocd-integration.md)
     * [`external-secrets`](./external-secrets-operator-integration.md)

   * If there is no need to use these new operators, define `false` values for them in the existing `value.yaml` file:

      <details>
      <summary><b>View: values.yaml</b></summary>

      ```yaml

      gerrit-operator:
        gerrit:
          version: "3.5.2"
      externalSecrets:
        enabled: false
      argocd:
        enabled: false

      ```

      </details>

4. The `edp-jenkins-role` is renamed to the `jenkins-resources-role`. Delete the `edp-jenkins-role` with the following command:

        kubectl delete role edp-jenkins-role -n <edp-namespace>

  The `jenkins-resources-role` role will be created automatically while EDP upgrade.

5. Recreate the `edp-jenkins-resources-permissions` RoleBinding according to the following template:
    <details>
    <summary><b>View: jenkins-resources-role</b></summary>

      ```yaml
         apiVersion: rbac.authorization.k8s.io/v1
         kind: RoleBinding
         metadata:
          name: edp-jenkins-resources-permissions
          namespace: <edp-namespace>
         roleRef:
          apiGroup: rbac.authorization.k8s.io
          kind: Role
          name: jenkins-resources-role

      ```

      </details>

6. To upgrade EDP to the v.2.12.x, run the following command:

      helm upgrade edp epamedp/edp-install -n <edp-namespace> --values values.yaml --version=2.12.x

  !!! Note
      To verify the installation, it is possible to test the deployment before applying it to the cluster with the following command:<br>
      `helm upgrade edp epamedp/edp-install -n <edp-namespace> --values values.yaml --version=2.12.x  --dry-run`

5. After the update, please remove the `gerrit-operator.gerrit.version` value. In this case, the default value will be used, and Gerrit will be updated to the `v3.6.1` version. Run the following command:

        helm upgrade edp epamedp/edp-install -n <edp-namespace> --values values.yaml --version=2.12.x

  !!! Note
      To verify the installation, it is possible to test the deployment before applying it to the cluster with the following command:<br>
      `helm upgrade edp epamedp/edp-install -n <edp-namespace> --values values.yaml --version=2.12.x  --dry-run`

6. Update image versions for the Jenkins agents in the *ConfigMap*:

        kubectl edit configmap jenkins-slaves -n <edp-namespace>

   * The versions of the images must be the following:

        epamedp/edp-jenkins-codenarc-agent:3.0.8
        epamedp/edp-jenkins-dotnet-21-agent:3.0.7
        epamedp/edp-jenkins-dotnet-31-agent:3.0.7
        epamedp/edp-jenkins-go-agent:3.0.11
        epamedp/edp-jenkins-gradle-java11-agent:3.0.5
        epamedp/edp-jenkins-gradle-java8-agent:3.0.5
        epamedp/edp-jenkins-helm-agent:3.0.8
        epamedp/edp-jenkins-maven-java11-agent:3.0.6
        epamedp/edp-jenkins-maven-java8-agent:3.0.6
        epamedp/edp-jenkins-npm-agent:3.0.7
        epamedp/edp-jenkins-opa-agent:3.0.5
        epamedp/edp-jenkins-python-38-agent:3.0.5
        epamedp/edp-jenkins-terraform-agent:3.0.6

   * Add Jenkins agents by following the template:

    <details>
    <summary><b>View: jenkins-slaves</b></summary>

    ```yaml
      sast-template: |
        <org.csanchez.jenkins.plugins.kubernetes.PodTemplate>
        <inheritFrom></inheritFrom>
        <name>sast</name>
        <namespace></namespace>
        <privileged>false</privileged>
        <alwaysPullImage>false</alwaysPullImage>
        <instanceCap>2147483647</instanceCap>
        <slaveConnectTimeout>100</slaveConnectTimeout>
        <idleMinutes>5</idleMinutes>
        <activeDeadlineSeconds>0</activeDeadlineSeconds>
        <label>sast</label>
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
            <image>epamedp/edp-jenkins-sast-agent:0.1.3</image>
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

   * If required, update the requests and limits for the following Jenkins agents:

     - `edp-jenkins-codenarc-agent`
     - `edp-jenkins-go-agent`
     - `edp-jenkins-gradle-java11-agent`
     - `edp-jenkins-gradle-java8-agent`
     - `edp-jenkins-maven-java11-agent`
     - `edp-jenkins-maven-java8-agent`
     - `edp-jenkins-npm-agent`
     - `edp-jenkins-dotnet-21-agent`
     - `edp-jenkins-dotnet-31-agent`

    EDP requires to start with the following values:

    <details>
    <summary><b>View: jenkins-slaves</b></summary>

    ```yaml
      <resourceRequestCpu>500m</resourceRequestCpu>
      <resourceRequestMemory>1Gi</resourceRequestMemory>
      <resourceLimitCpu>2</resourceLimitCpu>
      <resourceLimitMemory>5Gi</resourceLimitMemory>

    ```

    </details>

   * Restart the Jenkins pod.

7. Update Jenkins provisioners according to the [Manage Jenkins CI Pipeline Job Provisioner](../operator-guide/manage-jenkins-ci-job-provision.md) instruction.

8. Restart the `codebase-operator`, to recreate the Code Review and Build pipelines for the codebases.

!!! Warning
    In case there are different EDP versions on one cluster, the following error may occur on the `init` stage of Jenkins Groovy pipeline: `java.lang.NumberFormatException: For input string: ""`. To fix this issue, please run the following command using [`kubectl` v1.24.4+](https://github.com/kubernetes/kubernetes/blob/master/CHANGELOG/CHANGELOG-1.24.md):

        kubectl patch codebasebranches.v2.edp.epam.com <codebase-branch-name>  -n <edp-namespace>  '--subresource=status' '--type=merge' -p '{"status": {"build": "0"}}'