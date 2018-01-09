import http.client
import urllib.parse
from base64 import b64encode
import json
import sys
from xml.dom.minidom import *
import os

def run_groovy_in_nexus (host, port, username, password, script, scriptName):
    #setting creds
    credentials = b64encode(b"%s:%s" % (bytes(username, encoding='utf-8'), bytes(password, encoding='utf-8'))).decode("ascii")
    # clean script in Nexus
    connection = http.client.HTTPConnection(host,port)
    connection.set_debuglevel(7)
    headers = {"Content-type": "application/json", 'Authorization': 'Basic %s' % credentials}
    connection.request("DELETE", "/service/siesta/rest/v1/script/%s" % scriptName, "", headers)
    r1 = connection.getresponse()
    r1.read()
    print(r1.status, r1.reason)
    # copy script to Nexus
    headers = {"Content-type": "application/json", "Accept": "application/json", 'Authorization': 'Basic %s' % credentials}
    params={ "name": "%s" % scriptName, "type": "groovy", "content": "%s" % script }
    connection.request("POST", "/service/siesta/rest/v1/script/", json.dumps(params), headers)
    r1 = connection.getresponse()
    r1.read() #
    print(r1.status, r1.reason)
    # run script in Nexus
    headers = {"Content-type": "text/plain", 'Authorization': 'Basic %s' % credentials}
    connection.request("POST", "/service/siesta/rest/v1/script/%s/run" % scriptName, "", headers)
    r1 = connection.getresponse()
    r1.read() #
    print(r1.status, r1.reason)

def add_creds_to_jenkins (host, port, username, password, credUser, credPass, credDesc):
    #setting creds
    credentials = b64encode(b"%s:%s" % (bytes(username, encoding='utf-8'), bytes(password, encoding='utf-8'))).decode("ascii")
    # clean script in Nexus
    connection = http.client.HTTPConnection(host,port)
    connection.set_debuglevel(7)
    headers = {"Content-type": "application/json", 'Authorization': 'Basic %s' % credentials}
    connection.request("GET", "/crumbIssuer/api/xml?xpath=concat(//crumbRequestField,\":\",//crumb)", "", headers)
    r1 = connection.getresponse()
    #r1.read()
    #print(r1.status, r1.reason)
    crumb = r1.read().decode().split(':')[1]
    #print (crumb)

    # copy script to Nexus
    headers = {"Content-type": "application/x-www-form-urlencoded", 'Authorization': 'Basic %s' % credentials, 'Jenkins-Crumb' : '%s' % crumb}
    params = {
                "": "0",
                "credentials":
                {
                "scope": "GLOBAL",
                "id": "%s" % credUser,
                "username": "%s" % credUser,
                "password": "%s" % credPass,
                "description": "%s" % credDesc,
                "$class": "com.cloudbees.plugins.credentials.impl.UsernamePasswordCredentialsImpl"
                }
        }    
    connection.request("POST", "/credentials/store/system/domain/_/createCredentials", 'json=' + json.dumps(params), headers)
    r1 = connection.getresponse()
    r1.read() #
    print(r1.status, r1.reason)

def main():
    """Main entry point for script."""
    #defining vars
    host = os.environ['NEXUS_HOST'] if os.environ.get('NEXUS_HOST', None) is not None else "nexus"
    port = os.environ['NEXUS_PORT'] if os.environ.get('NEXUS_PORT', None) is not None else "8081"
    username = os.environ['NEXUS_ADMIN'] if os.environ.get('NEXUS_ADMIN', None) is not None else "admin"
    password = os.environ['NEXUS_PASSWORD'] if os.environ.get('NEXUS_PASSWORD', None) is not None else "admin123"
    hostJenkins = os.environ['JENKINS_HOST'] if os.environ.get('JENKINS_HOST', None) is not None else "jenkins"
    portJenkins = os.environ['JENKINS_PORT'] if os.environ.get('JENKINS_PORT', None) is not None else "8080"
    usernameJenkins = os.environ['JENKINS_ADMIN'] if os.environ.get('JENKINS_ADMIN', None) is not None else "admin"
    passwordJenkins = os.environ['JENKINS_PASSWORD'] if os.environ.get('JENKINS_PASSWORD', None) is not None else "password"
    # blob names
    npmBlobName = os.environ['NPM_BLOB_STORAGE'] if os.environ.get('NPM_BLOB_STORAGE', None) is not None else "npm-storage"
    npmBlobPath = npmBlobName
    # repo names
    npmHosted = os.environ['NPM_SNAPSHOTS'] if os.environ.get('NPM_SNAPSHOTS', None) is not None else "npm-internal"
    npmProxy = os.environ['NPM_REGISTRY'] if os.environ.get('NPM_REGISTRY', None) is not None else "npm-registry"
    npmGroup = os.environ['NPM_GROUP'] if os.environ.get('NPM_GROUP', None) is not None else "npm-all"
    # npm common settings
    npmProxyURL = 'https://registry.npmjs.org/'
    npmValidate = 'true'
    npmWritePolicy = 'allow'
    npmMemberRepo = [npmProxy, npmHosted]
    # roles
    npmRoleForReadId = os.environ['NPM_READ_ROLE'] if os.environ.get('NPM_READ_ROLE', None) is not None else "npm-ro"
    npmRoleForReadName = npmRoleForReadId
    npmRoleForReadDesc = npmRoleForReadId
    npmRoleForPublishId = os.environ['NPM_PUBLISH_ROLE'] if os.environ.get('NPM_PUBLISH_ROLE', None) is not None else "npm-all"
    npmRoleForPublishName = npmRoleForPublishId
    npmRoleForPublishDesc = npmRoleForPublishId
    # roles settings
    npmRoleForReadPriv = ['nx-repository-view-npm-*-browse','nx-repository-view-npm-*-read']
    npmRoleForPublishPriv = ['nx-repository-view-npm-*-*']
    # users
    npmReadUser = os.environ['NPM_READ_USER'] if os.environ.get('NPM_READ_USER', None) is not None else "npm-read-user"
    npmReadUserPassword = os.environ['NPM_READ_PASSWORD'] if os.environ.get('NPM_READ_PASSWORD', None) is not None else "%s" % npmReadUser
    npmPublishUser = os.environ['NPM_PUBLISH_USER'] if os.environ.get('NPM_PUBLISH_USER', None) is not None else "npm-publish-user"
    npmPublishUserPassword = os.environ['NPM_PUBLISH_PASSWORD'] if os.environ.get('NPM_PUBLISH_PASSWORD', None) is not None else "%s" % npmPublishUser
    # user with roles
    npmReadUserRole = [npmRoleForReadId]
    npmPublishUserRole = [npmRoleForPublishId]
    # inner script settings
    scriptName = 'nexus-configure'
    # allow to run specific groovy script in Nexus
    script="""existingBlobStore = blobStore.getBlobStoreManager().get('%s')
    if (existingBlobStore == null) {
    blobStore.createFileBlobStore('%s', '%s')
    }""" % (npmBlobName, npmBlobName, npmBlobPath)
    run_groovy_in_nexus (host, port, username, password, script, scriptName)

    script=""" import org.sonatype.nexus.repository.config.Configuration
    configuration = new Configuration(
        repositoryName: '%s',
       recipeName: 'npm-hosted',
       online: true,
       attributes: [
                storage: [
                        writePolicy: '%s'.toUpperCase(),
                        blobStoreName: '%s',
                        strictContentTypeValidation: Boolean.valueOf('%s')
                ]
        ]
    )

    def existingRepository = repository.getRepositoryManager().get('%s')
    if (existingRepository != null) {
        existingRepository.stop()
        configuration.attributes['storage']['blobStoreName'] = existingRepository.configuration.attributes['storage']['blobStoreName']
        existingRepository.update(configuration)
        existingRepository.start()
    } else {
        repository.getRepositoryManager().create(configuration)
    }""" % (npmHosted, npmWritePolicy, npmBlobName, npmValidate, npmHosted)

    run_groovy_in_nexus (host, port, username, password, script, scriptName)

    script="""
    import org.sonatype.nexus.repository.config.Configuration


    configuration = new Configuration(
        repositoryName: '%s',
        recipeName: 'npm-proxy',
        online: true,
        attributes: [
                proxy  : [
                        remoteUrl: '%s',
                        contentMaxAge: 1440.0,
                        metadataMaxAge: 1440.0
                ],
                httpclient: [
                        blocked: false,
                        autoBlock: true,
                        connection: [
                                useTrustStore: false
                        ]
                ],
                storage: [
                        blobStoreName: '%s',
                        strictContentTypeValidation: Boolean.valueOf('%s')
                ],
                negativeCache: [
                        enabled: true,
                        timeToLive: 1440.0
                ]
        ]
    )

    def existingRepository = repository.getRepositoryManager().get('%s')

    if (existingRepository != null) {
		existingRepository.stop()
		configuration.attributes['storage']['blobStoreName'] = existingRepository.configuration.attributes['storage']['blobStoreName']
		existingRepository.update(configuration)
		existingRepository.start()
    } else {
    repository.getRepositoryManager().create(configuration)
	}
    """ % (npmProxy, npmProxyURL, npmBlobName, npmValidate, npmProxy)

    run_groovy_in_nexus (host, port, username, password, script, scriptName)

    script = """
	import org.sonatype.nexus.repository.config.Configuration

	configuration = new Configuration(
			repositoryName: '%s',
			recipeName: 'npm-group',
			online: true,
			attributes: [
					group  : [
							memberNames: %s
					],
					storage: [
							blobStoreName: '%s',
							strictContentTypeValidation: Boolean.valueOf('%s')
					]
			]
	)

	def existingRepository = repository.getRepositoryManager().get('%s')

	if (existingRepository != null) {
		existingRepository.stop()
		configuration.attributes['storage']['blobStoreName'] = existingRepository.configuration.attributes['storage']['blobStoreName']
		existingRepository.update(configuration)
		existingRepository.start()
	} else {
		repository.getRepositoryManager().create(configuration)
	}
	""" % (npmGroup, npmMemberRepo, npmBlobName, npmValidate, npmGroup)

    run_groovy_in_nexus (host, port, username, password, script, scriptName)

    script = """
	import org.sonatype.nexus.security.user.UserManager
	import org.sonatype.nexus.security.role.NoSuchRoleException

	authManager = security.getSecuritySystem().getAuthorizationManager(UserManager.DEFAULT_SOURCE)

	def existingRole = null

	npmRoleId = '%s'
	npmRoleName = '%s'
	npmRoleDesc = '%s'

	try {
		existingRole = authManager.getRole(npmRoleId)
	} catch (NoSuchRoleException ignored) {
		// could not find role
	}

	//privileges = (parsed_args.privileges == null ? new HashSet() : parsed_args.privileges.toSet())
	//roles = (parsed_args.roles == null ? new HashSet() : parsed_args.roles.toSet())
	privileges = %s
	roles = new HashSet()
	if (existingRole != null) {
		existingRole.setName(npmRoleName)
		existingRole.setDescription(npmRoleDesc)
		existingRole.setPrivileges(privileges)
		existingRole.setRoles(roles)
		authManager.updateRole(existingRole)
	} else {
		security.addRole(npmRoleId, npmRoleName, npmRoleDesc, privileges, roles.toList())
	}
	""" % (npmRoleForReadId, npmRoleForReadName, npmRoleForReadDesc, npmRoleForReadPriv)

    run_groovy_in_nexus (host, port, username, password, script, scriptName)

    script = """
	import org.sonatype.nexus.security.user.UserManager
	import org.sonatype.nexus.security.role.NoSuchRoleException

	authManager = security.getSecuritySystem().getAuthorizationManager(UserManager.DEFAULT_SOURCE)

	def existingRole = null

	npmRoleId = '%s'
	npmRoleName = '%s'
	npmRoleDesc = '%s'

	try {
		existingRole = authManager.getRole(npmRoleId)
	} catch (NoSuchRoleException ignored) {
		// could not find role
	}

	//privileges = (parsed_args.privileges == null ? new HashSet() : parsed_args.privileges.toSet())
	//roles = (parsed_args.roles == null ? new HashSet() : parsed_args.roles.toSet())
	privileges = %s
	roles = new HashSet()
	if (existingRole != null) {
		existingRole.setName(npmRoleName)
		existingRole.setDescription(npmRoleDesc)
		existingRole.setPrivileges(privileges)
		existingRole.setRoles(roles)
		authManager.updateRole(existingRole)
	} else {
		security.addRole(npmRoleId, npmRoleName, npmRoleDesc, privileges, roles.toList())
	}
    """ % (npmRoleForPublishId, npmRoleForPublishName, npmRoleForPublishDesc, npmRoleForPublishPriv)

    run_groovy_in_nexus (host, port, username, password, script, scriptName)
    script = """
	import org.sonatype.nexus.security.user.UserNotFoundException
	userName = '%s'
	userPassword = '%s'
	userFirstName = '%s'
	userLastName = '%s'
	userEmail = '%s'
	roles = %s

	try {
		// update an existing user
		user = security.securitySystem.getUser(userName)
		user.setFirstName(userFirstName)
		user.setLastName(userLastName)
		user.setEmailAddress(userEmail)
		security.securitySystem.updateUser(user)
		security.setUserRoles(userName, roles)
		security.securitySystem.changePassword(userName, userPassword)
	} catch(UserNotFoundException ignored) {
		// create the new user
		security.addUser(userName, userFirstName, userLastName, userEmail, true, userPassword, roles)
	}
	""" % (npmReadUser, npmReadUserPassword, npmReadUser, npmReadUser, '%s@test.com' % npmReadUser, npmReadUserRole )

    run_groovy_in_nexus (host, port, username, password, script, scriptName)

    script = """
	import org.sonatype.nexus.security.user.UserNotFoundException
	userName = '%s'
	userPassword = '%s'
	userFirstName = '%s'
	userLastName = '%s'
	userEmail = '%s'
	roles = %s

	try {
		// update an existing user
		user = security.securitySystem.getUser(userName)
		user.setFirstName(userFirstName)
		user.setLastName(userLastName)
		user.setEmailAddress(userEmail)
		security.securitySystem.updateUser(user)
		security.setUserRoles(userName, roles)
		security.securitySystem.changePassword(userName, userPassword)
	} catch(UserNotFoundException ignored) {
		// create the new user
		security.addUser(userName, userFirstName, userLastName, userEmail, true, userPassword, roles)
	}
	""" % (npmPublishUser, npmPublishUserPassword, npmPublishUser, npmPublishUser, '%s@test.com' % npmPublishUser, npmPublishUserRole )

    run_groovy_in_nexus (host, port, username, password, script, scriptName)

    script = """
	import org.sonatype.nexus.security.realm.RealmManager

	realmManager = container.lookup(RealmManager.class.getName())
	realmManager.enableRealm("NpmToken",true)
	"""

    run_groovy_in_nexus (host, port, username, password, script, scriptName)
    # add credentials to Jenkins
    add_creds_to_jenkins (hostJenkins, portJenkins, usernameJenkins, passwordJenkins, npmReadUser, npmReadUserPassword, "This is an account to read from NPM repositories")
    add_creds_to_jenkins (hostJenkins, portJenkins, usernameJenkins, passwordJenkins, npmPublishUser, npmPublishUserPassword, "This is an account to publish to NPM repositories")
if __name__ == "__main__":
    main()
