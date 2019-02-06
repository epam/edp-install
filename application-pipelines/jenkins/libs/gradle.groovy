def getGradleArtifactVersion() {
        withCredentials([usernamePassword(credentialsId: "${vars.nexusCredentialsId}", passwordVariable: 'PASSWORD', usernameVariable: 'USERNAME')]) {
            return sh(
                    script: """
                            ${vars.gradleCommand} -PnexusLogin=${USERNAME} -PnexusPassword=${PASSWORD} properties|egrep "version: "|awk '{print \$2}'
                        """,
                    returnStdout: true
            ).trim().toLowerCase()
        }
}

def getGradleArtifactID() {
        withCredentials([usernamePassword(credentialsId: "${vars.nexusCredentialsId}", passwordVariable: 'PASSWORD', usernameVariable: 'USERNAME')]) {
            return sh(
                    script: """
                            ${vars.gradleCommand} -PnexusLogin=${USERNAME} -PnexusPassword=${PASSWORD} properties|egrep "rootProject: root project "|awk -F "'" '{print \$2}' 
                        """,
                    returnStdout: true
            ).trim()
        }
}

def getGradleGroupID() {

        withCredentials([usernamePassword(credentialsId: "${vars.nexusCredentialsId}", passwordVariable: 'PASSWORD', usernameVariable: 'USERNAME')]) {
            return sh(
                    script: """
                        ${vars.gradleCommand} -PnexusLogin=${USERNAME} -PnexusPassword=${PASSWORD} properties|egrep \"group: \"|awk '{print \$2}'
                    """,
                    returnStdout: true
            ).trim()
        }
}

def getConstants() {
    vars['gradleInitScriptPath'] = "${vars.devopsRoot}/${vars.pipelinesPath}/settings/gradle/init.gradle"
    vars['gradleCommand'] = "gradle -I ${vars.gradleInitScriptPath} -PnexusMavenRepositoryUrl=${vars.nexusMavenRepositoryUrl}-group"
}

return this;
