# Instruction How to Run

This section describes how to execute build and run petclinic-backed application in QA/Demo/Production and Local environment.

To run application in local or run integration tests in-memory hsqldb is used.
While QA/Demo/Production can use real db like Postgres.

Petclinic backend build/test/run is configurable using Maven and Spring Boot Configuration profiles.

To select maven profile **-P**  key is used: example:
```sh
$ mvn spring-boot:run -P hsqldb
```

To run spring boot jar, with specific spring configuration profile chosen **spring.profiles.active** java env variable should be specified, example:
```sh
$ java -jar petclinic.jar -Dspring.profiles.active=osenv
```
Maven spring-boot plugin also have special java env **run.profiles** variable to choose spring configuration profile.
```sh
$ spring-boot:run -Drun.profiles=osenv
```

## Liquibase artifact
Most instruction bellow requires liquibase artifact be build and deployed to local maven repository. To do this run this command:

```sh
$ cd database
$  mvn clean install
```

## Maven Test'&'Build

Default configuration to run test is uses hsqldb and liquibase to deploy db schema.
While hsqldb and liquibase is excluded from final JAR artifact.

No extra steps are required to run test and pack jar, bellows commands that can be used on CI as well:

To run test:
```sh
$ cd backend
$  mvn test
```
To generate jar:
```sh
$ cd backend
$  mvn package
```

## Maven Local Run
To run in local environment it is reasonable to use hsqldb and liquibase. Maven profile **hsqldb** can do that.

```sh
$ cd backend
$  mvn spring-boot:run -P hsqldb
```

## JetBrains Idea
In JetBrains Idea it is reasonable to use hsqldb and liquibase. For this reason needs to choose maven profile hsqldb, so JetBrains Idea will import configuration properly.

## QA/Demo/Production
QA/Demo/Production environment will require to use real database like PostgreSQL. For this reason jar can be build with regular
```sh
$ cd backend
$  mvn package
```

However, configuration needs to be externalized. Spring boot profile osenv can be used for this case.

To run petclinic it is necessary to specify configuration as OSN ENV variables first:

```sh
$  export PETCLINIC_JDBC_URL=jdbc:postgresql://postgresql/petclinic
$  export PETCLINIC_JDBC_USER=root
$  export PETCLINIC_JDBC_PASSWORD=root
$  export PETCLINIC_JDBC_DIALECT=org.hibernate.dialect.PostgreSQLDialect
```

Then execute spring boot jar

```sh
$  java -jar petclinic.jar -Dspring.profiles.active=osenv
```

Alternatively, to test os environment variables on local use spring boot maven plugin:
```sh
$ cd backend
$  mvn spring-boot:run -P osenv
```
