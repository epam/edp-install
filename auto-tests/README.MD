Copyright 2018 EPAM Systems.

Licensed under the Apache License, Version 2.0 (the "License");  
you may not use this file except in compliance with the License.  
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software  
distributed under the License is distributed on an "AS IS" BASIS,  
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  
See the License for the specific language governing permissions and  
limitations under the License.

To run the tests please following mvn command:
mvn test -DocpEdpPrefix=EDP_ENV_PREFIX -Dsurefire.suiteXmlFiles=TESTNG_SUITE

Example: run 2 testng test suites against MR environment (mr-57-24) following command should be executed:
mvn test -DocpEdpPrefix=mr-57-24 -Dsurefire.suiteXmlFiles=testng-smoke-suite.xml,testng-business-suite.xml
