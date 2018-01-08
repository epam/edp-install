import groovy.json.JsonSlurperClassic

def run(vars) {
    dir("${vars.serviceDir}") {
        timeout(time: 10, unit: 'MINUTES') { // Just in case something goes wrong, pipeline will be killed after a timeout
            def qualityGateResult = waitForQualityGate() // Reuse taskId previously collected by withSonarQubeEnv
            println("[JENKINS][DEBUG] qualityGateResult - ${qualityGateResult}")
            if (qualityGateResult.status != 'OK') {
                error "[JENKINS][ERROR] Sonar quality gate check failed: ${qualityGateResult.status}"
            }
        }
    }
    this.result = "success"
}

return this;
