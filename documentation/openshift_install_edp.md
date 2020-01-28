## EDP Installation on OpenShift

### Prerequisites
1. OpenShift cluster installed with minimum 2 worker nodes with total capacity 16 Cores and 40Gb RAM;
2. Nodes kernel parameters and security limits are configured to meet Docker host for Sonarqube 7.9 [requirements](https://hub.docker.com/_/sonarqube):
    - vm.max_map_count=262144
    - fs.file-max=65536
    - ulimit nofile 65536
    - ulimit nproc 4096   
3. Load balancer (if any exists in front of OpenShift router or ingress controller) is configured with the disabled HTTP/2 protocol and header size of 32k support;
4. Cluster nodes and pods should have access to the cluster via external URLs. For instance, you should add in AWS your VPC NAT gateway elastic IP to your cluster external load balancers security group);
5. Keycloak instance is installed in the "security" project. To get accurate information on how to install Keycloak, please refer to the [Keycloak Installation on OpenShift](openshift_install_keycloak.md) instruction;
6. The "openshift" realm is created in Keycloak;
7. The "keycloak" secret with administrative access username and password exists in the "security" project; 
8. Machine with [oc](https://docs.okd.io/latest/cli_reference/get_started_cli.html#installing-the-cli) is installed with a cluster-admin access to the OpenShift cluster;

### Admin Space
Before starting EDP deployment, the Admin Space (a special namespace in K8S or a project in OpenShift) should be deployed from where afterwards EDP will be deployed.

To deploy the Admin Space, follow the steps below:

* Go to the [releases](https://github.com/epmd-edp/edp-install/releases) page of this repository, choose a version, download an archive and unzip it.

_**NOTE**: It is highly recommended to use the latest released version._

* Apply the "edp-preinstall" template to create the Admin Space:
```bash
 oc process -f oc-templates/edp-preinstall.yaml | oc apply -f -
```
* Add the edp-deploy-role role to EDP service account: 
`oc create clusterrolebinding <any_name> --clusterrole=edp-deploy-role --serviceaccount=edp-deploy:edp`
 
* Add admin role to EDP service account: 
`oc create clusterrolebinding <any_name> --clusterrole=admin --serviceaccount=edp-deploy:edp`

* Add the security context constraint edp to edp service account in edp-deploy project: 
```bash
oc adm policy add-scc-to-user edp -z edp -n edp-deploy
```

* If this is your first EDP tenant on this cluster, perform the following

    * Create admin secret for the Wizard database: 
`
oc -n edp-deploy create secret generic super-admin-db --from-literal=username=<db_admin_username> --from-literal=password=<db_admin_password>
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

* Create secret for EDP tenant database user:
```bash
oc -n edp-deploy create secret generic admin-console-db --from-literal=username=<tenant_db_username> --from-literal=password=<tenant_db_password>
```
    
### Install EDP
* Choose an EDP tenant name, e.g. "demo", and create the <edp_name>-edp-cicd namespace (e.g. "demo-edp-cicd").
* Create EDP service account in the <edp_name>-edp-cicd namespace:
`oc -n <your_edp_name>-edp-cicd create sa edp`

* Add the edp-deploy-role role to EDP service account: 
`oc create clusterrolebinding <any_unique_name> --clusterrole=edp-deploy-role --serviceaccount=<your_edp_name>-edp-cicd:edp`
 
* Add admin role to EDP service account: 
`oc create clusterrolebinding <any_unique_name> --clusterrole=admin --serviceaccount=<your_edp_name>-edp-cicd:edp`

* Add security context constraint edp to edp service account in <your_edp_name>-edp-cicd project: 
```bash
oc adm policy add-scc-to-user edp -z edp -n <your_edp_name>-edp-cicd
```

* Deploy operators in the <edp_name>-edp-cicd namespace by following the corresponding instructions in their repositories:
    - [keycloak-operator](https://github.com/epmd-edp/keycloak-operator/tree/v1.2.1-80)
    - [codebase-operator](https://github.com/epmd-edp/codebase-operator/tree/v2.2.0-90)
    - [reconciler](https://github.com/epmd-edp/reconciler/tree/v2.2.0-111)
    - [cd-pipeline-operator](https://github.com/epmd-edp/cd-pipeline-operator/tree/v2.2.1-57)
    - [edp-component-operator](https://github.com/epmd-edp/edp-component-operator/tree/v0.1.0-3)
    - [nexus-operator](https://github.com/epmd-edp/nexus-operator/tree/v2.2.0-62)
    - [sonar-operator](https://github.com/epmd-edp/sonar-operator/tree/v2.2.0-99)
    - [admin-console-operator](https://github.com/epmd-edp/admin-console-operator/tree/v2.2.0-77)
    - [gerrit-operator](https://github.com/epmd-edp/gerrit-operator/tree/release-2.2)
    - [jenkins-operator](https://github.com/epmd-edp/jenkins-operator/tree/v2.2.1-131)

* Create an OpenShift template with with additional tools (e.g. Sonar, Gerrit, Nexus, Secrets, any other resources) that are non-mandatory.
* Inspect the list of parameters that can be used in the OpenShift template and replaced during the provisioning:
    
   - EDP_NAME - this parameter will be replaced with the EDP_NAME value, which is set in EDP-Install template;
   - DNS_WILDCARD - this parameter will be replaced with the DNS_WILDCARD value, which is set in EDP-Install template;
       
* "Users" section in Gerrit and Nexus resources definitions should be filled with administrators of your tenant 
       
_*NOTE*: Other parameters must be hardcoded in a template._

Find below a template sample for additional tools:
```yaml
apiVersion: template.openshift.io/v1
kind: Template
metadata:
  annotations:
    description: Template for deploying additional components on top of EDP
    openshift.io/display-name: EDP - The Rocket
    template.openshift.io/provider-display-name: EPAM
    template.openshift.io/support-url: https://www.epam.com
  creationTimestamp: null
  name: additional-tools
objects:
- apiVersion: v2.edp.epam.com/v1alpha1
  kind: Nexus
  metadata:
    name: nexus
    namespace: ${EDP_NAME}-edp-cicd
  spec:
    edpSpec:
      dnsWildcard: ${DNS_WILDCARD}
    keycloakSpec:
      enabled: true
    version: 3.15.1
    volumes:
    - capacity: 10Gi
      name: data
      storage_class: gp2
    users:
    - email: ""
      first_name: ""
      last_name: ""
      roles:
      - nx-admin
      username: admin1@example.com
    - email: ""
      first_name: ""
      last_name: ""
      roles:
      - nx-admin
      username: admin2@example.com
- apiVersion: v2.edp.epam.com/v1alpha1
  kind: Sonar
  metadata:
    name: sonar
    namespace: ${EDP_NAME}-edp-cicd
  spec:
    edpSpec:
      dnsWildcard: ${DNS_WILDCARD}
    type: Sonar
    version: 7.9-community
    volumes:
    - capacity: 1Gi
      name: data
      storage_class: gp2
    - capacity: 1Gi
      name: db
      storage_class: gp2
- apiVersion: v2.edp.epam.com/v1alpha1
  kind: GitServer
  metadata:
    name: gerrit
    namespace: ${EDP_NAME}-edp-cicd
  spec:
    createCodeReviewPipeline: false
    edpSpec:
      dnsWildcard: ${DNS_WILDCARD}
    gitHost: gerrit.${EDP_NAME}-edp-cicd
    gitUser: jenkins
    httpsPort: 443
    nameSshKeySecret: gerrit-ciuser-sshkey
    sshPort: 22
- apiVersion: v2.edp.epam.com/v1alpha1
  kind: Gerrit
  metadata:
    name: gerrit
    namespace: ${EDP_NAME}-edp-cicd
  spec:
    keycloakSpec:
      enabled: true
      url: ""
    sshPort: 0
    type: Gerrit
    version: 2.16.10
    volumes:
    - capacity: 1Gi
      name: data
      storage_class: gp2
    users:
    - groups:
      - Administrators
      username: admin1@example.com
    - groups:
      - Administrators
      username: admin2@example.com
parameters:
- description: Unique name for EDP tenant which will be used during installation
  displayName: Name for EDP tenant
  name: EDP_NAME
  required: true
  value: test
- description: Unique DNS wildcard name for EDP tenant which will be used by ingresses
    in k8s
  displayName: DNS wildcard for EDP tenant
  name: DNS_WILDCARD
  required: true
  value: default_wildcard
```

* Create a file with the template and create an OpenShift template with the following command:

`oc -n edp-deploy apply -f <filename>`

* Apply EDP template using oc. 

>**WARNING**: Chart has some **hardcoded** parameters, which are optional for editing, and some **mandatory** parameters that can be specified by user.

Find below the description of both parameters types:

Hardcoded parameters (optional): 
```
   - EDP_VERSION - EDP Image and tag. The released version can be found on [Dockerhub](https://hub.docker.com/r/epamedp/edp-install/tags);
   - ADDITIONAL_TOOLS_TEMPLATE_NAME - name of the OpenShift template in the edp-deploy project that is additionally deployed during the installation (Sonar, Gerrit, Nexus, Secrets, edpName, dnsWildCard, etc.). User variables can be used and are replaced during the provisioning, all the rest must be hardcoded in a template.
   - JENKINS_VOLUME_CAPACITY - size of persistent volume for Jenkins data, it is recommended to use not less then 10 GB;
   - JENKINS_IMAGE_VERSION - EDP image and tag. The released version can be found on [Dockerhub](https://hub.docker.com/r/epamedp/edp-jenkins/tags);
   - JENKINS_STAGES_VERSION - version of EDP-Stages library for Jenkins. The released version can be found on [GitHub](https://github.com/epmd-edp/edp-library-stages/releases);
   - JENKINS_PIPELINES_VERSION - version of EDP-Pipeline library for Jenkins. The released version can be found on [GitHub](https://github.com/epmd-edp/edp-library-pipelines/releases);
   - ADMIN_CONSOLE_VERSION - EDP image and tag. The released version can be found on [Dockerhub](https://hub.docker.com/r/epamedp/edp-admin-console/tags);
```

 Mandatory parameters:
```
   - EDP_NAME - previously defined name of your EDP tenant that is to be deployed (e.g. "demo");
   - DNS_WILDCARD - DNS wildcard for routing in your K8S cluster;
   - STORAGE_CLASS_NAME - storage class that will be used for persistent volumes provisioning;
   - EDP_SUPER_ADMINS - administrators of your tenant separated by comma (,);
```

* Edit the oc-templates/edp-install.yaml file by applying your own parameters;
* Add EDP install template in OpenShift with the following command:
 ```
oc -n edp-deploy apply -f oc-templates/edp-install.yaml
```
* Run an OpenShift template deployment or apply template directly in OpenShift Web Console.

Find below the sample of launching an OpenShift template for EDP installation:
```
oc new-app --template=edp --param=<>... --param=<>
```
* The full installation with integration between tools will take at least 10 minutes.