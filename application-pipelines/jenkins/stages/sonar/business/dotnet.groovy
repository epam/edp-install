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
        def scannerHome = tool 'SonarScannerMSBuild'
        withSonarQubeEnv('Sonar') {
            sh """
                dotnet ${scannerHome}/SonarScanner.MSBuild.dll begin /k:${vars.gerritProject} \\ 
                /d:sonar.projectKey=${vars.gerritProject} \\
                /d:sonar.projectName=${vars.gerritProject} \\               
                /d:sonar.branch=${vars.serviceBranch} /d:sonar.cs.opencover.reportsPaths=${vars.workDir}/*Tests*/*.xml
                dotnet build ${vars.sln_filename}
                dotnet ${scannerHome}/SonarScanner.MSBuild.dll end
            """
        }
        timeout(time: 10, unit: 'MINUTES') {
            def qualityGateResult = waitForQualityGate()
            if (qualityGateResult.status != 'OK')
                error "[JENKINS][ERROR] Sonar quality gate check has been failed with status ${qualityGateResult.status}"
        }
    }
    this.result = "success"
}

return this;
