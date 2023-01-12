# Install Tekton

EPAM Delivery Platform uses Tekton resources, such as Tasks, Pipelines, Triggers, and Interceptors, for running the CI/CD pipelines.

Inspect the main steps to perform for installing the Tekton resources via the Tekton release files.

## Prerequisites

* Kubectl version 1.24.0 or higher is installed. Please refer to the [Kubernetes official website](https://v1-24.docs.kubernetes.io/releases/download/) for details.
* For Openshift/OKD, the latest version of the `oc` utility is required. Please refer to the [OKD page](https://github.com/okd-project/okd/releases) on GitHub for details.
* Created AWS ECR repository for Kaniko cache. By default, the Kaniko cache repository name is `kaniko-cache` and this parameter is located in our Tekton `common-library`.

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

!!! note
    Tekton Operator also deploys [Pipelines as Code CI](https://pipelinesascode.com/) that requires OpenShift v4.11 (based on Kubernetes v1.24) or higher. This feature is optional and its deployments can be scaled to zero replicas.

Install Tekton Operator v0.64.0 using the release file:

```bash
kubectl apply -f https://github.com/tektoncd/operator/releases/download/v0.64.0/openshift-release.yaml
```

After the installation, the Tekton Operator will install the following components: Pipeline, Trigger, and Addons.

!!! note
    If there is the following error in the `openshift-operators` namespace for `openshift-pipelines-operator` and `tekton-operator-webhook` deployments:

    ```bash
    Error: container has runAsNonRoot and image will run as root
    ```

    Patch the deployments with the following commands:

    ```bash
    kubectl -n openshift-operators patch deployment openshift-pipelines-operator -p '{"spec": {"template": {"spec": {"securityContext": {"runAsUser": 1000}}}}}'
    kubectl -n openshift-operators patch deployment tekton-operator-webhook -p '{"spec": {"template": {"spec": {"securityContext": {"runAsUser": 1000}}}}}'
    ```

Grant access for Tekton Service Accounts in the `openshift-pipelines` namespace to the Privileged SCC:

```bash
oc adm policy add-scc-to-user privileged system:serviceaccount:openshift-pipelines:tekton-operators-proxy-webhook
oc adm policy add-scc-to-user privileged system:serviceaccount:openshift-pipelines:tekton-pipelines-controller
oc adm policy add-scc-to-user privileged system:serviceaccount:openshift-pipelines:tekton-pipelines-resolvers
oc adm policy add-scc-to-user privileged system:serviceaccount:openshift-pipelines:tekton-pipelines-webhook
oc adm policy add-scc-to-user privileged system:serviceaccount:openshift-pipelines:tekton-triggers-controller
oc adm policy add-scc-to-user privileged system:serviceaccount:openshift-pipelines:tekton-triggers-core-interceptors
oc adm policy add-scc-to-user privileged system:serviceaccount:openshift-pipelines:tekton-triggers-webhook
oc adm policy add-scc-to-user privileged system:serviceaccount:openshift-pipelines:pipelines-as-code-controller
oc adm policy add-scc-to-user privileged system:serviceaccount:openshift-pipelines:pipelines-as-code-watcher
oc adm policy add-scc-to-user privileged system:serviceaccount:openshift-pipelines:pipelines-as-code-webhook
oc adm policy add-scc-to-user privileged system:serviceaccount:openshift-pipelines:default
```

## Related Articles
* [Install via Helmfile](install-via-helmfile.md)