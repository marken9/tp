# marken9 - Project Portfolio Page

**Project: UniTasker**

UniTasker is a desktop application used to keep track of tasks and courses.

## Summary of Contributions

**New Features**

- Add support for bulk marking/unmarking tasks
  - Justification: Reduce repetitive commands and improve efficiency when marking/unmarking multiple tasks 
  - Highlight: The command supports partial success by marking all valid task indexes
  while skipping and reporting any invalid indexes
- Add ability to delete all marked tasks
  - Justification: The user can quickly remove all tasks that have been marked 
  as completed without manual deletion
  - Highlight: Supports deletion across todos, deadlines, and events, 
  providing consistent behavior for all task types
- Add ability to find tasks by substring
  - Justification: The user can quickly locate relevant tasks without scanning entire lists
  - Highlight: Searches across all categories, including todos, deadlines, and events
- Add priority level and sorting for todos
  - Justification: The user wants to prioritize doing certain todos first over others
  - Highlight: Improves todo organisation within categories without requiring manual reordering

**Code Contributed**

Contribution: [RepoSense Link](https://nus-cs2113-ay2526-s2.github.io/tp-dashboard/?search=marken9&sort=groupTitle&sortWithin=title&timeframe=commit&mergegroup=&groupSelect=groupByRepos&breakdown=true&checkedFileTypes=docs~functional-code~test-code~other&since=2026-02-20T00%3A00%3A00&filteredFileName=&tabOpen=true&tabType=authorship&tabAuthor=marken9&tabRepo=AY2526S2-CS2113-T14-3%2Ftp%5Bmaster%5D&authorshipIsMergeGroup=false&authorshipFileTypes=docs~functional-code~test-code~other&authorshipIsBinaryFileTypeChecked=false&authorshipIsIgnoredFilesChecked=false)

General contributions:
- Implemented core commands for todos and categories
- Refactored the Command architecture for better modularity and extensibility
- Designed and implemented CategoryList as the central data structure 
for managing categories and their tasks
- Support saving of todos inside the storage class

**Test Coverage Contributions**
- Integration testing for commands related to categories and todos

**Developer Guide Contributions**
- Main architecture class diagram
- Reorder todo sequence diagram to illustrate the architecture
- AppContainer component with class diagram
- Command component with class diagram
- CategoryList class diagram
- Delete marked command sequence diagram

**User Guide Contributions**
- Todo specific commands (reorder, priority, sort)
- General commands like add, mark/unmark.

**Review/Mentoring Contributions**
- Reviewed PRs and provided feedback
- Assist teammates in following the forking workflow
