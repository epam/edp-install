# Install Keycloak

Inspect the prerequisites and the main steps to perform for installing Keycloak.

!!! note
    The installation process below is given for a Kubernetes cluster. The steps that differ for an OpenShift cluster are
    indicated in the notes.

## Prerequisites

* Kubectl version 1.18.0 is installed. Please refer to the [Kubernetes official website](https://v1-18.docs.kubernetes.io/docs/setup/release/notes/) for details.
* [Helm](https://helm.sh) version 3.6.0 is installed. Please refer to the [Helm page](https://github.com/helm/helm/releases/tag/v3.6.0) on GitHub for details.

!!! note
    EDP team is using a helm chart from the [codecentric](https://github.com/codecentric/helm-charts/tree/master/charts/keycloak)
    repository, but other repositories can be used as well (e.g. [bitnami](https://github.com/bitnami/charts/tree/master/bitnami/keycloak/)).

## Installation

To install Keycloak, follow the steps below:

1. Check that a security namespace is created. If not, run the following command to create it:

      kubectl create namespace security

  !!! note
      On an OpenShift cluster, run the `oc` command instead of `kubectl` one.

2. Add a chart repository:

      helm repo add codecentric https://codecentric.github.io/helm-charts
      helm repo update

3. Create Keycloak admin secret:

      kubectl -n security create secret generic keycloak-admin-creds \
      --from-literal=username=<keycloak_admin_username> \
      --from-literal=password=<keycloak_admin_password>

4. Create PostgreSQL admin secret:

      kubectl -n security create secret generic keycloak-postgresql \
      --from-literal=postgresql-password=<postgresql_password> \
      --from-literal=postgresql-postgres-password=<postgresql_postgres_password>

5. Install Keycloak v.15.0.2 which is included in the [codecentric/keycloak](https://artifacthub.io/packages/helm/codecentric/keycloak) Helm chart v.15.1.0:

  !!! info
      The Keycloak can be deployed in a production ready mode (e.g. it can include multiple replicas, persistent storage, autoscaling, monitoring, etc.).
      For details, please refer to the [Official Chart Documentation](https://github.com/codecentric/helm-charts/tree/master/charts/keycloak).

  ---
      helm install keycloak codecentric/keycloak \
      --version 15.1.0 \
      --values values.yaml \
      --namespace security

  Check out the *values.yaml* file sample of the Keycloak customization:

  <details>
  <summary><b>View: values.yaml</b></summary>

```yaml
replicas: 1

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
        echo '{"realm": "openshift","enabled": true}' > /realm/openshift.json
    volumeMounts:
      - name: realm
        mountPath: /realm

extraVolumeMounts: |
  - name: realm
    mountPath: /realm

extraVolumes: |
  - name: realm
    emptyDir: {}

extraEnv: |
  - name: PROXY_ADDRESS_FORWARDING
    value: "true"
  - name: KEYCLOAK_USER
    valueFrom:
      secretKeyRef:
        name: keycloak-admin-creds
        key: username
  - name: KEYCLOAK_PASSWORD
    valueFrom:
      secretKeyRef:
        name: keycloak-admin-creds
        key: password
  - name: KEYCLOAK_IMPORT
    value: /realm/openshift.json

# This block should be uncommented if you install Keycloak on Kubernetes
ingress:
  enabled: true
  annotations:
    kubernetes.io/ingress.class: nginx
    ingress.kubernetes.io/affinity: cookie
  rules:
    - host: keycloak.<ROOT_DOMAIN>
      paths:
        - path: "/"
          pathType: Prefix

# This block should be uncommented if you set Keycloak to OpenShift and change the host field
# route:
#   enabled: true
#   host: "keycloak.<ROOT_DOMAIN>"

resources:
  limits:
    memory: "2048Mi"
  requests:
    cpu: "50m"
    memory: "512Mi"

# Use PostgreSQL deployed in a container
persistence:
  deployPostgres: true
  dbVendor: postgres

postgresql:
  postgresqlUsername: admin
  postgresqlDatabase: keycloak
  existingSecret: keycloak-postgresql
  persistence:
    enabled: true
    size: "3Gi"
    # If the StorageClass with reclaimPolicy: Retain is used, install an additional StorageClass before installing Keycloak
    # (the code is given below).
    # If the default StorageClass will be used - change "gp2-retain" to "gp2"
    storageClass: "gp2-retain"
```

  </details>

6. Install an additional StorageClass (optional):

  !!! note
      If the Keycloak installation uses a StorageClass with **reclaimPolicy: Retain**, install additional StorageClass *storageclass.yaml*.

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

7.  Install the custom SecurityContextConstraints (only for OpenShift):

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

## Configuration

To prepare Keycloak for integration with EDP, follow the steps below:

1. Ensure that the "openshift" realm is created.

2. Create a user edp_&#8249;EDP_PROJECT&#8250; in "Master" realm.

  !!! note
      This user should be used by EDP to access Keycloak. Please refer to the [Install EDP](install-edp.md) section for details.

3. In the "Role Mappings" tab, assign the proper roles to user:

* Realm Roles:

  * create-realm,

  * offline_access,

  * uma_authorization

* Client Roles "openshift-realm":

  * impersonation,

  * manage-authorization,

  * manage-clients,

  * manage-users

!![Role mappings](../assets/operator-guide/keycloak-roles.png "Role mappings")
