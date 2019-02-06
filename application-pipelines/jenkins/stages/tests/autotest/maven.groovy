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
import hudson.FilePath

def run(vars) {
    dir("${vars.workDir}") {
        def runCommandFile = new FilePath(
                Jenkins.getInstance().getComputer(env['NODE_NAME']).getChannel(),
                "${vars.workDir}/run.json"
        )
        if (!runCommandFile.exists())
            error "[JENKINS][ERROR] There is no run.json file in the project ${vars.gerritProject}. Can't define command to run autotests"

        def parsedRunCommandJson = new JsonSlurperClassic().parseText(runCommandFile.readToString())
        if (!("precommit" in parsedRunCommandJson.keySet()))
            error "[JENKINS][ERROR] Haven't found precommit command in file run.json. It's mandatory to be specified, please check"

        try {
            withCredentials([usernamePassword(credentialsId: "${vars.nexusCredentialsId}", passwordVariable: 'PASSWORD', usernameVariable: 'USERNAME')]) {
                sh "${parsedRunCommandJson.precommit} -Dnexus.username=${USERNAME} -Dnexus.password=${PASSWORD} -B --settings ${vars.devopsRoot}/${vars.mavenSettings}"
            }
        }

        catch (Exception ex) {
            error "[JENKINS][ERROR] Tests have been failed with error - ${ex}"
        }
        finally {
            switch (vars.itemMap.report_framework.toLowerCase()) {
                case "allure":
                    allure([
                            includeProperties: false,
                            reportBuildPolicy: 'ALWAYS',
                            results          : [[path: 'target/allure-results']]
                    ])
                    break
                default:
                    println("[JENKINS][WARNING] Can't publish test results. Testing framework is unknown.")
                    break
            }
        }
    }
    this.result = "success"
}

return this
