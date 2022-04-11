# EPAM Delivery Platform :rocket:

 | :heavy_exclamation_mark: Please refer to [EDP documentation](https://epam.github.io/edp-install/) to get the notion of the main concepts and guidelines. |
 | --- |

**EPAM Delivery Platform (EDP)** is out of the box integrated ecosystem for software development connected to a local development environment.

EPAM Delivery Platform, which is also called **"The Rocket"**, is a platform that allows shortening the time that is passed before an active development can be started from several months to several hours.

EDP consists of the following:

- The platform based on managed infrastructure and container orchestration;
- Security covering authentication, authorization, and SSO for platform services;
- Development and testing toolset;
- Well-established engineering process and EPAM practices (EngX) reflected in CI/CD pipelines, and delivery analytics;
- Local development with debug capabilities.

>_**NOTE**: To get accurate information about the EDP architecture, please refer to the
>[EDP Architecture](https://github.com/epam/edp-architecture#edp-architecture) page._

## EDP Installation
EDP can be installed both on OpenShift and Kubernetes orchestration platforms. Please refer to the [Install EDP](https://epam.github.io/edp-install/operator-guide/install-edp/) section of the Operator Guide
for details and info on prerequisites.

>_**NOTE**: To get the notion of the most useful EDP terms, please refer to the [EDP Glossary](https://epam.github.io/edp-install/glossary/) page._

## The Admin Console User Interface
The Admin Console management tool allows users to collaborate easily with the environments: add and remove applications, autotests, libraries, CD pipelines,
branches and much more. To get more accurate information, please check the
[Admin Console](https://epam.github.io/edp-install/user-guide/) user guide.

## EDP Pipeline Framework
The general EDP Pipeline Framework consists of three parts:

1. **Jenkinsfile** - a text file that keeps the definition of a Jenkins Pipeline and is checked into source control.
Every Job has its Jenkinsfile that is stored in the specific application repository and in Jenkins as the plain text.

2. **Loading Shared Libraries** - a part where every job loads libraries with the help of the shared libraries
mechanism for Jenkins that allows to create reproducible pipelines, write them uniformly, and manage the update process.
There are two main libraries: EDP Library Pipelines with the common logic described for the main pipelines (Code Review,
Build, Deploy pipelines) and EDP Library Stages that keeps the description of the stages for every pipeline.

3. **Run Stages** - a part where the predefined default stages are launched.

The main conception is realized on the [Jenkins Shared Libraries](https://www.jenkins.io/doc/book/pipeline/shared-libraries/)
allowing to define the external pipeline source and then reuse the predefined code from the central storage. The [EDP Library Pipelines](https://github.com/epam/edp-library-pipelines#edp-library-pipelines-overview) repository contains a structure and the execution subsequence of the stages parameters.
The EDP Library Stages repository describes the specific steps and their realization in frames of a specific pipeline.

If EDP pipelines are not enough for the CI/CD needs, it is possible to add a custom stage. To do this, a user creates the stage,
adds it to the application repository, thus extending the EDP Pipelines Framework by customization,
realization, and redefinition of the user stages. In such a case, the priority goes to the user stages.

>_**NOTE**: For detailed information about the pipelines and stages, please check out the [EDP Pipeline Framework](https://epam.github.io/edp-install/user-guide/pipeline-framework/) page._

## EDP Repositories Description
EDP consists of the components that are presented as repositories. To find the necessary repository and get more details about its deployment and scheme, please refer to the Table 1.

_Table 1. EDP Main Repositories._

| Repository | Description | Link |
|---|---|---|
| admin-console | EDP Admin Console overview page with the local development info. | https://github.com/epam/edp-admin-console/tree/master#edp-admin-console |
| admin-console-operator | The operator overview page with the corresponding description, installation, local development, and architecture scheme. | https://github.com/epam/edp-admin-console-operator/tree/master#admin-console-operator |
| edp-install | Main overview repository. | https://github.com/epam/edp-install/tree/master#epam-delivery-platform |
| edp-architecture | The general **architecture** of EPAM Delivery Platform. | https://github.com/epam/edp-architecture#edp-architecture |
| codebase-operator | The operator overview page with the corresponding description, installation, local development, and architecture scheme. In addition, the Jira Fix Version, Jira Server, Git Server, Codebase, Codebase Branch controllers overview and schemes. | https://github.com/epam/edp-codebase-operator/tree/master#codebase-operator |
| edp-component-operator | The operator overview page with the corresponding description, installation, and local development. | https://github.com/epam/edp-component-operator/tree/master#edp-component-operator |
| cd-pipeline-operator | The operator overview page with the corresponding description, installation, local development, and architecture scheme. | https://github.com/epam/edp-cd-pipeline-operator/tree/master#cd-pipeline-operator |
| edp-library-pipelines | The operator overview page with the corresponding description. | https://github.com/epam/edp-library-pipelines/blob/master/README.md#edp-library-pipelines |
| edp-library-stages | The operator overview page with the corresponding description. | https://github.com/epam/edp-library-stages |
| gerrit-operator | The operator overview page with the corresponding description, installation, local development, and architecture scheme. | https://github.com/epam/edp-gerrit-operator/tree/master#gerrit-operator |
| jenkins-operator | The operator overview page with the corresponding description, installation, local development, and architecture scheme. | https://github.com/epam/edp-jenkins-operator/tree/master#jenkins-operator |
| keycloak-operator | The operator overview page with the corresponding description, installation, local development, and architecture scheme. | https://github.com/epam/edp-keycloak-operator/tree/master#keycloak-operator |
| nexus-operator | The operator overview page with the corresponding description, installation, local development, and architecture scheme. | https://github.com/epam/edp-nexus-operator/tree/master#nexus-operator |
| reconciler | The operator overview page with the corresponding description, installation, and local development. | https://github.com/epam/edp-reconciler/tree/master#reconciler-operator |
| sonar-operator | The operator overview page with the corresponding description, installation, local development, and architecture scheme. | https://github.com/epam/edp-sonar-operator/tree/master#sonar-operator |
| perf-operator | The operator overview page with the corresponding description, installation, local development, and architecture scheme. | https://github.com/epam/edp-perf-operator/tree/master#perf-operator |

