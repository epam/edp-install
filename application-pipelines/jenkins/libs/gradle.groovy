def copyGradlePropertiesFile(){
        sh "cp -f ${vars.gradlePropertiesPath} ${vars.workDir}"
}

def getGradleArtifactVersion() {
        return sh(
                script: """
                        ${vars.gradleCommand} properties|egrep "version: "|awk '{print \$2}'    
                    """,
                returnStdout: true
        ).trim().toLowerCase()
}

def getGradleArtifactID() {
        return sh(
                script: """
                        ${vars.gradleCommand} properties|egrep "rootProject: root project "|awk -F "'" '{print \$2}' 
                    """,
                returnStdout: true
        ).trim()
}

def getGradleGroupID() {

        return sh(
                script: """
                    ${vars.gradleCommand} properties|egrep \"group: \"|awk '{print \$2}'
                """,
                returnStdout: true
        ).trim()
}

def getConstants() {
    vars['gradleInitScriptPath'] = "${vars.devopsRoot}/${vars.pipelinesPath}/settings/gradle/init.gradle"
    vars['gradlePropertiesPath'] = "${vars.devopsRoot}/${vars.pipelinesPath}/settings/gradle/gradle.properties"
    vars['gradleCommand'] = "gradle -I ${vars.gradleInitScriptPath} -PnexusMavenRepositoryUrl=${vars.nexusMavenRepositoryUrl}-group"
}

return this;
