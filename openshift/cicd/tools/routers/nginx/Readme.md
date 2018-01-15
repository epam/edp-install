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

Additionally, you should know some moments about proxy_pass directive
```
A request URI is passed to the server as follows:

If the proxy_pass directive is specified with a URI, then when a request is passed to the server, the part of a normalized request URI matching the location is replaced by a URI specified in the directive:
location /name/ {
    proxy_pass http://127.0.0.1/remote/;
}
If proxy_pass is specified without a URI, the request URI is passed to the server in the same form as sent by a client when the original request is processed, or the full normalized request URI is passed when processing the changed URI:
location /some/path/ {
    proxy_pass http://127.0.0.1;
}
Before version 1.1.12, if proxy_pass is specified without a URI, the original request URI might be passed instead of the changed URI in some cases.
```

The above can be handy when you don't have localion, for instance, `api` in your target service. In this case, you should use trailing slash at the end of proxy_pass directive.