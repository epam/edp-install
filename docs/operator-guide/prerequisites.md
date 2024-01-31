# EDP Installation Prerequisites Overview

Before installing EDP:

* Install and configure [Kubernetes](kubernetes-cluster-settings.md) or [OpenShift](openshift-cluster-settings.md) cluster;
* Install EDP components for the selected EDP installation scenario.

## EDP Core Components

EDP installation scenario is based on the Tekton as a CI tool and [EDP Portal](../user-guide/index.md) as a UI tool.

Find below the list of the key components used by EPAM Delivery Platform:

|Component|Requirement level|Cluster|
|:-|:-:|:-|
|[Tekton](install-tekton.md)|Mandatory|:simple-kubernetes:{ .kubernetes } :simple-redhatopenshift:{ .openshift }|
|[Argo CD](install-argocd.md)|Mandatory|:simple-kubernetes:{ .kubernetes } :simple-redhatopenshift:{ .openshift }|
|[NGINX Ingress Controller](install-ingress-nginx.md)[^1]| Mandatory|:simple-kubernetes:{ .kubernetes }|
|[Keycloak](install-keycloak.md)|Optional|:simple-kubernetes:{ .kubernetes } :simple-redhatopenshift:{ .openshift }|
|[DefectDojo](install-defectdojo.md)|Optional|:simple-kubernetes:{ .kubernetes } :simple-redhatopenshift:{ .openshift }|
|[ReportPortal](install-reportportal.md)|Optional|:simple-kubernetes:{ .kubernetes } :simple-redhatopenshift:{ .openshift }|
|[Kiosk](install-kiosk.md)|Optional|:simple-kubernetes:{ .kubernetes } :simple-redhatopenshift:{ .openshift }|
|[External Secrets](install-external-secrets-operator.md)|Optional|:simple-kubernetes:{ .kubernetes } :simple-redhatopenshift:{ .openshift }|
|[Harbor](install-harbor.md)|Optional|:simple-kubernetes:{ .kubernetes }|

[^1]:
    OpenShift cluster uses Routes to provide access to pods from external resources.

!!! note
    Alternatively, use the [Cluster Add-Ons](add-ons-overview.md) approach to install the EDP components.

After setting up the cluster and installing EDP components according to the selected scenario, proceed to the [EDP installation](install-edp.md).

## Related Articles

* [Set Up Kubernetes](kubernetes-cluster-settings.md)
* [Set Up OpenShift](openshift-cluster-settings.md)
* [Install EDP](install-edp.md)
