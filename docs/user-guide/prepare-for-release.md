# Prepare for Release

After the necessary applications are added to EDP, they can be managed via the Admin Console. To prepare for the release, create a new branch from a selected commit with a set of CI pipelines (Code Review and Build pipelines), launch the Build pipeline, and add a new CD pipeline as well.

!!! note
    Please refer to the [Add Application](add-application.md) and [Add CD Pipeline](add-cd-pipeline.md) for the details on how to add an application or a CD pipeline.

Become familiar with the following preparation steps for release and a CD pipeline structure:

* Create a new branch
* Launch the Build pipeline
* Add a new CD pipeline
* Check CD pipeline structure

## Create a New Branch

1. Open Gerrit via the Admin Console Overview page to have this tab available in a web browser.

2. Being in Admin Console, open the Applications section and click an application from the list to create a new branch.

3. Once clicked the application name, scroll down to the Branches menu and click the Create button to open the Create New Branch dialog box, fill in the Branch Name field by typing a branch name.

  * Open the Gerrit tab in the web browser, navigate to Projects → List → select the application → Branches → gitweb for a necessary branch.

  * Select the commit that will be the last included to a new branch commit.

  * Copy to clipboard the commit hash.

4.  Paste the copied hash to the From Commit Hash field and click Proceed.

!!! note
    If the commit hash is not added to the **From Commit Hash** field, the new branch will be created from the head of the master branch.

## Launch the Build Pipeline

1. After the new branches are added, open the details page of every application and click the CI link that refers to Jenkins.

  !!! note
      The adding of a new branch may take some time. As soon as the new branch is created, it will be displayed in the list of the Branches menu.

2. To build a new version of a corresponding Docker container (an image stream in OpenShift terms) for the new branch, start the Build pipeline. Being in Jenkins, select the new branch tab and click the link to the Build pipeline.

3. Navigate to the Build with Parameters option and click the Build button to launch the Build pipeline.

  !!! warning
      The predefined default parameters should not be changed when triggering the Build pipeline, otherwise, it will lead to the pipeline failure.


## Add a New CD Pipeline

1. Add a new CD pipeline and indicate the new release branch using the Admin console tool. Pay attention to the Applications menu, the necessary application(s) should be selected there, as well as the necessary branch(es) from the drop-down list.

  !!! note
      For the details on how to add a CD pipeline, please refer to the [Add CD Pipeline](add-cd-pipeline.md) page.

2. As soon as the Build pipelines are successfully passed in Jenkins, the Docker Registry, which is used in EDP by default, will have the new image streams (Docker container in Kubernetes terms) version that corresponds to the current branch.

3. Open the Kubernetes/OpenShift page of the project via the Admin Console Overview page → go to CodebaseImageStream (in OpenShift, go to Builds → Images) → check whether the image streams are created under the specific name (the combination of the application and branch names) and the specific tags are added. Click every image stream link.

## Check CD Pipeline Structure

When the CD pipeline is added through the Admin Console, it becomes available in the CD pipelines list. Every pipeline has the details page with the additional information. To explore the CD pipeline structure, follow the steps below:

1. Open Admin Console and navigate to Continuous Delivery section, click the newly created CD pipeline name.

2. Discover the CD pipeline components:

  - Applications - the list of applications with the image streams and links to Jenkins for the respective branch;

  - Stages - a set of stages with the defined characteristics and links to Kubernetes/OpenShift project;

  !!! note
      Initially, an environment is empty and does not have any deployment unit. When deploying the subsequent stages, the artifacts of the selected versions will be deployed to the current project and the environment will display the current stage status.
      The project has a standard pattern: &#8249;edp-name&#8250;-&#8249;pipeline-name&#8250;-&#8249;stage-name&#8250;.

  - Deployed Versions - the deployment status of the specific application and the predefined stage.

## Launch CD Pipeline Manually

Follow the steps below to deploy the QA and UAT application stages:

1. As soon as the Build pipelines for both applications are successfully passed, the new version of the Docker container will appear, thus allowing to launch the CD pipeline.
Simply navigate to Continuous Delivery and click the pipeline name to open it in Jenkins.

2. Click the QA stage link.

3. Deploy the QA stage by clicking the Build Now option.

4. After the initialization step starts, in case another menu is opened, the Pause for Input option will appear. Select the application version in the drop-down list and click Proceed. The pipeline passes the following stages:
  - Init - initialization of the Jenkins pipeline outputs with the stages that are the Groovy scripts that execute the current code;
  - Deploy - the deployment of the selected versions of the docker container and third-party services. As soon as the Deployed pipeline stage is completed, the respective environment will be deployed.
  - Approve - the verification stage that enables to Proceed or Abort this stage;
  - Promote-images - the creation of the new image streams for the current versions with the pattern combination: [pipeline name]-[stage name]-[application name]-[verified];

  After all the stages are passed, the new image streams will be created in the Kubernetes/OpenShift with the new names.

5. Deploy the UAT stage, which takes the versions that were verified during the QA stage, by clicking the Build Now option, and select the necessary application versions. The launch process is the same as for all the deploy pipelines.

6. To get the status of the pipeline deployment, open the CD pipeline details page and check the Deployed versions state.

## CD Pipeline as a Team Environment

Admin Console allows creating a CD pipeline with a part of the application set as a team environment. To do this, perform the following steps;

1. Open the Continuous Delivery section → click the Create button → enter the pipeline name (e.g. team-a) → select ONE application and choose the master branch for it → add one DEV stage.
2. As soon as the CD pipeline is added to the CD pipelines list, its details page will display the links to Jenkins and Kubernetes/OpenShift.
3. Open Jenkins and deploy the DEV stage by clicking the Build Now option.
4. Kubernetes/OpenShift keeps an independent environment that allows checking the new versions, thus speeding up the developing process when working with several microservices.

As a result, the team will have the same abilities to verify the code changes when developing and during the release.

### Related Articles

* [Add Application](add-application.md)
* [Add CD Pipeline](add-cd-pipeline.md)
* [Autotest as Qulity Gate](../use-cases/autotest-as-quality-gate.md)
* [Build Pipeline](build-pipeline.md)
* [CD Pipeline Details](cd-pipeline-details.md)
* [Customize CD Pipeline](customize-cd-pipeline.md)