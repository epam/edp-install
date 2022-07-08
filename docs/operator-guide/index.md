# Overview

The EDP Operator guide is intended for DevOps and provides information on EDP installation, configuration and customization, as well as the platform support. Inspect the documentation to adjust the EPAM Delivery Platform
according to your business needs:


* The **Installation** section provides the prerequisites for EDP installation, including [Kubernetes](kubernetes-cluster-settings.md) or [OpenShift](openshift-cluster-settings.md) cluster setup,
[Keycloak](install-keycloak.md), [Kiosk](install-kiosk.md) and [Ingress-nginx](install-ingress-nginx.md) setup and the subsequent [deployment of EPAM Delivery Platform](install-edp.md).

* The **Configuration** section indicates the options to set the project with [adding a code language](add-other-code-language.md), [backup](restore-edp-with-velero.md), [VCS import strategy](import-strategy.md),
[managing Jenkins pipelines](overview-manage-jenkins-pipelines.md), and [logging](install-loki.md).

* The **Integration** section comprises the [AWS](enable-irsa.md), [GitHub](github-integration.md), [GitLab](gitlab-integration.md), and [Jira](jira-integration.md) integration options.

* The **Tutorials** section provides information on working with various aspects, for example, [deploying AWS EKS cluster](deploy-aws-eks.md), [deploying OKD 4.9 cluster](deploy-okd.md), [managing Jenkins agent](add-jenkins-agent.md), etc.


