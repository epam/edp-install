# Install Argo CD

Inspect the prerequisites and the main steps to perform for enabling Argo CD in EDP.

## Prerequisites

The following tools must be installed:

* [Keycloak](./install-keycloak.md)
* [EDP](./install-edp.md)
* [Kubectl version 1.23.0](https://v1-23.docs.kubernetes.io/releases/download/)
* [Helm version 3.10.0](https://github.com/helm/helm/releases/tag/v3.10.0)

## Installation

Argo CD enablement for EDP consists of two major steps:

* Argo CD integration with EDP (SSO enablement, codebase onboarding, etc.)
* Argo CD installation

!!! info
    It is also possible to install Argo CD using the Helmfile. For details, please refer to the [Install via Helmfile](./install-via-helmfile.md#deploy-argo-cd) page.

### Integrate With EDP

To enable Argo CD integration, ensure that the `argocd.enabled` flag [values.yaml](https://github.com/epam/edp-install/blob/master/deploy-templates/values.yaml) is set to `true`

### Install With Helm

Argo CD can be installed in several ways, please follow the [official documentation](https://argo-cd.readthedocs.io/en/stable/operator-manual/installation/) for more details.

Follow the steps below to install Argo CD using Helm:

!!! warning "For the OpenShift users:"
    When using the OpenShift platform, apply the `SecurityContextConstraints` resource. Change the namespace in the `users` section if required.

    <details>
    <summary><b>View: argocd-scc.yaml</b></summary>
    
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
        - min: 99
          max: 65543
    groups: []
    kind: SecurityContextConstraints
    metadata:
      annotations:
          "helm.sh/hook": "pre-install"
      name: argo-redis-ha
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
    seccompProfiles:
      - '*'
    users:
    - system:serviceaccount:argocd:argo-redis-ha
    - system:serviceaccount:argocd:argo-redis-ha-haproxy
    - system:serviceaccount:argocd:argocd-notifications-controller
    - system:serviceaccount:argocd:argo-argocd-repo-server
    - system:serviceaccount:argocd:argocd-server
    volumes:
    - configMap
    - downwardAPI
    - emptyDir
    - persistentVolumeClaim
    - projected
    - secret
    ```
    </details>

1. Check out the *values.yaml* file sample of the Argo CD customization, which is based on the `HA mode without autoscaling`:

  <details>
  <summary><b>View: kubernetes-values.yaml</b></summary>

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
        - "argocd.<Values.global.dnsWildCard>"
    config:
      # required when SSO is enabled
      url: "https://argocd.<.Values.global.dnsWildCard>"
      application.instanceLabelKey: argocd.argoproj.io/instance-edp
      oidc.config: |
        name: Keycloak
        issuer: https://<.Values.global.keycloakEndpoint>/auth/realms/<.Values.global.edpName>-main
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
        g, ArgoCDAdmins, role:admin

  configs:
    params:
      application.namespaces: <.Values.global.edpName>

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

  <details>
  <summary><b>View: openshift-values.yaml</b></summary>

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
    route:
      enabled: true
      hostname: "argocd.<.Values.global.dnsWildCard>"
      termination_type: edge
      termination_policy: Redirect
    config:
      # required when SSO is enabled
      url: "https://argocd.<.Values.global.dnsWildCard>"
      application.instanceLabelKey: argocd.argoproj.io/instance-edp
      oidc.config: |
        name: Keycloak
        issuer: https://<.Values.global.keycloakEndpoint>/auth/realms/<.Values.global.edpName>-main
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
        g, ArgoCDAdmins, role:admin

  configs:
    params:
      application.namespaces: <.Values.global.edpName>

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

  * <.Values.global.dnsWildCard> is the EDP DNS WildCard.
  * <.Values.global.keycloakEndpoint> is the Keycloak Hostname.
  * <.Values.global.edpName> is the EDP name.

2. Run the installation:

  ```bash
  kubectl create ns argocd
  helm repo add argo https://argoproj.github.io/argo-helm
  helm install argo --version 5.16.1 argo/argo-cd -f values.yaml -n argocd
  ```

3. Update the `argocd-secret` secret in the `argocd` namespace by providing the correct Keycloak client secret (`oidc.keycloak.clientSecret`)
   with the value from the `keycloak-client-argocd-secret` secret in the EDP namespace. Then restart the deployment:

  ```bash
  ARGOCD_CLIENT=$(kubectl -n <EDP_NAMESPACE> get secret keycloak-client-argocd-secret  -o jsonpath='{.data.clientSecret}')
  kubectl -n argocd patch secret argocd-secret -p="{\"data\":{\"oidc.keycloak.clientSecret\": \"${ARGOCD_CLIENT}\"}}" -v=1
  kubectl -n argocd rollout restart deployment argo-argocd-server
  ```

## Related Articles

* [Argo CD Integration](argocd-integration.md)
* [Install via Helmfile](./install-via-helmfile.md#deploy-argo-cd)