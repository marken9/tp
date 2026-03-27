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

### Adding a todo: `todo`
Adds a new item to the list of todo items.

Format: `todo n/TODO_NAME d/DEADLINE`

* The `DEADLINE` can be in a natural language format.
* The `TODO_NAME` cannot contain punctuation.  

Example of usage: 

`todo n/Write the rest of the User Guide d/next week`

`todo n/Refactor the User Guide to remove passive voice d/13/04/2020`

## FAQ

**Q**: How do I transfer my data to another computer? 

**A**: Copy all the .txt files inside the folder with UniTasker and paste in the same folder where UniTasker.jar
is located in the other computer.

## Command Summary

{Give a 'cheat sheet' of commands here}

* Add todo `todo n/TODO_NAME d/DEADLINE`
