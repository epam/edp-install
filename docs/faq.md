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

## How To Change the Lifespan of an Access Token That Is Used for Headlamp and 'oidc-login' Plugin?

Change the Access Token Lifespan: go to your Keycloak and select *Openshift realm* > *Realm settings* > *Tokens* >
*Access Token Lifespan* > set a new value to the field and save this change.

By default, "Access Token Lifespan" value is 5 minutes.

!![Access Token Lifespan](./assets/faq/keycloak-access-token-lifespan.png "Access Token Lifespan")