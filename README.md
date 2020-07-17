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
allowing to define the external pipeline source and then reuse the predefined code from the central storage. The EDP Library 
Pipelines repository contains a structure and the execution subsequence of the stages parameters. 
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
| admin-console | EDP Admin Console user guide with the chapters describing possible actions on its functionality | https://github.com/epmd-edp/admin-console/tree/master#edp-admin-console |
| edp-install | Main overview repository with the respective installation instructions on different platforms | https://github.com/epmd-edp/edp-install/tree/master#how-to-install-edp |
|  |  |  |


----- 
### Related Instructions
* [EDP Installation on Kubernetes](documentation/kubernetes_install_edp.md)
* [EDP Installation on OpenShift](documentation/openshift_install_edp.md)
* [Keycloak Installation on Kubernetes](documentation/kubernetes_install_keycloak.md)
* [Keycloak Installation on OpenShift](documentation/openshift_install_keycloak.md)
* [Google Container Registry Integration](documentation/setup_google_container_registry.md)
* [Replication of Gerrit Development to Gerrit Production](documentation/gerrit_dev_to_prod.md)
* [Add a New Custom Global Pipeline Library](https://github.com/epmd-edp/edp-library-pipelines/blob/master/documentation/add_new_custom_global_pipeline_lib.md#add-a-new-custom-global-pipeline-library)
* [Customize CI Pipeline](https://github.com/epmd-edp/edp-library-pipelines/blob/master/documentation/customize_ci_pipeline.md#customize-ci-pipeline)