import groovy.json.*

void getKeycloakAccessToken() {
    def keycloakSecret = openshift.withProject('security') {
        openshift.selector('secret', 'keycloak').object()
    }

    def username = new String(keycloakSecret.data.username.decodeBase64())
    def password = new String(keycloakSecret.data.password.decodeBase64())

    response = httpRequest url: 'http://keycloak.security:8080/auth/realms/master/protocol/openid-connect/token',
            httpMode: 'POST',
            contentType: 'APPLICATION_FORM',
            requestBody: "grant_type=password&username=${username}&password=${password}&client_id=admin-cli",
            consoleLogResponseBody: false

    vars['keycloakAccessToken'] = new groovy.json.JsonSlurper().parseText(response.content).access_token
}

def createKeycloakRealm(realm, accessToken) {
    try {
        tryCreateKeycloakRealm(realm, accessToken)
    } catch (Exception ex) {
        println("[JENKINS][ERROR] Exception - ${ex}")
    }
}

def tryCreateKeycloakRealm(realm, accessToken) {

    def requestBodyMap = [:]
    requestBodyMap['realm'] = realm
    requestBodyMap['defaultRoles'] = [ "developer" ]
    requestBodyMap['enabled'] = true
    requestBody = JsonOutput.toJson(requestBodyMap)

    httpRequest url: "http://keycloak.security:8080/auth/admin/realms",
            httpMode: 'POST',
            contentType: 'APPLICATION_JSON',
            requestBody: requestBody,
            customHeaders: [[name: 'Authorization', value: "Bearer ${accessToken}"]],
            consoleLogResponseBody: true
}

def deleteKeycloakRealm(realm, accessToken) {
    try {
        tryDeleteKeycloakRealm(realm, accessToken)
    } catch (Exception ex) {
        println("[JENKINS][ERROR] Exception - ${ex}")
    }
}

def tryDeleteKeycloakRealm(realm, accessToken) {
    httpRequest url: "http://keycloak.security:8080/auth/admin/realms/${realm}",
            httpMode: 'DELETE',
            customHeaders: [[name: 'Authorization', value: "Bearer ${accessToken}"]],
            consoleLogResponseBody: true
}

def mapKeycloakRoles(realm, accessToken) {
    response = httpRequest url: "http://keycloak.security:8080/auth/admin/realms/master/clients?clientId=${realm}-realm",
            httpMode: 'GET',
            customHeaders: [[name: 'Authorization', value: "Bearer ${accessToken}"]],
            consoleLogResponseBody: false
    clientId = new groovy.json.JsonSlurperClassic().parseText(response.content)[0].id

    response = httpRequest url: "http://keycloak.security:8080/auth/admin/realms/master/clients/${clientId}/roles/realm-admin",
            httpMode: 'GET',
            customHeaders: [[name: 'Authorization', value: "Bearer ${accessToken}"]],
            consoleLogResponseBody: false,
            validResponseCodes: '100:399,404'
    realmAdminRole = response.content

    if (response.status != 200) {
        getAllClientRoles(realm, clientId, accessToken)
        createRealmAdminCompositeRole(realm, clientId, accessToken)
        mapRealmAdminRole(realm, accessToken)
    }
    else {
        response = httpRequest url: "http://keycloak.security:8080/auth/admin/realms/${vars.projectPrefix}-edp/users",
                httpMode: 'GET',
                customHeaders: [[name: 'Authorization', value: "Bearer ${accessToken}"]],
                consoleLogResponseBody: false
        realmUsers = new groovy.json.JsonSlurperClassic().parseText(response.content)
        realmUsers.each() { user ->
            assignRealmAdminRoleToUser(clientId, realmAdminRole, user, accessToken)
        }
    }
}

def getAllClientRoles(realm, clientId, accessToken) {
    try {
        tryGetAllClientRoles(realm, clientId, accessToken)
    } catch (Exception ex) {
        println("[JENKINS][ERROR] Exception - ${ex}")
    }
}

def tryGetAllClientRoles(realm, clientId, accessToken) {
    response = httpRequest url: "http://keycloak.security:8080/auth/admin/realms/master/clients/${clientId}/roles",
            httpMode: 'GET',
            customHeaders: [[name: 'Authorization', value: "Bearer ${accessToken}"]],
            consoleLogResponseBody: false
    vars['clientRoles'] = response.content
}

def createRealmAdminCompositeRole(realm, clientId, accessToken) {
    try {
        tryCreateRealmAdminCompositeRole(realm, clientId, accessToken)
    } catch (Exception ex) {
        println("[JENKINS][ERROR] Exception - ${ex}")
    }
}

def tryCreateRealmAdminCompositeRole(realm, clientId, accessToken) {
    requestBodyMap = [:]
    requestBodyMap['name'] = "realm-admin"
    requestBodyMap['description'] = "\${role_realm-admin}"
    requestBody = JsonOutput.toJson(requestBodyMap)

    response = httpRequest url: "http://keycloak.security:8080/auth/admin/realms/master/clients/${clientId}/roles",
            httpMode: 'POST',
            contentType: 'APPLICATION_JSON',
            requestBody: requestBody,
            customHeaders: [[name: 'Authorization', value: "Bearer ${accessToken}"]],
            consoleLogResponseBody: false

    response = httpRequest url: "http://keycloak.security:8080/auth/admin/realms/master/clients/${clientId}/roles/realm-admin/composites",
            httpMode: 'POST',
            contentType: 'APPLICATION_JSON',
            requestBody: vars.clientRoles,
            customHeaders: [[name: 'Authorization', value: "Bearer ${accessToken}"]],
            consoleLogResponseBody: false
}

def mapRealmAdminRole(realm, accessToken) {
    try {
        tryMapRealmAdminRole(realm, accessToken)
    } catch (Exception ex) {
        println("[JENKINS][ERROR] Exception - ${ex}")
    }
}

def tryMapRealmAdminRole(realm, accessToken) {
    requestBodyMap = [:]
    requestBodyMap['name'] = "${realm}-realm-admin"
    requestBodyMap['identityProviderAlias'] = "${vars.projectPrefix}-edp"
    requestBodyMap['identityProviderMapper'] = "keycloak-oidc-role-to-role-idp-mapper"
    requestBodyMap['config'] = [:]
    requestBodyMap['config']['external.role'] = "administrator"
    requestBodyMap['config']['role'] = "${realm}-realm.realm-admin"
    requestBody = JsonOutput.toJson(requestBodyMap)

    httpRequest url: "http://keycloak.security:8080/auth/admin/realms/master/identity-provider/instances/${vars.projectPrefix}-edp/mappers",
            httpMode: 'POST',
            contentType: 'APPLICATION_JSON',
            requestBody: requestBody,
            customHeaders: [[name: 'Authorization', value: "Bearer ${accessToken}"]],
            consoleLogResponseBody: false
}

def assignRealmAdminRoleToUser(clientId, role, user, accessToken) {
    try {
        tryAssignRealmAdminRoleToUser(clientId, role, user, accessToken)
    } catch (Exception ex) {
        println("[JENKINS][ERROR] Exception - ${ex}")
    }
}

def tryAssignRealmAdminRoleToUser(clientId, role, user, accessToken) {
    response = httpRequest url: "http://keycloak.security:8080/auth/admin/realms/${vars.projectPrefix}-edp/users/${user.id}/role-mappings",
            httpMode: 'GET',
            customHeaders: [[name: 'Authorization', value: "Bearer ${accessToken}"]],
            consoleLogResponseBody: false

    if (response.content.contains("administrator")) {
        response = httpRequest url: "http://keycloak.security:8080/auth/admin/realms/master/users?username=${user.username}",
                httpMode: 'GET',
                customHeaders: [[name: 'Authorization', value: "Bearer ${accessToken}"]],
                consoleLogResponseBody: false
        foundUsers = new groovy.json.JsonSlurperClassic().parseText(response.content)
        foundUsers.each() { item ->
            if (item.username == user.username) {
                httpRequest url: "http://keycloak.security:8080/auth/admin/realms/master/users/${item.id}/role-mappings/clients/${clientId}",
                        httpMode: 'POST',
                        contentType: 'APPLICATION_JSON',
                        customHeaders: [[name: 'Authorization', value: "Bearer ${accessToken}"]],
                        requestBody: "[${role}]",
                        consoleLogResponseBody: false
            }
        }
    }
}

return this;