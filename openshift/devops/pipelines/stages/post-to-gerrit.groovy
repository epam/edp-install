def run(vars) {
    dir("${vars.workDir}") {
        sonarToGerrit (
                scoreConfig: [
                        issueFilterConfig: [
                                severity:"CRITICAL",
                                newIssuesOnly: false,
                                changedLinesOnly: false
                        ],
                        sonarURL: 'http://sonar-infra.main.edp.projects.epam.com',
                        category: 'Sonar-Verified',
                        noIssuesScore: +1,
                        issuesScore: -1,
                        subJobConfigs: [[projectPath: "${vars.workDir}", sonarReportPath: 'target/sonar/sonar-report.json']]

                ]
        )
    }
    this.result = "success"
}
return this;