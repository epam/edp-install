# KubeRocketCI Releases

## Overview

Get acquainted with the latest KubeRocketCI releases.

* [Version 3.13.1](#3.13.1)
* [Version 3.13.0](#3.13.0)
* [Version 3.12.4](#3.12.4)
* [Version 3.12.3](#3.12.3)
* [Version 3.12.2](#3.12.2)
* [Version 3.12.1](#3.12.1)
* [Version 3.12.0](#3.12.0)
* [Version 3.11.3](#3.11.3)
* [Version 3.11.2](#3.11.2)
* [Version 3.11.1](#3.11.1)
* [Version 3.11.0](#3.11.0)

For earlier releases, please refer to the [OLD-RELEASES.md](OLD-RELEASES.md) file.

## Version 3.13.1 <a name="3.13.1"></a> (April 4, 2026)

### Fixed Issues

* Fixed pagination failure in Tekton Results PipelineRun query and performance bottleneck caused by sequential summary lookups. Added promise coalescing to prevent thundering herd on cache miss. ([EPMDEDP-16623](https://jira.epam.com/jira/browse/EPMDEDP-16623), [#202](https://github.com/KubeRocketCI/krci-portal/pull/202), [#194](https://github.com/KubeRocketCI/krci-portal/pull/194))
* Fixed live log subscription causing page freezing by adding throttling to subscription updates. ([EPMDEDP-16617](https://jira.epam.com/jira/browse/EPMDEDP-16617), [#191](https://github.com/KubeRocketCI/krci-portal/pull/191))
* Fixed misaligned `inputDockerStreams` in CDPipeline edit form. ([EPMDEDP-16633](https://jira.epam.com/jira/browse/EPMDEDP-16633), [#197](https://github.com/KubeRocketCI/krci-portal/pull/197))
* Fixed GitFusion HTTP status codes not being mapped to tRPC error codes; 404 and 401 errors now surface correctly in the portal. ([EPMDEDP-16620](https://jira.epam.com/jira/browse/EPMDEDP-16620), [#193](https://github.com/KubeRocketCI/krci-portal/pull/193))
* Fixed security vulnerability in `go-git` library (5.16.5→5.17.1) in codebase-operator and cd-pipeline-operator. ([EPMDEDP-16599](https://jira.epam.com/jira/browse/EPMDEDP-16599), [#188](https://github.com/epam/edp-cd-pipeline-operator/pull/188))

### Enhancements

* OIDC issuer URL is now exposed in the `config.get` API response, enabling clients to discover the issuer without additional configuration. ([EPMDEDP-16577](https://jira.epam.com/jira/browse/EPMDEDP-16577), [#203](https://github.com/KubeRocketCI/krci-portal/pull/203))
* Aligned security pages layouts across Trivy, SonarQube, and Dependency Track sections. ([EPMDEDP-16616](https://jira.epam.com/jira/browse/EPMDEDP-16616), [#192](https://github.com/KubeRocketCI/krci-portal/pull/192))
* PipelineRun list migrated from records table to Tekton Results table for improved performance and reliability. ([EPMDEDP-16623](https://jira.epam.com/jira/browse/EPMDEDP-16623), [#196](https://github.com/KubeRocketCI/krci-portal/pull/196))
* CDPipeline form handling refactored for improved reliability and state management. ([EPMDEDP-16628](https://jira.epam.com/jira/browse/EPMDEDP-16628), [#200](https://github.com/KubeRocketCI/krci-portal/pull/200))
* GitFusion now disables `regcred` image pull secret usage by default, simplifying deployment in environments without private registries. ([EPMDEDP-16599](https://jira.epam.com/jira/browse/EPMDEDP-16599), [#58](https://github.com/KubeRocketCI/gitfusion/pull/58))

## Version 3.13.0 <a name="3.13.0"></a> (March 28, 2026)

### What's new

KubeRocketCI 3.13.0 introduces the new **KubeRocketCI Portal** — a completely new platform UI built from the ground up with React 19, tRPC, and Radix UI. The new portal replaces the previous edp-headlamp component and brings a modern, full-stack architecture with significantly improved user experience and performance.

The portal ships with a **Platform Overview Dashboard** featuring DORA metrics, resource health indicators, and pipeline activity trends. A new **Trivy Security suite** provides vulnerability scanning, configuration audit reports, cluster compliance reports, RBAC assessments, and exposed secrets detection. **SonarQube SAST integration** displays project quality gates, bugs, vulnerabilities, and code coverage directly in the portal. **Dependency Track SCA integration** enables software composition analysis visibility.

The platform now supports **multi-CI environments** with the ability to run both Tekton and GitLab CI pipelines side by side. A new `tektonDisabled` field on GitServer resources allows disabling Tekton Stack for servers that use GitLab CI exclusively.

**GitFusion** is now a core platform dependency (`enabled: true` by default), providing unified Git operations across GitHub, GitLab, and Bitbucket. A new pipeline listing API enables browsing CI/CD pipeline status directly from the portal. Now it also allows for Pull Request and Merge Request browsing.

Container image scanning with **Trivy and Grype** has been added to the CI pipelines, with per-image DefectDojo engagements and per-branch scan isolation for improved security traceability.

We continue to publish helpful video content on our [YouTube channel](https://www.youtube.com/@theplatformteam). Here's the latest content:

* [KubeRocketCI Portal Overview: Getting Started and Navigating the Platform](https://www.youtube.com/watch?v=bgfSDTQSopY)
* [Pipeline Tuning in KubeRocketCI](https://www.youtube.com/watch?v=f4LpMICI5OE)
* [Sensitive Data Management in KubeRocketCI](https://www.youtube.com/watch?v=J7jY7h_33Y4)
* [Quick Links in KubeRocketCI: How to Add and Customize Platform Navigation](https://www.youtube.com/watch?v=oXizHW3XlMw)
* [KubeRocketAI: AI Agents for IDEs](https://www.youtube.com/watch?v=so9E0xjEzp8)
* [Deploying Applications to Remote Kubernetes Clusters with KubeRocketCI and ArgoCD](https://www.youtube.com/watch?v=3Gm8YLj-0x4)

### Breaking Changes

* **Tekton v1beta1 → v1 API migration** — requires Tekton Pipelines >= v0.44. ([#573](https://github.com/epam/edp-tekton/issues/573))
* **External Secrets resources v1beta1 → v1 API** - with automatic fallback for older clusters. ([#486](https://github.com/epam/edp-install/issues/486))
* **GitFusion now enabled by default** — set `gitfusion.enabled: false` to opt out. ([#539](https://github.com/epam/edp-install/issues/539))
* **CodeMie integration** - removed from the platform chart and codebase-operator. ([#535](https://github.com/epam/edp-install/issues/535))
* **Parameter renames in Tekton tasks**: `COMPONENT_NAME` → `CODEBASE_NAME`, `PROJECT_BRANCH` → `CODEBASE_BRANCH`. Custom TriggerTemplates and pipelines referencing old parameter names must be updated. ([#612](https://github.com/epam/edp-tekton/pull/612)) ([#606](https://github.com/epam/edp-tekton/pull/606))
* **DefectDojo engagement naming** - changed to per-branch format. Historical findings will be orphaned under old engagement names. The hardcoded `product_type_name=Tenant` has been removed; use `DD_PRODUCT_TYPE_NAME` parameter. ([#606](https://github.com/epam/edp-tekton/pull/606))
* **`ciTool` enum validation** - added to Codebase CRD. Only `tekton` and `gitlab` values are accepted. Existing CRs with non-standard values will fail validation on next update. ([#260](https://github.com/epam/edp-codebase-operator/pull/260))
* **edp-headlamp deprecated** — migrated configuration values should be moved to the `krci-portal` section. `edp-headlamp.enabled: false` is set by default, replaced by `krci-portal`.
* **Java 8 and Java 11 Tekton pipelines** - marked as deprecated for all codebase types. ([#536](https://github.com/epam/edp-tekton/issues/536))

### Upgrades

* The chart-testing tool has been updated to the [v3.14.0](https://github.com/helm/chart-testing/releases) version. ([#588](https://github.com/epam/edp-tekton/issues/588))
* The golangci-lint component has been updated to the [v2.8.0](https://golangci-lint.run/docs/product/changelog/#v280) version. ([#257](https://github.com/epam/edp-codebase-operator/issues/257))
* Updated Gradle build tool to [7.6.5](https://docs.gradle.org/7.6.5/release-notes.html). ([#549](https://github.com/epam/edp-tekton/issues/549))
* Updated Operator SDK from v1.39.2 to [v1.42.0](https://github.com/operator-framework/operator-sdk/releases/tag/v1.42.0) across all operators. ([#252](https://github.com/epam/edp-codebase-operator/issues/252)) ([#180](https://github.com/epam/edp-cd-pipeline-operator/issues/180))
* Updated Go dependencies for Kubernetes [1.34](https://kubernetes.io/blog/2025/08/27/kubernetes-v1-34-release/) compatibility. ([#580](https://github.com/epam/edp-tekton/issues/580))
* Updated kubectl image from deprecated `bitnamilegacy/kubectl:1.25` to `alpine/kubectl:1.34.2` and migrated all task scripts from bash to POSIX sh for Alpine compatibility. ([#586](https://github.com/epam/edp-tekton/issues/586))

### New Functionality

* Introduced KubeRocketCI Portal ([krci-portal](https://github.com/KubeRocketCI/krci-portal) component) as a new platform UI component, replacing [edp-headlamp](https://github.com/epam/edp-headlamp). The portal is built with React 19, tRPC, and Radix UI and includes a Platform Overview Dashboard, Trivy Security suite, SonarQube SAST integration, and Dependency Track SCA integration. ([#537](https://github.com/epam/edp-install/issues/537))
* Added image digest propagation across the platform. Container image SHA256 digests are now stored in CodebaseImageStream CRD tags and propagated through ArgoCD ApplicationSets for immutable image references. ([#246](https://github.com/epam/edp-codebase-operator/issues/246))
* Added container image scanning with Trivy and Grype to CI pipelines with per-image DefectDojo engagements. ([#608](https://github.com/epam/edp-tekton/pull/608))
* Implemented CIS-based image discovery in image-scan-chart task for automated security scanning of Helm chart images. ([#610](https://github.com/epam/edp-tekton/pull/610))
* Added multi-CI support with `tektonDisabled` field on GitServer CRD and `ciTool` enum validation (`tekton`, `gitlab`) on Codebase CRD. ([#260](https://github.com/epam/edp-codebase-operator/pull/260))
* Added annotation-based GitLab CI template selection via ConfigMap lookup (replaces fragile auto-discovery). ([#261](https://github.com/epam/edp-codebase-operator/pull/261))
* Added pipeline listing API (`GET /api/v1/pipelines`) to GitFusion across GitHub, GitLab, and Bitbucket with filtering, pagination, and caching. ([#54](https://github.com/KubeRocketCI/gitfusion/issues/54))
* Added interactive Portal Tours for guided onboarding of the Projects and Deployments modules. ([#146](https://github.com/KubeRocketCI/krci-portal/pull/146))
* Added `security` pipeline field to CodebaseBranch CRD. ([#254](https://github.com/epam/edp-codebase-operator/issues/254))
* Added support for Python 3.13.11 in Tekton pipelines. ([#597](https://github.com/epam/edp-tekton/issues/597))
* Added support for Java 25 in Tekton build and autotest pipelines. ([#572](https://github.com/epam/edp-tekton/issues/572))
* Added PNPM as a build tool option for JavaScript/TypeScript projects in the portal. ([#801](https://github.com/epam/edp-headlamp/issues/801))
* Added step name parameter support for autotest execution in deploy pipelines, enabling custom naming of test execution steps. ([#590](https://github.com/epam/edp-tekton/pull/590))
* Added deployment submit review step to the portal deployment workflow. ([#184](https://github.com/KubeRocketCI/krci-portal/pull/184))

### Enhancements

* GitFusion is now included in the platform Helm chart and enabled by default. Added Pull Request and Merge Request browsing in the KubeRocketCI portal via GitFusion integration. ([#539](https://github.com/epam/edp-install/issues/539))
* Added KubeRocketAI agents to multiple KubeRocketCI repositories. ([#491](https://github.com/epam/edp-install/issues/491)) ([#535](https://github.com/epam/edp-tekton/issues/535)) ([#220](https://github.com/epam/edp-codebase-operator/issues/220))
* Migrated all Tekton resources from `v1beta1` to `v1` (GA) API. ([#573](https://github.com/epam/edp-tekton/issues/573))
* Migrated External Secrets resources from `v1beta1` to `v1` API version with automatic fallback for older clusters. ([#486](https://github.com/epam/edp-install/issues/486))
* Improved Git resource status updates performance (about 60% faster). ([#227](https://github.com/epam/edp-codebase-operator/issues/227))
* Extended PipelineRun metadata with full commit SHA, branch name, PR/MR number, full repository path, and target branch for review pipelines. ([#560](https://github.com/epam/edp-tekton/issues/560))
* Updated out-of-the-box autotest pipelines. ([#521](https://github.com/epam/edp-tekton/issues/521))
* Added support for scanning multiple Docker images in the `image-scan-remote` pipeline per execution. ([#531](https://github.com/epam/edp-tekton/issues/531))
* Added `CloneRepositoryCredentials` field to Codebase CR specification for clone strategy with private repositories. ([#234](https://github.com/epam/edp-codebase-operator/issues/234))
* Removed Maven test ignore flag for proper test execution. ([#601](https://github.com/epam/edp-tekton/issues/601))
* Added `securityContext` with `runAsUser: 0` to sonar-scanner step for proper permissions. ([#577](https://github.com/epam/edp-tekton/issues/577))
* Updated Git URL path validation in codebase creation (disallow trailing spaces). ([#236](https://github.com/epam/edp-codebase-operator/issues/236))
* Parameterized DefectDojo integration with per-branch scan isolation. Each git branch now gets its own engagement (e.g., `code-security-main`), preventing cross-branch finding contamination. ([#606](https://github.com/epam/edp-tekton/pull/606))
* Parameterized container images in security tasks with `BASE_IMAGE` parameters for centralized registry configuration. ([#594](https://github.com/epam/edp-tekton/pull/594))
* Replaced deprecated `sonar.login` SonarQube parameter with current supported alternative. ([#576](https://github.com/epam/edp-tekton/pull/576))
* Consolidated Bitbucket event processing to eliminate code duplication. ([#591](https://github.com/epam/edp-tekton/pull/591))

### Fixed Issues

* Fixed helm-lint and SonarQube configuration compatibility issues. ([#604](https://github.com/epam/edp-tekton/issues/604))
* Fixed image tagging to use branch name instead of SHA. ([#596](https://github.com/epam/edp-tekton/issues/596))
* Fixed git-sha value for GitLab when merge strategy doesn't create merge commit. ([#569](https://github.com/epam/edp-tekton/issues/569))
* Fixed GitLab interceptor to use `merge_commit_sha` with fallback to `last_commit.id` for fast-forward merges. ([#569](https://github.com/epam/edp-tekton/issues/569))
* Added `NEXUS_HOST_URL` variable to autotests task. ([#613](https://github.com/epam/edp-tekton/issues/613))
* Added support for onboarding Git projects using access token instead of SSH keys. ([#231](https://github.com/epam/edp-codebase-operator/issues/231))
* Fixed image-scan-remote to align with image-scan-chart approach, eliminating cross-contamination bug where shared engagements incorrectly closed findings from other images. ([#612](https://github.com/epam/edp-tekton/pull/612))
* Fixed auto-restart of K8s watch on API server timeout with improved stale data recovery in the portal. ([#182](https://github.com/KubeRocketCI/krci-portal/pull/182))
* (edp-headlamp) Fixed Base64 vulnerability connected with Quick Links in the portal. ([#856](https://github.com/epam/edp-headlamp/issues/856))
* (edp-headlamp) Fixed multiple autotests selection in quality gate configuration. ([#847](https://github.com/epam/edp-headlamp/issues/847))
* (edp-headlamp) Fixed release branch options display in portal. ([#830](https://github.com/epam/edp-headlamp/issues/830))

### Documentation

* Added platform [landing page](https://kuberocketci.io/). ([#313](https://github.com/KubeRocketCI/docs/pull/313))

The [Getting Started](https://docs.kuberocketci.io/docs/about-platform) section is updated with the following:

* The entire section has been updated to reflect UI changes and terminology. ([#335](https://github.com/KubeRocketCI/docs/pull/338)) ([#335](https://github.com/KubeRocketCI/docs/pull/336))

The [Operator Guide](https://docs.kuberocketci.io/docs/operator-guide) section is updated with the following:

* The [Automated Kubernetes Backup and Restore Workflows With Velero](https://docs.kuberocketci.io/docs/next/operator-guide/disaster-recovery/install-velero-add-ons#install-velero) page has been updated. ([#318](https://github.com/KubeRocketCI/docs/pull/318))
* The [Enable Git Resource Discovery](https://docs.kuberocketci.io/docs/next/operator-guide/extensions/git-discovery) page has been updated. ([#295](https://github.com/KubeRocketCI/docs/pull/295))
* The [Efficient Kubernetes Autoscaling With Karpenter and KEDA: A Comprehensive Guide](https://docs.kuberocketci.io/docs/next/operator-guide/kubernetes-cluster-scaling/namespace-and-cluster-autoscaling) page has been updated. ([#302](https://github.com/KubeRocketCI/docs/pull/302))
* The [Atlantis: Enterprise-Grade Terraform Automation for Kubernetes](https://docs.kuberocketci.io/docs/next/operator-guide/infrastructure-providers/atlantis-installation) page has been updated. ([#293](https://github.com/KubeRocketCI/docs/pull/293))

The [User Guide](https://docs.kuberocketci.io/docs/user-guide) section is updated with the following:

* The entire section has been updated to reflect UI changes and terminology. ([#333](https://github.com/KubeRocketCI/docs/pull/333)) ([#336](https://github.com/KubeRocketCI/docs/pull/336))

The [Developer Guide](https://docs.kuberocketci.io/docs/developer-guide) section is updated with the following:

* Update Autotest coverage scheme. ([#317](https://github.com/KubeRocketCI/docs/pull/317))

## Version 3.12.4 <a name="3.12.4"></a> (December 19, 2025)

### Fixed Issues

* Fixed broken cluster mapping when integrating a remote cluster using the bearer token authentication. ([#834](https://github.com/epam/edp-headlamp/issues/834)) ([#835](https://github.com/epam/edp-headlamp/issues/835))
* Fixed incorrect API endpoint in build pipelines for Bitbucket release branches. ([#558](https://github.com/epam/edp-tekton/issues/558))

## Version 3.12.3 <a name="3.12.3"></a> (October 6, 2025)

### Fixed Issues

* Rollback git-init image version to fix large repository cloning issues. ([#538](https://github.com/epam/edp-tekton/issues/538))

## Version 3.12.2 <a name="3.12.2"></a> (October 2, 2025)

### Fixed Issues

* Fixed the issue requiring double authentication in incognito mode. ([#815](https://github.com/epam/edp-headlamp/issues/815))

## Version 3.12.1 <a name="3.12.1"></a> (October 2, 2025)

### Enhancements

* Migrated to bitnamilegacy image repository from [deprecated bitnami](https://github.com/bitnami/charts/issues/35164) for improved reliability. ([#540](https://github.com/epam/edp-tekton/issues/540))

## Version 3.12.0 <a name="3.12.0"></a> (July 22, 2025)

### What's new

A new "History" tab has been added to the Pipelines section. It displays the history of PipelineRun executions retrieved from external storage, allowing users to access historical data.

Several new pipeline types have been introduced to the platform:

* **Security pipelines** run dedicated security scans using specialized tools. By separating security scanning from build pipelines, teams can identify vulnerabilities earlier while reducing overall build times.
* **Test pipelines** execute automated tests for environments independently of deployments. This allows teams to validate changes quickly without the overhead of full environment deployment.
* **Release pipelines** adapt to your organization's unique approval processes, ensuring consistent, compliant releases.

These workflows operate independently, giving teams flexibility to optimize their product delivery for maximum efficiency.

The KubeRocketCI portal now supports automatic discovery of Git accounts, repositories, and branches. Users no longer need to manually enter full repository paths or branch names — they can simply select them from a list. This significantly improves usability, reduces input errors, and speeds up onboarding and configuration within the platform.

To help users get started and make the most of the platform, we continue to publish helpful video content on our [YouTube channel](https://www.youtube.com/@theplatformteam). Over the past three months, we’ve released a series of tutorials and overviews aimed at improving usability and deepening understanding of KubeRocketCI features. Here’s the latest content:

* 🎥[KubeRocketCI: Autotests Overview](https://youtu.be/ytaO-ZaQb0c?si=_SP7nQUB9gfgz7o5)
* 🎥[KubeRocketCI: GitOps Overview](https://youtu.be/gFR4HHdDoQM?si=awU9Te_QZjAQwrtn)
* 🎥[KubeRocketCI: Testing Changes With Feature Branch](https://youtu.be/V_xPrSgbMRg?si=OROGb6m2dNQz9b7q)
* 🎥[KubeRocketCI: Branches Management](https://youtu.be/AsWQKiRvNDY?si=EOc_m_BCOyL1HaA9)
* 🎥[KubeRocketCI: Deep Dive into CI/CD Pipelines](https://youtu.be/1DYKYpWOQP0?si=xiGxU5gqQ_-nktjw)
* 🎥[KubeRocketCI: Deep Dive into Codebases](https://youtu.be/uH2FVcEWtmg?si=Xi8R2e6C7Vgt9gdh)
* 🚀[KubeRocketCI v3.11 Release Demo | Key Features Overview](https://youtu.be/CLlPv4vKadM?si=2yk1Koe2vPt-7uxQ)
* 🎤[[Podcast] KubeRocketCI Codebase Overview](https://youtu.be/-s8eL6zpzGs?si=1zslCOrIaTKgsPGu)
* 🎤[[Podcast] KubeRocketCI Architecture Overview](https://youtu.be/Zu_YyHLYurE?si=xhZbuMrlV9ELF1XG)
* 🎤[[Podcast] KubeRocketCI Platform Overview](https://youtu.be/ip6B7g917ps?si=DImWO7u5k52SUI_o)

### New Functionality

* Several new pipeline types have been added to the platform: Security, Release, and Test. ([#755](https://github.com/epam/edp-headlamp/issues/755)) ([#757](https://github.com/epam/edp-headlamp/issues/757))
* A "History" tab has been added to the Pipelines section. This tab shows the history of PipelineRun executions stored in OpenSearch. ([#740](https://github.com/epam/edp-headlamp/issues/740)) ([#741](https://github.com/epam/edp-headlamp/issues/741))
* KubeRocketCI now supports deploying applications to remote clusters in Capsule tenants. ([#453](https://github.com/epam/edp-install/issues/453))
* The ability to create a branch from another branch has been added to codebases. ([#214](https://github.com/epam/edp-codebase-operator/issues/214))
* The ability to override [podSecurityContext](https://kubernetes.io/docs/tasks/configure-pod-container/security-context/) and [securityContext](https://kubernetes.io/docs/tasks/configure-pod-container/security-context/) parameters has been added to the Helm chart configuration. This allows users to define custom security settings at the pod and container levels. ([#162](https://github.com/epam/edp-cd-pipeline-operator/issues/162))
* The `deployableResources` parameter has been added to the platform configuration. It allows users to disable creation of default pipelines. ([#456](https://github.com/epam/edp-install/issues/456))
* The `marketplaceTemplates` parameter has been added to the Marketplace template configuration. It allows users to disable template creation for default templates. ([#465](https://github.com/epam/edp-install/issues/465))
* The `podTemplate` parameter has been added to all TriggerTemplate resources within the platform to allow users to control execution environment parameters such as resource limits, node selectors, tolerations, and custom containers. ([#498](https://github.com/epam/edp-tekton/issues/498))

### Enhancements

* To improve traceability, PipelineRuns in the Pipelines section are now ordered by creation date instead of completion date. ([#777](https://github.com/epam/edp-headlamp/issues/777))
* The Git resource management has been improved. Previously, users had to type account name, repository name, and branch name in the KubeRocketCI portal. Now users select these resources in the drop-down lists. ([#758](https://github.com/epam/edp-headlamp/issues/758))
* The Pipelines section now allows users to filter PipelineRuns by their type. ([#755](https://github.com/epam/edp-headlamp/issues/755))
* Branch naming validation has been aligned with naming patterns supported by version control systems. ([#732](https://github.com/epam/edp-headlamp/issues/732))
* A copy button has been added to the "Deployed Versions" column of the Environment details page. This button copies essential deployment information, such as Deployment Flow name, Environment name, namespace, deployed application name, and version. ([#730](https://github.com/epam/edp-headlamp/issues/730))
* Various tooltips have been updated throughout the portal. ([#769](https://github.com/epam/edp-headlamp/issues/769)) ([#779](https://github.com/epam/edp-headlamp/issues/779)) ([#780](https://github.com/epam/edp-headlamp/issues/780))
* A warning message has been added to the Environment details page when enabling the "Values override" option. ([#766](https://github.com/epam/edp-headlamp/issues/766))
* The codebase build number now updates for every pipeline execution, regardless of how the pipeline was started (manual build, rerun, or pull request merge) or whether any tasks were skipped. This ensures that the build version always increments consistently for each successful pipeline execution. ([#509](https://github.com/epam/edp-tekton/issues/509))
* Resource terminology has been updated within Tekton Pipelines and Tasks to align with new resource naming. ([#503](https://github.com/epam/edp-tekton/issues/503)) ([#500](https://github.com/epam/edp-tekton/issues/500))

### Fixed Issues

* Fixed UI flickering when setting or resetting filter values. ([#748](https://github.com/epam/edp-headlamp/issues/748))
* Fixed incorrect placement of filter controls and reset button across the app. ([#748](https://github.com/epam/edp-headlamp/issues/748))
* Fixed an issue where Git links returned 404 error codes and branches were missing after codebase creation using clone strategy from GitHub. ([#791](https://github.com/epam/edp-headlamp/issues/791))
* Fixed an issue that caused infinite PipelineRun page loading if the API URL endpoint was incorrectly specified or missing in the "krci-config" Secret. ([#784](https://github.com/epam/edp-headlamp/issues/784))
* Fixed an issue where the "get-version" step showed incorrect processing time when the PipelineRun failed. ([#780](https://github.com/epam/edp-headlamp/issues/780))
* Fixed an issue where incorrect commit hash caused layout shift on the branch creation window. ([#780](https://github.com/epam/edp-headlamp/issues/780))
* Fixed an issue where remote clusters created with bearer authentication tokens were incorrectly shown as IRSA. ([#780](https://github.com/epam/edp-headlamp/issues/780))
* Fixed an issue where typing a slash in the GitLab account name input cleared the field. ([#780](https://github.com/epam/edp-headlamp/issues/780))
* Fixed an issue where the "Undo Changes" button didn't clear the authentication type. ([#775](https://github.com/epam/edp-headlamp/issues/775))
* Fixed an issue where GitLab repository paths were considered invalid. ([#750](https://github.com/epam/edp-headlamp/issues/750))
* Fixed an issue where the "Clear" button for filters in the Pipelines section didn't work correctly. ([#748](https://github.com/epam/edp-headlamp/issues/748))
* Fixed an issue causing the Marketplace to show no results after applying filters, even when matching applications were present. ([#748](https://github.com/epam/edp-headlamp/issues/748))
* Fixed an issue where some Kubernetes labels were incorrectly filtered in the KubeRocketCI portal. ([#743](https://github.com/epam/edp-headlamp/issues/743))
* Fixed an issue where the Pipelines section could show no PipelineRuns. ([#738](https://github.com/epam/edp-headlamp/issues/738))

### Documentation

The [Operator Guide](https://docs.kuberocketci.io/docs/operator-guide) section is updated with the following:

* The [Upgrade KubeRocketCI v3.10 to 3.11](https://docs.kuberocketci.io/docs/operator-guide/upgrade/upgrade-edp-3.11) page has been updated. ([#259](https://github.com/KubeRocketCI/docs/pull/259))
* The [Deploy AWS EKS Cluster](https://docs.kuberocketci.io/docs/operator-guide/deploy-aws-eks) page has been updated. ([#256](https://github.com/KubeRocketCI/docs/pull/256)) ([#255](https://github.com/KubeRocketCI/docs/pull/255))
* The [Install Tekton](https://docs.kuberocketci.io/docs/next/operator-guide/install-tekton) page has been updated. ([#280](https://github.com/KubeRocketCI/docs/pull/280))
* The [Atlantis Installation](https://docs.kuberocketci.io/docs/operator-guide/infrastructure-providers/atlantis-installation) page has been updated. ([#267](https://github.com/KubeRocketCI/docs/pull/267))
* The [Deploy Application In Remote Cluster via IRSA](https://docs.kuberocketci.io/docs/operator-guide/cd/deploy-application-in-remote-cluster-via-irsa) page has been updated. ([#265](https://github.com/KubeRocketCI/docs/pull/265))
* The [Install KubeRocketCI](https://docs.kuberocketci.io/docs/quick-start/platform-installation) page has been updated. ([#269](https://github.com/KubeRocketCI/docs/pull/269))

The [FAQ](https://docs.kuberocketci.io/faq/general-questions) section is updated with the following:

* The [How to Decrease Pipeline Processing Time?](https://docs.kuberocketci.io/faq/how-to/developer/pipeline-processing-time) page has been added. ([#264](https://github.com/KubeRocketCI/docs/pull/264))
* The [How to View Grafana Metrics for an Application?](https://docs.kuberocketci.io/faq/how-to/developer/access-grafana-dashboard) page has been added. ([#264](https://github.com/KubeRocketCI/docs/pull/264))
* The [How to Troubleshoot Issues in the Create Resource Window?](https://docs.kuberocketci.io/faq/how-to/developer/unprocessable-entity-message) page has been added. ([#264](https://github.com/KubeRocketCI/docs/pull/264))
* The [How to Troubleshoot Git Server Connection?](https://docs.kuberocketci.io/faq/how-to/devops/troubleshoot-git-server-connection) page has been added. ([#264](https://github.com/KubeRocketCI/docs/pull/264))

The [User Guide](https://docs.kuberocketci.io/docs/user-guide) section is updated with the following:

* The [Configuration Overview](https://docs.kuberocketci.io/docs/user-guide/configuration-overview) page has been updated. ([#280](https://github.com/KubeRocketCI/docs/pull/280))
* The [Add Git Server](https://docs.kuberocketci.io/docs/user-guide/add-git-server) page has been updated. ([#277](https://github.com/KubeRocketCI/docs/pull/277))
* The duplicate of the  [Deployment Strategies Overview](https://docs.kuberocketci.io/docs/user-guide/auto-stable-trigger-type) page has been deleted. ([#276](https://github.com/KubeRocketCI/docs/pull/276))
* The [Pipelines Overview](https://docs.kuberocketci.io/docs/user-guide/pipelines) page has been updated. ([#268](https://github.com/KubeRocketCI/docs/pull/268))
* The [Manage Branches](https://docs.kuberocketci.io/docs/user-guide/manage-branches) page has been updated. ([#266](https://github.com/KubeRocketCI/docs/pull/266))
* The [Preview Argo CD Diff in Deploy Pipelines](https://docs.kuberocketci.io/docs/user-guide/argo-cd-preview) page has been updated. ([#271](https://github.com/KubeRocketCI/docs/pull/271))

The [Developer Guide](https://docs.kuberocketci.io/docs/developer-guide) section is updated with the following:

* The [Annotations and Labels](https://docs.kuberocketci.io/docs/developer-guide/annotations-and-labels) page has been updated. ([#257](https://github.com/KubeRocketCI/docs/pull/257))

The [Getting Started](https://docs.kuberocketci.io/docs/about-platform) section is updated with the following:

* The [Glossary](https://docs.kuberocketci.io/docs/glossary) page has been updated. ([#275](https://github.com/KubeRocketCI/docs/pull/275))

## Version 3.11.3 <a name="3.11.3"></a> (May 26, 2025)

### Enhancements

* Remote cluster connection status has been improved by using the lightweight "/api" endpoint, avoiding errors caused by insufficient RBAC permissions. ([#140](https://github.com/epam/edp-cd-pipeline-operator/issues/140))

## Version 3.11.2 <a name="3.11.2"></a> (April 11, 2025)

### New Functionality

* Introduced custom widget functionality on the Overview page for displaying deployment statuses, including Deployment Flows, Environments utilizing the codebase, and deployed codebase versions. ([#709](https://github.com/epam/edp-headlamp/issues/709))

### Enhancements

* Documentation links have been updated across the KubeRocketCI portal to direct users to more relevant and helpful documentation pages. ([#704](https://github.com/epam/edp-headlamp/issues/704))
* To speed up build process and make it possible to run security scans by demand, security steps, such as Dependency-Track and DefectDojo scans, have been moved to a separate pipeline. ([#464](https://github.com/epam/edp-tekton/issues/464))
* Enhanced security by restricting access to source map files to prevent potential attackers from retrieving original frontend source code. ([#720](https://github.com/epam/edp-headlamp/issues/720))

### Fixed Issues

* Fixed an issue when users couldn't see the **Kubeconfig** tab on the **Account settings** windows on the first login. ([#672](https://github.com/epam/edp-headlamp/issues/672))
* Fixed an issue when the **Delete git server** button was displayed in the Git Server integration blank. ([#714](https://github.com/epam/edp-headlamp/issues/714))
* Fixed an issue when the build number in the **CodebaseBranch** custom resource was reset intermittently, preventing the building of new versions of components due to existing tags in the version control system. ([#194](https://github.com/epam/edp-codebase-operator/issues/194))
* Fixed improper search filter input processing. ([#718](https://github.com/epam/edp-headlamp/issues/718))
* Fixed unexpected notification crash when initiating a PipelineRun with parameters. ([#706](https://github.com/epam/edp-headlamp/issues/706))
* Fixed broken pull request links in Pipelines.  ([#460](https://github.com/epam/edp-tekton/issues/460))

## Version 3.11.1 <a name="3.11.1"></a> (March 31, 2025)

### Enhancements

* The `edp-config` ConfigMap has been renamed to `krci-config`. ([#417](https://github.com/epam/edp-install/pull/417))
* Now the `ImagePullSecrets` can be defined in the values.yaml file of the Helm chart. ([#438](https://github.com/epam/edp-install/issues/438))
* The `/recheck` and `/ok-to-test` command logic has been modified. Now users are required to enter only the command. Extra symbols are unacceptable. ([#458](https://github.com/epam/edp-tekton/issues/458))

## Version 3.11.0 <a name="3.11.0"></a> (March 22, 2025)

### What's new

To start from, we introduce the 'protected' label feature to enhance the manageability and integrity of our deployment workflows. When applied to Codebases, Deployment Flows or Environments, this label disables the ability to edit or delete them. This feature is intended to safeguard critical deployment workflows from accidental or unauthorized modifications. For example, to apply the label, add the **app.edp.epam.com/edit-protection: delete-update** label to the resource to deny the update and delete access.

The differential approve feature has been added to the deploy pipelines. This feature allows to see Argo CD difference in application configuration before approving/rejecting them. To enable this feature, select the `diff-approve` deploy pipeline when creating or editing the Environment. This enhancement facilitates proper update validation and improves stability in application envrionments.

Now users can integrate external clusters to the platform using IRSA. This change will come in handy for those who use AWS EKS cluster as additional workloads.

Additionally, now users can delete Git Servers using KubeRocketCI portal. This change facilitates seamless Git Server management and improves user experience. To delete a Git Server from the portal, navigate to **Configuration** -> **Version Control System** and click the bin icon.

This release also offers new Java 21 language version support. It includes updated review and build pipelines to simplify the integration of applications using Java 21.

As usual, the KubeRocketCI portal has undergone several visual improvements. The chat assistant button has been moved to the top-right corner of the screen. The Tekton tab has been relocated from the Configuration section to the Pipelines section. Users can now customize the Components list view by showing or hiding selected columns and adjusting their width. Additionally, a new kubeconfig tab has been added to the Account settings window, allowing users to see the time remaining until session expiration and generate their kubeconfig file for using kubectl on their local machine.

### Upgrades

* Go language is updated to the [1.24](https://tip.golang.org/doc/go1.24) version. ([#444](https://github.com/epam/edp-tekton/issues/444))

### New Functionality

* Deploy pipelines now include a differential approve feature, allowing users to see Argo CD differences in application configuration before approval. Enable it by selecting the diff-approve deploy pipeline in the Environment settings. ([#424](https://github.com/epam/edp-tekton/issues/424))
* The protected label has been added to the platform. When configured, it denies users to modify or delete a resource within the portal. ([#606](https://github.com/epam/edp-headlamp/issues/606))
* The `Developer` role now allows to create, modify, and delete Deployment Flow and Environment resources. Additionally, the `Developers` group is also able to manage `codebasebranch` custom resources. ([#409](https://github.com/epam/edp-install/issues/409))([#400](https://github.com/epam/edp-install/issues/400))
* KubeRocketCI now supports specifying `imagePullSecrets` for the Tekton service account. This is essential for pulling container images from private registries in Tekton pipelines. ([#427](https://github.com/epam/edp-install/issues/427))
* External clusters can now be integrated using IRSA. ([#640](https://github.com/epam/edp-headlamp/issues/640))
* Now users can create a private codebase when using the **Create from template** and **Clone project** creation strategies. To create a private codebase, select the **Private** checkbox. ([#676](https://github.com/epam/edp-headlamp/issues/676))
* Java 21 language version support has been added into the platform. ([#584](https://github.com/epam/edp-headlamp/issues/584))
* The **Delete Git Server** button has been added to the Version Control System tab of the Configuration section. Now users can delete Git Server using UI instead of running a `kubectl delete` command. ([#654](https://github.com/epam/edp-headlamp/issues/654))
* Deployment Flows can now be filtered by application names they include. ([#648](https://github.com/epam/edp-headlamp/issues/648))

### Enhancements

* External cluster connection status is now displayed in the Configuration section. ([#686](https://github.com/epam/edp-headlamp/issues/686))
* A new tab called **kubeconfig** has been added to the **Account settings** window. It allows to view user's `kubeconfig` file and generate a local `kubeconfig` file to manage Kubernetes resources via `kubectl` commands. Additionally, this tab also displays the time remaining to session expiration. ([#667](https://github.com/epam/edp-headlamp/issues/667))
* The **About** tab of the **Platform settings** button now displays the platform version. ([#667](https://github.com/epam/edp-headlamp/issues/667))
* The `edp-config` ConfigMap resource can be found by searching both `edp-config` and `krci-config`. ([#669](https://github.com/epam/edp-headlamp/issues/669))
* The **Results** tab of the PipelineRun details page now supports component hyperlinks. ([#667](https://github.com/epam/edp-headlamp/issues/667))
* Now users can edit Tekton Pipelines and Tasks resources directly in KubeRocketCI portal, in the Pipelines section. ([#665](https://github.com/epam/edp-headlamp/issues/665))
* Tooltips with full text are displayed in fields with large texts. Hover the mouse cursor over the text to see it in full. ([#638](https://github.com/epam/edp-headlamp/issues/638))
* Users can now select which columns to display and set column width. ([#613](https://github.com/epam/edp-headlamp/issues/613))
* A copy button has been added next to the codebase name, codebase branch, and VCS tag in the codebase details page. This enhancement allows to easily copy the codebase information in one click. ([#598](https://github.com/epam/edp-headlamp/issues/598))
* The term **edp** versioning has been updated to **semver** to align with community-recognized terminology. ([#650](https://github.com/epam/edp-headlamp/issues/650))
* The Tekton tab has been excluded from the Configuration section. Now Tekton Pipelines and Tasks are shown in the Pipelines section of the KubeRocketCI portal. ([#382](https://github.com/epam/edp-install/issues/382))
* The **Monitoring** tab is now disabled in the Environment details page if Grafana is not configured for the platform. ([#652](https://github.com/epam/edp-headlamp/issues/652))
* Review and build pipelines are now displayed for the GitOps repositories in the codebase details page. ([#619](https://github.com/epam/edp-headlamp/issues/619))
* The **Chat assistant** button has been moved to the top-right corner of the screen. ([#646](https://github.com/epam/edp-headlamp/issues/646))
* The **Ingress** button has been added to the Environment preview in the Deployment Flow details page. It allows to quickly open the application endpoint. ([#592](https://github.com/epam/edp-headlamp/issues/592))
* Users can now choose which columns to display and adjust their widths. ([#613](https://github.com/epam/edp-headlamp/issues/613))
* The minimum number of items per page has been increased from 5 to 15 in the Components and Deployment Flows lists, and from 5 to 10 on the Environment details page. ([#588](https://github.com/epam/edp-headlamp/issues/588))
* Now the **Build with parameters** option retrieves the pipeline from a corresponding TriggerTemplate resource to initiate a new pipeline run. If no TriggerTemplate is matched, the system defaults to the existing behavior for Tekton PipelineRun creation. ([#678](https://github.com/epam/edp-headlamp/issues/678))
* The Clean and Deploy pipeline links with corresponding pipeline graphs have been added to the Environment overview page. This change will help users identify which pipelines are used in the Environment. ([#594](https://github.com/epam/edp-headlamp/issues/594))
* The Deployment Flow description can now be updated using the **Edit** button. ([#590](https://github.com/epam/edp-headlamp/issues/590))
* When the session token expires, users are redirected to the login page instead of being shown a message indicating they have insufficient permissions to view resources. ([#625](https://github.com/epam/edp-headlamp/issues/625))
* The Login page has been redesigned when using SSO.

### Fixed Issues

* Fixed an issue when Kaniko refused to build and push the images to the Docker Registry using the specified custom certificates. ([#427](https://github.com/epam/edp-tekton/issues/427))
* Fixed an issue when users could create a Deployment Flow with empty application list. ([#617](https://github.com/epam/edp-headlamp/issues/617))
* Fixed an issue when user could get the page crash image when navigating to component details page. ([#623](https://github.com/epam/edp-headlamp/issues/623))
* Fixed an issue when user was redirected to the **Add component info** stage in the codebase creation window if the branch field was empty. ([#680](https://github.com/epam/edp-headlamp/issues/680))
* Fixed an issue when the **Use minimal editor** button didn't work with the build pipeline manifest when the **Build with parameters** option was used to build a codebase. ([#634](https://github.com/epam/edp-headlamp/issues/634))
* Fixed an issue that caused infinite pipeline run list loading on the codebase details page. ([#627](https://github.com/epam/edp-headlamp/issues/627))
* Fixed a UI crash when changing application pod on the application terminal/logs window on the environment details page. ([#629](https://github.com/epam/edp-headlamp/issues/629))
* Fixed an issue when resource quota widget showed excessive amount of error messages in browser console. ([#615](https://github.com/epam/edp-headlamp/issues/615))
* Fixed an issue when the **Account settings** windows unexpectedly crashed on the very first login attempt. ([#636](https://github.com/epam/edp-headlamp/issues/636))
* Fixed an issue when the `APPLICATIONS_PAYLOAD` parameter contained corrupted data when the **Values Override** option was enabled. ([#611](https://github.com/epam/edp-headlamp/issues/611))
* Fixed an issue when the `apiClusterEndpoint` paramter in `edp-config` ConfigMap caused Argo CD diff. ([#421](https://github.com/epam/edp-install/pull/421))([#344](https://github.com/epam/edp-install/issues/344))
* Fixed an issue when KubeRocketCI Installation failed if the `env` parameter was empty when the `config.oidc.enabled` flag was set to `true`. ([#631](https://github.com/epam/edp-headlamp/issues/631))
* Fixed a bunch of crashes when KubeRocketCI portal couldn't get data from Kubernetes. ([#682](https://github.com/epam/edp-headlamp/issues/682))
* Fixed an incorrect PipelineRun count in the Overview widget. ([#581](https://github.com/epam/edp-headlamp/issues/581))

### Documentation

* The documentation site now supports the image zoom feature. ([#210](https://github.com/epam/edp-install/issues/210))
* Documentation pages has been equipped with metadata to improve SEO performance. ([#405](https://github.com/epam/edp-install/issues/405)) ([#412](https://github.com/epam/edp-install/issues/412))
* The FAQ section is no longer subject to documentation versioning. ([#173](https://github.com/epam/edp-install/issues/173))
* All pages from old site now redirect links to the new site. ([#414](https://github.com/epam/edp-install/issues/414))
* The site tab icon has been updated. ([#188](https://github.com/epam/edp-install/issues/188))
* The sample namespace has been updated from **edp** to **krci** throughout site. ([#415](https://github.com/epam/edp-install/issues/415))
* The **edp** versioning has been renamed to **semver** throughout site. ([#418](https://github.com/epam/edp-install/issues/418))
* A new section called [How Tos](https://docs.kuberocketci.io/faq/how-to) has been added to the documentation. It serves as a hub of quick answers to the frequently asked question in a form of "question-answer". ([#402](https://github.com/epam/edp-install/issues/402))

The [User Guide](https://docs.kuberocketci.io/docs/user-guide) section is updated with the following:

* The [Artifact Versioning in KubeRocketCI](https://docs.kuberocketci.io/docs/next/user-guide/artifact-versioning) page has been added. ([#412](https://github.com/epam/edp-install/issues/412))
* The [Application and Pipeline Statuses](https://docs.kuberocketci.io/docs/user-guide/application-and-pipeline-statuses) page has been added. ([#195](https://github.com/KubeRocketCI/docs/pull/195))
* The [KubeRocketCI Widgets](https://docs.kuberocketci.io/docs/user-guide/widgets) page has been added. ([#166](https://github.com/KubeRocketCI/docs/pull/166))
* The [Components Overview](https://docs.kuberocketci.io/docs/user-guide/components/) page has been added. ([#423](https://github.com/epam/edp-install/issues/423))
* The [Manage GitOps](https://docs.kuberocketci.io/docs/user-guide/gitops/) page has been added. ([#423](https://github.com/epam/edp-install/issues/423))
* The [Overview](https://docs.kuberocketci.io/docs/user-guide/) page has been updated. ([#423](https://github.com/epam/edp-install/issues/423))
* The [Add Application](https://docs.kuberocketci.io/docs/user-guide/add-application/) page has been updated. ([#423](https://github.com/epam/edp-install/issues/423))
* The [Manage Applications](https://docs.kuberocketci.io/docs/user-guide/application/) page has been updated. ([#423](https://github.com/epam/edp-install/issues/423))
* The [Add Autotest](https://docs.kuberocketci.io/docs/user-guide/add-autotest/) page has been updated. ([#423](https://github.com/epam/edp-install/issues/423))
* The [Manage Autotests](https://docs.kuberocketci.io/docs/user-guide/autotest/) page has been updated. ([#423](https://github.com/epam/edp-install/issues/423))
* The [Add Library](https://docs.kuberocketci.io/docs/user-guide/add-library/) page has been updated. ([#423](https://github.com/epam/edp-install/issues/423))
* The [Manage Libraries](https://docs.kuberocketci.io/docs/user-guide/library/) page has been updated. ([#423](https://github.com/epam/edp-install/issues/423))
* The [Add Infrastructure](https://docs.kuberocketci.io/docs/user-guide/add-infrastructure/) page has been updated. ([#423](https://github.com/epam/edp-install/issues/423))
* The [Manage Infrastructures](https://docs.kuberocketci.io/docs/user-guide/infrastructure/) page has been updated. ([#423](https://github.com/epam/edp-install/issues/423))
* The [Manage Branches](https://docs.kuberocketci.io/docs/user-guide/manage-branches/) page has been updated. ([#423](https://github.com/epam/edp-install/issues/423))
* The [Add Environment](https://docs.kuberocketci.io/docs/user-guide/add-cd-pipeline/) page has been updated. ([#423](https://github.com/epam/edp-install/issues/423))
* The [Manage Environments](https://docs.kuberocketci.io/docs/user-guide/manage-environments/) page has been updated. ([#423](https://github.com/epam/edp-install/issues/423))
* The [Manage Quick Links](https://docs.kuberocketci.io/docs/user-guide/quick-links) page has been updated. ([#158](https://github.com/KubeRocketCI/docs/pull/158))
* The [Manage Git Servers](https://docs.kuberocketci.io/docs/user-guide/git-server-overview/) page has been updated. ([#429](https://github.com/epam/edp-install/issues/429))
* The [Manage Container Registries](https://docs.kuberocketci.io/docs/user-guide/manage-container-registries/) page has been updated. ([#423](https://github.com/epam/edp-install/issues/423))
* The [KubeRocketCI: Tekton Overview](https://docs.kuberocketci.io/docs/user-guide/tekton-pipelines) page has been updated. ([#145](https://github.com/epam/edp-install/issues/145))

The [Developer Guide](https://docs.kuberocketci.io/docs/developer-guide) section is updated with the following:

* The [KubeRocketCI Developer Guide: Architecture, Components, and Contribution Workflow](https://docs.kuberocketci.io/docs/next/developer-guide) page has been updated. ([#144](https://github.com/KubeRocketCI/docs/pull/144))

The [Operator Guide](https://docs.kuberocketci.io/docs/operator-guide) section is updated with the following:

* The [KubeRocketCI Operator Guide: Installation, Configuration, and Administration](https://docs.kuberocketci.io/docs/operator-guide) page has been updated. ([#399](https://github.com/epam/edp-install/issues/399))
* The [Scaling with Karpenter and KEDA](https://docs.kuberocketci.io/docs/next/operator-guide/kubernetes-cluster-scaling/) page has been added. ([#146](https://github.com/KubeRocketCI/docs/pull/146))
* The [Deploy Application In Remote Cluster via IRSA](https://docs.kuberocketci.io/docs/next/operator-guide/cd/deploy-application-in-remote-cluster-via-irsa) page has been added. ([#176](https://github.com/KubeRocketCI/docs/pull/176))
* The [KrakenD Integration](https://docs.kuberocketci.io/docs/operator-guide/extensions/krakend) page has been updated. ([#169](https://github.com/KubeRocketCI/docs/pull/169))
* The [Authentication and Authorization: Overview](https://docs.kuberocketci.io/docs/operator-guide/auth/platform-auth-model) page has been updated. ([#172](https://github.com/KubeRocketCI/docs/pull/172))
* The [Integrate DefectDojo](https://docs.kuberocketci.io/docs/next/operator-guide/devsecops/defectdojo) page has been updated. ([#154](https://github.com/KubeRocketCI/docs/pull/154))
* The [Sonatype Nexus Repository OSS Integration](https://docs.kuberocketci.io/docs/operator-guide/artifacts-management/nexus-sonatype) page has been updated. ([#143](https://github.com/KubeRocketCI/docs/pull/143))

The [Use Cases](https://docs.kuberocketci.io/docs/use-cases) section is updated with the following:

* The [Deploying Feature Branches With KubeRocketCI: A Comprehensive Guide for Efficient Application Testing and Deployment](https://docs.kuberocketci.io/docs/operator-guide/artifacts-management/nexus-sonatype) page has been added. ([#423](https://github.com/epam/edp-install/issues/423))
* The [Scaffold and Deploy FastAPI Application](https://docs.kuberocketci.io/docs/use-cases/application-scaffolding) page has been updated. ([#143](https://github.com/KubeRocketCI/docs/pull/164))
