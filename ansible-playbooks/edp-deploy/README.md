Ansible playbook for deploying CI tools for EDP and integrate them together.

How to run:
1) Define parameters in the file group_vars/all with your actual values.
2) Run playbook with the command:
* ansible-playbook -i [oc_master_ip], -b -v -u [username] -e project=[oc_project] install.yml

Please make sure, that you have configured ssh access to the oc_master_ip server by ssh keys, or run the command above
 with -k key, in order to have an ability to determine your password.
 
Ansible playbook for updating ConfigMaps with new services.

How to run:
 1. ansible-playbook -i [OC_MASTER_IP], -e "git_repo_url=[GIT_REPO_URL] app_name=[NAME] \
  language=[LANGUAGE] build_tool=[BUILD_TOOL] framework=[FRAMEWORK] 
  route=[TRUE\FALSE]" add-business-application.yml
