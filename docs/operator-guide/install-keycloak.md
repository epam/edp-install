# Install Keycloak

Inspect the prerequisites and the main steps to perform for installing Keycloak.

!!! info
    The installation process below is given for a Kubernetes cluster. The steps that differ for an OpenShift cluster are
    indicated in the warnings blocks.

## Prerequisites

* Kubectl version 1.23.0 is installed. Please refer to the [Kubernetes official website](https://v1-23.docs.kubernetes.io/releases/download/) for details.
* [Helm](https://helm.sh) version 3.10.0+ is installed. Please refer to the [Helm page](https://github.com/helm/helm/releases/tag/v3.10.2) on GitHub for details.

!!! info
    EDP team is using a Keycloakx helm chart from the [codecentric](https://github.com/codecentric/helm-charts/tree/master/charts/keycloakx) repository, but other repositories can be used as well (e.g. [Bitnami](https://github.com/bitnami/charts/tree/master/bitnami/keycloak/)).
    Before installing Keycloak, it is necessary to install a [PostgreSQL database](https://www.postgresql.org/download/).

!!! info
    It is also possible to install Keycloak using the Helmfile. For details, please refer to the [Install via Helmfile](./install-via-helmfile.md#deploy-keycloak) page.

## PostgreSQL Installation

To install PostgreSQL, follow the steps below:

1. Check that a security namespace is created. If not, run the following command to create it:

      kubectl create namespace security

  !!! warning
      On the OpenShift platform, apply the `SecurityContextConstraints` resource. Change the namespace in the `users` section if required.

      ??? note "View: keycloak-scc.yaml"
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
            name: keycloak
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
            - system:serviceaccount:security:keycloakx
          volumes:
            - configMap
            - downwardAPI
            - emptyDir
            - persistentVolumeClaim
            - projected
            - secret
          ```

      ??? note "View: postgresql-keycloak-scc.yaml"
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
            name: postgresql-keycloak
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
          - system:serviceaccount:security:default
          volumes:
          - configMap
          - downwardAPI
          - emptyDir
          - persistentVolumeClaim
          - projected
          - secret
          ```

2. Create PostgreSQL admin secret:

  ```bash
  kubectl -n security create secret generic keycloak-postgresql \
  --from-literal=password=<postgresql_password> \
  --from-literal=postgres-password=<postgresql_postgres_password>
  ```

3. Add a helm chart repository:

  ```bash
  helm repo add bitnami https://charts.bitnami.com/bitnami
  helm repo update
  ```

4. Install PostgreSQL v15.2.0 using [bitnami/postgresql](https://artifacthub.io/packages/helm/bitnami/postgresql) Helm chart v12.1.15:

  !!! info
      The PostgreSQL can be deployed in production ready mode. For example, it may include multiple replicas, persistent storage, autoscaling, and monitoring.
      For details, please refer to the [official Chart documentation](https://github.com/bitnami/charts/tree/master/bitnami/postgresql).

  ```bash
  helm install postgresql bitnami/postgresql \
  --version 12.1.15 \
  --values values.yaml \
  --namespace security
  ```

  Check out the *values.yaml* file sample of the PostgreSQL customization:
  <details>
  <summary><b>View: values.yaml</b></summary>
  ```yaml
  # PostgreSQL read only replica parameters
  readReplicas:
    # Number of PostgreSQL read only replicas
    replicaCount: 1

  image:
    tag: 15.2.0-debian-11-r0

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
  ```
  </details>

## Keycloak Installation

To install Keycloak, follow the steps below:

1. Use `security` namespace from the PostgreSQL installation.

2. Add a chart repository:

  ```bash
  helm repo add codecentric https://codecentric.github.io/helm-charts
  helm repo update
  ```

3. Create Keycloak admin secret:

  ```bash
  kubectl -n security create secret generic keycloak-admin-creds \
  --from-literal=username=<keycloak_admin_username> \
  --from-literal=password=<keycloak_admin_password>
  ```

4. Install Keycloak 20.0.3 using [codecentric/keycloakx](https://artifacthub.io/packages/helm/codecentric/keycloakx) Helm chart:

  !!! info
      Keycloak can be deployed in production ready mode. For example, it may include multiple replicas, persistent storage, autoscaling, and monitoring.
      For details, please refer to the [official Chart documentation](https://github.com/codecentric/helm-charts/tree/master/charts/keycloakx).

  ```bash
  helm install keycloakx codecentric/keycloakx \
  --version 2.2.1 \
  --values values.yaml \
  --namespace security
  ```

  Check out the *values.yaml* file sample of the Keycloak customization:

  ??? note "View: values.yaml"

      ```yaml
      replicas: 1

      # Deploy the latest version
      image:
        tag: "20.0.3"

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

      # The following parameter is unrecommended to expose. Exposed health checks lead to an unnecessary attack vector.
      health:
        enabled: false
      # The following parameter is unrecommended to expose. Exposed metrics lead to an unnecessary attack vector.
      metrics:
        enabled: false

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
        # The following parameter is unrecommended to expose. Admin paths lead to an unnecessary attack vector.
        console:
          enabled: false
        rules:
          - host: keycloak.<ROOT_DOMAIN>
            paths:
              - path: '{{ tpl .Values.http.relativePath $ | trimSuffix "/" }}/'
                pathType: Prefix

      # This block should be uncommented if you set Keycloak to OpenShift and change the host field
      # route:
      #   enabled: false
      #   # Path for the Route
      #   path: '/'
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

## Configuration

To prepare Keycloak for integration with EDP, follow the steps below:

1. Ensure that the `openshift` realm is created.

2. Create the `edp_<EDP_PROJECT>` user and set the password in the `Master` realm.

  !!! note
      This user should be used by EDP to access Keycloak. Please refer to the [Install EDP](install-edp.md) and [Install EDP via Helmfile](install-via-helmfile.md) sections for details.

3. In the `Role Mapping` tab, assign the proper roles to the user:

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

## Related Articles

* [Install EDP](install-edp.md)
* [Install via Helmfile](install-via-helmfile.md)
* [Install Harbor](install-harbor.md)
