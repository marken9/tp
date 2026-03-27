# Developer Guide

## Acknowledgements

{list here sources of all reused/adapted ideas, code, documentation, and third-party libraries -- include links to the original source as well}

{Describe the design and implementation of the product. Use UML diagrams and short code snippets where applicable.}

## Design

### Architecture

![MainArchitecture]()

The **Architecture Diagram** given above explains the high-level design of the App

Given below is a quick overview of the main components and how they interact with each other.

**Main components of the architecture**

UniTasker is in charge of the app launch and shut down
- At app launch, it initializes the other components.
- At shut down, it shuts down the other components.

The bulk of the app's work is done by the following components:
- Command: 
- UI:
- AppContainer:
- Storage:

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




## Implementation

**TaskValidator**

## Product scope
### Target user profile

University students who need to manage multiple courses, assignments, deadlines, and personal tasks simultaneously. 
These users often juggle academic responsibilities across different modules, each with varying deadlines, 
priorities, and schedules. They require a simple and efficient system to organise their tasks,
keep track of coursework, and stay on top of deadlines.

### Value proposition

University students often struggle to keep track of tasks and course assessments across different 
platforms such as learning portals, calendars and notes. This fragmented approach leads
to missed deadlines, poor prioritisation, and unnecessary stress. UniTasker provides a centralized 
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

{Give non-functional requirements}

## Glossary

* *glossary item* - Definition

## Instructions for manual testing

{Give instructions on how to do a manual product testing e.g., how to load sample data to be used for testing}
