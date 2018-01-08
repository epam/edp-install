/**
 * Checkout from gerrit.  Parameters are defined by gerrit trigger
 * @param vars object with all pipeline variables
 */
def run(vars) {
    dir("${vars.serviceDir}") {
        try {
            sh "java -jar ./target/oc-petclinic-db-evolution-1.0-SNAPSHOT.jar --driver=org.postgresql.Driver --changeLogFile=db/db.changelog.xml --url=\"jdbc:postgresql://fake-db:5432/petclinic\" --username=petclinic --password=petclinic update"
        }
        catch(Exception ex) {
            error "[JENKINS][ERROR] Liquibase Update has been failed. Reason - ${ex.getMessage()}"
        }
        finally {
            openshiftDeleteResourceByJsonYaml apiURL: '', authToken: '', jsonyaml: vars.databaseYaml.readToString(), namespace: 'ci-cd', verbose: 'false'
            openshiftDeleteResourceByJsonYaml apiURL: '', authToken: '', jsonyaml: vars.databaseServiceYaml.readToString(), namespace: 'ci-cd', verbose: 'false'
        }
    }
    this.result = "success"
}

return this;