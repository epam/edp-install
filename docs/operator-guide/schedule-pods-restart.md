# Schedule Pods Restart

In case it is necessary to restart pods, use a CronJob according to the following template:

<details>
<summary><b>View: template</b></summary>

```yaml
---
kind: Role
apiVersion: rbac.authorization.k8s.io/v1
metadata:
  namespace: <NAMESPACE>
  name: pod-restart
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
  name: pod-restart
  namespace: <NAMESPACE>
subjects:
  - kind: ServiceAccount
    name: pod-watcher
    namespace: <NAMESPACE>
roleRef:
  kind: Role
  name: pod-restart
  apiGroup: ""
---
apiVersion: v1
kind: ServiceAccount
metadata:
  name: pod-watcher
  namespace: <NAMESPACE>
---
apiVersion: batch/v1beta1
kind: CronJob
metadata:
  name: remove-pod
  namespace: <NAMESPACE>
spec:
  schedule: "0 9 * * MON-FRI"
  jobTemplate:
    spec:
      template:
        spec:
          serviceAccountName: pod-watcher
          containers:
            - name: kubectl-runner
              image: bitnami/kubectl
              command:
                - /bin/sh
                - -c
                - podname=$(kubectl get -n <NAMESPACE> -o name --field-selector status.phase=Running --no-headers=true pods --sort-by=.metadata.name | grep <POD_NAME_PATTERN> | awk -F "/" '{print $2}'); kubectl delete pod ${podname} -n <NAMESPACE>
          restartPolicy: Never
```
---
</details>

Modify the Cron expression in the CronJob manifest if needed.