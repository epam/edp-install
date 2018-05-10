import jenkins.model.*;
import net.sf.json.*;
import org.kohsuke.stapler.*;

def keycloak_json ='''\
{
  "realm": "{{ tools.keycloak.realm_name }}",
  "auth-server-url": "{{ tools.keycloak.web_url }}/auth",
  "ssl-required": "external",
  "resource": "jenkins",
  "public-client": true,
  "confidential-port": 0
}'''.stripIndent()

if ( Jenkins.instance.pluginManager.activePlugins.find { it.shortName == "keycloak" } != null ) {
  println "--> setting keycloak plugins settings"

  def descriptor = Jenkins.instance.getDescriptorByType(org.jenkinsci.plugins.KeycloakSecurityRealm.DescriptorImpl.class)
  descriptor.setKeycloakJson(keycloak_json)
}
