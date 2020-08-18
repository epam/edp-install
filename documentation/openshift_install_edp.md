# EDP Installation on OpenShift

Inspect the prerequisites and the main steps to perform with the aim to install EPAM Delivery Platform on OpenShift.

## Prerequisites
1. OpenShift cluster installed with minimum 2 worker nodes with total capacity 16 Cores and 40Gb RAM;
2. Load balancer (if any exists in front of OpenShift router or ingress controller) is configured with session stickiness, disabled HTTP/2 protocol and header size of 64k support;
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
6. The installation machine with [oc](https://docs.openshift.com/container-platform/4.2/cli_reference/openshift_cli/getting-started-cli.html#cli-installing-cli_cli-developer-commands) is installed with the cluster-admin access to the OpenShift cluster; 
7. Helm 3.1 is installed on the installation machine with the help of the [Installing Helm](https://v3.helm.sh/docs/intro/install/) instruction.

## EDP Project
Choose an EDP tenant name, e.g. "demo", and create the <edp-project> project with this name.
Before starting EDP deployment, make sure to have the <edp-project> EDP project created in OpenShift.

## Install EDP
To store EDP data, use any existing Postgres database or create one during the installation. 
In addition, create two secrets in the <edp-project> project: one with administrative credentials and one with credentials for the EDP tenant (database schema). 
* Create secret for administrative access to database:
```
oc -n <edp-project> create secret generic super-admin-db --from-literal=username=<super_admin_db_username> --from-literal=password=<super_admin_db_password>
```

* Create secret for EDP tenant database user:
```
oc -n <edp-project> create secret generic db-admin-console --from-literal=username=<tenant_db_username> --from-literal=password=<tenant_db_password>
```

* For EDP, it is required to have Keycloak access to perform integration. To do this, create manually secret with an administrative access username 
and a password or use the existing secret and the commands as examples:

```bash
oc -n <edp_main_keycloak_project> get secret <edp_main_keycloak_secret> --export -o yaml | oc -n <edp_cicd_project> apply -f -
```

* To add the Helm EPAMEDP Charts for local client, run "helm repo add":
     ```bash
     helm repo add epamedp https://chartmuseum.demo.edp-epam.com/
     ```
* Choose available Helm chart version:
     ```bash
     helm search repo epamedp/edp-install
     NAME                    CHART VERSION   APP VERSION     DESCRIPTION
     epamedp/edp-install     2.4.0           1.16.0          A Helm chart for Kubernetes
     ```

     _**NOTE:** It is highly recommended to use the latest released version._

* EDP installation chart disposes of the following parameters: 
 ```   
    General parameters:
    - global.version                                                    # EDP version;
    - global.edpName                                                    # Name of your EDP project <edp-project> that was previously defined;
    - global.platform                                                   # openshift or kubernetes;
    - global.dnsWildCard                                                # DNS wildcard for routing in your K8S cluster;
    - global.admins                                                     # Administrators of your tenant separated by comma (,) (eg --set 'global.admins={test@example.com}');
    - global.developers                                                 # Developers of your tenant separated by comma (,) (eg --set 'global.developers={test@example.com}');
    - global.database.deploy                                            # Deploy DB to current project or use from another;
    - global.database.image                                             # DB image, e.g. postgres:9.6;
    - global.database.host                                              # Host to DB (<db-name>.<namespace>);
    - global.database.name                                              # Name of DB;
    - global.database.port                                              # Port of DB;
    - global.database.storage.class                                     # Type of storage class;
    - global.database.storage.size                                      # Size of storage;
    - global.webConsole.enabled                                         # Set to true if you want to have Openshift dashboard link in Admin Console;
    - global.webConsole.url                                             # Openshift dashboard URL;
    - edp.adminGroups                                                   # Admin groups of your tenant separated by comma (,) (eg --set 'edp.adminGroups={test-admin-group}');
    - edp.developerGroups                                               # Developer groups of your tenant separated by comma (,) (eg --set 'edp.developerGroups={test-admin-group}');
    - dockerRegistry.url                                                # URL to docker registry;
    - dockerRegistry.enabled                                            # Enable Docker registry link in Adminconsole       
        
    Jenkins parameters:
    - jenkins-operator.image.name                                       # EDP image. The released image can be found on [Dockerhub](https://hub.docker.com/r/epamedp/jenkins-operator);
    - jenkins-operator.image.version                                    # EDP tag. The released image can be found on [Dockerhub](https://hub.docker.com/r/epamedp/jenkins-operator/tags);
    - jenkins-operator.jenkins.deploy                                   # Flag to enable/disable Jenkins deploy (eg true/false);
    - jenkins-operator.jenkins.image                                    # EDP image. The released image can be found on [Dockerhub](https://hub.docker.com/r/epamedp/edp-jenkins);
    - jenkins-operator.jenkins.version                                  # EDP tag. The released version can be found on [Dockerhub](https://hub.docker.com/r/epamedp/edp-jenkins/tags);
    - jenkins-operator.jenkins.initImage                                # Init Docker image for Jenkins deployment;
    - jenkins-operator.jenkins.imagePullSecrets                         # Secrets to pull from private Docker registry;
    - jenkins-operator.jenkins.basePath                                 # Base path for Jenkins URL;
    - jenkins-operator.jenkins.storage.class                            # Type of storage class. By default: gp2;
    - jenkins-operator.jenkins.storage.size                             # Size of persistent volume for Jenkins data, it is recommended to use not less then 10 GB. By default: 10Gi;
    - jenkins-operator.jenkins.libraryPipelinesRepo                     # URL to library pipelines repository. By default: https://github.com/epmd-edp/edp-library-pipelines.git;
    - jenkins-operator.jenkins.libraryPipelinesVersion                  # Version of EDP-Pipeline library for Jenkins. The released version can be found on [Github](https://github.com/epmd-edp/edp-library-pipelines/releases);
    - jenkins-operator.jenkins.libraryStagesRepo                        # URL to library stages repository. By default: https://github.com/epmd-edp/edp-library-stages.git;
    - jenkins-operator.jenkins.libraryStagesVersion                     # Version of EDP-Stages library for Jenkins. The released version can be found on [Github](https://github.com/epmd-edp/edp-library-stages/releases);
    
    CD pipeline parameters:
    - cd-pipeline-operator.image.name                                   # EDP image. The released image can be found on [Dockerhub](https://hub.docker.com/r/epamedp/cd-pipeline-operator);
    - cd-pipeline-operator.image.version                                # EDP tag. The released image can be found on [Dockerhub](https://hub.docker.com/r/epamedp/cd-pipeline-operator/tags);
        
    Keycloak parameters:
    - keycloak-operator.image.name                                      # EDP image. The released image can be found on [Dockerhub](https://hub.docker.com/r/epamedp/keycloak-operator);
    - keycloak-operator.image.version                                   # EDP tag. The released image can be found on [Dockerhub](https://hub.docker.com/r/epamedp/keycloak-operator/tags);
    - keycloak-operator.keycloak.url                                    # URL to Keycloak;
    
    Codebase parameters:
    - codebase-operator.image.name                                      # EDP image. The released image can be found on [Dockerhub](https://hub.docker.com/r/epamedp/codebase-operator);
    - codebase-operator.image.version                                   # EDP tag. The released image can be found on [Dockerhub](https://hub.docker.com/r/epamedp/codebase-operator/tags);
    - codebase-operator.jira.integration                                # Flag to enable/disable Jira integration (eg true/false);
    - codebase-operator.jira.name                                       # JiraServer CR name;
    - codebase-operator.jira.apiUrl                                     # API URL for development;
    - codebase-operator.jira.rootUrl                                    # URL to Jira server;
    - codebase-operator.jira.credentialName                             # Name of secret with credentials to Jira server;
    
    Nexus parameters:
    - nexus-operator.image.name                                         # EDP image. The released image can be found on [Dockerhub](https://hub.docker.com/r/epamedp/nexus-operator);
    - nexus-operator.image.version                                      # EDP tag. The released image can be found on [Dockerhub](https://hub.docker.com/r/epamedp/nexus-operator/tags);
    - nexus-operator.nexus.deploy                                       # Flag to enable/disable Nexus deploy (eg true/false);
    - nexus-operator.nexus.name                                         # Nexus name;
    - nexus-operator.nexus.image                                        # Image for Nexus. The image can be found on [Dockerhub] (https://hub.docker.com/r/sonatype/nexus3);
    - nexus-operator.nexus.version                                      # Nexus version. The released version can be found on [Dockerhub](https://hub.docker.com/r/sonatype/nexus3/tags)'
    - nexus-operator.nexus.basePath                                     # Base path for Nexus URL;
    - nexus-operator.nexus.imagePullSecrets                             # Secrets to pull from private Docker registry;
    - nexus-operator.nexus.storage.class                                # Storageclass for Nexus data volume. Default is "gp2";
    - nexus-operator.nexus.storage.size                                 # Nexus data volume capacity. Default is "10Gi";
    
    Sonar parameters:
    - sonar-operator.image.name                                         # EDP image. The released image can be found on [Dockerhub](https://hub.docker.com/r/epamedp/sonar-operator);
    - sonar-operator.image.version                                      # EDP tag. The released image can be found on [Dockerhub](https://hub.docker.com/r/epamedp/sonar-operator/tags);
    - sonar-operator.sonar.deploy                                       # Flag to enable/disable Sonar deploy (eg true/false);
    - sonar-operator.sonar.name                                         # Sonar name;
    - sonar-operator.sonar.image                                        # Sonarqube Docker image name. Default supported is "sonarqube";
    - sonar-operator.sonar.version                                      # Sonarqube Docker image tag. Default supported is "7.9-community";
    - sonar-operator.sonar.initImage                                    # Init Docker image for Sonarqube deployment. Default is "busybox";
    - sonar-operator.sonar.dbImage                                      # Docker image name for Sonarqube Database. Default in "postgres:9.6";
    - sonar-operator.sonar.storage.data.class                           # Storageclass for Sonarqube data volume. Default is "gp2";
    - sonar-operator.sonar.storage.data.size                            # Sonarqube data volume size. Default is "1Gi";
    - sonar-operator.sonar.storage.database.class                       # Storageclass for Sonarqube database volume. Default is "gp2";
    - sonar-operator.sonar.storage.database.size                        # Sonarqube database volume size. Default is "1Gi".
    - sonar-operator.sonar.imagePullSecrets                             # Secrets to pull from private Docker registry;
    - sonar-operator.sonar.basePath                                     # Base path for Sonar URL;

    
    Admin Console operator parameters:
    - admin-console-operator.image.name                                 # EDP image. The released image can be found on [Dockerhub](https://hub.docker.com/r/epamedp/admin-console-operator);
    - admin-console-operator.image.version                              # EDP tag. The released image can be found on [Dockerhub](https://hub.docker.com/r/epamedp/admin-console-operator/tags);
    - admin-console-operator.adminConsole.image                         # EDP image. The released image can be found on [Dockerhub](https://hub.docker.com/r/epamedp/edp-admin-console);
    - admin-console-operator.adminConsole.version                       # EDP tag. The released image can be found on [Dockerhub](https://hub.docker.com/r/epamedp/edp-admin-console/tags);
    - admin-console-operator.adminConsole.imagePullSecrets              # Secrets to pull from private Docker registry;
    - admin-console-operator.adminConsole.basePath                      # Base path for Admin Console URL;
    - admin-console-operator.adminConsole.testReportTools               # # List of automation tests frameworks. Default in "Allure";
    
    Gerrit parameters:
    - gerrit-operator.image.name                                        # EDP image. The released image can be found on [Dockerhub](https://hub.docker.com/r/epamedp/gerrit-operator);
    - gerrit-operator.image.version                                     # EDP tag. The released image can be found on [Dockerhub](https://hub.docker.com/r/epamedp/gerrit-operator/tags);
    - gerrit-operator.gerrit.deploy                                     # Flag to true/false Gerrit deploy;
    - gerrit-operator.gerrit.name                                       # Gerrit name;
    - gerrit-operator.gerrit.image                                      # Gerrit image, e.g. openfrontier/gerrit;
    - gerrit-operator.gerrit.imagePullSecrets                           # Secrets to pull from private Docker registry;
    - gerrit-operator.gerrit.version                                    # Gerrit version, e.g. 3.1.4;
    - gerrit-operator.gerrit.sshPort                                    # SSH port;
    - gerrit-operator.gitServer.name                                    # GitServer CR name;
    - gerrit-operator.gitServer.user                                    # Git user to connect;
    - gerrit-operator.gitServer.httpsPort                               # HTTPS port;
    - gerrit-operator.gitServer.nameSshKeySecret                        # Name of secret with credentials to Git server;
    - gerrit-operator.gitServer.sshPort                                 # SSH port;
    - gerrit-operator.gerrit.storage.class                              # Storageclass for Gerrit data volume. Default is "gp2";
    - gerrit-operator.gerrit.storage.size                               # Gerrit data volume size. Default is "1Gi";
    
    Reconciler parameters:
    - reconciler.image.name                                             # EDP image. The released image can be found on [Dockerhub](https://hub.docker.com/r/epamedp/reconciler);
    - reconciler.image.version                                          # EDP tag. The released image can be found on [Dockerhub](https://hub.docker.com/r/epamedp/reconciler/tags);
 ```  

* If the external database is used, set the global.database.host value to the database DNS name accessible from the <edp-project> project;

* Install EDP in the <edp-project> project with the helm command; find below the installation command example:
```bash
helm install epamedp/edp-install --wait --timeout=900s --namespace <edp-project> --set global.edpName=<edp-project> --set global.dnsWildCard=<k8s_cluster_DNS_wilcdard> --set global.platform=openshift
```

* As soon as Helm deploys components, create secrets for JIRA/GIT integration (if enabled) manually. Pay attention that 
secret names must be the same as 'credentialName' property for JIRA and 'nameSshKeySecret' for GIT.
      
>_**NOTE**: The full installation with integration between tools will take at least 10 minutes._

* After the installation, if EDP is installed without Gerrit, it is possible to configure the [GitHub](https://github.com/epmd-edp/jenkins-operator/blob/release/2.4/documentation/github-integration.md) or [GitLab](https://github.com/epmd-edp/jenkins-operator/blob/release/2.4/documentation/gitlab-integration.md) integration to work with EDP.