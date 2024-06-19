# Upgrade KubeRocketCI v3.8 to 3.9

!!! Important
    We suggest backing up the KubeRocketCI environment before starting the upgrade procedure.

This section provides detailed instructions for upgrading the KubeRocketCI to version 3.9.0. Follow the steps and requirements outlined below:

!!! Warning
    Starting from version v.3.9.x, KubeRocketCI no longer supports Kiosk as a tenancy engine tool. Please migrate to the Capsule engine or disable this option.

1. (Optional) Migrate from Kiosk tenancy engine.

  === "Capsule"
      1. Take look how to install Capsule using [edp-cluster addons](https://github.com/epam/edp-cluster-add-ons/tree/main/add-ons/capsule).
      2. [Integrate Capsule](https://epam.github.io/edp-install/operator-guide/capsule) with EDP platform.
      3. Update edp-install values file:

      ``` yaml title="values.yaml"
      ...
      cd-pipeline-operator:
        tenancyEngine: "capsule"
      ...
      ```

  === "Disable tenancy engine"

      To disable tenancy engine by updating the edp-install values.yaml file:

      ``` yaml title="values.yaml"
      ...
      cd-pipeline-operator:
        tenancyEngine: "none"
      ...
      ```

2. (Optional) Align the Keycloak integration.

  In KubeRocketCI version 3.9.0, the Keycloak configuration procedure has been altered in the [values](https://github.com/epam/edp-install/blob/v3.9.0/deploy-templates/values.yaml#L461) file. Please pay attention and adjust the configuration for your specific use case.

  === "Values 3.8.1"

      ``` yaml title="values.yaml"
      sso:
        # -- Install OAuth2-proxy and Keycloak CRs as a part of EDP deployment.
        enabled: false
        # -- Defines Keycloak realm name that is used as the Identity Provider (IdP) realm
        realmName: "broker"
        # -- Keycloak URL.
        keycloakUrl: https://keycloak.example.com
      ```

  === "Values 3.9.0"

      ``` yaml title="values.yaml"
      sso:
        # -- Install OAuth2-proxy and Keycloak CRs as a part of EDP deployment.
        enabled: false

        keycloakOperatorResources:
          # Set to false if using the add-ons approach (refer to https://github.com/epam/edp-cluster-add-ons)
          # for EDP installation and if the extension-oidc is already installed.
          # This prevents the creation of an additional Keycloak resource and secret.
          # The 'kind' and 'name' must be specified in case of using an existing Keycloak/ClusterKeycloak resource.
          # Create kind: Keycloak as a part of chart installation
          createKeycloakCR: true
          # Can be Keycloak or ClusterKeycloak.
          kind: Keycloak
          # Name of kind: Keycloak/ClusterKeycloak CR.
          name: main

        # -- Defines the Keycloak realm name, which by default is named after the namespace where EDP is deployed.
        # realmName: edp
        # -- Defines Keycloak sso realm name that is used as the Identity Provider (IdP) realm
        ssoRealmName: "broker"
        # -- Defines Keycloak client name that is used for the Identity Provider (IdP) client
        ssoClientName: "edp"
        # -- Keycloak URL.
        keycloakUrl: https://keycloak.example.com/auth
      ```

3. To initiate upgrading to the v3.9.0 version, run the following command:

  ```bash
  helm upgrade edp epamedp/edp-install -n edp --values values.yaml --version=3.9.0
  ```

  !!! Note
      To verify the installation, test the deployment before applying it to the cluster with the `--dry-run` tag:<br>
      `helm upgrade edp epamedp/edp-install -n edp --values values.yaml --version=3.9.0 --dry-run`
