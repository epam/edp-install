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

import com.openshift.internal.restclient.model.Secret;
import com.openshift.restclient.ClientBuilder;
import com.openshift.restclient.IClient;
import com.openshift.restclient.ResourceKind;
import io.qameta.allure.Feature;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import static com.epam.edp.sittests.smoke.StringConstants.BE_APP_NAME;
import static com.epam.edp.sittests.smoke.StringConstants.FE_APP_NAME;
import static com.epam.edp.sittests.smoke.StringConstants.GERRIT_PASSWORD;
import static com.epam.edp.sittests.smoke.StringConstants.GERRIT_USER;
import static com.epam.edp.sittests.smoke.StringConstants.OPENSHIFT_CICD_NAMESPACE;
import static com.epam.edp.sittests.smoke.StringConstants.OPENSHIFT_MASTER_URL;
import static com.epam.edp.sittests.smoke.StringConstants.OPENSHIFT_PASSWORD;
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

    private static final String CREATED_APP_SUFFIX = "created-java-project";

    private UrlBuilder urlBuilder;
    private IClient openShiftClient;
    private String openshiftNamespace;
    private String createdAppName;
    private String preCommitCreatedAppName;
    private String postCommitCreatedAppName;

    @Feature("Setup Openshift Client")
    @BeforeClass
    public void setUpOpenShiftClient() {
        this.openShiftClient = new ClientBuilder()
                .toCluster(OPENSHIFT_MASTER_URL)
                .withUserName(OPENSHIFT_USERNAME)
                .withPassword(OPENSHIFT_PASSWORD)
                .sslCertCallbackWithDefaultHostnameVerifier(true)
                .build();
    }

    @BeforeClass
    @Parameters("ocpEdpPrefix")
    public void setUp(String ocpEdpPrefix) {
        this.urlBuilder = new UrlBuilder(ocpEdpPrefix);
        this.openshiftNamespace = ocpEdpPrefix + "-" + OPENSHIFT_CICD_NAMESPACE;
        this.createdAppName = ocpEdpPrefix + "-" + CREATED_APP_SUFFIX;
        this.preCommitCreatedAppName = PRECOMMIT_PIPELINE_SUFFIX + createdAppName;
        this.postCommitCreatedAppName = POSTCOMMIT_PIPELINE_SUFFIX + createdAppName;
    }

    @DataProvider(name = "pipeline")
    public Object[][] pipeline() {
        return new Object[][]{{PRECOMMIT_BE_PIPELINE}, {PRECOMMIT_FE_PIPELINE}, {POSTCOMMIT_BE_PIPELINE},
                {POSTCOMMIT_FE_PIPELINE}, {preCommitCreatedAppName}, {postCommitCreatedAppName}};
    }

    @DataProvider(name = "application")
    public Object[][] application() {
        return new Object[][]{{BE_APP_NAME}, {FE_APP_NAME}, {createdAppName}};
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
        Secret secret = openShiftClient.get(ResourceKind.SECRET, "jenkins-token", openshiftNamespace);

        String username = new String(secret.getData("username")).trim();
        String token = new String(secret.getData("token")).trim();

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

    @Test
    public void testProjectWasCreatedInGitGroupRepoForCreateStrategy() {
        Secret secret = openShiftClient.get(ResourceKind.SECRET, "vcs-autouser", "edp-deploy");

        String username = new String(secret.getData("username")).trim();
        String password = new String(secret.getData("password")).trim();

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
                .get("https://git.epam.com/api/v4/projects/epmd-edp%2Ftemp%2F" + createdAppName)
                .then()
                .statusCode(HttpStatus.SC_OK);
    }
}
