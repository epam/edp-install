def run(vars) {
    dir("${vars.serviceDir}") {
        sh "npm run test:coverage"
    }
    this.result = "success"
}

return this;