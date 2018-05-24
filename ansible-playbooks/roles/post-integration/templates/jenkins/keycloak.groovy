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
