# KubeRocketCI Platform Repository Overview

## Purpose and Concept

The `edp-install` repository serves as the main deployment package for the KubeRocketCI Platform (formerly known as EPAM Delivery Platform or EDP). This repository is designed as a Helm chart wrapper that orchestrates the installation and configuration of all platform components that together form the complete KubeRocketCI ecosystem.

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

The `edp-install` chart integrates various components as dependencies, including:

- **codebase-operator**: Manages application source code repositories
- **cd-pipeline-operator**: Handles continuous delivery pipelines
- **edp-tekton**: Provides CI/CD pipeline functionality based on Tekton
- **edp-headlamp**: Offers a web-based management UI
- **gerrit-operator**: Manages Gerrit code review instances

Each component can be enabled or disabled via Helm values, allowing for a customized platform installation based on specific requirements.
