import groovy.json.*
import hudson.FilePath

def run(vars) {
    dir("${vars.workDir}") {
        def runCommandFile = new FilePath(
                Jenkins.getInstance().getComputer(env['NODE_NAME']).getChannel(),
                "${vars.workDir}/run.json"
        )
        if (!runCommandFile.exists())
            error "[JENKINS][ERROR] There is no run.json file in the project ${vars.gerritProject}. Can't define command to run autotests"

        def parsedRunCommandJson = new JsonSlurperClassic().parseText(runCommandFile.readToString())
        if (!("precommit" in parsedRunCommandJson.keySet()))
            error "[JENKINS][ERROR] Haven't found precommit command in file run.json. It's mandatory to be specified, please check"

        sh "${parsedRunCommandJson.precommit}"

        switch (vars.itemMap.report_framework.toLowerCase()) {
            case "allure":
                allure([
                        includeProperties: false,
                        reportBuildPolicy: 'ALWAYS',
                        results: [[path: 'target/allure-results']]
                ])
                break
            default:
                println("[JENKINS][WARNING] Can't publish test results. Testing framework is unknown.")
                break
        }
    }
    this.result = "success"
}

return this