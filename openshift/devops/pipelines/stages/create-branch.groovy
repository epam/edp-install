/**
 * Checkout from gerrit.  Parameters are defined by gerrit trigger
 * @param vars object with all pipeline variables
 */
def run(vars) {
        sh "git checkout -b 0.1.${vars.RCnum}-RC"
        sh "git push origin 0.1.${vars.RCnum}-RC"
}

return this;
