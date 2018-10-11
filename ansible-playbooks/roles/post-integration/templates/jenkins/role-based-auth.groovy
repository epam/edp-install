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

import hudson.*
import hudson.security.*
import jenkins.model.*
import java.util.*
import com.michelin.cio.hudson.plugins.rolestrategy.*
import java.lang.reflect.*
import java.util.logging.*
import groovy.json.*
import org.jenkinsci.plugins.*
import net.sf.json.*
import org.kohsuke.stapler.*

/// Check "done" file to avoid multiple runs
def JENKINS_HOME = System.getenv().get('JENKINS_HOME')
file = new File("${JENKINS_HOME}/done-rbac")
if (file.exists()) {
  println("[DEBUG] RBAC configuration has been already done")
  return
}

/**
 * ===================================
 *
 *        Keycloak config json
 *
 * ===================================
 */

def keycloak_json ='''\
{
  "realm": "{{ tools.keycloak.realm_name }}",
  "auth-server-url": "{{ tools.keycloak.web_url }}/auth",
  "ssl-required": "external",
  "resource": "jenkins",
  "public-client": true,
  "confidential-port": 0
}'''.stripIndent()

/**
 * ===================================
 *
 *                Roles
 *
 * ===================================
 */
def globalRoleRead = "read"
def globalBuildRole = "build"
def globalRoleAdmin = "admin"

/**
 * ===================================
 *
 *           Users and Groups
 *
 * ===================================
 */
def access = [
  admins: ["jenkins-administrators", "admin"],
  builders: ["jenkins-builders"],
  readers: ["jenkins-users"]
]

/**
 * ===================================
 *
 *           Permissions
 *
 * ===================================
 */

// TODO: drive these from a config file
def adminPermissions = [
"hudson.model.Hudson.Administer",
"hudson.model.Hudson.Read"
]

def readPermissions = [
"hudson.model.Hudson.Read",
"hudson.model.Item.Discover",
"hudson.model.Item.Read"
]

def buildPermissions = [
"hudson.model.Hudson.Read",
"hudson.model.Item.Build",
"hudson.model.Item.Cancel",
"hudson.model.Item.Read",
"hudson.model.Run.Replay"
]

def roleBasedAuthenticationStrategy = new RoleBasedAuthorizationStrategy()

Jenkins.instance.setAuthorizationStrategy(roleBasedAuthenticationStrategy)


SecurityRealm realm = new KeycloakSecurityRealm()
realm.getDescriptor().setKeycloakJson(keycloak_json)
Jenkins.instance.setSecurityRealm(realm)

/**
 * ===================================
 *
 *               HACK
 * Inspired by https://issues.jenkins-ci.org/browse/JENKINS-23709
 * Deprecated by on https://github.com/jenkinsci/role-strategy-plugin/pull/12
 *
 * ===================================
 */

Constructor[] constrs = Role.class.getConstructors();
for (Constructor<?> c : constrs) {
  c.setAccessible(true);
}

// Make the method assignRole accessible
Method assignRoleMethod = RoleBasedAuthorizationStrategy.class.getDeclaredMethod("assignRole", String.class, Role.class, String.class);
assignRoleMethod.setAccessible(true);
println("HACK! changing visibility of RoleBasedAuthorizationStrategy.assignRole")

/**
 * ===================================
 *
 *           Permissions
 *
 * ===================================
 */

Set<Permission> adminPermissionSet = new HashSet<Permission>();
adminPermissions.each { p ->
  def permission = Permission.fromId(p);
  if (permission != null) {
    adminPermissionSet.add(permission);
  } else {
    println("${p} is not a valid permission ID (ignoring)")
  }
}

Set<Permission> buildPermissionSet = new HashSet<Permission>();
buildPermissions.each { p ->
  def permission = Permission.fromId(p);
  if (permission != null) {
    buildPermissionSet.add(permission);
  } else {
    println("${p} is not a valid permission ID (ignoring)")
  }
}

Set<Permission> readPermissionSet = new HashSet<Permission>();
readPermissions.each { p ->
  def permission = Permission.fromId(p);
  if (permission != null) {
    readPermissionSet.add(permission);
  } else {
    println("${p} is not a valid permission ID (ignoring)")
  }
}

/**
 * ===================================
 *
 *      Permissions -> Roles
 *
 * ===================================
 */

// admins
Role adminRole = new Role(globalRoleAdmin, adminPermissionSet);
roleBasedAuthenticationStrategy.addRole(RoleBasedAuthorizationStrategy.GLOBAL, adminRole);

// builders
Role buildersRole = new Role(globalBuildRole, buildPermissionSet);
roleBasedAuthenticationStrategy.addRole(RoleBasedAuthorizationStrategy.GLOBAL, buildersRole);

// anonymous read
Role readRole = new Role(globalRoleRead, readPermissionSet);
roleBasedAuthenticationStrategy.addRole(RoleBasedAuthorizationStrategy.GLOBAL, readRole);

/**
 * ===================================
 *
 *      Roles -> Groups/Users
 *
 * ===================================
 */

access.admins.each { l ->
  println("Granting admin to ${l}")
  roleBasedAuthenticationStrategy.assignRole(RoleBasedAuthorizationStrategy.GLOBAL, adminRole, l);
}

access.builders.each { l ->
  println("Granting builder to ${l}")
  roleBasedAuthenticationStrategy.assignRole(RoleBasedAuthorizationStrategy.GLOBAL, buildersRole, l);
}

access.readers.each { l ->
  println("Granting read to ${l}")
  roleBasedAuthenticationStrategy.assignRole(RoleBasedAuthorizationStrategy.GLOBAL, readRole, l);
}

def GlobalKeycloakSettings = Jenkins.instance.getDescriptor("org.jenkinsci.plugins.KeycloakSecurityRealm")
GlobalKeycloakSettings.setKeycloakJson(keycloak_json)
GlobalKeycloakSettings.save()

Jenkins.instance.save()

// Create "done" file to avoid multiple runs
String filename = "${JENKINS_HOME}/done-rbac"
new File(filename).createNewFile()
