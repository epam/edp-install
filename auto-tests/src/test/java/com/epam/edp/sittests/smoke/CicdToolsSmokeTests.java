package com.epam.edp.sittests.smoke;

import org.apache.http.HttpStatus;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.useRelaxedHTTPSValidation;
import static io.restassured.RestAssured.when;

public class CicdToolsSmokeTests {
    private UrlBuilder urlBuilder;

    @BeforeClass
    @Parameters("ocpEdpSuffix")
    public void setUp(String ocpEdpSuffix) {
        this.urlBuilder = new UrlBuilder(ocpEdpSuffix);
    }

    @Test
    public void jenkinsSmokeTest() throws Exception {
        useRelaxedHTTPSValidation();
        when().
                get(urlBuilder.buildUrl("https", "jenkins", "edp-cicd", "login")).
                then().
                statusCode(HttpStatus.SC_OK);
    }

    @Test
    public void gerritSmokeTest() throws Exception {
        useRelaxedHTTPSValidation();
        when().
                get(urlBuilder.buildUrl("http", "gerrit", "edp-cicd", "")).
                then().
                statusCode(HttpStatus.SC_OK);
    }

    @Test
    public void nexusSmokeTest() throws Exception {
        useRelaxedHTTPSValidation();
        when().
                get(urlBuilder.buildUrl("http", "nexus", "edp-cicd", "")).
                then().
                statusCode(HttpStatus.SC_OK);
    }

    @Test
    public void sonarSmokeTest() throws Exception {
        useRelaxedHTTPSValidation();
        when().
                get(urlBuilder.buildUrl("http", "sonar", "edp-cicd", "")).
                then().
                statusCode(HttpStatus.SC_OK);
    }

    @Test
    public void keycloakSmokeTest() throws Exception {
        useRelaxedHTTPSValidation();
        when().
                get(urlBuilder.buildUrl("http", "keycloak", "edp-cockpit", "")).
                then().
                statusCode(HttpStatus.SC_OK);
    }

}
