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

def run(vars) {
    dir("${vars.workDir}") {
        nexusLib.uploadGroovyScriptToNexus(vars.nexusScripts.getNugetToken, "${vars.devopsRoot}/${vars.nexusScriptsPath}/${vars.nexusScripts.getNugetToken}.groovy")
        response = nexusLib.runNexusGroovyScript(vars.nexusScripts.getNugetToken, "{\"name\": \"${vars.nexusAutoUser}\"}")
        response = new JsonSlurperClassic().parseText(response.content)
        response = new JsonSlurperClassic().parseText(response.result)

        nugetApiKey = response.nuGetApiKey
        nugetPackagesPath = "/tmp/${vars.gerritProject}-nupkgs/"

        sh "dotnet pack ${vars.sln_filename} --no-build --output ${nugetPackagesPath}"
        sh "dotnet nuget push ${nugetPackagesPath} -k ${nugetApiKey} -s ${vars.nugetInternalRegistry}"
    }
    this.result = "success"
}

return this;
