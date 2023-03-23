# Protect Endpoints

OAuth2-Proxy is a versatile tool that serves as a reverse proxy, utilizing the OAuth 2.0 protocol with various providers like Google, GitHub, and Keycloak to provide both authentication and authorization.
This guide instructs readers on how to protect their applications' endpoints using OAuth2-Proxy.
By following these steps, users can strengthen their endpoints' security without modifying their current application code.
In the context of EDP, it has integration with the Keycloak OIDC provider, enabling it to link with any component that lacks built-in authentication.

!!! note
    OAuth2-Proxy is disabled by default when installing EDP.

## Prerequisites

* [Keycloak](install-keycloak.md) with OIDC authentication is installed.

## Enable OAuth2-Proxy

Enabling OAuth2-Proxy implies the following general steps:

1. Update your EDP deployment using command `--set 'oauth2_proxy.enabled=true'` **or** the `--values` file by enabling the oauth2_proxy parameter.
2. Check that OAuth2-Proxy is deployed successfully.
3. Enable authentication for your Ingress by adding `auth-signin` and `auth-url` of OAuth2-Proxy to its annotation.<br>

This will deploy and connect OAuth2-Proxy to your application endpoint.

## Enable OAuth2-Proxy on Tekton Dashboard

The example below illustrates how to use OAuth2-Proxy in practice when using the Tekton dashboard:

1. Run `helm upgrade` to update edp-install release:
```bash
helm upgrade --version <version> --set 'oauth2_proxy.enabled=true' edp-install --namespace <edp-project>
```
2. Check that OAuth2-Proxy is deployed successfully.
3. Edit the Tekton dashboard Ingress annotation by adding `auth-signin` and `auth-url` of oauth2-proxy by `kubectl` command:
   ```bash
   kubectl annotate ingress <ingress-name> nginx.ingress.kubernetes.io/auth-signin='https://<ingress-name>-<edp-project>.<dnsWildCard>/oauth2/start?rd=https://$host$request_uri' nginx.ingress.kubernetes.io/auth-url='http://oauth-edp-oauth2-proxy.<edp-project>.svc.cluster.local:8080/oauth2/auth'
   ```
## Related Articles
[Keycloak Installation](install-keycloak.md)<br>
[Keycloak OIDC Installation](configure-keycloak-oidc-eks.md)<br>
[Tekton Installation](install-tekton.md)<br>
