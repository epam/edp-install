package com.epam.edp.sittests.smoke;

import io.fabric8.openshift.api.model.Template;
import io.fabric8.openshift.client.OpenShiftClient;
import io.qameta.allure.Feature;
import org.apache.http.HttpStatus;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.ConfigBuilder;
import io.fabric8.openshift.client.DefaultOpenShiftClient;

import static com.epam.edp.sittests.smoke.StringConstants.*;
import static io.restassured.RestAssured.given;

public class AddEnvironmentSmokeTest {
    private static final String OPENSHIFT_CICD_NAMESPACE = "edp-cicd";
    private static final String OPENSHIFT_MASTER_URL = "https://openshift.main.edp.projects.epam.com:8443";

    private static final String OPENSHIFT_USERNAME = "integration_tests";
    private static final String OPENSHIFT_PASSWORD = "tests2018";
    private static final Boolean OPENSHIFT_TRUST_CERTS = false;
    private static final String DELETION_PIPELINE_NAME = "deletion-pipeline";
    private static final String BE_TEMPLATE_NAME = "springboot-" + BE_APP_NAME;
    private static final String FE_TEMPLATE_NAME = "react-" + FE_APP_NAME;

    private static String sitPipelineName;
    private static String qaPipelineName;
    private static String openshiftNamespace;
    private static String ocpEdpSuffix;
    private UrlBuilder urlBuilder;
    private OpenShiftClient openShiftClient;

    @Feature("Setup URL Builder")
    @BeforeClass
    @Parameters("ocpEdpSuffix")
    public void setUp(String ocpEdpSuffix) {
        this.urlBuilder = new UrlBuilder(ocpEdpSuffix);
        this.ocpEdpSuffix = ocpEdpSuffix;
        this.sitPipelineName = "sit-" + ocpEdpSuffix + "-deploy-pipeline";
        this.qaPipelineName = "qa-" + ocpEdpSuffix + "-deploy-pipeline";
        this.openshiftNamespace = OPENSHIFT_CICD_NAMESPACE + "-" + ocpEdpSuffix;
    }

    @DataProvider(name = "pipeline")
    public static Object[][] pipeline() {
        return new Object[][] { {sitPipelineName}, {qaPipelineName}, {DELETION_PIPELINE_NAME}};
    }

    @DataProvider(name = "application")
    public static Object[][] application() {
        return new Object[][] { {BE_TEMPLATE_NAME}, {FE_TEMPLATE_NAME} };
    }

    @Feature("Setup Openshift Client")
    @BeforeClass
    public void setUpOpenShiftClient() {
        Config config = new ConfigBuilder().withMasterUrl(OPENSHIFT_MASTER_URL)
                .withUsername(OPENSHIFT_USERNAME)
                .withPassword(OPENSHIFT_PASSWORD)
                .withTrustCerts(OPENSHIFT_TRUST_CERTS)
                .build();

        this.openShiftClient = new DefaultOpenShiftClient(config);
    }

    @Test(dataProvider = "pipeline")
    public void testJenkinsTestPipelinesHasBeenCreated(String pipeline) {
        given().log().all()
            .pathParam("pipeline", pipeline)
            .pathParam("folder", openshiftNamespace)
            .auth()
            .preemptive()
            .basic(JENKINS_USER, JENKINS_PASSWORD)
        .when()
            .get(urlBuilder.buildUrl("https",
                    "jenkins",OPENSHIFT_CICD_NAMESPACE,
                    "job/{folder}/job/{folder}-{pipeline}/api/json"))
        .then()
            .statusCode(HttpStatus.SC_OK);
    }

    @Test(dataProvider = "application")
    public void testApplicationTemplateHasBeenAdded(String application) {
        given().log().all()
                .pathParam("application", application)
                .pathParam("project", "edp-" + ocpEdpSuffix)
                .urlEncodingEnabled(false)
        .when()
                .get(urlBuilder.buildUrl("http",
                        "gerrit",OPENSHIFT_CICD_NAMESPACE,
                        "projects/{project}/branches/master/files/deploy-templates%2F{application}.yml/content"))
        .then()
                .statusCode(HttpStatus.SC_OK);
    }
}
