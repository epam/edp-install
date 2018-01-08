def run(vars) {
    dir("${vars.serviceDir}") {
        sh "npm test"
    }
    this.result = "success"
}

return this;