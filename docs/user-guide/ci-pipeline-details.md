# CI Pipeline Details

**CI Pipeline (Continuous Integration Pipeline)** - an EDP business entity that describes the integration of changes made to a codebase into a single project. The main idea of the CI pipeline is to review the changes in the code submitted through a Version Control System (VCS)
and build a new codebase version so that it can be transmitted to the Continuous Delivery Pipeline for the rest of the delivery process.

There are three codebase types in EPAM Delivery Platform:

1. **Applications** - a codebase that is developed in the Version Control System, has the full lifecycle starting from the Code Review stage to its deployment to the environment;
2. **Libraries** - this codebase is similar to the Application type, but it is not deployed and stored in the Artifactory. The library can be connected to other applications/libraries;
3. **Autotests** - a codebase that inspects the code and can be used as a quality gate for the CD pipeline stage. The autotest only has the Code Review pipeline and is launched for the stage verification.

!!! note
    For more information on the above mentioned codebase types, please refer to the [Add Application](add-application.md), [Add Library](add-library.md), [Add Autotests](add-autotest.md) and [Autotest as Quality Gate](../use-cases/autotest-as-quality-gate.md) pages.

![edp-ci-pipeline](../assets/user-guide/edp-ci-pipeline.png "edp-ci-pipeline")

### Related Articles

* [Add Application](add-application.md)
* [Add Autotest](add-autotest.md)
* [Add Library](add-library.md)
* [Adjust Jira Integration](../operator-guide/jira-integration.md)
* [Adjust VCS Integration With Jira](../operator-guide/jira-gerrit-integration.md)
* [Autotest as Quality Gate](../use-cases/autotest-as-quality-gate.md)
* [Build Pipeline](build-pipeline.md)
* [Code Review Pipeline](code-review-pipeline.md)
* [Pipeline Stages](pipeline-stages.md)
