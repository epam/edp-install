# Update Sonar Project Key for EDP v.2.10.3
##### Breaking Changes

When updating EDP to v.2.10.3, it is necessary to apply some changes to sonar stage that will operate the SonarQube projects for every application and their branches (e.g. _MyApp-master_), thus allowing to analyze statistics for each branch separately.

Such actions are required to be followed with the aim to store the SonarQube statistics from the previous EDP version: 

!!! Warning
     Do not run any pipeline with the updated **sonar stage** on any existing application before the completion of the first step.
     
1. Update the project key in SonarQube from old to new format by adding the default branch name.
  - Navigate to Project Settings -> Update Key:
  ![update_sonar_project_key](../assets/operator-guide/sonar-project1.png)
  - Enter the default branch name and click Update:
  ![update_sonar_project_key](../assets/operator-guide/sonar-project2.png)
2. Update the shared library to use a new version of **sonar stage**.
3. As the result, after the first run, the project name will be changed to a new format containing all previous statistics:

  ![sonar_project_activity](../assets/operator-guide/sonar-project_stat1.png)