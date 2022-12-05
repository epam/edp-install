# Install External Secrets Operator

Inspect the prerequisites and the main steps to perform for enabling [External Secrets Operator](https://external-secrets.io/) in EDP.

## Prerequisites

* Kubectl version 1.23.0 is installed. Please refer to the [Kubernetes official website](https://v1-23.docs.kubernetes.io/releases/download/) for details.
* [Helm](https://helm.sh) version 3.10.0+ is installed. Please refer to the [Helm page](https://github.com/helm/helm/releases/tag/v3.10.2) on GitHub for details.

## Installation

To install External Secrets Operator with Helm, run the following commands:

```bash
helm repo add external-secrets https://charts.external-secrets.io

helm install external-secrets \
   external-secrets/external-secrets \
    -version 0.6.1 \
    -n external-secrets \
    --create-namespace \
  # --set installCRDs=true
```

!!! info
    It is also possible to install External Secrets Operator using the [Helmfile](./install-via-helmfile.md#deploy-external-secrets-operator) or [Operator Lifecycle Manager (OLM)](https://operatorhub.io/operator/external-secrets-operator).

## Related Articles
* [External Secrets Operator Integration](external-secrets-operator-integration.md)