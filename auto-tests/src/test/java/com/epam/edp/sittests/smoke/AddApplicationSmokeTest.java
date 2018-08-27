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

import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.ConfigBuilder;
import io.fabric8.openshift.client.DefaultOpenShiftClient;
import io.fabric8.openshift.client.OpenShiftClient;
import io.qameta.allure.Feature;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpStatus;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.util.Map;

import static com.epam.edp.sittests.smoke.StringConstants.BE_APP_NAME;
import static com.epam.edp.sittests.smoke.StringConstants.FE_APP_NAME;
import static com.epam.edp.sittests.smoke.StringConstants.GERRIT_PASSWORD;
import static com.epam.edp.sittests.smoke.StringConstants.GERRIT_USER;
import static com.epam.edp.sittests.smoke.StringConstants.OPENSHIFT_CICD_NAMESPACE;
import static com.epam.edp.sittests.smoke.StringConstants.OPENSHIFT_MASTER_URL;
import static com.epam.edp.sittests.smoke.StringConstants.OPENSHIFT_PASSWORD;
import static com.epam.edp.sittests.smoke.StringConstants.OPENSHIFT_TRUST_CERTS;
import static com.epam.edp.sittests.smoke.StringConstants.OPENSHIFT_USERNAME;
import static com.epam.edp.sittests.smoke.StringConstants.POSTCOMMIT_PIPELINE_SUFFIX;
import static com.epam.edp.sittests.smoke.StringConstants.PRECOMMIT_PIPELINE_SUFFIX;
import static io.restassured.RestAssured.given;

/**
 * @author Pavlo_Yemelianov
 */
public class AddApplicationSmokeTest {
    private static final String PRECOMMIT_BE_PIPELINE = PRECOMMIT_PIPELINE_SUFFIX + BE_APP_NAME;
    private static final String PRECOMMIT_FE_PIPELINE = PRECOMMIT_PIPELINE_SUFFIX + FE_APP_NAME;

    private static final String POSTCOMMIT_BE_PIPELINE = POSTCOMMIT_PIPELINE_SUFFIX + BE_APP_NAME;
    private static final String POSTCOMMIT_FE_PIPELINE = POSTCOMMIT_PIPELINE_SUFFIX + FE_APP_NAME;

    private UrlBuilder urlBuilder;
    private OpenShiftClient openShiftClient;
    private String openshiftNamespace;

    @Feature("Setup Openshift Client")
    @BeforeClass
    public void setUpOpenShiftClient() {
        Config config = new ConfigBuilder().withMasterUrl(OPENSHIFT_MASTER_URL)
                .withUsername(OPENSHIFT_USERNAME)
                .withPassword(OPENSHIFT_PASSWORD)
                .withTrustCerts(OPENSHIFT_TRUST_CERTS)
                .build();

        this.openShiftClient = new DefaultOpenShiftClient(config);
    }

    @BeforeClass
    @Parameters("ocpEdpPrefix")
    public void setUp(String ocpEdpPrefix) {
        this.urlBuilder = new UrlBuilder(ocpEdpPrefix);
        this.openshiftNamespace = ocpEdpPrefix + "-" + OPENSHIFT_CICD_NAMESPACE;
    }

    @DataProvider(name = "pipeline")
    public static Object[][] pipeline() {
        return new Object[][]{{PRECOMMIT_BE_PIPELINE}, {PRECOMMIT_FE_PIPELINE}, {POSTCOMMIT_BE_PIPELINE},
                {POSTCOMMIT_FE_PIPELINE}};
    }

    @DataProvider(name = "application")
    public static Object[][] application() {
        return new Object[][]{{BE_APP_NAME}, {FE_APP_NAME}};
    }

    @Test(dataProvider = "application")
    public void testGerritProjectWasCreated(String application) {
        given().log().all()
                .pathParam("project", application)
                .auth()
                .basic(GERRIT_USER, GERRIT_PASSWORD)
                .when()
                .get(urlBuilder.buildUrl("https",
                        "gerrit",
                        "edp-cicd",
                        "a/projects/{project}"))
                .then()
                .statusCode(HttpStatus.SC_OK);
    }

    @Test(dataProvider = "pipeline")
    public void testJenkinsPipelineWasCreated(String pipeline) {
        Map<String, String> credentials = openShiftClient.secrets()
                .inNamespace(openshiftNamespace)
                .withName("jenkins-token")
                .get()
                .getData();

        String username = new String(Base64.decodeBase64(credentials.get("username"))).trim();
        String token = new String(Base64.decodeBase64(credentials.get("token"))).trim();

        given().log().all()
                .pathParam("pipeline", pipeline)
                .auth()
                .preemptive()
                .basic(username, token)
                .when()
                .get(urlBuilder.buildUrl("https",
                        "jenkins",
                        "edp-cicd",
                        "job/{pipeline}/api/json"))
                .then()
                .statusCode(HttpStatus.SC_OK);
    }

    @Test(dataProvider = "application")
    public void testApplicationTemplateHasBeenAdded(String application) {
        given().log().all()
                .pathParam("application", application)
                .urlEncodingEnabled(false)
                .when()
                .get(urlBuilder.buildUrl("https",
                        "gerrit", OPENSHIFT_CICD_NAMESPACE,
                        "projects/{application}/branches/master/files/deploy-templates%2F{application}.yaml/content"))
                .then()
                .statusCode(HttpStatus.SC_OK);
    }
}
