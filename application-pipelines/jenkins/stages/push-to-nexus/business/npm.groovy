def run(vars) {
    dir("${vars.workDir}") {
        withCredentials([usernamePassword(credentialsId: 'nexus', passwordVariable: 'PASSWORD', usernameVariable: 'USERNAME')]) {
            def token = sh(script: """
        curl -s -H "Accept: application/json" -H "Content-Type:application/json" -X PUT --data '{"name": "${USERNAME}", "password": "${PASSWORD}"}' ${vars.npmInternalRegistry}-/user/org.couchdb.user:${USERNAME} | grep -oE 'NpmToken\\.[0-9a-zA-Z-]+'
        """,
                    returnStdout: true)

            sh (script: """
            set +x
            npm set registry ${vars.npmInternalRegistry}
            auth=\$(echo -n '${USERNAME}:${PASSWORD}' | openssl base64); npm set _auth=\$auth
            npm set //${vars.npmInternalRegistry}:_authToken ${token}
            npm set email=${vars.gerritAutoUser}@epam.com
            """, returnStdout: false)
        }
        sh ("npm publish")
    }
    this.result = "success"
}

return this;
