/**
 * Checkout from gerrit.  Parameters are defined by gerrit trigger
 * @param vars object with all pipeline variables
 */
def run(vars) {
        println("Here - ${RCnum}")
        sh "git checkout -b 0.1.${RCnum}-RC"
        sh "git push origin 0.1.${RCnum}-RC"
}

return this;
