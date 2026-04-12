# wenjunyu5984 - Project Portfolio Page

**Project: UniTasker**

UniTasker is a desktop application used to keep track of tasks and courses.

Given below are my contributions to the project.

## Summary of Contributions

**New Features**

- Added the ability to set limit on end year
  - What it does: Allows user to manually set the end year. Any task that is added after the set year cannot be added to the list.
  - Justification: This gives the user the convenience of changing the end year whenever they want. For example if user wants to continue using the application after graduation for other personal use, they can simply extend the end year to a later date.
  - Highlights: This enhancement affects all timed task that can be added in the future and gives room for the user to plat around with the settings. User settings are also saved after exit.
- Added the ability to set limit on number of timed task per day
  - What it does: Allows user to set the maximum task they want to put on each day. If there are too many timed tasks on a particular day, the system will show a message warning user.
  - Justification: As University students, there might be days when they might be overwhelmed with tasks. To prevent them from burning out, they are given an option to set a maximum number of timed tasks on a particular date. This is also a way of encouraging users not to procrastinate if there are many tasks to be done on a particular date.  
- Added ability to list out all deadlines within a certain date range using [STARTYEAR][ENDYEAR][/deadline]
  - What it does: Allows user to list out all deadlines within a certain date range.
  - Justification: This gives user a method to access all deadlines within a certain period. Shorten long list of timed task, providing ease of checking of deadlines only.


**Code Contributed**: [RepoSense Link](https://nus-cs2113-ay2526-s2.github.io/tp-dashboard/?search=&sort=totalCommits%20dsc&sortWithin=totalCommits%20dsc&timeframe=commit&mergegroup=&groupSelect=groupByRepos&breakdown=true&checkedFileTypes=docs~functional-code~test-code~other&since=2026-02-20T00%3A00%3A00&filteredFileName=&tabOpen=true&tabType=authorship&tabAuthor=WenJunYu5984&tabRepo=AY2526S2-CS2113-T14-3%2Ftp%5Bmaster%5D&authorshipIsMergeGroup=false&authorshipFileTypes=docs~functional-code~test-code~other&authorshipIsBinaryFileTypeChecked=false&authorshipIsIgnoredFilesChecked=false)

General contributions:
- Commands related to deadlines
- UI refactoring for all tasks
- Implement Calendar class as a central data structure for storing and accessing timed class
- Implement the general structure for storage class, handling loading, saving and updating of all tasks, taskLimit and endYear.
- Implement logging configuration
- Added Limit command for year and task
- Added Task Validation for all tasks
- Added Date Validation for all timed task
- Added update for number of active timed task on each day

**Testing**

Added JUnit tests for the following classes:

- Deadline, DeadlineList, Storage, Calendar, LogConfig, DeadlineUi, ErrorUi, GeneralUi, DateUtils, TaskValidator

**Developer Guide Contributions**

- Added implementation details for deadline using a class diagram
- Added implementation details for storage using a class diagram
- Added implementation details for UI using a class diagram
- Added implementation details for TaskValidation using sequence diagram
- Added implementation details for DateUtils using sequence diagram
- Added manual testing for deadline commands and limit commands

**User Guide Contributions**

- Added documentation for the following features:
  - `delete`, `list`, `limit`, `deadline`
- Added documentation what command changes the total complete and incomplete task

**Review/Mentoring Contributions**
- Reviewed PRs and provided feedback
- Guided group member on how to push and pull commits to GitHub
