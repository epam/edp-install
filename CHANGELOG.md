# Changelog

## Overview

This section provides details on edp-install release lifecycle.

_**NOTE**: For details on EDP releases, please refer to the [RELEASES.md](./RELEASES.md) page._


<a name="unreleased"></a>
## [Unreleased]

### Code Refactoring

- Add keycloak resource [EPMDEDP-11369](https://jiraeu.epam.com/browse/EPMDEDP-11369)

### Routine

- Align helm charts versions [EPMDEDP-11260](https://jiraeu.epam.com/browse/EPMDEDP-11260)
- Update Keycloak version in helmfiles [EPMDEDP-11413](https://jiraeu.epam.com/browse/EPMDEDP-11413)

### Documentation

- Address markdown linting issues [EPMDEDP-10](https://jiraeu.epam.com/browse/EPMDEDP-10)
- Desciribe the Tekton Overview page [EPMDEDP-10634](https://jiraeu.epam.com/browse/EPMDEDP-10634)
- Add Landing Page [EPMDEDP-10808](https://jiraeu.epam.com/browse/EPMDEDP-10808)
- Update roadmap section [EPMDEDP-10808](https://jiraeu.epam.com/browse/EPMDEDP-10808)
- Describe Headlamp OIDC integration [EPMDEDP-11135](https://jiraeu.epam.com/browse/EPMDEDP-11135)
- Update mkdocs framework version [EPMDEDP-11238](https://jiraeu.epam.com/browse/EPMDEDP-11238)
- Add page feedback feature [EPMDEDP-11238](https://jiraeu.epam.com/browse/EPMDEDP-11238)
- Update Headlamp guide [EPMDEDP-11238](https://jiraeu.epam.com/browse/EPMDEDP-11238)
- Upgrade EDP to 3.1 [EPMDEDP-11260](https://jiraeu.epam.com/browse/EPMDEDP-11260)
- Update edp-install release version [EPMDEDP-11260](https://jiraeu.epam.com/browse/EPMDEDP-11260)
- Update article to upgrade EDP to 3.0.x version [EPMDEDP-11260](https://jiraeu.epam.com/browse/EPMDEDP-11260)
- Describe Argo CD with import strategy integration [EPMDEDP-11299](https://jiraeu.epam.com/browse/EPMDEDP-11299)
- Update RELEASES.md file [EPMDEDP-11307](https://jiraeu.epam.com/browse/EPMDEDP-11307)
- Describe installation of Amazon EBS CSI Driver [EPMDEDP-11319](https://jiraeu.epam.com/browse/EPMDEDP-11319)
- Upgrade EDP to 2.12.2 [EPMDEDP-11319](https://jiraeu.epam.com/browse/EPMDEDP-11319)
- Update RELEASES.md file [EPMDEDP-11319](https://jiraeu.epam.com/browse/EPMDEDP-11319)
- Add article 'Migrate CI Pipelines From Jenkins to Tekton' [EPMDEDP-11335](https://jiraeu.epam.com/browse/EPMDEDP-11335)
- Update Argo CD to 2.5.8 version [EPMDEDP-11408](https://jiraeu.epam.com/browse/EPMDEDP-11408)
- Update Keycloak installation guide [EPMDEDP-11413](https://jiraeu.epam.com/browse/EPMDEDP-11413)
- Update prerequisites page [EPMDEDP-11425](https://jiraeu.epam.com/browse/EPMDEDP-11425)
- Remove extra tables from the prerequisites page [EPMDEDP-11425](https://jiraeu.epam.com/browse/EPMDEDP-11425)


<a name="v3.1.0"></a>
## [v3.1.0] - 2023-01-24
### Features

- Implement kiosk install via helmfile [EPMDEDP-11164](https://jiraeu.epam.com/browse/EPMDEDP-11164)

### Bug Fixes

- Fix keycloak-external secret typo [EPMDEDP-10610](https://jiraeu.epam.com/browse/EPMDEDP-10610)
- Fix quoting for perf helm values [EPMDEDP-11163](https://jiraeu.epam.com/browse/EPMDEDP-11163)
- Add link for kiosk chart to helmfile [EPMDEDP-11164](https://jiraeu.epam.com/browse/EPMDEDP-11164)
- Fix library versions compatibility [EPMDEDP-11264](https://jiraeu.epam.com/browse/EPMDEDP-11264)

### Routine

- Update mkdocs framework [EPMDEDP-11129](https://jiraeu.epam.com/browse/EPMDEDP-11129)
- Remove Perf intergration Helm values duplicates [EPMDEDP-11163](https://jiraeu.epam.com/browse/EPMDEDP-11163)
- Change command [EPMDEDP-11169](https://jiraeu.epam.com/browse/EPMDEDP-11169)
- Remove edp-db secrets maintenance [EPMDEDP-11194](https://jiraeu.epam.com/browse/EPMDEDP-11194)
- Align helm charts versions [EPMDEDP-11260](https://jiraeu.epam.com/browse/EPMDEDP-11260)
- Update mkdocs Dockerfile [EPMDEDP-11264](https://jiraeu.epam.com/browse/EPMDEDP-11264)

### Documentation

- Describe Kaniko cache prerequisites [EPMDEDP-10532](https://jiraeu.epam.com/browse/EPMDEDP-10532)
- Update the Releases.md file with the 3.0.0 version [EPMDEDP-10615](https://jiraeu.epam.com/browse/EPMDEDP-10615)
- Update the Manage Jenkins CD Pipeline Job [EPMDEDP-10935](https://jiraeu.epam.com/browse/EPMDEDP-10935)
- Report-Portal change the link of helm chart in mkdocs [EPMDEDP-10977](https://jiraeu.epam.com/browse/EPMDEDP-10977)
- Fix Indent correction in the code block [EPMDEDP-11012](https://jiraeu.epam.com/browse/EPMDEDP-11012)
- Selenoid (Moon) Installation by Helmfile [EPMDEDP-11012](https://jiraeu.epam.com/browse/EPMDEDP-11012)
- Describe Tekton import strategy [EPMDEDP-11062](https://jiraeu.epam.com/browse/EPMDEDP-11062)
- Update index page [EPMDEDP-11129](https://jiraeu.epam.com/browse/EPMDEDP-11129)
- Add Storage Classes into Cluster settings [EPMDEDP-11169](https://jiraeu.epam.com/browse/EPMDEDP-11169)
- Update installation EDP guide [EPMDEDP-11170](https://jiraeu.epam.com/browse/EPMDEDP-11170)
- Update ArgoCD page [EPMDEDP-11171](https://jiraeu.epam.com/browse/EPMDEDP-11171)
- Update Headlamp User Guide [EPMDEDP-11182](https://jiraeu.epam.com/browse/EPMDEDP-11182)
- Add Install Tekton resources page [EPMDEDP-11203](https://jiraeu.epam.com/browse/EPMDEDP-11203)
- Update EDP install documentation [EPMDEDP-11216](https://jiraeu.epam.com/browse/EPMDEDP-11216)
- Add article to upgrade EDP to 3.0.x version [EPMDEDP-11220](https://jiraeu.epam.com/browse/EPMDEDP-11220)
- Install Tekton operator v0.64.0 on Openshift [EPMDEDP-11290](https://jiraeu.epam.com/browse/EPMDEDP-11290)
- Add scenarios for EDP install guide [EPMDEDP-11317](https://jiraeu.epam.com/browse/EPMDEDP-11317)


<a name="v3.0.0"></a>
## [v3.0.0] - 2022-12-19
### Features

- Use external-secrets to create clientSecret for edp-headlamp [EPMDEDP-10539](https://jiraeu.epam.com/browse/EPMDEDP-10539)
- Add ReportPortal deployment into the Helmfile [EPMDEDP-10663](https://jiraeu.epam.com/browse/EPMDEDP-10663)
- Add access token to Import strategy [EPMDEDP-10708](https://jiraeu.epam.com/browse/EPMDEDP-10708)
- Add gitlab, github secrets provisioning [EPMDEDP-11119](https://jiraeu.epam.com/browse/EPMDEDP-11119)

### Bug Fixes

- Fix keycloak-external secret typo [EPMDEDP-10610](https://jiraeu.epam.com/browse/EPMDEDP-10610)
- Change memory request to 2Gi for elasticsearch of report-portal [EPMDEDP-10663](https://jiraeu.epam.com/browse/EPMDEDP-10663)

### Code Refactoring

- Remove database related resources [EPMDEDP-10751](https://jiraeu.epam.com/browse/EPMDEDP-10751)

### Routine

- Enable edp-argocd-operator as subcomponent [EPMDEDP-10274](https://jiraeu.epam.com/browse/EPMDEDP-10274)
- Update current development version [EPMDEDP-10274](https://jiraeu.epam.com/browse/EPMDEDP-10274)
- Remove all records with edp-architecture github repo [EPMDEDP-10520](https://jiraeu.epam.com/browse/EPMDEDP-10520)
- Align helm charts versions [EPMDEDP-10610](https://jiraeu.epam.com/browse/EPMDEDP-10610)
- Bump release envs version [EPMDEDP-10610](https://jiraeu.epam.com/browse/EPMDEDP-10610)
- Bump jenkins-operator chart version [EPMDEDP-10610](https://jiraeu.epam.com/browse/EPMDEDP-10610)
- Disable init-db job if not using database [EPMDEDP-10621](https://jiraeu.epam.com/browse/EPMDEDP-10621)
- Remove edp-argocd-operator [EPMDEDP-10642](https://jiraeu.epam.com/browse/EPMDEDP-10642)
- Move gerrit sshPort to global section [EPMDEDP-10642](https://jiraeu.epam.com/browse/EPMDEDP-10642)
- Enable edp-tekton as subcomponent [EPMDEDP-10660](https://jiraeu.epam.com/browse/EPMDEDP-10660)
- Add SCC and gateway for Report-Portal [EPMDEDP-10663](https://jiraeu.epam.com/browse/EPMDEDP-10663)
- Remove the reconciler from helm chart and Readme file [EPMDEDP-10762](https://jiraeu.epam.com/browse/EPMDEDP-10762)
- Bump DefectDojo from 2.16.2 to 2.17.0 [EPMDEDP-10769](https://jiraeu.epam.com/browse/EPMDEDP-10769)
- Bump up DefectDojo helm version to 1.6.46 [EPMDEDP-10769](https://jiraeu.epam.com/browse/EPMDEDP-10769)
- Update current development version [EPMDEDP-10805](https://jiraeu.epam.com/browse/EPMDEDP-10805)
- Update Argo CD version [EPMDEDP-10840](https://jiraeu.epam.com/browse/EPMDEDP-10840)
- Add appproject creation if Argo CD integration enabled [EPMDEDP-10853](https://jiraeu.epam.com/browse/EPMDEDP-10853)
- Revert Add appproject creation if Argo CD integration enabled [EPMDEDP-10853](https://jiraeu.epam.com/browse/EPMDEDP-10853)
- Bump edp-tekton and edp-headlamp chart versions [EPMDEDP-11087](https://jiraeu.epam.com/browse/EPMDEDP-11087)
- Bump edp-tekton chart version [EPMDEDP-11087](https://jiraeu.epam.com/browse/EPMDEDP-11087)
- Bump edp-tekton chart version [EPMDEDP-11087](https://jiraeu.epam.com/browse/EPMDEDP-11087)
- Bump edp-tekton chart version [EPMDEDP-11087](https://jiraeu.epam.com/browse/EPMDEDP-11087)
- Bump edp-headlamp chart version [EPMDEDP-11087](https://jiraeu.epam.com/browse/EPMDEDP-11087)
- Bump edp-tekton chart version [EPMDEDP-11087](https://jiraeu.epam.com/browse/EPMDEDP-11087)
- Align edp-tekton chart version [EPMDEDP-11087](https://jiraeu.epam.com/browse/EPMDEDP-11087)
- Bump edp-tekton chart version [EPMDEDP-11087](https://jiraeu.epam.com/browse/EPMDEDP-11087)
- Bump up Kaniko to latest stable version [EPMDEDP-11088](https://jiraeu.epam.com/browse/EPMDEDP-11088)
- Enable exretnal secrets for Jira integration [EPMDEDP-11119](https://jiraeu.epam.com/browse/EPMDEDP-11119)
- Add secretString field to gitProvider [EPMDEDP-11119](https://jiraeu.epam.com/browse/EPMDEDP-11119)
- Disable jenkins-operator and admin-console-operator by default [EPMDEDP-11119](https://jiraeu.epam.com/browse/EPMDEDP-11119)
- Bump helm chart version of ingress-nginx to 4.4.0 [EPMDEDP-11131](https://jiraeu.epam.com/browse/EPMDEDP-11131)
- Bump helm chart version of external-secrets to 0.6.1 [EPMDEDP-11131](https://jiraeu.epam.com/browse/EPMDEDP-11131)
- Update Keycloak version [EPMDEDP-11156](https://jiraeu.epam.com/browse/EPMDEDP-11156)
- Remove edp-db secrets maintenance [EPMDEDP-11194](https://jiraeu.epam.com/browse/EPMDEDP-11194)

### Documentation

- Describe SSL automation with cert-manager on OKD [EPMDEDP-10089](https://jiraeu.epam.com/browse/EPMDEDP-10089)
- Create documentation describing the Logsight implementation [EPMDEDP-10254](https://jiraeu.epam.com/browse/EPMDEDP-10254)
- Update EDP version in documentation [EPMDEDP-10274](https://jiraeu.epam.com/browse/EPMDEDP-10274)
- Update the Releases.md file with the 2.12.0 version [EPMDEDP-10304](https://jiraeu.epam.com/browse/EPMDEDP-10304)
- Add documentation on Keycloak upgrade to 19.0.1 [EPMDEDP-10382](https://jiraeu.epam.com/browse/EPMDEDP-10382)
- Improve Helmfile documentation [EPMDEDP-10390](https://jiraeu.epam.com/browse/EPMDEDP-10390)
- Add OpenShift resources for EDP installation [EPMDEDP-10390](https://jiraeu.epam.com/browse/EPMDEDP-10390)
- How to change the build tool for Container Library [EPMDEDP-10393](https://jiraeu.epam.com/browse/EPMDEDP-10393)
- Add recreating RoleBinding and warning for older versions for Update EDP to 2.12.x [EPMDEDP-10398](https://jiraeu.epam.com/browse/EPMDEDP-10398)
- Fix formatting issue [EPMDEDP-10398](https://jiraeu.epam.com/browse/EPMDEDP-10398)
- Add section how to update EDP to 2.12.x [EPMDEDP-10398](https://jiraeu.epam.com/browse/EPMDEDP-10398)
- Update the Add Lib page in the AC guide [EPMDEDP-10443](https://jiraeu.epam.com/browse/EPMDEDP-10443)
- Language review fixes on EDP installation (OpenShift) [EPMDEDP-10448](https://jiraeu.epam.com/browse/EPMDEDP-10448)
- Update the Customize CI Pipeline Documentation [EPMDEDP-10495](https://jiraeu.epam.com/browse/EPMDEDP-10495)
- Update RoadMap [EPMDEDP-10568](https://jiraeu.epam.com/browse/EPMDEDP-10568)
- Update EKS deploy steps [EPMDEDP-10585](https://jiraeu.epam.com/browse/EPMDEDP-10585)
- Rewrite GitLab and GitHub integration [EPMDEDP-10606](https://jiraeu.epam.com/browse/EPMDEDP-10606)
- Fix secret format for import strategy [EPMDEDP-10708](https://jiraeu.epam.com/browse/EPMDEDP-10708)
- Switch from v1alpha1 to v1 [EPMDEDP-10708](https://jiraeu.epam.com/browse/EPMDEDP-10708)
- Add note about gitProvider in GitServer spec [EPMDEDP-10743](https://jiraeu.epam.com/browse/EPMDEDP-10743)
- Fix 'EDP Main Repositories' table [EPMDEDP-10762](https://jiraeu.epam.com/browse/EPMDEDP-10762)
- Align EDP install documentation to 2.12.1 and minor corrections [EPMDEDP-10770](https://jiraeu.epam.com/browse/EPMDEDP-10770)
- Describe Headlamp functionality [EPMDEDP-10795](https://jiraeu.epam.com/browse/EPMDEDP-10795)
- Add a link to the edp-tekton repository into the Readme file [EPMDEDP-10805](https://jiraeu.epam.com/browse/EPMDEDP-10805)
- Updated Java 8 Gradle and Maven agents versions, blurred account ID on screenshot [EPMDEDP-10805](https://jiraeu.epam.com/browse/EPMDEDP-10805)
- Update RELEASES.md for 2.12.1 [EPMDEDP-10806](https://jiraeu.epam.com/browse/EPMDEDP-10806)
- Deploy ReportPortal via helm chart and helmfile to OpenShift cluster [EPMDEDP-10811](https://jiraeu.epam.com/browse/EPMDEDP-10811)
- Use out of the box Argo CD functional instead of edp-operator [EPMDEDP-10841](https://jiraeu.epam.com/browse/EPMDEDP-10841)
- Update Headlamp User Guide [EPMDEDP-10910](https://jiraeu.epam.com/browse/EPMDEDP-10910)
- Update the Manage Jenkins CD Pipeline Job [EPMDEDP-10935](https://jiraeu.epam.com/browse/EPMDEDP-10935)
- Update the Add Custom Global Pipeline Lib document [EPMDEDP-10975](https://jiraeu.epam.com/browse/EPMDEDP-10975)
- ReportPortal Enable SAML integration [EPMDEDP-10981](https://jiraeu.epam.com/browse/EPMDEDP-10981)
- Add how to change an Access Token Lifespan to FAQ page [EPMDEDP-10995](https://jiraeu.epam.com/browse/EPMDEDP-10995)
- Add Report Portal integration with Tekton [EPMDEDP-11011](https://jiraeu.epam.com/browse/EPMDEDP-11011)
- Update Argo CD pages, update Argo CD SCC for Openshift [EPMDEDP-11020](https://jiraeu.epam.com/browse/EPMDEDP-11020)
- Remove public key from import stategy [EPMDEDP-11044](https://jiraeu.epam.com/browse/EPMDEDP-11044)
- Update Argo CD version, pin version of helm charts [EPMDEDP-11108](https://jiraeu.epam.com/browse/EPMDEDP-11108)


<a name="v2.12.2"></a>
## [v2.12.2] - 2023-02-03
### Routine

- Update EDP release 2.12.1 to 2.12.2 [EPMDEDP-11319](https://jiraeu.epam.com/browse/EPMDEDP-11319)

### Documentation

- Update Changelog for 2.12.1 and update RELEASES.md for 2.12.0 and 2.12.1 [EPMDEDP-10805](https://jiraeu.epam.com/browse/EPMDEDP-10805)


<a name="v2.12.1"></a>
## [v2.12.1] - 2022-10-28
### Routine

- Align helm charts versions [EPMDEDP-10805](https://jiraeu.epam.com/browse/EPMDEDP-10805)


<a name="v2.12.0"></a>
## [v2.12.0] - 2022-08-27
### Features

- Download required tools for Makefile targets [EPMDEDP-10105](https://jiraeu.epam.com/browse/EPMDEDP-10105)
- Enable Keycloak SSO for DefectDojo [EPMDEDP-10234](https://jiraeu.epam.com/browse/EPMDEDP-10234)
- Provision DefectDojo CI token with ExternalSecrets [EPMDEDP-10234](https://jiraeu.epam.com/browse/EPMDEDP-10234)
- Add edp-headlamp as subcomponent [EPMDEDP-10336](https://jiraeu.epam.com/browse/EPMDEDP-10336)
- Add capability to use ExternalSecrets [EPMDEDP-8314](https://jiraeu.epam.com/browse/EPMDEDP-8314)

### Bug Fixes

- Fix oidc group name for ArgoCD admins [EPMDEDP-9231](https://jiraeu.epam.com/browse/EPMDEDP-9231)
- Fix ArgoCD client name [EPMDEDP-9231](https://jiraeu.epam.com/browse/EPMDEDP-9231)

### Code Refactoring

- Use edp-component with v1 [EPMDEDP-10155](https://jiraeu.epam.com/browse/EPMDEDP-10155)
- Remove unnecessary resource check [EPMDEDP-10228](https://jiraeu.epam.com/browse/EPMDEDP-10228)
- Use repository and tag for image reference in chart [EPMDEDP-10389](https://jiraeu.epam.com/browse/EPMDEDP-10389)

### Routine

- Update ArgoCD to version v2.4.0 [EPMDEDP-10090](https://jiraeu.epam.com/browse/EPMDEDP-10090)
- Align keycloak CRs to the latest changes [EPMDEDP-10090](https://jiraeu.epam.com/browse/EPMDEDP-10090)
- Fix Jira Ticket pattern for changelog generator [EPMDEDP-10159](https://jiraeu.epam.com/browse/EPMDEDP-10159)
- Disable a TLS secret and change a volume size in the Defectdojo [EPMDEDP-10234](https://jiraeu.epam.com/browse/EPMDEDP-10234)
- Align helm charts versions [EPMDEDP-10274](https://jiraeu.epam.com/browse/EPMDEDP-10274)
- Enable edp-argocd-operator as subcomponent [EPMDEDP-10274](https://jiraeu.epam.com/browse/EPMDEDP-10274)
- Update kaniko images [EPMDEDP-10275](https://jiraeu.epam.com/browse/EPMDEDP-10275)
- Upgrade mkdocs to v8.4.0 [EPMDEDP-10283](https://jiraeu.epam.com/browse/EPMDEDP-10283)
- Fix mdx_truly_sane_lists issue when build mkdocs [EPMDEDP-10309](https://jiraeu.epam.com/browse/EPMDEDP-10309)
- Change 'go get' to 'go install' for git-chglog [EPMDEDP-10337](https://jiraeu.epam.com/browse/EPMDEDP-10337)
- Remove VERSION file [EPMDEDP-10387](https://jiraeu.epam.com/browse/EPMDEDP-10387)
- Add SecurityContextConstraints for kaniko service account [EPMDEDP-10393](https://jiraeu.epam.com/browse/EPMDEDP-10393)
- Add ExternalSecret for Kaniko docker config [EPMDEDP-10394](https://jiraeu.epam.com/browse/EPMDEDP-10394)
- Remove extra comma from list of stages [EPMDEDP-10394](https://jiraeu.epam.com/browse/EPMDEDP-10394)
- Remove Kubernetes and GitOps libraries stages from job provisioners [EPMDEDP-10397](https://jiraeu.epam.com/browse/EPMDEDP-10397)
- Update development version [EPMDEDP-8832](https://jiraeu.epam.com/browse/EPMDEDP-8832)
- Update changelog [EPMDEDP-9231](https://jiraeu.epam.com/browse/EPMDEDP-9231)
- Introduce helmfile as an approach for EDP ecosystem deployment [EPMDEDP-9231](https://jiraeu.epam.com/browse/EPMDEDP-9231)
- Update chart annotation [EPMDEDP-9515](https://jiraeu.epam.com/browse/EPMDEDP-9515)

### Documentation

- Changed the version of Ingress-nginx to 4.1.4 [EPMDEDP-10039](https://jiraeu.epam.com/browse/EPMDEDP-10039)
- Upgrade Keycloak version [EPMDEDP-10050](https://jiraeu.epam.com/browse/EPMDEDP-10050)
- Add Document on Deploying OKD Cluster [EPMDEDP-10097](https://jiraeu.epam.com/browse/EPMDEDP-10097)
- Add link to helmfile template, describe watchIngressWithoutClass parameter [EPMDEDP-10153](https://jiraeu.epam.com/browse/EPMDEDP-10153)
- Update release version [EPMDEDP-10158](https://jiraeu.epam.com/browse/EPMDEDP-10158)
- Add installation of DefectDojo [EPMDEDP-10234](https://jiraeu.epam.com/browse/EPMDEDP-10234)
- Add SAST stage into maven, gradle, and go builds of all CI provisioners [EPMDEDP-10234](https://jiraeu.epam.com/browse/EPMDEDP-10234)
- Update Roadmap section [EPMDEDP-10274](https://jiraeu.epam.com/browse/EPMDEDP-10274)
- Update link [EPMDEDP-10274](https://jiraeu.epam.com/browse/EPMDEDP-10274)
- Align README.md [EPMDEDP-10274](https://jiraeu.epam.com/browse/EPMDEDP-10274)
- Use aws instead of awscliv2 [EPMDEDP-10276](https://jiraeu.epam.com/browse/EPMDEDP-10276)
- Update the list of components [EPMDEDP-10319](https://jiraeu.epam.com/browse/EPMDEDP-10319)
- EDP integration with ArgoCD [EPMDEDP-10325](https://jiraeu.epam.com/browse/EPMDEDP-10325)
- How to deploy OKD 4.10 cluster [EPMDEDP-10330](https://jiraeu.epam.com/browse/EPMDEDP-10330)
- Add information related to Gerrit upgrade flow [EPMDEDP-10335](https://jiraeu.epam.com/browse/EPMDEDP-10335)
- Upgrade Keycloak to 19.0.1 version [EPMDEDP-10358](https://jiraeu.epam.com/browse/EPMDEDP-10358)
- Enable Keycloak deploy on OpenShift cluster [EPMDEDP-10358](https://jiraeu.epam.com/browse/EPMDEDP-10358)
- Fixing formatting for Enable Keycloak deploy on OpenShift cluster [EPMDEDP-10358](https://jiraeu.epam.com/browse/EPMDEDP-10358)
- Fixing links in the Helmfiles README [EPMDEDP-10358](https://jiraeu.epam.com/browse/EPMDEDP-10358)
- Fixing links in the Helmfiles README [EPMDEDP-10358](https://jiraeu.epam.com/browse/EPMDEDP-10358)
- Add overview SAST, Semgrep, and manage scanner pages [EPMDEDP-10383](https://jiraeu.epam.com/browse/EPMDEDP-10383)
- Add support 'platform type' in the default CI job provisioner [EPMDEDP-10393](https://jiraeu.epam.com/browse/EPMDEDP-10393)
- Add Kubernetes and GitOps library stages to job provisioners [EPMDEDP-8257](https://jiraeu.epam.com/browse/EPMDEDP-8257)
- Add external-secrets operator installation doc [EPMDEDP-8314](https://jiraeu.epam.com/browse/EPMDEDP-8314)
- How to work with External Secret Operator in EDP [EPMDEDP-8314](https://jiraeu.epam.com/browse/EPMDEDP-8314)
- Describe EDP upgrade procedure [EPMDEDP-8832](https://jiraeu.epam.com/browse/EPMDEDP-8832)
- Update the Releases.md file [EPMDEDP-8838](https://jiraeu.epam.com/browse/EPMDEDP-8838)
- Add plugin which need to update during update procedure [EPMDEDP-8923](https://jiraeu.epam.com/browse/EPMDEDP-8923)
- Update OIDC Lens integration section [EPMDEDP-9211](https://jiraeu.epam.com/browse/EPMDEDP-9211)
- Add OIDC Lens integration section, update prerequisites [EPMDEDP-9211](https://jiraeu.epam.com/browse/EPMDEDP-9211)
- Add Readme file for helmfile [EPMDEDP-9231](https://jiraeu.epam.com/browse/EPMDEDP-9231)


<a name="v2.11.1"></a>
## [v2.11.1] - 2022-06-30
### Routine

- Align helm charts versions [EPMDEDP-10158](https://jiraeu.epam.com/browse/EPMDEDP-10158)
- Backport Makefile from master branch [EPMDEDP-10158](https://jiraeu.epam.com/browse/EPMDEDP-10158)
- Update changelog [EPMDEDP-9469](https://jiraeu.epam.com/browse/EPMDEDP-9469)
- Update chart annotation [EPMDEDP-9515](https://jiraeu.epam.com/browse/EPMDEDP-9515)

### Documentation

- Update the Releases.md file [EPMDEDP-8838](https://jiraeu.epam.com/browse/EPMDEDP-8838)


<a name="v2.11.0"></a>
## [v2.11.0] - 2022-05-26
### Features

- Make image scaling on I/O pages [EPMDEDP-7726](https://jiraeu.epam.com/browse/EPMDEDP-7726)
- Update Makefile changelog target [EPMDEDP-8218](https://jiraeu.epam.com/browse/EPMDEDP-8218)
- Provision argocd client in Keycloak Enable [EPMDEDP-8257](https://jiraeu.epam.com/browse/EPMDEDP-8257)
- Parametrize ENV variables for codebase-operator deployment [EPMDEDP-8268](https://jiraeu.epam.com/browse/EPMDEDP-8268)
- Implement PlantUML integration [EPMDEDP-8303](https://jiraeu.epam.com/browse/EPMDEDP-8303)
- Add vega-lite chart plugin to MKDocs [EPMDEDP-8303](https://jiraeu.epam.com/browse/EPMDEDP-8303)
- Implement Mermaid diagrams integration with MKDocs [EPMDEDP-8303](https://jiraeu.epam.com/browse/EPMDEDP-8303)
- Generate helm docs automatically [EPMDEDP-8385](https://jiraeu.epam.com/browse/EPMDEDP-8385)
- Extend Kaniko template flexibility [EPMDEDP-8620](https://jiraeu.epam.com/browse/EPMDEDP-8620)

### Bug Fixes

- We don't have an image for the edp-component-operator [EPMDEDP-8049](https://jiraeu.epam.com/browse/EPMDEDP-8049)
- Fix release flow script in GH Actions [EPMDEDP-8227](https://jiraeu.epam.com/browse/EPMDEDP-8227)
- Remove redundant pymdown-extensions from requirements [EPMDEDP-8303](https://jiraeu.epam.com/browse/EPMDEDP-8303)
- Add strict version for pymdown-extensions  for compatibility with mkdocs  material theme [EPMDEDP-8303](https://jiraeu.epam.com/browse/EPMDEDP-8303)
- Fix changelog generation in GH Release Action [EPMDEDP-8468](https://jiraeu.epam.com/browse/EPMDEDP-8468)
- Correct image version [EPMDEDP-8471](https://jiraeu.epam.com/browse/EPMDEDP-8471)
- Align pygments version for mkdocs [EPMDEDP-8920](https://jiraeu.epam.com/browse/EPMDEDP-8920)

### Code Refactoring

- Remove the creation of users [EPMDEDP-7438](https://jiraeu.epam.com/browse/EPMDEDP-7438)
- Align Gitlab integration with Github [EPMDEDP-7979](https://jiraeu.epam.com/browse/EPMDEDP-7979)
- Remove unused Dockerfile [EPMDEDP-7999](https://jiraeu.epam.com/browse/EPMDEDP-7999)
- Add aws_region variable to edp-config [EPMDEDP-8164](https://jiraeu.epam.com/browse/EPMDEDP-8164)
- Enable config kaniko build from helm [EPMDEDP-8474](https://jiraeu.epam.com/browse/EPMDEDP-8474)

### Routine

- Populate chart with Artifacthub annotations [EPMDEDP-8049](https://jiraeu.epam.com/browse/EPMDEDP-8049)
- Add changelog generator options [EPMDEDP-8084](https://jiraeu.epam.com/browse/EPMDEDP-8084)
- Add automatic release GH Action [EPMDEDP-8084](https://jiraeu.epam.com/browse/EPMDEDP-8084)
- Add ct.yaml config [EPMDEDP-8227](https://jiraeu.epam.com/browse/EPMDEDP-8227)
- Update release flow [EPMDEDP-8227](https://jiraeu.epam.com/browse/EPMDEDP-8227)
- Update changelog [EPMDEDP-8227](https://jiraeu.epam.com/browse/EPMDEDP-8227)
- Update mkdocs to the latest version [EPMDEDP-8257](https://jiraeu.epam.com/browse/EPMDEDP-8257)
- Update helm-docs version to 1.10.0 [EPMDEDP-8257](https://jiraeu.epam.com/browse/EPMDEDP-8257)
- Use stable EDP helm repo [EPMDEDP-8475](https://jiraeu.epam.com/browse/EPMDEDP-8475)
- Align helm charts versions [EPMDEDP-8832](https://jiraeu.epam.com/browse/EPMDEDP-8832)
- Upgrade kaniko images [EPMDEDP-8850](https://jiraeu.epam.com/browse/EPMDEDP-8850)
- Replace the loop with a while block in the init-kaniko container [EPMDEDP-8918](https://jiraeu.epam.com/browse/EPMDEDP-8918)
- Update changelog [EPMDEDP-9185](https://jiraeu.epam.com/browse/EPMDEDP-9185)

### Documentation

- Add Multitenant Logging doc [EPMDEDP-7355](https://jiraeu.epam.com/browse/EPMDEDP-7355)
- Keycloak as Identity provider for EKS OIDC [EPMDEDP-7674](https://jiraeu.epam.com/browse/EPMDEDP-7674)
- Update the Changelog with RN v.2.10.0 [EPMDEDP-7834](https://jiraeu.epam.com/browse/EPMDEDP-7834)
- Fix the link in the Changelog [EPMDEDP-7834](https://jiraeu.epam.com/browse/EPMDEDP-7834)
- Refactor GitHub job provisioner to accept webhooks [EPMDEDP-7938](https://jiraeu.epam.com/browse/EPMDEDP-7938)
- Add images to the Promote App doc [EPMDEDP-7955](https://jiraeu.epam.com/browse/EPMDEDP-7955)
- Fix typos in the AC user guide [EPMDEDP-7958](https://jiraeu.epam.com/browse/EPMDEDP-7958)
- Refactor ecr-to-docker doc [EPMDEDP-7974](https://jiraeu.epam.com/browse/EPMDEDP-7974)
- Refactor ecr-to-docker doc [EPMDEDP-7974](https://jiraeu.epam.com/browse/EPMDEDP-7974)
- Update GitHub/GitLab Integration [EPMDEDP-7984](https://jiraeu.epam.com/browse/EPMDEDP-7984)
- Describe the default configs for the helm-lint stage [EPMDEDP-7988](https://jiraeu.epam.com/browse/EPMDEDP-7988)
- Add a Parallel Threads use case [EPMDEDP-8004](https://jiraeu.epam.com/browse/EPMDEDP-8004)
- Provide instruction on how to rename Sonar project [EPMDEDP-8012](https://jiraeu.epam.com/browse/EPMDEDP-8012)
- Fix typos in EDP Workflow doc [EPMDEDP-8017](https://jiraeu.epam.com/browse/EPMDEDP-8017)
- Add section on upgrading EDP to version 2.10.1 [EPMDEDP-8092](https://jiraeu.epam.com/browse/EPMDEDP-8092)
- Update EDP Releases doc [EPMDEDP-8108](https://jiraeu.epam.com/browse/EPMDEDP-8108)
- Bypass code review for Gerrit All-Projects [EPMDEDP-8111](https://jiraeu.epam.com/browse/EPMDEDP-8111)
- Manage hooks option explanation [EPMDEDP-8121](https://jiraeu.epam.com/browse/EPMDEDP-8121)
- Refactor import strategy documentation [EPMDEDP-8121](https://jiraeu.epam.com/browse/EPMDEDP-8121)
- Add IP range description to accept webhooks [EPMDEDP-8151](https://jiraeu.epam.com/browse/EPMDEDP-8151)
- Add webhook debug description [EPMDEDP-8151](https://jiraeu.epam.com/browse/EPMDEDP-8151)
- Edit Releases link [EPMDEDP-8175](https://jiraeu.epam.com/browse/EPMDEDP-8175)
- Add commit message example [EPMDEDP-8221](https://jiraeu.epam.com/browse/EPMDEDP-8221)
- Update EDP Releases documentation [EPMDEDP-8227](https://jiraeu.epam.com/browse/EPMDEDP-8227)
- Align section on updating EDP to version 2.10.2 [EPMDEDP-8227](https://jiraeu.epam.com/browse/EPMDEDP-8227)
- Describe Sonar requirements for project names [EPMDEDP-8283](https://jiraeu.epam.com/browse/EPMDEDP-8283)
- Upgrade Kiosk to version 0.2.11 [EPMDEDP-8287](https://jiraeu.epam.com/browse/EPMDEDP-8287)
- Upgrade mkdocs to the latest stable [EPMDEDP-8303](https://jiraeu.epam.com/browse/EPMDEDP-8303)
- Add information about new input stages [EPMDEDP-8313](https://jiraeu.epam.com/browse/EPMDEDP-8313)
- Update default job-provision in  operator guide due new deploy features [EPMDEDP-8313](https://jiraeu.epam.com/browse/EPMDEDP-8313)
- Describe helm-docs stage [EPMDEDP-8329](https://jiraeu.epam.com/browse/EPMDEDP-8329)
- Add clarification for helm-docs usage [EPMDEDP-8329](https://jiraeu.epam.com/browse/EPMDEDP-8329)
- Describe Container kaniko library stages [EPMDEDP-8341](https://jiraeu.epam.com/browse/EPMDEDP-8341)
- Add Kaniko library stages to job provisioners [EPMDEDP-8341](https://jiraeu.epam.com/browse/EPMDEDP-8341)
- Use gh-pages instead Chartmuseum [EPMDEDP-8386](https://jiraeu.epam.com/browse/EPMDEDP-8386)
- Describe branch naming convention [EPMDEDP-8422](https://jiraeu.epam.com/browse/EPMDEDP-8422)
- Describe copy shared secrets stage [EPMDEDP-8469](https://jiraeu.epam.com/browse/EPMDEDP-8469)
- Update helm chart repository link [EPMDEDP-8500](https://jiraeu.epam.com/browse/EPMDEDP-8500)
- Describe helm-uninstall step usage [EPMDEDP-8532](https://jiraeu.epam.com/browse/EPMDEDP-8532)
- Remove unused parameter from cd provisioner [EPMDEDP-8584](https://jiraeu.epam.com/browse/EPMDEDP-8584)
- Describe semi-auto-deploy-input step usage [EPMDEDP-8584](https://jiraeu.epam.com/browse/EPMDEDP-8584)
- Describe WAF implementation [EPMDEDP-8787](https://jiraeu.epam.com/browse/EPMDEDP-8787)
- Webhook identity already present on EKS cluster [EPMDEDP-8900](https://jiraeu.epam.com/browse/EPMDEDP-8900)
- Update mkdocs extensions [EPMDEDP-8920](https://jiraeu.epam.com/browse/EPMDEDP-8920)
- Add build pipeline for autotests [EPMDEDP-8920](https://jiraeu.epam.com/browse/EPMDEDP-8920)
- Fix identations for github and gitlab provisioners [EPMDEDP-8920](https://jiraeu.epam.com/browse/EPMDEDP-8920)
- Upgrade Keycloak version [EPMDEDP-8939](https://jiraeu.epam.com/browse/EPMDEDP-8939)
- Code review pipeline for kaniko must contain checkout step instead gerrit-checkout [EPMDEDP-8984](https://jiraeu.epam.com/browse/EPMDEDP-8984)

### BREAKING CHANGE:


existing config-maps with names kaniko-template and
docker-config must be backed-up and removed before applying
helm chart


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

[Unreleased]: https://github.com/epam/edp-install/compare/v3.1.0...HEAD
[v3.1.0]: https://github.com/epam/edp-install/compare/v3.0.0...v3.1.0
[v3.0.0]: https://github.com/epam/edp-install/compare/v2.12.2...v3.0.0
[v2.12.2]: https://github.com/epam/edp-install/compare/v2.12.1...v2.12.2
[v2.12.1]: https://github.com/epam/edp-install/compare/v2.12.0...v2.12.1
[v2.12.0]: https://github.com/epam/edp-install/compare/v2.11.1...v2.12.0
[v2.11.1]: https://github.com/epam/edp-install/compare/v2.11.0...v2.11.1
[v2.11.0]: https://github.com/epam/edp-install/compare/v2.10.2...v2.11.0
[v2.10.2]: https://github.com/epam/edp-install/compare/v2.10.1...v2.10.2
[v2.10.1]: https://github.com/epam/edp-install/compare/v2.10.0...v2.10.1
[v2.10.0]: https://github.com/epam/edp-install/compare/v2.9.0...v2.10.0
[v2.9.0]: https://github.com/epam/edp-install/compare/v2.8.4...v2.9.0
[v2.8.4]: https://github.com/epam/edp-install/compare/v2.8.3...v2.8.4
[v2.8.3]: https://github.com/epam/edp-install/compare/v2.8.2...v2.8.3
[v2.8.2]: https://github.com/epam/edp-install/compare/v2.8.1...v2.8.2
[v2.8.1]: https://github.com/epam/edp-install/compare/v2.8.0...v2.8.1
