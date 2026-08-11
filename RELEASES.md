# KubeRocketCI Releases

## Overview

Get acquainted with the latest KubeRocketCI releases.

* [Version 3.15.0](#3.15.0)
* [Version 3.14.1](#3.14.1)
* [Version 3.14.0](#3.14.0)
* [Version 3.13.5](#3.13.5)
* [Version 3.13.4](#3.13.4)
* [Version 3.13.3](#3.13.3)
* [Version 3.13.2](#3.13.2)
* [Version 3.13.1](#3.13.1)
* [Version 3.13.0](#3.13.0)

For earlier releases, please refer to the [OLD-RELEASES.md](OLD-RELEASES.md) file.

## Version 3.15.0 <a name="3.15.0"></a> (August 10, 2026)

### What's New

KubeRocketCI 3.15.0 is a **least-privilege release for the CI plane**. Tekton pipelines, the krci interceptor and the tekton-reporter no longer share a broad ServiceAccount: pipelines now run as `tekton-unprivileged` by default, every Role is reduced to the verbs it actually exercises, secret reads are scoped to named secrets, and configmap access is dropped entirely. Pipelines that legitimately call the Kubernetes API opt back in per Pipeline through the new `app.edp.epam.com/service-account` annotation. Review triggers on GitHub are additionally restricted to allowed pull request authors.

The **pull request reporter** introduced in 3.14.1 is refined for real-world use. Publishing failed-step logs into report comments is now **off by default** and, when enabled, can be overridden per PipelineRun or per GitServer with `app.edp.epam.com/reporter-logs`. Report comments gained a re-run hint, bolded failed rows and an optional recreate strategy instead of in-place updates.

The release also removes a large amount of superseded machinery: **Jira integration** is gone from Tekton pipelines and the portal, the **Tekton pruner** is replaced by Tekton Results, the legacy **Grafana/Prometheus** integration in pipelines-library gives way to the `tekton-monitoring` cluster add-on, and **edp-headlamp** is removed from the platform chart.

**codebase-operator** gets a substantially more robust git layer: CodebaseBranch operations move to a packless transport instead of full clones, create-strategy provisioning becomes idempotent and refuses destructive pushes, SSH host keys are verified for all git, GitServer and Gerrit connections, and TLS certificates are verified in integration secret connection checks. **gerrit-operator** is aligned with Gerrit 3.14 and drops its deprecated Keycloak integration.

### Breaking Changes

* Tekton pipelines now execute under the unprivileged `tekton-unprivileged` ServiceAccount by default, and pipeline, interceptor, autotest and reporter Roles are reduced to the verbs and named secrets they exercise. Custom pipelines whose tasks call the Kubernetes API must name a ServiceAccount with the `app.edp.epam.com/service-account` annotation on the Pipeline, or set `taskRunTemplate.serviceAccountName` in a custom TriggerTemplate. ([EPMDEDP-17248](https://jiraeu.epam.com/browse/EPMDEDP-17248), [#697](https://github.com/epam/edp-tekton/pull/697), [#696](https://github.com/epam/edp-tekton/pull/696), [#693](https://github.com/epam/edp-tekton/pull/693), [#691](https://github.com/epam/edp-tekton/pull/691), [#689](https://github.com/epam/edp-tekton/pull/689))
* Failed-step log publishing in pull request report comments is disabled by default (`reporter.logsReporting: false`), replacing best-effort secret masking with not reading pipeline secrets at all. Opt in globally, or per PipelineRun and GitServer with the `app.edp.epam.com/reporter-logs` annotation. ([EPMDEDP-17248](https://jiraeu.epam.com/browse/EPMDEDP-17248), [#694](https://github.com/epam/edp-tekton/pull/694), [#695](https://github.com/epam/edp-tekton/pull/695))
* The Tekton pruner is removed in favor of Tekton Results. ([EPMDEDP-17248](https://jiraeu.epam.com/browse/EPMDEDP-17248), [#690](https://github.com/epam/edp-tekton/pull/690), [#581](https://github.com/epam/edp-install/pull/581))
* Jira integration is removed from Tekton pipelines and from the build PipelineRun draft in the portal. ([EPMDEDP-17236](https://jiraeu.epam.com/browse/EPMDEDP-17236), [#676](https://github.com/epam/edp-tekton/pull/676), [#684](https://github.com/epam/edp-tekton/pull/684), [#359](https://github.com/KubeRocketCI/krci-portal/pull/359))
* The legacy Grafana/Prometheus integration is removed from pipelines-library, superseded by the `tekton-monitoring` cluster add-on. Enabling both caused Prometheus to scrape the same endpoint twice and double every `sum()`-based dashboard panel. ([EPMDEDP-17266](https://jiraeu.epam.com/browse/EPMDEDP-17266), [#702](https://github.com/epam/edp-tekton/pull/702), [#582](https://github.com/epam/edp-install/pull/582))
* `edp-headlamp` is removed from the platform chart. ([EPMDEDP-17210](https://jiraeu.epam.com/browse/EPMDEDP-17210), [#580](https://github.com/epam/edp-install/pull/580))
* The deprecated Keycloak integration is removed from gerrit-operator. ([#97](https://github.com/epam/edp-gerrit-operator/pull/97))

### Upgrades

* Go base image bumped to `golang:1.25-trixie` for edp-tekton. ([EPMDEDP-17238](https://jiraeu.epam.com/browse/EPMDEDP-17238), [#677](https://github.com/epam/edp-tekton/pull/677))
* gerrit-operator upgraded to Go 1.25, the Kubernetes 1.34 API and golangci-lint v2, with the default Gerrit image bumped for Gerrit 3.14. Provisioning now restores site-owner ownership of `All-Users` and `All-Projects`, drops the `plugin-manager` plugin, and replaces the fixed readiness delay with a startup probe. ([#97](https://github.com/epam/edp-gerrit-operator/pull/97))
* Dependency and vulnerability bumps across the platform: `golang.org/x/net` 0.52.0 → 0.55.0 in GitFusion, `golang.org/x/crypto` 0.50.0 → 0.52.0 and `go-git` 5.13.0 → 5.19.2 in gerrit-operator, and `cel-go`, `grpc`, `go-git` and `oras-go` in codebase-operator and cd-pipeline-operator. ([#73](https://github.com/KubeRocketCI/gitfusion/pull/73), [#95](https://github.com/epam/edp-gerrit-operator/pull/95), [#96](https://github.com/epam/edp-gerrit-operator/pull/96), [#101](https://github.com/epam/edp-gerrit-operator/pull/101), [#313](https://github.com/epam/edp-codebase-operator/pull/313), [#300](https://github.com/epam/edp-codebase-operator/pull/300), [#299](https://github.com/epam/edp-codebase-operator/pull/299), [#210](https://github.com/epam/edp-cd-pipeline-operator/pull/210), [#209](https://github.com/epam/edp-cd-pipeline-operator/pull/209), [#208](https://github.com/epam/edp-cd-pipeline-operator/pull/208), [#207](https://github.com/epam/edp-cd-pipeline-operator/pull/207))

### New Functionality

* Added a ServiceAccount resolution mechanism for PipelineRuns: the krci interceptor reads the `app.edp.epam.com/service-account` annotation from the Pipeline and falls back to `tekton.defaultServiceAccount`. ([EPMDEDP-17248](https://jiraeu.epam.com/browse/EPMDEDP-17248), [#697](https://github.com/epam/edp-tekton/pull/697))
* Added declarative custom Tekton Trigger registration: codebase-operator reconciles an EventListener `labelSelector` and the portal surfaces it in list and topology views. ([EPMDEDP-17247](https://jiraeu.epam.com/browse/EPMDEDP-17247), [#303](https://github.com/epam/edp-codebase-operator/pull/303), [#360](https://github.com/KubeRocketCI/krci-portal/pull/360))
* Added a `gitlab-set-label` task for merge request label voting. ([EPMDEDP-17247](https://jiraeu.epam.com/browse/EPMDEDP-17247), [#686](https://github.com/epam/edp-tekton/pull/686))
* Added a best-effort Argo CD diff preview to the GitOps review pipeline, rendering an MR manifest diff through the `argocd-diff-preview` cluster add-on (ALPHA, GitLab only; requires two flags). ([EPMDEDP-17228](https://jiraeu.epam.com/browse/EPMDEDP-17228), [#671](https://github.com/epam/edp-tekton/pull/671), [#704](https://github.com/epam/edp-tekton/pull/704))
* Added GitHub review pipeline trigger restriction to allowed pull request authors. ([EPMDEDP-17248](https://jiraeu.epam.com/browse/EPMDEDP-17248), [#692](https://github.com/epam/edp-tekton/pull/692))
* Added a normalized `gitUrlPath` hash label on Codebases, letting the interceptor resolve Codebases by label with a full-scan fallback. ([EPMDEDP-17250](https://jiraeu.epam.com/browse/EPMDEDP-17250), [#304](https://github.com/epam/edp-codebase-operator/pull/304), [#698](https://github.com/epam/edp-tekton/pull/698))
* Added validation of Codebase deletion against deployment usage, returning structured errors instead of silently orphaning resources. ([EPMDEDP-17271](https://jiraeu.epam.com/browse/EPMDEDP-17271), [#312](https://github.com/epam/edp-codebase-operator/pull/312))
* Added optional persistence of the portal SQLite session store on a PVC. ([EPMDEDP-17231](https://jiraeu.epam.com/browse/EPMDEDP-17231), [#351](https://github.com/KubeRocketCI/krci-portal/pull/351))
* Added inline duplicate project detection in the portal create wizard. ([EPMDEDP-17229](https://jiraeu.epam.com/browse/EPMDEDP-17229), [#349](https://github.com/KubeRocketCI/krci-portal/pull/349))
* Added optional SSH host key injection to gerrit-operator for predictable Gerrit host keys. ([#98](https://github.com/epam/edp-gerrit-operator/pull/98))
* Added a chart-wide `image` block to edp-tekton so the interceptor and reporter Deployments resolve their image from the values the deploy flow sets. ([EPMDEDP-17269](https://jiraeu.epam.com/browse/EPMDEDP-17269), [#705](https://github.com/epam/edp-tekton/pull/705))

### Enhancements

* Report comments now carry a re-run hint and drop emoji status icons. ([EPMDEDP-17196](https://jiraeu.epam.com/browse/EPMDEDP-17196), [#683](https://github.com/epam/edp-tekton/pull/683))
* Failed task rows are bolded in the report comment table, and a recreate comment strategy is available as an alternative to in-place updates. ([EPMDEDP-17263](https://jiraeu.epam.com/browse/EPMDEDP-17263), [#701](https://github.com/epam/edp-tekton/pull/701))
* GitServer annotations are passed through to the generated GitServer resource. ([EPMDEDP-17248](https://jiraeu.epam.com/browse/EPMDEDP-17248), [#695](https://github.com/epam/edp-tekton/pull/695))
* SonarQube projects are created without going through the Kubernetes API, and the unused `TENANT_NAME` result is dropped from `init-values`. ([EPMDEDP-17248](https://jiraeu.epam.com/browse/EPMDEDP-17248), [#687](https://github.com/epam/edp-tekton/pull/687))
* Host IPC access is denied in the interceptor SecurityContextConstraints on OpenShift. ([EPMDEDP-17248](https://jiraeu.epam.com/browse/EPMDEDP-17248), [#693](https://github.com/epam/edp-tekton/pull/693))
* Chart tests and Python caches are excluded from the packaged edp-tekton chart. ([EPMDEDP-17248](https://jiraeu.epam.com/browse/EPMDEDP-17248), [#693](https://github.com/epam/edp-tekton/pull/693))
* Removed dead git methods and vestigial CodebaseBranch workdir cleanup from codebase-operator. ([EPMDEDP-17257](https://jiraeu.epam.com/browse/EPMDEDP-17257), [#309](https://github.com/epam/edp-codebase-operator/pull/309))
* Replaced clone-based CodebaseBranch git operations with a packless transport. ([EPMDEDP-17251](https://jiraeu.epam.com/browse/EPMDEDP-17251), [#305](https://github.com/epam/edp-codebase-operator/pull/305))
* Made create-strategy provisioning idempotent and made it refuse destructive pushes. ([EPMDEDP-17252](https://jiraeu.epam.com/browse/EPMDEDP-17252), [#308](https://github.com/epam/edp-codebase-operator/pull/308))
* Added SSH host key verification for all git, GitServer and Gerrit connections. ([EPMDEDP-17256](https://jiraeu.epam.com/browse/EPMDEDP-17256), [#310](https://github.com/epam/edp-codebase-operator/pull/310))
* Added TLS certificate verification to integration secret connection checks. ([EPMDEDP-17262](https://jiraeu.epam.com/browse/EPMDEDP-17262), [#311](https://github.com/epam/edp-codebase-operator/pull/311))

### Fixed Issues

* Fixed build reporters voting on the wrong commit: GitHub, GitLab and Bitbucket build reporters now vote on the cloned commit, and a `Completed` aggregate without a cancel reason is reported as success. ([EPMDEDP-17241](https://jiraeu.epam.com/browse/EPMDEDP-17241), [#682](https://github.com/epam/edp-tekton/pull/682))
* Fixed timed-out pipelines never reporting commit status by reserving a `finally` budget. ([EPMDEDP-17203](https://jiraeu.epam.com/browse/EPMDEDP-17203), [#679](https://github.com/epam/edp-tekton/pull/679))
* Fixed the GitLab start commit status not always being posted as `running`. ([EPMDEDP-17203](https://jiraeu.epam.com/browse/EPMDEDP-17203), [#685](https://github.com/epam/edp-tekton/pull/685))
* Fixed PipelineRuns being stopped abruptly in the portal, so `finally` tasks now report the VCS commit status. ([EPMDEDP-17203](https://jiraeu.epam.com/browse/EPMDEDP-17203), [#358](https://github.com/KubeRocketCI/krci-portal/pull/358))
* Fixed archived PipelineRuns with an unfinalized Tekton Results summary rendering as **Running** instead of terminal. ([EPMDEDP-17203](https://jiraeu.epam.com/browse/EPMDEDP-17203), [#348](https://github.com/KubeRocketCI/krci-portal/pull/348))
* Fixed PipelineRun history timestamps not resolving across both Tekton Results watcher generations. ([EPMDEDP-17264](https://jiraeu.epam.com/browse/EPMDEDP-17264), [#363](https://github.com/KubeRocketCI/krci-portal/pull/363))
* Fixed Bitbucket review pipelines being retriggered on metadata-only pull request updates. ([EPMDEDP-17224](https://jiraeu.epam.com/browse/EPMDEDP-17224), [#674](https://github.com/epam/edp-tekton/pull/674))
* Fixed Helm chart publishing to ECR. ([EPMDEDP-17239](https://jiraeu.epam.com/browse/EPMDEDP-17239), [#678](https://github.com/epam/edp-tekton/pull/678))
* Fixed permanent report skips being logged at the wrong level. ([EPMDEDP-17196](https://jiraeu.epam.com/browse/EPMDEDP-17196), [#699](https://github.com/epam/edp-tekton/pull/699))
* Fixed remote checkout to check the fetched ref namespace and force-fetch. ([EPMDEDP-17253](https://jiraeu.epam.com/browse/EPMDEDP-17253), [#307](https://github.com/epam/edp-codebase-operator/pull/307))
* Removed a stray `init` master branch before pushing operator-authored repositories. ([EPMDEDP-17254](https://jiraeu.epam.com/browse/EPMDEDP-17254), [#306](https://github.com/epam/edp-codebase-operator/pull/306))
* Fixed the scaffolded Helm chart README drifting from `helm-docs` output. ([EPMDEDP-17244](https://jiraeu.epam.com/browse/EPMDEDP-17244), [#302](https://github.com/epam/edp-codebase-operator/pull/302))
* Fixed branch deletion in the portal being validated client-side by guesswork instead of against the operator webhook. ([EPMDEDP-17270](https://jiraeu.epam.com/browse/EPMDEDP-17270), [#365](https://github.com/KubeRocketCI/krci-portal/pull/365))
* Fixed the TriggerTemplate ServiceAccount placeholder not resolving for portal-started PipelineRuns. ([EPMDEDP-17267](https://jiraeu.epam.com/browse/EPMDEDP-17267), [#364](https://github.com/KubeRocketCI/krci-portal/pull/364))
* Fixed pipeline tasks not being bound to their own TaskRun. ([EPMDEDP-17234](https://jiraeu.epam.com/browse/EPMDEDP-17234), [#355](https://github.com/KubeRocketCI/krci-portal/pull/355))
* Fixed the `edpDefault` HTTPRoute hostname sentinel in the portal chart template. ([EPMDEDP-17232](https://jiraeu.epam.com/browse/EPMDEDP-17232), [#352](https://github.com/KubeRocketCI/krci-portal/pull/352))
* Fixed the portal SQLite database directory not being writable by the non-root runtime user. ([EPMDEDP-17231](https://jiraeu.epam.com/browse/EPMDEDP-17231), [#353](https://github.com/KubeRocketCI/krci-portal/pull/353))
* Fixed portal integration configuration vulnerabilities against SSRF probing and stored XSS, and removed a ReDoS-prone regex from git URL path normalization. ([EPMDEDP-17229](https://jiraeu.epam.com/browse/EPMDEDP-17229), [#350](https://github.com/KubeRocketCI/krci-portal/pull/350), [#354](https://github.com/KubeRocketCI/krci-portal/pull/354))
* Fixed Gerrit caches not being flushed after installing the All-Projects ACL. ([#100](https://github.com/epam/edp-gerrit-operator/pull/100))

### Documentation

* The [Isolate Deployment Environments with vcluster: Multi-Cluster CD Without a Second Cluster](https://docs.kuberocketci.io/blog/vcluster-deployment-isolation-kuberocketci) blog post has been published. ([EPMDEDP-17261](https://jiraeu.epam.com/browse/EPMDEDP-17261), [#406](https://github.com/KubeRocketCI/docs/pull/406))

The [Getting Started](https://docs.kuberocketci.io/docs/about-platform) section is updated with the following:

* The [Supported Versions and Compatibility](https://docs.kuberocketci.io/docs/supported-versions) page has been updated for the 3.15 release.

The [Operator Guide](https://docs.kuberocketci.io/docs/operator-guide) section is updated with the following:

* The [Tekton Pipeline Monitoring with Prometheus and Grafana](https://docs.kuberocketci.io/docs/operator-guide/ci/tekton-monitoring) page has been updated. ([EPMDEDP-17266](https://jiraeu.epam.com/browse/EPMDEDP-17266), [#407](https://github.com/KubeRocketCI/docs/pull/407))
* The [Tekton Long-Term Log Storage](https://docs.kuberocketci.io/docs/operator-guide/ci/tekton-long-term-storage) page has been updated. ([EPMDEDP-17261](https://jiraeu.epam.com/browse/EPMDEDP-17261), [#404](https://github.com/KubeRocketCI/docs/pull/404))
* The [Deploy Application In Remote Cluster via Token](https://docs.kuberocketci.io/docs/operator-guide/cd/deploy-application-in-remote-cluster-via-token) page has been updated. ([EPMDEDP-17261](https://jiraeu.epam.com/browse/EPMDEDP-17261), [#405](https://github.com/KubeRocketCI/docs/pull/405))
* The [Argo CD Integration](https://docs.kuberocketci.io/docs/operator-guide/cd/argocd-integration) page has been updated. ([EPMDEDP-17261](https://jiraeu.epam.com/browse/EPMDEDP-17261), [#405](https://github.com/KubeRocketCI/docs/pull/405))
* The [Upgrade KubeRocketCI v3.13 to 3.14](https://docs.kuberocketci.io/docs/operator-guide/upgrade/upgrade-krci-3.14) page has been updated. ([EPMDEDP-17210](https://jiraeu.epam.com/browse/EPMDEDP-17210), [#401](https://github.com/KubeRocketCI/docs/pull/401))
* The [Publish SonarQube Reports to Pull Requests](https://docs.kuberocketci.io/docs/operator-guide/code-quality/sonarqube-pr-decoration) page has been added. ([EPMDEDP-17222](https://jiraeu.epam.com/browse/EPMDEDP-17222), [#399](https://github.com/KubeRocketCI/docs/pull/399), [#400](https://github.com/KubeRocketCI/docs/pull/400))

The [User Guide](https://docs.kuberocketci.io/docs/user-guide) section is updated with the following:

* The [Add Custom Tekton Triggers](https://docs.kuberocketci.io/docs/user-guide/customize-ci-triggers) page has been added. ([EPMDEDP-17247](https://jiraeu.epam.com/browse/EPMDEDP-17247), [#402](https://github.com/KubeRocketCI/docs/pull/402))
* The [Add Cluster](https://docs.kuberocketci.io/docs/user-guide/add-cluster) page has been updated. ([EPMDEDP-17261](https://jiraeu.epam.com/browse/EPMDEDP-17261), [#405](https://github.com/KubeRocketCI/docs/pull/405))
* The [Add Git Server](https://docs.kuberocketci.io/docs/user-guide/add-git-server) page has been updated. ([EPMDEDP-16708](https://jiraeu.epam.com/browse/EPMDEDP-16708), [#403](https://github.com/KubeRocketCI/docs/pull/403))

## Version 3.14.1 <a name="3.14.1"></a> (July 20, 2026)

### Upgrades

Tekton cache is updated to the [0.4.5](https://artifacthub.io/packages/helm/epmdedp/tekton-cache/0.4.5) version.

### New Functionality

* Added review pipeline reporting to pull and merge requests: a finished review PipelineRun publishes a single self-updating comment with a per-task status table and the logs of any failed step, across GitHub, GitLab, and Bitbucket. The reporter ships as an independently toggleable `tekton-reporter` component. ([EPMDEDP-17196](https://jiraeu.epam.com/browse/EPMDEDP-17196), [#662](https://github.com/epam/edp-tekton/pull/662), [#663](https://github.com/epam/edp-tekton/pull/663), [#667](https://github.com/epam/edp-tekton/pull/667))
* Added an in-app notifications hub to the portal that surfaces platform events such as failed PipelineRuns in real time, with a header bell, unread badge, popover list, toasts, and per-user read state. ([EPMDEDP-17227](https://jiraeu.epam.com/browse/EPMDEDP-17227), [#342](https://github.com/KubeRocketCI/krci-portal/pull/342), [#344](https://github.com/KubeRocketCI/krci-portal/pull/344))

### Enhancements

* Review PipelineRuns now post a QUEUED commit status at webhook time and report their final state from a single aggregate-driven task, so queued and cancelled runs no longer leave a stuck "in progress" check on the commit. ([EPMDEDP-17203](https://jiraeu.epam.com/browse/EPMDEDP-17203), [#665](https://github.com/epam/edp-tekton/pull/665), [#666](https://github.com/epam/edp-tekton/pull/666))
* Superseded review pipelines (cancel-in-progress) now report a canceled commit status to the Git provider instead of a failure. ([EPMDEDP-17202](https://jiraeu.epam.com/browse/EPMDEDP-17202), [#664](https://github.com/epam/edp-tekton/pull/664))
* The portal now renders cancelled PipelineRuns as a neutral "Cancelled" state, with a dedicated status filter, instead of showing them as failed. ([EPMDEDP-17203](https://jiraeu.epam.com/browse/EPMDEDP-17203), [#346](https://github.com/KubeRocketCI/krci-portal/pull/346))

### Fixed Issues

* Fixed GitHub review pipelines reporting under the "Build Pipeline" status check; review runs now surface under the correct "Review Pipeline" context. ([EPMDEDP-17195](https://jiraeu.epam.com/browse/EPMDEDP-17195), [#660](https://github.com/epam/edp-tekton/pull/660))
* Fixed GitLab review pipelines retriggering on metadata-only merge request updates (label, assignee, title, or thread changes); only new commit pushes and comment commands now trigger a rerun. ([EPMDEDP-17223](https://jiraeu.epam.com/browse/EPMDEDP-17223), [#668](https://github.com/epam/edp-tekton/pull/668))

### Documentation

The [Getting Started](https://docs.kuberocketci.io/docs/about-platform) section is updated with the following:

* The [Install KubeRocketCI](https://docs.kuberocketci.io/docs/quick-start/platform-installation) page has been updated for the 3.14.1 release. ([#398](https://github.com/KubeRocketCI/docs/pull/398))

The [Operator Guide](https://docs.kuberocketci.io/docs/operator-guide) section is updated with the following:

* The [Upgrade KubeRocketCI v3.13 to 3.14](https://docs.kuberocketci.io/docs/operator-guide/upgrade/upgrade-krci-3.14) page has been added. ([#394](https://github.com/KubeRocketCI/docs/pull/394), [#398](https://github.com/KubeRocketCI/docs/pull/398))
* The [How to Install KubeRocketCI: Advanced Setup Guide](https://docs.kuberocketci.io/docs/operator-guide/install-kuberocketci) page has been updated for the 3.14.1 release. ([#398](https://github.com/KubeRocketCI/docs/pull/398))
* SEO metadata and interlinking were improved across the authentication and Microsoft Entra guides. ([#397](https://github.com/KubeRocketCI/docs/pull/397))

The [User Guide](https://docs.kuberocketci.io/docs/user-guide) section is updated with the following:

* The [Add Git Server](https://docs.kuberocketci.io/docs/user-guide/add-git-server) page has been updated to document Git token comment scopes for pull request reporting. ([#393](https://github.com/KubeRocketCI/docs/pull/393))

## Version 3.14.0 <a name="3.14.0"></a> (July 14, 2026)

### What's New

KubeRocketCI 3.14.0 adds **cancel-in-progress** for review pipelines, so superseded Tekton runs stop when a newer commit arrives on the same pull request or merge request. Enable it with `pipelines.cancelInProgress`; cancelled runs still execute `finally` tasks and report status to the git provider.

The release also introduces **Envoy Gateway** support across the platform. HTTPRoute exposure is available for Tekton EventListeners, GitServer webhooks, application Helm chart scaffolding, and the portal Networking tab on stage details. The portal surfaces Gateway API resources, external URLs derived from HTTPRoutes, and optional HTTPRoute configuration in the chart.

**Kubernetes mode** in the portal is significantly expanded with a redesigned cluster overview, custom resource and CRD browsing for users without cluster-wide CRD access, and scale, restart, and rollback actions for Deployments, StatefulSets, and DaemonSets. **GitLab CI** codebases now have a dedicated pipeline list and log viewer in the portal. Operators can sign in with a **Kubernetes Service Account token** when OIDC is not configured.

We continue to publish helpful video content on our [YouTube channel](https://www.youtube.com/@theplatformteam). Here's the latest content:

* [KubeRocketCI: Deep Dive into CI/CD Pipelines](https://www.youtube.com/watch?v=lC97P1XOEmU)
* [KubeRocketCI: Deep Dive into Projects](https://www.youtube.com/watch?v=P_L_R7z7fFY)
* [KubeRocketCI Portal Overview: Getting Started and Navigating the Platform](https://www.youtube.com/watch?v=Qne8WT6VZ9I)
* [KubeRocketCI v3.13 Release Demo | What's New](https://www.youtube.com/watch?v=i18BLlfScdM)
* [Secure Secret Data With ESO and AWS Parameter Store](https://www.youtube.com/watch?v=oa16R_tS_k8)

### Upgrades

* Tekton cache is updated to the [0.4.5](https://artifacthub.io/packages/helm/epmdedp/tekton-cache/0.4.5) version. ([EPMDEDP-17194](https://jiraeu.epam.com/browse/EPMDEDP-17194), [#655](https://github.com/epam/edp-tekton/pull/655))
* Tekton Pipelines dependency bumped to 1.6.2. ([#642](https://github.com/epam/edp-tekton/pull/642))

### New Functionality

* Added cancel-in-progress for review PipelineRuns superseded by a new commit on the same change. Controlled by `pipelines.cancelInProgress`. ([EPMDEDP-17181](https://jiraeu.epam.com/browse/EPMDEDP-17181), [#654](https://github.com/epam/edp-tekton/pull/654))
* Added Envoy Gateway and Gateway API support in the portal, including a Networking tab on stage details with Gateways, HTTPRoutes, and Ingresses, plus HTTPRoute-derived external URLs in the Applications table. ([EPMDEDP-17118](https://jiraeu.epam.com/browse/EPMDEDP-17118), [#311](https://github.com/KubeRocketCI/krci-portal/pull/311))
* Added optional HTTPRoute support for Tekton EventListeners and propagated Envoy Gateway configuration through the platform Helm chart. ([EPMDEDP-17140](https://jiraeu.epam.com/browse/EPMDEDP-17140), [#651](https://github.com/epam/edp-tekton/pull/651)) ([EPMDEDP-17177](https://jiraeu.epam.com/browse/EPMDEDP-17177), [#294](https://github.com/epam/edp-codebase-operator/pull/294))
* Added GitLab CI pipeline list and log viewer for codebases with `ciTool: gitlab`. ([EPMDEDP-17128](https://jiraeu.epam.com/browse/EPMDEDP-17128), [#314](https://github.com/KubeRocketCI/krci-portal/pull/314))
* Added Service Account token login to the portal and made OIDC configuration optional. ([EPMDEDP-17085](https://jiraeu.epam.com/browse/EPMDEDP-17085), [#277](https://github.com/KubeRocketCI/krci-portal/pull/277))
* Expanded Kubernetes mode with custom resource and CRD list and detail pages, permission-aware CR catalog, redesigned cluster overview, and scale, restart, and rollback actions for workloads. ([EPMDEDP-16788](https://jiraeu.epam.com/browse/EPMDEDP-16788), [#280](https://github.com/KubeRocketCI/krci-portal/pull/280))
* Added an admin audit events page with role-based access control and a date-range filter. ([EPMDEDP-17179](https://jiraeu.epam.com/browse/EPMDEDP-17179), [#334](https://github.com/KubeRocketCI/krci-portal/pull/334))
* Added a Monitoring tab on PipelineRun details with per-step CPU and memory metrics from Prometheus. ([EPMDEDP-17180](https://jiraeu.epam.com/browse/EPMDEDP-17180), [#338](https://github.com/KubeRocketCI/krci-portal/pull/338))
* Added stale branch detection and cleanup in codebase-operator, with a stale badge in the portal for branches missing in git. ([EPMDEDP-17188](https://jiraeu.epam.com/browse/EPMDEDP-17188), [#297](https://github.com/epam/edp-codebase-operator/pull/297), [#339](https://github.com/KubeRocketCI/krci-portal/pull/339))
* Added `clusterName` configuration for correct pipeline URLs in Tekton and the portal. ([EPMDEDP-17098](https://jiraeu.epam.com/browse/EPMDEDP-17098), [#647](https://github.com/epam/edp-tekton/pull/647))
* Added **Triggered By** actor display on PipelineRun details. ([EPMDEDP-17150](https://jiraeu.epam.com/browse/EPMDEDP-17150), [#332](https://github.com/KubeRocketCI/krci-portal/pull/332))
* Added current deployed version display in the stage deploy dropdown for CD promotion stages. ([EPMDEDP-17167](https://jiraeu.epam.com/browse/EPMDEDP-17167), [#330](https://github.com/KubeRocketCI/krci-portal/pull/330))
* Added a branch column to the pipeline applications table. ([EPMDEDP-17031](https://jiraeu.epam.com/browse/EPMDEDP-17031), [#267](https://github.com/KubeRocketCI/krci-portal/pull/267))
* Added scroll-to-top and scroll-to-bottom controls to the log viewer. ([EPMDEDP-17077](https://jiraeu.epam.com/browse/EPMDEDP-17077), [#272](https://github.com/KubeRocketCI/krci-portal/pull/272))

### Enhancements

* Added missing Tekton and Tekton Triggers permissions to the developer role. ([EPMDEDP-16728](https://jiraeu.epam.com/browse/EPMDEDP-16728), [#561](https://github.com/epam/edp-install/pull/561))
* PipelineRun list now uses numbered pagination instead of "Load More". ([EPMDEDP-17079](https://jiraeu.epam.com/browse/EPMDEDP-17079), [#316](https://github.com/KubeRocketCI/krci-portal/pull/316))
* Reduced Kubernetes events volume on overview and detail views in Kubernetes mode. ([EPMDEDP-16788](https://jiraeu.epam.com/browse/EPMDEDP-16788), [#280](https://github.com/KubeRocketCI/krci-portal/pull/280))
* Replaced the portal docs link with a YouTube channel card. ([EPMDEDP-17116](https://jiraeu.epam.com/browse/EPMDEDP-17116), [#333](https://github.com/KubeRocketCI/krci-portal/pull/333))
* Upgraded Node.js to 24.18.0 for npm and pnpm Tekton pipelines. ([EPMDEDP-17170](https://jiraeu.epam.com/browse/EPMDEDP-17170), [#653](https://github.com/epam/edp-tekton/pull/653))
* Enabled leader election in codebase-operator for improved HA. ([EPMDEDP-17188](https://jiraeu.epam.com/browse/EPMDEDP-17188), [#297](https://github.com/epam/edp-codebase-operator/pull/297))

### Fixed Issues

* Fixed Maven pipeline cache corruption by isolating the save-cache task from parallel push tasks. ([EPMDEDP-16759](https://jiraeu.epam.com/browse/EPMDEDP-16759), [#646](https://github.com/epam/edp-tekton/pull/646))
* Fixed Entra ID member login by deriving OIDC identity from the ID token. ([EPMDEDP-17138](https://jiraeu.epam.com/browse/EPMDEDP-17138), [#320](https://github.com/KubeRocketCI/krci-portal/pull/320))
* Fixed intermittent **Not Found** errors on Overview and namespace-scoped pages. ([EPMDEDP-17137](https://jiraeu.epam.com/browse/EPMDEDP-17137), [#319](https://github.com/KubeRocketCI/krci-portal/pull/319))
* Fixed silent submit when creating an environment without a Clean Pipeline template selected. ([EPMDEDP-17135](https://jiraeu.epam.com/browse/EPMDEDP-17135), [#318](https://github.com/KubeRocketCI/krci-portal/pull/318))
* Fixed deployed version dropdown population on CD promotion stages in multi-pipeline clusters. ([EPMDEDP-17124](https://jiraeu.epam.com/browse/EPMDEDP-17124), [#330](https://github.com/KubeRocketCI/krci-portal/pull/330))
* Fixed Git URL Path validation persisting when switching Git server provider. ([EPMDEDP-17144](https://jiraeu.epam.com/browse/EPMDEDP-17144), [#329](https://github.com/KubeRocketCI/krci-portal/pull/329))
* Fixed GitServer-owned credentials Secrets being treated as externally managed in the portal and garbage collection of credentials Secrets on GitServer deletion. ([EPMDEDP-17146](https://jiraeu.epam.com/browse/EPMDEDP-17146), [#288](https://github.com/epam/edp-codebase-operator/pull/288))
* Fixed nil-pointer render when gateway API values are unset in codebase-operator. ([EPMDEDP-17177](https://jiraeu.epam.com/browse/EPMDEDP-17177), [#294](https://github.com/epam/edp-codebase-operator/pull/294))
* Fixed allowed namespaces not being filtered in Kubernetes mode list views. ([EPMDEDP-17123](https://jiraeu.epam.com/browse/EPMDEDP-17123), [#308](https://github.com/KubeRocketCI/krci-portal/pull/308))
* Fixed silent save failure in resource edit forms and aligned stage quality gate step name validation to a minimum of 2 characters. ([EPMDEDP-17077](https://jiraeu.epam.com/browse/EPMDEDP-17077), [#270](https://github.com/KubeRocketCI/krci-portal/pull/270))
* Fixed release branches being creatable from a commit instead of a branch. ([EPMDEDP-17082](https://jiraeu.epam.com/browse/EPMDEDP-17082), [#275](https://github.com/KubeRocketCI/krci-portal/pull/275))
* Fixed Stage ConfigMap **Add variable** button visibility before permissions resolve. ([EPMDEDP-17091](https://jiraeu.epam.com/browse/EPMDEDP-17091), [#327](https://github.com/KubeRocketCI/krci-portal/pull/327))
* Fixed stale environment labels not being removed from CodebaseImageStream when an application is removed from a CD pipeline. ([EPMDEDP-17108](https://jiraeu.epam.com/browse/EPMDEDP-17108), [#202](https://github.com/epam/edp-cd-pipeline-operator/pull/202))
* Replaced the portal chart `tlsSecret` value with a structured `tlsCertificateRef`. ([EPMDEDP-17133](https://jiraeu.epam.com/browse/EPMDEDP-17133), [#326](https://github.com/KubeRocketCI/krci-portal/pull/326))
* Remediated dependency vulnerabilities in the portal and removed insecure TLS verification skip in GitFusion. ([EPMDEDP-17090](https://jiraeu.epam.com/browse/EPMDEDP-17090), [#281](https://github.com/KubeRocketCI/krci-portal/pull/281)) ([EPMDEDP-17112](https://jiraeu.epam.com/browse/EPMDEDP-17112), [#302](https://github.com/KubeRocketCI/krci-portal/pull/302)) ([EPMDEDP-17134](https://jiraeu.epam.com/browse/EPMDEDP-17134), [#70](https://github.com/KubeRocketCI/gitfusion/pull/70))

### Documentation

* The [landing page](https://kuberocketci.io/) has been updated. ([#376](https://github.com/KubeRocketCI/docs/pull/376))

The [Getting Started](https://docs.kuberocketci.io/docs/about-platform) section is updated with the following:

* The [Install KubeRocketCI](https://docs.kuberocketci.io/docs/quick-start/platform-installation) page has been updated. ([#392](https://github.com/KubeRocketCI/docs/pull/392))
* The [Supported Versions and Compatibility](https://docs.kuberocketci.io/docs/supported-versions) page has been updated for the 3.14 release. ([#392](https://github.com/KubeRocketCI/docs/pull/392))

The [Operator Guide](https://docs.kuberocketci.io/docs/operator-guide) section is updated with the following:

* The [Kubernetes Audit Trails Setup](https://docs.kuberocketci.io/docs/operator-guide/monitoring-and-observability/audit-trails-setup) page has been added. ([#387](https://github.com/KubeRocketCI/docs/pull/387))
* The [KubeRocketCI CLI Keycloak Client](https://docs.kuberocketci.io/docs/operator-guide/auth/krci-cli-client-for-keycloak) page has been updated. ([#385](https://github.com/KubeRocketCI/docs/pull/385))
* The [EKS OIDC With Keycloak](https://docs.kuberocketci.io/docs/operator-guide/auth/configure-keycloak-oidc-eks) page has been updated. ([#386](https://github.com/KubeRocketCI/docs/pull/386))
* The [How to Install KubeRocketCI: Advanced Setup Guide](https://docs.kuberocketci.io/docs/operator-guide/install-kuberocketci) page has been updated. ([#392](https://github.com/KubeRocketCI/docs/pull/392))
* The [Enable Git Resource Discovery](https://docs.kuberocketci.io/docs/operator-guide/extensions/git-discovery) page has been updated. ([#381](https://github.com/KubeRocketCI/docs/pull/381))
* The [Atlantis: Enterprise-Grade Terraform Automation for Kubernetes](https://docs.kuberocketci.io/docs/operator-guide/infrastructure-providers/atlantis-installation) page has been updated. ([#381](https://github.com/KubeRocketCI/docs/pull/381))
* The KrakenD documentation page has been removed from versions 3.13 and higher. ([#371](https://github.com/KubeRocketCI/docs/pull/371))

The [User Guide](https://docs.kuberocketci.io/docs/user-guide) section is updated with the following:

* The [Add Git Server](https://docs.kuberocketci.io/docs/user-guide/add-git-server) page has been updated. ([#382](https://github.com/KubeRocketCI/docs/pull/382)) ([#384](https://github.com/KubeRocketCI/docs/pull/384))
* The [Pipelines Overview](https://docs.kuberocketci.io/docs/user-guide/pipelines) page has been updated. ([#387](https://github.com/KubeRocketCI/docs/pull/387))
* The [KubeRocketCI: Tekton Overview](https://docs.kuberocketci.io/docs/user-guide/tekton-pipelines) page has been updated. ([#387](https://github.com/KubeRocketCI/docs/pull/387))

The [FAQ](https://docs.kuberocketci.io/faq/general-questions) section is updated with the following:

* The [How to Troubleshoot Git Server Connection?](https://docs.kuberocketci.io/faq/how-to/devops/troubleshoot-git-server-connection) page has been updated. ([#382](https://github.com/KubeRocketCI/docs/pull/382))

The [Use Cases](https://docs.kuberocketci.io/docs/use-cases) section is updated with the following:

* The [Deploy Application From a Feature Branch](https://docs.kuberocketci.io/docs/use-cases/deploy-application-from-feature-branch) page has been updated. ([#379](https://github.com/KubeRocketCI/docs/pull/379))
* The [Test Environment Using Environment Variables](https://docs.kuberocketci.io/docs/use-cases/cd-autotests-run-with-env-variables) page has been updated. ([#373](https://github.com/KubeRocketCI/docs/pull/373))
* The [Application With Custom Build Tool/Framework](https://docs.kuberocketci.io/docs/use-cases/tekton-custom-pipelines) page has been updated. ([#372](https://github.com/KubeRocketCI/docs/pull/372))
* The [Create and Use Custom Tekton Pipelines](https://docs.kuberocketci.io/docs/use-cases/custom-pipelines-flow) page has been updated. ([#370](https://github.com/KubeRocketCI/docs/pull/370))

## Version 3.13.5 <a name="3.13.5"></a> (May 22, 2026)

## Upgrades

Tekton cache is updated to the [0.4.4](https://artifacthub.io/packages/helm/epmdedp/tekton-cache/0.4.4) version.

## Version 3.13.4 <a name="3.13.4"></a> (May 21, 2026)

### What's New

KubeRocketCI 3.13.4 expands the KubeRocketCI Portal with a Kubernetes mode, webhook trigger management, richer PipelineRun operations, and more detailed Stage Monitoring. Users can now inspect platform resources from the portal, trigger PipelineRuns through the new endpoint with CLI parameters, and analyze stage health with per-pod historical metrics, network and storage metrics, and native deployment metrics.

Security and quality visibility were also improved. The portal now provides SCA findings APIs backed by Dependency-Track, server-side severity filtering with auto-paging and cancellation, SonarQube branch scope support, and SonarQube pull request proxy support.

The [KubeRocketCI Command Line Interface](https://github.com/KubeRocketCI/cli) is introduced as a new way to monitor and manage platform resources without opening the KubeRocketCI Portal. It helps users perform common operational tasks directly from the terminal.

### New Functionality

* Restored Kubernetes mode in the portal with a resources explorer and create resource action.
* Added REST and OpenAPI support used by the KubeRocketCI CLI, including accurate generated error response schemas for CLI and SDK consumers.
* Extended Stage Monitoring with per-pod historical data, network and storage metrics, and native deployment metrics.
* Added SCA REST endpoints with Dependency-Track integration and server-side severity filtering.

### Enhancements

* Replaced self-signed certificate generation with cert-manager.
* Improved PipelineRun list querying and filtering across platform-registered projects, including payload-aware codebase detection.
* Preserved pod templates and task scheduling configuration when rerunning PipelineRuns.
* Aligned the portal Helm chart fullname override with the `krci-portal` component name.
* Updated codebase-operator Kubernetes dependencies and bumped cd-pipeline-operator Argo CD dependencies.

### Fixed Issues

* Fixed Tekton cache failures caused by oversized `APPLICATIONS_PAYLOAD` results and conflicting files in cache archives.
* Fixed Kubernetes watch reconnect storms caused by stale `resourceVersion` values.
* Fixed branch deletion validation so branches used by deployment flows cannot be deleted.
* Fixed Tekton Results schema handling for `protojson` null values and Kubernetes metadata fields.
* Fixed broken portal links when configuration is not loaded and corrected PipelineRun visibility in stage details.
* Fixed repository URL validation so it runs only when the codebase creation strategy is **Clone**.
* Added a **Stop** action for running PipelineRuns with permission checks; the action is available in the list view and hidden in the PipelineRun details menu.

## Version 3.13.3 <a name="3.13.3"></a> (April 13, 2026)

### New Functionality

* Historical PipelineRuns can now be rerun from the portal. The portal resolves full PipelineRun data for historical records before starting the rerun action.
* The PipelineRun table now includes a sortable **Started at** column and a PipelineRun name search filter.
* Step logs now include copy and download actions, making troubleshooting and sharing logs easier.

### Fixed Issues

* Fixed incorrect change indicators in the CDPipeline resource edit form when application branch values were unchanged.
* Fixed branch diff rendering in the CDPipeline edit flow.
* Fixed the Project details page tour and Pipeline page crashes caused by undefined TriggerTemplate parameters.

## Version 3.13.2 <a name="3.13.2"></a> (April 8, 2026)

### New Functionality

* Added REST API endpoints to the portal and introduced OpenAPI specification generation.

### Enhancements

* Protected the portal `config.get` endpoint and moved cluster configuration under the authenticated application tree.
* Updated the changelog and commit validation workflow to support Jira-prefixed changelog entries and remove the previous pull request title length requirement. (needs careful review)
* Removed the legacy `krci-ai` framework from the portal.

### Fixed Issues

* Fixed Tekton trigger interceptor references by explicitly setting `kind: ClusterInterceptor`.
* Improved responsiveness of generic portal components and pages.
* Made `apiVersion` and `kind` optional for Kubernetes list items to improve compatibility with API responses.
* Fixed stage breadcrumbs and deployment create button permissions. (needs careful review)

## Version 3.13.1 <a name="3.13.1"></a> (April 4, 2026)

### Fixed Issues

* Fixed pagination failure in Tekton Results PipelineRun query and performance bottleneck caused by sequential summary lookups. Added promise coalescing to prevent thundering herd on cache miss. ([EPMDEDP-16623](https://jiraeu.epam.com/browse/EPMDEDP-16623), [#202](https://github.com/KubeRocketCI/krci-portal/pull/202), [#194](https://github.com/KubeRocketCI/krci-portal/pull/194))
* Fixed live log subscription causing page freezing by adding throttling to subscription updates. ([EPMDEDP-16617](https://jiraeu.epam.com/browse/EPMDEDP-16617), [#191](https://github.com/KubeRocketCI/krci-portal/pull/191))
* Fixed misaligned `inputDockerStreams` in CDPipeline edit form. ([EPMDEDP-16633](https://jiraeu.epam.com/browse/EPMDEDP-16633), [#197](https://github.com/KubeRocketCI/krci-portal/pull/197))
* Fixed GitFusion HTTP status codes not being mapped to tRPC error codes; 404 and 401 errors now surface correctly in the portal. ([EPMDEDP-16620](https://jiraeu.epam.com/browse/EPMDEDP-16620), [#193](https://github.com/KubeRocketCI/krci-portal/pull/193))
* Fixed security vulnerability in `go-git` library (5.16.5→5.17.1) in codebase-operator and cd-pipeline-operator. ([EPMDEDP-16599](https://jiraeu.epam.com/browse/EPMDEDP-16599), [#188](https://github.com/epam/edp-cd-pipeline-operator/pull/188))

### Enhancements

* OIDC issuer URL is now exposed in the `config.get` API response, enabling clients to discover the issuer without additional configuration. ([EPMDEDP-16577](https://jiraeu.epam.com/browse/EPMDEDP-16577), [#203](https://github.com/KubeRocketCI/krci-portal/pull/203))
* Aligned security pages layouts across Trivy, SonarQube, and Dependency Track sections. ([EPMDEDP-16616](https://jiraeu.epam.com/browse/EPMDEDP-16616), [#192](https://github.com/KubeRocketCI/krci-portal/pull/192))
* PipelineRun list migrated from records table to Tekton Results table for improved performance and reliability. ([EPMDEDP-16623](https://jiraeu.epam.com/browse/EPMDEDP-16623), [#196](https://github.com/KubeRocketCI/krci-portal/pull/196))
* CDPipeline form handling refactored for improved reliability and state management. ([EPMDEDP-16628](https://jiraeu.epam.com/browse/EPMDEDP-16628), [#200](https://github.com/KubeRocketCI/krci-portal/pull/200))
* GitFusion now disables `regcred` image pull secret usage by default, simplifying deployment in environments without private registries. ([EPMDEDP-16599](https://jiraeu.epam.com/browse/EPMDEDP-16599), [#58](https://github.com/KubeRocketCI/gitfusion/pull/58))

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
