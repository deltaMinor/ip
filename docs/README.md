# HERM35 User Guide

// Product screenshot goes here

// Product intro goes here

## Adding deadlines

// Describe the action and its outcome.

// Give examples of usage

Example: `keyword (optional arguments)`

// A description of the expected outcome goes here

```
expected output
```

## Feature Command-Based Task Management

// HERM35 allows users to manage tasks using simple, keyword-based commands. Each user input is parsed by 
// the Parser and mapped to a corresponding Command object, such as adding tasks, listing tasks, marking 
// tasks as complete, deleting tasks, or exiting the chatbot. This command abstraction makes the chatbot
// extensible and easy to maintain.


## Feature Task Storage to Hard Disk

// The chatbot automatically loads previously saved tasks from storage when it starts and saves updates as
// commands are executed. If stored data cannot be read, HERM35 falls back to an empty task list, ensuring
// the chatbot remains usable.

## Feature Flexible Date and Time Parsing

// The chatbot supports flexible date and time input formats when creating deadlines and events. Users may
// enter dates using numeric formats (e.g. 12/3/2026), month names (e.g. 31 Dec 2026), or partial dates 
// that default to the current year. Optional time information is also supported, including 24-hour time,
// HH:MM format, and AM/PM notation.'

## Feature Event and Deadline Scheduling

// HERM35 supports multiple task types: to-do tasks, deadline tasks, and event tasks. Deadline tasks accept
// a /by specifier, while event tasks accept /from and /to specifiers, allowing users to clearly define 
// time-bounded tasks with start and end points.

## Feature Built-In Help and User Guidance

// The chatbot includes a help command that provides users with guidance on available commands and their
// usage. If users enter malformed or incomplete commands, HERM35 responds with specific and helpful error
// messages instead of generic failures.