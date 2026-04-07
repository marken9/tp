# Vansh Puri (benguy6) - Project Portfolio Page

**Project: UniTasker**

UniTasker is a desktop application used to keep track of tasks and courses.

Given below are my contributions to the project.

## Summary of Contributions

**New Features**

- Implemented comprehensive help system for the entire application
    - What it does: Provides contextual help documentation for all 4 feature modes (task, deadline, event, course) and command variants, accessible via `help <topic>` command.
    - Justification: Users need accessible documentation to discover commands and understand command syntax without leaving the application.
    - Highlights: 300-line `CommandHelp.java` with smart topic routing; supports mode-specific help (`help task`, `help deadline`), command-variant help (`help add`, `help delete`), and general help overview.

- Implemented complete course management and grade tracking system
    - What it does: Allows users to add/manage courses, track assessment components with weightages, record scores, and compute weighted grades automatically.
    - Justification: Students need to track grades across multiple modules in one place, with automatic weighted grade calculation for multiple assessments per course.
    - Highlights: Supports full CRUD operations for courses and assessments; validates weightage limits (max 100%); persists data across sessions; provides weighted score calculations with intelligent handling of ungraded assessments.

**Code Contributed**: [RepoSense Link](https://nus-cs2113-ay2526-s2.github.io/tp-dashboard/?search=benguy6&sort=groupTitle&sortWithin=title&timeframe=commit&mergegroup=&groupSelect=groupByRepos&breakdown=true&checkedFileTypes=docs~functional-code~test-code~other&since=2026-02-20T00%3A00%3A00&filteredFileName=&tabOpen=true&tabType=authorship&tabAuthor=benguy6&tabRepo=AY2526S2-CS2113-T14-3%2Ftp%5Bmaster%5D&authorshipIsMergeGroup=false&authorshipFileTypes=docs~functional-code~test-code~other&authorshipIsBinaryFileTypeChecked=false&authorshipIsIgnoredFilesChecked=false)

General contributions:
- Implemented core course tracking classes: `Assessment.java`, `Course.java`, `CourseList.java`, `CourseManager.java` (645 LoC total)
- Implemented `CourseStorage.java` with file-based persistence and encoding/decoding strategy
- Implemented `CourseException.java` for course-specific error handling
- Implemented `CommandHelp.java` (300 LoC) with comprehensive help system for all modes and commands
- Integrated course feature into `UniTasker.java` initialization and command loop
- Added defensive assertions to `Task.java` and `Category.java` for precondition validation
- Updated `CourseClassDiagram.puml` with professional styling and simplified method signatures

**Testing**

Added JUnit tests for the following classes:

- `AssessmentTest.java` (70 LoC) - Validates grading logic, weighted score calculations, encoding/decoding
- `CourseTest.java` (45 LoC) - Tests assessment CRUD operations and course state management
- `CourseStorageTest.java` (88 LoC) - Tests file I/O operations: missing files, single/multiple courses, save/load roundtrips

**Developer Guide Contributions**

- Added Course Tracker architecture and design overview with explanation of manager → list → course → assessment hierarchy
- Added Course Tracker sequence diagram (`CourseAddSequenceDiagram`) showing command execution flow
- Added design considerations for course grading calculation and persistence strategy

**User Guide Contributions**

- Added comprehensive documentation for Course Tracker feature including:
  - `course add`, `course delete`, `course list`, `course view` - Course management commands
  - `course add-assessment` - Adding assessment components with weightage and max score
  - `course score` - Recording assessment scores
  - `course delete-assessment` - Removing assessments from courses

**Review/Mentoring Contributions**
- Reviewed PRs and provided feedback
- Assisted teammates with code quality and documentation standards
