# KubeRocketCI Platform Repository Overview

## Purpose and Concept

The `edp-install` repository serves as the main deployment package for the KubeRocketCI Platform (formerly known as EPAM Delivery Platform or EDP). This repository is designed as a Helm chart wrapper that orchestrates the installation and configuration of all platform components that together form the complete KubeRocketCI ecosystem.

### How It Works

The edp-install repository functions as a centralized Helm chart that:

1. **Declares Dependencies**: Defines all platform components as Helm chart dependencies in `Chart.yaml`
2. **Provides Configuration**: Contains default values for all components in `values.yaml`
3. **Coordinates Installation**: Ensures proper installation order and relationships between components
4. **Customizes Deployment**: Allows for fine-grained control over which components are enabled and how they're configured
5. **Manages Resources**: Creates platform-level resources such as RBAC rules, QuickLinks, and integration points

When deployed, the chart installs all enabled components and their required resources, configuring them to work together as a cohesive platform.

## Core Principles

- **Component Orchestration**: The repository doesn't contain the actual implementation of platform components but rather acts as a coordinator that brings together all necessary dependencies.
- **Modular Architecture**: Each platform capability is implemented as a separate operator/component, linked as a Helm chart dependency.
- **Customizable Deployment**: The chart allows flexible configuration of the platform through values files.
- **Enterprise-Grade CI/CD**: KubeRocketCI provides a comprehensive set of tools for the entire software delivery lifecycle.

## Platform Description

KubeRocketCI is an enterprise-grade CI/CD platform built on Kubernetes that provides:

- Software development workflow automation
- Automated application deployment
- Multi-tenancy support
- GitOps workflows
- Secured CI/CD pipelines
- Compliance with industry standards

The platform leverages Tekton for CI/CD pipelines, integrates with various source control systems, and provides extensive observability and security features.

## Technical Foundation

KubeRocketCI is built on the following principles:

- **Kubernetes-Native**: Fully leverages Kubernetes Custom Resources and Operators
- **GitOps-First**: Follows GitOps principles for deployment and configuration
- **Operator Pattern**: Uses Kubernetes operators for managing platform components
- **Helm-Based Deployment**: Utilizes Helm charts for packaging and deployment
- **Extensibility**: Provides mechanisms for extending platform functionality
- **Multi-Tenancy**: Offers isolation between different teams and applications

## Component Integration

The `edp-install` chart integrates various components as dependencies, each serving a specific purpose within the platform ecosystem:

### codebase-operator

The `codebase-operator` is responsible for managing application components within KubeRocketCI:

- **Purpose**: Handles the provisioning and lifecycle of application components in the platform
- **Functionality**:
  - Creates and manages Git repositories when users add existing repos or create new ones
  - Manages branch creation and synchronization between the platform and Git
  - Handles component metadata and configuration
  - Manages integration with Git providers (GitHub, GitLab, Bitbucket, etc.)
- **Custom Resources**: 
  - `Codebase`: Represents an application component in the platform
  - `CodebaseBranch`: Represents a branch of a component
  - `CodebaseImageStream`: Collects available component tags that can be deployed
  - `GitServer`: Defines Git provider connections
  - `JiraIssueMetadata`: Manages Jira issue integration
  - `JiraServer`: Defines Jira server connections
  - `QuickLink`: Creates UI navigation links
  - `Template`: Defines templates that can be used for creating new components

### cd-pipeline-operator

The `cd-pipeline-operator` manages continuous delivery processes:

- **Purpose**: Orchestrates the deployment pipelines and environments for applications
- **Functionality**:
  - Creates and manages deployment pipelines (Deployment Flows)
  - Handles environment (Stages) creation and configuration
  - Generates ArgoCD ApplicationSet resources for GitOps deployments
  - Manages parameter passing and version selection during deployments
  - Orchestrates automatic or manual pipeline executions
- **Custom Resources**:
  - `CDPipeline`: Defines a Deployment Flow
  - `Stage`: Defines an Environment within a Deployment Flow

### edp-tekton

The `edp-tekton` component provides the core CI/CD capabilities using Tekton:

- **Purpose**: Provides the core CI/CD functionality through Tekton resources
- **Functionality**:
  - Deploys the KubeRocketCI interceptor for extracting metadata from Git events
  - Provides a library of pre-defined Tekton tasks and pipelines
  - Includes trigger templates for various CI/CD events
  - Configures default pipeline resources for common development workflows
  - Supplies building blocks for custom CI/CD pipelines
- **Resources**: Various Tekton CRDs including Tasks, Pipelines, TriggerTemplates, and more

### edp-headlamp

This component provides the web interface for the platform:

- **Purpose**: Offers a user-friendly UI for platform interaction
- **Functionality**:
  - Displays platform components and their status
  - Provides management interfaces for pipelines and deployments
  - Visualizes CI/CD execution and results
  - Offers access to platform configuration
  - Provides Kubernetes mode for direct cluster interaction
- **Based on**: Headlamp, a Kubernetes web UI framework
- **Features**: Custom plugins and extensions for KubeRocketCI-specific functionality

### gerrit-operator

An optional component for Gerrit integration:

- **Purpose**: Manages Gerrit servers and related resources
- **Functionality**:
  - Deploys and configures Gerrit servers
  - Manages Gerrit projects, groups, and access
  - Handles replication configuration
  - Processes merge requests
- **Custom Resources**:
  - `Gerrit`: Represents a Gerrit server instance
  - `GerritGroup`: Manages user groups in Gerrit
  - `GerritGroupMember`: Defines group memberships
  - `GerritMergeRequest`: Handles code merge operations
  - `GerritProject`: Manages Gerrit projects
  - `GerritProjectAccess`: Controls access rights to projects
  - `GerritReplicationConfig`: Configures Git replication
- **Note**: This component is disabled by default

## Dependency Configuration

### Accessing Default Values

The default values for each dependency can be viewed in two main ways:

1. **GitHub Repositories**: Each component has its own repository with documentation:

    - [edp-codebase-operator](https://github.com/epam/edp-codebase-operator)
    - [edp-cd-pipeline-operator](https://github.com/epam/edp-cd-pipeline-operator)
    - [edp-tekton](https://github.com/epam/edp-tekton)
    - [edp-headlamp](https://github.com/epam/edp-headlamp)
    - [edp-gerrit-operator](https://github.com/epam/edp-gerrit-operator)

2. **Helm Commands**: Use Helm to display the values schema:

    ```bash
    helm show values epamedp/<dependency_name> --version <version>
    ```

### Overriding Dependency Values

To customize the behavior of platform components, you can override their default values in the `values.yaml` file of the `edp-install` chart. Follow these steps:

1. **Enable the Component**: First, ensure the component is enabled:

    ```yaml
    <dependency-name>:
      enabled: true
    ```

2. **Add Custom Values**: Under the same section, add any configuration values you want to override:

    ```yaml
    <dependency-name>:
      enabled: true
      # Basic configuration
      key1: "custom-value1"
      key2: "custom-value2"

      # Nested configuration
      nestedConfig:
        setting1: "custom-setting1"
        setting2: "custom-setting2"
    ```

3. **Apply Selectively**: You only need to specify the values you want to change - other values will use their defaults

4. **Use Proper Structure**: Follow the exact structure from the component's values schema

5. **Global vs. Component-Specific**: Some values may be configured globally or at the component level - component-specific settings take precedence

This approach allows for granular control over each component while maintaining a centralized configuration.

## Deployment Methods

The `edp-install` Helm chart can be deployed using several methods, depending on your preferences and infrastructure requirements:

### Method 1: Direct Helm Installation from Repository

This method uses the official Helm repository to deploy the platform:

1. **Add the repository**:

    ```bash
    helm repo add epamedp https://epam.github.io/edp-helm-charts/stable
    ```

2. **Find the desired version**:

    ```bash
    helm search repo epamedp/edp-install
    ```

3. **Deploy with custom values**:

    ```bash
    helm install krci epamedp/edp-install --wait --timeout=900s \
    --version 3.12.0 \
    --values values.yaml \
    --namespace krci \
    --create-namespace
    ```

### Method 2: Local Chart Deployment

This method involves cloning the repository (or downloading the chart) and deploying it locally:

1. **Build dependencies**:

    ```bash
    helm dependency build
    ```

2. **Install from local directory**:

    ```bash
    helm install krci . --wait --timeout=900s \
    --version 3.12.0 \
    --values values.yaml \
    --namespace krci \
    --create-namespace
    ```

### Method 3: Cluster Add-Ons Repository

KubeRocketCI can also be deployed using the `edp-cluster-add-ons` repository:

1. **Clone the forked repository**:

    ```bash
    git clone git@github.com:<account_name>/edp-cluster-add-ons.git
    ```

2. **Configure values**:

    Navigate to `clusters/core/addons/kuberocketci` directory and modify the `values.yaml` file as needed.

3. **Deploy using Argo CD or Helm**:

    Deploy using your preferred method - either through Argo CD or manually with Helm commands

### Method 4: Cloud Marketplace Deployments

KubeRocketCI is also available through cloud marketplaces:

- **AWS Marketplace**: For AWS-specific deployment options
- **CIVO Marketplace**: For CIVO cloud deployments

For detailed instructions on marketplace deployments, refer to the [official KubeRocketCI documentation](https://docs.kuberocketci.io/).
