//===========================
// Base classes
//===========================
import jenkins.model.*
import hudson.model.*
import groovy.io.FileType
//===========================
// Global security classes
//===========================
import hudson.security.csrf.DefaultCrumbIssuer
//===========================
//===========================
//Openshift sync plugin
//==========================
import io.fabric8.jenkins.openshiftsync.GlobalPluginConfiguration
//===========================
//Kubernetes plugin
//===========================
import org.csanchez.jenkins.plugins.kubernetes.*
//===========================
//Sonar plugin
//===========================
import hudson.plugins.sonar.*
import hudson.plugins.sonar.model.*
//===========================
//Gerrit plugin
//===========================
import com.sonyericsson.hudson.plugins.gerrit.trigger.PluginImpl;
import com.sonyericsson.hudson.plugins.gerrit.trigger.GerritServer;
import com.sonyericsson.hudson.plugins.gerrit.trigger.config.Config;
import com.sonyericsson.hudson.plugins.gerrit.trigger.config.PluginConfig;
//===========================
//Job-dsl plugin
//===========================
import hudson.model.FreeStyleProject;
import hudson.tasks.Shell;
import javaposse.jobdsl.plugin.*;
import hudson.triggers.*
// Security disabling for DSl plugin
import javaposse.jobdsl.plugin.GlobalJobDslSecurityConfiguration
import jenkins.model.GlobalConfiguration
//===========================
def JENKINS_HOME = System.getenv().get('JENKINS_HOME')
def GERRIT_SSH_PORT = System.getenv().get('GERRIT_SSH_PORT')
file = new File("${JENKINS_HOME}/done.txt")
if (file.exists()) {
    println "Configuration of Jenkins has been already done"
}
else {
    //===========================
    // Enabling Crumb
    //===========================
    def instance = Jenkins.instance
    instance.setCrumbIssuer(new DefaultCrumbIssuer(true))
    instance.save()
    //===========================
    // Security disabling for DSl plugin
    GlobalConfiguration.all().get(GlobalJobDslSecurityConfiguration.class).useScriptSecurity=false
    //===========================

    //===========================
    // Modifying cloud Openshift that is located in Jenkins global configuration
    //===========================
    // Example of adding a new template*/
    //
    //PodTemplate p = new PodTemplate()
    //p.setName('test-template')
    //p.setLabel('test-template')
    //p.setRemoteFs('/home/jenkins')
    //
    //instance.clouds[0].addTemplate((PodTemplate) p)
    //
    //instance.save()
    //===========================
    def openshiftCloud = instance.clouds[0]
    def templates = openshiftCloud.getTemplates()
    def templatesRemove = []
    for (template in templates)
    {
        if (template.getName() =~  "^(nodejs|maven)") {
            templatesRemove.add(template)
        }
    }
    for (template in templatesRemove)
    {
        openshiftCloud.removeTemplate(template)
    }
    instance.save()
    //===========================
    // Deleting some plugins from base Openshift image
    //===========================
    //listPluginsToRemove = """"""
    //def plugins = listPluginsToRemove.trim().split('\n')
    //def pluginManager = instance.getPluginManager()
    //plugins.each {it ->
    //  def pluginName = it.split(':')[0]
    //  def pluginWrapperToUninstall = pluginManager.getPlugin(pluginName)
    //  pluginWrapperToUninstall.doDoUninstall()
    //}
    //instance.save()
    //instance.doSafeRestart()
    //===========================
    //===========================
    // Modifying Sonar Plugin for getting an ability to post-configure it via job
    //===========================
    SonarGlobalConfiguration sonarConf = Hudson.instance.getDescriptorByType(SonarGlobalConfiguration.class)
    def sinst = new SonarInstallation(
            "Sonar", // Name
            "http://sonar:9000",
            "5.3",
            "31fd1e3aebd44bc47a6caa9d274765a44ed82e5d", // Token
            "",
            "",
            "",
            "",
            "",
            new TriggersConfig(),
            "",
            "",
            "" // Additional Analysis Properties
    )
    sonarConf.setInstallations(sinst)
    sonarConf.save()
    //===========================
    //===========================
    // Modifying Gerrit Plugin for getting an ability to post-configure it via job
    //===========================

    PluginImpl plugin = PluginImpl.getInstance();

    server = new GerritServer("gerrit", false)

    PluginConfig pluginConfig = plugin.getPluginConfig();

    server.getConfig().setNumberOfSendingWorkerThreads(pluginConfig.getNumberOfSendingWorkerThreads());
    server.getConfig().setGerritHostName("gerrit")
    server.getConfig().setGerritFrontEndURL("http://gerrit:8080/")
    server.getConfig().setGerritAuthKeyFile(new File("${JENKINS_HOME}/.ssh/id_rsa"))
    server.getConfig().setGerritSshPort("${GERRIT_SSH_PORT}".toInteger())
    server.getConfig().setGerritUserName("jenkins")
    server.getConfig().setUseRestApi(true)
    server.getConfig().setGerritHttpUserName("jenkins")
    server.getConfig().setGerritHttpPassword("jenkins")
    server.getConfig().setRestCodeReview(true)
    server.getConfig().setRestVerified(true)

    plugin.addServer(server);
    server.start();
    plugin.save();
    //===========================
    //===========================
    // Generatin keys for Gerrit exchange
    def cmd = [
            'bash',
            '-c',
            '''[ ! -d ${JENKINS_HOME}/.ssh ] && mkdir -p ${JENKINS_HOME}/.ssh
          |cat /dev/zero | ssh-keygen -b 4096 -t rsa -f ${JENKINS_HOME}/.ssh/id_rsa -C "jenkins@example.com" -q -N ""
          |'''.stripMargin() ]
    println cmd.execute().text
    //===========================
    //===========================
    // Creating a job dsl
    //===========================
    def jobName="jobsprovisioning"
    project = Jenkins.instance.createProject(FreeStyleProject, jobName)
    project.getBuildersList().clear()
    // Copy app settings
    def checkDir = "[[ -d ${JENKINS_HOME}/app-settings ]] || mkdir \"${JENKINS_HOME}/app-settings\"\n"
    def copyJson = $/oc get cm project-settings --template='{{ index .data "app.settings.json" }}' > ${JENKINS_HOME}/app-settings/app.settings.json/$
    project.buildersList.add(new Shell(checkDir + copyJson))

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
           |[ ! -d ${JENKINS_HOME}/jobs/${jobName}/workspace ] && mkdir -p ${JENKINS_HOME}/jobs/${jobName}/workspace
           |cp ${JENKINS_HOME}/init.groovy.d/dsl/*.groovy ${JENKINS_HOME}/jobs/${jobName}/workspace/
           |""".stripMargin() ]
    println cmd.execute().text

    //================================
    String filename = "${JENKINS_HOME}/done.txt"
    boolean success = new File(filename).createNewFile()
}