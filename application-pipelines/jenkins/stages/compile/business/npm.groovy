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
        withCredentials([usernamePassword(credentialsId: "${vars.nexusCredentialsId}", passwordVariable: 'PASSWORD', usernameVariable: 'USERNAME')]) {
            def token = sh(script: """
        curl -s -H "Accept: application/json" -H "Content-Type:application/json" -X PUT --data '{"name": "${USERNAME}", "password": "${PASSWORD}"}' ${vars.npmGroupRegistry}-/user/org.couchdb.user:${USERNAME} | grep -oE 'NpmToken\\.[0-9a-zA-Z-]+'
        """,
                    returnStdout: true)
        }
        sh(script: """
            set +x
            npm set registry ${vars.npmGroupRegistry}
            """)

        sh "npm install && npm run build:clean"
    }
    this.result = "success"
}

return this;
