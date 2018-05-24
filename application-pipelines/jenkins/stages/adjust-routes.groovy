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

def run(vars, postfix, action) {
        vars.get(vars.appSettingsKey).each() { application ->
            if (application.route && application.deployed) {
                switch (action) {
                    case "create":
                        if (application.route && application.deployed) {
                            routePrefix = (!application.route_site) ? application.name : application.route_site
                            routePostfix = (postfix == "-stable") ? "" : postfix
                            sh "oc export route -n ${vars.deployProject} ${application.name} | oc patch " +
                                    "--patch='{\"spec\":{\"host\":\"${routePrefix}-${vars.pipelineProject}${routePostfix}.${vars.wildcard}\"}}' " +
                                    "--local=true -f - -o yaml | oc patch --patch='{\"metadata\":{\"name\":\"${application.name}${postfix}\"}}' " +
                                    "--local=true -f - -o yaml | oc patch --patch='{\"metadata\":{\"namespace\":\"${vars.deployProject}\"}}' " +
                                    "--local=true -f - -o yaml | oc create -f -"
                        }
                        break
                    case "delete":
                        sh "oc -n ${vars.deployProject} delete route ${application.name}${postfix} --ignore-not-found=true"
                        break
                }
            }
        }
    this.result = "success"
}

return this;