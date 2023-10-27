# Deploy OKD 4.10 Cluster

This instruction provides detailed information on the OKD 4.10 cluster deployment in the AWS Cloud and contains the additional setup necessary for the managed infrastructure.

A full description of the cluster deployment can be found in the [official documentation](https://docs.openshift.com/container-platform/4.10/authentication/managing_cloud_provider_credentials/cco-mode-sts.html).

## Prerequisites

Before the OKD cluster deployment and configuration, make sure to check the prerequisites.

### Required Tools

1. Install the following tools listed below:

   * [AWS CLI](https://docs.aws.amazon.com/cli/latest/userguide/cli-chap-install.html)
   * [OpenShift CLI](https://docs.openshift.com/container-platform/4.10/cli_reference/openshift_cli/getting-started-cli.html)
   * [Lens](https://k8slens.dev/) (optional)

2. Create the AWS IAM user with [the required permissions](https://docs.okd.io/4.9/installing/installing_aws/installing-aws-account.html#installation-aws-permissions_installing-aws-account). Make sure the AWS account is active, and the user doesn't have a permission boundary. Remove any Service Control Policy (SCP) restrictions from the AWS account.

3. Generate a key pair for cluster node SSH access. Please perform the steps below:
   * Generate the SSH key. Specify the path and file name, such as ~/.ssh/id_ed25519, of the new SSH key. If there is an existing key pair, ensure that the public key is in the ~/.ssh directory.

      ```bash
      ssh-keygen -t ed25519 -N '' -f <path>/<file_name>
      ```

   * Add the SSH private key identity to the SSH agent for a local user if it has not already been added.

      ```bash
      eval "$(ssh-agent -s)"
      ```

   * Add the SSH private key to the ssh-agent:

      ```bash
      ssh-add <path>/<file_name>
      ```

4. Build the `ccoctl` tool:
   * Clone the `cloud-credential-operator` repository.

      ```bash
      git clone https://github.com/openshift/cloud-credential-operator.git
      ```

   * Move to the `cloud-credential-operator` folder and build the `ccoctl` tool.

      ```bash
      cd cloud-credential-operator && git checkout release-4.10
      GO_PACKAGE='github.com/openshift/cloud-credential-operator'
      go build -ldflags "-X $GO_PACKAGE/pkg/version.versionFromGit=$(git describe --long --tags --abbrev=7 --match 'v[0-9]*')" ./cmd/ccoctl
      ```

## Prepare for the Deployment Process

Before deploying the OKD cluster, please perform the steps below:

### Create AWS Resources

Create the AWS resources with the Cloud Credential Operator utility (the `ccoctl` tool):

1. Generate the public and private RSA key files that are used to set up the OpenID Connect identity provider for the cluster:

    ```bash
    ./ccoctl aws create-key-pair
    ```

2. Create an OpenID Connect identity provider and an S3 bucket on AWS:

    ```bash
    ./ccoctl aws create-identity-provider \
    --name=<NAME> \
    --region=<AWS_REGION> \
    --public-key-file=./serviceaccount-signer.public
    ```

   where:

   * NAME - is the name used to tag any cloud resources created for tracking,
   * AWS_REGION - is the AWS region in which cloud resources will be created.

3. Create the IAM roles for each component in the cluster:

   * Extract the list of the `CredentialsRequest` objects from the OpenShift Container Platform release image:

      ```bash
      oc adm release extract \
      --credentials-requests \
      --cloud=aws \
      --to=./credrequests \
      --quay.io/openshift-release-dev/ocp-release:4.10.25-x86_64
      ```

    !!! note
        A version of the openshift-release-dev docker image can be found in the [Quay registry](https://quay.io/repository/openshift-release-dev/ocp-release?tab=tags).

   * Use the `ccoctl` tool to process all `CredentialsRequest` objects in the `credrequests` directory:

      ```bash
      ccoctl aws create-iam-roles \
      --name=<NAME> \
      --region=<AWS_REGION> \
      --credentials-requests-dir=./credrequests
      --identity-provider-arn=arn:aws:iam::<AWS_ACCOUNT_ID>:oidc-provider/<NAME>-oidc.s3.<AWS_REGION>.amazonaws.com
      ```

### Create OKD Manifests

Before deploying the OKD cluster, please perform the steps below:

1. Download the [OKD installer](https://github.com/openshift/okd/releases/tag/4.10.0-0.okd-2022-07-09-073606).

2. Extract the installation program:

      tar -xvf openshift-install-linux.tar.gz

3. Download the installation pull secret for any private registry. This pull secret allows to authenticate with the services that are provided by the authorities, including [Quay.io](https://quay.io/), serving the container images for OKD components. For example, here is a pull secret for Docker Hub:

   <details>
      <Summary><b>The pull secret for the private registry</b></Summary>
    ```json
    {
      "auths":{
        "https://index.docker.io/v1/":{
          "auth":"$TOKEN"
        }
      }
    }
    ```
   </details>

4. Create a deployment directory and the **install-config.yaml** file:

   ```bash
   mkdir okd-deployment
   touch okd-deployment/install-config.yaml
   ```

    To specify more details about the OKD cluster platform or to modify the values of the required parameters, customize the **install-config.yaml** file for the AWS. Please see below an example of the customized file:

   <details>
      <Summary><b>install-config.yaml - OKD cluster’s platform installation configuration file</b></Summary>
    ```yaml
    apiVersion: v1
    baseDomain: <YOUR_DOMAIN>
    credentialsMode: Manual
    compute:
    - architecture: amd64
      hyperthreading: Enabled
      name: worker
      platform:
        aws:
          rootVolume:
            size: 30
          zones:
            - eu-central-1a
          type: r5.large
      replicas: 3
    controlPlane:
      architecture: amd64
      hyperthreading: Enabled
      name: master
      platform:
        aws:
          rootVolume:
            size: 50
          zones:
            - eu-central-1a
          type: m5.xlarge
      replicas: 3
    metadata:
      creationTimestamp: null
      name: 4-10-okd-sandbox
    networking:
      clusterNetwork:
      - cidr: 10.128.0.0/14
        hostPrefix: 23
      machineNetwork:
      - cidr: 10.0.0.0/16
      networkType: OVNKubernetes
      serviceNetwork:
      - 172.30.0.0/16
    platform:
      aws:
        region: eu-central-1
        userTags:
          user:tag: 4-10-okd-sandbox
    publish: External
    pullSecret: <PULL_SECRET>
    sshKey: |
      <SSH_KEY>
    ```
   </details>

    where:

   * YOUR_DOMAIN - is a base domain,
   * PULL_SECRET - is a created pull secret for a private registry,
   * SSH_KEY - is a created SSH key.

5. Create the required OpenShift Container Platform installation manifests:

   ```bash
   ./openshift-install create manifests --dir okd-deployment
   ```

6. Copy the manifests generated by the `ccoctl` tool to the `manifests` directory created by the installation program:

   ```bash
   cp ./manifests/* ./okd-deployment/manifests/
   ```

7. Copy the private key generated in the `tls` directory by the `ccoctl` tool to the installation directory:

   ```bash
   cp -a ./tls ./okd-deployment
   ```

## Deploy the Cluster

To initialize the cluster deployment, run the following command:

   ```bash
   ./openshift-install create cluster --dir okd-deployment --log-level=info
   ```

!!! note
    If the cloud provider account configured on the host does not have sufficient permissions to deploy the cluster, the installation process stops, and the missing permissions are displayed.

When the cluster deployment is completed, directions for accessing the cluster are displayed in the terminal, including a link to the web console and credentials for the **kubeadmin** user. The `kubeconfig` for the cluster will be located in **okd-deployment/auth/kubeconfig**.

  <details>
  <Summary><b>Example output</b></Summary>
```
...
INFO Install complete!
INFO To access the cluster as the system:admin user when using 'oc', run 'export KUBECONFIG=/home/myuser/install_dir/auth/kubeconfig'
INFO Access the OpenShift web-console here: https://console-openshift-console.apps.mycluster.example.com
INFO Login to the console with the user: "kubeadmin", and password: "4vYBz-Ee6gm-ymBZj-Wt5AL"
INFO Time elapsed: 36m22s:
```
  </details>

!!! warning
    The Ignition config files contain certificates that expire after 24 hours, which are then renewed at that time. Do not turn off the cluster for this time, or you will have to update the certificates manually. See [OpenShift Container Platform documentation](https://docs.openshift.com/container-platform/4.10/installing/installing_aws/installing-aws-customizations.html#installation-launching-installer_installing-aws-customizations) for more information.

## Log Into the Cluster

To log into the cluster, export the `kubeconfig`:

      export KUBECONFIG=<installation_directory>/auth/kubeconfig

Optionally, use the [Lens](https://k8slens.dev/) tool for further work with the Kubernetes cluster.

!!! note
    To install and manage the cluster, refer to [Lens documentation](https://docs.k8slens.dev/main/).

## Manage OKD Cluster Without the Inbound Rules

In order to manage the OKD cluster without the `0.0.0.0/0` inbound rules, please perform the steps below:

1. Create a Security Group with a list of your external IPs:

   ```bash
   aws ec2 create-security-group --group-name <SECURITY_GROUP_NAME> --description "<DESCRIPTION_OF_SECURITY_GROUP>" --vpc-id <VPC_ID>
   aws ec2 authorize-security-group-ingress \
   --group-id '<SECURITY_GROUP_ID>' \
   --ip-permissions 'IpProtocol=all,PrefixListIds=[{PrefixListId=<PREFIX_LIST_ID>}]'
   ```

2. Manually attach this new Security Group to all master nodes of the cluster.

3. Create another Security Group with an Elastic IP of the Cluster VPC:

   ```bash
   aws ec2 create-security-group --group-name custom-okd-4-10 --description "Cluster Ip to 80, 443" --vpc-id <VPC_ID>
   aws ec2 authorize-security-group-ingress \
    --group-id '<SECURITY_GROUP_ID>' \
    --protocol all \
    --port 80 \
    --cidr <ELASTIC_IP_OF_CLUSTER_VPC>
   aws ec2 authorize-security-group-ingress \
    --group-id '<SECURITY_GROUP_ID>' \
    --protocol all \
    --port 443 \
    --cidr <ELASTIC_IP_OF_CLUSTER_VPC>
   ```

4. Modify the cluster load balancer via the `router-default` svc in the `openshift-ingress` namespace, paste two Security Groups created on previous steps:

   <details>
      <Summary><b>The pull secret for the private registry</b></Summary>
    ```
    apiVersion: v1
    kind: Service
    metadata:
      name: router-default
      namespace: openshift-ingress
      annotations:
        service.beta.kubernetes.io/aws-load-balancer-additional-resource-tags: "tag_name=some_value"
        service.beta.kubernetes.io/aws-load-balancer-security-groups: "<SECURITY_GROUP_IDs>"
        ...
    ```
   </details>

## Optimize Spot Instances Usage

In order to optimize the usage of Spot Instances on the AWS, add the following line under the `providerSpec` field in the MachineSet of Worker Nodes:

```yaml
providerSpec:
  value:
    spotMarketOptions: {}
```

## Related Articles

* [Deploy AWS EKS Cluster](deploy-aws-eks.md)
* [Associate IAM Roles With Service Accounts](enable-irsa.md)
* [Deploy OKD 4.9 Cluster](deploy-okd.md)
