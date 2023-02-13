# EDP Releases

## Overview

Get acquainted with the latest EDP releases.

* [Version 3.1.0](#3.1.0)
* [Version 3.0.0](#3.0.0)
* [Version 2.12.2](#2.12.2)
* [Version 2.12.1](#2.12.1)
* [Version 2.12.0](#2.12.0)
* [Version 2.11.0](#2.11.0)
<details>
  <summary>Earlier Versions</summary>

* [Version 2.10.2](#2.10.2)
* [Version 2.10.1](#2.10.1)
* [Version 2.10.0](#2.10.0)
* [Version 2.9.0](#2.9.0)
* [Version 2.8.4](#2.8.4)
* [Version 2.8.3](#2.8.3)
* [Version 2.8.2](#2.8.2)
* [Version 2.8.1](#2.8.1)
* [Version 2.8.0](#2.8.0)
* [Version 2.7.8](#2.7.8)
* [Version 2.7.7](#2.7.7)
* [Version 2.7.6](#2.7.6)
* [Version 2.7.5](#2.7.5)
* [Version 2.7.4](#2.7.4)
* [Version 2.7.2](#2.7.2)
* [Version 2.7.1](#2.7.1)
* [Version 2.7.0](#2.7.0)
* [Version 2.6.4](#2.6.4)
* [Version 2.6.3](#2.6.3)
* [Version 2.6.2](#2.6.2)
* [Version 2.6.1](#2.6.1)
* [Version 2.6.0](#2.6.0)
</details>

## Version 2.12.2 <a name="2.12.2"></a> (February 13, 2023)

### Features

* Gerrit and Jenkins Operators now can manage respective resources through custom URL.
* The basePath key can be indicated in the Gerrit Operator custom resource to form gerritApiUrl.

### Fixed Issues

* Fix Gerrit project syncer and controller conflict in the Gerrit Operator to reduce the delay during the multiple projects sync.
* Fix Jira project info error handler to work correctly with non-existing tickets.

## Version 3.1.0 <a name="3.1.0"></a> (January 24, 2023)

### Upgrades

* Update Tekton Operator to the [v0.64.0](https://tekton.dev/docs/operator/) version.

### New Functionality

* Gerrit and Jenkins Operators now can manage respective resources through custom URL.
* Provide the ability to install `kiosk helm chart` for users using helmfile.
* [Headlamp] Ensure secret is created in correct format for GitServer.
* Use Argo CD for the deployment of an application added with the Import strategy.

### Enhancements

* [Headlamp] Disable 'Create' and 'Clone' button for Import strategy.
* [Headlamp] Merge Applications, Libraries, and Autotests into Components section.
* [Headlamp] Zoom in the tooltips for a better view.
* `Headlamp` creates a Tekton PipelineRun with a name that consists of an application name and a branch.
* Remove duplicate parameters for configuring `perf-operator` in Helm chart.
* Deprecated Kubernetes resources for `edp-db` have been removed from the `edp-install` helm chart.

### Fixed Issues

* Fix the consideration of the `commit hash validity` during the promotion of a new codebase branch.
* Tekton CI pipelines generate Jira `fixVersion` in lowercase, previously both uppercase and lowercase names were allowed causing an error.
* [Headlamp] Fix the usage of autotests as a part of quality gate.
* [Headlamp] Fix the description in the `Relative Path` field.
* [Headlamp] Fix the commit message pattern for Jira integration.
* [Headlamp] Fix the font size for the Components page titles.

### Documentation

* The [Operator Guide](https://epam.github.io/edp-install/operator-guide/) is updated with the following:
  * The [Install Tekton](https://epam.github.io/edp-install/operator-guide/install-tekton/) page is updated.
  * The [Manage Jenkins CI Pipeline Job Provisioner](https://epam.github.io/edp-install/operator-guide/manage-jenkins-ci-job-provision/) page is updated.
  * The [Install ReportPortal](https://epam.github.io/edp-install/operator-guide/install-reportportal/) page is updated.
  * The [Install via Helmfile](https://epam.github.io/edp-install/operator-guide/install-via-helmfile/) page is updated.
  * The [Install EDP](https://epam.github.io/edp-install/operator-guide/install-edp/) page is updated.
  * The [EDP Installation Prerequisites Overview](https://epam.github.io/edp-install/operator-guide/prerequisites/) page is added.
  * The [Enable VCS Import Strategy](https://epam.github.io/edp-install/operator-guide/import-strategy/) page is updated.
  * The [Add a Custom Global Pipeline Library](https://epam.github.io/edp-install/user-guide/add-custom-global-pipeline-lib/) page is updated.
  * The [Set Up Kubernetes](https://epam.github.io/edp-install/operator-guide/kubernetes-cluster-settings/) page is updated.
  * The [Set Up OpenShift](https://epam.github.io/edp-install/operator-guide/openshift-cluster-settings/) page is updated.
  * The [Install Keycloak](https://epam.github.io/edp-install/operator-guide/install-keycloak/) page is updated.
  * The [Argo CD Integration](https://epam.github.io/edp-install/operator-guide/argocd-integration/) page is updated.
  * The [Upgrade EDP v.2.11.x to v.2.12.x](https://epam.github.io/edp-install/operator-guide/upgrade-edp-2.11.x-to-2.12.x/) page is updated.
  * The [Upgrade EDP v.2.12.x to v.3.0.x](https://epam.github.io/edp-install/operator-guide/upgrade-edp-2.12.x-to-3.0.x/) page is added.

* The [Quick Start](https://epam.github.io/edp-install/getting-started/) page is updated.

## Version 3.0.0 <a name="3.0.0"></a> (December 19, 2022)

## What's New

In EDP 3.0.0, Tekton is used alongside Jenkins for building, testing, and deploying application components. Tekton is a cloud-native CI/CD solution working seamlessly with Kubernetes and OpenShift Container Platform.

Argo CD is integrated with EDP and allows using GitOps approach for Kubernetes application deployment.

The Keycloak operator is now available on the OperatorHub. Now this operator can be installed on the OpenShift cluster using the OperatorHub installation approach.

EDP Headlamp UI tool is now used as a new EDP dashboard. Because of the EDP Headlamp implementation, the edp-admin-console and edp-reconciler tools are deprecated and completely removed.

EDP suggests ReportPortal as a primary test result aggregation tool. The ReportPortal tool integration with EDP allows categorizing the automated test results and reduce test results analysis efforts using built-in analytics features and Machine Learning.

Explore the upgrades, new functionality, breaking changes and improvements below.

### Upgrades

* Kubectl is updated to the [1.24.3](https://github.com/kubernetes/kubernetes/blob/master/CHANGELOG/CHANGELOG-1.24.md#v1243) version.
* Helm is updated to the [3.10.2](https://github.com/helm/helm/releases/tag/v3.10.2) version.
* Gerrit is updated to the [3.6.2](https://www.gerritcodereview.com/3.6.html#362) version.
* Jenkins is updated to the [2.375.1](https://www.jenkins.io/doc/upgrade-guide/2.375/) version, as well as Jenkins plugins are updated to the latest stable versions.
* SonarQube is updated to the [8.9.10](https://www.sonarqube.org/downloads/) version.
* Nexus is updated to the [3.43.0](https://github.com/sonatype/nexus-public/releases) version.
* Argo CD is updated to the [2.5.3](https://github.com/argoproj/argo-cd/releases/tag/v2.5.3) version.
* Keycloak is updated to the [20.0.1](https://www.keycloak.org/archive/downloads-20.0.1.html) version.
* Golang is updated to the [1.19.3](https://pkg.go.dev/golang.org/dl/go1.19.3) version.
* DefectDojo is updated to the [2.17.0](https://github.com/DefectDojo/django-DefectDojo/blob/master/helm/defectdojo/Chart.yaml) version.

### New Functionality

* The `edp-tekton` is enabled as a EDP subcomponent and a part of the EDP deployment pipeline.
* The web and desktop versions of EDP Headlamp are implemented.
* The `keycloak-operator` is published on the OperatorHub. The Keycloak client is updated alongside with the KeycloakClient custom resource.
* The ReportPortal functionality is integrated into EDP. The ReportPortal deployment is added into the Helmfile.
* The Argo CD functionality is integrated into EDP. Argo CD application is deployed in the edp-tenant and Gerrit Argo CD user is created.
* The `basePath` variable is added to the `gerrit-operator` to form the `gerritApiUrl`.

### Enhancements

* Dotnet 2.1 is no longer supported.
* The `edp-reconciler` is no longer supported. All V1 API handlers are switched to V2 version and database resources are removed from the deployment templates.
* The `edp-admin-console` is no longer supported. All REST and WebUI handlers are deprecated and database resources are removed from the deployment templates.
* The `edp-argocd-operator` is no longer supported.
* The process of resource updating operations is refactored for the codebase controller, thus shortening the reconciliation period.
* Keycloak adapter client structure is updated when updating `KeycloakClient` custom resource.
* New approach to SonarQube plugins installation is implemented in the `sonar-operator` repository.
* By default, anonymous statistics for SonarQube deployment is disabled.

### Fixed Issues

* The `create-release` job possible failure is fixed on the `create-branch` step.
* The `codebase-operator` leader election is fixed.
* The `ImageName` parameter is aligned for the verified stages of the CD pipeline.
* The `sonar-project.properties` are now considered in the Npm code review.

### Documentation

* The [EDP RoadMap](https://epam.github.io/edp-install/roadmap/) is updated.

* The [Headlamp User Guide](https://epam.github.io/edp-install/headlamp-user-guide/) is created.

* The [Operator Guide](https://epam.github.io/edp-install/operator-guide/) is updated with the following:
  * The ReportPortal section is created with the [Integration With Tekton](https://epam.github.io/edp-install/operator-guide/report-portal-integration-tekton/) and [Keycloak Integration](https://epam.github.io/edp-install/operator-guide/reportportal-keycloak/) pages.
  * The [Use Cert-Manager in OpenShift](https://epam.github.io/edp-install/operator-guide/ssl-automation-okd/) page is added.
  * The [Logsight Integration](https://epam.github.io/edp-install/operator-guide/logsight-integration/) page is added.
  * The [Upgrade Keycloak v.17.0.x-Legacy to v.19.0.x](https://epam.github.io/edp-install/operator-guide/upgrade-keycloak-17.0.x-legacy-to-19.0.x/) page is added.
  * The [Overview](https://epam.github.io/edp-install/operator-guide/) page is updated.
  * The [Debug GitLab Webhooks in Jenkins](https://epam.github.io/edp-install/operator-guide/gitlab-debug-webhooks/) page is updated.
  * The [GitLab Integration](https://epam.github.io/edp-install/operator-guide/gitlab-integration/) page is updated.
  * The [Debug GitHub Webhooks in Jenkins](https://epam.github.io/edp-install/operator-guide/github-debug-webhooks/) page is updated.
  * The [GitHub Integration](https://epam.github.io/edp-install/operator-guide/github-integration/) page is updated.
  * The [Enable VCS Import Strategy](https://epam.github.io/edp-install/operator-guide/import-strategy/) page is updated.
  * The [Adjust Jira Integration](https://epam.github.io/edp-install/operator-guide/jira-integration/) page is updated.
  * The [Install via Helmfile](https://epam.github.io/edp-install/operator-guide/install-via-helmfile/) page is updated.
  * The [Install ReportPortal](https://epam.github.io/edp-install/operator-guide/install-reportportal/) page is updated.
  * The [Install Argo CD](https://epam.github.io/edp-install/operator-guide/install-argocd/) page is updated.
  * The [Install DefectDojo](https://epam.github.io/edp-install/operator-guide/install-defectdojo/) page is updated.
  * The [Install Keycloak](https://epam.github.io/edp-install/operator-guide/install-keycloak/) page is updated.
  * The [Install EDP](https://epam.github.io/edp-install/operator-guide/install-edp/) page is updated.
  * The [Deploy AWS EKS Cluster](https://epam.github.io/edp-install/operator-guide/deploy-aws-eks/) page is updated.
  * The [Manage Jenkins CI Pipeline Job Provisioner](https://epam.github.io/edp-install/operator-guide/manage-jenkins-ci-job-provision/) page is updated.
  * The [Argo CD Integration](https://epam.github.io/edp-install/operator-guide/argocd-integration/) page is updated.

* The [User Guide](https://epam.github.io/edp-install/user-guide/) is updated with the following:
  * The [Add a Custom Global Pipeline Library](https://epam.github.io/edp-install/user-guide/add-custom-global-pipeline-lib/) page is updated.
  * The [CI Pipeline for Container](https://epam.github.io/edp-install/user-guide/container-stages/) page is updated with the [Tools for Container Images Building](https://epam.github.io/edp-install/user-guide/container-stages/#tools-for-container-images-building) section.
  * The [Add Library](https://epam.github.io/edp-install/user-guide/add-library/) page is updated.
  * The [Customize CI Pipeline](https://epam.github.io/edp-install/user-guide/customize-ci-pipeline/) page is updated.

* The [FAQ](https://epam.github.io/edp-install/faq/) documentation section is updated with the following:
  * [How To Change the Lifespan of an Access Token That Is Used for Headlamp and 'oidc-login' Plugin?](https://epam.github.io/edp-install/faq/#how-to-change-the-lifespan-of-an-access-token-that-is-used-for-headlamp-and-oidc-login-plugin)

## Version 2.12.1 <a name="2.12.1"></a> (October 28, 2022)

### Fixed Issues

* Kiosk integration with the cd-pipeline operator is no longer mandatory for the EDP deployment.
* By default, Java 8 Maven and Java 8 Gradle Jenkins agents now use Java 8 instead of Java 11.
* The 431 error for Nexus is fixed by increasing the request header size.
* Remote Git HEAD is set as a default branch in Gerrit.

## Version 2.12.0 <a name="2.12.0"></a> (August 30, 2022)

## What's New

EDP 2.12.0 version presents EDP Argo CD Operator that runs as an adapter layer between the EDP Platform and Argo CD and manages the EDP Argo CD Tenants. Argo CD is suggested as a solution providing the Continuous Delivery capabilities.

Another new EDP subcomponent is EDP Headlamp that is a new UI on React.js that will replace the EDP Admin Console in future releases. EDP Headlamp, based on the Kinvolk Headlamp UI Client, provides the ability to define pipelines and project resources in a simple way.

Now EDP also provides the implemented Static Application Security Testing (SAST) support allowing to work with the Semgrep security scanner and the DefectDojo vulnerability management system to check the source code for known vulnerabilities. SAST is introduced as a mandatory part of the CI Pipelines.

Since this release, External Secret Operator is a recommended secret management tool for the EDP components.

EDP operator manifests have been updated to the latest v1 API version. Now EDP Platform supports Kubernetes versions 1.22+ and runs on the 4.9 and 4.10 OKD versions.

Creating the IAM Roles for Service Account is a recommended way to work with AWS Resources from the OKD cluster.

Explore the upgrades, new functionality, breaking changes and improvements below.

### Upgrades

* Gerrit is updated to the 3.6.1 version, please refer to the [official website](https://www.gerritcodereview.com/3.6.html#361).
* Keycloak is updated to the 19.0.1 version, please refer to the [official website](https://www.keycloak.org/2022/07/keycloak-1901-released.html).
* EDP Custom resource definitions now use the stable `apiextensions.k8s.io/v1` API version, please refer to the [official website](https://kubernetes.io/docs/reference/using-api/deprecation-guide/#v1-22).
* SonarQube is updated to the 8.9.9 version, please refer to the [official website](https://jira-legacy-sonarsource.valiantys.net/secure/ReleaseNote.jspa?version=17361&styleName=Html&projectId=10930&Create=Create&atl_token=BSPV-9NYM-JVTI-0WKN_db90422a2bfe60333c54cb36582a6312179301f5_lout).
* Nexus is updated to the 3.41.0 version, please refer to the [official website](https://help.sonatype.com/repomanager3/product-information/release-notes/2022-release-notes/nexus-repository-3.41.0---3.41.1-release-notes).
* The Alpine Operators Base Image is updated to the 3.16.2 version, please refer to the [official website](https://alpinelinux.org/posts/Alpine-3.13.12-3.14.8-3.15.6-3.16.2-released.html).
* Jenkins is updated to the 2.346.3 version, please refer to the [official website](https://www.jenkins.io/changelog-stable/#v2.346.3).
* All components in the Jenkins agents are updated to the latest stable versions.
* Go version in all EDP operators are updated to the 1.18.4 version, please refer to the [official website](https://go.dev/doc/devel/release#go1.18).
* Kaniko images are updated to the 1.8.1 version, please refer to the [official website](https://newreleases.io/project/github/GoogleContainerTools/kaniko/release/v1.8.1).

### New Functionality

* Now it is possible to use External Secrets Operator.
* The SAST Static security analysis testing, namely the DefectDojo component, is installed as a part of the EDP ecosystem.
* [EDP Headlamp](https://github.com/epam/edp-headlamp) is added as a new EDP subcomponent that will replace the EDP Admin Console in future releases.
* [EDP Argo CD Operator](https://github.com/epam/edp-argocd-operator) is a new EDP subcomponent that manages the EDP Argo CD Tenants.
* The Helmfile is introduced as an approach for the EDP ecosystem deployment.
* The Logsight integration is implemented as an optional step into the CD pipeline on the `edp-delivery`.
* The Keycloak-X is now used on the EDP platform.
* For the Keycloak Operator, a user can manage the assignment of the default scope mapper for the `keycloakclient` custom resource.

#### Breaking Changes

* Starting from this release, it is expected that a `CodebaseBranches` custom resource definition uses a `status` field as a subresource.

### Enhancements

* The OKD 4.10 cluster is deployed in the AWS cloud. Please refer to the [Deploy OKD 4.10 Cluster](https://epam.github.io/edp-install/operator-guide/deploy-okd-4.10/) page for the details.
* The RBAC schema for the `cd-pipeline-operator` and `jenkins-operator` is refactored.
* The Keycloak custom resources are aligned with the Argo CD integration.
* The EDP Helm charts are populated with the metadata to be exposed on the Artifact Hub.
* The `previous-stage-name` is removed from the Jenkins deployed versions view.
* The `deployments` is now a default deployment type for the OpenShift cluster.

### Fixed Issues

* The exponential back-off is used when retrying the `GerritGroupMemeber` reconciliation.
* The `Chart.yaml` content is aligned throughout the EDP.
* Git checkout is fixed for an Autotest step of the CD Pipeline.
* The EDP deploy on the OKD 4.9 cluster works as expected after a typo has been fixed in the CI job-provisioner for the OpenShift cluster, and user creation process for the OpenShift cluster has been refactored.
* A `status` field is now ignored during the update of a Stage custom resource, and a CD Stage is successfully created.
* The password policy can be successfully created in the Keycloak realm using a custom resource.
* A status for the `GerritGroupMember` custom resource is updated as expected.
* The deprecated Custom Resource Definition fields are removed from the EDP `codebase-operator`.
* The `mdx_truly_sane_lists` issue when building MkDocs is fixed.
* The `sonar-operator` API is updated to be compliant with SonarQube 8.9.9.

### Documentation

* The [EDP RoadMap](https://epam.github.io/edp-install/roadmap/#roadmap) is updated.

* The [Operator Guide](https://epam.github.io/edp-install/operator-guide/) is updated with the following:
    * The [Upgrade EDP v.2.11.0 to v.2.12.0](https://epam.github.io/edp-install/operator-guide/upgrade-edp-2.11.x-to-2.12.x/) page is added.
    * The [Install via Helmfile](https://epam.github.io/edp-install/operator-guide/install-via-helmfile/) page is updated.
    * The [Deploy OKD 4.10 Cluster](https://epam.github.io/edp-install/operator-guide/deploy-okd-4.10/) page is added.
    * The [Install DefectDojo](https://epam.github.io/edp-install/operator-guide/install-defectdojo/) page is added.
    * The **Secrets Management** section is added including the [Install External Secrets Operator](https://epam.github.io/edp-install/operator-guide/install-external-secrets-operator/) and [External Secrets Operator Integration](https://epam.github.io/edp-install/operator-guide/external-secrets-operator-integration/) pages.
    * The [Install Keycloak](https://epam.github.io/edp-install/operator-guide/install-keycloak/) page is added.
    * The [Argo CD Integration](https://epam.github.io/edp-install/operator-guide/argocd-integration/) page is added.
    * The **Static Application Security Testing** section is added including the [Static Application Security Testing Overview](https://epam.github.io/edp-install/operator-guide/overview-sast/), [Add Security Scanner](https://epam.github.io/edp-install/operator-guide/add-security-scanner/), and [Semgrep](https://epam.github.io/edp-install/operator-guide/sast-scaner-semgrep/) pages.
    * The [Manage Jenkins CI Pipeline Job Provisioner](https://epam.github.io/edp-install/operator-guide/manage-jenkins-ci-job-provision/) page is updated.
    * The [Associate IAM Roles With Service Accounts](https://epam.github.io/edp-install/operator-guide/enable-irsa/) page is updated.
    * The [Install NGINX Ingress Controller](https://epam.github.io/edp-install/operator-guide/install-ingress-nginx/) page is updated.
    * The [EKS OIDC With Keycloak](https://epam.github.io/edp-install/operator-guide/configure-keycloak-oidc-eks/) page is updated.
    * The [Upgrade EDP v.2.10.x to v.2.11.x](https://epam.github.io/edp-install/operator-guide/upgrade-edp-2.10.x-to-2.11.x/) page is updated.

* The [User Guide](https://epam.github.io/edp-install/user-guide/) is updated with the following:
    * The [CI Pipeline for Terraform](https://epam.github.io/edp-install/user-guide/terraform-stages/) page is updated.

## Version 2.11.0 <a name="2.11.0"></a> (May 27, 2022)

### What's New

EDP 2.11.0 version presents the CD pipeline customizations, namely, now it is possible to populate secrets to different environments and stages; to remove Helm releases from a namespace, thus allowing to redeploy the application from scratch.
CD pipeline flexible optimizations providing the ability to define custom logic of the application deployment. In addition to the automation deployment proceeding with the latest versions and manual selection of the necessary version, there is a possibility to build your own custom deployment logic.
From the side of operators, there is a bulk of Gerrit improvements for the merge requests: tracking MR statuses, creating MRs directly from a custom resource and with the data specified in a config map. In Keycloak, now it is possible to configure Keycloak Realm Password Policy, to make the Keycloak Realm role mappers
optional, to set the full reconciliation for the Keycloak Realm user, and to configure and create the Authentication flows via the KeycloakAuthflow custom resource. Also, there are extensions of Jenkins Shared Libraries allowing to add any shared library using custom resource.
In addition, the Docker support is available.

Explore the upgrades, new functionality, breaking changes and improvements below.

### Upgrades

* SonarQube is updated to the LTS 8.9.8 Community Edition. For details, please refer to the [official website](https://jira.sonarsource.com/secure/ReleaseNote.jspa?version=17249&styleName=&projectId=10930&Create=Create&atl_token=BSPV-9NYM-JVTI-0WKN_447990944a5e476cea519acf4a65a799d7a41e01_lout).
* Jenkins is updated to the LTS 2.332.2 version. For details, please refer to the [official website](https://www.jenkins.io/doc/upgrade-guide/2.332/).
* All Alpine-based images are updated to the 3.15.4 version. For details, please refer to the [official website](https://git.alpinelinux.org/aports/log/?h=v3.15.4).
* Go language is updated to the 1.17.8 version. For details, please refer to the [official website](https://go.dev/doc/devel/release#go1.17.minor).
* Helm is updated to the 3.8.1 version on Jenkins agents. For details, please refer to the [official website](https://github.com/helm/helm/releases/tag/v3.8.1).
* Keycloak is updated to the 17.0.1 version. For details, please refer to the [official website](https://www.keycloak.org/docs/latest/release_notes/index.html).
* Kubectl is updated to the 1.23.5 version on Jenkins agents. For details, please refer to the [official website](https://github.com/kubernetes/kubernetes/blob/master/CHANGELOG/CHANGELOG-1.23.md#v1235).
* Nexus is updated to the LTS 3.38.1 version. For details, please refer to the [official website](https://help.sonatype.com/repomanager3/product-information/release-notes/2022-release-notes/nexus-repository-3.38.0---3.38.1-release-notes).

### New Functionality

* Now it is possible to add a library for Dockerfile, thus having CI pipelines for Docker.
* The copy-secret pipeline stage is added providing the ability to populate Kubernetes secrets across deployed environments/stages.
* The ability to remove Helm releases from a namespace, thus allowing to redeploy the application from scratch.
* Validation of the generated documentation for the Helm deployment templates.
* Use pre-defined deployment logic or your own: either keep the automation deploy proceeding with the latest versions or select manually the necessary version for deploy.
* EDP versioning supports the arbitrary artifacts version name for branches, i.e. tags can be of any value besides the RC / SNAPSHOT that are specified by default.
* The newly created CD pipeline includes the enabled _Discard old builds_ option with the configured rules.
* Extension of Kaniko template flexibility allowing to set extra arguments, environments, and resource requests for every container.
* Extension of Jenkins shared libraries allowing to add any shared library using custom resource.
* Ability to manage environment variables, namely, adding and configuring of a new environment variable for components.
* Configuration and creation of Authentication flows via the KeycloakAuthflow custom resource.
* Ability to configure Keycloak Realm Password Policy.
* Making the Keycloak Realm role mappers optional.
* Set the full reconciliation for the Keycloak Realm user.
* Configurable reconciliation time in Gerrit is added into environment variable.
* Ability to track the Gerrit merge request status in the custom resource status.
* Creating a merge request directly from the custom resource of the Gerrit operator.
* Defining additional parameters for Gerrit merge request using the custom resource.
* Creating a Gerrit merge request with contents specified in a config map.
* The ability to provide read-only repositories via the ReadOnly group in the Gerrit operator.
* Managing Sonar permission templates and groups using the Kubernetes custom resource.
* Defining Sonar default permissions template using the Sonar custom resource.
* Managing Nexus users via custom resource.

#### Breaking Changes

* Custom resource will have two keys: 'tag' for a single tag and 'tags' for the list of tags.
* Use gorilla's csrf implementation instead of beego's xsrf.
* Switch to use v2 admin console API for build, code review and deploy pipelines.
* Respective GerritGroupMember Custom Resources must be created to replace existing users[] mapping. Consult the release upgrade instruction.
* Update Gerrit config according to groups.
  * Implement Developers group creation;
  * Assign users to admins and developers groups using the GerritGroupMember CR;
  * Align permission for groups.

### Enhancements

* The TLS certificate option is added when using Ingress controller for SSL certificates.
* CI job provisioner now runs on the specific Jenkins label.
* There is a namespace defined for a Service Account in the Role Binding.
* The Kaniko build step has a timeout.
* The Kaniko provision logic is moved to the edp-install helm chart, thus providing the ability to deliver updates.
* Gerrit members are managed from GerritGroups and GerritGroupMembers, providing the successful upgrade from older versions.
* The creation of users within the EDP installation is removed.
* The images within the main EDP documentation are scalable.
* Disabled access to Gerrit for users without admin/developer roles in Keycloak.
* The replacing of the default branch option is added to specification in codebase operator.
* Postponed reconciliation for the Import strategy for the codebase operator.
* Disable of putting deploy configs by a flag for the codebase operator.

### Fixed Issues

* Fixed the changelog generation in GH Release Action.
* Codebase operator does not repeat the GitServer custom resource reconciliation.
* Sonar proxy error in helm template file.
* Failed to get the CodebaseImageStream CR when branch name contains the slash character.
* CD pipeline fails if image is not promoted.
* Fixed build issue for operator Docker images.
* Jenkins role mapping CR controller does not return an error if a group does not exist.
* The Fix Version field does not contain data for the container libraries.
* An error occurs when deleting the KeycloakAuthFlow CR.
* Keycloak realm user does not synchronize roles correctly.
* The SonarGroup and SonarPermissionTemplate controllers do not remove failed custom resource.
* It is impossible to set any branch as a default existing in a repository using the Import strategy.
* Nexus operator does not disable anonymous access to Admin Console UI.

### Documentation

* The [Operator Guide](https://epam.github.io/edp-install/operator-guide/) is updated with the following pages:
   * [Associate IAM Roles With Service Accounts](https://epam.github.io/edp-install/operator-guide/enable-irsa/)
   * [Configure AWS WAF With Terraform](https://epam.github.io/edp-install/operator-guide/waf-tf-configuration/)
   * [Debug GitLab Webhooks in Jenkins](https://epam.github.io/edp-install/operator-guide/gitlab-debug-webhooks/)
   * [Debug GitHub Webhooks in Jenkins](https://epam.github.io/edp-install/operator-guide/github-debug-webhooks/)
   * [EKS OIDC With Keycloak](https://epam.github.io/edp-install/operator-guide/configure-keycloak-oidc-eks/)
   * [Enable VCS Import Strategy](https://epam.github.io/edp-install/operator-guide/import-strategy/)
   * [GitHub Integration](https://epam.github.io/edp-install/operator-guide/github-integration/)
   * [GitLab Integration](https://epam.github.io/edp-install/operator-guide/gitlab-integration/)
   * [Manage Jenkins CI Pipeline Job Provisioner](https://epam.github.io/edp-install/operator-guide/manage-jenkins-ci-job-provision/)
   * [Multitenant Logging](https://epam.github.io/edp-install/operator-guide/multitenant-logging/)
   * [Upgrade EDP v.2.10.2 to v.2.11.0](https://epam.github.io/edp-install/operator-guide/upgrade-edp-2.10.2-to-2.11.0/)

* The [User Guide](https://epam.github.io/edp-install/user-guide/) is updated with the following pages:
   * [CI Pipeline for Container](https://epam.github.io/edp-install/user-guide/container-stages/)
   * [Copy Shared Secrets](https://epam.github.io/edp-install/user-guide/copy-shared-secrets/)
   * [Helm Chart Testing and Documentation Tools](https://epam.github.io/edp-install/user-guide/helm-stages/)
   * [Helm Release Deletion](https://epam.github.io/edp-install/user-guide/helm-release-deletion/)
   * [Pipeline Stages](https://epam.github.io/edp-install/user-guide/pipeline-stages/)
   * [Promote Docker Images From ECR to Docker Hub](https://epam.github.io/edp-install/user-guide/ecr-to-docker-stages/)
   * [Semi Auto Deploy](https://epam.github.io/edp-install/user-guide/semi-auto-deploy/)
   * [Use Dockerfile Linters for Code Review Pipeline](https://epam.github.io/edp-install/user-guide/dockerfile-stages/)
   * [Use Open Policy Agent](https://epam.github.io/edp-install/user-guide/opa-stages/)

## Version 2.10.2 <a name="2.10.2"></a> (January 21, 2022)

### Enhancements

* It is possible to use the custom ct.yaml config defined per repository. For details, please refer to the [Use Chart Testing Tool for Code Review Pipeline](https://epam.github.io/edp-install/user-guide/helm-stages/#use-chart-testing-tool-for-code-review-pipeline) page.
* Jenkins pipelines use SSH Agent Jenkins Plugin instead of "eval ssh-agent" approach.
* Make possible to use oc and kubectl tools on Jenkins master, go and helm agents.
* Jenkins [ecr-to-docker](https://epam.github.io/edp-install/user-guide/ecr-to-docker-stages/#promote-docker-images-from-ecr-to-docker-hub) stage now supports EDP and default versioning.

## Version 2.10.1 <a name="2.10.1"></a> (January 4, 2022)

### Upgrades

* SonarQube is upgraded to the 8.9.6 version. For details, please refer to the [official website](https://jira.sonarsource.com/secure/ReleaseNote.jspa?projectId=10930&version=17136).
* The EDP Sonar operator is upgraded to the 2.10.2 version. For details, please refer to the [edp-sonar-operator](https://github.com/epam/edp-sonar-operator/tree/release/2.10) repository.

### Enhancements

* A new [CHANGELOG.md](https://github.com/epam/edp-install/blob/master/CHANGELOG.md#changelog) file is added providing the information regarding the changes in the edp-install repository.
* The [RELEASES.md](https://github.com/epam/edp-install/blob/master/RELEASES.md#edp-releases) file is updated providing the information regarding overall platform changes.

## Version 2.10.0 <a name="2.10.0"></a> (December 22, 2021)

### What's New

EDP 2.10.0 version provides the CI/CD improvements. For instance, each operator now exposes metadata information during start up, like: build tag/date, commit hash from which the operator was built. There are updates in Keycloak operator.
Now Keycloak Custom Resources allows configuring realm identity providers settings, managing the user federation, controlling the roles and groups, reconciling Keycloak Clients in Custom Resource with several strategies, setting user attributes, and managing events in Keycloak.
In addition, login attempts are optimized by synchronizing the access token cache.
General configuration for Ingress controller is provided in Helm charts, which simplifies logic for deployment to different environments. Since this release, EDP supports large Git repositories. As for the EDP components, each of them contains a CHANGELOG file providing the release information.

Explore the updates, new functionality, breaking changes and improvements below.

### Upgrades

* All Alpine based images are upgraded to the 3.13.7 version. For details, please refer to the [official website](https://git.alpinelinux.org/aports/log/?h=v3.13.7).
* Go is upgraded to the 1.17 version. For details, please refer to the [official website](https://go.dev/doc/go1.17).
* Helm is upgraded to the 3.7.1 version on Jenkins agents. For details, please refer to the [official website](https://newreleases.io/project/github/helm/helm/release/v3.7.1).
* Jenkins is upgraded to the LTS 2.303.3 version. For details, please refer to the [official website](https://www.jenkins.io/doc/upgrade-guide/2.303/).
* Keycloak is upgraded to the 15.0.2 version. For details, please refer to the [official website](https://www.keycloak.org/2021/08/keycloak-1502-released).
* Kubectl is upgraded to the 1.20.0 version on Jenkins agents. For details, please refer to the [official website](https://kubernetes.io/blog/2020/12/08/kubernetes-1-20-release-announcement/).
* Nexus is upgraded to the LTS 3.36.0 version. For details, please refer to the [official website](https://help.sonatype.com/repomanager3/product-information/release-notes/2021-release-notes/nexus-repository-3.36.0-release-notes).
* SonarQube is upgraded to the LTS 8.9.3 Community Edition. For details, please refer to the [official website](https://jira.sonarsource.com/secure/ReleaseNote.jspa?projectId=10930&version=16979).

### New Functionality

* Now EDP supports large Git repositories.
* Each operator/component provides meta information in logs, for example, the date of build, the build branch, tag, etc.
* The performance issue is addressed by defining history depth (up to 10) for job provisioner run.
* A new Custom Resource Definition KeycloakRealmIdentityProvider allows configuring realm identity providers settings.
* A new KeycloakRealmComponent Custom Resource allows managing the user federation in Keycloak.
* Several strategies are available for Keycloak Client reconciliation in Custom Resource.
* The token in the kc-token-main secret is updated immediately after the creation of a new realm.
* To ensure the desired and actual states are identical, a Custom Resource is reconciled in a configured period of time.
* Using a service account in Keycloak allows getting the admin realm token.
* KeycloakClient Custom Resource allows setting Front Channel Logout parameter in SAML clients.
* Keycloak Custom Resource allows recreating already existing Keycloak client scopes.
* When using the Import strategy of adding a codebase, Jenkins secrets can be created through the JenkinsServiceAccount Custom Resource.
* A new Jenkins agent is added with [Java SE 14](https://jdk.java.net/java-se-ri/14) and [Apache Maven 3.8.4](https://maven.apache.org/docs/3.8.4/release-notes.html).
* [GCC](https://gcc.gnu.org/) and [Make](https://www.gnu.org/software/make/) tools are added to the Jenkins agent image.
* Dynamic parameters are implemented for a codebase branch trigger release.
* A shared Golang library is created. For details, please refer to the [GitHub page](https://github.com/epam/edp-common).

#### Breaking Changes

* With v.2.10.0, EDP provides breaking changes and improvements in CI/CD process. For instance, the Jenkins job provisioner creates a Jenkinsfile (that contains the definition of the Jenkins pipeline) and configures it in the Jenkins pipeline as a pipeline script.
* From the Continuous Integration part, Jenkins pipelines are provisioned by a pipeline provisioner instead of a codebase operator. Please refer to the [Manage Jenkins CI Pipeline Job Provisioner](https://epam.github.io/edp-install/operator-guide/manage-jenkins-ci-job-provision/) page for the details.

### Enhancements

* Work with Ingress becomes intuitively clear, and the default Ingress creation process is improved.
* Golang tests are excluded from the sonar stage in the Code Review pipeline.
* The edp-codebase operator work is improved during Jenkins and Gerrit initialization.
* Login attempts are optimized by implementing synchronization on the access token cache for Keycloak.
* Stashing the hadolint files allows configuring the hadolint check.
* EDP allows to deploy and manage an application with the third-party dependency.
* In Keycloak, the id for a newly created realm is identical to the realm name.
* Events in Keycloak can be managed with a corresponding Custom Resource.
* In Keycloak, user attributes can be set using a corresponding Custom Resource.

### Fixed Issues

* Jenkins and Nexus deployments are fixed on the OKD cluster.
* A new branch can be created from an undefined commit.
* Empty 'Deployment Script' field in Admin Console by default.
* Jenkins is unable to create Jenkins agents.
* Code Review and Build pipelines fail on the sonar stage for .Net applications.
* The edp-cd-pipeline operator can not create a CD stage.
* Panic issue in case GetCodebaseImages doesn't exist.
* The unknown apiGroup field is removed in OpenShift RB.
* A user is unable to clone a codebase from a public repository.
* A Custom Resource is not deleted from a namespace after the codebase deletion from the EDP Admin Console.
* The whitespace in the git-tag stage is removed.
* Duplication of the key value in Keycloak during repeated reconciliation of KeycloakRealm Custom Resource is fixed.


### Documentation

* The [EDP Project Rules. Working Process](https://epam.github.io/edp-install/developer-guide/edp-workflow/) page is added to the [Developer Guide](https://epam.github.io/edp-install/developer-guide/).
* The [Operator Guide](https://epam.github.io/edp-install/operator-guide/) is updated with the following:
   * The **Upgrade** section is added including the [Upgrade EDP v.2.7.8 to v.2.8.4](https://epam.github.io/edp-install/operator-guide/upgrade-edp-2.7.8-to-2.8.4/) and [Upgrade EDP v.2.8.4 to v.2.9.0](https://epam.github.io/edp-install/operator-guide/upgrade-edp-2.8.4-to-2.9.0/) pages:
   * The [Enable VCS Import Strategy](https://epam.github.io/edp-install/operator-guide/import-strategy/) page is updated.
   * The [Schedule Pods Restart](https://epam.github.io/edp-install/operator-guide/schedule-pods-restart/) page is updated.
* The following pages are added to the [Use Cases](https://epam.github.io/edp-install/use-cases/) section:
   * [Autotest as Quality Gate](https://epam.github.io/edp-install/use-cases/autotest-as-quality-gate/)
   * [Promote Application in CD Pipeline](https://epam.github.io/edp-install/use-cases/promotion-procedure/)
* The [User Guide](https://epam.github.io/edp-install/user-guide/) is updated with the following:
   * [EDP CI/CD Overview](https://epam.github.io/edp-install/user-guide/cicd-overview/) is added.
   * [CI Pipeline Details](https://epam.github.io/edp-install/user-guide/ci-pipeline-details/) is added.
   * [Code Review Pipeline](https://epam.github.io/edp-install/user-guide/code-review-pipeline/) is added.
   * [Build Pipeline](https://epam.github.io/edp-install/user-guide/build-pipeline/) is added.
   * [CD Pipeline Details](https://epam.github.io/edp-install/user-guide/cd-pipeline-details/) is added.
   * [Prepare for Release](https://epam.github.io/edp-install/user-guide/prepare-for-release/) is added.
   * The [EDP Pipeline Framework](https://epam.github.io/edp-install/user-guide/pipeline-framework/) page is updated.
   * The [Autotest](https://epam.github.io/edp-install/user-guide/autotest/) section is updated.
* The [FAQ](https://epam.github.io/edp-install/faq/) section is added to the EDP documentation.


## Version 2.9.0 <a name="2.9.0"></a> (September 30, 2021)

### What's New

With v.2.9.0, EPAM Delivery Platform offers more flexibility in deployment and work. Thus, EDP and all the prerequisites can be installed with Terraform tool or Helm.
IRSA for Kaniko (Kaniko is a tool for building container images from Dockerfile) is an optional step. Alternative is to [use instance profiles](https://docs.aws.amazon.com/IAM/latest/UserGuide/id_roles_use_switch-role-ec2_instance-profiles.html).
Kiosk, a respective multi-tenancy extension to manage tenants and namespaces, is optional and can be enabled or disabled during EDP deployment. It is possible to get the list of namespaces owned by a specific edp-tenant with the help of Kubernetes labels.

EPAM Delivery Platform allows to configure authorization for key roles in Jenkins in a declarative way. Now, Jenkins operator reconciles any changes in shared libraries specification. As for the Keycloak, it is possible to make Jenkins Keycloak client confidential and to manage attributes for service account in Keycloak with Keycloak Client Custom Resource.
In addition, the roles for Keycloak proxy can be set in Nexus.

There are Gerrit operator improvements: each Gerrit repository is presented as a Custom Resource in Kubernetes. Available branches are displayed in Gerrit Project Custom Resource.

See the list of actions required to work with EDP v.2.9.0, new functionality and enhancements, as well as the list of fixed issues and updated documentation below.

#### Breaking Changes Actions

* Before updating EDP from v.2.8.X to v.2.9.X, please update the 'gerrit-is-credentials' secret by adding the new 'clientSecret' key with the value from 'gerrit-is-credentials.client_secret'.
* After EDP update, please restart the 'sonar-operator' pod to address proper Sonar plugin versioning. After sonar-operator is restarted, check the list of installed plugins in the corresponding SonarQube menu.

### Upgrades

* All alpine based images are updated to version 3.13.6. For details, please refer to [Alpine Official Website](https://alpinelinux.org/posts/Alpine-3.11.12-3.12.8.-3.13.6-released.html).

### New Functionality

* EDP and prerequisites can be installed using Terraform tool.
* Kiosk is optional and can be enabled or disabled during EDP deployment.
* IRSA for Kaniko is an optional step.

### Enhancements

* Permissions in Jenkins are configured in a declarative way.
* Keycloak proxy image can be defined in Nexus spec.
* It is possible to set roles for Keycloak proxy in Nexus spec.
* Keycloak Client Scope is managed with Kubernetes Custom Resource.
* There is an option to provision users in Keycloak and to assign them roles and groups using custom resources.
* CD pipelines reconciliation can be enabled or disabled.
* Now Jenkins Keycloak client supports confidential mode.
* Jenkins operator reconciles any changes in shared libraries specifications.
* It is possible to manage attributes for Service Account in Keycloak with KeycloakClient Custom Resource.
* It is possible to set a number of parallel threads for codebase branches reconciliation.
* Pod duplication on dockerbuild-verify stage is removed.
* Reconciler is refactored to not save an empty Jenkins agent to the database.
* GitHub pull-request builder is added to the Jenkins box.
* Artifact path in Go language is updated in Dockerfile for the build stage.
* All Gerrit repositories are presented as a Custom Resource in Kubernetes.
* Available branches are displayed in the GerritProject Custom Resource.
* Kubernetes labels are now used to get the list of namespaces owned by a specific edp-tenant.
* The Ingress API version is aligned with Kubernetes version.
* The image pull policy parameter can be redefined during the EDP installation (default: ifNotPresent).
* The environment variables are parametrized for admin-console deployment.


### Fixed Issues

* Jenkins operator constantly tries to create a Jenkins job.
* The status field is added to the CD pipeline operator CRD.
* Fix Gerrit plugin enablement during Jenkins provisioning.
* Routes are removed from the database and reconciler.
* It is impossible to create an application with a custom default branch.
* It is impossible to deploy Go-operator-sdk applications.
* There is no authentication in the checkout stage when the existing branch is set as a default one (cloned from a private repository).
* Codebase operator does not work correctly with a non-master branch.
* The deprecated agents for GitLab CI are listed in the GitlabCI.yaml file.
* Code-Review pipeline checkouts from an incorrect commit for GitLab integration.
* Build pipeline fails on the compile stage for .NET-3.1 libraries.
* The reconciler logger takes the wrong number of arguments.

### Documentation

* The structure of [EPAM Delivery Platform documentation framework](https://epam.github.io/edp-install/) is updated.
* The Prerequisites section is updated with the following pages:
   * [Ingress-nginx Installation on Kubernetes](https://epam.github.io/edp-install/operator-guide/install-ingress-nginx/)
   * [Kiosk Setup](https://epam.github.io/edp-install/operator-guide/install-kiosk/)
   * [Keycloak Installation on Kubernetes](https://epam.github.io/edp-install/operator-guide/install-keycloak/)
   * [Kubernetes Settings](https://epam.github.io/edp-install/operator-guide/kubernetes-cluster-settings/)
   * [OpenShift Settings](https://epam.github.io/edp-install/operator-guide/openshift-cluster-settings/)
* The [Install EDP](https://epam.github.io/edp-install/operator-guide/install-edp/) page is updated.
* New configuration and integration pages are added:
   * [Add Other Code Language](https://epam.github.io/edp-install/operator-guide/add-other-code-language/)
   * [Manage Jenkins Pipelines](https://epam.github.io/edp-install/operator-guide/overview-manage-jenkins-pipelines/)
   * [Associate IAM Roles With Service Accounts (IRSA)](https://epam.github.io/edp-install/operator-guide/enable-irsa/)
   * [Schedule Pods Restart](https://epam.github.io/edp-install/operator-guide/schedule-pods-restart/)
   * [IAM Roles For Loki Service Accounts](https://epam.github.io/edp-install/operator-guide/loki-irsa/)
   * [Install Grafana Loki](https://epam.github.io/edp-install/operator-guide/install-loki/)
   * [Backup EDP Tenant With Velero](https://epam.github.io/edp-install/operator-guide/install-velero/)
* New tutorials are added to the documentation:
   * [Deploy AWS EKS Cluster](https://epam.github.io/edp-install/operator-guide/deploy-aws-eks/#check-eks-cluster-deployment)
   * [Manage Jenkins Agent](https://epam.github.io/edp-install/operator-guide/add-jenkins-agent/)

## Version 2.8.4 <a name="2.8.4"></a> (September 10, 2021)

### Fixed Issues

* Helm chart fields are fixed: the correct indent is set for toleration/affinity/node selector fields.


## Version 2.8.3 <a name="2.8.3"></a> (August 23, 2021)

### Upgrades

* Nexus is updated to v.3.33.1. Please refer to the [official documentation](https://help.sonatype.com/repomanager3/release-notes#ReleaseNotes-NexusRepositoryManager3.33.1) for details.
* Jenkins is updated to v.2.289.3. Please refer to the [official documentation](https://www.jenkins.io/changelog-stable/#v2.289.3) for details.

### Fixed Issues

* In CD pipeline deployment, the sorting of the image tags for the Init stage is displayed in the wrong order.


## Version 2.8.2 <a name="2.8.2"></a> (August 5, 2021)

### Fixed Issues

* Missing field status is added to the [cdpipeline.crd yaml](https://github.com/epam/edp-cd-pipeline-operator/blob/release/2.8/deploy-templates/crds/edp_v1alpha1_cdpipeline_crd.yaml) file.


## Version 2.8.1 <a name="2.8.1"></a> (August 4, 2021)

### New Functionality and Enhancements

* IRSA is optional for EDP deployment process.
* The codebaseBranch field is removed from the [cdpipeline.crd yaml](https://github.com/epam/edp-cd-pipeline-operator/blob/release/2.8/deploy-templates/crds/edp_v1alpha1_cdpipeline_crd.yaml) file.
* A new field subresources status is added to the [cdpipeline.crd yaml](https://github.com/epam/edp-cd-pipeline-operator/blob/release/2.8/deploy-templates/crds/edp_v1alpha1_cdpipeline_crd.yaml) file.
* The inputDockerStreams field is aligned in the cd-pipeline structure with CRD.
* The site and path parameters are removed from database.

#### Fixed Issues

* User cannot create an application using the Create strategy in a custom default branch.


## Version 2.8.0 <a name="2.8.0"></a> (July 16, 2021)

### What's New

With the version 2.8.0, EDP offers a number of breaking changes making the work on the project smoother. For instance,
Keycloak is set for work in multi-tenancy mode allowing to work on a set project without interrupting the work of others.
Within the version 2.8.0, it is also possible to create an application with an empty repository and create
namespaces using Kiosk API. The third-party service provision functionality and Exposing Info tab were removed from Admin
Console. Besides, the initial structure for a new documentation framework is added to GitHub. 

There are enhancements in the EDP CI/CD framework, such as CI pipeline availability for Open Policy Agent (OPA), CI
pipelines defined for groovy-pipelines, and the possibility to apply a specific logic and customize the Init stage of
a CD pipeline.

See the list of actions required to work with EDP v.2.8.0, new functionality and enhancements, as well as the list of
fixed issues and updated documentation below.

 #### Breaking Changes Actions

 * Init prerequisite is removed from the Deploy pipeline and added as a standalone stage.

   Reload *jenkins-operator* pod once EDP is updated to version 2.8.0.

 * New Autodeploy functionality requires updating CodebaseImageStream resources.

   Update all CodebaseImageStream resources with *spec.codebase: {application-name}* field to which Codebase belongs this
 resource.
 ```yaml
 spec:
   codebase: {application-name}
   imageName: stub
   tags:
     - created: stub
       name: stub
 ```

 * CDPipeline CRD:
    * New *deploymentType* field has been added to spec. Available values: Container/Custom

      Container - default way to deploy applications from the registry.

      Custom - an empty CD pipeline is created in Jenkins (with the Init stage only).

 * CDStageDeployments CRD:
    * Spec has been changed. Resource stores only one tag to deploy it automatically on Jenkins.
 To fix the autodeploy for EDP v.2.8.0, remove all old CDStageDeployment resources from the cluster.

 * CDStageJenkinsDeployments CRD:
    * Spec has been changed. Resource stores only one tag to deploy it automatically on Jenkins.
 To fix the autodeploy for EDP v.2.8.0, remove all old CDStageDeployment (as CDStageJenkinsDeployment resources have owner
 refs to CDStageDeployments resources, it is only necessary to remove CDStageDeployments) resources from the cluster.

 * Codebase CRD:
   * New *emptyProject* field has been added to spec. Available values: true/false.

     True - codebase will be created without template code (Create strategy).

     False - codebase will be created with template code (Create strategy).


### Upgrades

* The operator-SDK library is updated to version 1.5.0.
* The Controller-runtime library is updated to version 0.8.3.
* Jenkins is upgraded to v.2.289.1. For details, please refer to the [Jenkins official website](https://www.jenkins.io/changelog-stable/).
* Keycloak is upgraded to v.13.0.1. For details, please refer to the [Keycloak official website](https://www.keycloak.org/2021/05/keycloak-1301-released.html).
* The Jenkins-agents are upgraded with dependencies to version.
* Helm is upgraded to v.3.6.0 in Jenkins for deploy process. For details, please refer to the [Helm page](https://github.com/helm/helm/releases/tag/v3.6.0) on GitHub.
* Helm tool is upgraded to v.3.5.3.
* The alpine base images are upgraded to version alpine:3.13.5. For details, please refer to the [Alpine Linux official documentation](https://alpinelinux.org/posts/Alpine-3.13.5-released.html).
* The jQuery is upgraded to version 3.6.0 to mitigate the vulnerabilities related to the previous version. For details,
please refer to the [jQuery official blog](https://blog.jquery.com/2021/03/02/jquery-3-6-0-released/).
* Kaniko executor is upgraded to version 1.6.0. For details, plese refer to the [Kaniko page](https://github.com/GoogleContainerTools/kaniko/releases/tag/v1.6.0) on GitHub.

### New Functionality and Enhancements

* ECR registry supports multi-tenancy per EDP installation.
* Baseline Kubernetes applications can be installed with Argo CD.
* A landscape is created for the current Kubernetes role model in EDP.
* Keycloak is set for work in multi-tenancy mode with minimal permissions so that a user can work in a set EDP project
without interrupting the work of other projects.
* All dependencies are pointed to tags from master branches in go mod for all operators.
* Jenkins pipeline can be started via Custom Resources.
* CI pipeline is available for OPA policies.
For details, please refer to the [Open Policy Agent](https://www.openpolicyagent.org/) official documentation and the
[Use Open Policy Agent Library in EDP](https://github.com/epam/edp-admin-console/blob/release/2.8/documentation/cicd_customization/opa_stages.md) page.
* CI pipelines are defined for groovy-pipelines.
* The Init stage of the CD pipeline can be customized. For details, please refer to the
[Add CD Pipelines](https://github.com/epam/edp-admin-console/blob/release/2.8/documentation/add_CD_pipelines.md) page.
* CD pipeline provisioner can be triggered periodically.
* There is an option to enable/disable auto deployment for a specific stage after the stage is created. For details, please
refer to the [Edit CD Pipeline](https://github.com/epam/edp-admin-console/blob/release/2.8/documentation/inspect_CD_pipeline.md#edit-cd-pipeline)
section of the Admin Console Guide.
* Initial components are provisioned for the AQA framework.
* New branches can be added with a slash in the name with default versioning.
* Default branch value can be defined with dots.
* It is possible to change the image streams for the pipelines with the “auto” deployment type.
* All applications in the CD pipeline are deployed in case of changes in one of them (Autodeploy).
* Keycloak admin credentials are replaced with realm admin credentials.
* Keycloak Realm can be installed without default roles.
* Authentication flows for a realm can be managed with separate Custom Resource.
* Authentication browser flow in Keycloak Realm can be managed using Custom Resource.
* The roles are deleted when they are removed from KeycloakRealmRolesBatch.
* Deployment objects can be used in OpenShift instead of DeploymentConfigs.
* The third-party services provision functionality is removed.
* Exposing Service Info block and functionality is removed from Admin Console.
* An application can be created with an empty GitHub repository. For details, please refer to
the [Add Application](https://github.com/epam/edp-admin-console/blob/release/2.8/documentation/add_applications.md#add-applications)
page of the Admin Console user guide.
* Namespaces can be created with Kiosk API.
* The codeql scan is added for the GitHub repositories.
* Gerrit operator adds a user to a group in a reconcile manner if a user is missing in Gerrit itself.
* Gerrit Administrators are fully reconciled from Gerrit CR.
* Keycloak realm can be specified to be integrated with Gerrit in the CR specification
* Users can manage mappers for SSO Realm provider in KeycloakRealm CR.
* Users can manage Themes and Security Defences in Keycloak Realm using CR.
* Keycloak Operator does not reconcile Realm Role from CR if a Role is already presented in the application (Keycloak Server).
* The initial structure for a new documentation framework is added to the GitHub to improve the documentation processing
and navigation.
Please refer to the [EPAM Delivery Platform documentation](https://epam.github.io/edp-install/) page for details.
* [EDP Stages](https://github.com/epam/edp-admin-console/blob/release/2.8/documentation/edp-stages.md) page is updated
and moved to GitHub.


#### Fixed Issues

* The advanced mapping section is not available for autotests codebase when Jira integration is true.
* It is impossible to delete the Jenkins folder while any codebase branches exist.
* EDP helmfile, hadolint linters fail with UI components.
* The user is unable to delete the CD pipeline stage from Admin Console.
* The validation message is not informative if no application is selected.
* Codebases, CD pipeline, and CD stages name must be 2 letters length minimum.
* Empty branches dropdown menus are clickable and show nothing.
* The validation message is “Invalid URL, log in or password” even when the URL is correct.
* The validation message is not informative in the Import strategy for applications.
* The Create button has been removed at the end of the application codebase creation.
* The codebase data is displayed incorrectly when changing strategy during creation.
* Clicking the Back button from the update page leads to cd pipeline overview instead of the pipeline details page.
* Application code contains hardcoded client secrets.
* False-positive security findings in the Admin Console code are fixed.
* The name Admin appears in the Admin Console instead of the username after redeploying the environment.
* It is impossible to assign a Sonar administrator role.
* Fix adding codebase via Admin Console (OpenShift 4.X).
* Action log message contains % and the repository name is shifted.
* Build pipeline failed on Sonar stage for Python applications.
* Inability to create AWS credentials in Jenkins.
* Auto deploy works only for the first CD pipeline stage.
* Impossible to create a CD pipeline with a disabled ‘application to promote’ option.
* The default branch for GitHub/GitLab provisioners.
* Linters are not created in the Code Review pipeline for applications added with the Import strategy.
* The Create and Proceed buttons for the Perf Integration are fixed.
* A branch version overwrites the branch name during the new branch creation for a codebase with EDP versioning.
* It is impossible to add an autotest using the Import strategy.
* It is impossible to deploy Go-operator-sdk applications.
* The Code Review pipeline checkouts from incorrect commit for GitLab integration.
* The Build pipeline failed on the Compile stage for .NET-3.1 libraries.
* The framework for groovy libraries is wrong.

#### Documentation

*	The new [EDP Stages](https://github.com/epam/edp-admin-console/blob/release/2.8/documentation/edp-stages.md) documentation describing the stages of EDP CI/CD Framework.
*	The new [Use Open Policy Agent Library in EDP](https://github.com/epam/edp-admin-console/blob/release/2.8/documentation/cicd_customization/opa_stages.md) documentation describing the OPA policy engine.
*	The new [EDP Glossary](https://github.com/epam/edp-install/blob/release/2.8/documentation/edp_glossary.md) documentation defining the most useful EDP terms.
*	The new [Inspect CD Pipeline](https://github.com/epam/edp-admin-console/blob/release/2.8/documentation/inspect_CD_pipeline.md)
documentation describing the editing of CD pipelines.
*	The [EDP Installation on Kubernetes](https://github.com/epam/edp-install/blob/release/2.8/documentation/kubernetes_install_edp.md) page is updated.
*	The [Keycloak Installation on Kubernetes](https://github.com/epam/edp-install/blob/release/2.8/documentation/install_keycloak.md) page is updated.
*	The following pages are updated in the EDP Admin Console guide:
     *	[Add Applications](https://github.com/epam/edp-admin-console/blob/release/2.8/documentation/add_applications.md);
     *	[Add Autotests](https://github.com/epam/edp-admin-console/blob/release/2.8/documentation/add_autotests.md);
     *	[Add CD Pipelines](https://github.com/epam/edp-admin-console/blob/release/2.8/documentation/add_CD_pipelines.md);
     *	[Add Libraries](https://github.com/epam/edp-admin-console/blob/release/2.8/documentation/add_libraries.md);
     *	[Add Other Code Language](https://github.com/epam/edp-admin-console/blob/release/2.8/documentation/add_other_code_language.md);
     *	[Adjust Integration With Jira Server](https://github.com/epam/edp-admin-console/blob/release/2.8/documentation/jira-server.md);
     *	[Delivery Dashboard Diagram](https://github.com/epam/edp-admin-console/blob/release/2.8/documentation/d_d_diagram.md);
     *  [EDP Admin Console Overview](https://github.com/epam/edp-admin-console#overview);
     *	[Inspect Application](https://github.com/epam/edp-admin-console/blob/release/2.8/documentation/inspect_application.md);
     *	[Inspect Autotest](https://github.com/epam/edp-admin-console/blob/release/2.8/documentation/inspect_autotest.md);
     *	[Inspect Library](https://github.com/epam/edp-admin-console/blob/release/2.8/documentation/inspect_library.md);
     *  [Use Lint Stages for Code Review](https://github.com/epam/edp-admin-console/blob/release/2.8/documentation/cicd_customization/code_review_stages.md#use-lint-stages-for-code-review).



## Version 2.7.8 <a name="2.7.8"></a> (May 27, 2021)

#### Breaking Changes

* Sonar KeycloakClient requires new configuration for correct assigning 'sonar-administrators' group to users from OIDC provider.

Update Sonar KeycloakClient resource with *spec.protocolMappers* field:
```yaml
spec:
  protocolMappers:
    - config:
        access.token.claim: 'false'
        claim.name: roles
        id.token.claim: 'true'
        jsonType.label: String
        multivalued: 'true'
        userinfo.token.claim: 'true'
      name: realm roles
      protocol: openid-connect
      protocolMapper: oidc-usermodel-realm-role-mapper
```
Then reload *sonar-operator* pod.

#### Enhancements

* The go module is renamed in keycloak-operator.
* Terraform library support is improved (get-version stage is fixed).
* Allure download repository is changed to github.com.

## Version 2.7.7 <a name="2.7.7"></a> (April 30, 2021)

#### Enhancements

* The init stage runtime is decreased in Deploy pipeline.

## Version 2.7.6 <a name="2.7.6"></a> (April 26, 2021)

#### Fixed Issues

* Versioning and Jira integration for Terraform library are fixed.

## Version 2.7.5 <a name="2.7.5"></a> (April 22, 2021)

#### New Functionality

* Cyrillic characters support for Jira is implemented.

## Version 2.7.4 <a name="2.7.4"></a> (April 20, 2021)

#### Upgrades

* The base alpine image for all operators is upgraded to version 3.11.10.

#### Enhancements

* The ecr-to-docker stage is aligned to the EDP versioning workflow.

#### Fixed Issues

* Support for slash characters in ImageStreams objects is fixed.
* The codebase operator with NPE failure under disabled Jira integration is fixed.

## Version 2.7.2 <a name="2.7.2"></a> (April 6, 2021)

#### Fixed Issues

* The keycloak operator dependency is fixed in the values.yaml file.

## Version 2.7.1 <a name="2.7.1"></a> (April 5, 2021)

#### Fixed Issues

* The keycloakOwner value is set for the KeycloakRealm CR (Custom Resource) in order to prevent an error when setting the owner reference with the absent keycloakOwner value.
* The ClusterRole/ClusterRoleBinding mapping is fixed for OpenShift deployment.
* The 'commit-validate' stage logic is fixed.
* The 'create-jira-issue-metadata' stage failure is fixed.
* The allure report paths are fixed.
* The local development environment configuration is aligned with the documentation.
* The deprecated variable normalizedName is removed.

## Version 2.7.0 <a name="2.7.0"></a> (April 2, 2021)

### What's New

With the version 2.7.0, EDP offers a bunch of upgrades for its components, provides new options and enhancements in CI/CD workflow
and refines the Admin Console usage. Amongst the recent developments there are improvements for the Helm, Kubernetes, Keycloak,
Docker, Terraform, and Jira. Besides, EDP migrated from OpenShift delivery to EKS-core cluster, optimized the resources consumption and
honed the work of the libraries and repositories. See the list of upgrades, new functionality and enhancements, as well as the fixed issues, below.

### Upgrades

*  The Helm tool is upgraded to version 3.5.3. For details, please refer to the [Helm 3.5.3](https://github.com/helm/helm/releases/tag/v3.5.3) page on GitHub.
*  Jenkins is upgraded to version 2.263.4. For details, please refer to the [LTS Changelog](https://www.jenkins.io/changelog-stable/) page.
*  Nexus is upgraded to version 3.30.0. For details, please refer to the [Nexus Release Notes](https://help.sonatype.com/repomanager3/release-notes#ReleaseNotes-NexusRepositoryManager3.30.0).
*  Gerrit is upgraded to version 3.3.2. For details, please refer to the [Gerrit Release Notes](https://www.gerritcodereview.com/3.3.html).
*  Keycloak is upgraded to version 12.0.4. For details, please refer to the [Keycloak Release Notes](https://www.keycloak.org/docs/latest/release_notes/#keycloak-12-0-0).
*  EDP-delivery is upgraded to the master version.
*  All go-alpine based images are updated to Alpine version 3.11.8. For details, please refer to the [Alpine Linux official page](https://alpinelinux.org/posts/Alpine-3.10.6-3.11.8-3.12.4-released.html).
*  The Kaniko executor is upgraded to version v1.5.1. For details, please refer to the [Kaniko Release Notes](https://github.com/GoogleContainerTools/kaniko/releases/tag/v1.5.1) page on GitHub.
*  The edp-admin-console Docker image is upgraded to Alpine 3.11.6. For details, please refer to the [Alpine Linux Release Notes](https://alpinelinux.org/posts/Alpine-3.11.6-released.html).

### New Functionality and Enhancements

*  The unused code from operators in a Helm chart, responsible for Kubernetes object provisioning, is removed.
*  CI Helm chart is improved to provide quick feedback from CI for Helm and Dockerfile.
*  EDP Helm charts are updated to follow the chart best practices defined by Helm.
*  It is now possible to create deploy-templates according to Helm chart development best practices.
*  The ‘buildTools’ parameter for Admin Console is added in the Helm chart.
*  The migration from OpenShift delivery to EKS-Core cluster is performed.
*  Orphaned Kubernetes resources are removed and aligned with mutated Kubernetes resources.
*  The possibility to introduce applications in EDP using the Kubernetes native declarative approach is implemented.
*  Resource requirements for EDP components in the Kubernetes cluster are aligned in order to ensure stability and proper scheduling of EDP.
*  The Keycloak client is now deleted after the deletion of KeycloakClient custom resources in Kubernetes.
*  The Keycloak realm is now deleted after the deletion of KeycloakRealm custom resources in Kubernetes.
*  The functionality supporting the Realm installation without integration with external Identity Provider is implemented.
*  The ability to manage Realm Roles in Keycloak using custom resources is implemented.
*  It is now possible to enable a Service Account for Keycloak Client using a configuration in custom resources.
*  It is now possible to specify the protocolMappers in KeycloakClient custom resources.
*  The ability to manage Keycloak Groups using separate custom resources is implemented.
*  An option to generate and create a secret for Keycloak Client is provided if the client is specified as confidential.
*  It is now possible to manage a batch of Keycloak Realm Roles in a single custom resource.
*  The possibility to switch off an automatic redirection in a certain realm is provided.
*  The possibility to run Terraform plan/apply with EDP stages using AWS Credentials Approach is implemented.
*  The documentation on the usage of Terraform in EDP is created.
Please refer to the [Use Terraform Library in EDP](https://github.com/epam/edp-admin-console/blob/master/documentation/cicd_customization/terraform_stages.md) page.
*  Dockerfile is removed from the edp-install repository.
*  The possibility to add Terraform code via the Admin console using CI stages is implemented.
Please refer to the [Library Info](https://github.com/epam/edp-admin-console/blob/master/documentation/add_libraries.md#the-library-info-menu) block on the
[Add Libraries](https://github.com/epam/edp-admin-console/blob/master/documentation/add_libraries.md#add-libraries) page.
*  The Search, Sort, and Filter functionality is provided in Admin Console.
Please refer to the [Inspect Application](https://github.com/epam/edp-admin-console/blob/master/documentation/inspect_application.md#inspect-application),
[Inspect Autotests](https://github.com/epam/edp-admin-console/blob/master/documentation/inspect_autotest.md#inspect-autotest),
[Inspect Library](https://github.com/epam/edp-admin-console/blob/master/documentation/inspect_library.md#inspect-library), and
[Add CD Pipelines](https://github.com/epam/edp-admin-console/blob/master/documentation/add_CD_pipelines.md#add-cd-pipelines) pages.
*  The databases block is removed from the Add application page as well as from app templates.
*  The text in the 'Advance Mapping' section is updated.
*  The capability to run Jenkins job during the deletion of a codebase is developed.
*  The init stage in the deployment pipeline is optimized to collect information only from the related codebases.
*  The possibility to run AQA tests for sit environment of the EDP deploy pipeline is implemented.
*  The ability is developed to prevent the automatic run of jobs for deleting the codebase branch if they have not finished previously.
*  The handling is added of the ADDITIONAL_BUILDTOOL_ARGS variable in EDP pipelines for the Maven build tool.
*  The codebasePath variable for the EDP4EDP release pipeline is added.
*  The links are updated in the edp-library pipelines for the master branch.
*  The pipeline init stage is modified to run auto-deploy correctly.
Please refer to the [Stages Menu](https://github.com/epam/edp-admin-console/blob/master/documentation/add_CD_pipelines.md#the-stages-menu) block of the Admin Console user guide
for the details on selecting the Auto Trigger Type.
*  Unnecessary codebases used during deployment are mitigated.
*  The possibility to run the Gradle autotests is added.
Please refer to the [Autotest Info](https://github.com/epam/edp-admin-console/blob/master/documentation/add_autotests.md#the-autotest-info-menu) block
of the EDP Admin Console Guide.
*  The backend validation process for codebases and cd pipelines is improved to not clear the form with already input data.
*  The delay time between DeletionJob reconciliation is increased to 10 seconds in case an error occurs.
*  The application provisioning process is improved.
*  The functionality intended to define which metadata from EDP should be published to Jira in predefined format is implemented.
Please refer to the [Advanced Settings](https://github.com/epam/edp-admin-console/blob/master/documentation/add_applications.md#the-advanced-settings-menu) block of the EDP Admin Console guide
for the updates on the Jira integration.
*  A new Jira pattern EDP_SEM_VERSION is added for pushing component version value in the MAJOR.MINOR.PATCH format to Jira.
*  The mirroring approach is implemented when cloning git metadata from source repositories.
*  The section displaying commit author names in the history of repositories is removed.
*  An option to use Shared Libraries from private repositories is now available.
*  The template is added for the edp-library-pipelines repository.
*  The links in every EDP repository on https://github.com/epam were updated.
*  The links to the new EDP location (github.com/epam/edp-COMPONENT) are updated.
* The possibility to specify openid-connect in the Client Protocol for corresponding custom resources is implemented.
* The possibility to run multiple EDP instances without privilege escalation is implemented.
* CPU Request/Limits are adjusted to align with the latest resource consumption.

### Fixed Issues

* The AUTH_KEYCLOAK_ENABLED flag is now configurable from Helm charts.
* The possibility to run multiple EDP deployments inside a single Kubernetes cluster is now provided.
* The visibility status in the projects created by Sonar is corrected to be ‘Private” by default.
* Customer resources for Sonar provide the correct dbImage.
* The Sonar operator now uses the correct Sonar token name for plugin configuration.
* Credentials for Shared Libraries are now saved in Jenkins after the installation.
* The saving of credentials for shared libraries in Jenkins is fixed.
* The Add button for editing the CD Pipeline is fixed.
* The issues occurring during the transition to the Delivery Dashboard diagram are fixed.
* The process of creating a Go application is improved.
* Jira integration functionality is now disabled when adding a codebase with the GitlabCI tool.
* The non-relevant provisioner code in the documentation was corrected.
* Missing parameters are added to the 'Add Job Provision' documentation.
* The validation message during the creation of an application/autotest is displayed as expected.
* Layout issues occurring during the process of adding quality gate autotests are corrected.
* Parallel autotests running in the deploy stage are set.
* The parallel execution of autotests is fixed.
* The newly created branch now takes the default branch as a basis of the Branch Version field.
* The default branch is now created when adding a codebase.
* EDP versioning is working as expected upon correcting an issue with the codebasebranch operator.
* The process of creating a branch using the specified hash commit is corrected.
* The Codebase branch behaviour is corrected to allow deletion of the entity even if its name contains only digits.
* The CRD schema for codebasebranch is corrected.
* The process of getting tags from codebase image streams during init deploy is fixed.
* The functionality allowing the user to deploy applications added using Gitlub CI as a CI tool is corrected.
* The Reconciler failure occurring during the JenkinsJob CR processing is fixed.
* The link in '.gitlab-ci.yml' file containing 'CLUSTER_URL' to the old cluster is updated.
* The database migration script is corrected to perform successful migration for all releases, starting from 2.5.x.
* The error ‘pq: invalid input value for enum "edp-delivery".action’ in the stack-trace logs is corrected.

#### Documentation

* The [Associate IAM Roles With Service Account](https://github.com/epam/edp-admin-console/blob/master/documentation/enable_irsa.md) page is added.
* The [Promote Docker Images From ECR to Docker Hub](https://github.com/epam/edp-admin-console/blob/master/documentation/cicd_customization/ecr_to_docker_stage.md#promote-docker-images-from-ecr-to-docker-hub) page is added.
* The [Use Terraform Library in EDP](https://github.com/epam/edp-admin-console/blob/master/documentation/cicd_customization/terraform_stages.md#use-terraform-library-in-edp) page is added.
* The [Use Lint Stages for Code Review](https://github.com/epam/edp-admin-console/blob/master/documentation/cicd_customization/code_review_stages.md#use-lint-stages-for-code-review) page is added.
* The [Add Other Code Language](https://github.com/epam/edp-admin-console/blob/master/documentation/add_other_code_language.md) instruction is updated.
* The [EDP Component Operator](https://github.com/epam/edp-component-operator/tree/master#edp-component-operator) instruction is updated.
* The following pages are updated in the EDP Admin Console user guide:
    * [Add Applications](https://github.com/epam/edp-admin-console/blob/master/documentation/add_applications.md#add-applications)
    * [Add Autotests](https://github.com/epam/edp-admin-console/blob/master/documentation/add_autotests.md#add-autotests)
    * [Add CD Pipelines](https://github.com/epam/edp-admin-console/blob/master/documentation/add_CD_pipelines.md#add-cd-pipelines)
    * [Add Libraries](https://github.com/epam/edp-admin-console/blob/master/documentation/add_libraries.md#add-libraries)
    * [Inspect Applications](https://github.com/epam/edp-admin-console/blob/master/documentation/inspect_application.md#inspect-application)
    * [Inspect Autotests](https://github.com/epam/edp-admin-console/blob/master/documentation/inspect_autotest.md#inspect-autotest)
    * [Inspect Library](https://github.com/epam/edp-admin-console/blob/master/documentation/inspect_library.md#inspect-library)

## Version 2.6.4 <a name="2.6.4"></a> (January 22, 2021)

#### Enhancements

* Custom Resources Definition codebasebranches are improved.

## Version 2.6.3 <a name="2.6.3"></a> (January 18, 2021)

#### Enhancements

* The 'buildTools' parameter for Admin Console is added in the Helm chart.

## Version 2.6.2 <a name="2.6.2"></a> (January 11, 2021)

#### Enhancements

* The apiVersion type of Admin Console deployment is updated.

#### Fixed Issues

* The database image path for Sonar Custom Resource is fixed.

## Version 2.6.1 <a name="2.6.1"></a> (January 4, 2021)

#### Enhancements

* The pipeline/stages version in Jenkins was changed from master to release-2.7.
* EDP Helm charts are updated to follow the chart best practices defined by Helm.
* The Perf-operator is deployed by default in EDP version 2.6.1.
* The resource requirements/limits are defined for EDP components.
* Resource requirements for EDP components in the Kubernetes cluster are aligned in order to ensure stability and proper scheduling of EDP.

#### Fixed Issues

* The CRD schema for codebasebranch is corrected.
* EDP versioning functionality was improved to work as expected upon correcting an issue with the codebasebranch operator.

## Version 2.6.0 <a name="2.6.0"></a> (December 22, 2020)

### What's New

With the version 2.6.0, EDP is now deployed on the AWS Core cluster. It becomes more stable due to the alignments of the resources requirements
for EDP operators on the Kubernetes cluster. Besides, this EDP version allows to work with performance metrics and tracking,
and applies a set of enhancements to the Admin Console. The release process is improved by upgrading the CD pipeline creation process.
The versioning enables to define the default branch on your own, and enhancements of Sonar, Helm Chart, Codebase and common design contribute to the effective work with EDP.
See the details and the list of components upgrades, as well as fixed issues, below.

### Upgrades

* Gerrit is updated to the 3.2.5.1 version.
For details, please refer to the [Gerrit Release](https://www.gerritcodereview.com/3.2.html#325).
* The Nexus repository manager is now updated to the 3.29.0 version.
For details, please refer to the [Nexus Release Notes](https://help.sonatype.com/repomanager3/release-notes?_ga=2.107131649.2033944580.1593595289-847168532.1593595289#ReleaseNotes-NexusRepositoryManager3.29.0).
* The Sonar-Gerrit plugin is updated for the integration with Java 11.
* The Keycloak proxy is updated to v.10.0.0.
For details, please refer to the [Keycloak Release Notes](https://www.keycloak.org/docs/latest/release_notes/#keycloak-10-0-0).
* Keycloak is updated to the 11.0.2 version.
* The new Keycloak version 11.0.3 is deployed on the AWS EKS core cluster.
* The AWS EKS core cluster of v.1.18 is deployed.
* Jenkins is updated to the 2.263.1 For details, please refer to the [LTS Changelog page](https://www.jenkins.io/changelog-stable/).
* The Kaniko executor is updated to the 3.0 version. For details, please refer to [Kaniko CHANGELOG](https://github.com/GoogleContainerTools/kaniko/blob/master/CHANGELOG.md#v130-release-2020-10-22) page on GitHub.
* The Helm tool is updated to the 4.2 version.
* The edp-admin-console Docker image is upgraded from golang:1.10.3-alpine3.8 to alpine:3.11.6.

### New Functionality and Enhancements

* Now EDP is integrated with the [EPAM PERF Board statistics and monitoring platform](https://perf-help.delivery.epam.com/help/) allowing to track the overall team performance as well as to set up necessary metrics.
* The connection to EPAM Perf Board is established and the new EDP PERF operator is created.
Please refer to the [Perf Operator](https://github.com/epmd-edp/perf-operator#perf-operator) page for details.
* EDP Admin Console enables to select Jenkins, Sonar, and GitLab as a DataSource for Perf integration during the application creation.
Please refer to the [Advanced Settings](https://github.com/epam/edp-admin-console/blob/master/documentation/add_applications.md#the-advanced-settings-menu) block of the Admin Console user guide.
* Application data page provides details on which DataSource is integrated with Perf.
* Now EDP Admin Console allows direct modifying of the existing CD pipeline by adding new extra steps, thus providing the ability to improve the release process.
Please refer to the [Add CD Pipelines](https://github.com/epam/edp-admin-console/blob/master/documentation/add_CD_pipelines.md) page of the Admin Console user guide.
* A unified approach is applied for the link generation in EDP Admin Console.
* EDP Admin Console can perform with context paths.
* The new link that leads to Perf Board is added on the Admin Console Overview page.
* Following the community`s feedback trends in development, the master branch is not hardcoded anymore and enables to define the default branch on your own.
Please refer to the [Application Info](https://github.com/epam/edp-admin-console/blob/master/documentation/add_applications.md#the-application-info-menu) block of the Admin Console user guide for the details.
* The EDP versioning logic is applied to the non-master default branch.
* It is possible to select the provisioner for the CD pipeline during its creation.
* A new ability to run Gatling performance tests using EDP.
* EDP is now deployed on the AWS EKS core cluster. For the details, please refer to the [AWS EKS page](https://aws.amazon.com/ru/about-aws/whats-new/2020/10/amazon-eks-supports-kubernetes-version-1-18/).
* The proper Ingress for the Helm chart is generated when using paths.
* Now Sonar has the installed C# plugin.
* Resource requirements for EDP operators on the Kubernetes cluster are aligned, thus making EDP more stable and with proper scheduling.
* The edp-install-wizard-db database is renamed to edp-db and aligned with the functionality provided by this pod.
* The working directory is cleaned up during the beginning of the codebase reconciliation.
* The unnecessary check for EDP versioning type in the codebase operator is removed.
* A new check for the JiraServer CR status allowing the Codebase operator to handle a case when the Jira server is not available.
* The CodebaseImageStream CR in OpenShift is the same as in Kubernetes.
* The improved common design of the buttons in the Application Code Language/Framework block is applied.

#### Fixed Issues

* There are broken links on the Admin Console Overview page.
* The Python library is not provisioned in Admin Console.
* The Confirmation window does not provide any data about the multi-module project.
* The Jenkins Slave element should not contain unnecessary values.
* The Jenkins DataSource is available for selection when adding a codebase with the Gitlab CI tool.
* Jenkins Operator creates a lot of rolebindings on the deployed environments.
* Jenkins Operator resets parameters of CD pipeline after the restarting.
* Jenkins does not delete the corresponding job of the CD pipeline after removing a stage.
* OpenShift admin group has the wrong role.
* OpenShift groups are removed after the version update.
* The CD Pipeline overview page does not display deployed version if a codebase is deployed by deployment.
* The Docker image name is missed on the pipeline overview page.
* The Build pipeline for the Python library does not contain stages.
* The wrong tooltip for the Branch filed in the Adding Stage dialog box is displayed.
* The codebase branches with the same name in different codebases are deleted after one of them is removed.
* The codebase overview page is blank when adding an application with the EDP versioning.
* The loading of static resources to the diagram page with enabled basePath functionality is fixed.
* There is a permission issue with access rights during the initialization of the PostgreSQL database.
* EDP install does not add the admin role to the EDP service account if a namespace has been created beforehand.
* The undescriptive tooltips are displayed for the Repository login and Repository password fields.

#### Documentation

* The [EDP Installation on Kubernetes](https://github.com/epam/edp-install/blob/master/documentation/kubernetes_install_edp.md) page is updated.
* The [EDP Installation on OpenShift](https://github.com/epam/edp-install/blob/master/documentation/openshift_install_edp.md) page is updated.
* The [EDP Overview](https://github.com/epam/edp-install/tree/master#epam-delivery-platform-rocket) page is updated.
* The [EDP Pipeline Framework](https://github.com/epam/edp-admin-console/blob/master/documentation/cicd_customization/edp_pipeline_framework.md#edp-pipeline-framework) page is added.
* The [Keycloak Installation on Kubernetes](https://github.com/epam/edp-install/blob/master/documentation/install_keycloak.md) page is updated.

