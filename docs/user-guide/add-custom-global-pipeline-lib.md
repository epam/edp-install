# Add a Custom Global Pipeline Library

In order to add a new custom global pipeline library, perform the steps below:

1. Navigate to Jenkins, go to *Manage Jenkins -> Configure System -> Global Pipeline Libraries* as many libraries as necessary can be configured.
Since these libraries will be globally usable, any Pipeline in the system can utilize functionality implemented in these libraries.
2. Specify the following values:

  ![add_custom_lib](../assets/user-guide/add_custom_lib.png)

  a - The name of a custom library;

  b - The version which can be branched, tagged or hashed of a commit.

  c - Allows pipelines using immediately classes or global variables defined by any libraries.

  d and e - Allows using the default version of the configured shared-library when the "Load implicitly" check box is selected, or if the pipeline references to the library only by the name, for example, `@Library('my-shared-library')`.

  !!! note
      If the "Default version" check box is **not defined**, the pipeline must specify a version, for example, `@Library('my-shared-library@master')`. If the "Allow default version to be overridden" check box is enabled in the Shared Library’s configuration, a @Library annotation may also override the default version defined for the library. This also enables the library with the selected "Load implicitly" check box to be loaded from a different version if necessary.

  ![add_custom_lib2](../assets/user-guide/add_custom_lib2.png)

  f - The URL of the repository;

  g - The credentials for the repository.

3.Use the Custom Global Pipeline Libraries on the pipeline, for example:

**Pipeline**

```java
@Library(['edp-custom-shared-library-name'])_

Build()
```
