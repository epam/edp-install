# Set Up Kiosk

[Kiosk](https://github.com/loft-sh/kiosk) is a multi-tenancy extension for managing tenants and namespaces in a shared Kubernetes cluster.
Within EDP, Kiosk is used to separate resources and enables the following options (see more [details](edp-kiosk-usage.md)):

* Access to the EDP tenants in a Kubernetes cluster;

* Multi-tenancy access at the service account level for application deploy.

Inspect the main steps to set up Kiosk for the proceeding EDP installation.

!!! note
    Kiosk deploy is mandatory for EDP v.2.8. In earlier versions, Kiosk is not implemented. Since EDP v.2.9.0, integration with Kiosk is an optional feature.
    You may not want to use it, so just skip those steps and disable in Helm parameters during [EDP deploy](./install-edp.md).

        # global.kioskEnabled: <true/false>


## Prerequisites

* Kubectl version 1.18.0 is installed. Please refer to the [Kubernetes official website](https://v1-18.docs.kubernetes.io/docs/setup/release/notes/) for details.
* [Helm](https://helm.sh) version 3.6.0 is installed. Please refer to the [Helm page](https://github.com/helm/helm/releases/tag/v3.6.0) on GitHub for details.

## Installation

* Deploy Kiosk version 0.2.11 in the cluster. To install it, run the following command:

        # Install kiosk with helm v3

        helm repo add kiosk https://charts.devspace.sh/
        kubectl create namespace kiosk
        helm install kiosk --version 0.2.11 kiosk/kiosk -n kiosk --atomic


For more details, please refer to the [Kiosk page](https://github.com/loft-sh/kiosk#1-install-kiosk) on the GitHub.

## Configuration

To provide access to the EDP tenant, follow the steps below.

* Check that a security namespace is created. If not, run the following command to create it:

        kubectl create namespace security

!!! note
    On an OpenShift cluster, run the `oc` command instead of `kubectl` one.

* Add a service account to the security namespace.

        kubectl -n security create sa edp

!!! info
    Please note that `edp` is the name of the EDP tenant here and in all the following steps.

* Apply the Account template to the cluster. Please check the sample below:
```yaml
apiVersion: tenancy.kiosk.sh/v1alpha1
kind: Account
metadata:
  name: edp-admin
spec:
  space:
    clusterRole: kiosk-space-admin
  subjects:
  - kind: ServiceAccount
    name: edp
    namespace: security
```

* Apply the ClusterRoleBinding to the 'kiosk-edit' cluster role (current role is added during installation of Kiosk). Please check the sample below:
```yaml
apiVersion: rbac.authorization.k8s.io/v1
kind: ClusterRoleBinding
metadata:
  name: edp-kiosk-edit
subjects:
- kind: ServiceAccount
  name: edp
  namespace: security
roleRef:
  kind: ClusterRole
  name: kiosk-edit
  apiGroup: rbac.authorization.k8s.io
```
* To provide access to the EDP tenant, [generate](https://docs.oracle.com/en-us/iaas/Content/ContEng/Tasks/contengaddingserviceaccttoken.htm) kubeconfig
with Service Account edp permission. The edp account created earlier is located in the security namespace.
