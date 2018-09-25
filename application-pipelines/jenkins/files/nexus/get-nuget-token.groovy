import groovy.json.*
import org.sonatype.nexus.security.authc.apikey.ApiKeyStore
import org.apache.shiro.subject.SimplePrincipalCollection

parsed_args = new JsonSlurper().parseText(args)

def getOrCreateNuGetApiKey(String userName) {
    realmName = "NexusAuthenticatingRealm"
    apiKeyDomain = "NuGetApiKey"
    principal = new SimplePrincipalCollection(userName, realmName)
    keyStore = container.lookup(ApiKeyStore.class.getName())
    apiKey = keyStore.getApiKey(apiKeyDomain, principal)
    if (apiKey == null) {
        apiKey = keyStore.createApiKey(apiKeyDomain, principal)
    }
    return apiKey.toString()
}

nuGetApiKey = getOrCreateNuGetApiKey(parsed_args.name)

return JsonOutput.toJson([
        nuGetApiKey: nuGetApiKey
])