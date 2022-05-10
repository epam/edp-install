# Install Argo CD

Inspect the prerequisites and the main steps to perform for enabling Argo CD in EDP.

## Prerequisites

* [Keycloak is installed](./install-keycloak.md)
* [EDP is installed](./install-edp.md)
* Kubectl version 1.18.0 is installed. Please refer to the [Kubernetes official website](https://v1-18.docs.kubernetes.io/docs/setup/release/notes/) for details.
* [Helm](https://helm.sh) version 3.6.0 is installed. Please refer to the [Helm page](https://github.com/helm/helm/releases/tag/v3.6.0) on GitHub for details.

## Installation

Argo CD enablement for EDP consists of two major steps:

* Argo CD integration with EDP (SSO enablement, codebase onboarding, etc.)
* Argo CD installation

### Integrate With EDP

To enable Argo CD integration, ensure that the `argocd.enabled` flag [values.yaml](https://github.com/epam/edp-install/blob/master/deploy-templates/values.yaml) is set to `true`

### Install With Helm

Argo CD can be installed in many ways, please follow the [official documentation](https://argo-cd.readthedocs.io/en/stable/operator-manual/installation/) for more details.

Follow the steps below to install Argo CD using Helm:

* Check out the *values.yaml* file sample of the Argo CD customization, which is based on `HA mode without autoscaling`:

  <details>
  <summary><b>View: values.yaml</b></summary>

  ```yaml
  redis-ha:
    enabled: true

  controller:
    enableStatefulSet: true

  server:
    replicas: 2
    extraArgs:
      - "--insecure"
    env:
      - name: ARGOCD_API_SERVER_REPLICAS
        value: '2'
    ingress:
      enabled: true
      hosts:
        - "argocd.{{ .Values.global.dnsWildCard }}"
    config:
      # required when SSO is enabled
      url: "https://argocd.{{ .Values.global.dnsWildCard }}"
      application.instanceLabelKey: argocd.argoproj.io/instance-edp
      oidc.config: |
        name: Keycloak
        issuer: https://{{ .Values.global.keycloakEndpoint }}/auth/realms/{{ .Values.global.edpName }}-main
        clientID: argocd
        clientSecret: $oidc.keycloak.clientSecret
        requestedScopes:
          - openid
          - profile
          - email
          - groups
    rbacConfig:
      # users may be still be able to login,
      # but will see no apps, projects, etc...
      policy.default: ''
      scopes: '[groups]'
      policy.csv: |
        # default global admins
        g, Argo CDAdmins, role:admin

  configs:
    secret:
      extra:
        oidc.keycloak.clientSecret: "REPLACE"

  repoServer:
    replicas: 2

  # we use Keycloak so no DEX is required
  dex:
    enabled: false

  # Disabled for multitenancy env with single instance deployment
  applicationSet:
    enabled: false
  ```

  </details>

    Populate Argo CD values with the values from the EDP [values.yaml](https://github.com/epam/edp-install/blob/master/deploy-templates/values.yaml):

  * .Values.global.dnsWildCard - EDP DNS WildCard
  * .Values.global.keycloakEndpoint - Keycloak Hostname
  * .Values.global.edpName - EDP name

* Run installation

  ```bash
  kubectl create ns argocd
  helm repo add argo https://argoproj.github.io/argo-helm
  helm install argocd argo/argo-cd -f values.yaml
  ```

* Update `argocd-secret` secret (in argocd namespace) by providing correct keycloak client secret (`oidc.keycloak.clientSecret`) with value from the `keycloak-client-argocd-secret` secret in EDP namespace and restart the deployment:

  ```bash
  ARGOCD_CLIENT=$(kubectl -n <EDP_NAMESPACE> get secret keycloak-client-argocd-secret  -o jsonpath='{.data.clientSecret}')
  kubectl -n argocd patch secret argocd-secret -p="{\"data\":{\"oidc.keycloak.clientSecret\": \"${ARGOCD_CLIENT}\"}}" -v=1
  kubectl -n argocd rollout restart deployment argocd-server
  ```
