# Annotations and Labels

EPAM Delivery Platform uses labels to interact with various resources in a Kubernetes cluster. This guide details the resources, annotations, and labels used by the platform to streamline operations, enhance monitoring, and enforce governance.

## Labels

The table below contains all the labels used in EDP:

| Label Key                           | Target Resources     | Possible Values                                                         | Description                                                               |
|:------------------------------------|:---------------------|:------------------------------------------------------------------------|:--------------------------------------------------------------------------|
| app.edp.epam.com/secret-type        | Secrets              | `jira`, `nexus`, `sonar`, `defectdojo`, `dependency-track`,`repository` | Identifies the type of the secret.                                        |
| app.edp.epam.com/integration-secret | Secrets              | `true`                                                                  | Indicates if the secret is used for integration.                          |
| app.edp.epam.com/codebase           | PipelineRun          | `<codebase_name>`                                                       | Identifies the codebase associated with the PipelineRun.                  |
| app.edp.epam.com/codebasebranch     | PipelineRun          | `<codebase_name>-<branch_name>`                                         | Identifies the codebase branch associated with the PipelineRun.           |
| app.edp.epam.com/pipeline           | PipelineRun, Taskrun | `<environment_name>`                                                    | Used by the EDP Portal to display autotests status(on Deploy environment) |
| app.edp.epam.com/pipelinetype       | PipelineRun, Taskrun | `autotestRunner`, `build`, `review`                                     | Identifies the type of the Pipeline.                                      |
| app.edp.epam.com/parentPipelineRun  | PipelineRun          | `<cd-pipeline-autotest-runner-name>`                                    | Used by the EDP Portal to display autotests status(on Deploy environment) |
| app.edp.epam.com/stage              | PipelineRun, Taskrun | `<stage_name>`                                                          | Used by the EDP Portal to display autotests status(on Deploy environment) |
| app.edp.epam.com/branch             | PipelineRun          | `<branch_name>`                                                         | Identifies the branch associated with the PipelineRun.                    |
| app.edp.epam.com/codebaseType       | Codebase             | `system`,`application`                                                  | Identify the type of the codebase.                                        |
| app.edp.epam.com/systemType         | Codebase             | `gitops`                                                                | Identify system repositories.                                             |
| app.edp.epam.com/gitServer          | Ingress              | `<gitServer_name>`                                                      | Identifies the ingress associated with the GitServer.                     |

### Labels Usage in Secrets

The table below shows what labels are used by specific secrets:

| Secret Name          | Labels                                                                                     |
| :------------------- | :----------------------------------------------------------------------------------------- |
| ci-argocd            | app.edp.epam.com/integration-secret=true<br> app.edp.epam.com/secret-type=argocd           |
| ci-defectdojo        | app.edp.epam.com/integration-secret=true<br> app.edp.epam.com/secret-type=defectdojo       |
| ci-dependency-track  | app.edp.epam.com/integration-secret=true<br> app.edp.epam.com/secret-type=dependency-track |
| ci-jira              | app.edp.epam.com/secret-type=jira                                                          |
| ci-nexus             | app.edp.epam.com/integration-secret=true<br> app.edp.epam.com/secret-type=nexus            |
| ci-sonarqube         | app.edp.epam.com/integration-secret=true<br> app.edp.epam.com/secret-type=sonar            |
| gerrit-ciuser-sshkey | app.edp.epam.com/secret-type=repository                                                    |
| kaniko-docker-config | app.edp.epam.com/integration-secret=true<br> app.edp.epam.com/secret-type=registry         |
| regcred              | app.edp.epam.com/integration-secret=true<br> app.edp.epam.com/secret-type=registry         |

### Labels Usage in Tekton Pipeline Runs

The table below displays what labels are used in specific Tekton pipelines:

| PipelineRun              | Labels                                                                                                                                                                                                                                      |
| :----------------------- | :------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------ |
| review-pipeline          | app.edp.epam.com/codebase: `<codebase_name>` <br> app.edp.epam.com/codebasebranch: `<codebase_name>`-`<branch_name>`<br> app.edp.epam.com/pipelinetype: `review`                                                                            |
| build-pipeline           | app.edp.epam.com/codebase: `<codebase_name>` <br> app.edp.epam.com/codebasebranch: `<codebase_name>`-`<branch_name>`<br> app.edp.epam.com/pipelinetype: `build`                                                                             |
| autotest-runner-pipeline | app.edp.epam.com/pipeline: `<pipeline_name>`<br> app.edp.epam.com/pipelinetype: `autotestRunner` <br> app.edp.epam.com/stage: `<stage>`                                                                                                     |
| autotest-pipeline        | app.edp.epam.com/branch: `<branch>`<br> app.edp.epam.com/codebase: `<codebase_name>`<br> app.edp.epam.com/parentPipelineRun: `<cd_pipeline>`-`<stage>`<br> app.edp.epam.com/pipeline: `<cd_pipeline>`<br> app.edp.epam.com/stage: `<stage>` |

### Pipeline Usage Example

To demonstrate label usage in the EDP Tekton pipelines, find below some EDP resource examples:

  ```yaml title="Codebase specification"
  metadata:
    ...
    name: demo
    ...
  spec:
    ...
    defaultBranch: main
    type: application
    framework: react
    lang: javascript
    ...
  ```

  ```yaml title="CD Pipeline specification"
  spec:
    ...
    applications:
      - demo
    inputDockerStreams:
      - demo-main
    name: mypipe
    ...
  ```

  ```yaml title="Stage specification"
  spec:
    ...
    cdPipeline: mypipe
    name: dev
    namespace: edp-delivery-ms-delivery-dev-mypipe-dev
    qualityGates:
      - autotestName: autotests
        branchName: master
        qualityGateType: autotests
        stepName: autotest
    ...
  ```

The table below shows all the pipelines associated with the `demo` codebase:

| Pipeline Name                      | Type   | Labels                                                                                                                                                                                                    |
| :--------------------------------- | :----- | :-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| gerrit-npm-react-app-review        | Review | app.edp.epam.com/codebase: demo<br> app.edp.epam.com/codebasebranch: demo-main<br> app.edp.epam.com/pipelinetype: review                                                                                  |
| gerrit-npm-react-app-build-default | Build  | app.edp.epam.com/codebase: demo<br> app.edp.epam.com/codebasebranch: demo-main<br> app.edp.epam.com/pipelinetype: build                                                                                   |
| autotest-runner                    | Deploy | app.edp.epam.com/pipeline: mypipe<br> app.edp.epam.com/pipelinetype: autotestRunner<br> app.edp.epam.com/stage: dev                                                                                       |
| autotests-gradle, autotests-maven  | Deploy | app.edp.epam.com/branch: master<br> app.edp.epam.com/codebase: autotests<br> app.edp.epam.com/parentPipelineRun: mypipe-dev-<hash\><br> app.edp.epam.com/pipeline: mypipe<br> app.edp.epam.com/stage: dev |

The list of all the tasks associated with the `demo` codebase is presented below:

| Task Name          | Labels                                                                                                                                                                                                    |
| :----------------- | :-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| init-autotest      | app.edp.epam.com/pipeline: mypipe<br> app.edp.epam.com/pipelinetype: autotestRunner<br> app.edp.epam.com/stage: dev                                                                                       |
| run-autotest       | app.edp.epam.com/branch: master<br> app.edp.epam.com/codebase: autotests<br> app.edp.epam.com/parentPipelineRun: mypipe-dev-<hash\><br> app.edp.epam.com/pipeline: mypipe<br> app.edp.epam.com/stage: dev |
| wait-for-autotests | app.edp.epam.com/pipeline: mypipe<br> app.edp.epam.com/pipelinetype: autotestRunner<br> app.edp.epam.com/stage: dev                                                                                       |
| promote-images     | app.edp.epam.com/pipeline: mypipe<br> app.edp.epam.com/pipelinetype: autotestRunner<br> app.edp.epam.com/stage: dev                                                                                       |
