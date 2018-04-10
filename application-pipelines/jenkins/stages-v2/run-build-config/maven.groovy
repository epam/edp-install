def run(vars) {
    openshift.withCluster() {
        openshift.withProject() {
            if (!openshift.selector("buildconfig", "${vars.application.name}").exists())
                openshift.newBuild("--name=${vars.application.name}", "--image-stream=s2i-${vars.application.language.toLowerCase()}", "--binary=true")
            openshift.selector("bc", "${vars.application.name}").startBuild("--from-dir=${vars.workDir}/target", "--wait=true")
            if (vars.promoteImage)
                openshift.tag("${openshift.project()}/${vars.application.name}:latest", "${vars.targetProject}/${vars.application.name}:latest")
            else
                println("[JENKINS][WARNING] Image wasn't promoted since there are no environments were added\r\n" +
                        "[JENKINS][WARNING] If your like to promote your images please add environment via your cockpit panel")
        }
    }
}

return this;