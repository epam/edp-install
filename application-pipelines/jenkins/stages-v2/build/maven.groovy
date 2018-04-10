def run(vars) {
    dir("${vars.workDir}") {
        sh "mvn clean package -B -DskipTests=true --settings ${vars.devopsRoot}/${vars.mavenSettings}"
    }
    this.result = "success"
}

return this;
