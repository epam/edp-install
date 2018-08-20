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

import hudson.FilePath
import groovy.json.*
import org.apache.commons.lang.RandomStringUtils

def run(vars) {
    new File("${vars.workDir}/${vars.atProject}").mkdirs()
    dir("${vars.workDir}/${vars.atProject}") {
        checkout([$class                           : 'GitSCM', branches: [[name: "master"]],
                  doGenerateSubmoduleConfigurations: false, extensions: [],
                  submoduleCfg                     : [],
                  userRemoteConfigs                : [[url    : "ssh://${vars.gerritAutoUser}@${vars.gerritHost}:" +
                          "${vars.gerritSshPort}/${vars.atProject}"]]])

        stash name: 'tests', includes: "**", useDefaultExcludes: false

        if (!fileExists("${vars.workDir}/${vars.atProject}/run.json"))
            error "[JENKINS][ERROR] There is no run.json file in the project ${vars.atProject}. " +
                    "Can't define command to run autotests"

        def runCommandFile = ""
        if (env['NODE_NAME'].equals("master")) {
            jsonFile=new File("${vars.workDir}/${vars.atProject}/run.json")
            runCommandFile = new FilePath(jsonFile).readToString()
        } else {
            runCommandFile = new FilePath(
                    Jenkins.getInstance().getComputer(env['NODE_NAME']).getChannel(),
                    "${vars.workDir}/${vars.atProject}/run.json").readToString()
        }

        def parsedRunCommandJson = new JsonSlurperClassic().parseText(runCommandFile)

        if (vars.projectPrefix.isEmpty())
            vars['environmentName'] = vars.pipelineProject
        else
            vars['environmentName'] = vars.pipelineProject.drop(vars.projectPrefix.length() + 1)

        if (!(vars.environmentName in parsedRunCommandJson.keySet()))
            error "[JENKINS][ERROR] Haven't found ${vars.environmentName} command in file run.json. " +
                    "It's mandatory to be specified, please check"

        def runCommand = parsedRunCommandJson["${vars.environmentName}"]

        // Determine slave type
        def slaveType=""
        def testFramework = ""
        vars.get(vars.atSettingsKey).each() { autotest ->
            if(autotest.name == vars.atProject) {
                slaveType = autotest.build_tool.toLowerCase()
                testFramework = autotest.report_framework.toLowerCase()
            }
        }

        if(slaveType == "")
            error "[JENKINS][ERROR] Haven't found slave type for ${vars.atProject} command in file auto-test settings. " +
                    "It's mandatory to be specified, please check"

        node("${slaveType}") {
            atRoot = new File("/tmp/${RandomStringUtils.random(10, true, true)}")
            dir("${atRoot}") {
                unstash 'tests'
                unstash 'mavenSettings'
                try {
                    sh "${runCommand}  -B --settings settings/maven/settings.xml"
                }
                catch (Exception ex) {
                    error "[JENKINS][ERROR] Tests from ${vars.atProject} have been failed. Reason - ${ex}"
                }
                finally {
                    switch (testFramework) {
                        case "allure":
                            allure([
                                    includeProperties: false,
                                    jdk              : '',
                                    properties       : [],
                                    reportBuildPolicy: 'ALWAYS',
                                    results          : [[path: 'target/allure-results']]
                            ])
                            break
                        default:
                            println("[JENKINS][WARNING] Can't publish test results. Testing framework is undefined.")
                            break
                    }
                }
            }
        }
    }
    this.result = "success"
}
return this;