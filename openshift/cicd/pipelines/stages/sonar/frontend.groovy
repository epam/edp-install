/**
 * Checkout from gerrit.  Parameters are defined by gerrit trigger
 * @param vars object with all pipeline variables
 */
def run(vars) {
    dir("${vars.serviceDir}") {
        def scannerHome = tool 'SonarQube Scanner'
        withSonarQubeEnv('Sonar') {
            sh "${scannerHome}/bin/sonar-scanner"
        }
    }
    this.result = "success"
}

return this;
