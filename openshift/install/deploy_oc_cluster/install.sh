#!/bin/bash

# Just static installation run
# composed from MSRA project
ansible-playbook -i ansible/inventory/openshift -u viktor_voronin@epam.com \
  --private-key /home/viktor_voronin\@epam.com/.ssh/id_rsa ansible/os_prepare.yml