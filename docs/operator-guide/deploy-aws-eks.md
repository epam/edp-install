# Deploy AWS EKS Cluster

This instruction offers a comprehensive guide on deploying an Amazon Elastic Kubernetes Service (EKS) cluster, ensuring a scalable and secure Kubernetes environment on AWS. For those looking to optimize their EKS cluster configurations, it is highly recommended to consult the [AWS EKS Best Practices](https://aws.github.io/aws-eks-best-practices/) guide. This resource covers a wide range of topics crucial for the successful deployment and operation of your EKS clusters, including:

- **Security**: Best practices for securing your EKS clusters, including IAM roles, network policies, and secrets management.
- **Networking**: Guidance on setting up VPCs, subnets, and load balancers to ensure efficient and secure network traffic.
- **Monitoring and Logging**: Strategies for implementing comprehensive monitoring and logging solutions using AWS CloudWatch and other tools to maintain visibility into cluster performance and operational health.
- **Performance**: Tips for optimizing cluster performance through the proper selection of EC2 instances, efficient load balancing, and autoscaling configurations.
- **Cost Optimization**: Techniques for managing and reducing costs associated with running EKS clusters, including instance selection and resource allocation strategies.

By adhering to these best practices, developers and system administrators can ensure that their AWS EKS clusters are robust, secure, and cost-effective, facilitating a smooth and efficient CI/CD pipeline for software development.

## Prerequisites

!!! note
    Our approach to deploying the AWS EKS Cluster is based on the widely-used [terraform-aws-eks module](https://github.com/terraform-aws-modules/terraform-aws-eks) from the Terraform AWS Modules community. This module facilitates the creation of AWS Elastic Kubernetes Service (EKS) resources with best practices in mind. We encourage users to review the module's documentation to fully understand its capabilities and how it aligns with the requirements of your specific deployment scenario.

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

!!! note
    Before initiating the deployment, it is crucial to consult the [AWS Service Limits](https://docs.aws.amazon.com/general/latest/gr/aws_service_limits.html) documentation. Please review and adjust these limits as necessary to ensure your deployment proceeds smoothly without hitting service constraints.

## Terraform Backend

This step will do the following:

* Create S3 bucket with policy to store terraform states.
* Create DynamoDB to support state locking and consistency checking.

To create the required resources, do the following:

1. Fork and clone git repo with project [edp-terraform-aws-platform](https://github.com/epmd-edp/edp-terraform-aws-platform.git),
rename it in the correspondence with project name.

  ```bash
  git clone https://github.com/epmd-edp/edp-terraform-aws-platform.git
  mv edp-terraform-aws-platform edp-terraform-aws-platform-edp
  cd edp-terraform-aws-platform-edp/s3-backend
  ```

2. Fill the input variables for Terraform run in the `s3-backend/template.tfvars` file, refer to the [s3-backend/example.tfvars](https://github.com/epmd-edp/edp-terraform-aws-platform/blob/main/s3-backend/example.tfvars) as an example.

  ```tf title="s3-backend/template.tfvars"
  region = "eu-central-1"

  tags = {
    "SysName"      = "Terraform-Backend"
    "SysOwner"     = "owner@example.com"
    "Environment"  = "EKS-TEST-CLUSTER"
  }
  ```

  Find the detailed description of the variables in the [s3-backend/variables.tf](https://github.com/epmd-edp/edp-terraform-aws-platform/blob/main/s3-backend/variables.tf) file.

3. Run Terraform apply. Initialize the backend and apply the changes.

  ```bash
  terraform init
  terraform apply -var-file=./template.tfvars
  ```

  !!! note "View: Terraform output example"
      ```bash
      Outputs:

      terraform_lock_table_dynamodb_id = "terraform_locks"
      terraform_states_s3_bucket_name = "terraform-states-012345678910"
      ```

## AWS IAM Roles

This step will do the following:

* Create the AWS IAM role EKSDeployerRole

Take the following steps:

1. Navigate to IAM module directory.

  ```bash
  cd ../iam
  ```

2. Setup backend for store Terraform states remotely and support state locking and consistency checking via DynamoDB.
Insert the missing fields in the file `iam/providers.tf`.

  ```tf title="iam/providers.tf"
  ...
    backend "s3" {
      bucket         = "terraform-states-012345678910"
      key            = "eu-central-1/test/iam/terraform.tfstate"
      region         = "eu-central-1"
      acl            = "bucket-owner-full-control"
      dynamodb_table = "terraform_locks"
      encrypt        = true
    }
  ...
  ```

3. Fill in the input variables for Terraform run in the `iam/template.tfvars` file. Use the [iam/example.tfvars](https://github.com/epmd-edp/edp-terraform-aws-platform/blob/main/iam/example.tfvars) as an example.
Please find the detailed description of the variables in the [iam/variables.tf](https://github.com/epmd-edp/edp-terraform-aws-platform/blob/main/iam/variables.tf) file.

  ```tf title="iam/template.tfvars"
  create_iam_deployer = true

  region = "eu-central-1"

  deployer_iam_permissions_boundary_policy_arn = "arn:aws:iam::012345678910:policy/eo_role_boundary"

  tags = {
    "SysName"      = "Terraform-Backend"
    "SysOwner"     = "owner@example.com"
    "Environment"  = "EKS-TEST-CLUSTER"
  }
  ```

4.  Initialize the backend and apply the changes.

  ```bash
  terraform init
  terraform apply -var-file=./template.tfvars
  ```

  !!! note "View: Terraform output example"
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
* Create the AWS VPC Subnets for instances and AWS ALB
* Create the AWS VPC Routing

Take the following steps:

1. Navigate to VPC module directory.

  ```bash
  cd ../vpc
  ```

2. Setup backend for store Terraform states remotely and support state locking and consistency checking via DynamoDB.
Insert the missing fields in the file `vpc/providers.tf`.

  ```tf title="vpc/providers.tf"
  ...
    backend "s3" {
      bucket         = "terraform-states-012345678910"
      key            = "eu-central-1/test/vpc/terraform.tfstate"
      region         = "eu-central-1"
      acl            = "bucket-owner-full-control"
      dynamodb_table = "terraform_locks"
      encrypt        = true
    }
  ...
  ```

3. Fill in the input variables for Terraform run in the `vpc/template.tfvars` file. Use the [vpc/example.tfvars](https://github.com/epmd-edp/edp-terraform-aws-platform/blob/main/vpc/example.tfvars) as an example.
Please find the detailed description of the variables in the [vpc/variables.tf](https://github.com/epmd-edp/edp-terraform-aws-platform/blob/main/vpc/variables.tf) file.

  ```tf title="vpc/template.tfvars"
  region   = "eu-central-1"

  role_arn = "arn:aws:iam::012345678910:role/EKSDeployerRole"

  platform_name = "test"

  platform_cidr = "192.168.0.0/20"

  subnet_azs    = ["eu-central-1a", "eu-central-1b", "eu-central-1c"]

  private_cidrs = ["192.168.0.0/22", "192.168.4.0/22", "192.168.8.0/22"]

  public_cidrs  = ["192.168.12.0/24", "192.168.13.0/24", "192.168.14.0/24"]

  tags = {
    "SysName"      = "Terraform-Backend"
    "SysOwner"     = "owner@example.com"
    "Environment"  = "EKS-TEST-CLUSTER"
  }
  ```

4. Initialize the backend and apply the changes.

  ```bash
  terraform init
  terraform apply -var-file=./template.tfvars
  ```

  !!! note "View: Terraform output example"
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
* Create the AWS ASGs for the EKS Cluster
* Create the AWS ALB
* (Optional) Create the AWS IAM role Kaniko to use AWS ECR

Take the following steps:

1. Navigate to EKS module directory.

  ```bash
  cd ../eks
  ```

2. Setup backend for store Terraform states remotely and support state locking and consistency checking via DynamoDB.
Insert the missing fields in the file `eks/providers.tf`.

  ```tf title="eks/providers.tf"
  ...
    backend "s3" {
      bucket         = "terraform-states-012345678910"
      key            = "eu-central-1/test/eks/terraform.tfstate"
      region         = "eu-central-1"
      acl            = "bucket-owner-full-control"
      dynamodb_table = "terraform_locks"
      encrypt        = true
    }
  ...
  ```

3. Fill in the input variables for Terraform run in the `eks/template.tfvars` file. Use the [eks/example.tfvars](https://github.com/epmd-edp/edp-terraform-aws-platform/blob/main/eks/example.tfvars) as an example.
Please find the detailed description of the variables in the [eks/variables.tf](https://github.com/epmd-edp/edp-terraform-aws-platform/blob/main/eks/variables.tf) file.

  ```tf title="eks/providers.tf"
  region               = "eu-central-1"
  platform_name        = "test"
  platform_domain_name = "example.com"

  role_arn                      = "arn:aws:iam::012345678910:role/EKSDeployerRole"
  role_permissions_boundary_arn = "arn:aws:iam::012345678910:policy/eo_role_boundary"

  vpc_id             = "vpc-012345678910"
  private_subnets_id = ["subnet-012345678910", "subnet-012345678910", "subnet-012345678910"]
  public_subnets_id  = ["subnet-012345678910", "subnet-012345678910", "subnet-012345678910"]

  tags = {
    "SysName"      = "Terraform-Backend"
    "SysOwner"     = "owner@example.com"
    "Environment"  = "EKS-TEST-CLUSTER"
  }
  ```

4. (Optional) To create Kaniko AWS IAM Role, navigate to IAM module directory.

  ```bash
  cd ../iam
  ```

  Fill in the input variables for Terraform run in the `iam/template.tfvars` file. Use the [iam/example.tfvars](https://github.com/epmd-edp/edp-terraform-aws-platform/blob/main/iam/example.tfvars) as an example.

  Please find the detailed description of the variables in the [iam/variables.tf](https://github.com/epmd-edp/edp-terraform-aws-platform/blob/main/iam/variables.tf) file.

  ```tf title="iam/template.tfvars"
  create_iam_kaniko = true

  region = "eu-central-1"

  kaniko_iam_permissions_boundary_policy_arn = "arn:aws:iam::012345678910:policy/eo_role_boundary"

  tags = {
    "SysName"      = "Terraform-Backend"
    "SysOwner"     = "owner@example.com"
    "Environment"  = "EKS-TEST-CLUSTER"
  }

  cluster_oidc_issuer_url = "https://oidc.eks.eu-central-1.amazonaws.com/id/012345678910"
  oidc_provider_arn       = "arn:aws:iam::012345678910:oidc-provider/oidc.eks.eu-central-1.amazonaws.com/id/012345678910"
  namespace               = "edp"
  ```

  Initialize the backend and apply the changes.

  ```bash
  terraform init
  terraform apply -var-file=./template.tfvars
  ```

5. Update local Kubernetes configuration:

  ```bash
  aws eks update-kubeconfig --region <REGION> --name <CLUSTER_NAME>
  ```

6. Once AWS EKS Cluster is successfully deployed, you can navigate to our [EDP addons](add-ons-overview.md) to install and manage cluster applications using the GitOps approach.

## Related Articles

* [Cluster Add-Ons Overview](add-ons-overview.md)
* [Install EDP](install-edp.md)
