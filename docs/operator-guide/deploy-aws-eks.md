# Deploy AWS EKS Cluster

This instruction provides detailed information on the Amazon Elastic Kubernetes Service cluster deployment and contains the additional setup necessary for the managed infrastructure.

## Prerequisites

Before the EKS cluster deployment and configuration, make sure to check the prerequisites.

#### Required Tools

Install the required tools listed below:

*  [Git](https://git-scm.com/book/en/v2)

*  [tfenv](https://github.com/tfutils/tfenv)

*  [AWS CLI](https://docs.aws.amazon.com/cli/latest/userguide/cli-chap-install.html)

*  [kubectl](https://docs.aws.amazon.com/eks/latest/userguide/install-kubectl.html)

*  [helm](https://helm.sh/docs/intro/install/)

*  [lens](https://k8slens.dev/) (optional)

To check the correct tools installation, run the following commands:

    $ git --version
    $ tfenv --version
    $ aws --version
    $ kubectl version
    $ helm version

#### AWS Account and IAM Roles

* Make sure the AWS account is active.

* Create the AWS IAM role: EKSDeployerRole to deploy EKS cluster on the project side.
The provided resources will allow to use cross-account deployment by assuming the created EKSDeployerRole from the root AWS account. Take the following steps:

  1. Clone git repo with the [edp-terraform-aws-platform.git](https://github.com/epmd-edp/edp-terraform-aws-platform.git) ism-deployer project, and rename it according to the project name.

    <details>
    <Summary><b> clone project</b></Summary>

        $ git clone https://github.com/epmd-edp/edp-terraform-aws-platform.git
        $ mv edp-terraform-aws-platform edp-terraform-aws-platform-<PROJECT_NAME>
        $ cd edp-terraform-aws-platform-<PROJECT_NAME>/iam-deployer

    </details>

     where:

     * &#8249;PROJECT_NAME&#8250; - is a project name or a unique platform identifier, for example, `shared` or `test-eks`.

   2. Fill in the input variables for Terraform run in the &#8249;iam-deployer/terraform.tfvars&#8250; file. Use the [iam-deployer/template.tfvars](https://github.com/epmd-edp/edp-terraform-aws-platform/blob/main/iam-deployer/template.tfvars) as an example. Please find the detailed description of the variables in the [iam-deployer/variables.tf](https://github.com/epmd-edp/edp-terraform-aws-platform/blob/main/iam-deployer/variables.tf) file.

    <details>
    <Summary><b>terraform.tfvars file example</b></Summary>

        aws_profile = "aws_user"

        region = "eu-central-1"

        tags = {
          "SysName"      = "EKS"
          "SysOwner"     = "owner@example.com"
          "Environment"  = "EKS-TEST-CLUSTER"
          "CostCenter"   = "0000"
          "BusinessUnit" = "BU"
          "Department"   = "DEPARTMENT"
        }

    </details>

   3. Run the `terraform apply` command. Then initialize the backend and apply the changes.

     <details>
     <Summary><b>apply the changes</b></Summary>

        $ terraform init
        $ terraform apply
        ...
        Do you want to perform these actions?
        Terraform will perform the actions described above.
        Only 'yes' will be accepted to approve.

        Enter a value: yes

        aws_iam_role.deployer: Creating...
        aws_iam_role.deployer: Creation complete after 4s [id=EKSDeployerRole]

        Apply complete! Resources: 1 added, 0 changed, 0 destroyed.

        Outputs:

        deployer_iam_role_arn = "arn:aws:iam::012345678910:role/EKSDeployerRole"
        deployer_iam_role_id = "EKSDeployerRole"
        deployer_iam_role_name = "EKSDeployerRole"

    </details>

  4. Commit the local state. At this run, Terraform will use the [local backend](https://www.terraform.io/docs/language/settings/backends/local.html) to store the state on the local filesystem. Terraform locks that state using system APIs and performs operations locally.
  It is not mandatory to store the resulted state file in Git, but this option can be used since the file data is not sensitive. Optionally, commit the state of the s3-backend project.

        $ git add iam-deployer/terraform.tfstate iam-deployer/terraform.tfvars
        $ git commit -m "Terraform state for IAM deployer role"

* Create the AWS IAM role: ServiceRoleForEKS<PROJECT_NAME>WorkerNode to connect to the EKS cluster. Take the following steps:

  0. Use the local state file or the AWS S3 bucket for saving the state file. The AWS S3 bucket creation is described in the [Terraform Backend](https://epam.github.io/edp-install/operator-guide/deploy-aws-eks/#terraform-backend) section.

  1. Go to the folder with the `iam-workernode` role [edp-terraform-aws-platform.git](https://github.com/epmd-edp/edp-terraform-aws-platform.git), and rename it according to the project name.

    <details>
    <Summary><b> go to the iam-workernode folder</b></Summary>

        $ cd edp-terraform-aws-platform-<PROJECT_NAME>/iam-workernode

    </details>

     where:

     * &#8249;PROJECT_NAME&#8250; - is a project name or a unique platform identifier, for example, `shared` or `test-eks`.

  2. Fill in the input variables for Terraform run in the &#8249;iam-workernode/terraform.tfvars&#8250; file, use the [iam-workernode/template.tfvars](https://github.com/epmd-edp/edp-terraform-aws-platform/blob/main/iam-workernode/template.tfvars) as an example. Please find the detailed description of the variables in the [iam-workernode/variables.tf](https://github.com/epmd-edp/edp-terraform-aws-platform/blob/main/iam-workernode/variables.tf) file.

    <details>
    <Summary><b>terraform.tfvars file example</b></Summary>

        role_arn = "arn:aws:iam::012345678910:role/EKSDeployerRole"

        platform_name = "<PROJECT_NAME>"

        iam_permissions_boundary_policy_arn = "arn:aws:iam::012345678910:policy/some_role_boundary"

        region = "eu-central-1"

        tags = {
          "SysName"      = "EKS"
          "SysOwner"     = "owner@example.com"
          "Environment"  = "EKS-TEST-CLUSTER"
          "CostCenter"   = "0000"
          "BusinessUnit" = "BU"
          "Department"   = "DEPARTMENT"
        }

    </details>

  3. Run the `terraform apply` command. Then initialize the backend and apply the changes.

    <details>
    <Summary><b>apply the changes</b></Summary>

        $ terraform init
        $ terraform apply
        ...
        Do you want to perform these actions?
        Terraform will perform the actions described above.
        Only 'yes' will be accepted to approve.

        Enter a value: yes

    </details>

* Create the AWS IAM role: ServiceRoleForEKSShared for the EKS cluster.
Take the following steps:

  1. Create the AWS IAM role: ServiceRoleForEKSShared

  2. Attach the following policies: "AmazonEKSClusterPolicy" and "AmazonEKSServicePolicy"

* Configure AWS profile for deployment from the local node.
Please, refer to the AWS documentation for [detailed guide](https://docs.aws.amazon.com/sdk-for-php/v3/developer-guide/guide_credentials_profiles.html) to configure profiles.

* Create AWS Key pair for EKS cluster nodes access. Please refer to the AWS documentation for [detailed guide](https://docs.aws.amazon.com/AWSEC2/latest/UserGuide/ec2-key-pairs.html#having-ec2-create-your-key-pair) to create a Key pair.

* Create a public Hosted Zone if there is no any to provide for EKS cluster deployment. Please, refer to the AWS documentation for [detailed guide](https://docs.aws.amazon.com/Route53/latest/DeveloperGuide/CreatingHostedZone.html) to create a Hosted zone.


#### Terraform Backend

The Terraform configuration for EKS cluster deployment has a backend block, which defines where and how the operations are performed, and where the state snapshots are stored. Currently, the best practice is to store the state as a given key in a given bucket on Amazon S3.

This backend also supports state locking and consistency checking via Dynamo DB, which can be enabled by setting the `dynamodb_table` field to an existing DynamoDB table name.

In the following configuration a single DynamoDB table can be used to lock multiple remote state files. Terraform generates key names that include the values of the bucket and key variables.

In the [edp-terraform-aws-platform.git](https://github.com/epmd-edp/edp-terraform-aws-platform.git) repo an optional project is provided to create initial resources to start using Terraform from the scratch.

The provided resources will allow to use the following **Terraform options**:

 * to store Terraform states remotely in the Amazon S3 bucket;

 * to manage remote state access with S3 bucket policy;

 * to support state locking and consistency checking via DynamoDB.

After Terraform run the following **AWS resources** will be created:

* S3 bucket: terraform-states-&#8249;AWS_ACCOUNT_ID&#8250;

* S3 bucket policy: terraform-states-&#8249;AWS_ACCOUNT_ID&#8250;

* DynamoDB lock table: terraform_locks

Please, skip this section if you already have the listed resources for further Terraform remote backend usage.

To create the required resources, do the following:

1. Clone git repo with s3-backend project [edp-terraform-aws-platform.git](https://github.com/epmd-edp/edp-terraform-aws-platform.git), rename it in the correspondence with project name.

    <details>
    <Summary><b>clone project</b></Summary>

        $ git clone https://github.com/epmd-edp/edp-terraform-aws-platform.git

        $ mv edp-terraform-aws-platform tedp-terraform-aws-platform-<PROJECT_NAME>

        $ cd edp-terraform-aws-platform-<PROJECT_NAME>/s3-backend
    </details>

  where:

  &#8249;PROJECT_NAME&#8250; - is a project name, a unique platform identifier, e.g. shared, test-eks etc.

 2. Fill the input variables for Terraform run in the &#8249;s3-backend/terraform.tfvars&#8250; file, refer to the [s3-backend/template.tfvars](https://github.com/epmd-edp/edp-terraform-aws-platform/blob/main/s3-backend/template.tfvars) as an example.

    <details>
    <Summary><b>terraform.tfvars file example</b></Summary>

        region = "eu-central-1"

        s3_states_bucket_name = "terraform-states"

        table_name = "terraform_locks"

        tags = {
          "SysName"      = "EKS"
          "SysOwner"     = "owner@example.com"
          "Environment"  = "EKS-TEST-CLUSTER"
          "CostCenter"   = "0000"
          "BusinessUnit" = "BU"
          "Department"   = "DEPARTMENT"
        }

    </details>

  Find the detailed description of the variables in the [s3-backend/variables.tf](https://github.com/epmd-edp/edp-terraform-aws-platform/blob/main/s3-backend/variables.tf) file.

3. Run Terraform apply. Initialize the backend and apply the changes.

    <details>
    <Summary><b>apply the changes</b></Summary>

        $ terraform init
        $ terraform apply
        ...
        Do you want to perform these actions?
        Terraform will perform the actions described above.
        Only 'yes' will be accepted to approve.

        Enter a value: yes

        aws_dynamodb_table.terraform_lock_table: Creating...
        aws_s3_bucket.terraform_states: Creating...
        aws_dynamodb_table.terraform_lock_table: Creation complete after 27s [id=terraform-locks-test]
        aws_s3_bucket.terraform_states: Creation complete after 1m10s [id=terraform-states-test-012345678910]
        aws_s3_bucket_policy.terraform_states: Creating...
        aws_s3_bucket_policy.terraform_states: Creation complete after 1s [id=terraform-states-test-012345678910]

        Apply complete! Resources: 3 added, 0 changed, 0 destroyed.

        Outputs:

        terraform_lock_table_dynamodb_id = "terraform_locks"
        terraform_states_s3_bucket_name = "terraform-states-012345678910"

    </details>

4. Commit the local state. At this run Terraform will use the local backend to store state on the local filesystem, locks that state using system APIs, and performs operations locally.
There is no strong requirements to store the resulted state file in the git, but it's possible at will since there is no sensitive data. On your choice, commit the state of the s3-backend project or not.

        $ git add s3-backend/terraform.tfstate

        $ git commit -m "Terraform state for s3-backend"

  As a result, the projects that run Terraform can use the following definition for remote state configuration:

  <details>
  <Summary><b>providers.tf - terraform backend configuration block</b></Summary>
```
terraform {
  backend "s3" {
    bucket         = "terraform-states-<AWS_ACCOUNT_ID>"
    key            = "<PROJECT_NAME>/<REGION>/terraform/terraform.tfstate"
    region         = "<REGION>"
    acl            = "bucket-owner-full-control"
    dynamodb_table = "terraform_locks"
    encrypt        = true
  }
}
```
  </details>

  where:

  - AWS_ACCOUNT_ID - is AWS account id, e.g. 012345678910,
  - REGION - is AWS region, e.g. eu-central-1,
  - PROJECT_NAME - is a project name, a unique platform identifier, e.g. shared, test-eks etc.

  <details>
  <Summary><b>View: providers.tf - terraform backend configuration example</b></Summary>
```
terraform {
  backend "s3" {
    bucket         = "terraform-states-012345678910"
    key            = "test-eks/eu-central-1/terraform/terraform.tfstate"
    region         = "eu-central-1"
    acl            = "bucket-owner-full-control"
    dynamodb_table = "terraform_locks"
    encrypt        = true
  }
}
```
  </details>

!!! note
    At the moment, it is recommended to use common s3 bucket and Dynamo DB in the root EDP account both for Shared and Standalone clusters deployment.


## Deploy EKS Cluster

To deploy the EKS cluster, make sure that all the above-mentioned Prerequisites are ready to be used.

#### EKS Cluster Deployment with Terraform

1. Clone git repo with the Terraform project for EKS infrastructure [edp-terraform-aws-platform.git](https://github.com/epmd-edp/edp-terraform-aws-platform.git) and rename it in the correspondence with project name if not yet.

    <details>
    <Summary><b>clone project</b></Summary>

        $ git clone https://github.com/epmd-edp/edp-terraform-aws-platform.git
        $ mv edp-terraform-aws-platform edp-terraform-aws-platform-<PROJECT_NAME>
        $ cd edp-terraform-aws-platform-<PROJECT_NAME>
    </details>

  where:

  * &#8249;PROJECT_NAME&#8250; - is a project name, a unique platform identifier, e.g. shared, test-eks etc.

2. Configure [Terraform backend](https://www.terraform.io/docs/language/settings/backends/configuration.html) according to your project needs or use instructions from the Configure Terraform backend section.

3. Fill the input variables for Terraform run in the &#8249;terraform.tfvars&#8250; file, refer to the [template.tfvars](https://github.com/epmd-edp/edp-terraform-aws-platform/blob/main/template.tfvars) file and apply the changes. See details below.
Be sure to put the correct values of the variables created in the Prerequisites section. Find the detailed description of the variables in the [variables.tf](https://github.com/epmd-edp/edp-terraform-aws-platform/blob/main/variables.tf) file.

  !!! warning
      Please, do not use upper case in the input variables. It can lead to unexpected issues.

   <details>
   <Summary><b>template.tfvars file template</b></Summary>

````
# Check out all the inputs based on the comments below and fill the gaps instead <...>
  # More details on each variable can be found in the variables.tf file

  create_elb = true # set to true if you'd like to create ELB for Gerrit usage

  region   = "<REGION>"
  role_arn = "<ROLE_ARN>"

  platform_name        = "<PLATFORM_NAME>"        # the name of the cluster and AWS resources
  platform_domain_name = "<PLATFORM_DOMAIN_NAME>" # must be created as a prerequisite

  # The following will be created or used existing depending on the create_vpc value
  subnet_azs    = ["<SUBNET_AZS1>", "<SUBNET_AZS2>"]
  platform_cidr = "<PLATFORM_CIDR>"
  private_cidrs = ["<PRIVATE_CIDRS1>", "<PRIVATE_CIDRS2>"]
  public_cidrs  = ["<PUBLIC_CIDRS1>", "<PUBLIC_CIDRS2>"]

  infrastructure_public_security_group_ids = [
    "<INFRASTRUCTURE_PUBLIC_SECURITY_GROUP_IDS1>",
    "<INFRASTRUCTURE_PUBLIC_SECURITY_GROUP_IDS2>",
  ]

  ssl_policy = "<SSL_POLICY>"

  # EKS cluster configuration
  cluster_version = "1.22"
  key_name        = "<AWS_KEY_PAIR_NAME>" # must be created as a prerequisite
  enable_irsa     = true

  cluster_iam_role_name            = "<SERVICE_ROLE_FOR_EKS>"
  worker_iam_instance_profile_name = "<SERVICE_ROLE_FOR_EKS_WORKER_NODE"

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

  map_users = [
    {
      "userarn" : "<IAM_USER_ARN1>",
      "username" : "<IAM_USER_NAME1>",
      "groups" : ["system:masters"]
    },
    {
      "userarn" : "<IAM_USER_ARN2>",
      "username" : "<IAM_USER_NAME2>",
      "groups" : ["system:masters"]
    }
  ]

  map_roles = [
    {
      "rolearn" : "<IAM_ROLE_ARN1>",
      "username" : "<IAM_ROLE_NAME1>",
      "groups" : ["system:masters"]
    },
  ]

  tags = {
    "SysName"      = "<SYS_NAME>"
    "SysOwner"     = "<SYSTEM_OWNER>"
    "Environment"  = "<ENVIRONMENT>"
    "CostCenter"   = "<COST_CENTER>"
    "BusinessUnit" = "<BUSINESS_UNIT>"
    "Department"   = "<DEPARTMENT>"
    "user:tag"     = "<PLATFORM_NAME>"
  }

  # Variables for demand pool
  demand_instance_types      = ["r5.large"]
  demand_max_nodes_count     = 0
  demand_min_nodes_count     = 0
  demand_desired_nodes_count = 0

  // Variables for spot pool
  spot_instance_types      = ["r5.xlarge", "r5.large", "r4.large"] # need to ensure we use nodes with more memory
  spot_max_nodes_count     = 2
  spot_desired_nodes_count = 2
  spot_min_nodes_count     = 2

````

   </details>

  !!! note
      The file above is an example. Please find the latest version in the project repo in the [terraform.tfvars](https://github.com/epmd-edp/edp-terraform-aws-platform/blob/main/template.tfvars) file.

  **There are the following possible scenarios to deploy the EKS cluster:**

  <details>
  <Summary><b>Case 1: Create new VPC and deploy the EKS cluster, terraform.tfvars file example</b></Summary>

```
create_elb     = true # set to true if you'd like to create ELB for Gerrit usage

region   = "eu-central-1"
role_arn = "arn:aws:iam::012345678910:role/EKSDeployerRole"

platform_name        = "test-eks"
platform_domain_name = "example.com" # must be created as a prerequisite

# The following will be created or used existing depending on the create_vpc value
subnet_azs    = ["eu-central-1a", "eu-central-1b"]
platform_cidr = "172.31.0.0/16"
private_cidrs = ["172.31.0.0/20", "172.31.16.0/20"]
public_cidrs  = ["172.31.32.0/20", "172.31.48.0/20"]

# Use this parameter the second time you apply the code to specify new AWS Security Groups
infrastructure_public_security_group_ids = [
  #  "sg-00000000000000000",
  #  "sg-00000000000000000",
]

# EKS cluster configuration
cluster_version = "1.22"
key_name        = "test-kn" # must be created as a prerequisite
enable_irsa     = true

# Define if IAM roles should be created during the deployment or used existing ones
cluster_iam_role_name            = "ServiceRoleForEKSShared"
worker_iam_instance_profile_name = "ServiceRoleForEksSharedWorkerNode0000000000000000000000"

add_userdata = <<EOF
export TOKEN=$(aws ssm get-parameter --name edprobot --query 'Parameter.Value' --region eu-central-1 --output text)
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

map_users = [
  {
    "userarn" : "arn:aws:iam::012345678910:user/user_name1@example.com",
    "username" : "user_name1@example.com",
    "groups" : ["system:masters"]
  },
  {
    "userarn" : "arn:aws:iam::012345678910:user/user_name2@example.com",
    "username" : "user_name2@example.com",
    "groups" : ["system:masters"]
  }
]

map_roles = [
  {
    "rolearn" : "arn:aws:iam::012345678910:role/EKSClusterAdminRole",
    "username" : "eksadminrole",
    "groups" : ["system:masters"]
  },
]

tags = {
  "SysName"      = "EKS"
  "SysOwner"     = "owner@example.com"
  "Environment"  = "EKS-TEST-CLUSTER"
  "CostCenter"   = "2020"
  "BusinessUnit" = "BU"
  "Department"   = "DEPARTMENT"
  "user:tag"     = "test-eks"
}

# Variables for spot pool
spot_instance_types      = ["r5.large", "r4.large"] # need to ensure we use nodes with more memory
spot_max_nodes_count     = 1
spot_desired_nodes_count = 1
spot_min_nodes_count     = 1
```
  </details>

4. Run Terraform apply. Initialize the backend and apply the changes.

  <details>
  <Summary><b>apply the changes</b></Summary>

````
   $ terraform init
   $ terraform apply
   ...

   Do you want to perform these actions?
   Terraform will perform the actions described above.
   Only 'yes' will be accepted to approve.
   Enter a value: yes
   ...

````
  </details>

#### Check EKS cluster deployment

As a result, the &#8249;PLATFORM_NAME&#8250; EKS cluster is deployed to the specified AWS account.

Make sure you have all required tools listed in the Install required tools section.

To connect to the cluster find the **kubeconfig**_<PLATFORM_NAME> file in the project folder which is output of the last Terraform apply run. Move it to the **~/.kube/** folder.

        $ mv kubeconfig_<PLATFORM_NAME> ~/.kube/

Run the following commands to ensure the EKS cluster is up and has required nodes count:

        $ kubectl config get-contexts
        $ kubectl get nodes


!!! note
    If the there are any authorisation issues, make sure the users section in the kubeconfig_<PLATFORM_NAME> file has all required parameters based on you AWS CLI version.
    Find more details in the [create kubeconfig](https://docs.aws.amazon.com/eks/latest/userguide/create-kubeconfig.html#create-kubeconfig-manually) AWS user guide. And pay attention on the kubeconfig_aws_authenticator terraform input variables.

Optionally, a [Lens](https://k8slens.dev/) tool can be installed and used for further work with Kubernetes cluster. Refer to the [original documentation](https://docs.k8slens.dev/main/) to add and process the cluster.
