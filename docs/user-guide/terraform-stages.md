# CI Pipelines for Terraform

EPAM Delivery Platform ensures the implemented Terraform support by adding a separate component type called **Infrastructure**. The **Infrastructure** codebase type allows to work with Terraform code that is processed by means of stages in the **Code-Review** and **Build** pipelines.

## Pipeline Stages for Terraform

Under the hood, Infrastructure codebase type, namely Terraform, looks quite similar to other codebase types. The distinguishing characterstic of the Infrastructure codebase type is that there is a stage called **terraform-check** in both of **Code Review** and **Build** pipelines. This stage runs the pre-commit activities which in their turn run the following commands and tools:

1. [Terraform fmt](https://developer.hashicorp.com/terraform/cli/commands/fmt) - the first step of the stage is basically the `terraform fmt` command. The `terraform fmt` command automatically updates the formatting of Terraform configuration files to follow the standard conventions and make the code more readable and consistent.

2. [Lock provider versions](https://developer.hashicorp.com/terraform/tutorials/configuration-language/provider-versioning) - locks the versions of the Terraform providers used in the project. This ensures that the project uses specific versions of the providers and prevents unexpected changes from impacting the infrastructure due to newer provider versions.

3. [Terraform validate](https://developer.hashicorp.com/terraform/cli/commands/validate) - checks the syntax and validity of the Terraform configuration files. It scans the configuration files for all possible issues.

4. [Terraform docs](https://github.com/terraform-docs/terraform-docs) - generates human-readable documentation for the Terraform project.

5. [Tflint](https://github.com/terraform-linters/tflint) - additional validation step using the `tflint` linter to provide more in-depth checks in addition to what the `terraform validate` command does.

6. [Checkov](https://github.com/bridgecrewio/checkov) - runs the `checkov` command against the Terraform codebase to identify any security misconfigurations or compliance issues.

7. [Tfsec](https://github.com/aquasecurity/tfsec) - another security-focused validation step using the `tfsec` command. Tfsec is a security scanner for Terraform templates that detects potential security issues and insecure configurations in the Terraform code.

!!! note
    The commands and their attributes are displayed in the [**.pre-commit-config.yaml**](https://github.com/epmd-edp/hcl-terraform-terraform/blob/master/.pre-commit-config.yaml) file.

## Related Articles

* [User Guide Overview](../user-guide/index.md)
* [Add Infrastructure](../user-guide/add-infrastructure.md)
* [Manage Infrastructures](../user-guide/infrastructure.md)