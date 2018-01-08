/**
 * Checkout from gerrit.  Parameters are defined by gerrit trigger
 * @param vars object with all pipeline variables
 */
def run(vars) {
    dir("${vars.serviceDir}") {
        //sh "mvn clean install --settings ${vars.devopsRoot}/infrastructure/pipelines/settings/maven/settings.xml"
        sh "mvn package -P assembly-jar-with-dependencies --settings ${vars.devopsRoot}/infrastructure/pipelines/settings/maven/settings.xml"
        sh "mvn package -P assembly-tar --settings ${vars.devopsRoot}/infrastructure/pipelines/settings/maven/settings.xml"

    }
    this.result = "success"
}

return this;
