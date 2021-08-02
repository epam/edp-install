# Keycloak Installation on Kubernetes

Inspect the prerequisites and the main steps to perform for installing Keycloak on Kubernetes.

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

2. Add a chart repository:

        helm repo add codecentric https://codecentric.github.io/helm-charts
        helm repo update

3. Create Keycloak admin secret:

        kubectl -n security create secret generic keycloak-admin-creds --from-literal=username=<keycloak_admin_username> --from-literal=password=<keycloak_admin_password>

4. Install Keycloak v.13.0.1:

  !!! info
      The Keycloak can be deployed in a production ready mode (e.g. it can include multiple replicas, persistent storage, autoscaling, monitoring, etc.).
      For details, please refer to the [Official Chart Documentation](https://github.com/codecentric/helm-charts/tree/master/charts/keycloak).

  ---
      helm install keycloak codecentric/keycloak \
      --version 11.0.1 \
      --set image.tag=13.0.1 \
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

ingress:
  enabled: true
  annotations:
    kubernetes.io/ingress.class: nginx
    ingress.kubernetes.io/affinity: cookie
  rules:
    - host: keycloak.example.com
      paths:
        - /

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
  postgresqlUsername: username
  postgresqlPassword: passwords
  postgresqlDatabase: keycloak
  persistence:
    enabled: true
    size: "3Gi"
    storageClass: "gp2"
```

</details>

## Configuration

To prepare Keycloak for integration with EDP, follow the steps below:

1. Ensure that the "openshift" realm is created.

2. Create a user in "Master" realm.

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

![keycloak-roles](../assets/operator-guide/keycloak-roles.png "keycloak-roles")
