/* Copyright 2018 EPAM Systems.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at
http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.

See the License for the specific language governing permissions and
limitations under the License. */

import groovy.json.*

def createPipeline(pipelineName, applicationName, applicationStages, pipelineScript, pipelinePath, devopsRepository) {
    pipelineJob("${pipelineName}") {
        logRotator {
            numToKeep(10)
            daysToKeep(7)
        }
        triggers {
            gerrit {
                events {
                    if (pipelineName.contains("Build"))
                        changeMerged()
                    else
                        patchsetCreated()
                }
                project("plain:${applicationName}", ['ant:**'])
            }
        }
        definition {
            cpsScm {
                scm {
                    git {
                        remote { url(devopsRepository) }
                        branches("master")
                        scriptPath("${pipelineScript}")
                    }
                }
                parameters {
                    stringParam("STAGES", "${applicationStages}","")
                    stringParam("GERRIT_PROJECT_NAME", "${applicationName}","")
                    if (pipelineName.contains("Build"))
                        stringParam("BRANCH", "master","")
                }
            }
        }
    }
}

def gerritSshPort = "{{ gerrit_ssh_port }}"
def appRepositoryBase = "ssh://jenkins@gerrit:${gerritSshPort}"
def stages = [:]
stages['Code-review-application'] = "[{\"name\": \"gerrit-checkout\"},{\"name\": \"compile\"},{\"name\": \"tests\"}," +
        "{\"name\": \"sonar\"}]"
stages['Code-review-autotest'] = "[{\"name\": \"gerrit-checkout\"},{\"name\": \"tests\"},{\"name\": \"sonar\"}]"
stages['Build-maven'] = "[{\"name\": \"checkout\"},{\"name\": \"get-version\"},{\"name\": \"compile\"}," +
        "{\"name\": \"tests\"},{\"name\": \"sonar\"},{\"name\": \"build\"},{\"name\": \"build-image\"}," +
        "{\"name\": \"push\"},{\"name\": \"git-tag\"}]"
stages['Build-npm'] = stages['Build-maven']
stages['Build-gradle'] = stages['Build-maven']
stages['Build-dotnet'] = "[{\"name\": \"checkout\"},{\"name\": \"get-version\"},{\"name\": \"compile\"}," +
        "{\"name\": \"tests\"},{\"name\": \"sonar\"},{\"name\": \"build-image\"}," +
        "{\"name\": \"push\"},{\"name\": \"git-tag\"}]"

['app.settings.json', 'auto-test.settings.json'].each() { settingsFile ->
    new JsonSlurperClassic().parseText(new File("${JENKINS_HOME}/project-settings/${settingsFile}").text).each() { item ->
        def applicationName = item.name
        if (settingsFile == 'app.settings.json') {

            createPipeline("Code-review-${applicationName}", applicationName, stages['Code-review-application'],
                    "code-review.groovy", "", "${appRepositoryBase}/${item.name}")
            createPipeline("Build-${applicationName}", applicationName, stages["Build-${item.build_tool.toLowerCase()}"],
                    "build.groovy", "", "${appRepositoryBase}/${item.name}")
        }
        else
            createPipeline("Code-review-${applicationName}", applicationName, stages['Code-review-autotest'],
                    "code-review.groovy", "", "${appRepositoryBase}/${item.name}")

    }
}

if (Boolean.valueOf("${PARAM}")) {
    def appName = "${NAME}"
    def type = "${TYPE}"
    def buildTool = "${BUILD_TOOL}"
    if (type == "app") {
        createPipeline("Code-review-${appName}", appName, stages['Code-review-application'], "code-review.groovy", "", "${appRepositoryBase}/${appName}")
        createPipeline("Build-${appName}", appName, stages["Build-${buildTool}"], "build.groovy", "", "${appRepositoryBase}/${appName}")
    } else {
        createPipeline("Code-review-${appName}", appName, stages['Code-review-autotest'], "code-review.groovy", "", "${appRepositoryBase}/${appName}")
    }
}