# Install EDP

Inspect the main steps to install EPAM Delivery Platform. Please check the prerequisites section before starting installation.

!!! note
    The installation process below is given for a Kubernetes cluster. The steps that differ for an OpenShift cluster are
    indicated in the notes.

!!! note
    &#8249;edp-project&#8250; is the name of the EDP tenant in all the following steps.

1. Create a &#8249;edp-project&#8250; namespace or a space(Kiosk) depending on whether you use Kiosk or not.

  * If you don't use Kiosk, create a namespace:

        kubectl create namespace <edp-project>

    !!! note
        For an OpenShift cluster, run the `oc` command instead of `kubectl` one.

  * If you use Kiosk, create a space:

        apiVersion: tenancy.kiosk.sh/v1alpha1
        kind: Space
        metadata:
          name: <edp-project>
        spec:
          account: <edp-project>-admin

  !!! note
      Kiosk is mandatory for EDP v.2.8. It is not implemented for the previous versions, and is optional for EDP since v.2.9.

  To store EDP data, use any existing Postgres database or create one during the installation.
  Additionally, create two secrets in the &#8249;edp-project&#8250; namespace: one with administrative credentials and another with credentials for the EDP tenant (database schema).

2. Create a secret for administrative access to the database:

      kubectl -n <edp-project> create secret generic super-admin-db \
        --from-literal=username=<super_admin_db_username> \
        --from-literal=password=<super_admin_db_password>

  !!! warning
      Do not use the **admin** username here since **admin** is a reserved name.

3. Create a secret for an EDP tenant database user.

      kubectl -n <edp-project> create secret generic db-admin-console \
        --from-literal=username=<tenant_db_username> \
        --from-literal=password=<tenant_db_password>

  !!! warning
      Do not use the **admin** username here since **admin** is a reserved name.

4. For EDP, it is required to have Keycloak access to perform the integration. Create a secret with user and password provisioned in the step 2 of the [Keycloak Configuration](./install-keycloak.md#configuration) section.

      kubectl -n <edp-project> create secret generic keycloak \
        --from-literal=username=<username> \
        --from-literal=password=<password>

5. Add the Helm EPAMEDP Charts for local client.

      helm repo add epamedp https://chartmuseum.demo.edp-epam.com/

6. Choose the required Helm chart version:

      helm search repo epamedp/edp-install
      NAME                    CHART VERSION   APP VERSION     DESCRIPTION
      epamedp/edp-install     2.9.0           2.9.0          A Helm chart for EDP Install

  !!! note
      It is highly recommended to use the latest released version.

7. Mind the parameters in the EDP installation chart. For details, please refer to the [values.yaml](https://github.com/epam/edp-install/blob/master/deploy-templates/values.yaml) file.

8. If the external database is used, set the global.database.host value to the database DNS name accessible from the &#8249;edp-project&#8250; namespace.

9. Install EDP in the &#8249;edp-project&#8250; namespace with the helm tool.

      helm install edp epamedp/edp-install --wait --timeout=900s \
      --version <edp_version> \
      --values values.yaml \
      --namespace <edp-project>

See the details on parameters below:

<details>
<summary><b>View: values.yaml</b></summary>

```yaml

global:

  # Name of your <edp-project> EDP namespace that was previously defined;
  edpName: <edp-project>

  # DNS wildcard for routing in your K8S cluster;
  dnsWildCard: <DNS_wilcdard>

  # Enable or disable integration with Kiosk (by default value is true)
  kioskEnabled: <true/false>

  # Kubernetes API server;
  webConsole:
    url: <kubeconfig.clusters.cluster.server>

  # set platform type: openshift or kubernetes;
  platform: <platform_type>

  # Administrators of your tenant separated by comma (,) e.g. user@example.com;
  admins: [user1@example.com,user2@example.com]

  # Developers of your tenant separated by comma (,) e.g. user@example.com;
  developers: [user1@example.com,user2@example.com]

keycloak-operator:
  keycloak:
    # URL to Keycloak;
    url: <keycloak_endpoint>

dockerRegistry:
  enabled: true
  # URL to docker registry e.g. <aws_account_id>.dkr.ecr.<region>.amazonaws.com;
  url: <aws_account_id>.dkr.ecr.<region>.amazonaws.com

gerrit-operator:
  gerrit:
    # Gerrit SSH node port;
    sshPort: <gerrit_ssh_port>

edp:
  # Admin groups of your tenant separated by comma (,) e.g. test-admin-group;
  adminGroups:
    - "<edp-project>-edp-admin"
  # Developer groups of your tenant separated by comma (,) e.g. test-admin-group;
  developerGroups:
    - "<edp-project>-edp-developer"

```

</details>

!!! note
    Set `global.platform=openshift` while deploying EDP in OpenShift.

!!! info
    The full installation with integration between tools will take at least 10 minutes.

## Next Steps

Consult [VCS integration](./import-strategy.md) section, if you plan to integrate [GitLab](./gitlab-integration.md) or [GitHub](./github-integration.md) with EDP.

## Related Articles

* [Enable VCS Import Strategy](./import-strategy.md)
* [GitHub Integration](http://localhost:8000/edp-install/operator-guide/github-integration/)
* [GitLab Integration](http://localhost:8000/edp-install/operator-guide/gitlab-integration/)
* [Install Keycloak](http://localhost:8000/edp-install/operator-guide/install-keycloak/)
* [Set Up Kubernetes](kubernetes-cluster-settings.md)
* [Set Up OpenShift](openshift-cluster-settings.md)
