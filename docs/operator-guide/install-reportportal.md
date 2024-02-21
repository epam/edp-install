# Install ReportPortal

Inspect the prerequisites and the main steps to perform for installing ReportPortal.

## Prerequisites

* [Kubectl](https://kubernetes.io/docs/tasks/tools/) version 1.23.0 is installed. Please refer to the [Kubernetes official website](https://v1-23.docs.kubernetes.io/releases/download/) for details.
* [Helm](https://helm.sh) version 3.10.2 is installed. Please refer to the [Helm page](https://github.com/helm/helm/releases/tag/v3.10.2) on GitHub for details.

!!! info
    Please refer to the [ReportPortal Helm Chart](https://github.com/reportportal/kubernetes/tree/develop/reportportal) section for details.

## MinIO Installation

To install MinIO, follow the steps below:

1. Check that `edp` namespace is created. If not, run the following command to create it:

  ```bash
  kubectl create namespace edp
  ```

  !!! warning "For the OpenShift users:"
      When using the OpenShift platform, install the `SecurityContextConstraints` resources.<br>
      In case of using a custom namespace for the `reportportal`, change the namespace in the `users` section.

  <details>
  <summary><b>View: report-portal-third-party-resources-scc.yaml</b></summary>

  ```yaml
  apiVersion: security.openshift.io/v1
  kind: SecurityContextConstraints
  metadata:
    annotations:
      "helm.sh/hook": "pre-install"
    name: report-portal-minio-rabbitmq-postgresql
  allowHostDirVolumePlugin: false
  allowHostIPC: false
  allowHostNetwork: false
  allowHostPID: false
  allowHostPorts: false
  allowPrivilegeEscalation: true
  allowPrivilegedContainer: false
  allowedCapabilities: null
  allowedFlexVolumes: []
  defaultAddCapabilities: []
  fsGroup:
    type: MustRunAs
    ranges:
      - min: 999
        max: 65543
  groups: []
  priority: 1
  readOnlyRootFilesystem: false
  requiredDropCapabilities:
    - KILL
    - MKNOD
    - SETUID
    - SETGID
  runAsUser:
    type: MustRunAsRange
    uidRangeMin: 1
    uidRangeMax: 65543
  seLinuxContext:
    type: MustRunAs
  supplementalGroups:
    type: RunAsAny
  users:
    - system:serviceaccount:report-portal:minio
    - system:serviceaccount:report-portal:rabbitmq
    - system:serviceaccount:report-portal:postgresql
  volumes:
    - configMap
    - downwardAPI
    - emptyDir
    - persistentVolumeClaim
    - projected
    - secret
  ```
  </details>

  <details>
  <summary><b>View: report-portal-elasticsearch-scc.yaml</b></summary>

  ```yaml
  apiVersion: security.openshift.io/v1
  kind: SecurityContextConstraints
  metadata:
    annotations:
      "helm.sh/hook": "pre-install"
    name: report-portal-elasticsearch
  allowHostDirVolumePlugin: false
  allowHostIPC: false
  allowHostNetwork: false
  allowHostPID: false
  allowHostPorts: false
  allowPrivilegedContainer: true
  allowedCapabilities: []
  allowedFlexVolumes: []
  defaultAddCapabilities: []
  fsGroup:
    type: MustRunAs
    ranges:
      - max: 1000
        min: 1000
  groups: []
  priority: 0
  readOnlyRootFilesystem: false
  requiredDropCapabilities: []
  runAsUser:
    type: MustRunAsRange
    uidRangeMax: 1000
    uidRangeMin: 0
  seLinuxContext:
    type: MustRunAs
  supplementalGroups:
    type: RunAsAny
  users:
    - system:serviceaccount:report-portal:elasticsearch-master
  volumes:
    - configMap
    - downwardAPI
    - emptyDir
    - persistentVolumeClaim
    - projected
    - secret
  ```
  </details>

2. Add a chart repository:

  ```bash
  helm repo add bitnami https://charts.bitnami.com/bitnami
  helm repo update
  ```

3. Create MinIO admin secret:

  ```bash
  kubectl -n edp create secret generic reportportal-minio-creds \
  --from-literal=root-password=<root_password> \
  --from-literal=root-user=<root_user>
  ```

4. Install MinIO v.11.10.3 using [bitnami/minio](https://artifacthub.io/packages/helm/bitnami/minio) Helm chart v.11.10.3:

  ```bash
  helm install minio bitnami/minio \
  --version 11.10.3 \
  --values values.yaml \
  --namespace edp
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

1. Use `edp` namespace from the MinIO installation.

2. Use `bitnami` chart repository from the MinIO installation.

3. Create RabbitMQ admin secret:

  ```bash
  kubectl -n edp create secret generic reportportal-rabbitmq-creds \
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
  --namespace edp
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
  kubectl -n edp exec -it rabbitmq-0 -- rabbitmqctl set_vm_memory_high_watermark 0.8
  ```

## Elasticsearch Installation

To install Elasticsearch, follow the steps below:

1. Use `edp` namespace from the MinIO installation.

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
  --namespace edp
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

rbac:
  create: true

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

1. Use `edp` namespace from the MinIO installation.

2. Add a chart repository:

  ```bash
  helm repo add bitnami-archive https://raw.githubusercontent.com/bitnami/charts/archive-full-index/bitnami
  helm repo update
  ```

3. Create PostgreSQL admin secret:

  ```bash
  kubectl -n edp create secret generic reportportal-postgresql-creds \
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
  --namespace edp
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
serviceAccount:
  enabled: true
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

1. Use `edp` namespace from the MinIO installation.

  !!! warning "For the OpenShift users:"
      When using the OpenShift platform, install the `SecurityContextConstraints` resource.<br>
      In case of using a custom namespace for the `reportportal`, change the namespace in the `users` section.

  <details>
  <summary><b>View: report-portal-reportportal-scc.yaml</b></summary>

```yaml
apiVersion: security.openshift.io/v1
kind: SecurityContextConstraints
metadata:
  annotations:
    "helm.sh/hook": "pre-install"
  name: report-portal
allowHostDirVolumePlugin: false
allowHostIPC: false
allowHostNetwork: false
allowHostPID: false
allowHostPorts: false
allowPrivilegedContainer: true
allowedCapabilities: []
allowedFlexVolumes: []
defaultAddCapabilities: []
fsGroup:
  type: MustRunAs
  ranges:
    - max: 1000
      min: 1000
groups: []
priority: 0
readOnlyRootFilesystem: false
requiredDropCapabilities: []
runAsUser:
  type: MustRunAsRange
  uidRangeMax: 1000
  uidRangeMin: 0
seLinuxContext:
  type: MustRunAs
supplementalGroups:
  type: RunAsAny
users:
  - system:serviceaccount:report-portal:reportportal
volumes:
  - configMap
  - downwardAPI
  - emptyDir
  - persistentVolumeClaim
  - projected
  - secret
```
  </details>

2. Add a chart repository:

  ```bash
  helm repo add report-portal "https://reportportal.github.io/kubernetes"
  helm repo update
  ```

3. Install ReportPortal v.5.8.0 using Helm chart v.5.8.0:

  ```bash
  helm install report-portal report-portal/reportportal \
  --values values.yaml \
  --namespace edp
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
  serviceAccountName: "reportportal"
  securityContext:
    runAsUser: 0
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

4. For the OpenShift platform, install a Gateway with Route:

  <details>
  <summary><b>View: gateway-config-cm.yaml</b></summary>

```yaml
kind: ConfigMap
metadata:
  name: gateway-config
  namespace: report-portal
apiVersion: v1
data:
  traefik-dynamic-config.yml: |
    http:
        middlewares:
          strip-ui:
            stripPrefix:
              prefixes:
                - "/ui"
              forceSlash: false
          strip-api:
            stripPrefix:
              prefixes:
                - "/api"
              forceSlash: false
          strip-uat:
            stripPrefix:
              prefixes:
                - "/uat"
              forceSlash: false

        routers:
          index-router:
            rule: "Path(`/`)"
            service: "index"
          ui-router:
            rule: "PathPrefix(`/ui`)"
            middlewares:
            - strip-ui
            service: "ui"
          uat-router:
            rule: "PathPrefix(`/uat`)"
            middlewares:
            - strip-uat
            service: "uat"
          api-router:
            rule: "PathPrefix(`/api`)"
            middlewares:
            - strip-api
            service: "api"

        services:
          uat:
            loadBalancer:
              servers:
              - url: "http://report-portal-reportportal-uat:9999/"

          index:
            loadBalancer:
              servers:
              - url: "http://report-portal-reportportal-index:8080/"

          api:
            loadBalancer:
              servers:
              - url: "http://report-portal-reportportal-api:8585/"

          ui:
            loadBalancer:
              servers:
              - url: "http://report-portal-reportportal-ui:8080/"
  traefik.yml: |
    entryPoints:
      http:
       address: ":8081"
      metrics:
       address: ":8082"

    metrics:
      prometheus:
        entryPoint: metrics
        addEntryPointsLabels: true
        addServicesLabels: true
        buckets:
          - 0.1
          - 0.3
          - 1.2
          - 5.0

    providers:
      file:
        filename: /etc/traefik/traefik-dynamic-config.yml
```
  </details>

  <details>
  <summary><b>View: gateway-deployment.yaml</b></summary>

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  labels:
    app: reportportal
  name: gateway
  namespace: report-portal
spec:
  replicas: 1
  selector:
    matchLabels:
      component: gateway
  template:
    metadata:
      labels:
        component: gateway
    spec:
      containers:
        - image: quay.io/waynesun09/traefik:2.3.6
          name: traefik
          ports:
            - containerPort: 8080
              protocol: TCP
          resources: {}
          volumeMounts:
            - mountPath: /etc/traefik/
              name: config
              readOnly: true
      volumes:
        - name: config
          configMap:
            defaultMode: 420
            name: gateway-config
```
  </details>

  <details>
  <summary><b>View: gateway-route.yaml</b></summary>

```yaml
kind: Route
apiVersion: route.openshift.io/v1
metadata:
  labels:
    app: reportportal
  name: reportportal
  namespace: report-portal
spec:
  host: report-portal.<CLUSTER_DOMAIN>
  port:
    targetPort: http
  tls:
    insecureEdgeTerminationPolicy: Redirect
    termination: edge
  to:
    kind: Service
    name: gateway
    weight: 100
  wildcardPolicy: None
```
  </details>

  <details>
  <summary><b>View: gateway-service.yaml</b></summary>

```yaml
apiVersion: v1
kind: Service
metadata:
  labels:
    app: reportportal
    component: gateway
  name: gateway
  namespace: report-portal
spec:
  ports:
    # use 8081 to allow for usage of the dashboard which is on port 8080
    - name: http
      port: 8081
      protocol: TCP
      targetPort: 8081
  selector:
    component:  gateway
  sessionAffinity: None
  type: ClusterIP
```
  </details>
!!! note
    For user access: default/1q2w3e<br>
    For admin access: superadmin/erebus<br>
    Please refer to the [ReportPortal.io](https://reportportal.io/installation) page for details.

!!! note
    It is also possible to install ReportPortal via cluster add-ons. For details, please refer to the [Install via Add-Ons](add-ons-overview.md) page.

## Related Articles

* [Install EDP](install-edp.md)
* [Install via AWS Marketplace](aws-marketplace-install.md)
* [Install via Civo](install-via-civo.md)
* [Install via Add-Ons](add-ons-overview.md)
