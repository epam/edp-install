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
            if (!openshift.selector("buildconfig", "${vars.itemMap.name}").exists())
                openshift.newBuild("--name=${vars.itemMap.name}", "--image-stream=s2i-${vars.itemMap.language.toLowerCase()}", "--binary=true", "-e NGINX_STATIC_DIR=public")
            openshift.selector("bc", "${vars.itemMap.name}").startBuild("--from-dir=${vars.workDir}", "--wait=true")
            if (vars.promoteImage) {
                openshift.tag("${openshift.project()}/${vars.itemMap.name}:latest", "${vars.targetProject}/${vars.itemMap.name}:latest")
                sh "oc -n ${vars.targetProject} policy add-role-to-group registry-viewer system:unauthenticated"
            }
            else
                println("[JENKINS][WARNING] Image wasn't promoted since there are no environments were added\r\n" +
                        "[JENKINS][WARNING] If your like to promote your images please add environment via your cockpit panel")
        }
    }
}

return this;