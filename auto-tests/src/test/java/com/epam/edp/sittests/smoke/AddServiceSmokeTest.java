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

import org.apache.http.HttpStatus;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.openshift.internal.restclient.model.Secret;
import com.openshift.restclient.ClientBuilder;
import com.openshift.restclient.IClient;
import com.openshift.restclient.ResourceKind;
import io.qameta.allure.Feature;

import static com.epam.edp.sittests.smoke.StringConstants.GERRIT_SECRET;
import static com.epam.edp.sittests.smoke.StringConstants.GERRIT_USER;
import static com.epam.edp.sittests.smoke.StringConstants.OPENSHIFT_CICD_NAMESPACE;
import static com.epam.edp.sittests.smoke.StringConstants.OPENSHIFT_MASTER_URL;
import static com.epam.edp.sittests.smoke.StringConstants.OPENSHIFT_PASSWORD;
import static com.epam.edp.sittests.smoke.StringConstants.OPENSHIFT_USERNAME;
import static com.epam.edp.sittests.smoke.StringConstants.RABBITMQ_SERVICE_NAME;
import static io.restassured.RestAssured.given;

/**
 * @author Alexander Morozov
 */
public class AddServiceSmokeTest {
    private UrlBuilder urlBuilder;
    private String ocpEdpPrefix;

    private IClient openShiftClient;
    private String openshiftNamespace;

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
        this.ocpEdpPrefix = ocpEdpPrefix;
        this.openshiftNamespace = ocpEdpPrefix + "-" + OPENSHIFT_CICD_NAMESPACE;
    }

    @Test
    public void testServiceTemplateHasBeenAdded() {
        Secret secret = openShiftClient.get(ResourceKind.SECRET, GERRIT_SECRET, openshiftNamespace);
        String gerrit_password = new String(secret.getData("password")).trim();

        given().log().all()
                .pathParam("service", RABBITMQ_SERVICE_NAME)
                .pathParam("project", ocpEdpPrefix + "-edp")
                .urlEncodingEnabled(false)
                .auth()
                .basic(GERRIT_USER, gerrit_password)
                .when()
                .get(urlBuilder.buildUrl("https",
                        "gerrit", OPENSHIFT_CICD_NAMESPACE,
                        "a/projects/{project}/branches/master/files/deploy-templates%2F{service}.yaml/content"))
                .then()
                .statusCode(HttpStatus.SC_OK);
    }
}
