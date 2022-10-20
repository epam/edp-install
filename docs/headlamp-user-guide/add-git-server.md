# Add Git Server

Add Git servers to use the **Import** strategy when creating an application, autotest or library in EDP Headlamp (*Codebase Info* step of the *Create Application/Autotest/Library* dialog). Enabling the Import strategy is a prerequisite to integrate EDP with Gitlab or GitHub.

!!! note
    `GitServer` Custom Resource can be also created in the Lens IDE. See steps 3 and 4 in the [Enable VCS Import Strategy](../operator-guide/import-strategy.md) article.

To add a Git server, navigate to the **Git servers** section on the navigation bar and click **Create** (the plus sign icon in the lower-right corner of the screen). Once clicked, the **Create Git server** dialog will appear. You can create a Git server [in YAML](#YAML) or [via the three-step menu](#menu) in the dialog.

## Create Git Server in YAML <a name="YAML"></a>

Click **Edit YAML** in the upper-right corner of the **Create Git server** dialog to open the YAML editor and create a Git server.

!![Edit YAML](../assets/headlamp-user-guide/headlamp-yaml-edit-git-server.png "Edit YAML")

To edit YAML in the minimal editor, turn on the **Use minimal editor** toggle in the upper-right corner of the **Create Git server** dialog.

To save the changes, select the **Save & Apply** button.

## Create Git Server in the Dialog <a name="menu"></a>

Fill in the following fields:

!![Create Git server](../assets/headlamp-user-guide/headlamp-create-git-server.png "Create Git server")

* *Git provider*
* *Host*
* *User*
* *SSH port*
* *HTTPS port*
* *Private SSH key*: generate your private SSH key locally using a special command.
* *Access token*: access token is generated in your Gitlab or GitHub account.

Click the **Apply** button to add the Git server to the Git servers list and to the Lens IDE. A `Secret` for this Git server is automatically generated in the Lens IDE.

## Related Articles

* [Enable VCS Import Strategy](../operator-guide/import-strategy.md)