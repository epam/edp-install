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

Ansible playbook for deploying CI tools for EDP and integrate them together.

How to run:
1) Define parameters in the file group_vars/all with your actual values.
2) Run playbook with the command:
* ansible-playbook -i [oc_master_ip], -b -v -u [username] -e project=[oc_project] install.yml

Please make sure, that you have configured ssh access to the oc_master_ip server by ssh keys, or run the command above
 with -k key, in order to have an ability to determine your password.
  