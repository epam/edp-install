# Semgrep

[Semgrep](https://semgrep.dev/) is an open-source static source code analyzer for finding bugs and enforcing code standards.

Semgrep scanner is installed on the EDP Jenkins SAST agent and runs on the `sast` pipeline stage.
For details, please refer to the
[edp-library-stages repository](https://github.com/epam/edp-library-stages/blob/master/src/com/epam/edp/stages/impl/ci/impl/security/SASTMavenGradleGoApplication.groovy#L70).

## Supported Languages

Semgrep supports more than 20 languages, see the full list in the [official documentation](https://semgrep.dev/docs/supported-languages/).
EDP uses Semgrep to scan Java, JavaScript and Go languages.

## Related Articles

* [Add Security Scanner](add-security-scanner.md)
