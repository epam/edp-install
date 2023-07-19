# GitLab Webhook Configuration

Follow the steps below to automatically create and integrate Jenkins GitLab webhooks.

!!! note
    Before applying the GitLab integration, make sure to enable Integrate GitHub/GitLab in Jenkins. For details, please refer to
    the [Integrate GitHub/GitLab in Jenkins](import-strategy-jenkins.md) page.

1. Ensure the new job provisioner is created, as well as **Secret** with SSH key and **GitServer** custom resources.

2. Ensure the access token for GitLab is created.

3. Create the Jenkins Credential ID by navigating to **Dashboard** -> **Manage Jenkins** -> **Manage Credentials** -> **Global** -> **Add Credentials**:

  * Select the *Secret text* kind.
  * Select the *Global* scope.
  * **Secret** is the access token that was created earlier.
  * **ID** is the *gitlab-access-token* ID.
  * Use the description of the current Credential ID.

  !![Jenkins credential](../assets/operator-guide/jenkins-cred.png "Jenkins credential")

  !!! warning
      When using the GitLab integration, a webhook is automatically created. After the removal of the application, the webhook
      stops working but is not deleted. If necessary, it must be deleted manually.

  !!! note
      The next step is necessary if it is needed to see the status of Jenkins Merge Requests builds in the GitLab CI/CD
      Pipelines section.

4. In order to see the status of Jenkins Merge Requests builds in the GitLab CI/CD Pipelines section, configure the
   GitLab plugin by navigating to Manage Jenkins -> Configure System and filling in the GitLab plugin settings:

  * Connection name is *gitlab*.
  * **GitLab host URL** is a host URL to GitLab.
  * Use the *gitlab-access-token* credentials.

  !![GitLab plugin configuration](../assets/operator-guide/gitlab-plugin-configuration.png "GitLab plugin configuration")

  Find below an example of the Merge Requests build statuses in the GitLab CI/CD Pipelines section:

  !![GitLab pipelines statuses](../assets/operator-guide/gitlab-pipeline-stats.png "GitLab pipelines statuses")

### Related Articles

* [Adjust Jira Integration](jira-integration.md)
* [Integrate GitHub/GitLab in Jenkins](../operator-guide/import-strategy-jenkins.md)
* [Integrate GitHub/GitLab in Tekton](../operator-guide/import-strategy-tekton.md)
* [Grant Jenkins Access to the Gitlab Project](https://docs.gitlab.com/ee/integration/jenkins.html#grant-jenkins-access-to-the-gitlab-project)
* [Manage Jenkins CI Pipeline Job Provision](manage-jenkins-ci-job-provision.md)
