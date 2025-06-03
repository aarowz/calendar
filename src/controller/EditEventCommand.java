package controller;

import model.*;
import view.IView;
import exceptions.CommandExecutionException;

import java.io.IOException;
import java.time.LocalDateTime;

/**
 * Represents a command to edit a calendar event.
 * This command uses a builder to create the updated event, preserving immutability.
 */
public class EditEventCommand implements ICommand {

  private final String originalSubject;
  private final LocalDateTime originalStart;

  private final String newSubject;
  private final LocalDateTime newStart;
  private final LocalDateTime newEnd;
  private final String newDescription;
  private final String newLocation;
  private final EventStatus newStatus;

  /**
   * Constructs an EditEventCommand with new values to overwrite the original event.
   * Any value may be null, indicating that the original value should be kept.
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
   * Executes this command by finding the target event and replacing it with an updated version.
   *
   * @param calendar the calendar model
   * @param view     the view for output messages
   * @throws CommandExecutionException if the event could not be edited
   */
  @Override
  public void execute(ICalendar calendar, IView view) throws CommandExecutionException {
    try {
      IEvent original = calendar.findEvent(originalSubject, originalStart);
      if (original == null) {
        throw new CommandExecutionException("Original event not found.");
      }

      String updatedSubject = newSubject != null ? newSubject : original.getSubject();
      LocalDateTime updatedStart = newStart != null ? newStart : original.getStart();
      LocalDateTime updatedEnd = newEnd != null ? newEnd : original.getEnd();
      String updatedDesc = newDescription != null ? newDescription : original.getDescription();
      String originalLocationStr = original.getLocation() == null ? null : original.getLocation().toString();
      String updatedLocation = newLocation != null ? newLocation : originalLocationStr;
      EventStatus updatedStatus = newStatus != null ? newStatus : original.getStatus();

      IEvent updated = new CalendarEventBuilder()
              .setSubject(updatedSubject)
              .setStart(updatedStart)
              .setEnd(updatedEnd)
              .setDescription(updatedDesc)
              .setLocation(updatedLocation)
              .setStatus(updatedStatus)
              .build();

      calendar.removeEvent(original);
      calendar.addEvent(updated);

      view.renderMessage("Event edited: " + updatedSubject);
    } catch (Exception e) {
      throw new CommandExecutionException("Failed to edit event: " + e.getMessage(), e);
    }
  }

  /**
   * Returns a string summary of the edit command.
   *
   * @return description string
   */
  @Override
  public String toString() {
    return "EditEventCommand{editing \"" + originalSubject + "\" @ " + originalStart + "}";
  }
}