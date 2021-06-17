# Pipeline Stages

Get acquainted with EDP CI/CD workflow and stages description.

## EDP CI/CD Workflow

Within EDP, the pipeline framework comprises the following pipelines:

* Code Review;
* Build;
* Deploy.

!!! note
    Please refer to the [EDP Pipeline Framework](pipeline-framework.md) page for details.

The diagram below shows the commit path through these pipelines and the respective stages.

![stages](http://www.plantuml.com/plantuml/proxy?src=https://raw.githubusercontent.com/epam/edp-install/master/docs/user-guide/stages.puml)

## Stages Description

The table below provides the details on all the stages in the EDP pipeline framework:

| Name  | Dependency  | Description  | Pipeline  | Application | Library | Autotest | Source code | Documentation |
|---|---|---|---|---|---|---|---|---|
| init |   | Initiates information gathering | Create Release, Code Review, Build | + | + |  | [Build.groovy](https://github.com/epam/edp-library-pipelines/blob/master/vars/Build.groovy)  | |
| checkout |  | Performs for all files the checkout from a selected branch of the Git repository. For the main branch - from HEAD, for code review - from the commit | Create Release, Build | + |+ | | [Checkout.groovy](https://github.com/epam/edp-library-stages/blob/master/src/com/epam/edp/stages/impl/ci/impl/checkout/Checkout.groovy) | |
| compile| | Compiles the code, includes individual groovy files for each type of app or lib (NPM, DotNet, Python, Maven, Gradle) | Code Review, Build | + | + | | [Compile](https://github.com/epam/edp-library-stages/tree/master/src/com/epam/edp/stages/impl/ci/impl/compile) | |
| tests | | Launches testing procedure, includes individual groovy files for each type of app or lib | Code Review, Build | + | + | + | [Tests](https://github.com/epam/edp-library-stages/tree/master/src/com/epam/edp/stages/impl/ci/impl/tests) | |
| sonar | | Launches testing via SonarQube scanner and includes individual groovy files for each type of app or lib | Code Review, Build | + | + | | [Sonar](https://github.com/epam/edp-library-stages/tree/master/src/com/epam/edp/stages/impl/ci/impl/sonar) | |
| build | | Builds the application, includes individual groovy files for each type of app or lib (Go, Maven, Gradle, NPM) | Code Review, Build | + | | | [Build](https://github.com/epam/edp-library-stages/tree/master/src/com/epam/edp/stages/impl/ci/impl/build) | |
| create-branch | EDP create-release process | Creates default branch in Gerrit during create and clone strategies| Create Release| + | + | + | [CreateBranch.groovy](https://github.com/epam/edp-library-stages/blob/master/src/com/epam/edp/stages/impl/ci/impl/createbranch/CreateBranch.groovy) | |
| trigger-job | EDP create-release process | Triggers "build" job | Create Release | + | + | + | [TriggerJob.groovy](https://github.com/epam/edp-library-stages/blob/master/src/com/epam/edp/stages/impl/ci/impl/triggerjob/TriggerJob.groovy) | |
| gerrit-checkout | | Performs checkout to the current project branch in Gerrit | Code Review | + | + | + | [GerritCheckout.groovy](https://github.com/epam/edp-library-stages/blob/master/src/com/epam/edp/stages/impl/ci/impl/gerritcheckout/GerritCheckout.groovy) | |
| commit-validate | Optional in EDP Admin Console | Takes Jira parameters, when "Jira Integration" is enabled for the project in the Admin Console. | Code Review | + | + | | [CommitValidate.groovy](https://github.com/epam/edp-library-stages/blob/master/src/com/epam/edp/stages/impl/ci/impl/commitvalidate/CommitValidate.groovy) | |
| dockerfile-lint | | Launches linting tests for Dockerfile | Code Review | + | | | [LintDockerApplicationLibrary.groovy](https://github.com/epam/edp-library-stages/blob/master/src/com/epam/edp/stages/impl/ci/impl/lint/LintDockerApplicationLibrary.groovy) | [Use lint stages for code review](https://github.com/epam/edp-admin-console/blob/master/documentation/cicd_customization/code_review_stages.md) |
| dockerbuild-verify | "Build" stage (if there are no "COPY" layers in Dockerfile) |Launches build procedure for Dockerfile without pushing an image to the repository | Code Review | + | | | [BuildDockerfileApplicationLibrary.groovy](https://github.com/epam/edp-library-stages/blob/master/src/com/epam/edp/stages/impl/ci/impl/lint/LintHelmApplicationLibrary.groovy) | [Use lint stages for code review](https://github.com/epam/edp-admin-console/blob/master/documentation/cicd_customization/code_review_stages.md) |
| helm-lint | | Launches linting tests for deployment charts | Code Review | + | | | [LintHelmApplicationLibrary.groovy](https://github.com/epam/edp-library-stages/blob/master/src/com/epam/edp/stages/impl/ci/impl/lint/LintHelmApplicationLibrary.groovy) | [Use lint stages for code review](https://github.com/epam/edp-admin-console/blob/master/documentation/cicd_customization/code_review_stages.md) |
| get-version | | Defines the versioning of the project depending on the versioning schema selected in Admin Console | Build | + | + | | [GetVersion](https://github.com/epam/edp-library-stages/tree/master/src/com/epam/edp/stages/impl/ci/impl/getversion) | |
| terraform-plan | AWS credentials added to Jenkins | Checks Terraform version, and installs default version if necessary, and launches terraform init, returns AWS username which used for action, and terraform plan command is called with an output of results to .tfplan file | Build | | + | | [TerraformPlan.groovy](https://github.com/epam/edp-library-stages/blob/master/src/com/epam/edp/stages/impl/ci/impl/iac/TerraformPlan.groovy) | [Use Terraform library in EDP](https://github.com/epam/edp-admin-console/blob/master/documentation/cicd_customization/terraform_stages.md)|
| terraform-apply | AWS credentials added to Jenkins, the "Terraform-plan" stage | Checks Terraform version, and installs default version if necessary, and launches terraform init, launches terraform plan from saves before .tfplan file, asks to approve, and run terraform apply from .tfplan file | Build | | + | | [TerraformApply.groovy](https://github.com/epam/edp-library-stages/blob/master/src/com/epam/edp/stages/impl/ci/impl/iac/TerraformApply.groovy) | [Use Terraform library in EDP](https://github.com/epam/edp-admin-console/blob/master/documentation/cicd_customization/terraform_stages.md) |
| build-image-from-dockerfile | Platform: OpenShift | Builds Dockerfile | Build | + | | + | [.groovy files for building Dockerfile image](https://github.com/epam/edp-library-stages/tree/master/src/com/epam/edp/stages/impl/ci/impl/builddockerfileimage) | |
| build-image-kaniko | Platform: k8s | Builds Dockerfile using the Kaniko tool | Build | + | | | [BuildImageKaniko.groovy](https://github.com/epam/edp-library-stages/blob/master/src/com/epam/edp/stages/impl/ci/impl/buildimagekaniko/BuildImageKaniko.groovy) | |
| push | | Pushes an artifact to the Nexus repository | Build | + | + | | [Push](https://github.com/epam/edp-library-stages/tree/master/src/com/epam/edp/stages/impl/ci/impl/push) | |
| create-Jira-issue-metadata | "get-version" stage | Creates a temporary CR in the namespace and after that pushes Jira Integration data to Jira ticket, and delete CR | Build | + | + | | [JiraIssueMetadata.groovy](https://github.com/epam/edp-library-stages/blob/master/src/com/epam/edp/stages/impl/ci/impl/jiraissuemetadata/JiraIssueMetadata.groovy) | |
| ecr-to-docker | DockerHub credentials added to Jenkins | Copies the docker image from the ECR project registry to DockerHub via the Crane tool after it is built | Build | + | | | [EcrToDocker.groovy](https://github.com/epam/edp-library-stages/blob/master/src/com/epam/edp/stages/impl/ci/impl/ecrtodocker/EcrToDocker.groovy) | [Promote Docker images from ECR to Docker hub](https://github.com/epam/edp-admin-console/blob/master/documentation/cicd_customization/ecr_to_docker_stage.md) |
| git-tag | "Get-version" stage | Creates a tag in SCM for the current build | Build | + | + | | [GitTagApplicationLibrary.groovy](https://github.com/epam/edp-library-stages/blob/master/src/com/epam/edp/stages/impl/ci/impl/gittag/GitTagApplicationLibrary.groovy) | |
| deploy | | Deploys the application | Deploy | + | | | [Deploy.groovy](https://github.com/epam/edp-library-stages/blob/master/src/com/epam/edp/stages/impl/cd/impl/Deploy.groovy) | |
| manual | | Works with the manual approve to proceed | Deploy | + | | | [ManualApprove.groovy](https://github.com/epam/edp-library-stages/blob/master/src/com/epam/edp/stages/impl/cd/impl/ManualApprove.groovy) | |
| promote-images | | Promotes docker images to the registry | Deploy | + | | | [PromoteImage.groovy](https://github.com/epam/edp-library-stages/blob/master/src/com/epam/edp/stages/impl/cd/impl/PromoteImages.groovy) | |
| promote-images-ecr | | Promotes docker images to ECR | Deploy | + | | | [PromoteImagesECR.groovy](https://github.com/epam/edp-library-stages/blob/master/src/com/epam/edp/stages/impl/cd/impl/PromoteImagesECR.groovy) | |

!!! note
    The Create Release pipeline is an internal EDP mechanism for adding, importing or cloning a codebase. It is not a part of the pipeline framework.

## Related Articles

* [Add Job Provisioner](https://github.com/epam/edp-jenkins-operator/blob/master/documentation/add-job-provision.md)
* [GitLab Integration](../operator-guide/gitlab-integration.md)
* [GitHub Integration](../operator-guide/github-integration.md)
* [Job Provisions for GCP Issues](https://github.com/epam/edp-jenkins-operator/blob/master/documentation/internal/job-provisions/ci/job_provisions_gcp.md)
