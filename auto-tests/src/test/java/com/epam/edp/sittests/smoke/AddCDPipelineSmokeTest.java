/* Copyright 2019 EPAM Systems.

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

import com.openshift.internal.restclient.model.Project;
import com.openshift.internal.restclient.model.Secret;
import com.openshift.restclient.ClientBuilder;
import com.openshift.restclient.IClient;
import com.openshift.restclient.ResourceKind;
import io.qameta.allure.Feature;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.testng.annotations.*;
import org.testng.Assert;

import static com.epam.edp.sittests.smoke.StringConstants.DELETION_PIPELINE_NAME;
import static com.epam.edp.sittests.smoke.StringConstants.OPENSHIFT_CICD_NAMESPACE;
import static com.epam.edp.sittests.smoke.StringConstants.OPENSHIFT_MASTER_URL;
import static com.epam.edp.sittests.smoke.StringConstants.OPENSHIFT_PASSWORD;
import static com.epam.edp.sittests.smoke.StringConstants.OPENSHIFT_USERNAME;
import static io.restassured.RestAssured.given;

public class AddCDPipelineSmokeTest {
    private String sitStageName;
    private String qaStageName;
    private String openshiftNamespace;
    private String openshiftSitNamespace;
    private String openshiftQaNamespace;
    private String CDPipelineName;
    private UrlBuilder urlBuilder;
    private IClient openShiftClient;
    private String adminconsoleAccessToken;

    @Feature("Setup Openshift Client")
    @BeforeClass
    public void setUpOpenShiftClient() {
        this.openShiftClient = new ClientBuilder()
                .toCluster(OPENSHIFT_MASTER_URL)
                .withUserName(OPENSHIFT_USERNAME)
                .withPassword(OPENSHIFT_PASSWORD)
                .sslCertCallbackWithDefaultHostnameVerifier(false)
                .build();
    }

    @Feature("Setup URL Builder")
    @BeforeClass
    @Parameters("ocpEdpPrefix")
    public void setUp(String ocpEdpPrefix) {
        this.urlBuilder = new UrlBuilder(ocpEdpPrefix);
        this.CDPipelineName = "team-a";
        this.sitStageName = "sit";
        this.qaStageName = "qa";
        this.openshiftNamespace = ocpEdpPrefix + "-" + OPENSHIFT_CICD_NAMESPACE;
        this.openshiftSitNamespace = ocpEdpPrefix + "-" + CDPipelineName + "-" + sitStageName;
        this.openshiftQaNamespace = ocpEdpPrefix + "-" + CDPipelineName + "-" + qaStageName;
    }

    @BeforeMethod
    @Parameters("ocpEdpPrefix")
    public void setUpAccessToken(String ocpEdpPrefix) {
        Secret secret = openShiftClient.get(ResourceKind.SECRET, "admin-console-creator", openshiftNamespace);

        String userName = new String(secret.getData("username")).trim();
        String userPassword = new String(secret.getData("password")).trim();

        secret = openShiftClient.get(ResourceKind.SECRET, "admin-console-client", openshiftNamespace);

        String clientName = new String(secret.getData("username")).trim();
        String clientPassword = new String(secret.getData("password")).trim();

        this.adminconsoleAccessToken = given()
                .contentType(ContentType.URLENC)
                .param("client_id", clientName)
                .param("client_secret", clientPassword)
                .param("grant_type", "password")
                .param("username", userName)
                .param("password", userPassword)
                .when()
                .post(StringConstants.KEYCLOAK_URL + "/auth/realms/"+ ocpEdpPrefix + "-edp/protocol/openid-connect/token")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .contentType(ContentType.JSON)
                .extract()
                .path("access_token");
    }

    @DataProvider(name = "stage")
    public Object[][] stage() {
        return new Object[][]{{sitStageName}, {qaStageName}};
    }

    @Test(dataProvider = "stage")
    public void testJenkinsTestPipelinesHasBeenCreated(String stage) {
        Secret secret = openShiftClient.get(ResourceKind.SECRET, "jenkins-admin-token", openshiftNamespace);

        String username = new String(secret.getData("username")).trim();
        String token = new String(secret.getData("password")).trim();

        given().log().all()
                .pathParam("stage", stage)
                .pathParam("folder", CDPipelineName + "-cd-pipeline")
                .auth()
                .preemptive()
                .basic(username, token)
                .when()
                .get(urlBuilder.buildUrl("https",
                        "jenkins", OPENSHIFT_CICD_NAMESPACE,
                        "job/{folder}/job/{stage}/api/json"))
                .then()
                .statusCode(HttpStatus.SC_OK);
    }

    @DataProvider(name = "namespace")
    public Object[][] namespace() {
        return new Object[][]{{openshiftSitNamespace}, {openshiftQaNamespace}};
    }

    @Test(dataProvider = "namespace")
    public void testOpenshiftStagesProjectsHasBeenCreated(String namespace) throws Exception {
        openShiftClient.get(ResourceKind.PROJECT, namespace, "");
    }

    @DataProvider(name = "pipeline")
    public Object[][] pipeline() {
        return new Object[][]{{CDPipelineName}};
    }

/*    @Test(dataProvider = "pipeline")
    public void testAdminConsoleCDPipelineHasBeenCreated(String pipeline) {
        given().pathParam("pipeline", pipeline)
                .auth()
                .oauth2(adminconsoleAccessToken)
                .when()
                .get(urlBuilder.buildUrl("https",
                        "edp-admin-console", OPENSHIFT_CICD_NAMESPACE,
                        "api/v1/edp/cd-pipeline/{pipeline}"))
                .then()
                .statusCode(HttpStatus.SC_OK);
    }*/
}
