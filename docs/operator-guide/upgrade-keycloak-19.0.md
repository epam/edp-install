# Upgrade Keycloak v17.0 to 19.0

Starting from Keycloak v.18.x.x, the Keycloak server has been moved from the Wildfly (JBoss) Application Server to [Quarkus](https://quarkus.io/) framework and is called Keycloak.X.

There are two ways to upgrade Keycloak v.17.0.x-legacy to v.19.0.x on Kubernetes, please perform the steps described in the [Prerequisites](#Prrqsts) section of this tutorial, and then select a suitable upgrade strategy for your environment:

* [Upgrade Postgres database to a minor release v.11.17](#KPU)
* [Migrate Postgres database from Postgres v.11.x to v.14.5](#KPM)

## Prerequisites <a name="Prrqsts"></a>

Before upgrading Keycloak, please perform the steps below:

1. Create a backup/snapshot of the Keycloak database volume. Locate the AWS `volumeID` and then create its [snapshot](https://docs.aws.amazon.com/AWSEC2/latest/UserGuide/ebs-creating-snapshot.html) on AWS:

  * Find the `PVC` name attached to the Postgres pod. It can be similar to `data-keycloak-postgresql-0` if the Postgres `StatefulSet` name is `keycloak-postgresql`:

    ```bash
    kubectl get pods keycloak-postgresql-0 -n security -o jsonpath='{.spec.volumes[*].persistentVolumeClaim.claimName}{"\n"}'
    ```

  * Locate the `PV` `volumeName` in the `data-keycloak-postgresql-0` Persistent Volume Claim:

    ```bash
    kubectl get pvc data-keycloak-postgresql-0 -n security -o jsonpath='{.spec.volumeName}{"\n"}'
    ```

  * Get `volumeID` in the Persistent Volume:

    ```bash
    kubectl get pv ${pv_name} -n security -o jsonpath='{.spec.awsElasticBlockStore.volumeID}{"\n"}'
    ```

2. Add two additional keys: `password` and `postgres-password`, to the `keycloak-postgresql` secret in the Keycloak namespace.

  !!! Note
      * The `password` key must have the same value as the `postgresql-password` key.
      * The `postgres-password` key must have the same value as the `postgresql-postgres-password` key.

  The latest chart for Keycloak.X does not have an option to override Postgres password and admin password keys in the secret, and it uses the Postgres [defaults](https://github.com/codecentric/helm-charts/blob/master/charts/keycloakx/values.yaml#L371), therefore, a new secret scheme must be implemented:

    ```bash
    kubectl -n security edit secret keycloak-postgresql
    ```

    ```yaml
    data:
      postgresql-password: XXXXXX
      postgresql-postgres-password: YYYYYY
      password: XXXXXX
      postgres-password: YYYYYY
    ```

3. Save Keycloak `StatefulSet` names, for example, `keycloak` and `keycloak-postgresql`. These names will be used in the new Helm deployments:

  ```bash
  $ kubectl get statefulset -n security
  NAME                  READY   AGE
  keycloak              1/1     18h
  keycloak-postgresql   1/1     18h
  ```

## Upgrade Postgres Database to a Minor Release v.11.17 <a name="KPU"></a>

To upgrade Keycloak by upgrading [Postgres Database](https://www.postgresql.org/) to a minor release v.11.17, perform the steps described in the [Prerequisites](#Prrqsts) section of this tutorial, and then perform the following steps:

### Delete Keycloak Resources

1. Delete `Keycloak` and `Prostgres` `StatefulSets`:

  ```bash
  kubectl delete statefulset keycloak keycloak-postgresql -n security
  ```

2. Delete the Keycloak `Ingress`object, to prevent hostname duplication issues:

  ```bash
  kubectl delete ingress keycloak -n security
  ```

### Upgrade Keycloak

1. Make sure the Keycloak chart repository is added:

  ```bash
  helm repo add codecentric https://codecentric.github.io/helm-charts
  helm repo update
  ```

2. Create values for Keycloak:

  !!! Note
      Since the Keycloak.X release, Keycloak  and Postgres database charts are separated.
      Upgrade Keycloak, and then install the Postgres database.

  !!! Note
      * `nameOverride: "keycloak"` sets the name of the Keycloak pod. It must be the same Keycloak name as in the previous `StatefulSet`.
      * Change Ingress host name to the Keycloak host name.
      * `hostname: keycloak-postgresql` is the hostname of the pod with the Postgres database that is the same as Postgres StatefulSet name, for example, `keycloak-postgresql`.
      * `"/opt/keycloak/bin/kc.sh start --auto-build"` was used in the legacy Keycloak version. However, it is no longer required in the new Keycloak version since it is [deprecated](https://www.keycloak.org/docs/latest/upgrading/index.html#changes-to-the-server-configuration-and-startup) and used by default.
      * Optionally, use the following command for applying the old Keycloak theme:

          ```bash
          bin/kc.sh start --features-disabled=admin2
          ```
  <details>
  <summary><b>View: keycloak-values.yaml</b></summary>

  ```yaml
  nameOverride: "keycloak"

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
    hostname: keycloak-postgresql
    port: 5432
    username: admin
    database: keycloak
  ```
  </details>

3. Upgrade the Keycloak Helm chart:

  !!! Note
      * The Helm chart is substituted with the new Keyacloak.X instance.
      * Change the namespace and the values file name if required.

  ```bash
  helm upgrade keycloak codecentric/keycloakx --version 1.6.0 --values keycloak-values.yaml -n security
  ```

  !!! Note
      If there are error messages when upgrading via Helm, make sure that `StatefulSets` are removed. If they are removed and the error still persists, try to add the `--force` flag to the Helm command:

      ```bash
      helm upgrade keycloak codecentric/keycloakx --version 1.6.0 --values keycloak-values.yaml -n security --force
      ```

### Install Postgres

1. Add Bitnami chart repository and update Helm repos:

  ```bash
  helm repo add bitnami https://charts.bitnami.com/bitnami
  helm repo update
  ```

2. Create values for Postgres:

  !!! Note
      * Postgres v.11 and Postgres v.14.5 are not compatible.
      * Postgres image will be upgraded to a minor release v.11.17.
      * `fullnameOverride: "keycloak-postgresql"` sets the name of the Postgres StatefulSet. It must be the same as in the previous `StatefulSet`.

  <details>
  <summary><b>View: postgres-values.yaml</b></summary>

  ```yaml
  fullnameOverride: "keycloak-postgresql"

  # PostgreSQL read only replica parameters
  readReplicas:
    # Number of PostgreSQL read only replicas
    replicaCount: 1

  global:
    postgresql:
      auth:
        username: admin
        existingSecret: keycloak-postgresql
        secretKeys:
          adminPasswordKey: postgres-password
          userPasswordKey: password
        database: keycloak

  image:
    registry: docker.io
    repository: bitnami/postgresql
    tag: 11.17.0-debian-11-r3

  auth:
    existingSecret: keycloak-postgresql
    secretKeys:
      adminPasswordKey: postgres-password
      userPasswordKey: password

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

3. Install the Postgres database chart:

  !!! Note
      Change the namespace and the values file name if required.

  ```bash
  helm install postgresql bitnami/postgresql \
  --version 11.7.6 \
  --values postgres-values.yaml \
  --namespace security
  ```

4. Log in to Keycloak and check that everything works as expected.

### Clean and Analyze Database

Optionally, run the [vacuumdb](https://www.postgresql.org/docs/current/app-vacuumdb.html) application on the database, to recover space occupied by "dead tuples" in the tables, analyze the contents of database tables, and collect statistics for PostgreSQL query engine to improve performance:

```bash
PGPASSWORD="${postgresql_postgres-password}" vacuumdb --analyze --verbose -d keycloak -U postgres
```
For all databases, run the following command:

```bash
PGPASSWORD="${postgresql_postgres-password}" vacuumdb --analyze --verbose --all -U postgres
```

## Migrate Postgres Database From Postgres v.11.x to v.14.5 <a name="KPM"></a>

!!! Info
    There is a [Postgres database migration script](#Pdms) at the end of this tutorial. Please read the section below before using the script.

To upgrade Keycloak by migrating Postgres database from Postgres v.11.x to v.14.5, perform the steps described in the [Prerequisites](#Prrqsts) section of this tutorial, and then perform the following steps:

### Export Postgres Databases

1. Log in to the current Keycloak Postgres pod and create a logical backup of all roles and databases using the [pg_dumpall](https://www.postgresql.org/docs/current/app-pg-dumpall.html) application. If there is no access to the Postgres Superuser, backup the Keycloak database with the [pg_dump](https://www.postgresql.org/docs/current/app-pgdump.html) application:

  !!! Note
      * The secret key `postgresql-postgres-password` is for the `postgres` Superuser and `postgresql-password` is for `admin` user. The `admin` user is indicated by default in the Postgres Helm chart.<br>
      The `admin` user may not have enough permissions to dump all Postgres databases and roles, so the preferred option for exporting all objects is using the `pg_dumpall` tool with the `postgres` Superuser.<br>
      * If the `PGPASSWORD` variable is not specified before using the `pg_dumpall` tool, you will be prompted to enter a password for each database during the export.
      * If the `-l keycloak` parameter is specified, `pg_dumpall` will connect to the `keycloak` database for dumping global objects and discovering what other databases should be dumped. By default, `pg_dumpall` will try to connect to `postgres` or `template1` databases. This parameter is optional.
      * The `pg_dumpall --clean` option adds SQL commands to the dumped file for dropping databases before recreating them during import, as well as `DROP` commands for roles and tablespaces (`pg_dump` also has this option). If the `--clean` parameter is specified, connect to the `postgres` database initially during import via `psql`. The `psql` script will attempt to drop other databases immediately, and that will fail for the database you are connected to. This flag is optional, and it is not included into this tutorial.

  ```bash
  PGPASSWORD="${postgresql_postgres-password}" pg_dumpall -h localhost -p 5432 -U postgres -l keycloak > /tmp/keycloak_wildfly_db_dump.sql
  ```

  !!! Note
      If there is no working password for the `postgres` Superuser, try the `admin` user using the [pg_dump](https://www.postgresql.org/docs/current/app-pgdump.html) tool to export the `keycloak` database without global roles:

      ```bash
      PGPASSWORD="${postgresql_password}" pg_dump -h localhost -p 5432 -U admin -d keycloak > /tmp/keycloak_wildfly_db_dump.sql
      ```

  !!! Info
      Double-check that the contents of the dumped file is not empty. It usually contains more than 4000 lines.

2. Copy the file with the database dump to a local machine. Since `tar` may not be present in the pod and `kubectl cp` will not work without `tar`, use the following command:

  ```bash
  kubectl exec -n security ${postgresql_pod} -- cat /tmp/keycloak_wildfly_db_dump.sql  > keycloak_wildfly_db_dump.sql
  ```

  !!! Note
      Please find below the alternative commands for exporting the database to the local machine without copying the file to a pod for Postgres and admin users:

      ```bash
      kubectl exec -n security ${postgresql_pod} "--" sh -c "PGPASSWORD='"${postgresql_postgres-password}"' pg_dumpall -h localhost -p 5432 -U postgres" > keycloak_wildfly_db_dump.sql
      kubectl exec -n security ${postgresql_pod} "--" sh -c "PGPASSWORD='"${postgresql_password}"' pg_dump -h localhost -p 5432 -U admin -d keycloak" > keycloak_wildfly_db_dump.sql
      ```

3. Delete the dumped file from the pod for security reasons:

  ```bash
  kubectl exec -n security ${postgresql_pod} "--" sh -c "rm /tmp/keycloak_wildfly_db_dump.sql"
  ```

### Delete Keycloak Resources

1. Delete all previous Keycloak resources along with the Postgres database and keycloak `StatefulSets`, `Ingress`, and custom resources via Helm, or via the tool used for their deployment.

  ```bash
  helm list -n security
  helm delete keycloak -n security
  ```

  !!! Warning
      Don't delete the whole namespace. Keep the `keycloak-postgresql` and `keycloak-admin-creds` secrets.

2. Delete the volume in AWS, from which a snapshot has been created. Then delete the PVC:

  ```bash
  kubectl delete pvc data-keycloak-postgresql-0 -n security
  ```

### Install Postgres

1. Add Bitnami chart repository and update Helm repos:

  ```bash
  helm repo add bitnami https://charts.bitnami.com/bitnami
  helm repo update
  ```

2. Create Postgres values:

  !!! Note
      `fullnameOverride: "keycloak-postgresql"` sets the name of the Postgres StatefulSet. It must be same as in the previous `StatefulSet`.

  <details>
  <summary><b>View: postgres-values.yaml</b></summary>

  ```yaml
  nameOverride: "keycloak-postgresql"

  # PostgreSQL read only replica parameters
  readReplicas:
    # Number of PostgreSQL read only replicas
    replicaCount: 1

  global:
    postgresql:
      auth:
        username: admin
        existingSecret: keycloak-postgresql
        secretKeys:
          adminPasswordKey: postgres-password
          userPasswordKey: password
        database: keycloak

  auth:
    existingSecret: keycloak-postgresql
    secretKeys:
      adminPasswordKey: postgres-password
      userPasswordKey: password

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

3. Install the Postgres database:

  !!! Note
      Change the namespace and the values file name if required.

  ```bash
  helm install postgresql bitnami/postgresql \
  --version 11.7.6 \
  --values postgres-values.yaml \
  --namespace security
  ```

4. Wait for the database to be ready.

### Import Postgres Databases

1. Upload the database dump to the new Keycloak Postgres pod:

  ```bash
  cat keycloak_wildfly_db_dump.sql | kubectl exec -i -n security ${postgresql_pod} "--" sh -c "cat > /tmp/keycloak_wildfly_db_dump.sql"
  ```

  !!! Warning
      Database import must be done before deploying Keycloak, because Keycloak will write its own data to the database during the start, and the import will partially fail.<br>
      If that happened, scale down the keycloak `StatefulSet`, and try to drop the Keycloak database in the Postgres pod:

      ```bash
      dropdb -i -e keycloak -p 5432 -h localhost -U postgres
      ```

      If there still are some conflicting objects like roles, drop them via the [DROP ROLE](https://www.postgresql.org/docs/current/sql-droprole.html) command.

      If the previous steps do not help, downscale the Keycloak and Postgres `StatefulSets` and delete the attached `PVC` (save the `volumeID` before removing), and delete the volume on AWS if using `gp2-retain`. In case of using `gp2`, the volume will be deleted automatically after removing PVC. After that, redeploy the Postgres database, so that the new `PVC` is automatically created.

2. Import the [SQL dump](https://www.postgresql.org/docs/current/backup-dump.html) file to the Postgres database cluster:

  !!! Info
      Since the databases were exported in the `sql` format, the [psql](https://www.postgresql.org/docs/current/app-psql.html) tool will be used to restore (reload) them. [pg_restore](https://www.postgresql.org/docs/current/app-pgrestore.html) does not support this plain-text format.

  * If the entire Postgres database cluster was migrated with the `postgres` Superuser using `pg_dumpall`, use the import command without indicating the database:

    ```bash
    psql -U postgres -f /tmp/keycloak_wildfly_db_dump.sql
    ```

  * If the database was migrated with the `admin` user using `pg_dump`, the `postgres` Superuser still can be used to restore it, but, in this case, a database must be indicated:

    !!! Warning
        If the database name was not indicated during the import for the file dumped with `pg_dump`, the `psql` tool will import this database to a default Postgres database called `postgres`.

    ```bash
    psql -U postgres -d keycloak -f /tmp/keycloak_wildfly_db_dump.sql
    ```

  * If the `postgres` Superuser is not accessible in the Postgres pod, run the command under the `admin` or any other user that has the database permissions. In this case, indicate the database as well:

    ```bash
    psql -U admin -d keycloak -f /tmp/keycloak_wildfly_db_dump.sql
    ```

3. After a successful import, delete the dump file from the pod for security reasons:

  ```bash
  kubectl exec -n security ${postgresql_pod} "--" sh -c "rm /tmp/keycloak_wildfly_db_dump.sql"
  ```

  !!! Note
      Please find below the alternative commands for importing the database from the local machine to the pod without storing the backup on a pod for `postgres` or `admin` users:

      ```bash
      cat "keycloak_wildfly_db_dump.sql" | kubectl exec -i -n "${keycloak_namespace}" "${postgres_pod_name}" "--" sh -c "cat | PGPASSWORD='"${postgresql_superuser_password}"' psql -h "${db_host}" -p "${db_port}" -U "${postgres_username}""
      cat "keycloak_wildfly_db_dump.sql" | kubectl exec -i -n "${keycloak_namespace}" "${postgres_pod_name}" "--" sh -c "cat | PGPASSWORD='"${postgresql_superuser_password}"' psql -h "${db_host}" -p "${db_port}" -U "${postgres_username}" -d "${database_name}""
      cat "keycloak_wildfly_db_dump.sql" | kubectl exec -i -n "${keycloak_namespace}" "${postgres_pod_name}" "--" sh -c "cat | PGPASSWORD='"${postgresql_admin_password}"' psql -h "${db_host}" -p "${db_port}" -U "${postgres_username}" -d "${database_name}""
      ```

### Install Keycloak

1. Make sure the Keycloak chart repository is added:

  ```bash
  helm repo add codecentric https://codecentric.github.io/helm-charts
  helm repo update
  ```

2. Create Keycloak values:

  !!! Note
      * `nameOverride: "keycloak"` sets the name of the Keycloak pod. It must be the same Keycloak name as in the previous `StatefulSet`.
      * Change Ingress host name to the Keycloak host name.
      * `hostname: keycloak-postgresql` is the hostname of the pod with the Postgres database that is the same as Postgres StatefulSet name, for example, `keycloak-postgresql`.
      * `"/opt/keycloak/bin/kc.sh start --auto-build"` was used in the legacy Keycloak version. However, it is no longer required in the new Keycloak version since it is [deprecated](https://www.keycloak.org/docs/latest/upgrading/index.html#changes-to-the-server-configuration-and-startup) and used by default.
      * Optionally, use the following command for applying the old Keycloak theme:

          ```bash
          bin/kc.sh start --features-disabled=admin2
          ```

  !!! Info
      [Automatic database migration](https://www.keycloak.org/docs/latest/upgrading/index.html#automatic-relational-database-migration) will start after the Keycloak installation.

  <details>
  <summary><b>View: keycloak-values.yaml</b></summary>

  ```yaml
  nameOverride: "keycloak"

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
    hostname: keycloak-postgresql
    port: 5432
    username: admin
    database: keycloak
  ```
  </details>

3. Deploy Keycloak:

  !!! Note
      Change the namespace and the values file name if required.

  ```bash
  helm install keycloak codecentric/keycloakx --version 1.6.0 --values keycloak-values.yaml -n security
  ```

4. Log in to Keycloak and check if everything has been imported correctly.

### Clean and Analyze Database

Optionally, run the [vacuumdb](https://www.postgresql.org/docs/current/app-vacuumdb.html) application on the database, to analyze the contents of database tables and collect statistics for the Postgres query optimizer:

```bash
PGPASSWORD="${postgresql_postgres-password}" vacuumdb --analyze --verbose -d keycloak -U postgres
```
For all databases, run the following command:

```bash
PGPASSWORD="${postgresql_postgres-password}" vacuumdb --analyze --verbose --all -U postgres
```

### Postgres Database Migration Script<a name="Pdms"></a>

!!! Info
    Please read the [Migrate Postgres Database From Postgres v.11.x to v.14.5](#KPM) section of this tutorial before using the script.

!!! Note
    * The [`kubectl`](https://github.com/kubernetes/kubectl) tool is required for using this script.
    * This script will likely work for any other Postgres database besides Keycloak after some adjustments. It queries the `pg_dump`, `pg_dumpall`, `psql`, and `vacuumdb` commands under the hood.

The following script can be used for exporting and importing Postgres databases as well as optimizing them with the [vacuumdb](https://www.postgresql.org/docs/current/app-vacuumdb.html) application. Please examine the code and make the adjustments if required.

* By default, the following command exports Keycloak Postgres databases from a Kubernetes pod to a local machine:

      ./script.sh

  After running the command, please follow the prompt.

* To import a database backup to a newly created Postgres Kubernetes pod, pass a database dump sql file to the script:

      ./script.sh path-to/db_dump.sql

* The `-h` flag prints help, and `-c|-v` runs the `vacuumdb` garbage collector and analyzer.

<details>
<summary><b>View: keycloak_db_migration.sh</b></summary>

```bash
#!/bin/bash

# set -x

db_migration_help(){
    echo "Keycloak Postgres database migration"
    echo
    echo "Usage:"
    echo "------------------------------------------"
    echo "Export Keycloak Postgres database from pod"
    echo "Run without parameters:"
    echo "      $0"
    echo "------------------------------------------"
    echo "Import Keycloak Postgres database to pod"
    echo "Pass filename to script:"
    echo "      $0 path/to/db_dump.sql"
    echo "------------------------------------------"
    echo "Additional options: "
    echo "      $0 [OPTIONS...]"
    echo "Options:"
    echo "h     Print Help."
    echo "c|v   Run garbage collector and analyzer."
}

keycloak_ns(){
    printf '%s\n' 'Enter keycloak namespace: '
    read -r keycloak_namespace

    if [ -z "${keycloak_namespace}" ]; then
        echo "Don't skip namespace"
        exit 1
    fi
}

postgres_pod(){
    printf '%s\n' 'Enter postgres pod name: '
    read -r postgres_pod_name

    if [ -z "${postgres_pod_name}" ]; then
        echo "Don't skip pod name"
        exit 1
    fi
}

postgres_user(){
    printf '%s\n' 'Enter postgres username: '
    printf '%s' "Skip to use [postgres] superuser: "
    read -r postgres_username

    if [ -z "${postgres_username}" ]; then
        postgres_username='postgres'
    fi
}

pgdb_host_info(){
    database_name='keycloak'
    db_host='localhost'
    db_port='5432'
}

postgresql_admin_pass(){
    postgresql_password='POSTGRES_PASSWORD'
    postgresql_admin_password="$(kubectl exec -n "${keycloak_namespace}" "${postgres_pod_name}" "--" \
        sh -c "printenv ${postgresql_password}")"
}

postgresql_su_pass(){
    postgresql_postgres_password='POSTGRES_POSTGRES_PASSWORD'
    postgresql_superuser_password="$(kubectl exec -n "${keycloak_namespace}" "${postgres_pod_name}" "--" \
        sh -c "printenv ${postgresql_postgres_password}")"

    if [ -z "${postgresql_superuser_password}" ]; then
        echo "SuperUser password variable does not exist. Using user password instead..."
        postgresql_admin_pass
        postgresql_superuser_password="${postgresql_admin_password}"
    fi
}

keycloak_pgdb_export(){
    current_cluster="$(kubectl config current-context | tr -dc '[:alnum:]-')"
    exported_db_name="keycloak_db_dump_${current_cluster}_${keycloak_namespace}_${postgres_username}_$(date +"%Y%m%d%H%M").sql"

    if [ "${postgres_username}" == 'postgres' ]; then
        # call a function to get a pass for postgres user
        postgresql_su_pass
        kubectl exec -n "${keycloak_namespace}" "${postgres_pod_name}" "--" \
            sh -c "PGPASSWORD='"${postgresql_superuser_password}"' pg_dumpall -h "${db_host}" -p "${db_port}" -U "${postgres_username}"" > "${exported_db_name}"
    else
        # call a function to get a pass for admin user
        postgresql_admin_pass
        kubectl exec -n "${keycloak_namespace}" "${postgres_pod_name}" "--" \
            sh -c "PGPASSWORD='"${postgresql_admin_password}"' pg_dump -h "${db_host}" -p "${db_port}" -U "${postgres_username}" -d "${database_name}"" > "${exported_db_name}"
    fi

    separate_lines="---------------"

    if [ ! -s "${exported_db_name}" ]; then
        rm -f "${exported_db_name}"
        echo "${separate_lines}"
        echo "Something went wrong. The database dump file is empty and was not saved."
    else
        echo "${separate_lines}"
        grep 'Dumped' "${exported_db_name}" | sort -u
        echo "Database has been exported to $(pwd)/${exported_db_name}"
    fi
}

keycloak_pgdb_import(){
    echo "Preparing Import"
    echo "----------------"

    if [ ! -f "$1" ]; then
        echo "The file $1 does not exist."
        exit 1
    fi

    keycloak_ns
    postgres_pod
    postgres_user
    pgdb_host_info

    if [ "${postgres_username}" == 'postgres' ]; then
        # restore full backup with all databases and roles as superuser or a single database
        postgresql_su_pass
        if [ -n "$(cat "$1" | grep 'CREATE ROLE')" ]; then
            cat "$1" | kubectl exec -i -n "${keycloak_namespace}" "${postgres_pod_name}" "--" \
                sh -c "cat | PGPASSWORD='"${postgresql_superuser_password}"' psql -h "${db_host}" -p "${db_port}" -U "${postgres_username}""
        else
            cat "$1" | kubectl exec -i -n "${keycloak_namespace}" "${postgres_pod_name}" "--" \
                sh -c "cat | PGPASSWORD='"${postgresql_superuser_password}"' psql -h "${db_host}" -p "${db_port}" -U "${postgres_username}" -d "${database_name}""
        fi
    else
        # restore a single database
        postgresql_admin_pass
        cat "$1" | kubectl exec -i -n "${keycloak_namespace}" "${postgres_pod_name}" "--" \
            sh -c "cat | PGPASSWORD='"${postgresql_admin_password}"' psql -h "${db_host}" -p "${db_port}" -U "${postgres_username}" -d "${database_name}""
    fi
}

vacuum_pgdb(){
    echo "Preparing garbage collector and analyzer"
    echo "----------------------------------------"

    keycloak_ns
    postgres_pod
    postgres_user
    pgdb_host_info

    if [ "${postgres_username}" == 'postgres' ]; then
        postgresql_su_pass
        kubectl exec -n "${keycloak_namespace}" "${postgres_pod_name}" "--" \
            sh -c "PGPASSWORD='"${postgresql_superuser_password}"' vacuumdb --analyze --all -h "${db_host}" -p "${db_port}" -U "${postgres_username}""
    else
        postgresql_admin_pass
        kubectl exec -n "${keycloak_namespace}" "${postgres_pod_name}" "--" \
            sh -c "PGPASSWORD='"${postgresql_admin_password}"' vacuumdb --analyze -h "${db_host}" -p "${db_port}" -U "${postgres_username}" -d "${database_name}""
    fi
}

while [ "$#" -eq 1 ]; do
    case "$1" in
        -h | --help)
            db_migration_help
            exit 0
            ;;
        -c | --clean | -v | --vacuum)
            vacuum_pgdb
            exit 0
            ;;
        --)
            break
            ;;
        -*)
            echo "Invalid option '$1'. Use -h|--help to see the valid options" >&2
            exit 1
            ;;
        *)
            keycloak_pgdb_import "$1"
            exit 0
            ;;
    esac
    shift
done

if [ "$#" -gt 1 ]; then
    echo "Please pass a single file to the script"
    exit 1
fi

echo "Preparing Export"
echo "----------------"
keycloak_ns
postgres_pod
postgres_user
pgdb_host_info
keycloak_pgdb_export
```
</details>

## Related Articles

* [Deploy OKD 4.10 Cluster](deploy-okd-4.10.md)
