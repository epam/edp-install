# Upgrade EDP v3.3 to 3.4

!!! Important
    We suggest making a backup of the EDP environment before starting the upgrade procedure.

!!! Note
    Pay attention that the following components: `perf-operator`, `edp-admin-console`, `edp-admin-console-operator`, and `edp-jenkins-operator` are deprecated and should be additionally migrated in order to avoid their deletion. For migration details, please refer to the [Migrate CI Pipelines From Jenkins to Tekton](migrate-ci-pipelines-from-jenkins-to-tekton.md) instruction.

This section provides the details on the EDP upgrade to v3.4.1. Explore the actions and requirements below.

1. Update Custom Resource Definitions (CRDs). Run the following command to apply all necessary CRDs to the cluster:

  ```
  kubectl apply -f https://raw.githubusercontent.com/epam/edp-cd-pipeline-operator/v2.15.0/deploy-templates/crds/v2.edp.epam.com_cdpipelines.yaml
  kubectl apply -f https://raw.githubusercontent.com/epam/edp-cd-pipeline-operator/v2.15.0/deploy-templates/crds/v2.edp.epam.com_stages.yaml
  kubectl apply -f https://raw.githubusercontent.com/epam/edp-keycloak-operator/v1.17.0/deploy-templates/crds/v1.edp.epam.com_clusterkeycloakrealms.yaml
  kubectl apply -f https://raw.githubusercontent.com/epam/edp-keycloak-operator/v1.17.0/deploy-templates/crds/v1.edp.epam.com_clusterkeycloaks.yaml
  kubectl apply -f https://raw.githubusercontent.com/epam/edp-keycloak-operator/v1.17.0/deploy-templates/crds/v1.edp.epam.com_keycloakauthflows.yaml
  kubectl apply -f https://raw.githubusercontent.com/epam/edp-keycloak-operator/v1.17.0/deploy-templates/crds/v1.edp.epam.com_keycloakclients.yaml
  kubectl apply -f https://raw.githubusercontent.com/epam/edp-keycloak-operator/v1.17.0/deploy-templates/crds/v1.edp.epam.com_keycloakclientscopes.yaml
  kubectl apply -f https://raw.githubusercontent.com/epam/edp-keycloak-operator/v1.17.0/deploy-templates/crds/v1.edp.epam.com_keycloakrealmcomponents.yaml
  kubectl apply -f https://raw.githubusercontent.com/epam/edp-keycloak-operator/v1.17.0/deploy-templates/crds/v1.edp.epam.com_keycloakrealmgroups.yaml
  kubectl apply -f https://raw.githubusercontent.com/epam/edp-keycloak-operator/v1.17.0/deploy-templates/crds/v1.edp.epam.com_keycloakrealmidentityproviders.yaml
  kubectl apply -f https://raw.githubusercontent.com/epam/edp-keycloak-operator/v1.17.0/deploy-templates/crds/v1.edp.epam.com_keycloakrealmrolebatches.yaml
  kubectl apply -f https://raw.githubusercontent.com/epam/edp-keycloak-operator/v1.17.0/deploy-templates/crds/v1.edp.epam.com_keycloakrealmroles.yaml
  kubectl apply -f https://raw.githubusercontent.com/epam/edp-keycloak-operator/v1.17.0/deploy-templates/crds/v1.edp.epam.com_keycloakrealms.yaml
  kubectl apply -f https://raw.githubusercontent.com/epam/edp-keycloak-operator/v1.17.0/deploy-templates/crds/v1.edp.epam.com_keycloakrealmusers.yaml
  kubectl apply -f https://raw.githubusercontent.com/epam/edp-keycloak-operator/v1.17.0/deploy-templates/crds/v1.edp.epam.com_keycloaks.yaml
  kubectl apply -f https://raw.githubusercontent.com/epam/edp-codebase-operator/v2.17.0/deploy-templates/crds/v2.edp.epam.com_templates.yaml
  kubectl apply -f https://raw.githubusercontent.com/epam/edp-codebase-operator/v2.17.0/deploy-templates/crds/v2.edp.epam.com_codebasebranches.yaml
  kubectl apply -f https://raw.githubusercontent.com/epam/edp-codebase-operator/v2.17.0/deploy-templates/crds/v2.edp.epam.com_codebaseimagestreams.yaml
  kubectl apply -f https://raw.githubusercontent.com/epam/edp-codebase-operator/v2.17.0/deploy-templates/crds/v2.edp.epam.com_codebases.yaml
  kubectl apply -f https://raw.githubusercontent.com/epam/edp-codebase-operator/v2.17.0/deploy-templates/crds/v2.edp.epam.com_gitservers.yaml
  kubectl apply -f https://raw.githubusercontent.com/epam/edp-codebase-operator/v2.17.0/deploy-templates/crds/v2.edp.epam.com_jiraservers.yaml
  kubectl apply -f https://raw.githubusercontent.com/epam/edp-gerrit-operator/v2.16.0/deploy-templates/crds/v2.edp.epam.com_gerrits.yaml
  ```


2. Remove deprecated components:

    <details>
    <summary><b>View: values.yaml</b></summary>

    ```yaml
    perf-operator:
      enabled: false
    admin-console-operator:
      enabled: false
    jenkins-operator:
      enabled: false
    ```
    </details>


3. Since the [values.yaml](https://github.com/epam/edp-install/blob/master/deploy-templates/values.yaml#L37) file structure has been modified, move the **dockerRegistry** subsection to the **global** section:

  The dockerRegistry value has been moved to the global section:

    ```yaml
    global:
      dockerRegistry:
        # -- Define Image Registry that will to be used in Pipelines. Can be ecr (default), harbor
        type: "ecr"
        # -- Docker Registry endpoint
        url: "<AWS_ACCOUNT_ID>.dkr.ecr.<AWS_REGION>.amazonaws.com"
    ```


4. (Optional) To integrate EDP with Jira, rename the default values from <epam-jira-user> to <jira-user> for a secret name. In case Jira is already integrated, it will continue working.

        ```yaml
        codebase-operator:
          jira:
            credentialName: "jira-user"
        ```

5. (Optional) To switch to the Harbor registry, change the secret format for the external secret from [kaniko-docker-config v3.3.0](https://raw.githubusercontent.com/epam/edp-install/v3.3.0/deploy-templates/templates/external-secrets/externalsecret-kaniko.yaml) to [kaniko-docker-config v3.4.1](https://raw.githubusercontent.com/epam/edp-install/v3.4.0/deploy-templates/templates/external-secrets/externalsecret-kaniko.yaml):

  <details>
  <summary><b>View: old format</b></summary>

  ```json
   "kaniko-docker-config": {"secret-string"} //base64 format
  ```
  </details>

  <details>
  <summary><b>View: new format</b></summary>
  ```json
  "kaniko-docker-config": {
    "auths" : {
      "registry.com" :
      {"username":"<registry-username>","password":"<registry-password>","auth":"secret-string"}
    }
  }
  ```
  </details>

6. To upgrade EDP to the v3.4.1, run the following command:

  ```bash
  helm upgrade edp epamedp/edp-install -n edp --values values.yaml --version=3.4.1
  ```

  !!! Note
      To verify the installation, it is possible to test the deployment before applying it to the cluster with the `--dry-run` tag:<br>
      `helm upgrade edp epamedp/edp-install -n edp --values values.yaml --version=3.4.1 --dry-run`
