Copyright 2018 EPAM Systems.

Licensed under the Apache License, Version 2.0 (the "License");  
you may not use this file except in compliance with the License.  
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software  
distributed under the License is distributed on an "AS IS" BASIS,  
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  
See the License for the specific language governing permissions and  
limitations under the License.

EPAM Delivery Platform (EDP) aka "The Rocket"

This repository project includes all source files that are part of EPAM Delivery Platform (EDP) aka "The Rocket"

Structure of the project includes the following root directories:

1) ansible-playbooks
Contains all necessary playbooks that are used to deploy EDP itself and to perform other actions like adding a business application,
adding environments, adding autotest and etc.
At the moment we have the following Ansible playbooks:
- install.yml (Installs all EDP tools with post integration between each other)
- add-environments (Adds environments specified via Cockpit panel)
- add-business-application (Adds business applications specified via Cockpit panel)
- add-autotests (Adds autetests specified via Cockpit panel)

2) application-pipelines
Contains files that describe all Jenkins pipelines logic and their stages that are used in CI/CD process in SDLC.

3) edp-images
Contains separate folders for each docker image that used in EDP. Each folder includes Dockerfile with build instruction for
a particular image and if needed some files for the build.

4) oc-templates
Contains Openshift templates files that are used during EDP deployment or to perform other actions like adding business application,
adding environments, adding autotest and etc.

In order to have a more flexible solution, all these files which are copied to some particular project
to local DevOps repository can be changed at any time by project's DevOps team in order to add or change some logic of behavior of EDP.

