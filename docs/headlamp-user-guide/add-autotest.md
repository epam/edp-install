# Add Autotests

Headlamp enables to clone or import an autotest, add it to the environment with its subsequent deployment in Gerrit (in case the Clone strategy is used) and building of the Code Review pipeline in Jenkins/Tekton, as well as to use it for work with an application under development.
 It is also possible to use autotests as quality gates in a newly created CD pipeline.

!!! info
    Please refer to the [Add Application](add-application.md) section for the details on how to add an application codebase type.
    For the details on how to use autotests as quality gates, please refer to the [Stages Menu](add-cd-pipeline.md#stages-menu) section of the [Add CD Pipeline](add-cd-pipeline.md) documentation.

To add an autotest, navigate to the **Autotests** section on the navigation bar and click **Create** (the plus sign icon in the lower-right corner of the screen). Once clicked, the **Create Autotest** dialog will appear. You can create an autotest [in YAML](#YAML) or [via the three-step menu](#menu) in the dialog.

## Create Autotest in YAML <a name="YAML"></a>

Click **Edit YAML** in the upper-right corner of the **Create Autotest** dialog to open the YAML editor and create an autotest.

!![Edit YAML](../assets/headlamp-user-guide/headlamp-yaml-edit-autotest.png "Edit YAML")

To edit YAML in the minimal editor, turn on the **Use minimal editor** toggle in the upper-right corner of the **Create Autotest** dialog.

To save the changes, select the **Save & Apply** button.

## Create Autotest in the Dialog <a name="menu"></a>

The **Create Autotest** dialog contains the three steps:

* The Codebase Info Menu
* The Autotest Info Menu
* The Advanced Settings Menu

### The Codebase Info Menu

There are two available strategies: clone and import.

1. Select the existing namespace from the drop-down list.

2. In the **Codebase Integration Strategy** field, select the necessary configuration strategy:

  * **Clone** – clones the indicated repository into EPAM Delivery Platform. While cloning the existing repository, it is required to fill in the additional fields as well. Select the **Codebase Authentication** check box in case you clone the private repository, and fill in the repository login and password/access token.

  !![Clone autotest](../assets/headlamp-user-guide/headlamp-clone-autotest.png "Clone autotest")

  * **Import** - allows configuring a replication from the Git server. While importing the existing repository, select the Git server from the drop-down list and define the relative path to the repository, such as */epmd-edp/examples/basic/edp-auto-tests-simple-example*.

  !![Import autotest](../assets/headlamp-user-guide/headlamp-import-autotest.png "Import autotest")

  !!! note
      In order to use the **Import** strategy, make sure to adjust it by following the [Enable VCS Import Strategy](../operator-guide/import-strategy.md) page.

3. Click the Proceed button to switch to the next menu.

  ### The Autotest Info Menu

  !![Autotest info](../assets/headlamp-user-guide/headlamp-autotest-info-menu.png "Autotest info")

4. Fill in the **Autotest Name** field by entering at least two characters and by using the lower-case letters, numbers and inner dashes.

5. Specify the name of the default branch where you want the development to be performed.

  !!! note
      The default branch cannot be deleted.

6. Type the necessary description in the **Description** field.

7. In the **Application code language/framework** field, select the Java code language (specify Java 8 or Java 11 to be used) and get the default Maven build tool OR add another code language.
Selecting **Other** allows extending the default code languages and get the necessary build tool, for details, inspect the [Add Other Code Language](../operator-guide/add-other-code-language.md) section.

  !!! note
      Using the **Create** strategy does not allow to customize the default code language set.

8. The **Select Build Tool** field can dispose of the default Maven tool, Gradle or other built tool in accordance with the selected code language.

9. All the autotest reports will be created in the Allure framework that is available in the **Autotest Report Framework** field by default.x

10. Click the Proceed button to switch to the next menu.

  ### The Advanced Settings Menu

  !![Advanced settings](../assets/headlamp-user-guide/headlamp-advanced-settings-autotest.png "Advanced settings")

11. In the **Select CI Tool** field, choose the necessary tool: Jenkins or GitLab CI, where Jenkins is the default tool and the GitLab CI tool can be additionally adjusted. For details, please refer to the [Adjust GitLab CI Tool](../operator-guide/gitlabci-integration.md) page.

  !!! note
      The GitLab CI tool is available only with the **Import** strategy and makes the **Jira integration** feature unavailable.

12. Select **Jenkins agent** that will be used to handle a codebase. For details, refer to the [Manage Jenkins Agent](../operator-guide/add-jenkins-agent.md) instruction and inspect the steps that should be done to add a new Jenkins agent.

13. Select a **CI pipeline provisioner** that will be used to handle a codebase. For details, refer to the [Manage Jenkins CI Pipeline Job Provisioner](../operator-guide/manage-jenkins-ci-job-provision.md) instruction and become familiar with the main steps to add another job provisioner.

14. Select the necessary codebase versioning type:

  * **default**: Using the default versioning type, in order to specify the version of the current artifacts, images, and tags in the Version Control System, a developer should navigate to the corresponding file and change the version **manually**.

  * **edp**: Using the edp versioning type, a developer indicates the version number from which all the artifacts will be versioned and, as a result, **automatically** registered in the corresponding file (e.g. pom.xml).

  When selecting the edp versioning type, the extra field will appear:

  !![Edp versioning](../assets/headlamp-user-guide/headlamp-edp-versioning-autotest.png "Edp versioning")

  a. Type the version number from which you want the artifacts to be versioned.

  !!! note
      The **Start Version From** field must be filled out in compliance with the semantic versioning rules, e.g. 1.2.3 or 10.10.10. Please refer to the [Semantic Versioning](https://semver.org/) page for details.

  !![Jira integration](../assets/headlamp-user-guide/headlamp-integrate-jira-server-autotest.png)

15. Select the **Integrate with Jira Server** check box in case it is required to connect Jira tickets with the commits and have a respective label in the Fix Version field.

  !!! note
      To adjust the Jira integration functionality, first apply the necessary changes described on the [Adjust Jira Integration](../operator-guide/jira-integration.md) page, and [Adjust VCS Integration With Jira](../operator-guide/jira-gerrit-integration.md). Pay attention that the Jira integration feature is not available when using the GitLab CI tool.

16. As soon as the Jira server is set, select it in the **Select Jira Server** field.

17. Specify the pattern to validate a commit message. Use regular expression to indicate the pattern that is followed on the project to validate a commit message in the code review pipeline. An example of the pattern: `^[PROJECT_NAME-d{4}]:.*$`

18. Specify the pattern to find a Jira ticket number in a commit message. Based on this pattern, the value from EDP will be displayed in Jira.

19. In the **Advanced Mapping** section, specify the names of the Jira fields that should be filled in with attributes from EDP:

  !![Advance mapping](../assets/headlamp-user-guide/headlamp-advanced-mapping-autotests.png)

  a. Select the name of the field in a Jira ticket. The available fields are the following: *Fix Version/s*, *Component/s* and *Labels*.

  b. Click the plus icon to add the mapping field name.

  c. Enter Jira pattern for the field name:

  * For the **Fix Version/s** field, select the **EDP_VERSION** variable that represents an EDP upgrade version, as in _2.7.0-SNAPSHOT_.
  Combine variables to make the value more informative. For example, the pattern **EDP_VERSION-EDP_COMPONENT** will be displayed as _2.7.0-SNAPSHOT-nexus-operator_ in Jira.
  * For the **Component/s** field select the **EDP_COMPONENT** variable that defines the name of the existing repository. For example, _nexus-operator_.
  * For the **Labels** field select the **EDP_GITTAG**variable that defines a tag assigned to the commit in Git Hub. For example, _build/2.7.0-SNAPSHOT.59_.

  d. Click the bin icon to remove the Jira field name.

20. Click the Apply button to add the autotest to the Autotests list.

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
