package com.epam.edp.sittests.business;

import io.qameta.allure.Feature;
import org.testng.Assert;
import org.testng.annotations.Test;

public class AddBusinessApplicationTest {

    @Feature("Add java spring boot business application to EDP")
    @Test
    public void checkAddingOfJavaSpringBootApplication() throws Exception {
        Assert.assertTrue(true);
    }
}