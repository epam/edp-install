# Install Keycloak

Inspect the prerequisites and the main steps to perform for installing Keycloak.

!!! note
    The installation process below is given for a Kubernetes cluster. The steps that differ for an OpenShift cluster are
    indicated in the notes.

## Prerequisites

* Kubectl version 1.18.0 is installed. Please refer to the [Kubernetes official website](https://v1-18.docs.kubernetes.io/docs/setup/release/notes/) for details.
* [Helm](https://helm.sh) version 3.6.0 is installed. Please refer to the [Helm page](https://github.com/helm/helm/releases/tag/v3.6.0) on GitHub for details.

!!! note
    EDP team is using a helm chart from the [codecentric](https://github.com/codecentric/helm-charts/tree/master/charts/keycloakx) repository, but other repositories can be used as well (e.g. [Bitnami](https://github.com/bitnami/charts/tree/master/bitnami/keycloak/)).
    Before installing Keycloak, it is necessary to install a [PostgreSQL database](https://www.postgresql.org/download/).

!!! info
    It is also possible to install Keycloak using the Helmfile. For details, please refer to the [Install via Helmfile](./install-via-helmfile.md#deploy-keycloak) page.

## PostgreSQL Installation

To install PostgreSQL, follow the steps below:

1. Check that a security namespace is created. If not, run the following command to create it:

      kubectl create namespace security

  !!! note
      On an OpenShift cluster, run the `oc` command instead of `kubectl` one.

2. Add a chart repository:

      helm repo add bitnami https://charts.bitnami.com/bitnami
      helm repo update

3. Create PostgreSQL admin secret:

      kubectl -n security create secret generic keycloak-postgresql \
      --from-literal=password=<postgresql_password> \
      --from-literal=postgres-password=<postgresql_postgres_password>

4. Install PostgreSQL v.14.4.0 using [bitnami/postgresql](https://artifacthub.io/packages/helm/bitnami/postgresql) Helm chart v.11.6.19:

  !!! info
      The PostgreSQL can be deployed in production ready mode. For example, it may include multiple replicas, persistent storage, autoscaling, and monitoring.
      For details, please refer to the [official Chart documentation](https://github.com/bitnami/charts/tree/master/bitnami/postgresql).

  ---
      helm install postgresql bitnami/postgresql \
      --version 11.6.19 \
      --values values.yaml \
      --namespace security

  Check out the *values.yaml* file sample of the PostgreSQL customization:

  <details>
  <summary><b>View: values.yaml</b></summary>

```yaml
# PostgreSQL read only replica parameters
readReplicas:
  # Number of PostgreSQL read only replicas
  replicaCount: 1

global:
  postgresql:
    auth:
      username: admin
      existingSecret: keycloak-postgresql
      database: keycloak

primary:
  persistence:
    enabled: true
    size: 3Gi
    # If the StorageClass with reclaimPolicy: Retain is used, install an additional StorageClass before installing PostgreSQL
    # (the code is given below).
    # If the default StorageClass will be used - change "gp2-retain" to "gp2"
    storageClass: "gp2-retain"
```

  </details>

5. Install an additional StorageClass (optional):

  !!! note
      If the PostgreSQL installation uses a StorageClass with **reclaimPolicy: Retain**, install additional StorageClass *storageclass.yaml*.

  <details>
  <summary><b>View: storageclass.yaml</b></summary>

```yaml
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
```

  </details>

6.  Install the custom SecurityContextConstraints (only for OpenShift):

  !!! note
      If you use OpenShift as your deployment platform, add *customsecuritycontextconstraints.yaml*.

  <details>
  <summary><b>View: customsecuritycontextconstraints.yaml</b></summary>

```yaml
allowHostDirVolumePlugin: false
allowHostIPC: false
allowHostNetwork: false
allowHostPID: false
allowHostPorts: false
allowPrivilegeEscalation: true
allowPrivilegedContainer: false
allowedCapabilities: null
apiVersion: security.openshift.io/v1
defaultAddCapabilities: null
allowedCapabilities: []
allowedFlexVolumes: []
defaultAddCapabilities: []
fsGroup:
  type: MustRunAs
  ranges:
    - min: 999
      max: 65543
groups: []
kind: SecurityContextConstraints
metadata:
  annotations:
      "helm.sh/hook": "pre-install"
  name: customscc
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
- system:serviceaccount:security:keycloak
- system:serviceaccount:security:default
volumes:
- configMap
- downwardAPI
- emptyDir
- persistentVolumeClaim
- projected
- secret
```

  </details>

## Keycloak Installation

To install Keycloak, follow the steps below:

1. Use `security` namespace from the PostgreSQL installation.

2. Add a chart repository:

      helm repo add codecentric https://codecentric.github.io/helm-charts
      helm repo update

3. Create Keycloak admin secret:

      kubectl -n security create secret generic keycloak-admin-creds \
      --from-literal=username=<keycloak_admin_username> \
      --from-literal=password=<keycloak_admin_password>

4. Install Keycloak 19.0.1 using [codecentric/keycloakx](https://artifacthub.io/packages/helm/codecentric/keycloakx) Helm chart:

  !!! info
      Keycloak can be deployed in production ready mode. For example, it may include multiple replicas, persistent storage, autoscaling, and monitoring.
      For details, please refer to the [official Chart documentation](https://github.com/codecentric/helm-charts/tree/master/charts/keycloakx).

  ---
      helm install keycloakx codecentric/keycloakx \
      --version 1.4.2 \
      --values values.yaml \
      --namespace security

  Check out the *values.yaml* file sample of the Keycloak customization:

  <details>
  <summary><b>View: values.yaml</b></summary>

```yaml
replicas: 1

# Deploy the latest verion
image:
  tag: "19.0.1"

# start: create OpenShift realm which is required by EDP
extraInitContainers: |
  - name: realm-provider
    image: busybox
    imagePullPolicy: IfNotPresent
    command:
      - sh
    args:
      - -c
      - |
        echo '{"realm": "openshift","enabled": true}' > /opt/keycloak/data/import/openshift.json
    volumeMounts:
      - name: realm
        mountPath: /opt/keycloak/data/import

extraVolumeMounts: |
  - name: realm
    mountPath: /opt/keycloak/data/import

extraVolumes: |
  - name: realm
    emptyDir: {}

command:
  - "/opt/keycloak/bin/kc.sh"
  - "--verbose"
  - "start"
  - "--auto-build"
  - "--http-enabled=true"
  - "--http-port=8080"
  - "--hostname-strict=false"
  - "--hostname-strict-https=false"
  - "--spi-events-listener-jboss-logging-success-level=info"
  - "--spi-events-listener-jboss-logging-error-level=warn"
  - "--import-realm"

extraEnv: |
  - name: KC_PROXY
    value: "passthrough"
  - name: KEYCLOAK_ADMIN
    valueFrom:
      secretKeyRef:
        name: keycloak-admin-creds
        key: username
  - name: KEYCLOAK_ADMIN_PASSWORD
    valueFrom:
      secretKeyRef:
        name: keycloak-admin-creds
        key: password
  - name: JAVA_OPTS_APPEND
    value: >-
      -XX:+UseContainerSupport
      -XX:MaxRAMPercentage=50.0
      -Djava.awt.headless=true
      -Djgroups.dns.query={{ include "keycloak.fullname" . }}-headless

# This block should be uncommented if you install Keycloak on Kubernetes
ingress:
  enabled: true
  annotations:
    kubernetes.io/ingress.class: nginx
    ingress.kubernetes.io/affinity: cookie
  rules:
    - host: keycloak.<ROOT_DOMAIN>
      paths:
        - path: '{{ tpl .Values.http.relativePath $ | trimSuffix "/" }}/'
          pathType: Prefix

# This block should be uncommented if you set Keycloak to OpenShift and change the host field
# route:
#   enabled: false
#   # Path for the Route
#   path: '{{ tpl .Values.http.relativePath $ | trimSuffix "/" }}/'
#   # Host name for the Route
#   host: "keycloak.<ROOT_DOMAIN>"
#   # TLS configuration
#   tls:
#     enabled: true

resources:
  limits:
    memory: "2048Mi"
  requests:
    cpu: "50m"
    memory: "512Mi"

# Check database readiness at startup
dbchecker:
  enabled: true

database:
  vendor: postgres
  existingSecret: keycloak-postgresql
  hostname: postgresql
  port: 5432
  username: admin
  database: keycloak
```

  </details>

## Configuration

To prepare Keycloak for integration with EDP, follow the steps below:

1. Ensure that the `openshift` realm is created.

2. Create a user `edp_<EDP_PROJECT>` in `Master` realm.

  !!! note
      This user should be used by EDP to access Keycloak. Please refer to the [Install EDP](install-edp.md) section for details.

3. In the `Role Mapping` tab, assign the proper roles to user:

* Realm Roles:

  * create-realm,

  * offline_access,

  * uma_authorization

* Client Roles `openshift-realm`:

  * impersonation,

  * manage-authorization,

  * manage-clients,

  * manage-users

!![Role mappings](../assets/operator-guide/keycloak-roles.png "Role mappings")
