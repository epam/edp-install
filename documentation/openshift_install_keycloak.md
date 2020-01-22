## Keycloak installation on OpenShift

* Create security project 
```bash
oc create project security
```

* Add security context constraint anyuid to keycloak service account in security project:
```bash
oc adm policy add-scc-to-user anyuid -z keycloak -n security
```

* Deploy keycloak in security namespace from the following template:
```yaml
apiVersion: v1
kind: Template
metadata:
  name: keycloak
  annotations:
    iconClass: icon-keycloak
    description: Openshift template for KeyCloak service
    openshift.io/provider-display-name: EDP
    openshift.io/support-url: https://www.epam.com
    tags: edp
objects:
- kind: Secret
  apiVersion: v1
  metadata:
    name: ${SERVICE_NAME}
  stringData:
    username: ${KEYCLOAK_USER}
    password: ${KEYCLOAK_PASSWORD}
- kind: Secret
  apiVersion: v1
  metadata:
    name: "${SERVICE_NAME}-db"
  stringData:
    ${SERVICE_NAME}-db-user: ${DB_USER}
    ${SERVICE_NAME}-db-password: ${DB_PASSWORD}
- apiVersion: v1
  kind: ServiceAccount
  metadata:
    name: ${SERVICE_NAME}
- apiVersion: v1
  kind: PersistentVolumeClaim
  metadata:
    name: "${SERVICE_NAME}-data"
  spec:
    accessModes:
    - ReadWriteOnce
    resources:
      requests:
        storage: ${STORAGE_CAPACITY}
    storageClassName: ${STORAGE_CLASS}
- apiVersion: v1
  kind: Route
  metadata:
    annotations:
      description: "Route for ${SERVICE_NAME} service."
    name: ${SERVICE_NAME}
    labels:
      app: ${SERVICE_NAME}
  spec:
    tls:
      insecureEdgeTerminationPolicy: Redirect
      termination: edge
    to:
      kind: Service
      name: ${SERVICE_NAME}
    port:
      targetPort: gui
- apiVersion: v1
  kind: Service
  metadata:
    name: ${SERVICE_NAME}
    labels:
      app: ${SERVICE_NAME}
  spec:
    ports:
    - name: gui
      port: 8080
    selector:
      app: ${SERVICE_NAME}
    type: ClusterIP
- apiVersion: v1
  kind: Service
  metadata:
    name: "${SERVICE_NAME}-db"
    labels:
      app: "${SERVICE_NAME}-db"
  spec:
    ports:
    - name: "${SERVICE_NAME}-db"
      port: 5432
    selector:
      app: "${SERVICE_NAME}-db"
    type: ClusterIP
    sessionAffinity: None
- apiVersion: v1
  kind: DeploymentConfig
  metadata:
    name: ${SERVICE_NAME}
    labels:
      app: ${SERVICE_NAME}
  spec:
    replicas: 1
    selector:
      app: ${SERVICE_NAME}
    strategy:
      activeDeadlineSeconds: 21600
      type: Rolling
      rollingParams:
        failurePolicy: Abort
        intervalSeconds: 1
        maxSurge: 25%
        maxUnavailable: 25%
        timeoutSeconds: 600
        updatePeriodSeconds: 1
    template:
      metadata:
        labels:
          app: ${SERVICE_NAME}
      spec:
        serviceAccountName: ${SERVICE_NAME}
        initContainers:
        - name: "init-${SERVICE_NAME}-db"
          image: busybox
          command: ["sh", "-c", "while ! nc -w 1 ${SERVICE_NAME}-db 5432 </dev/null; do echo waiting for ${SERVICE_NAME}-db; sleep 10; done;"]
        containers:
        - name: ${SERVICE_NAME}
          image: "${SERVICE_IMAGE}:${SERVICE_VERSION}"
          imagePullPolicy: Always
          ports:
            - name: gui
              containerPort: 8080
          env:
          - name: KEYCLOAK_USER
            valueFrom:
              secretKeyRef:
                name: ${SERVICE_NAME}
                key: username
          - name: KEYCLOAK_PASSWORD
            valueFrom:
              secretKeyRef:
                name: ${SERVICE_NAME}
                key: password
          - name: POSTGRES_DATABASE
            value: ${SERVICE_NAME}-db
          - name: POSTGRES_USER
            valueFrom:
              secretKeyRef:
                name: ${SERVICE_NAME}-db
                key: ${SERVICE_NAME}-db-user
          - name: POSTGRES_PASSWORD
            valueFrom:
              secretKeyRef:
                name: ${SERVICE_NAME}-db
                key: ${SERVICE_NAME}-db-password
          - name: POSTGRES_PORT_5432_TCP_ADDR
            value: ${SERVICE_NAME}-db
          - name: POSTGRES_PORT_5432_TCP_PORT
            value: '5432'
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
    triggers:
      - type: ConfigChange
- apiVersion: v1
  kind: DeploymentConfig
  metadata:
    labels:
      app: ${SERVICE_NAME}
    name: "${SERVICE_NAME}-db"
  spec:
    replicas: 1
    selector:
      app: "${SERVICE_NAME}-db"
    strategy:
      activeDeadlineSeconds: 21600
      recreateParams:
        timeoutSeconds: 600
      resources: {}
      type: Recreate
    template:
      metadata:
        labels:
          app: "${SERVICE_NAME}-db"
      spec:
        containers:
        - name: "${SERVICE_NAME}-db"
          env:
          - name: POSTGRES_DB
            value: "${SERVICE_NAME}-db"
          - name: POSTGRES_USER
            valueFrom:
              secretKeyRef:
                name: ${SERVICE_NAME}-db
                key: ${SERVICE_NAME}-db-user
          - name: POSTGRES_PASSWORD
            valueFrom:
              secretKeyRef:
                name: ${SERVICE_NAME}-db
                key: ${SERVICE_NAME}-db-password
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
              name: ${SERVICE_NAME}-db
        serviceAccount: ${SERVICE_NAME}
        volumes:
        - name: ${SERVICE_NAME}-db
          persistentVolumeClaim:
            claimName: ${SERVICE_NAME}-data
parameters:
- displayName: Service name
  name: SERVICE_NAME
  required: true
  value: "keycloak"
- displayName: Service name
  name: SERVICE_NAME
  required: true
  value: "keycloak"
- displayName: Application version
  name: SERVICE_VERSION
  required: true
  value: "3.4.3.Final"
- displayName: Application image
  name: SERVICE_IMAGE
  value: "jboss/keycloak"
  required: true
- displayName: "keycloak-db password"
  name: DB_PASSWORD
  generate: expression
  from: "[a-zA-Z0-9]{10}"
- displayName: "keycloak-db user"
  name: DB_USER
  value: "keycloak"
- displayName: "keycloak password"
  name: KEYCLOAK_PASSWORD
  value: admin
- displayName: "keycloak user"
  name: KEYCLOAK_USER
  value: admin
- displayName: Keycloak storage class
  name: STORAGE_CLASS
  required: true
  value: "gp2"
- displayName: Keycloak storage capacity
  name: STORAGE_CAPACITY
  required: true
  value: "2Gi"
```

* Wait for keycloak URL to be accessible 