# Use chart testing tool for Code Review

This section contains the description of **helm-lint** stage which one can use in Code-Review pipeline.

These stage helps obtain quick call on the validity of the code in the Code-Review pipeline for all types of applications supported by EDP out of the box.

  ![add_custom_lib2](../assets/user-guide/stages1.png)

Inspect the functions performed by the following stages:

1. [**helm-lint**](https://github.com/helm/chart-testing#chart-testing) stage launches the _ct lint --charts-deploy-templates/_ command in order to validate the chart.

## Related Articles

* [EDP Pipeline Framework](pipeline-framework.md)
