# EPAM Delivery Platform

**EPAM Delivery platform (EDP)** is out of the box integrated ecosystem for software development connected to a local development environment.

EPAM Delivery Platform, which is also called **"The Rocket"**, is a platform that allows shortening the time that is passed before an active development can be started from several months to several hours.

EDP consists of the following:

- The platform based on managed infrastructure and container orchestration;
- Security covering authentication, authorization, and SSO for platform services;
- Development and testing toolset;
- Well-established engineering process and EPAM practices (EngX) reflected in CICD pipelines, and delivery analytics;
- Local development with debug capabilities.

## Features

## Admin Console UI

Admin Console is a central management tool in the EDP ecosystem that provides the ability to define pipelines, project resources and new technologies in a simple way. Using Admin Console enables to manage business entities:

- Create Codebases as Applications, Libraries and Autotests;
- Create/Update CD Pipelines;

!!! note
    To interact with Admin Console via REST API, explore the [Create Codebase Entity](developer-guide/rest-api.md) page.

![overview-page](./assets/ac_overview_page.png "overview-page")

- **Navigation bar** – consists of seven sections: Overview, Continuous Delivery, Applications, Autotests, Libraries, and Delivery Dashboard Diagram. Click the necessary section to add an entity, open a home page or check the diagram.
- **User name** – displays the registered user name.
- **Main links** – displays the corresponding links to the major adjusted toolset, to the management tool and to the OpenShift cluster.

Admin Console is a complete tool allowing to manage and control added to the environment codebases (applications, autotests, libraries) as well as to create a CD pipeline and check the visualization diagram.
Inspect the main features available in Admin Console by following the corresponding link:

- [Add Application](user-guide/add-application.md)
- [Add Autotest](user-guide/add-autotest.md)
- [Add Library](user-guide/add-library.md)
- [Add CD Pipeline](user-guide/add-cd-pipeline.md)
- [Delivery Dashboard Diagram](user-guide/d-d-diagram.md)

## Architecture

EPAM Delivery Platform (EDP) is suitable for all aspects of delivery starting from development including the capability to deploy production environment.
EDP architecture is represented on a diagram below.

![high_level_arch_diagram](./assets/high_level_arch_diagram.png "high_level_arch_diagram")

EDP consists of three cross-cutting concerns:

1. Infrastructure as a Service;
2. Container orchestration and centralized services;
3. Security.

On the top of these indicated concerns, EDP adds several blocks that include:

- **EDP CICD Components**. EDP component enables a feature in CICD or an instance artifacts storage and distribution (Nexus or Artifactory), static code analysis (Sonar), etc.;
- **EDP Artifacts**. This element represents an artifact that is being delivered through EDP and presented as a code.

    >_Artifact samples: frontend, backend, mobile, applications, functional and non-functional autotests, workloads for 3rd party components that can be deployed together with applications._

- **EDP development and production environments** that share the same logic. Environments wrap a set of artifacts with a specific version, and allow performing SDLC routines in order to be sure of the artifacts quality;
- **Pipelines**. Pipelines cover CICD process, production rollout and updates. They also connect three elements indicated above via automation allowing SDLC routines to be non-human;

### Technology Stack

Explore the EDP technology stack diagram

![edp_technology_stack](./assets/edp_technology_stack.png "edp_technology_stack")

The EDP IaaS layer supports most popular public clouds AWS, Azure and GCP keeping the capability to be deployed on private/hybrid clouds based on OpenStack.
EDP containers are based on [Docker technology](https://www.docker.com/), orchestrated by Kubernetes compatible solutions.

There are two main options for Kubernetes provided by EDP:

- Managed Kubernetes in Public Clouds to avoid installation and management of Kubernetes cluster, and get all benefits of scaling, reliability of this solution;
- OpenShift that is a Platform as a Service on the top of Kubernetes from Red Hat. OpenShift is the default option for on-premise installation and it can be considered whether the _solution built on the top of EDP_ should be **cloud-agnostic** or require **enterprise support**;

There is no limitation to run EDP on vanilla Kubernetes.

!!! note
    To get accurate information about EDP architecture, please refer to the
    [EDP Architecture](https://github.com/epam/edp-architecture#edp-architecture) page.
