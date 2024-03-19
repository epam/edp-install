# Package Registry

This page describes the supported package registry providers and provides detailed instruction on how to adjust configurations to work properly with these package registry providers.

## Supported Package Registry Providers

Currently, KubeRocketCI Tekton pipelines support the following package registries:

* Nexus;
* GitLab;
* GitHub;
* Azure DevOps.

The table below displays the supported registries and the languages they correspond to:

| Language   |                        Framework                        |     Build Tool     |              Proxy Registry               |        Snapshots/Releases Registry        |
|------------|:-------------------------------------------------------:|:------------------:|:-----------------------------------------:|:-----------------------------------------:|
| Java       |              Java 8<br>Java 11<br>Java 17               |       Maven        | Nexus<br>Gitlab<br>GitHub<br>Azure DevOps | Nexus<br>Gitlab<br>GitHub<br>Azure DevOps |
| Python     |             Python 3.8<br>FastAPI<br>Flask              |       Python       |      Nexus<br>Gitlab<br>Azure DevOps      |      Nexus<br>Gitlab<br>Azure DevOps      |
| C#         |                  .Net 3.1<br>.Net 6.0                   |        .Net        |           No proxy is used for this language.                           | Nexus<br>Gitlab<br>GitHub<br>Azure DevOps |
| JavaScript | React<br>Vue<br>Angular<br>Express<br>Next.js<br>Antora |        NPM         | Nexus<br>Gitlab<br>GitHub<br>Azure DevOps | Nexus<br>Gitlab<br>GitHub<br>Azure DevOps |

## Proxy Package Registry Configuration

By default, KubeRocketCI uses Nexus as the proxy registry for storing and caching application dependencies. This setting is fixed and cannot be modified.

## Snapshot/Release Package Registry Configuration

The edp-tekton Helm Chart allows to override the default settings for package registries through `tekton.configs` part of its [values.yaml](https://github.com/epam/edp-tekton/blob/release/0.11/charts/pipelines-library/values.yaml#L81) file.

To provide necessary credentials for accessing the package registries, the user should create the `package-registries-auth-secret` secret and
set the `tekton.packageRegistriesSecret.enabled` value to `true` to mount the secret into the pipeline.

To replace the default name of the secret, the user should set the `tekton.packageRegistriesSecret.name` parameter to the desired value:

```yaml
tekton:
  packageRegistriesSecret:
    enabled: true
    name: "package-registries-auth-secret"

  configs:
    # Build Tool: Maven
    mavenConfigMap: "custom-maven-settings"
    # Build Tool: NPM
    npmConfigMap: "custom-npm-settings"
    # Build Tool: Python
    pythonConfigMap: "custom-python-settings"
    # Build Tool: .Net
    nugetConfigMap: "custom-nuget-settings"
```

### Customizing Maven Settings

A new custom configuration map should contain the `settings.xml` file, which overrides the default Maven configuration.

For example, the following configuration map contains the `settings.xml` file with the following settings:

* **Proxy registry**: Nexus.
* **Snapshots/Releases registry**: Azure DevOps registry.
* **Authentication**:
  * `CI_USERNAME` and `CI_PASSWORD` - these environment variables are used for authentication to Nexus.
  * `CI_AZURE_DEVOPS_USERNAME` and `CI_AZURE_DEVOPS_PASSWORD` - these environment variables are used for authentication to Azure DevOps registry.
* **Secrets**:
  * `CI_AZURE_DEVOPS_USERNAME` and `CI_AZURE_DEVOPS_PASSWORD` - these environment variables are taken from the `package-registries-auth-secret` secret.

??? Note "Example: custom-maven-settings.yaml "
    ```yaml
    apiVersion: v1
    kind: ConfigMap
    metadata:
      name: new-custom-maven-settings
    data:
      settings.xml: |
        <settings xmlns="http://maven.apache.org/SETTINGS/1.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0 http://maven.apache.org/xsd/settings-1.0.0.xsd">
            <localRepository>/workspace/source/cache</localRepository>

            <pluginGroups>
                <pluginGroup>org.sonarsource.scanner.maven</pluginGroup>
            </pluginGroups>
            <servers>
                <!-- The "nexus" server is defined to provide credentials required by the mirror. -->
                <server>
                    <id>nexus</id>
                    <username>${env.CI_USERNAME}</username>
                    <password>${env.CI_PASSWORD}</password>
                </server>
                <!-- The "azure-devops-registry" server is defined to provide credentials required by the Azure DevOps registry.
                Username and password are used for authentication.
                More documentation: https://learn.microsoft.com/en-us/azure/devops/artifacts/get-started-maven?view=azure-devops -->
                <server>
                    <id>azure-devops-registry</id>
                    <username>${env.CI_AZURE_DEVOPS_USERNAME}</username>
                    <password>${env.CI_AZURE_DEVOPS_PASSWORD}</password>
                </server>
            </servers>

            <mirrors>
                <mirror>
                    <!--This sends everything else to /public -->
                    <id>nexus</id>
                    <mirrorOf>*</mirrorOf>
                    <url>http://nexus.nexus:8081/repository/edp-maven-group</url>
                </mirror>
            </mirrors>

            <profiles>
                <profile>
                    <id>sonar</id>
                    <activation>
                         <activeByDefault>true</activeByDefault>
                    </activation>
                    <properties>
                        <sonar.login>
                            ${env.SONAR_TOKEN}
                        </sonar.login>
                        <sonar.host.url>
                            ${env.SONAR_HOST_URL}
                        </sonar.host.url>
                    </properties>
                </profile>
                <!-- Azure DevOps registry profile for managing artifacts within Azure DevOps. -->
                <profile>
                    <id>azure-devops-registry</id>
                    <properties>
                        <altSnapshotDeploymentRepository>azure-devops-registry::default::https://pkgs.dev.azure.com/<ORGANIZATION_NAME>/<PROJECT_NAME>/_packaging/<FEED_NAME>/maven/v1</altSnapshotDeploymentRepository>
                        <altReleaseDeploymentRepository>azure-devops-registry::default::https://pkgs.dev.azure.com/<ORGANIZATION_NAME>/<PROJECT_NAME>/_packaging/<FEED_NAME>/maven/v1</altReleaseDeploymentRepository>
                    </properties>
                </profile>
            </profiles>
            <!-- Specify the active profile here. If you want to push packages to nexus (default), gitlab registry, github registry,
            or Azure DevOps registry, change the activeProfile id to the required profile id. -->
            <activeProfiles>
                <activeProfile>azure-devops-registry</activeProfile>
            </activeProfiles>
        </settings>
    ```

### Customizing NPM Settings

A new custom configuration map should contain the `.npmrc-ci`, `.npmrc-publish-snapshots` and `.npmrc-publish-releases` files,
which override the default npm configuration.

For example, the following configuration map contains the `.npmrc-ci`, `.npmrc-publish-snapshots` and `.npmrc-publish-releases` files
with the following settings:

* **Proxy registry**: Nexus.
* **Snapshots/Releases registry**: Azure DevOps registry.
* **Authentication**:
  * 'upBase64' ("${CI_USERNAME}:${CI_PASSWORD}" string in base64) - this environment variable is used for authentication to Nexus.
  * `CI_AZURE_DEVOPS_USERNAME` and `CI_AZURE_DEVOPS_PASSWORD_IN_BASE64` - these environment variables are used for authentication to Azure DevOps registry.
* **Secrets**:
  * `CI_AZURE_DEVOPS_USERNAME` and `CI_AZURE_DEVOPS_PASSWORD_IN_BASE64` - these environment variables are taken from the `package-registries-auth-secret` secret.

??? Note "Example: custom-npm-settings.yaml "
    ```yaml
    apiVersion: v1
    kind: ConfigMap
    metadata:
      name: custom-npm-settings
    data:
      .npmrc-ci: |
        registry=${NEXUS_HOST_URL}/repository/edp-npm-group
        _auth=${upBase64}
        cache=${NPM_CACHE_DIR}

      .npmrc-publish-snapshots: |
        registry=https://pkgs.dev.azure.com/<ORGANIZATION_NAME>/<PROJECT_NAME>/_packaging/<FEED_NAME>/npm/registry
        username=${CI_AZURE_DEVOPS_USERNAME}
        _password=${CI_AZURE_DEVOPS_PASSWORD_IN_BASE64}
        email=${CI_AZURE_DEVOPS_USERNAME}
        cache=${NPM_CACHE_DIR}

      .npmrc-publish-releases: |
        registry=https://pkgs.dev.azure.com/<ORGANIZATION_NAME>/<PROJECT_NAME>/_packaging/<FEED_NAME>/npm/registry
        _auth=${upBase64}
        cache=${NPM_CACHE_DIR}
    ```

### Customizing Python Configurations

For customizing python settings, a new configuration map can be prepared to replace the default pythonConfigMap.
This custom configuration map should contain the PIP_TRUSTED_HOST, PIP_INDEX, PIP_INDEX_URL, REPOSITORY_URL_SNAPSHOTS and REPOSITORY_URL_RELEASES environment variables, which overrides the default python configuration.

For example, the following configuration map contains the following settings:

* **Proxy registry**: Azure DevOps registry.
* **Snapshots/Releases registry**: Azure DevOps registry.
* **Authentication**:
  * 'upBase64' ("${CI_USERNAME}:${CI_PASSWORD}" string in base64) - this environment variable is used for authentication to Azure DevOps registry.

??? Note "Example: custom-python-settings.yaml "
    ```yaml
    apiVersion: v1
    kind: ConfigMap
    metadata:
      name: custom-python-settings
    data:
      PIP_INDEX_PATH: "/<ORGANIZATION_NAME>/<PROJECT_NAME>/_packaging/<FEED_NAME>/pypi"
      PIP_INDEX_URL_PATH: "/<ORGANIZATION_NAME>/<PROJECT_NAME>/_packaging/<FEED_NAME>/pypi/simple"

      REPOSITORY_SNAPSHOTS_PATH: "/<ORGANIZATION_NAME>/<PROJECT_NAME>/_packaging/<FEED_NAME>/pypi/upload"
      REPOSITORY_RELEASES_PATH: "/<ORGANIZATION_NAME>/<PROJECT_NAME>/_packaging/<FEED_NAME>/pypi/upload"
    ```

### Customizing NuGet Settings

A new custom configuration map should contain the `nuget.config` file, which overrides the default NuGet configuration.

For example, the following configuration map contains the `nuget.config` file with the following settings:

* **Snapshots/Releases registry**: Azure DevOps registry.
* **Authentication**:
  * `CI_AZURE_DEVOPS_USERNAME` and `CI_AZURE_DEVOPS_PASSWORD`  - these environment variables are used for authentication to Azure DevOps registry.
* **Secrets**:
  * `CI_AZURE_DEVOPS_USERNAME` and `CI_AZURE_DEVOPS_PASSWORD` - these environment variables are taken from the `package-registries-auth-secret` secret.

??? Note "Example: custom-nuget-settings.yaml "
    ```yaml
    apiVersion: v1
    kind: ConfigMap
    metadata:
      name: custom-nuget-settings
    data:
      nuget.config: |
        <?xml version="1.0" encoding="utf-8"?>
        <configuration>
            <packageSources>
                <add key="nugetStorageSnapshots" value="https://pkgs.dev.azure.com/<ORGANIZATION_NAME>/<PROJECT_NAME>/_packaging/<FEED_NAME>/nuget/v3/index.json" />
                <add key="nugetStorageReleases" value="https://pkgs.dev.azure.com/<ORGANIZATION_NAME>/<PROJECT_NAME>/_packaging/<FEED_NAME>/nuget/v3/index.json" />
            </packageSources>
            <packageSourceCredentials>
                <nugetStorageSnapshots>
                    <add key="Username" value="%CI_AZURE_DEVOPS_USERNAME%" />
                    <add key="ClearTextPassword" value="%CI_AZURE_DEVOPS_PASSWORD%" />
                </nugetStorageSnapshots>
                <nugetStorageReleases>
                    <add key="Username" value="%CI_AZURE_DEVOPS_USERNAME%" />
                    <add key="ClearTextPassword" value="%CI_AZURE_DEVOPS_PASSWORD%" />
                </nugetStorageReleases>
            </packageSourceCredentials>
        </configuration>
    ```

## Related Articles

* [Manage Container Registries](../user-guide/manage-container-registries.md)
* [Nexus Sonatype Integration](nexus-sonatype.md)
* [Manage Git Servers](../user-guide/git-server-overview.md)
