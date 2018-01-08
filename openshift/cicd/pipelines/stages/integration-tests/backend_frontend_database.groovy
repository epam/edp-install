def run(vars) {
    dir("${vars.serviceDir}/../sit-tests/rest-tests") {
        sh "mvn clean test -Dhost=http://backend-sit.10.17.129.14.xip.io --settings ${vars.devopsRoot}/infrastructure/pipelines/settings/maven/settings.xml"
    }
    this.result = "success"
}

return this;
