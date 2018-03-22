/**
 * Checkout from gerrit.  Parameters are defined by gerrit trigger
 * @param vars object with all pipeline variables
 */
def run(vars) {
    dir("${vars.serviceDir}") {
        withCredentials([usernamePassword(credentialsId: 'npm-read-user', passwordVariable: 'PASSWORD', usernameVariable: 'USERNAME')]) {
            def token = sh(script: """
        curl -s -H "Accept: application/json" -H "Content-Type:application/json" -X PUT --data '{"name": "${USERNAME}", "password": "${PASSWORD}"}' ${vars.npmRegistry}-/user/org.couchdb.user:${USERNAME} | grep -oE 'NpmToken\\.[0-9a-zA-Z-]+'
        """,
                    returnStdout: true)
        }
        sh(script: """
            set +x
            npm set registry ${vars.npmRegistry}
            """)

        sh "npm install && npm run install-types && npm run build:prod"
    }
    this.result = "success"
}

return this;
