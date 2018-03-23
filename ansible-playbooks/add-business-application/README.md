Ansible playbook for updating ConfigMaps with new services.

How to run:
 1. ansible-playbook -i [OC_MASTER_IP], -e "git_repo_url=[GIT_URL] app_name=[APPLICATION_NAME] \
  language=[APPLICATION_LANGUAGE] build_tool=[APPLICATION_BUILD_TOOL] framework=[APPLICATION_FRAMEWORK] 
  route=[TRUE\FALSE]" add-business-application.yml