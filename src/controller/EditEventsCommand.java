// Dreshta Boghra & Aaron Zhou
// CS3500 HW4

package controller;

import java.io.IOException;
import java.time.LocalDateTime;

import model.EventStatus;
import model.ICalendar;
import view.IView;
import exceptions.CommandExecutionException;

/**
 * Represents a command to edit a group of related calendar events.
 * This typically modifies a subset of a recurring series (starting from a given instance).
 */
public class EditEventsCommand implements ICommand {

  private final String originalSubject;
  private final LocalDateTime originalStart;

  private final String newSubject;
  private final LocalDateTime newStart;
  private final LocalDateTime newEnd;
  private final String newDescription;
  private final String newLocation;
  private final String newStatus;

  /**
   * Constructs an EditEventsCommand to edit multiple events in a series.
   *
   * @param originalSubject the subject used to find the series
   * @param originalStart   the date/time to anchor the edit
   * @param newSubject      the new subject
   * @param newStart        the new start time
   * @param newEnd          the new end time
   * @param newDescription  the updated description
   * @param newLocation     the updated location
   * @param newStatus       the updated status ("public" or "private")
   */
  public EditEventsCommand(String originalSubject,
                           LocalDateTime originalStart,
                           String newSubject,
                           LocalDateTime newStart,
                           LocalDateTime newEnd,
                           String newDescription,
                           String newLocation,
                           String newStatus) {
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
   * Executes the command by applying edits to all events in the series starting from
   * the specified one.
   *
   * @param calendar the calendar model
   * @param view     the view for rendering output or errors
   * @throws CommandExecutionException if something goes wrong during the update
   */
  @Override
  public void execute(ICalendar calendar, IView view) throws CommandExecutionException,
          IOException {
    try {
      // convert string status to enum only if provided
      EventStatus parsedStatus = null;
      if (newStatus != null) {
        parsedStatus = EventStatus.valueOf(newStatus.toUpperCase());
      }

      // apply edit to subset of series
      calendar.editEvents(
              originalSubject,
              originalStart,
              newSubject,
              newStart,
              newEnd,
              newDescription,
              parsedStatus,
              newLocation
      );

      // notify user
      view.renderMessage("Events successfully edited.\n");

    } catch (Exception e) {
      view.renderMessage("Failed to edit event: " + e.getClass().getSimpleName() + "\n");
      throw new CommandExecutionException("EditEventsCommand failed", e);
    }
  }

  /**
   * Returns a string representation for debugging or logging.
   */
  @Override
  public String toString() {
    return String.format(
            "EditEventsCommand: [%s at %s] → subject='%s', start=%s, " +
                    "end=%s, desc='%s', loc='%s', status='%s'",
            originalSubject, originalStart,
            newSubject, newStart, newEnd,
            newDescription, newLocation, newStatus
    );
  }
}