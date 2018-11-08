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

def run(vars) {
    dir("${vars.workDir}") {
        vars['groupID'] = sh(
                script: """
                    gradle properties|egrep \"group: \"|awk '{print \$2}'
                """,
                returnStdout: true
        ).trim()
        vars['artifactVersion'] = sh(
                script: """
                        gradle properties|egrep "version: "|awk '{print \$2}'    
                    """,
                returnStdout: true
        ).trim().toLowerCase()
        vars['artifactID'] = sh(
                script: """
                        gradle properties|egrep "rootProject: root project "|awk -F "'" '{print \$2}' 
                    """,
                returnStdout: true
        ).trim()
        vars['sonarProjectKey'] = "${vars.groupID}:${vars.artifactID}:change-${vars.gerritChangeNumber}"
        vars['deployableModule'] = "${vars.artifactId}".trim()
        println("[JENKINS][DEBUG] Deployable module: ${vars.deployableModule}")
    }
    println("[JENKINS][DEBUG] Version - ${vars.version}")
    vars['businissAppVersion'] = "${vars.version}-${BUILD_NUMBER}"
}
return this;