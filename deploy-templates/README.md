# edp-install

![Version: 3.8.0-SNAPSHOT](https://img.shields.io/badge/Version-3.8.0--SNAPSHOT-informational?style=flat-square) ![Type: application](https://img.shields.io/badge/Type-application-informational?style=flat-square) ![AppVersion: 3.8.0-SNAPSHOT](https://img.shields.io/badge/AppVersion-3.8.0--SNAPSHOT-informational?style=flat-square)

A Helm chart for EDP Install

**Homepage:** <https://epam.github.io/edp-install/>

## Maintainers

| Name | Email | Url |
| ---- | ------ | --- |
| epmd-edp | <SupportEPMD-EDP@epam.com> | <https://solutionshub.epam.com/solution/epam-delivery-platform> |
| sergk |  | <https://github.com/SergK> |

## Source Code

* <https://github.com/epam/edp-install>

## Requirements

| Repository | Name | Version |
|------------|------|---------|
| @epamedp | cd-pipeline-operator | 2.18.0 |
| @epamedp | codebase-operator | 2.21.0 |
| @epamedp | edp-component-operator | 0.13.0 |
| @epamedp | edp-headlamp | 0.13.1 |
| @epamedp | edp-tekton | 0.10.2 |
| @epamedp | gerrit-operator | 2.19.0 |

## Values

| Key | Type | Default | Description |
|-----|------|---------|-------------|
| annotations | object | `{}` |  |
| cd-pipeline-operator.capsuleTenant | object | `{"create":true,"manageNamespace":true,"secretManager":"none","spec":null}` | Required tenancyEngine: capsule. Specify Capsule Tenant specification for Environments. |
| cd-pipeline-operator.capsuleTenant.manageNamespace | bool | `true` | should the operator manage(create/delete) namespaces for stages |
| cd-pipeline-operator.capsuleTenant.secretManager | string | `"none"` | flag that indicates whether the operator should manage secrets for stages; values: own/eso/none. own - just copy secrets; eso - secrete will be managed by External Secrets Operator(operator should be installed in the cluster); none - not enable secrets management logic; |
| cd-pipeline-operator.enabled | bool | `true` |  |
| cd-pipeline-operator.tenancyEngine | string | `"none"` | defines the type of the tenant engine that can be "none", "kiosk" or "capsule"; for Stages with external cluster tenancyEngine will be ignored. Default: none |
| codebase-operator.enabled | bool | `true` |  |
| edp-headlamp.config.baseURL | string | `""` | base url path at which headlamp should run |
| edp-headlamp.config.oidc.clientID | string | `""` | OIDC client ID |
| edp-headlamp.config.oidc.clientSecretKey | string | `"clientSecret"` | OIDC client secret key |
| edp-headlamp.config.oidc.clientSecretName | string | `"keycloak-client-headlamp-secret"` | OIDC client secret name |
| edp-headlamp.config.oidc.enabled | bool | `false` |  |
| edp-headlamp.config.oidc.issuerRealm | string | `""` | OIDC issuer realm |
| edp-headlamp.config.oidc.keycloakUrl | string | `"https://keycloak.example.com"` | Keycloak URL |
| edp-headlamp.config.oidc.scopes | string | `""` | OIDC scopes to be used |
| edp-headlamp.enabled | bool | `true` |  |
| edp-tekton.dashboard.enabled | bool | `true` | https://epam.github.io/edp-install/operator-guide/oauth2-proxy/ |
| edp-tekton.dashboard.ingress.annotations | object | `{}` | Annotations for Ingress resource |
| edp-tekton.dashboard.openshift_proxy | object | `{"enabled":false}` | https://epam.github.io/edp-install/operator-guide/oauth2-proxy/?h=#enable-oauth2-proxy-on-tekton-dashboard |
| edp-tekton.dashboard.openshift_proxy.enabled | bool | `false` | Enable oauth-proxy to include authorization layer on tekton-dashboard. Default: flase |
| edp-tekton.dashboard.readOnly | bool | `false` | Define mode for Tekton Dashboard. Enable/disaable capability to create/modify/remove Tekton objects via Tekton Dashboard. Default: false. |
| edp-tekton.enabled | bool | `true` |  |
| edp-tekton.tekton-cache.enabled | bool | `true` |  |
| externalSecrets.enabled | bool | `false` | Configure External Secrets for EDP platform. Deploy SecretStore. Default: false |
| externalSecrets.manageEDPInstallSecrets | bool | `true` | Create necessary secrets for EDP installation, using External Secret Operator |
| externalSecrets.manageEDPInstallSecretsName | string | `"/edp/deploy-secrets"` | Value name in AWS ParameterStore or AWS SecretsManager. Used when manageEDPInstallSecrets is true |
| externalSecrets.secretProvider.aws.region | string | `"eu-central-1"` | AWS Region where secrets are stored, e.g. eu-central-1 |
| externalSecrets.secretProvider.aws.role | string | `nil` | IAM Role to be used for Accessing AWS either Parameter Store or Secret Manager. Format: arn:aws:iam::<AWS_ACCOUNT_ID>:role/<AWS_IAM_ROLE_NAME> |
| externalSecrets.secretProvider.aws.service | string | `"ParameterStore"` | Use AWS as a Secret Provider. Can be ParameterStore or SecretsManager |
| extraObjects | list | `[]` | Array of extra K8s manifests to deploy |
| extraQuickLinks | object | `{}` |  |
| gerrit-operator.enabled | bool | `false` |  |
| global.dnsWildCard | string | `nil` | a cluster DNS wildcard name |
| global.gitProvider | string | `"github"` | Can be gerrit, github or gitlab. Default: github |
| global.platform | string | `"kubernetes"` | platform type that can be "kubernetes" or "openshift" |
| global.version | string | `"3.8.0-SNAPSHOT"` | EDP version |
| oauth2_proxy.enabled | bool | `false` | Install oauth2-proxy as a part of EDP deployment. Default: false |
| oauth2_proxy.existingSecret.secretKey | string | `"cookie-secret"` | Secret key which stores cookie-secret |
| oauth2_proxy.existingSecret.secretName | string | `"oauth2-proxy-cookie-secret"` | Secret name which stores cookie-secret |
| oauth2_proxy.extraArgs | object | `{}` |  |
| oauth2_proxy.extraEnv | list | `[]` |  |
| oauth2_proxy.extraVolumeMounts | list | `[]` | Additional volumeMounts to be added to the oauth2-proxy container |
| oauth2_proxy.extraVolumes | list | `[]` | Additional volumes to be added to the oauth2-proxy pod |
| oauth2_proxy.image.repository | string | `"quay.io/oauth2-proxy/oauth2-proxy"` | oauth2-proxy image repository |
| oauth2_proxy.image.tag | string | `"v7.4.0"` | oauth2-proxy image tag |
| oauth2_proxy.ingress.annotations | object | `{}` |  |
| oauth2_proxy.ingress.pathType | string | `"Prefix"` | pathType is only for k8s >= 1.1= |
| oauth2_proxy.ingress.tls | list | `[]` | See https://kubernetes.io/blog/2020/04/02/improvements-to-the-ingress-api-in-kubernetes-1.18/#specifying-the-class-of-an-ingress ingressClassName: nginx |
| sso | object | `{"admins":["stub_user_one@example.com"],"developers":["stub_user_one@example.com","stub_user_two@example.com"],"enabled":false,"keycloakUrl":"https://keycloak.example.com"}` | Enable SSO for EDP oauth2-proxy. Default: false |
| sso.admins | list | `["stub_user_one@example.com"]` | Administrators of your tenant |
| sso.developers | list | `["stub_user_one@example.com","stub_user_two@example.com"]` | Developers of your tenant |
| sso.keycloakUrl | string | `"https://keycloak.example.com"` | Keycloak URL |

