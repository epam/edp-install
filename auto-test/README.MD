To run the tests please following mvn command:
mvn test -DocpEdpSuffix=EDP_ENV_SUFFIX -Dsurefire.suiteXmlFiles=TESTNG_SUITE 

Example: run 2 testng test suites against MR environment (mr-57-24) following command should be executed:
mvn test -DocpEdpSuffix=mr-57-24 -Dsurefire.suiteXmlFiles=testng-smoke-suite.xml,testng-business-suite.xml
