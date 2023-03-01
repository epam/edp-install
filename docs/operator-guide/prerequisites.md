# EDP Installation Prerequisites Overview

Before installing EDP:

* Install and configure [Kubernetes](kubernetes-cluster-settings.md) or [OpenShift](openshift-cluster-settings.md) cluster.
* Install EDP components for the selected EDP installation scenario.

## EDP Installation Scenarios

There are two EDP installation scenarios based on the selected CI tool: Tekton (default) or Jenkins.

**Scenario 1: Tekton CI tool.** By default, EDP uses Tekton as a CI tool and [EDP Headlamp](../headlamp-user-guide/index.md) as a UI tool.

**Scenario 2: Jenkins CI tool.** To use Jenkins as a CI tool, it is required to install the deprecated Admin Console UI tool. Admin Console is used only as a dependency for Jenkins, and Headlamp will still be used as a UI tool.

!!! note
    Starting from version 3.0.0, all the new enhancements and functionalities will be introduced only for *Tekton deploy scenario*. Jenkins deploy scenario will be supported at the bug fix and security breach level only. We understand that some users may need additional functionality in Jenkins, so if any, please create your request [here](https://github.com/epam/edp-jenkins-operator/issues/new). To stay up-to-date with all the updates, please check the [Release Notes](https://github.com/epam/edp-install/blob/master/RELEASES.md) page.

Find below the list of the components to be installed for each scenario:

|Component|Tekton CI tool|Jenkins CI tool|Cluster|
|:-|:-:|:-:|:-|
|[Tekton](install-tekton.md)|Mandatory| - |:simple-kubernetes:{ .kubernetes } :simple-redhatopenshift:{ .openshift }|
|[NGINX Ingress Controller](install-ingress-nginx.md)[^1]| Mandatory|Mandatory|:simple-kubernetes:{ .kubernetes }|
|[Keycloak](install-keycloak.md)|Mandatory|Mandatory|:simple-kubernetes:{ .kubernetes } :simple-redhatopenshift:{ .openshift }|
|[DefectDojo](install-defectdojo.md)|Mandatory|Mandatory|:simple-kubernetes:{ .kubernetes } :simple-redhatopenshift:{ .openshift }|
|[Argo CD](install-argocd.md)|Mandatory|Optional|:simple-kubernetes:{ .kubernetes } :simple-redhatopenshift:{ .openshift }|
|[ReportPortal](install-reportportal.md)|Optional|Optional|:simple-kubernetes:{ .kubernetes } :simple-redhatopenshift:{ .openshift }|
|[Kiosk](install-kiosk.md)|Optional|Optional|:simple-kubernetes:{ .kubernetes } :simple-redhatopenshift:{ .openshift }|
|[External Secrets](external-secrets-operator-integration.md)|Optional|Optional|:simple-kubernetes:{ .kubernetes } :simple-redhatopenshift:{ .openshift }|

[^1]:
    OpenShift cluster uses Routes to provide access to pods from external resources.

!!! note
    Alternatively, use [Helmfiles](install-via-helmfile.md#deploy-components) to install the EDP components.

After setting up the cluster and installing EDP components according to the selected scenario, proceed to the [EDP installation](install-edp.md).

## Related Articles

* [Set Up Kubernetes](kubernetes-cluster-settings.md)
* [Set Up OpenShift](openshift-cluster-settings.md)
* [Install EDP](install-edp.md)
