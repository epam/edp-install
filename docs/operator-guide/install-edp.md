# Install EDP

Inspect the main steps to install EPAM Delivery Platform. Please check the [Prerequisites Overview](prerequisites.md) page before starting the installation. Also to authenticate each of the release artifacts, please refer to the  [Verification of EDP Artifacts](artifacts-verification.md) guide.
There are two recommended ways to deploy EPAM Delivery Platform:

* Using Helm (see below);
* Using [Argo CD GitOps approach](./add-ons-overview.md);
* Using [AWS Marketplace](./aws-marketplace-install.md).

!!! note
    The installation process below is given for a Kubernetes cluster. The steps that differ for an OpenShift cluster are
    indicated in the notes.

!!! Disclaimer
    EDP is aligned with industry standards for storing and managing sensitive data, ensuring optimal security. However, the use of custom solutions introduces uncertainties, thus the responsibility for the safety of your data is totally covered by platform administrator.

1. EDP manages secrets via External Secret Operator to integrate with a multitude of utilities. For insights into the secrets in use and their utilization, refer to the provided [External Secrets Operator Integration](./external-secrets-operator-integration.md).

2. Create an edp namespace or a Kiosk space depending on whether [Kiosk](./edp-kiosk-usage.md) is used or not.

  * Without Kiosk, create a namespace:

    ```bash
    kubectl create namespace edp
    ```

    !!! note
        For an OpenShift cluster, run the `oc` command instead of the `kubectl` one.

  * With Kiosk, create a relevant space:

    ```yaml
    apiVersion: tenancy.kiosk.sh/v1alpha1
    kind: Space
    metadata:
      name: edp
    spec:
      account: edp-admin
    ```

  !!! note
      Kiosk is mandatory for EDP v.2.8.x. It is not implemented for the previous versions, and is optional for EDP since v.2.9.x.

3. (Optional) Deploy and configure Keycloak to enable Single Sign-On approach. To see the details on how to configure Keycloak correctly, please refer to the [Install Keycloak](./install-keycloak.md#configuration) page.

4. Add the Helm charts repository:

  ```bash
  helm repo add epamedp https://epam.github.io/edp-helm-charts/stable
  ```

5. Choose the required Helm chart version:

  ```bash
  helm search repo epamedp/edp-install
  NAME                    CHART VERSION   APP VERSION     DESCRIPTION
  epamedp/edp-install     3.9.0           3.9.0           A Helm chart for EDP Install
  ```

  !!! note
      It is highly recommended to use the latest released version.

6. EDP can be integrated with the following version control systems:

  * [Gerrit](https://gerrit-review.googlesource.com/Documentation/)
  * [GitHub](https://docs.github.com/en) (by default)
  * [GitLab](https://docs.gitlab.com/)

  This integration implies in what system the development of the application will be or is already being carried out. The `global.gitProviders` flag in the edp-install controls this integration:

  Global VCS configuration (can be multimple values):

  ``` yaml title="values.yaml"
  ...
  global:
    gitProviders:
      - gerrit
      - github
      - gitlab
  ...
  ```

  Tekton event listner configuration:

  === "GitHub"

      ``` yaml title="values.yaml"
      ...
      edp-tekton:
        gitServers:
          github:
            gitProvider: github
            host: github.com
            webhook:
              skipWebhookSSLVerification: false
            eventListener:
              enabled: true
              resources:
                requests:
                  memory: "64Mi"
                  cpu: "50m"
                limits:
                  memory: "128Mi"
                  cpu: "500m"
              ingress:
                enabled: true
      ...
      ```

  === "GitLab"

      ``` yaml title="values.yaml"
      ...
      edp-tekton:
        gitServers:
          gitlab:
            gitProvider: gitlab
            host: gitlab.com
            webhook:
              skipWebhookSSLVerification: false
            eventListener:
              enabled: true
              resources:
                requests:
                  memory: "64Mi"
                  cpu: "50m"
                limits:
                  memory: "128Mi"
                  cpu: "500m"
              ingress:
                enabled: true
      ...
      ```
  === "Gerrit"

      ``` yaml title="values.yaml"
      ...
      edp-tekton:
        gitServers:
          gerrit:
            eventListener:
              enabled: true
              ingress:
                enabled: false
              resources:
                limits:
                  cpu: 500m
                  memory: 128Mi
                requests:
                  cpu: 50m
                  memory: 64Mi
            gitProvider: gerrit
            gitUser: edp-ci
            host: gerrit.edp
            nameSshKeySecret: gerrit-ciuser-sshkey
            quickLink:
              host: gerrit.example.com
            sshPort: <gerrit_port>
            webhook:
              skipWebhookSSLVerification: false
      ...
      ```

  Internal Gerrit server can be deployed as a result of EDP deployment when the Gerrit is selected. For more details on how to integrate EDP with GitLab or GitHub instead of Gerrit, please refer to the [Integrate GitHub/GitLab in Tekton](./import-strategy-tekton.md) page.

7. (Optional) Integrate platform with SonarQube:

  * External SonarQube - any SonarQube that is installed separately from EDP. For example, SonarQube that is installed using [edp-cluster-add-ons](https://github.com/epam/edp-cluster-add-ons) or another public SonarQube server. For more details on how EDP recommends to configure SonarQube to work with the platform, please refer to the [SonarQube Integration](./sonarqube.md) page.

  * Internal SonarQube - SonarQube that is installed along with EDP.

      ``` yaml title="values.yaml"
      ...
      sonar-operator:
        enabled: true
      ...
      ```

8. (Optional) Integrate platform with Nexus:

  * External Nexus - any Nexus that is installed separately from EDP. For example, Nexus that installed using [edp-cluster-add-ons](https://github.com/epam/edp-cluster-add-ons/blob/main/chart/values.yaml#L82) or another public Nexus server. For more details on how EDP recommends to configure Nexus to work with the platform, please refer to the [Nexus Sonatype Integration](./nexus-sonatype.md) page.

  * Internal Nexus - Nexus that is installed along with EDP.

      ``` yaml title="values.yaml"
      ...
      nexus-operator:
        enabled: true
      ...
      ```

9. (Optional) Configure Container Registry for image storage.

  Since EDP v3.4.0, we enabled users to configure Harbor registry instead of AWS ECR and Openshift-registry. We recommend installing Harbor using our [edp-cluster-add-ons](https://github.com/epam/edp-cluster-add-ons/blob/main/chart/values.yaml#L53) although you can install it any other way. To integrate EDP with Harbor, see [Harbor integration](container-registry-harbor-integration-tekton-ci.md) page.

  To enable Harbor as a registry storage, use the values below:

  ```yaml
  global:
    dockerRegistry:
      type: "harbor"
      url: "harbor.example.com"
  ```

10. Check the parameters in the EDP installation chart. For details, please refer to the [values.yaml](https://github.com/epam/edp-install/blob/v3.8.1/deploy-templates/values.yaml) file.

11. Install EDP in the **edp** namespace with the Helm tool:

  ```bash
  helm install edp epamedp/edp-install --wait --timeout=900s \
  --version <edp_version> \
  --values values.yaml \
  --namespace edp
  ```

  See the details on the parameters below:<a name="values"></a>

  ``` yaml title="Example values.yaml file"
  global:
    # -- platform type that can be either "kubernetes" or "openshift"
    platform: "kubernetes"
    # DNS wildcard for routing in the Kubernetes cluster;
    dnsWildCard: "example.com"
    # -- Administrators of your tenant
    # -- Can be gerrit, github or gitlab. By default: github
    gitProviders:
      - github
    dockerRegistry:
      # -- Docker Registry endpoint
      url: "<AWS_ACCOUNT_ID>.dkr.ecr.<AWS_REGION>.amazonaws.com"
      type: "ecr"

  sso:
    enabled: false
    # Keycloak address with which the platform will be integrated
    keycloakUrl: "https://keycloak.example.com"
    admins:
      - "stub_user_one@example.com"
    developers:
      - "stub_user_one@example.com"
      - "stub_user_two@example.com"

  # AWS Region, e.g. "eu-central-1"
  awsRegion:

  edp-tekton:
    # Tekton Kaniko configuration section
    kaniko:
      # -- AWS IAM role to be used for kaniko pod service account (IRSA). Format: arn:aws:iam::<AWS_ACCOUNT_ID>:role/<AWS_IAM_ROLE_NAME>
      roleArn:
    gitServers:
      github:
        gitProvider: github
        host: github.com
        webhook:
          skipWebhookSSLVerification: false
        eventListener:
          enabled: true
          resources:
            requests:
              memory: "64Mi"
              cpu: "50m"
            limits:
              memory: "128Mi"
              cpu: "500m"
          ingress:
            enabled: true

  edp-headlamp:
    config:
      oidc:
        enabled: false
  ```

  !!! note
      Set `global.platform=openshift` while deploying EDP in OpenShift.

  !!! info
      The full installation with integration between tools will take at least 10 minutes.

12. To check if the installation is successful, run the command below:

    ```bash
    helm status edp -n edp
    ```

    You can also check ingress endpoints to get EDP Portal endpoint to enter EDP Portal UI:

    ```bash
    kubectl describe ingress -n edp
    ```

13. Once EDP is successfully installed, you can navigate to our [Use Cases](../use-cases/index.md) to try out EDP functionality.

## Related Articles

* [Quick Start](../quick-start/platform-installation.md)
* [Integrate GitHub/GitLab in Tekton](../operator-guide/import-strategy-tekton.md)
* [Set Up Kubernetes](kubernetes-cluster-settings.md)
* [Set Up OpenShift](openshift-cluster-settings.md)
* [EDP Installation Prerequisites Overview](prerequisites.md)
* [Headlamp OIDC Integration](headlamp-oidc.md)
* [Verification of EDP Artifacts](artifacts-verification.md)
