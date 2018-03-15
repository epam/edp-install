package com.googlesource.gerrit.plugins;


import com.google.common.base.Strings;
import com.google.gerrit.common.TimeUtil;
import com.google.gerrit.extensions.annotations.PluginName;
import com.google.gerrit.pgm.init.api.AllProjectsConfig;
import com.google.gerrit.pgm.init.api.ConsoleUI;
import com.google.gerrit.pgm.init.api.InitFlags;
import com.google.gerrit.pgm.init.api.InitStep;
import com.google.gerrit.reviewdb.client.*;
import com.google.gerrit.reviewdb.server.ReviewDb;
import com.google.gwtorm.server.OrmException;
import com.google.gwtorm.server.ResultSet;
import com.google.gwtorm.server.SchemaFactory;
import com.google.inject.Inject;

import org.apache.commons.validator.routines.EmailValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class AddUser implements InitStep {

    private final static Logger logger = LoggerFactory.getLogger(AddUser.class);
    private final static String NON_INTERACTIVE_USERS = "Non-Interactive Users";
    private final static String ADMINISTRATORS = "Administrators";
    private final static String SSH_PREFIX = "id_";
    private final static String SSH_SUFFIX = "_rsa.pub";

    private final ConsoleUI ui;
    private final InitFlags flags;
    private final String pluginName;
    private final AllProjectsConfig allProjectsConfig;
    private SchemaFactory<ReviewDb> dbFactory;
    private ReviewDb db;

    private HashMap<Integer, String> groupsMap;

    private String admin_user;
    private String admin_email;
    private String admin_fullname;
    private String admin_pwd;
    private String users;
    private String sshPath;
    private String sshPrefix;
    private String sshSuffix;

    @Inject
    AddUser(@PluginName String pluginName, ConsoleUI ui,
            AllProjectsConfig allProjectsConfig, InitFlags flags) {
        this.pluginName = pluginName;
        this.allProjectsConfig = allProjectsConfig;
        this.flags = flags;
        this.ui = ui;
        defineGroups();
    }

    @Inject(optional = true)
    void set(SchemaFactory<ReviewDb> dbFactory) {
        this.dbFactory = dbFactory;
    }

    private void defineGroups() {
        groupsMap = new HashMap<Integer, String>();
        groupsMap.put(1, ADMINISTRATORS);
        groupsMap.put(2, NON_INTERACTIVE_USERS);
    }

    private Integer getKeyByValue(String stringToSearch) {
        for (Map.Entry<Integer, String> e : groupsMap.entrySet()) {
            Object value = e.getValue();
            if (value.equals(stringToSearch)) {
                return e.getKey();
            }
        }
        return null;
    }

    @Override
    public void run() throws Exception {
    }

    @Override
    public void postRun() throws Exception {
        AuthType authType =
                flags.cfg.getEnum(AuthType.values(), "auth", null, "type", null);
        if (authType != AuthType.DEVELOPMENT_BECOME_ANY_ACCOUNT) {
            return;
        }
        logger.info("Auth Type : " + authType);

        // Retrieve env variables
        admin_user = System.getenv("GERRIT_ADMIN_USER");
        admin_email = System.getenv("GERRIT_ADMIN_EMAIL");
        admin_fullname = System.getenv("GERRIT_ADMIN_FULLNAME");
        admin_pwd = System.getenv("GERRIT_ADMIN_PWD");
        users = System.getenv("GERRIT_ACCOUNTS");
        sshPath = System.getenv("GERRIT_PUBLIC_KEYS_PATH");
        sshPrefix = lookupFromEnvironmentVariables("GERRIT_USER_PUBLIC_KEY_PREFIX", SSH_PREFIX);
        sshSuffix = lookupFromEnvironmentVariables("GERRIT_USER_PUBLIC_KEY_SUFFIX", SSH_SUFFIX);


        db = dbFactory.open();

        try {
            // must have an admin fullname for us to do any other steps
            if (admin_fullname != null) {
                List<Account> admins = searchAccount(admin_fullname);
                if (admins.isEmpty()) {
                    // TODO - Review this code
                    add();
                } else {
                    for (Account account : admins) {
                        update(account, admin_user, admin_fullname, admin_email, admin_pwd, null);
                    }
                }


                if (users != null) {
                    String[] records = users.split(";");

                    if (records != null) {
                        for (String entry : records) {
                            String[] userData = entry.split(",");
                            if (userData != null) {
                                List<Account> accounts = searchAccount(userData[0]);
                                if (accounts.isEmpty()) {
                                    add(userData[0], userData[1], userData[2], userData[3], userData[4]);
                                } else {
                                    for (Account account : accounts) {
                                        update(account, userData[0], userData[1], userData[2], userData[3], userData[4]);
                                    }
                                }
                            }
                        }
                    }
                }
            }

        } finally {
            db.close();
        }
    }

    private String lookupFromEnvironmentVariables(String env, String defaultValue) {
        String rc = System.getenv(env);
        if (rc == null || rc.isEmpty()) {
            return defaultValue;
        }
        return rc;
    }

    // TODO Review code to add first ADMIN USER
    private void add() throws OrmException, IOException {
        ui.header("Gerrit Administrator");
        logger.info("Create administrator user");
        Account.Id id = new Account.Id(db.nextAccountId());
        String username = ui.readString("admin", "username");
        String name = ui.readString("Administrator", "name");
        String httpPassword = ui.readString("secret", "HTTP password");
        AccountSshKey sshKey = retrieveSshKey(username, id);
        String email = readEmail(sshKey);
        AccountExternalId extUser =
                new AccountExternalId(id, new AccountExternalId.Key(
                        AccountExternalId.SCHEME_USERNAME, username));
        if (!Strings.isNullOrEmpty(httpPassword)) {
            extUser.setPassword(httpPassword);
        }
        db.accountExternalIds().insert(Collections.singleton(extUser));

        if (email != null) {
            AccountExternalId extMailto =
                    new AccountExternalId(id, new AccountExternalId.Key(
                            AccountExternalId.SCHEME_MAILTO, email));
            extMailto.setEmailAddress(email);
            db.accountExternalIds().insert(Collections.singleton(extMailto));
        }

        Account a = new Account(id, TimeUtil.nowTs());
        a.setFullName(name);
        a.setPreferredEmail(email);
        db.accounts().insert(Collections.singleton(a));

        AccountGroupMember m =
                new AccountGroupMember(new AccountGroupMember.Key(id,
                        new AccountGroup.Id(1)));
        db.accountGroupMembers().insert(Collections.singleton(m));

        if (sshKey != null) {
            db.accountSshKeys().insert(Collections.singleton(sshKey));
        }
    }

    private void add(String user, String fullname, String email, String httpPassword, String groups) throws OrmException, IOException {

        logger.info("Create user : " + user);
        System.out.println("Create user : " + user);
        Account.Id id = new Account.Id(db.nextAccountId());

        AccountSshKey sshKey = retrieveSshKey(user, id);

        AccountExternalId extUser =
                new AccountExternalId(id, new AccountExternalId.Key(
                        AccountExternalId.SCHEME_USERNAME, user));
        if (!Strings.isNullOrEmpty(httpPassword)) {
            extUser.setPassword(httpPassword);
        }
        db.accountExternalIds().insert(Collections.singleton(extUser));

        if (email != null) {
            AccountExternalId extMailto =
                    new AccountExternalId(id, new AccountExternalId.Key(
                            AccountExternalId.SCHEME_MAILTO, email));
            extMailto.setEmailAddress(email);
            db.accountExternalIds().insert(Collections.singleton(extMailto));
        }

        Account a = new Account(id, TimeUtil.nowTs());
        a.setUserName(user);
        a.setFullName(fullname);
        a.setPreferredEmail(email);
        db.accounts().insert(Collections.singleton(a));

        if (groups != null) {
            for (String group : groups.split(":")) {
                if (group != null) {
                    AccountGroupMember m = new AccountGroupMember(new AccountGroupMember.Key(id, new AccountGroup.Id(getKeyByValue(group))));
                    db.accountGroupMembers().insert(Collections.singleton(m));
                }
            }
        }

        if (sshKey != null) {
            db.accountSshKeys().insert(Collections.singleton(sshKey));
        }
    }

    private void update(Account account, String user, String fullname, String email, String pwd, String groups) throws OrmException, IOException {

        Account.Id id = account.getId();

        if (id.equals(ADMIN_ACCOUNT_ID())) {
            logger.info("Update user : " + user);
            System.out.println("Update user : " + user);
            account.setUserName(user);
            account.setFullName(fullname);
            account.setPreferredEmail(email);
            db.accounts().update(Collections.singleton(account));

            AccountExternalId.Key extId_key = new AccountExternalId.Key(AccountExternalId.SCHEME_USERNAME, account.getUserName());
            AccountExternalId extUser = db.accountExternalIds().get(extId_key);
            if (extUser != null) {
                extUser.setPassword((pwd == null) ? "secret" : pwd);
                db.accountExternalIds().update(Collections.singleton(extUser));
            }

            AccountSshKey sshKey = retrieveSshKey(user, id);
            System.out.println("SSH Public Key retrieved : " + sshKey.getSshPublicKey());

            if (sshKey != null) {
                ResultSet<AccountSshKey> resuts =
                        db.accountSshKeys().byAccountLast(id);

                if (resuts.toList().isEmpty()) {
                    db.accountSshKeys().insert(Collections.singleton(sshKey));
                } else {
                    logger.info("Public SSH Key already exist in Gerrit : " + resuts.toList().get(0).getSshPublicKey() + ", for the user : " + id);
                    System.out.println("Public SSH Key already exist in Gerrit : " + resuts.toList().get(0).getSshPublicKey() + ", for the user : " + id);
                }
            }

        } else {
            logger.info("Update user : " + user);
            System.out.println("Update user : " + user);
            account.setUserName(user);
            account.setFullName(fullname);
            account.setPreferredEmail(email);
            db.accounts().update(Collections.singleton(account));

            AccountExternalId.Key extId_key = new AccountExternalId.Key(AccountExternalId.SCHEME_USERNAME, account.getUserName());
            AccountExternalId extUser = db.accountExternalIds().get(extId_key);
            if (extUser != null) {
                extUser.setPassword((pwd == null) ? "secret" : pwd);
                db.accountExternalIds().update(Collections.singleton(extUser));
            }

            AccountSshKey sshKey = retrieveSshKey(user, id);
            System.out.println("SSH Public Key retrieved : " + sshKey.getSshPublicKey());

            if (sshKey != null) {
                db.accountSshKeys().insert(Collections.singleton(sshKey));
            }
        }


    }

    private String readEmail(AccountSshKey sshKey) {
        String defaultEmail = "admin@gmail.com";
        if (sshKey != null && sshKey.getComment() != null) {
            String c = sshKey.getComment().trim();
            if (EmailValidator.getInstance().isValid(c)) {
                defaultEmail = c;
            }
        }
        return readEmail(defaultEmail);
    }

    private String readEmail(String defaultEmail) {
        String email = ui.readString(defaultEmail, "email");
        if (email != null && !EmailValidator.getInstance().isValid(email)) {
            ui.message("error: invalid email address\n");
            return readEmail(defaultEmail);
        }
        return email;
    }

    private AccountSshKey retrieveSshKey(String user, Account.Id id) throws IOException {
        String userPublicSshKeyFile = "";
        String sshKeyFileToSearch = sshPrefix + user + sshSuffix;
        logger.info("SSH Key to search : " + sshKeyFileToSearch + ", for the user : " + user);
        System.out.println("SSH Key to search : " + sshKeyFileToSearch + ", for the user : " + user);
        
        Path userPublicSshKeyPath = Paths.get(sshPath, sshKeyFileToSearch);
        if (Files.exists(userPublicSshKeyPath)) {
            userPublicSshKeyFile = userPublicSshKeyPath.toString();
        }
        return createSshKey(id, userPublicSshKeyFile);

    }

    private AccountSshKey createSshKey(Account.Id id, String keyFile)
            throws IOException {
        Path p = Paths.get(keyFile);
        if (!Files.exists(p)) {
            throw new IOException(String.format(
                    "Cannot add public SSH key: %s is not a file", keyFile));
        }
        String content = null;
        try {
            content = new String(Files.readAllBytes(p), StandardCharsets.UTF_8);
            logger.info("Content of the SSH Key retrieved : " + content + ", for the user : " + id.toString());
            System.out.println("Content of the SSH Key retrieved : " + content + ", for the user : " + id.toString());
            return new AccountSshKey(new AccountSshKey.Id(id, 0), content);
        } catch (Exception e) {
            logger.info("Cannot read the ssh key file: " + keyFile);
            logger.info("Will continue along, but will not function as expected... there will be no SSH key for: " + id);
            System.out.println("Cannot read the ssh key file: " + keyFile);
            System.out.println("Will continue along, but will not function as expected... there will be no SSH key for: " + id);
        }

        return null;
    }

    private static Account.Id ADMIN_ACCOUNT_ID() {
        return Account.Id.parse("1000000");
    }

    private List<Account> searchAccount(String fullname) throws OrmException {
        ResultSet<Account> result = db.accounts().byFullName(fullname);
        List<Account> accounts = result.toList();

        if (!accounts.isEmpty()) {
            return accounts;
        } else {
            return new ArrayList<Account>();
        }
    }
}
