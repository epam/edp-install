FROM openshift/origin
RUN yum install -y git ansible jq
WORKDIR /opt/edp/
COPY ansible-playbooks /opt/edp/ansible-playbooks
COPY application-pipelines /opt/edp/application-pipelines