# Telemetry

The codebase operator for the EPAM Delivery Platform gathers anonymous data through telemetry. This data provides a clear picture of how the platform is being used and empowers the development team to make informed decisions and strategic enhancements to meet evolving operational needs. The anonymous data collected also plays an essential role in adopting a Software Development Life Cycle (SDLC) process strategically.

## Telemetry Data

The codebase-operator collects the following data:

- The version of the platform
- The number of codebases created and their parameters: language (for example, Java, NodeJS, etc.), framework (for example, FastAPI, Flask, etc.), build tool (for example, Maven, Gradle, etc.), strategy (for example, Clone, Create, Import), and Type (for example, library, application, etc.)
- The number of CD pipelines created and their parameters: deployment type (for example, Auto, Manual), and the number of stages
- The number of Git providers connected to the platform and their types (for example, GitHub, GitLab, Gerrit)
- Where Jira is enabled or not
- The type of the Docker registry connected to the platform (for example, Docker Hub, Harbor, ECR)

```go
package telemetry

type CodebaseMetrics struct {
  Lang       string `json:"lang"`
  Framework  string `json:"framework"`
  BuildTool  string `json:"buildTool"`
  Strategy   string `json:"strategy"`
  Type       string `json:"type"`
  Versioning string `json:"versioning"`
}

type CdPipelineMetrics struct {
  DeploymentType string `json:"deploymentType"`
  NumberOfStages int    `json:"numberOfStages"`
}

type PlatformMetrics struct {
  CodebaseMetrics   []CodebaseMetrics   `json:"codebaseMetrics"`
  CdPipelineMetrics []CdPipelineMetrics `json:"cdPipelineMetrics"`
  GitProviders      []string            `json:"gitProviders"`
  JiraEnabled       bool                `json:"jiraEnabled"`
  RegistryType      string              `json:"registryType"`
  Version           string              `json:"version"`
}
```

You can verify the code which collects the data in the [codebase-operator](https://github.com/epam/edp-codebase-operator/tree/master/pkg/telemetry) repository.

## Collecting Timeline

The codebase-operator collects the data every 24 hours and sends it to the EDP Telemetry Service. The first data points are collected 24 hours after the codebase-operator is deployed to allow users to opt-out of telemetry.

## Disabling Telemetry

The codebase-operator collects telemetry data by default. To disable telemetry, set the `TELEMETRY_ENABLED` environment variable to `false` in the codebase-operator's deployment configuration. To achieve this, run the following command:

```bash
helm upgrade --install codebase-operator codebase-operator/codebase-operator --set "telemetryEnabled=false"
```
