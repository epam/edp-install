# Verification of EDP Artifacts

This documentation outlines platform SLSA integration and guides verifying image authenticity and provenance.

Supply Chain Levels of [Software Assurance (SLSA)](https://slsa.dev/) is a framework for assessing and enhancing software supply chain security.
Software Supply Chain Security is a critical aspect of modern software development and deployment. Supply Chain Levels of Software Assurance (SLSA) provides a framework for assessing and enhancing the security of your software supply chain.

## Prerequisites

Ensure you have installed [rekor-cli](https://docs.sigstore.dev/logging/installation/) and [cosign](https://docs.sigstore.dev/system_config/installation/) on your environment before proceeding.

***
## Release Assets
| Asset                   | Description                   |
|-------------------------|-------------------------------|
| [codebase-operator](https://github.com/epam/edp-codebase-operator/tree/master)       | [Docker Image](https://hub.docker.com/r/epamedp/cd-pipeline-operator)                  |
| [edp-headlamp](https://github.com/epam/edp-headlamp)            | [Docker Image](https://hub.docker.com/r/epamedp/edp-headlamp)                  |
| [edp-tekton](https://github.com/epam/edp-tekton)              | [Docker Image](https://hub.docker.com/r/epamedp/edp-tekton)                  |
| [cd-pipeline-operator](https://github.com/epam/edp-cd-pipeline-operator)    | [Docker Image](https://hub.docker.com/r/epamedp/cd-pipeline-operator)                  |
| [gerrit-operator](https://github.com/epam/edp-gerrit-operator)          | [Docker Image](https://hub.docker.com/r/epamedp/gerrit-operator)                  |
| edp-gerrit              | [Docker Image](https://hub.docker.com/r/epamedp/edp-gerrit)                  |

***

## Verify Container Images

EPAM Delivery Platform's container images are signed using cosign with the [cosign.pub](https://raw.githubusercontent.com/epam/edp-install/master/cosign.pub) key for signing and transparency. You can verify a container image's signature by executing the `cosign verify` command.

To confirm the authenticity of the image, please follow the steps given below:

1. Run the `cosign verify` command. See the example below:

  ```bash
  cosign verify --insecure-ignore-tlog=true  --key https://raw.githubusercontent.com/epam/edp-install/master/cosign.pub epamedp/keycloak-operator:1.18.2 | jq .
  ```

Verification for [epamedp/keycloak-operator:1.18.2](https://hub.docker.com/layers/epamedp/keycloak-operator/1.18.2/images/sha256-766228ec8833339d8e1492873c05ee24941871871a06f792cba43e59374ef9c1?context=explore):

  ```bash
   - The cosign claims were validated
   - The signatures were verified against the specified public key
   [
     {
       "critical": {
         "identity": {
           "docker-reference": "index.docker.io/epamedp/keycloak-operator"
         },
         "image": {
           "docker-manifest-digest": "sha256:766228ec8833339d8e1492873c05ee24941871871a06f792cba43e59374ef9c1"
         },
         "type": "cosign container image signature"
       },
       "optional": null
     }
   ]
  ```

## Verify Container Image With SLSA Attestations

An [SLSA](https://slsa.dev/) Level 3 provenance is verified using "INSERT OUT TOOL (SEEMS TO BE COSIGN VERIFY)". The following (SEEMS TO BE COSIGN VERIFY) command will verify the signature of an attestation and how it was issued. It will contain the payloadType, payload, and signature.

1. Run the cosign verify-attestation command using the [cosign.pub](https://raw.githubusercontent.com/epam/edp-install/master/cosign.pub):

  ```bash
  cosign verify-attestation --insecure-ignore-tlog=true  --key https://raw.githubusercontent.com/epam/edp-install/master/cosign.pub --type slsaprovenance docker.io/epamedp/keycloak-operator:1.18.2 | jq .
  ```

Verification for [epamedp/keycloak-operator:1.18.2](https://hub.docker.com/layers/epamedp/keycloak-operator/1.18.2/images/sha256-766228ec8833339d8e1492873c05ee24941871871a06f792cba43e59374ef9c1?context=explore):

  ```bash
     - The cosign claims were validated
     - The signatures were verified against the specified public key
   {
     "payloadType": "application/vnd.in-toto+json",
     "payload": "eyJfdHlwZSI6Imh0dHBzOi8vaW4tdG90by5pby9TdGF0ZW1lbnQvdjAuMSIsInByZWRpY2F0ZVR5cGUiOiJodHRwczovL3Nsc2EuZGV2L3Byb3ZlbmFuY2UvdjAuMiIsInN1YmplY3QiOlt7Im5hbWUiOiJpbmRleC5kb2NrZXIuaW8vZXBhbWVkcC9rZXljbG9hay1vcGVyYXRvciIsImRpZ2VzdCI6eyJzaGEyNTYiOiI3NjYyMjhlYzg4MzMzMzlkOGUxNDkyODczYzA1ZWUyNDk0MTg3MTg3MWEwNmY3OTJjYmE0M2U1OTM3NGVmOWMxIn19XSwicHJlZGljYXRlIjp7ImJ1aWxkZXIiOnsiaWQiOiJodHRwczovL3Rla3Rvbi5kZXYvY2hhaW5zL3YyIn0sImJ1aWxkVHlwZSI6InRla3Rvbi5kZXYvdjFiZXRhMS9UYXNrUnVuIiwiaW52b2NhdGlvbiI6eyJjb25maWdTb3VyY2UiOnt9LCJwYXJhbWV0ZXJzIjp7IkJVSUxERVJfSU1BR0UiOiJnY3IuaW8va2FuaWtvLXByb2plY3QvZXhlY3V0b3I6djEuMTIuMS1kZWJ1ZyIsIkNPTlRFWFQiOiIuLyIsIkRPQ0tFUkZJTEUiOiJEb2NrZXJmaWxlIiwiSU1BR0UiOiJlcGFtZWRwL2tleWNsb2FrLW9wZXJhdG9yOjEuMTguMiIsIklNQUdFX1RBUiI6ImtleWNsb2FrLW9wZXJhdG9yXzEuMTguMiJ9LCJlbnZpcm9ubWVudCI6eyJhbm5vdGF0aW9ucyI6eyJtZXRhLmhlbG0uc2gvcmVsZWFzZS1uYW1lIjoiZWRwLWN1c3RvbS1waXBlbGluZXMiLCJtZXRhLmhlbG0uc2gvcmVsZWFzZS1uYW1lc3BhY2UiOiJlZHAtZGVsaXZlcnkiLCJwaXBlbGluZS50ZWt0b24uZGV2L2FmZmluaXR5LWFzc2lzdGFudCI6ImFmZmluaXR5LWFzc2lzdGFudC01ZjIyN2JhNGM0IiwicGlwZWxpbmUudGVrdG9uLmRldi9yZWxlYXNlIjoiMjI5OWIxNSIsInRla3Rvbi5kZXYvY2F0ZWdvcmllcyI6IkltYWdlIEJ1aWxkIiwidGVrdG9uLmRldi9kaXNwbGF5TmFtZSI6IkJ1aWxkIGFuZCB1cGxvYWQgY29udGFpbmVyIGltYWdlIHVzaW5nIEthbmlrbyIsInRla3Rvbi5kZXYvcGlwZWxpbmVzLm1pblZlcnNpb24iOiIwLjE3LjAiLCJ0ZWt0b24uZGV2L3BsYXRmb3JtcyI6ImxpbnV4L2FtZDY0IiwidGVrdG9uLmRldi90YWdzIjoiaW1hZ2UtYnVpbGQifSwibGFiZWxzIjp7ImFwcC5rdWJlcm5ldGVzLmlvL2Jhc2VkLW9uIjoiMC42IiwiYXBwLmt1YmVybmV0ZXMuaW8vbWFuYWdlZC1ieSI6IkhlbG0iLCJhcHAua3ViZXJuZXRlcy5pby92ZXJzaW9uIjoiMC44LjAiLCJoZWxtLnNoL2NoYXJ0IjoiZWRwLWN1c3RvbS1waXBlbGluZXMtMC44LjAiLCJrOHNsZW5zLWVkaXQtcmVzb3VyY2UtdmVyc2lvbiI6InYxIiwidGVrdG9uLmRldi9tZW1iZXJPZiI6InRhc2tzIiwidGVrdG9uLmRldi9waXBlbGluZSI6ImdlcnJpdC1vcGVyYXRvcnMtYXBwLXJlbGVhc2UtZWRwIiwidGVrdG9uLmRldi9waXBlbGluZVJ1biI6ImtleWNsb2FrLW9wZXJhdG9yLXJlbGVhc2UiLCJ0ZWt0b24uZGV2L3BpcGVsaW5lVGFzayI6Imthbmlrby1idWlsZCIsInRla3Rvbi5kZXYvdGFzayI6ImthbmlrbyJ9fX0sImJ1aWxkQ29uZmlnIjp7InN0ZXBzIjpbeyJlbnRyeVBvaW50IjoiL2thbmlrby9leGVjdXRvciBcXFxuICAtLWRvY2tlcmZpbGU9L3dvcmtzcGFjZS9zb3VyY2UvRG9ja2VyZmlsZSBcXFxuICAtLWNvbnRleHQ9L3dvcmtzcGFjZS9zb3VyY2UvLi8gXFxcbiAgLS1kZXN0aW5hdGlvbj1lcGFtZWRwL2tleWNsb2FrLW9wZXJhdG9yOjEuMTguMiBcXFxuICAtLWRpZ2VzdC1maWxlPS90ZWt0b24vcmVzdWx0cy9JTUFHRV9ESUdFU1QgXFxcbiAgLS10YXItcGF0aD1rZXljbG9hay1vcGVyYXRvcl8xLjE4LjIudGFyIFxcXG4iLCJhcmd1bWVudHMiOm51bGwsImVudmlyb25tZW50Ijp7ImNvbnRhaW5lciI6ImJ1aWxkLWFuZC1wdXNoIiwiaW1hZ2UiOiJvY2k6Ly9nY3IuaW8va2FuaWtvLXByb2plY3QvZXhlY3V0b3JAc2hhMjU2OmE3ZWE5ZjY5ZDc3ZDdlN2EwZWE4MjFmMTUwNjliZTQ1NDIwYTUzNmY4MWFiNTc4N2E5ODg2NTllNDhjMjUzNzcifSwiYW5ub3RhdGlvbnMiOm51bGx9LHsiZW50cnlQb2ludCI6InNldCAtZVxuaW1hZ2U9XCJlcGFtZWRwL2tleWNsb2FrLW9wZXJhdG9yOjEuMTguMlwiXG5lY2hvIC1uIFwiJHtpbWFnZX1cIiB8IHRlZSBcIi90ZWt0b24vcmVzdWx0cy9JTUFHRV9VUkxcIlxuIiwiYXJndW1lbnRzIjpudWxsLCJlbnZpcm9ubWVudCI6eyJjb250YWluZXIiOiJ3cml0ZS11cmwiLCJpbWFnZSI6Im9jaTovL2RvY2tlci5pby9saWJyYXJ5L2FscGluZUBzaGEyNTY6NzE0NGY3YmFiM2Q0YzI2NDhkN2U1OTQwOWYxNWVjNTJhMTgwMDZhMTI4YzczM2ZjZmYyMGQzYTRhNTRiYTQ0YSJ9LCJhbm5vdGF0aW9ucyI6bnVsbH1dfSwibWV0YWRhdGEiOnsiYnVpbGRTdGFydGVkT24iOiIyMDIzLTEwLTMxVDExOjE2OjIxWiIsImJ1aWxkRmluaXNoZWRPbiI6IjIwMjMtMTAtMzFUMTE6MTY6NDBaIiwiY29tcGxldGVuZXNzIjp7InBhcmFtZXRlcnMiOmZhbHNlLCJlbnZpcm9ubWVudCI6ZmFsc2UsIm1hdGVyaWFscyI6ZmFsc2V9LCJyZXByb2R1Y2libGUiOmZhbHNlfSwibWF0ZXJpYWxzIjpbeyJ1cmkiOiJvY2k6Ly9nY3IuaW8va2FuaWtvLXByb2plY3QvZXhlY3V0b3IiLCJkaWdlc3QiOnsic2hhMjU2IjoiYTdlYTlmNjlkNzdkN2U3YTBlYTgyMWYxNTA2OWJlNDU0MjBhNTM2ZjgxYWI1Nzg3YTk4ODY1OWU0OGMyNTM3NyJ9fSx7InVyaSI6Im9jaTovL2RvY2tlci5pby9saWJyYXJ5L2FscGluZSIsImRpZ2VzdCI6eyJzaGEyNTYiOiI3MTQ0ZjdiYWIzZDRjMjY0OGQ3ZTU5NDA5ZjE1ZWM1MmExODAwNmExMjhjNzMzZmNmZjIwZDNhNGE1NGJhNDRhIn19XX19",
     "signatures": [
       {
         "keyid": "SHA256:7E2nAQnycq4vfPlzmLZGzpK/Vr6oXKqqGokDyrBSLck",
         "sig": "MEQCICjb8b7O5IHQhxP92uVUBElkm1Tsx9Kivh+JwdWvLXZBAiBshHoU7Nr2pEf9UdLB0Ob2GqYJQvn+ojSLSruWmbw2Ww=="
       }
     ]
   }
  ```

For more details about attestation, please refer to the official [cosign documentation](https://docs.sigstore.dev/verifying/attestation/) page.

## Verify Release Pipeline
Within each release component, you will discover a Rekor UUID, which serves to validate the flow of the release pipeline. Execute the following command to obtain comprehensive information about the release pipeline of [keycloak-operator](https://github.com/epam/edp-keycloak-operator/releases/tag/v1.18.2) with UUID

```24296fb24b8ad77a73b22d25e80bfd351fbad7444da4cda9e1509c810fd7d1fa815b288b761245ce```:

   ```bash
   rekor-cli get --uuid 24296fb24b8ad77a73b22d25e80bfd351fbad7444da4cda9e1509c810fd7d1fa815b288b761245ce --format json | jq -r .Attestation | jq .
   ```

Verification for [epamedp/keycloak-operator:1.18.2](https://hub.docker.com/layers/epamedp/keycloak-operator/1.18.2/images/sha256-766228ec8833339d8e1492873c05ee24941871871a06f792cba43e59374ef9c1?context=explore):

  ```bash
   {
     "_type": "https://in-toto.io/Statement/v0.1",
     "predicateType": "https://slsa.dev/provenance/v0.2",
     "subject": null,
     "predicate": {
       "builder": {
         "id": "https://tekton.dev/chains/v2"
       },
       "buildType": "tekton.dev/v1beta1/PipelineRun",
       "invocation": {
         "configSource": {},
         "parameters": {
           "build-image": "golang:1.20-bullseye",
           "chart-path": "deploy-templates",
           "codebase-name": "keycloak-operator",
           "dockerhub-account": "epamedp",
           "extra-build-commands": "VERSION=$(params.version) make build",
           "git-source-revision": "release/1.18",
           "git-source-url": "ssh://edp-ci@gerrit:30022/keycloak-operator",
           "goproxy": "http://athens-athens-proxy:80",
           "version": "1.18.2"
         },
         "environment": {
           "annotations": {
             "meta.helm.sh/release-name": "edp-custom-pipelines",
             "meta.helm.sh/release-namespace": "edp-delivery"
           },
           "labels": {
             "app.kubernetes.io/managed-by": "Helm",
             "app.kubernetes.io/version": "0.8.0",
             "helm.sh/chart": "edp-custom-pipelines-0.8.0",
             "k8slens-edit-resource-version": "v1",
             "tekton.dev/pipeline": "gerrit-operators-app-release-edp"
           }
         }
       },
  ...
  ```

By signing all our artifacts, we assure you that they are trustworthy. This guide is indispensable for developers and administrators to enhance their software's reliability and meet modern security standards. The adoption of SLSA will bring you confidence while using the platform.

## Related Articles

* [In-Toto Attestations](https://docs.sigstore.dev/verifying/attestation/)