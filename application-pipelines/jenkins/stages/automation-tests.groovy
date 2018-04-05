import hudson.FilePath

def run(vars) {
    new File("${vars.workDir}/${vars.atProject}").mkdirs()
    dir("${vars.workDir}/${vars.atProject}") {
        checkout([$class                           : 'GitSCM', branches: [[name: "master"]],
                  doGenerateSubmoduleConfigurations: false, extensions: [],
                  submoduleCfg                     : [],
                  userRemoteConfigs                : [[url    : "ssh://${vars.gerritAutoUser}@${vars.gerritHost}:${vars.gerritSshPort}/${vars.atProject}"]]])

        if (!fileExists("${vars.workDir}/${vars.atProject}/run.json"))
            error "[JENKINS][ERROR] There is no run.json file in the project ${vars.atProject}. Can't define command to run autotests"

        def runCommandFile = new FilePath(
                Jenkins.getInstance().getComputer(env['NODE_NAME']).getChannel(),
                "${vars.workDir}/${vars.atProject}/run.json"
        ).readToString()
        def parsedRunCommandJson = new JsonSlurperClassic().parseText(runCommandFile)

        if (!("default" in parsedRunCommandJson.keySet()))
            error "[JENKINS][ERROR] Haven't found default command in file run.json. It's mandatory to be specified, please check"

        def runCommand = vars.environment in parsedRunCommandJson.keySet() ? parsedRunCommandJson["${vars.environment}"] : parsedRunCommandJson["default"]
        sh "${runCommand}"
    }
    this.result = "success"
}

return this;