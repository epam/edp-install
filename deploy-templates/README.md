# edp-install

![Version: 3.4.0-SNAPSHOT](https://img.shields.io/badge/Version-3.4.0--SNAPSHOT-informational?style=flat-square) ![Type: application](https://img.shields.io/badge/Type-application-informational?style=flat-square) ![AppVersion: 3.4.0-SNAPSHOT](https://img.shields.io/badge/AppVersion-3.4.0--SNAPSHOT-informational?style=flat-square)

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
| @epamedp | admin-console-operator | 2.14.0 |
| @epamedp | cd-pipeline-operator | 2.14.1 |
| @epamedp | codebase-operator | 2.16.0 |
| @epamedp | edp-component-operator | 0.13.0 |
| @epamedp | edp-headlamp | 0.6.0 |
| @epamedp | edp-tekton | 0.5.0 |
| @epamedp | gerrit-operator | 2.15.0 |
| @epamedp | jenkins-operator | 2.15.0 |
| @epamedp | keycloak-operator | 1.15.0 |
| @epamedp | nexus-operator | 2.15.0 |
| @epamedp | sonar-operator | 2.14.0 |

## Values

| Key | Type | Default | Description |
|-----|------|---------|-------------|
| EDPComponents | object | `{}` |  |
| admin-console-operator.enabled | bool | `false` |  |
| annotations | object | `{}` |  |
| argo-cd.enabled | string | `"false"` | Configure Argo CD for EDP platform. |
| argo-cd.oidcSecretName | string | `nil` | Custom secret name for argo-cd keycloak client. Ignored if external secret enabled. |
| argo-cd.url | string | `nil` |  |
| awsRegion | string | `nil` | AWS Region, e.g. "eu-central-1" |
| cd-pipeline-operator.enabled | bool | `true` |  |
| codebase-operator.enabled | bool | `true` |  |
| edp-headlamp.config.oidc.clientID | string | `"kubernetes"` |  |
| edp-headlamp.config.oidc.clientSecretKey | string | `"clientSecret"` |  |
| edp-headlamp.config.oidc.clientSecretName | string | `"keycloak-client-headlamp-secret"` |  |
| edp-headlamp.config.oidc.enabled | bool | `false` |  |
| edp-headlamp.config.oidc.issuerRealm | string | `"openshift"` |  |
| edp-headlamp.enabled | bool | `true` |  |
| edp-tekton.ctLint | string | `nil` |  |
| edp-tekton.dashboard.enabled | bool | `true` | Deploy EDP Dashboard as a part of pipeline library when true. Default: true |
| edp-tekton.dashboard.ingress.annotations | object | `{}` | Annotations for Ingress resource |
| edp-tekton.dashboard.openshift_proxy | object | `{"enabled":false}` | https://epam.github.io/edp-install/operator-guide/oauth2-proxy/?h=#enable-oauth2-proxy-on-tekton-dashboard |
| edp-tekton.dashboard.openshift_proxy.enabled | bool | `false` | Enable oauth-proxy to include authorization layer on tekton-dashboard. Default: flase |
| edp-tekton.enabled | bool | `true` |  |
| externalSecrets.enabled | bool | `false` | Configure External Secrets for EDP platform. Deploy SecretStore |
| externalSecrets.manageEDPInstallSecrets | bool | `true` | Create necessary secrets for EDP installation, using External Secret Operator |
| externalSecrets.manageEDPInstallSecretsName | string | `"/edp/deploy-secrets"` | Value name in AWS ParameterStore or AWS SecretsManager. Used when manageEDPInstallSecrets is true |
| externalSecrets.secretProvider.aws.region | string | `"eu-central-1"` | AWS Region where secrets are stored, e.g. eu-central-1 |
| externalSecrets.secretProvider.aws.role | string | `nil` | IAM Role to be used for Accessing AWS either Parameter Store or Secret Manager. Format: arn:aws:iam::<AWS_ACCOUNT_ID>:role/<AWS_IAM_ROLE_NAME> |
| externalSecrets.secretProvider.aws.service | string | `"ParameterStore"` | Use AWS as a Secret Provider. Can be ParameterStore or SecretsManager |
| gerrit-operator.enabled | bool | `true` |  |
| global.admins | list | `["stub_user_one@example.com"]` | Administrators of your tenant |
| global.developers | list | `["stub_user_one@example.com","stub_user_two@example.com"]` | Developers of your tenant |
| global.dnsWildCard | string | `nil` | a cluster DNS wildcard name |
| global.dockerRegistry | object | `{"type":"ecr","url":"<AWS_ACCOUNT_ID>.dkr.ecr.<AWS_REGION>.amazonaws.com"}` | Optional parameter. Link to use custom nexus. Format: http://<service-name>.<nexus-namespace>:8081 or http://<ip-address>:<port> nexusUrl: |
| global.dockerRegistry.type | string | `"ecr"` | Define Image Registry that will to be used in Pipelines. Can be ecr (default), harbor |
| global.dockerRegistry.url | string | `"<AWS_ACCOUNT_ID>.dkr.ecr.<AWS_REGION>.amazonaws.com"` | Docker Registry endpoint |
| global.edpName | string | `"stub-namespace"` | namespace or a project name (in case of OpenShift) |
| global.gerritSSHPort | string | `"22"` | Gerrit SSH node port |
| global.gitProvider | string | `"gerrit"` | Can be gerrit, github or gitlab. By default: gerrit |
| global.keycloakUrl | string | `"https://keycloak.example.com"` | Keycloak URL |
| global.kioskEnabled | bool | `false` |  |
| global.platform | string | `"kubernetes"` | platform type that can be "kubernetes" or "openshift" |
| global.version | string | `"3.4.0-SNAPSHOT"` | EDP version |
| global.webConsole.url | string | `"https://xxxxxxxxxxxxxxxxxxxx.sk1.eu-central-1.eks.amazonaws.com"` | URL to OpenShift/Kubernetes Web console |
| jenkins-operator.enabled | bool | `false` |  |
| kaniko.existingDockerConfig | string | `nil` | Existing secret which contains docker-config, if not defined then 'kaniko-docker-config' will be created with default value: { "credStore": "ecr-login"} |
| kaniko.initKanikoContainer.extraEnvVars | list | `[]` | Array with extra environment variables to add to the init-kaniko container |
| kaniko.initKanikoContainer.image | string | `"busybox:1.35.0"` | init container image which waits for Dockerfile before starting actual build |
| kaniko.initRepositoryContainer.extraCommandOptions | string | `""` | Configure extra options for command 'aws ecr create-repository' |
| kaniko.initRepositoryContainer.extraEnvVars | list | `[]` | Array with extra environment variables to add to the init-repository container |
| kaniko.initRepositoryContainer.image | string | `"amazon/aws-cli:2.9.4"` | aws-cli image is used to provision non-existing AWS ECR repository |
| kaniko.kanikoContainer.extraEnvVars | list | `[]` | Array with extra environment variables to add to the Kaniko container |
| kaniko.kanikoContainer.image | string | `"gcr.io/kaniko-project/executor:v1.12.1-debug"` | kaniko image |
| kaniko.kanikoContainer.resources.limits | object | `{}` | The resources limits for the Kaniko containers |
| kaniko.kanikoContainer.resources.requests | object | `{}` | The requested resources for the Kaniko containers |
| kaniko.nodeSelector | object | `{}` | nodeSelector Node labels for pod assignment |
| kaniko.roleArn | string | `nil` | AWS IAM role to be used for kaniko pod service account (IRSA). Format: arn:aws:iam::<AWS_ACCOUNT_ID>:role/<AWS_IAM_ROLE_NAME> |
| kaniko.tolerations | list | `[]` | tolerations Tolerations for pod assignment |
| keycloak-operator.enabled | bool | `true` |  |
| nexus-operator.enabled | bool | `true` |  |
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
| sonar-operator.enabled | bool | `true` |  |
| vcs.enabled | string | `"false"` |  |

