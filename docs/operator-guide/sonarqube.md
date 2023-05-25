# Custom SonarQube Integration

This documentation guide provides comprehensive instructions for integrating SonarQube with the EPAM Delivery Platform. It offers two scenarios: installing a customized SonarQube alongside EDP or integrating an existing customized SonarQube with the installed EDP.

## Prerequisites

Before proceeding, ensure that you have the following prerequisites:

* A [Kubernetes](kubernetes-cluster-settings.md) or [OpenShift](openshift-cluster-settings.md) cluster is installed and properly configured;
* Custom [SonarQube](https://docs.sonarqube.org/latest/setup-and-upgrade/install-the-server/) is installed and configured.

## Integration Scenarios

By default, EDP comes with its own SonarQube installation, providing powerful code analysis capabilities. However, EDP also offers the flexibility to integrate custom SonarQube instances tailored to specific project requirements.

In order to enable secure authentication and access control, it is necessary to create a SonarQube token. This token serves as a unique identifier and allows seamless integration between SonarQube and EDP.

To generate the SonarQube token, follow the steps below:

1. Open the SonarQube UI and navigate to `Administration` -> `Security` -> `User`. Create a new user or select an existing one. Click the `Options List` icon to create a token:

  !![SonarQube user settings](../assets/operator-guide/sonar-create-user.png "SonarQube user settings")

2. Type the `ci-user` username, define an expiration period, and click the `Generate` button to create the token:

  !![SonarQube create token](../assets/operator-guide/sonar-generate-token.png "SonarQube create token")

3. Click the `Copy` button to copy the generated `<Sonarqube-token>`:

  !![SonarQube token](../assets/operator-guide/sonar-copy-token.png "SonarQube token")

Next, users need to determine the specific scenario that aligns with their requirements.

### Install EDP With Custom SonarQube

This scenario is preferred when users don't have EDP installed but already have their own configured and functional SonarQube instance. In such cases, users simply need to create a secret for SonarQube, replace the `sonarUrl` parameter with the actual SonarQube URL, and disable the default SonarQube configuration. To accomplish this, follow the steps below:

1. Create secret manually or using the `externalSecrets` operator:

  === "External Secrets Operator"

      If you use [External Secrets Operator](install-external-secrets-operator.md), you can leverage the [scenario to create external secret](https://epam.github.io/edp-install/operator-guide/external-secrets-operator-integration/?h=#edp-install-scenario).

      ```yaml
      externalSecrets:
        enabled: true
      ```

  === "Manual Secret"

      If you don't use [External Secrets Operator](install-external-secrets-operator.md), create the `sonar-ciuser-token` secret in the `<edp-project>` namespace.

        ```yaml
        apiVersion: v1
        kind: Secret
        metadata:
          name: sonar-ciuser-token
          namespace: <edp-project>
        data:
          secret: <Sonarqube-token>
          username: username
        type: Opaque
        ```

2. To use custom SonarQube in EDP, redefine the following parameters in the [values.yaml](https://github.com/epam/edp-install/blob/master/deploy-templates/values.yaml) file. In the `sonarUrl` parameter, input the appropriate values for your SonarQube service, namespace and a port `http(s)://<service-name>.<sonarqube-namespace>:9000`. Alternatively, use the address and a port to external SonarQube `http(s)://<endpoint>:9000` instead.
  ```yaml
  global:
    sonarUrl: ""

  sonar-operator:
    enabled: false
  ```

3. Proceed with the installation of EDP by following the [Install EDP](../operator-guide/install-edp.md) guide.

### Integrate Custom SonarQube With Existing EDP

This scenario is preferred when users have an already installed and preconfigured SonarQube that is offered by EDP as an `EDP-sonar-operator` but they decided to add a new third-party SonarQube. This algorithm will let users save their existing logs from deletion. In this scenario, user is supposed to have the `EDP-sonar-operator` installed already with the `sonar-ciuser-token` secret. If this is the case, follow the steps below:

1. Patch the `sonar-ciuser-token` secret with the new SonarQube token. This can be performed by the command below. Replace the `<Sonarqube-token>` with your actual SonarQube token and the `<edp-project>` with the appropriate EDP project namespace:

  ```bash
  kubectl patch secret sonar-ciuser-token -p '{"data":{"secret":"'$(echo -n "<Sonarqube-token>" | base64)'"}}' --namespace <edp-project>
  ```

2. Update the [values.yaml](https://github.com/epam/edp-install/blob/master/deploy-templates/values.yaml) file by setting the `sonarUrl` parameter:

  ```yaml
  global:
    sonarUrl: ""
  ```

3. Upgrade your EDP installation with the updated values:

  ```bash
  helm upgrade --install edp epamedp/edp-install --wait --timeout=900s \
  --version <edp_version> \
  --values values.yaml \
  --namespace <edp-project>
  ```

After following the instructions provided, you should be able to integrate your custom SonarQube with the EPAM Delivery Platform using one of the two available scenarios.

## Related Articles
* [Install EDP With Values File](install-edp.md)
* [Install Tekton With Values File](install-tekton.md)
* [Install External Secrets Operator](install-external-secrets-operator.md)
* [External Secrets Operator Integration](external-secrets-operator-integration.md)