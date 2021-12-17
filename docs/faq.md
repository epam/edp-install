# FAQ

## How Do I Set Parallel Reconciliation for a Number of Codebase Branches?

Set the CODEBASE_BRANCH_MAX_CONCURRENT_RECONCILES Env variable in codebase-operator by updating Deployment template. For example:

```bash

          ...
          env:
            - name: WATCH_NAMESPACE
          ...

            - name: CODEBASE_BRANCH_MAX_CONCURRENT_RECONCILES
              value: 10
          ...
```

It's not recommended to set the value above 10.