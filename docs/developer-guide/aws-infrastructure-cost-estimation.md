# AWS Infrastructure Cost Estimation

Effective planning and budgeting are essential for developing applications in cloud computing, with a key part being accurate infrastructure cost estimation. This not only helps in keeping within budget but also enables informed decision-making and resource optimization for project viability.

This guide aims to offer an in-depth look at the factors affecting AWS infrastructure costs for KubeRocketCI and includes analytics and tools for cost estimation.

## Platform Components and Approximate Costs

This section contains tables outlining the key components of our AWS infrastructure, including a brief description of each component's role, its purpose within our infrastructure, and an estimate of its monthly cost.

!!! note
    The costs mentioned below are estimates. For the most accurate and up-to-date pricing, please refer to the [AWS official documentation](productNameLowercase&aws-products-pricing.sort-order=asc&awsf.Free%20Tier%20Type=*all&awsf.tech-category=*all).

The table below outlines key AWS infrastructure components for KubeRocketCI, detailing each component's role, purpose, and estimated monthly cost:

| Component                                     | Description                                                                             | Purpose Within Infrastructure                                                          |
|-----------------------------------------------|-----------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------|
| Application Load Balancer (ALB)               | Distributes incoming application traffic across multiple targets.                       | Ensures high availability and fault tolerance for our applications.                    |
| Virtual Private Cloud (VPC)                   | Provides an isolated section of the AWS cloud where resources can be launched.          | Segregates our infrastructure for enhanced security and management.                    |
| 3x Network Address Translation (NAT) Gateways | Enables instances in a private subnet to connect to the internet or other AWS services. | Provides internet access to EC2 instances without exposing them to the public internet. |
| Elastic Container Registry (ECR)              | A fully managed container registry.                                                     | Stores, manages, and deploys container images.                                   |
| Elastic Kubernetes Service (EKS)              | A managed Kubernetes service.                                                           | Simplifies running Kubernetes applications on AWS.                                     |
| Elastic Block Store (EBS)                     | Provides persistent block storage volumes for use with EC2 instances.                   | Offers highly available and durable storage for our applications.                      |
| Elastic Compute Cloud (EC2)                   | Provides scalable computing capacity.                                                   | Hosts our applications, supporting varied compute workloads.                           |

The table below presents an itemized estimate of monthly costs for KubeRocketCI's AWS infrastructure components, including ALB, VPC, EC2, and more:

| Component                                                                                               | Approximate Monthly Cost       |
|---------------------------------------------------------------------------------------------------------|--------------------------------|
| Application Load Balancer (ALB)                                                                         | **$30.00**                     |
| Virtual Private Cloud (VPC)<br> - 3x Network Address Translation Gateways <br> - 3x Public IPv4 Address | <br>**$113.88**<br>**$10.95**  |
| Elastic Container Registry (ECR)                                                                        | **$5.00**                      |
| Elastic Kubernetes Service (EKS)<br>  - 1x EKS Clusters                                                 | <br>**$73.00**                 |
| Elastic Block Store (EBS)                                                                               | **$14.28**                     |
| Elastic Compute Cloud (EC2)<br> - 2x c5.2xlarge (Spot) <br> - 2x c5.2xlarge (On-Demand)                 | <br>**$219.11**<br>**$576.00** |

## AWS Pricing Calculator

To further assist in your planning and budgeting efforts, we have pre-configured the AWS Pricing Calculator with inputs matching our infrastructure setup. This tool allows you to explore and adjust the cost estimation based on your specific needs, giving you a personalized overview of potential expenses.

Access the AWS Pricing Calculator with our pre-configured setup here: [AWS Pricing Calculator](https://calculator.aws/#/estimate?id=42ed1a892c891ebcd905734b437f722122983f61)

## Related Articles

* [EDP Deployment on AWS](aws-deployment-diagram.md)
* [EDP Reference Architecture on AWS](aws-reference-architecture.md)
