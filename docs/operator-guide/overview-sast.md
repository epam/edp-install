# Static Application Security Testing (SAST)

EPAM Delivery Platform provides the implemented Static Application Security Testing support allowing to work with a security scanner (Semgrep) and a vulnerability management system (DefectDojo) to check your source code for known vulnerabilities.

## Supported Languages

EDP SAST supports a number of languages and package managers.

|Language (package managers)|Scan tool|Build Tool|
|:-:|-|-|
| Java | [Semgrep](./sast-scaner-semgrep.md) |Maven, Gradle|
| Go | [Semgrep](./sast-scaner-semgrep.md) |Go|
| React| [Semgrep](./sast-scaner-semgrep.md) |Npm|

## Supported Vulnerability Management System

If it is necessary to get and manage a SAST report after scanning, you need to deploy a vulnerability management system, for instance, DefectDojo.

### DefectDojo

[DefectDojo](https://www.defectdojo.com/) is a vulnerability management and security orchestration platform that allows managing the uploaded security reports.

Inspect the prerequisites and the main steps for [installing DefectDojo](./install-defectdojo.md) on Kubernetes or OpenShift platforms.

## Related Articles

* [Add Security Scanner](add-security-scanner.md)
* [Semgrep](sast-scaner-semgrep.md)