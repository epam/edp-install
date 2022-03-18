# Delete Jenkins Job Provision

To delete the job provisioner, take the following steps:

1. Delete the job provisioner from Jenkins. Navigate to Admin Console->Jenkins->jobs->job-provisions folder, select the necessary provisioner and click the drop-down right to the provisioner name.
Select **Delete project**.

  !![Delete job provisioner](../assets/operator-guide/delete-job-from-jenkins.png "Delete job provisioner")

2. Run the commands below in the 'edp-db' pod:

        psql edp-db admin
        # Replace admin with the admin login from the db-admin-console secrets

        SET search_path to <NAMESPACE_NAME>;

        SELECT * FROM job_provisioning;

3. Check the id of the necessary provisioner.

4. Run the deletion command:

        DELETE FROM job_provisioning WHERE id=<PROVISIONER_ID>;
