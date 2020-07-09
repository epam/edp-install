# EDP Installation on OpenShift

Inspect the prerequisites and the main steps to perform with the aim to install EPAM Delivery Platform on OpenShift.

## Prerequisites
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
7. The installation machine with [oc](https://docs.okd.io/latest/cli_reference/get_started_cli.html#installing-the-cli) is installed with the cluster-admin access to the OpenShift cluster; 
8. Helm 3.1 is installed on the installation machine with the help of the [Installing Helm](https://v3.helm.sh/docs/intro/install/) instruction.

## Install EDP
* Choose an EDP tenant name, e.g. "demo", and create the <edp-project> project with any name (e.g. "demo").
Before starting EDP deployment, EDP project <edp-project> in OpenShift should be created.

* Create secret for EDP admin database user:
```
oc -n <edp-project> create secret generic super-admin-db --from-literal=username=<super_admin_db_username> --from-literal=password=<super_admin_db_password>
```

* Create secret for EDP tenant database user:
```
oc -n <edp-project> create secret generic db-admin-console --from-literal=username=<tenant_db_username> --from-literal=password=<tenant_db_password>
```

* Apply EDP chart using Helm. 

Find below the description of optional and mandatory parameters types.

Optional parameters:
 ```
    - jenkins-operator.jenkins.initImage                                # Init Docker image for Jenkins deployment;
    - jenkins-operator.jenkins.pullSecrets                              # Secrets to pull from private Docker registry;
    - jenkins-operator.jenkins.basePath                                 # Base path for Jenkins URL;
    - jenkins-operator.jenkins.storageClass                             # Type of storage class. By default: gp2;
    - jenkins-operator.jenkins.volumeCapacity                           # Size of persistent volume for Jenkins data, it is recommended to use not less then 10 GB. By default: 10Gi;
    - jenkins-operator.jenkins.libraryPipelinesRepo                     # URL to library pipelines repository. By default: https://github.com/epmd-edp/edp-library-pipelines.git;
    - jenkins-operator.jenkins.libraryPipelinesVersion                  # Version of EDP-Pipeline library for Jenkins. The released version can be found on [Github](https://github.com/epmd-edp/edp-library-pipelines/releases);
    - jenkins-operator.jenkins.libraryStagesRepo                        # URL to library stages repository. By default: https://github.com/epmd-edp/edp-library-stages.git;
    - jenkins-operator.jenkins.libraryStagesVersion                     # Version of EDP-Stages library for Jenkins. The released version can be found on [Github](https://github.com/epmd-edp/edp-library-stages/releases);
 ```

Mandatory parameters: 
 ```   
    General parameters:
    - global.version                                                    # EDP version;
    - global.edpName                                                    # Name of your EDP project <edp-project> that was previously defined;
    - global.platform                                                   # openshift or kubernetes;
    - global.dnsWildCard                                                # DNS wildcard for routing in your K8S cluster;
    - global.admins                                                     # Administrators of your tenant separated by comma (,) (eg --set 'global.admins={test@example.com}');
    - global.developers                                                 # Developers of your tenant separated by comma (,) (eg --set 'global.developers={test@example.com}');
    - global.database.image                                             # DB image, e.g. postgres:9.6;
    - global.database.host                                              # Host to DB (<db-name>.<namespace>);
    - global.database.name                                              # Name of DB;
    - global.database.port                                              # Port of DB;
    - global.database.storage.class                                     # Type of storage class;
    - global.database.storage.size                                      # Size of storage;
    - edp.webConsole                                                    # URL to Openshift Web console;
    - edp.adminGroups                                                   # Admin groups of your tenant separated by comma (,) (eg --set 'edp.adminGroups={test-admin-group}');
    - edp.developerGroups                                               # Developer groups of your tenant separated by comma (,) (eg --set 'edp.developerGroups={test-admin-group}');
        
    Jenkins parameters:
    - jenkins-operator.image.name                                       # EDP image. The released image can be found on [Dockerhub](https://hub.docker.com/repository/docker/epamedp/jenkins-operator);
    - jenkins-operator.image.version                                    # EDP tag. The released image can be found on [Dockerhub](https://hub.docker.com/repository/docker/epamedp/jenkins-operator/tags);
    - jenkins-operator.jenkins.deploy                                   # Flag to enable/disable Jenkins deploy;
    - jenkins-operator.jenkins.image                                    # EDP image. The released image can be found on [Dockerhub](https://hub.docker.com/r/epamedp/edp-jenkins);
    - jenkins-operator.jenkins.version                                  # EDP tag. The released version can be found on [Dockerhub](https://hub.docker.com/r/epamedp/edp-jenkins/tags);
    
    CD pipeline parameters:
    - cd-pipeline-operator.image.name                                   # EDP image. The released image can be found on [Dockerhub](https://hub.docker.com/repository/docker/epamedp/cd-pipeline-operator);
    - cd-pipeline-operator.image.version                                # EDP tag. The released image can be found on [Dockerhub](https://hub.docker.com/repository/docker/epamedp/cd-pipeline-operator/tags);
        
    Keycloak parameters:
    - keycloak-operator.image.name                                      # EDP image. The released image can be found on [Dockerhub](https://hub.docker.com/repository/docker/epamedp/keycloak-operator);
    - keycloak-operator.image.version                                   # EDP tag. The released image can be found on [Dockerhub](https://hub.docker.com/repository/docker/epamedp/keycloak-operator/tags);
    - keycloak-operator.keycloak.url                                    # URL to Keycloak;
    
    Codebase parameters:
    - codebase-operator.image.name                                      # EDP image. The released image can be found on [Dockerhub](https://hub.docker.com/repository/docker/epamedp/codebase-operator);
    - codebase-operator.image.version                                   # EDP tag. The released image can be found on [Dockerhub](https://hub.docker.com/repository/docker/epamedp/codebase-operator/tags);
    - codebase-operator.jira.integration                                # Flag to enable/disable Jira integration;
    - codebase-operator.jira.name                                       # JiraServer CR name;
    - codebase-operator.jira.apiUrl                                     # API URL for development;
    - codebase-operator.jira.rootUrl                                    # URL to Jira server;
    - codebase-operator.jira.credentialName                             # Name of secret with credentials to Jira server;
    
    Nexus parameters:
    - nexus-operator.image.name                                         # EDP image. The released image can be found on [Dockerhub](https://hub.docker.com/repository/docker/epamedp/nexus-operator);
    - nexus-operator.image.version                                      # EDP tag. The released image can be found on [Dockerhub](https://hub.docker.com/repository/docker/epamedp/nexus-operator/tags);
    - nexus-operator.nexus.deploy                                       # Flag to enable/disable Nexus deploy;
    - nexus-operator.nexus.name                                         # Nexus name;
    - nexus-operator.nexus.image                                        # Image for Nexus. The image can be found on [Dockerhub] (https://hub.docker.com/r/sonatype/nexus3);
    - nexus-operator.nexus.version                                      # Nexus version. The released version can be found on [Dockerhub](https://hub.docker.com/r/sonatype/nexus3/tags)'
    
    Sonar parameters:
    - sonar-operator.image.name                                         # EDP image. The released image can be found on [Dockerhub](https://hub.docker.com/repository/docker/epamedp/sonar-operator);
    - sonar-operator.image.version                                      # EDP tag. The released image can be found on [Dockerhub](https://hub.docker.com/repository/docker/epamedp/sonar-operator/tags);
    - sonar-operator.sonar.deploy                                       # Flag to enable/disable Sonar deploy;
    - sonar-operator.sonar.name                                         # Flag to enable/disable Sonar deploy;
    - sonar-operator.sonar.image                                        # Sonarqube Docker image name. Default supported is "sonarqube";
    - sonar-operator.sonar.version                                      # Sonarqube Docker image tag. Default supported is "7.9-community";
    - sonar-operator.sonar.initImage                                    # Init Docker image for Sonarqube deployment. Default is "busybox";
    - sonar-operator.sonar.dbImage                                      # Docker image name for Sonarqube Database. Default in "postgres:9.6";
    - sonar-operator.sonar.dataVolumeStorageClass                       # Storageclass for Sonarqube data volume. Default is "gp2";
    - sonar-operator.sonar.dataVolumeCapacity                           # Sonarqube data volume capacity. Default is "1Gi";
    - sonar-operator.sonar.dbVolumeStorageClass                         # Storageclass for Sonarqube database volume. Default is "gp2";
    - sonar-operator.sonar.dbVolumeCapacity                             # Sonarqube database volume capacity. Default is "1Gi".
    
    Admin Console operator parameters:
    - admin-console-operator.image.name                                 # EDP image. The released image can be found on [Dockerhub](https://hub.docker.com/repository/docker/epamedp/admin-console-operator);
    - admin-console-operator.image.version                              # EDP tag. The released image can be found on [Dockerhub](https://hub.docker.com/repository/docker/epamedp/admin-console-operator/tags);
    - admin-console-operator.adminConsole.image                         # EDP image. The released image can be found on [Dockerhub](https://hub.docker.com/repository/docker/epamedp/edp-admin-console);
    - admin-console-operator.adminConsole.version                       # EDP image. The released image can be found on [Dockerhub](https://hub.docker.com/repository/docker/epamedp/edp-admin-console/tags);
    - admin-console-operator.adminConsole.pullSecrets                   # Secrets to pull from private Docker registry;
    
    Gerrit parameters:
    - gerrit-operator.image.name                                        # EDP image. The released image can be found on [Dockerhub](https://hub.docker.com/repository/docker/epamedp/gerrit-operator);
    - gerrit-operator.image.version                                     # EDP tag. The released image can be found on [Dockerhub](https://hub.docker.com/repository/docker/epamedp/gerrit-operator/tags);
    - gerrit-operator.gerrit.deploy                                     # Flag to enable/disable Gerrit deploy;
    - gerrit-operator.gerrit.name                                       # Gerrit name;
    - gerrit-operator.gerrit.image                                      # Gerrit image, e.g. openfrontier/gerrit;
    - gerrit-operator.gerrit.version                                    # Gerrit version, e.g. 3.1.4;
    - gerrit-operator.gerrit.sshPort                                    # SSH port;
    - gerrit-operator.gitServer.name                                    # GitServer CR name;
    - gerrit-operator.gitServer.user                                    # Git user to connect;
    - gerrit-operator.gitServer.httpsPort                               # HTTPS port;
    - gerrit-operator.gitServer.nameSshKeySecret                        # Name of secret with credentials to Git server;
    - gerrit-operator.gitServer.sshPort                                 # SSH port;
    
    Reconciler parameters:
    - reconciler.image.name                                             # EDP image. The released image can be found on [Dockerhub](https://hub.docker.com/repository/docker/epamedp/reconciler);
    - reconciler.image.version                                          # EDP tag. The released image can be found on [Dockerhub](https://hub.docker.com/repository/docker/epamedp/reconciler/tags);
 ```  

For some reasons, you may want to integrate with DB from another namespace. To achieve this:
   * Set global.database.host as <db-name>.<another_namespace>;
   * Create 'super-admin-db' secret with credentials from existing admin credentials to DB;
   * Create 'db-admin-console' secret;

Inspect the sample of launching a Helm template for EDP installation:
```bash
helm install edp-install --wait --timeout=900s --namespace <edp-project> --set global.edpName=<edp-project> deploy-templates
```
As soon as Helm deploys components, create secrets for JIRA/GIT integration (if enabled) manually. Pay attention that 
secret names must be the same as 'credentialName' property for JIRA and 'nameSshKeySecret' for GIT.
 
 * Deploy operators in the <edp-project> project by following the corresponding instructions in their repositories:
     - [keycloak-operator](https://github.com/epmd-edp/keycloak-operator)
     - [codebase-operator](https://github.com/epmd-edp/codebase-operator)
     - [reconciler](https://github.com/epmd-edp/reconciler)
     - [cd-pipeline-operator](https://github.com/epmd-edp/cd-pipeline-operator)
     - [nexus-operator](https://github.com/epmd-edp/nexus-operator)
     - [sonar-operator](https://github.com/epmd-edp/sonar-operator)
     - [admin-console-operator](https://github.com/epmd-edp/admin-console-operator)
     - [gerrit-operator](https://github.com/epmd-edp/gerrit-operator)
     - [jenkins-operator](https://github.com/epmd-edp/jenkins-operator)
     
>_**NOTE**: The full installation with integration between tools will take at least 10 minutes._