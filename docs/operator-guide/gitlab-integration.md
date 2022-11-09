# GitLab Integration

Follow the steps below to automatically create and integrate Jenkins GitLab webhooks.

!!! note
    Before applying the GitLab integration, make sure to enable VCS Import strategy. For details, please refer to the [Enable VCS Import Strategy](import-strategy.md) section.

1. Create a new job provisioner by following the [instruction](manage-jenkins-ci-job-provision.md#gitlab-gitlab).The job provisioner will create a job suite for an application added to EDP. It will also create webhooks for the project in GitLab using a GitLab token.

2. Create the personal access token in GitLab:

  * Log in to GitLab.
  * In the top-right corner, click the avatar and select **Settings**.
  * On the **User Settings** menu, select *Access Tokens*.
  * Choose a name and an optional expiry date for the token.
  * In the **Scopes** block, select the *api* scope for the token.

  !![Personal access tokens](../assets/operator-guide/scopes.png "Personal access tokens")

  * Click the **Create personal access token** button.

  !!! note
      Make sure to save the access token as there will not be any ability to access it once again.

  It is also possible to create a project access token instead of a personal access token. In this case, the GitLab Jenkins plugin will be able to accept payloads from webhooks for the project only:

  * Log in to GitLab and navigate to the project.
  * On the **User Settings** menu, select *Access Tokens*.
  * Choose a name and an optional expiry date for the token.
  * Choose a role: *Owner* or *Maintainer*.
  * In the **Scopes** block, select the *api* scope for the token.

  !![Project access tokens](../assets/operator-guide/scopes-project.png "Project access tokens")

  * Click the **Create project access token** button.

3. Create the Jenkins Credential ID by navigating to **Jenkins** -> **Credentials** -> **System** -> **Global Credentials** -> **Add Credentials**:

  * Select the *Secret text* kind.
  * Select the *Global* scope.
  * **Secret** is the access token that was created earlier.
  * **ID** is the *gitlab-access-token* ID.
  * Use the description of the current Credential ID.

  !![Jenkins credential](../assets/operator-guide/jenkins-cred.png "Jenkins credential")

  !!! warning
      When using the GitLab integration, a webhook is automatically created. After the removal of the application, the webhook stops working but is not deleted. If necessary, it must be deleted manually.

  !!! note
      The next step is necessary if it is needed to see the status of Jenkins Merge Requests builds in the GitLab CI/CD Pipelines section.

4. In order to see the status of Jenkins Merge Requests builds in the GitLab CI/CD Pipelines section, configure the GitLab plugin by navigating to Manage Jenkins -> Configure System and filling in the GitLab plugin settings:

  * Connection name is *gitlab*.
  * **GitLab host URL** is a host URL to GitLab.
  * Use the *gitlab-access-token* credentials.

  !![GitLab plugin configuration](../assets/operator-guide/gitlab-plugin-configuration.png "GitLab plugin configuration")

  Find below an example of the Merge Requests build statuses in the GitLab CI/CD Pipelines section:

  !![GitLab pipelines statuses](../assets/operator-guide/gitlab-pipeline-stats.png "GitLab pipelines statuses")

### Related Articles

* [Adjust Jira Integration](jira-integration.md)
* [Enable VCS Import Strategy](import-strategy.md)
* [Jenkins integration with GitLab](https://docs.gitlab.com/ee/integration/jenkins.html)
* [Manage Jenkins CI Pipeline Job Provision](manage-jenkins-ci-job-provision.md)
