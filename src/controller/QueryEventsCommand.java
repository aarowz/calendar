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
      // create a list of the events that are on the given query date
      List<IEvent> events = calendar.getEventsOn(queryDate);

      // if there's no events on the given query date
      if (events.isEmpty()) {
        view.renderMessage("No events found on " + queryDate);
      } else {
        // create a string builder that accumulates the events on the given day
        StringBuilder sb = new StringBuilder();
        sb.append("Events on ").append(queryDate).append(":\n");

        // keep appending for each event
        for (IEvent event : events) {
          sb.append("- ").append(event.toString()).append("\n");
        }

        // render the events (basically display it in the console, and later probably through the
        // app view itself
        view.renderMessage(sb.toString());
      }
    } catch (IOException e) {
      // if there is an exception with the IO extensions that we use, indicate the internal error
      throw new CommandExecutionException("Failed to render query results", e);
    } catch (Exception e) {
      // if there is an exception with the command because the user inputted bogus, indicate the
      // error
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