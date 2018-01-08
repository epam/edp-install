import hudson.FilePath

def run(vars) {
    dir("${vars.serviceDir}") {
        vars['databaseYaml'] = new FilePath(Jenkins.getInstance().getComputer(env['NODE_NAME']).getChannel(), "${vars.devopsRoot}/infrastructure/pipelines/settings/database/database.yaml")
        vars['databaseServiceYaml'] = new FilePath(Jenkins.getInstance().getComputer(env['NODE_NAME']).getChannel(), "${vars.devopsRoot}/infrastructure/pipelines/settings/database/service.yaml")
        openshiftCreateResource apiURL: '', authToken: '', jsonyaml: vars.databaseYaml.readToString(), namespace: 'ci-cd', verbose: 'false'
        openshiftCreateResource apiURL: '', authToken: '', jsonyaml: vars.databaseServiceYaml.readToString(), namespace: 'ci-cd', verbose: 'false'
        sh "mvn package -P assembly-jar-with-dependencies --settings ${vars.devopsRoot}/infrastructure/pipelines/settings/maven/settings.xml"
        openshiftVerifyDeployment apiURL: '', authToken: '', depCfg: 'fake-db', namespace: 'ci-cd', replicaCount: '1', verbose: 'false', verifyReplicaCount: 'true', waitTime: '5', waitUnit: 'min'
    }
    this.result = "success"
}

return this;
