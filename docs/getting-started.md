# Quick Start

## Software Requirements

- Kubernetes cluster 1.23+ or OpenShift 4.9+;
- [Kubectl](https://kubernetes.io/docs/tasks/tools/){target=_blank} tool;
- [Helm 3.10.x+](https://helm.sh/docs/intro/install/){target=_blank};
- [Keycloak 18.0+](https://www.keycloak.org){target=_blank};
- [Kiosk 0.2.11](https://github.com/loft-sh/kiosk){target=_blank}.

## Minimal Hardware Requirements

The system should have the following specifications to run properly:

- CPU: 8 Core
- Memory: 32 Gb

## EDP Toolset

EPAM Delivery Platform supports the following tools:

|Domain|Related Tools/Solutions|
|- |- |
|Artifacts Management|Nexus Repository, Jfrog Artifactory|
|AWS|IRSA, AWS ECR, AWS EFS, Parameter Store, S3, ALB/NLB, Route53|
|Build|.NET, Go, Apache Gradle, Apache Maven, NPM|
|Cluster Backup|Velero|
|Code Review|Gerrit, GitLab, GitHub|
|Container Registry|AWS ECR, OpenShift Registry, Harbor, DockerHub|
|Containers|Hadolint, Kaniko, Crane|
|Documentation as Code|MkDocs, Antora (AsciiDoc)|
|Infrastructure as Code|Terraform, TFLint, Terraform Docs, Crossplane, AWS Controllers for Kubernetes|
|Kubernetes Deployment|Kubectl, Helm, Helm Docs, Chart Testing, Argo CD, Argo Rollout|
|Kubernetes Multitenancy|Kiosk|
|Logging|OpenSearch, EFK, ELK, Loki, Splunk|
|Monitoring|Prometheus, Grafana, VictoriaMetrics|
|Pipeline Orchestration|Tekton, Jenkins|
|Policies/Rules|Open Policy Agent|
|Secrets Management|External Secret Operator, Vault|
|Secure Development|SonarQube, DefectDojo, Dependency Track,  Semgrep, Grype, Trivy, Clair, GitLeaks, CycloneDX Generator, tfsec, checkov|
|SSO|Keycloak, oauth2-proxy|
|Test Report Tool|ReportPortal, Allure|
|Tracing|OpenTelemetry, Jaeger|

## Install EDP

To install EDP with the necessary parameters, please refer to the [Install EDP](./operator-guide/install-edp.md) section of the [Operator Guide](https://epam.github.io/edp-install/operator-guide/).
Mind the parameters in the EDP installation chart. For details, please refer to the [values.yaml](https://github.com/epam/edp-install/blob/master/deploy-templates/values.yaml).

Find below the example of the installation command:

        helm install edp epamedp/edp-install --wait --timeout=900s \
        --set global.dnsWildCard=<cluster_DNS_wilcdard> \
        --set global.platform=<platform_type> \
        --set awsRegion=<region> \
        --namespace edp

!!! warning
    Please be aware that the command above is an example.

## Related Articles
[Getting Started](overview.md)
