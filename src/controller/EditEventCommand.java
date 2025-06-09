// Dreshta Boghra & Aaron Zhou
// CS3500 HW4

package controller;

import java.io.IOException;
import java.time.LocalDateTime;

import model.EventStatus;
import model.IDelegator;
import view.IView;
import exceptions.CommandExecutionException;

/**
 * Represents a command to edit a single calendar event.
 * This command identifies the event by its subject and start time,
 * then replaces it with a new set of properties. Works for both
 * standalone events and events that are part of a series.
 */
public class EditEventCommand implements ICommand {
  private final String originalSubject;
  private final LocalDateTime originalStart;
  private final String newSubject;
  private final LocalDateTime newStart;
  private final LocalDateTime newEnd;
  private final String newDescription;
  private final String newLocation;
  private final String newStatus;

  /**
   * Constructs a command for editing a single event.
   *
   * @param originalSubject the original subject used to identify the event
   * @param originalStart   the original start time of the event
   * @param newSubject      the new subject/title of the event
   * @param newStart        the new start time
   * @param newEnd          the new end time
   * @param newDescription  the new description
   * @param newLocation     the new location
   * @param newStatus       the new visibility status ("public" or "private"),
   *                        or null if unchanged
   */
  public EditEventCommand(String originalSubject,
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
   * Executes the command by editing one event in the calendar model.
   * Fills in original values for any missing required fields.
   *
   * @param model the calendar model to update
   * @param view  the view to report output to
   * @throws CommandExecutionException if editing fails for any reason
   */
  @Override
  public void execute(IDelegator model, IView view) throws CommandExecutionException,
          IOException {
    try {
      // parse status if provided
      EventStatus parsedStatus = null;
      if (newStatus != null) {
        parsedStatus = EventStatus.valueOf(newStatus.toUpperCase());
      }

      // fill in subject/start defaults if missing (required by event builder)
      String finalSubject = (newSubject != null) ? newSubject : originalSubject;
      LocalDateTime finalStart = (newStart != null) ? newStart : originalStart;

      // call the model to apply the update
      model.editEvent(
              originalSubject,     // used for locating the event
              originalStart,       // used for locating the event
              finalSubject,        // updated or same subject
              finalStart,          // updated or same start
              newEnd,              // optional new end
              newDescription,      // optional new description
              parsedStatus,        // optional new status
              newLocation          // optional new location
      );

      // report success
      view.renderMessage("Event successfully edited.\n");

    } catch (Exception e) {
      // otherwise report failure
      view.renderMessage("Failed to edit event: " + e + "\n");
      throw new CommandExecutionException("EditEventCommand failed", e);
    }
  }

  /**
   * Returns a string representation of the command for logging/debugging.
   */
  @Override
  public String toString() {
    return String.format(
            "EditEventCommand: [%s at %s] → subject='%s', start=%s, end=%s, desc='%s', " +
                    "loc='%s', status='%s'",
            originalSubject, originalStart,
            newSubject, newStart, newEnd,
            newDescription, newLocation, newStatus
    );
  }
}