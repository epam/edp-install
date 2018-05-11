FROM openshift/origin
RUN yum install -y git ansible jq
WORKDIR /opt/edp/
COPY ansible-playbooks /opt/edp/ansible-playbooks
COPY application-pipelines /opt/edp/application-pipelines
COPY oc-templates /opt/edp/oc-templates
COPY edp-images /opt/edp/edp-images
COPY README.md /opt/edp/
