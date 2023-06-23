# External Secrets Operator Integration

[External Secrets Operator (ESO)](https://external-secrets.io/) can be integrated with EDP.

There are [multiple Secrets Providers](https://external-secrets.io/latest/guides-introduction/) that can be used within ESO. EDP is integrated with two major providers:

* [Kubernetes Secrets](https://kubernetes.io/docs/concepts/configuration/secret/)
* [AWS Systems Manager Parameter Store](https://docs.aws.amazon.com/systems-manager/latest/userguide/systems-manager-parameter-store.html)

## Kubernetes

All secrets are stored in Kubernetes in pre-defined namespaces. EDP suggests using the following approach for secrets management:

* `EDP_NAMESPACE-vault`, where EDP_NAMESPACE is a name of the namespace where EDP is deployed, such as `edp-vault`. This namespace is used by EDP platform. Access to secrets in the `edp-vault` is permitted only for `EDP Administrators`.

* `EDP_NAMESPACE-cicd-vault`, where EDP_NAMESPACE is a name of the namespace where EDP is deployed, such as `edp-cicd-vault`. Development team uses access to secrets in the `edp-cicd-vault`for microservices development.

See a diagram below for more details:

![eso-with-kubernetes](../assets/operator-guide/eso-k8s.png)

### EDP Install Scenario
In order to [install EDP](./install-edp.md), a list of passwords must be created: defectdojo-ciuser-token, keycloak-client-headlamp-secret and keycloak. Secrets are provided automatically when using ESO.

1. Create a common namespace for secrets and EDP:

    ```bash
    kubectl create namespace edp-vault
    kubectl create namespace edp
    ```

2. Create secrets in the `edp-vault` namespace:

    ```yaml
    apiVersion: v1
    kind: Secret
    metadata:
      name: keycloak
      namespace: edp-vault
    data:
      password: cGFzcw==  # pass
      username: dXNlcg==  # user
    type: Opaque
    ```

3. In the `edp-vault` namespace, create a Role with a permission to read secrets:

    ```yaml
    apiVersion: rbac.authorization.k8s.io/v1
    kind: Role
    metadata:
      namespace: edp-vault
      name: external-secret-store
    rules:
    - apiGroups: [""]
      resources:
      - secrets
      verbs:
      - get
      - list
      - watch
    - apiGroups:
      - authorization.k8s.io
      resources:
      - selfsubjectrulesreviews
      verbs:
      - create
    ```

4. In the `edp-vault` namespace, create a ServiceAccount used by `SecretStore`:

    ```yaml
    apiVersion: v1
    kind: ServiceAccount
    metadata:
      name: secret-manager
      namespace: edp
    ```

5. Connect the Role from the `edp-vault` namespace with the ServiceAccount in the `edp` namespace:

    ```yaml
    apiVersion: rbac.authorization.k8s.io/v1
    kind: RoleBinding
    metadata:
      name: eso-from-edp
      namespace: edp-vault
    subjects:
      - kind: ServiceAccount
        name: secret-manager
        namespace: edp
    roleRef:
      apiGroup: rbac.authorization.k8s.io
      kind: Role
      name: external-secret-store
    ```

6. Create a SecretStore in the `edp` namespace, and use ServiceAccount for authentication:

    ```yaml
    apiVersion: external-secrets.io/v1beta1
    kind: SecretStore
    metadata:
      name: edp-vault
      namespace: edp
    spec:
      provider:
        kubernetes:
          remoteNamespace: edp-vault  # namespace with secrets
          auth:
            serviceAccount:
              name: secret-manager
          server:
            caProvider:
              type: ConfigMap
              name: kube-root-ca.crt
              key: ca.crt
    ```

7. Each secret must be defined by the `ExternalSecret` object. A code example below creates the `keycloak` secret in the `edp` namespace based on a secret with the same name in the `edp-vault` namespace:

    ```yaml
    apiVersion: external-secrets.io/v1beta1
    kind: ExternalSecret
    metadata:
      name: keycloak
      namespace: edp
    spec:
      refreshInterval: 1h
      secretStoreRef:
        kind: SecretStore
        name: edp-vault
      # target:
      #   name: secret-to-be-created  # name of the k8s Secret to be created. metadata.name used if not defined
      data:
      - secretKey: username       # key to be created
        remoteRef:
          key: keycloak           # remote secret name
          property: username      # value will be fetched from this field
      - secretKey: password       # key to be created
        remoteRef:
          key: keycloak           # remote secret name
          property: password      # value will be fetched from this field
    ```

Apply the same approach for enabling secrets management in the namespaces used for microservices development, such as `sit` and `qa` on the diagram above.

## AWS Systems Manager Parameter Store

AWS SSM Parameter Store can be used as a [Secret Provider for ESO](https://external-secrets.io/latest/provider-aws-parameter-store/). For EDP, it is recommended to use the [IAM Roles For Service Accounts approach](https://external-secrets.io/latest/provider-aws-parameter-store/#eks-service-account-credentials) (see a diagram below).

![eso-with-ssm](../assets/operator-guide/eso-ssm.png)

### EDP Install Scenario
In order to [install EDP](./install-edp.md), a list of passwords must be created: defectdojo-ciuser-token, keycloak-client-headlamp-secret and keycloak. Follow the steps below, to get secrets from the SSM:

1. In the AWS, create an AWS IAM policy and an IAM role used by `ServiceAccount` in `SecretStore`. The IAM role must have permissions to get values from the SSM Parameter Store.<a name="step 1"></a>

    a. Create an IAM policy that allows to get values from the Parameter Store with the `edp/` path. Use your `AWS Region` and `AWS Account Id`:

    ```json
    {
    "Version": "2012-10-17",
    "Statement": [
        {
            "Sid": "VisualEditor0",
            "Effect": "Allow",
            "Action": "ssm:GetParameter*",
            "Resource": "arn:aws:ssm:eu-central-1:012345678910:parameter/edp/*"
        }
    ]
    }
    ```

    b. Create an AWS IAM role with trust relationships (defined below) and attach the IAM policy. Put your string for `Federated` value ([see more](./enable-irsa.md) on IRSA enablement for EKS Cluster) and AWS region.<a name="step 1.b"></a>

    ```json
    {
        "Version": "2012-10-17",
        "Statement": [
            {
                "Effect": "Allow",
                "Principal": {
                    "Federated": "arn:aws:iam::012345678910:oidc-provider/oidc.eks.eu-central-1.amazonaws.com/id/XXXXXXXXXXXXXXXXXX"
                },
                "Action": "sts:AssumeRoleWithWebIdentity",
                "Condition": {
                    "StringLike": {
                        "oidc.eks.eu-central-1.amazonaws.com/id/XXXXXXXXXXXXXXXXXX:sub": "system:serviceaccount:edp:*"
                    }
                }
            }
        ]
    }
    ```

2. Create a secret in the AWS Parameter Store with the name `/edp/my-json-secret`. This secret is represented as a parameter of type string within the AWS Parameter Store:<a name="step 2"></a>


    ```json
    {
      "keycloak":
      {
        "username": "keycloak-username",
        "password": "keycloak-password"
      },
      "defectdojo-ciuser-token":
      {
        "token": "XXXXXXXXXXXX",
        "url": "https://defectdojo.example.com"
      },
      "kaniko-docker-config":  "secret-string",
      "github-config":
      {
        "id_rsa": "id-rsa-key",
        "token": "github-token",
        "secretString": "XXXXXXXXXXXX"
      },
      "gitlab-config":
      {
        "id_rsa": "id-rsa-key",
        "token": "gitlab-token",
        "secretString": "XXXXXXXXXXXX"
      },
      "jira-user":
      {
        "username": "jira-username",
        "password": "jira-password"
      },
      "harbor": {
        "HARBOR_ADMIN_PASSWORD": "password",
        "secretKey": "secret-key",
        "REGISTRY_HTPASSWD": "ht-password",
        "REGISTRY_PASSWD": "password"
      },
      "oauth2-proxy-cookie-secret": {"cookie-secret": "XXXXXXXXXXXX"},
      "nexus-proxy-cookie-secret": {"cookie-secret": "XXXXXXXXXXXX"},
      "keycloak-client-headlamp-secret":  "XXXXXXXXXXXX",
      "keycloak-client-argo-secret":  "XXXXXXXXXXXX"
    }
    ```
  For better clarification, please refer to the table below to understand the contents of the secret above:

    |Name|Field|Description|
    |:-|:-|:-|
    |keycloak|username|Admin username for keycloak, used by keycloak operator|
    |keycloak|password|Admin password for keycloak, used by keycloak operator|
    |defectdojo-ciuser-token|token|Defectdojo token with admin permissions|
    |defectdojo-ciuser-token|url|Defectdojo url|
    |github-config|id_rsa|Private key from github repo in base64|
    |github-config|token|Api token|
    |github-config|secretString|Random string|
    |gitlab-config|id_rsa|Private key from gitlab repo in base64|
    |gitlab-config|token|Api token|
    |gitlab-config|secretString|Random string|
    |jira-user|username|Jira username in base64|
    |jira-user|password|Jira password in base64|
    |oauth2-proxy-cookie-secret|cookie-secret|Secret key for keycloak client in base64|
    |nexus-proxy-cookie-secret|cookie-secret|Secret key for keycloak client in base64|
    |keycloak-client-headlamp-secret||Secret key for keycloak client in base64|
    |keycloak-client-argo-secret||Secret key for keycloak client in base64|
    |harbor|HARBOR_ADMIN_PASSWORD|Administrator password in base64|
    |harbor|secretKey|Secret string in base64|
    |harbor|REGISTRY_HTPASSWD|Secret string in base64|
    |harbor|REGISTRY_PASSWD|Secret string in base64|

    To create the `kaniko-docker-config` field using a string in base64 format, you can use the following command with your login/password:

    ```bash
      kaniko=$(echo -n '<username>:<password>' | base64) && echo -e "{\n  \"auths\": {\n    \"https://index.docker.io/v1/\": {\n      \"auth\": \"$kaniko\"\n    }\n  },\n  \"credsStore\": \"ecr-login\"\n}" | base64

    ```


3. Set External Secret operator enabled by updating the values.yaml file:

    ```yaml title="EDP install values.yaml"
    externalSecrets:
      enabled: true
    ```

4. Install/upgrade edp-install:

    ```bash
    helm upgrade --install edp epamedp/edp-install --wait --timeout=900s \
    --version <edp_version> \
    --values values.yaml \
    --namespace <edp-project> \
    --atomic
    ```


## Related Articles
* [Install External Secrets Operator](install-external-secrets-operator.md)