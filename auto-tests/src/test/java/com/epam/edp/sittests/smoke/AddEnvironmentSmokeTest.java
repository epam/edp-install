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
import org.apache.http.HttpStatus;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import static com.epam.edp.sittests.smoke.StringConstants.DELETION_PIPELINE_NAME;
import static com.epam.edp.sittests.smoke.StringConstants.OPENSHIFT_CICD_NAMESPACE;
import static com.epam.edp.sittests.smoke.StringConstants.OPENSHIFT_MASTER_URL;
import static com.epam.edp.sittests.smoke.StringConstants.OPENSHIFT_PASSWORD;
import static com.epam.edp.sittests.smoke.StringConstants.OPENSHIFT_USERNAME;
import static io.restassured.RestAssured.given;

public class AddEnvironmentSmokeTest {
    private String sitPipelineName;
    private String qaPipelineName;
    private String openshiftNamespace;
    private UrlBuilder urlBuilder;
    private IClient openShiftClient;

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

    @Feature("Setup URL Builder")
    @BeforeClass
    @Parameters("ocpEdpPrefix")
    public void setUp(String ocpEdpPrefix) {
        this.urlBuilder = new UrlBuilder(ocpEdpPrefix);
        this.sitPipelineName = ocpEdpPrefix + "-sit-deploy-pipeline";
        this.qaPipelineName = ocpEdpPrefix + "-qa-deploy-pipeline";
        this.openshiftNamespace = ocpEdpPrefix + "-" + OPENSHIFT_CICD_NAMESPACE;
    }

    @DataProvider(name = "pipeline")
    public Object[][] pipeline() {
        return new Object[][]{{sitPipelineName}, {qaPipelineName}, {DELETION_PIPELINE_NAME}};
    }

    @Test(dataProvider = "pipeline")
    public void testJenkinsTestPipelinesHasBeenCreated(String pipeline) {
        Secret secret = openShiftClient.get(ResourceKind.SECRET, "jenkins-token", openshiftNamespace);

        String username = new String(secret.getData("username")).trim();
        String token = new String(secret.getData("token")).trim();

        given().log().all()
                .pathParam("pipeline", pipeline)
                .pathParam("folder", openshiftNamespace)
                .auth()
                .preemptive()
                .basic(username, token)
                .when()
                .get(urlBuilder.buildUrl("https",
                        "jenkins", OPENSHIFT_CICD_NAMESPACE,
                        "job/{folder}/job/{folder}-{pipeline}/api/json"))
                .then()
                .statusCode(HttpStatus.SC_OK);
    }
}
