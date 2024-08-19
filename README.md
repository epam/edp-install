# KubeRocketCI Platform :rocket:

 | :heavy_exclamation_mark: Please refer to [KRCI documentation](https://docs.kuberocketci.io/) to get the notion of the main concepts and guidelines. |
 |-----------------------------------------------------------------------------------------------------------------------------------------------------------|

**KubeRocketCI (KRCI)** is out of the box integrated ecosystem for software development connected to a local development environment.

KubeRocketCI, which is also called **"The Rocket"**, is a platform that allows shortening the time that is passed before an active development can be started from several months to several hours.

The platform consists of the following:

- The platform based on managed infrastructure and container orchestration;
- Security covering authentication, authorization, and SSO for platform services;
- Development and testing toolset;
- Well-established engineering process and EPAM practices (EngX) reflected in CI/CD pipelines, and delivery analytics;
- Local development with debug capabilities;
- A set of pre-configured pipelines for different types of applications (polyglot microservices);
- Observability stack.

## KubeRocketCI Installation

KubeRocketCI can be installed both on OpenShift and Kubernetes orchestration platforms. Please refer to the [Install KubeRocketCI](https://docs.kuberocketci.io/docs/operator-guide/install-kuberocketci) section of the Operator Guide
for details and info on prerequisites.

>_**NOTE**: To get the notion of the most useful KubeRocketCI terms, please refer to the [KRCI Glossary](https://docs.kuberocketci.io/docs/glossary) page._

## KubeRocketCI Repositories Description

KubeRocketCI consists of the components that are presented as repositories. To find the necessary repository and get more details about its deployment and scheme, please refer to the Table 1.

_Table 1. KRCI Main Repositories._

| Repository             | Description                                                                                                              | Link                                                                                     |
|------------------------|--------------------------------------------------------------------------------------------------------------------------|------------------------------------------------------------------------------------------|
| edp-install            | Main overview repository.                                                                                                | https://github.com/epam/edp-install/tree/master#epam-delivery-platform                   |
| codebase-operator      | The operator overview page with the corresponding description, installation, local development, and architecture scheme. In addition, the Jira Fix Version, Jira Server, Git Server, Codebase, Codebase Branch controllers overview and schemes. | https://github.com/epam/edp-codebase-operator/tree/master#codebase-operator |
| cd-pipeline-operator   | The operator overview page with the corresponding description, installation, local development, and architecture scheme. | https://github.com/epam/edp-cd-pipeline-operator/tree/master#cd-pipeline-operator        |
| gerrit-operator        | The operator overview page with the corresponding description, installation, local development, and architecture scheme. | https://github.com/epam/edp-gerrit-operator/tree/master#gerrit-operator                  |
| keycloak-operator      | The operator overview page with the corresponding description, installation, local development, and architecture scheme. | https://github.com/epam/edp-keycloak-operator/tree/master#keycloak-operator              |
| nexus-operator         | The operator overview page with the corresponding description, installation, local development, and architecture scheme. | https://github.com/epam/edp-nexus-operator/tree/master#nexus-operator                    |
| sonar-operator         | The operator overview page with the corresponding description, installation, local development, and architecture scheme. | https://github.com/epam/edp-sonar-operator/tree/master#sonar-operator                    |
| edp-headlamp           | The operator overview page with the corresponding description, assets, and local development.                            | https://github.com/epam/edp-headlamp/tree/master#edp-headlamp                            |
| edp-tekton             | The operator overview page with the description of its two main components: EDP Interceptor and Tekton Pipelines.        | https://github.com/epam/edp-tekton/tree/master#edp-tekton                                |
