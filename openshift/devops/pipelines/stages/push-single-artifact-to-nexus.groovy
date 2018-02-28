
def run(vars) {
    dir("${vars.workDir}") {
        def nexusRepository = vars.artifact.version.toUpperCase().contains("SNAPSHOT") ? "${vars.nexusRepository}-snapshots" : "${vars.nexusRepository}-releases"
                sh "mvn deploy:deploy-file \
                    -DgroupId=${vars.mavenEdpGroup} \
                    -DartifactId=${vars.artifact.id} \
                    -Dversion=${vars.artifact.version} \
                    -Dfile=${vars.artifact.path} \
                    -DrepositoryId=nexus \
                    -Durl=${nexusRepository} \
                    --settings ${vars.devopsRoot}/${vars.mavenSettings}"
    }
    this.result = "success"
}

return this;
