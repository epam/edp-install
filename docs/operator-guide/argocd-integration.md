# Argo CD Integration

KubeRocketCI uses Argo CD as a [part of the Continues Delivery/Continues Deployment](../user-guide/cd-pipeline-details.md)
implementation. Argo CD follows the best GitOps practices, uses Kubernetes native approach for the Deployment Management, has rich UI and
required RBAC capabilities.

## Argo CD Deployment Approach in KubeRocketCI

Argo CD can be installed using [two different approaches](https://argo-cd.readthedocs.io/en/stable/operator-manual/installation):

* Cluster-wide scope with the cluster-admin access
* Namespaced scope with the single namespace access

Both approaches can be deployed with High Availability (HA) or Non High Availability (non HA) installation manifests.

KubeRocketCI uses the HA deployment with the cluster-admin permissions, to minimize cluster resources consumption by sharing
single Argo CD instance across multiple EDP Tenants. Please follow [the installation instructions](./install-argocd.md) to deploy Argo CD.

## Argo CD Integration

See a diagram below for the details:

!![edp-argocd](../assets/operator-guide/edp-argocd.png "Argo CD Diagram")

* Argo CD is deployed in a separate `argocd` namespace.
* Argo CD uses a `cluster-admin` role for managing cluster-scope resources.
* The `control-plane` application is created using the App of Apps approach, and its code is managed by the `control-plane` members.
* The `control-plane` is used to onboard new Argo CD Tenants (Argo CD Projects - AppProject).
* The `EDP Tenant Member` manages `Argo CD Applications` using `kind: Application` in the `edpTenant` namespace.

The [App Of Apps approach](https://argo-cd.readthedocs.io/en/stable/operator-manual/cluster-bootstrapping/) is used to
manage the `EDP Tenants`. Inspect the [edp-grub](https://github.com/SergK/edp-grub) repository structure that is used to
provide the EDP Tenants for the Argo CD Projects:

```bash
edp-grub
├── LICENSE
├── README.md
├── apps                      ### All Argo CD Applications are stored here
│   ├── grub-argocd.yaml      # Application that provisions Argo CD Resources - Argo Projects (EDP Tenants)
│   └── grub-keycloak.yaml    # Application that provisions Keycloak Resources - Argo CD Groups (EDP Tenants)
├── apps-configs
│   └── grub
│       ├── argocd            ### Argo CD resources definition
│       │   └── edp.yaml
│       └── keycloak          ### Keycloak resources definition
│           └── edp.yaml
├── bootstrap
│   └── root.yaml             ### Root application in App of Apps, which provision Applications from /apps
└── examples                  ### Examples
    └── tenant
        └── edp-petclinic.yaml
```

The Root Application must be created under the `control-plane` scope.

## Argo CD Configuration

Now that Argo CD is integrated, it is time to configure it properly. To configure Argo CD for KubeRocketCI, follow the steps below:

1. Modify the `argocd-cmd-params-cm` ConfigMap in the `argocd` namespace and add the `application.namespaces` parameter to the subsection data:

  === "kubectl"

      ```bash
      kubectl patch configmap argocd-cmd-params-cm -n argocd --type merge -p '{"data":{"application.namespaces":"edp"}}'
      ```

  === "manifest"

      ```yaml
      data:
        application.namespaces: edp
      ```

2. Add a [credential template](https://argo-cd.readthedocs.io/en/stable/user-guide/private-repositories/#private-repositories)
for GitHub, GitLab, Gerrit integrations. The credential template must be created for each Git server.

  === "GitHub/GitLab"

      Generate an SSH key pair and add a public key to GitLab or GitHub account.

      !!! warning
          Use an additional GitHub/GitLab User to access a repository.<br>
          For example:<br>
          - GitHub, add a User to a repository with a "Read" role.<br>
          - GitLab, add a User to a repository with a "Guest" role.

      ```bash
      ssh-keygen -t ed25519 -C "email@example.com" -f argocd
      ```

      Copy SSH private key to Argo CD namespace

      ```bash
      EDP_NAMESPACE="edp"
      VCS_HOST="<github.com_or_gitlab.com>"
      ACCOUNT_NAME="<ACCOUNT_NAME>"
      URL="ssh://git@${VCS_HOST}:22/${ACCOUNT_NAME}"

      kubectl create secret generic ${EDP_NAMESPACE} -n argocd \
      --from-file=sshPrivateKey=argocd \
      --from-literal=url="${URL}"
      kubectl label --overwrite secret ${EDP_NAMESPACE} -n argocd "argocd.argoproj.io/secret-type=repo-creds"
      ```

      Add public SSH key to GitHub/GitLab account.

  === "Gerrit"

      Copy existing SSH private key for Gerrit to Argo CD namespace

      ```bash
      EDP_NAMESPACE="edp"
      GERRIT_PORT=$(kubectl get gerrit gerrit -n ${EDP_NAMESPACE} -o jsonpath='{.spec.sshPort}')
      GERRIT_ARGOCD_SSH_KEY_NAME="gerrit-ciuser-sshkey"
      GERRIT_URL=$(echo "ssh://edp-ci@gerrit.${EDP_NAMESPACE}:${GERRIT_PORT}" | base64)
      kubectl get secret ${GERRIT_ARGOCD_SSH_KEY_NAME} -n ${EDP_NAMESPACE} -o json | jq 'del(.data.username,.metadata.annotations,.metadata.creationTimestamp,.metadata.labels,.metadata.resourceVersion,.metadata.uid,.metadata.ownerReferences)' | jq '.metadata.namespace = "argocd"' | jq --arg name "${EDP_NAMESPACE}" '.metadata.name = $name' | jq --arg url "${GERRIT_URL}" '.data.url = $url' | jq '.data.sshPrivateKey = .data.id_rsa' | jq 'del(.data.id_rsa,.data."id_rsa.pub")' | kubectl apply -f -
      kubectl label --overwrite secret ${EDP_NAMESPACE} -n argocd "argocd.argoproj.io/secret-type=repo-creds"
      ```

3. Add [SSH Known hosts](https://argo-cd.readthedocs.io/en/stable/user-guide/private-repositories/#unknown-ssh-hosts)
   for Gerrit, GitHub, GitLab integration.

  === "GitHub/GitLab"

      Add GitHub/GitLab host to Argo CD config map with known hosts

      ```bash
      EDP_NAMESPACE="edp"
      VCS_HOST="<VCS_HOST>"
      KNOWN_HOSTS_FILE="/tmp/ssh_known_hosts"
      ARGOCD_KNOWN_HOSTS_NAME="argocd-ssh-known-hosts-cm"

      rm -f ${KNOWN_HOSTS_FILE}
      kubectl get cm ${ARGOCD_KNOWN_HOSTS_NAME} -n argocd -o jsonpath='{.data.ssh_known_hosts}' > ${KNOWN_HOSTS_FILE}
      ssh-keyscan ${VCS_HOST} >> ${KNOWN_HOSTS_FILE}
      kubectl create configmap ${ARGOCD_KNOWN_HOSTS_NAME} -n argocd --from-file ${KNOWN_HOSTS_FILE} -o yaml --dry-run=client | kubectl apply -f -
      ```

  === "Gerrit"

      Add Gerrit host to Argo CD config map with known hosts

      ```bash
      EDP_NAMESPACE="edp"
      KNOWN_HOSTS_FILE="/tmp/ssh_known_hosts"
      ARGOCD_KNOWN_HOSTS_NAME="argocd-ssh-known-hosts-cm"
      GERRIT_PORT=$(kubectl get gerrit gerrit -n ${EDP_NAMESPACE} -o jsonpath='{.spec.sshPort}')

      rm -f ${KNOWN_HOSTS_FILE}
      kubectl get cm ${ARGOCD_KNOWN_HOSTS_NAME} -n argocd -o jsonpath='{.data.ssh_known_hosts}' > ${KNOWN_HOSTS_FILE}
      kubectl exec -it deployment/gerrit -n ${EDP_NAMESPACE} -- ssh-keyscan -p ${GERRIT_PORT} gerrit.${EDP_NAMESPACE} >> ${KNOWN_HOSTS_FILE}
      kubectl create configmap ${ARGOCD_KNOWN_HOSTS_NAME} -n argocd --from-file ${KNOWN_HOSTS_FILE} -o yaml --dry-run=client | kubectl apply -f -
      ```

4. Create an Argo CD Project (EDP Tenant), for example, with the `edp` name:

  ```yaml title="AppProject"
  apiVersion: argoproj.io/v1alpha1
  kind: AppProject
  metadata:
    name: edp
    namespace: argocd
    # Finalizer that ensures that project is not deleted until it is not referenced by any application
    finalizers:
      - resources-finalizer.argocd.argoproj.io
  spec:
    destinations:
      # by default edp work with 'edp-*' namespace
      - namespace: 'edp-*'
      # allow to deploy to specific server (local in our case)
        name: in-cluster
    # Deny all cluster-scoped resources from being created, except for Namespace
    clusterResourceWhitelist:
    - group: ''
      kind: Namespace
    # Allow all namespaced-scoped resources to be created, except for ResourceQuota, LimitRange, NetworkPolicy
    namespaceResourceBlacklist:
    - group: ''
      kind: ResourceQuota
    - group: ''
      kind: LimitRange
    - group: ''
      kind: NetworkPolicy
    # we are ok to create any resources inside namespace
    namespaceResourceWhitelist:
    - group: '*'
      kind: '*'
    # enable access only for specific git server. The example below 'edp' - it is namespace where EDP deployed
    sourceRepos:
      - ssh://git@github.com/*
    # enable capability to deploy objects from namespaces
    sourceNamespaces:
      - edp
  ```

4. Check that your new Repository, Known Hosts, and AppProject are added to the Argo CD UI.

5. Generate Argo CD project token for deploy integration:

  ```bash
  URL=<ARGO CD URL>
  TOKEN=$(argocd proj role create-token edp developer -i argocd-ci -t)


  cat <<EOF | kubectl apply -f -
  apiVersion: v1
  kind: Secret
  metadata:
    name: argocd-ci
    namespace: edp
    labels:
      app.edp.epam.com/integration-secret: "true"
      app.edp.epam.com/secret-type: "argocd"
  type: Opaque
  stringData:
    token: $TOKEN
    url: $URL
  EOF
  ```

Once Argo CD is successfully integrated, EDP user can utilize Argo CD to deploy [CD pipelines](../user-guide/add-cd-pipeline.md#deploy-application).

## Check Argo CD Integration (Optional)

This section provides the information on how to test the integration with Argo CD and is not mandatory to be followed.

1. Add an Argo CD application:

  ??? Note "Example: Argo CD Application "
      ```yaml
      apiVersion: argoproj.io/v1alpha1
      kind: Application
      metadata:
        name: demo
      spec:
        project: edp
        destination:
          namespace: edp-demo
          server: https://kubernetes.default.svc
        source:
          helm:
            parameters:
              - name: image.tag
                value: master-0.1.0-1
              - name: image.repository
                value: image-repo
          path: deploy-templates
          # github/gitlab example ssh://git@github.com/<github_account_name>/<repository_name>.git
          # gerrit example ssh://<gerrit_user>@gerrit.edp:30007/<repository_name>.git
          repoURL: ssh://git@github.com/edp/demo.git
          targetRevision: master
        syncPolicy:
          syncOptions:
            - CreateNamespace=true
          automated:
            selfHeal: true
            prune: true
      ```

2. Check that your new Application is added to the Argo CD UI under the `edp` Project scope.

## Deploy Argo CD Application to Remote Cluster (Optional)

KubeRocketCI also supports deploying Argo CD applications to a remote cluster. To deploy applications to remote clusters, follow the steps below:

1. Create `ServiceAccount` `ClusterRoleBinding` and `Secret` for that `ServiceAccount`.

2. Receive the bearer token:

  ```bash
  BEAR_TOKEN=$(kubectl get secret <serviceaccount-secret-name> -o jsonpath='{.data.token}' | base64 --decode)
  ```

3. Create ArgoCD secret for remote cluster:

  ```yaml title="manifest"
  apiVersion: v1
  kind: Secret
  metadata:
    name: edp-remote-cluster
    namespace: argocd
  data:
    # Remote cluster config
    config: {"bearerToken":"<BEAR_TOKEN>","tlsClientConfig":{"insecure":false,"caData":"<certificate-authority-data>"}}
    # Remote cluster name
    name: "edp-remote-cluster"
    # Cluster endpoint URL
    server: "https://xxxxxxxxxxxxxxxxxxxx.sk1.eu-central-1.eks.amazonaws.com"
  type: stringData
  ```

4. Update an Argo CD Project (EDP Tenant), with the `edp` name:

  ```yaml title="AppProject"
  apiVersion: argoproj.io/v1alpha1
  kind: AppProject
  metadata:
    name: edp
  spec:
    destinations:
        # Add block that allow deploy in remote cluster
        # by default edp work with 'edp-*' namespace
      - namespace: 'edp-*'
        # allow to deploy to specific server (remote in our case)
        name: edp-remote-cluster
  ```

5. Add a remote cluster in the KubeRocketCI portal. Please refer to the [Add Cluster](../user-guide/add-cluster.md) page for details.

## Keycloak Integration (Optional)

!!! Note
    To proceed with the steps below, you need the [edp-keycloak-operator](https://github.com/epam/edp-keycloak-operator) to be deployed.

To provide Argo CD with the Keycloak SSO authorization mechanism, follow the guidelines below:

1. Create secret `keycloak-client-argocd-secret`.

  ```bash
  kubectl create secret generic keycloak-client-argocd-secret \
  --from-literal=clientSecret=$(openssl rand -base64 32) \
  --namespace=argocd
  ```

2. Update the `argocd-cm` ConfigMap:

  === "kubectl"

      ```bash
      kubectl patch configmap argocd-cm -n argocd --patch "$(cat <<EOF
      data:
        oidc.config: |
          name: Keycloak
          issuer: https://<keycloakEndpoint>/auth/realms/edp
          clientID: argocd-tenant
          clientSecret: $keycloak-client-argocd-secret:clientSecret
          requestedScopes:
            - openid
            - profile
            - email
            - groups
      EOF
      )"
      ```

  === "manifest"

      ```yaml
      data:
        oidc.config:
          url: "https://argocd.<.Values.global.dnsWildCard>"
          application.instanceLabelKey: argocd.argoproj.io/instance-edp
          oidc.config: |
            name: Keycloak
            issuer: https://<.Values.global.keycloakEndpoint>/auth/realms/edp
            clientID: argocd-tenant
            clientSecret: $keycloak-client-argocd-secret:clientSecret
            requestedScopes:
              - openid
              - profile
              - email
              - groups
      ```

3. Create a Keycloak Group:

  ```yaml
  apiVersion: v1.edp.epam.com/v1
  kind: KeycloakRealmGroup
  metadata:
    name: argocd-edp-users
  spec:
    name: ArgoCD-edp-users
    realm: main
  ```

4. Create a Keycloak Client:

  ```yaml
  apiVersion: v1.edp.epam.com/v1
  kind: KeycloakClient
  metadata:
    name: argocd
    namespace: argocd
  spec:
    advancedProtocolMappers: true
    attributes:
      post.logout.redirect.uris: +
    clientId: argocd-tenant
    defaultClientScopes:
      - groups
    realmRef:
      kind: ClusterKeycloakRealm
      name: main
    secret: keycloak-client-argocd-secret
    webUrl: "https://argocd.<.Values.global.dnsWildCard>"
  ```

5. In Keycloak, add users to the `ArgoCD-edp-users` Keycloak Group.

6. Update spec in project:

  ```yaml title="AppProject"
  spec:
    description: CD pipelines for edp
    roles:
      - name: developer
        description: Users for edp tenant
        policies:
          - p, proj:edp:developer, applications, create, edp/*, allow
          - p, proj:edp:developer, applications, delete, edp/*, allow
          - p, proj:edp:developer, applications, get, edp/*, allow
          - p, proj:edp:developer, applications, override, edp/*, allow
          - p, proj:edp:developer, applications, sync, edp/*, allow
          - p, proj:edp:developer, applications, update, edp/*, allow
          - p, proj:edp:developer, repositories, create, edp/*, allow
          - p, proj:edp:developer, repositories, delete, edp/*, allow
          - p, proj:edp:developer, repositories, update, edp/*, allow
          - p, proj:edp:developer, repositories, get, edp/*, allow
        groups:
          # Keycloak Group name
          - ArgoCD-edp-users
  ```

7. Then restart the deployment:

  ```bash
  kubectl -n argocd rollout restart deployment argo-argocd-server
  ```

## Related Articles

* [Install Argo CD](install-argocd.md)
