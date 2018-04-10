package com.epam.edp.sittests.smoke;

import org.apache.http.HttpStatus;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

/**
 * @author Pavlo_Yemelianov
 */
public class AddApplicationSmokeTest {
    private static final String TEST_APP_NAME = "springboot-helloworld";
    private static final String GERRIT_USER = "admin";
    private static final String JENKINS_USER = "admin";
    private static final String GERRIT_PASSWORD = "secret";
    private static final String JENKINS_PASSWORD = "password";
    private static final String PRECOMMIT_PIPELINE_NAME = "gerrit-precommit-" + TEST_APP_NAME;
    private static final String POSTCOMMIT_PIPELINE_NAME = "gerrit-postcommit-" + TEST_APP_NAME;

    private UrlBuilder urlBuilder;

    @BeforeClass
    @Parameters("ocpEdpSuffix")
    public void setUp(String ocpEdpSuffix) {
        this.urlBuilder = new UrlBuilder(ocpEdpSuffix);
    }

    @Test
    public void testGerritProjectWasCreated() {
        given()
            .pathParam("project", TEST_APP_NAME)
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

    @Test
    public void testJenkinsPreCommitPipelineWasCreated() {
        given()
            .pathParam("pipeline", PRECOMMIT_PIPELINE_NAME)
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
    public void testJenkinsPostCommitPipelineWasCreated() {
        given()
            .pathParam("pipeline", POSTCOMMIT_PIPELINE_NAME)
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
