# Manage Custom Certificates

Familiarize yourself with the detailed instructions on adding certificates to EDP resources as well as with the respective setup for Keycloak.

EDP components that support custom certificates can be found in the table below:

| Helm Chart | Sub Resources |
| - | - |
| admin-console-operator | admin-console |
| gerrit-operator | edp-gerrit |
| jenkins-operator | jenkins-operator, edp-jenkins, jenkins agents |
| sonar-operator | sonar-operator, edp-sonar |
| keycloak-operator | keycloak-operator |
| nexus-operator | oauth2-proxy |
| edp-install | oauth2-proxy |
| edp-headlamp | edp-headlamp |

## Prerequisites

* The certificate in the `*.crt` format is used;
* [Kubectl](https://v1-23.docs.kubernetes.io/releases/download/) version 1.23.0 is installed;
* [Helm](https://helm.sh/docs/intro/install/) version 3.10.2 is installed;
* [Java](https://www.oracle.com/in/java/technologies/downloads/) with the `keytool` command inside;
* [jq](https://stedolan.github.io/jq/) is installed.

## Enable the SPI Truststore of Keycloak

To import custom certificates to Keycloak, follow the steps below:

1. Generate the `cacerts` local keystore and import the certificate there using the keytool tool:

  ```bash
  keytool -importcert -file CA.crt \
   -alias CA.crt -keystore ./cacerts \
   -storepass changeit -trustcacerts \
   -noprompt
  ```

2. Create the `custom-keycloak-keystore` keystore secret from the `cacerts` file in the `security` namespace:

  ```bash
  kubectl -n security create secret generic custom-keycloak-keystore \
  --from-file=./cacerts
  ```

3. Create the `spi-truststore-data` SPI truststore secret in the `security` namespace:

  ```bash
  kubectl -n security create secret generic spi-truststore-data \
  --from-literal=KC_SPI_TRUSTSTORE_FILE_FILE=/opt/keycloak/spi-certs/cacerts \
  --from-literal=KC_SPI_TRUSTSTORE_FILE_PASSWORD=changeit
  ```

4. Update the Keycloak `values.yaml` file from the [Install Keycloak](install-keycloak.md) page.

  ??? note "View: values.yaml"

      ```yaml
      ...
      extraVolumeMounts: |
        ...
        # Use the Keycloak truststore for SPI connection over HTTPS/TLS
        - name: spi-certificates
          mountPath: /opt/keycloak/spi-certs
          readOnly: true
        ...

      extraVolumes: |
        ...
        # Use the Keycloak truststore for SPI connection over HTTPS/TLS
        - name: spi-certificates
          secret:
            secretName: custom-keycloak-keystore
            defaultMode: 420
        ...

      ...
      extraEnvFrom: |
        - secretRef:
            name: spi-truststore-data
      ...
      ```

## Enable Custom Certificates in EDP Components

Creating custom certificates is a necessary but not sufficient condition for applying, therefore, certificates should be enabled as well.

1. Create the `custom-ca-certificates` secret in the EDP namespace.

   ```bash
   kubectl -n edp create secret generic custom-ca-certificates \
   --from-file=CA.crt
   ```

2. Add the certificate by mounting the `custom-ca-certificates` secret to the operator pod as a volume.<br>

  Example of specifying custom certificates for the `keycloak-operator`:

  ```yaml
  ...
  keycloak-operator:
    enabled: true

    # -- Additional volumes to be added to the pod
    extraVolumes:
      - name: custom-ca
        secret:
          defaultMode: 420
          secretName: custom-ca-certificates

    # -- Additional volumeMounts to be added to the container
    extraVolumeMounts:
      - name: custom-ca
        mountPath: /etc/ssl/certs/CA.crt
        readOnly: true
        subPath: CA.crt
  ...
  ```

!!! note
    Before moving ahead, be aware that starting from version 3.3.0, our development team has officially deprecated the Jenkins deploy scenario. This means that as of version 3.3.0 and in all subsequent versions (3.3.x and above), the Jenkins deploy scenario is no longer supported or actively maintained.<br>
    For users running versions 3.3.x and below, the Jenkins deploy scenario remains available. However, we encourage you to plan for the transition to a supported deployment method to ensure continued compatibility and access to the latest features and enhancements. To perform migration, please familiarize yourself with the [Migrate CI Pipelines From Jenkins to Tekton](migrate-ci-pipelines-from-jenkins-to-tekton.md).<br>
    For those who still use EDP v3.3.x and below, the information below remains valid and applicable.

3. For Sonar, Jenkins and Gerrit, change the flag in the `caCerts.enabled` field to `true`. Also, change the name of the secret in the `caCerts.secret` field to `custom-ca-certificates`.

  Example of specifying custom certificates for `Gerrit` via the `gerrit-operator` helm chart values:

  ```yaml
  ...
  gerrit-operator:
    enabled: true
    gerrit:
      caCerts:
        # -- Flag for enabling additional CA certificates
        enabled: true
        # -- Change init CA certificates container image
        image: adoptopenjdk/openjdk11:alpine
        # -- Name of the secret containing additional CA certificates
        secret: custom-ca-certificates
  ...
  ```


## Integrate Custom Certificates Into Jenkins Agents

This section describes how to add custom certificates to Jenkins agents to use them from Java applications.

!!! info
    For example, `curl` doesn't use keystore files specified in this part of the documentation.

EDP Jenkins agents keep keystore files in two places:

 - `/etc/ssl/certs/java` folder with the `cacerts` file;
 - `/opt/java/openjdk/lib/security` folder with the `blocked.certs`, `cacerts`, `default.policy` and `public_suffix_list.dat` files.


1. Copy the files in `/etc/ssl/certs/java` and `/opt/java/openjdk/lib/security` directories from Jenkins agent pod to the local `tmp` folder.<br>
There is a `copy_certs.sh` script below that can manage this. It copies the files in `/etc/ssl/certs/java` and `/opt/java/openjdk/lib/security` directories from Jenkins agent pod to the local `tmp` folder
and imports the custom certificate into the keystore files, after which it creates the
`jenkins-agent-opt-java-openjdk-lib-security-cacerts` and `jenkins-agent-etc-ssl-certs-java-cacerts` secrets from updated keystore files in EDP namespace.
Also, the `jenkins-agent-opt-java-openjdk-lib-security-cacerts` secret contains three additional files: `blocked.certs`, `default.policy` and `public_suffix_list.dat` which managed by the `copy_certs.sh` script as well. Expand the drop-down button below to see the contents of the `copy_certs.sh` script.

  ??? note "View: copy_certs.sh"

      ```bash
      # Fill in the variables `ns` and `ca_file`
      ns="edp-project"
      ca_file="/tmp/CA.crt"

      images=$(kubectl get -n "${ns}" cm jenkins-slaves -ojson | jq -r ".data[]" | grep image\> | sed 's/\s*<.*>\(.*\)<.*>/\1/')

      image=$(for i in ${images[@]}; do echo $i; done | grep maven-java8)
      pod_name=$(echo "${image}" | tr '.:/' '-')

      overrides="{\"apiVersion\":\"v1\",\"kind\":\"Pod\",\"metadata\":{\"name\":\"${pod_name}\", \"namespace\": \"${ns}\"},
      \"spec\":{\"containers\":[{\"name\":\"${pod_name}\",\"image\":\"${image}\",
      \"command\":[\"sh\",\"-c\",\"while true;do sleep 30;done;\"]}]}}"

      kubectl run -n "${ns}" "${pod_name}" --image "${image}" --overrides="${overrides}"

      kubectl wait --for=condition=ready pod "${pod_name}" -n "${ns}"

      cacerts_location=$(kubectl exec -n "${ns}" "${pod_name}" \
        -- find / -name cacerts -exec ls -la "{}" \; 2>/dev/null | grep -v ^l | awk '{print $9}')

      for cacerts in ${cacerts_location[@]}; do echo $(dirname "${cacerts}"); kubectl exec -n "${ns}" "${pod_name}" -- ls $(dirname "${cacerts}"); done

      for cacerts in ${cacerts_location[@]}; do \
          echo $(dirname "${cacerts}"); \
          mkdir -p "/tmp$(dirname "${cacerts}")"; \
          from_files=''; \
          for file in $(kubectl exec -n "${ns}" "${pod_name}" -- ls $(dirname "${cacerts}")); do \
              kubectl exec -n "${ns}" "${pod_name}" -- cat "$(dirname "${cacerts}")/${file}" > "/tmp$(dirname "${cacerts}")/${file}"; \
              from_files="${from_files} --from-file=/tmp$(dirname "${cacerts}")/${file}"
          done ; \
          keytool -import -storepass changeit -alias kubernetes -file ${ca_file} -noprompt -keystore "/tmp${cacerts}"; \
          kubectl -n "${ns}" create secret generic "jenkins-agent${cacerts//\//-}" $from_files \
      done

      kubectl delete -n "${ns}" pod "${pod_name}" --force --grace-period=0
      ```


   Before using the `copy_certs.sh` script, keep in mind the following:

   - assign actual values to the variables `ns` and `ca_file`;
   - the script collects all the images from the `jenkins-slaves` ConfigMap and uses the image of
    the `maven-java8` agent as the base image of the temporary pod to get the keystore files;
   - custom certificate is imported using the `keytool` application;
   - the `jenkins-agent-opt-java-openjdk-lib-security-cacerts` and `jenkins-agent-etc-ssl-certs-java-cacerts` secrets will be created
    in the EDP namespace.

2. Run the `copy_certs.sh` script from the previous point after the requirements are met.

3. Update manually the `jenkins-slaves` ConfigMap.

  Add this block with the mount of secrets to the `<volumes></volumes>` block of each Jenkins agent:

  ```xml
  ...
          <org.csanchez.jenkins.plugins.kubernetes.volumes.SecretVolume>
            <mountPath>/etc/ssl/certs/java</mountPath>
            <secretName>jenkins-agent-etc-ssl-certs-java-cacerts</secretName>
          </org.csanchez.jenkins.plugins.kubernetes.volumes.SecretVolume>
          <org.csanchez.jenkins.plugins.kubernetes.volumes.SecretVolume>
            <mountPath>/opt/java/openjdk/lib/security</mountPath>
            <secretName>jenkins-agent-opt-java-openjdk-lib-security-cacerts</secretName>
          </org.csanchez.jenkins.plugins.kubernetes.volumes.SecretVolume>
  ...
  ```

  As an example, the template of `gradle-java11-template` is shown below:<br>

  ```xml
  ...
        </workspaceVolume>
        <volumes>
          <org.csanchez.jenkins.plugins.kubernetes.volumes.SecretVolume>
            <mountPath>/etc/ssl/certs/java</mountPath>
            <secretName>jenkins-agent-etc-ssl-certs-java-cacerts</secretName>
          </org.csanchez.jenkins.plugins.kubernetes.volumes.SecretVolume>
          <org.csanchez.jenkins.plugins.kubernetes.volumes.SecretVolume>
            <mountPath>/opt/java/openjdk/lib/security</mountPath>
            <secretName>jenkins-agent-opt-java-openjdk-lib-security-cacerts</secretName>
          </org.csanchez.jenkins.plugins.kubernetes.volumes.SecretVolume>
        </volumes>
        <containers>
  ...
  ```

4. Reload the Jenkins pod:

  ```bash
  ns="edp"
  kubectl rollout restart -n "${ns}" deployment/jenkins
  ```


## Related Articles

* [Install EDP](install-edp.md)
* [Install Keycloak](install-keycloak.md)
