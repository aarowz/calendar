# CS3500 Assignment 6: Calendar (Part 3)

## Authors

Dreshta Boghra & Aaron Zhou

---

## Intro

In Assignment 5, we completed the implementations of the model and controller for the calendar. But
now that we must implement a GUI for the entire program, we must focus on implementing the elements
that connect the controller and the view together.

We fixed the bugs that exist within our model and controller thanks to the feedback from the
assignment 5 grading. However, we did not have time to implement some of the assignment 5 self eval
recommendations due to how late it was given back to us. Please do not take points off for that.

---

## 1) Changes in Our Program

| #  | Change Description                         | Justification                                                                                                                                             |
|----|--------------------------------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------|
| 1  | Added ICalendarGuiFeatures interface       | Defined the controller’s feature set for the GUI to invoke in a way that avoided controller coupling.                                                     |
| 2  | Added CalendarGuiController class          | Implemented GUI-specific controller logic to handle user interactions and update the view/model.                                                          |
| 3  | Added ButtonPanel class                    | Encapsulated all GUI control buttons and their interactions, which helped us improve our layout modularity a LOT from our initial draft.                  |
| 4  | Added CalendarGuiView class                | Provided the top-level GUI window, managing layout, integration with controller, and visual updates.                                                      |
| 5  | Added CalendarSelectorPanel class          | Designed full support of switching calendars from a dropdown panel.                                                                                       |
| 6  | Added CreateEventPanel class               | Handled the input form for creating events, including UI elements and validation logic.                                                                   |
| 7  | Added DatePickerDialog class               | Created a reusable, user-friendly dialog for selecting dates in the GUI that applied for the adding and editing methods.                                  |
| 8  | Added EditEventPanel class                 | Provided a form for selecting and editing existing events with full validation and pre-fill support in case the user already had certain settings stored. |
| 9  | Added NewCalendarPanel class               | Fully implemented the panel we used to support creating new calendars in the GUI (planned future feature).                                                |
| 10 | Added ScrollEventsPanel class              | Displayed event lists in a scrollable format, showing inline status tags like [Private].                                                                  |
| 11 | Added DateDropDownUtil class               | Centralized dropdown logic for consistent date/time inputs across forms.                                                                                  |
| 12 | Added DefaultCalendarInitializerUtil class | Automatically ensured a default calendar is available in GUI mode.                                                                                        |
| 13 | Added EventFormDataUtil class              | Validated and parsed form input fields into usable LocalDateTime objects with error handling.                                                             |

---

## 2) How to Run Our Program

On macOS, in Terminal, navigate to the folder containing the file `calendarGit.jar`.

To run the program in **headless mode**, navigate to the same folder containing the file
calendarGit.jar and supply a path to a `.txt` file containing commands:

```
java -jar calendarGit.jar --mode headless [command file path]
```

To run the program in **interactive mode** (basically we're saying that the user can apply
calendar commands directly), execute the command:

```
java -jar calendarGit.jar --mode interactive
```

To open the **GUI**, execute the command:

```
java -jar calendarGit.jar
```

If any other command-line arguments are entered, the program will display an error message suitably
and quit.

---

## 3) Which Features Work and Which Features Don't

### Fully Implemented

| Category             | Feature Description                                                                                                          |
|----------------------|------------------------------------------------------------------------------------------------------------------------------|
| **Event Creation**   | We made sure that our users can create single or all-day events via a form. Private events are supported through a checkbox. |
| **Event Editing**    | We made sure that users can select and edit existing events, including subject, time, location, and privacy status.          |
| **Event Querying**   | We made sure that users can view up to 10 events from a selected date. Events are displayed in chronological order.          |
| **Availability**     | Our GUI supports a visual schedule view; the controls allow user-level "availability" visualization.                         |
| **Calendars**        | A default calendar is initialized; UI stubs are in place for switching and creating new calendars.                           |
| **Copying Events**   | Supported in CLI (headless mode); not available via GUI in this version.                                                     |
| **Timezone Support** | Each calendar is initialized with system timezone by default. Timezone editing is stubbed.                                   |
| **CLI Modes**        | Headless mode supports script-based calendar commands. GUI mode is fully interactive.                                        |
| **Command Parsing**  | Robust CLI command parser supports creation, editing, querying, copying, and more.                                           |
| **Termination**      | GUI closes gracefully via standard window controls. CLI completes upon script execution.                                     |
| **Architecture**     | Clean MVC structure with separate model, controller, and view layers. GUI components are modular.                            |
| **Design Patterns**  | Command, Strategy, MVC, and Interface Segregation are all demonstrated through extensible GUI logic.                         |

### Not Yet Completed

| Area           | Status                                                        |
|----------------|---------------------------------------------------------------|
| Edge Cases lol | We tested thoroughly, but there's always more to discover ... |

---

## 4) Team Member Contributions for assignment 4 and 5

| Component        | Contributor     |
|------------------|-----------------|
| Model            | Dreshta         |
| View             | Dreshta & Aaron |
| Controller       | Aaron           |
| Util             | Dreshta & Aaron |
| App Entry Point  | Dreshta & Aaron |
| Model Tests      | Dreshta         |
| View Tests       | Aaron           |
| Controller Tests | Dreshta & Aaron |
| GUI              | Dreshta & Aaron |

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