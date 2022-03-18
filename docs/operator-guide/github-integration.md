# GitHub Integration

Discover the steps below to apply the GitHub integration correctly.

!!! note
    Before applying the GitHub integration, make sure to enable VCS Import strategy. For details, please refer to the [Enable VCS Import Strategy](import-strategy.md) section.

1. Create a new **Job Provision** by following the [instruction](manage-jenkins-ci-job-provision.md#github-github). The job provisioner will create a job suite for an application added to EDP. It will also create webhooks for the project in GitHub.

  !!! note
      The steps below are required in order to automatically create and integrate Jenkins with GitHub webhooks.

2. Create access token for GitHub:

  * Click the profile account and navigate to Settings;
  * Go to Developer Settings;
  * Select Personal access token and generate a new one with the following parameters

  !![Repo permission](../assets/operator-guide/github-scopes-1.png "Repo permission")

  !!! note
      The access is required for the GitHub Pull Request Builder plugin to get Pull Request commits, their status, and author info.

  !![Admin permission](../assets/operator-guide/github-scopes-2.png "Admin permission")
  !![User permission](../assets/operator-guide/github-scopes-3.png "User permission")

  !!! warning
      Make sure to copy a new personal access token right at this moment because there will not be any ability to see it again.

3. Navigate to **Jenkins -> Credentials -> System -> Global credentials -> Add credentials**, and create new credentials with the **Secret text** kind. In the Secret field, provide the GitHub API token, fill in the **ID** field with the **github-access-token** value:

  !![Jenkins github credentials](../assets/operator-guide/api_token2.png "Jenkins github credentials")

4. Navigate to **Jenkins -> Manage Jenkins -> Configure system -> GitHub**, and configure the GitHub server:

  !![Github plugin config](../assets/operator-guide/github_int.png "Github plugin config")

  !!! note
      Keep the **Manage hooks** check box clear since the Job Provisioner automatically creates webhooks in the repository regardless of the check box selection.

5. Configure the GitHub Pull Request Builder plugin (this plugin is responsible for listening on Pull Request webhook events and triggering Code Review jobs):

  !!! note
      The **Secret** field is optional, for details, please refer to the official [GitHub pull request builder plugin documentation](https://wiki.jenkins.io/display/JENKINS/GitHub+pull+request+builder+plugin).

  !![Github pull plugin config](../assets/operator-guide/pull_request.png "Github pull plugin config")

### Related Articles

* [Enable VCS Import Strategy](import-strategy.md)
* [Adjust Jira Integration](jira-integration.md)
* [Manage Jenkins CI Pipeline Job Provision](manage-jenkins-ci-job-provision.md)
