# GitLab Integration

Discover the steps below to apply the GitLab integration correctly:

1. Create access token in **Gitlab**:

  * Log in to **GitLab**;
  * In the top-right corner, click your avatar and select **Settings**;
  * On the **User Settings** menu, select **Access Tokens**;
  * Choose a name and an optional expiry date for the token;
  * In the Scopes block, select the *api* scope for the token;

  ![scopes](../assets/operator-guide/scopes.png "scopes")

  * Click the **Create personal access token** button.

  !!! note
      Make sure to save the access token as there won`t be the ability to access it once again.

2. Install **GitLab plugin** (if not installed) by navigating to *Manage Jenkins* and switching to plugin manager, select the **GitLab Plugin** check box:

  ![gitlab-plugin](../assets/operator-guide/gitlab-plugin.png "gitlab-plugin")

3. Create Jenkins Credential ID by navigating to *Jenkins -> Credentials -> System -> Global Credentials -> Add Credentials*:

  * Select GitLab API token;
  * Select Global scope;
  * API token - the **Access Token** that was created earlier;
  * ID - the **gitlab-access-token** ID;
  * Description - the description of the current Credential ID;

  ![jenkins-cred](../assets/operator-guide/jenkins-cred.png "jenkins-cred")

4. Configure **Gitlab plugin** by navigating to *Manage Jenkins -> Configure System* and fill in the **GitLab plugin** settings:

  * Connection name - gitlab;
  * Gitlab host URL - a host URL to GitLab;
  * Credentials - credentials with **Access Token** to GitLab (**gitlab-access-token**);

  ![gitlab-plugin-configuration](../assets/operator-guide/gitlab-plugin-configuration.png "gitlab-plugin-configuration")

5. Create a new **Job Provision** by following the [instruction](manage-jenkins-ci-job-provision.md#gitlab-gitlab).

  !!! warning
      Using the GitLab integration, a webhook is automatically created. After the removal of the application, the webhook stops working but not deleted. If necessary, it must be deleted manually.

### Related Articles

* [Adjust Jira Integration](jira-integration.md)
* [Enable VCS Import Strategy](import-strategy.md)
* [Manage Jenkins CI Pipeline Job Provision](manage-jenkins-ci-job-provision.md)
