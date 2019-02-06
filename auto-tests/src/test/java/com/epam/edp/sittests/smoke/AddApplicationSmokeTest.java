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
import static com.epam.edp.sittests.smoke.StringConstants.GERRIT_SECRET;
import static com.epam.edp.sittests.smoke.StringConstants.GERRIT_USER;
import static com.epam.edp.sittests.smoke.StringConstants.OPENSHIFT_CICD_NAMESPACE;
import static com.epam.edp.sittests.smoke.StringConstants.OPENSHIFT_MASTER_URL;
import static com.epam.edp.sittests.smoke.StringConstants.OPENSHIFT_PASSWORD;
import static com.epam.edp.sittests.smoke.StringConstants.OPENSHIFT_USERNAME;
import static com.epam.edp.sittests.smoke.StringConstants.BUILD_PIPELINE_SUFFIX;
import static com.epam.edp.sittests.smoke.StringConstants.CODEREVIEW_PIPELINE_SUFFIX;
import static io.restassured.RestAssured.given;

/**
 * @author Pavlo_Yemelianov
 */
public class AddApplicationSmokeTest {
    private static final String CREATED_JAVA_GRADLE_APP_SUFFIX = "created-java-gradle-project";
    private static final String CLONED_JAVA_MAVEN_APP_SUFFIX = "cloned-java-maven-project";
    private static final String CLONED_JAVASCRIPT_NPM_APP_SUFFIX = "cloned-javascript-npm-project";

    private UrlBuilder urlBuilder;
    private IClient openShiftClient;
    private String openshiftNamespace;
    private String clonedJavaMavenAppName;
    private String clonedJavascriptNpmAppName;
    private String createdJavaGradleAppName;
    private String codeReviewCreatedJavaGradleAppName;
    private String buildCreatedJavaGradleAppName;
    private String codeReviewClonedJavaMavenAppName;
    private String buildClonedJavaMavenAppName;
    private String codeReviewClonedJavascriptNpmAppAppName;
    private String buildClonedJavascriptNpmAppAppName;

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
        this.codeReviewCreatedJavaGradleAppName = CODEREVIEW_PIPELINE_SUFFIX + createdJavaGradleAppName;
        this.buildCreatedJavaGradleAppName = BUILD_PIPELINE_SUFFIX + createdJavaGradleAppName;
        this.clonedJavaMavenAppName = ocpEdpPrefix + "-" + CLONED_JAVA_MAVEN_APP_SUFFIX;
        this.codeReviewClonedJavaMavenAppName = CODEREVIEW_PIPELINE_SUFFIX + clonedJavaMavenAppName;
        this.buildClonedJavaMavenAppName = BUILD_PIPELINE_SUFFIX + clonedJavaMavenAppName;
        this.clonedJavascriptNpmAppName = ocpEdpPrefix + "-" + CLONED_JAVASCRIPT_NPM_APP_SUFFIX;
        this.codeReviewClonedJavascriptNpmAppAppName = CODEREVIEW_PIPELINE_SUFFIX + clonedJavascriptNpmAppName;
        this.buildClonedJavascriptNpmAppAppName = BUILD_PIPELINE_SUFFIX + clonedJavascriptNpmAppName;
    }

    @DataProvider(name = "pipeline")
    public Object[][] pipeline() {
        return new Object[][]{{codeReviewCreatedJavaGradleAppName}, {buildCreatedJavaGradleAppName}, {codeReviewClonedJavaMavenAppName},
                {buildClonedJavaMavenAppName}, {codeReviewClonedJavascriptNpmAppAppName}, {buildClonedJavascriptNpmAppAppName}};
    }

    @DataProvider(name = "application")
    public Object[][] application() {
        return new Object[][]{{createdJavaGradleAppName}, {clonedJavaMavenAppName}, {clonedJavascriptNpmAppName}};
    }

    @Test(dataProvider = "application")
    public void testGerritProjectWasCreated(String application) {
        Secret secret = openShiftClient.get(ResourceKind.SECRET, GERRIT_SECRET, openshiftNamespace);
        String gerrit_password = new String(secret.getData("password")).trim();

        given().log().all()
                .pathParam("project", application)
                .auth()
                .basic(GERRIT_USER, gerrit_password)
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
        Secret secret = openShiftClient.get(ResourceKind.SECRET, GERRIT_SECRET, openshiftNamespace);
        String gerrit_password = new String(secret.getData("password")).trim();

        given().log().all()
                .pathParam("application", application)
                .urlEncodingEnabled(false)
                .auth()
                .basic(GERRIT_USER, gerrit_password)
                .when()
                .get(urlBuilder.buildUrl("https",
                        "gerrit", OPENSHIFT_CICD_NAMESPACE,
                        "a/projects/{application}/branches/master/files/deploy-templates%2F{application}.yaml/content"))
                .then()
                .statusCode(HttpStatus.SC_OK);
    }
}
