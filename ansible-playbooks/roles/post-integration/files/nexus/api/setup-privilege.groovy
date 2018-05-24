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

import groovy.json.JsonSlurper
import org.sonatype.nexus.security.privilege.NoSuchPrivilegeException
import org.sonatype.nexus.security.user.UserManager
import org.sonatype.nexus.security.privilege.Privilege

parsed_args = new JsonSlurper().parseText(args)

authManager = security.getSecuritySystem().getAuthorizationManager(UserManager.DEFAULT_SOURCE)

def privilege
boolean update = true

try {
    privilege = authManager.getPrivilege(parsed_args.name)
} catch (NoSuchPrivilegeException ignored) {
    // could not find any existing  privilege
    update = false
    privilege = new Privilege(
            'id': parsed_args.name,
            'name': parsed_args.name
    )
}

privilege.setDescription(parsed_args.description)
privilege.setType(parsed_args.type)
privilege.setProperties([
        'format': parsed_args.format,
        'repository': parsed_args.repository,
        'actions': parsed_args.actions.join(',')
] as Map<String, String>)

if (update) {
    authManager.updatePrivilege(privilege)
} else {
    authManager.addPrivilege(privilege)
}
