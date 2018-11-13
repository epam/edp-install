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

import io.restassured.config.RedirectConfig;
import io.restassured.config.RestAssuredConfig;
import org.apache.http.HttpStatus;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.useRelaxedHTTPSValidation;

public class CicdToolsSmokeTests {
    private UrlBuilder urlBuilder;

    @BeforeClass
    @Parameters("ocpEdpPrefix")
    public void setUp(String ocpEdpPrefix) {
        this.urlBuilder = new UrlBuilder(ocpEdpPrefix);
    }

    @Test
    public void jenkinsSmokeTest() {
        useRelaxedHTTPSValidation();
        given().log().all()
                .when().get(urlBuilder.buildUrl("https", "jenkins", "edp-cicd", "login"))
                .then()
                .statusCode(HttpStatus.SC_OK);
    }

    @Test
    public void gerritSmokeTest() {
        useRelaxedHTTPSValidation();
        given().log().all()
                .when().get(urlBuilder.buildUrl("https", "gerrit", "edp-cicd", ""))
                .then()
                .statusCode(HttpStatus.SC_OK);
    }

    @Test
    public void nexusSmokeTest() {
        useRelaxedHTTPSValidation();
        given().config(RestAssuredConfig.config().redirect(RedirectConfig.redirectConfig().followRedirects(false)))
                .when().get(urlBuilder.buildUrl("https", "nexus", "edp-cicd", ""))
                .then()
                .statusCode(HttpStatus.SC_TEMPORARY_REDIRECT);
    }

    @Test
    public void sonarSmokeTest() {
        useRelaxedHTTPSValidation();
        given().log().all()
                .when().get(urlBuilder.buildUrl("https", "sonar", "edp-cicd", ""))
                .then()
                .statusCode(HttpStatus.SC_OK);
    }

}
