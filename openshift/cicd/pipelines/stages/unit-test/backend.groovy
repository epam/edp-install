/**
 * Checkout from gerrit.  Parameters are defined by gerrit trigger
 * @param vars object with all pipeline variables
 */
def run(vars) {
    dir("${vars.serviceDir}") {
        sh "mvn org.jacoco:jacoco-maven-plugin:prepare-agent -Dmaven.test.failure.ignore=true verify org.jacoco:jacoco-maven-plugin:report -B --settings ${vars.devopsRoot}/infrastructure/pipelines/settings/maven/settings.xml"
        junit "target/surefire-reports/*.xml"
    }
    this.result = "success"
}

return this;