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

import io.fabric8.openshift.api.model.Template;
import io.fabric8.openshift.client.OpenShiftClient;
import io.qameta.allure.Feature;
import org.apache.http.HttpStatus;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.ConfigBuilder;
import io.fabric8.openshift.client.DefaultOpenShiftClient;

import static com.epam.edp.sittests.smoke.StringConstants.*;
import static io.restassured.RestAssured.given;

public class AddEnvironmentSmokeTest {
    private static String sitPipelineName;
    private static String qaPipelineName;
    private static String openshiftNamespace;
    private static String ocpEdpSuffix;
    private UrlBuilder urlBuilder;
    private OpenShiftClient openShiftClient;

    @Feature("Setup URL Builder")
    @BeforeClass
    @Parameters("ocpEdpSuffix")
    public void setUp(String ocpEdpSuffix) {
        this.urlBuilder = new UrlBuilder(ocpEdpSuffix);
        this.ocpEdpSuffix = ocpEdpSuffix;
        this.sitPipelineName = "sit-" + ocpEdpSuffix + "-deploy-pipeline";
        this.qaPipelineName = "qa-" + ocpEdpSuffix + "-deploy-pipeline";
        this.openshiftNamespace = OPENSHIFT_CICD_NAMESPACE + "-" + ocpEdpSuffix;
    }

    @DataProvider(name = "pipeline")
    public static Object[][] pipeline() {
        return new Object[][] { {sitPipelineName}, {qaPipelineName}, {DELETION_PIPELINE_NAME}};
    }

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

    @Test(dataProvider = "pipeline")
    public void testJenkinsTestPipelinesHasBeenCreated(String pipeline) {
        given().log().all()
            .pathParam("pipeline", pipeline)
            .pathParam("folder", openshiftNamespace)
            .auth()
            .preemptive()
            .basic(JENKINS_USER, JENKINS_PASSWORD)
        .when()
            .get(urlBuilder.buildUrl("https",
                    "jenkins",OPENSHIFT_CICD_NAMESPACE,
                    "job/{folder}/job/{folder}-{pipeline}/api/json"))
        .then()
            .statusCode(HttpStatus.SC_OK);
    }
}
