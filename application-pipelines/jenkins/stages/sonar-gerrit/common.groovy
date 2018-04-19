def run(vars) {
    dir("${vars.workDir}") {
        withSonarQubeEnv('Sonar') {
            sh "mvn sonar:sonar -Dsonar.analysis.mode=preview -Dsonar.report.export.path=sonar-report.json" +
                    " -Dsonar.branch=precommit -B --settings ${vars.devopsRoot}/${vars.mavenSettings}"
        }
        sonarToGerrit inspectionConfig: [baseConfig: [projectPath: "${vars.serviceDir}"], serverURL: "${vars.sonarRoute}"],
                notificationConfig: [commentedIssuesNotificationRecipient: 'NONE', negativeScoreNotificationRecipient: 'NONE'],
                reviewConfig: [issueFilterConfig: [newIssuesOnly: false, changedLinesOnly: false, severity: 'CRITICAL']],
                scoreConfig: [category: 'Sonar-Verified', issueFilterConfig: [severity: 'CRITICAL']]
    }
    this.result = "success"
}

return this;