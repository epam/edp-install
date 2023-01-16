# Basic Concepts

Consult [EDP Glossary](./glossary.md) section for definitions mentioned on this page and [EDP Toolset](https://epam.github.io/edp-install/getting-started/#edp-toolset) to have a full list of tools used with the Platform. The below table contains a full list of features provided by EDP.

|Features|Description|
|-|-|
|Cloud Agnostic|EDP runs on Kubernetes cluster, so any Public Cloud Provider which provides Kubernetes can be used. Kubernetes clusters deployed on-premises work as well|
|CI/CD for Microservices|EDP is initially designed to support CI/CD for [Microservices](https://microservices.io/){target=_blank} running as containerized applications inside Kubernetes Cluster. EDP also supports CI for:<br>- Terraform Modules, <br>- Open Policy Rules,<br>- Workflows for Java11, JavaScript (React), .Net, Python, Groovy Pipelines, Go|
|Version Control System (VCS)|EDP installs *Gerrit* as a default Source Code Management (SCM) tool. EDP also supports *GitHub* and *GitLab* integration|
|Branching Strategy|EDP supports [Trunk-based](https://trunkbaseddevelopment.com/){target=_blank} development as well as [GitHub/GitLab flow](https://guides.github.com/introduction/flow/){target=_blank}. EDP creates two Pipelines per each codebase branch (see [Pipeline Framework](./user-guide/pipeline-framework.md)): *Code Review* and *Build*|
|Repository Structure|EDP provides separate Git repository per each Codebase and doesn't work with *Monorepo*. However, EDP does support customization and runs *helm-lint*, *dockerfile-lint* steps using *Monorepo* approach.|
|Artifacts Versioning|EDP supports two approaches for Artifacts versioning: <br>- *default* (BRANCH-[TECH_STACK_VERSION]-BUILD_ID)<br>- *EDP* (MAJOR.MINOR.PATCH-BUILD_ID), which is [SemVer](https://semver.org/){target=_blank}.<br>Custom versioning can be created by implementing [get-version](./user-guide/pipeline-stages.md) stage|
|Application Library|EDP provides baseline codebase templates for Microservices, Libraries, within *create* strategy while onboarding new Codebase|
|Stages Library|Each EDP Pipeline consists of pre-defined steps (stages). Consult [library documentation](./user-guide/pipeline-stages.md) for more details|
|CI Pipelines|EDP provides CI Pipelines (running in Jenkins) for first-class citizens: <br>- Applications (Microservices) based on Java8, Java11, JavaScript (React), .Net, Python, Go<br>- Libraries based on Java8, Java11, JavaScript (React), .Net, Python, Go, Groovy Pipelines, Terraform<br>- Autotests based on Java8, Java11|
|CD Pipelines|EDP provides capabilities to design CD Pipelines (in Admin Console) for Microservices and defines logic for artifacts flow (promotion) from env to env. Artifacts promotion is performed automatically (*Autotests*), manually (*User Approval*) or combining both approaches|
|Autotests|EDP provides CI pipeline for autotest implemented in Java. Autotests can be used as *Quality Gates* in CD Pipelines|
|Custom Pipeline Library|EDP can be extended by introducing Custom Pipeline Library|
|Dynamic Environments|Each EDP CD Pipeline creates/destroys environment upon user requests|
