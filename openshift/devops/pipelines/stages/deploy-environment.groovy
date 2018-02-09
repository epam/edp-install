/**
 * Checkout from gerrit.  Parameters are defined by gerrit trigger
 * @param vars object with all pipeline variables
 */
def run(vars) {
    dir("${vars.workDir}/openshift/install/deploy_ci_tools") {
        wrap([$class: 'AnsiColorBuildWrapper', colorMapName: 'xterm']) {
            withEnv(['ANSIBLE_FORCE_COLOR=true', 'PYTHONUNBUFFERED=1']) {
                sh "ansible-playbook install.yml -i localhost, -v -e project=${vars.ocProjectName} -e ocadm=oc -e ansible_connection=local"
            }
        }
    }
    this.result = "success"
}

return this;
