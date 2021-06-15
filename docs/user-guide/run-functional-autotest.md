# Run Functional Autotests

This chapter describes the process of adding, configuring, and running autotests. Run the added autotest on the deployed
environment or add it to a newly created CD pipeline stage.
Explore the process of launching the autotest locally, review the successful and unsuccessful Allure reports, and resolve
the encountered issue.

## The Predefined EDP Entities

Explore the predefined EDP entities:

1. Three applications: cart-service, order-service, and zuul:
  ![entities1](../assets/user-guide/entities1.png)
2. Every application has two branches:
  ![entities2](../assets/user-guide/entities2.png)
3. One CD pipeline with the DEV stage and a manual approve of the trigger type:
  ![entities3](../assets/user-guide/entities3.png)
4. The project deployment of the ms-master CD pipeline in OpenShift:
  ![entities4](../assets/user-guide/entities4.png)
5. The endpoint by the service status path of the master branch returns the following text: OK!
6. The endpoint of the release-8.0 branch should return the following text: OK!

## Add and Configure an Autotest

Follow the steps below to add an autotest using Admin Console and to configure it in Gerrit within the deployed environment:

1. Open the Admin Console and add an autotest:

  ![add_autotest](../assets/user-guide/add_autotest.png)

  !!! note
      To get more information on how to add autotests, please refer to the [Add Autotest](add-autotest.md) instruction.

2. After the provisioning of the new autotest is successfully completed, implement the necessary configuration for the added autotest. Open Gerrit via the Admin Console overview page → select the created autotest:
  ![add_autotest](../assets/user-guide/add_autotest2.png)
3. Navigate to the **Branches** tab and click the gitweb of the master branch:
  ![add_autotest](../assets/user-guide/add_autotest3.png)
4. Switch to the tree tab and open the **run.json** file:
  ![add_autotest](../assets/user-guide/add_autotest4.png)
5. Explore the **run.json** file where the stage is the key, the value is the command to run:

      {
        "sit": "mvn test -Dendpoint=https://zuul-gk-ms-release-sit.dev.gk-edp.com -Dsurefire.suiteXmlFiles=testng-smoke-suite.xml",
        "codereview": "mvn test -Dendpoint=https://zuul-gk-ms-master-dev.dev.gk-edp.com -Dsurefire.suiteXmlFiles=testng-smoke-suite.xml"
      }

  !!! info
      Continuous Integration is used when verifying the added to autotests changes that`s why the "codereview" key should be added.
      The "codereview" key is the **mandatory** and will be used during the Code Review pipeline processing to autotest itself by triggering when pushing a new code to the repository.

6. Create change using the Gerrit web console: open the rest-autotests project → create change → click the Publish button →
hit Edit and Add → type the `run.json` to open this file.

  !!! note
      To get more information on how to add a change using the Gerrit web console, please refer to the [Creating a Change in Gerrit](hhttps://gerrit-review.googlesource.com/Documentation/user-inline-edit.html) page.

7. Open the deployed environment in OpenShift and copy the external URL address for zuul application:
  ![configure_autotest](../assets/user-guide/configure_autotest.png)
8. Define another command value by pasting the copied URL address, click Save and then Publish button to trigger the CI pipeline in Jenkins:
  ![configure_autotest](../assets/user-guide/configure_autotest2.png)
9. Click the respective link to Jenkins in the History block to check that the Code Review pipeline is triggered:
  ![configure_autotest](../assets/user-guide/configure_autotest3.png)
10. Wait for the Code Review pipeline to be successfully passed and click the Allure report link to verify the autotests results:
    ![configure_autotest](../assets/user-guide/configure_autotest4.png)

  !!! info
      The autotests pass only the Code Review pipeline.

11. Explore the results in Allure by clicking the EDP SIT Tests link:
  ![allure_report](../assets/user-guide/allure_report.png)
12. Navigate between the included autotests and check the respective results:
  ![allure_report](../assets/user-guide/allure_report2.png)
13. Return to Gerrit, which already displays the appropriate marks, and merge the changes by clicking the Code-Review+2 and Submit buttons.

## Local Launch of Autotests

There is an ability to run the autotests locally using the IntelliJIDEA application. To launch the rest-autotests project for the local verification, perform the following steps:

1. Clone the project to the local machine.

2. Open the project in IntelliJIDEA and find the **run.json** file to copy out the necessary command value, then click the Add Configuration button, hit the plus sign → go to Maven and paste the copied command value into the Command line field→ click Apply and OK → hit the necessary button to run the added command value:
  ![launch_autotest](../assets/user-guide/launch_autotest.png)
3. As a result, all launched tests will be successfully passed:
  ![launch_autotest](../assets/user-guide/launch_autotest2.png)

## Add an Autotest to a New CD Pipeline

Add an additional CD pipeline and define two stages (SIT - System Integration Testing and QA - Quality Assurance) with the different Quality Gate types. Pay attention that the QA stage will be able to be launched only after the SIT stage is completed.

1. Add a new CD pipeline (e.g. under the ms-release name).
2. Select all three applications and specify the release-8.0 branch:
  ![add_autotest](../assets/user-guide/add_autotest5.png)
3. Add SIT stage by defining the **Autotests** type in the Quality gate type field, and select the respective check box as well:
  ![add_autotest](../assets/user-guide/add_autotest6.png)
4. Add the QA stage and define the **Manual** type in the Quality gate type field.
5. As soon as the CD pipeline is provisioned, the details page will display the added stages with the corresponding quality gate types. Click the CD pipeline name on the top of the details page:
  ![details_page](../assets/user-guide/details_page.png)
6. Being navigated to Jenkins, select the SIT stage and trigger it by clicking the Build Now option from the left-side panel, and define the necessary version on the Initialization stage:
  ![jenkins_sit](../assets/user-guide/jenkins_sit_stage.png)

  !!! info
      To trigger the CD pipeline, first, be confident that all applications have passed the Build pipelines and autotests have passed the single Code Review pipelines.

7. The SIT stage will not be passed successfully as the mentioned endpoint doesn`t exist.
To resolve the issue, apply the configuration using IntelliJIDEA (_see above the additional information on how to make changes locally: [Local Launch of Autotests](#local-launch-autotest);
8. Being in IntelliJIDEA, click Edit Configuration → Maven → type the name - ms-release-sit → click OK:
  ![intellij_idea](../assets/user-guide/i_idea.png)
9. Find the **run.json** file to copy out the necessary command value for SIT stage, then click the Edit Configuration button → go to Maven ms-release-sit and paste the copied command value into the Command line field→ click Apply and OK:
  ![intellij_idea](../assets/user-guide/i_idea2.png)
10. Push the changes to send them for review to Gerrit and submit the changes.
11. After the changes are added, the SIT stage should be triggered one more time. Open Jenkins and click the Build Now option from the left-side panel, then select the latest version in the appeared notification during the Initialization stage.
As a result, the triggered pipeline won`t be passed. Click the Allure link to get more information about the issues:

  ![jenkins_sit](../assets/user-guide/failed_build.png)

12. The Allure report displays the failed tests and explains the failure reason. In the current case, the expected string doesn`t match the initial one:
  ![allure_report](../assets/user-guide/allure_report3.png)

## Deploy SIT Stage

The pipeline can be failed due to the version that was selected for deployment. This version includes the new mentioned
changes that affected the pipeline processing. In order to resolve this issue and successfully deploy the CD pipeline,
a developer/user should choose the previous version without new changes. To do this, follow the steps below:

1. Open Jenkins and trigger the SIT stage one more time, select the **previous** version on the Initialization stage:
  ![jenkins_sit](../assets/user-guide/jenkins_sit_stage2.png)
2. As soon as the automation-tests stage is passed, check the Allure report:
  ![allure_report](../assets/user-guide/allure_report2.png)
3. After the Promote-images stage is completed on the SIT stage, the QA stage can be triggered and deployed as the Docker images were promoted to the next stage:
  ![jenkins_sit](../assets/user-guide/jenkins_sit_stage3.png)
As a result, the SIT stage will be deployed successfully, thus allowing to trigger the next QA stage.
