# Prerequisuites

For HostPath template only:

1. oc login  -u system:admin -n default

2. oc edit scc anyuid

```
allowHostDirVolumePlugin: true
allowHostIPC: false
allowHostNetwork: false
allowHostPID: false
allowHostPorts: true
```

3. oc adm policy add-scc-to-user anyuid -n ci-cd -z jenkins

4. Prepare the catalog /opt/data/jenkins with rights chmod 777
5. Also you need to choose one of your slaves and add it an appropriate the following label to the chosen node:
```
 oc label node [your_node_name] role=jenkins
```
# Jenkins EDP
1. Deploy Jenkins template from **edp-jenkins.yaml** to Openshift cluster
2. Deploy Jenkins instance from previously created template