# Changelog

## Overview

Get acquainted with the latest EDP releases.

*   [Version 2.8.0](#2.8.0)
*   [Version 2.7.8](#2.7.8)
*   [Version 2.7.7](#2.7.7)
*   [Version 2.7.6](#2.7.6)
*   [Version 2.7.5](#2.7.5)
*   [Version 2.7.4](#2.7.4)
*   [Version 2.7.2](#2.7.2)
*   [Version 2.7.1](#2.7.1)
*   [Version 2.7.0](#2.7.0)
*   [Version 2.6.4](#2.6.4)
*   [Version 2.6.3](#2.6.3)
*   [Version 2.6.2](#2.6.2)
*   [Version 2.6.1](#2.6.1)
*   [Version 2.6.0](#2.6.0)

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

* The [EDP Components List](https://github.com/epam/edp-architecture/blob/master/documentation/edp_components.md#edp-components-list) page is added.
* The [EDP Installation on Kubernetes](https://github.com/epam/edp-install/blob/master/documentation/kubernetes_install_edp.md) page is updated.
* The [EDP Installation on OpenShift](https://github.com/epam/edp-install/blob/master/documentation/openshift_install_edp.md) page is updated.
* The [EDP Overview](https://github.com/epam/edp-install/tree/master#epam-delivery-platform-rocket) page is updated.
* The [EDP Pipeline Framework](https://github.com/epam/edp-admin-console/blob/master/documentation/cicd_customization/edp_pipeline_framework.md#edp-pipeline-framework) page is added.
* The [Keycloak Installation on Kubernetes](https://github.com/epam/edp-install/blob/master/documentation/install_keycloak.md) page is updated.

