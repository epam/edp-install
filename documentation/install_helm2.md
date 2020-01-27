# Install Helm 2
* Run the following command to install Helm 2 client on your machine:
```bash
curl https://raw.githubusercontent.com/helm/helm/master/scripts/get | bash
```
* [Generate certificates](https://helm.sh/docs/tiller_ssl/#generating-certificate-authorities-and-certificates) (this is optional but required for the production/development clusters with RBAC);
* Verify that you logged into the Kubernetes cluster with ```kubectl```;
* Create a dedicated service account and RoleBinding:
```yaml
apiVersion: v1
kind: ServiceAccount
metadata:
  name: tiller
  namespace: kube-system
---
apiVersion: rbac.authorization.k8s.io/v1
kind: ClusterRoleBinding
metadata:
  name: tiller
roleRef:
  apiGroup: rbac.authorization.k8s.io
  kind: ClusterRole
  name: cluster-admin
subjects:
  - kind: ServiceAccount
    name: tiller
    namespace: kube-system
```
* Install the cluster component named Tiller:
```bash
helm init --override 'spec.template.spec.containers[0].command'='{/tiller,--storage=secret}' --tiller-tls --tiller-tls-verify --tiller-tls-cert=tiller.cert.pem --tiller-tls-key=tiller.key.pem --tls-ca-cert=ca.cert.pem --service-account=tiller --tiller-namespace=kube-system
```
> _NOTE: Adapt paths and omit TLS flags in case you skipped the certificates generation._
* Verify that tiller deployment in kube-system namespace is completed.