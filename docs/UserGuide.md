# User Guide

## Introduction

UniTasker is a desktop app for managing tasks and courses, optimized for use via a
Command Line Interface (CLI).

## Quick Start

{Give steps to get started quickly}

1. Ensure that you have Java 17 or above installed.
2. Down the latest version of `UniTasker` from [here](http://link.to/duke).
3. Copy the file to the folder you want to use as the home folder for your UniTasker
4. Open a command terminal, `cd` into the folder you put the jar file in, and
use the `java -jar UniTasker.jar` command to run the application
5. Type a command in the command box and press Enter to execute it.

## Features 

{Give detailed description of each feature}

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

Format: `add todo [categoryIndex] [description]`

- `categoryIndex`: Integer value corresponding to the category
- `description`: Description of the task

**Example:**

add todo 1 finish tutorial

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

Format:

delete `category` index or,

delete [TASKTYPE] [CATEGORYINDEX] [TASKINDEX]

- TASKTYPE : `todo`, `deadline`, `event`, `recurring`
- CATEGORYINDEX: Integer value up to number of categories added
- TASKINDEX: What the task is about

*Note*: Use `delete deadline/event categoryIndex all` to delete all deadlines/events in specific category

Examples:

`delete deadline 1 1`
`delete deadline 1 all`


### Mark/Unmark Command: `mark` `unmark`
Mark an existing item on the list. `mark` can be used to mark the following: `category`, `todo`, `deadline`, `event`, `recurring`

Unmark an existing item on the list. `unmark` can be used to unmark the following: `category`, `todo`, `deadline`, `event`, `recurring`


**List Command**: `list`
Creates a list of task. `list` can be used to crate a list on the following: `category`, `todo`, `deadline`, `event`, `recurring`, `range`

List out all tasks based on key word

Format:

list [KEYWORD] [CATEGORYINDEX] [START] [END] [TASKTYPE]

- KEYWORD: `category`,`todo`,`deadline`,`event`,`range`,`recurring`, `limit` 
- CATEGORYINDEX: Integer value up to number of categories added
- START: Start date
- END: End date
- TASKTYPE: `deadline`, `event`

Examples:

`list deadline` `list deadline 1` 

`list limit`

`list range 25-04-2026 27-04-2026`
`list range 25-04-2026 27-04-2026 /deadline`

*Note*: 

- *Start and End are only applicable for Deadline and Event*
- *Add tasktype after end if you want to see only deadline or event*

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

## FAQ

**Q**: How do I transfer my data to another computer? 

**A**: Copy all the .txt files inside the folder with UniTasker and paste in the same folder where UniTasker.jar
is located in the other computer.

## Command Summary

| Action      | Format, Examples | 
|-------------|------------------|
| help        | `help`           |
| add         | `add [keyword]`  |
| delete      | `add [keyword]`  |
| list        | `add [keyword]`  |
| mark/unmark | `add [keyword]`  |
| reorder     | `add [keyword]`  |
| priority    | `add [keyword]`  |
| sort        | `add [keyword]`  |
| find        | `add [keyword]`  |
| course      | `add [keyword]`  |

