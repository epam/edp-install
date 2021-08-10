# Overview

Jenkins job provisioners are responsible for creating and managing pipelines in Jenkins.
In other words, provisioners configure all Jenkins pipelines and bring them to the state described in the provisioners code.
Two types of provisioners are available in EDP:

 - CI-provisioner - manages the application folder, and its Code Review, Build and Create Release pipelines.

 - CD-provisioner - manages the Deploy pipelines.

The subsections describe the creation/update process of provisioners and their content depending on EDP customization.
