# AWS Marketplace Installation

This document provides the detailed information regarding the EPAM Delivery Platform product installation from the AWS marketplace.

## Prerequisites

Please make sure that the [Prerequisites](prerequisites.md) for deploying the product are met. According to the prerequisite, the minimal set of created resources for successful deployment of the platform should be as follows:

* domain available and associated with a cluster;
* cluster admin access is granted;
* `edp` namespace is created;
* Keycloak endpoint is available;
* credentials for platform integration are [available](./install-keycloak.md#configuration) in the `edp` namespace;
* `nexus-proxy-cookie-secret` secret is available in `edp` namespace;
* IAM Role with IRSA access to AWS ECR for Kaniko push pipeline;

## Deploy EPAM Delivery Platform

To deploy EPAM Delivery Platform from AWS Marketplace, follow the steps below:

- Define mandatory parameters you would like to install using the command

  ```bash
  helm install edp-install \
      --namespace edp ./* \
      --set global.edpName=edp \
      --set global.dnsWildCard=example.com \
      --set global.webConsole.url=https://xxxxxxxxxxxxxxxxxxxx.sk1.eu-central-1.eks.amazonaws.com \
      --set global.admins={"stub_user_one@example.com"} \
      --set global.developers={"stub_user_one@example.com"} \
      --set global.gitProvider=gerrit \
      --set global.gerritSSHPort=30022 \
      --set global.keycloakUrl=https://keycloak.example.com \
      --set global.dockerRegistry.type=ecr \
      --set global.dockerRegistry.url=<AWS_ACCOUNT_ID>.dkr.ecr.<AWS_REGION>.amazonaws.com \
      --set awsRegion=<AWS_REGION> \
      --set argocd.enabled=true \
      --set argocd.url=https://argocd.example.com
  ```

- Run the command to get ingress endpoints and access to the EPAM Delivery Platform components UI:

  ```bash
  kubectl get ingress -n edp
  ```

As a result, you will get access to EPAM Delivery Platform components via ingresses.

## Related Articles

* [Install EDP via Helmfile](install-via-helmfile.md)
* [Integrate GitHub/GitLab in Tekton](../operator-guide/import-strategy-tekton.md)
* [Set Up Kubernetes](kubernetes-cluster-settings.md)
* [Set Up OpenShift](openshift-cluster-settings.md)
* [EDP Installation Prerequisites Overview](prerequisites.md)
* [Headlamp OIDC Integration](headlamp-oidc.md)