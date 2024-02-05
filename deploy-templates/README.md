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
| cd-pipeline-operator.tenancyEngine | string | `"none"` | Defines the type of the tenant engine that can be "none", "kiosk" or "capsule"; for Stages with external cluster tenancyEngine will be ignored. Default: none |
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
| externalSecrets | object | `{"enabled":false,"manageEDPInstallSecrets":true,"manageEDPInstallSecretsName":"/edp/deploy-secrets","secretProvider":{"aws":{"region":"eu-central-1","role":null,"service":"ParameterStore"}}}` | AWS Region name, e.g. "eu-central-1". Mandatory if global.dockerRegistry.type=ecr for kaniko build-task. https://github.com/epam/edp-tekton/blob/release/0.9/charts/pipelines-library/templates/tasks/kaniko.yaml#L73 awsRegion: "" Configure External Secrets Operator to provision secrets for Platform and/or EDP https://external-secrets.io/latest/provider-aws-secrets-manager/ |
| externalSecrets.enabled | bool | `false` | Configure External Secrets for EDP platform. Deploy SecretStore. Default: false |
| externalSecrets.manageEDPInstallSecrets | bool | `true` | Create necessary secrets for EDP installation, using External Secret Operator |
| externalSecrets.manageEDPInstallSecretsName | string | `"/edp/deploy-secrets"` | Value name in AWS ParameterStore or AWS SecretsManager. Used when manageEDPInstallSecrets is true |
| externalSecrets.secretProvider.aws.region | string | `"eu-central-1"` | AWS Region where secrets are stored, e.g. eu-central-1 |
| externalSecrets.secretProvider.aws.role | string | `nil` | IAM Role to be used for Accessing AWS either Parameter Store or Secret Manager. Format: arn:aws:iam::<AWS_ACCOUNT_ID>:role/<AWS_IAM_ROLE_NAME> |
| externalSecrets.secretProvider.aws.service | string | `"ParameterStore"` | Use AWS as a Secret Provider. Can be ParameterStore or SecretsManager |
| extraObjects | list | `[]` | Array of extra K8s manifests to deploy |
| extraQuickLinks | object | `{}` | Define extra Quick Links, more details: https://github.com/epam/edp-codebase-operator/ |
| gerrit-operator.enabled | bool | `false` |  |
| global.dnsWildCard | string | `nil` | a cluster DNS wildcard name |
| global.gitProvider | string | `"github"` | Can be gerrit, github or gitlab. Default: github |
| global.platform | string | `"kubernetes"` | platform type that can be "kubernetes" or "openshift" |
| global.version | string | `"3.8.0-SNAPSHOT"` | EDP version |
| quickLinks | string | `` | Define platform Quick Links, more details: https://github.com/epam/edp-codebase-operator/ |
| sso.admins | list | `["stub_user_one@example.com"]` | Administrators of your tenant. |
| sso.developers | list | `["stub_user_one@example.com","stub_user_two@example.com"]` | Developers of your tenant |
| sso.enabled | bool | `true` | Install OAuth2-proxy and Keycloak CRs as a part of EDP deployment. |
| sso.existingSecret.secretKey | string | `"cookie-secret"` | Secret key which stores cookie-secret |
| sso.existingSecret.secretName | string | `"oauth2-proxy-cookie-secret"` | Secret name which stores cookie-secret |
| sso.extraArgs | object | `{}` | Extra arguments to provide to the OAuth2-proxy |
| sso.extraEnv | list | `[]` | Additional container environment variables |
| sso.extraVolumeMounts | list | `[]` | Additional volumeMounts to be added to the OAuth2-proxy container |
| sso.extraVolumes | list | `[]` | Additional volumes to be added to the OAuth2-proxy pod |
| sso.image.repository | string | `"quay.io/oauth2-proxy/oauth2-proxy"` | OAuth2-proxy image repository |
| sso.image.tag | string | `"v7.4.0"` | OAuth2-proxy image tag |
| sso.ingress.annotations | object | `{}` | Additional ingress annotations |
| sso.ingress.ingressClassName | string | `""` | Defines which ingress controller will implement the resource, e.g. nginx |
| sso.ingress.pathType | string | `"Prefix"` | Ingress path type. One of `Exact`, `Prefix` or `ImplementationSpecific` |
| sso.ingress.tls | list | `[]` | Ingress TLS configuration |
| sso.keycloakUrl | string | `"https://keycloak.example.com"` | Keycloak URL. |
| sso.realmName | string | `"broker"` | Defines Keycloak realm name that is used as the Identity Provider (IdP) realm |

