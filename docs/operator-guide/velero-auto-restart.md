# Velero Auto Restart by Cronjob

In case if Velero logs 403 error after a cluster is up and running (with [amazon-eks-pod-identity-webhook](https://github.com/aws/amazon-eks-pod-identity-webhook)), it is necessary to restart the Velero pod. Use a cronjob according to the following template:

<details>
<summary><b>View: template</b></summary>

```yaml
---
kind: Role
apiVersion: rbac.authorization.k8s.io/v1
metadata:
  namespace: <VELERO_NAMESPACE>
  name: velero-restart
rules:
  - apiGroups: [""]
    resources:
      - pods
    verbs:
      - 'delete'
      - 'list'
---
kind: RoleBinding
apiVersion: rbac.authorization.k8s.io/v1beta1
metadata:
  name: velero-restart
  namespace: <VELERO_NAMESPACE>
subjects:
  - kind: ServiceAccount
    name: velero-pod-watcher
    namespace: <VELERO_NAMESPACE>
roleRef:
  kind: Role
  name: velero-restart
  apiGroup: ""
---
apiVersion: v1
kind: ServiceAccount
metadata:
  name: velero-pod-watcher
  namespace: <VELERO_NAMESPACE>
---
apiVersion: batch/v1beta1
kind: CronJob
metadata:
  name: remove-velero-pod
  namespace: <VELERO_NAMESPACE>
spec:
  schedule: "0 9 * * MON-FRI"
  jobTemplate:
    spec:
      template:
        spec:
          serviceAccountName: velero-pod-watcher
          containers:
            - name: kubectl-runner
              image: bitnami/kubectl
              command:
                - /bin/sh
                - -c
                - podname=$(kubectl get -n <VELERO_NAMESPACE> -o name --field-selector status.phase=Running --no-headers=true pods --sort-by=.metadata.creationTimestamp | tail -n 1 | awk -F "/" '{print $2}'); kubectl delete pod ${podname} -n <VELERO_NAMESPACE>
          restartPolicy: Never
```
</details>

Modify Cron expression if necessary in the CronJob manifest.