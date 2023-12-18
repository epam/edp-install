# Changelog

## Overview

This section provides details on edp-install release lifecycle.

_**NOTE**: For details on EDP releases, please refer to the [RELEASES.md](./RELEASES.md) page._


<a name="unreleased"></a>
## [Unreleased]


<a name="v3.7.0"></a>
## v3.7.0 - 2023-12-18
### Features

- Update secret labels type ([#74](https://github.com/epam/edp-install/issues/74))
- Make it possible to change tekton-cache parameters from edp-install chart ([#74](https://github.com/epam/edp-install/issues/74))
- Add connection status to the integration secret annotation ([#122](https://github.com/epam/edp-install/issues/122))
- Make VCS secret visible in EDP Portal ([#111](https://github.com/epam/edp-install/issues/111))
- Add public key for artifacts verification ([#103](https://github.com/epam/edp-install/issues/103))
- Add new Nexus icon, add deptrack and defectdojo links ([#104](https://github.com/epam/edp-install/issues/104))
- Migrate registry url from EDP component to edp-config configmap ([#98](https://github.com/epam/edp-install/issues/98))
- Implement integration with docker hub ([#43](https://github.com/epam/edp-install/issues/43))
- Add label to keycloak secret for visibility in EDP Portal ([#92](https://github.com/epam/edp-install/issues/92))
- Remove deprecated edpName parameter ([#76](https://github.com/epam/edp-install/issues/76))

### Bug Fixes

- Update docker registry component creation condition ([#118](https://github.com/epam/edp-install/issues/118))
- Remove sonar, nexus and keycloak operators as a part of edp-install ([#110](https://github.com/epam/edp-install/issues/110))
- Remove secretKey duplication from registry secrets ([#63](https://github.com/epam/edp-install/issues/63))

### Code Refactoring

- Migrate keycloak users under sso subsection ([#83](https://github.com/epam/edp-install/issues/83))
- Add parameter to disable keycloak-operator resource creation for argocd ([#79](https://github.com/epam/edp-install/issues/79))
- Align VCS secret name pattern ([#77](https://github.com/epam/edp-install/issues/77))
- Add parameter to disable keycloak-operator resource creation ([#79](https://github.com/epam/edp-install/issues/79))
- Disable sonar, nexus, gerrit operators deploy as part of edp-install ([#80](https://github.com/epam/edp-install/issues/80))
- Move tenancyEngine flag out of global section ([#9](https://github.com/epam/edp-install/issues/9))

### Routine

- Align helm charts versions ([#126](https://github.com/epam/edp-install/issues/126))
- Minimize mandatory parameters in values.yaml ([#121](https://github.com/epam/edp-install/issues/121))
- Update current development version ([#110](https://github.com/epam/edp-install/issues/110))
- Remove sonar, nexus and keycloak operators as a part of edp-install ([#110](https://github.com/epam/edp-install/issues/110))
- Upgrade pull request template ([#107](https://github.com/epam/edp-install/issues/107))
- Delete unused component ([#101](https://github.com/epam/edp-install/issues/101))
- Refactor edp-install keycloak-operator components creation ([#101](https://github.com/epam/edp-install/issues/101))
- Migrate component from targetRealm CR field ([#105](https://github.com/epam/edp-install/issues/105))
- Refactor edp-install keycloak-operator components creation ([#101](https://github.com/epam/edp-install/issues/101))
- Make possible use token instead RBAC for openshift registry ([#98](https://github.com/epam/edp-install/issues/98))
- Update CHANGELOG.md ([#85](https://github.com/epam/edp-install/issues/85))
- Disable SSO integration by default ([#89](https://github.com/epam/edp-install/issues/89))
- Remove artifacthub annotations related to images ([#85](https://github.com/epam/edp-install/issues/85))
- Update current development version ([#85](https://github.com/epam/edp-install/issues/85))
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

- Add Telemetry section ([#123](https://github.com/epam/edp-install/issues/123))
- Update the RELEASES.md file ([#123](https://github.com/epam/edp-install/issues/123))
- Add overview page for multi-tenancy ([#124](https://github.com/epam/edp-install/issues/124))
- Align developer Guide page titles ([#119](https://github.com/epam/edp-install/issues/119))
- Update deployment diagram section ([#120](https://github.com/epam/edp-install/issues/120))
- Update reference architecture document ([#120](https://github.com/epam/edp-install/issues/120))
- Add reference architecture documents ([#120](https://github.com/epam/edp-install/issues/120))
- Fix Document is empty issue for price page ([#108](https://github.com/epam/edp-install/issues/108))
- Move FAQ section under Operator Guide ([#108](https://github.com/epam/edp-install/issues/108))
- Add pricing page ([#108](https://github.com/epam/edp-install/issues/108))
- Update docs framework to the latest stable version ([#31](https://github.com/epam/edp-install/issues/31))
- Upgrade EDP from v3.5 to v3.6 ([#115](https://github.com/epam/edp-install/issues/115))
- Tekton VCS integration hot fix ([#116](https://github.com/epam/edp-install/issues/116))
- Update the Supported Versions page ([#116](https://github.com/epam/edp-install/issues/116))
- Align VCS integration in mkdocs ([#116](https://github.com/epam/edp-install/issues/116))
- Artifacts Verification page hot fix ([#117](https://github.com/epam/edp-install/issues/117))
- Fix typos in RELEASES md ([#112](https://github.com/epam/edp-install/issues/112))
- Move Google Analytics code injection to build phase ([#112](https://github.com/epam/edp-install/issues/112))
- Update the RELEASES md file ([#112](https://github.com/epam/edp-install/issues/112))
- Update mkdocs to version 9.4.8 ([#113](https://github.com/epam/edp-install/issues/113))
- Update artifacts verification page ([#103](https://github.com/epam/edp-install/issues/103))
- Update tekton installation guide ([#110](https://github.com/epam/edp-install/issues/110))
- Artifacts Verification hot fix ([#103](https://github.com/epam/edp-install/issues/103))
- Fix Artifacts Verification page naming in mkdocs ([#103](https://github.com/epam/edp-install/issues/103))
- Describe artifacts verification ([#103](https://github.com/epam/edp-install/issues/103))
- Add registry description to values.yaml ([#110](https://github.com/epam/edp-install/issues/110))
- Update the Align Jira Integration page ([#108](https://github.com/epam/edp-install/issues/108))
- Update the External Secrets Operator Integration page ([#106](https://github.com/epam/edp-install/issues/106))
- Update Roadmap section ([#109](https://github.com/epam/edp-install/issues/109))
- Fix link on page ([#109](https://github.com/epam/edp-install/issues/109))
- Remove the rest of Jenkins mentions from mkdocs ([#109](https://github.com/epam/edp-install/issues/109))
- Remove Jenkins Scenario from mkdocs ([#109](https://github.com/epam/edp-install/issues/109))
- Update the user guide section ([#100](https://github.com/epam/edp-install/issues/100))
- Consolidate the description of codebase types and strategies ([#100](https://github.com/epam/edp-install/issues/100))
- Update the Secure Delivery on the Platform page in mkdocs ([#81](https://github.com/epam/edp-install/issues/81))
- Add mkdocs-meta-descriptions-plugin support ([#91](https://github.com/epam/edp-install/issues/91))
- Remove deprecated pages ([#91](https://github.com/epam/edp-install/issues/91))
- Add BingSite verification ([#100](https://github.com/epam/edp-install/issues/100))
- Remove deprecated components from the README ([#47](https://github.com/epam/edp-install/issues/47))
- Describe environment customization using edp-gitops extensions ([#102](https://github.com/epam/edp-install/issues/102))
- Update EKS installation documentation ([#88](https://github.com/epam/edp-install/issues/88))
- Add a link to a video of the EDP Nexus integration ([#99](https://github.com/epam/edp-install/issues/99))
- Align edp-install guide ([#97](https://github.com/epam/edp-install/issues/97))
- Add a link to a video of the EDP SonarQube integration ([#99](https://github.com/epam/edp-install/issues/99))
- Remove link to a video of the installation process from AWS Marketplace ([#75](https://github.com/epam/edp-install/issues/75))
- Add the Capsule Integration page to mkdocs ([#93](https://github.com/epam/edp-install/issues/93))
- Upgrade EDP from v3.4 to v3.5 ([#94](https://github.com/epam/edp-install/issues/94))
- Add a link to a video of the EDP GitHub integration ([#96](https://github.com/epam/edp-install/issues/96))
- Add security disclaimer before installation ([#95](https://github.com/epam/edp-install/issues/95))
- Update the RELEASES md file ([#90](https://github.com/epam/edp-install/issues/90))
- Add a link to a video of the installation process from AWS Marketplace ([#75](https://github.com/epam/edp-install/issues/75))
- Update the RELEASES md file ([#90](https://github.com/epam/edp-install/issues/90))
- Align VCS secret pattern([#65](https://github.com/epam/edp-install/issues/65))
- Align Nexus secret for the new approach([#65](https://github.com/epam/edp-install/issues/65))
- Align Sonarqube secret for the new approach([#65](https://github.com/epam/edp-install/issues/65))
- Align jira secret for the new approach([#65](https://github.com/epam/edp-install/issues/65))
- Align DefectDojo secret for the new approach([#65](https://github.com/epam/edp-install/issues/65))
- Align the Install EDP via AWS Marketplace page ([#75](https://github.com/epam/edp-install/issues/75))
- Upgrade EDP to v3.3 to 3.4 hot fix ([#63](https://github.com/epam/edp-install/issues/63))
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


[Unreleased]: https://github.com/epam/edp-install/compare/v3.7.0...HEAD
