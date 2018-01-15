# Installing and configuring ingress controller in Openshift

## Prerequisites

- Please, ensure that you have Openshift installed and you have admin rights to manage it.

## Workflow

1. To try this stuff, let's roll out **Ingress controller** with default account. That's why we will need to give proper rights to the account:
```
oc adm policy add-scc-to-user anyuid -n sit -z default
oc adm policy add-cluster-role-to-user cluster-admin -n sit -z default
```
Also we need to amend the parameter **hostPort: true** while editing the following instance:
```
oc edit scc anyuid
```

2. Adjust your services' names in both yaml files. Draw your attention to these sections
```
default-backend-service in nginx-deployment.yaml
```
```
serviceName: frontend in nginx-conf.yaml
servicePort: 3000 in nginx-conf.yaml
```
All of the services must exist, otherwise your nginx pod might be crashed.

3. Having wanted rights, let's create **Nginx configuration** in Openshift
```
oc create -f nginx-conf.yaml
```

4. Create **Nginx deployment** in Openshift
```
oc create -f nginx-deployment.yaml
```