package com.epam.edp.sittests.smoke;

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
    private static final String PRECOMMIT_BE_PIPELINE = PRECOMMIT_PIPELINE_SUFFIX + BE_APP_NAME;
    private static final String PRECOMMIT_FE_PIPELINE = PRECOMMIT_PIPELINE_SUFFIX + FE_APP_NAME;

    private static final String POSTCOMMIT_BE_PIPELINE = POSTCOMMIT_PIPELINE_SUFFIX + BE_APP_NAME;
    private static final String POSTCOMMIT_FE_PIPELINE = POSTCOMMIT_PIPELINE_SUFFIX + FE_APP_NAME;

    private UrlBuilder urlBuilder;

    @BeforeClass
    @Parameters("ocpEdpSuffix")
    public void setUp(String ocpEdpSuffix) {
        this.urlBuilder = new UrlBuilder(ocpEdpSuffix);
    }

    @DataProvider(name = "pipeline")
    public static Object[][] pipeline() {
        return new Object[][] { {PRECOMMIT_BE_PIPELINE}, {PRECOMMIT_FE_PIPELINE}, {POSTCOMMIT_BE_PIPELINE},
                {POSTCOMMIT_FE_PIPELINE} };
    }

    @DataProvider(name = "application")
    public static Object[][] application() {
        return new Object[][] { {BE_APP_NAME}, {FE_APP_NAME} };
    }

    @Test(dataProvider = "application")
    public void testGerritProjectWasCreated(String application) {
        given()
            .pathParam("project", application)
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

    @Test(dataProvider = "pipeline")
    public void testJenkinsPipelineWasCreated(String pipeline) {
        given()
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
}
