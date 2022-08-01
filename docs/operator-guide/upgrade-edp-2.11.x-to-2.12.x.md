# Upgrade EDP v.2.11.x to v.2.12.x

This section provides the details on the EDP upgrade from the v.2.11.x to the v.2.12.x. Explore the actions and requirements below.

!!! Notes
    * EDP now supports Kubernetes 1.22: Ingress Resources use `networking.k8s.io/v1`, and Ingress Operators use CustomResourceDefinition `apiextensions.k8s.io/v1`.
    * EDP Team now delivers its own Gerrit Docker image: [epamedp/edp-gerrit](https://hub.docker.com/r/epamedp/edp-gerrit/tags). It is based on the [openfrontier Gerrit Docker image](https://github.com/openfrontier/docker-gerrit/).

!!! Warning
    In current release, EDP contains Gerrit `v3.6.1`. According to [Official Gerrit Upgrade flow](https://www.gerritcodereview.com/3.6.html#offline-upgrade), a user must initially upgrade to Gerrit `v3.5.2`, and then upgrade to `v3.6.1`. Therefore, define the `gerrit-operator.gerrit.version=3.5.2` value in the edp-install `values.yaml` file. After the successful upgrade, this value can be removed, and the chart will install `v3.6.1`.
