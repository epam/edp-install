# Install EDP

Inspect the main steps to install EPAM Delivery Platform. Please check the [Prerequisites Overview](prerequisites.md) page before starting the installation.
There are two ways to deploy EPAM Delivery Platform: using Helm (see below) and using [Helmfile](./install-via-helmfile.md#deploy-epam-delivery-platform).

!!! note
    The installation process below is given for a Kubernetes cluster. The steps that differ for an OpenShift cluster are
    indicated in the notes.

!!! note
    &#8249;edp-project&#8250; is the name of the EDP tenant in all the following steps.

1. Create an &#8249;edp-project&#8250; namespace or a Kiosk space depending on whether [Kiosk](./edp-kiosk-usage.md) is used or not.

  * Without Kiosk, create a namespace:

    ```bash
    kubectl create namespace <edp-project>
    ```

    !!! note
        For an OpenShift cluster, run the `oc` command instead of the `kubectl` one.

  * With Kiosk, create a relevant space:

    ```yaml
    apiVersion: tenancy.kiosk.sh/v1alpha1
    kind: Space
    metadata:
      name: <edp-project>
    spec:
      account: <edp-project>-admin
    ```

  !!! note
      Kiosk is mandatory for EDP v.2.8.x. It is not implemented for the previous versions, and is optional for EDP since v.2.9.x.

2. For EDP, it is required to have Keycloak access to perform the integration. Create a secret with user and password provisioned in the step 2 of the [Keycloak Configuration](./install-keycloak.md#configuration) section.

  ```bash
  kubectl -n <edp-project> create secret generic keycloak \
    --from-literal=username=<username> \
    --from-literal=password=<password>
  ```

3. Generate a cookie-secret for proxy with the following command:

  ```bash
  nexus_proxy_cookie_secret=$(openssl rand -base64 32 | head -c 32)
  ```
  Create `nexus-proxy-cookie-secret` in the <edp-project> namespace:

  ```bash
  kubectl -n <edp-project> create secret generic nexus-proxy-cookie-secret \
      --from-literal=cookie-secret=${nexus_proxy_cookie_secret}
  ```

4. Add the Helm EPAMEDP Charts for local client.

  ```bash
  helm repo add epamedp https://epam.github.io/edp-helm-charts/stable
  ```

5. Choose the required Helm chart version:

  ```bash
  helm search repo epamedp/edp-install
  NAME                    CHART VERSION   APP VERSION     DESCRIPTION
  epamedp/edp-install     3.2.1           3.2.1           A Helm chart for EDP Install
  ```

  !!! note
      It is highly recommended to use the latest released version.

6. By default, EDP uses Tekton as a CI tool (see more in the [Prerequisites Overview](prerequisites.md) page). To use Jenkins instead of Tekton, redefine the following parameters in the [values.yaml](https://github.com/epam/edp-install/blob/master/deploy-templates/values.yaml) file:

  ??? note "View: values.yaml"
      ```yaml
      ...
      edp-tekton:
        enabled: false
      ...
      jenkins-operator:
        enabled: true
      ...
      admin-console-operator:
        enabled: true
      ...
      ```

7. EDP can be integrated with the following version control systems:

  * [Gerrit](https://gerrit-review.googlesource.com/Documentation/) (by default)
  * [GitHub](https://docs.github.com/en)
  * [GitLab](https://docs.gitlab.com/)

  This integration implies in what system the development of the application will be or is already being carried out. The `global.gitProvider` flag in the edp-install controls this integration:

  === "Gerrit (by default)"

      ``` yaml title="values.yaml"
      ...
      global:
        gitProvider: gerrit
      ...
      ```

  === "GitHub"

      ``` yaml title="values.yaml"
      ...
      global:
        gitProvider: github
      ...
      ```

  === "GitLab"

      ``` yaml title="values.yaml"
      ...
      global:
        gitProvider: gitlab
      ...
      ```

  By default, the internal Gerrit server is deployed as a result of EDP deployment. For more details on how to integrate EDP with GitLab or GitHub instead of Gerrit, please refer to the [Enable VCS Import Strategy](./import-strategy.md) article.

8. Check the parameters in the EDP installation chart. For details, please refer to the [values.yaml](https://github.com/epam/edp-install/blob/release/3.2/deploy-templates/values.yaml) file.

9. Install EDP in the &#8249;edp-project&#8250; namespace with the Helm tool.

  ```bash
  helm install edp epamedp/edp-install --wait --timeout=900s \
  --version <edp_version> \
  --values values.yaml \
  --namespace <edp-project>
  ```

  See the details on the parameters below:<a name="values"></a>

  === "Tekton CI tool"

      ``` yaml title="Example values.yaml file"
      global:
        # -- namespace or a project name (in case of OpenShift)
        edpName: <edp-project>
        # -- platform type that can be "kubernetes" or "openshift"
        platform: "kubernetes"
        # DNS wildcard for routing in the Kubernetes cluster;
        dnsWildCard: <DNS_wildcard>
        # -- Administrators of your tenant
        admins:
          - "stub_user_one@example.com"
        # -- Developers of your tenant
        developers:
          - "stub_user_one@example.com"
          - "stub_user_two@example.com"
        # Kubernetes API server;
        webConsole:
          url: <kubeconfig.clusters.cluster.server>
        # -- Can be gerrit, github or gitlab. By default: gerrit
        gitProvider: gerrit
        # -- Gerrit SSH node port
        gerritSSHPort: "22"
        # URL to Keycloak;
        keycloakUrl: <keycloak_endpoint>

      # AWS Region, e.g. "eu-central-1"
      awsRegion:

      argocd:
        # -- Enable ArgoCD integration
        enabled: true
        # -- ArgoCD URL in format schema://URI
        # @default -- `""` (defaults to https://argocd.{{ .Values.global.dnsWildCard }})
        url: ""

      # Kaniko configuration section
      kaniko:
        # -- AWS IAM role to be used for kaniko pod service account (IRSA). Format: arn:aws:iam::<AWS_ACCOUNT_ID>:role/<AWS_IAM_ROLE_NAME>
        roleArn:

      edp-tekton:
        # Tekton Kaniko configuration section
        kaniko:
          # -- AWS IAM role to be used for kaniko pod service account (IRSA). Format: arn:aws:iam::<AWS_ACCOUNT_ID>:role/<AWS_IAM_ROLE_NAME>
          roleArn:

      dockerRegistry:
        # -- Docker Registry endpoint
        url: "<AWS_ACCOUNT_ID>.dkr.ecr.<AWS_REGION>.amazonaws.com"

      perf-operator:
        enabled: false

      edp-headlamp:
        config:
          oidc:
            enabled: false
      ```

  === "Jenkins CI tool"

      ``` yaml title="Example values.yaml file"
      global:
        # -- namespace or a project name (in case of OpenShift)
        edpName: <edp-project>
        # -- platform type that can be "kubernetes" or "openshift"
        platform: "kubernetes"
        # DNS wildcard for routing in the Kubernetes cluster;
        dnsWildCard: <DNS_wildcard>
        # -- Administrators of your tenant
        admins:
          - "stub_user_one@example.com"
        # -- Developers of your tenant
        developers:
          - "stub_user_one@example.com"
          - "stub_user_two@example.com"
        # Kubernetes API server;
        webConsole:
          url: <kubeconfig.clusters.cluster.server>
        # -- Can be gerrit, github or gitlab. By default: gerrit
        gitProvider: gerrit
        # -- Gerrit SSH node port
        gerritSSHPort: "22"
        # URL to Keycloak;
        keycloakUrl: <keycloak_endpoint>

      # AWS Region, e.g. "eu-central-1"
      awsRegion:

      argocd:
        # -- Enable ArgoCD integration
        enabled: false

      # Kaniko configuration section
      kaniko:
        # -- AWS IAM role to be used for kaniko pod service account (IRSA). Format: arn:aws:iam::<AWS_ACCOUNT_ID>:role/AWS_IAM_ROLE_NAME>
        roleArn:

      dockerRegistry:
        # -- Docker Registry endpoint
        url: "<AWS_ACCOUNT_ID>.dkr.ecr.<AWS_REGION>.amazonaws.com"

      jenkins-operator:
        enabled: true

      admin-console-operator:
        enabled: true

      edp-tekton:
        enabled: false

      perf-operator:
        enabled: false

      edp-headlamp:
        config:
          oidc:
            enabled: false
      ```

  !!! note
      Set `global.platform=openshift` while deploying EDP in OpenShift.

  !!! info
      The full installation with integration between tools will take at least 10 minutes.

## Related Articles

* [Enable VCS Import Strategy](./import-strategy.md)
* [GitHub Integration](github-integration.md)
* [GitLab Integration](gitlab-integration.md)
* [Set Up Kubernetes](kubernetes-cluster-settings.md)
* [Set Up OpenShift](openshift-cluster-settings.md)
* [EDP Installation Prerequisites Overview](prerequisites.md)
* [Headlamp OIDC Integration](headlamp-oidc.md)
