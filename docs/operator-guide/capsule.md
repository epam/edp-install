# Capsule Integration

This documentation guide provides comprehensive instructions of integrating Capsule with the EPAM Delivery Platform to enhance security and resource management.

!!! note
    When integrating the EPAM Delivery Platform with Capsule, it's essential to understand that the platform needs administrative rights to make and oversee resources. This requirement might raise security concerns, but it's important to clarify that it only pertains to the deployment process within the platform.<br>
    There is an alternative approach available. You can manually create permissions for each deployment flow. This alternative method can be used to address and lessen these security concerns.

## Installation

To install the Capsule tool, use the [Cluster Add-Ons](https://github.com/epam/edp-cluster-add-ons) approach. For more details, please refer to the [Capsule](https://capsule.clastix.io/docs/general) official page.

## Configuration

To use Capsule in EDP, follow the steps below:


1. Run the command below to upgrade EDP with Capsule capabilities:

  ```bash
  helm upgrade --install edp epamedp/edp-install -n edp --values values.yaml --set cd-pipeline-operator.tenancyEngine=capsule
  ```

2. Open the `CapsuleConfiguration` custom resource called `default`:

  ```bash
  kubectl edit CapsuleConfiguration default
  ```

  Add the tenant name (by default, it's the EDP namespace name) to the manifest's spec section as follows:

  ```yaml
  spec:
    userGroups:
      - system:serviceaccounts:edp
  ```

As a result, EDP will be using Capsule capabilities to manage tenants, thus providing better access management.

## Related Articles

* [Install EDP With Values File](install-edp.md)
* [Cluster Add-Ons Overview](add-ons-overview.md)
* [Set Up Kiosk](capsule.md)
* [EDP Kiosk Usage](edp-kiosk-usage.md)