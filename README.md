# How to Install EDP

_EDP installation can be applied on two container orchestration platforms: OpenShift and Kubernetes._

## Kubernetes

### Prerequisites
1. Machine with [kubectl](https://kubernetes.io/docs/tasks/tools/install-kubectl/) installed with a cluster-admin access to the Kubernetes cluster;
2. Helm installed by executing the following command:

`curl https://raw.githubusercontent.com/helm/helm/master/scripts/get-helm-3 | bash
`

### Admin Space

Before starting EDP deployment, the Admin Space (a special namespace in K8S or a project in OpenShift) should be deployed from where afterwards EDP will be deployed. 

To deploy the Admin Space, follow the steps below:

* Go to the [releases](https://github.com/epmd-edp/edp-install/releases) page of this repository, choose a version, download an archive and unzip it.

_**NOTE:**: It is highly recommended to use the latest released version._

* Apply the "edp-preinstall" template to create the Admin Space:
 
 `kubectl apply -f edp-preinstall.yaml`
* Add cluster admin role to EDP service account: 

`kubectl create clusterrolebinding <any_name> --clusterrole=admin --serviceaccount=edp-deploy:edp`
* Deploy operators by following the corresponding instructions in their repositories:
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
    
### Install EDP

* Apply EDP chart using Helm. Find below the description of each parameter: 
    - edp.name - name of your EDP tenant to be deployed;
    - edp.version - EDP Image and tag. The released version can be found on [Dockerhub](https://hub.docker.com/r/epamedp/edp-install/tags);
    - edp.superAdmins - administrators of your tenant separated by comma;
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
  id_rsa: YWTkvRCt0bUdXSC9PcGRGcUZUR
  id_rsa.pub: YWTkvRCt0bUdXSC9PcGRGcUZUR 
  username: Z2l0bGFiLXNzaGtleQ==
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
Find below the sample of launching a Helm template for EDP installation:
```
helm install <helm_release_name> --namespace edp-deploy --set edp.name=<edp_name> --set edp.version=epamedp/edp-install:<tag> --set 'edp.superAdmins='username1,username2' --set edp.dnsWildCard=<dns_wildcard> --set edp.storageClass=<storage_class_name> --set edp.additionalToolsTemplate=<config_map_name> --set jenkins.version=epamedp/edp-jenkins:<tag> --set jenkins.volumeCapacity=10Gi --set jenkins.stagesVersion=<version> --set jenkins.pipelinesVersion=<version> --set adminConsole.version=epamedp/edp-admin-console:<tag> .
```
* In several seconds, the project <*edp-name*>-edp-cicd will be created. The full installation with integration between tools will take about 10 minutes.