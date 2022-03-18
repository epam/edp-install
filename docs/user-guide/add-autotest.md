# Add Autotests

Admin Console enables to clone or import an autotest, add it to the environment with its subsequent deployment in Gerrit (in case the Clone strategy is used) and building of the Code Review pipeline in Jenkins, as well as to use it for work with an application under development.
 It is also possible to use autotests as quality gates in a newly created CD pipeline.

!!! info
    Please refer to the [Add Application](add-application.md) section for the details on how to add an application codebase type.
    For the details on how to use autotests as quality gates, please refer to the the [Stages Menu](add-cd-pipeline.md#stages-menu) section of the [Add CD Pipeline](add-cd-pipeline.md) documentation.

Navigate to the **Autotests** section on the left-side navigation bar and click the Create button.

Once clicked, the three-step menu will appear:

* The Codebase Info Menu
* The Autotest Info Menu
* The Advanced Settings Menu

## The Codebase Info Menu

There are two available strategies: clone and import. The Clone strategy flow is displayed below:

!![Clone autotest](../assets/user-guide/codebase-info-autotest.png "Clone autotest")

1. **Clone** - this strategy allows cloning the autotest from the indicated repository into EPAM Delivery Platform.
   While cloning the existing repository, you have to fill in the additional fields as well.
2. In the **Git Repository URL** field, specify the link to the repository with the autotest.
3. With the Clone strategy, select the **Codebase Authentication** check box and fill in the requested fields:

  * Repository Login – enter your login data.
  * Repository password (or API Token) – enter your password or indicate the API Token.

    !!! note
        The **Codebase Authentication** check box should be selected only in case you clone the private repository.
        If you define the public repository, there is no need to enter credentials.

  If there is a necessity to use the **Import** strategy that allows configuring a replication from the Git server,
  explore the steps below:

  !![Import autotest](../assets/user-guide/import-strategy-autotest.png "Import autotest")

  a. **Import** - this strategy allows configuring a replication from the Git server.

  !!! note
      In order to use the **Import** strategy, make sure to adjust it by following the [Enable VCS Import Strategy](../operator-guide/import-strategy.md) page.

  b. In the **Git Server** field, select the necessary Git server from the drop-down list.

  c. In the Relative path field, indicate the respective path to the repository, e.g. **/epmd-edp/examples/basic/edp-auto-tests-simple-example**.

4. After completing the Codebase Info menu step, click the Proceed button to switch to the next menu.

  ## The Autotest Info Menu

  !![Autotest info](../assets/user-guide/autotest-info-menu.png "Autotest info")

5. Fill in the **Autotest Name** field by entering at least two characters and by using the lower-case letters, numbers and inner dashes.

  !!! info
      The **Import** strategy does not have an **Autotest Name** field.

6. Specify the name of the default branch where you want the development to be performed.

  !!! note
      The default branch cannot be deleted.

7. Type the necessary description in the **Description** field.

8. In the **Autotest Code Language** field, select the Java code language (specify Java 8 or Java 11 to be used) and get the default Maven build tool OR add another code language.
Selecting **Other** allows extending the default code languages and get the necessary build tool, for details, inspect the [Add Other Code Language](../operator-guide/add-other-code-language.md) section.

  !!! note
      Using the **Create** strategy does not allow to customize the default code language set.

9. The **Select Build Tool** field can dispose of the default Maven tool, Gradle or other built tool in accordance with the selected code language.

10. All the autotest reports will be created in the Allure framework that is available in the **Autotest Report Framework** field by default. Click the Proceed button to switch to the next menu.

  ## The Advanced Settings Menu

  !![Advanced settings](../assets/user-guide/advanced-settings-autotest.png "Advanced settings")

11. Select CI pipeline provisioner that will be used to handle a codebase. For details, refer to the [Manage Jenkins CI Pipeline Job Provisioner](../operator-guide/manage-jenkins-ci-job-provision.md) instruction and become familiar with the main steps to add an additional job provisioner.

12. Select Jenkins agent that will be used to handle a codebase. For details, refer to the [Manage Jenkins Agent](../operator-guide/add-jenkins-agent.md) instruction and inspect the steps that should be done to add a new Jenkins agent.

13. Select the necessary codebase versioning type:

  * **default** - the previous versioning logic that is realized in EDP Admin Console 2.2.0 and lower versions. Using the default versioning type, in order to specify the version of the current artifacts, images, and tags in the Version Control System, a developer should navigate to the corresponding file and change the version **manually**.

  * **edp** - the new versioning logic that is available in EDP Admin Console 2.3.0 and subsequent versions. Using the edp versioning type, a developer indicates the version number from which all the artifacts will be versioned and, as a result, **automatically** registered in the corresponding file (e.g. pom.xml).

  When selecting the edp versioning type, the extra field will appear:

  !![Edp versioning](../assets/user-guide/edp-versioning-autotest.png "Edp versioning")

  a. Type the version number from which you want the artifacts to be versioned.

  !!! note
      The **Start Version From** field should be filled out in compliance with the semantic versioning rules, e.g. 1.2.3 or 10.10.10. Please refer to the [Semantic Versioning](https://semver.org/) page for details.

14. In the **Select CI Tool** field, choose the necessary tool: Jenkins or GitLab CI, where Jenkins is the default tool and
    the GitLab CI tool can be additionally adjusted. For details, please refer to the [Adjust GitLab CI Tool](../operator-guide/gitlabci-integration.md) page.

  !!! note
      The GitLab CI tool is available only with the **Import** strategy and makes the **Jira integration** feature unavailable.

  !![Jira integration](../assets/user-guide/integrate-jira-server-autotest.png)

15. Select the **Integrate with Jira Server** check box in case it is required to connect Jira tickets with the commits and have a respective label in the Fix Version field.

  !!! note
      To adjust the Jira integration functionality, first apply the necessary changes described on the [Adjust Jira Integration](../operator-guide/jira-integration.md) page, and [Adjust VCS Integration With Jira](../operator-guide/jira-gerrit-integration.md). Pay attention that the Jira integration feature is not available when using the GitLab CI tool.

16. As soon as the Jira server is set, select it in the **Select Jira Server** field.

17. Indicate the pattern using any character, which is followed on the project, to validate a commit message.

18. Indicate the pattern using any character, which is followed on the project, to find a Jira ticket number in a commit message.

19. In the **Advanced Mapping** section, specify the names of the Jira fields that should be filled in with attributes from EDP. Upon clicking the question mark icon, observe the tips on how to indicate and combine variables necessary for identifying the format of values to be displayed.

  !![Advance mapping](../assets/user-guide/advance-mapping.png)

  a. Select the name of the field in a Jira ticket. The available fields are the following: **Fix Version/s**, **Component/s** and **Labels**.

  b. Select the pattern of predefined variables, based on which the value from EDP will be displayed in Jira. Combine several variables to obtain the desired value.

  * For the **Fix Version/s** field, select the **EDP_VERSION** variable that represents an EDP upgrade version, as in _2.7.0-SNAPSHOT_. Combine variables to make the value more informative. For example, the pattern **EDP_VERSION-EDP_COMPONENT** will be displayed as _2.7.0-SNAPSHOT-nexus-operator_ in Jira;

  * For the **Component/s** field, select the **EDP_COMPONENT** variable that defines the name of the existing repository. For example, _nexus-operator_;

  * For the **Labels** field, select the **EDP_GITTAG** variable that defines a tag assigned to the commit in GitHub. For example, _build/2.7.0-SNAPSHOT.59_.

  c. Click the plus icon to add more Jira field names.

  d. Click the delete icon to remove the Jira field name.

  !![Integrate with perf server](../assets/user-guide/integrate-perf-server-autotest.png "Integrate with perf server")

20. Select the **Integrate with Perf Server** check box to enable the integration with the [PERF Board](https://www.epam.com/telescopeai) (_Project Performance Board_) for monitoring the overall team performance and setting up necessary metrics.

  !!! note
      If this option is needed, please refer to the [Perf Server Integration](../operator-guide/perf-integration.md) to adjust the integration. After the integration is adjusted, the **Integrate with Perf Server** check box will appear in the **Advanced Settings** menu.

21. In the **Select Perf Server** field, select the name of the Perf server with which the integration should be performed and click the Proceed button to switch to the next menu.

  !![Perf integration](../assets/user-guide/perf-integration-autotest.png "Perf integration")

22. Select the necessary DataSource (_Jenkins/GitLab, Sonar_) from which the data should be transferred to the Project Performance Board.

23. Click the Create button to create an autotest, check the CONFIRMATION summary, click Continue to add an autotest to the Autotests list.

!!! note
    After the complete adding of the autotest, inspect the [Autotest Overview](autotest.md) part.

### Related Articles

* [Autotest Overview](autotest.md)
* [Delivery Dashboard Diagram](d-d-diagram.md)

---

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
