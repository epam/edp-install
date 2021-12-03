# Use Open Policy Agent

[Open Policy Agent (OPA)](https://www.openpolicyagent.org/) is a policy engine that provides:

- High-level declarative policy language [Rego](https://www.openpolicyagent.org/docs/latest/#rego);
- API and tooling for policy execution.

EPAM Delivery Platform ensures the implemented Open Policy Agent support allowing to work with [Open Policy Agent bundles](https://www.openpolicyagent.org/docs/latest/management-bundles/) that is processed by means of stages in the **Code Review** and **Build** pipelines. These pipelines are expected to be created after the Rego OPA Library is added.

## Code Review Pipeline Stages

In the **Code Review** pipeline, the following stages are available:

1. **checkout** stage, a standard step during which all files are checked out from a selected branch of the Git repository.

2. **tests** stage containing a script that performs the following actions:

  2.1. Runs [policy tests](https://www.openpolicyagent.org/docs/latest/policy-testing/).

  2.2. Converts OPA test results into JUnit format.

  2.3. Publishes JUnit-formatted results to Jenkins.

## Build Pipeline Stages

In the **Build** pipeline, the following stages are available:

1. **checkout** stage, a standard step during which all files are checked out from a selected branch of the Git repository.

2. **get-version** optional stage, a step where library version is determined either via:
  2.1. Standard EDP versioning functionality.
  2.2. Manually specified version. In this case *.manifest* file in a root directory **MUST** be provided. File must contain a JSON document with revision field. Minimal example: `{ "revision": "1.0.0" }"`.

3. **tests** stage containing a script that performs the following actions:
  3.1. Runs [policy tests](https://www.openpolicyagent.org/docs/latest/policy-testing/).
  3.2. Converts OPA test results into JUnit format.
  3.3. Publishes JUnit-formatted results to Jenkins.

4. **git-tag** stage, a standard step where git branch is tagged with a version.

### Related Articles

- [EDP Pipeline Framework](pipeline-framework.md)
