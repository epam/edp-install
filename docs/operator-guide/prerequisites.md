# EDP Installation Prerequisites Overview

Before installing EDP:

* Install and configure [Kubernetes](kubernetes-cluster-settings.md) or [OpenShift](openshift-cluster-settings.md) cluster.
* Install EDP components for the selected EDP installation scenario.

## EDP Installation Scenarios

There are two EDP installation scenarios based on the selected CI tool: Tekton (default) or Jenkins.

**Scenario 1: Tekton CI tool.** By default, EDP uses Tekton as a CI tool and [EDP Headlamp](../headlamp-user-guide/index.md) as a UI tool.

**Scenario 2: Jenkins CI tool.** To use Jenkins as a CI tool, it is required to install the deprecated Admin Console UI tool. Admin Console is used only as a dependency for Jenkins, and Headlamp will still be used as a UI tool.

Find below the list of the components to be installed for each scenario:

|Component|Tekton CI tool|Jenkins CI tool|
|- |- |- |
|[Tekton](install-tekton.md)|Mandatory| - |
|[NGINX Ingress Controller](install-ingress-nginx.md)|Mandatory |Mandatory|
|[Keycloak](install-keycloak.md)|Mandatory|Mandatory|
|[DefectDojo](install-defectdojo.md)|Mandatory|Mandatory|
|[Argo CD](install-argocd.md)|Mandatory|Optional|
|[ReportPortal](install-reportportal.md)|Optional|Optional|
|[Kiosk](install-kiosk.md)|Optional|Optional|
|[External Secrets](external-secrets-operator-integration.md)|Optional|Optional|

!!! note
    Alternatively, use [Helmfiles](install-via-helmfile.md#deploy-components) to install the EDP components.

After setting up the cluster and installing EDP components according to the selected scenario, proceed to the [EDP installation](install-edp.md).

## Related Articles

* [Set Up Kubernetes](kubernetes-cluster-settings.md)
* [Set Up OpenShift](openshift-cluster-settings.md)
* [Install EDP](install-edp.md)
