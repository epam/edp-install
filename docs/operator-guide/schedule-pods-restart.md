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
  name: apps-restart
rules:
  - apiGroups: ["apps"]
    resources:
      - deployments
      - statefulsets
    verbs:
      - 'get'
      - 'list'
      - 'patch'
---
kind: RoleBinding
apiVersion: rbac.authorization.k8s.io/v1
metadata:
  name: apps-restart
  namespace: <NAMESPACE>
subjects:
  - kind: ServiceAccount
    name: apps-restart-sa
    namespace: <NAMESPACE>
roleRef:
  kind: Role
  name: apps-restart
  apiGroup: ""
---
apiVersion: v1
kind: ServiceAccount
metadata:
  name: apps-restart-sa
  namespace: <NAMESPACE>
---
apiVersion: batch/v1beta1
kind: CronJob
metadata:
  name: apps-rollout-restart
  namespace: <NAMESPACE>
spec:
  schedule: "0 9 * * MON-FRI"
  jobTemplate:
    spec:
      template:
        spec:
          serviceAccountName: apps-restart-sa
          containers:
            - name: kubectl-runner
              image: bitnami/kubectl
              command:
                - /bin/sh
                - -c
                - kubectl get -n <NAMESPACE> -o name deployment,statefulset | grep <NAME_PATTERN>| xargs kubectl -n <NAMESPACE> rollout restart
          restartPolicy: Never
```
</details>

Modify the Cron expression in the CronJob manifest if needed.
