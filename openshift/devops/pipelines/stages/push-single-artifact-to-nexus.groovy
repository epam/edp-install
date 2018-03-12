
def run(vars) {
    dir("${vars.workDir}") {
                sh "mvn deploy:deploy-file \
                    -DgroupId=${vars.mavenEdpGroup} \
                    -DartifactId=${vars.artifact.id} \
                    -Dversion=${vars.artifact.version} \
                    -Dfile=${vars.artifact.path} \
                    -DrepositoryId=nexus \
                    -Durl=${vars.artifact.repository} \
                    --settings ${vars.devopsRoot}/${vars.mavenSettings}"
    }
    this.result = "success"
}

return this;
