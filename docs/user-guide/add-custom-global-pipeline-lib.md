# Add a Custom Global Pipeline Library

In order to add a new custom global pipeline library, perform the steps below:

1. Navigate to Jenkins and go to *Manage Jenkins -> Configure System -> Global Pipeline Libraries*.

  !!! note
      It is possible to configure as many libraries as necessary. Since these libraries will be globally usable, any pipeline in the system can utilize the functionality implemented in these libraries.

2. Specify the following values:

  !![Add custom library](../assets/user-guide/add_custom_lib.png)

  a. **Library name**: The name of a custom library.

  b. **Default version**: The version which can be branched, tagged or hashed of a commit.

  c. **Load implicitly**: If checked, scripts will automatically have access to this library without needing to request it via @Library. It means that there is no need to upload the library manually because it will be downloaded automatically during the build for each job.

  d. **Allow default version to be overridden**: If checked, scripts may select a custom version of the library by appending @someversion in the @Library annotation. Otherwise, they are restricted to using the version selected here.

  e. **Include @Library changes in job recent changes**: If checked, any changes in the library will be included in the changesets of a build, and changing the library would cause new builds to run for Pipelines that include this library. This can be overridden in the jenkinsfile: @Library(value="name@version", changelog=true|false).

  f. **Cache fetched versions on controller for quick retrieval**: If checked, versions fetched using this library will be cached on the controller. If a new library version is not downloaded during the build for some reason, remove the previous library version from cache in the Jenkins workspace.

  !!! note
      If the **Default version** check box is **not defined**, the pipeline must specify a version, for example, `@Library('my-shared-library@master')`. If the **Allow default version to be overridden** check box is enabled in the Shared Library’s configuration, a @Library annotation may also override the default version defined for the library.

  !![Source code management](../assets/user-guide/add_custom_lib2.png)

  g. **Project repository**: The URL of the repository

  h. **Credentials**: The credentials for the repository.

3. Use the Custom Global Pipeline Libraries on the pipeline, for example:

**Pipeline**

```java
@Library(['edp-library-stages', 'edp-library-pipelines', 'edp-custom-shared-library-name'])_

Build()
```
!!! note
    `edp-custom-shared-library-name` is the name of the Custom Global Pipeline Library that should be added to the Jenkins Global Settings.

### Related Articles

* [Jenkins Official Documentation: Extending with Shared Libraries](https://www.jenkins.io/doc/book/pipeline/shared-libraries/)
