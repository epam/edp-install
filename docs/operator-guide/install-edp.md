# Install EDP

Inspect the main steps to install EPAM Delivery Platform. Please check the prerequisites section before starting the installation.
There are two ways to deploy EPAM Delivery Platform: using Helm (see below) and using [Helmfile](./install-via-helmfile.md#deploy-epam-delivery-platform).

!!! note
    The installation process below is given for a Kubernetes cluster. The steps that differ for an OpenShift cluster are
    indicated in the notes.

!!! note
    &#8249;edp-project&#8250; is the name of the EDP tenant in all the following steps.

1. Create an &#8249;edp-project&#8250; namespace or a Kiosk space depending on whether Kiosk is used or not.

  * Without Kiosk, create a namespace:

        kubectl create namespace <edp-project>

    !!! note
        For an OpenShift cluster, run the `oc` command instead of `kubectl` one.

  * With Kiosk, create a relevant space:

        apiVersion: tenancy.kiosk.sh/v1alpha1
        kind: Space
        metadata:
          name: <edp-project>
        spec:
          account: <edp-project>-admin

  !!! note
      Kiosk is mandatory for EDP v.2.8.x. It is not implemented for the previous versions, and is optional for EDP since v.2.9.x.

  To store EDP data, use any existing Postgres database or create one during the installation.
  Additionally, create two secrets in the &#8249;edp-project&#8250; namespace: one with administrative credentials and another with credentials for the EDP tenant (database schema).

2. For EDP, it is required to have Keycloak access to perform the integration. Create a secret with user and password provisioned in the step 2 of the [Keycloak Configuration](./install-keycloak.md#configuration) section.

      kubectl -n <edp-project> create secret generic keycloak \
        --from-literal=username=<username> \
        --from-literal=password=<password>

3. Add the Helm EPAMEDP Charts for local client.

      helm repo add epamedp https://epam.github.io/edp-helm-charts/stable

4. Choose the required Helm chart version:

      helm search repo epamedp/edp-install
      NAME                    CHART VERSION   APP VERSION     DESCRIPTION
      epamedp/edp-install     2.12.1          2.12.1          A Helm chart for EDP Install

  !!! note
      It is highly recommended to use the latest released version.

5. Check the parameters in the EDP installation chart. For details, please refer to the [values.yaml](https://github.com/epam/edp-install/blob/master/deploy-templates/values.yaml) file.

6. With the external database, set the global.database.host value to the database DNS name accessible from the &#8249;edp-project&#8250; namespace.

7. Install EDP in the &#8249;edp-project&#8250; namespace with the helm tool.

      helm install edp epamedp/edp-install --wait --timeout=900s \
      --version <edp_version> \
      --values values.yaml \
      --namespace <edp-project>

See the details on parameters below:

<details>
<summary><b>View: values.yaml</b></summary>

```yaml

global:

  # Name of the <edp-project> EDP namespace that was previously defined;
  edpName: <edp-project>

  # DNS wildcard for routing in the Kubernetes cluster;
  dnsWildCard: <DNS_wilcdard>

  # Enable or disable integration with Kiosk (by default the value is true)
  kioskEnabled: <true/false>

  # Kubernetes API server;
  webConsole:
    url: <kubeconfig.clusters.cluster.server>

  # set platform type: OpenShift or kubernetes;
  platform: <platform_type>

  # Administrators of the tenant separated by comma (,) e.g. user@example.com;
  admins: [user1@example.com,user2@example.com]

  # Developers of the tenant separated by comma (,) e.g. user@example.com;
  developers: [user1@example.com,user2@example.com]

# AWS Region, e.g. "eu-central-1"
awsRegion:

keycloak-operator:
  keycloak:
    # URL to Keycloak;
    url: <keycloak_endpoint>

dockerRegistry:
  enabled: true
  # URL to Docker registry e.g. <aws_account_id>.dkr.ecr.<region>.amazonaws.com;
  url: <aws_account_id>.dkr.ecr.<region>.amazonaws.com

gerrit-operator:
  gerrit:
    # free Nodeport;
    sshPort: <gerrit_ssh_port>
kaniko:
  # AWS IAM role with push access to ECR, e.g. arn:aws:iam::<AWS_ACCOUNT_ID>:role/<AWS_IAM_ROLE_NAME>
  roleArn:

```

</details>

!!! note
    Set `global.platform=openshift` while deploying EDP in OpenShift.

!!! info
    The full installation with integration between tools will take at least 10 minutes.

## Next Steps

Consult [VCS integration](./import-strategy.md) section, if it is necessary to integrate [GitLab](./gitlab-integration.md) or [GitHub](./github-integration.md) with EDP.

## Related Articles

* [Enable VCS Import Strategy](./import-strategy.md)
* [GitHub Integration](http://localhost:8000/edp-install/operator-guide/github-integration/)
* [GitLab Integration](http://localhost:8000/edp-install/operator-guide/gitlab-integration/)
* [Install Keycloak](http://localhost:8000/edp-install/operator-guide/install-keycloak/)
* [Set Up Kubernetes](kubernetes-cluster-settings.md)
* [Set Up OpenShift](openshift-cluster-settings.md)
