# Manage Infrastructures

This section describes the subsequent possible actions that can be performed with the newly added or existing infrastructures.

## Check and Remove Application

As soon as the infrastructure is successfully provisioned, the following will be created:

- An Infrastructure Codebase type will appear in the Codebase list of the Components section.
- With the **Create** strategy, a new project will be generated on GitHub or another integrated VCS. When **Clone** is chosen, the repository will be forked from the original and copied to the EDP-integrated repository. If **Import** is selected, the platform connects to the chosen repository.

The added application will be listed in the Applications list allowing you to do the following:

!![Applications menu](../assets/user-guide/edp-portal-inspect-infrastructure-menu.png "Applications menu")

* **Infrastructure status** - displays the Git Server status. Can be red or green depending on if the EDP Portal managed to connect to the Git Server with the specified credentials or not.
* **Infrastructure name (clickable)** - displays the infrastructure name set during the Git Server creation.
* **Open documentation** - opens the documentation that leads to this page.
* **Enable filtering** - enables filtering by Git Server name and namespace where this custom resource is located in.
* **Create new infrastructure** - displays the **Create new component** menu.
* **Edit infrastructure** - edit the infrastructure by selecting the options icon next to its name in the infrastructures list, and then selecting **Edit**. For details see the [Edit Existing Application](#edit-existing-application) section.
* **Delete infrastructure** - remove infrastructure by clicking the vertical ellipsis button and then selecting **Delete**.

There are also options to sort the infrastructures:

* Sort the existing infrastructures in a table by clicking the sorting icons in the table header. Sort the infrastructures alphabetically by their name, language, build tool, framework, and CI tool. You can also sort the infrastructures by their status: Created, Failed, or In progress.

* Select a number of infrastructures displayed per page (15, 25 or 50 rows)  and navigate between pages if the number of applications exceeds the capacity of a single page.

## Edit Existing Infrastructure

EDP Portal provides the ability to enable, disable or edit the Jira Integration functionality for infrastructures.

1. To edit an infrastructure directly from the infrastructures overview page or when viewing the infrastructure data:

  - Select **Edit** in the options icon menu:

  !![Edit infrastructure on the Infrastructures overview page](../assets/user-guide/edp-portal-edit-infrastructure-1.png "Edit infrastructure on the Infrastructures overview page")

  !![Edit infrastructure when viewing the infrastructure data](../assets/user-guide/edp-portal-edit-infrastructure-2.png "Edit infrastructure when viewing the infrastructure data")

  - The **Edit Infrastructure** dialog opens.

2. To enable Jira integration, in the **Edit Infrastructure** dialog do the following:

   !![Edit application](../assets/user-guide/edp-portal-edit-codebase-infrastructure.png "Edit infrastructure")

   a. Mark the **Integrate with Jira server** check box and fill in the necessary fields. Please see steps d-h on the [Add Infrastructure page](add-infrastructure.md#the-advanced-settings-menu).

   b. Select the **Apply** button to apply the changes.

3. To disable Jira integration, in the **Edit Infrastructure** dialog do the following:

   a. Clear the **Integrate with Jira server** check box.

   b. Select the **Apply** button to apply the changes.

4. To create, edit and delete infrastructure branches, please refer to the [Manage Branches](../user-guide/manage-branches.md) page.

## Related Articles

* [Add Infrastructure](add-infrastructure.md)
* [Manage Branches](../user-guide/manage-branches.md)
