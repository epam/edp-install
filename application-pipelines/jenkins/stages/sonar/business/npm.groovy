def run(vars) {
    dir("${vars.workDir}") {
        def scannerHome = tool 'SonarQube Scanner'
        withSonarQubeEnv('Sonar') {
            sh "${scannerHome}/bin/sonar-scanner -Dsonar.branch=${vars.serviceBranch}"
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