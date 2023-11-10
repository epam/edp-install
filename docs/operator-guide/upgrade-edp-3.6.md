# Upgrade EDP v3.5 to 3.6

!!! Important
    We suggest backing up the EDP environment before starting the upgrade procedure.

This section provides detailed instructions for upgrading the EPAM Delivery Platform to version 3.6.0. Follow the steps and requirements outlined below:

1. Update Custom Resource Definitions (CRDs). Run the following command to apply all the necessary CRDs to the cluster:

  ```bash
  kubectl apply -f https://raw.githubusercontent.com/epam/edp-codebase-operator/v2.20.0/deploy-templates/crds/v2.edp.epam.com_codebases.yaml
  ```

2. Familiarize yourself with the updated structure of the [values.yaml](https://github.com/epam/edp-install/blob/v3.6.0/deploy-templates/values.yaml#L38) file and adjust it accordingly:

  2.1 A new parameter called [space](https://github.com/epam/edp-install/blob/v3.6.0/deploy-templates/values.yaml#L38) has been added to the `DockerRegistry` section. It is designed to form URLs in CodebaseImageStreams. This parameter is set the same as the `EPAM Delivery Platform` namespace name. Ensure you define the `space` parameter prior to the update.

  !!! warning
      This [parameter](https://github.com/epam/edp-install/blob/v3.6.0/deploy-templates/values.yaml#L38) is a significant change and must be set before the update.

  ```yaml
  global:
    dockerRegistry:
      type: "harbor"
      url: "registry.example.com"
      space: "edp"
  ```

  2.2 Subcomponents, such as [sonar-operator](https://github.com/epam/edp-sonar-operator), [nexus-operator](https://github.com/epam/edp-nexus-operator), and [keycloak-operator](https://github.com/epam/edp-keycloak-operator), have been removed since dependencies are no longer provisioned by the [edp-install](https://github.com/epam/edp-install/blob/v3.6.0/deploy-templates/values.yaml) Helm Chart. To install and integrate shared components with EDP, please use the [edp-cluster-add-ons](https://github.com/epam/edp-cluster-add-ons) approach or refer to the [SonarQube Integration](sonarqube.md) and [Nexus Sonatype Integration](nexus-sonatype.md) documentation pages.

  2.3 The Argo CD integration dependency has been deleted as now we implement it using [edp-cluster-add-ons](https://github.com/epam/edp-cluster-add-ons) approach. To install and integrate Argo CD as a shared component, use the [edp-cluster-add-ons](https://github.com/epam/edp-cluster-add-ons) approach.

  2.4 The handling of secrets for stages namespaces in the [cd-pipeline-operator](https://github.com/epam/edp-cd-pipeline-operator/blob/v2.17.0/deploy-templates/values.yaml#L102) has been updated. The parameter `manageSecrets` has been replaced with `secretManager`. If your environment previously utilized this parameter, manually modify it from `manageSecrets: true` to `secretManager: own`. Otherwise, set it to `secretManager: none`:

  ```yaml
  cd-pipeline-operator:
    # -- flag that indicates whether the operator should manage secrets for stages;
    # own - just copy secrets;
    # eso - secrete will be managed by External Secrets Operator(operator should be installed in the cluster);
    # none - not enable secrets management logic;
    secretManager: none
  ```

3. To upgrade EDP to the v3.6.0, run the following command:

  ```bash
  helm upgrade edp epamedp/edp-install -n edp --values values.yaml --version=3.6.0
  ```

  !!! Note
      To verify the installation, it is possible to test the deployment before applying it to the cluster with the `--dry-run` tag:<br>
      `helm upgrade edp epamedp/edp-install -n edp --values values.yaml --version=3.6.0 --dry-run`
