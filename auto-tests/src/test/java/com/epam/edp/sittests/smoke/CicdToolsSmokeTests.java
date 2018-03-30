package com.epam.edp.sittests.smoke;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.useRelaxedHTTPSValidation;
import static io.restassured.RestAssured.when;

public class CicdToolsSmokeTests {

    private String ocpEdpSuffix;

    @BeforeClass
    @Parameters("ocpEdpSuffix")
    public void setUp(String ocpEdpSuffix){
        this.ocpEdpSuffix = ocpEdpSuffix;
    }

    @Test
    public void jenkinsSmokeTest() throws Exception {
        useRelaxedHTTPSValidation();
        when().
                get(getUrl("https", "jenkins", "login")).
                then().
                statusCode(HttpStatus.SC_OK);
    }

    @Test
    public void gerritSmokeTest() throws Exception {
        useRelaxedHTTPSValidation();
        when().
                get(getUrl("http", "gerrit", "")).
                then().
                statusCode(HttpStatus.SC_OK);
    }

    @Test
    public void nexusSmokeTest() throws Exception {
        useRelaxedHTTPSValidation();
        when().
                get(getUrl("http", "nexus", "")).
                then().
                statusCode(HttpStatus.SC_OK);
    }

    @Test
    public void sonarSmokeTest() throws Exception {
        useRelaxedHTTPSValidation();
        when().
                get(getUrl("http", "sonar", "")).
                then().
                statusCode(HttpStatus.SC_OK);
    }

    @Test
    public void keycloakSmokeTest() throws Exception {
        useRelaxedHTTPSValidation();
        when().
                get(getUrl("http", "keycloak", "")).
                then().
                statusCode(HttpStatus.SC_OK);
    }


    private String getUrl(String protocol, String service, String path) {
       return new StringBuilder(protocol)
               .append("://")
               .append(service)
               .append("-edp-cicd")
               .append(StringUtils.isNoneEmpty(ocpEdpSuffix) ? "-" : "")
               .append(ocpEdpSuffix)
               .append(".main.edp.projects.epam.com")
               .append(StringUtils.isNoneEmpty(path) ? "/" : "")
               .append(path)
               .toString();
    }
}
