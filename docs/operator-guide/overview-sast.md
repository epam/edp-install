# Static Application Security Testing Overview

EPAM Delivery Platform provides the implemented Static Application Security Testing support allowing to work with the [Semgrep security scanner](https://semgrep.dev/) and the [DefectDojo vulnerability management system](https://www.defectdojo.com/) to check the source code for known vulnerabilities.

## Supported Languages

EDP SAST supports a number of languages and package managers.

|Language (Package Managers)|Scan Tool|Build Tool|
|:-:|-|-|
| Java | [Semgrep](./sast-scaner-semgrep.md) |Maven, Gradle|
| Go | [Semgrep](./sast-scaner-semgrep.md) |Go|
| React| [Semgrep](./sast-scaner-semgrep.md) |Npm|

## Supported Vulnerability Management System

To get and then manage a SAST report after scanning, it is necessary to deploy the vulnerability management system, for instance, DefectDojo.

### DefectDojo

[DefectDojo](https://www.defectdojo.com/) is a vulnerability management and security orchestration platform that allows managing the uploaded security reports.

Inspect the prerequisites and the main steps for [installing DefectDojo](./install-defectdojo.md) on Kubernetes or OpenShift platforms.

## Related Articles

* [Add Security Scanner](add-security-scanner.md)
* [Semgrep](sast-scaner-semgrep.md)