# Enable VCS Import Strategy

In order to use the **Import** strategy, it is required to add a Secret with SSH key, GitServer Custom Resource, and Jenkins credentials by taking the steps below.

1. Create a `Secret` in the &#8249;edp-project&#8250; namespace for the Git account with the **id_rsa**, **id_rsa.pub**, and **username** fields.

  As a sample, it is possible to use the following command:

      kubectl create secret generic gitlab-sshkey -n <edp-project> \
        --from-file=id_rsa=id_rsa \
        --from-file id_rsa.pub=id_rsa.pub \
        --from-literal=username=user@example.com

2. Create `GitServer` Custom Resource in the project namespace with the **gitHost**, **gitUser**, **httpsPort**, **sshPort**, **nameSshKeySecret**, and **createCodeReviewPipeline** fields.

  As a sample, it is possible to use the following template:

      apiVersion: v2.edp.epam.com/v1alpha1
      kind: GitServer
      metadata:
        name: <git-server-name>
        namespace: <edp-project>
      spec:
        createCodeReviewPipeline: false
        gitHost: git.sample.com
        gitUser: git
        httpsPort: 443
        nameSshKeySecret: gitlab-sshkey
        sshPort: 22

  !!! note
      The value of the **nameSshKeySecret** property is the name of the Secret that is indicated in the first point above.

3. Create `Jenkinsserviceaccount` Custom Resource with the **credentials** field that corresponds to the **nameSshKeySecret** property above.

      apiVersion: v2.edp.epam.com/v1alpha1
      kind: JenkinsServiceAccount
      metadata:
        name: gitlab-sshkey
        namespace: <edp-project>
      spec:
        credentials: gitlab-sshkey
        ownerName: ''
        type: ssh

    Double-check if the credentials are created in Jenkins correctly. Navigate to **Jenkins -> Credentials -> System -> Global Credentials -> Add Credentials**:

  ![credential](../assets/operator-guide/add-credentials.png "credential")

4. Make sure that the value of **INTEGRATION_STRATEGIES** variable is set to **Import** in the edp-admin-console deployment (should be by default). You can check it here:

      spec:
        containers:
          - name: edp-admin-console
          ....
            env:
              - name: INTEGRATION_STRATEGIES
                value: 'Create,Clone,Import'

  !!! note
      The default values can be found in the deployment templates for `edp-admin-console-operator` in [edp-install umbrella chart](https://github.com/epam/edp-install/blob/master/deploy-templates/values.yaml)

5. The **Import** strategy can be found on the **Applications** page of the Admin Console. For details, please refer to the [Add Applications](../user-guide/add-application.md) page.
