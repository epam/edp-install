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
