def run(vars, postfix="-stable") {
    openshift.withCluster() {
        vars.get(vars.appSettingsKey).each() { application ->
            if(application.route) {
                routePrefix = (!application.route_site) ? application.name : application.route_site
                routePostfix = (postfix == "-stable") ? "" : postfix
                sh "oc export route -n ${vars.deployProject} ${application.name} | oc patch " +
                        "--patch='{\"spec\":{\"host\":\"${routePrefix}-${vars.pipelineProject}${routePostfix}.${vars.wildcard}\"}}' " +
                        "--local=true -f - -o yaml | oc patch --patch='{\"metadata\":{\"name\":\"${application.name}${postfix}\"}}' " +
                        "--local=true -f - -o yaml | oc patch --patch='{\"metadata\":{\"namespace\":\"${vars.deployProject}\"}}' " +
                        "--local=true -f - -o yaml | oc create -f -"
            }
        }
    }
    this.result = "success"
}

return this;