// Dreshta Boghra & Aaron Zhou
// CS3500 HW4

package controller;

import model.*;
import view.IView;
import exceptions.CommandExecutionException;

import java.io.IOException;
import java.time.LocalDateTime;

/**
 * Represents a command to edit multiple calendar events.
 * This command uses a builder to create the updated event, preserving immutability.
 */
public class EditEventsCommand implements ICommand {

  // original start time is used to locate matching events
  private final LocalDateTime originalStart;

  // updated values for each editable field
  private final String newSubject;
  private final LocalDateTime newStart;
  private final LocalDateTime newEnd;
  private final String newDescription;
  private final String newLocation;
  private final EventStatus newStatus;

  /**
   * Constructs a command for editing events that match a given start time.
   * Any non-null field is used to update the matching events.
   *
   * @param originalStart  start time used to find the event(s)
   * @param newSubject     new subject (nullable)
   * @param newStart       new start time (nullable)
   * @param newEnd         new end time (nullable)
   * @param newDescription new description (nullable)
   * @param newLocation    new location (nullable)
   * @param newStatus      new status (nullable)
   */
  public EditEventsCommand(LocalDateTime originalStart,
                           String newSubject,
                           LocalDateTime newStart,
                           LocalDateTime newEnd,
                           String newDescription,
                           String newLocation,
                           EventStatus newStatus) {
    this.originalStart = originalStart;
    this.newSubject = newSubject;
    this.newStart = newStart;
    this.newEnd = newEnd;
    this.newDescription = newDescription;
    this.newLocation = newLocation;
    this.newStatus = newStatus;
  }

  /**
   * Executes the edit command by updating all matching events in the model.
   *
   * @param calendar the calendar model
   * @param view     the view to render output messages
   * @throws CommandExecutionException if editing fails
   */
  @Override
  public void execute(ICalendar calendar, IView view) throws CommandExecutionException {
    try {
      // create the new updated event from input fields
      calendar.editEvents(originalStart, (oldEvent) -> {
        CalendarEvent.Builder builder = new CalendarEvent.Builder();

        // set each field to new value if it exists, otherwise copy original
        builder.subject(newSubject != null ? newSubject : oldEvent.getSubject());
        builder.start(newStart != null ? newStart : oldEvent.getStart());
        builder.end(newEnd != null ? newEnd : oldEvent.getEnd());
        builder.description(newDescription != null ? newDescription : oldEvent.getDescription());
        builder.location(newLocation != null ? newLocation : oldEvent.getLocation());
        builder.status(newStatus != null ? newStatus : oldEvent.getStatus());

        return builder.build();
      });

      // let the user know what happened
      view.renderMessage("Event(s) edited: start=" + originalStart);

    } catch (Exception e) {
      throw new CommandExecutionException("Failed to edit events: " + e.getMessage(), e);
    }
  }

  /**
   * Returns a string representation of the edit command.
   *
   * @return summary string
   */
  @Override
  public String toString() {
    return "EditEventsCommand{start=" + originalStart + "}";
  }
}