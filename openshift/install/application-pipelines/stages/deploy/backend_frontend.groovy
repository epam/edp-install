def run(vars) {
    vars['projects'].each() { project ->
        sh "oc -n ${project} set image deploymentconfig/${vars.serviceType} ${vars.serviceType}=${project}/${vars.serviceType}:${vars.imageTag} --source=imagestreamtag"
        openshiftVerifyDeployment apiURL: '', authToken: '', depCfg: "${vars.serviceType}", namespace: "${project}", replicaCount: '1', verbose: 'false', verifyReplicaCount: 'true', waitTime: '5', waitUnit: 'min'
    }
}

return this;