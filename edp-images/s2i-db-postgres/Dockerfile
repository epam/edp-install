FROM centos/s2i-base-centos7

MAINTAINER JavaCC <javacc@epam.com>

ENV LIQUIBASE_VERSION=3.4.2 \
    POSTGRESQL_VERSION=9.4.1212 \
    JAVA_VERSION=1.8.0

ENV LIQUIBASE_HOME=/opt/liquibase \
    JDBC_DRIVER_HOME=/opt/jdbc_drivers \
    APP_ROOT_SRC=$APP_ROOT/src \
    STI_SCRIPTS_PATH=/usr/libexec/s2i

# Set labels used in OpenShift to describe the builder images
LABEL io.k8s.description="Platform for running database liquibase migrations" \
      io.k8s.display-name="PostgreSQL database liquibase migration" \
      io.openshift.s2i.scripts-url="image:///usr/libexec/s2i" \
      io.openshift.s2i.destination="/opt/app-root"

# Install as root
USER root

RUN INSTALL_PKGS="tar unzip bc which lsof java-${JAVA_VERSION}-openjdk java-${JAVA_VERSION}-openjdk-devel" && \
    yum install -y --enablerepo=centosplus $INSTALL_PKGS && \
    rpm -V $INSTALL_PKGS && \
    yum clean all

RUN mkdir $LIQUIBASE_HOME && \
    curl -L https://github.com/liquibase/liquibase/releases/download/liquibase-parent-${LIQUIBASE_VERSION}/liquibase-${LIQUIBASE_VERSION}-bin.tar.gz | \
    tar -xz -C $LIQUIBASE_HOME && \
    chmod +x $LIQUIBASE_HOME/liquibase && \
    ln -s $LIQUIBASE_HOME/liquibase /usr/local/bin/ && \
    mkdir $JDBC_DRIVER_HOME && \
    curl -o $JDBC_DRIVER_HOME/psql-jdbc.jar https://jdbc.postgresql.org/download/postgresql-${POSTGRESQL_VERSION}.jar && \
    ln -s $JDBC_DRIVER_HOME/psql-jdbc.jar $LIQUIBASE_HOME/lib/

# S2I scripts
COPY s2i/bin $STI_SCRIPTS_PATH

RUN chown -R 1001:1001 $APP_ROOT && chmod -R ug+rwx $APP_ROOT

USER 1001

WORKDIR $APP_ROOT

CMD $STI_SCRIPTS_PATH/usage