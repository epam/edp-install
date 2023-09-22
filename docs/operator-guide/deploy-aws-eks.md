# Deploy AWS EKS Cluster

This instruction provides detailed information on the Amazon Elastic Kubernetes Service cluster deployment and contains the additional setup such as Argo CD installation and EDP addons usage for the infrastructure managment.

## Prerequisites

Before the EKS cluster deployment and configuration, make sure to check the prerequisites. Install the required tools listed below:

*  [Git](https://git-scm.com/book/en/v2)
*  [Terraform](https://www.terraform.io/)
*  [AWS CLI](https://docs.aws.amazon.com/cli/latest/userguide/cli-chap-install.html)
*  [tfenv](https://github.com/tfutils/tfenv)

To check the correct tools installation, run the following commands:

  ```bash
  git --version
  terraform version
  aws --version
  tfenv --version
  ```

## Terraform Backend

This step will do the following:

* Create S3 bucket with policy to store terraform states.

* Create DynamoDB to support state locking and consistency checking.

To create the required resources, do the following:

1. Fork<a name="fork"></a> and clone git repo with project [edp-terraform-aws-platform](https://github.com/epmd-edp/edp-terraform-aws-platform.git), rename it in the correspondence with project name.

  ```bash
  git clone https://github.com/epmd-edp/edp-terraform-aws-platform.git
  mv edp-terraform-aws-platform edp-terraform-aws-platform-edp
  cd edp-terraform-aws-platform-edp/s3-backend
  ```

 2. Fill the input variables for Terraform run in the `s3-backend/template.tfvars` file, refer to the [s3-backend/example.tfvars](https://github.com/epmd-edp/edp-terraform-aws-platform/blob/main/s3-backend/example.tfvars) as an example.

  ```tf
  # -- e.g eu-central-1
  region = "<REGION>" # mandatory

  tags = {
    "SysName"      = "Terraform-Backend"
    "SysOwner"     = "owner@example.com"
    "Environment"  = "EKS-TEST-CLUSTER"
  } # isn't required
  ```
  Find the detailed description of the variables in the [s3-backend/variables.tf](https://github.com/epmd-edp/edp-terraform-aws-platform/blob/main/s3-backend/variables.tf) file.

3. Run Terraform apply. Initialize the backend and apply the changes.

  ```bash
  terraform init
  terraform apply -var-file=./template.tfvars
  ```

  ??? note "View: Terraform output example"
      ```bash
      Outputs:

      terraform_lock_table_dynamodb_id = "terraform_locks"
      terraform_states_s3_bucket_name = "terraform-states-012345678910"
      ```

4. (Optional) Commit the local state.

  ```bash
  git add s3-backend/terraform.tfstate
  git commit -m "Terraform state for s3-backend"
  ```

## AWS Account and IAM Roles

This step will do the following:

* Create the AWS IAM role EKSDeployerRole

* Create the AWS IAM role ServiceRoleForEKSWorkerNode

* Create the AWS IAM role AWSServiceRoleEKS

Take the following steps:

1. Navigate to IAM module directory.

  ```bash
  cd ../iam
  ```

2. (Optional) Setup backend for store Terraform states remotely and support state locking and consistency checking via DynamoDB. Insert the missing fields in the file `iam/providers.tf`.

  ```tf
  ...
    backend "s3" {
      bucket         = "terraform-states-<ACCOUNT_ID>"
      key            = "<REGION>/<CLUSTER_NAME>/iam/terraform.tfstate"
      region         = "<REGION>"
      acl            = "bucket-owner-full-control"
      dynamodb_table = "terraform_locks"
      encrypt        = true
    }
  ...
  ```

  !!! note
      If you plan to work with a local state, then comment out this block (the EDP team does not recommend this approach)

3. Fill in the input variables for Terraform run in the `iam/template.tfvars` file. Use the [iam/example.tfvars](https://github.com/epmd-edp/edp-terraform-aws-platform/blob/main/iam/example.tfvars) as an example. Please find the detailed description of the variables in the [iam/variables.tf](https://github.com/epmd-edp/edp-terraform-aws-platform/blob/main/iam/variables.tf) file.

  ```tf
  # Template file to use as an example to create terraform.tfvars file. Fill the gaps instead of <...>
  # More details on each variable can be found in the variables.tf file

  # -- Condition to create or not IAM role for cluster deploy
  create_iam_deployer = true

  # -- Condition to create or not IAM role for Kaniko deploy
  # -- Need in case if used ECR registry. Create it after deploy EKS CLuster
  create_iam_kaniko = false

  # -- Worker and Deployer role variables

  # -- e.g eu-central-1
  region = "<REGION>" # mandatory

  # -- Information about boundary policies
  # -- https://docs.aws.amazon.com/IAM/latest/UserGuide/access_policies_boundaries.html
  iam_permissions_boundary_policy_arn = "arn:aws:iam::<AWS_ACCOUNT_ID>:policy/eo_role_boundary" # mandatory

  tags = "" # isn't mandatory

  # -- Kaniko role variables

  # -- More information
  # -- https://docs.aws.amazon.com/IAM/latest/UserGuide/id_roles_providers.html
  cluster_oidc_issuer_url = "https://oidc.eks.<AWS_REGION>.amazonaws.com/id/<AWS_OIDC_ID>"

  oidc_provider_arn = "arn:aws:iam::<AWS_ACCOUNT_ID>:oidc-provider/oidc.eks.<AWS_REGION>.amazonaws.com/id/<AWS_OIDC_ID>"

  namespace = "edp"
  ```

4.  Initialize the backend and apply the changes.

  ```bash
  terraform init
  terraform apply -var-file=./template.tfvars
  ```

  ??? note "View: Terraform output example"
      ```bash

      Outputs:

      kaniko_iam_role_arn = []
      kaniko_iam_role_name = []
      deployer_iam_role_arn = [
        "arn:aws:iam::012345678910:role/EKSDeployerRole",
      ]
      deployer_iam_role_name = [
        "EKSDeployerRole",
      ]
      ```

## AWS VPC configuration (Optional)

This step will do the following:

* Create the AWS VPC

* Create the AWS VPC Subnets for instances and LB

* Create the AWS VPC Routing

Take the following steps:

1. Navigate to VPC module directory.

  ```bash
  cd ../vpc
  ```

2. (Optional) Setup backend for store Terraform states remotely and support state locking and consistency checking via DynamoDB. Insert the missing fields in the file `vpc/providers.tf`.

  ```tf
  ...
    backend "s3" {
      bucket         = "terraform-states-<ACCOUNT_ID>"
      key            = "<REGION>/<CLUSTER_NAME>/vpc/terraform.tfstate"
      region         = "<REGION>"
      acl            = "bucket-owner-full-control"
      dynamodb_table = "terraform_locks"
      encrypt        = true
    }
  ...
  ```

  !!! note
      If you plan to work with a local state, then comment out this block (the EDP team does not recommend this approach)

3. Fill in the input variables for Terraform run in the `vpc/template.tfvars` file. Use the [vpc/example.tfvars](https://github.com/epmd-edp/edp-terraform-aws-platform/blob/main/vpc/example.tfvars) as an example. Please find the detailed description of the variables in the [vpc/variables.tf](https://github.com/epmd-edp/edp-terraform-aws-platform/blob/main/vpc/variables.tf) file.

  ```tf
  # -- e.g eu-central-1
  region   = "<REGION>"

  role_arn = "arn:aws:iam::<ACCOUNT_ID>:role/EKSDeployerRole"

  platform_name = "<PLATFORM_NAME>"

  # -- VPC CIDR, e.g. "10.0.0.0/16"
  platform_cidr = "<PLATFORM_CIDR>"

  # -- VPC Subnets AZs, e.g. ["eu-central-1a", "eu-central-1b", "eu-central-1c"]
  subnet_azs    = ["<SUBNET_AZS1>", "<SUBNET_AZS2>"]

  # -- Private Subnets CIDR, e.g. ["10.0.1.0/24", "10.0.2.0/24", "10.0.3.0/24"]
  private_cidrs = ["<PRIVATE_CIDRS1>", "<PRIVATE_CIDRS2>"]

  # -- Private Subnets CIDR, e.g. ["10.0.101.0/24", "10.0.102.0/24", "10.0.103.0/24"]
  public_cidrs  = ["<PUBLIC_CIDRS1>", "<PUBLIC_CIDRS2>"]

  # -- Tags for resources, isn't mandatory
  tags = ""
  ```

4. Initialize the backend and apply the changes.

  ```bash
  terraform init
  terraform apply -var-file=./template.tfvars
  ```

  ??? note "View: Terraform output example"
      ```bash

      Outputs:

      private_subnets = [
        "subnet-012345678910",
        "subnet-012345678910",
        "subnet-012345678910",
      ]
      public_subnets = [
        "subnet-012345678910",
        "subnet-012345678910",
        "subnet-012345678910",
      ]
      vpc_id = "vpc-012345678910"
      ```

## Deploy and preconfigure AWS EKS

This step will do the following:

* Create the EKS Cluster

* Configure the EKS base configuration

* Deploy base components into EKS cluster

* Instal and configure Argo CD

* Onboard EDP addons on EKS cluster

Take the following steps:

1. Navigate to EKS module directory.

  ```bash
  cd ../eks
  ```

2. (Optional) Setup backend for store Terraform states remotely and support state locking and consistency checking via DynamoDB. Insert the missing fields in the file `eks/providers.tf`.

  ```tf
  ...
    backend "s3" {
      bucket         = "terraform-states-<ACCOUNT_ID>"
      key            = "<REGION>/<CLUSTER_NAME>/eks/terraform.tfstate"
      region         = "<REGION>"
      acl            = "bucket-owner-full-control"
      dynamodb_table = "terraform_locks"
      encrypt        = true
    }
  ...
  ```

  !!! note
      If you plan to work with a local state, then comment out this block (the EDP team does not recommend this approach)

3. Fill in the input variables for Terraform run in the `eks/template.tfvars` file. Use the [eks/example.tfvars](https://github.com/epmd-edp/edp-terraform-aws-platform/blob/main/eks/example.tfvars) as an example. Please find the detailed description of the variables in the [eks/variables.tf](https://github.com/epmd-edp/edp-terraform-aws-platform/blob/main/eks/variables.tf) file.

  ```tf
  # -- e.g eu-central-1
  region               = "<REGION>"
  platform_name        = "<PLATFORM_NAME>"
  platform_domain_name = "<PLATFORM_DNS>"

  role_arn                      = "arn:aws:iam::<AWS_ACCOUNT_ID>:role/EKSDeployerRole"
  role_permissions_boundary_arn = "arn:aws:iam::<AWS_ACCOUNT_ID>:policy/eo_role_boundary"

  vpc_id             = "<VPC_ID>" # VPC ID
  private_subnets_id = []         # EKS must have two subnets.
  public_subnets_id  = []         # ALB must have two subnets.

  infra_public_security_group_ids = [] # List with security groups

  # -- Parameter in AWS Parameter Store that contain data in format "account:token" in base64 format
  add_userdata = <<EOF
  export TOKEN=$(aws ssm get-parameter --name <PARAMETER_NAME> --query 'Parameter.Value' --region <REGION> --output text)
  cat <<DATA > /var/lib/kubelet/config.json
  {
    "auths":{
      "https://index.docker.io/v1/":{
        "auth":"$TOKEN"
      }
    }
  }
  DATA
  EOF

  spot_instance_types = [] # list with instance types

  aws_auth_users = [] # -- AWS List users
  tags           = ""

  enable_argocd = true

  argocd_manage_add_ons = true

  eks_addons_repo_ssh_key_secret_name = "<AWS_SECRET_MANAGER_KEY>" # ssh key with add-ons repository

  repo_url = "<SSH_REPO_URL>" # repository with add-ons

  addons_path = "<ADD_ONS_FOLDER>" # path to add-ons folder at repository

  # OIDC Identity provider
  cluster_identity_providers = {}
  ```

  This section contains variables to configure addons approach. Please find the detailed description about [EDP addons](add-ons-overview.md).

4. Setup Argo CD for work with addons and store and management cluster applications using the GitOps approach. Fill in the input variables in the file [eks/values/argocd-values.yaml](https://github.com/epmd-edp/edp-terraform-aws-platform/blob/master/eks/values/argocd-values.yaml).

  ```yaml
  configs:
    ssh:
      knownHosts: |
        # -- list of known host in format:
        # [host]:port key-type key
        # Example
        # [ssh.github.com]:443 ssh-rsa qgSdfOuiYhew/+afhQnvjfjhnhnqgSdfOuiYhew/+afhQnvjfjhnhn
  ...
  server:
    ingress:
      hosts:
        - "argocd.example.com"
  ```

5. Update local Kubernetes configuration:

  ```bash
  aws eks update-kubeconfig --region <REGION> --name <CLUSTER_NAME>
  ```

After following the instructions provided, you should be able to configure AWS and create an EKS cluster with installed base utilities.

## Related Articles

* [Cluster Add-Ons Overview](add-ons-overview.md)
* [Install EDP](install-edp.md)
