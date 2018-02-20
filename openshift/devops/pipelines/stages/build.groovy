import hudson.FilePath

def run(vars) {
    new FilePath(Jenkins.getInstance().getComputer(env['NODE_NAME']).getChannel(), "${vars.workDir}/openshift/custom-images").listDirectories().each() { directory ->
        def imageName = directory.getName()
        dir("${directory}") {
            def filePath = new FilePath(Jenkins.getInstance().getComputer(env['NODE_NAME']).getChannel(), "${directory}/Dockerfile")
            switch (imageName) {
                case "edp-install": fromDir = "${vars.workDir}"
                default: fromDir = "."
            }
            script {
                openshift.withCluster() {
                    openshift.withProject() {
                        if (!openshift.selector("buildconfig", "${imageName}").exists())
                            openshift.newBuild("--name=${imageName}", "--dockerfile=\"${filePath.readToString().tokenize('\n').join('\n')}\"")
                        openshift.selector("bc", "${imageName}").startBuild("--from-dir=${fromDir}", "--wait=true")
                        openshift.tag("infra/${imageName}:latest", "${vars.imageProject}/${imageName}:${vars.tagVersion}")
                    }
                }
            }
        }
    }
    this.result = "success"
}

return this;
