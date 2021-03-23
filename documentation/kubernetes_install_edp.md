# EDP Installation on Kubernetes

Inspect the prerequisites and the main steps to perform with the aim to install EPAM Delivery Platform on Kubernetes.

## Prerequisites
1. Kubernetes cluster installed with minimum 2 worker nodes with total capacity 16 Cores and 40Gb RAM;
2. Machine with [kubectl](https://kubernetes.io/docs/tasks/tools/install-kubectl/) installed with a cluster-admin access to the Kubernetes cluster;
3. Ingress controller is installed in a cluster, for example [ingress-nginx](https://kubernetes.github.io/ingress-nginx/deploy/);
4. Ingress controller is configured with the disabled HTTP/2 protocol and header size of 64k support;

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

5. Load balancer (if any exists in front of ingress controller) is configured with session stickiness, disabled HTTP/2 protocol and header size of 32k support;
6. Cluster nodes and pods should have access to the cluster via external URLs. For instance, you should add in AWS your VPC NAT gateway elastic IP to your cluster external load balancers security group);
7. Keycloak instance is installed. To get accurate information on how to install Keycloak, please refer to the [Keycloak Installation on Kubernetes](kubernetes_install_keycloak.md)) instruction;
8. The "openshift" realm is created in Keycloak;
9. Helm 3.1 or higher is installed on the installation machine with the help of the [Installing Helm](https://v3.helm.sh/docs/intro/install/) instruction.
10. It is highly recommended to use a storage class with the [Retain Reclaim Policy](https://kubernetes.io/docs/concepts/storage/persistent-volumes/#retain):
    - Storage class template with the Retain Reclaim Policy:
    ```yaml
    kind: StorageClass
    apiVersion: storage.k8s.io/v1
    metadata:
      name: gp2-retain
    provisioner: kubernetes.io/aws-ebs
    parameters:
      fsType: ext4
      type: gp2
    reclaimPolicy: Retain
    volumeBindingMode: WaitForFirstConsumer
    ```

## EDP Namespace
Choose an EDP tenant name, e.g. "demo", and create the <edp-project> namespace with this name.
Before starting the EDP deployment, make sure to have the <edp-project> EDP namespace created in K8S.

## Install EDP
To store EDP data, use any existing Postgres database or create one during the installation.
In addition, create two secrets in the <edp-project> namespace: one with administrative credentials and one with credentials for the EDP tenant (database schema).
* Create secret for administrative access to database:
```
kubectl -n <edp-project> create secret generic super-admin-db --from-literal=username=<super_admin_db_username> --from-literal=password=<super_admin_db_password>
```

* Create secret for EDP tenant database user. If you want to use the same username as for the administrative access, the passwords must be the same as well:
```
kubectl -n <edp-project> create secret generic db-admin-console --from-literal=username=<tenant_db_username> --from-literal=password=<tenant_db_password>
```

* Create secret for Sonar database:
```
kubectl -n <global.edpName> create secret generic sonar-db --from-literal=database-user=admin --from-literal=database-password=<password>
```

* For EDP, it is required to have Keycloak access to perform the integration. To do this, create manually secret with an administrative access username
and a password or use the existing secret and the commands as examples:
```bash
kubectl -n <edp_main_keycloak_project> get secret <edp_main_keycloak_secret> --export -o yaml | kubectl -n <edp_cicd_project> apply -f -
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
    - global.edpName                                                    # Name of your <edp-project> EDP namespace that was previously defined;
    - global.platform                                                   # openshift or kubernetes;
    - global.dnsWildCard                                                # DNS wildcard for routing in your K8S cluster;
    - global.admins                                                     # Administrators of your tenant separated by comma (,) (eg --set 'global.admins={test@example.com}');
    - global.developers                                                 # Developers of your tenant separated by comma (,) (eg --set 'global.developers={test@example.com}');
    - global.database.deploy                                            # Deploy DB to current namespace or use from another. Set to true if you want to install new DB with this chart;
    - global.database.image                                             # DB image, e.g. postgres:9.6;
    - global.database.host                                              # Host to DB (<db-name>.<namespace>);
    - global.database.name                                              # Name of DB;
    - global.database.port                                              # Port of DB;
    - global.database.storage.class                                     # Type of storage class for DB volume. By default: "gp2", but it is highly recommended to use "gp2-retain". For details, please refer to point 10 of the Prerequisites section;
    - global.database.storage.size                                      # Size of storage for DB volume;
    - global.webConsole.url                                             # Kubernetes cluster URL;
    - edp.adminGroups                                                   # Admin groups of your tenant separated by comma (,) (eg --set 'edp.adminGroups={test-admin-group}');
    - edp.developerGroups                                               # Developer groups of your tenant separated by comma (,) (eg --set 'edp.developerGroups={test-admin-group}');
    - dockerRegistry.url                                                # URL to docker registry;

    Jenkins parameters:
    - jenkins-operator.image.name                                       # EDP image. The released image can be found on [Dockerhub](https://hub.docker.com/r/epamedp/jenkins-operator);
    - jenkins-operator.image.version                                    # EDP tag. The released image can be found on [Dockerhub](https://hub.docker.com/r/epamedp/jenkins-operator/tags);
    - jenkins-operator.jenkins.deploy                                   # Flag to enable/disable Jenkins deploy (eg true/false);
    - jenkins-operator.jenkins.image                                    # EDP image. The released image can be found on [Dockerhub](https://hub.docker.com/r/epamedp/edp-jenkins);
    - jenkins-operator.jenkins.version                                  # EDP tag. The released version can be found on [Dockerhub](https://hub.docker.com/r/epamedp/edp-jenkins/tags);
    - jenkins-operator.jenkins.initImage                                # Init Docker image for Jenkins deployment;
    - jenkins-operator.jenkins.pullSecrets                              # Secrets to pull from private Docker registry;
    - jenkins-operator.jenkins.basePath                                 # Base path for Jenkins URL;
    - jenkins-operator.jenkins.storage.class                            # Type of storage class. By default: "gp2", but it is highly recommended to use "gp2-retain". For details, please refer to point 10 of the Prerequisites section;
    - jenkins-operator.jenkins.storage.size                             # Size of persistent volume for Jenkins data, it is recommended to use not less then 10 GB. By default: 10Gi;
    - jenkins-operator.jenkins.sharedLibraries[i].name                  # EDP shared-library name;
    - jenkins-operator.jenkins.sharedLibraries[i].url                   # EDP shared-library repository link;
    - jenkins-operator.jenkins.sharedLibraries[i].tag                   # EDP shared-library repository version;
    - jenkins-operator.jenkins.sharedLibraries[i].secret                # Name of Kubernetes secret which contains credentials to private repository. Use only if repo is private.;
    - jenkins-operator.jenkins.sharedLibraries[i].type                  # Type of connection to repository (eg ssh, password and token);

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
    - nexus-operator.nexus.storage.class                                # Storageclass for Nexus data volume. Default is "gp2", but it is highly recommended to use "gp2-retain". For details, please refer to point 10 of the Prerequisites section;
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
    - sonar-operator.sonar.storage.data.class                           # Storageclass for Sonarqube data volume. Default is "gp2", but it is highly recommended to use "gp2-retain". For details, please refer to point 10 of the Prerequisites section;
    - sonar-operator.sonar.storage.data.size                            # Sonarqube data volume size. Default is "1Gi";
    - sonar-operator.sonar.storage.database.class                       # Storageclass for Sonarqube database volume. Default is "gp2", but it is highly recommended to use "gp2-retain". For details, please refer to point 10 of the Prerequisites section;
    - sonar-operator.sonar.storage.database.size                        # Sonarqube database volume size. Default is "1Gi"
    - sonar-operator.sonar.imagePullSecrets                             # Secrets to pull from private Docker registry;
    - sonar-operator.sonar.basePath                                     # Base path for Sonar URL;

    Admin Console operator parameters:
    - admin-console-operator.image.name                                 # EDP image. The released image can be found on [Dockerhub](https://hub.docker.com/r/epamedp/admin-console-operator);
    - admin-console-operator.image.version                              # EDP tag. The released image can be found on [Dockerhub](https://hub.docker.com/r/epamedp/admin-console-operator/tags);
    - admin-console-operator.adminConsole.image                         # EDP image. The released image can be found on [Dockerhub](https://hub.docker.com/r/epamedp/edp-admin-console);
    - admin-console-operator.adminConsole.version                       # EDP image. The released image can be found on [Dockerhub](https://hub.docker.com/r/epamedp/edp-admin-console/tags);
    - admin-console-operator.adminConsole.pullSecrets                   # Secrets to pull from private Docker registry;
    - admin-console-operator.adminConsole.imagePullSecrets              # Secrets to pull from private Docker registry;
    - admin-console-operator.adminConsole.basePath                      # Base path for Admin Console URL;
    - admin-console-operator.adminConsole.projectUrlMask                # URL mask that leads to namespace in Kubernetes (eg --set 'adminConsole.projectUrlMask=/#/overview?namespace={namespace}');
    - admin-console-operator.adminConsole.imageStreamUrlMask            # URL mask that leads to image stream in Kubernetes (eg --set 'adminConsole.imageStreamUrlMask=/{stream}/');
    - admin-console-operator.adminConsole.authKeycloakEnabled           # Enabled or disabled integration with Keycloak;
    - admin-console-operator.adminConsole.buildTools                    # List of build tools wich admin console supports (eg --set 'adminConsole.buildTools=maven,helm');

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
    - gerrit-operator.gitServer.nameSshKeySecret                        # Name of the secret with credentials to Git server;
    - gerrit-operator.gerrit.storage.class                              # Storageclass for Gerrit data volume. Default is "gp2", but it is highly recommended to use "gp2-retain". For details, please refer to point 10 of the Prerequisites section;
    - gerrit-operator.gerrit.storage.size                               # Gerrit data volume size. Default is "1Gi";

    Reconciler parameters:
    - reconciler.image.name                                             # EDP image. The released image can be found on [Dockerhub](https://hub.docker.com/r/epamedp/reconciler);
    - reconciler.image.version                                          # EDP tag. The released image can be found on [Dockerhub](https://hub.docker.com/r/epamedp/reconciler/tags);

    PERF operator parameters:
    - perf-operator.image.name                                          # EDP image. The released image can be found on [Dockerhub](https://hub.docker.com/r/epamedp/perf-operator);
    - perf-operator.image.version                                       # EDP tag. The released image can be found on [Dockerhub](https://hub.docker.com/r/epamedp/perf-operator/tags);
    - perf-operator.perf.integration                                    # Flag to enable/disable PERF integration (e.g. true/false);
    - perf-operator.perf.name                                           # PerfServer CR name;
    - perf-operator.perf.apiUrl                                         # API URL for development;
    - perf-operator.perf.rootUrl                                        # URL to PERF project;
    - perf-operator.perf.credentialName                                 # Name of secret with credentials to PERF server;
    - perf-operator.perf.projectName                                    # Name of project in PERF;
    - perf-operator.perf.luminate.enabled                               # Flag to enable/disable the Luminate integration (e.g. true/false);
    - perf-operator.perf.luminate.apiUrl                                # API URL for development;
    - perf-operator.perf.luminate.credentialName                        # Name of secret with Luminate credentials;
 ```

* If the external database is used, set the global.database.host value to the database DNS name accessible from the <edp-project> namespace;

* Install EDP in the <edp-project> namespace with the helm command; find below the installation command example:
```bash
helm install epamedp/edp-install --wait --timeout=900s --namespace <edp-project> --set global.edpName=<edp-project> --set global.dnsWildCard=<k8s_cluster_DNS_wilcdard> --set global.platform=kubernetes
```

* As soon as Helm deploys components, create manually secrets for JIRA/GIT/PERF integration (if enabled).
Pay attention that secret names should be the same as the 'credentialName' property in JiraServer/PerfServer custom
resources, and the 'nameSshKeySecret' property for GIT.

> **INFO**: If your system requires to use Luminate, pay attention that the secret name must be the same as the **perf-operator.perf.luminate.credentialName** property.


* After the installation, it is necessary to configure the [GitHub](https://github.com/epam/edp-admin-console/blob/release/2.5/documentation/github-integration.md#github-integration) or [GitLab](https://github.com/epam/edp-admin-console/blob/release/2.5/documentation/gitlab-integration.md#gitlab-integration) integration to work with EDP.
>_**NOTE**: The full installation with integration between tools will take at least 10 minutes._
