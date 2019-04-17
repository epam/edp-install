/* Copyright 2019 EPAM Systems.

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
import jenkins.model.Jenkins

def createCiPipeline(pipelineName, applicationName, applicationStages, pipelineScript, repository, watchBranch = "master") {
    pipelineJob("${applicationName}/${watchBranch.toUpperCase()}-${pipelineName}") {
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
                project("plain:${applicationName}", ["plain:${watchBranch}"])
            }
        }
        definition {
            cpsScm {
                scm {
                    git {
                        remote { url(repository) }
                        branches("${watchBranch}")
                        scriptPath("${pipelineScript}")
                    }
                }
                parameters {
                    stringParam("STAGES", "${applicationStages}", "Consequence of stages in JSON format to be run during execution")
                    stringParam("GERRIT_PROJECT_NAME", "${applicationName}", "Gerrit project name(Application name) to be build")
                    if (pipelineName.contains("Build"))
                        stringParam("BRANCH", "${watchBranch}", "Branch to build artifact from")
                }
            }
        }
    }
}

def createReleasePipeline(pipelineName, applicationName, applicationStages, pipelineScript, repository) {
    pipelineJob("${applicationName}/${pipelineName}") {
        logRotator {
            numToKeep(14)
            daysToKeep(30)
        }
        definition {
            cpsScm {
                scm {
                    git {
                        remote { url(repository) }
                        branches("master")
                        scriptPath("${pipelineScript}")
                    }
                }
                parameters {
                    stringParam("STAGES", "${applicationStages}", "")
                    if (pipelineName.contains("Create-release")) {
                        stringParam("GERRIT_PROJECT", "${applicationName}", "")
                        stringParam("RELEASE_NAME", "", "Name of the release(branch to be created)")
                        stringParam("COMMIT_ID", "", "Commit ID that will be used to create branch from for new release. If empty, HEAD of master will be used")
                    }
                }
            }
        }
    }
}

def createListView(applicationName,branchName){
    listView("${applicationName}/${branchName}") {
        jobFilters {
            regex {
                matchType(MatchType.INCLUDE_MATCHED)
                matchValue(RegexMatchValue.NAME)
                regex("^${branchName}-(Code-review|Build).*")
            }
        }
        columns {
            status()
            weather()
            name()
            lastSuccess()
            lastFailure()
            lastDuration()
            buildButton()
        }
    }
}

Jenkins jenkins = Jenkins.instance
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
stages['Create-release'] = "[{\"name\": \"checkout\"},{\"name\": \"create-branch\"},{\"name\": \"trigger-job\"}]"

new JsonSlurperClassic().parseText(new File("${JENKINS_HOME}/project-settings/auto-test.settings.json").text).each() { item ->
    def applicationName = item.name
    def applicationFolder = jenkins.getItem(applicationName)
    if (applicationFolder == null){
        folder(applicationName)
    }
    createListView(applicationName,"MASTER")
    createCiPipeline("Code-review-${applicationName}", applicationName, stages['Code-review-autotest'],
            "code-review.groovy", "${appRepositoryBase}/${item.name}")
}

if (Boolean.valueOf("${PARAM}")) {
    def appName = "${NAME}"
    def buildTool = "${BUILD_TOOL}"

    def applicationFolder = jenkins.getItem(appName)
    if (applicationFolder == null){
        folder(appName)
    }

    createReleasePipeline("Create-release-${appName}", appName, stages["Create-release"], "create-release.groovy", "${appRepositoryBase}/${appName}")

    if (BRANCH) {
        def branch = "${BRANCH}"
        createListView(appName,"${branch.toUpperCase()}")
        createCiPipeline("Code-review-${appName}", appName, stages['Code-review-application'], "code-review.groovy", "${appRepositoryBase}/${appName}", branch)
        createCiPipeline("Build-${appName}", appName, stages["Build-${buildTool.toLowerCase()}"], "build.groovy", "${appRepositoryBase}/${appName}", branch)
    }
}