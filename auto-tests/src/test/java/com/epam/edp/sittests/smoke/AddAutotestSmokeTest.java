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
import io.restassured.http.ContentType;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpStatus;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.util.Map;

import static com.epam.edp.sittests.smoke.StringConstants.GERRIT_PASSWORD;
import static com.epam.edp.sittests.smoke.StringConstants.GERRIT_USER;
import static com.epam.edp.sittests.smoke.StringConstants.JENKINS_PASSWORD;
import static com.epam.edp.sittests.smoke.StringConstants.JENKINS_USER;
import static com.epam.edp.sittests.smoke.StringConstants.OPENSHIFT_MASTER_URL;
import static com.epam.edp.sittests.smoke.StringConstants.OPENSHIFT_PASSWORD;
import static com.epam.edp.sittests.smoke.StringConstants.OPENSHIFT_TRUST_CERTS;
import static com.epam.edp.sittests.smoke.StringConstants.OPENSHIFT_USERNAME;
import static com.epam.edp.sittests.smoke.StringConstants.PRECOMMIT_PIPELINE_SUFFIX;
import static io.restassured.RestAssured.given;

/**
 * @author Pavlo_Yemelianov
 */
public class AddAutotestSmokeTest {
    private static final String AUTOTESTS_NAME = "helloworld-autotests";
    private static final String COPY_AUTOTEST_PREFIX = "copy-autotest";

    private String copyAutotestName;

    private UrlBuilder urlBuilder;
    private OpenShiftClient openShiftClient;

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
    @Parameters("ocpEdpSuffix")
    public void setUp(String ocpEdpSuffix) {
        this.copyAutotestName = COPY_AUTOTEST_PREFIX + "-" + ocpEdpSuffix;
        this.urlBuilder = new UrlBuilder(ocpEdpSuffix);
    }

    @DataProvider(name = "autotest")
    public Object[][] autotest() {
        return new Object[][]{{AUTOTESTS_NAME}, {copyAutotestName}};
    }

    @Test(dataProvider = "autotest")
    public void testGerritProjectWasCreated(String autotest) {
        given().log().all()
                .pathParam("project", autotest)
                .auth()
                .basic(GERRIT_USER, GERRIT_PASSWORD)
                .when()
                .get(urlBuilder.buildUrl("http",
                        "gerrit",
                        "edp-cicd",
                        "a/projects/{project}"))
                .then()
                .statusCode(HttpStatus.SC_OK);
    }

    @Test(dataProvider = "autotest")
    public void testJenkinsPipelineWasCreated(String autotest) {
        String pipeline = PRECOMMIT_PIPELINE_SUFFIX + autotest;
        given().log().all()
                .pathParam("pipeline", pipeline)
                .auth()
                .preemptive()
                .basic(JENKINS_USER, JENKINS_PASSWORD)
                .when()
                .get(urlBuilder.buildUrl("http",
                        "jenkins",
                        "edp-cicd",
                        "job/{pipeline}/api/json"))
                .then()
                .statusCode(HttpStatus.SC_OK);
    }

    @Test
    public void testCopyAutotestProjectWasCreatedInGitGroupRepo() {
        Map<String, String> credentials = openShiftClient.secrets()
                .inNamespace("edp-deploy")
                .withName("auto-epmc-java-vcs-with-ssh")
                .get()
                .getData();
        String username = new String(Base64.decodeBase64(credentials.get("username"))).trim();
        String password = new String(Base64.decodeBase64(credentials.get("password"))).trim();

        String token = given()
                .contentType(ContentType.URLENC)
                .param("grant_type", "password")
                .param("username", username)
                .param("password", password)
                .post("https://git.epam.com/oauth/token")
                .then()
                .extract()
                .path("access_token");

        given().log().all()
                .auth()
                .preemptive()
                .oauth2(token)
                .urlEncodingEnabled(false)
                .when()
                .get("https://git.epam.com/api/v4/projects/epmd-edp%2Ftemp%2F" + copyAutotestName)
                .then()
                .statusCode(HttpStatus.SC_OK);
    }
}
