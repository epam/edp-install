
# EDP API

## Create Codebase Entity

EDP allows you to create three codebase types: Application, Autotest and Library. There are also several strategy types for each codebase: Create, Clone and Import.
Depending on the selected codebase type and the respective strategy, you should specify a different set of fields in a request.

!!! note
    The Route, Database and VCS are optional fields. In accordance with the necessary deploy set, you have to add the necessary fields into request._

### Request

`POST /api/v1/edp/codebase`

### Application (Create)

    {
        "name": "app01",
        "type": "application",
        "strategy": "create",
        "lang": "java",
        "framework": "springboot",
        "buildTool": "maven",
        "multiModule": false,
        "route": {
            "site": "api",
            "path": "/"
        },
        "database": {
            "kind": "postgresql",
            "version": "postgres:9.6",
            "capacity": "1Gi",
            "storage": "efs"
        },
        "description": "Description",
        "gitServer": "gerrit",
        "jenkinsSlave": "maven",
        "jobProvisioning": "default",
        "deploymentScript": "openshift-template"
    }

### Application (Clone)

    {
        "name": "app01",
        "type": "application",
        "strategy": "clone",
        "lang": "java",
        "framework": "springboot",
        "buildTool": "maven",
        "multiModule": false,
        "repository": {
            "url": "http(s)://git.sample.com/sample.git",
            // login and password are required only if repo is private
            "login": "login",
            "password": "password"
        },
        "description": "Description",
        "gitServer": "gerrit",
        "jenkinsSlave": "maven",
        "jobProvisioning": "default",
        "deploymentScript": "openshift-template"
    }

### Application (Import)

    {
        "type": "application",
        "strategy": "import",
        "lang": "java",
        "framework": "springboot",
        "buildTool": "maven",
        "multiModule": false,
        "description": "Description",
        "gitServer": "git-epam",
        "gitUrlPath": "/relative/path/to/repo",
        "jenkinsSlave": "maven",
        "jobProvisioning": "default",
        "deploymentScript": "openshift-template"
    }

### Autotests (Clone)

    {
        "name": "aut01",
        "type": "autotests",
        "strategy": "clone",
        "lang": "java",
        "framework": "springboot",
        "buildTool": "maven",
        "testReportFramework": "allure",
        "repository": {
            "url": "http(s)://git.sample.com/sample.git",
            // login and password are required only if repo is private
            "login": "login",
            "password": "password"
        },
        "description": "Description",
        "jenkinsSlave": "maven",
        "jobProvisioning": "default"
    }

### Autotests (Import)

    {
        "type": "autotests",
        "strategy": "import",
        "lang": "java",
        "framework": "springboot",
        "buildTool": "maven",
        "testReportFramework": "allure",
        "description": "Description",
        "gitServer": "git-epam",
        "gitRelativePath": "/relative/path/to/repo",
        "jenkinsSlave": "maven",
        "jobProvisioning": "default"
    }

### Library (Create)

    {
        "name": "lib01",
        "type": "library",
        "strategy": "create",
        "lang": "java",
        "buildTool": "maven",
        "multiModule": false,
        "jenkinsSlave": "maven",
        "jobProvisioning": "default",
    }

### Library (Clone)

    {
        "name": "lib01",
        "type": "library",
        "strategy": "clone",
        "lang": "java",
        "buildTool": "maven",
        "multiModule": false,
        "repository": {
        "url": "http(s)://git.sample.com/sample.git",
        // login and password are required only if repo is private
        "login": "login",
        "password": "password"
        },
        "vcs": null,
        "jenkinsSlave": "maven",
        "jobProvisioning": "default",
    }

### Library (Import)

    {
        "type": "library",
        "strategy": "import",
        "lang": "java",
        "buildTool": "maven",
        "multiModule": false,
        "gitServer": "git-epam",
        "gitUrlPath": "/relative/path/to/repo",
        "jenkinsSlave": "maven",
        "jobProvisioning": "default",
    }

### Response

    Status 200 OK

## Get Codebase by Name

### Request

`GET /api/v1/edp/codebase/{codebaseName}`

example: `localhost/api/v1/edp/codebase/app01`

### Response

    Status 200 OK
    {
        "id": 1,
        "name": "app01",
        "language": "java",
        "build_tool": "maven",
        "framework": "springboot",
        "strategy": "create",
        "git_url": "",
        "route_site": "api",
        "route_path": "/",
        "type": "application",
        "status": "active",
        "testReportFramework": "",
        "description": "Description",
        "codebase_branch": [
            {
                "id": 1,
                "branchName": "master",
                "from_commit": "",
                "status": "active",
                "branchLink": "",
                "jenkinsLink": "",
                "appName": "",
                "codebaseDockerStream": null
            }
        ],
        "gitServer": "gerrit",
        "gitProjectPath": null,
        "jenkinsSlave": "maven",
        "jobProvisioning": "default",
        "deploymentScript": "openshift-template"
    }

## Get All Codebases

### Request

    GET /api/v1/edp/codebase?type={codebaseType}

    example: localhost/api/v1/edp/codebase?type=application

### Response

       Status 200 OK
       [
           {
               "id": 1,
               "name": "app01",
               "language": "java",
               "build_tool": "maven",
               "framework": "springboot",
               "strategy": "create",
               "git_url": "",
               "route_site": "api",
               "route_path": "/",
               "type": "application",
               "status": "active",
               "testReportFramework": "",
               "description": "Description",
               "codebase_branch": [
                   {
                       "id": 1,
                       "branchName": "master",
                       "from_commit": "",
                       "status": "active",
                       "branchLink": "",
                       "jenkinsLink": "",
                       "appName": "",
                       "codebaseDockerStream": [
                           {
                               "id": 1,
                               "ocImageStreamName": "app01-master",
                               "imageLink": "",
                               "jenkinsLink": ""
                           }
                       ]
                   }
               ],
               "gitServer": "gerrit",
               "gitProjectPath": null,
               "jenkinsSlave": "maven",
               "jobProvisioning": "",
               "deploymentScript": "openshift-template"
           },
           {
               "id": 2,
               "name": "app02",
               "language": "java",
               "build_tool": "maven",
               "framework": "springboot",
               "strategy": "create",
               "git_url": "",
               "route_site": "app",
               "route_path": "/",
               "type": "application",
               "status": "failed",
               "testReportFramework": "",
               "description": "",
               "codebase_branch": [
                   {
                       "id": 2,
                       "branchName": "master",
                       "from_commit": "",
                       "status": "inactive",
                       "branchLink": "",
                       "jenkinsLink": "",
                       "appName": "",
                       "codebaseDockerStream": [
                           {
                               "id": 2,
                               "ocImageStreamName": "app02-master",
                               "imageLink": "",
                               "jenkinsLink": ""
                           }
                       ]
                   }
               ],
               "gitServer": "gerrit",
               "gitProjectPath": null,
               "jenkinsSlave": "maven",
               "jobProvisioning": "",
               "deploymentScript": "openshift-template"
           }
       ]

## Create CD Pipeline Entity

### Request

`POST /api/v1/edp/cd-pipeline`

    {
       "name":"pipe1",
       "applications":[
          {
             "appName":"app01",
             "inputDockerStream":"app01-master"
          }
       ],
       "stages":[
          {
             "name":"sit",
             "description":"description-sit",
             "qualityGateType":"manual",
             "stepName":"approve",
             "triggerType":"manual",
             "order":0,
             "qualityGates": [
                 {
                 "qualityGateType":"manual",
                 "stepName":"step-one-one",
                 "autotestName": null,
                 "branchName": null
                },
                {
                 "qualityGateType":"manual",
                 "stepName":"step-two-two",
                 "autotestName": null,
                 "branchName": null
                }
             ]
          }
       ]
    }

### Response

    Status 200 OK

## Get CD Pipeline Entity by Name

### Request

    GET /api/v1/edp/cd-pipeline/{cdPipelineName}

    example: localhost/api/v1/edp/cd-pipeline/pipe1

### Response

    Status 200 OK
    {
        "id": 1,
        "name": "pipe1",
        "status": "active",
        "jenkinsLink": "",
        "codebaseBranches": [
            {
                "id": 1,
                "branchName": "master",
                "from_commit": "",
                "status": "active",
                "branchLink": "",
                "jenkinsLink": "",
                "appName": "java-springboot-helloworld",
                "codebaseDockerStream": [
                    {
                        "id": 1,
                        "ocImageStreamName": "java-springboot-helloworld-master",
                        "imageLink": "",
                        "jenkinsLink": ""
                    }
                ]
            }
        ],
        "stages": [
            {
                "id": 1,
                "name": "sit",
                "description": "sit",
                "triggerType": "manual",
                "order": 0,
                "platformProjectLink": "",
                "platformProjectName": env-am-test-deploy-sit",
                "qualityGates": [
                    {
                        "id": 1,
                        "qualityGateType": "manual",
                        "stepName": "manual",
                        "cdStageId": 1,
                        "autotest": null,
                        "codebaseBranch": null
                    }
                ],
                "source": {
                    "type": "library",
                    "library": {
                        "name": "lib01",
                        "branch": "master"
                    }
                }
            }
        ],
        "services": [],
        "applicationsToPromote": [
            "java-springboot-helloworld"
        ]
    }

## Get CD Stage Entity by Pipeline and Stage Names

### Request

    GET /api/v1/edp/cd-pipeline/{cdPipelineName}/stage/{stageName}

    example: `localhost/api/v1/edp/cd-pipeline/pipe1/stage/sit

### Response

    {
        "name": "sit",
        "cdPipeline": "pipe1",
        "description": "sit",
        "triggerType": "manual",
        "order": "0",
        "applications": [
            {
                "name": "java-springboot-helloworld",
                "branchName": "master",
                "inputIs": "java-springboot-helloworld-master",
                "outputIs": "am-test-deploy-sit-java-springboot-helloworld-verified"
            }
        ],
        "qualityGates": [
            {
                "id": 1,
                "qualityGateType": "manual",
                "stepName": "manual",
                "cdStageId": 1,
                "autotest": null,
                "codebaseBranch": null
            }
        ]
    }

## Update CD Pipeline Entity

### Request

    PUT /api/v1/edp/cd-pipeline/{cdPipelineName}/update

    example: localhost/api/v1/edp/cd-pipeline/pipe1/update

### Change Set of Applications

    {
        "applications":[
          {
             "appName":"app01",
             "inputDockerStream":"app01-master"
          },
          {
             "appName":"app02",
             "inputDockerStream":"app02"
          }
        ]
    }

### Response

    204 No Content

### Change Set of Stages

    {
        "stages": [
            {
                "name": "sit",
                "description": "sit",
                "triggerType": "manual",
                "order": 0,
                "platformProjectLink": "",
                "platformProjectName": env-deploy-sit",
                "qualityGates": [
                    {
                        "id": 1,
                        "qualityGateType": "manual",
                        "stepName": "manual",
                        "cdStageId": 1,
                        "autotest": null,
                        "codebaseBranch": null
                    }
                ],
                "source": {
                    "type": "library",
                    "library": {
                        "name": "lib01",
                        "branch": "master"
                    }
                }
            }
        ]
    }

### Response

    204 No Content
