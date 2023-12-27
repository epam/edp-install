# CD Pipeline Details

**CD Pipeline (Continuous Delivery Pipeline)** - an EDP business entity that describes the whole delivery process of the selected application set via the respective stages.
The main idea of the CD pipeline is to promote the application build version between the stages by applying the sequential verification (i.e. the second stage will be available if the verification on the first stage is successfully completed).
The CD pipeline can include the essential set of applications with its specific stages as well.

In other words, the CD pipeline allows the selected image stream (Docker container in Kubernetes terms) to pass a set of stages for the verification process (SIT - system integration testing with the automatic type of a quality gate, QA - quality assurance, UAT - user acceptance testing with the manual testing).

!!! note
    It is possible to change the image stream for the application in the CD pipeline. Please refer to the [Edit CD Pipeline](add-cd-pipeline.md#edit-cd-pipeline) section for the details.

A CI/CD pipeline helps to automate steps in a software delivery process, such as the code build initialization, automated tests running, and deploying to a staging or production environment.
Automated pipelines remove manual errors, provide standardized development feedback cycle, and enable the fast product iterations. To get more information on the CI pipeline, please refer to the [CI Pipeline Details](ci-pipeline-details.md) chapter.

The codebase stream is used as a holder for the output of the stage, i.e. after the Docker container (or an image stream in OpenShift terms) passes the stage verification, it will be placed to the new codebase stream.
Every codebase has a branch that has its own codebase stream - a Docker container that is an output of the build for the corresponding branch.

!!! note
    For more information on the main terms used in EPAM Delivery Platform, please refer to the [EDP Glossary](../glossary.md)

!![EDP CD pipeline](../assets/user-guide/edp-cd-pipeline.png "EDP CD pipeline")

Explore the details of the CD pipeline below.

## Deploy Pipeline

The Deploy pipeline is used by default on any stage of the Continuous Delivery pipeline. It addresses the following concerns:

* Deploying the application(s) to the main STAGE (SIT, QA, UAT) environment in order to run autotests and to promote image build versions to the next environments afterwards.
* Deploying the application(s) to a custom STAGE environment in order to run autotests and check manually that everything is ok with the application.
* Deploying the latest or a stable and some particular numeric version of an image build that exists in Docker registry.
* Promoting the image build versions from the main STAGE (SIT, QA, UAT) environment.
* Auto deploying the application(s) version from the passed payload (using the CODEBASE_VERSION job parameter).

Find below the functional diagram of the Deploy pipeline with the default stages:

!!! note
    The input for a CD pipeline depends on the Trigger Type for a deploy stage and can be either Manual or Auto.

!![Deploy pipeline stages](http://www.plantuml.com/plantuml/proxy?src=https://raw.githubusercontent.com/epam/edp-install/master/docs/user-guide/deploy-pipeline.puml)

### Related Articles

* [Add Application](add-application.md)
* [Add Autotest](add-autotest.md)
* [Add CD Pipeline](add-cd-pipeline.md)
* [Add Library](add-library.md)
* [CI Pipeline Details](ci-pipeline-details.md)
* [CI/CD Overview](cicd-overview.md)
* [EDP Glossary](../glossary.md)
* [Prepare for Release](prepare-for-release.md)
