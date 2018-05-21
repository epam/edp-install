package com.epam.edp.sittests.smoke;

import org.apache.http.HttpStatus;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import static com.epam.edp.sittests.smoke.StringConstants.*;
import static io.restassured.RestAssured.given;

/**
 * @author Alexander Morozov
 */
public class AddServiceSmokeTest {
    private UrlBuilder urlBuilder;
    private String ocpEdpSuffix;

    @BeforeClass
    @Parameters("ocpEdpSuffix")
    public void setUp(String ocpEdpSuffix) {
        this.urlBuilder = new UrlBuilder(ocpEdpSuffix);
        this.ocpEdpSuffix = ocpEdpSuffix;
    }

    public void testServiceTemplateHasBeenAdded() {
        given().log().all()
                .pathParam("service", RABBITMQ_SERVICE_NAME)
                .pathParam("project", "edp-" + ocpEdpSuffix)
                .urlEncodingEnabled(false)
                .when()
                .get(urlBuilder.buildUrl("http",
                        "gerrit",OPENSHIFT_CICD_NAMESPACE,
                        "projects/{project}/branches/master/files/deploy-templates%2F{service}.yaml/content"))
                .then()
                .statusCode(HttpStatus.SC_OK);
    }
}
