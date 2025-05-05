# edp-install

![Version: 3.12.0-SNAPSHOT](https://img.shields.io/badge/Version-3.12.0--SNAPSHOT-informational?style=flat-square) ![Type: application](https://img.shields.io/badge/Type-application-informational?style=flat-square) ![AppVersion: 3.12.0-SNAPSHOT](https://img.shields.io/badge/AppVersion-3.12.0--SNAPSHOT-informational?style=flat-square)

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
| @epamedp | cd-pipeline-operator | 2.25.1 |
| @epamedp | codebase-operator | 2.27.2 |
| @epamedp | edp-headlamp | 0.22.0 |
| @epamedp | edp-tekton | 0.18.0 |
| @epamedp | gerrit-operator | 2.23.1 |

## Values

| Key | Type | Default | Description |
|-----|------|---------|-------------|
| annotations | object | `{}` |  |
| cd-pipeline-operator.capsuleTenant | object | `{"create":true,"spec":null}` | Required tenancyEngine: capsule. Specify Capsule Tenant specification for Environments. |
| cd-pipeline-operator.enabled | bool | `true` |  |
| cd-pipeline-operator.manageNamespace | bool | `true` | should the operator manage(create/delete) namespaces for stages Refer to the guide for managing namespace (https://docs.kuberocketci.io/docs/operator-guide/auth/namespace-management) |
| cd-pipeline-operator.secretManager | string | `"own"` | Flag indicating whether the operator should manage secrets for stages. This parameter controls the provisioning of the 'regcred' secret within deployed environments, facilitating access to private container registries. Set the parameter to "none" under the following conditions:   - If 'global.dockerRegistry.type=ecr' and IRSA is enabled, or   - If 'global.dockerRegistry.type=openshift'. For private registries, choose the most appropriate method to provide credentials to deployed environments. Refer to the guide for managing container registries (https://docs.kuberocketci.io/docs/user-guide/manage-container-registries). Possible values: own/eso/none.   - own: Copies the secret once from the parent namespace, without subsequent reconciliation. If updated in the parent namespace, manual updating in all created namespaces is required.   - eso: The secret will be managed by the External Secrets Operator (requires installation and configuration in the cluster: https://docs.kuberocketci.io/docs/operator-guide/secrets-management/install-external-secrets-operator).   - none: Disables secrets management logic. |
| cd-pipeline-operator.serviceAccount.annotations | object | `{}` |  |
| cd-pipeline-operator.tenancyEngine | string | `"none"` | Defines the type of the tenant engine that can be "none", "kiosk" or "capsule"; for Stages with external cluster tenancyEngine will be ignored. Default: none |
| codebase-operator.enabled | bool | `true` |  |
| edp-headlamp.config.baseURL | string | `""` | base url path at which headlamp should run |
| edp-headlamp.config.oidc | object | `{"clientID":"","clientSecretKey":"clientSecret","clientSecretName":"keycloak-client-headlamp-secret","enabled":false,"issuerUrl":"","scopes":""}` | For detailed instructions, refer to: https://docs.kuberocketci.io/docs/operator-guide/auth/configure-keycloak-oidc-eks, https://docs.kuberocketci.io/docs/operator-guide/auth/ui-portal-oidc |
| edp-headlamp.config.oidc.clientID | string | `""` | OIDC client ID |
| edp-headlamp.config.oidc.clientSecretKey | string | `"clientSecret"` | OIDC client secret key |
| edp-headlamp.config.oidc.clientSecretName | string | `"keycloak-client-headlamp-secret"` | OIDC client secret name |
| edp-headlamp.config.oidc.issuerUrl | string | `""` | Azure Entra: https://sts.windows.net/<tenant-id>/ |
| edp-headlamp.config.oidc.scopes | string | `""` | OIDC scopes to be used |
| edp-headlamp.enabled | bool | `true` |  |
| edp-headlamp.ingress.annotations | object | `{}` | Annotations for Ingress resource |
| edp-headlamp.ingress.enabled | bool | `true` | Enable external endpoint access. Default Ingress/Route host pattern: portal-{{ .Release.Namespace }}.{{ .Values.global.dnsWildCard }} |
| edp-headlamp.ingress.tls | list | `[]` | Ingress TLS configuration |
| edp-tekton.enabled | bool | `true` |  |
| edp-tekton.gitServers | object | `{}` |  |
| edp-tekton.grafana | object | `{"enabled":false}` | https://docs.kuberocketci.io/docs/operator-guide/ci/tekton-monitoring |
| edp-tekton.interceptor.enabled | bool | `true` | Deploy KubeRocketCI interceptor as a part of pipeline library when true. Default: true |
| edp-tekton.pipelines.deployableResources | object | `{"c":{"cmake":true,"make":true},"cs":{"dotnet3.1":false,"dotnet6.0":false},"deploy":true,"docker":true,"gitops":true,"go":{"beego":true,"gin":true,"operatorsdk":true},"groovy":true,"helm":true,"helm-pipeline":true,"infrastructure":true,"java":{"java11":true,"java17":true,"java21":true,"java8":false},"js":{"angular":true,"antora":true,"express":true,"next":true,"react":true,"vue":true},"opa":false,"python":{"ansible":true,"fastapi":true,"flask":true,"python3.8":false},"security":true,"tasks":true,"terraform":true}` | This section contains the list of pipelines and tasks that will be installed. |
| edp-tekton.pipelines.deployableResources.c | object | `{"cmake":true,"make":true}` | This section control the installation of the review and build pipelines. |
| edp-tekton.pipelines.deployableResources.deploy | bool | `true` | This flag control the installation of the Deploy pipelines. |
| edp-tekton.pipelines.deployableResources.tasks | bool | `true` | This flag control the installation of the tasks. |
| edp-tekton.pipelines.image.registry | string | `"docker.io"` | Registry for tekton pipelines images. Default: docker.io |
| edp-tekton.pipelines.imagePullSecrets | list | `[]` | List of image pull secrets used by the Tekton ServiceAccount for pulling images from private registries. Example: imagePullSecrets:   - name: regcred |
| edp-tekton.pipelines.podTemplate | list | `[]` | This section allows to determine on which nodes to run tekton pipelines |
| edp-tekton.tekton-cache.enabled | bool | `true` |  |
| edp-tekton.tekton.pruner.create | bool | `true` |  |
| externalSecrets.enabled | bool | `false` | Configure External Secrets for KubeRocketCI platform. Deploy SecretStore. Default: false |
| externalSecrets.manageCodemieSecretsName | string | `"/edp/codemie-secrets"` |  |
| externalSecrets.manageEDPInstallSecrets | bool | `true` | Create necessary secrets for KubeRocketCI installation, using External Secret Operator |
| externalSecrets.manageEDPInstallSecretsName | string | `"/edp/deploy-secrets"` | Value name in AWS ParameterStore or AWS SecretsManager. Used when manageEDPInstallSecrets is true |
| externalSecrets.manageGitProviderSecretsName | string | `"/edp/git-provider-secrets"` |  |
| externalSecrets.secretProvider.aws.region | string | `"eu-central-1"` | AWS Region where secrets are stored, e.g. eu-central-1 |
| externalSecrets.secretProvider.aws.role | string | `nil` | IAM Role to be used for Accessing AWS either Parameter Store or Secret Manager. Format: arn:aws:iam::<AWS_ACCOUNT_ID>:role/<AWS_IAM_ROLE_NAME> |
| externalSecrets.secretProvider.aws.service | string | `"ParameterStore"` | Use AWS as a Secret Provider. Can be ParameterStore or SecretsManager |
| externalSecrets.secretProvider.generic.secretStore.name | string | `"example-secret-store"` | Defines SecretStore name. |
| externalSecrets.secretProvider.generic.secretStore.providerConfig | object | `{}` | Defines SecretStore provider configuration. |
| externalSecrets.type | string | `"aws"` | Defines provider type. One of `aws` or `generic`. |
| extraObjects | list | `[]` | Array of extra K8s manifests to deploy |
| extraQuickLinks | object | `{}` | Define extra Quick Links, more details: https://github.com/epam/edp-codebase-operator/ |
| gerrit-operator.enabled | bool | `false` |  |
| global.adminGroupName | string | `""` |  |
| global.apiClusterEndpoint | string | `""` | API Сluster Endpoint configuration for static kubeconfig generation |
| global.apiGatewayUrl | string | `""` | API Gateway URL configuration for Widget Functionality |
| global.availableClusters | string | `""` | Define the list of available remote clusters to deploy applications. Example: "cluster1, cluster2, cluster3" |
| global.developerGroupName | string | `""` |  |
| global.dnsWildCard | string | `nil` | a cluster DNS wildcard name |
| global.dockerRegistry.awsRegion | string | `""` | Defines the geographic area where the (AWS) Elastic Container Registry repository is hosted (optional). E.g. "eu-central-1". Mandatory if 'global.dockerRegistry.type=ecr' for kaniko build-task. Ref: https://github.com/epam/edp-tekton/blob/release/0.10/charts/pipelines-library/templates/tasks/kaniko.yaml#L73 |
| global.dockerRegistry.space | string | `""` | Defines project name. |
| global.dockerRegistry.type | string | `""` | Defines type of registry. One of `ecr`, `harbor`, `dockerhub`, `openshift`, `nexus` or `ghcr`. 'openshift' registry is available only in case if platform is deployed on the OpenShift cluster and the variable global.platform is set to 'openshift'. |
| global.dockerRegistry.url | string | `""` | Defines registry endpoint URL. |
| global.gitProviders | list | `["github"]` | Can be gerrit, github, gitlab or bitbucket. Default: github |
| global.platform | string | `"kubernetes"` | platform type that can be "kubernetes" or "openshift" |
| global.viewerGroupName | string | `""` |  |
| marketplaceTemplates | object | `{"enabled":true}` | This block is a configuration section that controls the creation of the default marketplace templates. |
| quickLinks | object | `` | Define platform Quick Links, more details: https://github.com/epam/edp-codebase-operator/ Example: "https://argocd.example.com" |
| quickLinks.logging.provider | string | `""` | Define the provider name for correct URL generation. Available providers: "opensearch", "datadog". If the provider name is not specified, the base URL will be used. |
| quickLinks.monitoring.provider | string | `""` | Define the provider name for correct URL generation. Available providers: "grafana", "datadog". If the provider name is not specified, the base URL will be used. |

