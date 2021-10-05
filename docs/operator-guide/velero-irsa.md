# IAM Roles for Velero Service Accounts

!!! note
    Make sure that IRSA is enabled and [amazon-eks-pod-identity-webhook](https://github.com/aws/amazon-eks-pod-identity-webhook/tree/master) is deployed according to the [Associate IAM Roles With Service Accounts](./enable-irsa.md) documentation.

[Velero AWS plugin](https://github.com/vmware-tanzu/velero-plugin-for-aws) requires access to AWS resources. Follow the steps below to create a required role:

1. Create AWS IAM Policy "AWSIRSA&#8249;CLUSTER_NAME&#8250;&#8249;VELERO_NAMESPACE&#8250;Velero_policy":


      {
          "Version": "2012-10-17",
          "Statement": [
              {
                  "Effect": "Allow",
                  "Action": [
                      "ec2:DescribeVolumes",
                      "ec2:DescribeSnapshots",
                      "ec2:CreateTags",
                      "ec2:CreateVolume",
                      "ec2:CreateSnapshot",
                      "ec2:DeleteSnapshot"
                  ],
                  "Resource": "*"
              },
              {
                  "Effect": "Allow",
                  "Action": [
                      "s3:GetObject",
                      "s3:DeleteObject",
                      "s3:PutObject",
                      "s3:AbortMultipartUpload",
                      "s3:ListMultipartUploadParts"
                  ],
                  "Resource": [
                      "arn:aws:s3:::velero-*/*"
                  ]
              },
              {
                  "Effect": "Allow",
                  "Action": [
                      "s3:ListBucket"
                  ],
                  "Resource": [
                      "arn:aws:s3:::velero-*"
                  ]
              }
          ]
      }

2. Create AWS IAM Role "AWSIRSA&#8249;CLUSTER_NAME&#8250;&#8249;VELERO_NAMESPACE&#8250;Velero" with trust relationships:


      {
        "Version": "2012-10-17",
        "Statement": [
          {
            "Effect": "Allow",
            "Principal": {
              "Federated": "arn:aws:iam::<AWS_ACCOUNT_ID>:oidc-provider/<OIDC_PROVIDER>"
            },
            "Action": "sts:AssumeRoleWithWebIdentity",
            "Condition": {
              "StringEquals": {
                "<OIDC_PROVIDER>:sub": "system:serviceaccount:<VELERO_NAMESPACE>:edp-velero"
             }
           }
         }
       ]
      }


3. Attach the "AWSIRSA&#8249;CLUSTER_NAME&#8250;&#8249;VELERO_NAMESPACE&#8250;Velero_policy" policy to the "AWSIRSA&#8249;CLUSTER_NAME&#8250;&#8249;VELERO_NAMESPACE&#8250;Velero" role.

4. Make sure that [Amazon S3](https://aws.amazon.com/s3/) bucket with name velero-&#8249;CLUSTER_NAME&#8250; exists.

5. Provide key value **eks.amazonaws.com/role-arn: "arn:aws:iam::<AWS_ACCOUNT_ID>:role/AWSIRSA‹CLUSTER_NAME›‹VELERO_NAMESPACE›Velero"** into the **serviceAccount.server.annotations** parameter in *values.yaml* during the [Velero Installation](./install-velero.md#installation).

## Related Articles

* [Associate IAM Roles With Service Accounts](../operator-guide/enable-irsa.md)
* [Install Velero](../operator-guide/install-velero.md)