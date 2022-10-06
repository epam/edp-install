# Adjust Jira Integration

In order to adjust the Jira server integration, first add JiraServer CR by performing the following:

1. Create Secret in the OpenShift/Kubernetes namespace for Jira Server account with the **username** and **password** fields:

      apiVersion: v1
      data:
        password: passwordInBase64
        username: usernameInBase64
      kind: Secret
      metadata:
        name: epam-jira-user
      type: kubernetes.io/basic-auth

2. Create JiraServer CR in the OpenShift/Kubernetes namespace with the **apiUrl**, **credentialName** and **rootUrl** fields:

      apiVersion: v2.edp.epam.com/v1
      kind: JiraServer
      metadata:
        name: epam-jira
      spec:
        apiUrl: 'https://jira-api.example.com'
        credentialName: jira-user
        rootUrl: 'https://jira.example.com'
      status:
        available: true
        last_time_updated: '2021-04-05T10:51:07.042048633Z'

  !!! note
      The value of the **credentialName** property is the name of the Secret, which is indicated in the first point above.

3. Being in Admin Console, navigate to the Advanced Settings menu to check that the Integrate with Jira Server check box appeared:

    !![Advanced settings](../assets/operator-guide/jira_integration_ac.png "Advanced settings")
