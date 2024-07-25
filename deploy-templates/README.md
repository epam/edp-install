# edp-install

![Version: 3.10.0-SNAPSHOT](https://img.shields.io/badge/Version-3.10.0--SNAPSHOT-informational?style=flat-square) ![Type: application](https://img.shields.io/badge/Type-application-informational?style=flat-square) ![AppVersion: 3.10.0-SNAPSHOT](https://img.shields.io/badge/AppVersion-3.10.0--SNAPSHOT-informational?style=flat-square)

A Helm chart for KubeRocketCI Platform

**Homepage:** <https://docs.kuberocketci.io/>

## Maintainers

| Name | Email | Url |
| ---- | ------ | --- |
| epmd-edp | <SupportEPMD-EDP@epam.com> | <https://solutionshub.epam.com/solution/kuberocketci> |
| sergk |  | <https://github.com/SergK> |

## Source Code

* <https://github.com/epam/edp-install>

## Requirements

| Repository | Name | Version |
|------------|------|---------|
| @epamedp | cd-pipeline-operator | 2.20.0 |
| @epamedp | codebase-operator | 2.23.0 |
| @epamedp | edp-headlamp | 0.15.0 |
| @epamedp | edp-tekton | 0.12.0 |
| @epamedp | gerrit-operator | 2.21.0 |

## Values

| Key | Type | Default | Description |
|-----|------|---------|-------------|
| annotations | object | `{}` |  |
| cd-pipeline-operator.capsuleTenant | object | `{"create":true,"spec":null}` | Required tenancyEngine: capsule. Specify Capsule Tenant specification for Environments. |
| cd-pipeline-operator.enabled | bool | `true` |  |
| cd-pipeline-operator.manageNamespace | bool | `true` | should the operator manage(create/delete) namespaces for stages Refer to the guide for managing namespace (https://docs.kuberocketci.io/docs/operator-guide/auth/namespace-management) |
| cd-pipeline-operator.secretManager | string | `"own"` | Flag indicating whether the operator should manage secrets for stages. This parameter controls the provisioning of the 'regcred' secret within deployed environments, facilitating access to private container registries. Set the parameter to "none" under the following conditions:   - If 'global.dockerRegistry.type=ecr' and IRSA is enabled, or   - If 'global.dockerRegistry.type=openshift'. For private registries, choose the most appropriate method to provide credentials to deployed environments. Refer to the guide for managing container registries (https://docs.kuberocketci.io/docs/user-guide/manage-container-registries). Possible values: own/eso/none.   - own: Copies the secret once from the parent namespace, without subsequent reconciliation. If updated in the parent namespace, manual updating in all created namespaces is required.   - eso: The secret will be managed by the External Secrets Operator (requires installation and configuration in the cluster: https://docs.kuberocketci.io/docs/operator-guide/secrets-management/install-external-secrets-operator).   - none: Disables secrets management logic. |
| cd-pipeline-operator.tenancyEngine | string | `"none"` | Defines the type of the tenant engine that can be "none", "kiosk" or "capsule"; for Stages with external cluster tenancyEngine will be ignored. Default: none |
| codebase-operator.enabled | bool | `true` |  |
| edp-headlamp.config.baseURL | string | `""` | base url path at which headlamp should run |
| edp-headlamp.config.oidc | object | `{"clientID":"","clientSecretKey":"clientSecret","clientSecretName":"keycloak-client-headlamp-secret","enabled":false,"issuerRealm":"","keycloakUrl":"https://keycloak.example.com/auth","scopes":""}` | For detailed instructions, refer to: https://docs.kuberocketci.io/docs/operator-guide/auth/configure-keycloak-oidc-eks, https://docs.kuberocketci.io/docs/operator-guide/auth/ui-portal-oidc |
| edp-headlamp.config.oidc.clientID | string | `""` | OIDC client ID |
| edp-headlamp.config.oidc.clientSecretKey | string | `"clientSecret"` | OIDC client secret key |
| edp-headlamp.config.oidc.clientSecretName | string | `"keycloak-client-headlamp-secret"` | OIDC client secret name |
| edp-headlamp.config.oidc.issuerRealm | string | `""` | OIDC issuer realm |
| edp-headlamp.config.oidc.keycloakUrl | string | `"https://keycloak.example.com/auth"` | Keycloak URL |
| edp-headlamp.config.oidc.scopes | string | `""` | OIDC scopes to be used |
| edp-headlamp.enabled | bool | `true` |  |
| edp-headlamp.ingress.annotations | object | `{}` | Annotations for Ingress resource |
| edp-headlamp.ingress.enabled | bool | `true` | Enable external endpoint access. Default Ingress/Route host pattern: portal-{{ .Release.Namespace }}.{{ .Values.global.dnsWildCard }} |
| edp-headlamp.ingress.tls | list | `[]` | Ingress TLS configuration |
| edp-tekton.dashboard.enabled | bool | `false` | https://docs.kuberocketci.io/docs/operator-guide/auth/oauth2-proxy |
| edp-tekton.dashboard.ingress.annotations | object | `{}` | Annotations for Ingress resource |
| edp-tekton.dashboard.ingress.enabled | bool | `true` | Enable external endpoint access. Default Ingress/Route host pattern: tekton-{{ .Release.Namespace }}.{{ .Values.global.dnsWildCard }} |
| edp-tekton.dashboard.ingress.tls | list | `[]` | Uncomment it to enable tekton-dashboard OIDC on EKS cluster nginx.ingress.kubernetes.io/auth-signin: 'https://<oauth-ingress-host>/oauth2/start?rd=https://$host$request_uri' nginx.ingress.kubernetes.io/auth-url: 'http://oauth2-proxy.edp.svc.cluster.local:8080/oauth2/auth' |
| edp-tekton.dashboard.openshift_proxy | object | `{"enabled":false}` | https://docs.kuberocketci.io/docs/operator-guide/auth/oauth2-proxy#enable-oauth2-proxy-on-tekton-dashboard |
| edp-tekton.dashboard.openshift_proxy.enabled | bool | `false` | Enable oauth-proxy to include authorization layer on tekton-dashboard. Default: flase |
| edp-tekton.dashboard.readOnly | bool | `false` | Define mode for Tekton Dashboard. Enable/disaable capability to create/modify/remove Tekton objects via Tekton Dashboard. Default: false. |
| edp-tekton.enabled | bool | `true` |  |
| edp-tekton.gitServers | object | `{}` |  |
| edp-tekton.grafana | object | `{"enabled":false}` | https://docs.kuberocketci.io/docs/operator-guide/ci/tekton-monitoring |
| edp-tekton.tekton-cache.enabled | bool | `true` |  |
| externalSecrets.enabled | bool | `false` | Configure External Secrets for KubeRocketCI platform. Deploy SecretStore. Default: false |
| externalSecrets.manageCodemieSecretsName | string | `"/edp/codemie-secrets"` |  |
| externalSecrets.manageEDPInstallSecrets | bool | `true` | Create necessary secrets for KubeRocketCI installation, using External Secret Operator |
| externalSecrets.manageEDPInstallSecretsName | string | `"/edp/deploy-secrets"` | Value name in AWS ParameterStore or AWS SecretsManager. Used when manageEDPInstallSecrets is true |
| externalSecrets.secretProvider.aws.region | string | `"eu-central-1"` | AWS Region where secrets are stored, e.g. eu-central-1 |
| externalSecrets.secretProvider.aws.role | string | `nil` | IAM Role to be used for Accessing AWS either Parameter Store or Secret Manager. Format: arn:aws:iam::<AWS_ACCOUNT_ID>:role/<AWS_IAM_ROLE_NAME> |
| externalSecrets.secretProvider.aws.service | string | `"ParameterStore"` | Use AWS as a Secret Provider. Can be ParameterStore or SecretsManager |
| externalSecrets.secretProvider.generic.secretStore.name | string | `"example-secret-store"` | Defines SecretStore name. |
| externalSecrets.secretProvider.generic.secretStore.providerConfig | object | `{}` | Defines SecretStore provider configuration. |
| externalSecrets.type | string | `"aws"` | Defines provider type. One of `aws` or `generic`. |
| extraObjects | list | `[]` | Array of extra K8s manifests to deploy |
| extraQuickLinks | object | `{}` | Define extra Quick Links, more details: https://github.com/epam/edp-codebase-operator/ |
| gerrit-operator.enabled | bool | `false` |  |
| global.dnsWildCard | string | `nil` | a cluster DNS wildcard name |
| global.dockerRegistry.awsRegion | string | `""` | Defines the geographic area where the (AWS) Elastic Container Registry repository is hosted (optional). E.g. "eu-central-1". Mandatory if 'global.dockerRegistry.type=ecr' for kaniko build-task. Ref: https://github.com/epam/edp-tekton/blob/release/0.10/charts/pipelines-library/templates/tasks/kaniko.yaml#L73 |
| global.dockerRegistry.space | string | `""` | Defines project name. |
| global.dockerRegistry.type | string | `""` | Defines type of registry. One of `ecr`, `harbor`, `dockerhub`, `openshift`, `nexus` or `ghcr`. 'openshift' registry is available only in case if platform is deployed on the OpenShift cluster and the variable global.platform is set to 'openshift'. |
| global.dockerRegistry.url | string | `""` | Defines registry endpoint URL. |
| global.gitProviders | list | `["github"]` | Can be gerrit, github or gitlab. Default: github |
| global.platform | string | `"kubernetes"` | platform type that can be "kubernetes" or "openshift" |
| quickLinks | string | `` | Define platform Quick Links, more details: https://github.com/epam/edp-codebase-operator/ |
| sso.admins | list | `["stub_user_one@example.com"]` | Administrators of your tenant. |
| sso.affinity | object | `{}` | Affinity settings for pod assignment |
| sso.developers | list | `["stub_user_one@example.com","stub_user_two@example.com"]` | Developers of your tenant |
| sso.enabled | bool | `false` | Install OAuth2-proxy and Keycloak CRs as a part of KubeRocketCI deployment. |
| sso.existingSecret.secretKey | string | `"cookie-secret"` | Secret key which stores cookie-secret |
| sso.existingSecret.secretName | string | `"oauth2-proxy-cookie-secret"` | Secret name which stores cookie-secret |
| sso.extraArgs | object | `{}` | Extra arguments to provide to the OAuth2-proxy |
| sso.extraEnv | list | `[]` | Additional container environment variables |
| sso.extraVolumeMounts | list | `[]` | Additional volumeMounts to be added to the OAuth2-proxy container |
| sso.extraVolumes | list | `[]` | Additional volumes to be added to the OAuth2-proxy pod |
| sso.image.repository | string | `"quay.io/oauth2-proxy/oauth2-proxy"` | OAuth2-proxy image repository |
| sso.image.tag | string | `"v7.4.0"` | OAuth2-proxy image tag |
| sso.ingress.annotations | object | `{}` | Additional ingress annotations |
| sso.ingress.enabled | bool | `true` | Enable ingress controller resource |
| sso.ingress.ingressClassName | string | `""` | Defines which ingress controller will implement the resource, e.g. nginx |
| sso.ingress.pathType | string | `"Prefix"` | Ingress path type. One of `Exact`, `Prefix` or `ImplementationSpecific` |
| sso.ingress.tls | list | `[]` | Ingress TLS configuration |
| sso.keycloakOperatorResources.createKeycloakCR | bool | `true` |  |
| sso.keycloakOperatorResources.kind | string | `"Keycloak"` |  |
| sso.keycloakOperatorResources.name | string | `"main"` |  |
| sso.keycloakUrl | string | `"https://keycloak.example.com/auth"` | Keycloak URL. |
| sso.nodeSelector | object | `{}` | Node labels for pod assignment |
| sso.ssoClientName | string | `"edp"` | Defines Keycloak client name that is used for the Identity Provider (IdP) client |
| sso.ssoRealmName | string | `"broker"` | Defines Keycloak sso realm name that is used as the Identity Provider (IdP) realm |
| sso.tolerations | list | `[]` | Toleration labels for pod assignment |

