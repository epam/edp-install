# Upgrade EDP v3.2 to 3.3

!!! Important
    We suggest making a backup of the EDP environment before starting the upgrade procedure.

!!! Note
    We currently disabled cache volumes for go and npm in the EDP 3.3 release.

This section provides the details on the EDP upgrade to v3.3.0. Explore the actions and requirements below.

1. Update Custom Resource Definitions (CRDs). Run the following command to apply all necessary CRDs to the cluster:

  ```bash
  kubectl apply -f https://raw.githubusercontent.com/epam/edp-codebase-operator/v2.16.0/deploy-templates/crds/v2.edp.epam.com_codebases.yaml
  kubectl apply -f https://raw.githubusercontent.com/epam/edp-jenkins-operator/v2.15.0/deploy-templates/crds/v2.edp.epam.com_jenkins.yaml
  ```

2. If you use Gerrit VCS, delete the corresponding resource due to changes in annotations:

  ```bash
  kubectl -n edp delete EDPComponent gerrit
  ```
  The deployment will create a new EDPComponent called `gerrit` instead.

3. To upgrade EDP to the v3.3.0, run the following command:

  ```bash
  helm upgrade edp epamedp/edp-install -n edp --values values.yaml --version=3.3.0
  ```

  !!! Note
      To verify the installation, it is possible to test the deployment before applying it to the cluster with the `--dry-run` tag:<br>
      `helm upgrade edp epamedp/edp-install -n edp --values values.yaml --version=3.3.0  --dry-run`

4. In EDP v3.3.0, a new feature was introduced allowing manual pipeline re-triggering by sending a comment with `/recheck`.
To enable the re-trigger feature for applications that were added before the upgrade, please proceed with the following:

  4.1 For Gerrit VCS, add the following event to the `webhooks.config` configuration file in the `All-Projects` repository:

    ```
    [remote "commentadded"]
      url = http://el-gerrit-listener:8080
      event = comment-added
    ```

  4.2 For GitHub VCS, check the `Issue comments` permission for each webhook in every application added before the EDP upgrade to 3.3.0.

  4.3 For GitLab VCS, check the `Comments` permission for each webhook in every application added before the EDP upgrade to 3.3.0.
