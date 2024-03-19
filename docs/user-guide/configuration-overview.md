# Configuration Overview

The Configuration tab of the KubeRocketCI portal is designed to integrate KubeRocketCI with third-party components. In this page you will get a brief overview of all the sections presented in the Configuration tab.

## Configuration Features

Here is the list of all the sections provided in the Configuration tab. Familiarize yourself with purposes of each section:

|Configuration Section|Description|
|:-|:-|
|[Links](quick-links.md)|Configure links for quick access to required tools that will be displayed on the Overview page or in specific resource details, such as application or stage details. Additionally, this section is used to configure widgets, such as SonarQube and DependencyTrack.|
|[Nexus](../operator-guide/nexus-sonatype.md)|Integrate Nexus to store and manage your application artifacts, facilitating a Continuous Integration flow within the platform.|
|[Registry](../quick-start/integrate-container-registry.md)|Integrate platform with a container registry to store container artifacts.|
|[Clusters](add-cluster.md)|Integrate platform with external clusters to enable remote cluster deployment.|
|[GitOps](gitops.md)|Onboard a specific repository used for the GitOps approach purposes.|
|[Argo CD](../operator-guide/argocd-integration.md)|Integrate platform with Argo CD to enable the GitOps approach.|
|[DefectDojo](../operator-guide/install-defectdojo.md)|Connect platform with DefectDojo to manage and track security defects in applications.|
|[DependencyTrack](../operator-guide/dependency-track.md)|Connect the platform with the DependencyTrack tool for monitoring and managing vulnerabilities within third-party components.|
|[SonarQube](../operator-guide/sonarqube.md)|Integrate SonarQube to enable static code analysis.|
|[Git Server](add-git-server.md)|Connect the platform to Version Control Systems, such as GitHub, GitLab, or Gerrit for source code management.|
|[Jira](../operator-guide/jira-integration.md)|Integrate Jira to track and deliver the status of your projects on a dedicated dashboard.|
|[SSO](../operator-guide/install-keycloak.md)|Integrate platform with identity provider to enable the Single Sign-On approach.|

## Related Articles

* [Add Quick Link](quick-links.md)
* [Add Git Server](add-git-server.md)
* [Add Cluster](add-cluster.md)
* [Manage GitOps](gitops.md)
* [Manage Container Registries](manage-container-registries.md)
