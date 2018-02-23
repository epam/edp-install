import groovy.json.JsonSlurperClassic

def run(vars) {
    dir("${vars.workDir}") {
        timeout(time: 10, unit: 'MINUTES') {
            def qualityGateResult = waitForQualityGate()
            if (qualityGateResult.status != 'OK') {
                error "[JENKINS][ERROR] Sonar quality gate check failed: ${qualityGateResult.status}"
            }
        }
    }
    this.result = "success"
}

return this;
