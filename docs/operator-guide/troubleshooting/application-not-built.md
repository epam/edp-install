# Codebase Build Process is Failed

## Problem

Application can't succeed with build.

  !![Tree diagram window](../../assets/quick-start/tree_diagram.png "Tree diagram window")

## Cause

Code quality checks may failed in SonarQube.

## Solution

1. Navigate to your application in KubeRocketCI portal.

2. In the build history section, click the failed pipeline run name to open the pipeline run in Tekton.

3. Open the sonar-scanner step:

  If the quality check is insufficient, it means that SonarQube rated the quality of your codebase as low. It is required to refine the code to pass the sonar-scanner step.

  !![Failed sonar-scanner step](../../assets/quick-start/failure_details.png "Failed sonar-scanner step")

4. Check if the SonarQube project is created in the SonarQube tool integrated.

5. Once the SonarQube project is created, build application again.

  If you have set up SonarCloud for monitoring code quality, be aware that the initial build of the codebase is likely to fail. Following this failed pipeline run, you should see a new project created within the organization. Once this happens, establish a Quality Gate in SonarCloud subsequent to the initial pipeline execution, and then initiate the build pipeline again. This procedure is described in detail in the [Create Application](../../quick-start/create-application.md) page.

## Related Articles

* [Create Application](../../quick-start/create-application.md)
* [SonarQube Integration](../sonarqube.md)
