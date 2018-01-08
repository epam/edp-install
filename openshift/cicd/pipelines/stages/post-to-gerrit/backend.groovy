def run(vars) {
    dir("${vars.serviceDir}") {
        sonarToGerrit category: 'Sonar-Verified',
                issuesScore: '-1', noIssuesScore: '+1',
                changedLinesOnly: false,
                postScore: true,
                severity: "CRITICAL",
                subJobConfigs: [[projectPath: "${vars.serviceDir}", sonarReportPath: 'target/sonar/sonar-report.json']]
    }
    this.result = "success"
}

return this;
