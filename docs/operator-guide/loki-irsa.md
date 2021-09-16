# IAM Roles For Service Accounts for Loki

!!! note
    Make sure that IRSA is enabled and [amazon-eks-pod-identity-webhook](https://github.com/aws/amazon-eks-pod-identity-webhook/tree/master#amazon-eks-pod-identity-webhook) is deployed according to the [Associate IAM Roles With Service Accounts](./enable-irsa.md) documentation.

It is possible to use Amazon Simple Storage Service [Amazon S3](https://aws.amazon.com/s3/) as object storage for Loki.
In this case [Loki](https://grafana.com/docs/loki/latest/configuration/examples/#aws) requires access to AWS resources. Follow the steps below to create a required role:

1. Create AWS IAM Policy "AWSIRSA&#8249;CLUSTER_NAME&#8250;&#8249;LOKI_NAMESPACE&#8250;Loki_policy":


      {
          "Version": "2012-10-17",
          "Statement": [
              {
                  "Effect": "Allow",
                  "Action": [
                      "s3:ListObjects",
                      "s3:ListBucket",
                      "s3:PutObject",
                      "s3:GetObject",
                      "s3:DeleteObject"
                  ],
                  "Resource": [
                      "arn:aws:s3:::loki-*"
                  ]
              },
              {
                  "Effect": "Allow",
                  "Action": [
                      "s3:ListBucket"
                  ],
                  "Resource": [
                      "arn:aws:s3:::loki-*"
                  ]
              }
          ]
      }

2. Create AWS IAM Role "AWSIRSA&#8249;CLUSTER_NAME&#8250;&#8249;LOKI_NAMESPACE&#8250;Loki" with trust relationships:


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
                "<OIDC_PROVIDER>:sub": "system:serviceaccount:<LOKI_NAMESPACE>:edp-loki"
             }
           }
         }
       ]
      }


3. Attach the "AWSIRSA&#8249;CLUSTER_NAME&#8250;&#8249;LOKI_NAMESPACE&#8250;Loki_policy" policy to the "AWSIRSA&#8249;CLUSTER_NAME&#8250;&#8249;LOKI_NAMESPACE&#8250;Loki" role.

4. Make sure that [Amazon S3](https://aws.amazon.com/s3/) bucket with name loki-&#8249;CLUSTER_NAME&#8250; exists.

5. Create a service account:


      apiVersion: v1
      kind: ServiceAccount
      metadata:
        name: edp-loki
        namespace: <LOKI_NAMESPACE>
        annotations:
          eks.amazonaws.com/role-arn: "arn:aws:iam::<AWS_ACCOUNT_ID>:role/AWSIRSA‹CLUSTER_NAME›‹LOKI_NAMESPACE›Loki"

6. Add the **service account name** value into the **serviceAccount.name** parameter in *values.yaml* during the [Loki Installation](./install-loki.md#installation).
