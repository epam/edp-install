import groovy.json.*

def gerritPostCommit(appName, gerProject, postCommitScript, pipelinePath, devopsRepo, serviceType) {

    pipelineJob("Gerrit-Postcommit-${appName}") {
        logRotator {
            numToKeep(10)
            daysToKeep(7)
        }

        triggers {
            gerritTrigger {
                triggerOnEvents {
                    changeMerged()
                }
                gerritProjects {
                    gerritProject {
                        compareType('ANT')
                        pattern("${gerProject}")
                        branches {
                            branch {
                                compareType('ANT')
                                pattern("**")
                            }
                        }
                        disableStrictForbiddenFileVerification(false)
                    }
                }
            }
        }
        definition {

            cpsScm {

                scm {
                    git {
                        remote { url(devopsRepo) }
                        branches("master")
                        scriptPath("${postCommitScript}")
                    }
                }
                parameters {
                    stringParam("PIPELINES_PATH", "${pipelinePath}")
                    stringParam("SERVICE_TYPE", "${serviceType}")
                    stringParam("GERRIT_PROJECT_NAME", "${gerProject}")
                }


            }
        }
    }

}

def gerritPreCommit(appName, gerProject, preCommitScript, pipelinePath, devopsRepo) {
    pipelineJob("Gerrit-Precommit-${appName}") {
        logRotator {
            numToKeep(10)
            daysToKeep(7)
        }

        triggers {
            gerritTrigger {
                triggerOnEvents {
                    changeMerged()
                }
                gerritProjects {
                    gerritProject {
                        compareType('ANT')
                        pattern("${gerProject}")
                        branches {
                            branch {
                                compareType('ANT')
                                pattern('**')
                            }
                        }
                        disableStrictForbiddenFileVerification(false)
                    }
                }
            }
        }
        definition {

            cpsScm {

                scm {
                    git {
                        remote { url(devopsRepo) }
                        scriptPath("${preCommitScript}")
                    }
                }
                parameters {
                    stringParam("PIPELINES_PATH", "${pipelinePath}")
                }


            }
        }
    }

}

def appSettings = new File("${JENKINS_HOME}/app-settings/app.settings.json")
if (appSettings.exists()) {

    def slurper = new JsonSlurperClassic()
    def apps = slurper.parseText(appSettings.text)
    for (app in apps) {
        def appName = app.name
        def devopsRepo = "ssh://jenkins@gerrit:29418/devops"
        def pipelinePath = 'openshift/cicd/pipelines'
        def gerProject = app.name
        def serviceType = app.type
        def postCommitScript = "${pipelinePath}/Jenkinsfile_Gerrit_Postcommit.groovy"
        def preCommitScript = "${pipelinePath}/Jenkinsfile_Gerrit_Precommit.groovy"

        gerritPostCommit(appName, gerProject, postCommitScript, pipelinePath, devopsRepo, serviceType)
        gerritPreCommit(appName, gerProject, preCommitScript, pipelinePath, devopsRepo)

    }
}