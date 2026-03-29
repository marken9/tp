# Developer Guide

## Acknowledgements

{list here sources of all reused/adapted ideas, code, documentation, and third-party libraries -- include links to the original source as well}

This project was built from scratch by the UniTasker team. The following thrid-party libraries and references were consulted during development:
- Java SE 17 Standard Library
- JUnit 5
- PlantUML
- SE-EDU AddressBook-Level3
{Describe the design and implementation of the product. Use UML diagrams and short code snippets where applicable.}

## Design

This section describes the design and implementation of the key components of UniTasker. UML diagrams are provided for highlighted classes and interaction flows

### Architecture

![MainArchitecture]()

The **Architecture Diagram** given above explains the high-level design of the App

**Main components of the architecture**

UniTasker is in charge of the app launch and shut down
- At app launch, it initializes the other components.
- At shut down, it shuts down the other components.

The bulk of the app's work is done by the following components:
- Command: Responsible for executing user instructions correctly
- UI: Responsible for displaying messages to the user.
- AppContainer: Acts as a central container for sharing application data.
- Storage: Responsible for saving and loading data from local files.

**How the architecture components interact with each other:**

![ReorderCommand](/docs/pictures/ReorderTodoSequenceDiagram.png)

1. User enters command in terminal
2. xx
3. xx

**AppContainer component**

The `AppContainer` consists of the following:

- `CategoryList categories` – stores all categories and their associated tasks (todos, deadlines, events)
- `Calendar calendar` – Manages the mapping of dates to tasks with date information (deadlines and events) ???
- `Storage storage` – handles saving and loading of data from local files
- `CourseParser courseParser` – processes course-related commands
- `int dailyTaskLimit` – defines the maximum number of tasks allowed per day
- `int startYear` – lower bound for valid date ranges
- `int endYear` – upper bound for valid date ranges

The `AppContainer` component,
- Stores all the information required for the application in a single object
- Enables commands to operate without directly depending on global/static variables

**Storage component**

**UI component**

**Command component**

![CommandClassDiagram](/docs/pictures/CommandClassDiagram.png)

The `Command` component handles the execution of user commands. Each supported command is represented by a separate command class implementing the common `Command` interface.

The component consists of:
- `CommandParser`, which maps raw user input to a concrete command object
- `Command`, which defines the common `execute(AppContainer container)` method
- Concrete command classes such as `AddCommand`, `DeleteCommand`, and so on.
- `CommandSupport`, which provides shared helper methods such as data saving and category index retrieval

How the `Command` component works:

1. User input is received by `UniTasker`
2. Input is passed to `CommandParser`
3. `CommandParser` returns the appropriate `Command` object
4. `execute(container)` is called
5. Command updates the model via `AppContainer`
6. Changes are saved via `Storage`
7. Output messages are displayed via `UI`

## Implementation

**Deadline Class Diagram**

The figure below illustrates the relationship between Deadline class and the following classes: Task, Timed, Calendar, DateUtils, DeadlineList, TaskList. 

![Deadline Class Diagram](docs/pictures/deadlineClassDiagram.png)
*<div align="center"> Figure x - Deadline Class Diagram </div>*


**Key Design Considerations**

- Deadline and Events are time based tasks which differentiate them from todo tasks.
- To be able to show Deadlines and Events within a certain range of dates, a Timed interface is used to distinguish them so that the Calendar class can store and query them polymorphically.
- DateUtils hold 3 DateTimeFormatter constants which gives users the ability to store deadlines and events in different formats.
- DeadlineList extends the generic TaskList<T>, inheriting add/delete/mark/contains operations.
- Calendar uses a TreeMap<LocalDate, List<Task>> to enable efficient range queries without iterating the entire task list.

### Feature: Task Validation ###

There are two main types of validations for task. 

- Time Validation
- Occurrence Validation

The following figures explains how tasks are validated based on Time and Occurrence.

**DateUtils.parse()**

DateUtils is used as a Time Validator. 

The sequence diagram below illustrates how a date string entered by the user is parsed, validated, and return as a LocalDateTime. This flow is triggered whenever a timed task, a deadline or event, is added. Recurring events will be illustrated in a different diagram as it does not follow numerical convention. 

Example: `Add deadline 1 Homework /by 31-12-2025 1800` or `Add event 1 Homework /from 31-12-2025 1800 /to 01-01-2026`

**Note: The command in the diagram has been generalised as a date since DateUtils validates dates only**

![DateUtils Sequence Diagram](docs/pictures/DateUtilsSequence.png)
*<div align="center"> Figure x - DateUtils: parse() Sequence Diagram </div>*

**Parsing Flow Summary:**

- A task is followed by a date string
- CommandParser parse the line and calls AddCommand
- AddCommand then calls for handleAddEvent/handleAddDeadline, depending on whether an event or a deadline is being added.
- As this is a command given from User, the isLoading flag is set to false.
- DateUtils will then check for the following conditions : is input empty or null, is the date parsed following the FULL_FORMATTER or DATE_ONLY FORMATTER
- If only date is parsed, time is automatically set to 2359
- If all the above checks pass and isLoading == false, check if date is in the past
- If all checks pass, the last check would be if the task exist within the stipulated timeframe that user has set/ default timeframe
- If all checks are parsed, the task will be added to its tasklist (DeadlineList or EventList) and registered in the Calendar
- If any of the checks fail along the way, an error message pertaining to the error will be shown to the user

**Implementation Note - isLoading Flag**

The IsLoading parameter is set to true when DateUtils.parse() is called from the storage layer (file loading), and false during user input. This allows tasks saved in a previous session to be restored if their dates have now passed, while preventing the user form directly scheduling past tasks interactively.

**TaskValidator**

TaskValidator ensures that there is a unique occurrence of a given task with no overlaps.

Before any task (Todo, Deadline, Event) is added to the system, the AddCommand invokes three sequential validation passes via TaskValidator. These checks ensure that no Task have the same name, workload per day does not exceed set limit and there is no overlap in events. The diagram below shows the full interaction.

![TaskValidator Sequence Diagram](docs/pictures/TaskValidatorSequence.png)
*<div align="center"> Figure x - Task Validator Sequence Diagram </div>*

**Parsing Flow Summary**

- add task with its description, date with or without time and category index
- retrieve all tasks from todoList, deadlineList and eventList
- Check for any duplicate names, if no check the number of workload for each day with registered timed task
- If totalTimedTask is greater than or equal to maxTask, throw a HighWorkloadException error
- Otherwise, check for any overlap in timing with existing events
- If yes throw an OverlapEventException, otherwise all validators have been passed and task is added successfully

## Product scope

### Target user profile

UniTasker is designed for university students who need to manage multiple courses, assignments, deadlines, and personal tasks simultaneously. These users often:
- juggle academic responsibilities across different modules, each with varying deadlines, 
priorities, and schedules. 
- They require a simple and efficient system to organise their tasks,
keep track of coursework, and stay on top of deadlines.
- prefer a fast, keyboard-driven interface over GUI-heavy applications

### Value proposition

University students often struggle to keep track of tasks and course assessments across different 
platforms such as learning portals, calendars and notes. This fragmented approach leads
to missed deadlines, poor prioritisation, and unnecessary stress. 

UniTasker provides a centralized 
task management solution that consolidates todos, deadlines, events and course information into a 
single platform. Through a simple command-line interface, it allows students to efficiently organise, 
update, and review their tasks and assessments. This helps students to stay on top of their workload
and focus on completing their academic responsibilities.

## User Stories

| Version | As a ...           | I want to ...                                                              | So that I can ...                                                              |
|---------|--------------------|----------------------------------------------------------------------------|--------------------------------------------------------------------------------|
| v1.0    | University Student | create categories for each of my courses                                   | organise my tasks by module                                                    |
| v1.0    | University Student | view a specific category                                                   | focus on tasks related to a single course                                      |
| v1.0    | University Student | assign priority levels to todos in a category                              | identify important todos easily                                                |
| v1.0    | University Student | sort todos within a category by priority                                   | focus on high-priority todos first                                             |
| v1.0    | University Student | track all tasks with a due date                                            | keep track of all my deadlines                                                 |
| v1.0    | University Student | arrange tasks which occur or are due within a certain time period          | prioritise tasks that are due earlier                                          |
| v1.0    | University Student | delete all deadlines within a category                                     | quickly remove deadlines in a category                                         | 
| v1.0    | University Student | have my deadlines sorted by earliest date                                  | easily identify earliest due deadline                                          |
| v1.0    | University Student | track all tasks with a start date and time and end date and time           | keep track of all my events                                                    |
| v1.0    | University Student | have my events sorted by earliest date                                     | easily identify events that happen earliest                                    |
| v1.0    | University Student | track all recurring tasks with a start date and time and end date and time | keep track of all my recurring events                                          |
| v1.0    | University Student | add a course                                                               | keep track of all the modules I am taking                                      | 
| v1.0    | University Student | delete a course                                                            | remove modules I am no longer taking                                           |
| v1.0    | University Student | list all courses                                                           | see an overview of all my modules                                              |
| v1.0    | University Student | add assessments to a course                                                | track the componenets that make up the grades                                  |
| v1.0    | University Student | delete an assessment from a course                                         | remove an incorrect or irrelevant assessment from the tracker                  |
| v1.0    | University Student | view all assessments within a course                                       | understand how my course grading is structured                                 |
| v1.0    | University Student | record my score for an assessment                                          | keep track of my performance in each assessment                                |
| v2.0    | University Student | delete all marked tasks                                                    | quickly clean up completed work across categories                              |
| v2.0    | University Student | search for tasks across all categories                                     | quickly find relevant tasks                                                    |
| v2.0    | University Student | customize the maximum tasks permitted per day                              | schedule my tasks without burning myself out                                   |
| v2.0    | University Student | customize the year of my schedule                                          | plan beyond my acedemic years                                                  |
| v2.0    | University Student | customise the duration to add a certain recurring event                    | adjust it based on the event                                                   |
| v2.0    | University Student | have reminders for events and deadlines coming soon                        | plan my time accordingly to complete/attend them                               |
| v2.0    | University Student | have different views of events                                             | so that it is clearer to disntinguish the important ones without having others |


## Non-Functional Requirements

1. Should work on any mainstream OS as long as it has Java `17` or above installed.
2. Should be able to hold up to 500 tasks/courses without a noticeable reduction in performance
3. A user with above average typing speed for regular English text 
can accomplish most of the tasks faster using commands than using the mouse.

## Glossary

* *Mainstream OS* - Windows, Linux, Unix, MacOS

## Instructions for manual testing

{Give instructions on how to do a manual product testing e.g., how to load sample data to be used for testing}
