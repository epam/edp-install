import hudson.FilePath

def run(vars) {
    def imagesDirectories = new FilePath(Jenkins.getInstance().getComputer(env['NODE_NAME']).getChannel(), "${vars.workDir}/openshift/install/custom-images").listDirectories()

    vars['images'] = []
    imagesDirectories.each() { directory ->
        def imageName = directory.getName()
        vars.images.push(imageName)
        dir("${directory}") {
            def filePath = new FilePath(Jenkins.getInstance().getComputer(env['NODE_NAME']).getChannel(), "${directory}/Dockerfile")
            def fromDir
            switch (imageName) {
                case "edp-install": fromDir = "${vars.workDir}"
                default: fromDir = "."
            }
            script {
                openshift.withCluster() {
                    openshift.withProject() {
                        if (!openshift.selector("buildconfig", "${imageName}").exists()) {
                            openshift.newBuild("--name=${imageName}", "--dockerfile=\"${filePath.readToString().tokenize('\n').join('\n')}\"")
                            println("[JENKINS][DEBUG] Build config ${imageName} didn't exist, we've created it")
                        }
                        openshift.selector("bc", "${imageName}").startBuild("--from-dir=${fromDir}", "--wait=true")
                        println("[JENKINS][DEBUG] Build config ${imageName} has been completed")
                    }
                }
            }
        }
    }
    this.result = "success"
}

return this;
