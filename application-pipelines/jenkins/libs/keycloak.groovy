import groovy.json.*

void getKeycloakAccessToken() {
    def keycloakSecret = openshift.withProject() {
        openshift.selector('secret', 'keycloak-admin').object()
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

return this;