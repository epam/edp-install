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

import org.apache.commons.lang.RandomStringUtils

def run(vars) {
    vars['sonarAnalysisRunTempDir'] = new File("${vars.workDir}/../${RandomStringUtils.random(10, true, true)}")

    dir("${vars.sonarAnalysisRunTempDir}") {
        sh(script: """
            cd ${vars.workDir}
            for i in \$(git diff --diff-filter=AMR --name-status origin/master | awk \'{print \$2}\'); do cp --parents \$i ${vars.sonarAnalysisRunTempDir}/; done
            cp -f pom.xml ${vars.sonarAnalysisRunTempDir}/
            cp --parents -r src/test/ ${vars.sonarAnalysisRunTempDir}
            cp --parents -r target/ ${vars.sonarAnalysisRunTempDir}""")

        withSonarQubeEnv('Sonar') {
            sh "mvn sonar:sonar -Dsonar.analysis.mode=preview -Dsonar.report.export.path=sonar-report.json" +
                    " -Dsonar.branch=precommit -B --settings ${vars.devopsRoot}/${vars.mavenSettings}"
        }
        sonarToGerrit inspectionConfig: [baseConfig: [projectPath: ""], serverURL: "${vars.sonarRoute}"],
                notificationConfig: [commentedIssuesNotificationRecipient: 'NONE', negativeScoreNotificationRecipient: 'NONE'],
                reviewConfig: [issueFilterConfig: [newIssuesOnly: false, changedLinesOnly: false, severity: 'CRITICAL']],
                scoreConfig: [category: 'Sonar-Verified', issueFilterConfig: [severity: 'CRITICAL']]
    }
    this.result = "success"
}

return this;