## Installation on Kubernetes

### Prerequisites
1. Kubernetes cluster installed with minimum 2 worker nodes with total capacity 16 Cores and 40Gb RAM;
2. Machine with [kubectl](https://kubernetes.io/docs/tasks/tools/install-kubectl/) installed with a cluster-admin access to the Kubernetes cluster;
3. Helm installed by executing the following command:

`curl https://raw.githubusercontent.com/helm/helm/master/scripts/get-helm-3 | bash
`

### Admin Space

Before starting EDP deployment, the Admin Space (a special namespace in K8S or a project in OpenShift) should be deployed from where afterwards EDP will be deployed. 

To deploy the Admin Space, follow the steps below:

* Go to the [releases](https://github.com/epmd-edp/edp-install/releases) page of this repository, choose a version, download an archive and unzip it.

_**NOTE:**: It is highly recommended to use the latest released version._

* Apply the "edp-preinstall" template to create the Admin Space:
 
 `kubectl apply -f kubernetes-templates/edp-preinstall.yaml`
* Add the edp-deploy-role role to EDP service account: 

`kubectl create clusterrolebinding <any_name> --clusterrole=edp-deploy-role --serviceaccount=edp-deploy:edp`
 
* Add admin role to EDP service account: 

`kubectl create clusterrolebinding <any_name> --clusterrole=admin --serviceaccount=edp-deploy:edp`
* Create secret for database:

`kubectl -n edp-deploy create secret generic admin-console-db --from-literal=username=<your_username> --from-literal=password=<your_password>`
* Deploy database from the following template:
```
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
                  name: admin-console-db
            - name: POSTGRES_PASSWORD
              valueFrom:
                secretKeyRef:
                  key: password
                  name: admin-console-db
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
      serviceAccountName: edp
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
    
### Install EDP
* Choose an edp name, e.g. "demo", and create the <edp_name>-edp-cicd namespace (e.g. "demo-edp-cicd").
* Create EDP service account in the <edp_name>-edp-cicd namespace:

`kubectl -n <your_edp_name>-edp-cicd create sa edp`
* Add the edp-deploy-role role to EDP service account: 

`kubectl create clusterrolebinding <any_unique_name> --clusterrole=edp-deploy-role --serviceaccount=<your_edp_name>-edp-cicd:edp`
 
* Add admin role to EDP service account: 

`kubectl create clusterrolebinding <any_unique_name> --clusterrole=admin --serviceaccount=<your_edp_name>-edp-cicd:edp`
* Deploy operators in the <edp_name>-edp-cicd namespace by following the corresponding instructions in their repositories:
    - [keycloak-operator](https://github.com/epmd-edp/keycloak-operator)
    - [codebase-operator](https://github.com/epmd-edp/codebase-operator)
    - [reconciler](https://github.com/epmd-edp/reconciler)
    - [cd-pipeline-operator](https://github.com/epmd-edp/cd-pipeline-operator)
    - [edp-component-operator](https://github.com/epmd-edp/edp-component-operator)
    - [nexus-operator](https://github.com/epmd-edp/nexus-operator)
    - [sonar-operator](https://github.com/epmd-edp/sonar-operator)
    - [admin-console-operator](https://github.com/epmd-edp/admin-console-operator)
    - [gerrit-operator](https://github.com/epmd-edp/gerrit-operator)
    - [jenkins-operator](https://github.com/epmd-edp/jenkins-operator)

* Apply EDP chart using Helm. Find below the description of each parameter: 
    - edp.name - name of your EDP tenant to be deployed;
    - edp.version - EDP Image and tag. The released version can be found on [Dockerhub](https://hub.docker.com/r/epamedp/edp-install/tags);
    - edp.superAdmins - administrators of your tenant separated by escaped comma (\,);
    - edp.dnsWildCard - DNS wildcard for routing in your K8S cluster;
    - edp.storageClass - storage class that will be used for persistent volumes provisioning;
    - jenkins.version - EDP image and tag. The released version can be found on [Dockerhub](https://hub.docker.com/r/epamedp/edp-jenkins/tags);
    - jenkins.volumeCapacity - size of persistent volume for Jenkins data, it is recommended to use not less then 10 GB;
    - jenkins.stagesVersion - version of EDP-Stages library for Jenkins. The released version can be found on [GitHub](https://github.com/epmd-edp/edp-library-stages/releases);
    - jenkins.pipelinesVersion - version of EDP-Pipeline library for Jenkins. The released version can be found on [GitHub](https://github.com/epmd-edp/edp-library-pipelines/releases);
    - adminConsole.version - EDP image and tag. The released version can be found on [Dockerhub](https://hub.docker.com/r/epamedp/edp-admin-console/tags);
    - edp.additionalToolsTemplate - name of the config map in edp-deploy project with a Helm template that is additionally deployed during the installation (Sonar, Gerrit, Nexus, Secrets, edpName, dnsWildCard, etc.). User variables can be used and are replaced during the provisioning, all the rest must be hardcoded in a template.  
    
    Find below a template sample:
```
apiVersion: v2.edp.epam.com/v1alpha1
kind: Nexus
metadata:
  name: nexus
  namespace: '{{ .Values.edpName }}-edp-cicd'
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
  version: 3.15.1
  volumes:
    - capacity: 5Gi
      name: data
      storage_class: gp2
---
apiVersion: v2.edp.epam.com/v1alpha1
kind: Sonar
metadata:
  name: sonar
  namespace: '{{ .Values.edpName }}-edp-cicd'
spec:
  edpSpec:
    dnsWildcard: '{{ .Values.dnsWildCard }}'
  type: Sonar
  version: 7.6-community
  volumes:
    - capacity: 1Gi
      name: data
      storage_class: gp2
    - capacity: 1Gi
      name: db
      storage_class: gp2
---
apiVersion: v2.edp.epam.com/v1alpha1
kind: GitServer
metadata:
  name: git-epam
  namespace: '{{ .Values.edpName }}-edp-cicd'
spec:
  createCodeReviewPipeline: true
  gitHost: 'git.epam.com'
  gitUser: git
  httpsPort: 443
  nameSshKeySecret: gitlab-sshkey
  sshPort: 22
---
apiVersion: v1
data:
  id_rsa: XXXXXXXXXXXXXXXXXXXXXXXX
  id_rsa.pub: XXXXXXXXXXXXXXXXXXXXXXXX
  username: XXXXXXXXXXXXXXXXXXXXXXX
kind: Secret
metadata:
  name: gitlab-sshkey
  namespace: '{{ .Values.edpName }}-edp-cicd'
type: Opaque
---
apiVersion: v2.edp.epam.com/v1alpha1
kind: JenkinsServiceAccount
metadata:
  name: gitlab-sshkey
  namespace: '{{ .Values.edpName }}-edp-cicd'
spec:
  credentials: 'gitlab-sshkey'
  type: ssh
```
 - Create a file with the template and create config map with the following command:

`kubectl -n edp-deploy create cm additional-tools --from-file=template=<filename>`

 - Edit kubernetes-templates/values.yaml file with your own parameters
 - Run Helm chart installation

Find below the sample of launching a Helm template for EDP installation:
```
helm install <helm_release_name> --namespace edp-deploy kubernetes-templates
```
* In several seconds, the project <*edp-name*>-edp-cicd will be created. The full installation with integration between tools will take at least 10 minutes.