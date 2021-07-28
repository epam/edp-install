### IAM Roles With Service Accounts (IRSA) for Kaniko
_**NOTE:** The information below is relevant in case ECR is used as Docker container registry._

The "build-image-kaniko" stage manages ECR through IRSA that should be available on the cluster. For details on how to associate IAM roles with service account, please refer to the [Associate IAM Roles With Service Accounts](https://github.com/epam/edp-admin-console/blob/master/documentation/enable_irsa.md#associate-iam-roles-with-service-accounts) page.
Follow the steps below to create a required role:
* Create AWS IAM Policy "AWSIRSA<CLUSTER_NAME><EDP_NAMESPACE>Kaniko_policy":
```json
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Effect": "Allow",
            "Action": [
                "ecr:*",
                "cloudtrail:LookupEvents"
            ],
            "Resource": "arn:aws:ecr:eu-central-1:<AWS_ACCOUNT_ID>:repository/<EDP_NAMESPACE>/*"
        },
        {
            "Effect": "Allow",
            "Action": "ecr:GetAuthorizationToken",
            "Resource": "*"
        }
    ]
}
```
* Create AWS IAM Role "AWSIRSA<CLUSTER_NAME><EDP_NAMESPACE>Kaniko" with trust relationships:
```json
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
          "<OIDC_PROVIDER>:sub": "system:serviceaccount:<EDP_NAMESPACE>:edp-kaniko"
       }
     }
   }
 ]
}
```
* Attach the "AWSIRSA<CLUSTER_NAME><EDP_NAMESPACE>Kaniko_policy" policy to the "AWSIRSA<CLUSTER_NAME><EDP_NAMESPACE>Kaniko" role.
* Define the resulted **arn** role value into the **kanikoRoleArn** parameter during the installation.
