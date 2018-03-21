** Gerrit **

The following steps needs to be done before gerrit will be ready for deployment:

1. copy /root/.kube.config file from master node to your machine
2. oc login -u system:admin -n default
3. oc -n ci-cd adm policy add-scc-to-user anyuid -z gerrit
4. oc -n ci-cd create secret generic gerrit-db --from-literal=database-name=[db-name] --from-literal=database-user=[user] --from-literal=database-password=[password]
5. oc -n ci-cd create secret generic gitlab-oauth --from-literal=secret=[openid_secret] --from-literal=id=[openid_id]
6. oc -n ci-cd create -f gerrit.yaml
7. Deploy gerrit from template through Openshift UI
8. Put your private ssh key from you auto-user for replication to path /var/gerrit/review_site/id_rsa in gerrit pod
9. To get OAUTH you need:
```
oc login kubernetes.default:443 --username=$OPENSHIFT_ADMIN --password=$OPENSHIFT_PASSWORD --insecure-skip-tls-verify
oc project $OPENSHIFT_PROJECT
oc set env dc gerrit AUTH_TYPE='OAUTH'
```
