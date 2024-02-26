# Quality Control

In EPAM Delivery Platform, we guarantee the quality of the product not only by using the most advanced tools and best practices but also by covering the whole product functionality with our dedicated automated tests.

## Autotest Coverage Scheme

Autotests are significant part of our verification flow. Continuous improvement of the verification mechanisms quality is performed to provide users with the most stable version of the platform.

The autotest coverage status is presented on the scheme below:

  !![Autotest coverage status](../assets/developer-guide/autotests-coverage.png "Autotest coverage status")

## Release Testing

In our testing flow, each release is verified by the following tests:

| Test Group | Description | What's Covered |
|-|:-|:-|
| API Tests | Tekton Gerrit, GitHub, and GitLab API long regression | Codebase provisioning, reviewing and building pipelines, adding new branches, deploying applications (in a custom namespace), Jira integration, and rechecking for review pipeline. |
| UI Tests | Tekton Gerrit, GitHub, and GitLab UI long regression | Codebase provisioning, reviewing and building pipelines, adding new branches, deploying applications (in a custom namespace), Jira integration, and rechecking for review pipeline. |
| Short Tests | Tekton Gerrit , GitHub, and GitLab API short regression | Codebase provisioning, reviewing and building pipelines, deploying applications (in a custom namespace), rechecking for review pipeline |
| Smoke | Tekton Gerrit Smoke | Codebase provisioning, reviewing and building pipelines, deploying applications. |

## Related Articles

* [EDP Project Rules. Working Process](edp-workflow.md)
* [Operator Development](local-development.md)
