package com.epam.edp.sittests.smoke;

/**
 * @author Pavlo_Yemelianov
 */
public class StringConstants {
    public static final String BE_APP_NAME = "springboot-helloworld";
    public static final String FE_APP_NAME = "react-helloworld";

    public static final String RABBITMQ_SERVICE_NAME = "rabbitmq";

    public static final String PRECOMMIT_PIPELINE_SUFFIX = "gerrit-precommit-";
    public static final String POSTCOMMIT_PIPELINE_SUFFIX = "gerrit-postcommit-";
    public static final String DELETION_PIPELINE_NAME = "deletion-pipeline";

    public static final String JENKINS_USER = "admin";
    public static final String JENKINS_PASSWORD = "password";

    public static final String GERRIT_USER = "admin";
    public static final String GERRIT_PASSWORD = "secret";

    public static final String OPENSHIFT_CICD_NAMESPACE = "edp-cicd";
    public static final String OPENSHIFT_MASTER_URL = "https://openshift.main.edp.projects.epam.com:8443";
    public static final String OPENSHIFT_USERNAME = "integration_tests";
    public static final String OPENSHIFT_PASSWORD = "tests2018";
    public static final Boolean OPENSHIFT_TRUST_CERTS = false;
}
