Ansible playbook for deploying CI tools for EDP and integrate them together.

How to run:
1) Define parameters in the file group_vars/all with your actual values.
2) Run playbook with the command:
* ansible-playbook -i [oc_master_ip], -b -v -u [username] -e project=[oc_project] install.yml

Please make sure, that you have configured ssh access to the oc_master_ip server by ssh keys, or run the command above
 with -k key, in order to have an ability to determine your password.
