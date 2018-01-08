/**
 * Checkout from gerrit.  Parameters are defined by gerrit trigger
 * @param vars object with all pipeline variables
 */
def run(vars) {
    dir("${vars.serviceDir}") {
        withSonarQubeEnv('Sonar') {
            sh "mvn sonar:sonar -Dsonar.branch=${vars.serviceBranch} -B --settings ${vars.devopsRoot}/infrastructure/pipelines/settings/maven/settings.xml"
        }
    }
    this.result = "success"
}

return this;
