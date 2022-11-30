# Enable VCS Import Strategy

!!! note
    Enabling the VCS Import strategy is a prerequisite to integrate EDP with GitLab or GitHub.

=== "Tekton"

    In order to use the **Import** strategy, it is required to add a Secret with SSH key, and GitServer Custom Resource by taking the steps below.

    1. Generate an SSH key pair and add a public key to [GitLab](https://docs.gitlab.com/ee/ssh/) or [GitHub](https://docs.github.com/en/authentication/connecting-to-github-with-ssh/generating-a-new-ssh-key-and-adding-it-to-the-ssh-agent) account.

      ```bash
      ssh-keygen -t ed25519 -C "email@example.com"
      ```

    2. Generate access token for [GitLab](https://docs.gitlab.com/ee/user/profile/personal_access_tokens.html) or [GitHub](https://docs.github.com/en/authentication/keeping-your-account-and-data-secure/creating-a-personal-access-token) account with read/write access to the API.

    3. Create a `Secret` in the `edp-project` namespace for the Git account with the **id_rsa**, **username**, and **token** fields.

      Take the following template as an example (use *github* instead of *gitlab* for GitHub):

      ```bash
      kubectl create secret generic gitlab -n <edp-project> \
        --from-file=id_rsa=id_rsa \
        --from-literal=username=user@example.com \
        --from-literal=token=your_gitlab_access_token
      ```

    4. Update `gitProvider` value in `global` section of edp-install values.yaml to enable Gitlab/Github integration:

      ??? note "View: values.yaml"
          ```yaml
          global:
            # -- Can be gerrit, github or gitlab. By default: gerrit
            gitProvider: <git_provider_name>
          ```

    5. Upgrade edp-install release: `helm upgrade edp epamedp/edp-install --values values.yaml`.

=== "Jenkins"

    In order to use the **Import** strategy, it is required to add a Secret with SSH key, GitServer Custom Resource, and Jenkins credentials by taking the steps below.

    1. Generate an SSH key pair and add a public key to [GitLab](https://docs.gitlab.com/ee/ssh/) or [GitHub](https://docs.github.com/en/authentication/connecting-to-github-with-ssh/generating-a-new-ssh-key-and-adding-it-to-the-ssh-agent) account.

      ```bash
      ssh-keygen -t ed25519 -C "email@example.com"
      ```

    2. Generate access token for [GitLab](https://docs.gitlab.com/ee/user/profile/personal_access_tokens.html) or [GitHub](https://docs.github.com/en/authentication/keeping-your-account-and-data-secure/creating-a-personal-access-token) account with read/write access to the API.

    3. [Add a Git Server via Headlamp](../headlamp-user-guide/add-git-server.md), or create `GitServer` Custom Resource in the project namespace with the **gitHost**, **gitUser**, **gitProvider**, **httpsPort**, **sshPort** and **nameSshKeySecret** fields. The **gitProvider** field can be either *gitlab*, *github*, or *gerrit*.

      Take the following template as an example:

      ```yaml
      apiVersion: v2.edp.epam.com/v1
      kind: GitServer
      metadata:
        name: <git-server-name>
        namespace: <edp-project>
      spec:
        gitHost: git.sample.com
        gitUser: git
        gitProvider: gitlab
        httpsPort: 443
        nameSshKeySecret: gitlab
        sshPort: 22
      ```

      !!! note
          The value of the **nameSshKeySecret** property is the name of the Secret that is indicated in the first step above.

    4. Create the `Jenkinsserviceaccount` Custom Resource with the **credentials** field that corresponds to the **nameSshKeySecret** property above.

      ```yaml
      apiVersion: v2.edp.epam.com/v1
      kind: JenkinsServiceAccount
      metadata:
        name: gitlab
        namespace: <edp-project>
      spec:
        credentials: gitlab
        ownerName: ''
        type: ssh
      ```

      Double-check that the credentials are created in Jenkins correctly. Navigate to **Jenkins -> Credentials -> System -> Global Credentials -> Add Credentials**:

        !![Jenkins credentials](../assets/operator-guide/add-credentials.png "Jenkins credentials")

    5. The next step is to integrate Jenkins with [GitHub](github-integration.md) or [GitLab](gitlab-integration.md).

### Related Articles

* [Add Git Server](../headlamp-user-guide/add-git-server.md)
* [Add Application](../user-guide/add-application.md)
* [GitHub Integration](github-integration.md)
* [GitLab Integration](gitlab-integration.md)