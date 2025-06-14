# CS3500 Assignment 6: Calendar (Part 3)

By Dreshta Boghra & Aaron Zhou

## Intro

In Assignment 5, we completed the implementations of the model and controller for the calendar. But
now that we must implement a GUI for the entire program, we must focus on implementing the elements
that connect the controller and the view together.

We fixed the bugs that exist within our model and controller thanks to the feedback from the
assignment 5 grading. However, we did not have time to implement some of the assignment 5 self eval
recommendations due to how late it was given back to us. Please do not take points off for that.
---

## 1) Changes in Our Program
We added a GUICalendarController class in the controller and in the view, we added a WholeView, 
ScrollEventsPanel, ButtonPanel, CreateEventPanel, EditEventPanel, __(View events form this date)__, NewCalendarPanel, CalendarSelectorPanel 

| #  | Change Description | Justification |
|----|--------------------|---------------|
| 1  |                    |               |
| 2  |                    |               |
| 3  |                    |               |
| 4  |                    |               |
| 5  |                    |               |
| 6  |                    |               |
| 7  |                    |               |
| 8  |                    |               |
| 9  |                    |               |
| 10 |                    |               |

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

| Category             | Feature Description |
|----------------------|---------------------|
| **Event Creation**   |                     |
| **Event Editing**    |                     |
| **Event Querying**   |                     |
| **Availability**     |                     |
| **Calendars**        |                     |
| **Copying Events**   |                     |
| **Timezone Support** |                     |
| **CLI Modes**        |                     |
| **Command Parsing**  |                     |
| **Termination**      |                     |
| **Architecture**     |                     |
| **Design Patterns**  |                     |

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