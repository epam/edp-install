# Overview

The EPAM Delivery Platform (EDP) Developer Guide serves as a comprehensive technical resource specifically designed for developers. It offers detailed insights into expanding the functionalities of EDP. This section focuses on explaining the development approach and fundamental architectural blueprints that form the basis of the platform's ecosystem.

Within these pages, you'll find architectural diagrams, component schemas, and deployment strategies essential for grasping the structural elements of EDP. These technical illustrations serve as references, providing a detailed understanding of component interactions and deployment methodologies. Understanding the architecture of EDP and integrating third-party solutions into its established framework enables the creation of efficient, scalable, and customizable solutions within the EPAM Delivery Platform.

The diagram below illustrates how GitHub repositories and Docker registries are interconnected within the EDP ecosystem.

![EDP Components Diagram](../assets/developer-guide/architecture/edp-components.inline.svg)

# Release Channels

As a publicly available product, the EPAM Delivery Platform relies on various channels to share information, gather feedback, and distribute new releases effectively. This section outlines the diverse channels through which users can engage with our platform and stay informed about the latest developments and enhancements.

## Marketplaces

Our product is presented on AWS and Civo marketplaces. It's essential to ensure that the product information on these platforms is up-to-date and accurately reflects the latest version of our software:

- [AWS Marketplace](https://aws.amazon.com/marketplace/pp/prodview-u7xcz6pvwwwoa)
- [CIVO Marketplace](https://www.civo.com/marketplace/edp)

## OperatorHub

Our product operators are showcased on OperatorHub, enabling seamless integration and management capabilities:

- [Keycloak Operator](https://operatorhub.io/operator/edp-keycloak-operator)
- [Nexus Operator](https://operatorhub.io/operator/nexus-operator)
- [Sonar Operator](https://operatorhub.io/operator/sonar-operator)

## GitHub Repositories

Our platform components, optional enhancements, add-ons, and deployment resources are hosted on GitHub repositories. Explore the following repositories to access the source code of components.

### Platform Components

Each platform component is available in its corresponding GitHub project:

- [EDP Portal UI](https://github.com/epam/edp-headlamp/releases)
- [Codebase Operator](https://github.com/epam/edp-codebase-operator/releases)
- [Tekton](https://github.com/epam/edp-tekton/releases)
- [CD Pipeline Operator](https://github.com/epam/edp-cd-pipeline-operator/releases)
- [Gerrit Operator](https://github.com/epam/edp-gerrit-operator/releases)
- [EDP Install](https://github.com/epam/edp-install/releases)

### Optional Components

These optional components enhance the platform's installation and configuration experience:

- [Keycloak Operator](https://github.com/epam/edp-keycloak-operator/releases)
- [Nexus Operator](https://github.com/epam/edp-nexus-operator/releases)
- [Sonar Operator](https://github.com/epam/edp-sonar-operator/releases)

### Add-ons Repository

The Add-ons repository provides a streamlined pathway for deploying the all-in-one solution:

- [Cluster Add-Ons](https://github.com/epam/edp-cluster-add-ons)

### Tekton Custom Library

Explore additional tools and customizations in our Tekton Custom Library:

- [Tekton Custom Library](https://github.com/epmd-edp/tekton-custom-library)

### Platform Test Data

Access test data from the 'Create' onboarding strategy:

- [EPMD-EDP Project](https://github.com/epmd-edp)

### Helm Charts

Helm chart artifacts are available in repository:

- [Helm Charts](https://github.com/epam/edp-helm-charts)

## DockerHub

Our DockerHub repository hosts Docker images for various platform components:

- [Keycloak Operator](https://hub.docker.com/repository/docker/epamedp/keycloak-operator/)
- [Nexus Operator](https://hub.docker.com/repository/docker/epamedp/nexus-operator/)
- [Sonar Operator](https://hub.docker.com/repository/docker/epamedp/sonar-operator/)
- [EDP Portal UI](https://hub.docker.com/repository/docker/epamedp/edp-headlamp/)
- [Codebase Operator](https://hub.docker.com/repository/docker/epamedp/codebase-operator/)
- [Tekton](https://hub.docker.com/repository/docker/epamedp/edp-tekton)
- [Tekton Cache](https://hub.docker.com/repository/docker/epamedp/tekton-cache)
- [CD Pipeline Operator](https://hub.docker.com/repository/docker/epamedp/cd-pipeline-operator/)
- [Gerrit Operator](https://hub.docker.com/repository/docker/epamedp/gerrit-operator)

## Social Media

To maintain an active presence on social media channels and share valuable content about our software releases, we continuously publish materials across the following media:

- [SolutionsHub](https://solutionshub.epam.com/solution/epam-delivery-platform)
- [Medium](https://medium.com/epam-delivery-platform)
- [YouTube](https://www.youtube.com/@theplatformteam)
