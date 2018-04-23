FROM openshift/origin
RUN yum install -y git ansible jq
WORKDIR /opt/edp/
COPY ansible-playbooks /opt/edp/ansible-playbooks
COPY application-pipelines /opt/edp/application-pipelines
COPY oc-templates/add-business-application.yaml /opt/edp/templates/
COPY oc-templates/add-environments.yaml /opt/edp/templates/
COPY oc-templates/add-autotests.yaml /opt/edp/templates/
