import hudson.FilePath
import groovy.json.*
import org.apache.commons.lang.RandomStringUtils

def run(vars) {
    new File("${vars.workDir}/${vars.atProject}").mkdirs()
    dir("${vars.workDir}/${vars.atProject}") {
        checkout([$class                           : 'GitSCM', branches: [[name: "master"]],
                  doGenerateSubmoduleConfigurations: false, extensions: [],
                  submoduleCfg                     : [],
                  userRemoteConfigs                : [[url    : "ssh://${vars.gerritAutoUser}@${vars.gerritHost}:" +
                          "${vars.gerritSshPort}/${vars.atProject}"]]])

        stash name: 'tests', includes: "**", useDefaultExcludes: false

        if (!fileExists("${vars.workDir}/${vars.atProject}/run.json"))
            error "[JENKINS][ERROR] There is no run.json file in the project ${vars.atProject}. " +
                    "Can't define command to run autotests"

        def runCommandFile = ""
        if (env['NODE_NAME'].equals("master")) {
            jsonFile=new File("${vars.workDir}/${vars.atProject}/run.json")
            runCommandFile = new FilePath(jsonFile).readToString()
        } else {
            runCommandFile = new FilePath(
                    Jenkins.getInstance().getComputer(env['NODE_NAME']).getChannel(),
                    "${vars.workDir}/${vars.atProject}/run.json").readToString()
        }

        def parsedRunCommandJson = new JsonSlurperClassic().parseText(runCommandFile)

        if (vars.projectSuffix.isEmpty())
            vars['environmentName'] = vars.pipelineProject
        else
            vars['environmentName'] = vars.pipelineProject.take(vars.pipelineProject.length() - vars.projectSuffix.length() - 1)

        if (!(vars.environmentName in parsedRunCommandJson.keySet()))
            error "[JENKINS][ERROR] Haven't found ${vars.environmentName} command in file run.json. " +
                    "It's mandatory to be specified, please check"

        def runCommand = parsedRunCommandJson["${vars.environmentName}"]

        // Determine slave type
        def slaveType=""
        def testFramework = ""
        vars.get(vars.atSettingsKey).each() { autotest ->
            if(autotest.name == vars.atProject) {
                slaveType = autotest.build_tool.toLowerCase()
                testFramework = autotest.report_framework.toLowerCase()
            }
        }

        if(slaveType == "")
            error "[JENKINS][ERROR] Haven't found slave type for ${vars.atProject} command in file auto-test settings. " +
                    "It's mandatory to be specified, please check"

        node("${slaveType}") {
            atRoot = new File("/tmp/${RandomStringUtils.random(10, true, true)}")
            dir("${atRoot}") {
                unstash 'tests'
                try {
                    sh "${runCommand}"
                }
                catch (Exception ex) {
                    commonLib.failJob("[JENKINS][ERROR] Tests from ${vars.atProject} have been failed. Reason - ${ex}")
                }
                finally {
                    switch (testFramework) {
                        case "allure":
                            allure([
                                    includeProperties: false,
                                    jdk              : '',
                                    properties       : [],
                                    reportBuildPolicy: 'ALWAYS',
                                    results          : [[path: 'target/allure-results']]
                            ])
                            break
                        default:
                            println("[JENKINS][WARNING] Can't publish test results. Testing framework is undefined.")
                            break
                    }
                }
            }
        }
    }
    this.result = "success"
}
return this;