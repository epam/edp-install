/**
 * Checkout from gerrit.  Parameters are defined by gerrit trigger
 * @param vars object with all pipeline variables
 */
def run(vars) {
    dir("${vars.workDir}") {
        sh "mvn org.jacoco:jacoco-maven-plugin:prepare-agent -Dmaven.test.failure.ignore=true verify org.jacoco:jacoco-maven-plugin:report -B --settings ${vars.devopsRoot}/${vars.mavenSettings}"
        junit "target/surefire-reports/*.xml"
    }
    this.result = "success"
}

return this;