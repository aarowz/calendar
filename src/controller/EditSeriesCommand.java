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
 * Represents a command to edit all events in a series.
 * This modifies all occurrences that share the original subject,
 * starting from the specified instance forward in time.
 */
public class EditSeriesCommand implements ICommand {

  private final String originalSubject;
  private final LocalDateTime originalStart;

  private final String newSubject;
  private final LocalDateTime newStart;
  private final LocalDateTime newEnd;
  private final String newDescription;
  private final String newLocation;
  private final String newStatus;

  /**
   * Constructs a command to edit an entire series of calendar events.
   *
   * @param originalSubject the original subject used to find the series
   * @param originalStart   the starting point in the series to begin editing
   * @param newSubject      the new subject/title for each event
   * @param newStart        the new start time
   * @param newEnd          the new end time
   * @param newDescription  the updated description
   * @param newLocation     the updated location
   * @param newStatus       the updated visibility ("public" or "private")
   */
  public EditSeriesCommand(String originalSubject,
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
   * Executes the command to edit all events in a series.
   *
   * @param calendar the model to operate on
   * @param view     the output view for user interaction
   * @throws CommandExecutionException if the series could not be edited
   */
  @Override
  public void execute(ICalendar calendar, IView view)
          throws CommandExecutionException, IOException {
    try {
      // only parse status if it is being updated
      EventStatus parsedStatus = null;
      if (newStatus != null) {
        parsedStatus = EventStatus.valueOf(newStatus.toUpperCase());
      }

      // update all events in the series
      calendar.editEventSeries(
              originalSubject,
              originalStart,
              newSubject,
              newStart,
              newEnd,
              newDescription,
              parsedStatus,
              newLocation
      );

      view.renderMessage("Event series successfully edited.\n");

    } catch (IllegalArgumentException | NullPointerException e) {
      throw new CommandExecutionException("Failed to edit series: " + e.getMessage());
    }
  }

  /**
   * Returns a string representation of this command for debugging.
   */
  @Override
  public String toString() {
    return String.format(
            "EditSeriesCommand: [%s at %s] → subject='%s', start=%s, end=%s, " +
                    "desc='%s', loc='%s', status='%s'",
            originalSubject, originalStart,
            newSubject, newStart, newEnd,
            newDescription, newLocation, newStatus
    );
  }
}