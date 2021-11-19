# EDP Project Rules. Working Process

This page contains the details on the project rules and working process for EDP team and contributors. Explore the main points about working with Gerrit, following the main commit flow, as well as the details about commit types and message below.

## Project Rules

Before starting the development, please check the project rules:

1. It is highly recommended to become familiar with the Gerrit flow. For details, please refer to the [Gerrit official documentation](https://gerrit-documentation.storage.googleapis.com/Documentation/3.4.1/index.html) and pay attention to the main points:

  a. Voting in Gerrit

  b. Resolution of Merge Conflict

  c. Comments resolution

  d. One Jira task should have one Merge Request (MR). If there are many changes within one MR, add the next patch set to the open MR by selecting the _Amend commit_ check box.

2. Only the Assignee is responsible for the MR merge and Jira task status.

4. Every MR should be merged in a timely manner.

5. Log time to Jira ticket.

## Working Process

With EDP, the main workflow is based on the getting a Jira task and creating a Merge Request according to the rules described below.

!!! Workflow
    Get Jira task → implement, verify by yourself the results →  create Merge Request (MR) → send for review → resolve comments/add changes, ask colleagues for the final review → track the MR merge → verify by yourself the results → change the status in the Jira ticket to CODE COMPLETE or RESOLVED → share necessary links with a QA specialist in the QA Verification channel → QA specialist closes the Jira task after his verification → Jira task should be CLOSED.

**Commit Flow**

1. Get Jira task. Please be aware of the following points:

  a. Every task has a reporter who can provide more details in case something is not clear.

  b. The responsible person for the task and code implementation is the assignee who tracks the following:

   - actual Jira task status

   - time logging

   - add comments, attach necessary files

   - in comments, add link that refers to the merged MR (optional, if not related to many repositories)

   - code review and the final merge

   - MS Teams chats - ping other colleagues, answer questions, etc.

   - verification by a QA specialist

   - bug fixing

  c. Pay attention to the task Status that differs in different entities, the workflow will help to see the whole task processing:

  d. There are several entities that are used on the EDP project: Story, Improvement, Task, Bug.

2. Implement feature, improvement, fix and check the results on your own. If it is impossible to check the results of your work before the merge, verify all later.

3. Create a Merge Request, for details, please refer to the Code Review Process.

4. When committing, use the pattern: [EPMDEDP-JIRA Task Number]: commit type: Commit message.

   a. [EPMDEDP] - is the default part;

   b. JIRA Task Number - the number of your Jira task;

   c. commit type:

   `feat`: (new feature for the user, not a new feature for build script)

   `fix`: (bug fix for the user, not a fix to a build script)

   `docs`: (changes to the documentation)

   `style`: (formatting, missing semicolons, etc; no production code change)

   `refactor`: (refactoring production code, eg. renaming a variable)

   `test`: (adding missing tests, refactoring tests; no production code change)

   `chore`: (updating grunt tasks etc; no production code change)

   `!`: (added to other commit types to mark breaking changes, e.g. *[EPMDEDP-7799]: refactor!: Remove deprecated sonar-gerrit plugin*)

   d. Commit message:

   * brief, for example:

      *[EPMDEDP-0000]: fix: Fix Gerrit plugin for Jenkins provisioning*

    or

   * descriptive, for example:

      *[EPMDEDP-0000]: feat: Provide the ability to configure hadolint check*

      _* Add configuration files .hadolint.yaml and .hadolint.yml to stash_

### Related Articles

* [Conventional Commits](https://www.conventionalcommits.org/)
* [Semantic Commit Messages](https://seesparkbox.com/foundry/semantic_commit_messages)
* [Karma](http://karma-runner.github.io/1.0/dev/git-commit-msg.html)