# Migrate CI Pipelines From Jenkins to Tekton

To migrate the CI pipelines for a codebase from Jenkins to Tekton, follow the steps below:

- [Migrate CI Pipelines From Jenkins to Tekton](#migrate-ci-pipelines-from-jenkins-to-tekton)
- [Deploy a Custom EDP Scenario With Tekton and Jenkins CI Tools](#deploy-a-custom-edp-scenario-with-tekton-and-jenkins-ci-tools)
- [Disable Jenkins Triggers](#disable-jenkins-triggers)
- [Manage Tekton Triggers the Codebase(s)](#manage-tekton-triggers-the-codebases)
- [Switch CI Tool for Codebase(s)](#switch-ci-tool-for-codebases)

## Deploy a Custom EDP Scenario With Tekton and Jenkins CI Tools

Make sure that Tekton stack is deployed according to the [documentation](../operator-guide/prerequisites.md#edp-installation-scenarios).
Enable Tekton as an EDP subcomponent:

```yaml title="values.yaml"
edp-tekton:
  enabled: true
```

## Disable Jenkins Triggers

To disable Jenkins Triggers for the codebase, add the following code to the provisioner:

```groovy title="job-provisioner"
def tektonCodebaseList = ["<codebase_name>"]
if (!tektonCodebaseList.contains(codebaseName.toString())){
    triggers {
        gerrit {
            events {
                if (pipelineName.contains("Build"))
                    changeMerged()
                else
                    patchsetCreated()
            }
            project("plain:${codebaseName}", ["plain:${watchBranch}"])
        }
    }
}
```

!!! note
    The sample above shows the usage of Gerrit VCS where the `<codebase_name>` value is your codebase name.

* If using GitHub or GitLab, additionally remove the webhook from the relevant repository.
* If webhooks generation for new codebase(s) is not required, correct the code above so that it creates a webhook in the job-provisioner.
* To recreate the pipeline in Jenkins, trigger the job-provisioner.
* Check that the new pipeline is created without triggering Gerrit events.

## Manage Tekton Triggers the Codebase(s)

By default, each Gerrit project inherits configuration from the **All-Projects** repository.

To exclude triggering in Jenkins and Tekton CI tools simultaneously, edit the configuration in the **All-Projects** repository or in the project which inherits rights from your project.

Edit the **webhooks.config** file in the **refs/meta/config** and remove all context from this configuration.

!!! warning
    The clearance of the **webhooks.config** file will disable the pipeline trigger in Tekton.

To use Tekton pipelines, add the configuration to the corresponding Gerrit project (**webhooks.config** file in the **refs/meta/config**):

```yaml title="webhooks.config"

[remote "changemerged"]
  url = http://el-gerrit-listener:8080
  event = change-merged
[remote "patchsetcreated"]
  url = http://el-gerrit-listener:8080
  event = patchset-created

```

## Switch CI Tool for Codebase(s)

Go to the codebase Custom Resource and change the `spec.ciTool` field from `jenkins` to `tekton`.

## Related Articles
* [Install EDP](install-edp.md)
* [Install Tekton](install-tekton.md)
