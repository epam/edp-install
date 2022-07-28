# Install DefectDojo

Inspect the main steps to perform for installing DefectDojo via Helm Chart.

!!! info
    It is also possible to install DefectDojo using the Helmfile. For details, please refer to the [Install via Helmfile](./install-via-helmfile.md##deploy-defectdojo) page.

## Prerequisites

* Kubectl version 1.20.0 is installed. Please refer to the [Kubernetes official website](https://v1-20.docs.kubernetes.io/docs/setup/release/notes/) for details.
* [Helm](https://helm.sh) version 3.9.2 is installed. Please refer to the [Helm page](https://github.com/helm/helm/releases/tag/v3.9.2) on GitHub for details.

## Installation

!!! info
    Please refer to the [DefectDojo Helm Chart](https://github.com/DefectDojo/django-DefectDojo/tree/master/helm/defectdojo)
    and [Deploy DefectDojo into the Kubernetes cluster](https://github.com/DefectDojo/django-DefectDojo/blob/dev/readme-docs/KUBERNETES.md)
    sections for details.

To install DefectDojo, follow the steps below:

1. Check that a security namespace is created. If not, run the following command to create it:

   ```bash
   kubectl create namespace defectdojo
   ```

  !!! note
      On the OpenShift cluster, run the `oc` command instead of the `kubectl` one.

2. Add a chart repository:

   ```bash
   helm repo add defectdojo 'https://raw.githubusercontent.com/DefectDojo/django-DefectDojo/helm-charts'
   helm repo update
   ```

3. Create PostgreSQL admin secret:

   ```bash
   kubectl -n defectdojo create secret generic defectdojo-postgresql-specific \
   --from-literal=postgresql-password=<postgresql_password> \
   --from-literal=postgresql-postgres-password=<postgresql_postgres_password>
   ```

  !!! note
      The `postgresql_password` and `postgresql_postgres_password` passwords must be 16 characters long.

4. Create Rabbitmq admin secret:

   ```bash
   kubectl -n defectdojo create secret generic defectdojo-rabbitmq-specific \
   --from-literal=rabbitmq-password=<rabbitmq_password> \
   --from-literal=rabbitmq-erlang-cookie=<rabbitmq_erlang_cookie>
   ```

  !!! note
      The `rabbitmq_password` password must be 10 characters long.

      The `rabbitmq_erlang_cookie` password must be 32 characters long.

5. Create DefectDojo admin secret:

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


6. Install DefectDojo v.2.12.0 using [defectdojo/defectdojo](https://github.com/DefectDojo/django-DefectDojo/tree/master/helm/defectdojo) Helm chart v.1.6.35:

   ```bash
   helm upgrade --install \
   defectdojo \
   defectdojo/defectdojo \
   --namespace defectdojo \
   --values values.yaml
   ```

  Check out the *values.yaml* file sample of the DefectDojo customization:

  <details>
  <summary><b>View: values.yaml</b></summary>

```yaml

fullnameOverride: defectdojo
host: defectdojo.<ROOT_DOMAIN>
site_url: https://defectdojo.<ROOT_DOMAIN>
alternativeHosts:
  - defectdojo-django.defectdojo

initializer:
  # should be false after initial installation was performed
  run: true
django:
  uwsgi:
    livenessProbe:
      # Enable liveness checks on uwsgi container. Those values are use on nginx readiness checks as well.
      # default value is 120, so in our case 20 is just fine
      initialDelaySeconds: 20
```

  </details>

## Configuration

To prepare DefectDojo for integration with EDP, follow the steps below:

1. Get credentials of the DefectDojo admin.

   ```bash
   echo "DefectDojo admin password: $(kubectl \
   get secret defectdojo \
   --namespace=defectdojo \
   --output jsonpath='{.data.DD_ADMIN_PASSWORD}' \
   | base64 --decode)"
   ```

2. Get a token of the DefectDojo user:

  * Login to the DefectDojo UI using the credentials.

  * Go to the API v2 key (token).

  * Copy the API key.

3. Create a DefectDojo secret in your edp namespace:

   ```bash
   kubectl -n <edp_namespace> create secret generic defectdojo \
   --from-literal=token=<dd_token_of_dd_user> \
   --from-literal=url="http://defectdojo-django.defectdojo"
   ```

## Related Articles

* [Install via Helmfile](install-via-helmfile.md)