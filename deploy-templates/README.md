# edp-install

![Version: 3.7.1](https://img.shields.io/badge/Version-3.7.1-informational?style=flat-square) ![Type: application](https://img.shields.io/badge/Type-application-informational?style=flat-square) ![AppVersion: 3.7.1](https://img.shields.io/badge/AppVersion-3.7.1-informational?style=flat-square)

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
| @epamedp | edp-headlamp | 0.12.0 |
| @epamedp | edp-tekton | 0.10.1 |
| @epamedp | gerrit-operator | 2.19.0 |

## Values

| Key | Type | Default | Description |
|-----|------|---------|-------------|
| EDPComponents | object | `{}` |  |
| annotations | object | `{}` |  |
| cd-pipeline-operator.enabled | bool | `true` |  |
| cd-pipeline-operator.tenancyEngine | string | `"none"` | defines the type of the tenant engine that can be "none", "kiosk" or "capsule" |
| codebase-operator.enabled | bool | `true` |  |
| edp-headlamp.config.oidc.clientID | string | `"kubernetes"` |  |
| edp-headlamp.config.oidc.clientSecretKey | string | `"clientSecret"` |  |
| edp-headlamp.config.oidc.clientSecretName | string | `"keycloak-client-headlamp-secret"` |  |
| edp-headlamp.config.oidc.enabled | bool | `false` |  |
| edp-headlamp.config.oidc.issuerRealm | string | `"openshift"` |  |
| edp-headlamp.enabled | bool | `true` |  |
| edp-tekton.dashboard.enabled | bool | `true` | Deploy EDP Dashboard as a part of pipeline library when true. Default: true |
| edp-tekton.dashboard.ingress.annotations | object | `{}` | Annotations for Ingress resource |
| edp-tekton.dashboard.openshift_proxy | object | `{"enabled":false}` | https://epam.github.io/edp-install/operator-guide/oauth2-proxy/?h=#enable-oauth2-proxy-on-tekton-dashboard |
| edp-tekton.dashboard.openshift_proxy.enabled | bool | `false` | Enable oauth-proxy to include authorization layer on tekton-dashboard. Default: flase |
| edp-tekton.enabled | bool | `true` |  |
| edp-tekton.tekton-cache.enabled | bool | `true` |  |
| externalSecrets.enabled | bool | `false` | Configure External Secrets for EDP platform. Deploy SecretStore |
| externalSecrets.manageEDPInstallSecrets | bool | `true` | Create necessary secrets for EDP installation, using External Secret Operator |
| externalSecrets.manageEDPInstallSecretsName | string | `"/edp/deploy-secrets"` | Value name in AWS ParameterStore or AWS SecretsManager. Used when manageEDPInstallSecrets is true |
| externalSecrets.secretProvider.aws.region | string | `"eu-central-1"` | AWS Region where secrets are stored, e.g. eu-central-1 |
| externalSecrets.secretProvider.aws.role | string | `nil` | IAM Role to be used for Accessing AWS either Parameter Store or Secret Manager. Format: arn:aws:iam::<AWS_ACCOUNT_ID>:role/<AWS_IAM_ROLE_NAME> |
| externalSecrets.secretProvider.aws.service | string | `"ParameterStore"` | Use AWS as a Secret Provider. Can be ParameterStore or SecretsManager |
| extraObjects | list | `[]` | Array of extra K8s manifests to deploy |
| gerrit-operator.enabled | bool | `false` |  |
| global.dnsWildCard | string | `nil` | a cluster DNS wildcard name |
| global.gitProvider | string | `"github"` | Can be gerrit, github or gitlab. By default: github |
| global.platform | string | `"kubernetes"` | platform type that can be "kubernetes" or "openshift" |
| global.version | string | `"3.7.2"` | EDP version |
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
| sso | object | `{"admins":["stub_user_one@example.com"],"developers":["stub_user_one@example.com","stub_user_two@example.com"],"enabled":false,"keycloakUrl":"https://keycloak.example.com"}` | Enable SSO for EDP components. Required keycloak-operator deployment. Default: false |
| sso.admins | list | `["stub_user_one@example.com"]` | Administrators of your tenant |
| sso.developers | list | `["stub_user_one@example.com","stub_user_two@example.com"]` | Developers of your tenant |
| sso.keycloakUrl | string | `"https://keycloak.example.com"` | Keycloak URL |

