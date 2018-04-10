def run(vars) {
    dir("${vars.workDir}") {
        sh "mvn compile -B --settings ${vars.devopsRoot}/${vars.mavenSettings}"
    }
    this.result = "success"
}

return this;
