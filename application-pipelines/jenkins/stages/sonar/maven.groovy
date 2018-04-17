def run(vars) {
    dir("${vars.workDir}") {
        withSonarQubeEnv('Sonar') {
            sh "mvn sonar:sonar -Dsonar.branch=${vars.serviceBranch} -B --settings ${vars.devopsRoot}/${vars.mavenSettings}"
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
