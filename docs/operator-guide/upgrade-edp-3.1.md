# Upgrade EDP v3.0 to 3.1

!!! Important
    We suggest making a backup of the EDP environment before starting the upgrade procedure.

This section provides the details on the EDP upgrade to v3.1. Explore the actions and requirements below.

1. Update Custom Resource Definitions (CRDs). Run the following command to apply all necessary CRDs to the cluster:

  ```
  kubectl apply -f https://raw.githubusercontent.com/epam/edp-jenkins-operator/v2.13.2/deploy-templates/crds/v2.edp.epam.com_jenkins.yaml
  kubectl apply -f https://raw.githubusercontent.com/epam/edp-gerrit-operator/v2.13.4/deploy-templates/crds/v2.edp.epam.com_gerrits.yaml
  ```

2. To upgrade EDP to the v3.1, run the following command:

      helm upgrade edp epamedp/edp-install -n <edp-namespace> --values values.yaml --version=3.1.0

  !!! Note
      To verify the installation, it is possible to test the deployment before applying it to the cluster with the following command:<br>
      `helm upgrade edp epamedp/edp-install -n <edp-namespace> --values values.yaml --version=3.1.0  --dry-run`
