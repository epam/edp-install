# Deploy OKD 4.9 Cluster

This instruction provides detailed information on the OKD 4.9 cluster deployment in the AWS Cloud and contains the additional setup necessary for the managed infrastructure.

A full description of the cluster deployment can be found in the [official documentation](https://docs.okd.io/4.9/installing/installing_aws/installing-aws-customizations.html).

## Prerequisites

Before the OKD cluster deployment and configuration, make sure to check the prerequisites.

### Required Tools

1. Install the following tools listed below:

   * [AWS CLI](https://docs.aws.amazon.com/cli/latest/userguide/cli-chap-install.html)
   * [OpenShift CLI](https://docs.openshift.com/container-platform/4.9/cli_reference/openshift_cli/getting-started-cli.html)

2. Create the AWS IAM user with [the required permissions](https://docs.okd.io/4.9/installing/installing_aws/installing-aws-account.html#installation-aws-permissions_installing-aws-account). Make sure the AWS account is active, and the user doesn't have a permission boundary. Remove any Service Control Policy (SCP) restrictions from the AWS account.

3. Generate a key pair for cluster node SSH access. Please perform the steps below:
   * Generate the SSH key. Specify the path and file name, such as ~/.ssh/id_ed25519, of the new SSH key. If there is an existing key pair, ensure that the public key is in the ~/.ssh directory.

         ssh-keygen -t ed25519 -N '' -f <path>/<file_name>

   * Add the SSH private key identity to the SSH agent for a local user if it has not already been added.

         eval "$(ssh-agent -s)"

   * Add the SSH private key to the ssh-agent:

         ssh-add <path>/<file_name>

## Prepare for the Deployment Process

Before deploying the OKD cluster, please perform the steps below:

1. Download the [OKD installer](https://github.com/openshift/okd/releases/tag/4.9.0-0.okd-2022-02-12-140851).

2. Extract the installation program:

      tar -xvf openshift-install-linux.tar.gz

3. Download the installation pull secret for any private registry.

    This pull secret allows to authenticate with the services that are provided by the included authorities, including [Quay.io](https://quay.io/) serving container images for OKD components. For example, here is a pull secret for Docker Hub:

   <details>
      <Summary><b>The pull secret for the private registry</b></Summary>
    ```
    {
      "auths":{
        "https://index.docker.io/v1/":{
          "auth":"$TOKEN"
        }
      }
    }
    ```
   </details>

4. Create the deployment directory and the **install-config.yaml** file:

      mkdir okd-deployment
      touch okd-deployment/install-config.yaml

    To specify more details about the OKD cluster platform or to modify the values of the required parameters, customize the **install-config.yaml** file for AWS. Please see an example of the customized file below:

   <details>
      <Summary><b>install-config.yaml - OKD cluster’s platform installation configuration file</b></Summary>
    ```
    apiVersion: v1
    baseDomain: <YOUR_DOMAIN>
    compute:
    - architecture: amd64
      hyperthreading: Enabled
      name: worker
      platform:
        aws:
          zones:
            - eu-central-1a
          rootVolume:
            size: 50
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
      name: 4-9-okd-sandbox
    platform:
      aws:
        region: eu-central-1
        userTags:
          user:tag: 4-9-okd-sandbox
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

## Deploy the Cluster

To initialize the cluster deployment, run the following command:

    ./openshift-install create cluster --dir <installation_directory> --log-level=info

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
    The Ignition config files contain certificates that expire after 24 hours, which are then renewed at that time. Do not turn off the cluster for this time, or you will have to update the certificates manually. See [OpenShift Container Platform documentation](https://docs.openshift.com/container-platform/4.9/installing/installing_aws/installing-aws-customizations.html#installation-launching-installer_installing-aws-customizations) for more information.

## Log Into the Cluster

To log into the cluster, export the `kubeconfig`:

      export KUBECONFIG=<installation_directory>/auth/kubeconfig

## Related Articles

* [Deploy AWS EKS Cluster](deploy-aws-eks.md)
* [Deploy OKD 4.10 Cluster](deploy-okd-4.10.md)