/**
 * Checkout from gerrit.  Parameters are defined by gerrit trigger
 * @param vars object with all pipeline variables
 */
def run(vars) {
    dir("${vars.serviceDir}") {
        withSonarQubeEnv('Sonar') {
            sh "mvn sonar:sonar -Dsonar.analysis.mode=preview -Dsonar.report.export.path=sonar-report.json -Dsonar.branch=precommit -B --settings ${vars.devopsRoot}/${vars.mavenSettings}"
        }
    }
    this.result = "success"
}

return this;
