## Keycloak Installation on Kubernetes

In order to install Keycloak on Kubernetes cluster, follow the steps below:

* Create a namespace with any name <keycloak-namespace> for Keycloak:
```bash
kubectl create namespace <keycloak-namespace>
```

* Create a secret for Keycloak database:
```bash
kubectl -n <keycloak-namespace> create secret generic keycloak-db --from-literal=keycloak-db-user=<keycloak_db_username> --from-literal=keycloak-db-password=<keycloak_db_password>
```

* Create a secret for Keycloak admin user:
```bash
kubectl -n <keycloak-namespace> create secret generic keycloak --from-literal=username=<keycloak_admin_username> --from-literal=password=<keycloak_admin_password>
```

* Deploy Keycloak in security namespace from the following template:
```yaml
apiVersion: v1
kind: ServiceAccount
metadata:
  name: keycloak
---
apiVersion: v1
kind: PersistentVolumeClaim
metadata:
  name: "keycloak-data"
spec:
  accessModes:
  - ReadWriteOnce
  resources:
    requests:
      storage: "2Gi"
  storageClassName: "gp2"
---
apiVersion: v1
kind: Service
metadata:
  name: keycloak
  labels:
    app: keycloak
spec:
  ports:
  - name: gui
    port: 8080
  selector:
    app: keycloak
  type: ClusterIP
---
apiVersion: v1
kind: Service
metadata:
  name: "keycloak-db"
  labels:
    app: "keycloak-db"
spec:
  ports:
  - name: "keycloak-db"
    port: 5432
  selector:
    app: "keycloak-db"
  type: ClusterIP
  sessionAffinity: None
---
apiVersion: extensions/v1beta1
kind: Deployment
metadata:
  name: keycloak
  labels:
    app: keycloak
spec:
  replicas: 1
  selector:
    matchLabels:
      app: keycloak
  strategy:
    type: RollingUpdate
  template:
    metadata:
      labels:
        app: keycloak
    spec:
      serviceAccountName: keycloak
      initContainers:
      - name: "init-keycloak-db"
        image: busybox
        command: ["sh", "-c", "while ! nc -w 1 keycloak-db 5432 </dev/null; do echo waiting for keycloak-db; sleep 10; done;"]
      containers:
      - name: keycloak
        image: "quay.io/keycloak/keycloak:8.0.2"
        imagePullPolicy: Always
        ports:
          - name: gui
            containerPort: 8080
        env:
        - name: KEYCLOAK_USER
          valueFrom:
            secretKeyRef:
              name: keycloak
              key: username
        - name: KEYCLOAK_PASSWORD
          valueFrom:
            secretKeyRef:
              name: keycloak
              key: password
        - name: DB_DATABASE
          value: keycloak-db
        - name: DB_USER
          valueFrom:
            secretKeyRef:
              name: keycloak-db
              key: keycloak-db-user
        - name: DB_PASSWORD
          valueFrom:
            secretKeyRef:
              name: keycloak-db
              key: keycloak-db-password
        - name: DB_ADDR
          value: keycloak-db
        - name: DB_PORT
          value: '5432'
        - name: DB_VENDOR
          value: postgres
        - name: PROXY_ADDRESS_FORWARDING
          value: "true"
        livenessProbe:
          failureThreshold: 5
          initialDelaySeconds: 180
          periodSeconds: 20
          successThreshold: 1
          tcpSocket:
            port: 8080
          timeoutSeconds: 5
        readinessProbe:
          failureThreshold: 5
          initialDelaySeconds: 60
          periodSeconds: 20
          successThreshold: 1
          tcpSocket:
            port: 8080
          timeoutSeconds: 5
        resources:
          requests:
            memory: 500Mi
        terminationMessagePath: /dev/termination-log
        terminationMessagePolicy: File
      dnsPolicy: ClusterFirst
      restartPolicy: Always
      schedulerName: default-scheduler
      securityContext: {}
      terminationGracePeriodSeconds: 30
---
apiVersion: extensions/v1beta1
kind: Deployment
metadata:
  labels:
    app: keycloak
  name: "keycloak-db"
spec:
  replicas: 1
  selector:
    matchLabels:
      app: "keycloak-db"
  strategy:
    type: Recreate
  template:
    metadata:
      labels:
        app: "keycloak-db"
    spec:
      containers:
      - name: "keycloak-db"
        env:
        - name: POSTGRES_DB
          value: "keycloak-db"
        - name: POSTGRES_USER
          valueFrom:
            secretKeyRef:
              name: keycloak-db
              key: keycloak-db-user
        - name: POSTGRES_PASSWORD
          valueFrom:
            secretKeyRef:
              name: keycloak-db
              key: keycloak-db-password
        - name: PGDATA
          value: /var/lib/postgresql/data/pgdata
        - name: POD_IP
          valueFrom: { fieldRef: { fieldPath: status.podIP } }
        image: postgres:9.6
        imagePullPolicy: IfNotPresent
        resources:
          requests:
            memory: 500Mi
        ports:
          - containerPort: 5432
            protocol: TCP
        livenessProbe:
          exec:
            command:
            - sh
            - -c
            - exec pg_isready --host $POD_IP
          initialDelaySeconds: 60
          timeoutSeconds: 5
          failureThreshold: 6
        readinessProbe:
          exec:
            command:
            - sh
            - -c
            - exec pg_isready --host $POD_IP
          initialDelaySeconds: 60
          timeoutSeconds: 3
          periodSeconds: 5
        volumeMounts:
          - mountPath: /var/lib/postgresql/data
            name: keycloak-db
      serviceAccount: keycloak
      volumes:
      - name: keycloak-db
        persistentVolumeClaim:
          claimName: keycloak-data
```

* Modify the "host" value (using `keycloak-security.<your-domain.name>` format)
  in the following template and deploy it in a `security` namespace:
```yaml
apiVersion: extensions/v1beta1
kind: Ingress
metadata:
  annotations:
    nginx.ingress.kubernetes.io/force-ssl-redirect: "true"
  generation: 1
  labels:
    app: keycloak
  name: keycloak
spec:
  rules:
  - host: keycloak-security.example.com
    http:
      paths:
      - backend:
          serviceName: keycloak
          servicePort: 8080
        path: /
```

* Wait for the Keycloak URL to become accessible.
