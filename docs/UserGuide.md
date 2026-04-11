# User Guide

## Introduction

UniTasker is a desktop app for managing tasks and courses, optimized for use via a
Command Line Interface (CLI).

- [Quick Start](#quick-start)
- [Features](#features-)

  - [Adding tasks: `add`](#add-command-add)
    - [Adding a category: `category`](#add-category-add-category)
    - [Adding a todo: `todo`](#add-todo-add-todo)
    - [Adding a deadline: `deadline`](#add-deadline-add-deadline)
    - [Adding a non-recurring event: `event`](#add-event-add-event)
    - [Adding a recurring event: `recurring`](#add-recurring-event-add-recurring)
  - [Deleting tasks: `delete`](#delete-command-delete)
    - [Deleting a category: `category`](#delete-category-delete-category)
    - [Deleting marked tasks: `marked`](#delete-marked-delete-marked)
    - [Deleting a todo/deadline: `todo` `deadline`](#delete-task-todos-and-deadlines-delete-tasktype)
    - [Deleting an event (recurring, non-recurring, occurrence): `event` `recurring` `occurrence`](#delete-events-delete-eventtype)
  - [Marking a task: `mark`](#mark-command-mark)
    - [Marking a todo/deadline: `todo` `deadline`](#mark-task-todos-and-deadlines-mark-tasktype)
    - [Marking an event (recurring, non-recurring, occurrence): `event` `recurring` `occurrence`](#mark-events-mark-eventtype)
  - [Unmarking a task: `unmark`](#unmark-command-unmark)
    - [Unmarking a todo/deadline: `todo` `deadline`](#unmark-task-todos-and-deadlines-unmark-tasktype)
    - [Unmarking an event (recurring, non-recurring, occurrence): `event` `recurring` `occurrence`](#unmark-events-unmark-eventtype)
  - [Listing tasks: `list`](#list-command-list)
    - [Listing categories: `list category`](#list-category-list-category)
    - [Listing todos: `list todo`](#list-todo-list-todo)
    - [Listing deadlines: `list deadline`](#list-deadline-list-deadline)
    - [Listing limit: `list limit`](#list-limit-list-limit)
    - [Listing range: `list range`](#list-range-list-range)
    - [Listing events (main list): `event`](#list-event-list-event)
    - [Listing recurring events (groups of recurring events): `recurring`](#list-recurring-list-recurring)
    - [Listing occurrence (recurring events within a group): `occurrence`](#list-occurrence-list-occurrence)
  - [Reorder: `reorder`](#reorder-command-reorder)
    - [Reorder category: `category`](#reorder-category-reorder-category)
    - [Reorder todo: `todo`](#reorder-todo-reorder-todo)
  - [Set priority to a todo: `priority`](#priority-command-priority)
  - [Sort todos within a category: `sort todo`](#sort-command-sort-todo)
  - [Find tasks: `find`](#find-command-find)
  - [Set limit to number of tasks or year: `limit`](#limit-command-limit)
  - [See reminders for tasks: `reminder`](#reminder-command-reminder)
  - [Course commands: `course`](#course-command-course)
    - [Add course: `course add`](#add-course-course-add)
    - [Delete course: `course delete`](#delete-course-course-delete)
    - [List courses: `course list`](#list-courses-course-list)
    - [View course: `course view`](#view-course-course-view)
    - [Add assessment: `course add-assessment`](#add-assessment-course-add-assessment)
    - [Record score: `course score`](#record-score-course-score)
    - [Delete assessment: `course delete-assessment`](#delete-assessment-course-delete-assessment)
  - [Undo: `undo`](#undo-command-undo)
  - [Exiting the program: `exit`](#exit-program-exit)
  - [Saving and loading the data](#saving-and-loading-the-data)
  - [Editing the data file](#editing-the-data-file)

- [FAQ](#faq)
- [Command Summary](#command-summary)

## Quick Start

1. Ensure that you have Java 17 or above installed.
2. Down the latest version of `UniTasker` from [here](http://link.to/duke).
3. Copy the file to the folder you want to use as the home folder for your UniTasker
4. Open a command terminal, `cd` into the folder you put the jar file in, and
use the `java -jar UniTasker.jar` command to run the application
5. Type a command in the command box and press Enter to execute it.
6. It is recommended to use the `add category` command first as tasks can only be added inside a category.

## Features 
### Add Command: `add`

Adds a new item to the list. The `add` command supports multiple task types: `category`, `todo`, `deadline`, `event`, and `recurring`.

---

#### Add Category: `add category`

Adds a new category.

Format: `add category [name]`

- `name`: Name of the category

**Example:**

`add category School`


---

#### Add Todo: `add todo`

Adds a todo item under a specific category.

Format: `add todo [CATEGORYINDEX] [DESCRIPTION] /p [PRIORITYVALUE]`

- `categoryIndex`: Integer value corresponding to the category
- `description`: Description of the task
- `/p`: Optional priority flag to add a todo with set priority value. Default value is 0.
- `priorityValue`: Integer value for priority. Only 0 to 5 inclusive is allowed.

**Examples:**

`add todo 1 finish tutorial` 
`add todo 1 reply email /p 5`

---

#### Add Deadline: `add deadline`

Adds a deadline with a specified due date and time.

Format: `add deadline [CATEGORYINDEX] [DESCRIPTION] /by [DATE TIME]`

- `categoryIndex`: Integer value corresponding to the category
- `description`: Description of the task
- `/by`: Keyword indicating deadline
- `date time`: Format `dd-MM-yyyy HHmm`

**Example:**

`add deadline 1 Homework /by 25-05-2026 1800`

---

#### Add Event: `add event`

Adds an event with a start and end time.

Format: `add event [CATEGORYINDEX] [DESCRIPTION] /from [START] /to [END]`

- `categoryIndex`: Integer value corresponding to the category
- `description`: Description of the event
- `/from`: Keyword indicating event start time
- `/to`: Keyword indicating event end time
- `start` and `end`: Format `dd-MM-yyyy HHmm`


**Example:**

`add event 1 meeting /from 25-05-2026 1400 /to 25-05-2026 1600`

---

#### Add Recurring Event: `add recurring`

Adds a weekly recurring event.

Format: `add recurring [CATEGORYINDEX] weekly event [DESCRIPTION] /from [DAY TIME] /to [DAY TIME] (/date or /month) [END DURATION]`

- `categoryIndex`: Integer value corresponding to the category
- `description`: Description of the event
- `/from`: Start day and time (e.g. Friday 1600)
- `/to`: End day and time
- `day time`: Format `EEEE HHmm` where EEE is Monday, Tuesday, Wednesday, Thursday, Friday
- `/date` or `/month` : (optional) end duration for the recurring group
- `end duration` : (optional) number of months or end date , Format for end date: `dd-MM-yyyy`

````
______________________________________________________________________
add recurring 1 weekly event lecture /from Friday 1030 /to Friday 1130 /month 2
______________________________________________________________________
This recurring event has been added:
[RE][ ] lecture (from: Friday 1030 to: Friday 1130)
______________________________________________________________________
add recurring 1 weekly event CS2113 lecture /from Friday 1600 /to Friday 1800 /date 24-04-2026
______________________________________________________________________
This recurring event has been added:
[RE][ ] CS2113 lecture (from: Friday 1600 to: Friday 1800)
______________________________________________________________________
````

**Examples:**

`add recurring 1 weekly event CS2113 lecture /from Friday 1600 /to Friday 1800`

*Note*: *Use `/date or /month` but not both to set the end duration*

---

### Delete Command: `delete`
Delete an existing item on the list. `delete` can be used to delete the following: `category`, `todo`, `deadline`, `event`, `recurring`

#### Delete Category: `delete category`

Format: `delete category [CATEGORYINDEX]`

Example: `delete category 1`

#### Delete Marked: `delete marked`
Deletes all marked tasks.

Format: `delete marked`

Example: `delete marked`

#### Delete Task (Todos and Deadlines): `delete [TASKTYPE]`

Format: `delete [TASKTYPE] [CATEGORYINDEX] [TASKINDEX]`

- TASKTYPE : `todo`, `deadline`, `
- CATEGORYINDEX: Integer value up to number of categories added
- TASKINDEX: Integer value up to number of tasks in the category

Examples:

`delete deadline 1 1`
`delete deadline 1 all`

*Note*: *Use `delete todo/deadline categoryIndex all` to delete all todos/deadlines in specific category*



#### Delete Events `delete [EVENTTYPE]`

Format: `delete [EVENTTYPE] [CATEGORYINDEX] [UIINDEX]`

- EVENTTYPE : `event`, `recurring`,`occurrence`
- CATEGORYINDEX: Integer value up to number of categories added
- UIINDEX: follow the UI index for its respective list type:
- Sequence:
  - do `list occurrence [CATEGORYINDEX] [UIINDEX]` → then `delete occurrence [CATEGORYINDEX] [UIINDEX]`
  - do `list recurring` → then `delete recurring [CATEGORYINDEX] [UIINDEX]` 
  - do `list event` or `list event /all` or `list event /normal` → then `delete event [CATEGORYINDEX] [UIINDEX]`

````
______________________________________________________________________
list event
______________________________________________________________________
ALL EVENTS
______________________________________________________________________
[1]school:
1. [E][ ] meeting (from: 04-04-2026 1000 to: 04-04-2026 1100)
2. [E][ ] consultation (from: 10-04-2026 1800 to: 10-04-2026 1900)
3. [E][ ] session (from: 11-04-2026 1800 to: 11-04-2026 1900)
4. [RE]lecture (from: Friday 1030 to: Friday 1130)
5. [RE]CS2113 lecture (from: Friday 1600 to: Friday 1800)
6. [RE]CS2113 seminar (from: Friday 2000 to: Friday 2100)

______________________________________________________________________
delete event 1 1
______________________________________________________________________
This event has been deleted:
[E][ ] meeting (from: 04-04-2026 1000 to: 04-04-2026 1100)
______________________________________________________________________
````

````
______________________________________________________________________
list recurring
______________________________________________________________________
ALL RECURRING EVENTS
______________________________________________________________________
[1]school:
1. [RE]lecture (from: Friday 1030 to: Friday 1130)
2. [RE]CS2113 lecture (from: Friday 1600 to: Friday 1800)
3. [RE]CS2113 seminar (from: Friday 2000 to: Friday 2100)

______________________________________________________________________
delete recurring 1 1
______________________________________________________________________
This recurring event has been deleted:
[RE]lecture (from: Friday 1030 to: Friday 1130)
______________________________________________________________________
````

````
______________________________________________________________________
list event
______________________________________________________________________
ALL EVENTS
______________________________________________________________________
[1]school:
1. [E][ ] consultation (from: 10-04-2026 1800 to: 10-04-2026 1900)
2. [E][ ] session (from: 11-04-2026 1800 to: 11-04-2026 1900)
3. [RE]CS2113 lecture (from: Friday 1600 to: Friday 1800)
4. [RE]CS2113 seminar (from: Friday 2000 to: Friday 2100)

______________________________________________________________________
list occurrence 1 3
______________________________________________________________________
OCCURRENCES FOR: CS2113 lecture
______________________________________________________________________
[1]school:
1. [RE][ ] CS2113 lecture (from: 10-04-2026 1600 to: 10-04-2026 1800)
2. [RE][ ] CS2113 lecture (from: 17-04-2026 1600 to: 17-04-2026 1800)
3. [RE][ ] CS2113 lecture (from: 24-04-2026 1600 to: 24-04-2026 1800)

______________________________________________________________________
delete occurrence 1 1
______________________________________________________________________
This recurring event has been deleted:
[RE][ ] CS2113 lecture (from: 10-04-2026 1600 to: 10-04-2026 1800)
______________________________________________________________________
````



Examples:

`delete occurrence 1 1`
`delete recurring 1 1`
`delete event 1 all`

*Note*:
- *Use `delete event categoryIndex all` to delete all events in specific category*
- *For deleting events always use its respective list views first before using its delete operations to match the index to delete (shown above under UIINDEX description)*

---
### Mark Command: `mark`
#### Mark Task (Todos and Deadlines) `mark [TASKTYPE]`
Mark existing task(s) (todos and deadlines) in a category.

Format: `mark [TASKTYPE] [CATEGORYINDEX] [TASKINDEX]...`

- TASKTYPE : `todo`, `deadline`, 
- CATEGORYINDEX: Integer value up to number of categories added
- TASKINDEX: One or more integer values corresponding to tasks in the category

**Examples:**
- `mark todo 1 1`
- `mark todo 1 1 2 3 4`


#### Mark Events `mark [EVENTTYPE]`
Mark existing event(s) in the category.

Format: `mark [EVENTTYPE] [CATEGORYINDEX] [UIINDEX]...`

- EVENTTYPE : `event`, `occurrence`
- CATEGORYINDEX: Integer value up to number of categories added
- UIINDEX: follow the one or more UI index(es) for its respective list type:
  - Sequence:
  - do `list occurrence [CATEGORYINDEX] [UIINDEX]` → then `mark occurrence [CATEGORYINDEX] [UIINDEX]`
  - do `list event` or `list event /all` or `list event /normal`
    → then `mark event [CATEGORYINDEX] [UIINDEX]` 

````
______________________________________________________________________
list event
______________________________________________________________________
ALL EVENTS
______________________________________________________________________
[1]school:
1. [E][ ] consultation (from: 10-04-2026 1800 to: 10-04-2026 1900)
2. [E][ ] session (from: 11-04-2026 1800 to: 11-04-2026 1900)
3. [RE]CS2113 lecture (from: Friday 1600 to: Friday 1800)
4. [RE]CS2113 seminar (from: Friday 2000 to: Friday 2100)

______________________________________________________________________
mark event 1 1
______________________________________________________________________
This task is marked as done:
[E][X] consultation (from: 10-04-2026 1800 to: 10-04-2026 1900)
______________________________________________________________________
Marked 1 event(s) successfully.
````

````
list event
______________________________________________________________________
ALL EVENTS
______________________________________________________________________
[1]school:
1. [E][X] consultation (from: 10-04-2026 1800 to: 10-04-2026 1900)
2. [E][ ] session (from: 11-04-2026 1800 to: 11-04-2026 1900)
3. [RE]CS2113 lecture (from: Friday 1600 to: Friday 1800)
4. [RE]CS2113 seminar (from: Friday 2000 to: Friday 2100)

______________________________________________________________________
list occurrence 1 3
______________________________________________________________________
OCCURRENCES FOR: CS2113 lecture
______________________________________________________________________
[1]school:
1. [RE][ ] CS2113 lecture (from: 10-04-2026 1600 to: 10-04-2026 1800)
2. [RE][ ] CS2113 lecture (from: 17-04-2026 1600 to: 17-04-2026 1800)
3. [RE][ ] CS2113 lecture (from: 24-04-2026 1600 to: 24-04-2026 1800)

______________________________________________________________________
mark occurrence 1 2
______________________________________________________________________
This task is marked as done:
[RE][X] CS2113 lecture (from: 17-04-2026 1600 to: 17-04-2026 1800)
______________________________________________________________________
Marked 1 event(s) successfully.
````

**Examples**: 

`mark event 1 1` `mark occurrence 1 1`
`mark event 1 1 3` `mark occurrence 1 1 2`

*Note*: 
- *For marking events always use its respective list views first before using its mark operations to match the index to mark (shown above under UIINDEX description)*
- *For multiple marking of events e.g. `mark event 1 1 3` if 3/1 is a recurring group it will not be marked*


### Unmark Command: `unmark`
#### Unmark Task (Todos and Deadlines) `unmark [TASKTYPE]`
Unmark existing task(s) (todos and deadlines) in the category.

Format: `unmark [TASKTYPE] [CATEGORYINDEX] [TASKINDEX]...`

- TASKTYPE : `todo`, `deadline`,
- CATEGORYINDEX: Integer value up to number of categories added
- TASKINDEX: One or more integer values corresponding to tasks in the category

**Examples:**
- `unmark deadline 1 1`
- `unmark deadline 1 1 2 3 4`

#### Unmark Events `unmark [EVENTTYPE]`
Unmark existing event(s) in the category.

Format: `unmark [EVENTTYPE] [CATEGORYINDEX] [UIINDEX]...`

- EVENTTYPE : `event`, `occurrence`
- CATEGORYINDEX: Integer value up to number of categories added
- UIINDEX: follow the one or more UI index(es) for its respective list type:
  - Sequence:
  - do `list occurrence [CATEGORYINDEX] [UIINDEX]` -> then `unmark occurrence [CATEGORYINDEX] [UIINDEX]` 
  - do `list event` or `list event /all` or `list event /normal` -> then `unmark event [CATEGORYINDEX] [UIINDEX]`

````
list event
______________________________________________________________________
ALL EVENTS
______________________________________________________________________
[1]school:
1. [E][X] consultation (from: 10-04-2026 1800 to: 10-04-2026 1900)
2. [E][ ] session (from: 11-04-2026 1800 to: 11-04-2026 1900)
3. [RE]CS2113 lecture (from: Friday 1600 to: Friday 1800)
4. [RE]CS2113 seminar (from: Friday 2000 to: Friday 2100)

______________________________________________________________________
unmark event 1 1
______________________________________________________________________
This task is marked as not done:
[E][ ] consultation (from: 10-04-2026 1800 to: 10-04-2026 1900)
______________________________________________________________________
Unmarked 1 event(s) successfully.
````

````
list event
______________________________________________________________________
ALL EVENTS
______________________________________________________________________
[1]school:
1. [E][ ] consultation (from: 10-04-2026 1800 to: 10-04-2026 1900)
2. [E][ ] session (from: 11-04-2026 1800 to: 11-04-2026 1900)
3. [RE]CS2113 lecture (from: Friday 1600 to: Friday 1800)
4. [RE]CS2113 seminar (from: Friday 2000 to: Friday 2100)

______________________________________________________________________
list occurrence 1 3
______________________________________________________________________
OCCURRENCES FOR: CS2113 lecture
______________________________________________________________________
[1]school:
1. [RE][ ] CS2113 lecture (from: 10-04-2026 1600 to: 10-04-2026 1800)
2. [RE][X] CS2113 lecture (from: 17-04-2026 1600 to: 17-04-2026 1800)
3. [RE][ ] CS2113 lecture (from: 24-04-2026 1600 to: 24-04-2026 1800)

______________________________________________________________________
unmark occurrence 1 2
______________________________________________________________________
This task is marked as not done:
[RE][ ] CS2113 lecture (from: 17-04-2026 1600 to: 17-04-2026 1800)
______________________________________________________________________
Unmarked 1 event(s) successfully.
````

Example: 
`unmark event 1 1` `unmark occurrence 1 1`
`mark event 1 1 3` `mark occurrence 1 1 2`

*Note*: 
- *For unmarking events always use its respective list views first before using its unmark operations to match the index to unmark (shown above under UIINDEX description)*
- *For multiple unmarking of events e.g. `unmark event 1 1 3` if 3/1 is a recurring group it will not be unmarked*


---

### List Command: `list`
Displays a list of tasks. The `list` command can be used with the following keywords: 
`category`, `todo`, `deadline`, `range`, `limit`, `event`, `recurring`, `occurence`

---

#### List Category: `list category`
List all categories or one selected category.

Format: `list category [CATEGORYINDEX]`

- categoryIndex: Optional argument. Integer value up to number of categories added.

Examples: `list category`, `list category 1`

---

#### List Todo: `list todo`
Lists all todos in every category

Format: `list todo`

Example: `list todo`

---

#### List Deadline: `list deadline`
Lists all deadlines in every category

Format: `list deadline`

Example: `list deadline`

---

#### List Limit: `list limit`
Shows the current year range and daily task limit.

Format: `list limit`

Example: `list limit`

---

#### List Range: `list range`
Shows all deadlines and/or events within a date range

Format: `list range [START] [END] [FLAG]`

- START: Start date
- END: End date
- FLAG: Optional flag that is either `/deadline` or `/event`

Examples: 
`list range 25-06-2026 27-06-2026`
`list range 25-06-2026 27-06-2026 /deadline`
`list range 25-06-2026 27-06-2026 /event`

---

#### List Event: `list event`
Format: `list event [TYPE]`

- TYPE: `/normal`, `/all`

`list event /normal` shows non-recurring events

`list event` shows both non-recurring events and recurring events (collapsed view)

Examples:

`list event /normal` `list event /all` `list event`

*Note*:

- *Type is optional*
- *Unknown type will be ignored*

#### List Recurring: `list recurring`
List recurring shows groups of recurring events.

Format : `list recurring`

Examples:

`list recurring`

*Note*: *Anything after 'recurring' will be ignored*

#### List Occurrence: `list occurrence`
List occurrence shows all events within a recurring group.

Format: `list occurence [CATEGORYINDEX] [UIINDEX]`

- CATEGORYINDEX: Integer value up to number of categories added
- UIINDEX: Positive integer displayed in `list event` or `list event /all`

````
list event
______________________________________________________________________
ALL EVENTS
______________________________________________________________________
[1]school:
1. [E][ ] meeting (from: 04-04-2026 1000 to: 04-04-2026 1100)
2. [RE]CS2113 seminar (from: Friday 2000 to: Friday 2100)

______________________________________________________________________
list occurrence 1 2
______________________________________________________________________
OCCURRENCES FOR: CS2113 seminar
______________________________________________________________________
[1]school:
1. [RE][ ] CS2113 seminar (from: 03-04-2026 2000 to: 03-04-2026 2100)
2. [RE][ ] CS2113 seminar (from: 10-04-2026 2000 to: 10-04-2026 2100)
3. [RE][ ] CS2113 seminar (from: 17-04-2026 2000 to: 17-04-2026 2100)
4. [RE][ ] CS2113 seminar (from: 24-04-2026 2000 to: 24-04-2026 2100)

______________________________________________________________________
````

Examples:

`list occurrence 1 1`

*Note*: *Must use `list event` or `list event /all` before using this list command*

---

### Reorder Command: `reorder`
Change the ordering of the following: `category` or `todo`.

---

#### Reorder Category: `reorder category`

Format: `reorder category [FROMINDEX] [TOINDEX]`

- `FROMINDEX` and `TOINDEX`: Integer value up to number of categories added

Example: `reorder category 1 2`

---

#### Reorder Todo: `reorder todo`

Format: `reorder todo [CATEGORYINDEX] [FROMINDEX] [TOINDEX]`

- `CATEGORYINDEX`: Integer value up to number of categories added
- `FROMINDEX` and `TOINDEX`: Integer value up to number of todos added

Example: `reorder todo 1 1 2`

---

### Priority Command: `priority`
Set the priority value of a todo.

Format: `priority todo [CATEGORYINDEX] [TODOINDEX] [PRIORITYVALUE]`

- `CATEGORYINDEX`: Positive integer value up to number of categories added. 
- `TODOINDEX`: Positive integer value up to number of todos added.
- `PRIORITYVALUE`: Integer value within 0 to 5 inclusive.

Example: `priority todo 1 1 5`

---

### Sort Command: `sort todo`
Sort todos within a category by priority value.

Format: `sort todo [CATEGORYINDEX]`

- `CATEGORYINDEX`: Positive integer value up to number of categories added.

Example: `sort todo 1`

---

### Find Command: `find`
Find any task (todos, deadlines and events) that contains the substring entered.

Format: `find [SUBSTRING]`

- `SUBSTRING`: Any string. 
- This command is case-insensitive.

Example: `find assignment`

---

### Limit Command: `limit`
Sets a limit on the following: task,year,...
Allow user to set the limit for the following: `Task`, `Year`

Format: `limit [KEYWORD] [INT]`

- KEYWORD: `task`, `year`
- INT: Integer value

Examples:

`limit task 5` 

`limit year 2035`

*Note*: Year refers to the furthest year that can be accessed/added to from the list

---

### Reminder Command: `reminder`
Shows the pending tasks (deadlines and events) for the day

Format:

`reminder`

*Note*: *Anything after 'reminder' will be ignored*

---

### Course Command: `course`
Manages your course grading structure. Supports adding/deleting courses and assessments,
recording scores, and viewing weighted grades.

---

#### Add Course: `course add`
Adds a new course to the tracker.

Format: `course add [COURSE_CODE]`

- `COURSE_CODE`: The course code (e.g. CS2113). Case-insensitive, stored in uppercase.

**Example:**

`course add CS2113`

---

#### Delete Course: `course delete`
Deletes an existing course from the tracker.

Format: `course delete [COURSE_CODE]`

**Example:**

`course delete CS2113`

---

#### List Courses: `course list`
Lists all courses currently tracked.

Format: `course list`

---

#### View Course: `course view`
Displays all assessments and scores for a specific course.

Format: `course view [COURSE_CODE]`

**Example:**

`course view CS2113`

---

#### Add Assessment: `course add-assessment`
Adds an assessment component to a course.

Format: `course add-assessment [COURSE_CODE] /n [NAME] /w [WEIGHTAGE] /ms [MAX_SCORE]`

- `COURSE_CODE`: The course code
- `/n`: Assessment name
- `/w`: Weightage as a percentage (e.g. 40 for 40%). Total weightage across all assessments cannot exceed 100%.
- `/ms`: Maximum score for the assessment

**Example:**

`course add-assessment CS2113 /n Finals /w 40 /ms 100`

---

#### Record Score: `course score`
Records your score for an assessment.

Format: `course score [COURSE_CODE] /n [NAME] /s [SCORE]`

- `SCORE`: Must not exceed the maximum score of the assessment

**Example:**

`course score CS2113 /n Finals /s 85`

---

#### Delete Assessment: `course delete-assessment`
Deletes an assessment from a course.

Format: `course delete-assessment [COURSE_CODE] /n [NAME]`

**Example:**

`course delete-assessment CS2113 /n Finals`

---

### Undo Command: `undo`
Undoes the most recent course command that modified data.

Format: `undo`

- Only course commands that modify data can be undone (`course add`, `course delete`, `course add-assessment`)
- Commands that only view data (`course list`, `course view`) are not undoable
- Undo history is cleared when the app exits

**Example:**

`course add CS2113` followed by `undo` will remove CS2113.

*Note*: *Undo is currently supported for course commands only.*

---

### Exit program: `exit`
Exits the program

Format:

`exit`

*Note*: *Anything after 'exit' will be ignored*

---

### Saving and loading the data
UniTasker data is saved automatically to the same folder after any change to the data.
If the save data file exists, data will also be loaded automatically into the program.

---

### Editing the data file
UniTasker data is saved as text files in the same directory as the JAR file.  
Advanced users may edit these files directly, but should copy and edit existing entries to maintain the correct format.

*Note*: If changes to the data file makes its format invalid, data loading can behave unexpectedly.
For example, invalid lines may result in wrong information loaded or the entire line being skipped completely.

---

## FAQ

**Q**: How do I transfer my data to another computer? 

**A**: Copy all the .txt files inside the folder with UniTasker and paste in the same folder where UniTasker.jar
is located in the other computer.

## Command Summary

| Action      | Format, Examples                                                                                                                                                                                                                                                                                                                 | 
|-------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| help        | `help`                                                                                                                                                                                                                                                                                                                           |
| add         | `add category [DESC]`, `add todo [CATEGORYINDEX] [DESCRIPTION] /p [PRIORITYVALUE]`, <br/> `add deadline [CATEGORYINDEX] [DESCRIPTION] /by [DATE TIME]`, <br/> `add event [CATEGORYINDEX] [DESCRIPTION] /from [START] /to [END]`, <br/> `add recurring [CATEGORYINDEX] weekly event [DESCRIPTION] /from [DAY TIME /to [DAY TIME]` |
| delete      | `delete [KEYWORD] [CATEGORYINDEX] [TASKINDEX]`, `delete [KEYWORD] [CATEGORYINDEX] all`, `delete marked`                                                                                                                                                                                                                          |
| list        | `list category [CATEGORYINDEX]`, `list todo`, `list deadline`, `list limit`, `list range [START] [END] [FLAG]`, <br/> `list event [TYPE]`, `list recurring`, `list occurence [CATEGORYINDEX] [UIINDEX]`                                                                                                                          |
| mark/unmark | `mark [TASKTYPE] [CATEGORYINDEX] [TASKINDEX]`, `unmark [TASKTYPE] [CATEGORYINDEX] [TASKINDEX]`                                                                                                                                                                                                                                   |
| reorder     | `reorder category [FROMINDEX] [TOINDEX]`, `reorder todo [CATEGORYINDEX] [FROMINDEX] [TOINDEX]`                                                                                                                                                                                                                                   |
| priority    | `priority todo [CATEGORYINDEX] [TODOINDEX] [PRIORITYVALUE]`                                                                                                                                                                                                                                                                      |
| sort        | `sort todo [CATEGORYINDEX]`                                                                                                                                                                                                                                                                                                      |
| find        | `find [SUBSTRING]`                                                                                                                                                                                                                                                                                                               |
| limit       | `limit [KEYWORD] [INT]`                                                                                                                                                                                                                                                                                                          |
| reminder    | `reminder`                                                                                                                                                                                                                                                                                                                       |
| course      | `course add [COURSE_CODE]`, `course delete [COURSE_CODE]`, `course list`, <br/> `course view [COURSE_CODE]`, `course add-assessment [COURSE_CODE] /n [NAME] /w [WEIGHTAGE] /ms [MAX_SCORE]`, <br/> `course score [COURSE_CODE] /n [NAME] /s [SCORE]`, `course delete-assessment [COURSE_CODE] /n [NAME]`                         |                                                                                                           |
| undo        | `undo`                                                                                                                                                                                                                                                                                                                           |
| exit        | `exit`                                                                                                                                                                                                                                                                                                                           |

