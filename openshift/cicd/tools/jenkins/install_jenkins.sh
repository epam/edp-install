#!/usr/bin/env bash
#oc login -u admin -p admin

oc new-project $1 --display-name="$1"

# add permission to admin
oc policy add-role-to-user edit admin -n $1

# create template
oc create -f jenkins.yaml -n $1

# create app
oc new-app jenkins-persistent-glusterfs -e INSTALL_PLUGINS=analysis-core:1.92,findbugs:4.71,pmd:3.49,checkstyle:3.49,dependency-check-jenkins-plugin:2.1.1,htmlpublisher:1.14,jacoco:2.2.1,analysis-collector:1.52 -n $1

# Remove all Jenkins objects
oc delete deploymentconfig -n ci-cd jenkins
oc delete route -n ci-cd jenkins
oc delete pvc -n ci-cd jenkins
oc delete service -n ci-cd jenkins
oc delete service -n ci-cd jenkins-jnlp
oc delete serviceaccount -n ci-cd jenkins
oc delete cm -n ci-cd jenkins-slaves
oc delete rolebinding -n ci-cd jenkins_edit

# Remove all Nexus objects
oc delete deploymentconfig -n ci-cd nexus
oc delete route -n ci-cd nexus
oc delete pvc -n ci-cd nexus-pv
oc delete service -n ci-cd nexus
oc delete serviceaccount -n ci-cd nexus
oc delete job -n ci-cd nexus-integration

# Remove all Gerrit objects
oc delete dc -n ci-cd gerrit
oc delete dc -n ci-cd gerrit-db
oc delete route -n ci-cd gerrit
oc delete service -n ci-cd gerrit
oc delete service -n ci-cd gerrit-db
oc delete pvc -n ci-cd gerrit-data
oc delete pvc -n ci-cd gerrit-db
oc delete cm -n ci-cd gerrit
oc delete serviceaccount -n ci-cd gerrit

# Add permissions for Sonar and Gerrit service accounts
oc -n ci-cd adm policy add-scc-to-user anyuid -z sonar
oc -n ci-cd adm policy add-scc-to-user anyuid -z gerrit