// Dreshta Boghra & Aaron Zhou
// CS3500 HW4

package controller;

import model.ICalendar;
import model.EventStatus;
import view.IView;
import exceptions.CommandExecutionException;

import java.time.LocalDateTime;

/**
 * Represents a command to edit a calendar event.
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
   * Constructs an edit command with fields to modify an existing event.
   *
   * @param originalSubject the subject of the event to be edited
   * @param originalStart   the original start time of the event to be edited
   * @param newSubject      the new subject (nullable to keep unchanged)
   * @param newStart        the new start time (nullable to keep unchanged)
   * @param newEnd          the new end time (nullable to keep unchanged or all-day)
   * @param newDescription  the new description (nullable to keep unchanged)
   * @param newLocation     the new location (nullable to keep unchanged)
   * @param newStatus       the new status (nullable to keep unchanged)
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
   * Executes the edit operation on the calendar.
   *
   * @param calendar the calendar model
   * @param view     the output view
   * @throws CommandExecutionException if the event cannot be found or the edit fails
   */
  @Override
  public void execute(ICalendar calendar, IView view) throws CommandExecutionException {
    // TODO: locate the original event using originalSubject and originalStart
    // TODO: validate and apply changes to the found event
    // TODO: replace the old event with the edited one in the model
    // TODO: show success or failure message using the view
  }

  /**
   * Optional: returns a string representation of the command.
   */
  @Override
  public String toString() {
    return "EditEventCommand{editing \"" + originalSubject + "\" @ " + originalStart + "}";
  }
}