# EDP Releases

## Overview

Get acquainted with the latest EDP releases.

* [Version 3.9.0](#3.9.0)
* [Version 3.8.0](#3.8.0)
* [Version 3.7.5](#3.7.5)
* [Version 3.7.4](#3.7.4)
* [Version 3.7.3](#3.7.3)
* [Version 3.7.2](#3.7.2)
* [Version 3.7.1](#3.7.1)
* [Version 3.7.0](#3.7.0)

<details>
  <summary>Earlier Versions</summary>

* [Version 3.6.0](#3.6.0)
* [Version 3.5.3](#3.5.3)
* [Version 3.5.2](#3.5.2)
* [Version 3.5.1](#3.5.1)
* [Version 3.5.0](#3.5.0)
* [Version 3.4.1](#3.4.1)
* [Version 3.4.0](#3.4.0)
* [Version 3.3.0](#3.3.0)
* [Version 3.2.2](#3.2.2)
* [Version 3.2.1](#3.2.1)
* [Version 3.2.0](#3.2.0)
* [Version 3.1.0](#3.1.0)
* [Version 3.0.0](#3.0.0)
* [Version 2.12.2](#2.12.2)
* [Version 2.12.1](#2.12.1)
* [Version 2.12.0](#2.12.0)
* [Version 2.11.0](#2.11.0)
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

## Version 3.9.0 <a name="3.9.0"></a> (June 13, 2024)

## What's New

We are introducing the built-in pipeline tracker. This new feature allows you to monitor Tekton pipeline runs directly within KubeRocketCI, removing the need to open extra tabs in your browser. With pipeline statuses and details readily accessible, your workflow becomes more streamlined and efficient.

As usual, we are continuously working on refining the user interface. For better user experience, we've made a major contribution to optimizing KubeRocketCI. Changes affect almost all the aspects of using UI, whether it's codebase creation, deployment, third-party tool integration, platform settings, etc. Besides, now it features real-time pipelines within the KubeRocketCI so there is no need to navigate to Tekton to observe pipeline runs, their statuses are always at hand now.

Apart from that, we implemented GitHub Container Registry (GHCR) support. This allows you to seamlessly store your container images using GitHub's solution, offering more options for managing your container images.

Lastly, announce the Kiosk tool deprecation. For those who are looking for a tenancy management solution instead of an obsolete Kiosk, we encourage considering Capsule as a more flexible solution.

### New Functionality

* The developer role in KubeRocketCI now grants the ability to view the Kubernetes resources within the KubeRocketCIб such as codebases, environments, widgets, etc. ([#191](https://github.com/epam/edp-install/issues/191))
* The Keycloak realm name value is now configurable. Now users can redefine the realm name, patterns no longer include '-main', and the namespace is used as the default realm name. ([#183](https://github.com/epam/edp-install/issues/183))
* The `sso.ingress.enabled` parameter has been added to manipulate ingress creation for OAuth2-Proxy. ([#178](https://github.com/epam/edp-install/issues/178))
* GitHub Container Registry support has been implemented. Now users have one another option for storing container images. ([#238](https://github.com/epam/edp-headlamp/issues/238))
* Added the ability for KeycloakRealmRole custom resource to configure client roles alongside roles as composite roles. ([#44](https://github.com/epam/edp-keycloak-operator/issues/44))
* The ability to configure Keycloak realm token settings has been added to the platform. ([#38](https://github.com/epam/edp-keycloak-operator/issues/38))
* Added custom SSL/TLS certificate support for the Keycloak HTTPS connection. ([#36](https://github.com/epam/edp-keycloak-operator/issues/36))
* The KeycloakClient custom resource now supports creating authorization policies and permissions for a client. ([#28](https://github.com/epam/edp-keycloak-operator/issues/28))
* The GerritMergeRequest custom resource is now capable of removing the files in the repository. ([#30](https://github.com/epam/edp-gerrit-operator/issues/30))

### Breaking Change

* The Kiosk tenancy management tool has been deprecated. As an alternative, consider using [Capsule](https://epam.github.io/edp-install/operator-guide/capsule/). ([#55](https://github.com/epam/edp-cd-pipeline-operator/issues/55))

### Enhancements

* The SSO tab of the Configuration section has been improved. To simplify integration, now it contains a table of related ingresses. ([#173](https://github.com/epam/edp-headlamp/issues/173)) 
* The Configuration section structure has been updated for better navigation. ([#192](https://github.com/epam/edp-headlamp/issues/192)) ([#194](https://github.com/epam/edp-headlamp/issues/194)) ([#188](https://github.com/epam/edp-headlamp/issues/188))
* The codebase creation flow has been redesigned for simplicity. ([#177](https://github.com/epam/edp-headlamp/issues/177))
* The procedure of deploying KubeRocketCI wothout ingress has been streamlined. ([#185](https://github.com/epam/edp-install/issues/185))
* The KeycloakRealm custom resource has been updated to use separate CRs, KeycloakRealm and KeycloakRealmIdentityProvider, for a more flexible setup, and the deprecated SSO configuration has been removed. ([#47](https://github.com/epam/edp-keycloak-operator/issues/47))
* The edp-keycloak-operator has been enhanced to support the reconciliation of user attributes, including FirstName, SecondName, and Email, with optional support for the Email-Verified field, ensuring consistency with the source of truth.  ([#45](https://github.com/epam/edp-keycloak-operator/issues/45))
* The KeycloakClient custom resource has been provided with authorization settings to configure client scopes. ([#41](https://github.com/epam/edp-keycloak-operator/issues/41))
* The KeycloakRealmComponent can now reference to secrets. Now users can create Keycloak realms that store their secret strings in a user-defined secret. ([#30](https://github.com/epam/edp-keycloak-operator/issues/30))
* The KubeRocketCI UI has been significantly revamped. It concerns all the tabs and section, including notifications and UI settings. ([#258](https://github.com/epam/edp-headlamp/issues/258)) 
* The Components section design has been refined. ([#252](https://github.com/epam/edp-headlamp/issues/252)) ([#253](https://github.com/epam/edp-headlamp/issues/253))
* The **Actions** button Add permissions check for actions ([#249](https://github.com/epam/edp-headlamp/issues/249)) ([#250](https://github.com/epam/edp-headlamp/issues/250))
* Tekton pipelines are now displayed in real-time mode within the KubeRocketCI UI directly. To see the these real-time pipeline details, simply click on any pipeline name. ([#225](https://github.com/epam/edp-headlamp/issues/225))
* The ability to add custom webhooks via the `Webhook Url` field has been added to Git Servers. ([#242](https://github.com/epam/edp-headlamp/issues/242))
* Tekton resources no longer appear as unsynchronized in Argo CD when KubeRocketCI has been deployed using the add-ons approach. ([#169](https://github.com/epam/edp-headlamp/issues/169))
* The Configuration section structure has been updated for better navigation. ([#192](https://github.com/epam/edp-headlamp/issues/192)) ([#194](https://github.com/epam/edp-headlamp/issues/194)) ([#188](https://github.com/epam/edp-headlamp/issues/188))
* GitOps approach can now be enabled for multiple applications separately within one stage. ([#180](https://github.com/epam/edp-headlamp/issues/180))
* The Markeplace page has been updated. ([#178](https://github.com/epam/edp-headlamp/issues/178))
* The URL field validation logic has been updated in the third-party tool integration forms of the Configuration section. ([#175](https://github.com/epam/edp-headlamp/issues/175))
* The Overview page has been redesigned. Now it additionally displays widget status at the left border of each widget. ([#172](https://github.com/epam/edp-headlamp/issues/172))
* The ability to delete multiple codebases at a time has been added to the Components section. ([#171](https://github.com/epam/edp-headlamp/issues/171))
* The Environment details page has been significantly redesigned. Now it contains much more information, such as creation time, application image, promotion option. Besides, they contain quick links to Argo CD, Grafana, Kibana, application logs, etc.  ([#258](https://github.com/epam/edp-headlamp/issues/258))
* Commit message validation is now applied to all of the pipeline runs from GitHub. Previously, commits were validated solely for pull requests. ([#193](https://github.com/epam/edp-tekton/issues/193))
* Quality Gates are implemented in the review process that check for alignment between the Helm chart name and the codebase name. ([#191](https://github.com/epam/edp-tekton/issues/191))
* In order to better manage and control distributed development, we facilitated the flexibility of using different package registries in our Gradle Tekton pipelines. User can now leverage different package registries, such as Nexus, GitLab, GitHub, and Azure DevOps Registries. ([#132](https://github.com/epam/edp-tekton/issues/132))              
* Pipeline steps and task sequences were optimised by removing the `get-nexus-repository-url` step and adjusting task sequences for application and library codebase types for enhanced efficiency and streamlined processes. ([#132](https://github.com/epam/edp-tekton/issues/132)) ([#177](https://github.com/epam/edp-tekton/issues/177))
* For better security, the permissions scope has been shrinked for the edp-cd-pipeline-operator. ([#52](https://github.com/epam/edp-cd-pipeline-operator/issues/52))
* The SonarQube integration for new codebases now correctly uses 'master' as the default branch name, ensuring alignment between the codebase and SonarQube project configurations. ([#207](https://github.com/epam/edp-tekton/issues/207))
* Redundant websockets for streaming pipelineruns were removed, leaving the only one with filtering by labels. ([#170](https://github.com/epam/edp-headlamp/issues/170))
* The `helm-validate` step now uses Go cache for better performance. ([#44](https://github.com/epam/edp-cd-pipeline-operator/issues/44))
* To reflect the latest best practices and features, we updated the chart templates within the edp-codebase-operator. ([#68](https://github.com/epam/edp-codebase-operator/issues/68))
* Validation message for creating a new branch with an invalid name in a codebase have been updated. ([#240](https://github.com/epam/edp-headlamp/issues/240))

### Fixed Issues

* Fixed issue when users couldn't see the error messages in some pages. ([#183](https://github.com/epam/edp-headlamp/issues/183))
* Fixed issue when users could create codebases with corrupted Git Servers. ([#179](https://github.com/epam/edp-headlamp/issues/179))
* Fixed issue with various pop-ups throughout the entire Configuration section. ([#223](https://github.com/epam/edp-headlamp/issues/223))
* Fixed issue when users had to double click the **Sign In** button to log into the KubeRocketCI user interface. ([#218](https://github.com/epam/edp-headlamp/issues/218))
* Fixed issue when users couldn't see all of the application images in the stage details page if the promote option was disabled. ([#174](https://github.com/epam/edp-headlamp/issues/174))
* Fixed issue when the **Go to source code** button in the stage details page led to the wrong endpoint. ([#198](https://github.com/epam/edp-headlamp/issues/198))
* Fixed wrong namespace validation in the stage creation window. ([#155](https://github.com/epam/edp-headlamp/issues/155))
* Fixed issue when the build pipelines interfered with each other. ([#71](https://github.com/epam/edp-codebase-operator/issues/71))
* Fixed issue when the Tekton resource pruner didn't prune unused resources completely. ([#205](https://github.com/epam/edp-tekton/issues/205))
* The deploy button has been fixed: it now correctly appears inactive in situations where its functionality is actually unavailable. ([#155](https://github.com/epam/edp-headlamp/issues/155))
* Fixed issue when the deploy pipeline failed if the `ingress.enabled` parameter was set to `true`. ([#80](https://github.com/epam/edp-codebase-operator/issues/80))
* The version conflict in the Added ability to KeycloakRealmRole CR configure client roles alongside roles as composite roles. FastAPI codebase build pipeline related to h11 and httpcore has been resolved by downgrading h11 to a version compatible with httpcore. ([#195](https://github.com/epam/edp-tekton/issues/195))
* The `init-autotest` stage failure in the **deploy-with-autotests** pipeline template has been resolved, allowing the application to deploy successfully and initialize autotests. ([#199](https://github.com/epam/edp-tekton/issues/199))
* The CreateContainerConfigError error in the `helm-push` step has been fixed for both Helm application and library codebase types. ([#184](https://github.com/epam/edp-tekton/issues/184))
* Rudimentary workspaces and volumes have been removed from the `get-version`, `jira`, `commit-validate`, `getDefaultVersion`, and other related tasks. ([#78](https://github.com/epam/edp-tekton/issues/78))
* Fixed issue when the edp-tekton component couldn't get deployed in the OpenShift cluster due to a misconfigured route resource.  ([#151](https://github.com/epam/edp-tekton/issues/151))
* The build and review pipelines for a Next.js-based project now successfully complete the build step. ([#201](https://github.com/epam/edp-tekton/issues/201))
* Fixed issue when users couldn't create multiple event listeners when deploying KubeRocketCI into the OpenShift cluster. ([#175](https://github.com/epam/edp-tekton/issues/175))

### Documentation

* The [Run KubeRocketCI on Kind Cluster](https://youtu.be/b9dpNSpq1AI?si=1KRIlaTKs8k71Cjg) guide is published in our YouTube channel.
* The [Extending KubeRocketCI: Integrating Custom Build Tools, Frameworks, Languages, and Pipelines](https://youtu.be/HTEUI0Ynygc?si=CvMw_ivvWCiyNXW9) guide is published in our YouTube channel.

The [Getting Started](https://epam.github.io/edp-install/overview/) section is updated with the following:
  * The [Supported Versions and Compatibility](https://epam.github.io/edp-install/supported-versions/) page has been updated. ([#187](https://github.com/epam/edp-install/issues/187))
  * The [Install EDP](https://epam.github.io/edp-install/quick-start/platform-installation/) page has been updated. ([#184](https://github.com/epam/edp-install/issues/184))

The [Operator Guide](https://epam.github.io/edp-install/operator-guide/) is updated with the following:
  * A new subsection called [Troubleshooting](https://epam.github.io/edp-install/operator-guide/troubleshooting/overview/) has been added. It aims to help users with the frequently asked questions: ([#177](https://github.com/epam/edp-install/issues/177))
    * The [Container Registry Reset](https://epam.github.io/edp-install/operator-guide/troubleshooting/troubleshoot-container-registries/) page has been added.
    * The [Application Is Not Deployed](https://epam.github.io/edp-install/operator-guide/troubleshooting/troubleshoot-stages/) page has been added. ([#177](https://github.com/epam/edp-install/issues/177))
    * The [Codebase Creation Issue](https://epam.github.io/edp-install/operator-guide/troubleshooting/troubleshoot-git-server/) page has been added. ([#177](https://github.com/epam/edp-install/issues/177))
    * The [Resource Observability Issue](https://epam.github.io/edp-install/operator-guide/troubleshooting/resource-observability/) page has been added. ([#177](https://github.com/epam/edp-install/issues/177))
    * The [Application Already Exists Error (Gerrit VCS)](https://epam.github.io/edp-install/operator-guide/troubleshooting/troubleshoot-applications/) page has been added. ([#177](https://github.com/epam/edp-install/issues/177))
    * The [Codebase Build Process is Failed](https://epam.github.io/edp-install/operator-guide/troubleshooting/application-not-built/) page has been added. ([#177](https://github.com/epam/edp-install/issues/177))
    * The [Invalid Codebase ID Issue (GitHub/GitLab VCS)](https://epam.github.io/edp-install/operator-guide/troubleshooting/invalid-codebase-name/) page has been added. ([#177](https://github.com/epam/edp-install/issues/177))
  * The [Upgrade KubeRocketCI v3.7 to 3.8](https://epam.github.io/edp-install/operator-guide/upgrade-edp-3.8/) page has been added. ([#199](https://github.com/epam/edp-install/issues/199))
  * The [SonarQube Project Visibility](https://epam.github.io/edp-install/operator-guide/sonarqube-visibility/) page has been added. ([#180](https://github.com/epam/edp-install/issues/180))
  * The [Package Registry](https://epam.github.io/edp-install/operator-guide/package-registry/) page has been added. ([#182](https://github.com/epam/edp-install/issues/182))
  * The [Install Velero](https://epam.github.io/edp-install/operator-guide/install-velero) page has been updated. ([#235](https://github.com/epam/edp-install/issues/235))
  * The [Monitoring](https://epam.github.io/edp-install/operator-guide/tekton-monitoring/) page has been updated. ([#212](https://github.com/epam/edp-install/issues/212))
  * The [Customize Deployment](https://epam.github.io/edp-install/operator-guide/customize_deployment/) page has been updated. ([#197](https://github.com/epam/edp-install/issues/197))
  * The [KubeRocketCI Access Model](https://epam.github.io/edp-install/operator-guide/edp-access-model/) page has been updated. ([#175](https://github.com/epam/edp-install/issues/175))
  * The [Argo CD Integration](https://epam.github.io/edp-install/operator-guide/argocd-integration/) page has been updated. ([#176](https://github.com/epam/edp-install/issues/176))
  * The [Deploy AWS EKS Cluster](https://epam.github.io/edp-install/operator-guide/deploy-aws-eks/) page has been updated. ([#174](https://github.com/epam/edp-install/issues/174))
  * The [Install EDP](https://epam.github.io/edp-install/operator-guide/install-edp/) page has been updated. ([#181](https://github.com/epam/edp-install/issues/181))

The [Developer Guide](https://epam.github.io/edp-install/developer-guide/) is updated with the following:
  * The [KubeRocketCI Project Rules. Working Process](https://epam.github.io/edp-install/developer-guide/edp-workflow/) page has been updated. ([#206](https://github.com/epam/edp-install/issues/206))
  * The [AWS Infrastructure Cost Estimation](https://epam.github.io/edp-install/developer-guide/aws-infrastructure-cost-estimation/) page has been added. ([#202](https://github.com/epam/edp-install/issues/202))
  * The [EDP Reference Architecture on AWS](https://epam.github.io/edp-install/developer-guide/aws-reference-architecture/) page has been updated. ([#177](https://github.com/epam/edp-install/issues/177))

The [User Guide](https://epam.github.io/edp-install/user-guide/) has been updated with the following.
  * The [Components Overview](https://epam.github.io/edp-install/user-guide/components/) page has been added. ([#204](https://github.com/epam/edp-install/issues/204))
  * The [Manage GitOps](https://epam.github.io/edp-install/user-guide/gitops/) page has been added. ([#204](https://github.com/epam/edp-install/issues/204))
  * The [Overview](https://epam.github.io/edp-install/user-guide/) page has been updated. ([#204](https://github.com/epam/edp-install/issues/204))
  * The [Add Application](https://epam.github.io/edp-install/user-guide/add-application/) page has been updated. ([#204](https://github.com/epam/edp-install/issues/204))
  * The [Manage Applications](https://epam.github.io/edp-install/user-guide/application/) page has been updated. ([#204](https://github.com/epam/edp-install/issues/204))
  * The [Add Autotest](https://epam.github.io/edp-install/user-guide/add-autotest/) page has been updated. ([#204](https://github.com/epam/edp-install/issues/204))
  * The [Manage Autotests](https://epam.github.io/edp-install/user-guide/autotest/) page has been updated. ([#204](https://github.com/epam/edp-install/issues/204))
  * The [Add Library](https://epam.github.io/edp-install/user-guide/add-library/) page has been updated. ([#204](https://github.com/epam/edp-install/issues/204))
  * The [Manage Libaries](https://epam.github.io/edp-install/user-guide/library/) page has been updated. ([#204](https://github.com/epam/edp-install/issues/204))
  * The [Add Infrastructure](https://epam.github.io/edp-install/user-guide/add-infrastructure/) page has been updated. ([#204](https://github.com/epam/edp-install/issues/204))
  * The [Manage Infrastructures](https://epam.github.io/edp-install/user-guide/infrastructure/) page has been updated. ([#204](https://github.com/epam/edp-install/issues/204))
  * The [Manage Branches](https://epam.github.io/edp-install/user-guide/manage-branches/) page has been updated. ([#204](https://github.com/epam/edp-install/issues/204))
  * The [Add Environment](https://epam.github.io/edp-install/user-guide/add-cd-pipeline/) page has been updated. ([#204](https://github.com/epam/edp-install/issues/204))
  * The [Manage Environments](https://epam.github.io/edp-install/user-guide/manage-environments/) page has been updated. ([#204](https://github.com/epam/edp-install/issues/204))
  * The [Add Quality Gate](https://epam.github.io/edp-install/user-guide/add-quality-gate/) page has been updated. ([#204](https://github.com/epam/edp-install/issues/204))
  * The [Manage Git Servers](https://epam.github.io/edp-install/user-guide/git-server-overview/) page has been updated. ([#204](https://github.com/epam/edp-install/issues/204))
  * The [Manage Container Registries](https://epam.github.io/edp-install/user-guide/manage-container-registries/) page has been updated. ([#223](https://github.com/epam/edp-install/issues/223))

### Upgrades

* The Go language is updated to the [1.22](https://tip.golang.org/doc/go1.22) version. ([#57](https://github.com/epam/edp-keycloak-operator/issues/57))
* EDP Keycloak operator [v1.21.0](https://github.com/epam/edp-keycloak-operator/blob/master/CHANGELOG.md) has been published on [OperatorHub](https://operatorhub.io/operator/edp-keycloak-operator). ([#37](https://github.com/epam/edp-keycloak-operator/issues/37))
* The alpine packages were updated to the [3.18.6](https://git.alpinelinux.org/aports/log/?h=v3.18.6) version. ([#49](https://github.com/epam/edp-gerrit-operator/issues/49))
* The Tekton dashboard  is updated to the [v0.46.0](https://github.com/tektoncd/dashboard/releases/tag/v0.46.0) version. ([#180](https://github.com/epam/edp-tekton/issues/180))

## Version 3.8.0 <a name="3.8.0"></a> (March 12, 2024)

## What's new

To start from, we're undergoing a significant platform transformation as we reveal our new identity: KubeRocketCI. This rebranding endeavor underscores our dedication to innovation and marks the start of a fresh chapter in our journey.

This release introduces crucial improvements to our Tekton deploy pipelines. With this update, users have more freedom to customize Tekton pipelines to fit their needs, making deployment smoother. Whether users are setting up environments beforehand or conducting tests afterward, our new approach makes it easier.

For better convenience, we're introducing multiple Git Servers support. This major feature will be especially appreciated by those who spread their code over multiple version control systems.

In this release, we've broadened our support for repository platforms, including Azure Artifacts, Artifactory, GitLab Repository, and GitHub Registry. Additionally, we've enhanced Nexus to serve as a unified repository capable of managing application artifacts and container images. This improvement simplifies asset management by aggregating all development assets into one centralized location.

We've improved our KubeRocketCI portal user interface. The latest update introduces an enhanced stages menu, boasting a clearer and more comprehensive design. Additionally, we've revamped the Quick Links menu in the Configuration tab and streamlined the sections panel for easier navigation, all aimed at enhancing user experience.

To showcase the platform's accessibility, we've published EDP on the Civo marketplace. It means that users interested in deploying a Civo cluster, can also onboard EDP in just a few clicks. Watch our [video tutorial](https://youtu.be/QjZoPnIKDtA?si=FBNkrKtfZOn28rYV) to see how elementary it is.

### New Functionality

* The deploy pipeline logic has been reworked. With this change, users can personally set pre-deploy and post-deploy tasks for the deploy pipelines, which makes them highly flexible. ([#142](https://github.com/epam/edp-headlamp/issues/142))
* The Argo CD integration section is added to the Configuration tab. With this section, users will be able to integrate Argo CD directly in the KubeRocketCI portal. ([#135](https://github.com/epam/edp-headlamp/issues/135))
* The pipeline stage menu has been redesigned. Now it has three tabs: Applications, Pipelines, and Monitoring. ([#152](https://github.com/epam/edp-headlamp/issues/152))
* The Quick Link creation flow is managed now via KubeRocketCI portal. ([#157](https://github.com/epam/edp-headlamp/issues/157))
* Now users can define node selectors and tolerations for the oauth2-proxy Helm chart to deploy on specific nodes. ([#164](https://github.com/epam/edp-install/issues/164))
* The new naming convention for the ingress objects has been defined. ([#159](https://github.com/epam/edp-install/issues/159))
* The secret support has been enabled in the `KeycloakRealmComponent` resource. ([#30](https://github.com/epam/edp-keycloak-operator/issues/30))
* The stage creation menu now allows to choose a pipeline template. Although there is an option to create custom pipeline templates, we also provide two predefined templates: one with autotests and one without. ([#165](https://github.com/epam/edp-headlamp/issues/165))
* The ability to set Nexus as a container registry tool has been added to the KubeRocketCI portal. It's located in the Registry section of the Configuration tab. ([#160](https://github.com/epam/edp-headlamp/issues/160))
* The `Skip Webhook SSL Verification` option has been added to the Git Server creation form.  ([#137](https://github.com/epam/edp-headlamp/issues/137))
* Custom package registry integration support (Nexus, GitLab, GitHub, Azure DevOps) has been added to the following languages:
  * .Net ([#127](https://github.com/epam/edp-tekton/issues/127))
  * Python ([#123](https://github.com/epam/edp-tekton/issues/123))
  * NPM ([#115](https://github.com/epam/edp-tekton/issues/115))
  * Antora ([#115](https://github.com/epam/edp-tekton/issues/115))
* Now users can manage the cleanup policies in Nexus using the `NexusCleanupPolicy` custom resource. ([#25](https://github.com/epam/edp-nexus-operator/issues/25))
* The EDP Nexus Operator now manages Nexus scripts. ([#21](https://github.com/epam/edp-nexus-operator/issues/21))
* A new custom resource named NexusBlobStore has been introduced for configuring blob stores. ([#20](https://github.com/epam/edp-nexus-operator/issues/20))

### Enhancements

* The environment details page has been redesigned. Now it displays stages as separate rectangle blocks. ([#142](https://github.com/epam/edp-headlamp/issues/142))
* To simplify the structure of the values.yaml file, the `sso` and `oauth2_proxy` sections were merged in a single one. ([#145](https://github.com/epam/edp-install/issues/145))
* To make Argo CD credentials validation easier, the `app.edp.epam.com/integration-secret-connected` annotation was added to the `ci-argocd` secret with its connection status. ([#39](https://github.com/epam/edp-codebase-operator/issues/39))
* The `Cluster Token` field has been added to the cluster secret creation process in the KubeRocketCI portal. ([#161](https://github.com/epam/edp-headlamp/issues/161))
* A random set of symbols is now appended to the GitOps repository name when the repository is created in the GitOps Configuration tab. ([#163](https://github.com/epam/edp-headlamp/issues/163))
* As a result of a redesign of the deployment logic, the `Update` button has been removed from the pipeline stage menu as it is now considered redundant. To redeploy the application, utilize the `Deploy` button instead. ([#162](https://github.com/epam/edp-headlamp/issues/162))
* The `JiraServer` custom resource will no longer be created during installation of the edp-install Helm chart if the Jira integration is disabled. ([#150](https://github.com/epam/edp-headlamp/issues/150))
* Tooltips and links throughout the entire KubeRocketCI portal have been updated. ([#148](https://github.com/epam/edp-headlamp/issues/148))
* Secret integration forms for most of the Configuration tab have been updated. ([#138](https://github.com/epam/edp-headlamp/issues/138))
* Now, KubeRocketCI users have the capability to define both node selectors and tolerations for the edp-tekton Helm chart. ([#126](https://github.com/epam/edp-tekton/issues/126))
* Maven builds now support setting custom package registry integration from the settings.xml file, such as Nexus, GitLab, GitHub, Azure DevOps. ([#106](https://github.com/epam/edp-tekton/issues/106))
* The SonarQube interaction has been refactor. Now the sonar-operator supports branching. This ehnacement allows for creating new branches in SonarQube projects instead of creating new projects for each new build. ([107](https://github.com/epam/edp-tekton/issues/107))
* The `sso` section of the values.yaml file has been updated. From now on, users are allowed to redefine the realm name using the `realmName` parameter. ([#149](https://github.com/epam/edp-install/issues/149))
* The `el-gitlab-listener` and `el-github-listener` resources were renamed to `event-listener`. ([#36](https://github.com/epam/edp-codebase-operator/issues/36))

### Fixed Issues

* Fixed incorrect ingress annotation in the Tekton dashboard. ([#143](https://github.com/epam/edp-install/issues/143))
* Fixed issue when the codebase creation failed if the `Empty project` field was enabled. ([#43](https://github.com/epam/edp-codebase-operator/issues/43))
* Fixed issue when the tree diagram window displayed not the whole set of stages. ([#146](https://github.com/epam/edp-headlamp/issues/146))
* Fixed issue with improper Component filter work when user gets an empty page if it is located not on the first page of the Components list. ([#144](https://github.com/epam/edp-headlamp/issues/144))
* Fixed issue where a pipeline run would fail if the application was deployed in two separate pipelines with identical stage name. ([#137](https://github.com/epam/edp-tekton/issues/137))

### Documentation

* The EPAM Delivery Platform has been included to the [Capsule adopters](https://github.com/projectcapsule/capsule/blob/main/ADOPTERS.md) list. ([#18](https://github.com/epam/edp-cd-pipeline-operator/issues/18))
* Both the Previous and Next navigation buttons have been added to mkdocs. ([#119](https://github.com/epam/edp-install/issues/119))
* The [Effortless CI/CD Mastery: Installing EPAM Delivery Platform with Civo Marketplace](https://medium.com/epam-delivery-platform/effortless-ci-cd-mastery-installing-epam-delivery-platform-with-civo-marketplace-01bbc63af7fb) article has been published to the Medium blog.
* The [Quick Start - Part 1](https://www.youtube.com/watch?v=ILlY4niCWeU) video tutorial has been published on our YouTube channel. ([#140](https://github.com/epam/edp-install/issues/140))
* The [Use Cases](https://epam.github.io/edp-install/use-cases/) section has been refactored. ([#119](https://github.com/epam/edp-install/issues/119))
* The [RoadMap](https://epam.github.io/edp-install/roadmap/) section has been updated. (([#119](https://github.com/epam/edp-install/issues/119)))
* The [Main Page](https://epam.github.io/edp-install/) of our documentation has been updated. (([#119](https://github.com/epam/edp-install/issues/119)))
* Documentation pages have been provided with widgets that display the page contibutors, the creation date, and the update dates. (([#119](https://github.com/epam/edp-install/issues/119)))
* The README.md file has been updated for the following repositories:
  * [Keycloak Operator](https://github.com/epam/edp-keycloak-operator/blob/master/README.md) ([#132](https://github.com/epam/edp-keycloak-operator/issues/132))
  * [EDP Portal](https://github.com/epam/edp-headlamp/blob/master/README.md) ([#132](https://github.com/epam/edp-headlamp/issues/132))
  * [EDP Tekton](https://github.com/epam/edp-tekton/blob/master/README.md) ([#132](https://github.com/epam/edp-tekton/issues/132))
  * [CD Pipeline Operator](https://github.com/epam/edp-cd-pipeline-operator/blob/master/README.md) ([#132](https://github.com/epam/edp-cd-pipeline-operator/issues/132))
  * [Nexus Operator](https://github.com/epam/edp-nexus-operator/blob/master/README.md) ([#132](https://github.com/epam/edp-nexus-operator/issues/132))
  * [Sonar Operator](https://github.com/epam/edp-sonar-operator/blob/master/README.md) ([#3](https://github.com/epam/edp-sonar-operator/issues/3))

The [Getting Started](https://epam.github.io/edp-install/overview/) section is updated with the following:
  * The [Compliance](https://epam.github.io/edp-install/compliance/) page has been added. ([#119](https://github.com/epam/edp-install/issues/119))
  * The [Supported Versions and Compatibility](https://epam.github.io/edp-install/supported-versions/) page has been updated. ([#119](https://github.com/epam/edp-install/issues/119))
  * The [Install EDP](https://epam.github.io/edp-install/quick-start/platform-installation/) has been added. ([#119](https://github.com/epam/edp-install/issues/119))
  * The [Integrate SonarQube](https://epam.github.io/edp-install/quick-start/integrate-sonarcloud/) page has been added. ([#119](https://github.com/epam/edp-install/issues/119))
  * The [Integrate GitHub](https://epam.github.io/edp-install/quick-start/integrate-github/) page has been added. ([#119](https://github.com/epam/edp-install/issues/119))
  * The [Integrate DockerHub](https://epam.github.io/edp-install/quick-start/integrate-container-registry/) page has been added. ([#119](https://github.com/epam/edp-install/issues/119))
  * The [Create Application](https://epam.github.io/edp-install/quick-start/create-application/) page has been added. ([#119](https://github.com/epam/edp-install/issues/119))
  * The [Integrate Argo CD](https://epam.github.io/edp-install/quick-start/integrate-argocd/) page has been added. ([#119](https://github.com/epam/edp-install/issues/119))
  * The [Deploy Application](https://epam.github.io/edp-install/quick-start/deploy-application/) page has been added. ([#119](https://github.com/epam/edp-install/issues/119))

The [Operator Guide](https://epam.github.io/edp-install/operator-guide/) is updated with the following:
  * The [Install via Civo](https://epam.github.io/edp-install/operator-guide/install-via-civo/) page has been added ([#152](https://github.com/epam/edp-install/issues/152))
  * The [Protect Endpoints](https://epam.github.io/edp-install/operator-guide/oauth2-proxy/) page has been updated. ([#145](https://github.com/epam/edp-install/issues/145))
  * The EDP installation flow has been re-organized for better naviagation. ([#163](https://github.com/epam/edp-install/issues/163))
  * The [Change Container Registry](https://epam.github.io/edp-install/operator-guide/container-registries/) page has been added. ([#136](https://github.com/epam/edp-install/issues/136))
  * The [Multi-Tenancy Overview](https://epam.github.io/edp-install/operator-guide/overview-multi-tenancy/) page has been added. ([#124](https://github.com/epam/edp-install/issues/124))
  * The [Integrate Capsule](https://epam.github.io/edp-install/operator-guide/capsule/) ([#124](https://github.com/epam/edp-install/issues/124))
  * The [Upgrade EDP v3.6 to 3.7](https://epam.github.io/edp-install/operator-guide/upgrade-edp-3.7/) page has been added. ([#127](https://github.com/epam/edp-install/issues/127))
  * The [Argo CD Integration](https://epam.github.io/edp-install/operator-guide/argocd-integration/) page has been added. ([#125](https://github.com/epam/edp-install/issues/125))
  * The [EKS OIDC With Keycloak](https://epam.github.io/edp-install/operator-guide/configure-keycloak-oidc-eks/) page has been updated.
  * The [External Secrets Operator Integration](([#26](https://github.com/epam/edp-install/issues/26))) page has been updated.

The [Developer Guide](https://epam.github.io/edp-install/developer-guide/) is updated with the following:
  * The [Overview](https://epam.github.io/edp-install/developer-guide/) page has been updated.
  * Create the [Quality Control](https://epam.github.io/edp-install/developer-guide/autotest-coverage/) page has been added. ([#161](https://github.com/epam/edp-install/issues/161))
  * The [Microsoft Teams Notification](https://epam.github.io/edp-install/operator-guide/notification-msteams/) page has been updated. ([#156](https://github.com/epam/edp-install/issues/156))
  * The [Annotations and labels](https://epam.github.io/edp-install/developer-guide/annotations-and-labels/) page has been added. ([#146](https://github.com/epam/edp-install/issues/146))

The [User Guide](https://epam.github.io/edp-install/user-guide/) has been updated.

### Upgrades

* KubeRocketCI is now based on Headlamp [0.22.0](https://github.com/headlamp-k8s/headlamp/releases/tag/v0.22.0) version. ([#139](https://github.com/epam/edp-headlamp/issues/139))
* The mkdocs engine has been updated to the [9.5.7](https://squidfunk.github.io/mkdocs-material/changelog/) version. ([#146](https://github.com/epam/edp-install/issues/146))
* The alpine image is updated to the [3.18.6](https://hub.docker.com/layers/library/alpine/3.18.6/images/sha256-695ae78b4957fef4e53adc51febd07f5401eb36fcd80fff3e5107a2b4aa42ace?context=explore) version. ([#35](https://github.com/epam/edp-codebase-operator/issues/35))
* The [EDP Keycloak Operator](https://operatorhub.io/operator/edp-keycloak-operator) is updated to the [1.20.0](https://github.com/epam/edp-keycloak-operator/releases/tag/v1.20.0) version. ([#27](https://github.com/epam/edp-keycloak-operator/issues/27))
* The [EDP Nexus Operator](https://operatorhub.io/operator/nexus-operator) is updated to the [3.2.0](https://github.com/epam/edp-nexus-operator/releases/tag/v3.2.0) version. ([#26](https://github.com/epam/edp-nexus-operator/issues/26))

## Version 3.7.5 <a name="3.7.5"></a> (January 19, 2024)

### Enhancements

* SSH private key format has been aligned for CI pipelines.

## Version 3.7.4 <a name="3.7.4"></a> (January 18, 2024)

### Enhancements

* Users can now modify the Version Control System integration secret in the EDP Portal, even if the Git Server has already been onboarded.

## Version 3.7.3 <a name="3.7.3"></a> (January 3, 2024)

### New Functionality

* In the SonarQube widget, quality gates have been provided with the Failed and Passed statuses of quality gate runs.

### Enhancements

* The system codebase icon has been updated. ([#127](https://github.com/epam/edp-headlamp/issues/127))
* No secrets found messages have been added to the Configuration page list. ([#118](https://github.com/epam/edp-headlamp/issues/118))

### Fixed Issues

* Fixed incorrect SonarQube widget loading status when there was no SonarQube component integrated. ([#118](https://github.com/epam/edp-headlamp/issues/118))

## Version 3.7.2 <a name="3.7.2"></a> (December 18, 2023)

### New Functionality

* Integration status has been provided for registry integration. Additionally, integration status can now display the connection error status. ([#29](https://github.com/epam/edp-codebase-operator/issues/29))

### Fixed Issues

* Fixed wrong Tekton cache service endpoint. ([#89](https://github.com/epam/edp-tekton/issues/89))
* Fixed improper telemetry work. ([#31](https://github.com/epam/edp-codebase-operator/issues/31))

## Version 3.7.1 <a name="3.7.1"></a> (December 18, 2023)

### Fixed Issues

* Fixed issue that prevented environment stage deletion if external cluster configuration was incorrect. ([#10](https://github.com/epam/edp-cd-pipeline-operator/issues/10))

## Version 3.7.0 <a name="3.7.0"></a> (December 15, 2023)

## What's new

In this release, we introduce DependencyTrack and SonarQube widgets on the application details page, enhancing observability. The SonarQube widget provides essential information on bugs, vulnerabilities,  code smells, coverage, and duplications. Whereas DependencyTrack widget highlights potential security risks with severity levels.
With these widgets, it is getting much easier to monitor application's quality.

We've invested considerable effort in major user interface enhancements to make your navigation seamless. In the Configuration section, tabs are now intelligently categorized for an intuitive experience, each equipped with status indicators for easy integration tracking.
We have elevated the overall user experience to a more sophisticated standard, underscoring our commitment to providing a refined and intuitive interface. Additionally, we've included comprehensive descriptions for all sections, simplifying your navigation journey.

Originally developed to meet the specific requirements of internal EDP use, the Nexus operator has undergone a redesign to embrace general use cases. We are pleased to announce its availability on OperatorHub, allowing users to deploy this versatile operator independently for an enhanced experience.
Now, users can effortlessly leverage this independent component to enhance experience with Nexus.

In our commitment to optimizing your workflow, we've implemented Tekton cache support for CI pipelines. Jointly with pipeline dependency reorganization, this feature results in significant time savings.
Expect a remarkable improvement in the overall performance of Tekton pipelines, ensuring valuable time efficiency.

### Upgrades

* EDP Portal is now based on Headlamp [0.21.0](https://github.com/headlamp-k8s/headlamp/releases/tag/v0.21.0) version. ([#99](https://github.com/epam/edp-headlamp/issues/99))
* mkdocs has been updated to the [9.4.8](https://github.com/squidfunk/mkdocs-material/releases/tag/9.4.8) version. ([#113](https://github.com/epam/edp-install/issues/113))

### New Functionality

* Now EDP Portal displays the secret creation status for component integration. ([#122](https://github.com/epam/edp-install/issues/122))
* An integration status identifier has been added for DefectDojo, Dependency-Track, Registry, Nexus, and SonarQube tabs in the Configuration section. ([#29](https://github.com/epam/edp-headlamp/issues/29))
* Passwords and tokens are now hidden in EDP Portal, and users can reveal the hidden values by clicking the crossed eye button. ([#104](https://github.com/epam/edp-headlamp/issues/104))

### Enhancements

* The Tekton resource pruner has been updated to prune only Pods and PVCs, preserving the history of Pipeline Runs in Tekton Dashboard. ([#45](https://github.com/epam/edp-tekton/pull/45))
* The versioning of the codebase has been adjusted to be independent of the codebase type. ([#74](https://github.com/epam/edp-install/issues/74))
* Tekton cache support has been implemented for CI pipelines, with the option to enable it directly in the [values.yaml](https://github.com/epam/edp-install/blob/master/deploy-templates/values.yaml) file. ([#74](https://github.com/epam/edp-install/issues/74))
* To streamline EDP deployment, the minimal set of mandatory parameters has been decreased. ([#121](https://github.com/epam/edp-install/issues/121))
* The KeycloakRealmIdentityProvider resource now supports Kubernetes secret reference, allowing users to specify a Kubernetes secret in the "config.clientSecret" parameter. ([#21](https://github.com/epam/edp-keycloak-operator/issues/21))
* EDP Portal labels have been updated for a better user experience, including tooltips for buttons, fields, and descriptions. ([#113](https://github.com/epam/edp-headlamp/issues/113))
* The Configuration section of the EDP Portal has been updated, categorizing internal tabs. ([#111](https://github.com/epam/edp-headlamp/issues/111))
* The Deployment type field has been removed from the Create CD Pipeline menu. ([#109](https://github.com/epam/edp-headlamp/issues/109))
* An ingress controller is no longer required for application deployment. ([#101](https://github.com/epam/edp-headlamp/issues/101))
* Git Server integration no longer requires creating secrets. All necessary data can be created via UI. ([#100](https://github.com/epam/edp-headlamp/issues/100))
* EDP Portal now automatically encodes sensitive data (e.g., SSH keys, tokens) when users input it in the corresponding fields. ([#97](https://github.com/epam/edp-headlamp/issues/97))
* To expedite CI pipelines, Tekton cache support has been implemented. ([#74](https://github.com/epam/edp-tekton/issues/74))
* To accelerate pipelines execution, the number of independent tasks has been decreased. ([#68](https://github.com/epam/edp-tekton/issues/68))
* Now EDP uses branches as version baselines for Dependency-Track reports. ([#71](https://github.com/epam/edp-tekton/issues/71))
* We are continuously working on to create the best CI/CD platform ever made. To help ourselves achieve this aim, we enabled basic anonymous telemetry throughout the platform. It doesn't collect sensitive data, it aims to collect patterns of user behavior within our platform only. ([#112](https://github.com/epam/edp-install/issues/112))

### Fixed Issues

* Fixed issue when EDP deployment fails if the dockerRegistry.url parameter is not set. ([#118](https://github.com/epam/edp-install/issues/118))
* Fixed issue when the default Keycloak realm role didn't function properly. ([#22](https://github.com/epam/edp-keycloak-operator/issues/22))
* Fixed issue when the KeycloakRealm resource renaming caused resource deletion. ([#18](https://github.com/epam/edp-keycloak-operator/issues/18))
* Fixed issue when the catalog filter was shown improperly. ([#113](https://github.com/epam/edp-headlamp/issues/113))
* Fixed issue when Dependency-Track didn't setup project name for GoLang codebases. ([#70](https://github.com/epam/edp-tekton/issues/70))

### Documentation

* The new tab called [Pricing](https://epam.github.io/edp-install/pricing/) has been added to the mkdocs. In this page, users can see the support plans offered by the EDP team.
* The [Platform Multitenancy! Learn How We Adopt Capsule to Give Our Developers More Freedom 🚀](https://medium.com/epam-delivery-platform/platform-multitenancy-learn-how-we-adtopt-capsule-to-give-our-developers-more-freedom-ade7a8d84a33) article has been published on the Medium blog.
* The [Elevating CI/CD Security with Supply Chains](https://solutionshub.epam.com/blog/post/ci_cd_security) article has been published on the SolutionsHub blog.
* The [README.md](https://github.com/epam/edp-headlamp/blob/master/README.md) file of the [edp-headlamp](https://github.com/epam/edp-headlamp) operator has been updated with the new description and introduction video.
* The [README.md](https://github.com/epam/edp-sonar-operator/blob/master/README.md) file of the [edp-sonar-operator](https://github.com/epam/edp-sonar-operator) has been updated. ([#3](https://github.com/epam/edp-sonar-operator/issues/3))

The [Getting Started](https://epam.github.io/edp-install/overview/) section is updated with the following:
  * The [Supported Versions and Compatibility](https://epam.github.io/edp-install/supported-versions/) page has been updated. ([#116](https://github.com/epam/edp-install/issues/116))
  * The [Glossary](https://epam.github.io/edp-install/glossary/) page has been updated.

The [Operator Guide](https://epam.github.io/edp-install/operator-guide/) is updated with the following:
  * The [Upgrade EDP v3.5 to v3.6](https://epam.github.io/edp-install/operator-guide/upgrade-edp-3.6/) page has been added. ([#115](https://github.com/epam/edp-install/issues/115))
  * The [Integrate GitHub/GitLab in Tekton](https://epam.github.io/edp-install/operator-guide/import-strategy-tekton/) page has been updated. ([#116](https://github.com/epam/edp-install/issues/116))
  * The [Verification of EDP Artifacts](https://epam.github.io/edp-install/operator-guide/artifacts-verification/) page has been updated. ([#117](https://github.com/epam/edp-install/issues/117))

The [Developer Guide](https://epam.github.io/edp-install/developer-guide/) is updated with the following:
  * The [Overview](https://epam.github.io/edp-install/developer-guide/) page has been added.
  * The [Reference Architecture](https://epam.github.io/edp-install/developer-guide/reference-architecture/) page has been added. ([#120](https://github.com/epam/edp-install/issues/120))
  * The [Kubernetes Deployment](https://epam.github.io/edp-install/developer-guide/kubernetes-deployment/) page has been added. ([#120](https://github.com/epam/edp-install/issues/120))
  * The [Reference CI/CD Pipeline](https://epam.github.io/edp-install/developer-guide/reference-cicd-pipeline/) page has been added. ([#120](https://github.com/epam/edp-install/issues/120))
  * The [EDP Reference Architecture on AWS](https://epam.github.io/edp-install/developer-guide/aws-reference-architecture/) page has been added. ([#120](https://github.com/epam/edp-install/issues/120))
  * The [EDP Deployment on AWS](https://epam.github.io/edp-install/developer-guide/aws-deployment-diagram/) page has been added. ([#120](https://github.com/epam/edp-install/issues/120))

## Version 3.6.0 <a name="3.6.0"></a> (November 3, 2023)

## What's New

Exciting news! Our platform is now available on the [AWS Marketplace](https://aws.amazon.com/marketplace/pp/prodview-u7xcz6pvwwwoa), providing users with an additional free installation option [via AWS Marketplace](https://epam.github.io/edp-install/operator-guide/aws-marketplace-install/).

In this release, we've significantly extended the Overview page, introducing various valuable enhancements. Alongside dynamic widgets displaying key metrics, such as CD Pipelines, Stages, Branches, Codebases, and Tekton Pipeline runs, the page now also features a user-friendly list of recent pipeline runs. These pipeline runs are thoughtfully designed with clickable links, enabling swift navigation to their corresponding details in Tekton. This major update offers a detailed view of your deployment processes and provides easy access to essential information, making pipeline management easier.

EDP Portal now offers a simplified and efficient way for registries integrating and managing. This improvement simplifies the process of registry integration and management, allowing you to focus on what matters most.

Introducing digital signatures using [cosign](https://github.com/sigstore/cosign) for all artifacts within the platform. This significant security enhancement strengthens the overall safety of the platform.

We've implemented the Tree Diagram window for pipelines in both the Component details menu and the Overview page. This feature provides users with a comprehensive real-time view of the pipeline directly from the EDP Portal. Users can navigate to each pipeline task by clicking its name and zoom in or out for better observability.

We've also optimized the task sequence to reduce execution time, significantly boosting the overall performance of pipelines by decreasing the time required for pipelines to run.

In addition, the SSO Integration tab has been introduced in the Configuration section. Users can integrate a platform with the Keycloak identity provider in this tab.

### Upgrades

* EDP Portal is now based on Headlamp version [0.20.0](https://github.com/headlamp-k8s/headlamp/releases/tag/v0.20.0). ([#62](https://github.com/epam/edp-headlamp/issues/62))

### New Functionality

* Git Servers and Clusters are now editable directly in EDP Portal.  ([#82](https://github.com/epam/edp-headlamp/issues/82))
* The ability to watch application logs has been added to the stage menu. ([#83](https://github.com/epam/edp-headlamp/issues/83))
* The application terminal feature has been added directly in the stage menu of EDP Portal.
* The ability to open application URL form the stage menu has been added to EDP Portal. ([#77](https://github.com/epam/edp-headlamp/issues/77))
* The Container Registry page of the Configuration section has been updated. Now users can perform a hot switch between container registries eliminating the need to redeploy the platform. ([#20](https://github.com/epam/edp-headlamp/issues/20))
* The Tree Diagram window for pipelines has been added both Component details menu and Overview page. ([#66](https://github.com/epam/edp-headlamp/issues/66))
* The SSO Integration page has been added to the Configuration section. ([#57](https://github.com/epam/edp-headlamp/issues/57))

### Enhancements

* The pull request template has been updated. ([#107](https://github.com/epam/edp-install/issues/107))
* The text field of password type have been provided with the show/hide button to reveal the password. ([#84](https://github.com/epam/edp-headlamp/issues/84))
* Users can now specify a custom namespace when creating or updating a stage for Environment. ([#75](https://github.com/epam/edp-headlamp/issues/75))
* Now users can start Build pipelines only if the branch status is in green state. ([#78](https://github.com/epam/edp-headlamp/issues/78))
* Filters on the Overview page now cache previous filtering settings. ([#74](https://github.com/epam/edp-headlamp/issues/74))
* The unused "URL" field has been removed from the Jira integration secret form. ([#67](https://github.com/epam/edp-headlamp/issues/67)).
* The ability to set resource limits for the Tekton dashboard and Tekton event listener resources has been added to the values.yaml file. ([#54](https://github.com/epam/edp-tekton/issues/54))

### Fixed issues

* Removed extra "secretKey" field from the kaniko-docker-config secret. ([#63](https://github.com/epam/edp-install/issues/63))
* Fixed issue when the auto deploy feature doesn't work if [GitOps approach](https://epam.github.io/edp-install/user-guide/gitops/) is used. ([#23](https://github.com/epam/edp-codebase-operator/issues/23))
* Fixed improper cobebase status filtering in the Components list. ([#73](https://github.com/epam/edp-headlamp/issues/73))
* Fixed improper GitOps repo status handling. ([#65](https://github.com/epam/edp-headlamp/issues/65))

### Documentation

The [Getting Started](https://epam.github.io/edp-install/overview/) section is updated with the following:
  * The [Supported Versions and Compatibility](https://epam.github.io/edp-install/supported-versions/) page has been updated.
  * The [Glossary](https://epam.github.io/edp-install/glossary/) page has been updated.

The [Operator Guide](https://epam.github.io/edp-install/operator-guide/) is updated with the following:
  * The [External Secrets Operator Integration](https://epam.github.io/edp-install/operator-guide/external-secrets-operator-integration/) page has been updated. ([#106](https://github.com/epam/edp-install/issues/106))
  * The [Upgrade EDP from v3.4 to v3.5](https://epam.github.io/edp-install/operator-guide/upgrade-edp-3.5/) page has been added. ([#94](https://github.com/epam/edp-install/issues/94))
  * The [Nexus Sonatype Integration](https://epam.github.io/edp-install/operator-guide/nexus-sonatype/) page has been updated.
  * The [Adjust Jira Integration](https://epam.github.io/edp-install/operator-guide/jira-integration/) page has been updated. ([#65](https://github.com/epam/edp-install/issues/65))
  * The [Deploy AWS EKS Cluster](https://epam.github.io/edp-install/operator-guide/deploy-aws-eks/) page has been updated.
  * The [SonarQube Integration](https://epam.github.io/edp-install/operator-guide/sonarqube/) page has been updated. ([#88](https://github.com/epam/edp-install/issues/88))
  * The [Verification of EDP Artifacts](https://epam.github.io/edp-install/operator-guide/artifacts-verification/) page has been added.
  * The [Install DefectDojo](https://epam.github.io/edp-install/operator-guide/install-defectdojo/) page has been updated. ([#65](https://github.com/epam/edp-install/issues/65))
  * The [Install via AWS Marketplace](https://epam.github.io/edp-install/operator-guide/aws-marketplace-install/) page has been updated. ([#75](https://github.com/epam/edp-install/issues/75))
  * The [Capsule Integration](https://epam.github.io/edp-install/operator-guide/capsule/) page has been updated. ([#93](https://github.com/epam/edp-install/issues/93))
  * The [Install EDP](https://epam.github.io/edp-install/operator-guide/install-edp/) page has been updated. ([#95](https://github.com/epam/edp-install/issues/95))
  * The [Install via Helmfile](https://epam.github.io/edp-install/operator-guide/install-via-helmfile/) page has been updated. ([#95](https://github.com/epam/edp-install/issues/95))
  * The [Secure Delivery on the Platform](https://epam.github.io/edp-install/operator-guide/overview-devsecops/) page has been updated.
  * The [Nexus Sonatype Integration](https://epam.github.io/edp-install/operator-guide/nexus-sonatype/) page has been updated. ([#99](https://github.com/epam/edp-install/issues/99))
  * The [Integrate GitHub/GitLab in Tekton](https://epam.github.io/edp-install/operator-guide/import-strategy-tekton/) page has been updated. ([#96](https://github.com/epam/edp-install/issues/96))

The [User Guide](https://epam.github.io/edp-install/user-guide/) is updated with the following:
  * The [Components Overview](https://epam.github.io/edp-install/user-guide/components/) page has been added. ([#100](https://github.com/epam/edp-install/issues/100))
  * The [Manage GitOps](https://epam.github.io/edp-install/user-guide/gitops/) page has been added.
  * The [Overview](https://epam.github.io/edp-install/user-guide/) page has been updated.
  * The [Add Application](https://epam.github.io/edp-install/user-guide/add-application/) page has been updated.
  * The [Manage Applications](https://epam.github.io/edp-install/user-guide/application/) page has been updated.
  * The [Add Autotest](https://epam.github.io/edp-install/user-guide/add-autotest/) page has been updated.
  * The [Manage Autotests](https://epam.github.io/edp-install/user-guide/autotest/) page has been updated.
  * The [Add Library](https://epam.github.io/edp-install/user-guide/add-library/) page has been updated.
  * The [Manage Libaries](https://epam.github.io/edp-install/user-guide/library/) page has been updated.
  * The [Add Infrastructure](https://epam.github.io/edp-install/user-guide/add-infrastructure/) page has been updated.
  * The [Manage Infrastructures](https://epam.github.io/edp-install/user-guide/infrastructure/) page has been updated.
  * The [Manage Branches](https://epam.github.io/edp-install/user-guide/manage-branches/) page has been updated.
  * The [Add Environment](https://epam.github.io/edp-install/user-guide/add-cd-pipeline/) page has been updated.
  * The [Manage Environments](https://epam.github.io/edp-install/user-guide/manage-environments/) page has been updated.
  * The [Add Quality Gate](https://epam.github.io/edp-install/user-guide/add-quality-gate/) page has been updated.
  * The [Manage Git Servers](https://epam.github.io/edp-install/user-guide/git-server-overview/) page has been updated. ([#102](https://github.com/epam/edp-install/issues/102))

The [Developer Guide](https://epam.github.io/edp-install/developer-guide/) is updated with the following:
  * The [EDP Project Rules. Working Process](https://epam.github.io/edp-install/developer-guide/edp-workflow/) page has been updated.

Other:
  * The [RoadMap](https://epam.github.io/edp-install/roadmap/) page has been updated.
  * The [README.md](https://github.com/epam/edp-install/blob/master/README.md) file has been updated. ([#47](https://github.com/epam/edp-install/issues/47))


## Version 3.5.3 <a name="3.5.3"></a> (September 28, 2023)

### Upgrades

* The Go language is upgraded to the [1.20](https://tip.golang.org/doc/go1.20) version.

### New Functionality

* Added two new buttons, "Show Logs" and "Show Terminal" to the Environments stage within the EDP Portal UI. These buttons empower users to effortlessly access logs and directly log into the associated pod, improving visibility and control during the deployment process.
* Implemented the capability to sign images when pushing them to Harbor. This added layer of security ensures that your images are signed and verified, enhancing the trustworthiness of your deployments.

### Enhancements

* Updated the Argo CD application creation process. The 'namespace' field in the 'destination' section of the Application resource is now sourced from the 'Stage.spec' resource.

#### Breaking Changes

* The Git Server status now provides a comprehensive error message when failing. If your cluster hosts multiple EDP environments, it is required to update the edp-codebase-operator. However, if you have only one EDP environment in the cluster, applying these changes is unnecessary.

### Fixed Issues

* Fixed issue when Build pipeline failed on sonar step for Go operator SDK.
* Fixed issue when the update-build-number-gradle task updated all the version keys presented in the build.gradle file.
* Fixed issue when successful codebase creation notification couldn't appear.

## Version 3.5.2 <a name="3.5.2"></a> (September 22, 2023)

### Enhancements

* We've streamlined the installation process by disabling SSO integration and excluding the edp-keycloak-operator installation by default, reducing initial setup prerequisites.


## Version 3.5.1 <a name="3.5.1"></a> (September 22, 2023)

### Routine

* The Artifact Hub annotations related to container images have been removed from the [Chart.yaml](https://github.com/epam/edp-install/blob/master/deploy-templates/Chart.yaml) file to prevent redundant checks.


## Version 3.5.0 <a name="3.5.0"></a> (September 21, 2023)

## What's new

In this release, we've streamlined EDP installation. The EDP installer now focuses on setting up only core components like edp-headlamp, edp-tekton, codebase-operator, and cd-pipeline-operator. Additional resources, such as Nexus and SonarQube operators can be installed separately. Moreover, the edp-sonar-operator is now accessible as a standalone solution on [OperatorHub](https://operatorhub.io/operator/sonar-operator).

To enhance the security of your software delivery process, we present a new feature: Security Supply Chain with Tekton Chains. It allows for securely capturing and verifying metadata, including source code, dependencies, containers, infrastructure, and applications. With the help of Tekton Chains, users can sign software artifacts with cryptographic keys (e.g., x509 or KMS) and store them in various backends like Tekton Results API or OCI registries. This feature also ensures compliance with [SLSA](https://slsa.dev/spec/v1.0/requirements) L2 and L3 provenance standards.

We've significantly restructured our secret management process. Currently, this updated secret approach has been implemented for the components, such as Jira, DefectDojo, DependencyTrack, Nexus, and SonarQube. EDP Portal has been equipped with the ability to both create and manage these secrets.

We've introduced [Capsule](https://capsule.clastix.io/) support to enhance our CI/CD platform, bringing tenant management benefits to our users. One of the key advantages of integrating Capsule is its multi-tenancy capabilities. With Capsule, we can now efficiently manage multiple tenants in our platform, isolating their environments, resources, and data. This means improved security, scalability, and resource allocation, ensuring a smoother experience for everyone using our platform.

The Configuration section has a new view, with redesigned Container Registry tab. Now, you can create integration secrets for Harbor or AWS ECR directly in the EDP Portal UI. Additionally, the Configuration section has two new tabs: GitOps and Links. In the GitOps tab, you have the flexibility to customize the default [values.yaml](https://github.com/epam/edp-install/blob/release/3.5/deploy-templates/values.yaml) file. Plus, you are able to create or edit EDP components from the Overview page using the Links section.

### New Functionality

* The `Values override` checkbox has been added to the CD pipelines in the Environments section. When this option is enabled, default values are overwritten with custom one when deploying a stage.
* The edp-keycloak-operator now supports setting multiple equally accessible URLs for one application. The edp-keycloak-operator version v1.17.1 has been published on OperatorHub.
* The `edpName` value has been removed from the values.yaml file. Now the default Helm values are used instead.
* The cd-pipeline operator now can use the Capsule tool capabilities to provide multi-tenancy.
* The `kioskEnabled` parameter has been removed from the values.yaml file. To enable Kiosk, use the `--set global.tenancyEngine=kiosk` parameter instead.
* Nexus-operator has been aligned to work with the LTS Nexus 3.58.1 version.
* The method Uint32 for generating cryptographic values has been replaced with the crypto/rand method to improve overall security.
* The `sonar_url` and `nexus_url` parameters have been deprecated in the values.yaml file and migrated to appropriate secrets.
* Now EDP Portal notifies users if no namespace is set in the EDP Portal settings.
* Now the Create button in the Components section is hidden in case if no Git Server or GitOps repository is connected.
* The link that refers to a newly created application is now displayed if it was successfully created in Marketplace.
* The Git Server provisioning has been modified. Now users can provision only one Git Server.
* The required fields for most tabs in the Configuration section have been redesigned.
* A new tab called GitOps has been added to the Configuration section. It is designed to create a repository that follows the GitOps approach where the parameter are stored. These parameters can override the default values of the CD application. GitOps entities in the contain a link to the values.yaml files from CD Stage.
* The DependencyTrack tab has been added to the Configuration section. It allows to add the DependencyTrack stage into pipelines to have an additional security scanner.
* The Configuration section now contains a new tab called Links. It enables users to create or edit EDP components that are usually shown in the Overview page of the EDP Portal UI.

### Enhancements

* The SonarQube scanner now uses Java11 to work with Java8 applications.
* The default host for the GitLab server is now set to `gitlab.com`.
* To make the UI more interactive, clicking on the Deploy/Update/Uninstall buttons in the Environments section is now accompanied by corresponding notifications.
* The `keycloakUrl` parameter has been moved to the OIDC section.
* The `admins` and `developers` fields for Keycloak users have been moved to the sso subsection.
* The `sso.enabled` parameter has been added to allow user to manage edp-keycloak-operator resource creation for Argo CD.
* The secret name pattern for Version Control Systems has been modified for GitHub and GitLab. The current secret name is `ci-github` and `ci-gitlab`.
* The docker-registry component has been renamed to container-registry in the Overview page.
* The `validateMaintainers` parameter is now disabled by default for the Chart Testing linter.

### Fixed Issues

* Fixed security issue when the NuGet token was shown in output logs.
* Fixed issue with external component logic when necessary secrets weren't mounted to Tekton tasks.
* The default versioning type on longer relies on the application version to prevent Jira integration issues.
* Fixed issue when Build pipeline failed for Go operator SDK on  the `sonar` step.
* Fixed issue when incorrect properties were set for the Git Server resource.
* Fixed incorrect the execution sequence in the update-build-number and SAST tasks for NPM.
* Fixed issue when the "Push" task pushed incorrect Java, C#, and Python application version into Nexus.
* Fixed unexpected Review pipeline failure on the `sonar` stage for Maven autotest codebase type if Clone or Import strategy is used.

### Documentation

The [Getting Started](https://epam.github.io/edp-install/overview/) section is updated with the following:
  * The [Supported Versions and Compatibility](https://epam.github.io/edp-install/supported-versions/) page has been updated.

The [Operator Guide](https://epam.github.io/edp-install/operator-guide/) is updated with the following:
  * The [Adjust Jira Integration](https://epam.github.io/edp-install/operator-guide/jira-integration/) page has been updated.
  * The [Install DependencyTrack](https://epam.github.io/edp-install/operator-guide/dependency-track/) page has been added.
  * The [Install via AWS Marketplace](https://epam.github.io/edp-install/operator-guide/aws-marketplace-install/) page has been added.
  * The [Cluster Add-Ons Overview](https://epam.github.io/edp-install/operator-guide/add-ons-overview/) page has been added.
  * The [Upgrade EDP v3.3 to 3.4](https://epam.github.io/edp-install/operator-guide/upgrade-edp-3.4/) page has been added.
  * The [Install EDP](https://epam.github.io/edp-install/operator-guide/install-edp/) page has been updated.
  * The [Integrate Harbor With EDP Pipelines](https://epam.github.io/edp-install/operator-guide/container-registry-harbor-integration-tekton-ci/) page has been updated.
  * The [Install DefectDojo](https://epam.github.io/edp-install/operator-guide/install-defectdojo/) page has been updated.
  * The [SonarQube Integration](https://epam.github.io/edp-install/operator-guide/sonarqube/) page has been updated.
  * The [Nexus Sonatype Integration](https://epam.github.io/edp-install/operator-guide/nexus-sonatype/) page has been updated.

The [Developer Guide](https://epam.github.io/edp-install/developer-guide/) is updated with the following:
  * The [EDP Project Rules. Working Process](https://epam.github.io/edp-install/operator-guide/nexus-sonatype/) page has been updated.

Other:
  * The edp namespace name has been changed to `edp` throughout the whole documentation.
  * The URL for the EDP documentation has been changed.


## Version 3.4.1 <a name="3.4.1"></a> (August 28, 2023)

### Upgrades

* EDP portal is now based on Headlamp version [0.19.1](https://github.com/headlamp-k8s/headlamp/releases/tag/v0.19.1).

### New Functionality

* The grid view mode has been added to the Overview page in EDP Portal.
* The Marketplace section has been provided with pagination and status handling.
* EDP documentation buttons have been re-organized. Now the icon looks like a circle with question mark inside. Better still, it now contains three buttons: "Documentation" to open respective documentation page, "Join Discussions" to navigate to EDP discussions on GitHub and "Open an issue/request" button to create an issue for the EDP team.

### Enhancements

* The `validateMaintainers` parameter for the ChartTesting tool is now set to `false` by default.

### Fixed Issues

* Fixed issue when successful resource deletion in EDP Portal caused a return to the previous page.
* Fixed improper real-time image tag updating.
* Fixed issue when the Marketplace section page was empty if there is no view mode selected.


## Version 3.4.0 <a name="3.4.0"></a> (August 18, 2023)

## What's new

Introducing the "Marketplace" section, a new addition designed to offer pre-configured templates that expedite application setup, simplify development, and improve templates management efficiency. Craft personalized templates aligned with your organization's requirements for enhanced process streamlining, time efficiency, and governance.

Presenting the integration of [Kubernetes Cluster Add-Ons](https://medium.com/epam-delivery-platform/edp-kubernetes-cluster-add-ons-4a3b4ccc4627) in our latest EDP release, featuring the Argo CD tool and GitOps methodology. [Our extensive array of add-ons](https://github.com/epam/edp-cluster-add-ons) streamline component management and installation at the Kubernetes level, extending EDP capabilities with an array of Observability, Security, and Scalability options.

In this release, we introduce the choice between the All-In-One and Shared Approach in EDP. The latest release enables users to optimize by utilizing shared components such as SonarQube and Nexus across multiple EDP tenants, streamlining accessibility and reducing operational complexity.

By incorporating Harbor into our ecosystem, we reaffirm our dedication to the cloud-agnostic essence of the EPAM Delivery Platform. While adopting Harbor as our default OCI compliant artifact storage, we maintain unwavering support for cloud-based container registries. This commitment empowers users with enduring flexibility to fulfill their distinct needs, exemplifying our drive to cultivate a dynamic and adaptable platform.

In this update, our technical shift aims to improve the User Interface by extending the developer-friendly configuration section across the platform. This involves transforming CD Pipelines into Environments for more precise and efficient management, reinforcing our commitment to enhancing platform efficiency and adaptability, accessible via both UI and CLI.

This update also brings Antora, an advanced documentation-as-code solution, seamlessly integrated into our Marketplace's Template. Empower your team to create, deploy, and maintain up-to-date documentation while freeing technical writers from design concerns.

EDP's scope goes beyond CI/CD excellence, notably in Keycloak management. If Keycloak is vital to your solution, consider its crucial aspects: streamlined user control, attribute updates, and deletions, along with realm, group, role, client scope, and client management. Simplifying this complexity through a declarative approach is where the Keycloak Operator excels, enabling efficient bulk user operations, streamlined realm management, and self-service Single Sign-On (SSO) integration for developers. Discover the EDP Keycloak Operator on [OperatorHub](https://operatorhub.io/operator/edp-keycloak-operator).

Our latest release has transitioned exclusively to Tekton deploy scenario. This shift optimizes feature implementation and aligns with evolving user needs. This change reflects our commitment to technical advancement and efficient delivery. Jenkins is no longer supported.

To strengthen security measures, we're [expanding our security checks by integrating](https://epam.github.io/edp-install/operator-guide/overview-devsecops/) a diverse array of open-source security tools tailored to specific functionalities. This enhancement provides security practices into the software development lifecycle through a robust DevSecOps approach.

### Upgrades

* Tekton-dashboard is updated to the [0.36.1](https://github.com/tektoncd/dashboard/releases/tag/v0.36.1) version.
* Alpine image is updated to the [3.18.2](https://hub.docker.com/layers/library/alpine/3.18.2/images/sha256-25fad2a32ad1f6f510e528448ae1ec69a28ef81916a004d3629874104f8a7f70?context=explore) version for all operators.
* Nexus image is updated to the [3.58.1](https://hub.docker.com/layers/sonatype/nexus3/3.58.1/images/sha256-586060431b645ddd323f4fb142e4e3fa1684205c1c1351d633a58a0326d35bbb?context=explore) version.
* EDP portal is now based on Headlamp version [0.19.0](https://headlamp.dev/blog/2023/08/headlamp-0.19.0-a-new-home-with-a-cleaner-ui/).

### New Functionality

* The Marketplace section has been added to EDP Portal. It allows users to view Template kind resources which will be useful when managing applications.
* New section called Configuration is added to EDP Portal. In this section, users can connect EDP with different clusters, integrate with different container registries, such as AWS ECR or Harbor, provide Nexus as an artifact storage or integrate code review analysis powered by SonarQube.
* Antora framework support has been added to JavaScript language to rapidly scaffold documentation engine.
* The Helm framework has been added into Helm language frameworks in library codebase mapping.
* The Next.js framework support is added to JavaScript language.
* User interface has been significantly refactored. The Headlamp UI has been renamed to EDP Portal.
* The Keycloak user password can also be set from secret.
* The ability to configure SubComponent of component in a realm has been added.
* The edp-keycloak-operator is now provided with CI/CD established on GitHub.
* The codebase-operator now requires the helm-docks stage to ensure documentation updates with chart changes.
* Since EDP v3.4.0, the Jenkins deploy scenario is considered deprecated.
* Users can now provide credentials of private registry into any CD namespace.
* The codebase status tooltip has become interactive. Users can copy status message.
* The links to corresponding documentation has been added throughout the whole EDP Portal.

### Enhancements

* The `el-listener-app-tls` endpoint now supports TLS connection for better security.
* The `branchVersionPostfix` field is no longer mandatory when creating release branches.
* Manual/auto trigger type labels are displayed nearly the stage name in CD Pipelines.
* The CD Pipeline page has been redesigned.
* The labels for DefectDojo and Jira secrets have been added to EDP Portal.
* An example of values.yaml file with custom certs support has been provided.
* The `nexusUrl` parameter has been added to the global section.
* The default codebase branch has been set for GitLab/GitHub.
* The codebase-operator doesn't depend on the perf-operator anymore.
* The use of '--' characters in the Codebase name is no longer allowed.
* The `StartFrom` parameter for Codebase versioning is now required for edp version type.
* The Helm template has been aligned for the codebase-operator.
* Users now have the option to configure Keycloak Frontend URL via the edp-keycloak-operator.
* The additional printer columns for CR Keycloak and Realm custom resources has been added to cd-pipeline-operator, edp-keycloak-operator and codebase-operator.
* The `KeycloakClient` attributes have been provided with the default values.
* The build pipeline run button has been moved to branch component.
* Terraform language has been renamed to HCL in the application code language list.
* Https prefixes are now prepended to EDPComponent URLs if they lack it.
* The "in-cluster" option has been set as default into cluster select when creating stage.
* The `Stage.spec.source` parameter has been provided with default value.

### Fixed Issues

* Fixed unexpected error when using the /recheck comment for rerun review pipeline in GitLab or GitHub.
* Fixed issue when it was impossible to set autotests when creating a new stage for CD Pipelines.
* Fixed issue with updating CD Pipeline applications when reopening Create/Edit dialogs.
* Fixed issue when EDP Portal showed incorrect deploy version value for Helm applications.
* Flask and FastAPI frameworks have been removed from Python language on library creation.
* Fixed issue when the Environments (formerly CD Pipelines) pages wasn't showing. Fixed improper Tekton resource status calculation.
* Fixed issue in UI when users had to do extra clicks after adding stages to CD Pipeline to make the Deploy button active.
* The `UPDATE_PASSWORD` action is no longer required by default if it is not explicitly set in the spec.requiredUserActions.
* The `keycloak.Spec.url/keycloak.Spec.basePath` formation has been refactored.
* Fixed issue when the edp-keycloak-operator didn't configure Keycloak.
* Fixed issue when oauth-proxy route creation was incorrect.

### Documentation

* The [Getting Started] section is updated with the following:
  * The [Overview](https://epam.github.io/edp-install/overview/) page has been updated.
  * The [Supported Versions](https://epam.github.io/edp-install/supported-versions/) page has been updated.

* The [User Guide](https://epam.github.io/edp-install/user-guide/) is updated with the following:
  * To simplify navigation, the [User Guide](https://epam.github.io/edp-install/user-guide/) section has been reorganized. Now it resembles EDP Portal structure.
  * The [Add Cluster](https://epam.github.io/edp-install/user-guide/add-cluster/) page has been added.
  * The [Manage Clusters](https://epam.github.io/edp-install/user-guide/cluster/) page has been added.
  * The [Add Infrastructure](https://epam.github.io/edp-install/user-guide/add-infrastructure/) page has been added.
  * The [Manage Infrastructures](https://epam.github.io/edp-install/user-guide/infrastructure/) page has been added.
  * The [Marketplace Overview](https://epam.github.io/edp-install/user-guide/marketplace/) page has been added.
  * The [Add Component via Marketplace](https://epam.github.io/edp-install/user-guide/add-marketplace/) page has been added.
  * The [Add Application](https://epam.github.io/edp-install/user-guide/add-application/) page has been updated.
  * The [Add Library](https://epam.github.io/edp-install/user-guide/add-library/) page has been updated.
  * The [Add Autotest](https://epam.github.io/edp-install/user-guide/add-autotest/) page has been updated.
  * The Application [Overview](https://epam.github.io/edp-install/user-guide/application/) page has been updated and renamed to Manage Applications.
  * The Library [Overview](https://epam.github.io/edp-install/user-guide/library/) page has been updated and renamed to Manage Libraries.
  * The Autotest [Overview](https://epam.github.io/edp-install/user-guide/autotest/) page has been updated and renamed to Manage Autotests.
  * The [Adjust Jira Integration](https://epam.github.io/edp-install/operator-guide/jira-integration/) page has been updated.

* The [Operator Guide](https://epam.github.io/edp-install/operator-guide/) is updated with the following:
  * The [Uninstall EDP](https://epam.github.io/edp-install/operator-guide/delete-edp/) page has been added.
  * The [Custom SonarQube Integration](https://epam.github.io/edp-install/operator-guide/sonarqube/) page has been added.
  * The [Install Harbor](https://epam.github.io/edp-install/operator-guide/install-harbor/) page has been added.
  * The [Harbor OIDC Configuration](https://epam.github.io/edp-install/operator-guide/harbor-oidc/) page has been added.
  * The [v3.2 to 3.3] page has been added.
  * The [Integrate Harbor With EDP Pipelines](https://epam.github.io/edp-install/operator-guide/container-registry-harbor-integration-tekton-ci/) page has been added.
  * Most of the documentation related to Version Control Systems has been moved to the corresponding [subsection](https://epam.github.io/edp-install/operator-guide/vcs/).
  * The [CI Pipelines for Terraform](https://epam.github.io/edp-install/user-guide/terraform-stages/) page has been updated.
  * The Enable VCS Import Strategy page has been split into 2 pages called [Integrate GitHub/GitLab in Jenkins](https://epam.github.io/edp-install/operator-guide/import-strategy-jenkins/) and [Integrate GitHub/GitLab in Tekton](https://epam.github.io/edp-install/operator-guide/import-strategy-tekton/).
  * The GitHub Integration page has been renamed to [GitHub Webhook Configuration](https://epam.github.io/edp-install/operator-guide/github-integration/).
  * The GitLab Integration page has been renamed to [GitLab Webhook Configuration](https://epam.github.io/edp-install/operator-guide/gitlab-integration/).
  * The [EDP install](https://epam.github.io/edp-install/operator-guide/install-edp/) page has been updated.
  * The [EKS OIDC With Keycloak](https://epam.github.io/edp-install/operator-guide/configure-keycloak-oidc-eks/) page has been updated.
  * The [Headlamp OIDC Configuration](https://epam.github.io/edp-install/operator-guide/headlamp-oidc/) page has been updated.
  * The [Monitoring](https://epam.github.io/edp-install/operator-guide/tekton-monitoring/) page has been updated.
  * The [Manage Custom Certificates](https://epam.github.io/edp-install/operator-guide/manage-custom-certificate/) page has been updated.

* The [Use Cases](https://epam.github.io/edp-install/use-cases/) is updated with the following:
  * The [Autotest as a Quality Gate](https://epam.github.io/edp-install/use-cases/autotest-as-quality-gate/) page has been updated.

* Other:
  * The Autodeploy feature description has been added to the [arch.md](https://github.com/epam/edp-cd-pipeline-operator/blob/master/docs/arch.md) file.
  * The [RoadMap](https://epam.github.io/edp-install/roadmap/) section has been updated.


## Version 3.3.0 <a name="3.3.0"></a> (May 25, 2023)

## What's new

In this release, [Headlamp UI](https://epam.github.io/edp-install/user-guide/) has been updated. Particularly, the component creation procedure has been significantly changed to be more intuitive. Users can also observe visualized autotest monitoring provided with clickable links to the corresponding Tekton pipelines. Also, we provided completely new component type called Infrastructure which is designed to build infrastructures powered by Terraform.

We are thrilled to announce that one of our features called Autodeploy is now also available for Tekton CI tool. It is designed to automatically define the latest artifact tag and upgrade this service on each environment where this service is deployed. This crucial feature aimed to automate the application update procedure when using Tekton.

With version 3.3.0, the Create and Clone strategies have become available for [GitLab/GitHub](https://epam.github.io/edp-install/operator-guide/import-strategy/) version control systems. This enhancement provides increased flexibility for onboarding your applications within our platform.

We have provided Java [Multi-Module](https://epam.github.io/edp-install/user-guide/add-application/?h=multi+module#codebase-info-menu) support for Tekton CI tool. This enhancement improves code organization, build time, dependency management, testing, parallel development, and overall maintainability of applications.

One of our major highlights is that code review pipelines can now be re-triggered manually to force their runs. This feature was implemented to simplify the code review process.

From now on, EDP supports Terraform infrastructure as an additional language out of the box. Better still, we have added the new frameworks, such as Vue, Angular and Express frameworks for JavaScript, Gin framework for Go and .Net 3.1 framework support for C# which migrated from the previous deploy scenario.

Apart from that, we are presenting our [EDP Introduction video](https://www.youtube.com/watch?v=Xsy1UKMb8vg&ab_channel=ThePlatformTeam). This video provides a comprehensive overview of the platform's capabilities and benefits, making it easier for users to get started and maximize their productivity.

Lastly, users can easily check their applications for bugs or other issues using [Autotests as a Quality Gate](https://epam.github.io/edp-install/user-guide/autotest/#add-autotest-as-a-quality-gate). This powerful addition allows users to conveniently add multiple autotests to any environment, be it development or production. By running the appropriate autotests, users can effectively evaluate the stability of your application and ensure it functions properly.

### Upgrades

* Alpine image version is updated to the [3.16.4](https://hub.docker.com/layers/library/alpine/3.16.4/images/sha256-0b29a7f4d42d6b5d6433ea91322903900e81b95d47d97d909a6e388e840f4f4a?context=explore) version.
* Go language is updated to the [1.19](https://go.dev/blog/go1.19) version.
* Prometheus stack is updated to the [45.21.0](https://github.com/prometheus-community/helm-charts/tree/main/charts/kube-prometheus-stack/templates/prometheus) version.
* Argo CD is updated to the [2.7.0](https://github.com/argoproj/argo-cd/releases/tag/v2.7.0) version.
* Headlamp is updated to the [0.16.0](https://headlamp.dev/docs/latest/installation/desktop/linux-installation#upgrading) version.
* Tekton dashboard is updated to the [0.35.0](https://github.com/tektoncd/dashboard/releases) version.
* Semgrep scanner is updated to the [1.19.0](https://github.com/returntocorp/semgrep/releases/tag/v1.19.0) version.
* Go security scanner is updated to the [2.15.0](https://github.com/securego/gosec/releases/tag/v2.15.0) version.

### New Functionality

* The additional volumes in [OAuth2-Proxy](https://epam.github.io/edp-install/operator-guide/manage-custom-certificate/) are now can be added to improve data management.
* Custom certificates are now also supported by Headlamp to make Headlamp UI more secure.
* Nexus proxy usage is implemented for the NPM package manager. This feature will increase Nexus flexibility.
* Code review pipelines can now be re-triggered manually to force their runs by sending "/recheck" reply message. This might be helpful if the code review process is stuck.
* Autotests as a Quality Gates are now displayed in the CD Pipelines menu for Tekton deploy scenario. This allows users to monitor run status for autotests. By running the appropriate autotests, users can ensure if the application functions properly. Autotests are to also provided with clickable links to have to the possibility to observe pipeline runs.
* GitLab and GitHub now support Create and Clone strategies. This improvement provides increased flexibility for onboarding applications in EDP.
* New type of component called Infrastructure is added to Headlamp. It allows users to create cloud infrastructures powered by AWS from scratch.
* Vue, Angular and Express frameworks support is added for JavaScript language.
* Gin framework support is added for Go language.
* Headlamp UI now supports deploying Helm applications.
* To improve overall integration with other tools. We have added such links to pipeline stages that lead users to Grafana, Kibana, Argo CD, and cluster.

### Enhancements

* To simplify secret creation workflow, Argo CD OIDC client can now be provided with custom secret name.
* In the `Components` menu in Headlamp UI, near the build status icon, the `Go to the Source code` and `Go to the Quality Gates` clickable icons is added to navigate users directly to the corresponding application code and quality gates.
* To simplify bug reporting procedure, templates for reporting GitHub issues in our operators are created. Please see an [example](https://github.com/epam/edp-keycloak-operator/issues/new/choose).
* The [codebase template](https://github.com/epam/edp-codebase-operator/blob/master/build/templates/applications/helm-chart/kubernetes/templates/deployment.yaml) chart has been refactored, so now it is aligned to upstream and  doesn't contain deprecated parameters.
* The .Net language option is hidden from the codebase creation menu for non-Jenkins CI tools.
* Now the `code-review` pipeline includes the `Dockerbuild-verify` stage that depends on the `build` step, thus allowing to build the application.

### Fixed Issues

* The `JenkinsFolder` repository URL is added for create/clone strategy if Jenkins deploy scenario is used.
* The hard-coded `project-creator` SSH user was removed, Git application path to Git repo provisioning was aligned.
* Fixed issue when the first reconciliation fails with a git clone error for import strategy.
* Hotfix swap namespace/name in delete resource request.
* Fixed issue when Java and JavaScript pipelines were failing when the JIRA integration option was enabled for create strategy.
* Fixed wrong tag attachment when EDP versioning is used.

### Documentation

* The [User Guide](https://epam.github.io/edp-install/user-guide/) is updated with the following:
  * The [Add CD Pipeline](https://epam.github.io/edp-install/user-guide/add-cd-pipeline/) page has been updated.
  * The [Add Application](https://epam.github.io/edp-install/user-guide/add-application/) page has been updated.
  * The [Add Autotest](https://epam.github.io/edp-install/user-guide/add-autotest/) page has been updated.
  * The [Add Library](https://epam.github.io/edp-install/user-guide/add-library/) page has been updated.

* The [Operator Guide](https://epam.github.io/edp-install/operator-guide/) is updated with the following:
  * The [Manage Namespace](https://epam.github.io/edp-install/operator-guide/namespace-management/) page has been added.
  * The [Argo CD Integration](https://epam.github.io/edp-install/operator-guide/argocd-integration/#deploy-argo-cd-application) page has been updated.
  * The [Aggregate Application Logs Using EFK Stack](https://epam.github.io/edp-install/operator-guide/kibana-ilm-rollover/) page has been added.
  * The [Protect Endpoints](https://epam.github.io/edp-install/operator-guide/oauth2-proxy/) page has been updated.
  * The [Install EDP](https://epam.github.io/edp-install/operator-guide/install-edp/) page has been updated.
  * The [Microsoft Teams Notification](https://epam.github.io/edp-install/operator-guide/notification-msteams/) page has been added.
  * The [Set Up Kubernetes](https://epam.github.io/edp-install/operator-guide/kubernetes-cluster-settings/) page has been updated.
  * The [Enable VCS Import Strategy](https://epam.github.io/edp-install/operator-guide/import-strategy/) page has been updated.

* The [Use Cases](https://epam.github.io/edp-install/use-cases/) is updated with the following:
  * The [Secured Secrets Management for Application Deployment](https://epam.github.io/edp-install/use-cases/external-secrets/) page has been added.


## Version 3.2.2 <a name="3.2.2"></a> (April 21, 2023)

### Fixed Issues

* Fixed issue when the `keycloakclientscope` resource couldn't be created depending on the Argo CD integration settings.

## Version 3.2.1 <a name="3.2.1"></a> (March 31, 2023)

### New Functionality

* The ability of using custom certificates is added to nexus-operator OAuth2-Proxy.
* The ability of using custom certificates is added to edp-install OAuth2-Proxy.

### Fixed Issues

* Remove the unused RoleBinding for jenkins-operator for OpenShift deploy scenario.

## Version 3.2.0 <a name="3.2.0"></a> (March 26, 2023)

## What's New

One of the major highlights of this release is the addition of support for the latest Java 17 language version. Furthermore, we have added support for the FastAPI and Flask frameworks for Python language. We have also included support for the .NET 6.0 framework for C# language. We have expanded our support to include Helm as a library. This will allow users to onboard their custom charts and facilitate development.

To improve security and streamline the login process, we have improved the [RBAC model](https://epam.github.io/edp-install/operator-guide/edp-access-model/?h=rbac#cluster-rbac-resources) for cluster login. Thus, we have enriched this with more granular permissions.

We are pleased to announce that our platform now has the ability to use [custom certificates](https://epam.github.io/edp-install/operator-guide/manage-custom-certificate/) when deploying platform. This feature provides greater flexibility and customization options for users, enabling them to provide an additional level of flexibility.

In addition to this, we have introduced the ability to enable [Single Sign-On](https://epam.github.io/edp-install/operator-guide/oauth2-proxy/) for the services (applications) that do not support OIDC from the box. This feature offers high flexibility, enabling you to choose the optimal authentication solution.

We have integrated Kaniko to OpenShift internal registry, allowing users to build and store their container images with OpenShift native solution. This integration offers a range of benefits, including faster build times and greater control over image for OpenShift users.

We are excited to introduce a new section in our documentation called ["Use Cases"](https://epam.github.io/edp-install/use-cases/). This section has been thoughtfully designed to guide you through the most common scenarios for using EDP, helping you to gain a better understanding of how our platform can be utilized to its full potential. By reading through these use cases, you will be equipped with the knowledge and tools you need to make the most of EDP and build advanced, high-quality applications with ease.

Explore the upgrades, new functionality and improvements below.

### Upgrades

* Keycloak is updated to the [20.0.3](https://www.keycloak.org/2023/01/keycloak-2003-released.html) version.
* Operator SDK is updated to the [1.25.3](https://github.com/operator-framework/operator-sdk/releases) version.
* Alpine image is updated to the [3.16.4](https://hub.docker.com/_/alpine/tags?page=1&name=3.16.4) version.
* .NET is updated to the [6.0](https://support.microsoft.com/en-us/topic/-net-6-0-update-b85603d0-00d5-4aa4-adac-b045322d35fc) version.
* Findbugs plugin version in SonarQube is updated to the [4.2.2](https://github.com/spotbugs/sonar-findbugs/releases/tag/4.2.2) version.
* Our latest application version includes an upgrade to the upstream Headlamp version [0.16.0](https://github.com/headlamp-k8s/headlamp/releases/tag/v0.16.0), which is now the foundation for our platform.
* Semgrep is updated to the [1.2.1](https://github.com/returntocorp/semgrep/blob/develop/CHANGELOG.md) version.
* Argo CD is updated to the [2.5.8](https://github.com/argoproj/argo-cd/releases/tag/v2.5.8) version.

### New Functionality

* The `VERSION` file creation for Go codebases is removed.
* Read-only mode in tekton-dashboard is now available.
* Cd-pipeline-operator now manages projects instead of namespaces on OpenShift.
* Kiosk integration when using OpenShift cluster is removed.
* The `cd-pipeline-operator` is now responsible for RBAC in the created namespaces.
* The ability to set constant requeue time in GroupMember reconciler is added.
* Add monitoring and logging stack to the helmfile.
* Health check for Headlamp deployment is added.

### Enhancements

* In Headlamp UI, the `Repository URL` field is renamed to 'Forked from' for the cases of using clone strategy.
* Tekton pipelines are now available in Headlamp UI.
* The user is now able to onboard an application with custom framework/language version.
* Empty project button is now hidden if clone/import strategy is used.
* Commit validation functionality is available without JIRA integration.
* Error message of reconciliation is available over status in case of failure in Headlamp UI.
* The update/uninstall buttons when deploy pipeline run is in "running" status have been disabled.
* The `Last time updated` field has been removed from Headlamp codebaseBranch info.
* In the components overview list, icons have been provided for the language/framework/build tool/ci tool elements.
* The Quality Gate Pipeline can now be run by the user only when the health status is green.
* Request-limit resource block is added to Tekton tasks.
* We have made the `awsRegion` parameter optional in order to eliminate the dependency on any specific cloud provider.
* The deployment of EDP has been aligned with the requirements of the OpenShift cluster (OKD 4.10).

### Fixed Issues

* Change icon for docker-registry on overview page.
* From now on, the `Git repo relative path` field is checked for uniqueness when creating application.
* The field `From Commit Hash` in Headlamp is now validated to ensure that the entered commit hash exists so now users can not enter any numbers in the field.
* App list rendering has been optimized.
* Resource details page crashes when resource has no status.

### Documentation

* The [EDP RoadMap](https://epam.github.io/edp-install/roadmap/#roadmap) page is updated.

* The [Use Cases](https://epam.github.io/edp-install/use-cases/) is updated with the following:
  * The [Scaffold and Deploy FastAPI Application](https://epam.github.io/edp-install/use-cases/application-scaffolding/) page is added.
  * The [Deploy Application With Custom Build Tool/Framework](https://epam.github.io/edp-install/use-cases/tekton-custom-pipelines/) page is added.

* The [User Guide](https://epam.github.io/edp-install/user-guide/) is updated with the following:
  * The Headlamp User Guide tab is renamed to [User Guide](https://epam.github.io/edp-install/user-guide/).
  * The [Headlamp OIDC Integration](https://epam.github.io/edp-install/operator-guide/headlamp-oidc/) page is added.
  * The [Add Quality Gate](https://epam.github.io/edp-install/user-guide/add-quality-gate/) page is added.
  * The [Add Application](https://epam.github.io/edp-install/user-guide/add-application/) page is added.

* The [Operator Guide](https://epam.github.io/edp-install/operator-guide/) is updated with the following:
  * The [Report-Portal integration](https://epam.github.io/edp-install/operator-guide/report-portal-integration-tekton/) page is added.
  * The [Tekton Overview](https://epam.github.io/edp-install/operator-guide/tekton-overview/) page is added.
  * The [Upgrade EDP v2.12 to 3.0](https://epam.github.io/edp-install/operator-guide/upgrade-edp-3.0/) page is updated.
  * The [Argo CD Integration](https://epam.github.io/edp-install/operator-guide/argocd-integration/) page is updated.
  * The [Install Amazon EBS CSI Driver](https://epam.github.io/edp-install/operator-guide/ebs-csi-driver/) page is added.
  * The [Migrate CI Pipelines From Jenkins to Tekton](https://epam.github.io/edp-install/operator-guide/migrate-ci-pipelines-from-jenkins-to-tekton/) page is added.
  * The [Install Keycloak](https://epam.github.io/edp-install/operator-guide/install-keycloak/) page is updated.
  * The [Monitoring](https://epam.github.io/edp-install/operator-guide/tekton-monitoring/) page is added.
  * The [EDP Installation Prerequisites Overview](https://epam.github.io/edp-install/operator-guide/prerequisites/) page is updated.
  * The [Set Up Kubernetes](https://epam.github.io/edp-install/operator-guide/kubernetes-cluster-settings/) page is updated.
  * The [Manage Custom Certificates](https://epam.github.io/edp-install/operator-guide/manage-custom-certificate/) page is added.
  * The [Protect Endpoints](https://epam.github.io/edp-install/operator-guide/oauth2-proxy) page is added.

* The [Developer Guide](https://epam.github.io/edp-install/developer-guide/) is updated with the following:
  * The Local Development Guide page is updated and renamed to [Workspace Setup Manual](https://epam.github.io/edp-install/developer-guide/local-development/).

* The [Getting Started](https://epam.github.io/edp-install/overview/) is updated with the following:
  * The [EDP Overview](https://epam.github.io/edp-install/#epam-delivery-platform) page is updated.
  * The [Quick Start](https://epam.github.io/edp-install/getting-started/) page is updated.
  * The [Supported Versions and Compatibility](https://epam.github.io/edp-install/supported-versions/) page is added.

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

* The [Headlamp User Guide](https://epam.github.io/edp-install/user-guide/) is created.

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

## Version 2.12.2 <a name="2.12.2"></a> (February 13, 2023)

### Features

* Gerrit and Jenkins Operators now can manage respective resources through custom URL.
* The basePath key can be indicated in the Gerrit Operator custom resource to form gerritApiUrl.

### Fixed Issues

* Fix Gerrit project syncer and controller conflict in the Gerrit Operator to reduce the delay during the multiple projects sync.

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
