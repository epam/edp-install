package com.epam.edp.sittests.smoke;

import org.apache.http.HttpStatus;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import static com.epam.edp.sittests.smoke.StringConstants.*;
import static io.restassured.RestAssured.given;

/**
 * @author Pavlo_Yemelianov
 */
public class AddAutotestSmokeTest {
    private static final String AUTOTESTS_NAME = "helloworld-autotests";
    private static final String PRECOMMIT_AUTOTESTS_PIPELINE = PRECOMMIT_PIPELINE_SUFFIX + AUTOTESTS_NAME;

    private UrlBuilder urlBuilder;

    @BeforeClass
    @Parameters("ocpEdpSuffix")
    public void setUp(String ocpEdpSuffix) {
        this.urlBuilder = new UrlBuilder(ocpEdpSuffix);
    }

    @Test
    public void testGerritProjectWasCreated() {
        given()
            .pathParam("project", AUTOTESTS_NAME)
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
    public void testJenkinsPipelineWasCreated() {
        given()
            .pathParam("pipeline", PRECOMMIT_AUTOTESTS_PIPELINE)
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
