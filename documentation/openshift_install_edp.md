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
    - jenkins.sharedLibraryRepo.pipelines           # URL to library pipelines repository. By default: https://github.com/epmd-edp/edp-library-pipelines.git;
    - jenkins.sharedLibraryRepo.stages              # URL to library stages repository. By default: https://github.com/epmd-edp/edp-library-stages.git;
    - jenkins.storageClass                          # Type of storage class. By default: gp2; 
    - jenkins.volumeCapacity                        # Size of persistent volume for Jenkins data, it is recommended to use not less then 10 GB. By default: 10Gi;
 ```

Mandatory parameters: 
 ```   
    General parameters:
    - edp.name                                      # name of your EDP project <edp-project> that was previously defined;
    - edp.platform                                  # OpenShift or Kubernetes;
    - edp.version                                   # EDP image and tag. The released version can be found on [Dockerhub](https://hub.docker.com/r/epamedp/edp-install/tags);
    - edp.dnsWildCard                               # DNS wildcard for routing in your K8S cluster;
    - edp.admins                                    # Administrators of your tenant separated by comma (,) (eg --set 'edp.admins={test@mail.com}');
    - edp.developers                                # Developers of your tenant separated by comma (,) (eg --set 'edp.developers={test@mail.com}');
    - edp.adminGroups                               # Admin groups of your tenant separated by comma (,) (eg --set 'edp.adminGroups={test-admin-group}');
    - edp.developerGroups                           # Developer groups of your tenant separated by comma (,) (eg --set 'edp.developerGroups={test-admin-group}');
    - edp.webConsole                                # URL to Openshift Web console;
    
    Database parameters:
    - edp.db.image                                  # DB image, e.g. postgres:9.6;
    - edp.db.port                                   # Port of DB;
    - edp.db.host                                   # Host to DB (<db-name>.<namespace>);
    - edp.db.storage.class                          # Type of storage class;
    - edp.db.storage.size                           # Size of storage;
    
    Jenkins parameters:
    - jenkins.image                                 # EDP image. The released image can be found on [Dockerhub](https://hub.docker.com/r/epamedp/edp-jenkins);
    - jenkins.version                               # EDP tag. The released version can be found on [Dockerhub](https://hub.docker.com/r/epamedp/edp-jenkins/tags);
    - jenkins.sharedLibraryVersion.pipelines        # Version of EDP-Pipeline library for Jenkins. The released version can be found on [GitHub](https://github.com/epmd-edp/edp-library-pipelines/releases);
    - jenkins.sharedLibraryVersion.stages           # Version of EDP-Stages library for Jenkins. The released version can be found on [GitHub](https://github.com/epmd-edp/edp-library-stages/releases);
    
    Admin Console parameters:
    - adminConsole.image                            # EDP image. The image can be found on [Dockerhub](https://hub.docker.com/r/epamedp/edp-admin-console);
    - adminConsole.version                          # EDP tag. The released version can be found on [Dockerhub](https://hub.docker.com/r/epamedp/edp-admin-console/tags);
    
    Keycloak parameters:
    - keycloak.url                                  # URL to Keycloak;
    - keycloak.namespace                            # Namespace with deployed Keycloak;
    - keycloak.secretToCopy                         # Secret name for Keycloak to be copied to your namespace;
    
    GitServer parameters:
    - gitServer.name                                # GitServer CR name;
    - gitServer.user                                # Git user to connect;
    - gitServer.httpsPort                           # HTTPS port;
    - gitServer.sshPort                             # SSH port;
    
    Jira parameters:
    - jira.integration                              # Flag to enable/disable Jira integration;
    - jira.name                                     # JiraServer CR name;
    - jira.apiUrl                                   # API URL for development;
    - jira.rootUrl                                  # URL to Jira server;
    - jira.credentialName                           # Name of secret with credentials to Jira server;
    
    Gerrit parameters:
    - gerrit.deploy                                 # Flag to enable/disable Gerrit deploy;
    - gerrit.image                                  # Gerrit image, e.g. openfrontier/gerrit;
    - gerrit.version                                # Gerrit version, e.g. 3.1.4;
    - gerrit.sshPort                                # SSH port;
    
    Nexus parameters:
    - nexus.deploy                                  # Flag to enable/disable Nexus deploy;
    - nexus.image                                   # Image for Nexus. The image can be found on [Dockerhub] (https://hub.docker.com/r/sonatype/nexus3);
    - nexus.version                                 # Nexus version. The released version can be found on [Dockerhub](https://hub.docker.com/r/sonatype/nexus3/tags)'
    
    Sonar parameters:
    - sonar.deploy                                  # Flag to enable/disable Sonar deploy;
    - sonar.image                                   # Image for Sonar. The image can be found on [Dockerhub] (https://hub.docker.com/_/sonarqube);
    - sonar.version                                 # Sonar version. The released version can be found on [Dockerhub](https://hub.docker.com/_/sonarqube/?tab=tags);
 ```  

For some reasons, you may want to integrate with DB from another namespace. To achieve this:
   * Set edp.db.host as <db-name>.<another_namespace>;
   * Create 'super-admin-db' secret with credentials from existing admin credentials to DB;
   * Create 'db-admin-console' secret;

Inspect the sample of launching a Helm template for EDP installation:
```bash
helm install edp-install --wait --timeout=900s --namespace <edp-project> --set edp.name=<edp-project> deploy-templates
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