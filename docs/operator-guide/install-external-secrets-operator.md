# Install External Secrets Operator

Inspect the prerequisites and the main steps to perform for enabling [External Secrets](https://external-secrets.io/) in EDP.

## Prerequisites

* Kubectl version 1.16.0+ is installed. Please refer to the [Kubernetes official website](https://kubernetes.io/docs/tasks/tools/) for details.
* [Helm](https://helm.sh) version 3.6.0+ is installed. Please refer to the [Helm page](https://github.com/helm/helm/releases) on GitHub for details.

## Installation

To install External Secrets Operator with helm run the below commands:

```bash
helm repo add external-secrets https://charts.external-secrets.io

helm install external-secrets \
   external-secrets/external-secrets \
    -n external-secrets \
    --create-namespace \
  # --set installCRDs=true
```

!!! info
    It is also possible to install External Secrets Operator using the Helmfile. For details, please refer to the [Install via Helmfile](./install-via-helmfile.md#deploy-nginx-ingress-controller) page.

Install by Operator Lifecycle Manager (OLM) is also possible, consult [official documentation](https://operatorhub.io/operator/external-secrets-operator)
