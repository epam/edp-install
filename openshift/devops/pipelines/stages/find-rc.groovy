/**
 * Checkout from gerrit.  Parameters are defined by gerrit trigger
 * @param vars object with all pipeline variables
 */
def run(vars) {
    vars['RC'] = sh(
                script: """
                        git ls-remote --heads origin | grep RC | grep -o '\\..*[0-9]' | awk -F "." '{print \$3}' | sort -n -r | head -n 1
                    """,
                returnStdout: true
        ).trim()
    if (vars.RC=="")
        vars['RCnum']=1
    else
        vars['RCnum']=vars.RC.toInteger()+1
}

return this;
