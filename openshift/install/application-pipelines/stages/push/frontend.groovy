/**
 * Checkout from gerrit.  Parameters are defined by gerrit trigger
 * @param vars object with all pipeline variables
 */
def run(vars) {
    dir("${vars.serviceDir}") {
        withCredentials([usernamePassword(credentialsId: 'npm-publisher', passwordVariable: 'PASSWORD', usernameVariable: 'USERNAME')]) {
            def token = sh(script: """
        curl -s -H "Accept: application/json" -H "Content-Type:application/json" -X PUT --data '{"name": "${USERNAME}", "password": "${PASSWORD}"}' ${vars.npmSnapshotRegistry}-/user/org.couchdb.user:${USERNAME} | grep -oE 'NpmToken\\.[0-9a-zA-Z-]+'
        """,
                    returnStdout: true)

            sh (script: """
            set +x
            npm set registry ${vars.npmSnapshotRegistry}
            auth=\$(echo -n '${USERNAME}:${PASSWORD}' | openssl base64); npm set _auth=\$auth
            npm set //${vars.npmSnapshotRegistry}:_authToken ${token}
            npm set email=${vars['autoUser']}@epam.com
            """, returnStdout: false)
        }
        sh ("npm publish")
    }
    this.result = "success"
}

return this;
