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

def createPipeline(pipelineName, applicationName, pipelineScript, pipelinePath, devopsRepository) {
    pipelineJob("${pipelineName}") {
        logRotator {
            numToKeep(10)
            daysToKeep(7)
        }
        triggers {
            gerrit {
                events {
                    if (pipelineName.contains("postcommit"))
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
                    stringParam("PIPELINES_PATH", "${pipelinePath}")
                    stringParam("GERRIT_PROJECT_NAME", "${applicationName}")
                }
            }
        }
    }
}

def gerritSshPort = "{{ gerrit_ssh_port }}"
def devopsRepository = "ssh://jenkins@gerrit:${gerritSshPort}/{{ project_gitlab_edp }}"
def pipelinePath = 'application-pipelines/jenkins'

['app.settings.json', 'auto-test.settings.json'].each() { settingsFile ->
    new JsonSlurperClassic().parseText(new File("${JENKINS_HOME}/project-settings/${settingsFile}").text).each() { item ->
        def applicationName = item.name
        createPipeline("Gerrit-precommit-${applicationName}", applicationName, "${pipelinePath}/Gerrit-precommit-pipeline.groovy", pipelinePath,  devopsRepository)
        if (settingsFile == 'app.settings.json')
            createPipeline("Gerrit-postcommit-${applicationName}", applicationName, "${pipelinePath}/Gerrit-postcommit-pipeline.groovy", pipelinePath,  devopsRepository)
    }
}