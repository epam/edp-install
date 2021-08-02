# OpenShift Settings

Make sure the cluster meets the following conditions:

1. OpenShift cluster is installed with minimum 2 worker nodes with total capacity 32 Cores and 8Gb RAM;

2. Load balancer (if any exists in front of OpenShift router or ingress controller) is configured with session stickiness, disabled HTTP/2 protocol and header size of 64k support;
  Example of Config Map for Nginx ingress controller:

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

3. Cluster nodes and pods have access to the cluster via external URLs. For instance, add in AWS the VPC NAT gateway elastic IP to the cluster external load balancers security group);

4. Keycloak instance is installed. To get accurate information on how to install Keycloak, please refer to the [Keycloak Installation on OpenShift](install-keycloak.md) instruction;

5. The installation machine with [oc](https://docs.openshift.com/container-platform/4.2/cli_reference/openshift_cli/getting-started-cli.html#cli-installing-cli_cli-developer-commands) is installed with the cluster-admin access to the OpenShift cluster;

6. Helm 3.1 is installed on the installation machine with the help of the [Installing Helm](https://v3.helm.sh/docs/intro/install/) instruction.

7. A storage class is used with the [Retain Reclaim Policy](https://kubernetes.io/docs/concepts/storage/persistent-volumes/#retain):
  Storage class template with Retain Reclaim Policy:

      kind: StorageClass
      apiVersion: storage.k8s.io/v1
      metadata:
        name: gp2-retain
      provisioner: kubernetes.io/aws-ebs
      parameters:
        fsType: ext4
        type: gp2
      reclaimPolicy: Retain
      volumeBindingMode: WaitForFirstConsumer
