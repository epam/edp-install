# Overview

The Version Control Systems (VCS) section is dedicated to delivering comprehensive information on VCS within the EPAM Delivery Platform. This section comprises detailed descriptions of all the deployment strategies, along with valuable recommendations for their optimal usage, and the list of supported VCS, facilitating seamless integration with EDP.

## Supported VCS

EDP can be integrated with the following Version Control Systems:

* Gerrit (used by default);
* GitHub;
* GitLab.

!!! note
    So far, EDP doesn't support authorization mechanisms in the upstream GitLab.

## VCS Deployment Strategies

EDP offers the following strategies to work with repositories:

* **Create from template** – creates a project on the pattern in accordance with an application language, a build tool, and a framework selected while [creating application](../user-guide/add-application.md). This strategy is recommended for projects that start developing their applications from scratch.

!!! note
    Under the hood, all the built-in application frameworks, build tools and frameworks are stored in our public [GitHub](https://github.com/epmd-edp) repository.

* **Import project** - enables working with the repository located in the added [Git server](../user-guide/add-git-server.md). This scenario is preferred when the users already have an application stored in their own pre-configured repository and intends to continue working with their repository while also utilizing EDP simultaneously.

!!! note
    In order to use the **Import project** strategy, make sure to adjust it with the [Integrate GitHub/GitLab in Tekton](../operator-guide/import-strategy-tekton.md) page.
    The **Import project** strategy is not applicable for Gerrit.
    Also, it is impossible to choose the **Empty project** field when using the **Import project** strategy while [creating appication](../user-guide/add-application.md) since it is implied that you already have a ready-to-work application in your own repository, whereas the "Empty project" option creates a repository but doesn't put anything in it.

* **Clone project** – clones the indicated repository into EPAM Delivery Platform. In this scenario, the application repository is forked from the original application repository to EDP. Since EDP doesn't support multiple VCS integration for now, this strategy is recommended when the user has several applications located in several repositories.

## Related Articles

* [Add Git Server](../user-guide/add-git-server.md)
* [Add Application](../user-guide/add-application.md)
* [Integrate GitHub/GitLab in Tekton](../operator-guide/import-strategy-tekton.md)
