/**
 * Checkout from gerrit.  Parameters are defined by gerrit trigger
 * @param vars object with all pipeline variables
 */
def run(vars) {
    dir("${vars.serviceDir}") {
        try {
            sh (script: """
                set +x
                java -jar target/\$(ls target | grep jar) --driver=org.postgresql.Driver --changeLogFile=db/db.changelog.xml --url=\"jdbc:postgresql://fake-db:5432/\$DATABASE_NAME\" --username=\$DATABASE_USERNAME --password=\$DATABASE_PASSWORD update
            """)
        }
        catch(Exception ex) {
            error "[JENKINS][ERROR] Liquibase Update has been failed. Reason - ${ex.getMessage()}"
        }
        finally {
            openshiftDeleteResourceByJsonYaml apiURL: '', authToken: '', jsonyaml: vars.databaseYaml.readToString(), namespace: '', verbose: 'false'
            openshiftDeleteResourceByJsonYaml apiURL: '', authToken: '', jsonyaml: vars.databaseServiceYaml.readToString(), namespace: '', verbose: 'false'
        }
    }
    this.result = "success"
}

return this;