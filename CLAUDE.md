# edp-install

`edp-install` is the top-level umbrella Helm chart for the entire KubeRocketCI platform. It contains no compiled code — only Helm templates, a sub-chart dependency manifest, and values files. All component operators and services are pulled in as sub-chart dependencies from `https://epam.github.io/edp-helm-charts/stable`.

## Chart Layout

```
deploy-templates/
  Chart.yaml          — umbrella chart metadata + dependency list
  Chart.lock          — pinned dependency digests (commit when deps change)
  values.yaml         — canonical values with inline doc comments
  values-custom-certs.yaml   — example overlay for custom TLS certs
  values-no-ingress.yaml     — example overlay for no-ingress deployments
  templates/
    _helpers.tpl             — shared template helpers (labels, fullname, secretstore name)
    krci_config_map.yaml     — ConfigMap `krci-config` (platform-wide env surface)
    extra_manifests.yaml     — renders `extraObjects` values as arbitrary K8s manifests
    external-secrets/        — ExternalSecret + SecretStore resources for all platform secrets
    marketplace/             — pre-bundled CodebaseTemplate CRs (gin, springboot, terraform, etc.)
    openshift/               — OpenShift-specific RBAC and kaniko rolebindings
    quick-links/             — QuickLink CRs for argocd, sonar, nexus, defectdojo, logging, etc.
    rbac/                    — OIDC-group-based RBAC roles and rolebindings
```

## Sub-chart Dependencies (Chart.yaml)

All sourced from `@epamedp` (`https://epam.github.io/edp-helm-charts/stable`):

| Dependency | Condition key | Default |
|---|---|---|
| `codebase-operator` | `codebase-operator.enabled` | `true` |
| `cd-pipeline-operator` | `cd-pipeline-operator.enabled` | `true` |
| `edp-tekton` | `edp-tekton.enabled` | `true` |
| `gitfusion` | `gitfusion.enabled` | `true` |
| `krci-portal` | `krci-portal.enabled` | `true` |
| `edp-headlamp` | `edp-headlamp.enabled` | `false` |
| `gerrit-operator` | `gerrit-operator.enabled` | `false` |

## Key Values Conventions

- `global.platform` — `"kubernetes"` or `"openshift"` (affects RBAC templates in `templates/openshift/`)
- `global.dnsWildCard` — cluster wildcard; used as fallback for `clusterName` in tekton/portal URL construction
- `global.gitProviders` — list (e.g. `[github, gitlab, gerrit, bitbucket]`); controls GitServer creation in edp-tekton
- `global.dockerRegistry.type` — one of `ecr`, `harbor`, `dockerhub`, `openshift`, `nexus`, `ghcr`
- `externalSecrets.enabled` — when `true`, deploys SecretStore + ExternalSecret resources; AWS ParameterStore is default (`externalSecrets.type: aws`)
- `edp-tekton.clusterName` — must match `krci-portal.configEnv.DEFAULT_CLUSTER_NAME` for pipeline URL construction; leave blank to auto-derive from `global.dnsWildCard`
- `edp-tekton.pipelines.deployableResources` — fine-grained flags to enable/disable per-language pipeline sets

## Helm Operations

```bash
# Add the epamedp chart repo (required before update/install)
helm repo add epamedp https://epam.github.io/edp-helm-charts/stable

# Fetch/update sub-chart archives into deploy-templates/charts/
helm dependency update deploy-templates/

# Render templates locally for inspection (substitute real values)
helm template edp-install deploy-templates/ -f deploy-templates/values.yaml -n edp --debug

# Install / upgrade
helm upgrade --install edp-install deploy-templates/ \
  -f deploy-templates/values.yaml \
  -n edp --create-namespace

# Render and validate with chart-testing config (ct.yaml points to @epamedp repo)
ct lint --chart-dirs deploy-templates/ --all
```

## Makefile Targets

```bash
make helm-docs      # regenerate deploy-templates/README.md from values.yaml doc comments
make validate-docs  # fail if README.md is out of sync with values.yaml (runs in CI)
make changelog      # regenerate CHANGELOG.md using git-chglog (tools auto-downloaded to bin/)
```

`bin/helm-docs` and `bin/git-chglog` are downloaded on first use via `go get` into the local `bin/` directory (not installed globally).

## Bumping a Sub-chart Version

1. Update the `version` field for the dependency in `deploy-templates/Chart.yaml`.
2. Run `helm dependency update deploy-templates/` — this refreshes `Chart.lock` and downloads the new archive into `deploy-templates/charts/`.
3. Regenerate docs: `make helm-docs`.
4. Commit `Chart.yaml`, `Chart.lock`, and the updated `deploy-templates/README.md` together.
