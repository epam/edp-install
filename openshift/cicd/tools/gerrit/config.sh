oc login kubernetes.default:443 --username=$OPENSHIFT_ADMIN --password=$OPENSHIFT_PASSWORD
oc project $OPENSHIFT_PROJECT
oc set env dc gerrit AUTH_TYPE='OAUTH'