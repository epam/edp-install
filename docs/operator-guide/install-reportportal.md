# Install ReportPortal

Inspect the prerequisites and the main steps to perform for installing ReportPortal.

!!! info
    It is also possible to install ReportPortal using the Helmfile. For details, please refer to the [Install via Helmfile](./install-via-helmfile.md##deploy-reportportal) page.

## Prerequisites

* Kubectl version 1.20.0 is installed. Please refer to the [Kubernetes official website](https://v1-20.docs.kubernetes.io/docs/setup/release/notes/) for details.
* [Helm](https://helm.sh) version 3.9.2 is installed. Please refer to the [Helm page](https://github.com/helm/helm/releases/tag/v3.9.2) on GitHub for details.
* Helm-git plugin version 0.11.4 is installed. Please refer to the [GitHub](https://github.com/aslafy-z/helm-git) page for details.

!!! info
    Please refer to the [ReportPortal Helm Chart](https://github.com/reportportal/kubernetes/tree/develop/reportportal) section for details.

## MinIO Installation

To install MinIO, follow the steps below:

1. Check that `<edp-project>` namespace is created. If not, run the following command to create it:

  ```bash
  kubectl create namespace <edp-project>
  ```

2. Add a chart repository:

  ```bash
  helm repo add bitnami https://charts.bitnami.com/bitnami
  helm repo update
  ```

3. Create MinIO admin secret:

  ```bash
  kubectl -n <edp-project> create secret generic reportportal-minio-creds \
  --from-literal=root-password=<root_password> \
  --from-literal=root-user=<root_user>
  ```

4. Install MinIO v.11.10.3 using [bitnami/minio](https://artifacthub.io/packages/helm/bitnami/minio) Helm chart v.11.10.3:

  ```bash
  helm install minio bitnami/minio \
  --version 11.10.3 \
  --values values.yaml \
  --namespace <edp-project>
  ```

  Check out the *values.yaml* file sample of the MinIO customization:

  <details>
  <summary><b>View: values.yaml</b></summary>

```yaml
auth:
  existingSecret: reportportal-minio-creds
persistence:
  size: 1Gi
```

  </details>

## RabbitMQ Installation

To install RabbitMQ, follow the steps below:

1. Use `<edp-project>` namespace from the MinIO installation.

2. Use `bitnami` chart repository from the MinIO installation.

3. Create RabbitMQ admin secret:

  ```bash
  kubectl -n <edp-project> create secret generic reportportal-rabbitmq-creds \
  --from-literal=rabbitmq-password=<rabbitmq_password> \
  --from-literal=rabbitmq-erlang-cookie=<rabbitmq_erlang_cookie>
  ```

  !!! warning
      The `rabbitmq_password` password must be 10 characters long.<br>
      The `rabbitmq_erlang_cookie` password must be 32 characters long.

4. Install RabbitMQ v.10.3.8 using [bitnami/rabbitmq](https://artifacthub.io/packages/helm/bitnami/rabbitmq) Helm chart v.10.3.8:

  ```bash
  helm install rabbitmq bitnami/rabbitmq \
  --version 10.3.8 \
  --values values.yaml \
  --namespace <edp-project>
  ```

  Check out the *values.yaml* file sample of the RabbitMQ customization:

  <details>
  <summary><b>View: values.yaml</b></summary>

```yaml
auth:
  existingPasswordSecret: reportportal-rabbitmq-creds
  existingErlangSecret: reportportal-rabbitmq-creds
persistence:
  size: 1Gi
```

  </details>

5. After the rabbitmq pod gets the status Running, you need to configure the RabbitMQ memory threshold

  ```bash
  kubectl -n <edp-project> exec -it rabbitmq-0 -- rabbitmqctl set_vm_memory_high_watermark 0.8
  ```

## Elasticsearch Installation

To install Elasticsearch, follow the steps below:

1. Use `<edp-project>` namespace from the MinIO installation.

2. Add a chart repository:

  ```bash
  helm repo add elastic https://helm.elastic.co
  helm repo update
  ```

3. Install Elasticsearch v.7.17.3 using [elastic/elasticsearch](https://artifacthub.io/packages/helm/elastic/elasticsearch) Helm chart v.7.17.3:

  ```bash
  helm install elasticsearch elastic/elasticsearch \
  --version 7.17.3 \
  --values values.yaml \
  --namespace <edp-project>
  ```

  Check out the *values.yaml* file sample of the Elasticsearch customization:

  <details>
  <summary><b>View: values.yaml</b></summary>

```yaml
replicas: 1

extraEnvs:
  - name: discovery.type
    value: single-node
  - name: cluster.initial_master_nodes
    value: ""

resources:
  requests:
    cpu: "100m"
    memory: "2Gi"

volumeClaimTemplate:
  resources:
    requests:
      storage: 3Gi
```

  </details>

## PostgreSQL Installation

To install PostgreSQL, follow the steps below:

1. Use `<edp-project>` namespace from the MinIO installation.

2. Add a chart repository:

  ```bash
  helm repo add bitnami-archive https://raw.githubusercontent.com/bitnami/charts/archive-full-index/bitnami
  helm repo update
  ```

3. Create PostgreSQL admin secret:

  ```bash
  kubectl -n <edp-project> create secret generic reportportal-postgresql-creds \
  --from-literal=postgresql-password=<postgresql_password> \
  --from-literal=postgresql-postgres-password=<postgresql_postgres_password>
  ```

  !!! warning
      The `postgresql_password` and `postgresql_postgres_password` passwords must be 16 characters long.

4. Install PostgreSQL v.10.9.4 using Helm chart v.10.9.4:

  ```bash
  helm install postgresql bitnami-archive/postgresql \
  --version 10.9.4 \
  --values values.yaml \
  --namespace <edp-project>
  ```

  Check out the *values.yaml* file sample of the PostgreSQL customization:

  <details>
  <summary><b>View: values.yaml</b></summary>

```yaml
persistence:
  size: 1Gi
resources:
  requests:
    cpu: "100m"
postgresqlUsername: "rpuser"
postgresqlDatabase: "reportportal"
existingSecret: "reportportal-postgresql-creds"
initdbScripts:
  init_postgres.sh: |
    #!/bin/sh
    /opt/bitnami/postgresql/bin/psql -U postgres -d ${POSTGRES_DB} -c 'CREATE EXTENSION IF NOT EXISTS ltree; CREATE EXTENSION IF NOT EXISTS pgcrypto; CREATE EXTENSION IF NOT EXISTS pg_trgm;'
```

  </details>

## ReportPortal Installation

To install ReportPortal, follow the steps below:

1. Use `<edp-project>` namespace from the MinIO installation.

2. Add a chart repository:

  ```bash
  helm repo add report-portal "git+https://github.com/reportportal/kubernetes@reportportal?ref=master"
  helm repo update
  ```

3. Install ReportPortal v.5.7.2 using Helm chart v.5.7.2:

  ```bash
  helm install report-portal report-portal/reportportal \
  --values values.yaml \
  --namespace <edp-project>
  ```

  Check out the *values.yaml* file sample of the ReportPortal customization:

  <details>
  <summary><b>View: values.yaml</b></summary>

```yaml
serviceindex:
  resources:
    requests:
      cpu: 50m
uat:
  resources:
    requests:
      cpu: 50m
serviceui:
  resources:
    requests:
      cpu: 50m
serviceapi:
  resources:
    requests:
      cpu: 50m
serviceanalyzer:
  resources:
    requests:
      cpu: 50m
serviceanalyzertrain:
  resources:
    requests:
      cpu: 50m

rabbitmq:
  SecretName: "reportportal-rabbitmq-creds"
  endpoint:
    address: rabbitmq.<EDP_PROJECT>.svc.cluster.local
    user: user
    apiuser: user

postgresql:
  SecretName: "reportportal-postgresql-creds"
  endpoint:
    address: postgresql.<EDP_PROJECT>.svc.cluster.local

elasticsearch:
 endpoint: http://elasticsearch-master.<EDP_PROJECT>.svc.cluster.local:9200

minio:
  secretName: "reportportal-minio-creds"
  endpoint: http://minio.<EDP_PROJECT>.svc.cluster.local:9000
  endpointshort: minio.<EDP_PROJECT>.svc.cluster.local:9000
  accesskeyName: "root-user"
  secretkeyName: "root-password"

ingress:
  # IF YOU HAVE SOME DOMAIN NAME SET INGRESS.USEDOMAINNAME to true
  usedomainname: true
  hosts:
    - report-portal-<EDP_PROJECT>.<ROOT_DOMAIN>

```

  </details>

## Related Articles

* [Install via Helmfile](install-via-helmfile.md)