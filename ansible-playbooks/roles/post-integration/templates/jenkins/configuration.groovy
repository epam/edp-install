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
import org.csanchez.jenkins.plugins.kubernetes.*
import hudson.plugins.sonar.*
import hudson.plugins.sonar.model.*
import hudson.tools.*
import com.sonyericsson.hudson.plugins.gerrit.trigger.PluginImpl
import com.sonyericsson.hudson.plugins.gerrit.trigger.GerritServer
import com.sonyericsson.hudson.plugins.gerrit.trigger.config.PluginConfig
import net.sf.json.*
import hudson.model.FreeStyleProject
import hudson.tasks.Shell
import javaposse.jobdsl.plugin.*
import hudson.triggers.*
import com.cloudbees.plugins.credentials.CredentialsScope
import com.cloudbees.plugins.credentials.domains.Domain
import com.cloudbees.jenkins.plugins.sshcredentials.impl.*
import com.cloudbees.plugins.credentials.*
import com.cloudbees.plugins.credentials.common.*
import com.cloudbees.plugins.credentials.domains.*
import com.cloudbees.plugins.credentials.impl.*
import ru.yandex.qatools.allure.jenkins.tools.*
import hudson.scm.SCM
import jenkins.plugins.git.GitSCMSource
import org.jenkinsci.plugins.workflow.libs.*
import org.jenkinsci.plugins.workflow.libs.LibraryConfiguration
import org.jenkinsci.plugins.workflow.libs.SCMSourceRetriever
import jenkins.plugins.git.traits.*
import jenkins.scm.api.trait.SCMSourceTrait
import jenkins.plugins.git.traits.RefSpecsSCMSourceTrait.RefSpecTemplate

// Check "done" file to avoid multiple runs
def JENKINS_HOME = System.getenv().get('JENKINS_HOME')
file = new File("${JENKINS_HOME}/done-config")
if (file.exists()) {
    println("[DEBUG] Configuration of Jenkins has been already done")
    return
}

// Sonar plugin configuration
SonarGlobalConfiguration sonarConf = Hudson.instance.getDescriptorByType(SonarGlobalConfiguration.class)
def sonarProperties = new SonarInstallation(
        "Sonar",
        "http://sonar:9000",
        "{{ sonar_token_id }}",
        "5.3",
        "",
        new TriggersConfig(),
        ""
)
sonarConf.setInstallations(sonarProperties)
sonarConf.save()

def sonarDescriptor = Jenkins.instance.getDescriptor("hudson.plugins.sonar.SonarRunnerInstallation")

def sonarRunnerInstaller = new SonarRunnerInstaller("3.1.0.1141")
def installSourceProperty = new InstallSourceProperty([sonarRunnerInstaller])
def sonarRunnerInstance = new SonarRunnerInstallation("SonarQube Scanner", "", [installSourceProperty])

def sonarRunnerInstallations = sonarDescriptor.getInstallations()
sonarRunnerInstallations += sonarRunnerInstance
sonarDescriptor.setInstallations((SonarRunnerInstallation[]) sonarRunnerInstallations)
sonarDescriptor.save()

def sonarDescriptorMsBuild = Jenkins.instance.getDescriptor("hudson.plugins.sonar.MsBuildSQRunnerInstallation")

def sonarRunnerInstallerMsBuild = new MsBuildSonarQubeRunnerInstaller("4.3.1.1372-netcore")
def installSourcePropertyMsBuild = new InstallSourceProperty([sonarRunnerInstallerMsBuild])
def sonarRunnerInstanceMsBuild = new MsBuildSQRunnerInstallation("SonarScannerMSBuild", "", [installSourcePropertyMsBuild])

def sonarRunnerInstallationsMsBuild = sonarDescriptorMsBuild.getInstallations()
sonarRunnerInstallationsMsBuild += sonarRunnerInstanceMsBuild
sonarDescriptorMsBuild.setInstallations((MsBuildSQRunnerInstallation[]) sonarRunnerInstallationsMsBuild)
sonarDescriptorMsBuild.save()

// Modifying Gerrit Plugin for getting an ability to post-configure it via job
PluginImpl plugin = PluginImpl.getInstance();
PluginConfig pluginConfig = plugin.getPluginConfig();

server = new GerritServer("gerrit", false)
def config = server.getConfig()

def triggerConfig = [
    'gerritBuildStartedVerifiedValue':0,
    'gerritBuildStartedCodeReviewValue':0,
    'gerritBuildSuccessfulVerifiedValue':1,
    'gerritBuildSuccessfulCodeReviewValue':0,
    'gerritBuildFailedVerifiedValue':-1,
    'gerritBuildFailedCodeReviewValue':0,
    'gerritBuildUnstableVerifiedValue':0,
    'gerritBuildUnstableCodeReviewValue':0,
    'gerritBuildNotBuiltVerifiedValue':0,
    'gerritBuildNotBuiltCodeReviewValue':0,
  ]
config.setValues(JSONObject.fromObject(triggerConfig))
server.setConfig(config)

server.getConfig().setNumberOfSendingWorkerThreads(pluginConfig.getNumberOfSendingWorkerThreads())
server.getConfig().setGerritHostName("gerrit")
server.getConfig().setGerritFrontEndURL("{{ tools.gerrit.web_url }}")
server.getConfig().setGerritAuthKeyFile(new File("${JENKINS_HOME}/.ssh/id_rsa"))
server.getConfig().setGerritSshPort("{{ gerrit_ssh_port }}".toInteger())
server.getConfig().setGerritUserName("jenkins")
server.getConfig().setUseRestApi(true)
server.getConfig().setGerritHttpUserName("jenkins")
server.getConfig().setGerritHttpPassword("jenkins")
server.getConfig().setRestCodeReview(true)
server.getConfig().setRestVerified(true)

plugin.addServer(server)
server.start()
plugin.save()

//Add credentials
global_domain = Domain.global()
credentials_store = Jenkins.instance.getExtensionList('com.cloudbees.plugins.credentials.SystemCredentialsProvider')[0].getStore()
credentials = new BasicSSHUserPrivateKey(CredentialsScope.GLOBAL, "jenkins", "jenkins",
        new BasicSSHUserPrivateKey.UsersPrivateKeySource(), "", "Jenkins private ssh key for Gerrit")

credentials_slave = new BasicSSHUserPrivateKey(CredentialsScope.GLOBAL, "jenkins-slave", "jenkins-slave",
        new BasicSSHUserPrivateKey.FileOnMasterPrivateKeySource("/var/lib/jenkins/.ssh/jenkins-slave-id_rsa"),
        "", "SSH key to add jenkins slaves")
credentials_store.addCredentials(global_domain, credentials)
credentials_store.addCredentials(global_domain, credentials_slave)

// Creating a job dsl
def jobName = "Job-provisioning"
project = Jenkins.instance.createProject(FreeStyleProject, jobName)
project.getBuildersList().clear()

// Remove OpenShift Sample job
def sampleJob = Jenkins.instance.getItemByFullName('OpenShift Sample')
println("[DEBUG] Deleting ${sampleJob.name}")
sampleJob.delete()

// Copy app settings
def checkDir = "mkdir -p \"${JENKINS_HOME}/project-settings\"\n"
def getAppSettings = $/oc get cm project-settings -o jsonpath='{ .data.app\.settings\.json }' > \
${JENKINS_HOME}/project-settings/app.settings.json/$
def getAutotestSettings = $/oc get cm project-settings -o jsonpath='{ .data.auto-test\.settings\.json }' > \
${JENKINS_HOME}/project-settings/auto-test.settings.json/$
project.buildersList.add(new Shell(checkDir + getAppSettings + "\n" + getAutotestSettings + "\n"))

executeDslScripts = new ExecuteDslScripts()
executeDslScripts.setTargets("*.groovy")
executeDslScripts.setLookupStrategy(LookupStrategy.JENKINS_ROOT)

project.getBuildersList().add(executeDslScripts)
project.save()
project.scheduleBuild()

cmd = [
        'bash',
        '-c',
        """set -x
           |mkdir -p ${JENKINS_HOME}/jobs/${jobName}/workspace
           |cp ${JENKINS_HOME}/init.groovy.d/dsl/*.groovy ${JENKINS_HOME}/jobs/${jobName}/workspace/
           |""".stripMargin() ]
println("[DEBUG] ${cmd.execute().text}")

// Configure Allure
def allureDescriptor = Jenkins.instance.getDescriptor("ru.yandex.qatools.allure.jenkins.tools.AllureCommandlineInstallation")
def allureInstaller = new AllureCommandlineInstaller("2.6.0")
def allureInstallerProps = new InstallSourceProperty([allureInstaller])
def allureInstallation = new AllureCommandlineInstallation("Allure", "", [allureInstallerProps])
allureDescriptor.setInstallations(allureInstallation)
allureDescriptor.save()

// Configure shared libraries
def globalLibraries = Jenkins.instance.getDescriptor("org.jenkinsci.plugins.workflow.libs.GlobalLibraries")

GitSCMSource gitSCMSourceStages = new GitSCMSource(
        "EDP-library-stages",
        "{{ stages_repo }}",
        "",
        "*",
        "",
        false
)

GitSCMSource gitSCMSourcePipelines = new GitSCMSource(
        "EDP-library-pipelines",
        "{{ pipelines_repo }}",
        "",
        "*",
        "",
        false
)

{% if mr_deploy == 'true' %}
List<SCMSourceTrait> traits = new ArrayList<>()
traits.add(new BranchDiscoveryTrait())
List<RefSpecsSCMSourceTrait.RefSpecTemplate> templates = new ArrayList<>()
templates.add(new RefSpecTemplate('+refs/heads/*:refs/remotes/@{remote}/*'))
templates.add(new RefSpecTemplate("+refs/changes/*:refs/remotes/@{remote}/*"))
traits.add(new RefSpecsSCMSourceTrait(templates))

gitSCMSourceStages.setTraits(traits)
gitSCMSourcePipelines.setTraits(traits)
{% endif %}

SCMSourceRetriever sCMSourceRetrieverStages = new SCMSourceRetriever(gitSCMSourceStages)

SCMSourceRetriever sCMSourceRetrieverPipelines = new SCMSourceRetriever(gitSCMSourcePipelines)

LibraryConfiguration libraryConfigurationStages = new LibraryConfiguration("edp-library-stages",
        sCMSourceRetrieverStages)
LibraryConfiguration libraryConfigurationPipelines = new LibraryConfiguration("edp-library-pipelines",
        sCMSourceRetrieverPipelines)

libraryConfigurationStages.setDefaultVersion("{{ stages_version }}")
libraryConfigurationPipelines.setDefaultVersion("{{ pipelines_version }}")

libraryConfigurationStages.setImplicit(false)
libraryConfigurationPipelines.setImplicit(false)

globalLibraries.get().setLibraries([libraryConfigurationStages,libraryConfigurationPipelines])

Jenkins.instance.save()

// Create "done" file to avoid multiple runs
String filename = "${JENKINS_HOME}/done-config"
new File(filename).createNewFile()
