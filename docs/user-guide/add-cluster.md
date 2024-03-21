# Add Cluster

This page provides comprehensive instructions on how to integrate a new cluster into the KubeRocketCI workloads. By doing so, it creates an opportunity for users to employ multi-cluster deployment, thereby facilitating the segregation of different environments across various clusters.

## Prerequisites

Before moving ahead, ensure you have already performed the guidelines outlined in the [Argo CD Integration](../operator-guide/argocd-integration.md#deploy-argo-cd-application-to-remote-cluster-optional) page.

## Deploy to Remote Cluster

To deploy an application to a remote cluster, follow the steps below:

1. Navigate to `KubeRocketCI portal` -> `Configuration` -> `Clusters` and click the **+ Add cluster** button:

  !![Clusters menu](../assets/user-guide/add_new_cluster.png "Clusters menu")

2. In the drop-down window, specify the required fields:

  * **Cluster Name** - a unique and descriptive name for the new cluster;
  * **Cluster Host** - the cluster’s endpoint URL (e.g., example-cluster-domain.com);
  * **Cluster Token** - a Kubernetes token with permissions to access the cluster. This token is required for proper authorization;
  * **Skip TLS verification** - allows connect to cluster without cluster certificate verification;
  * **Cluster Certificate** - a Kubernetes certificate essential for authentication. Obtain this certificate from the configuration file of the user account you intend to use for accessing the cluster.

  !!! note
      The `Cluster Certificate` field is hidden if the `skip TLS verification` option is enabled.

  !![Add cluster](../assets/user-guide/edp-portal-add-cluster.png "Add cluster")

3. Click the **Apply** button to add the cluster.

As a result, the Kubernetes secret will be created for further integration and you will be able to select the integrated cluster when creating a new stage:

  !![Select cluster](../assets/user-guide/select-cluster.png "Select cluster")

## Related Articles

* [Argo CD Integration](../operator-guide/argocd-integration.md)
* [Add Application](add-application.md)
* [Add Library](add-library.md)
* [Add Autotest](add-autotest.md)
* [Add CD Pipeline](add-cd-pipeline.md)
