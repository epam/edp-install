
def run(vars) {
    dir("${vars.serviceDir}") {
        sh "mvn deploy -P assembly-tar -DskipTests=true --settings ${vars.devopsRoot}/${vars.mavenSettings}"
    }
    this.result = "success"
}

return this;
