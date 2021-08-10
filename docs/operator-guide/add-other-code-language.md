# Add Other Code Language

There is an ability to extend the default code languages when creating a codebase with the clone/import strategy.

![other-language](../assets/operator-guide/ac_other_language.png "other-language")

!!! warning
    The create strategy does not allow to customize the default code language set.

In order to customize the Build Tool list, perform the following:

*  Edit the edp-admin-console deployment by adding the necessary code language into the **BUILD TOOLS** field:
   
       kubectl edit deployment edp-admin-console -n <edp-project>

!!! note 
    On an OpenShift cluster, run the `oc` command instead of `kubectl` one.

!!! info
    &#8249;edp-project&#8250; is the name of the EDP tenant here and in all the following steps.

<details>
<summary><b>View: edp-admin-console deployment</b></summary>

```yaml
...
spec:
  containers:
  - env:
    ...
    - name: BUILD_TOOLS
      value: docker # List of custom build tools in Admin Console, e.g. 'docker,helm';
    ...
...
```
</details>

* Add the Jenkins agent by following the [instruction](https://github.com/epam/edp-jenkins-operator/blob/master/documentation/add-jenkins-slave.md#add-jenkins-slave).

* Add the Custom CI pipeline provisioner by following the [instruction](./manage-jenkins-job-provision.md#add-custom-ci-pipeline-job-provisioner).

* As a result, the newly added Jenkins agent will be available in the **Select Jenkins Slave** dropdown list of the
Advanced Settings block during the codebase creation:

  ![jenkins-agent](../assets/operator-guide/ac_jenkins_agent.png "jenkins-agent")

If it is necessary to create Code Review and Build pipelines, add corresponding entries (e.g. stages[Build-application-docker], [Code-review-application-docker]). See the example below:

```java
...
stages['Code-review-application-docker'] = '[{"name": "gerrit-checkout"}' + "${commitValidateStage}" + ',{"name": "sonar"}]'
stages['Build-application-docker'] = '[{"name": "checkout"},{"name": "get-version"},{"name": "sonar"},' +
                                     '{"name": "build-image-kaniko"}' + "${createJFVStage}" + ',{"name": "git-tag"}]'
...
```

![jenkins-provisioner](../assets/operator-guide/ac_jenkins_provisioner.png "jenkins-provisioner")

!!! note
    Application is one of the available options. Another option might be to add a library. Please refer to the [Add Library](../user-guide/add-library.md) page for details

## Related Articles

* [Manage Jenkins Agent](add-jenkins-agent.md)
* [Add Job Provisioner](manage-jenkins-ci-job-provision.md)
* [Add Library](../user-guide/add-library.md)
