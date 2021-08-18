# Install Velero

[Velero](https://velero.io/) is an open source tool to safely back up, recover, and migrate Kubernetes clusters and persistent volumes.
It works both on premises and in a public cloud. Velero consists of a server process running as a deployment in your
Kubernetes cluster and a command-line interface (CLI) with which DevOps teams and platform operators configure scheduled
backups, trigger ad-hoc backups, perform restores, and more.

## Installation

To install Velero, follow the steps below:

1. Create **velero** namespace:

        kubectl create namespace velero

  !!! note
      On an OpenShift cluster, run the `oc` command instead of `kubectl` one.

2. Add a chart repository:

        helm repo add vmware-tanzu https://vmware-tanzu.github.io/helm-charts
        helm repo update

  !!! note
      [Velero AWS plugin](https://github.com/vmware-tanzu/velero-plugin-for-aws) requires access to AWS resources.
      To configure access, please refer to [IRSA for Velero documentation](./velero-irsa.md).

3. Install **Velero v.2.14.13**:

        helm install velero vmware-tanzu/velero \
        --version 2.14.13 \
        --values values.yaml \
        --namespace velero

  Check out the *values.yaml* file sample of the Velero customization:


   <details>
   <summary><b>View: values.yaml</b></summary>

```yaml
image:
  repository: velero/velero
  tag: v1.5.3
securityContext:
  fsGroup: 65534
restic:
  securityContext:
    fsGroup: 65534
serviceAccount:
  server:
    create: false
    name: edp-velero
credentials:
  useSecret: false
configuration:
  provider: aws
  backupStorageLocation:
    name: default
    bucket: velero-<CLUSTER_NAME>
    config:
      region: eu-central-1
  volumeSnapshotLocation:
    name: default
    config:
      region: <AWS_REGION>
initContainers:
  - name: velero-plugin-for-aws
    image: velero/velero-plugin-for-aws:v1.1.0
    volumeMounts:
      - mountPath: /target
        name: plugins
```

  </details>

  !!! note
      In case of using cluster scheduling and [amazon-eks-pod-identity-webhook](https://github.com/aws/amazon-eks-pod-identity-webhook), it is necessary to restart the Velero pod after the cluster is up and running.
      Please refer to [Velero Auto Restart](./velero-auto-restart.md) documentation.

4. Install the client side (velero cli) according to the official [documentation](https://velero.io/docs/v1.5/basic-install/).

## Configuration

1. Create backup for all components in the namespace:

        velero backup create <BACKUP_NAME> --include-namespaces <NAMESPACE>

2. Create a daily backup of the namespace:

        velero schedule create <BACKUP_NAME>  --schedule "0 10 * * MON-FRI" --include-namespaces <NAMESPACE> --ttl 120h0m0s

3. To restore from backup, use the following command:

        velero restore create <RESTORE_NAME> --from-backup <BACKUP_NAME>
