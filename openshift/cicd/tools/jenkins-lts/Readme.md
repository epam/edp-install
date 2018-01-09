# Jenkins LTS without Openshift oath
1. Deploy Jenkins template from **jenkins-lts.yaml** to Openshift cluster
2. Deploy Jenkins instance from previously created template
3. Install basic plugins manually, especially pipeline plugin
4. Create pipeline job based on file **pipeline-install-plugins.groovy**