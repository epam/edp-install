# Container Registry Reset

## Problem

Reset container registry is not feasible due to the `RESET REGISTRY` button not accessible.

## Cause

The KubeRocketCI Portal does not allow to reconfigure the registry because the registry secrets have external owners.

## Solution

Remove the `kaniko-docker-config` and `regcred` resources from both the ExternalSecret custom resources and the Kubernetes secrets.

1. Check the kaniko-docker-config and regcred ExternalSecret custom resources (CRs) in the namespace:

  ```bash
  kubectl get externalsecret kaniko-docker-config -n edp-delivery-os-dev
  kubectl get externalsecret regcred -n edp-delivery-os-dev
  ```
  
2. Delete the `kaniko-docker-config` and `regcred` ExternalSecret CRs using the commands provided below:

  ```bash
  kubectl delete externalsecret kaniko-docker-config -n edp-delivery-os-dev
  kubectl delete externalsecret regcred -n edp-delivery-os-dev
  ```

3. Delete the `kaniko-docker-config` and `regcred` Kubernetes secrets if they have not been automatically deleted by the External Secrets Operator:

  ```bash
  kubectl delete secret kaniko-docker-config -n edp-delivery-os-dev
  kubectl delete secret regcred -n edp-delivery-os-dev
  ```

4. Disable the creation of the kaniko-docker-config and regcred ExternalSecret CRs in the values file of the edp-install Helm chart.

  !!! note
      By default, it takes 5-10 minutes to take affect but it may vary depending on your personal platform configuration.

5. Refresh the KubeRocketCI portal page and set your container registry again.

## Related Articles

* [Change Container Registry](https://epam.github.io/edp-install/operator-guide/container-registries/)