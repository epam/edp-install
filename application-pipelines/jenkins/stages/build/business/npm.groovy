def run(vars) {
    dir("${vars.workDir}") {
        withCredentials([usernamePassword(credentialsId: 'nexus', passwordVariable: 'PASSWORD', usernameVariable: 'USERNAME')]) {
            def token = sh(script: """
        curl -s -H "Accept: application/json" -H "Content-Type:application/json" -X PUT --data '{"name": "${USERNAME}", "password": "${PASSWORD}"}' ${vars.npmGroupRegistry}-/user/org.couchdb.user:${USERNAME} | grep -oE 'NpmToken\\.[0-9a-zA-Z-]+'
        """,
                    returnStdout: true)
        }
        sh(script: """
            set +x
            npm set registry ${vars.npmGroupRegistry}
            """)

        sh "npm install && npm run build:prod"
    }
    this.result = "success"
}

return this;