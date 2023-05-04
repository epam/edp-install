# Add Autotests

Headlamp enables to clone or import an autotest, add it to the environment with its subsequent deployment in Gerrit (in case the Clone strategy is used) and building of the Code Review pipeline in Jenkins/Tekton, as well as to use it for work with an application under development.
 It is also possible to use autotests as quality gates in a newly created CD pipeline.

!!! info
    Please refer to the [Add Application](add-application.md) section for the details on how to add an application codebase type.
    For the details on how to use autotests as quality gates, please refer to the [Stages Menu](add-cd-pipeline.md#the-stages-menu) section of the [Add CD Pipeline](add-cd-pipeline.md) documentation.

To add an autotest, navigate to the **Components** section on the navigation bar and click **Create** (the plus sign icon in the lower-right corner of the screen). Once clicked, the **Create new component** dialog will appear, then select **Autotest** and choose one of the strategies which will be described later in this page. You can create an autotest [in YAML](#YAML) or [via the two-step menu](#menu) in the dialog.

## Create Autotest in YAML <a name="YAML"></a>

Click **Edit YAML** in the upper-right corner of the **Create Autotest** dialog to open the YAML editor and create an autotest:

!![Edit YAML](../assets/headlamp-user-guide/headlamp-yaml-edit-autotest.png "Edit YAML")

To edit YAML in the minimal editor, turn on the **Use minimal editor** toggle in the upper-right corner of the **Create Autotest** dialog.

To save the changes, select the **Save & Apply** button.

## Create Autotest in the Dialog <a name="menu"></a>

The **Create Autotest** dialog contains the two steps:

* The Codebase Info Menu
* The Advanced Settings Menu

### The Codebase Info Menu

There are two available strategies: clone and import.

1. The **Create new component** menu should look like the picture below:

  !![Create new component menu](../assets/headlamp-user-guide/create_new_autotest.png "Create new component menu")

2. In the **Repository onboarding strategy** field, select the necessary configuration strategy:

  * **Clone** – clones the indicated repository into EPAM Delivery Platform.

  !![Clone autotest](../assets/headlamp-user-guide/headlamp-clone-autotest.png "Clone autotest")

  1. While cloning the existing repository, it is required to fill in the **Repository URL** field.

  2. Select the Git server from the drop-down list and define the relative path to the repository, such as `/epmd-edp/examples/basic/edp-auto-tests-simple-example`.

  3. Select the **Repository credentials** check box in case you clone the private repository, and fill in the repository login and password/access token.

  4. Fill in the **Component name** field by entering at least two characters and by using the lower-case letters, numbers and inner dashes.

  5. Type the necessary description in the **Description** field.

  6. In the **Autotest code language** field, select the Java code language with its framework (specify Java 8 or Java 11 to be used) and get the default Maven build tool OR add another code language. Selecting **Other** allows extending the default code languages and get the necessary build tool, for details, inspect the [Add Other Code Language](../operator-guide/add-other-code-language.md) section.

    !!! note
        Using the **Create** strategy does not allow to customize the default code language set.

  7. Select the Java framework if Java is selected above.

  8. The **Build Tool** field can dispose of the default Maven tool, Gradle or other built tool in accordance with the selected code language.

  9. All the autotest reports will be created in the Allure framework that is available in the **Autotest Report Framework** field by default.

  * **Import** - allows configuring a replication from the Git server. While importing the existing repository.

  !!! note
      In order to use the **Import** strategy, make sure to adjust it by the [Enable VCS Import Strategy](../operator-guide/import-strategy.md) page. Also, you cannot choose the **Empty project** field using the **Import project** strategy.

3. Click the **Proceed** button to switch to the next menu.

  ### The Advanced Settings Menu

The **Advanced Settings** menu should look like the picture below:

  !![Advanced settings](../assets/headlamp-user-guide/headlamp-advanced-settings-autotest.png "Advanced settings")

a. Specify the name of the default branch where you want the development to be performed.

!!! note
    The default branch cannot be deleted.

b. In the **CI tool** field, choose the necessary tool. Tekton is the default tool, whereas Jenkins and GitLab CI tools can be additionally adjusted. For details, please refer to the [Adjust GitLab CI Tool](../operator-guide/gitlabci-integration.md) page in case of using Gitlab CI. Refer to the [Manage Jenkins CI Pipeline Job Provisioner](../operator-guide/manage-jenkins-ci-job-provision.md) instruction in case of using Jenkins.

!!! note
    The GitLab CI tool is available only with the **Import** strategy and makes the **Jira integration** feature unavailable.

c. Select the necessary codebase versioning type:

* **default**: Using the default versioning type, in order to specify the version of the current artifacts, images, and tags in the Version Control System, a developer should navigate to the corresponding file and change the version **manually**.

* **edp**: Using the edp versioning type, a developer indicates the version number from which all the artifacts will be versioned and, as a result, **automatically** registered in the corresponding file (e.g. pom.xml).

  When selecting the edp versioning type, the extra field will appear:

  !![Edp versioning](../assets/headlamp-user-guide/headlamp-edp-versioning-autotest.png "Edp versioning")

  Type the version number from which you want the artifacts to be versioned.

!!! note
    The **Start Version From** field must be filled out in compliance with the semantic versioning rules, e.g. 1.2.3 or 10.10.10. Please refer to the [Semantic Versioning](https://semver.org/) page for details.

d. Specify the pattern to validate a commit message. Use regular expression to indicate the pattern that is followed on the project to validate a commit message in the code review pipeline. An example of the pattern: `^[PROJECT_NAME-d{4}]:.*$`

  !![Jira integration](../assets/headlamp-user-guide/headlamp-integrate-jira-server.png)

e. Select the **Integrate with Jira Server** check box in case it is required to connect Jira tickets with the commits and have a respective label in the Fix Version field.

!!! note
    To adjust the Jira integration functionality, first apply the necessary changes described on the [Adjust Jira Integration](../operator-guide/jira-integration.md) page, and [Adjust VCS Integration With Jira](../operator-guide/jira-gerrit-integration.md). Pay attention that the Jira integration feature is not available when using the GitLab CI tool.

f. As soon as the Jira server is set, select it in the **Jira Server** field.

g. Specify the pattern to find a Jira ticket number in a commit message. Based on this pattern, the value from EDP will be displayed in Jira.

  !![Mapping field name](../assets/headlamp-user-guide/headlamp-library-advanced-mapping.png "Mapping field name")

h. In the **Advanced Mapping** section, specify the names of the Jira fields that should be filled in with attributes from EDP:

1. Select the name of the field in a Jira ticket. The available fields are the following: *Fix Version/s*, *Component/s* and *Labels*.

2. Click the **Add** button to add the mapping field name.

3. Enter Jira pattern for the field name:

  * For the **Fix Version/s** field, select the **EDP_VERSION** variable that represents an EDP upgrade version, as in _2.7.0-SNAPSHOT_.
  Combine variables to make the value more informative. For example, the pattern **EDP_VERSION-EDP_COMPONENT** will be displayed as _2.7.0-SNAPSHOT-nexus-operator_ in Jira.
  * For the **Component/s** field select the **EDP_COMPONENT** variable that defines the name of the existing repository. For example, _nexus-operator_.
  * For the **Labels** field select the **EDP_GITTAG**variable that defines a tag assigned to the commit in Git Hub. For example, _build/2.7.0-SNAPSHOT.59_.

4. Click the bin icon to remove the Jira field name.

i. Click the **Apply** button to add the library to the Libraries list.

!!! note
    After the complete adding of the autotest, inspect the [Autotest Overview](autotest.md) part.

## Related Articles

* [Autotest Overview](autotest.md)
* [Add Application](add-application.md)
* [Add CD Pipelines](add-cd-pipeline.md)
* [Add Other Code Language](../operator-guide/add-other-code-language.md)
* [Adjust GitLab CI Tool](../operator-guide/gitlabci-integration.md)
* [Adjust Jira Integration](../operator-guide/jira-integration.md)
* [Adjust VCS Integration With Jira](../operator-guide/jira-gerrit-integration.md)
* [Enable VCS Import Strategy](../operator-guide/import-strategy.md)
* [Manage Jenkins CI Pipeline Job Provisioner](../operator-guide/manage-jenkins-ci-job-provision.md)
* [Manage Jenkins Agent](../operator-guide/add-jenkins-agent.md)
* [Perf Server Integration](../operator-guide/perf-integration.md)
