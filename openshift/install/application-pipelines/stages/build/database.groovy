/**
 * Checkout from gerrit.  Parameters are defined by gerrit trigger
 * @param vars object with all pipeline variables
 */
def run(vars) {
    dir("${vars.serviceDir}") {
        sh "mvn package -P assembly-jar-with-dependencies --settings ${vars.devopsRoot}/${vars.mavenSettings}"
        sh "mvn package -P assembly-tar --settings ${vars.devopsRoot}/${vars.mavenSettings}"

    }
    this.result = "success"
}

return this;
