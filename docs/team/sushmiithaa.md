# sushmiithaa - Project Portfolio Page

**Project: UniTasker**

UniTasker is a desktop application used to keep track of tasks and courses.

Given below are my contributions to the project.

## Summary of Contributions

**New Features**

- Added the ability to add non-recurring events and recurring events
    - What it does: 
      - Allows users to add non-recurring events.
      - Allow users to add weekly recurring events where they can set the recurring events for a defined number of months or until an end date.
    - Justification: In real life University students have both recurring events (e.g. lectures, tutorials) and non-recurring events (e.g. meetings) that they want to schedule.
    - Highlights: This functionality provides flexibility and convenience as users no longer have to repeatedly add non-recurring event every week to simulate recurring events (batch addition of events).
- Added ability to have different list of events
    - What it does:
        - Users can choose to list all non-recurring events: `list event /normal`.
        - Users can choose to view the overview of events `list event`.
        - Users can choose to view only recurring event groups `list recurring`.
        - Users can choose to view specific occurrence of events within a recurring event group `list occurrence`.
    - Justification: Providing only one list where all types of events can be seen can be messy and hard to read.
    - Highlights: Different views allow users to focus on what they want to know instead of being overwhelmed by the rest.
- Added the ability delete recurring groups, specific recurring events within a group and non-recurring events
    - What it does: 
      - Allows users to delete all recurring events within a group at one go `delete recurring`. 
      - Allow users to delete specific recurring events within a group `delete occurrence`. 
      - Allow users to delete non-recurring events `delete event`.
      - All three modes work by mapping the displayed list index to the underlying event list index.
    - Justification: Users may no longer have the recurring events or have it but not have to attend to one of it. In addition, non-recurring might also be cancelled. Offering all three options cater to all three conditions.
    - Highlights: Providing all three options allows users to easily delete and manage their events.
- Added ability to show reminders
    - What it does: User can see reminders of pending events and deadlines within that day when they start UniTasker. They can also view the reminders while using the app using the command: `reminder`.
    - Justification: To highlight the pending events and deadlines for the user to focus on for the day.


**Code Contributed**: [RepoSense Link](https://nus-cs2113-ay2526-s2.github.io/tp-dashboard/?search=t14-3&sort=totalCommits%20dsc&sortWithin=totalCommits%20dsc&timeframe=commit&mergegroup=&groupSelect=groupByRepos&breakdown=true&checkedFileTypes=docs~functional-code~test-code~other&since=2026-02-20T00%3A00%3A00&filteredFileName=&tabOpen=true&tabType=authorship&tabAuthor=sushmiithaa&tabRepo=AY2526S2-CS2113-T14-3%2Ftp%5Bmaster%5D&authorshipIsMergeGroup=false&authorshipFileTypes=docs~functional-code~test-code&authorshipIsBinaryFileTypeChecked=false&authorshipIsIgnoredFilesChecked=false)

General contributions:
- Commands related to events
- Reminder functionality
- Minor UI enhancements

**Testing**

Added JUnit tests for event related operations

**Developer Guide Contributions**

- Added implementation details for add event using sequence diagram
- Added implementation details for delete event using sequence diagram
- Added implementation details for list event using sequence diagram

**User Guide Contributions**

- Added documentation for the following features:
  `exit`, `add`, `list`, `delete`, `mark`, `unmark`
- Added table of content

**Review/Mentoring Contributions**
- Reviewed PRs and provided feedback


