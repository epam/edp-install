# edp-install

![Version: 2.11.0](https://img.shields.io/badge/Version-2.11.0-informational?style=flat-square) ![Type: application](https://img.shields.io/badge/Type-application-informational?style=flat-square) ![AppVersion: 2.11.0](https://img.shields.io/badge/AppVersion-2.11.0-informational?style=flat-square)

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
| @epamedp | admin-console-operator | 2.11.0 |
| @epamedp | cd-pipeline-operator | 2.11.0 |
| @epamedp | codebase-operator | 2.12.0 |
| @epamedp | edp-component-operator | 0.11.0 |
| @epamedp | gerrit-operator | 2.11.0 |
| @epamedp | jenkins-operator | 2.11.1 |
| @epamedp | keycloak-operator | 1.11.0 |
| @epamedp | nexus-operator | 2.11.0 |
| @epamedp | perf-operator | 2.11.0 |
| @epamedp | reconciler | 2.11.0 |
| @epamedp | sonar-operator | 2.11.0 |

## Values

| Key | Type | Default | Description |
|-----|------|---------|-------------|
| EDPComponents | object | `{}` |  |
| admin-console-operator.enabled | bool | `true` |  |
| annotations | object | `{}` |  |
| argocd.enabled | bool | `false` | Enable ArgoCD integration |
| argocd.url | string | `""` (defaults to https://argocd.{{ .Values.global.dnsWildCard }}) | ArgoCD URL in format schema://URI |
| awsRegion | string | `nil` |  |
| cd-pipeline-operator.enabled | bool | `true` |  |
| codebase-operator.enabled | bool | `true` |  |
| dockerRegistry.url | string | `"<AWS_ACCOUNT_ID>.dkr.ecr.<AWS_REGION>.amazonaws.com"` |  |
| gerrit-operator.enabled | bool | `true` |  |
| global.admins[0] | string | `"stub_user_one@example.com"` |  |
| global.database.affinity | object | `{}` |  |
| global.database.annotations | object | `{}` |  |
| global.database.deploy | bool | `true` |  |
| global.database.host | string | `"edp-db"` |  |
| global.database.image | string | `"postgres:9.6"` |  |
| global.database.imagePullPolicy | string | `"IfNotPresent"` |  |
| global.database.name | string | `"edp-db"` |  |
| global.database.nodeSelector | object | `{}` |  |
| global.database.port | int | `5432` |  |
| global.database.resources.limits.memory | string | `"512Mi"` |  |
| global.database.resources.requests.cpu | string | `"50m"` |  |
| global.database.resources.requests.memory | string | `"64Mi"` |  |
| global.database.storage.class | string | `"gp2"` |  |
| global.database.storage.size | string | `"2Gi"` |  |
| global.database.tolerations | list | `[]` |  |
| global.developers[0] | string | `"stub_user_one@example.com"` |  |
| global.developers[1] | string | `"stub_user_two@example.com"` |  |
| global.dnsWildCard | string | `"stub.com"` |  |
| global.edpName | string | `"stub-namespace"` |  |
| global.kioskEnabled | bool | `true` |  |
| global.platform | string | `"kubernetes"` |  |
| global.version | string | `"2.11.1"` |  |
| global.webConsole.url | string | `nil` |  |
| jenkins-operator.enabled | bool | `true` |  |
| kaniko.existingDockerConfig | string | `nil` | Existing secret which contains docker-config, if not defined then 'kaniko-docker-config' will be created with default value: { "credStore": "ecr-login"} |
| kaniko.initKanikoContainer.extraEnvVars | list | `[]` | Array with extra environment variables to add to the init-kaniko container |
| kaniko.initKanikoContainer.image | string | `"busybox:1.35.0"` | init container image which waits for Dockerfile before starting actual build |
| kaniko.initRepositoryContainer.extraCommandOptions | string | `""` | Configure extra options for command 'aws ecr create-repository' |
| kaniko.initRepositoryContainer.extraEnvVars | list | `[]` | Array with extra environment variables to add to the init-repository container |
| kaniko.initRepositoryContainer.image | string | `"amazon/aws-cli:2.5.2"` | aws-cli image is used to provision non-existing AWS ECR repository |
| kaniko.kanikoContainer.extraEnvVars | list | `[]` | Array with extra environment variables to add to the Kaniko container |
| kaniko.kanikoContainer.image | string | `"gcr.io/kaniko-project/executor:v1.8.0"` | kaniko image |
| kaniko.kanikoContainer.resources.limits | object | `{}` | The resources limits for the Kaniko containers |
| kaniko.kanikoContainer.resources.requests | object | `{}` | The requested resources for the Kaniko containers |
| kaniko.nodeSelector | object | `{}` | nodeSelector Node labels for pod assignment |
| kaniko.roleArn | string | `nil` | AWS IAM role to be used for kaniko pod servce account (IRSA) |
| kaniko.tolerations | list | `[]` | tolerations Tolerations for pod assignment |
| keycloak-operator.enabled | bool | `true` |  |
| keycloak-operator.keycloak.url | string | `"keycloak.example.com"` |  |
| nexus-operator.enabled | bool | `true` |  |
| perf-operator.enabled | bool | `true` |  |
| perf.enabled | string | `"false"` |  |
| reconciler.enabled | bool | `true` |  |
| sonar-operator.enabled | bool | `true` |  |
| vcs.enabled | string | `"false"` |  |

