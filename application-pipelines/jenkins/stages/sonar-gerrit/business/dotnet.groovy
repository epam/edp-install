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
                dotnet ${scannerHome}/SonarScanner.MSBuild.dll begin /k:${vars.gerritProject} /d:sonar.analysis.mode=preview \
                /d:sonar.report.export.path=sonar-report.json /d:sonar.branch=precommit
                dotnet build ${vars.sln_filename}
                dotnet ${scannerHome}/SonarScanner.MSBuild.dll end
            """
        }
        sonarToGerrit inspectionConfig: [baseConfig: [projectPath: "${vars.workDir}", sonarReportPath: '.sonarqube/out/.sonar/sonar-report.json'], serverURL: "${vars.sonarRoute}"],
                notificationConfig: [commentedIssuesNotificationRecipient: 'ALL', negativeScoreNotificationRecipient: 'ALL'],
                reviewConfig: [issueFilterConfig: [newIssuesOnly: false, changedLinesOnly: false, severity: 'MAJOR']],
                scoreConfig: [category: 'Sonar-Verified', issueFilterConfig: [severity: 'MAJOR']]
    }
    this.result = "success"
}

return this;
