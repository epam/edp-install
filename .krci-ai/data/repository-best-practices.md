# Best Practices for Working with edp-install

## Helm Chart Modifications

### Adding or Modifying Templates

When adding or modifying Kubernetes resource templates:

- Place templates in the appropriate subdirectory based on functionality:
  - `deploy-templates/templates/quick-links/` for UI navigation links
  - `deploy-templates/templates/rbac/` for RBAC resources
  - `deploy-templates/templates/external-secrets/` for external secrets integration
  - `deploy-templates/templates/marketplace/` for marketplace resources
- Use consistent naming conventions: `<resource-type>-<component>-<purpose>.yaml`
- Always include proper labels and annotations for resource identification
- Include conditional logic with `.Values` references to make templates configurable

### Values Configuration

When configuring values:

- All dependency-specific values should be under the dependency name as a top-level key:
  ```yaml
  <dependency-name>:
    enabled: true
    config: value
  ```
- Global values that affect multiple components should be under a `global:` section
- Always provide default values with clear comments explaining their purpose
- Use proper YAML formatting with consistent indentation

## Specific Resource Types

### External Secrets Resources

When working with External Secrets resources:

- Place all External Secrets resources in `deploy-templates/templates/external-secrets/`
- Follow the naming pattern: `<resource-type>-<name>.yaml`
- Include proper API versions and kinds for External Secrets Operator resources
- Configure proper refresh intervals for secrets
- Use templating for secret paths to make them configurable
- Include conditional logic for enabling/disabling external secrets integration

```yaml
# Example External Secret template
{{- if and .Values.externalSecrets.enabled .Values.externalSecrets.manageEDPInstallSecrets }}
{{- $secretStore := include "edp-install.secretStoreName" . }}
{{- $awsSecretName := .Values.externalSecrets.manageEDPInstallSecretsName }}
apiVersion: {{ include "app.externalSecrets.apiVersion" . }}
kind: ExternalSecret
metadata:
  name: <name-of-the-external-secret>
spec:
  refreshInterval: 1h
  secretStoreRef:
    kind: SecretStore
    name: {{ $secretStore }}
  data:
    - secretKey: <key-in-k8s-secret>
      remoteRef:
        key: {{ $awsSecretName }}
        property: <property-in-aws-parameter-store>
{{- end }}
```

### Marketplace Resources

When working with Marketplace resources:

- Place all Marketplace resources in `deploy-templates/templates/marketplace/`
- Include proper entitlement checking mechanisms
- Add license validation resources
- Configure marketplace-specific integration points
- Use conditional logic based on the deployment method
- Include documentation links in resources where appropriate

```yaml
# Example Marketplace resource template
{{ if .Values.marketplaceTemplates.enabled }}
apiVersion: v2.edp.epam.com/v1alpha1
kind: Template
metadata:
  name: <name-of-the-template>
spec:
  buildTool: <build-tool-name>
  category: <category-type>
  description: >-
    Example description of the application or service.
  displayName: <name-to-display>
  framework: <framework-name>
  icon:
    - base64data: <base64-encoded-svg-data>
      mediatype: image/svg+xml
  keywords:
    - <keyword1>
    - <keyword2>
  language: helm
  maintainers:
    - email: SupportEPMD-EDP@epam.com
      name: SupportEPMD-EDP
  maturity: stable
  minEDPVersion: 3.4.0
  source: https://github.com/KubeRocketCI/<template-repo>
  type: <application/library>
  version: 0.1.0
{{ end }}
```

### QuickLink Resources

When creating QuickLink resources:

- Place all QuickLink resources in `deploy-templates/templates/quick-links/`
- Follow the naming pattern: `quicklink-<name>.yaml`
- Include proper metadata and spec fields:
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
- Make the link URL configurable through values when appropriate
- Group related QuickLinks in the same file when they are conceptually related

### RBAC Resources

When working with RBAC resources:

- Place all RBAC resources in `deploy-templates/templates/rbac/`
- Follow the naming pattern: `<resource-type>-<component>-<purpose>.yaml`
- Group related RBAC resources together (Role, RoleBinding, ServiceAccount)
- Use the least privilege principle when defining permissions
- Include proper documentation on why specific permissions are needed

## Testing Changes

Before submitting changes:

- Validate templates with `helm lint ./deploy-templates`
- Render templates with `helm template ./deploy-templates`
- Test the full deployment in a development environment
- Verify all components install correctly when enabled

## Common Patterns

### Setting Dependency Values

To configure a dependency with values:

```yaml
# values.yaml
<dependency-name>:
  enabled: true
  key1: value1
  key2: value2
  
  nestedConfig:
    setting1: value
    setting2: value
```

### Enabling/Disabling Components

To enable or disable platform components it is necessary to set the `enabled` field under the respective dependency name:

```yaml
# values.yaml
codebase-operator:
  enabled: true  # Component enabled
  
gerrit-operator:
  enabled: false  # Component disabled
```

## Documentation

Always update the relevant documentation when making significant changes:

- Update README.md with usage instructions
- Update values.yaml comments for new configuration options
- Add example configurations for common use cases
