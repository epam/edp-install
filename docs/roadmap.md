---
hide:
  - navigation
---

# RoadMap

RoadMap consists of three streams:

* [Community](#i-community)
* [Architecture](#ii-architecture)
* [Building Blocks](#iii-building-blocks)
* [Admin Console](#iv-admin-console-ui)
* [Documentation](#v-documentation-as-code)

## I. Community

*Goals:*

* Innovation Through Collaboration
* Improve OpenSource Adoption
* Build Community around technology solutions EDP is built on

## Deliver Operators on OperatorHub

[OperatorHub](https://operatorhub.io/){target=_blank} is a defacto leading solution which consolidates Kubernetes Community around Operators. EDP follows the best practices of delivering Operators in a quick and reliable way. We want to improve Deployment and Management experience for our Customers by publishing [all EDP operators](https://operatorhub.io/?keyword=edp){target=_blank} on this HUB.

Another artifact aggregator which is used by EDP - [ArtifactHub](https://artifacthub.io/packages/search?ts_query_web=edp&sort=relevance&page=1){target=_blank}, that holds description for both components: [stable](https://artifacthub.io/packages/search?repo=epmdedp){target=_blank} and [under-development](https://artifacthub.io/packages/search?repo=epmdedp-dev){target=_blank}.

!!! success "OperatorHub. Keycloak Operator"
    [EDP Keycloak Operator](https://operatorhub.io/operator/edp-keycloak-operator) is now available from OperatorHub both for Upstream (Kubernetes) and OpenShift deployments.

## II. Architecture

*Goals:*

* Improve reusability for EDP components
* Integrate Kubernetes Native Deployment solutions
* Introduce abstraction layer for CI/CD components
* Build processes around the GitOps approach
* Introduce secrets management

### Kubernetes Multitenancy

Multiple instances of EDP are run in a single Kubernetes cluster. One way to achieve this is to use [Multitenancy](https://github.com/kubernetes-sigs/multi-tenancy){target=_blank}. Initially, [Kiosk](https://github.com/loft-sh/kiosk){target=_blank} was selected as tools that provides this capability. An alternative option that EDP Team took into consideration is [Capsule](https://github.com/clastix/capsule){target=_blank}. Another tool which goes far beyond multitenancy is [vcluster](https://github.com/loft-sh/vcluster){target=_blank} going a good candidate for *e2e testing* scenarios where one needs simple *lightweight kubernetes cluster* in CI pipelines.

!!! success "EDP Release 3.5.3"
    The EPAM Delivery Platform (EDP) has added [Capsule](operator-guide/capsule.md) as a general tenant management solution for Kubernetes. Capsule is an open-source operator that enables you to create and manage multiple tenants on a shared Kubernetes cluster, while ensuring resource isolation, security, and governance.

!!! success "EDP Release 3.5.3"
    Vcluster is actively used in EDP for [e2e testing](https://github.com/epam/edp-tekton/blob/master/charts/custom-pipelines/templates/tasks/e2e.yaml) purposes.

### Microservice Reference Architecture Framework

EDP provides basic *Application Templates* for a number of technology stacks (Java, .Net, NPM, Python) and [Helm](https://helm.sh/){target=_blank} is used as a deployment tool. The goal is to extend this library and provide: *Application Templates* which are built on pre-defined architecture patterns (e.g., Microservice, API Gateway, Circuit Breaker, CQRS, Event Driven) and *Deployment Approaches*: Canary, Blue/Green. This requires additional tools installation on cluster as well.

### Policy Enforcement for Kubernetes

Running workload in Kubernetes calls for extra effort from Cluster Administrators to ensure those workloads do follow best practices or specific requirements defined on organization level. Those requirements can be formalized in policies and integrated into: *CI Pipelines* and *Kubernetes Cluster* (through [Admission Controller](https://kubernetes.io/docs/reference/access-authn-authz/admission-controllers/){target=_blank} approach) - to guarantee proper resource management during development and runtime phases.
EDP uses [Open Policy Agent](https://www.openpolicyagent.org/){target=_blank} (from version 2.8.0), since it supports compliance check for more use-cases: Kubernetes Workloads, Terraform and Java code, HTTP APIs and [many others](https://www.openpolicyagent.org/docs/latest/){target=_blank}. [Kyverno](https://kyverno.io/docs/introduction/){target=_blank} is another option being checked in scope of this activity.

### Secrets Management

EDP should provide secrets management as a part of platform. There are multiple tools providing secrets management capabilities. The aim is to be aligned with GitOps and [Operator Pattern](https://kubernetes.io/docs/concepts/extend-kubernetes/operator/){target=_blank} approaches so [HashiCorp Vault](https://www.vaultproject.io/docs/platform/k8s){target=_blank}, [Banzaicloud Bank Vaults](https://github.com/banzaicloud/bank-vaults){target=_blank}, [Bitnami Sealed Secrets](https://github.com/bitnami-labs/sealed-secrets){target=_blank} are currently used for internal projects and some of them should be made publicly available - as a part of EDP Deployment.

!!! success "EDP Release 2.12.x"
    [External Secret Operator](operator-guide/external-secrets-operator-integration.md) is a recommended secret management tool for the EDP components.

### Release Management

[Conventional Commits](https://www.conventionalcommits.org){target=_blank} and [Conventional Changelog](https://github.com/conventional-changelog){target=_blank} are two approaches to be used as part of release process. Today EDP provides only capabilities to manage *Release Branches*. This activity should address this gap by formalizing and implementing *Release Process* as a part of EDP. Topics to be covered: Versioning, Tagging, Artifacts Promotion.

### Kubernetes Native CI/CD Pipelines

EDP has deprecated [Jenkins](https://www.jenkins.io/) in favour of [Tekton](https://tekton.dev). Jenkins is no longer available since EDP v3.4.4.

!!! success "EDP Release 2.12.x"
    [Argo CD](operator-guide/argocd-integration.md) is suggested as a solution providing the `Continuous Delivery` capabilities.

!!! success "EDP Release 3.0"
    [Tekton](operator-guide/install-tekton.md) is used as a CI/CD pipelines orchestration tool on the platform. Review [edp-tekton](http://github.com/epam/edp-tekton) GitHub repository that keeps all the logic behind this solution on the EDP (Pipelines, Tasks, TriggerTemplates, Interceptors, etc). Get acquainted with the series of publications on our [Medium Page](https://medium.com/epam-delivery-platform/part-1-tekton-adoption-d5d47bf1bfc0).

### Advanced EDP Role-based Model

EDP has a number of base roles which are used across EDP. In some cases it is necessary to provide more granular permissions for specific users. It is possible to do this using Kubernetes Native approach.

### Notifications Framework

EDP has a number of components which need to report their statuses: Build/Code Review/Deploy Pipelines, changes in Environments, updates with artifacts. The goal for this activity is to onboard Kubernetes Native approach which provides Notification capabilities with different sources/channels integration (e.g. Email, Slack, MS Teams). Some of these tools are [Argo Events](https://github.com/argoproj/argo-events){target=_blank}, [Botkube](https://www.botkube.io/){target=_blank}.

### Reconciler Component Retirement

Persistent layer, which is based on [edp-db](https://github.com/epam/edp-install/tree/master/deploy-templates/templates/db) (PostgreSQL) and [reconciler](https://github.com/epam/edp-reconciler/) component should be retired in favour of [Kubernetes Custom Resource (CR)](https://kubernetes.io/docs/concepts/extend-kubernetes/api-extension/custom-resources/){target=_blank}. The latest features in EDP are implemented using CR approach.

!!! success "EDP Release 3.0"
    Reconciler component is deprecated and is no longer supported. All the EDP components are migrated to Kubernetes Custom Resources (CR).

## III. Building Blocks

*Goals:*

* Introduce best practices from Microservice Reference Architecture deployment and observability using Kubernetes Native Tools
* Enable integration with the Centralized Test Reporting Frameworks
* Onboard SAST/DAST tool as a part of CI pipelines and Non-Functional Testing activities

!!! success "EDP Release 2.12.x"
    [SAST](operator-guide/overview-sast.md) is introduced as a mandatory part of the [CI Pipelines](user-guide/ci-pipeline-details.md). The [list of currently supported SAST scanners](https://epam.github.io/edp-install/operator-guide/overview-sast/#supported-languages) and the [instruction on how to add them](./operator-guide/add-security-scanner.md) are also available.

### Infrastructure as Code

EDP Target tool for Infrastructure as Code (IaC) is [Terraform](https://www.terraform.io/){target=_blank}. EDP sees two CI/CD scenarios while working with IaC: *Module Development* and *Live Environment Deployment*. Today, EDP provides [basic capabilities (CI Pipelines)](user-guide/terraform-stages.md) for *Terraform Module Development*. At the same time, currently EDP doesn't provide Deployment pipelines for *Live Environments* and the feature is under development. [Terragrunt](https://terragrunt.gruntwork.io/){target=_blank} is an option to use in *Live Environment* deployment. Another Kubernetes Native approach to provision infrastructure components is [Crossplane](https://crossplane.io/){taget=_blank}.

### Database Schema Management

One of the challenges for Application running in Kubernetes is to manage database schema. There are a number of tools which provides such capabilities, e.g. [Liquibase](https://www.liquibase.org/){target=_blank}, [Flyway](https://flywaydb.org/){target=_blank}. Both tools provide versioning control for database schemas. There are different approaches on [how to run migration scripts in Kubernetes](https://www.liquibase.org/blog/using-liquibase-in-kubernetes){target=_blank}: in [init container](https://kubernetes.io/docs/concepts/workloads/pods/init-containers/){target=_blank}, as separate [Job](https://kubernetes.io/docs/concepts/workloads/controllers/job/){target=_blank} or as a separate CD stage. Purpose of this activity is to provide database schema management solution in Kubernetes as a part of EDP. EDP Team investigates [SchemaHero](https://schemahero.io/){target=_blank} tool and use-cases which suits Kubernetes native approach for database schema migrations.

### Open Policy Agent

[Open Policy Agent](https://www.openpolicyagent.org/){target=_blank} is introduced in version [2.8.0](user-guide/opa-stages.md). EDP now supports CI for [Rego Language](https://www.openpolicyagent.org/docs/latest/#rego){target=_blank}, so you can develop your own policies. The next goal is to provide pipeline steps for running compliance policies check for Terraform, Java, Helm Chart as a part of CI process.

### Report Portal

EDP uses [Allure Framework](https://github.com/allure-framework/allure2){target=_blank} as a *Test Report tool*. Another option is to integrate [Report Portal](https://reportportal.io/){target=_blank} into EDP ecosystem.

!!! success "EDP Release 3.0"
    Use [ReportPortal](operator-guide/install-reportportal.md) to consolidate and analyze your Automation tests results. Consult our pages on how to perform [reporting](operator-guide/report-portal-integration-tekton.md) and [Keycloak integration](operator-guide/reportportal-keycloak.md).

### Carrier

[Carrier](https://github.com/carrier-io){target=_blank} provides Non-functional testing capabilities.

### Java 17

EDP supports two LTS versions of Java: 8 and 11. The goal is to provide [Java 17 (LTS)](https://jdk.java.net/17/){target=_blank} support.

!!! success "EDP Release 3.2.1"
    CI Pipelines for [Java 17](features.md) is available in EDP.

### Velero

[Velero](https://velero.io/){target=_blank} is used as a cluster backup tool and is deployed as a part of Platform. Currently, Multitenancy/On-premise support for backup capabilities is in process.

### Istio

[Istio](https://istio.io/latest/){target=_blank} is to be used as a *Service Mesh* and to address challenges for Microservice or Distributed Architectures.

### Kong

[Kong](https://github.com/Kong/kong){target=_blank} is one of tools which is planned to use as an *API Gateway* solution provider. Another possible candidate for investigation is [Ambassador API Gateway](https://www.getambassador.io/products/edge-stack/api-gateway/){target=_blank}

### OpenShift 4.X

EDP supports the [OpenShift 4.9](https://docs.openshift.com/container-platform/4.9/welcome/index.html) platform.

!!! success "EDP Release 2.12.x"
    EDP Platform runs on the latest OKD versions: [4.9](operator-guide/deploy-okd.md) and [4.10](operator-guide/deploy-okd-4.10.md). [Creating the IAM Roles for Service Account](https://docs.openshift.com/container-platform/4.10/authentication/managing_cloud_provider_credentials/cco-mode-sts.html#sts-mode-installing-manual-run-installer_cco-mode-sts) is a recommended way to work with AWS Resources from the OKD cluster.

## IV. Admin Console (UI)

*Goals:*

* Improve UХ for different user types to address their concerns in the delivery model
* Introduce user management capabilities
* Enrich with traceability metrics for products

!!! success "EDP Release 2.12.x"
    EDP Team has introduced a new UI component called [EDP Headlamp](https://github.com/epam/edp-headlamp), which will replace the [EDP Admin Console](user-guide/index.md) in future releases. EDP Headlamp is based on the [Kinvolk Headlamp UI Client](https://github.com/kinvolk/headlamp).

!!! success "EDP Release 3.0"
    [EDP Headlamp](https://github.com/epam/edp-headlamp) is used as a Control Plane UI on the platform.

!!! success "EDP Release 3.4"
    Since EDP v3.4.0, Headlamp UI has been renamed to EDP Portal.

### Users Management

EDP uses [Keycloak](https://www.keycloak.org/){target=_blank} as an Identity and Access provider. EDP roles/groups are managed inside the Keycloak realm, then these changes are propagated across the EDP Tools. We plan to provide this functionality in EDP Portal using the Kubernetes-native approach (Custom Resources).

### The Delivery Pipelines Dashboard

The [CD Pipeline section](user-guide/add-cd-pipeline.md) in EDP Portal provides basic information, such as environments, artifact versions deployed per each environment, and direct links to the namespaces. One option is to enrich this panel with metrics from the Prometheus, custom resources, or events. Another option is to use the existing dashboards and expose EDP metrics to them, for example, [plugin for Lens](https://github.com/lensapp/lens-extensions){target=_blank} or [OpenShift UI Console](https://github.com/openshift/console){target=_blank}.

### Split Jira and Commit Validation Sections

[Commit Validate step](user-guide/pipeline-stages.md#stages-description) was initially designed to be aligned with [Jira Integration](operator-guide/jira-integration.md) and cannot be used as single feature. Target state is to ensure features *CommitMessage Validation* and *Jira Integration* both can be used independently. We also want to add support for [Conventional Commits](https://www.conventionalcommits.org){target=_blank}.

!!! success "EDP Release 3.2.0"
    [EDP Portal](https://github.com/epam/edp-headlamp) has separate sections for [Jira Integration](operator-guide/jira-integration.md) and CommitMessage Validation step.

## V. Documentation as Code

*Goals:*

* Transparent documentation and clear development guidelines for EDP customization.
* Components that provide Documentation as Code feature should be integrated into EDP.

!!! success "EDP Release 3.4.0"
    Antora was introduced as framework that provides Documentation as Code capabilities.

Consolidate documentation in a single repository [edp-install](https://github.com/epam/edp-install/docs), use `mkdocs` tool to generate docs and GitHub Pages as a hosting solution.
