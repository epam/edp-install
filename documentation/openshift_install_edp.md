# EDP Installation on OpenShift

Inspect the prerequisites and the main steps to install EPAM Delivery Platform on OpenShift.

## OpenShift Cluster Settings 

Make sure the cluster meets the following conditions:
1. OpenShift cluster is installed with minimum 2 worker nodes with total capacity 16 Cores and 40Gb RAM;
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
3. Cluster nodes and pods have access to the cluster via external URLs. For instance, add in AWS the VPC NAT gateway elastic IP to the cluster external load balancers security group);
4. Keycloak instance is installed. To get accurate information on how to install Keycloak, please refer to the [Keycloak Installation on OpenShift](install_keycloak.md) instruction;
5. The installation machine with [oc](https://docs.openshift.com/container-platform/4.2/cli_reference/openshift_cli/getting-started-cli.html#cli-installing-cli_cli-developer-commands) is installed with the cluster-admin access to the OpenShift cluster;
6. Helm 3.1 is installed on the installation machine with the help of the [Installing Helm](https://v3.helm.sh/docs/intro/install/) instruction.
7. A storage class is used with the [Retain Reclaim Policy](https://kubernetes.io/docs/concepts/storage/persistent-volumes/#retain):
    - Storage class template with Retain Reclaim Policy:
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

## Prerequisites for EDP Installation
* Kiosk is deployed in the cluster. For details, please refer to the [Install kiosk](https://github.com/loft-sh/kiosk#1-install-kiosk) paragraph.
* A service account is added to the configuration namespace (e.g. 'prerequisite-namespace' namespace).
```
kubectl -n <configuration_namespace> create sa <organization_name>
```

* The Account template is applied to the cluster. Please check the sample below:
```yaml
apiVersion: tenancy.kiosk.sh/v1alpha1
kind: Account
metadata:
  name: <organization_name>
spec:
  space: 
    clusterRole: kiosk-space-admin
  subjects:
  - kind: ServiceAccount
    name: <organization_name>
    namespace: <configuration_namespace>
```

* The ClusterRoleBinding is applied to the 'kiosk-edit' cluster role (current role is added during installation of Kiosk). Please check the sample below:
```yaml
apiVersion: rbac.authorization.k8s.io/v1
kind: ClusterRoleBinding
metadata:
  name: <organization_name>-kiosk-edit
subjects:
- kind: ServiceAccount
  name: <organization_name>
  namespace: <configuration_namespace>
roleRef:
  kind: ClusterRole
  name: kiosk-edit
  apiGroup: rbac.authorization.k8s.io
```

## EDP Project
Choose an EDP tenant name, e.g. "demo", and create the eponymous <edp-project> space custom resource. As a result, EDP namespace will appear. 
Before starting the EDP deployment, make sure to have the <edp-project> EDP namespace created in OpenShift:
```yaml
apiVersion: tenancy.kiosk.sh/v1alpha1
kind: Space
metadata:
  name: <edp-project>
spec:
  account: <organization_name>
```

## Install EDP
To store EDP data, use any existing Postgres database or create one during the installation.
Additionally, create two secrets in the <edp-project> project: one with administrative credentials and another with credentials for the EDP tenant (database schema).
* Create a secret for administrative access to database:
```
oc -n <edp-project> create secret generic super-admin-db --from-literal=username=<super_admin_db_username> --from-literal=password=<super_admin_db_password>
```

* Create a secret for an EDP tenant database user. If you want to use the same username as for the administrative access, the passwords must be the same as well:
```
oc -n <edp-project> create secret generic db-admin-console --from-literal=username=<tenant_db_username> --from-literal=password=<tenant_db_password>
```

* For EDP, it is required to have Keycloak access to perform the integration. Create secret with user and password provisioned in step 4 (see above):

```
oc -n <edp-project> create secret generic keycloak --from-literal=username=<username> --from-literal=password=<password>
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

* Make sure EDP installation chart disposes of the following parameters:
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
    - global.database.storage.class                                     # Type of storage class for DB volume. By default: "gp2", but it is highly recommended to use "gp2-retain". For details, please refer to point 8 of the Prerequisites section;
    - global.database.storage.size                                      # Size of storage;
    - global.webConsole.url                                             # Openshift dashboard URL;
    - global.openshift.deploymentType                                   # Wich type of kind will be deployed to Openshift (values: deployments/deploymentConfigs);
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
    - jenkins-operator.jenkins.imagePullSecrets                         # Secrets to pull from private Docker registry;
    - jenkins-operator.jenkins.basePath                                 # Base path for Jenkins URL;
    - jenkins-operator.jenkins.storage.class                            # Type of storage class. By default: "gp2", but it is highly recommended to use "gp2-retain". For details, please refer to point 8 of the Prerequisites section;
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
    - nexus-operator.nexus.storage.class                                # Storageclass for Nexus data volume. Default is "gp2", but it is highly recommended to use "gp2-retain". For details, please refer to point 8 of the Prerequisites section;
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
    - sonar-operator.sonar.storage.data.class                           # Storageclass for Sonarqube data volume. Default is "gp2", but it is highly recommended to use "gp2-retain". For details, please refer to point 8 of the Prerequisites section;
    - sonar-operator.sonar.storage.data.size                            # Sonarqube data volume size. Default is "1Gi";
    - sonar-operator.sonar.storage.database.class                       # Storageclass for Sonarqube database volume. Default is "gp2, but it is highly recommended to use "gp2-retain". For details, please refer to point 8 of the Prerequisites section;
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
    - admin-console-operator.adminConsole.projectUrlMask                # URL mask that leads to project in Openshift (for Openshift 3.9 - /console/project/{namespace}/overview);
    - admin-console-operator.adminConsole.imageStreamUrlMask            # URL mask that leads to image stream in Openshift (for Openshift 3.9 - /console/project/{namespace}/browse/images/{stream});
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
    - gerrit-operator.gitServer.nameSshKeySecret                        # Name of secret with credentials to Git server;
    - gerrit-operator.gerrit.storage.class                              # Storageclass for Gerrit data volume. Default is "gp2", but it is highly recommended to use "gp2-retain". For details, please refer to point 8 of the Prerequisites section;
    - gerrit-operator.gerrit.storage.size                               # Gerrit data volume size. Default is "1Gi";

    Reconciler parameters:
    - reconciler.image.name                                             # EDP image. The released image can be found on [Dockerhub](https://hub.docker.com/r/epamedp/reconciler);
    - reconciler.image.version                                          # EDP tag. The released image can be found on [Dockerhub](https://hub.docker.com/r/epamedp/reconciler/tags);

    PERF operator parameters:
    - tags.perf-operator                                                # Flag to enable/disable Perf operator deploy (e.g. true/false). By default: "false";
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

* If the external database is used, set the global.database.host value to the database DNS name accessible from the <edp-project> project;

* Install EDP in the <edp-project> project with the helm command.
Depending on the cloud provider, the parameter values may differ. Make sure that the set of values ​is correct for your provider.
Find the basic installation command example for AWS cloud below:
```bash
    helm install <helm_release_name> epamedp/edp-install --version "2.5.0" --wait --timeout=900s --namespace <edp-project> \
    --set global.edpName=<edp-project> \
    --set global.dnsWildCard=<cluster_DNS_wilcdard> \
    --set global.webConsole.url=<cluster_webConsole_url> \
    --set global.database.host=<database_host> \
    --set global.platform=openshift \
    --set 'global.admins={user1@example.com,user2@example.com}' \
    --set 'global.developers={user@example.com}' \
    --set global.database.storage.class=gp2 \
    --set keycloak-operator.keycloak.url=<keycloak_url> \
    --set dockerRegistry.url=<docker_registry_url> \
    --set gerrit-operator.gerrit.sshPort=<gerrit_port> \
    --set 'edp.adminGroups={<edp-project>-edp-admin}' \
    --set 'edp.developerGroups={<edp-project>-edp-developer}' \
    --set jenkins-operator.jenkins.storage.class=gp2 \
    --set jenkins-operator.jenkins.storage.size=10Gi \
    --set nexus-operator.nexus.storage.class=gp2 \
    --set nexus-operator.nexus.storage.size=50Gi \
    --set sonar-operator.sonar.storage.data.class=gp2 \
    --set sonar-operator.sonar.storage.data.size=1Gi \
    --set sonar-operator.sonar.storage.database.class=gp2 \
    --set sonar-operator.sonar.storage.database.size=1Gi \
    --set gerrit-operator.gerrit.storage.class=gp2 \
    --set gerrit-operator.gerrit.storage.size=1Gi \
    --set codebase-operator.jira.integration=false \
    --set codebase-operator.jira.name=epam-jira \
    --set codebase-operator.jira.apiUrl=https://jiraeu-api.epam.com \
    --set codebase-operator.jira.rootUrl=https://jiraeu.epam.com \
    --set codebase-operator.jira.credentialName=epam-jira-user
```

* As soon as Helm deploys components, create manually secrets for JIRA/GIT/PERF integration (if enabled).
Make sure the secret names are the same as the 'credentialName' property in JiraServer/PerfServer custom resources,
 and the 'nameSshKeySecret' property for GIT.

> **INFO**: If the system requires to use Luminate, make sure the secret name is the same as the **perf-operator.perf.luminate.credentialName** property.

* After the installation, if EDP is installed without Gerrit, it is possible to configure the [GitHub](https://github.com/epam/edp-admin-console/blob/master/documentation/github-integration.md#github-integration) or [GitLab](https://github.com/epam/edp-admin-console/blob/master/documentation/gitlab-integration.md#gitlab-integration) integration to work with EDP.
>_**NOTE**: The full installation with integration between tools will take at least 10 minutes._
