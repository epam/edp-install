# Upgrade EDP v3.1 to 3.2

!!! Important
    We suggest making a backup of the EDP environment before starting the upgrade procedure.

This section provides the details on the EDP upgrade to v3.2.2. Explore the actions and requirements below.

1. Update Custom Resource Definitions (CRDs). Run the following command to apply all necessary CRDs to the cluster:

  ```
  kubectl apply -f https://raw.githubusercontent.com/epam/edp-codebase-operator/v2.15.0/deploy-templates/crds/v2.edp.epam.com_cdstagedeployments.yaml
  kubectl apply -f https://raw.githubusercontent.com/epam/edp-codebase-operator/v2.15.0/deploy-templates/crds/v2.edp.epam.com_codebasebranches.yaml
  kubectl apply -f https://raw.githubusercontent.com/epam/edp-codebase-operator/v2.15.0/deploy-templates/crds/v2.edp.epam.com_codebaseimagestreams.yaml
  kubectl apply -f https://raw.githubusercontent.com/epam/edp-codebase-operator/v2.15.0/deploy-templates/crds/v2.edp.epam.com_codebases.yaml
  kubectl apply -f https://raw.githubusercontent.com/epam/edp-codebase-operator/v2.15.0/deploy-templates/crds/v2.edp.epam.com_gitservers.yaml
  kubectl apply -f https://raw.githubusercontent.com/epam/edp-codebase-operator/v2.15.0/deploy-templates/crds/v2.edp.epam.com_gittags.yaml
  kubectl apply -f https://raw.githubusercontent.com/epam/edp-codebase-operator/v2.15.0/deploy-templates/crds/v2.edp.epam.com_imagestreamtags.yaml
  kubectl apply -f https://raw.githubusercontent.com/epam/edp-codebase-operator/v2.15.0/deploy-templates/crds/v2.edp.epam.com_jiraissuemetadatas.yaml
  kubectl apply -f https://raw.githubusercontent.com/epam/edp-codebase-operator/v2.15.0/deploy-templates/crds/v2.edp.epam.com_jiraservers.yaml
  kubectl apply -f https://raw.githubusercontent.com/epam/edp-jenkins-operator/v2.14.0/deploy-templates/crds/v2.edp.epam.com_cdstagejenkinsdeployments.yaml
  kubectl apply -f https://raw.githubusercontent.com/epam/edp-jenkins-operator/v2.14.0/deploy-templates/crds/v2.edp.epam.com_jenkins.yaml
  kubectl apply -f https://raw.githubusercontent.com/epam/edp-jenkins-operator/v2.14.0/deploy-templates/crds/v2.edp.epam.com_jenkinsagents.yaml
  kubectl apply -f https://raw.githubusercontent.com/epam/edp-jenkins-operator/v2.14.0/deploy-templates/crds/v2.edp.epam.com_jenkinsauthorizationrolemappings.yaml
  kubectl apply -f https://raw.githubusercontent.com/epam/edp-jenkins-operator/v2.14.0/deploy-templates/crds/v2.edp.epam.com_jenkinsauthorizationroles.yaml
  kubectl apply -f https://raw.githubusercontent.com/epam/edp-jenkins-operator/v2.14.0/deploy-templates/crds/v2.edp.epam.com_jenkinsfolders.yaml
  kubectl apply -f https://raw.githubusercontent.com/epam/edp-jenkins-operator/v2.14.0/deploy-templates/crds/v2.edp.epam.com_jenkinsjobbuildruns.yaml
  kubectl apply -f https://raw.githubusercontent.com/epam/edp-jenkins-operator/v2.14.0/deploy-templates/crds/v2.edp.epam.com_jenkinsjobs.yaml
  kubectl apply -f https://raw.githubusercontent.com/epam/edp-jenkins-operator/v2.14.0/deploy-templates/crds/v2.edp.epam.com_jenkinsscripts.yaml
  kubectl apply -f https://raw.githubusercontent.com/epam/edp-jenkins-operator/v2.14.0/deploy-templates/crds/v2.edp.epam.com_jenkinsserviceaccounts.yaml
  kubectl apply -f https://raw.githubusercontent.com/epam/edp-jenkins-operator/v2.14.0/deploy-templates/crds/v2.edp.epam.com_jenkinssharedlibraries.yaml
  kubectl apply -f https://raw.githubusercontent.com/epam/edp-component-operator/v0.13.0/deploy-templates/crds/v1.edp.epam.com_edpcomponents.yaml
  kubectl apply -f https://raw.githubusercontent.com/epam/edp-cd-pipeline-operator/v2.14.1/deploy-templates/crds/v2.edp.epam.com_cdpipelines.yaml
  kubectl apply -f https://raw.githubusercontent.com/epam/edp-cd-pipeline-operator/v2.14.1/deploy-templates/crds/v2.edp.epam.com_stages.yaml
  kubectl apply -f https://raw.githubusercontent.com/epam/edp-nexus-operator/v2.14.1/deploy-templates/crds/v2.edp.epam.com_nexuses.yaml
  kubectl apply -f https://raw.githubusercontent.com/epam/edp-nexus-operator/v2.14.1/deploy-templates/crds/v2.edp.epam.com_nexususers.yaml
  kubectl apply -f https://raw.githubusercontent.com/epam/edp-sonar-operator/v2.14.0/deploy-templates/crds/v2.edp.epam.com_sonargroups.yaml
  kubectl apply -f https://raw.githubusercontent.com/epam/edp-sonar-operator/v2.14.0/deploy-templates/crds/v2.edp.epam.com_sonarpermissiontemplates.yaml
  kubectl apply -f https://raw.githubusercontent.com/epam/edp-sonar-operator/v2.14.0/deploy-templates/crds/v2.edp.epam.com_sonars.yaml
  kubectl apply -f https://raw.githubusercontent.com/epam/edp-gerrit-operator/v2.14.0/deploy-templates/crds/v2.edp.epam.com_gerritgroupmembers.yaml
  kubectl apply -f https://raw.githubusercontent.com/epam/edp-gerrit-operator/v2.14.0/deploy-templates/crds/v2.edp.epam.com_gerritgroups.yaml
  kubectl apply -f https://raw.githubusercontent.com/epam/edp-gerrit-operator/v2.14.0/deploy-templates/crds/v2.edp.epam.com_gerritmergerequests.yaml
  kubectl apply -f https://raw.githubusercontent.com/epam/edp-gerrit-operator/v2.14.0/deploy-templates/crds/v2.edp.epam.com_gerritprojectaccesses.yaml
  kubectl apply -f https://raw.githubusercontent.com/epam/edp-gerrit-operator/v2.14.0/deploy-templates/crds/v2.edp.epam.com_gerritprojects.yaml
  kubectl apply -f https://raw.githubusercontent.com/epam/edp-gerrit-operator/v2.14.0/deploy-templates/crds/v2.edp.epam.com_gerritreplicationconfigs.yaml
  kubectl apply -f https://raw.githubusercontent.com/epam/edp-gerrit-operator/v2.14.0/deploy-templates/crds/v2.edp.epam.com_gerrits.yaml
  kubectl apply -f https://raw.githubusercontent.com/epam/edp-perf-operator/v2.13.0/deploy-templates/crds/v2.edp.epam.com_perfdatasourcegitlabs.yaml
  kubectl apply -f https://raw.githubusercontent.com/epam/edp-perf-operator/v2.13.0/deploy-templates/crds/v2.edp.epam.com_perfdatasourcejenkinses.yaml
  kubectl apply -f https://raw.githubusercontent.com/epam/edp-perf-operator/v2.13.0/deploy-templates/crds/v2.edp.epam.com_perfdatasourcesonars.yaml
  kubectl apply -f https://raw.githubusercontent.com/epam/edp-perf-operator/v2.13.0/deploy-templates/crds/v2.edp.epam.com_perfservers.yaml
  ```

2. Generate a cookie-secret for proxy with the following command:

  ```bash
  nexus_proxy_cookie_secret=$(openssl rand -base64 32 | head -c 32)
  ```
  Create `nexus-proxy-cookie-secret` in the <edp-project> namespace:

  ```bash
  kubectl -n <edp-project> create secret generic nexus-proxy-cookie-secret \
      --from-literal=cookie-secret=${nexus_proxy_cookie_secret}
  ```

4. EDP 3.2.2 features OIDC configuration for Headlamp. If this parameter is required, create `keycloak-client-headlamp-secret` as described in this [article](headlamp-oidc.md):

  ```bash
  kubectl -n <edp-project> create secret generic keycloak-client-headlamp-secret \
      --from-literal=clientSecret=<keycloak_client_secret_key>
  ```

5. Delete the following resources:

  ```
  kubectl -n <edp-project> delete KeycloakClient nexus
  kubectl -n <edp-project> delete EDPComponent nexus
  kubectl -n <edp-project> delete Ingress nexus
  kubectl -n <edp-project> delete deployment edp-tekton-dashboard
  ```

6. EDP release 3.2.2 uses the default cluster storageClass and we must check previous storageClass parameters. Align , if required, the `storageClassName` in EDP `values.yaml` to the same that were used by EDP PVC. For example:

  ```yaml
  edp-tekton:
    buildTool:
      go:
        cache:
          persistentVolume:
            # -- Specifies storageClass type. If not specified, a default storageClass for go-cache volume is used
            storageClass: ebs-sc

  jenkins-operator:
    enabled: true
    jenkins:
      storage:
        # -- Storageclass for Jenkins data volume
        class: gp2

  sonar-operator:
    sonar:
      storage:
        data:
          # --  Storageclass for Sonar data volume
          class: gp2
        database:
          # --  Storageclass for database data volume
          class: gp2

  gerrit-operator:
    gerrit:
      storage:
        # --  Storageclass for Gerrit data volume
        class: gp2

  nexus-operator:
    nexus:
      storage:
        # --  Storageclass for Nexus data volume
        class: gp2
  ```

7. To upgrade EDP to the v3.2.2, run the following command:

  ```
  helm upgrade edp epamedp/edp-install -n <edp-namespace> --values values.yaml --version=3.2.2
  ```

  !!! Note
      To verify the installation, it is possible to test the deployment before applying it to the cluster with the following command:<br>
      `helm upgrade edp epamedp/edp-install -n <edp-namespace> --values values.yaml --version=3.2.2  --dry-run`
