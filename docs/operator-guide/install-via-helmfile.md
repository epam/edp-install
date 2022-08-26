# Install via Helmfile

This article provides the instruction on how to deploy EDP and components in Kubernetes using [Helmfile](https://github.com/helmfile/helmfile) that is intended for deploying Helm charts. Helmfile templates are available in [GitHub repository](https://github.com/epam/edp-install/tree/master/helmfiles).

## Prerequisites

* [Helm](https://helm.sh) version 3.6.0+ is installed. Please refer to the [Helm](https://github.com/helm/helm/releases/tag/v3.6.0) page on GitHub for details.
* Helmfile version 0.142.0 is installed. Please refer to the [GitHub](https://github.com/helmfile/helmfile) page for details.
* Helm diff plugin version 3.5.0 is installed. Please refer to the [GitHub](https://github.com/databus23/helm-diff) page for details.

## Helmfile Structure

* The `envs/common.yaml` file contains the specification for environments pattern, list of helm repositories from which it is necessary to fetch the helm charts and additional Helm parameters.
* The `envs/platform.yaml` file contains global parameters that are used in various Helmfiles.
* The `releases/envs/` contains symbol links to environments files.
* The `releases/*.yaml` file contains description of parameters that is used when deploying a Helm chart.
* The `helmfile.yaml` file defines components to be installed by defining a path to Helm releases files.
* The `envs/ci.yaml` file contains stub parameters for CI linter.
* The `test/lint-ci.sh` script for running CI linter with debug loglevel and stub parameters.
* The `resources/*.yaml` file contains additional resources for the OpenShift platform.

## Operate Helmfile

Before applying the Helmfile, please fill in the global parameters in the `envs/platform.yaml` (check the examples in the `envs/ci.yaml`) and `releases/*.yaml` files for every Helm deploy.

Pay attention to the following recommendations while working with the Helmfile:

* To launch Lint, run the `test/lint-ci.sh` script.

* Display the difference between the deployed and environment state (`helm diff`):

      helmfile --environment platform -f helmfile.yaml diff

* Apply the deployment:

      helmfile  --selector component=ingress --environment platform -f helmfile.yaml apply

* Modify the deployment and apply the changes:

      helmfile  --selector component=ingress --environment platform -f helmfile.yaml sync

* To deploy the components according to the label, use the selector to target a subset of releases when running the Helmfile. It can be useful for large Helmfiles with the releases that are logically grouped together. For example, to display the difference only for the `nginx-ingress` file, use the following command:

      helmfile  --selector component=ingress --environment platform -f helmfile.yaml diff

* To destroy the release, run the following command:

      helmfile  --selector component=ingress --environment platform -f helmfile.yaml destroy

## Deploy Components

Using the Helmfile, the following components can be installed:

* [NGINX Ingress Controller](https://github.com/kubernetes/ingress-nginx/tree/master/charts/ingress-nginx)
* [Keycloak](https://github.com/codecentric/helm-charts/tree/master/charts/keycloak)
* [EPAM Delivery Platform](https://github.com/epam/edp-install/tree/master/deploy-templates)
* [Argo CD](https://github.com/argoproj/argo-helm/tree/master/charts/argo-cd)
* [External Secrets Operator](https://github.com/external-secrets/external-secrets/tree/main/deploy/charts/external-secrets)
* [DefectDojo](https://github.com/DefectDojo/django-DefectDojo/tree/master/helm/defectdojo)

### Deploy NGINX Ingress Controller

!!! Info
    Skip this step for the OpenShift platform, because it has its own Ingress Controller.

To install NGINX Ingress controller, follow the steps below:

1. In the `releases/nginx-ingress.yaml` file, set the `proxy-real-ip-cidr` parameter according to the value with AWS VPC IPv4 CIDR.

2. Install NGINX Ingress controller:

      helmfile  --selector component=ingress --environment platform -f helmfile.yaml apply

### Deploy Keycloak

Keycloak requires a database deployment, so it has two charts: `releases/keycloak.yaml` and `releases/postgresql-keycloak.yaml`.

To install Keycloak, follow the steps below:

1. Create a `security` namespace:

  !!! Note
      **For the OpenShift users:**<br>
      This namespace is also indicated as `users` in the following custom `SecurityContextConstraints` resources: `resources/keycloak-scc.yaml` and `resources/postgresql-keycloak-scc.yaml`. Change the namespace name when using a custom namespace.

    ```
    kubectl create namespace security
    ```

2. Create PostgreSQL admin secret:

      kubectl -n security create secret generic keycloak-postgresql \
      --from-literal=password=<postgresql_password> \
      --from-literal=postgres-password=<postgresql_postgres_password>

3. In the `envs/platform.yaml` file, set the `dnsWildCard` parameter.

4. Create Keycloak admin secret:

      kubectl -n security create secret generic keycloak-admin-creds \
      --from-literal=username=<keycloak_admin_username> \
      --from-literal=password=<keycloak_admin_password>

5. Install Keycloak:

      helmfile  --selector component=sso --environment platform -f helmfile.yaml apply

### Deploy EPAM Delivery Platform

To install EDP, follow the steps below:

1. Create a `platform` namespace:

      kubectl create namespace platform

2. Create a secret for administrative access to the database:

      kubectl -n platform create secret generic super-admin-db \
        --from-literal=username=<super_admin_db_username> \
        --from-literal=password=<super_admin_db_password>

  !!! warning
      Do not use the **admin** username here since the **admin** is a reserved name.

3. Create a secret for an EDP tenant database user:

      kubectl -n platform create secret generic db-admin-console \
        --from-literal=username=<tenant_db_username> \
        --from-literal=password=<tenant_db_password>

  !!! warning
      Do not use the `admin` username here since the `admin` is a reserved name.

4. For EDP, it is required to have Keycloak access to perform the integration. Create a secret with the user and password provisioned in the step 2 of the [Keycloak Configuration](./install-keycloak.md#configuration) section.

      kubectl -n platform create secret generic keycloak \
        --from-literal=username=<username> \
        --from-literal=password=<password>

5. In the `envs/platform.yaml` file, set the `edpName` and `keycloakEndpoint` parameters.

6. In the `releases/edp-install.yaml` file, check and fill in all values.

7. Install EDP:

      helmfile  --selector component=edp --environment platform -f helmfile.yaml apply

### Deploy Argo CD

To install Argo CD, follow the steps below:

1. Install Argo CD:

  !!! Note
      **For the OpenShift users:**<br>
      When using a custom namespace for ArgoCD, the `argocd` namespace is also indicated as `users` in the `resources/argocd-scc.yaml` custom `SecurityContextConstraints` resource. Change it there as well.

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

!!! info
    It is also possible to install DefectDojo via Helm Chart. For details, please refer to the [Install DefectDojo](./install-defectdojo.md) page.

To install DefectDojo via Helmfile, follow the steps below:

1. Create a DefectDojo namespace:

  !!! Note
      **For the OpenShift users:**<br>
      This namespace is also indicated as `users` in the `resources/defectdojo-route.yaml` custom `SecurityContextConstraints` resource. Change it when using a custom namespace. Also, change the namespace in the `resources/defectdojo-route.yaml` file.

  ```bash
  kubectl create namespace defectdojo
  ```

2. Modify the `host` in `resources/defectdojo-route.yaml` (only for OpenShift).

3. Create a PostgreSQL admin secret:

  ```bash
  kubectl -n defectdojo create secret generic defectdojo-postgresql-specific \
  --from-literal=postgresql-password=<postgresql_password> \
  --from-literal=postgresql-postgres-password=<postgresql_postgres_password>
  ```

  !!! note
      The `postgresql_password` and `postgresql_postgres_password` passwords must be 16 characters long.

4. Create a RabbitMQ admin secret:

  ```bash
  kubectl -n defectdojo create secret generic defectdojo-rabbitmq-specific \
  --from-literal=rabbitmq-password=<rabbitmq_password> \
  --from-literal=rabbitmq-erlang-cookie=<rabbitmq_erlang_cookie>
  ```

  !!! note
      The `rabbitmq_password` password must be 10 characters long.

      The `rabbitmq_erlang_cookie` password must be 32 characters long.

5. Create a DefectDojo admin secret:

  ```bash
  kubectl -n defectdojo create secret generic defectdojo \
  --from-literal=DD_ADMIN_PASSWORD=<dd_admin_password> \
  --from-literal=DD_SECRET_KEY=<dd_secret_key> \
  --from-literal=DD_CREDENTIAL_AES_256_KEY=<dd_credential_aes_256_key> \
  --from-literal=METRICS_HTTP_AUTH_PASSWORD=<metric_http_auth_password>
  ```

  !!! note
      The `dd_admin_password` password must be 22 characters long.

      The `dd_secret_key` password must be 128 characters long.

      The `dd_credential_aes_256_key` password must be 128 characters long.

      The `metric_http_auth_password` password must be 32 characters long.

6. In the `envs/platform.yaml` file, set the `dnsWildCard` parameter.

7. Install DefectDojo:

  ```bash
  helmfile  --selector component=defectdojo --environment platform -f helmfile.yaml apply
  ```

## Related Articles
* [Install EDP](install-edp.md)
* [Install NGINX Ingress Controller](install-ingress-nginx.md)
* [Install Keycloak](install-keycloak.md)
* [Install DefectDojo](install-defectdojo.md)
* [Install Argo CD](install-argocd.md)