# User Guide

## Introduction

UniTasker is a desktop app for managing tasks and courses, optimized for use via a
Command Line Interface (CLI).

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

Adds a new item to the list.`add` can be used to add the following: `category`, `todo`, `deadline`, `event`, `recurring`

Format: 

add `category` name or,

add [TASKTYPE] [CATEGORYINDEX] [DESCRIPTION] [DATE]

- TASKTYPE : `todo`, `deadline`, `event`, `recurring`
- CATEGORYINDEX: Integer value up to number of categories added
- DESCRIPTION: What the task is about
- DATE: Date & Time, Date only

Examples:
`add deadline 1 Homework /by 25-05-2026`

### Add Command: `add` Version 2

Adds a new item to the list. The `add` command supports multiple task types: `category`, `todo`, `deadline`, `event`, and `recurring`.

---

#### Add Category: `add category`

Adds a new category.

Format: `add category [name]`

- `name`: Name of the category

**Example:**

add category School


---

#### Add Todo: `add todo`

Adds a todo item under a specific category.

Format: `add todo [categoryIndex] [description] /p [priorityValue]`

- `categoryIndex`: Integer value corresponding to the category
- `description`: Description of the task
- `/p`: Optional priority flag to add a todo with set priority value. Default value is 0.
- `priorityValue`: Integer value for priority. Only 0 to 5 inclusive is allowed.

**Examples:**

add todo 1 finish tutorial  
add todo 1 reply email /p 5

---

#### Add Deadline: `add deadline`

Adds a deadline with a specified due date and time.

Format: `add deadline [categoryIndex] [description] /by [date time]`

- `categoryIndex`: Integer value corresponding to the category
- `description`: Description of the task
- `/by`: Keyword indicating deadline
- `date time`: Format `dd-MM-yyyy HHmm`

**Example:**

add deadline 1 Homework /by 25-05-2026 1800

---

#### Add Event: `add event`

Adds an event with a start and end time.

Format: `add event [categoryIndex] [description] /from [start] /to [end]`

- `categoryIndex`: Integer value corresponding to the category
- `description`: Description of the event
- `/from`: Keyword indicating event start time
- `/to`: Keyword indicating event end time
- `start` and `end`: Format `dd-MM-yyyy HHmm`
- `description`: Description of the event
- `/from`: Start date and time
- `/to`: End date and time

**Example:**

add event 1 meeting /from 25-05-2026 1400 /to 25-05-2026 1600

---

#### Add Recurring Event: `add recurring`

Adds a weekly recurring event.

Format: `add recurring [categoryIndex] weekly event [description] /from [day time] /to [day time]`

- `categoryIndex`: Integer value corresponding to the category
- `description`: Description of the event
- `/from`: Start day and time (e.g. Friday 1600)
- `/to`: End day and time

**Examples:**

add recurring 1 weekly event CS2113 lecture /from Friday 1600 /to Friday 1800

---

### Delete Command: `delete`
Delete an existing item on the list. `delete` can be used to delete the following: `category`, `todo`, `deadline`, `event`, `recurring`

#### Delete Category: `delete category`

Format: `delete category [CATEGORYINDEX]`

Example: `delete category 1`

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
- TASKINDEX: follow the UI index for its respective list type:
  - `delete occurrence [CATEGORYINDEX] [UIINDEX]` → `list occurrence [CATEGORYINDEX] [UIINDEX]`
  - `delete recurring [CATEGORYINDEX] [UIINDEX]` → `list recurring`
  - `delete event [CATEGORYINDEX] [UIINDEX]` → `list event` or `list event /all` or `list event /normal`

Examples:

`delete occurrence 1 1`
`delete recurring 1 1`
`delete event 1 all`

*Note*:
- *Use `delete event categoryIndex all` to delete all events in specific category*
- *For deleting events always use its respective list views first before using its delete operations to match the index to delete*

---
### Mark Command: `mark`
Mark existing task(s) in a category.

Format: `mark [TASKTYPE] [CATEGORYINDEX] [TASKINDEX]...`

- TASKTYPE : `todo`, `deadline`, `event`
- CATEGORYINDEX: Integer value up to number of categories added
- TASKINDEX: One or more integer values corresponding to tasks in the category

**Examples:**
- `mark todo 1 1`
- `mark todo 1 1 2 3 4`

  *Note*: *For marking events use index from `list event /all`*

### Unmark Command: `unmark`
Unmark existing task(s) in a category. 

Format: `unmark [TASKTYPE] [CATEGORYINDEX] [TASKINDEX]...`

- TASKTYPE : `todo`, `deadline`, `event`
- CATEGORYINDEX: Integer value up to number of categories added
- TASKINDEX: One or more integer values corresponding to tasks in the category

**Examples:**
- `unmark deadline 1 1`
- `unmark deadline 1 1 2 3 4`

*Note*: *For unmarking events use index from `list event /all`*

---

### List Command: `list`
Displays a list of tasks. 

- `list` can be used to crate a list on the following: `category`, `todo`, `deadline`, `event`, `recurring`, `range`

List out all tasks based on key word

Format :

list [KEYWORD] [CATEGORYINDEX] [START] [END] [TASKTYPE]

- KEYWORD: `category`,`todo`,`deadline`,`range`,`limit`
- CATEGORYINDEX: Integer value up to number of categories added
- START: Start date
- END: End date
- TASKTYPE: `/deadline`, `/event`

Examples:

`list deadline` `list deadline 1` 

`list limit`

`list range 25-04-2026 27-04-2026`
`list range 25-04-2026 27-04-2026 /deadline`

*Note*: 

- *CategoryIndex is not needed for limit*
- *Start and End are only applicable for Deadline and Event*
- *Add tasktype after end if you want to see only deadline or event*

#### List Event: `list event`
Format: list [KEYWORD] [TYPE]

- KEYWORD: `event`
- TYPE: `/normal`, `/all`

Examples:

`list event /normal` `list event /all` `list event`

*Note*:

- *Type is optional*
- *Unknown type will be ignored*

#### List Recurring: `list recurring`
Format : list [KEYWORD]

- KEYWORD: `recurring`

Examples:

`list recurring`

*Note*: *Anything after 'recurring' will be ignored*

#### List Occurrence: `list occurrence`
Format: list [KEYWORD] [CATEGORYINDEX] [UIINDEX]

- KEYWORD: `occurrence`
- CATEGORYINDEX: Integer value up to number of categories added
- UIINDEX: Positive integer displayed in `list event` or `list event /all`

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

Format:

limit [KEYWORD] [INT]

- KEYWORD: `task`, `year`
- INT: Integer value

Example:

`limit task 5` 

`limit year 2035`

*Note*: Year refers to the furthest year that can be accessed/added to from the list

---

### Reminder Command: `reminder`
Shows the pending tasks (deadlines and events) for the day

Format:

limit [KEYWORD]

- KEYWORD: `reminder`

Example:

`reminder`

*Note*: *Anything after 'reminder' will be ignored*

---

## FAQ

**Q**: How do I transfer my data to another computer? 

**A**: Copy all the .txt files inside the folder with UniTasker and paste in the same folder where UniTasker.jar
is located in the other computer.

## Command Summary

| Action      | Format, Examples                                                                                                             | 
|-------------|------------------------------------------------------------------------------------------------------------------------------|
| help        | `help`                                                                                                                       |
| add         | `add category [DESC]`, `add todo [CATEGORYINDEX] [DESC]`, <br/> `add todo [categoryIndex] [description] /p [priorityValue]`, |
| delete      | `delete [KEYWORD] [CATEGORYINDEX] [TASKINDEX]`, `delete [KEYWORD] [CATEGORYINDEX] all`                                       |
| list        | `list [keyword] [CATEGORYINDEX] [TASKINDEX]`                                                                                 |
| mark/unmark | `mark [TASKTYPE] [CATEGORYINDEX] [TASKINDEX]`, `unmark [TASKTYPE] [CATEGORYINDEX] [TASKINDEX]`                               |
| reorder     | `reorder category [FROMINDEX] [TOINDEX]`, `reorder todo [CATEGORYINDEX] [FROMINDEX] [TOINDEX]`                               |
| priority    | `priority todo [CATEGORYINDEX] [TODOINDEX] [PRIORITYVALUE]`                                                                  |
| sort        | `sort todo [CATEGORYINDEX]`                                                                                                  |
| find        | `find [SUBSTRING]`                                                                                                           |
| limit       | `limit [keyword]`                                                                                                            |
| reminder    | `reminder`                                                                                                                   |
| course      | `add [keyword]`                                                                                                              |

