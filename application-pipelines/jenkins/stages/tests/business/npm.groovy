def run(vars) {
    dir("${vars.workDir}") {
        sh "npm run test:coverage"
    }
    this.result = "success"
}

return this;