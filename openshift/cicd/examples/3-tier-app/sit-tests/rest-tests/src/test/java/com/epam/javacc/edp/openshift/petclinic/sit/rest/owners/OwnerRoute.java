package com.epam.javacc.edp.openshift.petclinic.sit.rest.owners;

import com.epam.javacc.edp.openshift.petclinic.sit.rest.Route;

public enum OwnerRoute implements Route {

    GET_OWNERS("/api/owner/list");

    private String route;

    OwnerRoute(String route) {
        this.route = route;
    }

    @Override
    public String getRoute() {
        return route;
    }
}