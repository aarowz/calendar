# CS3500 Assignment 4: Calendar (Part 1)
By Dreshta Boghra & Aaron Zhou

---

## 1) How to Run Our Program

Launch `CalendarApp.java` using this command:

```
java CalendarApp --mode interactive
```

This should start the program in **interactive mode** (basically we're saying that the user can apply calendar commands directly).

To run the program in **headless mode**, supply a path to a `.txt` file containing commands:

```
java CalendarApp --mode headless path/to/commands.txt
```

> Notes:
>
> - We added valid-commands, invalid-commands, and no-exit commands in the res directory that can be run in headless mode.
> - Command-line options are case-insensitive.

---

## 2) Which Features Work and Which Features Don't

### Fully Implemented (View + Controller)

- Creating single and recurring events (via builder pattern)
- Editing single or recurring events (via builder pattern)
- Querying events on a date
- Showing busy/available status
- Text-based command parsing and execution
- Interactive and headless CLI modes
- Full separation of MVC layers
- All controller/view code adheres to the **Builder Pattern**

### Not Yet Completed

- Edge cases lol (we tested thoroughly but there's always more to discover ...)

---

## 3) Team Member Contributions

| Component         | Contributor     |
| ----------------- |-----------------|
| Model             | Dreshta         |
| View              | Aaron           |
| Controller        | Aaron           |
| App Entry Point   | Dreshta & Aaron |
| Model Tests       | Dreshta         |
| View Tests        | Aaron           |
| Controller Tests  | Aaron           |

---

## 4) Additional Information for Grading

- We added a TON of comments to be able to keep track of where we are for the follow-up assignments.
- We tried our best so that all controller and view components use **Builder pattern** for construction.
- We made sure that code follows **MVC design principles** with separation between model, view, and controller for both tests and implementations.
- We made sure that each command is encapsulated in its own class using the **Command design pattern**.
- We made sure (or at least believe) that our entire project is documented with **Javadoc** and **consistent formatting**.