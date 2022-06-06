# Install via Helmfile

This article provides the instruction on how to deploy EDP and components in Kubernetes using [Helmfile](https://github.com/helmfile/helmfile) that is intended for deploying Helm charts.

## Prerequisites
* [Helm](https://helm.sh) version 3.6.0+ is installed. Please refer to the [Helm](https://github.com/helm/helm/releases/tag/v3.6.0) page on GitHub for details.
* Helmfile version 0.142.0 is installed. Please refer to the [GitHub](https://github.com/helmfile/helmfile) page for details.
* Helm diff plugin version 3.5.0 is installed. Please refer to the [GitHub](https://github.com/databus23/helm-diff) page for details.

## Helmfile Structure
* The envs/common.yaml file contains the specification for environments pattern, list of helm repositories from which it is necessary to fetch the helm charts and additional Helm parameters.
* The envs/platform.yaml file contains global parameters that are used in various Helmfiles.
* The releases/envs/ contains symbol links to environments files.
* The releases/*.yaml file contains description of parameters that is used when deploying a Helm chart.
* The helmfile.yaml file contains a path to Helm releases files.
* The envs/ci.yaml file contains stub parameters for linter.
* The test/lint-ci.sh script for running linter with debug loglevel and stub parameters.


## Deploy Components

Using the Helmfile, the following components can be installed:

* [NGINX Ingress Controller](https://github.com/kubernetes/ingress-nginx/tree/master/charts/ingress-nginx)
* [Keycloak](https://github.com/codecentric/helm-charts/tree/master/charts/keycloak)
* [EPAM Delivery Platform](https://github.com/epam/edp-install/tree/master/deploy-templates)
* [Argo CD](https://github.com/argoproj/argo-helm/tree/master/charts/argo-cd)

### Deploy NGINX Ingress Controller

To install NGINX Ingress controller, follow the steps below:

1. In the releases/nginx-ingress.yaml file, set the *proxy-real-ip-cidr* parameter according to the value with AWS VPC IPv4 CIDR.
2. Install NGINX Ingress controller:

    ```
    helmfile  --selector component=ingress --environment platform -f helmfile.yaml apply
    ```

### Deploy Keycloak

To install Keycloak, follow the steps below:

1. Create a security namespace:
    ```
    kubectl create namespace security
    ```
2. Create the Keycloak admin secret:
    ```
    kubectl -n security create secret generic keycloak-admin-creds \
    --from-literal=username=<keycloak_admin_username> \
    --from-literal=password=<keycloak_admin_password>
    ```
4. Create the PostgreSQL admin secret:
    ```
    kubectl -n security create secret generic keycloak-postgresql \
    --from-literal=postgresql-password=<postgresql_password> \
    --from-literal=postgresql-postgres-password=<postgresql_postgres_password>
    ```
5. In the envs/platform.yaml file, set the *dnsWildCard* parameter.

6. Install Keycloak:
    ```
    helmfile  --selector component=sso --environment platform -f helmfile.yaml apply
    ```
### Deploy EPAM Delivery Platform

To install EDP, follow the steps below:

1. Create a platform namespace:
    ```
    kubectl create namespace platform
    ```
2. Create a secret for administrative access to the database:
    ```
    kubectl -n platform create secret generic super-admin-db \
    --from-literal=username=<super_admin_db_username> \
    --from-literal=password=<super_admin_db_password>
    ```
    >_**NOTE:** Do not use the **admin** username here since the **admin** is a reserved name._

1. Create a secret for an EDP tenant database user:
    ```
    kubectl -n platform create secret generic db-admin-console \
    --from-literal=username=<tenant_db_username> \
    --from-literal=password=<tenant_db_password>
    ```
    >_**NOTE:** Do not use the **admin** username here since the **admin** is a reserved name._

1. For EDP, it is required to have Keycloak access to perform the integration. Create a secret with the user and password provisioned in the step 2 of the [Keycloak Configuration](https://epam.github.io/edp-install/operator-guide/install-keycloak/#configuration) section.
    ```
    kubectl -n platform create secret generic keycloak \
    --from-literal=username=<username> \
    --from-literal=password=<password>
    ```
5. In the envs/platform.yaml file, set the *edpName* and *keycloakEndpoint* parameters.
6. In the releases/edp-install.yaml file, check and fill in all values.
7. Install EDP:
    ```
    helmfile  --selector component=edp --environment platform -f helmfile.yaml apply
    ```
### Deploy Argo CD

To install Argo CD, follow the steps below:

1. Install Argo CD:
    ```
    helmfile  --selector component=argocd --environment platform -f helmfile.yaml apply
    ```
2. Update the `argocd-secret` secret (in the Argo CD namespace) by providing the correct Keycloak client secret (`oidc.keycloak.clientSecret`) with the value from the `keycloak-client-argocd-secret` secret in EDP namespace, and restart the deployment:

    ```bash
    ARGOCD_CLIENT=$(kubectl -n platform get secret keycloak-client-argocd-secret  -o jsonpath='{.data.clientSecret}')
    kubectl -n argocd patch secret argocd-secret -p="{\"data\":{\"oidc.keycloak.clientSecret\": \"${ARGOCD_CLIENT}\"}}" -v=1
    kubectl -n argocd rollout restart deployment argo-argocd-server
    ```

## Operate Helmfile

Before applying the Helmfile, please fill in the global parameters in the envs/platform.yaml and releases/*.yaml files for every Helm deploy.

Pay attention to the following recommendations while working with the Helmfile:

* To launch Lint, run the test/lint-ci.sh script.

* To show the difference between the deployed and environment state (helm diff), run the command:
    ```
    helmfile --environment platform -f helmfile.yaml diff
    ```
* To apply the deploy, run the command:
    ```
    helmfile  --selector component=ingress --environment platform -f helmfile.yaml apply
    ```
* To deploy components according to the label, use the selector to target a subset of releases when running the Helmfile. It can be useful when using large Helmfiles with releases that are logically grouped together. For example, to dispay the difference only for the nginx-ingress file, use the command:
    ```
    helmfile  --selector component=ingress --environment platform -f helmfile.yaml diff
    ```
* To destroy the release, run the command:
    ```
    helmfile  --selector component=ingress --environment platform -f helmfile.yaml destroy
    ```

### Related Articles

- [Install via Helmfile](https://epam.github.io/edp-install/operator-guide/install-via-helmfile/)
- [Install NGINX Ingress Controller](https://epam.github.io/edp-install/operator-guide/install-ingress-nginx/)
- [Install Keycloak](https://epam.github.io/edp-install/operator-guide/install-keycloak/)
- [Install EDP](https://epam.github.io/edp-install/operator-guide/install-edp/)
- [Install Argo CD](https://epam.github.io/edp-install/operator-guide/install-argocd/)