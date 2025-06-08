# CS3500 Assignment 4: Calendar (Part 2)

By Dreshta Boghra & Aaron Zhou

---

## 1) Changes in Our Program

| #  | Change Description               | Justification               |
|----|----------------------------------|-----------------------------|
| 1  | [Insert change description here] | [Insert justification here] |
| 2  | [Insert change description here] | [Insert justification here] |
| 3  | [Insert change description here] | [Insert justification here] |
| 4  | [Insert change description here] | [Insert justification here] |
| 5  | [Insert change description here] | [Insert justification here] |
| 6  | [Insert change description here] | [Insert justification here] |
| 7  | [Insert change description here] | [Insert justification here] |
| 8  | [Insert change description here] | [Insert justification here] |
| 9  | [Insert change description here] | [Insert justification here] |
| 10 | [Insert change description here] | [Insert justification here] |

---

## 2) How to Run Our Program

In Intellij, go to `Run -> Edit Configurations -> Applications -> CalendarApp`. Next, go to program
arguments and type:

```
--mode interactive
```

Finally, click on `apply` and then `ok`. This should start the program in **interactive mode** (
basically we're saying that the user can apply calendar commands directly).

To run the program in **headless mode**, supply a path to a `.txt` file containing commands:

```
--mode headless absolute/path/to/commands.txt
```

---

## 3) Which Features Work and Which Features Don't

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

## 4) Team Member Contributions

| Component        | Contributor     |
|------------------|-----------------|
| Model            | Dreshta         |
| View             | Aaron           |
| Controller       | Aaron           |
| App Entry Point  | Dreshta & Aaron |
| Model Tests      | Dreshta         |
| View Tests       | Aaron           |
| Controller Tests | Aaron           |

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