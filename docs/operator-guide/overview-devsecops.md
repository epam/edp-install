# Secure Delivery on the Platform

The EPAM Delivery Platform emphasizes the importance of incorporating security practices into the software development lifecycle through the DevSecOps approach. By integrating a diverse range of open-source and enterprise security tools tailored to specific functionalities, organizations can ensure efficient and secure software development. These tools, combined with fundamental DevSecOps principles such as collaboration, continuous security, and automation, contribute to the identification and remediation of vulnerabilities early in the process, minimizes risks, and fosters a security-first culture across the organization.

The EPAM Delivery Platform enabling seamless integration with various security tools and vulnerability management systems, enhancing the security of source code and ensuring compliance.

## Supported Solutions

The below table categorizes various open-source and enterprise security tools based on their specific functionalities. It provides a comprehensive view of the available options for each security aspect. This classification facilitates informed decision-making when selecting and integrating security tools into a development pipeline, ensuring an efficient and robust security stance. EDP supports the integration of both open-source and enterprise security tools, providing a flexible and versatile solution for security automation. See table below for more details.

| Functionality                      | Open-Source Tools (integrated in Pipelines) | Enterprise Tools (available for Integration)    |
|:----------------------------------:|--------------------------------------|--------------------------------------------------------|
| Hardcoded Credentials Scanner      | TruffleHog, GitLeaks, GitSecrets     | GitGuardian, SpectralOps, Bridgecrew                   |
| Static Application Security Testing| SonarQube, Semgrep CLI               | Veracode, Checkmarx, Coverity                          |
| Software Composition Analysis      | OWASP Dependency-Check, cdxgen, Nancy | Black Duck Hub, Mend, Snyk                            |
| Container Security                 | Trivy, Grype, Clair                  | Aqua Security, Sysdig Secure, Snyk                     |
| Infrastructure as Code Security    | Checkov, Tfsec                       | Bridgecrew, Prisma Cloud, Snyk                         |
| Dynamic Application Security Testing| OWASP Zed Attack Proxy              | Fortify WebInspect, Rapid7 InsightAppSec, Checkmarx    |
| Continuous Monitoring and Logging  | ELK Stack, OpenSearch, Loki          | Splunk, Datadog                                        |
| Security Audits and Assessments    | OpenVAS                              | Tenable Nessus, QualysGuard, BurpSuite Professional    |
| Vulnerability Management and Reporting | DefectDojo, OWASP Dependency-Track| -                                                     |

For obtaining and managing report post scanning, deployment of various vulnerability management systems and security tools is required. These include:

### DefectDojo

[DefectDojo](https://www.defectdojo.com/) is a comprehensive vulnerability management and security orchestration platform facilitating the handling of uploaded security reports. Examine the prerequisites and fundamental instructions for [installing DefectDojo](./install-defectdojo.md) on Kubernetes or OpenShift platforms.

### OWASP Dependency Track

[Dependency Track](https://dependencytrack.org/) is an intelligent Software Composition Analysis (SCA) platform that provides a comprehensive solution for managing vulnerabilities in third-party and open-source components.

### Gitleaks

[Gitleaks](https://github.com/zricethezav/gitleaks) is a versatile SAST tool used to scan Git repositories for hardcoded secrets, such as passwords and API keys, to prevent potential data leaks and unauthorized access.

### Trivy

[Trivy](https://github.com/aquasecurity/trivy) is a simple and comprehensive vulnerability scanner for containers and other artifacts, providing insight into potential security issues across multiple ecosystems.

### Grype

[Grype](https://github.com/anchore/grype) is a fast and reliable vulnerability scanner for container images and filesystems, maintaining an up-to-date vulnerability database for efficient and accurate scanning.

### Tfsec

[Tfsec](https://github.com/aquasecurity/tfsec) is an effective Infrastructure as Code (IaC) security scanner, tailored specifically for reviewing Terraform templates. It helps identify potential security issues related to misconfigurations and non-compliant practices, enabling developers to address vulnerabilities and ensure secure infrastructure deployment.

### Checkov

[Checkov](https://github.com/bridgecrewio/checkov) is a robust static code analysis tool designed for IaC security, supporting various IaC frameworks such as Terraform, CloudFormation, and Kubernetes. It assists in detecting and mitigating security and compliance misconfigurations, promoting best practices and adherence to industry standards across the infrastructure.

### Cdxgen

[Cdxgen](https://github.com/AppThreat/cdxgen) is a lightweight and efficient tool for generating Software Bill of Materials (SBOM) using CycloneDX, a standard format for managing component inventory. It helps organizations maintain an up-to-date record of all software components, their versions, and related vulnerabilities, streamlining monitoring and compliance within the software supply chain.

### Semgrep CLI

[Semgrep CLI](https://github.com/returntocorp/semgrep) is a versatile and user-friendly command-line interface for the Semgrep security scanner, enabling developers to perform Static Application Security Testing (SAST) for various programming languages. It focuses on detecting and preventing potential security vulnerabilities, code quality issues, and custom anti-patterns, ensuring secure and efficient code development.
