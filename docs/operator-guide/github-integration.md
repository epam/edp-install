# GitHub Integration

Discover the steps below to apply the GitHub integration correctly.

!!! note
    Before applying the GitHub integration, make sure to enable VCS Import strategy. For details, please refer to the [Enable VCS Import Strategy](import-strategy.md) section.

1. Create a new **Job Provision** by following the [instruction](manage-jenkins-ci-job-provision.md#github-github). The job provisioner will create a job suite for an application added to EDP. It will also create webhooks for the project in GitHub.

!!! note
    The steps below are required in order to automatically create and integrate Jenkins with GitHub webhooks.

1. Create access token for GitHub:

  * Click the profile account and navigate to Settings;
  * Go to Developer Settings;
  * Select Personal access token and generate a new one with the following parameters

  ![scopes-1](../assets/operator-guide/github-scopes-1.png "scopes-1")

  !!! note
      The access is required for the GitHub Pull Request Builder plugin to get Pull Request commits, their status, and author info.

  ![scopes-2](../assets/operator-guide/github-scopes-2.png "scopes-2")
  ![scopes-3](../assets/operator-guide/github-scopes-3.png "scopes-3")

  !!! warning
      Make sure to copy a new personal access token right at this moment because there will not be any ability to see it again.

2. Navigate to **Jenkins -> Credentials -> System -> Global credentials -> Add credentials**, and create new credentials with the **Secret text** kind. In the Secret field, provide the GitHub API token, fill in the **ID** field with the **github-access-token** value:

  ![jenkins_github_cred](../assets/operator-guide/api_token2.png "jenkins_github_cred")

3.	Navigate to **Jenkins -> Manage Jenkins -> Configure system -> GitHub**, and configure the GitHub server:

  ![github_plugin_config](../assets/operator-guide/github_int.png "github_plugin_config")

4. Configure the GitHub Pull Request Builder plugin (this plugin is responsible for listening on Pull Request webhook events and triggering Code Review jobs):

  !!! note
      The **Secret** field is optional, for details, please refer to the official [GitHub pull request builder plugin documentation](https://wiki.jenkins.io/display/JENKINS/GitHub+pull+request+builder+plugin).

  ![github_pull_plugin_config](../assets/operator-guide/pull_request.png "github_pull_plugin_config")

### Related Articles

* [Enable VCS Import Strategy](import-strategy.md)
* [Adjust Jira Integration](jira-integration.md)
* [Manage Jenkins CI Pipeline Job Provision](manage-jenkins-ci-job-provision.md)
