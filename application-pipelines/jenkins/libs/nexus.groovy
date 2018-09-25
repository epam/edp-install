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

def uploadGroovyScriptToNexus(scriptName, pathToScript) {
    requestBody = [:]
    requestBody['content'] = readFile pathToScript
    requestBody['name'] = scriptName
    requestBody['type'] = "groovy"
    requestBody = JsonOutput.toJson(requestBody)

    //Check if script exists
    response = getNexusGroovyScript(scriptName)
    if (response.status == 404)
        addNexusGroovyScript(requestBody)
    else if (response.status == 200)
        println("Script ${scriptName} is already uploaded")
}

def deleteNexusGroovyScript(name) {
    httpRequest  authentication: 'nexus',
            httpMode: 'DELETE',
            url: "http://nexus:8081/service/siesta/rest/v1/script/${name}",
            contentType: 'APPLICATION_JSON',
            validResponseCodes: '204,404'
}

def addNexusGroovyScript(requestBody) {
    httpRequest  authentication: 'nexus',
            httpMode: 'POST',
            url: 'http://nexus:8081/service/siesta/rest/v1/script',
            contentType: 'APPLICATION_JSON',
            requestBody: requestBody
}

def runNexusGroovyScript(name, requestBody) {
    httpRequest authentication: 'nexus',
            httpMode: 'POST',
            url: "http://nexus:8081/service/siesta/rest/v1/script/${name}/run",
            contentType: 'TEXT_PLAIN',
            requestBody: requestBody
}

def getNexusGroovyScript(name){
    httpRequest  authentication: 'nexus',
            httpMode: 'GET',
            url: "http://nexus:8081/service/siesta/rest/v1/script/${name}",
            validResponseCodes: '200,404'
}

return this;
