vars = [:]

node("master") {
    if (!env.PIPELINES_PATH)
        error("[JENKINS][ERROR] PIPELINES_PATH variable is not defined, please check.")

    vars['pipelinesPath'] = PIPELINES_PATH

    stage 'Input parameters'
    vars.projectNames=input(id: 'Input', message: 'Input project names', ok: 'OK',
            parameters: [
                    [$class: 'ValidatingStringParameterDefinition', defaultValue: '',
                     description: 'Input comma separated projects list', name: 'PROJECT_NAMES',
                     regex: '([a-z0-9]([-a-z0-9]*[a-z0-9])?(,)?)+[a-z0-9]$',
                     failedValidationMessage: 'Incorrect list of projects']
            ])
    try {
        assert vars.projectNames ==~ /([a-z0-9]([-a-z0-9]*[a-z0-9])?(,)?)+[a-z0-9]$/
    } catch (AssertionError err) {
        error "[JENKINS][DEBUG] - Project list does not match requirements"
    }

    stage 'Delete projects'
    vars.projectsToDelete=vars.projectNames.tokenize(',')
    source=load "${vars.pipelinesPath}/stages/delete-environment.groovy"
    source.run(vars)
}
