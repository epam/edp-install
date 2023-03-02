# Headlamp OIDC Configuration

This page provides the instructions of configuring the [OIDC authorization](https://openid.net/connect/) for
[EDP Headlamp UI](../headlamp-user-guide/index.md), thus allowing using SSO for authorization in Headlamp and controlling
user access and rights from one configuration point.

## Prerequisites

Ensure the following values are set first before starting the Headlamp OIDC configuration:

1. `realm_id`  = **openshift**

2. `client_id` = **kubernetes**

3. `keycloak_client_key`= **keycloak_client_secret_key** (received from: `Openshift realm` -> `clients` -> `kubernetes` -> `Credentials` -> `Client secret`)

4. `group` = **`<edp-project>-oidc-admins`, `<edp-project>-oidc-builders`, `<edp-project>-oidc-deployers`, 
`<edp-project>-oidc-developers`, `<edp-project>-oidc-viewers`** (Should be created manually in the realm from point 1)

!!! note
    The values indicated above are the result of the Keycloak configuration as an OIDC identity provider.
    To receive them, follow the instructions on the [Keycloak OIDC EKS Configuration](configure-keycloak-oidc-eks.md) page.

## Configure Keycloak

To proceed with the Keycloak configuration, perform the following:

1. Add the URL of the Headlamp to the `valid_redirect_uris` variable in [Keycloak](configure-keycloak-oidc-eks.md#keycloak_client):

  ??? note "View: keycloak_openid_client"
      ```yaml
        valid_redirect_uris = [
          "https://edp-headlamp-<edp_namespace>.<dns_wildcard>/*"
          "http://localhost:8000/*"
        ]
      ```

  Make sure to define the following Keycloak client values as indicated:

  !![Keycloak client configuration](../assets/operator-guide/headlamp-oidc-keycloak-2.png "Keycloak client configuration")

2. Configure the Keycloak client key in Kubernetes using the Kubernetes secrets or the [External Secrets Operator](external-secrets-operator-integration.md):

  ```yaml
  apiVersion: v1
  kind: Secret
  metadata:
    name: keycloak-client-headlamp-secret
    namespace: <edp-project>
  type: Opaque
  stringData:
    clientSecret: <keycloak_client_secret_key>
  ```

3. Assign user to one or more groups.

There are two types of group provided for users:

- Independent group
- Extension group

Independent group provides the minimum required permission set. Extension group extends the rights of an independent group.

For example, the `<edp-project>-oidc-viewers` group can be extended with rights from the `<edp-project>-oidc-builders` group.

| Group Name | Independent Group | Extension Group |
| - | :-: | :-: |
|`<edp-project>-oidc-admins`    | :material-check: | |
|`<edp-project>-oidc-developers`| :material-check: | |
|`<edp-project>-oidc-viewers`   | :material-check: | |
|`<edp-project>-oidc-builders`  | | :material-check: |
|`<edp-project>-oidc-deployers` | | :material-check: |

| Name | Action List |
| - | - |
| View | Getting of all namespaced resources |
| Build | Starting a PipelineRun from Headlamp UI |
| Deploy | Deploying a new version of application via Argo CD Application |

| Group Name | View | Build | Deploy | Full Namespace Access |
| - | :-: | :-: | :-: | :-: |
|`<edp-project>-oidc-admins`    | :material-check: | :material-check: | :material-check: | :material-check: |
|`<edp-project>-oidc-developers`| :material-check: | :material-check: | :material-check: | |
|`<edp-project>-oidc-viewers`   | :material-check: | | | |
|`<edp-project>-oidc-builders`  | | :material-check: | | |
|`<edp-project>-oidc-deployers` | | | :material-check: | |

## Integrate Headlamp With Kubernetes

Headlamp can be integrated in Kubernetes in three steps:

1. Update the [values.yaml](install-edp.md#values) file by enabling OIDC:

  ??? note "View: values.yaml"
      ```yaml
      edp-headlamp:
        config:
          oidc:
            enabled: true
      ```

2. Navigate to Headlamp and log in by clicking the `Sign In` button:

  !![Headlamp login page](../assets/operator-guide/headlamp-oidc-headlamp-1.png "Headlamp login page")

3. Go to `EDP` section -> `Account` -> `Settings`, and set up a namespace:

  !![Headlamp namespace settings](../assets/operator-guide/headlamp-oidc-headlamp-2.png "Headlamp namespace settings")

As a result, it is possible to control access and rights from the Keycloak endpoint.

## Related Articles

* [Configure Access Token Lifetime](../faq.md#how-to-change-the-lifespan-of-an-access-token-that-is-used-for-headlamp-and-oidc-login-plugin)
* [EKS OIDC With Keycloak](configure-keycloak-oidc-eks.md)
* [External Secrets Operator](external-secrets-operator-integration.md)