# Upgrade EDP v.2.8.4 to v.2.9.0

This section provides the details on the EDP upgrade from the v.2.8.4 to the v.2.9.0. Explore the actions and requirements below.

!!! Note
    Kiosk is optional for EDP v.2.9.0 and higher, and [enabled](https://github.com/epam/edp-install/blob/release/2.9/deploy-templates/values.yaml#L34) by default. To disable it, add the following parameter to the `values.yaml` file: `kioskEnabled: false`. Please refer to the [Set Up Kiosk](install-kiosk.md) documentation for the details.

1. With Amazon Elastic Container Registry to store the images, there are two options:
  * Enable IRSA and create AWS IAM Role for Kaniko image builder. Please refer to the [IAM Roles for Kaniko Service Accounts](kaniko-irsa.md) section for the details.
  * The Amazon Elastic Container Registry Roles can be stored in an [instance profile](https://docs.aws.amazon.com/IAM/latest/UserGuide/id_roles_use_switch-role-ec2_instance-profiles.html).

2. Before updating EDP from v.2.8.4 to v.2.9.0, update the `gerrit-is-credentials` secret by adding the new `clientSecret` key with the value from `gerrit-is-credentials.client_secret`:

      kubectl edit secret gerrit-is-credentials -n <edp-namespace>

  * Make sure it looks as follows (replace with the necessary key value):

        data:
          client_secret: example
          clientSecret: example

3. Update Custom Resource Definitions. This command will apply all the necessary CRDs to the cluster:

      kubectl apply -f https://raw.githubusercontent.com/epam/edp-gerrit-operator/release/2.9/deploy-templates/crds/v2_v1alpha1_gerritgroupmember_crd.yaml
      kubectl apply -f https://raw.githubusercontent.com/epam/edp-gerrit-operator/release/2.9/deploy-templates/crds/v2_v1alpha1_gerritgroup_crd.yaml
      kubectl apply -f https://raw.githubusercontent.com/epam/edp-gerrit-operator/release/2.9/deploy-templates/crds/v2_v1alpha1_gerritprojectaccess_crd.yaml
      kubectl apply -f https://raw.githubusercontent.com/epam/edp-gerrit-operator/release/2.9/deploy-templates/crds/v2_v1alpha1_gerritproject_crd.yaml
      kubectl apply -f https://raw.githubusercontent.com/epam/edp-jenkins-operator/release/2.9/deploy-templates/crds/v2_v1alpha1_jenkins_crd.yaml
      kubectl apply -f https://raw.githubusercontent.com/epam/edp-jenkins-operator/release/2.9/deploy-templates/crds/v2_v1alpha1_jenkinsagent_crd.yaml
      kubectl apply -f https://raw.githubusercontent.com/epam/edp-jenkins-operator/release/2.9/deploy-templates/crds/v2_v1alpha1_jenkinsauthorizationrolemapping_crd.yaml
      kubectl apply -f https://raw.githubusercontent.com/epam/edp-jenkins-operator/release/2.9/deploy-templates/crds/v2_v1alpha1_jenkinsauthorizationrole_crd.yaml
      kubectl apply -f https://raw.githubusercontent.com/epam/edp-keycloak-operator/release/1.9/deploy-templates/crds/v1_v1alpha1_keycloakclientscope_crd.yaml
      kubectl apply -f https://raw.githubusercontent.com/epam/edp-keycloak-operator/release/1.9/deploy-templates/crds/v1_v1alpha1_keycloakrealmuser_crd.yaml
      kubectl apply -f https://raw.githubusercontent.com/epam/edp-nexus-operator/release/2.9/deploy-templates/crds/edp_v1alpha1_nexus_crd.yaml

4. With Amazon Elastic Container Registry to store and Kaniko to build the images, add the **kanikoRoleArn** parameter to the values before starting the update process. This parameter is indicated in AWS Roles once IRSA is enabled and AWS IAM Role is created for Kaniko.The value should look as follows:

      kanikoRoleArn: arn:aws:iam::<AWS_ACCOUNT_ID>:role/AWSIRSA‹CLUSTER_NAME›‹EDP_NAMESPACE›Kaniko

5. To upgrade EDP to the v.2.9.0, run the following command:

      helm upgrade --install edp epamedp/edp-install -n <edp-namespace> --values values.yaml --version=2.9.0

  !!! Note
      To verify the installation, it is possible to test the deployment before applying it to the cluster with:<br>
      `helm upgrade --install edp epamedp/edp-install -n <edp-namespace> --values values.yaml --version=2.9.0  --dry-run`


6. Remove the following Kubernetes resources left from the previous EDP installation (it is optional):

      kubectl delete rolebinding edp-cd-pipeline-operator-<edp-namespace>-admin -n <edp-namespace>

7. After EDP update, please restart the 'sonar-operator' pod to address the proper Sonar plugin versioning. After 'sonar-operator' is restarted, check the list of installed plugins in the corresponding SonarQube menu.

8. Update Jenkins pipelines and stages to the new release tag:
  * Restart the Jenkins pod
  * In Jenkins, go to **Manage Jenkins** -> **Configure system** -> Find the **Global Pipeline Libraries** menu
  * Make sure that the **Default version** for *edp-library-stages* is *build/2.10.0-RC.1*
  * Make sure that the **Default version** for *edp-library-pipelines* is *build/2.10.0-RC.1*

9. Update image versions for the Jenkins agents in the *ConfigMap*:

      kubectl edit configmap jenkins-slaves -n <edp-namespace>

   * The versions of the images should be:

        epamedp/edp-jenkins-codenarc-agent:1.0.1
        epamedp/edp-jenkins-dotnet-21-agent:1.0.3
        epamedp/edp-jenkins-dotnet-31-agent:1.0.3
        epamedp/edp-jenkins-go-agent:1.0.4
        epamedp/edp-jenkins-gradle-java8-agent:1.0.3
        epamedp/edp-jenkins-gradle-java11-agent:2.0.3
        epamedp/edp-jenkins-helm-agent:1.0.7
        epamedp/edp-jenkins-maven-java8-agent:1.0.3
        epamedp/edp-jenkins-maven-java11-agent:2.0.4
        epamedp/edp-jenkins-npm-agent:2.0.3
        epamedp/edp-jenkins-opa-agent:1.0.2
        epamedp/edp-jenkins-python-38-agent:2.0.4
        epamedp/edp-jenkins-terraform-agent:2.0.5

   * Restart the Jenkins pod.

10. Update the compatible plugins in Jenkins:<br>
  * Go to **Manage Jenkins** -> **Manage Plugins** -> Select **Compatible** -> Click **Download now and install after restart**

### Related Articles

* [Set Up Kiosk](install-kiosk.md)
* [IAM Roles for Kaniko Service Accounts](kaniko-irsa.md)
