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

FROM openshift/origin
RUN yum install -y https://dl.fedoraproject.org/pub/epel/epel-release-latest-7.noarch.rpm
RUN yum install -y git ansible

RUN groupadd -g 1000 edp && useradd -u 1000 -g edp -m -c edp_user -d /home/edp edp \
    && mkdir -p /var/lib/origin/openshift.local.config \
    && chown -R edp:edp /var/lib/origin \
    && chmod -R 777 /var/lib/origin
ENV HOME /home/edp
WORKDIR /home/edp

COPY . ./
RUN chown -R edp:edp .

USER edp