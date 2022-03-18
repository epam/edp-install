# Use Dockerfile Linters for Code Review Pipeline

This section contains the description of **dockerbuild-verify**, **dockerfile-lint** stages which one can use in Code Review pipeline.

These stages help to obtain a quick response on the validity of the code in the Code Review pipeline in Kubernetes for all types of applications supported by EDP out of the box.

  !![Add stages](../assets/user-guide/dockerfile-stages.png)

Inspect the functions performed by the following stages:

1. **dockerbuild-verify** stage collects artifacts and builds an image from the Dockerfile without push to registry. This stage is intended to check if the image is built.

2. **dockerfile-lint** stage launches the [_hadolint_](https://github.com/hadolint/hadolint) command in order to check the Dockerfile.

### Related Articles

* [Use Terraform Library in EDP](terraform-stages.md)
* [EDP Pipeline Framework](pipeline-framework.md)
* [Promote Docker Images From ECR to Docker Hub](ecr-to-docker-stages.md)
* [CI Pipeline for Container](container-stages.md)
