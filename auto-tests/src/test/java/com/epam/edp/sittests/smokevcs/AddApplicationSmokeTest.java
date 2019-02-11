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

package com.epam.edp.sittests.smokevcs;

import com.epam.edp.sittests.smoke.UrlBuilder;
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

import static com.epam.edp.sittests.smoke.StringConstants.*;
import static io.restassured.RestAssured.given;

/**
 * @author Pavlo_Yemelianov
 */
public class AddApplicationSmokeTest {

    private static final String CREATED_JAVA_GRADLE_APP_SUFFIX = "created-java-gradle-project";
    private static final String CLONED_JAVA_MAVEN_APP_SUFFIX = "cloned-java-maven-project";
    private static final String CLONED_JAVASCRIPT_NPM_APP_SUFFIX = "cloned-javascript-npm-project";

    private IClient openShiftClient;
    private UrlBuilder urlBuilder;
    private String openshiftNamespace;
    private String clonedJavaMavenAppName;
    private String clonedJavascriptNpmAppName;
    private String createdJavaGradleAppName;

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
        this.createdJavaGradleAppName = ocpEdpPrefix + "-" + CREATED_JAVA_GRADLE_APP_SUFFIX;
        this.clonedJavaMavenAppName = ocpEdpPrefix + "-" + CLONED_JAVA_MAVEN_APP_SUFFIX;
        this.clonedJavascriptNpmAppName = ocpEdpPrefix + "-" + CLONED_JAVASCRIPT_NPM_APP_SUFFIX;
    }

    @DataProvider(name = "application")
    public Object[][] application() {
        return new Object[][]{{createdJavaGradleAppName}, {clonedJavaMavenAppName}, {clonedJavascriptNpmAppName}};
    }

    @Test(dataProvider = "application")
    public void testProjectWasCreatedInGitGroupRepoForCreateStrategy(String application) {
        Secret secret = openShiftClient.get(ResourceKind.SECRET, "vcs-autouser-for-tests", "edp-cicd-delivery");

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
                .pathParam("project", application)
                .auth()
                .preemptive()
                .oauth2(token)
                .urlEncodingEnabled(false)
                .when()
                .get("https://git.epam.com/api/v4/projects/epmd-edp%2Ftemp%2F{project}")
                .then()
                .statusCode(HttpStatus.SC_OK);
    }
}
