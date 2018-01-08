package com.epam.javacc.edp.openshift.petclinic.sit.rest.owners;

import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class OwnerResourceTests {

    @Test
    public void shouldReturnOwnersByLastName() {
        given()
            .param("lastName", "Es")
        .when()
            .get(OwnerRoute.GET_OWNERS.getUri())
        .then()
            .statusCode(200);
    }
}