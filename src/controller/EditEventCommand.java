// Dreshta Boghra & Aaron Zhou
// CS3500 HW4

package controller;

import model.*;
import view.IView;
import exceptions.CommandExecutionException;

import java.io.IOException;
import java.time.LocalDateTime;

/**
 * Represents a command to edit a calendar event.
 * This command uses a builder to construct the updated event with immutability.
 */
public class EditEventCommand implements ICommand {

  // the original subject and start time to identify the event to edit
  private final String originalSubject;
  private final LocalDateTime originalStart;

  // new field values (nullable) to overwrite the original event's fields
  private final String newSubject;
  private final LocalDateTime newStart;
  private final LocalDateTime newEnd;
  private final String newDescription;
  private final String newLocation;
  private final EventStatus newStatus;

  /**
   * Constructs an EditEventCommand with optional new values for the event fields.
   * Null values indicate that the field should remain unchanged.
   *
   * @param originalSubject the original event subject
   * @param originalStart   the original event start time
   * @param newSubject      new subject (nullable)
   * @param newStart        new start time (nullable)
   * @param newEnd          new end time (nullable)
   * @param newDescription  new description (nullable)
   * @param newLocation     new location (nullable)
   * @param newStatus       new status (nullable)
   */
  public EditEventCommand(String originalSubject,
                          LocalDateTime originalStart,
                          String newSubject,
                          LocalDateTime newStart,
                          LocalDateTime newEnd,
                          String newDescription,
                          String newLocation,
                          EventStatus newStatus) {
    // assign input values to fields
    this.originalSubject = originalSubject;
    this.originalStart = originalStart;
    this.newSubject = newSubject;
    this.newStart = newStart;
    this.newEnd = newEnd;
    this.newDescription = newDescription;
    this.newLocation = newLocation;
    this.newStatus = newStatus;
  }

  /**
   * Executes this command by finding and replacing the original event with an updated one.
   *
   * @param calendar the calendar model
   * @param view     the view for rendering output
   * @throws CommandExecutionException if the update fails
   */
  @Override
  public void execute(ICalendar calendar, IView view) throws CommandExecutionException {
    try {
      // start a new builder for the updated event
      CalendarEvent.Builder builder = new CalendarEvent.Builder();

      // only apply fields that aren't null
      if (newSubject != null) {
        builder.subject(newSubject); // set new subject
      }

      if (newStart != null) {
        builder.start(newStart); // set new start
      }

      if (newEnd != null) {
        builder.end(newEnd); // set new end
      }

      if (newDescription != null) {
        builder.description(newDescription); // set new description
      }

      if (newLocation != null) {
        builder.location(newLocation); // set new location
      }

      if (newStatus != null) {
        builder.status(newStatus); // set new status
      }

      // tell the model to update the event using this builder
      calendar.editEvent(originalSubject, originalStart, builder.build());

      // send confirmation to the user
      view.renderMessage("Event edited: " + originalSubject + " @ " + originalStart);
    } catch (Exception e) {
      // something went wrong so we wrap it in a CommandExecutionException
      throw new CommandExecutionException("Failed to edit event: " + e.getMessage(), e);
    }
  }

  /**
   * Returns a string summary of this command.
   *
   * @return description of the edit command
   */
  @Override
  public String toString() {
    // include original subject and time for clarity
    return "EditEventCommand{editing \"" + originalSubject + "\" @ " + originalStart + "}";
  }
}