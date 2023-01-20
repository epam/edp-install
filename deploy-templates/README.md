# edp-install

![Version: 2.13.0-SNAPSHOT](https://img.shields.io/badge/Version-2.13.0--SNAPSHOT-informational?style=flat-square) ![Type: application](https://img.shields.io/badge/Type-application-informational?style=flat-square) ![AppVersion: 2.13.0-SNAPSHOT](https://img.shields.io/badge/AppVersion-2.13.0--SNAPSHOT-informational?style=flat-square)

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
| @epamedp | admin-console-operator | 2.12.0 |
| @epamedp | cd-pipeline-operator | 2.12.1 |
| @epamedp | codebase-operator | 2.13.1 |
| @epamedp | edp-component-operator | 0.12.0 |
| @epamedp | edp-headlamp | 0.2.0 |
| @epamedp | edp-tekton | 0.2.1 |
| @epamedp | gerrit-operator | 2.12.0 |
| @epamedp | jenkins-operator | 2.12.1 |
| @epamedp | keycloak-operator | 1.12.0 |
| @epamedp | nexus-operator | 2.12.1 |
| @epamedp | perf-operator | 2.12.0 |
| @epamedp | sonar-operator | 2.12.0 |

## Values

| Key | Type | Default | Description |
|-----|------|---------|-------------|
| EDPComponents | object | `{}` |  |
| admin-console-operator.enabled | bool | `false` |  |
| annotations | object | `{}` |  |
| argocd.enabled | bool | `false` | Enable ArgoCD integration |
| argocd.url | string | `""` (defaults to https://argocd.{{ .Values.global.dnsWildCard }}) | ArgoCD URL in format schema://URI |
| awsRegion | string | `nil` | AWS Region, e.g. "eu-central-1" |
| cd-pipeline-operator.enabled | bool | `true` |  |
| codebase-operator.enabled | bool | `true` |  |
| dockerRegistry.url | string | `"<AWS_ACCOUNT_ID>.dkr.ecr.<AWS_REGION>.amazonaws.com"` | Docker Registry endpoint |
| edp-headlamp.enabled | bool | `true` |  |
| edp-tekton.enabled | bool | `true` |  |
| externalSecrets.enabled | bool | `false` | Configure External Secrets for EDP platform. Deploy SecretStore |
| externalSecrets.manageEDPInstallSecrets | bool | `true` | Create necessary secrets for EDP installation, using External Secret Operator |
| externalSecrets.manageEDPInstallSecretsName | string | `"/edp/deploy-secrets"` | Value name in AWS ParameterStore or AWS SecretsManager. Used when manageEDPInstallSecrets is true |
| externalSecrets.secretProvider.aws.region | string | `"eu-central-1"` | AWS Region where secrets are stored, e.g. eu-central-1 |
| externalSecrets.secretProvider.aws.role | string | `nil` | IAM Role to be used for Accessing AWS either Parameter Store or Secret Manager. Format: arn:aws:iam::093899590031:role/rolename |
| externalSecrets.secretProvider.aws.service | string | `"ParameterStore"` | Use AWS as a Secret Provider. Can be ParameterStore or SecretsManager |
| gerrit-operator.enabled | bool | `true` |  |
| global.admins | list | `["stub_user_one@example.com"]` | Administrators of your tenant |
| global.developers | list | `["stub_user_one@example.com","stub_user_two@example.com"]` | Developers of your tenant |
| global.dnsWildCard | string | `nil` | a cluster DNS wildcard name |
| global.edpName | string | `"stub-namespace"` | namespace or a project name (in case of OpenShift) |
| global.gerritSSHPort | string | `"22"` | Gerrit SSH node port |
| global.gitProvider | string | `"gerrit"` | Can be gerrit, github or gitlab. By default: gerrit |
| global.keycloakUrl | string | `"https://keycloak.example.com"` | Keycloak URL |
| global.kioskEnabled | bool | `true` |  |
| global.platform | string | `"kubernetes"` | platform type that can be "kubernetes" or "openshift" |
| global.version | string | `"2.13.0-SNAPSHOT"` | EDP version |
| global.webConsole.url | string | `nil` | URL to OpenShift/Kubernetes Web console |
| jenkins-operator.enabled | bool | `false` |  |
| kaniko.existingDockerConfig | string | `nil` | Existing secret which contains docker-config, if not defined then 'kaniko-docker-config' will be created with default value: { "credStore": "ecr-login"} |
| kaniko.initKanikoContainer.extraEnvVars | list | `[]` | Array with extra environment variables to add to the init-kaniko container |
| kaniko.initKanikoContainer.image | string | `"busybox:1.35.0"` | init container image which waits for Dockerfile before starting actual build |
| kaniko.initRepositoryContainer.extraCommandOptions | string | `""` | Configure extra options for command 'aws ecr create-repository' |
| kaniko.initRepositoryContainer.extraEnvVars | list | `[]` | Array with extra environment variables to add to the init-repository container |
| kaniko.initRepositoryContainer.image | string | `"amazon/aws-cli:2.9.4"` | aws-cli image is used to provision non-existing AWS ECR repository |
| kaniko.kanikoContainer.extraEnvVars | list | `[]` | Array with extra environment variables to add to the Kaniko container |
| kaniko.kanikoContainer.image | string | `"gcr.io/kaniko-project/executor:v1.9.1@sha256:7d1923bcf2a9ed1e33e1918c3e208c7a60dd2def7c6bd302cde0c839c5bf5425"` | kaniko image |
| kaniko.kanikoContainer.resources.limits | object | `{}` | The resources limits for the Kaniko containers |
| kaniko.kanikoContainer.resources.requests | object | `{}` | The requested resources for the Kaniko containers |
| kaniko.nodeSelector | object | `{}` | nodeSelector Node labels for pod assignment |
| kaniko.roleArn | string | `nil` | AWS IAM role to be used for kaniko pod service account (IRSA). Format: arn:aws:iam::<AWS_ACCOUNT_ID>:role/<AWS_IAM_ROLE_NAME> |
| kaniko.tolerations | list | `[]` | tolerations Tolerations for pod assignment |
| keycloak-operator.enabled | bool | `true` |  |
| nexus-operator.enabled | bool | `true` |  |
| perf-operator.enabled | bool | `true` |  |
| perf-operator.perf.integration | bool | `false` | Enable PERF integration |
| sonar-operator.enabled | bool | `true` |  |
| vcs.enabled | string | `"false"` |  |

