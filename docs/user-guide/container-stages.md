# CI Pipeline for Container

EPAM Delivery Platform ensures the implemented Container support allowing to work with Dockerfile that is processed by means of stages in the **Code-Review** and **Build** pipelines. These pipelines are expected to be created after the Container Library is added.

## Code Review Pipeline Stages

In the **Code Review** pipeline, the following stages are available:

1. **checkout** stage is a standard step during which all files are checked out from a selected branch of the Git repository.

2. **dockerfile-lint** stage uses the [hadolint](https://github.com/hadolint/hadolint) tool to perform linting tests for the Dockerfile.

3. **dockerbuild-verify** stage collects artifacts and builds an image from the Dockerfile without pushing to registry. This stage is intended to check if the image is built.

## Build Pipeline Stages

In the **Build** pipeline, the following stages are available:

1. **checkout** stage is a standard step during which all files are checked out from a master branch of the Git repository.

2. **get-version** stage where the library version is determined either via:

  2.1. EDP versioning functionality.

  2.2. Default versioning functionality.

3. **dockerfile-lint** stage uses the [hadolint](https://github.com/hadolint/hadolint) tool to perform linting tests for Dockerfile.

4. **build-image-kaniko** stage builds Dockerfile using the [Kaniko](https://github.com/GoogleContainerTools/kaniko) tool.

5. **git-tag** stage that is intended for tagging a repository in Git.

## Tools for Container Images Building

EPAM Delivery Platform ensures the implemented [Kaniko](https://github.com/GoogleContainerTools/kaniko) tool and [`BuildConfig`](https://docs.openshift.com/container-platform/4.10/cicd/builds/understanding-buildconfigs.html) object support. Using Kaniko tool allows building the container images from a Dockerfile both on the Kubernetes and OpenShift platforms. The `BuildConfig` object enables the building of the container images only on the OpenShift platform.

EDP uses the `BuildConfig` object and the Kaniko tool for creating containers from a Dockerfile and pushing them to the internal container image registry. For Kaniko, it is also possible to change the Docker config file and push the containers to different container image registries.

### Supported Container Image Build Tools

|Platform|Build Tools|
|-|-|
|Kubernetes| Kaniko|
|OpenShift| Kaniko, BuildConfig|

### Change Build Tool in the Build Pipeline

By default, EPAM Delivery Platform uses the [`build-image-kaniko`](https://github.com/epam/edp-library-stages/blob/master/src/com/epam/edp/stages/impl/ci/impl/buildimagekaniko/BuildImageKaniko.groovy)
stage for building container images on the Kubernetes platform and the [`build-image-from-dockerfile`](https://github.com/epam/edp-library-stages/tree/master/src/com/epam/edp/stages/impl/ci/impl/builddockerfileimage) stage for building container images on the OpenShift platform.

In order to change a build tool for the OpenShift Platform from the default `buildConfig` object to the Kaniko tool, perform the following steps:

1. Modify or update a job provisioner logic, follow the instructions on the [Manage Jenkins CI Pipeline Job Provisioner](../operator-guide/manage-jenkins-ci-job-provision.md#custom-custom-defaultgithubgitlab) page.
2. Update the required parameters for a new provisioner.
   For example, if it is necessary to change the build tool for Container build pipeline, update the list of stages:

    ```groovy
    stages['Build-library-kaniko'] = '[{"name": "checkout"},{"name": "get-version"}' +
    ',{"name": "dockerfile-lint"},{"name": "build-image-from-dockerfile"}' + "${createJIMStage}" + ',{"name": "git-tag"}]'
    ```

    ```groovy
    stages['Build-library-kaniko'] = '[{"name": "checkout"},{"name": "get-version"}' +
    ',{"name": "dockerfile-lint"},{"name": "build-image-kaniko"}' + "${createJIMStage}" + ',{"name": "git-tag"}]'
    ```

## Related Articles

- [Use Dockerfile Linters for Code Review Pipeline](dockerfile-stages.md)
- [Manage Jenkins CI Pipeline Job Provisioner](../operator-guide/manage-jenkins-ci-job-provision.md)
