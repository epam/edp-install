PIPELINES_PATH_DEFAULT = "openshift/devops/pipelines"

//Define common variables
vars = [:]
commonLib = null

node("master") {
    vars['pipelinesPath'] = env.PIPELINES_PATH ? PIPELINES_PATH : PIPELINES_PATH_DEFAULT
    vars['devopsRoot'] = "${WORKSPACE.replaceAll("@.*", "")}@script"
    commonLib = load "${vars['devopsRoot']}/${vars.pipelinesPath}/libs/common.groovy"
    commonLib.getConstants(vars)

    vars['branch'] = 'master'
    vars['RC'] = ""
    vars['rcNumber']=0
    vars['prefix']='RC'
    vars['version']='0.1'

    currentBuild.displayName = "${currentBuild.displayName}-${vars.branch}"
    currentBuild.description = """Branch: ${vars.branch}"""
    commonLib.getDebugInfo(vars)

    dir("${vars.devopsRoot}/${vars.pipelinesPath}/stages/") {
        stage("CHECKOUT") {
            stage = load "git-checkout.groovy"
            stage.run(vars)
        }

        stage("FIND LAST RC") {
            stage = load "find-rc.groovy"
            stage.run(vars)
        }

        stage("CREATE BRANCH") {
            stage = load "create-branch.groovy"
            stage.run(vars)
        }

    }

}

