# CS3500 Assignment 5: Calendar (Part 2)

By Dreshta Boghra & Aaron Zhou

## Intro

In Assignment 4, we only supported a single calendar and used ICalendar
in the controller. But now that we have features like switching between calendars and
copying events across them, we needed a way to group everything together.

IDelegator extends both ICalendar and ICalendarMulti, so it can handle both single-calendar and
multi-calendar operations. This makes our design more flexible and easier to extend in the future.
Similar to the facade design pattern, our IDelegator is the only class in the model that interacts
with the controller and vice versa.
We added the new commands for create, edit, use, and the 3 copy commands from assignment 5 as their
own classes in the controller.

We chose to disallow empty string calendar names ("") for usability and clarity. Although
the assignment does not explicitly forbid them, an empty name leads to ambiguous behavior
and breaks meaningful calendar selection and display. This design decision keeps the
interface clean and predictable.

We fixed our print statement thanks to the feedback from the
assignment 4 grading. However, we did not have to implement some of the assignment 4 self eval
recommendations due to how late it was given back to us. Please do not take points off for that.
---

## 1) Changes in Our Program

| #  | Change Description                    | Justification                                                                                                                                                       |
|----|---------------------------------------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| 1  | Introduced IDelegator interface       | - To centralize access between controller and model <br/> - To enable easy support for multiple backends (single vs. multi-calendar) without controller refactoring |
| 2  | Added DelegatorImpl class             | - To implement the new IDelegator <br/> - To route all model operations through a single class                                                                      |
| 3  | Added ICalendarMulti interface        | - To define a clean contract for managing multiple calendars and more                                                                                               |
| 4  | Added CalendarMulti class             | - To implement logic dealing with calendars as a whole                                                                                                              |
| 5  | Added CalendarWrapper class           | - To associate each calendar with a timezone in a clean, encapsulated way instead of hard coding it into calendar logic(so the original class doesn't change)       |
| 6  | Added TimeZoneUtil class using ZoneId | - To ensure that each calendar operates in its own timezone while preserving correct event behavior                                                                 |
| 7  | Added CreateCalendarCommand class     | - To support creation of named calendars with specified timezone                                                                                                    |
| 8  | Added UseCalendarCommand class        | - To enable calendar context switching                                                                                                                              |
| 9  | Added EditCalendarCommand class       | - To enable calendar context renaming                                                                                                                               |
| 10 | Added CopyEventCommand class          | - To enable single-day copying with timezone conversion                                                                                                             |
| 10 | Added CopyEventsOnCommand class       | - To enable date identifiable copying with timezone conversion                                                                                                      |
| 10 | Added CopyEventsBetweenCommand class  | - To enable date-range copying with timezone conversion                                                                                                             |
| 11 | Refactored CommandParser class        | - To fully apply the Command Design Pattern <br/> - To add helper method that ensure that each main paring method does not become too long                          |

We also added test classes for each of the new command classes we added and
for each new class we added in the model.

---

## 2) How to Run Our Program

On macOS, in Terminal, navigate to the folder containing the file `calendarGit.jar`. Next, enter
the command:

```
java -jar calendarGit.jar --mode interactive
```

This should start the program in **interactive mode** (
basically we're saying that the user can apply calendar commands directly).

To run the program in **headless mode**, navigate to the same folder containing the file
calendarGit.jar and supply a path to a `.txt` file containing commands:

```
java -jar calendarGit.jar --mode headless [command file path]
```

---

## 3) Which Features Work and Which Features Don't

### Fully Implemented (View + Controller)

| Category             | Feature Description                                                                                                                                                               |
|----------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **Event Creation**   | Creating single and recurring events (via builder pattern)                                                                                                                        |
| **Event Editing**    | Editing single or recurring events (via builder pattern)                                                                                                                          |
| **Event Querying**   | Querying events on a date or within a date range                                                                                                                                  |
| **Availability**     | Checking busy/available status at a given time                                                                                                                                    |
| **Calendars**        | Creating new named calendars with associated timezones <br/> Renaming calendar or changing its timezone <br/> Switching between calendars via `use calendar` command              |
| **Copying Events**   | A single event to another calendar at a new time <br/> All events on a specific day to another calendar <br/> A range of events (with series logic preserved) to another calendar |
| **Timezone Support** | Full timezone conversion support using `ZoneId`                                                                                                                                   |
| **CLI Modes**        | Interactive and headless CLI modes via `--mode`                                                                                                                                   |
| **Command Parsing**  | Text-based command parsing and execution                                                                                                                                          |
| **Termination**      | Clean app termination via `exit` command                                                                                                                                          |
| **Architecture**     | Full MVC separation (model, view, controller)                                                                                                                                     |
| **Design Patterns**  | Each command is implemented as a class (Command Pattern) <br/> Controller and view components built using **Builder Pattern**                                                     |

### Not Yet Completed

| Area           | Status                                                        |
|----------------|---------------------------------------------------------------|
| Edge Cases lol | We tested thoroughly, but there's always more to discover ... |

---

## 4) Team Member Contributions for assignment 4 and 5

| Component        | Contributor     |
|------------------|-----------------|
| Model            | Dreshta         |
| View             | Aaron           |
| Controller       | Aaron           |
| Util             | Dreshta & Aaron |
| App Entry Point  | Dreshta & Aaron |
| Model Tests      | Dreshta         |
| View Tests       | Aaron           |
| Controller Tests | Dreshta & Aaron |

---

## 5) Additional Information for Grading

- We added a TON of comments to be able to keep track of where we are for the follow-up assignments.
- We tried our best so that all controller and view components use **Builder pattern** for
  construction.
- We made sure that code follows **MVC design principles** with separation between model, view, and
  controller for both tests and implementations.
- We made sure that each command is encapsulated in its own class using the **Command design pattern
  **.
- We made sure (or at least believe) that our entire project is documented with **Javadoc** and *
  *consistent formatting**.