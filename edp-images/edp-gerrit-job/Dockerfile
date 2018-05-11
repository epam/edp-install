FROM openshift/origin
RUN yum install -y bash git openssh
WORKDIR /tmp
RUN curl -L -o add-user-plugin-2.11.3.jar "https://nexus-infra.main.edp.projects.epam.com/repository/gerrit-plugin/add-user-plugin-2.11.3.jar"