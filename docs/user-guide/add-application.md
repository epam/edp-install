# Add Application

KubeRocketCI portal allows you to create an application, clone an existing repository with the application to your Version Control System (VCS), or using an external repository and importing an application to the environment. When an application is created or cloned, the system automatically generates a corresponding repository within the integrated Version Control System. You can create an Application [in YAML](#YAML) or [via the two-step menu](#menu) in the dialog.

To add an application, navigate to the **Components** section on the navigation bar and click **+ Create component**:

  !![Create new application](../assets/user-guide/create_new_codebase.png "Create new application")

Once clicked, the **Create new component** dialog will appear, then select **Application** and click **Next**:

   !![Application info](../assets/user-guide/create-new-component.png "Application info")

Choose one of the strategies and click **Create**:

   !![Select strategy](../assets/user-guide/select_strategy.png  "Select strategy")

* **Create from template** – creates a project on the pattern in accordance with an application language, a build tool, and a framework. This strategy is recommended for projects that start developing their applications from scratch.

* **Import project** - allows using existing VCS repository to integrate with KubeRocketCI. While importing the existing repository, select the Git server from the drop-down list and define the relative path to the repository, such as `epmd-edp/python-python-flask`.

  !!! note
      In order to use the **Import project** strategy, make sure to adjust it with the [Integrate GitLab/GitHub in Tekton](../operator-guide/import-strategy-tekton.md) page.

* **Clone project** – clones the indicated repository into KubeRocketCI. While cloning the existing repository, it is required to fill in the **Repository URL** field and specify the credentials if needed:

  !![Clone application](../assets/user-guide/edp-portal-clone-application.png "Clone application")

## Create Application in YAML <a name="YAML"></a>

Click **Edit YAML** in the upper-right corner of the **Create Application** dialog to open the YAML editor and create the Application.

!![Edit YAML](../assets/user-guide/edp-portal-yaml-edit-application.png "Edit YAML")

To edit YAML in the minimal editor, turn on the **Use minimal editor** toggle in the upper-right corner of the **Create Application** dialog.

To save the changes, select the **Save & Apply** button.

## Create Application via UI <a name="menu"></a>

The **Create Application** dialog contains two steps:

* The Codebase Info Menu
* The Advanced Settings Menu

### Codebase Info Menu

Follow the instructions below to fill in the fields of the **Codebase Info** menu:

  In our example, we will use the **Create from template** strategy:

  !![Create application](../assets/user-guide/edp-portal-create-application.png "Create application")

1. Select all the settings that define how the application will be added to Git server:

  * **Git server** - the pre-configured server where the component will be hosted. Select one from the from the drop-down list. Please refer to the [Manage Git Servers](git-server-overview.md) page to learn how to create the one.
  * **Repository name** - the relative path to the repository, such as `epmd-edp/python-python-flask`.
  * **Component name** - the name of the application. Must be at least two characters using the lower-case letters, numbers and inner dashes.
  * **Description** - brief and concise description that explains the purpose of the application.
  * **Empty project** - check this box to create a application with an empty repository. The empty repository option is available only for the **Create from template** strategy.

2. Specify the application language properties:

  - **Application Code Language** - defines the code language with its supported frameworks:

    * Java – selecting specific Java version (8,11,17 are available).
    * JavaScript - selecting JavaScript allows using React, Vue, Angular, Express, Next.js and Antora frameworks.
    * Python - selecting Python allows using the Python v.3.8, FastAPI, Flask frameworks.
    * Go - selecting Go allows using the Beego, Gin and Operator SDK frameworks.
    * C# - selecting C# allows using the .Net v.3.1 and .Net v.6.0 frameworks.
    * Helm - selecting Helm allows using the Helm framework.
    * Other - selecting Other allows extending the default code languages when creating a codebase with the clone/import strategy.
    !!! note
        The **Create from template** strategy does not allow to customize the default code language set.

  - **Language version/framework** - defines the specific framework or language version of the application. The field depends on the selected code language.
  - **Select Build Tool** -  allows to choose the build tool to use. A set tools and can be changed in accordance with the selected code language.

    * Java - selecting Java allows using the Gradle or Maven tool.
    * JavaScript - selecting JavaScript allows using the NPM tool.
    * C# - selecting C# allows using the .Net tool.
    * Python - selecting Python allows using Python tool.
    * Go - selecting Go allows using Go tool.
    * Helm - selecting Helm allows using Helm tool.

    !!! note
        The **Select Build Tool** field disposes of the default tools and can be changed in accordance with the selected code language.

    !!! note
        Tekton pipelines offer built-in support for Java Maven Multi-Module projects. These pipelines are capable of recognizing Java deployable modules based on the information in the **pom.xml** file and performing relevant deployment actions. It's important to note that although the Dockerfile is typically located in the root directory, Kaniko, the tool used for building container images, uses the targets folder within the deployable module's context. For a clear illustration of a Multi-Module project structure, please refer to this [example](https://github.com/epmd-edp/java-maven-java17-multimodule.git) on GitHub, which showcases a commonly used structure for Java Maven Multi-Module projects.


### Advanced Settings Menu

In the **Advanced Settings** menu, specify the branch options and define the Jira settings:

  !![Advanced settings](../assets/user-guide/edp-portal-application-advanced-settings.png "Advanced settings")

* **Default branch** - the name of the branch where you want the development to be performed.

  !!! note
      The default branch cannot be deleted.

* **Codebase versioning type** - defines how will the application tag be changed once the new image version is built. There are two versioning types:
  * **default**: Using the default versioning type, in order to specify the version of the current artifacts, images, and tags in the Version Control System, a developer should navigate to the corresponding file and change the version **manually**.
  * **edp**: Using the edp versioning type, a developer indicates the version number from which all the artifacts will be versioned and, as a result, **automatically** registered in the corresponding file (e.g. pom.xml). When selecting the edp versioning type, the extra fields will appear, type the version number from which you want the artifacts to be versioned:

      !![Edp versioning](../assets/user-guide/edp-portal-edp-versioning-application.png "Edp versioning")

    !!! note
        The **Start Version From** field should be filled out in compliance with the semantic versioning rules, e.g. 1.2.3 or 10.10.10. Please refer to the [Semantic Versioning](https://semver.org/) page for details.

* **Specify the pattern to validate a commit message** - the regular expression used to indicate the pattern that is followed on the project to validate a commit message in the code review pipeline. An example of the pattern: `^[PROJECT_NAME-d{4}]:.*$`.

  !![JIRA integration](../assets/user-guide/edp-portal-integrate-jira-server.png "JIRA integration")

* **Integrate with Jira server** - this check box is used in case it is required to connect Jira tickets with the commits
and have a respective label in the **Fix Version** field.

!!! note
    To adjust the Jira integration functionality, first apply the necessary changes described on the [Adjust Jira Integration](../operator-guide/jira-integration.md) page,
    and [Adjust VCS Integration With Jira](../operator-guide/jira-gerrit-integration.md).

* **Jira Server** - the integrated Jira server with related Jira tasks.

* **Specify the pattern to find a Jira ticket number in a commit message** - based on this pattern, the value from KubeRocketCI will be displayed in Jira.

  !![Mapping field name](../assets/user-guide/edp-portal-advanced-mapping.png "Mapping fields")

* **Mapping field name** - the section where the additional Jira fields are specified the names of the Jira fields that should be filled in with attributes from KubeRocketCI:

  * Select the name of the field in a Jira ticket. The available fields are the following: *Fix Version/s*, *Component/s* and *Labels*.

  * Click the **Add** button to add the mapping field name.

  * Enter Jira pattern for the field name:

    * For the **Fix Version/s** field, select the **EDP_VERSION** variable that represents an EDP upgrade version, as in _2.7.0-SNAPSHOT_.Combine variables to make the value more informative. For example, the pattern **EDP_VERSION-EDP_COMPONENT** will be displayed as _2.7.0-SNAPSHOT-nexus-operator_ in Jira.
    * For the **Component/s** field select the **EDP_COMPONENT** variable that defines the name of the existing repository. For example, _nexus-operator_.
    * For the **Labels** field select the **EDP_GITTAG**variable that defines a tag assigned to the commit in Git Hub. For example, _build/2.7.0-SNAPSHOT.59_.

  * Click the bin icon to remove the Jira field name.

Click the **Apply** button to add the application to the Components list.

!!! note
    After the complete adding of the application, inspect the [Manage Applications](application.md) page to learn how you can operate applications.

## Related Articles

* [Manage Applications](application.md)
* [Add CD Pipeline](add-cd-pipeline.md)
* [Adjust Jira Integration](../operator-guide/jira-integration.md)
* [Adjust VCS Integration With Jira](../operator-guide/jira-gerrit-integration.md)
* [Integrate GitHub/GitLab in Tekton](../operator-guide/import-strategy-tekton.md)
