# Install Tekton

EPAM Delivery Platform uses Tekton resources, such as Tasks, Pipelines, Triggers, and Interceptors, for running the CI/CD pipelines.

Inspect the main steps to perform for installing the Tekton resources via the Tekton release files.

## Prerequisites

* Kubectl version 1.23.0 is installed. Please refer to the [Kubernetes official website](https://v1-23.docs.kubernetes.io/releases/download/) for details.

## Installation on Kubernetes Cluster

To install Tekton resources, follow the steps below:

!!! info
    Please refer to the [Install Tekton Pipelines](https://tekton.dev/docs/installation/pipelines/) and
    [Install and set up Tekton Triggers](https://tekton.dev/docs/installation/triggers/) sections for details.

1. Install Tekton pipelines v0.42.0 using the release file:

  !!! Note
      Tekton Pipeline resources are used for managing and running EDP Tekton Pipelines and Tasks.
      Please refer to the [EDP Tekton Pipelines](https://github.com/epam/edp-tekton/tree/master/charts/pipelines-library/templates/pipelines) and
      [EDP Tekton Tasks](https://github.com/epam/edp-tekton/tree/master/charts/pipelines-library/templates/tasks) pages for details.

   ```bash
   kubectl apply -f https://storage.googleapis.com/tekton-releases/pipeline/previous/v0.42.0/release.yaml
   ```

2. Install Tekton Triggers v0.22.0 using the release file:

  !!! Note
      Tekton Trigger resources are used for managing and running EDP Tekton EventListeners, Triggers, TriggerBindings and TriggerTemplates.
      Please refer to the [EDP Tekton Triggers](https://github.com/epam/edp-tekton/tree/master/charts/pipelines-library/templates/triggers) page for details.

   ```bash
   kubectl apply -f https://storage.googleapis.com/tekton-releases/triggers/previous/v0.22.0/release.yaml
   ```

3. Install Tekton Interceptors v0.22.0 using the release file:

  !!! Note
      EPAM Delivery Platform uses GitLab and GitHub ClusterInterceptors for managing requests from webhooks.

   ```bash
   kubectl apply -f https://storage.googleapis.com/tekton-releases/triggers/previous/v0.22.0/interceptors.yaml
   ```

## Installation on OKD cluster

To install Tekton resources, follow the steps below:

!!! info
    Please refer to the [Install Tekton Operator](https://tekton.dev/docs/operator/) documentation for details.

Install Tekton Operator v0.63.0 using the release file:

```bash
kubectl apply -f https://github.com/tektoncd/operator/releases/download/v0.63.0/openshift-release.yaml
```

After the installation, the Operator will install the following components: Pipeline, Trigger, and Addons.

## Related Articles
* [Install via Helmfile](install-via-helmfile.md)