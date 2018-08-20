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

package com.epam.edp.sittests.smoke;

import org.apache.commons.lang3.StringUtils;

/**
 * @author Pavlo_Yemelianov
 */
public class UrlBuilder {
    private String ocpEdpPrefix;

    public UrlBuilder(String ocpEdpPrefix) {
        this.ocpEdpPrefix = ocpEdpPrefix;
    }

    public String buildUrl(String protocol, String service, String namespace, String path) {
        return new StringBuilder(protocol)
                .append("://")
                .append(service)
                .append(StringUtils.isNoneEmpty(ocpEdpPrefix) ? "-" : "")
                .append(ocpEdpPrefix)
                .append("-")
                .append(namespace)
                .append(".main.edp.projects.epam.com")
                .append(StringUtils.isNoneEmpty(path) ? "/" : "")
                .append(path)
                .toString();
    }
}
