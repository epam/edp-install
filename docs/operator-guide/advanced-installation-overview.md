# Advanced Installation Overview

This page serves as a brief overview of all the advanced components within EDP. While these third-party tools are not mandatory, they significantly enhance the platform's capabilities, enabling the creation of a robust CI/CD environment.

## EDP Third-Party Components

Find below the list of the key components used by EPAM Delivery Platform:

|Component|Requirement Level|Cluster|
|:-|:-:|:-|
|[Tekton](install-tekton.md)|Mandatory|:simple-kubernetes:{ .kubernetes } :simple-redhatopenshift:{ .openshift }|
|[Argo CD](install-argocd.md)|Mandatory|:simple-kubernetes:{ .kubernetes } :simple-redhatopenshift:{ .openshift }|
|[NGINX Ingress Controller](install-ingress-nginx.md)[^1]| Mandatory|:simple-kubernetes:{ .kubernetes }|
|[Keycloak](install-keycloak.md)|Optional|:simple-kubernetes:{ .kubernetes } :simple-redhatopenshift:{ .openshift }|
|[DefectDojo](install-defectdojo.md)|Optional|:simple-kubernetes:{ .kubernetes } :simple-redhatopenshift:{ .openshift }|
|[ReportPortal](install-reportportal.md)|Optional|:simple-kubernetes:{ .kubernetes } :simple-redhatopenshift:{ .openshift }|
|[Kiosk](install-kiosk.md)[^2]|Optional|:simple-kubernetes:{ .kubernetes } :simple-redhatopenshift:{ .openshift }|
|[Capsule](capsule.md)[^2]|Optional|:simple-kubernetes:{ .kubernetes } :simple-redhatopenshift:{ .openshift }|
|[External Secrets](install-external-secrets-operator.md)|Optional|:simple-kubernetes:{ .kubernetes } :simple-redhatopenshift:{ .openshift }|
|[Nexus](nexus-sonatype.md)|Optional|:simple-kubernetes:{ .kubernetes } :simple-redhatopenshift:{ .openshift }|
|[Harbor](install-harbor.md)|Optional|:simple-kubernetes:{ .kubernetes }|

Although we have dedicated instructions for all of these third-party tools, for those who installed EDP using cluster add-ons, we recommend installing them via add-ons correspondingly.

[^1]:
    OpenShift cluster uses Routes to provide access to pods from external resources.
[^2]:
    These tools need to be installed in advance before deploying EDP.
