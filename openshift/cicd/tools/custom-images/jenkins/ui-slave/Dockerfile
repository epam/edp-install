FROM openshift/jenkins-slave-nodejs-centos7
USER root
RUN rm -rf /usr/local/bin/npm /usr/local/share/man/man1/node* /usr/local/lib/dtrace/node.d ~/.npm ~/.node-gyp /opt/local/bin/node opt/local/include/node /opt/local/lib/node_modules /opt/rh/rh-nodejs4/
RUN yum install epel-release -y
RUN curl --silent --location https://rpm.nodesource.com/setup_6.x | bash -
RUN yum install nodejs -y --nogpgcheck 