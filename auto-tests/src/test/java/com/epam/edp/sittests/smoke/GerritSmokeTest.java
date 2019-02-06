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
import org.testng.annotations.DataProvider;
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

import static io.restassured.RestAssured.given;

/**
 * @author Sergiy_Kulanov
 */
public class GerritSmokeTest {
    private static final String GERRIT_USER = "admin";
    private static final String GERRIT_SECRET = "gerrit-passwords";
    private static final String GERRIT_ADMIN = "admin";
    private static final String GERRIT_PROJECT_CREATOR = "project-creator";
    private static final String GERRIT_JENKINS = "jenkins";
    private static final String GERRIT_GROUP_CI = "Continuous Integration Tools";
    private static final String GERRIT_GROUP_BOOTSTRAP = "Project Bootstrappers";

    private UrlBuilder urlBuilder;
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
        this.openshiftNamespace = ocpEdpPrefix + "-" + OPENSHIFT_CICD_NAMESPACE;
    }

    @DataProvider(name = "userlist")
    public static Object[][] userlist() {
        return new Object[][]{{GERRIT_ADMIN}, {GERRIT_PROJECT_CREATOR}, {GERRIT_JENKINS}};
    }

    @Test(dataProvider = "userlist")
    public void testGerritUserExists(String userlist) {
        Secret secret = openShiftClient.get(ResourceKind.SECRET, GERRIT_SECRET, openshiftNamespace);
        String gerrit_password = new String(secret.getData("password")).trim();

        given().log().all()
                .pathParam("user", userlist)
                .auth()
                .basic(GERRIT_USER, gerrit_password)
                .when()
                .get(urlBuilder.buildUrl("https",
                        "gerrit",
                        "edp-cicd",
                        "a/accounts/{user}/name"))
                .then()
                .statusCode(HttpStatus.SC_OK);
    }

    @DataProvider(name = "grouplist")
    public static Object[][] grouplist() {
        return new Object[][]{{GERRIT_GROUP_CI}, {GERRIT_GROUP_BOOTSTRAP}};
    }

    @Test(dataProvider = "grouplist")
    public void testGerritGroupsExists(String grouplist) {
        Secret secret = openShiftClient.get(ResourceKind.SECRET, GERRIT_SECRET, openshiftNamespace);
        String gerrit_password = new String(secret.getData("password")).trim();

        given().log().all()
                .pathParam("group", grouplist)
                .auth()
                .basic(GERRIT_USER, gerrit_password)
                .when()
                .get(urlBuilder.buildUrl("https",
                        "gerrit",
                        "edp-cicd",
                        "a/groups/{group}"))
                .then()
                .statusCode(HttpStatus.SC_OK);
    }

}
