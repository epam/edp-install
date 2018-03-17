/**
 * Checkout from gerrit.  Parameters are defined by gerrit trigger
 * @param vars object with all pipeline variables
 */
def run(vars) {
    dir("${vars.workDir}") {
        vars.ocProjectNameSuffixes.each { projectSuffix ->
            ['edp-cicd-', 'edp-cockpit-', 'edp-sit-', 'edp-qa-', 'edp-uat-'].each { projectName ->
                try {
                    openshiftDeleteResourceByKey apiURL: '', authToken: '', keys: "${projectName}${projectSuffix}", namespace: '', types: 'project', verbose: 'false'
                    sleep(10)
                    sh "for project in \$(oc get project | grep .*${projectSuffix} | grep Terminating | awk '{print \$1}'); \
                        do oc -n \$project get pods | grep -v NAME | awk '{print \$1}' | xargs oc -n \$project delete pod --force --grace-period=0; \
                        done"
                } catch (Exception ex) {
                    println("[JENKINS][ERROR] Exception - ${ex}")
                }
            }
        }
        try {
            withCredentials([usernamePassword(credentialsId: 'Auto_EPMC-JAVA_VCS', passwordVariable: 'password', usernameVariable: 'username')]) {
                   token = getAuthToken(username, password)
            }
            project_id = getProjectID(token, "edp-" + vars.ocProjectNameSuffix)
            deleteProject(project_id, token)
        } catch (Exception ex) {
            println("[JENKINS][ERROR] Exception - ${ex}")
        }
    }
    this.result = "success"
}

def getAuthToken(user, pass) {
    def url = new URL("https://git.epam.com/oauth/token")
    def conn = url.openConnection()
    conn.setRequestMethod("POST")
    conn.setRequestProperty("Content-Type", "application/json")
    conn.doOutput = true

    def authString = "{\"grant_type\": \"password\", \"username\": \"${user}\", \"password\": \"${pass}\"}"

    def writer = new OutputStreamWriter(conn.outputStream)
    writer.write(authString)
    writer.flush()
    writer.close()
    conn.connect()

    def result = parseJSON(conn.content.text)
    return result.access_token
}

def getProjectID(authToken, project_name) {

    def url = new URL(vars.gitLabApiUrl + "/projects/epmd-edp%2Ftemp%2F${project_name}")
    def result = parseJSON(url.getText(requestProperties:["Authorization":"Bearer ${authToken}"]))
    return result.id
}

def deleteProject(projectID, token) {
    def url = new URL(vars.gitLabApiUrl + "/projects/${projectID}?access_token=${token}")
    def conn = url.openConnection()
    conn.setRequestMethod("DELETE")
    conn.setRequestProperty("Content-Type", "application/json")
    conn.doOutput = true

    def writer = new OutputStreamWriter(conn.outputStream)
    writer.flush()
    writer.close()
    conn.connect()

    def result = parseJSON(conn.content.text)
}

def parseJSON(json) {
    return new groovy.json.JsonSlurperClassic().parseText(json)
}

return this;
