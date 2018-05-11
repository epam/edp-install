def run(vars, postfix, action) {
        vars.get(vars.appSettingsKey).each() { application ->
            if (application.route && application.deployed) {
                switch (action) {
                    case "create":
                        if (application.route && application.deployed) {
                            routePrefix = (!application.route_site) ? application.name : application.route_site
                            routePostfix = (postfix == "-stable") ? "" : postfix
                            sh "oc export route -n ${vars.deployProject} ${application.name} | oc patch " +
                                    "--patch='{\"spec\":{\"host\":\"${routePrefix}-${vars.pipelineProject}${routePostfix}.${vars.wildcard}\"}}' " +
                                    "--local=true -f - -o yaml | oc patch --patch='{\"metadata\":{\"name\":\"${application.name}${postfix}\"}}' " +
                                    "--local=true -f - -o yaml | oc patch --patch='{\"metadata\":{\"namespace\":\"${vars.deployProject}\"}}' " +
                                    "--local=true -f - -o yaml | oc create -f -"
                        }
                        break
                    case "delete":
                        sh "oc -n ${vars.deployProject} delete route ${application.name}${postfix} --ignore-not-found=true"
                        break
                }
            }
        }
    this.result = "success"
}

return this;