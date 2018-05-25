# Copyright 2018 EPAM Systems.

# Licensed under the Apache License, Version 2.0 (the \"License\");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
# http://www.apache.org/licenses/LICENSE-2.0

# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an \"AS IS\" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.

# See the License for the specific language governing permissions and
# limitations under the License.

FROM openshift/origin
RUN yum install -y git ansible jq
WORKDIR /opt/edp/
COPY ansible-playbooks /opt/edp/ansible-playbooks
COPY application-pipelines /opt/edp/application-pipelines
COPY oc-templates /opt/edp/oc-templates
COPY edp-images /opt/edp/edp-images
COPY README.md /opt/edp/
COPY LICENSE-2.0 /opt/edp/