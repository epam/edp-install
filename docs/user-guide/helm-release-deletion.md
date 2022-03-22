# Helm Release Deletion

The **Helm release deletion** stage provides the ability to remove Helm releases from the namespace.

!!! Note
    Pay attention that this stages will remove all Helm releases from the namespace. To avoid loss of important data, before using this stage, make the necessary backups.

To remove Helm releases, follow the steps below:

1. Add the following step to the CD pipeline `{"name":"helm-uninstall","step_name":"helm-uninstall"}`. Alternatively, with this step, it is possible to create a [custom job provisioner](../operator-guide/manage-jenkins-cd-job-provision.md).

2. Run the job. The pipeline script will remove Helm releases from the namespace.

### Related Articles

* [Customize CD Pipeline](customize-cd-pipeline.md)
* [Manage Jenkins CD Pipeline Job Provisioner](../operator-guide/manage-jenkins-cd-job-provision.md)
