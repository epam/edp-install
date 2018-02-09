/**
 * Checkout from gerrit.  Parameters are defined by gerrit trigger
 * @param vars object with all pipeline variables
 */
def run(vars) {
    //sh "git ls-remote --heads origin | grep RC | grep -o '\\..*[0-9]' | awk -F \".\" '{print \$3}' | sort -n -r | head -n 1"
    //sh "git ls-remote --heads origin | grep RC | grep -o '\\..*[0-9]' | awk -F \".\" '{print \$3}' | sort -n -r | head -n 1"
    def RC1 = sh(
                script: """
                        git ls-remote --heads origin | grep RC | grep -o '\\..*[0-9]' | awk -F "." '{print \$3}' | sort -n -r | head -n 1
                    """,
                returnStdout: true
        ).trim()
    vars['RC']=RC1
    vars['RCnum']=RC1.toInteger()+1
    println("New RC number - ${vars.RCnum}")
}

return this;
