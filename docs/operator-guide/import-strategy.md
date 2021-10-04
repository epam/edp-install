# Enable VCS Import Strategy

In order to use the **Import** strategy, it is required to add Secret with SSH key, GitServer CR, and Jenkins credentials by taking the steps below.

1. Create a Secret in the &#8249;edp-project&#8250; namespace for the Git account with the **id_rsa**, **id_rsa.pub**, and **username** fields.

  As a sample, it is possible to use the following command:

      kubectl create secret generic gitlab-sshkey -n <edp-project> \
        --from-file=id_rsa=id_rsa \
        --from-file id_rsa.pub=id_rsa.pub \
        --from-literal=username=user@example.com

2. Create `GitServer` CR in the project namespace with the **gitHost**, **gitUser**, **httpsPort**, **sshPort**, **nameSshKeySecret**, and **createCodeReviewPipeline** fields.

  As a sample, it is possible to use the following template:

      apiVersion: v2.edp.epam.com/v1alpha1
      kind: GitServer
      metadata:
        name: <edp-project>
      spec:
        createCodeReviewPipeline: false
        gitHost: git.sample.com
        gitUser: git
        httpsPort: 443
        nameSshKeySecret: gitlab-sshkey
        sshPort: 22

  !!! note
      The value of the **nameSshKeySecret** property is the name of the Secret that is indicated in the first point above.

3. Create a Credential in Jenkins with the same ID as in the **nameSshKeySecret** property, and with the private key. Navigate to **Jenkins -> Credentials -> System -> Global Credentials -> Add Credentials**:

  ![credential](../assets/operator-guide/add-credentials.png "credential")

4. Change the Deployment Config of the Admin Console by adding the **Import** strategy to the **INTEGRATION_STRATEGIES** variable (check deployment templates for `edp-admin-console-operator` in [edp-install umbrella chart](https://github.com/epam/edp-install/blob/master/deploy-templates/values.yaml)):

      #...
      adminConsole:
        image: "epamedp/edp-admin-console-operator"
        version: "2.9.0"
        imagePullPolicy: "IfNotPresent"
        envs:
          - name: INTEGRATION_STRATEGIES
            value: "Create,Clone,Import"
      #...

5. As soon as the Admin Console is redeployed, the **Import** strategy will be added to the Create Application page. For details, please refer to the [Add Applications](../user-guide/add-application.md) page.
