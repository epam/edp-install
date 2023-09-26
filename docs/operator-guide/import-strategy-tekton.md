# Integrate GitHub/GitLab in Tekton

This page describes how to integrate EDP with GitLab or GitHub in case of following the Tekton deploy scenario.

## Integration Procedure

To start from, it is required to add both Secret with SSH key and GitServer custom resources
by taking the steps below.

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

3. Create a secret in the `edp-project` namespace for the Git account with the **id_rsa**, **username**, and **token** fields. Take the following template as an example (use ci-github instead of ci-gitlab for GitHub):

    ```yaml
    kubectl create secret generic ci-gitlab -n edp \
    --from-file=id_rsa=id_rsa \
    --from-literal=username=git \
    --from-literal=token=your_gitlab_access_token
    ```

## Related Articles

* [Add Git Server](../user-guide/add-git-server.md)
* [Add Application](../user-guide/add-application.md)
* [GitHub WebHook Configuration](github-integration.md)
* [GitLab WebHook Configuration](gitlab-integration.md)
