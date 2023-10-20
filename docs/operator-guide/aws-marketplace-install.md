# Install via AWS Marketplace

This documentation provides the detailed instructions on how to install the EPAM Delivery Platform via the AWS Marketplace.

To initiate the installation process, navigate to our dedicated [AWS Marketplace](https://aws.amazon.com/marketplace/pp/prodview-u7xcz6pvwwwoa#pdp-overview) page and commence the deployment of EPAM Delivery Platform.

!!! Disclaimer
    EDP is aligned with industry standards for storing and managing sensitive data, ensuring optimal security. However, the use of custom solutions introduces uncertainties, thus the responsibility for the safety of your data is totally covered by platform administrator.

## Prerequisites

Please familiarize yourself with the [Prerequisites](prerequisites.md) page before deploying the product. To perform a minimal installation, ensure that you meet the following requirements:

* The domain name is available and associated with the ingress object in cluster.
* Cluster administrator access.
* The [Tekton](install-tekton.md) resources are deployed.
* Access to the cluster via Service Account token is available.

## Deploy EPAM Delivery Platform

To deploy the platform, follow the steps below:

1. To apply Tekton stack, deploy Tekton resources by executing the command below:

  ```bash
   kubectl create ns tekton-pipelines
   kubectl create ns tekton-chains
   kubectl create ns tekton-pipelines-resolvers
   kubectl apply --filename https://storage.googleapis.com/tekton-releases/triggers/latest/release.yaml
   kubectl apply --filename https://storage.googleapis.com/tekton-releases/triggers/latest/interceptors.yaml
   kubectl apply --filename https://storage.googleapis.com/tekton-releases/pipeline/latest/release.yaml
   kubectl apply --filename https://storage.googleapis.com/tekton-releases/chains/latest/release.yaml
  ```

2. Define the mandatory parameters you would like to use for installation using the following command:

  ```bash
   kubectl create ns edp
   helm install edp-install \
      --namespace edp ./* \
      --set global.dnsWildCard=example.com \
      --set awsRegion=<AWS_REGION>
  ```

3. (Optional) Provide token to sign in to EDP Portal. Run the following command to create Service Account with cluster admin permissions:

  ```bash
  kubectl create serviceaccount edp-admin -n edp
  kubectl create clusterrolebinding edp-cluster-admin --clusterrole=cluster-admin --serviceaccount=edp:edp-admin
  kubectl apply -f - <<EOF
  apiVersion: v1
  kind: Secret
  metadata:
    name: edp-admin-token
    namespace: edp
    annotations:
      kubernetes.io/service-account.name: edp-admin
  type: kubernetes.io/service-account-token
  EOF
  ```

4. (Optional) To get access to EDP Portal, run the port-forwarding command:

  ```bash
   kubectl port-forward service/edp-headlamp 59480:80 -n edp
  ```

5. (Optional) To open EDP Portal, navigate to the `http://localhost:59480`.
6. (Optional) To get admin token to sign in to EDP Portal:

   ```bash
   kubectl get secrets -o jsonpath="{.items[?(@.metadata.annotations['kubernetes\.io/service-account\.name']=='edp-admin')].data.token}" -n edp|base64 --decode
   ```

As a result, you will get access to EPAM Delivery Platform components via EDP Portal UI. Navigate to our [Use Cases](../use-cases/index.md) to try out EDP functionality. Visit other subsections of the [Operator Guide](../operator-guide/index.md) to figure out how to configure EDP and integrate it with various tools.

## Related Articles

* [EPAM Delivery Platform on AWS Marketplace](https://aws.amazon.com/marketplace/pp/prodview-u7xcz6pvwwwoa#pdp-overview)
* [Integrate GitHub/GitLab in Tekton](../operator-guide/import-strategy-tekton.md)
* [Set Up Kubernetes](kubernetes-cluster-settings.md)
* [Set Up OpenShift](openshift-cluster-settings.md)
* [EDP Installation Prerequisites Overview](prerequisites.md)
* [Headlamp OIDC Integration](headlamp-oidc.md)
