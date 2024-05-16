# Application Already Exists Error (Gerrit VCS)

## Problem

User receives an error message when creating applications using Gerrit as a Git Server provider.

## Cause

Gerrit operator might get stuck during its work.

## Solution

Restarting the Gerrit-related pods can be a solution to the problem:

1. Check the `GerritProject`, `CodebaseImageStream`, `CodebaseBranch`, and `Codebase` custom resources related to the previously created application:

  ```bash
  kubectl get CodebaseBranch -n edp
  kubectl get CodebaseImageStream -n edp
  kubectl get Codebase -n edp
  kubectl get GerritProject -n edp
  ```

2. Delete the custom resources that relate to the problem application using the `kubectl delete` command.

3. Check the pods in your project namespace:

  ```bash
  kubectl get pods -n edp
  ```

4. Delete the `gerrit-operator` and `gerrit` pods so the replica set will be able to spin up new pods:

  ```bash
  kubectl get pods -n edp
  ```

5. Create the application again. Now it is supposed to create application successfully.


## Related Articles

* [Add Application](../../user-guide/add-application.md)