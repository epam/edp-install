# EDP CI/CD Overview

This chapter provides information on CI/CD basic definitions and flow, as well as its components and process.

## CI/CD Basic Definitions

The **Continuous Integration** part means the following:

* all components of the application development are in the same place and perform the same processes for running;
* the results are published in one place and replicated into EPAM GitLab or VCS (version control system);
* the repository also includes a storage tool (e.g. Nexus) for all binary artifacts that are produced by the Jenkins CI server after submitting changes from Code Review tool into VCS;

The `Code Review` and `Build` pipelines are used before the code is delivered. An important part of both of them is the integration tests that are launched during the testing stage.

Many applications (SonarQube, Gerrit, etc,) used by the project need databases for their performance.

The **Continuous Delivery** comprises an approach allowing to produce an application in short cycles so that it can be reliably released at any time point. This part is tightly bound with the usage of the `Code Review`, `Build`, and `Deploy` pipelines.

The `Deploy` pipelines deploy the applications configuration and their specific versions, launch automated tests and control quality gates for the specified environment.
As a result of the successfully completed process, the specific versions of images are promoted to the next environment. All environments are sequential and promote the build versions of applications one-by-one.
The logic of each stage is described as a code of Jenkins pipelines and stored in the VCS.

During the CI/CD, there are several continuous processes that run in the repository, find below the list of possible actions:

* Review the code with the help of Gerrit tool;
* Run the static analysis using SonarQube to control the quality of the source code and keep the historical data which helps to understand the trend and effectivity of particular teams and members;
* Analyze application source code using SAST, byte code, and binaries for coding/design conditions that are indicative of security vulnerabilities;
* Build the code with Jenkins and run automated tests that are written to make sure the applied changes will not break any functionality.

!!! note
    For the details on autotests, please refer to the [Autotest](autotest.md), [Add Autotest](add-autotest.md), and [Autotest as Quality Gate](../use-cases/autotest-as-quality-gate.md) pages.

The release process is divided into cycles and provides regular delivery of completed pieces of functionality while continuing the development and integration of new functionality into the product mainline.

Explore the main flow that is displayed on the diagram below:

!![EDP CI/CD pipeline](../assets/user-guide/edp-ci-cd-process.png "EDP CI/CD pipeline")


### Related Articles

* [Add Application](add-application.md)
* [Add Library](add-library.md)
* [Add CD Pipeline](add-cd-pipeline.md)
* [CI Pipeline Details](ci-pipeline-details.md)
* [CD Pipeline Details](cd-pipeline-details.md)
* [Customize CI Pipeline](customize-ci-pipeline.md)
* [EDP Pipeline Framework](pipeline-framework.md)
* [Customize CD Pipeline](customize-cd-pipeline.md)
* [EDP Stages](pipeline-stages.md)
* [Glossary](../glossary.md)
* [Use Terraform Library in EDP](terraform-stages.md)
