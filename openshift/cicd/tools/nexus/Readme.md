# Nexus installing and configuring

## Prerequisites

- Please, ensure that you have Jenkins installed and you have **Credentials plugin** installed and enabled.
- Please, ensure that you have turned on **CSRF Protection** on the page **http://server:port/configureSecurity/**, including **Default Crumb Issuer** enabled.

1. Deploy **Nexus template** from **nexus-template.yaml** to Openshift cluster
2. Deploy **Nexus instance** from previously created template
3. Deploy **Nexus job template** from **nexus-job-template.yaml** to Openshift cluster
4. Create special **Configmap** with name **nexus-integration**, for instance "oc -n your-project create configmap nexus-integration --from-file=config.py"
5. Deploy **Nexus job instance** from previously created template in order to **configure Nexus instance** from step 2
6. Check out that you have all necessary objects in Nexus and at least two Nexus users in Jenkins which have rights to read and publish respectively.