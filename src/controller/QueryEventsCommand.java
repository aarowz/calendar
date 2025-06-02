// Dreshta Boghra & Aaron Zhou
// CS3500 HW4

package controller;

import model.ICalendar;
import model.IEvent;
import view.IView;
import exceptions.CommandExecutionException;

import java.time.LocalDate;
import java.util.List;

/**
 * Represents a command to query and display events on a specific date.
 */
public class QueryEventsCommand implements ICommand {

  private final LocalDate queryDate;

  /**
   * Constructs a QueryEventsCommand with the specified query date.
   *
   * @param queryDate the date for which to retrieve events
   */
  public QueryEventsCommand(LocalDate queryDate) {
    this.queryDate = queryDate;
  }

  /**
   * Executes the query operation, retrieving events on the specified date
   * and displaying them using the view.
   *
   * @param calendar the calendar model
   * @param view     the output view
   * @throws CommandExecutionException if the query fails
   */
  @Override
  public void execute(ICalendar calendar, IView view) throws CommandExecutionException {
    // TODO: retrieve events from the model for the given date
    // TODO: format and render those events using the view
  }

  /**
   * Returns a string representation of the command.
   */
  @Override
  public String toString() {
    return "QueryEventsCommand{date=" + queryDate + "}";
  }
}