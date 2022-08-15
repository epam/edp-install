# Add Security Scanner

In order to add a new security scanner, perform the steps below:

1. Select a pipeline customization option from the [Customize CI Pipeline article](../user-guide/customize-ci-pipeline.md). Follow the steps described in this article, to create a new repository.

  !!! note
      This tutorial will focus on [adding a new stage using shared library via the custom global pipeline libraries](../user-guide/customize-ci-pipeline.md#add-a-new-stage-using-shared-library-via-custom-global-pipeline-libraries).

3. Open the new repository and create a directory with the `/src/com/epam/edp/customStages/impl/ci/impl/stageName/` name in the library repository, for example: `/src/com/epam/edp/customStages/impl/ci/impl/security/`. After that, add a Groovy file with another name to the same stages catalog, for example: `CustomSAST.groovy`.

4. Copy the logic from [`SASTMavenGradleGoApplication.groovy`](https://github.com/epam/edp-library-stages/blob/master/src/com/epam/edp/stages/impl/ci/impl/security/SASTMavenGradleGoApplication.groovy) stage into the new `CustomSAST.groovy` stage.

5. Add a new `runGoSecScanner` function to the stage:

    ```groovy
    @Stage(name = "sast-custom", buildTool = ["maven","gradle","go"], type = [ProjectType.APPLICATION])
    class CustomSAST {
    ...
        def runGoSecScanner(context) {
            def edpName = context.platform.getJsonPathValue("cm", "edp-config", ".data.edp_name")
            def reportData = [:]
            reportData.active = "true"
            reportData.verified = "false"
            reportData.path = "sast-gosec-report.json"
            reportData.type = "Gosec Scanner"
            reportData.productTypeName = "Tenant"
            reportData.productName = "${edpName}"
            reportData.engagementName = "${context.codebase.name}-${context.git.branch}"
            reportData.autoCreateContext = "true"
            reportData.closeOldFindings = "true"
            reportData.pushToJira = "false"
            reportData.environment = "Development"
            reportData.testTitle = "SAST"
            script.sh(script: """
                    set -ex
                    gosec -fmt=json -out=${reportData.path} ./...
            """)
            return reportData
        }
    ...
    }
    ```

6. Add function calls for the `runGoSecScanner` and `publishReport` functions:

    ```groovy
    ...
    script.node("sast") {
        script.dir("${testDir}") {
            script.unstash 'all-repo'
    ...
            def dataFromGoSecScanner = runGoSecScanner(context)
            publishReport(defectDojoCredentials, dataFromGoSecScanner)
        }
    }
    ...
    ```

7. Gosec scanner will be installed on the Jenkins SAST agent. It is based on the `epamedp/edp-jenkins-base-agent`. Please check [DockerHub](https://hub.docker.com/r/epamedp/edp-jenkins-base-agent/tags?page=1&ordering=last_updated) for its latest version.

   See below an example of the `edp-jenkins-sast-agent` Dockerfile:

   <details>
   <Summary><b>View: Default Dockerfile</b></Summary>

```
 # Copyright 2022 EPAM Systems.

 # Licensed under the Apache License, Version 2.0 (the "License");
 # you may not use this file except in compliance with the License.
 # You may obtain a copy of the License at
 # http://www.apache.org/licenses/LICENSE-2.0

 # Unless required by applicable law or agreed to in writing, software
 # distributed under the License is distributed on an "AS IS" BASIS,
 # WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.

 # See the License for the specific language governing permissions and
 # limitations under the License.

 FROM epamedp/edp-jenkins-base-agent:1.0.31

 SHELL ["/bin/bash", "-o", "pipefail", "-c"]

 USER root

 ENV SEMGREP_SCANNER_VERSION=0.106.0 \
     GOSEC_SCANNER_VERSION=2.12.0

 RUN apk --no-cache add \
     curl=7.79.1-r2 \
     build-base=0.5-r3 \
     python3-dev=3.9.5-r2 \
     py3-pip=20.3.4-r1 \
     go=1.16.15-r0

 # hadolint ignore=DL3059
 RUN pip3 install --no-cache-dir --upgrade --ignore-installed \
     pip==22.2.1 \
     ruamel.yaml==0.17.21 \
     semgrep==${SEMGREP_SCANNER_VERSION}

 # Install GOSEC
 RUN curl -Lo /tmp/gosec.tar.gz https://github.com/securego/gosec/releases/download/v${GOSEC_SCANNER_VERSION}/gosec_${GOSEC_SCANNER_VERSION}_linux_amd64.tar.gz && \
     tar xf /tmp/gosec.tar.gz && \
     rm -f /tmp/gosec.tar.gz && \
     mv gosec /bin/gosec

 RUN chown -R "1001:0" "$HOME" && \
     chmod -R "g+rw" "$HOME"

 USER 1001
```
   </details>

# Related Articles

* [Customize CI Pipeline](../user-guide/customize-ci-pipeline.md)
* [Static Application Security Testing (SAST)](overview-sast.md)
* [Semgrep](sast-scaner-semgrep.md)