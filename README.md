# EPAM Delivery Platform

**EPAM Delivery platform (EDP)** is out of the box integrated ecosystem for software development connected to a local development environment. 

EPAM Delivery Platform, which is also called **"The Rocket"**, is a platform that allows shortening the time that is passed before an active development can be started from several months to several hours.

EDP consists of the following:

- The platform based on managed infrastructure and container orchestration;
- Security covering authentication, authorization, and SSO for platform services;
- Development and testing toolset;
- Well-established engineering process and EPAM practices (EngX) reflected in CICD pipelines, and delivery analytics;
- Local development with debug capabilities.

>_**NOTE**: To get accurate information about the EDP architecture, please refer to the 
>[EDP Architecture](https://github.com/epmd-edp/edp-architecture#edp-architecture) page._ 

## EDP Installation
EDP can be installed both on [OpenShift](documentation/openshift_install_edp.md) and [Kubernetes](documentation/kubernetes_install_edp.md) orchestration 
platforms.

## The Admin Console User Interface
The Admin Console management tool allows users to collaborate easily with the environments: add and remove applications, autotests, libraries, CD pipelines, 
branches and much more. To get more accurate information, please check the 
[Admin Console](https://github.com/epmd-edp/admin-console/tree/master#edp-admin-console) user guide.

## EDP Pipeline Framework
The general EDP Pipeline Framework consists of three parts:

1. **Jenkinsfile** - a text file that keeps the definition of a Jenkins Pipeline and is checked into source control. 
Every Job has its Jenkinsfile that is stored in the specific application repository and in Jenkins as the plain text. 

2. **Loading Shared Libraries** - a part where every job loads libraries with the help of the shared libraries 
mechanism for Jenkins that allows to create reproducible pipelines, write them uniformly, and manage the update process. 
There are two main libraries: EDP Library Pipelines with the common logic described for the main pipelines Code Review, 
Build, Deploy pipelines and EDP Library Stages that keeps the description of the stages for every pipeline.

3. **Run Stages** - a part where the predefined default stages are launched. 

The main conception is realized on the [Jenkins Shared Libraries](https://www.jenkins.io/doc/book/pipeline/shared-libraries/) 
allowing to define the external pipeline source and then reuse the predefined code from the central storage. The [EDP Library 
Pipelines](https://github.com/epmd-edp/edp-library-pipelines#edp-library-pipelines-overview) repository contains a structure and the execution subsequence of the stages parameters. 
The EDP Library Stages repository describes the specific steps and their realization in frames of a specific pipeline.

If EDP pipelines are not enough for the CICD needs, it is possible to add a custom stage. To do this, a user creates the stage, 
adds it to the application repository, thus extending the EDP Pipelines Framework by customization, 
realization, and redefinition of the user stages. In such a case, the priority goes to the user stages.

## EDP Repositories and Documentation 
EDP consists of the components that are presented as repositories on the [EDP GitHub](https://github.com/epmd-edp) page. 
To find the necessary documentation at once, firstly, inspect the Table 1 listing the EDP main repositories and stored documentation. 

Secondly, pay attention that every repository has the overview page (_the README file_) and the _Related Articles_ block. 
This block contains the links to the necessary instructions main of which are located in the documentation folder of every repository. 

_Table 1. EDP Main Repositories and Documentation._

| Repository | Documentation | Link |
|---|---|---|
| admin-console | EDP Admin Console user guide with the chapters describing possible actions on its functionality. In addition, the GitLab and GitHub integration, VCS integration, adding of other code language, adjustment of import strategy, integration with Jira server instructions. | https://github.com/epmd-edp/admin-console/tree/master#edp-admin-console |
| admin-console-operator | The operator overview page with the corresponding description, installation, local development, and architecture scheme. | https://github.com/epmd-edp/admin-console-operator/tree/master#admin-console-operator |
| edp-install | Main overview repository with the respective installation instructions on different platforms. | https://github.com/epmd-edp/edp-install/tree/master#epam-delivery-platform |
| edp-architecture | The general architecture of EPAM Delivery Platform. | https://github.com/epmd-edp/edp-architecture#edp-architecture |
| codebase-operator | The operator overview page with the corresponding description, installation, local development, and architecture scheme. In addition, the Jira Fix Version, Jira Server, Git Server, Codebase, Codebase Branch controllers overview and schemes. | https://github.com/epmd-edp/codebase-operator/tree/master#codebase-operator |
| edp-component-operator | The operator overview page with the corresponding description, installation, and local development. | https://github.com/epmd-edp/edp-component-operator/tree/master#edp-component-operator |
| cd-pipeline-operator | The operator overview page with the corresponding description, installation, local development, and architecture scheme. | https://github.com/epmd-edp/cd-pipeline-operator/tree/master#cd-pipeline-operator |
| edp-library-pipelines | The operator overview page with the corresponding description. In addition, the CI pipeline customization and adding of a custom pipeline library instructions. | https://github.com/epmd-edp/edp-library-pipelines/blob/master/README.md#edp-library-pipelines |
| edp-library-stages | The operator overview page with the corresponding description. | https://github.com/epmd-edp/edp-library-stages |
| gerrit-operator | The operator overview page with the corresponding description, installation, local development, and architecture scheme. In addition, the Gerrit replication to GitLab and migration of Gerrit Development to Gerrit Production instructions. | https://github.com/epmd-edp/gerrit-operator/tree/master#gerrit-operator |
| jenkins-operator | The operator overview page with the corresponding description, installation, local development, and architecture scheme. In addition, the adding of Jenkins slave and job provision. | https://github.com/epmd-edp/jenkins-operator/tree/master#jenkins-operator |
| keycloak-operator | The operator overview page with the corresponding description, installation, local development, and architecture scheme. | https://github.com/epmd-edp/keycloak-operator/tree/master#keycloak-operator |
| nexus-operator | The operator overview page with the corresponding description, installation, local development, and architecture scheme. | https://github.com/epmd-edp/nexus-operator/tree/master#nexus-operator |
| reconciler | The operator overview page with the corresponding description, installation, and local development. | https://github.com/epmd-edp/reconciler/tree/master#reconciler-operator |
| sonar-operator | The operator overview page with the corresponding description, installation, local development, and architecture scheme. | https://github.com/epmd-edp/sonar-operator/tree/master#sonar-operator |


### Related Articles
* [EDP Installation on Kubernetes](documentation/kubernetes_install_edp.md)
* [EDP Installation on OpenShift](documentation/openshift_install_edp.md)
* [Keycloak Installation on Kubernetes](documentation/kubernetes_install_keycloak.md)
* [Keycloak Installation on OpenShift](documentation/openshift_install_keycloak.md)
* [Google Container Registry Integration](documentation/setup_google_container_registry.md)
* [Replication of Gerrit Development to Gerrit Production](documentation/gerrit_dev_to_prod.md)
* [Add a New Custom Global Pipeline Library](https://github.com/epmd-edp/edp-library-pipelines/blob/master/documentation/add_new_custom_global_pipeline_lib.md#add-a-new-custom-global-pipeline-library)
* [Customize CI Pipeline](https://github.com/epmd-edp/edp-library-pipelines/blob/master/documentation/customize_ci_pipeline.md#customize-ci-pipeline)