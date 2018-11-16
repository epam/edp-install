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

vars = [:]
commonLib = null

node("master") {
    if (!env.PIPELINES_PATH)
        error("[JENKINS][ERROR] PIPELINES_PATH variable is not defined, please check.")

    vars['pipelinesPath'] = PIPELINES_PATH
    vars['devopsRoot'] = "${WORKSPACE.replaceAll("@", "")}@script"

    stage('Input parameters') {
        vars.projectNames = input(id: 'Input', message: 'Input project names', ok: 'OK',
                parameters: [
                        [$class                 : 'ValidatingStringParameterDefinition', defaultValue: '',
                         description            : 'Input comma separated projects list', name: 'PROJECT_NAMES',
                         regex                  : '[a-z0-9]([-a-z0-9]*[a-z0-9])?(,[a-z0-9]([-a-z0-9]*[a-z0-9])?)*',
                         failedValidationMessage: 'Incorrect list of projects']
                ])
        try {
            assert vars.projectNames ==~ /[a-z0-9]([-a-z0-9]*[a-z0-9])?(,[a-z0-9]([-a-z0-9]*[a-z0-9])?)*/
        }
        catch (AssertionError err) {
            error "[JENKINS][DEBUG] - Project list does not match requirements"
        }
    }

    stage('Delete projects') {
        dir("${vars.devopsRoot}") {

            commonLib = load "${vars.pipelinesPath}/libs/common.groovy"
            commonLib.getConstants(vars)

            vars.projectsToDelete = vars.projectNames.tokenize(',')
            source = load "${vars.pipelinesPath}/stages/delete-environment.groovy"
            source.run(vars)
        }
    }
}