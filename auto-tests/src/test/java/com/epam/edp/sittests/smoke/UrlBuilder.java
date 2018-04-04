package com.epam.edp.sittests.smoke;

import org.apache.commons.lang3.StringUtils;

/**
 * @author Pavlo_Yemelianov
 */
public class UrlBuilder {
    private String ocpEdpSuffix;

    public UrlBuilder(String ocpEdpSuffix) {
        this.ocpEdpSuffix = ocpEdpSuffix;
    }

    public String buildUrl(String protocol, String service, String path) {
        return new StringBuilder(protocol)
                .append("://")
                .append(service)
                .append("-edp-cicd")
                .append(StringUtils.isNoneEmpty(ocpEdpSuffix) ? "-" : "")
                .append(ocpEdpSuffix)
                .append(".main.edp.projects.epam.com")
                .append(StringUtils.isNoneEmpty(path) ? "/" : "")
                .append(path)
                .toString();
    }
}
