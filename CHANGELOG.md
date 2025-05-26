# Changelog

## Overview

This section provides details on edp-install release lifecycle.

_**NOTE**: For details on EDP releases, please refer to the [RELEASES.md](./RELEASES.md) page._


<a name="unreleased"></a>
## [Unreleased]

### Features

- Add flag to disable default marketplace templates ([#465](https://github.com/epam/edp-install/issues/465))

### Routine

- Align edp-tekton values ([#456](https://github.com/epam/edp-install/issues/456))
- : Add annotation block for cd-pipeline-operator ([#453](https://github.com/epam/edp-install/issues/453))
- Update current development version ([#449](https://github.com/epam/edp-install/issues/449))
- Update current development version ([#442](https://github.com/epam/edp-install/issues/442))
- Rename ConfigMap from 'edp-config' to 'krci-config'
- Add ImagePullSecrets field for dependency charts ([#438](https://github.com/epam/edp-install/issues/438))
- Update current development version ([#430](https://github.com/epam/edp-install/issues/430))

### Documentation

- Update link redirects in docs ([#460](https://github.com/epam/edp-install/issues/460))
- Update RELEASES md file to contain 3-11-2 note ([#452](https://github.com/epam/edp-install/issues/452))
- Add 3-11-1 release notes and doc updates ([#435](https://github.com/epam/edp-install/issues/435))
- Describe release 3-11-0 in RELEASES md ([#435](https://github.com/epam/edp-install/issues/435))
- Add redirects for old pages ([#414](https://github.com/epam/edp-install/issues/414))


<a name="v3.11.3"></a>
## [v3.11.3] - 2025-05-26
### Routine

- Update current development version ([#470](https://github.com/epam/edp-install/issues/470))


<a name="v3.11.2"></a>
## [v3.11.2] - 2025-04-11
### Routine

- Update current development version ([#449](https://github.com/epam/edp-install/issues/449))


<a name="v3.11.1"></a>
## [v3.11.1] - 2025-03-31
### Routine

- Update current development version ([#442](https://github.com/epam/edp-install/issues/442))
- Rename ConfigMap from 'edp-config' to 'krci-config'
- Add ImagePullSecrets field for dependency charts ([#438](https://github.com/epam/edp-install/issues/438))


<a name="v3.11.0"></a>
## [v3.11.0] - 2025-03-22
### Features

- Enable flexible naming for default OIDC groups ([#378](https://github.com/epam/edp-install/issues/378))
- Enable node selector for tekton pipelines ([#367](https://github.com/epam/edp-install/issues/367))
- Enable configuration to disable pipeline by codebses ([#346](https://github.com/epam/edp-install/issues/346))

### Bug Fixes

- Fix typo in apiClusterEndpoint parameter

### Routine

- Update current development version ([#430](https://github.com/epam/edp-install/issues/430))
- Add imagePullSecrets field for Tekton ServiceAccount ([#427](https://github.com/epam/edp-install/issues/427))
- Make Codemie QuickLink deployment optional ([#424](https://github.com/epam/edp-install/issues/424))
- Add apiClusterEndpoint parameter for static kubeconfig generation
- Expand Developer Role Permissions ([#409](https://github.com/epam/edp-install/issues/409))
- Align Argo CD diff for edp-config ConfigMap ([#344](https://github.com/epam/edp-install/issues/344))
- Enable branches management for developers ([#400](https://github.com/epam/edp-install/issues/400))
- Update current development version ([#390](https://github.com/epam/edp-install/issues/390))
- Update values.yaml ([#384](https://github.com/epam/edp-install/issues/384))
- Redesign Quick Links Installation ([#384](https://github.com/epam/edp-install/issues/384))
- Remove Tekton Dashboard from edp-tekton ([#382](https://github.com/epam/edp-install/issues/382))
- Disable CodeMie eso creation if path empty ([#369](https://github.com/epam/edp-install/issues/369))
- Enable availableClusters parameter in edp-config configmap ([#365](https://github.com/epam/edp-install/issues/365))
- Clarify apiGatewayUrl usage example ([#344](https://github.com/epam/edp-install/issues/344))
- Update current development version ([#356](https://github.com/epam/edp-install/issues/356))
- Align ArgoCD diff for edp-config Config Map ([#344](https://github.com/epam/edp-install/issues/344))
- Update current development version ([#335](https://github.com/epam/edp-install/issues/335))
- Align permissions for developer to use approve feature ([#332](https://github.com/epam/edp-install/issues/332))
- Update OIDC configuration for edp-headlamp ([#325](https://github.com/epam/edp-install/issues/325))
- Update Pull Request Template ([#107](https://github.com/epam/edp-install/issues/107))
- Update current development version ([#318](https://github.com/epam/edp-install/issues/318))
- Update current development version ([#318](https://github.com/epam/edp-install/issues/318))

### Documentation

- Add redirect links to the new docs ([#414](https://github.com/epam/edp-install/issues/414))
- Fix link redirect ([#408](https://github.com/epam/edp-install/issues/408))
- Expand link redirects in docs ([#408](https://github.com/epam/edp-install/issues/408))
- Publish release notes for 3 10 5 ([#397](https://github.com/epam/edp-install/issues/397))
- Update redirect plugin to remove noindex tag ([#380](https://github.com/epam/edp-install/issues/380))
- Update redirect url([#380](https://github.com/epam/edp-install/issues/380))
- Update RELEASES md file with 3-10-4 notes ([#380](https://github.com/epam/edp-install/issues/380))
- Make abstract quick links ([#362](https://github.com/epam/edp-install/issues/362))
- Update releases md to 3-10-3 ([#360](https://github.com/epam/edp-install/issues/360))
- Update redirect to new documentation site ([#352](https://github.com/epam/edp-install/issues/352))
- Replace EDP With KubeRocketCI in RELEASES md ([#297](https://github.com/epam/edp-install/issues/297))
- Add 3-10-1 and 3-10-2 releases to RELEASES md ([#339](https://github.com/epam/edp-install/issues/339))
- Update RELEASES md file ([#311](https://github.com/epam/edp-install/issues/311))


<a name="v3.10.5"></a>
## [v3.10.5] - 2025-01-24
### Features

- Enable flexible naming for default OIDC groups ([#378](https://github.com/epam/edp-install/issues/378))

### Routine

- Update current development version ([#390](https://github.com/epam/edp-install/issues/390))
- Update values.yaml ([#384](https://github.com/epam/edp-install/issues/384))
- Redesign Quick Links Installation ([#384](https://github.com/epam/edp-install/issues/384))
- Remove Tekton Dashboard from edp-tekton ([#382](https://github.com/epam/edp-install/issues/382))


<a name="v3.10.4"></a>
## [v3.10.4] - 2025-01-10
### Features

- Enable node selector for tekton pipelines ([#367](https://github.com/epam/edp-install/issues/367))

### Routine

- Update current development version ([#376](https://github.com/epam/edp-install/issues/376))
- Disable CodeMie eso creation if path empty ([#369](https://github.com/epam/edp-install/issues/369))
- Enable availableClusters parameter in edp-config configmap ([#365](https://github.com/epam/edp-install/issues/365))
- Align ArgoCD diff for edp-config Config Map ([#344](https://github.com/epam/edp-install/issues/344))

### Documentation

- Make abstract quick links ([#362](https://github.com/epam/edp-install/issues/362))


<a name="v3.10.3"></a>
## [v3.10.3] - 2024-12-12
### Routine

- Update current development version ([#356](https://github.com/epam/edp-install/issues/356))


<a name="v3.10.2"></a>
## [v3.10.2] - 2024-11-05
### Routine

- Update current development version ([#335](https://github.com/epam/edp-install/issues/335))
- Align permissions for developer to use approve feature ([#332](https://github.com/epam/edp-install/issues/332))
- Update OIDC configuration for edp-headlamp ([#325](https://github.com/epam/edp-install/issues/325))


<a name="v3.10.1"></a>
## [v3.10.1] - 2024-10-27
### Routine

- Add ApprovalTask CRD ([#318](https://github.com/epam/edp-install/issues/318))

### Documentation

- Update RELEASES md file in 3_10 branch ([#311](https://github.com/epam/edp-install/issues/311))


<a name="v3.10.0"></a>
## [v3.10.0] - 2024-10-18
### Features

- Add Support for BitBucket as GitProvider ([#306](https://github.com/epam/edp-install/issues/306))
- Add the ability to enable tekton-pipelines monitoring ([#268](https://github.com/epam/edp-install/issues/268))
- Enable onboarding of 'Echo-server' via Marketplace Section ([#265](https://github.com/epam/edp-install/issues/265))
- Enable onboarding of 'Terminal' via Marketplace section ([#262](https://github.com/epam/edp-install/issues/262))
- Enable eso for ci-codemie ([#252](https://github.com/epam/edp-install/issues/252))
- Enable eso for ci-codemie ([#252](https://github.com/epam/edp-install/issues/252))

### Bug Fixes

- Align templates variables([#289](https://github.com/epam/edp-install/issues/289))

### Routine

- Update current development version ([#318](https://github.com/epam/edp-install/issues/318))
- Add apiGatewayUrl field to edp-config CM ([#316](https://github.com/epam/edp-install/issues/316))
- Add asset for the documentation ([#214](https://github.com/epam/edp-install/issues/214))
- Enable permissions to configmaps for developers ([#313](https://github.com/epam/edp-install/issues/313))
- Move Git Providers data to separate AWS PS object ([#309](https://github.com/epam/edp-install/issues/309))
- Align kubernetes rbac ([#305](https://github.com/epam/edp-install/issues/305))
- Align marketplace repository path([#301](https://github.com/epam/edp-install/issues/301))
- Remove oauth2proxy resources from chart([#289](https://github.com/epam/edp-install/issues/289))
- Validate 'type' field for QuickLink resources ([#294](https://github.com/epam/edp-install/issues/294))
- Add asset for the documentation ([#214](https://github.com/epam/edp-install/issues/214))
- Add asset for the documentation ([#214](https://github.com/epam/edp-install/issues/214))
- Add asset for the documentation ([#214](https://github.com/epam/edp-install/issues/214))
- Disable Tekton dashboard deployments([#276](https://github.com/epam/edp-install/issues/276))
- Update KubeRocketCI names and documentation links ([#273](https://github.com/epam/edp-install/issues/273))
- Add asset for the documentation ([#214](https://github.com/epam/edp-install/issues/214))
- Align label for chat secret ([#270](https://github.com/epam/edp-install/issues/270))
- Align keycloakUrl example value for portal chart([#264](https://github.com/epam/edp-install/issues/264))
- Add asset for the documentation ([#214](https://github.com/epam/edp-install/issues/214))
- Add asset for the documentation ([#214](https://github.com/epam/edp-install/issues/214))
- Add asset for the documentation ([#214](https://github.com/epam/edp-install/issues/214))
- Add asset for the documentation ([#214](https://github.com/epam/edp-install/issues/214))
- Align EDP Install Documentation to 3.9.0 ([#244](https://github.com/epam/edp-install/issues/244))
- Update current development version ([#239](https://github.com/epam/edp-install/issues/239))

### Documentation

- Update changelog file for release notes ([#292](https://github.com/epam/edp-install/issues/292))
- Update KubeRocketCI logo ([#298](https://github.com/epam/edp-install/issues/298))
- Update KRCI logo ([#298](https://github.com/epam/edp-install/issues/298))
- Update CHANGELOG md file
- Update link to new docs ([#278](https://github.com/epam/edp-install/issues/278))
- Replace EDP with KubeRocketCI in README md ([#278](https://github.com/epam/edp-install/issues/278))
- Add annoounce for new documentation URL ([#177](https://github.com/epam/edp-install/issues/177))
- Upgrade Documentation for KubeRocketCI 3.9.0 ([#248](https://github.com/epam/edp-install/issues/248))
- Adjust the RELEASES md file ([#242](https://github.com/epam/edp-install/issues/242))
- Adjust the RELEASES md file ([#242](https://github.com/epam/edp-install/issues/242))
- Update the RELEASES md file ([#242](https://github.com/epam/edp-install/issues/242))


<a name="v3.9.0"></a>
## [v3.9.0] - 2024-06-13
### Features

- enable silence ping logging in oauth2-proxy config ([#230](https://github.com/epam/edp-install/issues/230))
- Align keycloakUrl parameter usage ([#208](https://github.com/epam/edp-install/issues/208))
- Align keycloakUrl parameter usage ([#208](https://github.com/epam/edp-install/issues/208))
- Migrate from SSORealm to idp resources ([#208](https://github.com/epam/edp-install/issues/208))
- Developer role users can view portal resources([#191](https://github.com/epam/edp-install/issues/191))
- Make keycloak realmName value configurable([#183](https://github.com/epam/edp-install/issues/183))
- Add the parameter to manage outh2proxy ingress creation([#178](https://github.com/epam/edp-install/issues/178))

### Bug Fixes

- Add rbac for portal widgets([#237](https://github.com/epam/edp-install/issues/237))
- Update keycloak secret definition ([#208](https://github.com/epam/edp-install/issues/208))
- Fix indentation in values.yaml ([#169](https://github.com/epam/edp-install/issues/169))

### Routine

- Update release version to 3.9.0 ([#239](https://github.com/epam/edp-install/issues/239))
- Update argocd diff ([#232](https://github.com/epam/edp-install/issues/232))
- Add assets for the documentation ([#214](https://github.com/epam/edp-install/issues/214))
- Align oauth2-proxy issuer url ([#208](https://github.com/epam/edp-install/issues/208))
- Add eso objects for GHCR registry ([#218](https://github.com/epam/edp-install/issues/218))
- Set Default Value of secretManager to own for Private Repositories ([#216](https://github.com/epam/edp-install/issues/216))
- Switch to kind ClusterKeycloak in case if add-ons approach use([#188](https://github.com/epam/edp-install/issues/188))
- Align rights excessive removed([#191](https://github.com/epam/edp-install/issues/191))
- Add codeowners file to the repo ([#192](https://github.com/epam/edp-install/issues/192))
- Remove unused flag ([#178](https://github.com/epam/edp-install/issues/178))
- Set edp version in cm from Chart.yaml ([#173](https://github.com/epam/edp-install/issues/173))
- Disable sso by default ([#172](https://github.com/epam/edp-install/issues/172))
- Update current development version ([#172](https://github.com/epam/edp-install/issues/172))

### Documentation

- Update velero installation guide([#235](https://github.com/epam/edp-install/issues/235))
- Add cases to Troubleshooting section ([#214](https://github.com/epam/edp-install/issues/214))
- Update release timeline ([#187](https://github.com/epam/edp-install/issues/187))
- Add GitHub container registry to documentation ([#223](https://github.com/epam/edp-install/issues/223))
- Add description for GHCR registry integration ([#218](https://github.com/epam/edp-install/issues/218))
- Align grafana tekton-dashboard integration documetation([#212](https://github.com/epam/edp-install/issues/212))
- Describe custom pipeline deployment ([#197](https://github.com/epam/edp-install/issues/197))
- Update team working flow ([#206](https://github.com/epam/edp-install/issues/206))
- AWS Infrastructure Cost Estimation Page ([#202](https://github.com/epam/edp-install/issues/202))
- Add upgrade edp 3.8 guide ([#199](https://github.com/epam/edp-install/issues/199))
- Update user guide ([#204](https://github.com/epam/edp-install/issues/204))
- Update the EDP Access Model page ([#175](https://github.com/epam/edp-install/issues/175))
- Update the portal endpoint in quick guide ([#184](https://github.com/epam/edp-install/issues/184))
- Configuration AWS components of the Argo CD ([#8](https://github.com/epam/edp-install/issues/8))
- Deploy platform without Ingress objects ([#185](https://github.com/epam/edp-install/issues/185))
- Update the Quick Start Guide ([#184](https://github.com/epam/edp-install/issues/184))
- Update components versions within docs ([#175](https://github.com/epam/edp-install/issues/175))
- Update screenshots in User Guide ([#175](https://github.com/epam/edp-install/issues/175))
- Create Sonar project management page ([#180](https://github.com/epam/edp-install/issues/180))
- Align add-cluster documentation ([#176](https://github.com/epam/edp-install/issues/176))
- Different Registries Support as repository for artifacts ([#182](https://github.com/epam/edp-install/issues/182))
- Add Nexus container registry in mkdocs ([#179](https://github.com/epam/edp-install/issues/179))
- Align argocd integration documentation ([#176](https://github.com/epam/edp-install/issues/176))
- Deploy AWS EKS Cluster page minors ([#174](https://github.com/epam/edp-install/issues/174))
- Update EDP version in installation documentation ([#181](https://github.com/epam/edp-install/issues/181))
- Add AI TechWriter assets ([#181](https://github.com/epam/edp-install/issues/181))
- Add initial section for troubleshooting ([#177](https://github.com/epam/edp-install/issues/177))
- Add AI assistance assets ([#181](https://github.com/epam/edp-install/issues/181))
- Update documentation related to AWS Services ([#177](https://github.com/epam/edp-install/issues/177))
- Align Deploy AWS EKS Cluster page ([#174](https://github.com/epam/edp-install/issues/174))
- Update the Supported Versions file ([#175](https://github.com/epam/edp-install/issues/175))
- Remove deprecated edp-component operator from readme ([#175](https://github.com/epam/edp-install/issues/175))
- Update component versions on External Secrets Operator page ([#175](https://github.com/epam/edp-install/issues/175))
- Align helm chart version in doc ([#173](https://github.com/epam/edp-install/issues/173))
- Align values.yaml ([#173](https://github.com/epam/edp-install/issues/173))
- Update the RELEASES md file ([#169](https://github.com/epam/edp-install/issues/169))


<a name="v3.8.1"></a>
## [v3.8.1] - 2024-03-13
### Routine

- Disable sso by default ([#172](https://github.com/epam/edp-install/issues/172))


<a name="v3.8.0"></a>
## [v3.8.0] - 2024-03-12
### Features

- Deploy Tekton resources for multiple Git Providers ([#166](https://github.com/epam/edp-install/issues/166))
- Add nodeSelector, affinity, tolerations ([#164](https://github.com/epam/edp-install/issues/164))
- Add option to parametrize the ssoRealmName value([#149](https://github.com/epam/edp-install/issues/149))
- Add QuickLink Custom Resources ([#147](https://github.com/epam/edp-install/issues/147))
- Enable ci-argocd secret provisioning by eso ([#134](https://github.com/epam/edp-install/issues/134))
- Deploy default EDP components out of the box ([#133](https://github.com/epam/edp-install/issues/133))

### Bug Fixes

- Fix ingress annotation in Tekton dashboard ([#143](https://github.com/epam/edp-install/issues/143))
- Fix ESO key path for argocd-ci ([#134](https://github.com/epam/edp-install/issues/134))
- Align tekton edpoint to service name ([#126](https://github.com/epam/edp-install/issues/126))

### Code Refactoring

- Remove edp-component-operator post EDPComponent CRD migration ([#144](https://github.com/epam/edp-install/issues/144))
- Align values file for custom certificates ([#145](https://github.com/epam/edp-install/issues/145))
- Merge sso and oauth2_proxy sections ([#145](https://github.com/epam/edp-install/issues/145))

### Routine

- Update current development version ([#172](https://github.com/epam/edp-install/issues/172))
- Align sonar quick link icon ([#171](https://github.com/epam/edp-install/issues/171))
- Align gitServer structure ([#163](https://github.com/epam/edp-install/issues/163))
- Align creation of external secret resources ([#166](https://github.com/epam/edp-install/issues/166))
- Remove EDPComponent CRs ([#168](https://github.com/epam/edp-install/issues/168))
- Add main-keycloak QuickLink ([#168](https://github.com/epam/edp-install/issues/168))
- Add Nexus container registry example ([#167](https://github.com/epam/edp-install/issues/167))
- Add link to guide for managing namespace ([#162](https://github.com/epam/edp-install/issues/162))
- Add generic secretStore resource to use different ESO providers ([#155](https://github.com/epam/edp-install/issues/155))
- Align external-secret to previus pattern ([#160](https://github.com/epam/edp-install/issues/160))
- Add provider type for ESO SecretStore ([#155](https://github.com/epam/edp-install/issues/155))
- Move awsRegion to dockerRegistry section ([#154](https://github.com/epam/edp-install/issues/154))
- Do not create kaniko eso object if ecr registry([#153](https://github.com/epam/edp-install/issues/153))
- Align values.yaml parameters ([#142](https://github.com/epam/edp-install/issues/142))
- Update current development version ([#138](https://github.com/epam/edp-install/issues/138))
- Update current development version ([#137](https://github.com/epam/edp-install/issues/137))
- Align ESO Parameter Store path for argocd-ci ([#134](https://github.com/epam/edp-install/issues/134))
- Temporary use hardcoded values for ESO ci-argocd ([#134](https://github.com/epam/edp-install/issues/134))
- Align variable usage for extraQuickLinks ([#133](https://github.com/epam/edp-install/issues/133))
- Update current development version ([#129](https://github.com/epam/edp-install/issues/129))
- Update current development version ([#126](https://github.com/epam/edp-install/issues/126))

### Documentation

- Update the annotation and labels page ([#128](https://github.com/epam/edp-install/issues/128))
- Add description for OIDC section in Portal ([#170](https://github.com/epam/edp-install/issues/170))
- Align migration from Jenkins to Tekton ([#163](https://github.com/epam/edp-install/issues/163))
- Update the QS guide as per feedback ([#165](https://github.com/epam/edp-install/issues/165))
- Update the installation flow ([#163](https://github.com/epam/edp-install/issues/163))
- Add label to associate an ingress with a specific GitServer ([#166](https://github.com/epam/edp-install/issues/166))
- Update documentaton pages ([#163](https://github.com/epam/edp-install/issues/163))
- Create the autotest coverage page ([#161](https://github.com/epam/edp-install/issues/161))
- Update links for quickstart ([#119](https://github.com/epam/edp-install/issues/119))
- Fix link for the Quick Start ([#119](https://github.com/epam/edp-install/issues/119))
- Define name convention for ingress objects ([#159](https://github.com/epam/edp-install/issues/159))
- Add description for secretManager parameter ([#158](https://github.com/epam/edp-install/issues/158))
- Align ESO integration secrets list ([#26](https://github.com/epam/edp-install/issues/26))
- Add a link to the ESO configuration in the values.yaml file ([#157](https://github.com/epam/edp-install/issues/157))
- Quick Start link fix ([#119](https://github.com/epam/edp-install/issues/119))
- Fix microsoft-teams-webhook-url-secret secret name ([#156](https://github.com/epam/edp-install/issues/156))
- Update User Guide ([#148](https://github.com/epam/edp-install/issues/148))
- Civo guide and registry minors ([#152](https://github.com/epam/edp-install/issues/152))
- Create the Civo install guide  ([#152](https://github.com/epam/edp-install/issues/152))
- Describe platform and subcomponents release channels ([#151](https://github.com/epam/edp-install/issues/151))
- Update mkdocs-material version ([#146](https://github.com/epam/edp-install/issues/146))
- Align OAuth2-Proxy page ([#145](https://github.com/epam/edp-install/issues/145))
- Expand Configuration docs ([#141](https://github.com/epam/edp-install/issues/141))
- Update Annotations and labels section with GitOps codebase ([#146](https://github.com/epam/edp-install/issues/146))
- Bring minor fixes due to feedback ([#141](https://github.com/epam/edp-install/issues/141))
- Update edp version in docs ([#141](https://github.com/epam/edp-install/issues/141))
- Update the RELEASES md file ([#139](https://github.com/epam/edp-install/issues/139))
- Add Quick Start video part 1 ([#140](https://github.com/epam/edp-install/issues/140))
- How to restore CI CD after change registry ([#136](https://github.com/epam/edp-install/issues/136))
- Remove extra variable from QS guide ([#119](https://github.com/epam/edp-install/issues/119))
- Fix tekton-dashboard port in QS guide ([#119](https://github.com/epam/edp-install/issues/119))
- Bump EDP version in QS guide ([#119](https://github.com/epam/edp-install/issues/119))
- Fix port-forward command in QS guide ([#119](https://github.com/epam/edp-install/issues/119))
- Fix helm repo name in Quick Start guide ([#119](https://github.com/epam/edp-install/issues/119))
- Update the Quick Start guide ([#119](https://github.com/epam/edp-install/issues/119))
- Fix warning with the correct link ([#135](https://github.com/epam/edp-install/issues/135))
- Align component diagram to the dark theme ([#135](https://github.com/epam/edp-install/issues/135))
- Add component diagram to the Developer Guide ([#135](https://github.com/epam/edp-install/issues/135))
- Update Capsule integration guide ([#124](https://github.com/epam/edp-install/issues/124))
- Create the Quick Start guide ([#119](https://github.com/epam/edp-install/issues/119))
- Update the EDP logo for Capsule adopters ([#132](https://github.com/epam/edp-install/issues/132))
- Create EDP labels documentation for mkdocs ([#128](https://github.com/epam/edp-install/issues/128))
- Container registry integration ([#131](https://github.com/epam/edp-install/issues/131))
- Update the RELEASES md file with 3.7.3 changes ([#130](https://github.com/epam/edp-install/issues/130))
- Reduct fontsize for footer ([#119](https://github.com/epam/edp-install/issues/119))
- Update quickstart guide ([#119](https://github.com/epam/edp-install/issues/119))
- Update mkdocs-material to v9.5.3 ([#119](https://github.com/epam/edp-install/issues/119))
- Tune git-revision-date-localized plugin ([#119](https://github.com/epam/edp-install/issues/119))
- Completly remove Jenkins materials from docs ([#119](https://github.com/epam/edp-install/issues/119))
- Tune video size in documentation ([#119](https://github.com/epam/edp-install/issues/119))
- Disable creation date parsing for pages ([#119](https://github.com/epam/edp-install/issues/119))
- Update home page description ([#119](https://github.com/epam/edp-install/issues/119))
- Update roadmap section ([#119](https://github.com/epam/edp-install/issues/119))
- Refactor Use-cases page ([#119](https://github.com/epam/edp-install/issues/119))
- Add page contributors information ([#119](https://github.com/epam/edp-install/issues/119))
- Fix mermaid extention ([#119](https://github.com/epam/edp-install/issues/119))
- Fix versions page for the release 3.0.0 ([#119](https://github.com/epam/edp-install/issues/119))
- Update supported versions section ([#119](https://github.com/epam/edp-install/issues/119))
- Add compliance section to documentation ([#119](https://github.com/epam/edp-install/issues/119))
- Align ArgoCD integration with EDP ([#125](https://github.com/epam/edp-install/issues/125))
- Update mkdocs framework version to the latest stable ([#119](https://github.com/epam/edp-install/issues/119))
- Add upgrade guide for 3.7.x version ([#127](https://github.com/epam/edp-install/issues/127))


<a name="v3.7.5"></a>
## [v3.7.5] - 2024-01-19
### Routine

- Release 3.7.5 with extraline in private ssh key fix ([#138](https://github.com/epam/edp-install/issues/138))


<a name="v3.7.4"></a>
## [v3.7.4] - 2024-01-18
### Routine

- Update release version to 3.7.4 ([#137](https://github.com/epam/edp-install/issues/137))


<a name="v3.7.3"></a>
## [v3.7.3] - 2024-01-03
### Routine

- Update release version to 3.7.3 ([#129](https://github.com/epam/edp-install/issues/129))


<a name="v3.7.2"></a>
## [v3.7.2] - 2023-12-18
### Bug Fixes

- Align tekton edpoint to service name ([#126](https://github.com/epam/edp-install/issues/126))

### Routine

- Update release version to 3.7.2 ([#126](https://github.com/epam/edp-install/issues/126))


<a name="v3.7.1"></a>
## [v3.7.1] - 2023-12-18
### Routine

- Update release version to 3.7.1 ([#126](https://github.com/epam/edp-install/issues/126))


<a name="v3.7.0"></a>
## [v3.7.0] - 2023-12-18
### Features

- Update secret labels type ([#74](https://github.com/epam/edp-install/issues/74))
- Make it possible to change tekton-cache parameters from edp-install chart ([#74](https://github.com/epam/edp-install/issues/74))
- Add connection status to the integration secret annotation ([#122](https://github.com/epam/edp-install/issues/122))

### Bug Fixes

- Update docker registry component creation condition ([#118](https://github.com/epam/edp-install/issues/118))

### Routine

- Align helm charts versions ([#126](https://github.com/epam/edp-install/issues/126))
- Minimize mandatory parameters in values.yaml ([#121](https://github.com/epam/edp-install/issues/121))
- Update current development version ([#110](https://github.com/epam/edp-install/issues/110))

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


<a name="v3.6.0"></a>
## [v3.6.0] - 2023-11-03
### Features

- Make VCS secret visible in EDP Portal ([#111](https://github.com/epam/edp-install/issues/111))
- Add public key for artifacts verification ([#103](https://github.com/epam/edp-install/issues/103))
- Add new Nexus icon, add deptrack and defectdojo links ([#104](https://github.com/epam/edp-install/issues/104))
- Migrate registry url from EDP component to edp-config configmap ([#98](https://github.com/epam/edp-install/issues/98))
- Implement integration with docker hub ([#43](https://github.com/epam/edp-install/issues/43))
- Add label to keycloak secret for visibility in EDP Portal ([#92](https://github.com/epam/edp-install/issues/92))

### Bug Fixes

- Remove sonar, nexus and keycloak operators as a part of edp-install ([#110](https://github.com/epam/edp-install/issues/110))
- Remove secretKey duplication from registry secrets ([#63](https://github.com/epam/edp-install/issues/63))

### Routine

- Align helm charts versions ([#110](https://github.com/epam/edp-install/issues/110))
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

### Documentation

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


<a name="v3.5.3"></a>
## [v3.5.3] - 2023-09-28
### Routine

- Align helm charts versions ([#91](https://github.com/epam/edp-install/issues/91))


<a name="v3.5.2"></a>
## [v3.5.2] - 2023-09-22
### Routine

- Disable SSO integration by default ([#89](https://github.com/epam/edp-install/issues/89))


<a name="v3.5.1"></a>
## [v3.5.1] - 2023-09-22
### Routine

- Remove artifacthub annotations related to images ([#85](https://github.com/epam/edp-install/issues/85))
- Update CHANGELOG.md ([#85](https://github.com/epam/edp-install/issues/85))


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

[Unreleased]: https://github.com/epam/edp-install/compare/v3.11.3...HEAD
[v3.11.3]: https://github.com/epam/edp-install/compare/v3.11.2...v3.11.3
[v3.11.2]: https://github.com/epam/edp-install/compare/v3.11.1...v3.11.2
[v3.11.1]: https://github.com/epam/edp-install/compare/v3.11.0...v3.11.1
[v3.11.0]: https://github.com/epam/edp-install/compare/v3.10.5...v3.11.0
[v3.10.5]: https://github.com/epam/edp-install/compare/v3.10.4...v3.10.5
[v3.10.4]: https://github.com/epam/edp-install/compare/v3.10.3...v3.10.4
[v3.10.3]: https://github.com/epam/edp-install/compare/v3.10.2...v3.10.3
[v3.10.2]: https://github.com/epam/edp-install/compare/v3.10.1...v3.10.2
[v3.10.1]: https://github.com/epam/edp-install/compare/v3.10.0...v3.10.1
[v3.10.0]: https://github.com/epam/edp-install/compare/v3.9.0...v3.10.0
[v3.9.0]: https://github.com/epam/edp-install/compare/v3.8.1...v3.9.0
[v3.8.1]: https://github.com/epam/edp-install/compare/v3.8.0...v3.8.1
[v3.8.0]: https://github.com/epam/edp-install/compare/v3.7.5...v3.8.0
[v3.7.5]: https://github.com/epam/edp-install/compare/v3.7.4...v3.7.5
[v3.7.4]: https://github.com/epam/edp-install/compare/v3.7.3...v3.7.4
[v3.7.3]: https://github.com/epam/edp-install/compare/v3.7.2...v3.7.3
[v3.7.2]: https://github.com/epam/edp-install/compare/v3.7.1...v3.7.2
[v3.7.1]: https://github.com/epam/edp-install/compare/v3.7.0...v3.7.1
[v3.7.0]: https://github.com/epam/edp-install/compare/v3.6.0...v3.7.0
[v3.6.0]: https://github.com/epam/edp-install/compare/v3.5.3...v3.6.0
[v3.5.3]: https://github.com/epam/edp-install/compare/v3.5.2...v3.5.3
[v3.5.2]: https://github.com/epam/edp-install/compare/v3.5.1...v3.5.2
[v3.5.1]: https://github.com/epam/edp-install/compare/v3.5.0...v3.5.1
[v3.5.0]: https://github.com/epam/edp-install/compare/v3.4.1...v3.5.0
[v3.4.1]: https://github.com/epam/edp-install/compare/v3.4.0...v3.4.1
[v3.4.0]: https://github.com/epam/edp-install/compare/v3.3.0...v3.4.0
