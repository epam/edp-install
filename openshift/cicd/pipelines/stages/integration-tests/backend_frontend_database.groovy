def run(vars) {
    dir("${vars.serviceDir}/../sit-tests/rest-tests") {
        sh "mvn clean test -Dhost=http://backend.${vars.sitProject}:8080 --settings ${vars.devopsRoot}/${vars.mavenSettings}"
    }
    this.result = "success"
}

return this;
