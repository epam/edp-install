# Create Application

In EDP, all software components, such as applications, libraries, Terraform infrastructures, and automated tests, are termed as codebases. EDP provides flexible methods for scaffolding these components.

This guide will lead you through creating a Go application using the Gin framework. The  EDP Marketplace  will be utilized to streamline the application creation process.

## Application Onboarding

To create the first application, complete the instructions below:

1. In the EDP Portal, navigate to `EDP` -> `Marketplace`.

2. In the `Marketplace` section, select **Web Applications with Gin Framework**:

  !![Marketplace applications](../assets/quick-start/marketplace_application.png "Marketplace applications")

3. In the appeared window, fill in the required fields (ensure Git repo relative path is defined in the `github_username/repo_name` format and doesn't exist):

  !![Application blank](../assets/quick-start/add_marketplace_app.png "Application blank")

4. As soon as the codebase is created, navigate to it via the notification at the bottom left corner:

  !![Marketplace notification](../assets/quick-start/marketplace_notification.png "Marketplace notification")

## Build Application

Having created the Go application, proceed to build it by performing the following actions:

1. In the component details page, expand the application and click the "Go to the Source Code" button:

  !![Marketplace notification](../assets/quick-start/go_to_source_code.png "Application details")

2. In the opened Source Code, create new branch called **test**.

3. In the SonarCloud project page, copy the value of the SonarCloud organization name:

  !![Setting SonarCloud organization](../assets/quick-start/sonarcloud_organization.png "Setting SonarCloud organization")

4. In the **test** branch in GitHub, open the `sonar-project.properties` file and include the `sonar.organization` parameter that is equal to the value copied in the previous step, resulting in the following configuration:

    ```bash
    sonar.projectKey=my-go-gin-app
    sonar.projectName=my-go-gin-app
    sonar.go.coverage.reportPaths=coverage.out
    sonar.test.inclusions=**/*_test.go
    sonar.exclusions=**/cmd/**,**/deploy/**,**/deploy-templates/**,**/*.groovy,**/config/**
    sonar.language=go
    sonar.organization=organization-name
    ```

  !![Enable ingress](../assets/quick-start/set_organization.png "Specifying organization")

5. Commit the changes.

6. Create and merge a pull request:

  !![Pull request](../assets/quick-start/pull_request.png "Pull request")

7. In the component details page, click the **Trigger build pipeline run** button:

  !![Triggering pipeline run](../assets/quick-start/trigger_pipeline_run.png "Triggering pipeline run")

8. Enable port-forwarding for the edp-tekton-dashboard service (in case ingress is not deployed):

      kubectl port-forward service/edp-headlamp 64372:80 -n edp

  !!! note
      If the port is already in use, please select another available port. Use the `ss -tuln` or `netstat -tuln` command to see the ports at your disposal.

9. To monitor the build pipeline status, click the pipeline run name in the application details window:

  !![Failure details](../assets/quick-start/failure_details.png "Failure details")

  The initial pipeline is expected to fail, primarily due to SonarCloud intricacies. It is imperative to set a Quality Gate in SonarCloud after the initial pipeline run and subsequently re-trigger the build pipeline.

10. In the SonarCloud project, click the **Set New Code Definition** button:

  !![New code definition](../assets/quick-start/set_new_code-definition1.png "New code definition")

11. In the **New Code** page, set the **Previous version** option and click **Save**:

  !![New Code page](../assets/quick-start/setting_definition.png "New Code page")

12. In EDP Portal, trigger build pipeline run one more time and wait until the pipeline run is finished.

Build pipelines are designed to generate an executable image of an application. Once built, the image can be run in a target environment.

Now that you have successfully built an application, the next step involves creating an environment for deployment. To deploy the application, it is necessary to install and integrate Argo CD. To deploy the application, we need to install and integrate Argo CD. To do this, navigate to the [Integrate Argo CD](integrate-argocd.md) page.
