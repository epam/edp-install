FROM openshift/origin

ARG TOOLS_PATH

RUN yum install -y git ansible

WORKDIR /opt/edp/

COPY $TOOLS_PATH /opt/edp/
