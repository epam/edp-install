FROM frolvlad/alpine-glibc:latest

MAINTAINER Maxim_Buslovaev

ENV OC_VERSION=v3.7.0 \
    OC_TAG_SHA=7ed6862 \
    BUILD_DEPS='tar gzip' \
    RUN_DEPS='curl ca-certificates gettext'

RUN apk --no-cache add $BUILD_DEPS $RUN_DEPS && \
    curl -sLo /tmp/oc.tar.gz https://github.com/openshift/origin/releases/download/${OC_VERSION}/openshift-origin-client-tools-${OC_VERSION}-${OC_TAG_SHA}-linux-64bit.tar.gz && \
    tar xzvf /tmp/oc.tar.gz -C /tmp/ && \
    mv /tmp/openshift-origin-client-tools-${OC_VERSION}-${OC_TAG_SHA}-linux-64bit/oc /usr/local/bin/ && \
    rm -rf /tmp/oc.tar.gz /tmp/openshift-origin-client-tools-${OC_VERSION}-${OC_TAG_SHA}-linux-64bit && \
    apk del $BUILD_DEPS

RUN apk update && apk upgrade && \
    apk add --no-cache bash git openssh && \
    rm -rf /var/lib/apt/lists/* && \
    rm /var/cache/apk/*

ADD add-user-plugin-2.11.3.jar /tmp/add-user-plugin-2.11.3.jar

CMD ["/usr/local/bin/oc"]