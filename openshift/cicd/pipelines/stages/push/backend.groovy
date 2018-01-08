
def run(vars) {
    dir("${vars.serviceDir}") {
        sh "mvn deploy -DskipTests=true --settings ${vars.devopsRoot}/infrastructure/pipelines/settings/maven/settings.xml"
    }
    this.result = "success"
}

return this;
