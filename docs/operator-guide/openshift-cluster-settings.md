# Set Up OpenShift

Make sure the cluster meets the following conditions:

1. OpenShift cluster is installed with minimum 2 worker nodes with total capacity 8 Cores and 32Gb RAM.

2. Load balancer (if any exists in front of OpenShift router or ingress controller) is configured with session stickiness, disabled HTTP/2 protocol and header size of 64k support.

  Find below an example of the Config Map for the NGINX Ingress Controller:

  ``` yaml
  kind: ConfigMap
  apiVersion: v1
  metadata:
    name: nginx-configuration
    namespace: ingress-nginx
    labels:
      app.kubernetes.io/name: ingress-nginx
      app.kubernetes.io/part-of: ingress-nginx
  data:
    client-header-buffer-size: 64k
    large-client-header-buffers: 4 64k
    use-http2: "false"
  ```

3. Cluster nodes and pods have access to the cluster via external URLs. For instance, add in AWS the VPC NAT gateway elastic IP to the cluster external load balancers security group).

4. Keycloak instance is installed. To get accurate information on how to install Keycloak, please refer to the [Install Keycloak](install-keycloak.md) instruction.

5. The installation machine with [oc](https://docs.openshift.com/container-platform/4.10/cli_reference/openshift_cli/getting-started-cli.html) is installed with the cluster-admin access to the OpenShift cluster.

6. Helm 3.10 is installed on the installation machine with the help of the [Installing Helm](https://v3.helm.sh/docs/intro/install/) instruction.

7. Storage classes are used with the [Retain Reclaim Policy](https://kubernetes.io/docs/concepts/storage/persistent-volumes/#retain)
and [Delete Reclaim Policy](https://kubernetes.io/docs/concepts/storage/persistent-volumes/#delete).<br/>

  !!! info
      By default, EDP uses the default Storage Class in a cluster. The EDP development team recommends using the following Storage Classes.<br/>
      See an example below.

  Storage class templates with the Retain and Delete Reclaim Policies:
  === "gp3"
      ``` yaml
      kind: StorageClass
      apiVersion: storage.k8s.io/v1
      metadata:
        name: gp3
      annotations:
        storageclass.kubernetes.io/is-default-class: 'true'
      allowedTopologies: []
      mountOptions: []
      provisioner: ebs.csi.aws.com
      reclaimPolicy: Delete
      volumeBindingMode: Immediate
      allowVolumeExpansion: true
      ```

  === "gp3-retain"
      ``` yaml
      kind: StorageClass
      apiVersion: storage.k8s.io/v1
      metadata:
        name: gp3-retain
      allowedTopologies: []
      mountOptions: []
      provisioner: ebs.csi.aws.com
      reclaimPolicy: Retain
      volumeBindingMode: WaitForFirstConsumer
      allowVolumeExpansion: true
      ```

## Related Articles

* [Install Keycloak](install-keycloak.md)
