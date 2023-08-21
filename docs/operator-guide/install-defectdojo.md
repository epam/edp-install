# Install DefectDojo

Inspect the main steps to perform for installing DefectDojo via Helm Chart.

!!! info
    It is also possible to install DefectDojo using the EDP addons approach. For details, please refer to the [EDP addons approach](https://github.com/epam/edp-cluster-add-ons).

## Prerequisites

* [Kubectl](https://v1-26.docs.kubernetes.io/releases/download/) version 1.26.0 is installed.
* [Helm](https://helm.sh) version 3.12.0+ is installed.

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

  !!! warning "For the OpenShift users:"
      When using the OpenShift platform, install the `SecurityContextConstraints` resource. In case of using a custom namespace for `defectdojo`, change the namespace in the `users` section.<br>

      <details>
      <summary><b>View: defectdojo-scc.yaml</b></summary>

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
        name: defectdojo
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
      - system:serviceaccount:defectdojo:defectdojo
      - system:serviceaccount:defectdojo:defectdojo-rabbitmq
      - system:serviceaccount:defectdojo:default
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


6. Install DefectDojo v.2.22.4 using [defectdojo/defectdojo](https://github.com/DefectDojo/django-DefectDojo/tree/master/helm/defectdojo) Helm chart v.1.6.69:

   ```bash
   helm upgrade --install \
   defectdojo \
   --version 1.6.69 \
   defectdojo/defectdojo \
   --namespace defectdojo \
   --values values.yaml
   ```

  Check out the *values.yaml* file sample of the DefectDojo customization:

  <details>
  <summary><b>View: values.yaml</b></summary>

```yaml
tag: 2.22.4
fullnameOverride: defectdojo
host: defectdojo.<ROOT_DOMAIN>
site_url: https://defectdojo.<ROOT_DOMAIN>
alternativeHosts:
  - defectdojo-django.defectdojo

initializer:
  # should be false after initial installation was performed
  run: true
django:
  ingress:
    enabled: true # change to 'false' for OpenShift
    activateTLS: false
  uwsgi:
    livenessProbe:
      # Enable liveness checks on uwsgi container. Those values are use on nginx readiness checks as well.
      # default value is 120, so in our case 20 is just fine
      initialDelaySeconds: 20
```

  </details>

7. For the OpenShift platform, install a Route:

  <details>
  <summary><b>View: defectdojo-route.yaml</b></summary>

  ```yaml
  kind: Route
  apiVersion: route.openshift.io/v1
  metadata:
    name: defectdojo
    namespace: defectdojo
  spec:
    host: defectdojo.<ROOT_DOMAIN>
    path: /
    tls:
      insecureEdgeTerminationPolicy: Redirect
      termination: edge
    to:
      kind: Service
      name: defectdojo-django
    port:
      targetPort: http
    wildcardPolicy: None

  ```
  </details>

## Configuration

To prepare DefectDojo for integration with EDP, follow the steps below:

1. Get credentials of the DefectDojo admin:

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

3. Provision secrets using kubectl, EDP Portal UI or with the `externalSecrets` operator:

=== "kubectl"

    ```bash
    kubectl -n <edp_namespace> create secret generic defectdojo-ciuser-token \
    --from-literal=token=<dd_token_of_dd_user> \
    --from-literal=url="<defectdojo_url>"
    ```

=== "EDP Portal UI"

    Go to the `EDP Portal UI` open `EDP` -> `Configuration` -> `DefectDojo` change `URL` and `Token` and click `save` button.

    !![DefectDojo update manual secret](../assets/operator-guide/defectdojo-token.png "DefectDojo update manual secret")

=== "External Secrets Operator"

    Store defectdojo URL and Token in AWS Parameter Store with following format:
    ```bash
    "defectdojo-ciuser-token":
    {
      "token": "XXXXXXXXXXXX",
      "url": "https://defectdojo.example.com"
    },
    ```

    More detail of External Secrets Operator Integration can found on [the following page](external-secrets-operator-integration.md)

After following the instructions provided, you should be able to integrate your DefectDojo with the EPAM Delivery Platform using one of the few available scenarios.

## Related Articles

* [Install External Secrets Operator](install-external-secrets-operator.md)
* [External Secrets Operator Integration](external-secrets-operator-integration.md)
* [Install Harbor](install-harbor.md)