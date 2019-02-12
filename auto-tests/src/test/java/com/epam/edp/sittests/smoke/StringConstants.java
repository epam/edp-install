/* Copyright 2018 EPAM Systems.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at
http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.

See the License for the specific language governing permissions and
limitations under the License. */

package com.epam.edp.sittests.smoke;

/**
 * @author Pavlo_Yemelianov
 */
public class StringConstants {
    public static final String BE_APP_NAME = "springboot-helloworld";
    public static final String FE_APP_NAME = "react-helloworld";

    public static final String RABBITMQ_SERVICE_NAME = "rabbitmq";

    public static final String PRECOMMIT_PIPELINE_SUFFIX = "code-review-";
    public static final String POSTCOMMIT_PIPELINE_SUFFIX = "gerrit-postcommit-";
    public static final String DELETION_PIPELINE_NAME = "deletion-pipeline";

    public static final String GERRIT_USER = "admin";
    public static final String GERRIT_PASSWORD = "secret";

    public static final String OPENSHIFT_CICD_NAMESPACE = "edp-cicd";
    public static final String OPENSHIFT_MASTER_URL = "https://master.delivery.aws.main.edp.projects.epam.com";
    public static final String OPENSHIFT_USERNAME = "integration_tests";
    public static final String OPENSHIFT_PASSWORD = "tests2018";
    public static final Boolean OPENSHIFT_TRUST_CERTS = false;

    public static final String KEYCLOAK_URL = "https://keycloak-security.delivery.aws.main.edp.projects.epam.com";
}
