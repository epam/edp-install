/* Copyright 2018 EPAM Systems.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at
http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.

See the License for the specific language governing permissions and
limitations under the License. */

import jenkins.model.*
import hudson.model.*
import hudson.security.csrf.DefaultCrumbIssuer
import net.sf.json.*
import jenkins.model.GlobalConfiguration
import javaposse.jobdsl.plugin.GlobalJobDslSecurityConfiguration
import org.csanchez.jenkins.plugins.kubernetes.*

def JENKINS_HOME = System.getenv().get('JENKINS_HOME')
file = new File("${JENKINS_HOME}/init-done.txt")
if (file.exists()) {
    println("[DEBUG] Initialization of Jenkins has been already done")
    return
}

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

//================================
String filename = "${JENKINS_HOME}/init-done.txt"
new File(filename).createNewFile()
