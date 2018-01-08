def run(vars) {
    vars['projects'].each() { project ->
        openshift.withCluster() {
            openshift.withProject("${project}") {
                openshift.create("""apiVersion: batch/v1
kind: Job
metadata:
  name: ${vars.serviceType}-${System.currentTimeMillis()}-${vars.imageTag}-${BUILD_NUMBER}
spec:
  completions: 1
  parallelism: 1
  template:
    spec:
      containers:
      - env:
        - name: DB_ENV_POSTGRES_URL
          valueFrom:
            configMapKeyRef:
              key: databaseUrl
              name: env-config
        - name: DB_ENV_POSTGRES_USER
          valueFrom:
            secretKeyRef:
              key: database-user
              name: postgresql
        - name: DB_ENV_POSTGRES_PASSWORD
          valueFrom:
            secretKeyRef:
              key: database-password
              name: postgresql
        - name: LQB_ENV_CHANGELOG_FILE
          valueFrom:
            configMapKeyRef:
              key: liquibaseChangelogFile
              name: env-config
        image: docker-registry.default.svc:5000/${project}/${vars.serviceType}:${vars.imageTag}
        imagePullPolicy: IfNotPresent
        name: ${vars.serviceType}
      restartPolicy: OnFailure""")
            }
        }
    }
}

return this;