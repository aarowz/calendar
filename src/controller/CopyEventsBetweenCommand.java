// Dreshta Boghra & Aaron Zhou
// CS3500 HW5

package controller;

import java.io.IOException;
import java.time.LocalDate;

import exceptions.CommandExecutionException;
import model.IDelegator;
import view.IView;

/**
 * Command for copying all events in a date range to a new calendar starting at a specified date.
 */
public class CopyEventsBetweenCommand implements ICommand {
  private final LocalDate startDate;
  private final LocalDate endDate;
  private final String targetCalendar;
  private final LocalDate targetStartDate;

  /**
   * Constructs a command to copy events from a date range in the current calendar
   * to a different calendar starting at a new base date.
   *
   * @param startDate       the start of the source range (inclusive)
   * @param endDate         the end of the source range (inclusive)
   * @param targetCalendar  the destination calendar name
   * @param targetStartDate the new base date in the target calendar
   */
  public CopyEventsBetweenCommand(LocalDate startDate, LocalDate endDate,
                                  String targetCalendar, LocalDate targetStartDate) {
    this.startDate = startDate;
    this.endDate = endDate;
    this.targetCalendar = targetCalendar;
    this.targetStartDate = targetStartDate;
  }

  @Override
  public void execute(IDelegator model, IView view) throws CommandExecutionException, IOException {
    try {
      // delegate to the model to copy events in the given range
      model.copyEventsBetween(startDate, endDate, targetCalendar, targetStartDate);

      // confirm to the user
      view.renderMessage("Copied events from range " + startDate + " to " + endDate +
              " to calendar '" + targetCalendar +
              "' starting on " + targetStartDate);
    } catch (Exception e) {
      throw new CommandExecutionException("Failed to copy events between " +
              startDate + " and " + endDate + ": " + e.getMessage());
    }
  }
}