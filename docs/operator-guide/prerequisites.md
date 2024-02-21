# EDP Installation Prerequisites

Before installing EDP via Helm Chart, ensure to complete the following steps:

* Install and configure [Kubernetes](kubernetes-cluster-settings.md) or [OpenShift](openshift-cluster-settings.md) cluster;
* Install the [Nginx ingress controller](install-ingress-nginx.md);
* Install [Tekton](install-tekton.md);
* Install [Argo CD](install-argocd.md).

!!! note
    Alternatively, use the [cluster add-ons](add-ons-overview.md) approach to install the EDP components.

After setting up the cluster and installing EDP components according to the scenario, proceed to the [EDP installation](install-edp.md).

## Related Articles

* [Set Up Kubernetes](kubernetes-cluster-settings.md)
* [Set Up OpenShift](openshift-cluster-settings.md)
* [Install EDP](install-edp.md)
