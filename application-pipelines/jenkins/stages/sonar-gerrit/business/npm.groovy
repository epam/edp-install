def run(vars) {
    dir("${vars.workDir}") {
        def scannerHome = tool 'SonarQube Scanner'
        withSonarQubeEnv('Sonar') {
            sh "${scannerHome}/bin/sonar-scanner -Dsonar.analysis.mode=preview -Dsonar.report.export.path=sonar-report.json" +
                    " -Dsonar.branch=precommit"
        }
        sonarToGerrit inspectionConfig: [baseConfig: [projectPath: "${vars.workDir}", sonarReportPath: '.scannerwork/sonar-report.json'], serverURL: "${vars.sonarRoute}"],
                notificationConfig: [commentedIssuesNotificationRecipient: 'NONE', negativeScoreNotificationRecipient: 'NONE'],
                reviewConfig: [issueFilterConfig: [newIssuesOnly: false, changedLinesOnly: false, severity: 'CRITICAL']],
                scoreConfig: [category: 'Sonar-Verified', issueFilterConfig: [severity: 'CRITICAL']]
    }
    this.result = "success"
}

return this;