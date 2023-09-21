# Changelog

## Overview

This section provides details on edp-install release lifecycle.

_**NOTE**: For details on EDP releases, please refer to the [RELEASES.md](./RELEASES.md) page._


<a name="unreleased"></a>
## [Unreleased]


<a name="v3.5.0"></a>
## [v3.5.0] - 2023-09-21
### Features

- Remove deprecated edpName parameter ([#76](https://github.com/epam/edp-install/issues/76))

### Code Refactoring

- Migrate keycloak users under sso subsection ([#83](https://github.com/epam/edp-install/issues/83))
- Add parameter to disable keycloak-operator resource creation for argocd ([#79](https://github.com/epam/edp-install/issues/79))
- Align VCS secret name pattern ([#77](https://github.com/epam/edp-install/issues/77))
- Add parameter to disable keycloak-operator resource creation ([#79](https://github.com/epam/edp-install/issues/79))
- Disable sonar, nexus, gerrit operators deploy as part of edp-install ([#80](https://github.com/epam/edp-install/issues/80))
- Move tenancyEngine flag out of global section ([#9](https://github.com/epam/edp-install/issues/9))

### Routine

- Align helm charts versions ([#85](https://github.com/epam/edp-install/issues/85))
- Use github as a default gitserver ([#87](https://github.com/epam/edp-install/issues/87))
- Update logic for create Sonar and nexus secret([#86](https://github.com/epam/edp-install/issues/86))
- Align secret name for new secret approach ([#65](https://github.com/epam/edp-install/issues/65))
- Remove sonar_url and nexus_url depricated values ([#65](https://github.com/epam/edp-install/issues/65))
- Align external-secret operator provisioning ([#65](https://github.com/epam/edp-install/issues/65))
- Remove unnecessary kaniko logic for Jenkins templates ([#73](https://github.com/epam/edp-install/issues/73))
- Rename EDP-Component ([#61](https://github.com/epam/edp-install/issues/61))
- Update current development version ([#49](https://github.com/epam/edp-install/issues/49))
- Disable validateMaintainers by default for ct lint ([#49](https://github.com/epam/edp-install/issues/49))
- Update External Secret integration guide ([#55](https://github.com/epam/edp-install/issues/55))
- Update jira-integration documentation ([#56](https://github.com/epam/edp-install/issues/56))
- Update current development version ([#47](https://github.com/epam/edp-install/issues/47))

### Documentation

- Update the EDP Project Rules. Working Process page ([#82](https://github.com/epam/edp-install/issues/82))
- DependencyTrack integration ([#78](https://github.com/epam/edp-install/issues/78))
- Update the Install via AWS Marketplace page ([#75](https://github.com/epam/edp-install/issues/75))
- Rename edp namespace in the only right way ([#71](https://github.com/epam/edp-install/issues/71))
- Describe AWS Marketplace Install flow ([#75](https://github.com/epam/edp-install/issues/75))
- Add the Add-Ons Overview page to mkdocs ([#62](https://github.com/epam/edp-install/issues/62))
- Update the Supported Versions page and Manage Namespace pages ([#71](https://github.com/epam/edp-install/issues/71))
- Update defect dojo integration for use ci-user instead of admin ([#70](https://github.com/epam/edp-install/issues/70))
- Add article to upgrade EDP to 3.4 version ([#57](https://github.com/epam/edp-install/issues/57))
- Align edp-install documentation ([#58](https://github.com/epam/edp-install/issues/58))
- Align Harbor integration ([#54](https://github.com/epam/edp-install/issues/54))
- Align DefectDojo integration ([#50](https://github.com/epam/edp-install/issues/50))
- Put correct URL rewrites for old pages ([#60](https://github.com/epam/edp-install/issues/60))
- Ensure we still can service traffic on old URL ([#60](https://github.com/epam/edp-install/issues/60))
- Remove the headlamp prefix from documentation materials ([#60](https://github.com/epam/edp-install/issues/60))
- Bump tekton version ([#59](https://github.com/epam/edp-install/issues/59))
- Align SonarQube integration([#52](https://github.com/epam/edp-install/issues/52))
- Align Nexus Sonatype integration([#53](https://github.com/epam/edp-install/issues/53))
- Update the RELEASES.md file ([#51](https://github.com/epam/edp-install/issues/51))
- Install and integration with external Nexus Sonatype ([#53](https://github.com/epam/edp-install/issues/53))
- Align SonarQube integration ([#52](https://github.com/epam/edp-install/issues/52))
- Align DefectDojo integration ([#50](https://github.com/epam/edp-install/issues/50))


<a name="v3.4.1"></a>
## [v3.4.1] - 2023-08-28
### Routine

- Align helm charts versions ([#49](https://github.com/epam/edp-install/issues/49))
- Disable validateMaintainers by default for ct lint ([#49](https://github.com/epam/edp-install/issues/49))


<a name="v3.4.0"></a>
## [v3.4.0] - 2023-08-18

[Unreleased]: https://github.com/epam/edp-install/compare/v3.5.0...HEAD
[v3.5.0]: https://github.com/epam/edp-install/compare/v3.4.1...v3.5.0
[v3.4.1]: https://github.com/epam/edp-install/compare/v3.4.0...v3.4.1
[v3.4.0]: https://github.com/epam/edp-install/compare/v3.3.0...v3.4.0
