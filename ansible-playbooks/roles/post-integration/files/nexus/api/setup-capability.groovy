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
import org.sonatype.nexus.capability.CapabilityReference
import org.sonatype.nexus.capability.CapabilityType
import org.sonatype.nexus.internal.capability.DefaultCapabilityReference
import org.sonatype.nexus.internal.capability.DefaultCapabilityRegistry

parsed_args = new JsonSlurper().parseText(args)

parsed_args.capability_properties['headerEnabled'] = parsed_args.capability_properties['headerEnabled'].toString()
parsed_args.capability_properties['footerEnabled'] = parsed_args.capability_properties['footerEnabled'].toString()

def capabilityRegistry = container.lookup(DefaultCapabilityRegistry.class.getName())
def capabilityType = CapabilityType.capabilityType(parsed_args.capability_typeId)

DefaultCapabilityReference existing = capabilityRegistry.all.find { CapabilityReference capabilityReference ->
    capabilityReference.context().descriptor().type() == capabilityType
}

if (existing) {
    log.info(parsed_args.typeId + ' capability updated to: {}',
            capabilityRegistry.update(existing.id(), existing.active, existing.notes(), parsed_args.capability_properties).toString()
    )
}
else {
    log.info(parsed_args.typeId + ' capability created as: {}', capabilityRegistry.
            add(capabilityType, true, 'configured through api', parsed_args.capability_properties).toString()
    )
}
