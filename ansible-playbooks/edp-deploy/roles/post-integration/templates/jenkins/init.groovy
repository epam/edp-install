import jenkins.model.*
import hudson.model.*
import hudson.security.csrf.DefaultCrumbIssuer
import net.sf.json.*
import jenkins.model.GlobalConfiguration
import javaposse.jobdsl.plugin.GlobalJobDslSecurityConfiguration
import org.csanchez.jenkins.plugins.kubernetes.*

def JENKINS_URL = System.getenv().get('JENKINS_UI_URL')
def cloudPluginName = 'openshift'

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

// Configuring k8s plugin
KubernetesCloud openshift = instance.getCloud(cloudPluginName) ?: new KubernetesCloud(cloudPluginName)
openshift.setContainerCapStr("0")
if (!instance.getCloud(cloudPluginName)) {
    instance.clouds.add(openshift)
}
instance.save()