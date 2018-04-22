import jenkins.model.*
import hudson.model.*
import hudson.security.csrf.DefaultCrumbIssuer
import net.sf.json.*
import jenkins.model.GlobalConfiguration
import javaposse.jobdsl.plugin.GlobalJobDslSecurityConfiguration

def JENKINS_URL = System.getenv().get('JENKINS_UI_URL')

//Set Jenkins URL
urlConfig = JenkinsLocationConfiguration.get()
urlConfig.setUrl(JENKINS_URL)
urlConfig.save()
println("[DEBUG] Jenkins URL Set to ${JENKINS_URL}")

// Enabling Crumb
def instance = Jenkins.instance
instance.setCrumbIssuer(new DefaultCrumbIssuer(true))
instance.save()

// Security disabling for DSl plugin
GlobalConfiguration.all().get(GlobalJobDslSecurityConfiguration.class).useScriptSecurity = false
GlobalConfiguration.all().get(GlobalJobDslSecurityConfiguration.class).save()