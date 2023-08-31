# Integrate GitHub/GitLab in Jenkins

This page describes how to integrate EDP with GitLab or GitHub in case of following the Jenkins deploy scenario.

## Integration Procedure

To start from, it is required to add both Secret with SSH key and GitServer custom resources
by taking the steps below:

1. Generate an SSH key pair and add a public key to [GitLab](https://docs.gitlab.com/ee/ssh/)
   or [GitHub](https://docs.github.com/en/authentication/connecting-to-github-with-ssh/generating-a-new-ssh-key-and-adding-it-to-the-ssh-agent)
   account.

  ```bash
  ssh-keygen -t ed25519 -C "email@example.com"
  ```

2. Generate access token for [GitLab](https://docs.gitlab.com/ee/user/profile/personal_access_tokens.html)
   or [GitHub](https://docs.github.com/en/authentication/keeping-your-account-and-data-secure/creating-a-personal-access-token)
   account with read/write access to the API. Both personal and project access tokens are applicable.

  === "GitHub"

      To create access token in GitHub, follow the steps below:

      * Log in to GitHub.
      * Click the profile account and navigate to **Settings** -> **Developer Settings**.
      * Select *Personal access tokens (classic)* and generate a new token with the following parameters:

      !![Repo permission](../assets/operator-guide/github-scopes-1.png "Repo permission")

      !!! note
          The access below is required for the GitHub Pull Request Builder plugin to get Pull Request commits, their status, and author info.

      !![Admin permission](../assets/operator-guide/github-scopes-2.png "Admin permission")
      !![User permission](../assets/operator-guide/github-scopes-3.png "User permission")

      !!! warning
          Make sure to save a new personal access token because it won`t be displayed later.

  === "GitLab"

      To create access token in GitLab, follow the steps below:

      * Log in to GitLab.
      * In the top-right corner, click the avatar and select **Settings**.
      * On the **User Settings** menu, select **Access Tokens**.
      * Choose a name and an optional expiry date for the token.
      * In the **Scopes** block, select the **api** scope for the token.

      !![Personal access tokens](../assets/operator-guide/scopes.png "Personal access tokens")

      * Click the **Create personal access token** button.

      !!! note
          Make sure to save the access token as there will not be any ability to access it once again.

      In case you want to create a project access token instead of a personal one, the GitLab Jenkins plugin will be able to accept payloads from webhooks for the project only:

      * Log in to GitLab and navigate to the project.
      * On the **User Settings** menu, select *Access Tokens*.
      * Choose a name and an optional expiry date for the token.
      * Choose a role: *Owner* or *Maintainer*.
      * In the **Scopes** block, select the *api* scope for the token.

      !![Project access tokens](../assets/operator-guide/scopes-project.png "Project access tokens")

      * Click the **Create project access token** button.

3. Create secret in the `<edp-project>` namespace for the Git account with the **id_rsa**, **username**, and **token** fields. We recommend using EDP Portal to implement this:

  *  Open EDP Portal URL. Use the Sign-In option:

    !![Logging Page](../assets/use-cases/fastapi-scaffolding/login.png "Logging screen")

  *  In the top right corner, enter the `Cluster settings` and set the `Default namespace`. The `Allowed namespaces` field is optional. All the resources created via EDP Portal are created in the `Default namespace` whereas `Allowed namespaces` means the namespaces you are allowed to access in this cluster:

    !![Settings](../assets/use-cases/tekton-custom/cluster-settings.png "Cluster settings")

  *  Log into EDP Portal UI, select `EDP` -> `Git Servers` -> `+` to see the `Create Git Server` menu:

    !![Git Servers overview](../assets/operator-guide/edp-git-servers-overview.png)

  *  Choose your **Git provider**, insert **Host**, **Access token**, **Private SSH key**. Adjust **SSH port**, **User** and **HTTPS port** if needed and click `Apply`:

    !!! note
        Do not forget to press enter at the very end of the private key to have the last row empty.

    !![Create Git Servers menu](../assets/operator-guide/create-git-servers.png)

  *  After performing the steps above, two Kubernetes custom resources will be created in the default namespace: secret and GitServer. EDP Portal appends random symbols to both the secret and the GitServer to provide names with uniqueness. Also, the attempt to connect to your actual Git server will be performed. If the connection with the server is established, the Git server status should be green:

    !![Git server status](../assets/operator-guide/git-server-status.png)

    !!! note
        The value of the **nameSshKeySecret** property is the name of the Secret that is indicated in the first step above.

4. Create the `JenkinsServiceAccount` custom resource with the **credentials** field that corresponds to the **nameSshKeySecret** property above:

    ```yaml
    apiVersion: v2.edp.epam.com/v1
    kind: JenkinsServiceAccount
    metadata:
      name: gitlab # It can also be github.
      namespace: <edp-project>
    spec:
      credentials: <nameSshKeySecret>
      ownerName: ''
      type: ssh
    ```

5. Double-check that the new SSH credentials called `gitlab`/`github` are created in Jenkins using the SSH key. Navigate to `Jenkins` -> `Manage Jenkins` -> `Manage Credentials` -> `(global)`:

    !![Jenkins credentials](../assets/operator-guide/add-credentials.png "Jenkins credentials")

6. Create a new job provisioner by following the instructions for [GitHub](manage-jenkins-ci-job-provision.md#github-github) or [GitLab](manage-jenkins-ci-job-provision.md#gitlab-gitlab). The job provisioner will create a job suite for an application added to EDP. The job provisioner will also create webhooks for the project in GitLab using a GitLab token.

7. Configure [GitHub](../operator-guide/github-integration.md) or [GitLab](../operator-guide/gitlab-integration.md) plugins in Jenkins.

## Related Articles

* [Add Git Server](../user-guide/add-git-server.md)
* [Add Application](../user-guide/add-application.md)
* [GitHub Webhook Configuration](github-integration.md)
* [GitLab Webhook Configuration](gitlab-integration.md)
