## EDP Installation on Kubernetes

### Prerequisites
1. Kubernetes cluster installed with minimum 2 worker nodes with total capacity 16 Cores and 40Gb RAM;
2. Nodes kernel parameters and security limits are configured to meet Docker host for Sonarqube 7.9 [requirements](https://hub.docker.com/_/sonarqube):
    - vm.max_map_count=262144
    - fs.file-max=65536
    - ulimit nofile 65536
    - ulimit nproc 4096
3. Machine with [kubectl](https://kubernetes.io/docs/tasks/tools/install-kubectl/) installed with a cluster-admin access to the Kubernetes cluster;
4. Ingress controller is installed in a cluster, for example [ingress-nginx](https://kubernetes.github.io/ingress-nginx/deploy/);
5. Ingress controller is configured with the disabled HTTP/2 protocol and header size of 32k support;
6. Load balancer (if any exists in front of ingress controller) is configured with the disabled HTTP/2 protocol and header size of 32k support;
7. Cluster nodes and pods should have access to the cluster via external URLs. For instance, you should add in AWS your VPC NAT gateway elastic IP to your cluster external load balancers security group);
8. Keycloak instance is installed in the "security" namespace. To get accurate information on how to install Keycloak, please refer to the [Keycloak Installation on Kubernetes](kubernetes_install_keycloak.md)) instruction;
9. The "openshift" realm is created in Keycloak;
10. The "keycloak" secret with administrative access username and password exists in the "security" namespace;
11. Helm 2 (Helm 3 is not currently supported by EDP) is installed on installation machine and in Kubernetes cluster with the following [instruction](install_helm2.md) by executing the following command:

### Admin Space

Before starting EDP deployment, the Admin Space (a special namespace in K8S or a project in OpenShift) should be deployed from where afterwards EDP will be deployed. 

To deploy the Admin Space, follow the steps below:

* Go to the [releases](https://github.com/epmd-edp/edp-install/releases) page of this repository, choose a version, download an archive and unzip it.

_**NOTE**: It is highly recommended to use the latest released version._

* Apply the "edp-preinstall" template to create the Admin Space:
`kubectl apply -f kubernetes-templates/edp-preinstall.yaml`

* Add the edp-deploy-role role to EDP service account: 
`kubectl create clusterrolebinding <any_name> --clusterrole=edp-deploy-role --serviceaccount=edp-deploy:edp`
 
* Add admin role to EDP service account: 
`kubectl create clusterrolebinding <any_name> --clusterrole=admin --serviceaccount=edp-deploy:edp`

* If this is your first EDP tenant on this cluster, perform the following:

    * Create admin secret for the Wizard database: 
`
kubectl -n edp-deploy create secret generic super-admin-db --from-literal=username=<db_admin_username> --from-literal=password=<db_admin_password>
`

    * Deploy database from the following template:
```yaml
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

* Create secret for the EDP tenant database user:
```bash
kubectl -n edp-deploy create secret generic admin-console-db --from-literal=username=<tenant_db_username> --from-literal=password=<tenant_db_password>
```
    
### Install EDP
* Choose an EDP tenant name, e.g. "demo", and create the <edp_name>-edp-cicd namespace (e.g. "demo-edp-cicd").
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

* Create a config map with additional tools (e.g. Sonar, Gerrit, Nexus, Secrets, any other resources) that are non-mandatory.
* Inspect the list of parameters that can be used in the Helm chart and replaced during the provisioning:
    
    - edpName - this parameter will be replaced with the edp.name value, which is set in EDP-Install chart;
    - dnsWildCard - this parameter will be replaced with the edp.dnsWildCard value, which is set in EDP-Install chart;
    - users - this parameter will be replaced with the edp.superAdmins value, which is set in EDP-Install chart. 
    
_*NOTE*: The users parameter should be used in a cycle because it is presented as the list. Other parameters must be hardcorded in a template._
    
Become familiar with a template sample:
```yaml
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
  version: 7.9-community
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

* Create a file with the template and create a config map with the following command:
`kubectl -n edp-deploy create cm additional-tools --from-file=template=<filename>`

* Apply EDP chart using Helm. 

>**WARNING**: Chart has some **hardcoded** parameters, which are optional for editing, and some **mandatory** parameters that can be specified by user. 
 
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
    - vcs.* - parameters used for integrating Gerrit with external VCS. Are deprecated and will be removed soon. **DO NOT CHANGE THEM.**
    - perf.* - Integration with PERF is in progress. Should be false so far. 
```
 
Mandatory parameters:
 ```
    - edp.name - previously defined name of your EDP tenant that is to be deployed (e.g. "demo");
    - edp.superAdmins - administrators of your tenant separated by escaped comma (\,);
    - edp.dnsWildCard - DNS wildcard for routing in your K8S cluster;
    - edp.storageClass - storage class that will be used for persistent volumes provisioning;
 ```  
 
 * Edit kubernetes-templates/values.yaml file with your own parameters;
 * Run Helm chart installation;

Find below the sample of launching a Helm template for EDP installation:
```bash
helm install <helm_release_name> --namespace edp-deploy kubernetes-templates
```
* The full installation with integration between tools will take at least 10 minutes.