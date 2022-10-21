# Enable VCS Import Strategy

!!! note
    Enabling the VCS Import strategy is a prerequisite to integrate EDP with GitLab or GitHub.

In order to use the **Import** strategy, it is required to add a Secret with SSH key, GitServer Custom Resource, and Jenkins credentials by taking the steps below.

1. Generate an SSH key pair and add a public key to [GitLab](https://docs.gitlab.com/ee/ssh/) or [GitHub](https://docs.github.com/en/authentication/connecting-to-github-with-ssh/generating-a-new-ssh-key-and-adding-it-to-the-ssh-agent) account.

      ssh-keygen -t ed25519 -C "email@example.com"

2. Generate access token for [GitLab](https://docs.gitlab.com/ee/user/profile/personal_access_tokens.html) or [GitHub](https://docs.github.com/en/authentication/keeping-your-account-and-data-secure/creating-a-personal-access-token) account with read/write access to the API.

3. Create a `Secret` in the &#8249;edp-project&#8250; namespace for the Git account with the **id_rsa**, **id_rsa.pub**, **username** and **token** fields.

  As a sample, it is possible to use the following command (use *github-configuration* instead of *gitlab-configuration* for GitHub):

      kubectl create secret generic gitlab-configuration -n <edp-project> \
        --from-file=id_rsa=id_rsa \
        --from-file=id_rsa.pub=id_rsa.pub \
        --from-literal=username=user@example.com \
        --from-literal=token=your_gitlab_access_token

4. Create `GitServer` Custom Resource in the project namespace with the **gitHost**, **gitUser**, **gitProvider**, **httpsPort**, **sshPort** and **nameSshKeySecret** fields. The **gitProvider** field can be either *gitlab*, *github*, or *gerrit*.

  As a sample, it is possible to use the following template:

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
        nameSshKeySecret: gitlab-configuration
        sshPort: 22

  !!! note
      The value of the **nameSshKeySecret** property is the name of the Secret that is indicated in the first point above.

5. Create `Jenkinsserviceaccount` Custom Resource with the **credentials** field that corresponds to the **nameSshKeySecret** property above.

      apiVersion: v2.edp.epam.com/v1
      kind: JenkinsServiceAccount
      metadata:
        name: gitlab-configuration
        namespace: <edp-project>
      spec:
        credentials: gitlab-configuration
        ownerName: ''
        type: ssh

    Double-check if the credentials are created in Jenkins correctly. Navigate to **Jenkins -> Credentials -> System -> Global Credentials -> Add Credentials**:

  !![Jenkins credential](../assets/operator-guide/add-credentials.png "Jenkins credential")

6. Make sure that the value of **INTEGRATION_STRATEGIES** variable is set to **Import** in the edp-admin-console deployment (should be by default). You can check it here:

      spec:
        containers:
          - name: edp-admin-console
          ....
            env:
              - name: INTEGRATION_STRATEGIES
                value: 'Create,Clone,Import'

  !!! note
      The default values can be found in the deployment templates for `edp-admin-console-operator` in [edp-install umbrella chart](https://github.com/epam/edp-install/blob/master/deploy-templates/values.yaml)

!!! note
    The **Import** strategy can be found on the **Applications** page of the Admin Console. For details, please refer to the [Add Applications](../user-guide/add-application.md) page.

The next step is to integrate Jenkins with [GitHub](github-integration.md) or [GitLab](gitlab-integration.md).

### Related Articles

* [Add Application](../user-guide/add-application.md)
* [GitHub Integration](github-integration.md)
* [GitLab Integration](gitlab-integration.md)
