package com.epam.edp.sittests.smoke;

import org.apache.http.HttpStatus;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

/**
 * @author Sergiy_Kulanov
 */
public class GerritSmokeTest {
    private static final String GERRIT_USER = "admin";
    private static final String GERRIT_PASSWORD = "secret";
    private static final String GERRIT_ADMIN = "admin";
    private static final String GERRIT_PROJECT_CREATOR = "project-creator";
    private static final String GERRIT_JENKINS = "jenkins";
    private static final String GERRIT_GROUP_CI = "Continuous Integration Tools";
    private static final String GERRIT_GROUP_BOOTSTRAP = "Project Bootstrappers";

    private UrlBuilder urlBuilder;

    @BeforeClass
    @Parameters("ocpEdpSuffix")
    public void setUp(String ocpEdpSuffix) {
        this.urlBuilder = new UrlBuilder(ocpEdpSuffix);
    }

    @DataProvider(name = "userlist")
    public static Object[][] userlist() {
        return new Object[][] { {GERRIT_ADMIN}, {GERRIT_PROJECT_CREATOR}, {GERRIT_JENKINS} };
    }

    @Test(dataProvider = "userlist")
    public void testGerritUserExists(String userlist){
        given().log().all()
            .pathParam("user", userlist)
            .auth()
            .basic(GERRIT_USER, GERRIT_PASSWORD)
        .when()
            .get(urlBuilder.buildUrl("http",
                    "gerrit",
                    "edp-cicd",
                    "a/accounts/{user}/name"))
        .then()
            .statusCode(HttpStatus.SC_OK);
    }

    @DataProvider(name = "grouplist")
    public static Object[][] grouplist() {
        return new Object[][] { {GERRIT_GROUP_CI}, {GERRIT_GROUP_BOOTSTRAP} };
    }

    @Test(dataProvider = "grouplist")
    public void testGerritGroupsExists(String grouplist){
        given().log().all()
            .pathParam("group", grouplist)
            .auth()
            .basic(GERRIT_USER, GERRIT_PASSWORD)
        .when()
            .get(urlBuilder.buildUrl("http",
                    "gerrit",
                    "edp-cicd",
                    "a/groups/{group}"))
        .then()
            .statusCode(HttpStatus.SC_OK);
    }

}
