
def run(vars) {
    dir("${vars.workDir}") {
                sh "mvn org.apache.maven.plugins:maven-dependency-plugin:3.0.2:copy \
                    -Dartifact=${vars.mavenEdpGroup}:${vars.artifact.id}:${vars.artifact.version}:${vars.artifact.packaging} \
                    -DoutputDirectory=${vars.workDir} \
                    -Dmdep.stripVersion=true \
                    -Dmdep.useBaseVersion=true \
                    -Dmdep.overWriteReleases=true \
                    -Dmdep.overWriteSnapshots=true \
                    --settings ${vars.devopsRoot}/${vars.mavenSettings}"
    }
    this.result = "success"
}

return this;
