# Semi Auto Deploy

The **Semi Auto Deploy** stage provides the ability to deploy applications with the custom logic that comprises the following behavior:

* When the build of an application selected for deploy in the CD pipeline is completed, the Deploy pipeline is automatically triggered;
* By default, the deploy stage waits for 5 minutes, and if the user does not interfere with the process (cancels or selects certain versions of the application to deploy), then the deploy stage will deploy the latest versions of all applications;
* The stage can be used in the manual mode.

To enable the **Semi Auto Deploy** stage during the deploy process, follow the steps below:

1. Create or update the CD pipeline: make sure the trigger type for the stage is set to [auto](add-cd-pipeline.md#the-stages-menu).
2. Replace the `{"name":"auto-deploy-input","step_name":"auto-deploy-input"}` step to the `{"name":"semi-auto-deploy-input","step_name":"semi-auto-deploy-input"}` step in the CD pipeline. Alternatively, it is possible to create a [custom job provisioner](../operator-guide/manage-jenkins-cd-job-provision.md) with this step.
3. Run the Build pipeline for any application selected in the CD pipeline.

### Exceptional Cases

After the timeout starts and in case the pipeline has been interrupted not from the **Input requested** menu, the automatic deployment will be proceeding.
To resolve the issue and stop the pipeline, click the **Input requested** menu -> **Abort** or being on the pipeline UI, click the **Abort** button.

### Related Articles

* [Add CD Pipeline](add-cd-pipeline.md)
* [Customize CD Pipeline](customize-cd-pipeline.md)
* [Manage Jenkins CD Pipeline Job Provisioner](../operator-guide/manage-jenkins-cd-job-provision.md)
