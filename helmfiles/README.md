<!-- TOC -->

- [Install via Helmfile](#install-via-helmfile)
  - [Prerequisites](#prerequisites)
  - [Helmfile Structure](#helmfile-structure)
  - [Deploy Components](#deploy-components)
    - [Deploy NGINX Ingress Controller](#deploy-nginx-ingress-controller)
    - [Deploy Keycloak](#deploy-keycloak)
    - [Deploy EPAM Delivery Platform](#deploy-epam-delivery-platform)
    - [Deploy Argo CD](#deploy-argo-cd)
    - [Deploy External Secrets Operator](#deploy-external-secrets-operator)
    - [Deploy DefectDojo](#deploy-defectdojo)
    - [Deploy ReportPortal](#deploy-reportportal)
  - [Operate Helmfile](#operate-helmfile)
    - [Related Articles](#related-articles)

<!-- /TOC -->

# Install via Helmfile

This article provides the instruction on how to deploy EDP and components in Kubernetes using [Helmfile](https://github.com/helmfile/helmfile) that is intended for deploying Helm charts. Helmfile templates are available in [GitHub repository](https://github.com/epam/edp-install/tree/master/helmfiles).

## Prerequisites

* [Helm](https://helm.sh) version 3.6.0+ is installed. Please refer to the [Helm](https://github.com/helm/helm/releases/tag/v3.6.0) page on GitHub for details.
* Helmfile version 0.142.0 is installed. Please refer to the [GitHub](https://github.com/helmfile/helmfile) page for details.
* Helm diff plugin version 3.5.0 is installed. Please refer to the [GitHub](https://github.com/databus23/helm-diff) page for details.
* Helm-git plugin version 0.11.4 is installed. Please refer to the [GitHub](https://github.com/aslafy-z/helm-git) page for details.

## Helmfile Structure

* The `envs/common.yaml` file contains the specification for environments pattern, list of helm repositories from which it is necessary to fetch the helm charts and additional Helm parameters.
* The `envs/platform.yaml` file contains global parameters that are used in various Helmfiles.
* The `releases/envs/` contains symbol links to environments files.
* The `releases/*.yaml` file contains description of parameters that is used when deploying a Helm chart.
* The `helmfile.yaml` file defines components to be installed by defining a path to Helm releases files.
* The `envs/ci.yaml` file contains stub parameters for CI linter.
* The `test/lint-ci.sh` script for running CI linter with debug loglevel and stub parameters.

## Deploy Components

Using the Helmfile, the following components can be installed:

* [NGINX Ingress Controller](https://github.com/kubernetes/ingress-nginx/tree/master/charts/ingress-nginx)
* [Keycloak](https://github.com/codecentric/helm-charts/tree/master/charts/keycloak)
* [EPAM Delivery Platform](https://github.com/epam/edp-install/tree/master/deploy-templates)
* [Argo CD](https://github.com/argoproj/argo-helm/tree/master/charts/argo-cd)
* [External Secrets Operator](https://github.com/external-secrets/external-secrets/tree/main/deploy/charts/external-secrets)
* [DefectDojo](https://github.com/DefectDojo/django-DefectDojo/tree/master/helm/defectdojo)
* [ReportPortal](https://github.com/reportportal/kubernetes/tree/develop/reportportal)

### Deploy NGINX Ingress Controller

To install NGINX Ingress controller, follow the steps below:

1. In the `releases/nginx-ingress.yaml` file, set the `proxy-real-ip-cidr` parameter according to the value with AWS VPC IPv4 CIDR.

2. Install NGINX Ingress controller:

   ```bash
   helmfile  --selector component=ingress --environment platform -f helmfile.yaml apply
   ```
### Deploy Keycloak

To install Keycloak, follow the steps below:

1. Create a `security` namespace:

   ```bash
   kubectl create namespace security
   ```

2. Create PostgreSQL admin secret:

   ```bash
   kubectl -n security create secret generic keycloak-postgresql \
   --from-literal=password=<postgresql_password> \
   --from-literal=postgres-password=<postgresql_postgres_password>
   ```

3. In the `envs/platform.yaml` file, set the `dnsWildCard` parameter.

4. Create Keycloak admin secret:

   ```bash
   kubectl -n security create secret generic keycloak-admin-creds \
   --from-literal=username=<keycloak_admin_username> \
   --from-literal=password=<keycloak_admin_password>
   ```

5. Install the custom SecurityContextConstraints (only for OpenShift):

  > **NOTE**: If you use OpenShift as your deployment platform, add *customsecuritycontextconstraints.yaml*.

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

6. Install Keycloak:

   ```bash
   helmfile  --selector component=sso --environment platform -f helmfile.yaml apply
   ```

### Deploy EPAM Delivery Platform

To install EDP, follow the steps below:

1. Create a `platform` namespace:

   ```bash
   kubectl create namespace platform
   ```

2. For EDP, it is required to have Keycloak access to perform the integration. Create a secret with the user and password provisioned in the step 2 of the [Keycloak Configuration](https://epam.github.io/edp-install/operator-guide/install-keycloak/#configuration) section.

   ```bash
   kubectl -n platform create secret generic keycloak \
   --from-literal=username=<username> \
   --from-literal=password=<password>
   ```

3. In the `envs/platform.yaml` file, set the `edpName` and `keycloakEndpoint` parameters.

4. In the `releases/edp-install.yaml` file, check and fill in all values.

5. Install EDP:

   ```bash
   helmfile  --selector component=edp --environment platform -f helmfile.yaml apply
   ```

### Deploy Argo CD

To install Argo CD, follow the steps below:

1. Install Argo CD:

   ```bash
   helmfile  --selector component=argocd --environment platform -f helmfile.yaml apply
   ```

2. Update the `argocd-secret` secret (in the Argo CD namespace) by providing the correct Keycloak client secret (`oidc.keycloak.clientSecret`) with the value from the `keycloak-client-argocd-secret` secret in EDP namespace, and restart the deployment:

  ```bash
  ARGOCD_CLIENT=$(kubectl -n platform get secret keycloak-client-argocd-secret  -o jsonpath='{.data.clientSecret}')
  kubectl -n argocd patch secret argocd-secret -p="{\"data\":{\"oidc.keycloak.clientSecret\": \"${ARGOCD_CLIENT}\"}}" -v=1
  kubectl -n argocd rollout restart deployment argo-argocd-server
  ```

### Deploy External Secrets Operator

To install External Secrets Operator, follow the steps below:

  ```bash
  helmfile  --selector component=secrets --environment platform -f helmfile.yaml apply
  ```

### Deploy DefectDojo

> **NOTE**: It is also possible to install DefectDojo via Helm Chart. For details, please refer to the [Install DefectDojo](https://epam.github.io/edp-install/operator-guide/install-defectdojo) page.

To install DefectDojo via Helmfile, follow the steps below:

1. Create a DefectDojo namespace:

  ```bash
  kubectl create namespace defectdojo
  ```

2. Create a PostgreSQL admin secret:

  ```bash
  kubectl -n defectdojo create secret generic defectdojo-postgresql-specific \
  --from-literal=postgresql-password=<postgresql_password> \
  --from-literal=postgresql-postgres-password=<postgresql_postgres_password>
  ```

  > **NOTE**: The `postgresql_password` and `postgresql_postgres_password` passwords must be 16 characters long.

3. Create a RabbitMQ admin secret:

  ```bash
  kubectl -n defectdojo create secret generic defectdojo-rabbitmq-specific \
  --from-literal=rabbitmq-password=<rabbitmq_password> \
  --from-literal=rabbitmq-erlang-cookie=<rabbitmq_erlang_cookie>
  ```

  > **NOTE**: The `rabbitmq_password` password must be 10 characters long, and the `rabbitmq_erlang_cookie` password must be 32 characters long.

4. Create a DefectDojo admin secret:

  ```bash
  kubectl -n defectdojo create secret generic defectdojo \
  --from-literal=DD_ADMIN_PASSWORD=<dd_admin_password> \
  --from-literal=DD_SECRET_KEY=<dd_secret_key> \
  --from-literal=DD_CREDENTIAL_AES_256_KEY=<dd_credential_aes_256_key> \
  --from-literal=METRICS_HTTP_AUTH_PASSWORD=<metric_http_auth_password>
  ```

  > **NOTES**:
  > 
  > The `dd_admin_password` password must be 22 characters long. 
  > 
  > The `dd_secret_key` password must be 128 characters long.
  > 
  > The `dd_credential_aes_256_key` password must be 128 characters long. 
  > 
  > The `metric_http_auth_password` password must be 32 characters long.

5. In the `envs/platform.yaml` file, set the `dnsWildCard` parameter.

6. Install DefectDojo:

  ```bash
  helmfile  --selector component=defectdojo --environment platform -f helmfile.yaml apply
  ```

### Deploy ReportPortal

> **NOTE**: It is also possible to install ReportPortal via Helm Chart. For details, please refer to the [Install ReportPortal](https://epam.github.io/edp-install/operator-guide/install-reportportal/) page.

ReportPortal requires third-party deployments: RabbitMQ, ElasticSearch, PostgreSQL, MinIO.

To install third-party resources, follow the steps below:

1. Create a RabbitMQ admin secret:

  ```bash
  kubectl -n platform create secret generic reportportal-rabbitmq-creds \
  --from-literal=rabbitmq-password=<rabbitmq_password> \
  --from-literal=rabbitmq-erlang-cookie=<rabbitmq_erlang_cookie>
  ```

  > **WARNING**: The `rabbitmq_password` password must be 10 characters long, and the `rabbitmq_erlang_cookie` password must be 32 characters long.

2. Create a PostgreSQL admin secret:

  ```bash
  kubectl -n platform create secret generic reportportal-postgresql-creds \
  --from-literal=postgresql-password=<postgresql_password> \
  --from-literal=postgresql-postgres-password=<postgresql_postgres_password>
  ```

  > **WARNING**: The `postgresql_password` and `postgresql_postgres_password` passwords must be 16 characters long.

3. Create a MinIO admin secret:

  ```bash
  kubectl -n platform create secret generic reportportal-minio-creds \
  --from-literal=root-password=<root_password> \
  --from-literal=root-user=<root_user>
  ```

4. In the `envs/platform.yaml` file, set the `dnsWildCard` and `edpName` parameters.

5. Install third-party resources:

  ```bash
  helmfile --selector component=report-portal-third-party-resources --environment platform -f helmfile.yaml apply
  ```

6. After the rabbitmq pod gets the status Running, you need to configure the RabbitMQ memory threshold

  ```bash
  kubectl -n platform exec -it rabbitmq-0 -- rabbitmqctl set_vm_memory_high_watermark 0.8
  ```

To install ReportPortal via Helmfile, follow the steps below:

  ```bash
  helmfile --selector component=report-portal --environment platform -f helmfile.yaml apply
  ```

> **NOTES**:
>
> For user access: default/1q2w3e
>
> For admin access: superadmin/erebus
>
> Please refer to the [ReportPortal.io](https://reportportal.io/installation) page for details.

## Operate Helmfile

Before applying the Helmfile, please fill in the global parameters in the `envs/platform.yaml` and `releases/*.yaml` files for every Helm deploy.

Pay attention to the following recommendations while working with the Helmfile:

* To launch Lint, run the `test/lint-ci.sh` script.

* To show the difference between the deployed and environment state (helm diff), run the command:

      helmfile --environment platform -f helmfile.yaml diff

* To apply the deploy, run the command:

      helmfile  --selector component=ingress --environment platform -f helmfile.yaml apply

* To deploy components according to the label, use the selector to target a subset of releases when running the Helmfile. It can be useful when using large Helmfiles with releases that are logically grouped together. For example, to dispay the difference only for the nginx-ingress file, use the command:

      helmfile  --selector component=ingress --environment platform -f helmfile.yaml diff

* To destroy the release, run the command:

      helmfile  --selector component=ingress --environment platform -f helmfile.yaml destroy

### Related Articles

- [Install via Helmfile](https://epam.github.io/edp-install/operator-guide/install-via-helmfile/)
- [Install NGINX Ingress Controller](https://epam.github.io/edp-install/operator-guide/install-ingress-nginx/)
- [Install Keycloak](https://epam.github.io/edp-install/operator-guide/install-keycloak/)
- [Install EDP](https://epam.github.io/edp-install/operator-guide/install-edp/)
- [Install Argo CD](https://epam.github.io/edp-install/operator-guide/install-argocd/)
- [Install DefectDojo](https://epam.github.io/edp-install/operator-guide/install-defectdojo/)
- [Install ReportPortal](https://epam.github.io/edp-install/operator-guide/install-reportportal/)