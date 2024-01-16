# Deploy Application

Now, proceed to deploy our first application. This page provides comprehensive instructions on creating an environment and deploying the application within it.

## Create GitOps Repository

As a prerequisite, create a GitOps repository in your GitHub account. EDP Portal adheres to the GitOps approach when working with environments. In a GitOps repository, values are saved to redefine the default behavior (parameters) of deployment for each environment. The creation of a GitOps repository involves the following two steps:

1. In EDP Portal, navigate to `EDP` -> `Configuration` -> `GitOps` and click the **+ Add GitOps Repository** button:

  !![GitOps tab](../assets/quick-start/gitops_section.png "GitOps tab")

2. Define the following values and click **Save**:

  * Git server: `github`<br>
  * Git repo relative path: `github_account_name`<br>
  * Repository Name: `edp-gitops`

  !![Add GitOps repository](../assets/quick-start/add_gitops.png "Add GitOps repository")

## Create Environment

To create an environment, follow the steps below:

1. In EDP Portal, navigate to `EDP` -> `Environments` and click the **+ Create** button:

  !![Environments section](../assets/quick-start/create_environment.png "Environments section")

2. In the **Create CD Pipeline** window, enter the pipeline name and click the **Proceed** button:

  !![Pipelines tab](../assets/quick-start/pipelines_tab.png "Pipelines tab")

3. In the **Applications** tab, select the go-application and main branch:

  !![Applications tab](../assets/quick-start/applications_tab.png "Applications tab")

4. In the **Stages** tab, click the **Add Stage** button.

5. Define the following values and click **Apply**:

  * Cluster: `in-cluster`<br>
  * Stage name: `dev`<br>
  * Namespace: `edp-my-go-gin-app-dev`<br>
  * Description: `Development stage`<br>
  * Trigger type: `Manual`<br>
  * Quality gate type: `Manual`<br>
  * Step name: `dev`

  !![Create Stage window](../assets/quick-start/stages_tab.png "Create Stage window")

6. In the **Stages** tab, click the **Apply** button.

## Application Deployment

To Deploy application, follow the steps below:

1. In the Environments list, click the Environment name:

  !![Environments list](../assets/quick-start/environment_list.png "Environments list")

2. In the Environment details page, click the stage name to enter the stage details:

  !![Environment details](../assets/quick-start/environment_details.png "Environment details")

3. Once you enter the stage details, proceed to deploy the application:

  1. Select an application;
  2. Select the Image stream version;
  3. Click the **Deploy** button.

  !![Deploying application](../assets/quick-start/deploy_application.png "Deploying application")

Congratulations! You have passed the Quick Start guide! We hope you enjoyed this journey.

Now that you have a good understanding of how EDP works, you can further enhance its capabilities by integrating it with [Nexus](../operator-guide/nexus-sonatype.md). Additionally, explore other functionalities detailed in our [Use Cases](../use-cases/index.md) section. If you're eager to unlock the full potential of EDP, navigate to the [Operator Guide](../operator-guide/index.md) to fine-tune your EDP for optimal performance!
