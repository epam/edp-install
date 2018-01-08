package com.epam.javacc.edp.openshift.petclinic.sit.rest;

import static org.apache.commons.lang3.StringUtils.join;

public interface Route {

    String SERVICE_HOST = System.getProperty("host", "http://petclinic-backend.10.17.132.69.xip.io");

    default String getUri() {
        return join(getServiceHost(), getRoute());
    }

    default String getServiceHost() {
        return SERVICE_HOST;
    }

    String getRoute();
}