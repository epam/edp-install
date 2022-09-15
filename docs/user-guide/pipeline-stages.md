# Pipeline Stages

Get acquainted with EDP CI/CD workflow and stages description.

## EDP CI/CD Workflow

Within EDP, the pipeline framework comprises the following pipelines:

* Code Review;
* Build;
* Deploy.

!!! note
    Please refer to the [EDP Pipeline Framework](pipeline-framework.md) page for details.

The diagram below shows the delivery path through these pipelines and the respective stages. Please be aware that stages may differ for different codebase types.

!![stages](http://www.plantuml.com/plantuml/proxy?src=https://raw.githubusercontent.com/epam/edp-install/master/docs/user-guide/stages.puml)

## Stages Description

The table below provides the details on all the stages in the EDP pipeline framework:

| Name | Dependency  | Description  | Pipeline  | Application | Library | Autotest | Source code | Documentation |
|---|---|---|---|---|---|---|---|---|
| init |   | Initiates information gathering | Create Release, Code Review, Build | + | + |  | [Build.groovy](https://github.com/epam/edp-library-pipelines/blob/master/vars/Build.groovy)  | |
| checkout |  | Performs for all files the checkout from a selected branch of the Git repository. For the main branch - from HEAD, for code review - from the commit | Create Release, Build | + |+ | | [Checkout.groovy](https://github.com/epam/edp-library-stages/blob/master/src/com/epam/edp/stages/impl/ci/impl/checkout/Checkout.groovy) | |
| sast | | Launches vulnerability testing via Semgrep scanner. Pushes a vulnerability report to the DefectDojo. | Build | + | | | [Security](https://github.com/epam/edp-library-stages/tree/master/src/com/epam/edp/stages/impl/ci/impl/security) | |
| compile| | Compiles the code, includes individual groovy files for each type of app or lib (NPM, DotNet, Python, Maven, Gradle) | Code Review, Build | + | + | | [Compile](https://github.com/epam/edp-library-stages/tree/master/src/com/epam/edp/stages/impl/ci/impl/compile) | |
| tests | | Launches testing procedure, includes individual groovy files for each type of app or lib | Code Review, Build | + | + | + | [Tests](https://github.com/epam/edp-library-stages/tree/master/src/com/epam/edp/stages/impl/ci/impl/tests) | |
| sonar | | Launches testing via SonarQube scanner and includes individual groovy files for each type of app or lib | Code Review, Build | + | + | | [Sonar](https://github.com/epam/edp-library-stages/tree/master/src/com/epam/edp/stages/impl/ci/impl/sonar) | |
| build | | Builds the application, includes individual groovy files for each type of app or lib (Go, Maven, Gradle, NPM) | Code Review, Build | + | | | [Build](https://github.com/epam/edp-library-stages/tree/master/src/com/epam/edp/stages/impl/ci/impl/build) | |
| create-branch | EDP create-release process | Creates default branch in Gerrit during create and clone strategies| Create Release| + | + | + | [CreateBranch.groovy](https://github.com/epam/edp-library-stages/blob/master/src/com/epam/edp/stages/impl/ci/impl/createbranch/CreateBranch.groovy) | |
| trigger-job | EDP create-release process | Triggers "build" job | Create Release | + | + | + | [TriggerJob.groovy](https://github.com/epam/edp-library-stages/blob/master/src/com/epam/edp/stages/impl/ci/impl/triggerjob/TriggerJob.groovy) | |
| gerrit-checkout | | Performs checkout to the current project branch in Gerrit | Code Review | + | + | + | [GerritCheckout.groovy](https://github.com/epam/edp-library-stages/blob/master/src/com/epam/edp/stages/impl/ci/impl/gerritcheckout/GerritCheckout.groovy) | |
| commit-validate | Optional in EDP Admin Console | Takes Jira parameters, when "Jira Integration" is enabled for the project in the Admin Console. | Code Review | + | + | | [CommitValidate.groovy](https://github.com/epam/edp-library-stages/blob/master/src/com/epam/edp/stages/impl/ci/impl/commitvalidate/CommitValidate.groovy) | |
| dockerfile-lint | | Launches linting tests for Dockerfile | Code Review | + | | | [LintDockerApplicationLibrary.groovy](https://github.com/epam/edp-library-stages/blob/master/src/com/epam/edp/stages/impl/ci/impl/lint/LintDockerApplicationLibrary.groovy) | [Use Dockerfile Linters for Code Review](dockerfile-stages.md) |
| dockerbuild-verify | "Build" stage (if there are no "COPY" layers in Dockerfile) |Launches build procedure for Dockerfile without pushing an image to the repository | Code Review | + | | | [BuildDockerfileApplicationLibrary.groovy](https://github.com/epam/edp-library-stages/blob/master/src/com/epam/edp/stages/impl/ci/impl/lint/LintHelmApplicationLibrary.groovy) | [Use Dockerfile Linters for Code Review](dockerfile-stages.md) |
| helm-lint | | Launches linting tests for deployment charts | Code Review | + | | | [LintHelmApplicationLibrary.groovy](https://github.com/epam/edp-library-stages/blob/master/src/com/epam/edp/stages/impl/ci/impl/lint/LintHelmApplicationLibrary.groovy) | [Use helm-lint for Code Review](helm-stages.md) |
| helm-docs | | Checks generated documentation for deployment charts | Code Review | + | | | [HelmDocsApplication.groovy](https://github.com/epam/edp-library-stages/blob/master/src/com/epam/edp/stages/impl/ci/impl/lint/HelmDocsApplication.groovy) | [Use helm-docs for Code Review](helm-stages.md) |
| helm-uninstall | | Helm release deletion step to clear Helm releases | Deploy | + | | | [HelmUninstall.groovy](https://github.com/epam/edp-library-stages/tree/master/src/com/epam/edp/stages/impl/cd/impl/HelmUninstall.groovy) | [Helm release deletion](helm-release-deletion.md) |
| semi-auto-deploy-input | | Provides auto deploy with timeout and manual deploy flow | Deploy | + | | | [SemiAutoDeployInput.groovy](https://github.com/epam/edp-library-stages/tree/master/src/com/epam/edp/stages/impl/cd/impl/SemiAutoDeployInput.groovy) | [Semi Auto Deploy](semi-auto-deploy.md) |
| get-version | | Defines the versioning of the project depending on the versioning schema selected in Admin Console | Build | + | + | | [GetVersion](https://github.com/epam/edp-library-stages/tree/master/src/com/epam/edp/stages/impl/ci/impl/getversion) | |
| terraform-plan | AWS credentials added to Jenkins | Checks Terraform version, and installs default version if necessary, and launches terraform init, returns AWS username which used for action, and terraform plan command is called with an output of results to .tfplan file | Build | | + | | [TerraformPlan.groovy](https://github.com/epam/edp-library-stages/blob/master/src/com/epam/edp/stages/impl/ci/impl/iac/TerraformPlan.groovy) | [Use Terraform library in EDP](terraform-stages.md)|
| terraform-apply | AWS credentials added to Jenkins, the "Terraform-plan" stage | Checks Terraform version, and installs default version if necessary, and launches terraform init, launches terraform plan from saves before .tfplan file, asks to approve, and run terraform apply from .tfplan file         | Build | | + | | [TerraformApply.groovy](https://github.com/epam/edp-library-stages/blob/master/src/com/epam/edp/stages/impl/ci/impl/iac/TerraformApply.groovy) | [Use Terraform library in EDP](terraform-stages.md) |
| build-image-from-dockerfile | Platform: OpenShift | Builds Dockerfile                                                                                                                                                                                                            | Build | + | | + | [.groovy files for building Dockerfile image](https://github.com/epam/edp-library-stages/tree/master/src/com/epam/edp/stages/impl/ci/impl/builddockerfileimage) | |
| build-image-kaniko | Platform: k8s | Builds Dockerfile using the Kaniko tool                                                                                                                                                                                      | Build | + | | | [BuildImageKaniko.groovy](https://github.com/epam/edp-library-stages/blob/master/src/com/epam/edp/stages/impl/ci/impl/buildimagekaniko/BuildImageKaniko.groovy) | |
| push | | Pushes an artifact to the Nexus repository                                                                                                                                                                                   | Build | + | + | | [Push](https://github.com/epam/edp-library-stages/tree/master/src/com/epam/edp/stages/impl/ci/impl/push) | |
| create-Jira-issue-metadata | "get-version" stage | Creates a temporary CR in the namespace and after that pushes Jira Integration data to Jira ticket, and delete CR                                                                                                            | Build | + | + | | [JiraIssueMetadata.groovy](https://github.com/epam/edp-library-stages/blob/master/src/com/epam/edp/stages/impl/ci/impl/jiraissuemetadata/JiraIssueMetadata.groovy) | |
| ecr-to-docker | DockerHub credentials added to Jenkins | Copies the docker image from the ECR project registry to DockerHub via the Crane tool after it is built                                                                                                                      | Build | + | | | [EcrToDocker.groovy](https://github.com/epam/edp-library-stages/blob/master/src/com/epam/edp/stages/impl/ci/impl/ecrtodocker/EcrToDocker.groovy) | [Promote Docker Images From ECR to Docker Hub](ecr-to-docker-stages.md) |
| git-tag | "Get-version" stage | Creates a tag in SCM for the current build                                                                                                                                                                                   | Build | + | + | | [GitTagApplicationLibrary.groovy](https://github.com/epam/edp-library-stages/blob/master/src/com/epam/edp/stages/impl/ci/impl/gittag/GitTagApplicationLibrary.groovy) | |
| deploy | | Deploys the application                                                                                                                                                                                                      | Deploy | + | | | [Deploy.groovy](https://github.com/epam/edp-library-stages/blob/master/src/com/epam/edp/stages/impl/cd/impl/Deploy.groovy) | |
| manual | | Works with the manual approve to proceed                                                                                                                                                                                     | Deploy | + | | | [ManualApprove.groovy](https://github.com/epam/edp-library-stages/blob/master/src/com/epam/edp/stages/impl/cd/impl/ManualApprove.groovy) | |
| promote-images | | Promotes docker images to the registry                                                                                                                                                                                       | Deploy | + | | | [PromoteImage.groovy](https://github.com/epam/edp-library-stages/blob/master/src/com/epam/edp/stages/impl/cd/impl/PromoteImages.groovy) | |

!!! note
    The Create Release pipeline is an internal EDP mechanism for adding, importing or cloning a codebase. It is not a part of the pipeline framework.

### Related Articles

* [Manage Jenkins CI Job Provisioner](../operator-guide/manage-jenkins-ci-job-provision.md)
* [GitLab Integration](../operator-guide/gitlab-integration.md)
* [GitHub Integration](../operator-guide/github-integration.md)

