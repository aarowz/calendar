// Dreshta Boghra & Aaron Zhou
// CS3500 HW4

package controller;

import model.ICalendar;
import model.IEvent;
import view.IView;
import exceptions.CommandExecutionException;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

/**
 * Represents a command to query and display events on a specific date.
 * Delegates querying to the model and output to the view.
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
   * @param view     the output view to display results
   * @throws CommandExecutionException if the query fails
   */
  @Override
  public void execute(ICalendar calendar, IView view) throws CommandExecutionException {
    try {
      List<IEvent> events = calendar.getEventsOn(queryDate);
      if (events.isEmpty()) {
        view.renderMessage("No events found on " + queryDate);
      } else {
        StringBuilder sb = new StringBuilder();
        sb.append("Events on ").append(queryDate).append(":\n");
        for (IEvent event : events) {
          sb.append("- ").append(event.toString()).append("\n");
        }
        view.renderMessage(sb.toString());
      }
    } catch (IOException e) {
      throw new CommandExecutionException("Failed to render query results", e);
    } catch (Exception e) {
      throw new CommandExecutionException("Failed to query events: " + e.getMessage(), e);
    }
  }

  /**
   * Returns a string representation of the command.
   *
   * @return a description of the query command
   */
  @Override
  public String toString() {
    return "QueryEventsCommand{date=" + queryDate + "}";
  }
}