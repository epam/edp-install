#!/bin/bash
set -e
set -x

GERRIT_ADMIN_UID=${GERRIT_ADMIN_UID:-auto_epmc-java_vcs}
GERRIT_ADMIN_ID=${GERRIT_ADMIN_ID:-1}
GERRIT_ADMIN_HASH=${GERRIT_ADMIN_HASH:-'bcrypt:4:wxgrLX9c2mqVW1pB2554nQ==:mBx+9xsrIqPfz9+QCMPIp2redIfm+ot5'}
GERRIT_ADMIN_PASS=${GERRIT_ADMIN_PASS:-'6pn9EBU2+lum5Ipwp6jyhwkbM+g+AyWPvr+1IzSoVg'}
GERRIT_ADMIN_EMAIL=${GERRIT_ADMIN_EMAIL:-auto_epmc-java_vcs@epam.com}
DEFAULT_REPOSITORY=${DEFAULT_REPOSITORY:-'git@git.epam.com:yevgen_mospan/oc-petclinic.git'}
CHECKOUT_DIR=./git

JENKINS_NAME=${JENKINS_NAME:-jenkins-[0-9]}
JENKINS_HOST=${JENKINS_HOST:-jenkins}
GERRIT_NAME=${GERRIT_NAME:-gerrit-[0-9]}
GERRIT_SSH_HOST=${GERRIT_SSH_HOST:-gerrit}
DATABASE_TYPE=${DATABASE_TYPE:-psql}

DEFAULT_PROJECT=${DEFAULT_PROJECT:-oc-petclinic}

GERRIT_POD_NAME="$(kubectl get pods --show-all | grep ${GERRIT_NAME} | awk '{print $1;}')"
NEXT_WAIT_TIME=1
while [ "$GERRIT_POD_NAME" == "" ] && [ $NEXT_WAIT_TIME -le 3 ]
do
    sleep 10
    GERRIT_POD_NAME="$(kubectl get pods --show-all | grep ${GERRIT_NAME} | awk '{print $1;}')"
    (( NEXT_WAIT_TIME++ ))
done

while ! nc -w 1 $GERRIT_SSH_HOST 8080 </dev/null
do
  echo -n .
  sleep 10
done

ssh-keygen -y -f ~/.ssh/id_rsa > /tmp/id_rsa.pub

if [ ${DATABASE_TYPE} == "psql" ]; then
  export PGPASSWORD=${GERRIT_DATABASE_PASSWORD}
  DATABASE_CONNECTION_STRING="psql -h ${GERRIT_DATABASE_HOST} -U ${GERRIT_DATABASE_USER} -d ${GERRIT_DATABASE_NAME}"
else
  echo "Not yet supported"
fi

if echo "SELECT * FROM ACCOUNTS WHERE FULL_NAME = '${GERRIT_ADMIN_UID}';" | ${DATABASE_CONNECTION_STRING} | grep "(0 rows)"; then
  echo "INSERT INTO ACCOUNTS (FULL_NAME, PREFERRED_EMAIL, REGISTERED_ON, ACCOUNT_ID) VALUES ('${GERRIT_ADMIN_UID}', '${GERRIT_ADMIN_EMAIL}', now(), ${GERRIT_ADMIN_ID});" | ${DATABASE_CONNECTION_STRING}
  echo "INSERT INTO ACCOUNT_EXTERNAL_IDS (ACCOUNT_ID, EMAIL_ADDRESS, PASSWORD, EXTERNAL_ID) VALUES (${GERRIT_ADMIN_ID}, '${GERRIT_ADMIN_EMAIL}', '${GERRIT_ADMIN_HASH}', 'username:${GERRIT_ADMIN_UID}');" | ${DATABASE_CONNECTION_STRING}
  echo "INSERT INTO ACCOUNT_GROUP_MEMBERS (ACCOUNT_ID, GROUP_ID) VALUES (${GERRIT_ADMIN_ID}, 1);" | ${DATABASE_CONNECTION_STRING}
  echo "INSERT INTO ACCOUNT_GROUP_MEMBERS (ACCOUNT_ID, GROUP_ID) VALUES (${GERRIT_ADMIN_ID}, 2);" | ${DATABASE_CONNECTION_STRING}
fi

if [[ "`echo $(curl -I -u ${GERRIT_ADMIN_UID}:${GERRIT_ADMIN_PASS} http://${GERRIT_SSH_HOST}:8080/a/projects 2>/dev/null | head -n 1 | cut -d$' ' -f2)`" != "200" ]]; then
#Gerrit reboot
  kubectl -n ${NAMESPACE} delete pod ${GERRIT_POD_NAME}
  sleep 10
  while ! nc -w 1 ${GERRIT_SSH_HOST} 8080 </dev/null
  do
    echo -n .
    sleep 10
  done
  curl -X POST -u ${GERRIT_ADMIN_UID}:${GERRIT_ADMIN_PASS} -H "Content-Type: text/plain" --data "$(cat /tmp/id_rsa.pub)"  http://${GERRIT_SSH_HOST}:8080/a/accounts/${GERRIT_ADMIN_ID}/sshkeys
fi

#Create MSRA project
test "`echo $(curl -I -u ${GERRIT_ADMIN_UID}:${GERRIT_ADMIN_PASS} http://${GERRIT_SSH_HOST}:8080/a/projects/${DEFAULT_PROJECT}.git 2>/dev/null | head -n 1 | cut -d$' ' -f2)`" != "200" && ssh -i /root/.ssh/id_rsa -p 29418 ${GERRIT_ADMIN_UID}@${GERRIT_SSH_HOST} gerrit create-project ${DEFAULT_PROJECT}.git

#checkout project.config from All-Project.git
[ -d ${CHECKOUT_DIR} ] && mv ${CHECKOUT_DIR} ${CHECKOUT_DIR}.$$
mkdir ${CHECKOUT_DIR}

git init ${CHECKOUT_DIR}
cd ${CHECKOUT_DIR}

#start ssh agent and add ssh key
eval "$(ssh-agent)"
ssh-add /root/.ssh/id_rsa

#git config
git config user.name  ${GERRIT_ADMIN_UID}
git config user.email ${GERRIT_ADMIN_EMAIL}
git remote add origin ssh://${GERRIT_ADMIN_UID}@${GERRIT_SSH_HOST}:29418/All-Projects
#checkout project.config
git fetch -q origin refs/meta/config:refs/remotes/origin/meta/config
git checkout meta/config

#add label.Jenkins-Verified
git config -f project.config label.Verified.function MaxWithBlock
git config -f project.config --add label.Verified.defaultValue  0
git config -f project.config --add label.Verified.value "-1 Fails"
git config -f project.config --add label.Verified.value "0 No score"
git config -f project.config --add label.Verified.value "+1 Verified"

#add label.Sonar-Verified
git config -f project.config label.Sonar-Verified.function MaxWithBlock
git config -f project.config --add label.Sonar-Verified.defaultValue  0
git config -f project.config --add label.Sonar-Verified.value "-1 Fails"
git config -f project.config --add label.Sonar-Verified.value "0 No score"
git config -f project.config --add label.Sonar-Verified.value "+1 Verified"

##commit and push back
git add .
git commit -a -m "Added label - Verified and Sonar-Verified"

#Change global access right
##Remove anonymous access right.
git config -f project.config --unset access.refs/*.read "group Anonymous Users"
##add Jenkins access and verify right
git config -f project.config --add access.refs/heads/*.read "group Non-Interactive Users"
git config -f project.config --add access.refs/tags/*.read "group Non-Interactive Users"
git config -f project.config --add access.refs/heads/*.label-Code-Review "-1..+1 group Non-Interactive Users"
git config -f project.config --add access.refs/heads/*.label-Verified "-1..+1 group Non-Interactive Users"
git config -f project.config --add access.refs/heads/*.label-Sonar-Verified "-1..+1 group Non-Interactive Users"
##add project owners' right to add verify flag
git config -f project.config --add access.refs/heads/*.label-Verified "-1..+1 group Project Owners"
git config -f project.config --add access.refs/heads/*.label-Sonar-Verified "-1..+1 group Project Owners"
##commit and push back
git commit -a -m "Change access right." -m "Add access right for Jenkins. Remove anonymous access right"
git push origin meta/config:meta/config

cd -
rm -rf ${CHECKOUT_DIR}

#Create project

cd /root/
git clone --mirror ${DEFAULT_REPOSITORY}
cd javacc-microservices-taxi.git/
git remote set-url --push origin ssh://${GERRIT_ADMIN_UID}@gerrit:29418/${DEFAULT_PROJECT}
git fetch -p origin
git push --mirror 2> /dev/null || echo 0

#stop ssh agent
kill ${SSH_AGENT_PID}

#Gerrit reboot
kubectl -n ${NAMESPACE} delete pod $(kubectl get pods --show-all | grep ${GERRIT_NAME} | awk '{print $1;}')
sleep 10
while ! nc -w 1 $GERRIT_SSH_HOST 8080 </dev/null
do
  echo -n .
  sleep 10
done

exit 0

