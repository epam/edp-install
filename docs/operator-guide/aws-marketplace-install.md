# Install via AWS Marketplace

This documentation provides the detailed instructions on how to install the EPAM Delivery Platform from the AWS marketplace.

## Prerequisites

Please familiarize yourself with the [Prerequisites](prerequisites.md) page before deploying the product. To perform a minimal installation, ensure that you meet the following requirements:

* The domain name is available and associated with the cluster.
* Administrative rights within the cluster are required.
* The `edp` namespace is created.
* The Keycloak endpoint is available and credentials to integrate platform are [available](./install-keycloak.md#configuration) in `edp` namespace.
* The `nexus-proxy-cookie-secret` secret is available in the `edp` namespace.
* The IAM Role with IRSA access to AWS ECR for the Kaniko push pipeline is created.

## Deploy EPAM Delivery Platform

To deploy the platform, follow the steps below:

1. Define the mandatory parameters you would like to use for installation using the following command:

  ```bash
  helm install edp-install \
      --namespace edp ./* \
      --set global.dnsWildCard=example.com \
      --set global.admins={"stub_user_one@example.com"} \
      --set global.developers={"stub_user_one@example.com"} \
      --set global.gitProvider=gerrit \
      --set global.gerritSSHPort=30022 \
      --set sso.keycloakUrl=https://keycloak.example.com \
      --set global.dockerRegistry.type=ecr \
      --set global.dockerRegistry.url=<AWS_ACCOUNT_ID>.dkr.ecr.<AWS_REGION>.amazonaws.com \
      --set awsRegion=<AWS_REGION> \
      --set argocd.enabled=true \
      --set argocd.url=https://argocd.example.com
  ```

2. Run the following command to get ingress endpoints and copy the URL to access the EPAM Delivery Platform components UI:

  ```bash
  kubectl get ingress -n edp
  ```

As a result, you will get access to EPAM Delivery Platform components via EDP Portal UI. Navigate to our [Use Cases](../use-cases/index.md) to try out EDP functionality. Visit other subsections of the [Operator Guide](../operator-guide/index.md) to figure out how to configure EDP and integrate it with various tools.

## Related Articles

* [Install EDP via Helmfile](install-via-helmfile.md)
* [Integrate GitHub/GitLab in Tekton](../operator-guide/import-strategy-tekton.md)
* [Set Up Kubernetes](kubernetes-cluster-settings.md)
* [Set Up OpenShift](openshift-cluster-settings.md)
* [EDP Installation Prerequisites Overview](prerequisites.md)
* [Headlamp OIDC Integration](headlamp-oidc.md)
