# Application

This section describes the subsequent possible actions that can be performed with the newly added or existing applications.

## Check and Remove Application

As soon as the application is successfully provisioned, the following will be created:

- Code Review and Build pipelines in Jenkins for this application. The Build pipeline will be triggered automatically if at least one environment is already added.
- A new project in Gerrit or another VCS.
- SonarQube integration will be available after the Build pipeline in Jenkins is passed.
- Nexus Repository Manager will be available after the Build pipeline in Jenkins is passed as well.

!!! info
    To navigate quickly to OpenShift, Jenkins, Gerrit, SonarQube, Nexus, and other resources, click the Overview section on the navigation bar and hit the necessary link.

The added application will be listed in the Applications list allowing you to do the following:

![inspect-app](../assets/user-guide/inspect-application-menu.png "inspect-app")

1. Create another application by clicking the Create button and performing the same steps as described in [Add Applications](add-application.md) section.

2. Open application data by clicking its link name. Once clicked, the following blocks will be displayed:

  * **General Info** - displays common information about the created/cloned/imported application.
  * **Advanced Settings** - displays the specified job provisioner, Jenkins agent, deployment script, and the versioning type with the start versioning from number (the latter two fields appear in case of edp versioning type).
  * **Branches** - displays the status and name of the deployment branch, keeps the additional links to Jenkins and Gerrit. In case of edp versioning type, there are two additional fields:

    * **Build Number** - indicates the current build number;
    * **Last Successful Build** - indicates the last successful build number.

  * **Status Info** - displays all the actions that were performed during the creation/cloning/importing process.

3. Edit the application codebase by clicking the pencil icon. For details see the [Edit Existing Codebase](#edit-existing-codebase) section.

4. Remove application with the corresponding database and Jenkins pipelines:

  - Click the delete icon next to the application name;
  - Type the required application name;
  - Confirm the deletion by clicking the Delete button.

  !!! note
      The application that is used in a CD pipeline cannot be removed.

  ![inspect-app2](../assets/user-guide/inspect-application-menu2.png "inspect-app2")

5. Select a number of existing applications to be displayed on one page in the **Show entries** field. The filter allows to show 10, 25, 50 or 100 entries per page.

6. Sort the existing applications in a list by clicking the Name title. The applications will be displayed in alphabetical order.

7. Search the necessary application by entering the corresponding name, language or the build tool into the **Search** field. The search can be performed by the application name, language or a build tool.

8. Navigate between pages if the number of applications exceeds the capacity of a single page.

## Add a New Branch

When adding an application, the default branch is a **master** branch. In order to add a new branch, follow the steps below:

1. Navigate to the **Branches** block and click the Create button:

  ![addbranch1](../assets/user-guide/addbranch1.png "addbranch1")

2. Fill in the required fields:

  ![new-branch](../assets/user-guide/create-new-branch.png "new-branch")

  a. Release Branch - select the Release Branch check box if you need to create a release branch;

  b. Branch Name - type the branch name. Pay attention that this field remain static if you create a release branch.

  c. From Commit Hash - paste the commit hash from which the new branch will be created. Note that if the From Commit Hash field is empty, the latest commit from the branch name will be used.

  d. Branch Version - enter the necessary branch version for the artifact. The Release Candidate (RC) postfix is concatenated to the branch version number.

  e. Master Branch Version - type the branch version that will be used in a master branch after the release creation. The Snapshot postfix is concatenated to the master branch version number;

  f. Click the Proceed button and wait until the new branch will be added to the list.

  !!! info
      Adding of a new branch is indicated in the context of the edp versioning type. To get more detailed information on how to add a branch using the default versioning type, please refer [here](https://github.com/epam/edp-admin-console/blob/release-2.2/documentation/add_applications.md#-check-application-availability).

The default application repository is cloned and changed to the new indicated version before the build, i.e. the new indicated version will not be committed to the repository; thus, the existing repository will keep the default version.

## Edit Existing Codebase

The EDP Admin Console provides the ability to enable, disable or edit the Jira Integration functionality for applications via the Edit Codebase page.

1. Perform the editing from one of the following sections on the Admin Console interface:

  ![edit-codebase](../assets/user-guide/edit-codebase-1.png "edit-codebase")

  - Navigate to the codebase overview page and click the **pencil** icon, or

  ![edit-codebase2](../assets/user-guide/edit-codebase-2.png "edit-codebase2")

  - Navigate to the codebase list page and click the **pencil** icon.

  ![edit-codebase3](../assets/user-guide/edit-codebase-application.png "edit-codebase3")

2. To enable Jira integration, on the **Edit Codebase** page do the following:

   - mark the **Integrate with Jira server** checkbox and fill in the necessary fields;
   - click the **Proceed** button to apply the changes;
   - navigate to Jenkins and add the _create-jira-issue-metadata_ stage in the Build pipeline. Also add the _commit-validate_ stage in the Code-Review pipeline.

3. To disable Jira integration, on the **Edit Codebase** page do the following:

   - unmark the **Integrate with Jira server** checkbox;
   - click the **Proceed** button to apply the changes;
   - navigate to Jenkins and remove the _create-jira-issue-metadata_ stage in the Build pipeline. Also remove the _commit-validate_ stage in the Code-Review pipeline.

As a result, the necessary changes will be applied.

## Remove Branch

In order to remove the added branch with the corresponding  record in the Admin Console database, do the following:

1. Navigate to the Branches block by clicking the application name link in the Applications list;
2. Click the delete icon related to the necessary branch:

  ![remove-branch](../assets/user-guide/removebranch.png "removebranch")

3. Enter the branch name and click the Delete button;

!!! note
    The default **master** branch cannot be removed.

## Related Articles

* [Add Application](add-application.md)
* [Promote Application in CD Pipeline](../use-cases/promotion-procedure.md)