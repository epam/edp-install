# Customize Deployment

When deploying applications into environments, it's important to automate both pre-deployment and post-deployment procedures.

Pre-deployment procedures include essential tasks such as deploying databases, configuring specific software, and preparing the environment. Additionally, post-deployment procedures, such as testing, configuring, and removing old information from the environment, are crucial for ensuring the smooth operation of the deployed application. To facilitate these processes, the custom deployment feature was implemented in KubeRocketCI.

This page provides comprehensive guidelines on how to adjust the deployment logic to cater your needs.

## Deploy Custom Pipeline

Overall, the custom pipeline creation involves the following steps:

```mermaid
graph LR;
    A(Create TriggerTemplate resource) --> B(Create Pipeline resource) --> C(Deploy custom environment)
```

1. **Create TriggerTemplate resource** -  On this step, we create the `TriggerTemplate` custom resource that will appear as an option in the in the environment stage creation menu.
2. **Create Pipeline** - On this step, we create custom resource called `Pipeline` that complements the trigger template. This resource contains all the tasks to perform within the custom pipeline.
3. **Integration** - On this step, you simply select your custom pipeline logic when creating a stage for your environment.

To customize your deployment pipeline, follow the steps below:

1. Create the `TriggerTemplate` custom resource by adding the following label:
  ```
  labels:
      app.edp.epam.com/pipelinetype: deploy
  ```

!!! note
    Please refer to the `TriggerTemplate` [example](https://github.com/epam/edp-tekton/blob/master/charts/pipelines-library/templates/triggers/cd/deploy.yaml) for more details. Remember to set your pipeline name in the spec.resourcetemplates.spec.pipelineRef.name parameter (line #33).

2. Create the custom pipeline with your custom logic. Refer to the custom pipeline [example](https://github.com/epam/edp-tekton/blob/master/charts/pipelines-library/templates/pipelines/cd/deploy.yaml) for more details.

3. Apply the created manifest files in the `edp` namespace.

4. In the `Create stage` window of the KubeRocketCI portal, select the added trigger template in the corresponding window:

  !![Select trigger template](../assets/operator-guide/select_trigger_template.png "Select trigger template")

5. (Optional) In case you need to implement custom deployment in a remote cluster, do the following:

  * Connect the KubeRocketCI platform with the remote cluster if it is not integrated yet. Please refer to the [Add Cluster](../user-guide/add-cluster.md) page for more details;
  * Mount the secret to the `run-quality-gate` resource by changing the [volumes](https://github.com/epam/edp-tekton/blob/master/charts/pipelines-library/templates/tasks/run-quality-gate.yaml#L19) and [volumeMounts](https://github.com/epam/edp-tekton/blob/master/charts/pipelines-library/templates/tasks/run-quality-gate.yaml#L27) sections;
  * Switch the [context](https://github.com/epam/edp-tekton/blob/master/charts/pipelines-library/templates/tasks/run-quality-gate.yaml#L32) by specifying the appropriate kube config file of the `run-quality-gate` resource;
  * In the `Create stage` window of the KubeRocketCI portal, select the appropriate cluster in the corresponding window:

    !![Select cluster](../assets/user-guide/select-cluster.png "Select cluster")

# Related Articles

* [Add Cluster](../user-guide/add-cluster.md)
* [Deploy Application With Custom Build Tool/Framework](../use-cases/tekton-custom-pipelines.md)
* [Add Environment](../user-guide/add-cd-pipeline.md)
