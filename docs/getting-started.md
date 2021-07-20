---
hide:
  - navigation
---

# Getting Started

## Requirements

- Kubernetes cluster 1.18+, or OpenShift 4.6+
- [kubectl](https://kubernetes.io/docs/tasks/tools/){target=_blank} tool
- [helm 3.5.x+](https://helm.sh/docs/intro/install/){target=_blank}
- [Keycloak 11.0+](https://www.keycloak.org){target=_blank}
- [Kiosk v0.2.9](https://github.com/loft-sh/kiosk){target=_blank}
- [Amazon EKS Pod Identity Webhook](https://github.com/aws/amazon-eks-pod-identity-webhook){target=_blank} in case of using [AWS ECR](https://aws.amazon.com/ecr/){target=_blank} as Docker Registry

### Hardware

Minimal:

- CPU: 4 Core
- Memory: 16 Gb

## EDP Toolset

List of Tools used on the Platform:

|Domain|Related Tools/Solutions|
|- |- |
|Artefacts Management|Nexus Repository, AWS ECR|
|AWS|Amazon EKS Pod Identity Webhook, AWS ECR, AWS EFS|
|Build|.NET, Go, Apache Gradle, Apache Maven, NPM|
|Cluster Backup|Velero|
|Code Review|Gerrit, GitLab, GitHub |
|Docker|Hadolint, kaniko, crane|
|Infrastructure as Code|Terraform, TFLint|
|Kubernetes deployment|kubectl, helm, ct (Chart Testing)|
|Kubernetes Multitenancy|Kiosk|
|Logging|EFK, ELK, Loki|
|Monitoring|Prometheus, Grafana|
|Pipeline Orchestration|Jenkins, GitLab CI (basic)|
|Policies/Rules|Open Policy Agent|
|SSO|Keycloak, keycloak-proxy|
|Static Code Analysis|SonarQube|
|Test Report Tool|Allure|

## Install prerequisites

## Install EDP
