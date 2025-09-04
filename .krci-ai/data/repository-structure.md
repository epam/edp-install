# edp-install Repository Structure

## Repository Organization

The `edp-install` repository is organized in a way that separates core Helm chart functionality from documentation and supporting tools:

```
edp-install/
├── deploy-templates/          # Main Helm chart directory
│   ├── Chart.yaml            # Chart metadata and dependencies
│   ├── values.yaml           # Default configuration values
│   └── templates/            # Kubernetes resource templates
│       ├── _helpers.tpl      # Template helpers
│       ├── extra_manifests.yaml
│       ├── krci_config_map.yaml
│       ├── external-secrets/ # External Secrets integration
│       ├── marketplace/      # Marketplace specific resources
│       ├── openshift/        # OpenShift specific resources
│       ├── quick-links/      # QuickLink resources for UI navigation
│       └── rbac/            # Role-Based Access Control resources
├── docs/                     # Documentation files
├── helmfiles/                # Helmfile configurations
└── hack/                     # Support scripts and utilities
```

## Chart Structure Details

### deploy-templates/Chart.yaml

This is the main definition file for the Helm chart. It contains:

- Chart metadata (name, version, description)
- Dependencies on other Helm charts that provide the actual platform components
- Condition flags to enable/disable specific components

Each dependency in the `Chart.yaml` file represents a core component of the KubeRocketCI platform:

```yaml
dependencies:
- name: codebase-operator
  repository: "@epamedp"
  version: "2.28.0"
  condition: codebase-operator.enabled

- name: edp-headlamp
  repository: "@epamedp"
  version: "0.23.0"
  condition: edp-headlamp.enabled

# Additional dependencies...
```

### deploy-templates/values.yaml

This file contains the default values for the chart and all its dependencies. Key aspects include:

- Global configuration settings for the entire platform
- Component-specific configuration sections
- Feature flags and options for customizing the deployment

### Template Directories

The `templates` directory contains various Kubernetes resources organized by functionality:

#### external-secrets/

Contains resources for integrating with external secret management systems:

- SecretStore definitions for connecting to secret providers
- ExternalSecret resources for fetching secrets from external systems
- Templates for mapping external secrets to Kubernetes secrets
- Configuration for authentication with secret providers

#### marketplace/

Contains resources specific to cloud marketplace deployments:

- License validation resources
- Marketplace integration configurations
- Entitlement checking mechanisms
- Usage reporting resources

#### openshift/

Contains OpenShift-specific resources and configurations:

- Route definitions instead of Ingress
- SecurityContextConstraints
- OpenShift-specific service accounts and permissions
- Template configurations for OpenShift compatibility

#### quick-links/

Contains QuickLink custom resources for the platform UI navigation:

- Links to platform components and tools
- Documentation links
- Custom application links
- Monitoring and observability tool links

#### rbac/

Contains Role-Based Access Control definitions for platform components:

- ClusterRole and Role definitions
- ClusterRoleBinding and RoleBinding resources
- ServiceAccount configurations
- Permission structures for platform components

## Dependencies and Values

Dependencies in the `Chart.yaml` file must have corresponding values in the `values.yaml` file. For each dependency, values should be specified under a section named after the dependency:

```yaml
codebase-operator:
  enabled: true
  value1: "some-value"
  value2: "another-value"

edp-headlamp:
  enabled: true
  ingress:
    enabled: true
    host: "headlamp.example.com"
```

## Enabling/Disabling Components

Components can be enabled or disabled through the values file using the `.Values.<dependency-name>.enabled` syntax:

```yaml
codebase-operator:
  enabled: true  # Component is enabled

gerrit-operator:
  enabled: false  # Component is disabled
```

This allows for flexible deployment configurations based on specific requirements.
