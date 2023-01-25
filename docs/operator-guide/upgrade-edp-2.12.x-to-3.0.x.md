# Upgrade EDP v.2.12.x to v.3.0.x

!!! Important
    * Before starting the upgrade procedure, please make the necessary backups.
    * Kiosk integration is disabled by default. With EDP below v.3.0.x, define the `global.kioskEnabled` parameter in the [values.yaml](https://github.cm/epam/edp-install/blob/release/3.0/deploy-templates/values.yaml) file. For details, please refer to the [Set Up Kiosk](install-kiosk.md) page.
    * The `gerrit-ssh-port` parameter is moved from the `gerrit-operator.gerrit.sshport` to `global.gerritSSHPort` [values.yaml](https://github.com/epam/edp-install/blob/master/deploy-templates/values.yaml#L30) file.
    * In edp-gerrit-operator, the `gitServer.user` value is changed from the `jenkins` to `edp-ci`[values.yaml](https://github.com/epam/edp-gerrit-operator/blob/release/2.13/deploy-templates/values.yaml#L96) file.

This section provides the details on upgrading EDP from the v.2.12.x to the v.3.0.x. Explore the actions and requirements below.

1. Update Custom Resource Definitions (CRDs). Run the following command to apply all necessary CRDs to the cluster:

      kubectl apply -f https://raw.githubusercontent.com/epam/edp-gerrit-operator/d9a4d15244c527ef6d1d029af27574282a281b98/deploy-templates/crds/v2.edp.epam.com_gerrits.yaml
      kubectl apply -f https://raw.githubusercontent.com/epam/edp-codebase-operator/release/2.14/deploy-templates/crds/v2.edp.epam.com_cdstagedeployments.yaml
      kubectl apply -f https://raw.githubusercontent.com/epam/edp-codebase-operator/release/2.14/deploy-templates/crds/v2.edp.epam.com_codebasebranches.yaml
      kubectl apply -f https://raw.githubusercontent.com/epam/edp-codebase-operator/release/2.14/deploy-templates/crds/v2.edp.epam.com_codebaseimagestreams.yaml
      kubectl apply -f https://raw.githubusercontent.com/epam/edp-codebase-operator/release/2.14/deploy-templates/crds/v2.edp.epam.com_codebases.yaml
      kubectl apply -f https://raw.githubusercontent.com/epam/edp-codebase-operator/release/2.14/deploy-templates/crds/v2.edp.epam.com_gitservers.yaml
      kubectl apply -f https://raw.githubusercontent.com/epam/edp-codebase-operator/release/2.14/deploy-templates/crds/v2.edp.epam.com_gittags.yaml
      kubectl apply -f https://raw.githubusercontent.com/epam/edp-codebase-operator/release/2.14/deploy-templates/crds/v2.edp.epam.com_imagestreamtags.yaml
      kubectl apply -f https://raw.githubusercontent.com/epam/edp-codebase-operator/release/2.14/deploy-templates/crds/v2.edp.epam.com_jiraissuemetadatas.yaml
      kubectl apply -f https://raw.githubusercontent.com/epam/edp-codebase-operator/release/2.14/deploy-templates/crds/v2.edp.epam.com_jiraservers.yaml
      kubectl apply -f https://raw.githubusercontent.com/epam/edp-keycloak-operator/release/1.14/deploy-templates/crds/v1.edp.epam.com_keycloakauthflows.yaml
      kubectl apply -f https://raw.githubusercontent.com/epam/edp-keycloak-operator/release/1.14/deploy-templates/crds/v1.edp.epam.com_keycloakclients.yaml
      kubectl apply -f https://raw.githubusercontent.com/epam/edp-keycloak-operator/release/1.14/deploy-templates/crds/v1.edp.epam.com_keycloakclientscopes.yaml
      kubectl apply -f https://raw.githubusercontent.com/epam/edp-keycloak-operator/release/1.14/deploy-templates/crds/v1.edp.epam.com_keycloakrealmcomponents.yaml
      kubectl apply -f https://raw.githubusercontent.com/epam/edp-keycloak-operator/release/1.14/deploy-templates/crds/v1.edp.epam.com_keycloakrealmgroups.yaml
      kubectl apply -f https://raw.githubusercontent.com/epam/edp-keycloak-operator/release/1.14/deploy-templates/crds/v1.edp.epam.com_keycloakrealmidentityproviders.yaml
      kubectl apply -f https://raw.githubusercontent.com/epam/edp-keycloak-operator/release/1.14/deploy-templates/crds/v1.edp.epam.com_keycloakrealmrolebatches.yaml
      kubectl apply -f https://raw.githubusercontent.com/epam/edp-keycloak-operator/release/1.14/deploy-templates/crds/v1.edp.epam.com_keycloakrealmroles.yaml
      kubectl apply -f https://raw.githubusercontent.com/epam/edp-keycloak-operator/release/1.14/deploy-templates/crds/v1.edp.epam.com_keycloakrealms.yaml
      kubectl apply -f https://raw.githubusercontent.com/epam/edp-keycloak-operator/release/1.14/deploy-templates/crds/v1.edp.epam.com_keycloakrealmusers.yaml
      kubectl apply -f https://raw.githubusercontent.com/epam/edp-keycloak-operator/release/1.14/deploy-templates/crds/v1.edp.epam.com_keycloaks.yaml

2. Set the required parameters. For more details, please refer to the [values.yaml](https://github.com/epam/edp-install/blob/release/3.0/deploy-templates/values.yaml) file.

   <details>
   <summary><b>View: values.yaml</b></summary>

   ```yaml
   edp-tekton:
     enabled: false
   admin-console-operator:
     enabled: true
   jenkins-operator:
     enabled: true
   ```
   </details>

3. Add proper Helm annotations and labels as indicated below. This step is necessary starting from the release v.3.0.x as custom resources are managed by Helm and removed from the Keycloak Controller logic.
      ```
        kubectl label EDPComponent main-keycloak app.kubernetes.io/managed-by=Helm -n <edp-namespace>
        kubectl annotate EDPComponent main-keycloak meta.helm.sh/release-name=<edp-release-name> -n <edp-namespace>
        kubectl annotate EDPComponent main-keycloak meta.helm.sh/release-namespace=<edp-namespace> -n <edp-namespace>
        kubectl label KeycloakRealm main app.kubernetes.io/managed-by=Helm -n <edp-namespace>
        kubectl annotate KeycloakRealm main meta.helm.sh/release-name=<edp-release-name> -n <edp-namespace>
        kubectl annotate KeycloakRealm main meta.helm.sh/release-namespace=<edp-namespace> -n <edp-namespace>

      ```

4. To upgrade EDP to the v.3.0.x, run the following command:

      helm upgrade edp epamedp/edp-install -n <edp-namespace> --values values.yaml --version=3.0.x

  !!! Note
      To verify the installation, it is possible to test the deployment before applying it to the cluster with the following command:<br>
      `helm upgrade edp epamedp/edp-install -n <edp-namespace> --values values.yaml --version=3.0.x  --dry-run`

5. Update image versions for the Jenkins agents in the *ConfigMap*:

        kubectl edit configmap jenkins-slaves -n <edp-namespace>

   * The versions of the images must be the following:

        epamedp/edp-jenkins-codenarc-agent:3.0.10
        epamedp/edp-jenkins-dotnet-31-agent:3.0.9
        epamedp/edp-jenkins-go-agent:3.0.17
        epamedp/edp-jenkins-gradle-java11-agent:3.0.7
        epamedp/edp-jenkins-gradle-java8-agent:3.0.10
        epamedp/edp-jenkins-helm-agent:3.0.11
        epamedp/edp-jenkins-kaniko-docker-agent:1.0.9
        epamedp/edp-jenkins-maven-java11-agent:3.0.7
        epamedp/edp-jenkins-maven-java8-agent:3.0.10
        epamedp/edp-jenkins-npm-agent:3.0.9
        epamedp/edp-jenkins-opa-agent:3.0.7
        epamedp/edp-jenkins-python-38-agent:3.0.8
        epamedp/edp-jenkins-sast-agent:0.1.5
        epamedp/edp-jenkins-terraform-agent:3.0.9

   * Remove the `edp-jenkins-dotnet-21-agent` agent manifest.

   * Restart the Jenkins pod.

6. Attach the `id_rsa.pub` SSH public key from the `gerrit-ciuser-sshkey` secret to the `edp-ci` Gerrit user in the `gerrit` pod:

      ssh -p <gerrit_ssh_port> <host> gerrit set-account --add-ssh-key ~/id_rsa.pub

  !!! Notes
      * For this operation, use the `gerrit-admin` SSH key from secrets.
      * `<host>` is admin@localhost or any other user with permissions.

7. Change the username from `jenkins` to `edp-ci` in the `gerrit-ciuser-sshkey` secret:

      kubectl -n <edp-namespace> patch secret gerrit-ciuser-sshkey\
       --patch="{\"data\": { \"username\": \"$(echo -n edp-ci |base64 -w0)\" }}" -oyaml

!!! Warning
    In EDP v.3.0.x, [Admin Console](../../user-guide/#admin-console) is deprecated, and EDP interface is available only via [Headlamp](../../headlamp-user-guide/#overview).

## Related Articles
* [Migrate CI Pipelines From Jenkins to Tekton](migrate-ci-pipelines-from-jenkins-to-tekton.md)