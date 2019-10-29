# Copyright 2018 EPAM Systems.

# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
# http://www.apache.org/licenses/LICENSE-2.0

# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.

# See the License for the specific language governing permissions and
# limitations under the License.

FROM docker-registry.default.svc:5000/edp-cicd-delivery/jenkins:2.176.3
ENV HELM_VERSION="v2.15.1"
COPY plugins.txt /opt/openshift/configuration/plugins.txt
USER root
RUN wget -q https://get.helm.sh/helm-${HELM_VERSION}-linux-amd64.tar.gz -O - | tar -xzO linux-amd64/helm > /usr/bin/helm \
    && chmod +x /usr/bin/helm \
    && rm -rf linux-amd64
RUN /usr/local/bin/install-plugins.sh /opt/openshift/configuration/plugins.txt
USER jenkins