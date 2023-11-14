# Verification of EDP Artifacts

This documentation outlines platform SLSA integration and guides verifying image authenticity and provenance.

Supply Chain Levels of [Software Assurance (SLSA)](https://slsa.dev/) is a framework for assessing and enhancing software supply chain security.
Software Supply Chain Security is a critical aspect of modern software development and deployment. Supply Chain Levels of Software Assurance (SLSA) provides a framework for assessing and enhancing the security of your software supply chain.

## Prerequisites

Ensure you have installed [rekor-cli](https://docs.sigstore.dev/logging/installation/) and [cosign](https://docs.sigstore.dev/system_config/installation/) on your environment before proceeding.

***

## Release Assets

The table below represents a list of EDP components with corresponding images that are signed and pushed to DockerHub:

| Asset                   | Description                   |
|-------------------------|-------------------------------|
| [codebase-operator](https://github.com/epam/edp-codebase-operator/tree/master)       | [Docker Image](https://hub.docker.com/r/epamedp/cd-pipeline-operator)                  |
| [edp-headlamp](https://github.com/epam/edp-headlamp)            | [Docker Image](https://hub.docker.com/r/epamedp/edp-headlamp)                  |
| [edp-tekton](https://github.com/epam/edp-tekton)              | [Docker Image](https://hub.docker.com/r/epamedp/edp-tekton)                  |
| [cd-pipeline-operator](https://github.com/epam/edp-cd-pipeline-operator)    | [Docker Image](https://hub.docker.com/r/epamedp/cd-pipeline-operator)                  |
| [gerrit-operator](https://github.com/epam/edp-gerrit-operator)          | [Docker Image](https://hub.docker.com/r/epamedp/gerrit-operator)                  |
| [edp-gerrit](https://github.com/epam/edp-gerrit)              | [Docker Image](https://hub.docker.com/r/epamedp/edp-gerrit)                  |

***

## Verify Container Images

EPAM Delivery Platform's container images are signed using cosign with the [cosign.pub](https://raw.githubusercontent.com/epam/edp-install/master/cosign.pub) key for signing and transparency. You can verify a container image's signature by executing the `cosign verify` command.

To confirm the authenticity of the image, run the `cosign verify` command. See the example below:

  ```bash
  cosign verify  --key https://raw.githubusercontent.com/epam/edp-install/master/cosign.pub epamedp/codebase-operator:2.20.0 | jq .
  ```

Verification for [epamedp/codebase-operator:2.20.0](https://hub.docker.com/layers/epamedp/codebase-operator/2.20.0/images/sha256-36585a13b5b5ff5a15138e9d16cc74eb3aac4560b77be15161d3b3db25b89e1d?context=repo):

  ```bash
  Verification for index.docker.io/epamedp/codebase-operator:2.20.0
  The following checks were performed on each of these signatures:
    - The cosign claims were validated
    - The claims were present in the transparency log
    - The signatures were integrated into the transparency log when the certificate was valid
    - The signatures were verified against the specified public key
  [
    {
      "critical": {
        "identity": {
          "docker-reference": "index.docker.io/epamedp/codebase-operator"
        },
        "image": {
          "docker-manifest-digest": "sha256:36585a13b5b5ff5a15138e9d16cc74eb3aac4560b77be15161d3b3db25b89e1d"
        },
        "type": "cosign container image signature"
      },
      "optional": null
    }
  ]
  ```

## Verify Container Image With SLSA Attestations

An [SLSA](https://slsa.dev/) Level 3 provenance is verified using. The following command will verify the signature of an attestation and how it was issued. It will contain the payloadType, payload, and signature.

Run the `cosign verify-attestation` command using the [cosign.pub](https://raw.githubusercontent.com/epam/edp-install/master/cosign.pub):

  ```bash
  cosign verify-attestation --key https://raw.githubusercontent.com/epam/edp-install/master/cosign.pub --type slsaprovenance epamedp/codebase-operator:2.20.0 | jq .
  ```

Verification for [epamedp/codebase-operator:2.20.0](https://hub.docker.com/layers/epamedp/codebase-operator/2.20.0/images/sha256-36585a13b5b5ff5a15138e9d16cc74eb3aac4560b77be15161d3b3db25b89e1d?context=repo):

  ```bash
  Verification for epamedp/codebase-operator:2.20.0
  The following checks were performed on each of these signatures:
    - The cosign claims were validated
    - The claims were present in the transparency log
    - The signatures were integrated into the transparency log when the certificate was valid
    - The signatures were verified against the specified public key
  {
    "payloadType": "application/vnd.in-toto+json",
    "payload": "eyJfdHlwZSI6Imh0dHBzOi8vaW4tdG90by5pby9TdGF0ZW1lbnQvdjAuMSIsInByZWRpY2F0ZVR5cGUiOiJodHRwczovL3Nsc2EuZGV2L3Byb3ZlbmFuY2UvdjAuMiIsInN1YmplY3QiOlt7Im5hbWUiOiJpbmRleC5kb2NrZXIuaW8vZXBhbWVkcC9jb2RlYmFzZS1vcGVyYXRvciIsImRpZ2VzdCI6eyJzaGEyNTYiOiIzNjU4NWExM2I1YjVmZjVhMTUxMzhlOWQxNmNjNzRlYjNhYWM0NTYwYjc3YmUxNTE2MWQzYjNkYjI1Yjg5ZTFkIn19XSwicHJlZGljYXRlIjp7ImJ1aWxkZXIiOnsiaWQiOiJodHRwczovL3Rla3Rvbi5kZXYvY2hhaW5zL3YyIn0sImJ1aWxkVHlwZSI6InRla3Rvbi5kZXYvdjFiZXRhMS9UYXNrUnVuIiwiaW52b2NhdGlvbiI6eyJjb25maWdTb3VyY2UiOnt9LCJwYXJhbWV0ZXJzIjp7IkJVSUxERVJfSU1BR0UiOiJnY3IuaW8va2FuaWtvLXByb2plY3QvZXhlY3V0b3I6djEuMTIuMS1kZWJ1ZyIsIkNPTlRFWFQiOiIuLyIsIkRPQ0tFUkZJTEUiOiJEb2NrZXJmaWxlIiwiSU1BR0UiOiJlcGFtZWRwL2NvZGViYXNlLW9wZXJhdG9yOjIuMjAuMCIsIklNQUdFX1RBUiI6ImNvZGViYXNlLW9wZXJhdG9yXzIuMjAuMCJ9LCJlbnZpcm9ubWVudCI6eyJhbm5vdGF0aW9ucyI6eyJtZXRhLmhlbG0uc2gvcmVsZWFzZS1uYW1lIjoiZWRwLWN1c3RvbS1waXBlbGluZXMiLCJtZXRhLmhlbG0uc2gvcmVsZWFzZS1uYW1lc3BhY2UiOiJlZHAtZGVsaXZlcnkiLCJwaXBlbGluZS50ZWt0b24uZGV2L2FmZmluaXR5LWFzc2lzdGFudCI6ImFmZmluaXR5LWFzc2lzdGFudC1iZjRkNzRkMWM0IiwicGlwZWxpbmUudGVrdG9uLmRldi9yZWxlYXNlIjoiMjI5OWIxNSIsInRla3Rvbi5kZXYvY2F0ZWdvcmllcyI6IkltYWdlIEJ1aWxkIiwidGVrdG9uLmRldi9kaXNwbGF5TmFtZSI6IkJ1aWxkIGFuZCB1cGxvYWQgY29udGFpbmVyIGltYWdlIHVzaW5nIEthbmlrbyIsInRla3Rvbi5kZXYvcGlwZWxpbmVzLm1pblZlcnNpb24iOiIwLjE3LjAiLCJ0ZWt0b24uZGV2L3BsYXRmb3JtcyI6ImxpbnV4L2FtZDY0IiwidGVrdG9uLmRldi90YWdzIjoiaW1hZ2UtYnVpbGQifSwibGFiZWxzIjp7ImFwcC5rdWJlcm5ldGVzLmlvL21hbmFnZWQtYnkiOiJIZWxtIiwiYXBwLmt1YmVybmV0ZXMuaW8vdmVyc2lvbiI6IjAuOC4wIiwiaGVsbS5zaC9jaGFydCI6ImVkcC1jdXN0b20tcGlwZWxpbmVzLTAuOC4wIiwiazhzbGVucy1lZGl0LXJlc291cmNlLXZlcnNpb24iOiJ2MSIsInRla3Rvbi5kZXYvbWVtYmVyT2YiOiJ0YXNrcyIsInRla3Rvbi5kZXYvcGlwZWxpbmUiOiJnZXJyaXQtb3BlcmF0b3JzLWFwcC1yZWxlYXNlLWVkcCIsInRla3Rvbi5kZXYvcGlwZWxpbmVSdW4iOiJlZHAtY29kZWJhc2Utb3BlcmF0b3ItcmVsZWFzZSIsInRla3Rvbi5kZXYvcGlwZWxpbmVUYXNrIjoia2FuaWtvLWJ1aWxkIiwidGVrdG9uLmRldi90YXNrIjoia2FuaWtvLXJlbGVhc2UifX19LCJidWlsZENvbmZpZyI6eyJzdGVwcyI6W3siZW50cnlQb2ludCI6Ii9rYW5pa28vZXhlY3V0b3IgXFxcbiAgLS1kb2NrZXJmaWxlPS93b3Jrc3BhY2Uvc291cmNlL0RvY2tlcmZpbGUgXFxcbiAgLS1jb250ZXh0PS93b3Jrc3BhY2Uvc291cmNlLy4vIFxcXG4gIC0tZGVzdGluYXRpb249ZXBhbWVkcC9jb2RlYmFzZS1vcGVyYXRvcjoyLjIwLjAgXFxcbiAgLS1kaWdlc3QtZmlsZT0vdGVrdG9uL3Jlc3VsdHMvSU1BR0VfRElHRVNUIFxcXG4gIC0tdGFyLXBhdGg9Y29kZWJhc2Utb3BlcmF0b3JfMi4yMC4wLnRhciBcXFxuIiwiYXJndW1lbnRzIjpudWxsLCJlbnZpcm9ubWVudCI6eyJjb250YWluZXIiOiJidWlsZC1hbmQtcHVzaCIsImltYWdlIjoib2NpOi8vZ2NyLmlvL2thbmlrby1wcm9qZWN0L2V4ZWN1dG9yQHNoYTI1NjphN2VhOWY2OWQ3N2Q3ZTdhMGVhODIxZjE1MDY5YmU0NTQyMGE1MzZmODFhYjU3ODdhOTg4NjU5ZTQ4YzI1Mzc3In0sImFubm90YXRpb25zIjpudWxsfSx7ImVudHJ5UG9pbnQiOiJzZXQgLWVcbmltYWdlPVwiZXBhbWVkcC9jb2RlYmFzZS1vcGVyYXRvcjoyLjIwLjBcIlxuZWNobyAtbiBcIiR7aW1hZ2V9XCIgfCB0ZWUgXCIvdGVrdG9uL3Jlc3VsdHMvSU1BR0VfVVJMXCJcbiIsImFyZ3VtZW50cyI6bnVsbCwiZW52aXJvbm1lbnQiOnsiY29udGFpbmVyIjoid3JpdGUtdXJsIiwiaW1hZ2UiOiJvY2k6Ly9kb2NrZXIuaW8vbGlicmFyeS9hbHBpbmVAc2hhMjU2OjcxNDRmN2JhYjNkNGMyNjQ4ZDdlNTk0MDlmMTVlYzUyYTE4MDA2YTEyOGM3MzNmY2ZmMjBkM2E0YTU0YmE0NGEifSwiYW5ub3RhdGlvbnMiOm51bGx9XX0sIm1ldGFkYXRhIjp7ImJ1aWxkU3RhcnRlZE9uIjoiMjAyMy0xMS0wM1QxMzozMDo1NVoiLCJidWlsZEZpbmlzaGVkT24iOiIyMDIzLTExLTAzVDEzOjMxOjE1WiIsImNvbXBsZXRlbmVzcyI6eyJwYXJhbWV0ZXJzIjpmYWxzZSwiZW52aXJvbm1lbnQiOmZhbHNlLCJtYXRlcmlhbHMiOmZhbHNlfSwicmVwcm9kdWNpYmxlIjpmYWxzZX0sIm1hdGVyaWFscyI6W3sidXJpIjoib2NpOi8vZ2NyLmlvL2thbmlrby1wcm9qZWN0L2V4ZWN1dG9yIiwiZGlnZXN0Ijp7InNoYTI1NiI6ImE3ZWE5ZjY5ZDc3ZDdlN2EwZWE4MjFmMTUwNjliZTQ1NDIwYTUzNmY4MWFiNTc4N2E5ODg2NTllNDhjMjUzNzcifX0seyJ1cmkiOiJvY2k6Ly9kb2NrZXIuaW8vbGlicmFyeS9hbHBpbmUiLCJkaWdlc3QiOnsic2hhMjU2IjoiNzE0NGY3YmFiM2Q0YzI2NDhkN2U1OTQwOWYxNWVjNTJhMTgwMDZhMTI4YzczM2ZjZmYyMGQzYTRhNTRiYTQ0YSJ9fV19fQ==",
    "signatures": [
      {
        "keyid": "SHA256:7E2nAQnycq4vfPlzmLZGzpK/Vr6oXKqqGokDyrBSLck",
        "sig": "MEUCIAZLrA/wTkqmnCZXh85R9Y/Ue5f8wuGgjLMYdoFw9GRLAiEA/sE598EX5fppqbry+xvE+aap8+qHPioOin8t6Ttzx3k="
      }
    ]
  }
  ```

For more details about attestation, please refer to the official [cosign documentation](https://docs.sigstore.dev/verifying/attestation/) page.

## Verify Release Pipeline

Within each release component, you will discover a [Rekor UUID](https://search.sigstore.dev/?uuid=24296fb24b8ad77a671b24f6b83f79e46fe5214cde46ed045ceba35d640a7e017cbc5524e90329ff), which serves to validate the flow of the release pipeline. Execute the following command to obtain comprehensive information about the release pipeline of [codebase-operator](https://github.com/epam/edp-codebase-operator/releases/tag/v2.20.0) with UUID:

```24296fb24b8ad77a671b24f6b83f79e46fe5214cde46ed045ceba35d640a7e017cbc5524e90329ff```:

  ```bash
  rekor-cli get --uuid 24296fb24b8ad77a671b24f6b83f79e46fe5214cde46ed045ceba35d640a7e017cbc5524e90329ff --format json | jq -r .Attestation | jq .
  ```

  The result:

  ```bash
  {
    "_type": "https://in-toto.io/Statement/v0.1",
    "predicateType": "https://slsa.dev/provenance/v0.2",
    "subject": [
      {
        "name": "index.docker.io/epamedp/codebase-operator",
        "digest": {
          "sha256": "36585a13b5b5ff5a15138e9d16cc74eb3aac4560b77be15161d3b3db25b89e1d"
        }
      }
    ],
    "predicate": {
      "builder": {
        "id": "https://tekton.dev/chains/v2"
      },
      "buildType": "tekton.dev/v1beta1/TaskRun",
      "invocation": {
        "configSource": {},
        "parameters": {
          "BUILDER_IMAGE": "gcr.io/kaniko-project/executor:v1.12.1-debug",
          "CONTEXT": "./",
          "DOCKERFILE": "Dockerfile",
          "IMAGE": "epamedp/codebase-operator:2.20.0",
          "IMAGE_TAR": "codebase-operator_2.20.0"
        },
        "environment": {
          "annotations": {
              ...
          },
          "labels": {
              ...
          }
        }
      },
      "buildConfig": {
        "steps": [
          {
            "entryPoint": "/kaniko/executor \\\n  --dockerfile=/workspace/source/Dockerfile \\\n  --context=/workspace/source/./ \\\n  --destination=epamedp/codebase-operator:2.20.0 \\\n  --digest-file=/tekton/results/IMAGE_DIGEST \\\n  --tar-path=codebase-operator_2.20.0.tar \\\n",
            "arguments": null,
            "environment": {
              "container": "build-and-push",
              "image": "oci://gcr.io/kaniko-project/executor@sha256:a7ea9f69d77d7e7a0ea821f15069be45420a536f81ab5787a988659e48c25377"
            },
            "annotations": null
          },
          {
            "entryPoint": "set -e\nimage=\"epamedp/codebase-operator:2.20.0\"\necho -n \"${image}\" | tee \"/tekton/results/IMAGE_URL\"\n",
            "arguments": null,
            "environment": {
              "container": "write-url",
              "image": "oci://docker.io/library/alpine@sha256:7144f7bab3d4c2648d7e59409f15ec52a18006a128c733fcff20d3a4a54ba44a"
            },
            "annotations": null
          }
        ]
      },
      "metadata": {
          ...
      },
      "materials": [
        {
          "uri": "oci://gcr.io/kaniko-project/executor",
          "digest": {
            "sha256": "a7ea9f69d77d7e7a0ea821f15069be45420a536f81ab5787a988659e48c25377"
          }
        },
        {
          "uri": "oci://docker.io/library/alpine",
          "digest": {
            "sha256": "7144f7bab3d4c2648d7e59409f15ec52a18006a128c733fcff20d3a4a54ba44a"
          }
        }
      ]
    }
  }
  ```

By signing all our artifacts, we assure you that they are trustworthy. This guide is indispensable for developers and administrators to enhance their software's reliability and meet modern security standards. The adoption of SLSA will bring you confidence while using the platform.

## Related Articles

* [In-Toto Attestations](https://docs.sigstore.dev/verifying/attestation/)
