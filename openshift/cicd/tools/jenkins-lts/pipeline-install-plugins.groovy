import jenkins.model.*
import java.util.logging.Logger

def pluginParameter = """
ace-editor:1.0.1
authentication-tokens:1.1
blueocean-autofavorite:1.0.0
blueocean-commons:1.1.6
blueocean-config:1.1.6
blueocean-dashboard:1.1.6
blueocean-display-url:2.0
blueocean-events:1.1.6
blueocean-github-pipeline:1.1.6
blueocean-git-pipeline:1.1.6
blueocean-i18n:1.1.6
blueocean:1.1.6
blueocean-jwt:1.1.6
blueocean-personalization:1.1.6
blueocean-pipeline-api-impl:1.1.6
blueocean-pipeline-editor:0.2.0
blueocean-pipeline-scm-api:1.1.6
blueocean-rest-impl:1.1.6
blueocean-rest:1.1.6
blueocean-web:1.1.6
branch-api:2.0.9
cloudbees-folder:6.0.4
conditional-buildstep:1.3.1
config-file-provider:2.16.2
credentials-binding:1.12
credentials:2.1.13
display-url-api:2.0
docker-commons:1.8
docker-workflow:1.9
durable-task:1.13
favorite:2.3.0
git-client:2.4.4
github-api:1.85.1
github-branch-source:2.0.8
github:1.26.0
github-organization-folder:1.6
git:3.3.2
git-server:1.5
handlebars:1.1
icon-shim:2.0.3
jackson2-api:2.7.3
job-dsl:1.60
jquery-detached:1.2.1
junit:1.20
kubernetes:1.0
mailer:1.20
mapdb-api:1.0.1.0
matrix-auth:1.5
matrix-project:1.7.1
mercurial:1.54
metrics:3.1.2.9
momentjs:1.1
multiple-scms:0.6
openshift-client:1.0.2
openshift-login:1.0.0
openshift-pipeline:1.0.52
openshift-sync:0.1.32
parameterized-trigger:2.35.1
pipeline-build-step:2.5.1
pipeline-github-lib:1.0
pipeline-graph-analysis:1.3
pipeline-input-step:2.8
pipeline-milestone-step:1.3
pipeline-model-api:1.1.4
pipeline-model-declarative-agent:1.1.1
pipeline-model-definition:1.1.4
pipeline-model-extensions:1.1.4
pipeline-rest-api:2.9
pipeline-stage-step:2.2
pipeline-stage-tags-metadata:1.1.4
pipeline-stage-view:2.4
pipeline-utility-steps:1.5.1
plain-credentials:1.3
pubsub-light:1.8
run-condition:0.10
scm-api:2.1.1
script-security:1.31
sse-gateway:1.15
ssh-credentials:1.13
structs:1.7
subversion:2.9
token-macro:2.0
variant:1.1
workflow-aggregator:2.5
workflow-api:2.18
workflow-basic-steps:2.3
workflow-cps-global-lib:2.7
workflow-cps:2.39
workflow-durable-task-step:2.11
workflow-job:2.10
workflow-multibranch:2.14
workflow-remote-loader:1.2
workflow-scm-step:2.4
workflow-step-api:2.10
workflow-support:2.14
bouncycastle-api:2.16.2
nodejs:1.2.4
"""


def logger = Logger.getLogger("")
def installed = false
def initialized = false


def plugins = pluginParameter.split('\n')
logger.info("" + plugins)
def instance = Jenkins.getInstance()
def pm = instance.getPluginManager()
def uc = instance.getUpdateCenter()
uc.updateAllSites()
echo "1"
plugins.each {it ->
    //echo "${it}"
    pluginName = it.split(':')[0]
    println("Checking " + pluginName)
    if (!pm.getPlugin(pluginName)) {
        println("Looking UpdateCenter for " + pluginName)
        if (!initialized) {
            uc.updateAllSites()
            initialized = true
        }
        def plugin = uc.getPlugin(pluginName)
        if (plugin) {
            println("Installing " + pluginName)
            def installFuture = plugin.deploy()
            while(!installFuture.isDone()) {
                logger.info("Waiting for plugin install: " + pluginName)
                Thread.sleep(3000)
            }
            installed = true
        }
    }
}

if (installed) {
    println("Plugins installed, initializing a restart!")
    instance.save()
    instance.doSafeRestart()
}