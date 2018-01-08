FROM alpine:latest
MAINTAINER  Alexander Morozov

USER root
RUN apk add --no-cache --virtual .kubectl-deps openssl \
    && wget -O /usr/local/bin/kubectl https://storage.googleapis.com/kubernetes-release/release/v1.6.1/bin/linux/amd64/kubectl \
    && chmod +x /usr/local/bin/kubectl
RUN apk add --no-cache postgresql bash openssh git curl

COPY entrypoint.sh /

RUN chmod +x /entrypoint.sh

ENTRYPOINT ["/entrypoint.sh"]

