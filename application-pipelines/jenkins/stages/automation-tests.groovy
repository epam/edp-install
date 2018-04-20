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

        stash name: 'tests', includes: "*", useDefaultExcludes: false

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

        if (!(vars.pipelineProject in parsedRunCommandJson.keySet()))
            error "[JENKINS][ERROR] Haven't found ${vars.pipelineProject} command in file run.json. " +
                    "It's mandatory to be specified, please check"

        // Create latest routes for every application
        vars.get(vars.appSettingsKey).each() { application ->
            if(application.route) {
                sh "oc export route -n ${vars.deployProject} ${application.name} | oc patch " +
                        "--patch='{\"spec\":{\"host\":\"${application.name}-${vars.pipelineProject}-latest.${vars.wildcard}\"}}' " +
                        "--local=true -f - -o yaml | oc patch --patch='{\"metadata\":{\"name\":\"${application.name}-latest\"}}' " +
                        "--local=true -f - -o yaml | oc patch --patch='{\"metadata\":{\"namespace\":\"${vars.deployProject}\"}}' " +
                        "--local=true -f - -o yaml | oc create -f -"
            }
        }

        def runCommand = parsedRunCommandJson["${vars.pipelineProject}"]

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
            try {
                dir("${atRoot}") {
                    unstash 'tests'
                    sh "${runCommand}"
                }
                switch (testFramework) {
                    case "allure":
                        allure([
                                includeProperties: false,
                                jdk: '',
                                properties: [],
                                reportBuildPolicy: 'ALWAYS',
                                results: [[path: 'target/allure-results']]
                        ])
                        break
                    default:
                        println("[JENKINS][WARNING] Can't publish test results. Testing framework is undefined.")
                        break
                }
            }
            catch(Exception ex) {
                commonLib.failJob("[JENKINS][ERROR] Tests from ${vars.atProject} have been failed. Reason - ${ex}")
            }
            finally {
                // Deleting latest routes for every application
                vars.get(vars.appSettingsKey).each() { application ->
                    if(application.route) {
                        sh "oc delete -n ${vars.deployProject} route ${application.name}-latest"
                    }
                }
            }
        }
    }
    this.result = "success"
}
return this;