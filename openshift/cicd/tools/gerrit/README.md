copy /root/.kube.config file from master node to your machine
oc login -u system:admin -n default
oc -n ci-cd adm policy add-scc-to-user anyuid -z gerrit
oc create secret generic gerrit-db --from-literal=database-name=[db-name] --from-literal=database-user=[user] --from-literal=database-password=[password]
oc create secret generic auto_epmc-java_vcs_keys --from-file=.config=/root/.ssh/config --from-file=id_rsa=[private_key] --from-file=id_rsa.pub=/root/.ssh/[public_key]
oc create configmap gerrit --from-file=replication.config
