## EDP Installation on OpenShift

### Prerequisites
1. OpenShift cluster installed with minimum 2 worker nodes with total capacity 16 Cores and 40Gb RAM;
2. Load balancer (if any exists in front of OpenShift router or ingress controller) is configured with session stickiness, disabled HTTP/2 protocol and header size of 32k support;
    - Example of Config Map for Nginx ingress controller:
    ```yaml
    kind: ConfigMap
    apiVersion: v1
    metadata:
      name: nginx-configuration
      namespace: ingress-nginx
      labels:
        app.kubernetes.io/name: ingress-nginx
        app.kubernetes.io/part-of: ingress-nginx
    data:
      client-header-buffer-size: 64k
      large-client-header-buffers: 4 64k
      use-http2: "false"
      ```
3. Cluster nodes and pods should have access to the cluster via external URLs. For instance, you should add in AWS your VPC NAT gateway elastic IP to your cluster external load balancers security group);
4. Keycloak instance is installed. To get accurate information on how to install Keycloak, please refer to the [Keycloak Installation on OpenShift](openshift_install_keycloak.md) instruction;
5. The "openshift" realm is created in Keycloak;
6. The "keycloak" secret with administrative access username and password exists in the namespace where Keycloak in installed;
7. Installation machine with [oc](https://docs.okd.io/latest/cli_reference/get_started_cli.html#installing-the-cli) installed with the cluster-admin access to the OpenShift cluster; 
8. Helm 3 installed on the installation machine with the help of the following [instruction](https://v3.helm.sh/docs/intro/install/).

### EDP project
* Clone or download and extract the latest release version that should be installed to a separate folder;

* Choose an EDP tenant name, e.g. "demo", and create the <edp-project> project with any name (e.g. "demo").
Before starting EDP deployment, EDP project <edp-project> in OpenShift should be created.

* Create admin secret for the Wizard database: 
```bash
oc -n <edp-project> create secret generic super-admin-db --from-literal=username=<db_admin_username> --from-literal=password=<db_admin_password>
```

* Deploy database from the following template:
```yaml
apiVersion: v1
kind: ServiceAccount
metadata:
  name: postgres
---
apiVersion: v1 #PVC for EDP Install Wizard DB
kind: PersistentVolumeClaim
metadata:
  annotations:
    volume.beta.kubernetes.io/storage-provisioner: kubernetes.io/aws-ebs
  finalizers:
    - kubernetes.io/pvc-protection
  name: edp-install-wizard-db
spec:
  accessModes:
    - ReadWriteOnce
  resources:
    requests:
      storage: 2Gi
  storageClassName: gp2
  volumeMode: Filesystem
---
apiVersion: extensions/v1beta1 # EDP Install Wizard DB Deployment
kind: Deployment
metadata:
  generation: 1
  labels:
    app: edp-install-wizard-db
  name: edp-install-wizard-db
spec:
  selector:
    matchLabels:
      app: edp-install-wizard-db
  template:
    metadata:
      labels:
        app: edp-install-wizard-db
    spec:
      containers:
        - env:
            - name: POSTGRES_USER
              valueFrom:
                secretKeyRef:
                  key: username
                  name: super-admin-db
            - name: POSTGRES_PASSWORD
              valueFrom:
                secretKeyRef:
                  key: password
                  name: super-admin-db
            - name: PGDATA
              value: /var/lib/postgresql/data/pgdata
            - name: POD_IP
              valueFrom:
                fieldRef:
                  apiVersion: v1
                  fieldPath: status.podIP
            - name: POSTGRES_DB
              value: edp-install-wizard-db
          image: postgres:9.6
          imagePullPolicy: IfNotPresent
          livenessProbe:
            exec:
              command:
                - sh
                - -c
                - exec pg_isready --host $POD_IP -U postgres -d postgres
            failureThreshold: 5
            initialDelaySeconds: 60
            periodSeconds: 20
            successThreshold: 1
            timeoutSeconds: 5
          name: edp-install-wizard-db
          ports:
            - containerPort: 5432
              name: db
              protocol: TCP
          readinessProbe:
            exec:
              command:
                - sh
                - -c
                - exec pg_isready --host $POD_IP -U postgres -d postgres
            failureThreshold: 3
            initialDelaySeconds: 60
            periodSeconds: 20
            successThreshold: 1
            timeoutSeconds: 3
          resources:
            requests:
              memory: 512Mi
          volumeMounts:
            - mountPath: /var/lib/postgresql/data
              name: edp-install-wizard-db
      serviceAccountName: postgres
      volumes:
        - name: edp-install-wizard-db
          persistentVolumeClaim:
            claimName: edp-install-wizard-db
---
apiVersion: v1 # EDP Install Wizard DB Service
kind: Service
metadata:
  name: edp-install-wizard-db
spec:
  ports:
    - name: db
      port: 5432
      protocol: TCP
      targetPort: 5432
  selector:
    app: edp-install-wizard-db
  type: ClusterIP
```

* Create secret for EDP tenant database user:
```bash
oc -n <edp-project> create secret generic admin-console-db --from-literal=username=<tenant_db_username> --from-literal=password=<tenant_db_password>
```
    
### Install EDP
* Deploy operators in the <edp-project> project by following the corresponding instructions in their repositories:
    - [keycloak-operator](https://github.com/epmd-edp/keycloak-operator/tree/release-1.3)
    - [codebase-operator](https://github.com/epmd-edp/codebase-operator/tree/release-2.3)
    - [reconciler](https://github.com/epmd-edp/reconciler/tree/release-2.3)
    - [cd-pipeline-operator](https://github.com/epmd-edp/cd-pipeline-operator/tree/release-2.3)
    - [nexus-operator](https://github.com/epmd-edp/nexus-operator/tree/release-2.3)
    - [sonar-operator](https://github.com/epmd-edp/sonar-operator/tree/release-2.3)
    - [admin-console-operator](https://github.com/epmd-edp/admin-console-operator/tree/release-2.3)
    - [gerrit-operator](https://github.com/epmd-edp/gerrit-operator/tree/release-2.3)
    - [jenkins-operator](https://github.com/epmd-edp/jenkins-operator/tree/release-2.3)
    - [edp-component-operator](https://github.com/epmd-edp/edp-component-operator/tree/release-0.2)

* Create a config map with additional tools (e.g. Sonar, Gerrit, Nexus, Secrets, any other resources) that are non-mandatory.
* Inspect the list of parameters that can be used in the Helm chart and replaced during the provisioning:
    
    - edpName - this parameter will be replaced with the edp.name value, which is set in EDP-Install chart;
    - dnsWildCard - this parameter will be replaced with the edp.dnsWildCard value, which is set in EDP-Install chart;
    - users - this parameter will be replaced with the edp.superAdmins value, which is set in EDP-Install chart. 
       
* "sshPort" value in Gerrit and GitServer custom resources should be set to the same any free NodePort in your cluster which become Gerrit ssh port 
       
_*NOTE*: The users parameter should be used in a cycle because it is presented as the list. Other parameters must be hardcorded in a template._

Find below a template sample for additional tools:
```yaml
apiVersion: v2.edp.epam.com/v1alpha1
kind: Nexus
metadata:
  name: nexus
  namespace: '{{ .Values.edpName }}'
spec:
  edpSpec:
    dnsWildcard: '{{ .Values.dnsWildCard }}'
  keycloakSpec:
    enabled: true
  users:
  {{ range .Values.users }}
  - email: ''
    first_name: ''
    last_name: ''
    roles:
      - nx-admin
    username: {{ . }}
  {{ end }}
  image: "sonatype/nexus3"
  version: 3.21.2
  basePath: ""
  volumes:
    - capacity: 10Gi
      name: data
      storage_class: gp2
---
apiVersion: v2.edp.epam.com/v1alpha1
kind: Sonar
metadata:
  name: sonar
  namespace: '{{ .Values.edpName }}'
spec:
  edpSpec:
    dnsWildcard: '{{ .Values.dnsWildCard }}'
  type: Sonar
  image: sonarqube
  version: 7.9-community
  volumes:
    - capacity: 10Gi
      name: data
      storage_class: gp2
    - capacity: 10Gi
      name: db
      storage_class: gp2
---
apiVersion: v2.edp.epam.com/v1alpha1
kind: GitServer
metadata:
  name: gerrit
  namespace: '{{ .Values.edpName }}'
spec:
  createCodeReviewPipeline: false
  edpSpec:
    dnsWildcard: '{{ .Values.dnsWildCard }}'
  gitHost: 'gerrit.{{ .Values.edpName }}'
  gitUser: jenkins
  httpsPort: 443
  nameSshKeySecret: gerrit-ciuser-sshkey
  sshPort: 22
---
apiVersion: v2.edp.epam.com/v1alpha1
kind: Gerrit
metadata:
  name: gerrit
  namespace: '{{ .Values.edpName }}'
spec:
  image: openfrontier/gerrit
  keycloakSpec:
    enabled: true
  sshPort: 22
  type: Gerrit
  users:
  {{ range .Values.users }}
    - groups:
        - Administrators
      username: {{ . }}
  {{ end }}
  version: 3.1.4
  volumes:
    - capacity: 10Gi
      name: data
      storage_class: gp2
```

* Create a file with the template and create a config map with the following command:
```bash
oc -n <edp-project> create cm additional-tools --from-file=template=<filename>`
```

* Apply EDP chart using Helm. 

The deploy-templates/values.yaml file contains EDP Helm chart parameters.

>**WARNING**: Chart has some **hardcoded** parameters, which are already fixed in file and are optional for editing, and some **mandatory** parameters that must be specified by user. 
 
Find below the description of both parameters types.

Hardcoded parameters (optional): 
```
    - edp.version - EDP Image and tag. The released version can be found on [Dockerhub](https://hub.docker.com/r/epamedp/edp-install/tags);
    - edp.additionalToolsTemplate - name of the config map in edp-deploy project with a Helm template that is additionally deployed during the installation (Sonar, Gerrit, Nexus, Secrets, Any other resources). **You created it in the previous point.**
    - edp.devDeploy: Used for develomplent deploy using CI for production installation should be false;
    - jenkins.version - EDP image and tag. The released version can be found on [Dockerhub](https://hub.docker.com/r/epamedp/edp-jenkins/tags);
    - jenkins.volumeCapacity - size of persistent volume for Jenkins data, it is recommended to use not less then 10 GB;
    - jenkins.stagesVersion - version of EDP-Stages library for Jenkins. The released version can be found on [GitHub](https://github.com/epmd-edp/edp-library-stages/releases);
    - jenkins.pipelinesVersion - version of EDP-Pipeline library for Jenkins. The released version can be found on [GitHub](https://github.com/epmd-edp/edp-library-pipelines/releases);
    - adminConsole.version - EDP image and tag. The released version can be found on [Dockerhub](https://hub.docker.com/r/epamedp/edp-admin-console/tags);
    - perf.* - Integration with PERF is in progress. Should be false so far;
```

Mandatory parameters:
 ```
    - edp.name - previously defined name of your EDP project <edp-project>;
    - edp.superAdmins - administrators of your tenant separated by escaped comma (\,);
    - edp.dnsWildCard - DNS wildcard for routing in your Openshift cluster;
    - edp.storageClass - storage class that will be used for persistent volumes provisioning;
    - edp.platform - openshift or kubernetes
    - edp.keycloakNamespace: namespace where Keycloak is installed;
    - edp.keycloakUrl: FQDN Keycloak URL.
 ```  

 * Edit deploy-templates/values.yaml file with your own parameters;
 * Run Helm chart installation;

Find below the sample of launching a Helm template for EDP installation:
```bash
helm install edp-install --namespace <edp-project> deploy-templates
```

* The full installation with integration between tools will take at least 10 minutes.