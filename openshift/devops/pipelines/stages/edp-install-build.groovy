import hudson.FilePath

def run(vars) {
    def imagesDirectories = new FilePath(Jenkins.getInstance().getComputer(env['NODE_NAME']).getChannel(), "${vars.workDir}/openshift/custom-images").listDirectories()

    vars['images'] = []
    imagesDirectories.each() { directory ->
        def imageName = directory.getName()
        vars.images.push(imageName)
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
                        openshift.tag("${vars.dockerImageProject}/${imageName}:latest", "${vars.dockerImageProject}/${imageName}:${vars.tagVersion}")
                    }
                }
            }
        }
    }
    this.result = "success"
}

return this;
