# Kiosk Setup

Inspect the main steps to set up Kiosk for the proceeding EDP installation.

!!! note
    Integration with Kiosk is an optional feature. You may not want to use it, so just skip those steps and disable in Helm parameters during deploy.

        # global.kioskEnabled: <true/false>

## Prerequisites

* Kubectl version 1.18.0 is installed. Please refer to the [Kubernetes official website](https://v1-18.docs.kubernetes.io/docs/setup/release/notes/) for details.
* [Helm](https://helm.sh) version 3.6.0 is installed. Please refer to the [Helm page](https://github.com/helm/helm/releases/tag/v3.6.0) on GitHub for details.

## Installation

* Deploy Kiosk version 0.2.9 in the cluster. To install it, run the following command:

        # Install kiosk with helm v3
        kubectl create namespace kiosk
        helm install kiosk --version 0.2.9 --repo https://charts.devspace.sh/ kiosk --namespace kiosk --atomic

For more details, please refer to the [Kiosk page](https://github.com/loft-sh/kiosk#1-install-kiosk) on the GitHub.

## Configuration

To provide access to the EDP tenant, follow the steps below.

* Check that a security namespace is created. If not, run the following command to create it:

        kubectl create namespace security

!!! note
    On an OpenShift cluster, run the `oc` command instead of `kubectl` one.

* Add a service account to the security namespace.

        kubectl -n security create sa <edp-project>

!!! info
    &#8249;edp-project&#8250; is the name of the EDP tenant here and in all the following steps.

* Apply the Account template to the cluster. Please check the sample below:
```yaml
apiVersion: tenancy.kiosk.sh/v1alpha1
kind: Account
metadata:
  name: <edp-project>
spec:
  space:
    clusterRole: kiosk-space-admin
  subjects:
  - kind: ServiceAccount
    name: <edp-project>
    namespace: security
```

* Apply the ClusterRoleBinding to the 'kiosk-edit' cluster role (current role is added during installation of Kiosk). Please check the sample below:
```yaml
apiVersion: rbac.authorization.k8s.io/v1
kind: ClusterRoleBinding
metadata:
  name: <edp-project>-kiosk-edit
subjects:
- kind: ServiceAccount
  name: <edp-project>
  namespace: security
roleRef:
  kind: ClusterRole
  name: kiosk-edit
  apiGroup: rbac.authorization.k8s.io
```
* To provide access to the EDP tenant, [generate](https://docs.oracle.com/en-us/iaas/Content/ContEng/Tasks/contengaddingserviceaccttoken.htm) kubeconfig
with Service Account &#8249;edp-project&#8250; permission. The &#8249;edp-project&#8250; account created earlier is located in the security namespace.


