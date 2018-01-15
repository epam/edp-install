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
default-backend-service in ingress-deployment.yaml
```
```
serviceName: frontend in ingress.yaml
servicePort: 3000 in ingress.yaml
```
All of the services must exist, specifically default-backend-service, otherwise you will get your pod crashed.

3. Having wanted rights, let's create deployment of **Ingress** object in Openshift
```
oc create -f ingress-deployment.yaml
```

4. Create **Ingress** object in Openshift
```
oc create -f ingress.yaml
```
You can modify configuration of the ingress controller on the fly, especially change service to be proxied. And ingress controller automatically reloads new configuration.

If you'd like to use more secure way of deploying you can try out [this official link] (https://github.com/kubernetes/ingress-nginx/tree/master/deploy#install-with-rbac-roles)