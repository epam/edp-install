# Changelog

## Overview

This section provides details on edp-install release lifecycle.

_**NOTE**: For details on EDP releases, please refer to the [RELEASES.md](./RELEASES.md) page._


<a name="unreleased"></a>
## [Unreleased]

### Bug Fixes

- Fix release flow script in GH Actions [EPMDEDP-8227](https://jiraeu.epam.com/browse/EPMDEDP-8227)

### Code Refactoring

- Remove the creation of users [EPMDEDP-7438](https://jiraeu.epam.com/browse/EPMDEDP-7438)
- Align Gitlab integration with Github [EPMDEDP-7979](https://jiraeu.epam.com/browse/EPMDEDP-7979)
- Remove unused Dockerfile [EPMDEDP-7999](https://jiraeu.epam.com/browse/EPMDEDP-7999)

### Routine

- Add automatic release GH Action [EPMDEDP-8084](https://jiraeu.epam.com/browse/EPMDEDP-8084)
- Add changelog generator options [EPMDEDP-8084](https://jiraeu.epam.com/browse/EPMDEDP-8084)
- Update release flow [EPMDEDP-8227](https://jiraeu.epam.com/browse/EPMDEDP-8227)
- Add ct.yaml config [EPMDEDP-8227](https://jiraeu.epam.com/browse/EPMDEDP-8227)

### Documentation

- Update the Changelog with RN v.2.10.0 [EPMDEDP-7834](https://jiraeu.epam.com/browse/EPMDEDP-7834)
- Fix the link in the Changelog [EPMDEDP-7834](https://jiraeu.epam.com/browse/EPMDEDP-7834)
- Refactor GitHub job provisioner to accept webhooks [EPMDEDP-7938](https://jiraeu.epam.com/browse/EPMDEDP-7938)
- Add images to the Promote App doc [EPMDEDP-7955](https://jiraeu.epam.com/browse/EPMDEDP-7955)
- Fix typos in the AC user guide [EPMDEDP-7958](https://jiraeu.epam.com/browse/EPMDEDP-7958)
- Update GitHub/GitLab Integration [EPMDEDP-7984](https://jiraeu.epam.com/browse/EPMDEDP-7984)
- Add a Parallel Threads use case [EPMDEDP-8004](https://jiraeu.epam.com/browse/EPMDEDP-8004)
- Fix typos in EDP Workflow doc [EPMDEDP-8017](https://jiraeu.epam.com/browse/EPMDEDP-8017)
- Add section on upgrading EDP to version 2.10.1 [EPMDEDP-8092](https://jiraeu.epam.com/browse/EPMDEDP-8092)
- Update EDP Releases doc [EPMDEDP-8108](https://jiraeu.epam.com/browse/EPMDEDP-8108)
- Refactor import strategy documentation [EPMDEDP-8121](https://jiraeu.epam.com/browse/EPMDEDP-8121)
- Manage hooks option explanation [EPMDEDP-8121](https://jiraeu.epam.com/browse/EPMDEDP-8121)
- Add webhook debug description [EPMDEDP-8151](https://jiraeu.epam.com/browse/EPMDEDP-8151)
- Edit Releases link [EPMDEDP-8175](https://jiraeu.epam.com/browse/EPMDEDP-8175)
- Add commit message example [EPMDEDP-8221](https://jiraeu.epam.com/browse/EPMDEDP-8221)


<a name="v2.10.2"></a>
## [v2.10.2] - 2022-01-21
### Routine

- Add changelog generator options [EPMDEDP-8084](https://jiraeu.epam.com/browse/EPMDEDP-8084)
- Add ct.yaml config [EPMDEDP-8227](https://jiraeu.epam.com/browse/EPMDEDP-8227)
- Bump jenkins-operator version [EPMDEDP-8227](https://jiraeu.epam.com/browse/EPMDEDP-8227)
- Update release flow [EPMDEDP-8227](https://jiraeu.epam.com/browse/EPMDEDP-8227)


<a name="v2.10.1"></a>
## [v2.10.1] - 2022-01-04
### Routine

- Bump sonar-operator release version [EPMDEDP-8084](https://jiraeu.epam.com/browse/EPMDEDP-8084)

### Documentation

- Fix the link in the Changelog [EPMDEDP-7834](https://jiraeu.epam.com/browse/EPMDEDP-7834)
- Update the Changelog with RN v.2.10.0 [EPMDEDP-7834](https://jiraeu.epam.com/browse/EPMDEDP-7834)


<a name="v2.10.0"></a>
## [v2.10.0] - 2021-12-09
### Bug Fixes

- Fix provisioner code for import strategy [EPMDEDP-7893](https://jiraeu.epam.com/browse/EPMDEDP-7893)

### Code Refactoring

- Add namespace field in roleRef in OKD RB [EPMDEDP-7279](https://jiraeu.epam.com/browse/EPMDEDP-7279)
- Replace cluster-wide role/rolebinding to namespaced [EPMDEDP-7279](https://jiraeu.epam.com/browse/EPMDEDP-7279)
- Add Chart.lock with dependencies [EPMDEDP-7847](https://jiraeu.epam.com/browse/EPMDEDP-7847)
- Align helm chart to release process [EPMDEDP-7847](https://jiraeu.epam.com/browse/EPMDEDP-7847)

### Routine

- Update mkdocs to the latest stable [EPMDEDP-5906](https://jiraeu.epam.com/browse/EPMDEDP-5906)
- Bump EDP version in values.yaml [EPMDEDP-7847](https://jiraeu.epam.com/browse/EPMDEDP-7847)

### Documentation

- Job provisioner is responsible for the formation of Jenkinsfile [EPMDEDP-7136](https://jiraeu.epam.com/browse/EPMDEDP-7136)
- Update Keycloak installation guide [EPMDEDP-7598](https://jiraeu.epam.com/browse/EPMDEDP-7598)
- Add info on EDP upgrade [EPMDEDP-7635](https://jiraeu.epam.com/browse/EPMDEDP-7635)
- Move CI/CD Overview to IO pages [EPMDEDP-7698](https://jiraeu.epam.com/browse/EPMDEDP-7698)
- Move project rules to mkdocs [EPMDEDP-7777](https://jiraeu.epam.com/browse/EPMDEDP-7777)
- Update the links on GitHub [EPMDEDP-7781](https://jiraeu.epam.com/browse/EPMDEDP-7781)
- Fix typos in the Install EDP doc [EPMDEDP-7886](https://jiraeu.epam.com/browse/EPMDEDP-7886)
- Add info on the commit message for breaking changes [EPMDEDP-7937](https://jiraeu.epam.com/browse/EPMDEDP-7937)
- Remove symbol from commit message examples [EPMDEDP-7986](https://jiraeu.epam.com/browse/EPMDEDP-7986)

### BREAKING CHANGE:


Job provisioner create jenkinsfile and configure in jenkins pipeline as pipeline script.


<a name="v2.9.0"></a>
## [v2.9.0] - 2022-01-04

<a name="v2.8.4"></a>
## [v2.8.4] - 2022-01-04

<a name="v2.8.3"></a>
## [v2.8.3] - 2022-01-04

<a name="v2.8.2"></a>
## [v2.8.2] - 2022-01-04

<a name="v2.8.1"></a>
## [v2.8.1] - 2022-01-04

<a name="v2.8.0"></a>
## v2.8.0 - 2022-01-04

[Unreleased]: https://github.com/epam/edp-install/compare/v2.10.2...HEAD
[v2.10.2]: https://github.com/epam/edp-install/compare/v2.10.1...v2.10.2
[v2.10.1]: https://github.com/epam/edp-install/compare/v2.10.0...v2.10.1
[v2.10.0]: https://github.com/epam/edp-install/compare/v2.9.0...v2.10.0
[v2.9.0]: https://github.com/epam/edp-install/compare/v2.8.4...v2.9.0
[v2.8.4]: https://github.com/epam/edp-install/compare/v2.8.3...v2.8.4
[v2.8.3]: https://github.com/epam/edp-install/compare/v2.8.2...v2.8.3
[v2.8.2]: https://github.com/epam/edp-install/compare/v2.8.1...v2.8.2
[v2.8.1]: https://github.com/epam/edp-install/compare/v2.8.0...v2.8.1
