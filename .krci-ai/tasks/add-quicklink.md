# Task: Add QuickLink Resource

## Description

<description>
This task helps users add new QuickLink resources to the KubeRocketCI platform. QuickLinks provide navigation shortcuts in the platform UI, improving usability by allowing users to quickly access different parts of the platform or related tools.
</description>

### Reference Assets

<prerequisites>
Dependencies:

- Best practices for `edp-install` repository: [repository-best-practices.md](./.krci-ai/data/repository-best-practices.md)

Validation: Verify the dependency exists at the specified path before proceeding. HALT if it is missing.
</prerequisites>

## Overview

<overview>
Your task is to guide users through the process of adding a new QuickLink resource to the KubeRocketCI platform. This involves creating the necessary YAML configuration file, ensuring it adheres to best practices, and integrating it into the existing Helm chart structure.
</overview>

## Instructions

<instructions>
1. Analyze the documentation: Review the [repository-best-practices.md](./.krci-ai/data/repository-best-practices.md) file for guidelines on naming conventions, file structure, and configuration standards.

DO NOT: Continue until analyzing the [repository-best-practices.md](./.krci-ai/data/repository-best-practices.md) file.
</instructions>

## Implementation Steps

<implementation>
### STEP-BY-STEP Implementation

1. IMPORTANT FIRST STEP: Ask the user to provide the following information:

   NOTES: User can specify several QuickLinks in one session. Create a separate YAML manifest for each QuickLink.

   - QuickLink name (REQUIRED)
   - URL (REQUIRED)
   - Base64 SVG icon (OPTIONAL)

2. Create the QuickLink YAML manifest:

   - Create a new YAML file named `<quicklink-name>` in the `deploy-templates/quicklinks/` directory of the `edp-install` repository.
   - Use the following template for the QuickLink manifest:

   CRITICAL: All spec fields are mandatory and must be filled out.

   ```yaml
   {{- if .Values.quickLinks.<quicklink-name> }}
   apiVersion: v2.edp.epam.com/v1
   kind: QuickLink
   metadata:
     name: <quicklink-name>
     labels:
       {{- include "edp-install.labels" . | nindent 4 }}
   spec:
     icon: <base64-svg-encoded-icon>
     type: default
     url: {{ default "" .Values.quickLinks.<quicklink-name>.url }}
     visible: true
   {{- end }}
   ```

   - Update the `values.yaml` file to include a section for the new QuickLink:

   ```yaml
   quickLinks:
     <quicklink-name>:
       url: "<url>"
   ```
</implementation>

## Success Criteria

<success_criteria>
- A new QuickLink YAML file has been created in the correct directory
- The QuickLink configuration follows the recommended template
- The values.yaml file has been updated with the new QuickLink configuration
- The QuickLink appears correctly in the KubeRocketCI UI after deployment
</success_criteria>
