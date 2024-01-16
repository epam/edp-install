# Annotations and Labels used by EDP

The EDP uses labels to interact with various resources in a Kubernetes cluster. This guide details the resources, annotations and labels used by the platform to streamline operations, enhance monitoring, and enforce governance.

## Labels

| Labels key                          | Target resource(es)  | Possible values                                                         | Description                                                                  |
| :---------------------------------- | :------------------- | :---------------------------------------------------------------------- | :--------------------------------------------------------------------------- |
| app.edp.epam.com/secret-type        | Secrets              | `jira`, `nexus`, `sonar`, `defectdojo`, `dependency-track`,`repository` | Identifies the type of the secret.                                           |
| app.edp.epam.com/integration-secret | Secrets              | `true`                                                                  | Indicates if the secret is used for integration.                             |
| app.edp.epam.com/codebase           | Pipelinerun          | `<codebase_name>`                                                       | Identifies the codebase associated with the PipelineRun.                     |
| app.edp.epam.com/codebasebranch     | Pipelinerun          | `<codebase_name>-<branch_name>`                                         | Identifies the codebase branch associated with the PipelineRun.              |
| app.edp.epam.com/pipeline           | Pipelinerun, Taskrun | `<environment_name>`                                                    | Used by the EDP portal to to display autotests status(on Deploy environment) |
| app.edp.epam.com/pipelinetype       | Pipelinerun, Taskrun | `autotestRunner`, `build`, `review`                                     | Identifies the type of the Pipeline.                                         |
| app.edp.epam.com/parentPipelineRun  | Pipelinerun          | `<cd-pipeline-autotest-runner-name>`                                    | Used by the EDP portal to to display autotests status(on Deploy environment) |
| app.edp.epam.com/stage              | Pipelinerun, Taskrun | `<stage_name>`                                                          | Used by the EDP portal to to display autotests status(on Deploy environment) |
| app.edp.epam.com/branch             | Pipelinerun          | `<branch_name>`                                                         | Identifies the branch associated with the PipelineRun.                       |



pipeline
pipelinetype in headlamp for search task run for QG


parentPipelineRun stream autotest pipeline run (list of autotest pipelinerun in headlamp )
stage
pipeline


### Labels usage in Secrets

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



### Labels usage in Tekton Pipeline Runs

| PipelineRun              | Labels                                                                                                                                                                                                                                      |
| :----------------------- | :------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------ |
| review-pipeline          | app.edp.epam.com/codebase: `<codebase_name>` <br> app.edp.epam.com/codebasebranch: `<codebase_name>`-`<branch_name>`<br> app.edp.epam.com/pipelinetype: `rewiew`                                                                            |
| build-pipeline           | app.edp.epam.com/codebase: `<codebase_name>` <br> app.edp.epam.com/codebasebranch: `<codebase_name>`-`<branch_name>`<br> app.edp.epam.com/pipelinetype: `build`                                                                             |
| autotest-runner-pipeline | app.edp.epam.com/pipeline: `<pipeline_name>`<br> app.edp.epam.com/pipelinetype: `autotestRunner` <br> app.edp.epam.com/stage: `<stage>`                                                                                                     |
| autotest-pipeline        | app.edp.epam.com/branch: `<branch>`<br> app.edp.epam.com/codebase: `<codebase_name>`<br> app.edp.epam.com/parentPipelineRun: `<cd_pipeline>`-`<stage>`<br> app.edp.epam.com/pipeline: `<cd_pipeline>`<br> app.edp.epam.com/stage: `<stage>` |

### Pipeline usage example

To demonstrate label usage in EDP Tekton pipelines, here are a several simple EDP resources examples:

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

Pipelines associated to `demo` codebase:

| Pipeline Name                      | Type   | Labels                                                                                                                                                                                                    |
| :--------------------------------- | :----- | :-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| gerrit-npm-react-app-review        | Rewiew | app.edp.epam.com/codebase: demo<br> app.edp.epam.com/codebasebranch: demo-main<br> app.edp.epam.com/pipelinetype: rewiew                                                                                  |
| gerrit-npm-react-app-build-default | Build  | app.edp.epam.com/codebase: demo<br> app.edp.epam.com/codebasebranch: demo-main<br> app.edp.epam.com/pipelinetype: build                                                                                   |
| autotest-runner                    | Deploy | app.edp.epam.com/pipeline: mypipe<br> app.edp.epam.com/pipelinetype: autotestRunner<br> app.edp.epam.com/stage: dev                                                                                       |
| autotests-gradle, autotests-maven  | Deploy | app.edp.epam.com/branch: master<br> app.edp.epam.com/codebase: autotests<br> app.edp.epam.com/parentPipelineRun: mypipe-dev-<hash\><br> app.edp.epam.com/pipeline: mypipe<br> app.edp.epam.com/stage: dev |

Tasks associated to `demo` codebase:

| Task Name          | Labels                                                                                                                                                                                                    |
| :----------------- | :-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| init-autotest      | app.edp.epam.com/pipeline: mypipe<br> app.edp.epam.com/pipelinetype: autotestRunner<br> app.edp.epam.com/stage: dev                                                                                       |
| run-autotest       | app.edp.epam.com/branch: master<br> app.edp.epam.com/codebase: autotests<br> app.edp.epam.com/parentPipelineRun: mypipe-dev-<hash\><br> app.edp.epam.com/pipeline: mypipe<br> app.edp.epam.com/stage: dev |
| wait-for-autotests | app.edp.epam.com/pipeline: mypipe<br> app.edp.epam.com/pipelinetype: autotestRunner<br> app.edp.epam.com/stage: dev                                                                                       |
| promote-images     | app.edp.epam.com/pipeline: mypipe<br> app.edp.epam.com/pipelinetype: autotestRunner<br> app.edp.epam.com/stage: dev                                                                                       |
