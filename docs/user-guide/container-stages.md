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

### Related Articles

- [Use Dockerfile Linters for Code Review Pipeline](dockerfile-stages.md)