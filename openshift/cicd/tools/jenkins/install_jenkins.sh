#!/usr/bin/env bash
#oc login -u admin -p admin

oc new-project $1 --display-name="$1"

# add permission to admin
oc policy add-role-to-user edit admin -n $1

# create template
oc create -f jenkins.yaml -n $1

# create app
oc new-app jenkins-persistent-glusterfs --param=MEMORY_LIMIT=1Gi -e INSTALL_PLUGINS=analysis-core:1.92,findbugs:4.71,pmd:3.49,checkstyle:3.49,dependency-check-jenkins-plugin:2.1.1,htmlpublisher:1.14,jacoco:2.2.1,analysis-collector:1.52 -n $1

# Create SI project
oc new-project SI --display-name="SI"

# add permission to admin
oc policy add-role-to-user edit admin -n SI


# Give Jenkins access to SI project