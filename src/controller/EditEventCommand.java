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
   * @param model the calendar model
   * @param view  the view for output messages
   * @throws CommandExecutionException if the event could not be edited
   */
  @Override
  public void execute(ICalendar model, IView view) throws CommandExecutionException {
    // TO ADDRESS LATER: find out why we get an error when using CalendarModel instead of ICalendar
    // because CalendarModel implements ICalendar
    try {
      model.editEvent(originalSubject, originalStart,
              new CalendarEvent.Builder()
                      .subject(newSubject)
                      .start(newStart)
                      .end(newEnd)
                      .description(newDescription)
                      .location(newLocation)
                      .status(newStatus)
                      .build());
    } catch (IllegalArgumentException e) {
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