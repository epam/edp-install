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
    def runDir = vars.containsKey('sonarAnalysisRunTempDir') ? vars['sonarAnalysisRunTempDir'] : vars['workDir']
    dir("${runDir}") {
        withSonarQubeEnv('Sonar') {
            sh "gradle sonarqube -Dsonar.analysis.mode=preview -Dsonar.report.export.path=sonar-report.json" +
                    " -Dsonar.branch=precommit -Dsonar.projectKey=${vars.sonarProjectKey}" +
                    " -Dsonar.sources=${vars.workDir}/build -I ${vars.devopsRoot}/${vars.gradleInitScript}"
        }
        sonarToGerrit inspectionConfig: [baseConfig: [projectPath: "${vars.workDir}", sonarReportPath: 'build/sonar/sonar-report.json'], serverURL: "${vars.sonarRoute}"],
                notificationConfig: [commentedIssuesNotificationRecipient: 'NONE', negativeScoreNotificationRecipient: 'NONE'],
                reviewConfig: [issueFilterConfig: [newIssuesOnly: false, changedLinesOnly: false, severity: 'CRITICAL']],
                scoreConfig: [category: 'Sonar-Verified', issueFilterConfig: [severity: 'CRITICAL']]
    }
    this.result = "success"
}
return this;