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

Jenkins jenkins = Jenkins.instance
def gerritSshPort = "{{ gerrit_ssh_port }}"
def codebaseRepositoryBase = "ssh://jenkins@gerrit:${gerritSshPort}"
def stages = [:]

stages['Code-review-application'] = "[{\"name\": \"gerrit-checkout\"},{\"name\": \"compile\"},{\"name\": \"tests\"}," +
        "{\"name\": \"sonar\"}]"
stages['Code-review-library'] = "[{\"name\": \"gerrit-checkout\"},{\"name\": \"compile\"},{\"name\": \"tests\"}," +
        "{\"name\": \"sonar\"}]"
stages['Code-review-autotests'] = "[{\"name\": \"gerrit-checkout\"},{\"name\": \"tests\"},{\"name\": \"sonar\"}]"

stages['Build-library-maven'] = "[{\"name\": \"checkout\"},{\"name\": \"get-version\"},{\"name\": \"compile\"}," +
        "{\"name\": \"tests\"},{\"name\": \"sonar\"},{\"name\": \"build\"},{\"name\": \"push\"},{\"name\": \"git-tag\"}]"
stages['Build-library-npm'] = stages['Build-library-maven']
stages['Build-library-gradle'] = stages['Build-library-maven']
stages['Build-library-dotnet'] = "[{\"name\": \"checkout\"},{\"name\": \"get-version\"},{\"name\": \"compile\"}," +
        "{\"name\": \"tests\"},{\"name\": \"sonar\"},{\"name\": \"push\"},{\"name\": \"git-tag\"}]"
stages['Build-application-maven'] = "[{\"name\": \"checkout\"},{\"name\": \"get-version\"},{\"name\": \"compile\"}," +
        "{\"name\": \"tests\"},{\"name\": \"sonar\"},{\"name\": \"build\"},{\"name\": \"build-image\"}," +
        "{\"name\": \"push\"},{\"name\": \"git-tag\"}]"
stages['Build-application-npm'] = stages['Build-application-maven']
stages['Build-application-gradle'] = stages['Build-application-maven']
stages['Build-application-dotnet'] = "[{\"name\": \"checkout\"},{\"name\": \"get-version\"},{\"name\": \"compile\"}," +
        "{\"name\": \"tests\"},{\"name\": \"sonar\"},{\"name\": \"build-image\"}," +
        "{\"name\": \"push\"},{\"name\": \"git-tag\"}]"
stages['Create-release'] = "[{\"name\": \"checkout\"},{\"name\": \"create-branch\"},{\"name\": \"trigger-job\"}]"

if (Boolean.valueOf("${PARAM}")) {
    def codebaseName = "${NAME}"
    def buildTool = "${BUILD_TOOL}"

    def codebaseFolder = jenkins.getItem(codebaseName)
    if (codebaseFolder == null) {
        folder(codebaseName)
    }

    createListView(codebaseName, "Releases")
    createReleasePipeline("Create-release-${codebaseName}", codebaseName, stages["Create-release"], "create-release.groovy",
            "${codebaseRepositoryBase}/${codebaseName}")

    if (BRANCH) {
        def branch = "${BRANCH}"
        createListView(codebaseName, "${branch.toUpperCase()}")

        def type = "${TYPE}"
        createCiPipeline("Code-review-${codebaseName}", codebaseName, stages["Code-review-${type}"], "code-review.groovy",
                "${codebaseRepositoryBase}/${codebaseName}", branch)

        if (type.equalsIgnoreCase('application') || type.equalsIgnoreCase('library')) {
            createCiPipeline("Build-${codebaseName}", codebaseName, stages["Build-${type}-${buildTool.toLowerCase()}"], "build.groovy",
                    "${codebaseRepositoryBase}/${codebaseName}", branch)
        }
    }
}

def createCiPipeline(pipelineName, codebaseName, codebaseStages, pipelineScript, repository, watchBranch = "master") {
    pipelineJob("${codebaseName}/${watchBranch.toUpperCase()}-${pipelineName}") {
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
                project("plain:${codebaseName}", ["plain:${watchBranch}"])
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
                    stringParam("STAGES", "${codebaseStages}", "Consequence of stages in JSON format to be run during execution")
                    stringParam("GERRIT_PROJECT_NAME", "${codebaseName}", "Gerrit project name(Codebase name) to be build")
                    if (pipelineName.contains("Build"))
                        stringParam("BRANCH", "${watchBranch}", "Branch to build artifact from")
                }
            }
        }
    }
}

def createReleasePipeline(pipelineName, codebaseName, codebaseStages, pipelineScript, repository) {
    pipelineJob("${codebaseName}/${pipelineName}") {
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
                    stringParam("STAGES", "${codebaseStages}", "")
                    if (pipelineName.contains("Create-release")) {
                        stringParam("GERRIT_PROJECT", "${codebaseName}", "")
                        stringParam("RELEASE_NAME", "", "Name of the release(branch to be created)")
                        stringParam("COMMIT_ID", "", "Commit ID that will be used to create branch from for new release. If empty, HEAD of master will be used")
                    }
                }
            }
        }
    }
}

def createListView(codebaseName, branchName) {
    listView("${codebaseName}/${branchName}") {
        if (branchName.toLowerCase() == "releases") {
            jobFilters {
                regex {
                    matchType(MatchType.INCLUDE_MATCHED)
                    matchValue(RegexMatchValue.NAME)
                    regex("^Create-release.*")
                }
            }
        } else {
            jobFilters {
                regex {
                    matchType(MatchType.INCLUDE_MATCHED)
                    matchValue(RegexMatchValue.NAME)
                    regex("^${branchName}-(Code-review|Build).*")
                }
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
