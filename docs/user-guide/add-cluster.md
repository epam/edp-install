# Add Cluster

Adding other clusters allows deploying applications to several clusters when creating a stage of CD pipeline in EDP Portal.

!!! note
    Before proceeding with the content on this documentation page, it's essential to ensure that Argo CD is integrated into the same cluster with an identical name. At present, EDP operates with the shared Argo CD, necessitating the copying of the secret to the namespace where Argo CD is installed. The command sample is below:
    ```bash
    kubectl get secret <SECRET_NAME> --namespace=edp -o yaml | sed 's/namespace: .*/namespace: argocd/' | kubectl apply -f -
    ```

To add a cluster, follow the steps below:

1. Navigate to the **Configuration** section on the navigation bar and select **Clusters**. The appearance differs depending on the chosen display option:

  === "List option"

        !![Add Cluster](../assets/user-guide/configuration_menu.png "Configuration menu (List option)")

  === "Tiled option"

        !![Add Cluster](../assets/user-guide/configuration_menu_tiles_format.png "Configuration menu (Tiled option)")

2. Click the **+** button to enter the **Create new cluster** menu:

  !![Add Cluster](../assets/user-guide/add_new_cluster.png "Add Cluster")

3. Once clicked, the **Create new cluster** dialog will appear. You can create a Cluster in YAML or via UI:

=== "Add cluster in YAML"

    To add cluster in YAML, follow the steps below:

    * Click the **Edit YAML** button in the upper-right corner of the **Create New Cluster** dialog to open the YAML editor and create a Kubernetes secret.

    !![Edit YAML](../assets/user-guide/edp-portal-yaml-edit-cluster.png "Edit YAML")

    * To edit YAML in the minimal editor, turn on the **Use minimal editor** toggle in the upper-right corner of the **Create new cluster** dialog.

    * To save the changes, select the **Save & Apply** button.

=== "Add cluster via UI"

    To add cluster in YAML, follow the steps below:

    * To add a new cluster via the dialog menu, fill in the following fields in the **Create New Cluster** dialog:

    !![Add Cluster](../assets/user-guide/edp-portal-add-cluster.png "Add Cluster")

    * Click the **Apply** button to add the cluster to the clusters list.

As a result, the [Kubernetes secret](https://argo-cd.readthedocs.io/en/stable/operator-manual/declarative-setup/#clusters) will be created for further integration.

## Related Articles

* [Add Application](add-application.md)
* [Add Library](add-library.md)
* [Add Autotest](add-autotest.md)
* [Add CD Pipeline](add-cd-pipeline.md)
