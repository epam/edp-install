def run(vars, service) {
    sh "rm -rf ${vars.serviceDir}*"
    dir("${vars.serviceDir}") {
        checkout([$class: 'GitSCM', branches: [[name: "${vars.GitMicroservicesBranch}"]], doGenerateSubmoduleConfigurations: false, extensions: [[$class: 'CleanBeforeCheckout']], submoduleCfg: [], userRemoteConfigs: [[credentialsId: "${vars.credentialsId}", url: "${vars.GitMicroservicesUrl}"]]])
    }
    this.result="success"
}

return this;
