# Components Overview

In this section, we will introduce you to the different types of codebases and strategies for onboarding codebases onto the KubeRocketCI.

## Component and Codebase

From a business perspective, `Components` represent the functional building blocks of software projects. They define the purpose and functionality of different parts of a business application, such as core applications, libraries, automated tests, and infrastructure settings. Components are about what software does and how it aligns with business goals.

From a technical implementation perspective, `Codebases` are the Kubernetes custom resources that manage the technical aspects of these Components. They serve as the bridge between the business logic represented by Components and the underlying Git repositories. Codebases are responsible for the technical implementation, ensuring that the Components are efficiently stored, versioned, and synchronized with the version control system. They represent the state of Components from a technical standpoint.

### Components

Components are the building blocks of software projects. They come in different types, such as `Applications`, `Libraries`, `Autotests`, and `Infrastructure`. Each component type serves a specific purpose in the development process. Applications are the deployable unit of projects, libraries contain reusable code, autotests facilitate automated testing, and infrastructure defines a project's infrastructure settings.

### Codebases

Codebases are Kubernetes custom resources (CR) that represent the state of the components. They are a crucial link between component's state and underlying Git repositories. In essence, each codebase corresponds to a specific component and reflects its current state within a single Git repository. This one-to-one mapping ensures that the component's state is efficiently managed and versioned.

## Types

KubeRocketCI accommodates a variety of codebase types, each serving a specific purpose in the development process. The codebase types available in KubeRocketCI are:

- **Application**: the codebase that contains the source code and manifests of application that can be deployed to Kubernetes clusters. One can use different languages, frameworks, and build tools to develop application.

- **Library**: the codebase that contains reusable code components that can be shared across multiple projects. They are an essential resource for efficient and consistent development.

- **Autotest**: the codebase that facilitates the implementation of automated tests, helping ensure the quality, performance and reliability of applications.

- **Infrastructure**: Infrastructure codebases are used to define and manage the underlying infrastructure of projects using the `Infrastructure as Code` approach, ensuring consistency and reproducibility.

## Onboarding Strategies

The platform supports the following strategies to onboard codebases on the platform:

- **Create** from template - This strategy allows to create a new codebase from a predefined template that matches application language, build tool, and framework. One can choose from a variety of templates that cover different technologies and use cases. This strategy is recommended for projects that start developing their applications from scratch or want to follow the best practices of KubeRocketCI.

- **Import** project - This strategy allows to import an existing codebase from a Git server that is integrated with KubeRocketCI. One can select the Git server and the repository to import, and KubeRocketCI will replicate it to the platform and perform configure. This strategy is suitable for projects that already have a codebase on a Git server and want to leverage the benefits of KubeRocketCI.

- **Clone** project – This strategy allows to clone an existing codebase from any Git repository that is accessible via HTTPS. One can provide the repository URL and KubeRocketCI will clone it to the platform and configure it. This strategy is useful for projects that want to copy a codebase from an external source and customize it for their needs.

## Codebase Operator

The [codebase-operator](https://github.com/epam/edp-codebase-operator) is responsible for creating and managing the codebase custom resource on the  KubeRocketCI. The codebase CR defines the metadata and configuration of the codebase, such as the name, description, type, repository URL, branch, path, CD tool, etc. The codebase-operator watches for changes in the codebase CR and synchronizes them with the corresponding Git repository and KubeRocketCI components. [Learn more](https://github.com/epam/edp-codebase-operator/blob/master/docs/api.md) about the codebase-operator and the custom resource (CR) API.
