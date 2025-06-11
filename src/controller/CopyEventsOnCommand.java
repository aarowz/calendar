// Dreshta Boghra & Aaron Zhou
// CS3500 HW5

package controller;

import java.io.IOException;
import java.time.LocalDate;

import exceptions.CommandExecutionException;
import model.IDelegator;
import view.IView;

/**
 * Command for copying all events from a specific date to a new calendar/date.
 */
public class CopyEventsOnCommand implements ICommand {
  private final LocalDate sourceDate;
  private final String targetCalendar;
  private final LocalDate targetDate;

  /**
   * Constructs a command to copy all events from one calendar on a specific date
   * to another calendar on a new date.
   *
   * @param sourceDate     the date to copy events from
   * @param targetCalendar the name of the target calendar
   * @param targetDate     the date to copy events to in the target calendar
   */
  public CopyEventsOnCommand(LocalDate sourceDate, String targetCalendar, LocalDate targetDate) {
    this.sourceDate = sourceDate;
    this.targetCalendar = targetCalendar;
    this.targetDate = targetDate;
  }

  @Override
  public void execute(IDelegator model, IView view) throws CommandExecutionException, IOException {
    try {
      // delegate to the model to copy all events on the given date
      model.copyEventsOn(sourceDate, targetCalendar, targetDate);

      // inform the user that the copy succeeded
      view.renderMessage("Copied all events from " + sourceDate +
              " to calendar '" + targetCalendar +
              "' on " + targetDate);
    } catch (Exception e) {
      // wrap and rethrow any model-level errors
      throw new CommandExecutionException("Failed to copy events on " +
              sourceDate + ": " + e.getMessage());
    }
  }
}