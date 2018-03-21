import hudson.FilePath

def run(vars) {
    dir("${vars.serviceDir}") {
        vars['databaseYaml'] = new FilePath(Jenkins.getInstance().getComputer(env['NODE_NAME']).getChannel(), "${vars.devopsRoot}/${vars.pipelinesPath}/settings/fake-db/deployment_config.yaml")
        vars['databaseServiceYaml'] = new FilePath(Jenkins.getInstance().getComputer(env['NODE_NAME']).getChannel(), "${vars.devopsRoot}/${vars.pipelinesPath}/settings/fake-db/service.yaml")
        openshiftCreateResource apiURL: '', authToken: '', jsonyaml: vars.databaseYaml.readToString(), namespace: '', verbose: 'false'
        openshiftCreateResource apiURL: '', authToken: '', jsonyaml: vars.databaseServiceYaml.readToString(), namespace: '', verbose: 'false'
        sh "mvn package -P assembly-jar-with-dependencies --settings ${vars.devopsRoot}/${vars.mavenSettings}"
        openshiftVerifyDeployment apiURL: '', authToken: '', depCfg: 'fake-db', namespace: '', replicaCount: '1', verbose: 'false', verifyReplicaCount: 'true', waitTime: '5', waitUnit: 'min'
    }
    this.result = "success"
}

return this;
