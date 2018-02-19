def getBody(bodyType) {
    def greeting = """<html>
            <body>
                <H4>Dear Colleague(s),</H4>
                <div align="left">
                    This is notificaton from Jenkins <a href="${JENKINS_URL}/job/${JOB_NAME}">build job</a><br>
                    Reason:<br>
                </div>
            """
    def textMessage
    switch (bodyType) {
        case "approve":
            textMessage = """
            <div align="left">
                Job is waiting for your approve, please check<br>
            </div>
            """
        case "failed":
            textMessage = """
            <div align="left">
                Job has been failed please check<br>
            </div>
            """
    }
    def footer = """
            <hr>
            </body>
            <footer> This message has been generated automatically by <a href="${JENKINS_URL}">EDP Jenkins CI</a>. Please do not reply on this message.
            </html>
            """
    return(greeting + textMessage + footer)
}


void sendEmail(recipients, subject, bodyType) {
    body = getBody(bodyType)
    emailext to: "${recipients}", subject: "${subject}", body: body, mimeType: "text/html"
}

void failJob(failMessage) {
    println(failMessage)
    currentBuild.displayName = "${currentBuild.displayName}-FAILED"
    currentBuild.result = 'FAILURE'
    error failMessage
}

return this;