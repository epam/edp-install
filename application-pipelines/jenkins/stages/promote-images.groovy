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
    openshift.withCluster() {
        openshift.withProject() {
            sh "oc -n ${vars.targetProject} policy add-role-to-group registry-viewer system:unauthenticated"
            vars.get(vars.appSettingsKey).each() { application ->
                if(!application.deployed) {
                    println("[JENKINS][WARNING] Image ${application.name} hasn't been promoted since there is no image in ${vars.pipelineProject} project")
                    return
                }
                vars.targetTags.each() { tagName ->
                    openshift.tag("${vars.sourceProject}/${application.name}:${vars.sourceTag}", "${vars.targetProject}/${application.name}:${tagName}")
                }
            }
        }
    }
    this.result = "success"
}

return this;