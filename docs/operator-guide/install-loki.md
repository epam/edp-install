# Install Grafana Loki

EDP configures the logging with the help of [Grafana Loki](https://grafana.com/oss/loki/) aggregation system.

## Installation

To install Loki, follow the steps below:

1. Create **logging** namespace:

        kubectl create namespace logging

  !!! note
      On the OpenShift cluster, run the `oc` command instead of the `kubectl` command.

2. Add a chart repository:

        helm repo add grafana https://grafana.github.io/helm-charts
        helm repo update

  !!! note
      It is possible to use Amazon Simple Storage Service [Amazon S3](https://aws.amazon.com/s3/) as an object storage for Loki.
      To configure access, please refer to the [IRSA for Loki](./loki-irsa.md) documentation.

3. Install **Loki v.2.6.0**:

        helm install loki grafana/loki \
        --version 2.6.0 \
        --values values.yaml \
        --namespace logging

  Check out the *values.yaml* file sample of the Loki customization:


   <details>
   <summary><b>View: values.yaml</b></summary>

```yaml
image:
  repository: grafana/loki
  tag: 2.3.0
config:
  auth_enabled: false
  schema_config:
    configs:
    - from: 2021-06-01
      store: boltdb-shipper
      object_store: s3
      schema: v11
      index:
        prefix: loki_index_
        period: 24h
  storage_config:
    aws:
      s3: s3://<AWS_REGION>/loki-<CLUSTER_NAME>
    boltdb_shipper:
      active_index_directory: /data/loki/index
      cache_location: /data/loki/boltdb-cache
      shared_store: s3
  chunk_store_config:
    max_look_back_period: 24h
resources:
  limits:
    memory: "128Mi"
  requests:
    cpu: "50m"
    memory: "128Mi"
serviceAccount:
  create: false
  name: edp-loki
persistence:
  enabled: false
```

  </details>

  !!! note
      In case of using cluster scheduling and [amazon-eks-pod-identity-webhook](https://github.com/aws/amazon-eks-pod-identity-webhook#amazon-eks-pod-identity-webhook), it is necessary to restart the Loki pod after the cluster is up and running.
      Please refer to the [Schedule Pods Restart](schedule-pods-restart.md) documentation.

4. Configure [custom bucket policy](https://docs.aws.amazon.com/AmazonS3/latest/userguide/object-lifecycle-mgmt.html) to delete the old data.
