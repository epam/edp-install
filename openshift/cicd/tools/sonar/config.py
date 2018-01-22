import json
import sys
from xml.dom.minidom import *
import os
import time
import requests


class SonarApi:
    def __init__(self, **kwargs):
        self.host = kwargs["host"]
        self.port = kwargs["port"]
        self.username = kwargs["username"]
        self.password = kwargs["password"]
        self.credentials = (self.username, self.password)

    def check_user_exist(self, login):
        response = requests.get('http://' + self.host + ':' + self.port + '/api/users/search?login=%s' % login,
                                auth=self.credentials)
        for user in json.loads(response.text)['users']:
            if user['login'] == login:
                return True
        return False

    def create_user(self, login, name, password):
        if not self.check_user_exist(login):
            data = {'login': login, 'name': name, 'password': password}
            requests.post('http://' + self.host + ':' + self.port + '/api/users/create', data=data,
                          auth=self.credentials)
            print("[DEBUG] User %s has been created" % login)
        else:
            print("[DEBUG] User %s is already exist" % login)

    def check_token_exist(self, login, token_name):
        response = requests.get('http://' + self.host + ':' + self.port + '/api/user_tokens/search?login=%s' % login,
                                auth=self.credentials)
        for token in json.loads(response.text)['userTokens']:
            if token['name'] == token_name:
                return True
        return False

    def generate_token(self, login, token_name):
        if not self.check_token_exist(login, token_name):
            data = {'login': login, 'name': token_name}
            response = requests.post('http://' + self.host + ':' + self.port + '/api/user_tokens/generate', data=data,
                                     auth=self.credentials)

            print("[DEBUG] Token %s for login %s has been created" % (token_name, login))
            return json.loads(response.text)['token']
        else:
            print("[DEBUG] Token %s for login %s is already exist" % (token_name, login))

    def check_group_exist(self, name):
        response = requests.get('http://' + self.host + ':' + self.port + '/api/user_groups/search?q=%s&f=name' % name,
                                auth=self.credentials)
        if json.loads(response.text)['groups']:
            return True
        return False

    def create_group(self, name):
        if not self.check_group_exist(name):
            data = {'name': name}
            requests.post('http://' + self.host + ':' + self.port + '/api/user_groups/create', data=data,
                          auth=self.credentials)
            print("[DEBUG] Group %s has been created" % name)
        else:
            print("[DEBUG] Group %s is already exist" % name)

    def add_user_to_group(self, username, group):
        data = {'name': group, 'login': username}
        requests.post('http://' + self.host + ':' + self.port + '/api/user_groups/add_user', data=data,
                      auth=self.credentials)

    def add_permission_to_user(self, login, permission):
        if permission not in ['admin', 'codeviewer', 'issueadmin', 'scan', 'user']:
            print(
                "[WARNING] Permission %s is not allowed, possible values are admin, codeviewer, issueadmin, scan, user" % permission)
            return False
        data = {'login': login, 'permission': 'scan'}
        requests.post('http://' + self.host + ':' + self.port + '/api/permissions/add_user', data=data,
                      auth=self.credentials)
        print("[DEBUG] Permission %s for user %s has been granted" % (permission, login))
        return True

    def get_profile_id(self, quality_profile):
        response = requests.get(
            'http://' + self.host + ':' + self.port + '/api/qualityprofiles/search?qualityProfile=%s' % quality_profile,
            auth=self.credentials)
        profile_id = json.loads(response.text)['profiles'][0]['key']
        print("[DEBUG] Profile id of %s profile is %s" % (quality_profile, profile_id))
        return profile_id

    def activate_profile_rules(self, target_key, languages, statuses):
        data = {'activation': 'true', 'languages': languages, 'targetKey': target_key, 'statuses': statuses}
        requests.post('http://' + self.host + ':' + self.port + '/api/qualityprofiles/activate_rules', data=data,
                      auth=self.credentials)

    def deactivate_profile_rules(self, target_key, statuses):
        data = {'targetKey': target_key, 'statuses': statuses}
        requests.post('http://' + self.host + ':' + self.port + '/api/qualityprofiles/deactivate_rules', data=data,
                      auth=self.credentials)

    def set_default_profile(self, profile, language):
        data = {'qualityProfile': profile, 'language': language}
        requests.post('http://' + self.host + ':' + self.port + '/api/qualityprofiles/set_default', data=data,
                      auth=self.credentials)

    def restore_profile(self, xml_location):
        files = {'backup': (xml_location, open(xml_location, 'r'))}
        requests.post('http://' + self.host + ':' + self.port + '/api/qualityprofiles/restore', files=files,
                      auth=self.credentials)

    def restart(self):
        requests.post('http://' + self.host + ':' + self.port + '/api/system/restart',
                      auth=self.credentials)

    def get_status(self):
        response = requests.get('http://' + self.host + ':' + self.port + '/api/system/status',
                                auth=self.credentials)
        return json.loads(response.text)['status']

    def check_plugin_installed(self, plugin_key):
        response = requests.get('http://' + self.host + ':' + self.port + '/api/plugins/installed',
                                auth=self.credentials)
        for plugin in json.loads(response.text)['plugins']:
            if plugin['key'] == plugin_key:
                return True
        return False

    def install_plugin(self, plugin_key):
        if not self.check_plugin_installed(plugin_key):
            data = {'key': plugin_key}
            requests.post('http://' + self.host + ':' + self.port + '/api/plugins/install', data=data,
                          auth=self.credentials)
            print("[DEBUG] Plugin %s has been installed" % plugin_key)
            return True
        else:
            print("[DEBUG] Plugin %s is already installed" % plugin_key)
            return False


def main():
    sonar_data = {}
    sonar_data["host"] = os.environ['SONAR_HOST'] if os.environ.get('SONAR_HOST',
                                                                    None) is not None else "sonar"
    sonar_data["port"] = os.environ['SONAR_PORT'] if os.environ.get('SONAR_PORT', None) is not None else "9000"
    sonar_data["username"] = os.environ['SONAR_ADMIN'] if os.environ.get('SONAR_ADMIN', None) is not None else "admin"
    sonar_data['password'] = os.environ['SONAR_PASSWORD'] if os.environ.get('SONAR_PASSWORD',
                                                                            None) is not None else "admin"

    auto_user = os.environ['AUTO_USER'] if os.environ.get('AUTO_USER', None) is not None else "jenkins"
    auto_user_password = os.environ['AUTO_USER_PASSWORD'] if os.environ.get('AUTO_USER_PASSWORD',
                                                                            None) is not None else "jenkins"
    sonar = SonarApi(**sonar_data)

    need_reboot = False
    if sonar.install_plugin("pmd"):
        need_reboot = True
    if sonar.install_plugin("findbugs"):
        need_reboot = True
    if sonar.install_plugin("checkstyle"):
        need_reboot = True

    if need_reboot:
        print("[DEBUG] Plugins have been installed, rebooting server")
        sonar.restart()
        sonar_status = 'DOWN'
        while sonar_status != 'UP':
            try:
                sonar = SonarApi(**sonar_data)
                if sonar.get_status() == 'UP':
                    sonar_status = "UP"
            except Exception:
                time.sleep(30)
                print("[DEBUG] Sonar is not alive yet, waiting...")
        print("[DEBUG] Sonar is ready")

    print("[DEBUG] Uploading EngEx profile")
    sonar.restore_profile("/tmp/sonar/java-sonar-way-ext.xml")

    profile_name = "Sonar way ext"
    profile_id = sonar.get_profile_id(profile_name.replace(" ", "+"))

    sonar.set_default_profile(profile_name, 'java')
    sonar.activate_profile_rules(profile_id, 'java', 'READY')

    sonar.deactivate_profile_rules(profile_id, 'DEPRECATED')
    sonar.create_user(auto_user, auto_user, auto_user_password)
    sonar.create_group("non-interactive-users")
    sonar.add_user_to_group(auto_user, 'non-interactive-users')
    if not sonar.add_permission_to_user(auto_user, 'scan'):
        print("[ERROR] Can't add permission scan for user %s" % auto_user)
        return 1

    jenkins_token = sonar.generate_token(auto_user, "Jenkins")
    if not jenkins_token:
        return 0

    jenkins_data = {}
    jenkins_data['path'] = "/opt/data/jenkins"
    plugin_config_file = "%s/hudson.plugins.sonar.SonarGlobalConfiguration.xml" % jenkins_data['path']
    if not os.path.isfile(plugin_config_file):
        print(
            "[DEBUG] There is no file %s for configuration, please make sure that Sonar plugin is installed in Jenkins")
        return 0

    parsed_xml = parse(plugin_config_file)
    token_tag = parsed_xml.getElementsByTagName('serverAuthenticationToken')[0]
    token_tag.firstChild.replaceWholeText(jenkins_token)
    file = open(plugin_config_file, 'w')
    parsed_xml.writexml(file)
    file.close()


if __name__ == "__main__":
    sys.exit(main())
