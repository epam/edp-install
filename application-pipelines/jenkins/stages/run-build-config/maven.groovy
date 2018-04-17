def run(vars) {
    openshift.withCluster() {
        openshift.withProject() {
            if (!openshift.selector("buildconfig", "${vars.applicationMap.name}").exists())
                openshift.newBuild("--name=${vars.applicationMap.name}", "--image-stream=s2i-${vars.applicationMap.language.toLowerCase()}", "--binary=true")
            openshift.selector("bc", "${vars.applicationMap.name}").startBuild("--from-dir=${vars.workDir}/target", "--wait=true")
            if (vars.promoteImage) {
                openshift.tag("${openshift.project()}/${vars.applicationMap.name}:latest", "${vars.targetProject}/${vars.applicationMap.name}:latest")
                sh "oc -n ${vars.targetProject} policy add-role-to-group registry-viewer system:unauthenticated"
            }
            else
                println("[JENKINS][WARNING] Image wasn't promoted since there are no environments were added\r\n" +
                        "[JENKINS][WARNING] If your like to promote your images please add environment via your cockpit panel")
        }
    }
}

return this;