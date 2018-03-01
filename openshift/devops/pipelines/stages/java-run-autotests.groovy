/**
 * Checkout from gerrit.  Parameters are defined by gerrit trigger
 * @param vars object with all pipeline variables
 */
def run(vars) {
    dir("${vars.workDir}/auto-tests/") {
        sh "mvn test -DocpEdpSuffix=${vars.ocProjectNameSuffix} " +
            "-Dsurefire.suiteXmlFiles=${vars.testSuites} -B --settings ${vars.devopsRoot}/${vars.mavenSettings}"
        allure results: [[path: 'target/allure-results']]
    }
    this.result = "success"
}

return this;