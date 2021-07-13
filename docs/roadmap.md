---
hide:
  - navigation
---

# RoadMap

RoadMap consists of three streams:

* [Architecture](#i-architecture)
* [Building Blocks](#ii-building-blocks)
* [Admin Console](#iii-admin-console-ui)
* [Documentation](#iv-documentation-as-code)

## I. Architecture

_Goals:_

* Improve reusability for EDP components
* Integrate Kubernetes Native Deployment solutions
* Introduce abstraction layer for CI/CD components
* Build processes around the GitOps approach
* Introduce secrets management

### Kubernetes Multitenancy

Multiple instances of EDP are run in a single Kubernetes cluster. One way to achieve this is to use [Multitenancy](https://github.com/kubernetes-sigs/multi-tenancy){target=_blank}. Initially, [Kiosk](https://github.com/loft-sh/kiosk){target=_blank} was selected as tools that provides this capability. An alternative option that EDP Team took into consideration is [Capsule](https://github.com/clastix/capsule){target=_blank}. Another tool which goes far beyond multitenancy is [vcluster](https://github.com/loft-sh/vcluster){target=_blank} going a good candidate for *e2e testing* scenarios where one needs simple *lightweight kubernetes cluster* in CI pipelines.

### Microservice Reference Architecture Framework

EDP provides basic *Application Templates* for a number of technology stacks (Java, .Net, NPM, Python) and [Helm](https://helm.sh/){target=_blank} is used as a deployment tool. The goal is to extend this library and provide: *Application Templates* which are built on pre-defined architecture patterns (e.g., Microservice, API Gateway, Circuit Breaker, CQRS, Event Driven) and *Deployment Approaches*: Canary, Blue/Green. This requires additional tools installation on cluster as well.

### Policy Enforcement for Kubernetes

Running workload in Kubernetes calls for extra effort from Cluster Administrators to ensure those workloads do follow best practices or specific requirements defined on organization level. Those requirements can be formalized in policies and integrated into: *CI Pipelines* and *Kubernetes Cluster* (through [Admission Controller](https://kubernetes.io/docs/reference/access-authn-authz/admission-controllers/){target=_blank} approach) - to guarantee proper resource management during development and runtime phases.
EDP uses [Open Policy Agent](https://www.openpolicyagent.org/){target=_blank} (from version 2.8.0), since it supports compliance check for more use-cases: Kubernetes Workloads, Terraform and Java code, HTTP APIs and [many others](https://www.openpolicyagent.org/docs/latest/){target=_blank}. [Kyverno](https://kyverno.io/docs/introduction/){target=_blank} is another option being checked in scope of this activity.

### Secrets Management

EDP should provide secrets management as a part of platform. There are multiple tools providing secrets management capabilities. The aim is to be aligned with GitOps and [Operator Pattern](https://kubernetes.io/docs/concepts/extend-kubernetes/operator/){target=_blank} approaches so [HashiCorp Vault](https://www.vaultproject.io/docs/platform/k8s){target=_blank}, [Banzaicloud Bank Vaults](https://github.com/banzaicloud/bank-vaults){target=_blank}, [Bitnami Sealed Secrets](https://github.com/bitnami-labs/sealed-secrets){target=_blank} are currently used for internal projects and some of them should be made publicly available - as a part of EDP Deployment.

### Release Management

[Conventional Commits](https://www.conventionalcommits.org){target=_blank} and [Conventional Changelog](https://github.com/conventional-changelog){target=_blank} are two approaches to be used as part of release process. Today EDP provides only capabilities to manage *Release Branches*. This activity should address this gap by formalizing and implementing *Release Process* as a part of EDP. Topics to be covered: Versioning, Tagging, Artifacts Promotion.

### Kubernetes Native CI/CD Pipelines

EDP uses [Jenkins](https://www.jenkins.io/) as Pipeline Orchestrator. Jenkins runs workload for CI and CD parts. There is also basic support for [GitLab CI](https://docs.gitlab.com/ee/ci/){target=_blank}, but it provides Docker image build functionality only. EDP works on providing an alternative to Jenkins and use Kubernetes Native Approach for pipeline management. There are a number of tools, which provides such capability:

* [ArgoCD](https://argo-cd.readthedocs.io/en/stable/){target=_blank}
* [Argo Workflows](https://argoproj.github.io/argo-workflows/){target=_blank}
* [Argo Rollouts](https://argoproj.github.io/argo-rollouts/){target=_blank}
* [Tekton](https://tekton.dev/){target=_blank}
* [Drone](https://www.drone.io/){target=_blank}
* [Flux](https://fluxcd.io/){target=_blank}

This list is under investigation and solution is going to be implemented in two steps:

1. Introduce tool that provide *Continues Deployment* approach. ArgoCD is one of the best to go with.
2. Integrate EDP with tool that provides *Continues Integration* capabilities.

### Advanced EDP Role-based Model

EDP has a number of base roles which are used across EDP. In some cases it is necessary to provide more granular permissions for specific users. It is possible to do this using Kubernetes Native approach.

### Notifications Framework

EDP has a number of components which need to report their statuses: Build/Code Review/Deploy Pipelines, changes in Environments, updates with artifacts. The goal for this activity is to onboard Kubernetes Native approach which provides Notification capabilities with different sources/channels integration (e.g. Email, Slack, MS Teams). Some of these tools are [Argo Events](https://github.com/argoproj/argo-events){target=_blank}, [Botkube](https://www.botkube.io/){target=_blank}.

### Reconciler Component Retirement

Persistent layer, which is based on [edp-db](https://github.com/epam/edp-install/tree/master/deploy-templates/templates/db) (PostgreSQL) and [reconciler](https://github.com/epam/edp-reconciler/) component should be retired in favour of [Kubernetes Custom Resource (CR)](https://kubernetes.io/docs/concepts/extend-kubernetes/api-extension/custom-resources/){target=_blank}. The latest features in EDP are implemented using CR approach.

## II. Building Blocks

_Goals:_

* Introduce best practices from Microservice Reference Architecture deployment and observability using Kubernetes Native Tools
* Enable integration with the Centralized Test Reporting Frameworks
* Onboard SAST/DAST tool as a part of CI pipelines and Non-Functional Testing activities

### Infrastructure as Code

EDP Target tool for Infrastructure as Code (IaC) is [Terraform](https://www.terraform.io/){target=_blank}. EDP sees two CI/CD scenarios while working with IaC: *Module Development* and *Live Environment Deployment*. Today, EDP provides [basic capabilities (CI Pipelines)](./user-guide/terraform-stages.md) for *Terraform Module Development*. At the same time, currently EDP doesn't provide Deployment pipelines for *Live Environments* and the feature is under development. [Terragrunt](https://terragrunt.gruntwork.io/){target=_blank} is an option to use in *Live Environment* deployment. Another Kubernetes Native approach to provision infrastructure components is [Crossplane](https://crossplane.io/){taget=_blank}.

### Database Schema Management

One of the challenges for Application running in Kubernetes is to manage database schema. There are a number of tools which provides such capabilities, e.g. [Liquibase](https://www.liquibase.org/){target=_blank}, [Flyway](https://flywaydb.org/){target=_blank}. Both tools provide versioning control for database schemas. There are different approaches on [how to run migration scripts in Kubernetes](https://www.liquibase.org/blog/using-liquibase-in-kubernetes){target=_blank}: in [init container](https://kubernetes.io/docs/concepts/workloads/pods/init-containers/){target=_blank}, as separate [Job](https://kubernetes.io/docs/concepts/workloads/controllers/job/){target=_blank} or as a separate CD stage. Purpose of this activity is to provide database schema management solution in Kubernetes as a part of EDP. EDP Team investigates [SchemaHero](https://schemahero.io/){target=_blank} tool and use-cases which suits Kubernetes native approach for database schema migrations.

### Open Policy Agent

[Open Policy Agent](https://www.openpolicyagent.org/){target=_blank} is introduced in version [2.8.0](./user-guide/opa-stages.md). EDP now supports CI for [Rego Language](https://www.openpolicyagent.org/docs/latest/#rego){target=_blank}, so you can develop your own policies. The next goal is to provide pipeline steps for running compliance policies check for Terraform, Java, Helm Chart as a part of CI process.

### Report Portal

EDP uses [Allure Framework](https://github.com/allure-framework/allure2){target=_blank} as a *Test Report tool*. Another option is to integrate [Report Portal](https://reportportal.io/){target=_blank} into EDP ecosystem.

### Carrier

[Carrier](https://github.com/carrier-io){target=_blank} provides Non-functional testing capabilities.

### Java 17

EDP supports two LTS versions of Java: 8 and 11. The goal is to provide [Java 17 (LTS)](https://jdk.java.net/17/){target=_blank} support.

### Velero

[Velero](https://velero.io/){target=_blank} is used as a cluster backup tool and is deployed as a part of Platform. Currently, Multitenancy/On-premise support for backup capabilities is in process.

### Istio

[Istio](https://istio.io/latest/){target=_blank} is to be used as a *Service Mesh* and to address challenges for Microservice or Distributed Architectures.

### Kong

[Kong](https://github.com/Kong/kong){target=_blank} is one of tools which is planned to use as an *API Gateway* solution provider. Another possible candidate for investigation is [Ambassador API Gateway](https://www.getambassador.io/products/edge-stack/api-gateway/){target=_blank}

### OpenShift 4.X

[OpenShift 4.6](https://docs.openshift.com/container-platform/4.6/welcome/index.html) is a platform that EDP supports.

## III. Admin Console (UI)

_Goals:_

* Improve UХ for different user types to address their concerns in the delivery model
* Introduce user management capabilities
* Enrich with traceability metrics for products

### Users Management

EDP uses [Keycloak](https://www.keycloak.org/){target=_blank} as Identity and Access provider. EDP roles/groups are managed inside Keycloak realm, then these changes are propagated across EDP Tools. The plan is to provide this functionality in EDP Admin Console using Kubernetes native approach (Custom Resources).

### The Delivery Pipelines Dashboard

EDP [CD Pipeline section](./user-guide/add-cd-pipeline.md) in Admin Console provides basic information like: environments, artifact versions deployed per each environment, direct links to namespaces. One option is to enrich this panel with metrics (from prometheus, custom resources, events, etc). Another option is to use existing dashboards and expose EDP metrics to them, e.g. [plugin for Lens](https://github.com/lensapp/lens-extensions){target=_blank}, [OpenShift UI Console](https://github.com/openshift/console){target=_blank}

### Split Jira and Commit Validation Sections

[Commit Validate step](../user-guide/pipeline-stages/#stages-description) was initially designed to be aligned with [Jira Integration](./operator-guide/jira-integration.md) and cannot be used as single feature. Target state is to ensure features *CommitMessage Validation* and *Jira Integration* both can be used independently. We also want to add support for [Conventional Commits](https://www.conventionalcommits.org){target=_blank}.

## IV. Documentation as Code

_Goal:_

* Transparent documentation and clear development guidelines for EDP customization.

Consolidate documentation in a single repository [edp-install](https://github.com/epam/edp-install/docs), use `mkdocs` tool to generate docs and GitHub Pages as a hosting solution.
