// Dreshta Boghra & Aaron Zhou
// CS3500 HW4

package controller;

import model.IDelegator;
import model.ROIEvent;
import view.IView;
import exceptions.CommandExecutionException;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Represents a command to query and display calendar events, either on a specific date
 * or between a range of start and end date times.
 */
public class QueryEventsCommand implements ICommand {
  private final LocalDate queryDate;
  private final LocalDateTime rangeStart;
  private final LocalDateTime rangeEnd;

  /**
   * Constructor for a single-day query.
   *
   * @param queryDate the date for which events are queried
   */
  public QueryEventsCommand(LocalDate queryDate) {
    this.queryDate = queryDate;
    this.rangeStart = null;
    this.rangeEnd = null;
  }

  /**
   * Constructor for a range-based query.
   *
   * @param rangeStart the start datetime
   * @param rangeEnd   the end datetime
   */
  public QueryEventsCommand(LocalDateTime rangeStart, LocalDateTime rangeEnd) {
    this.queryDate = null;
    this.rangeStart = rangeStart;
    this.rangeEnd = rangeEnd;
  }

  /**
   * Executes the query and displays results to the user.
   * Chooses between single-day or date-range logic based on which constructor was used.
   */
  @Override
  public void execute(IDelegator model, IView view) throws CommandExecutionException {
    try {
      if (queryDate != null) {
        // original logic for: print events on <date>
        List<ROIEvent> events = model.getEventsOn(queryDate);
        if (events.isEmpty()) {
          view.renderMessage("No events found on " + queryDate);
        } else {
          StringBuilder sb = new StringBuilder("Events on " + queryDate + ":\n");
          for (ROIEvent event : events) {
            sb.append("- ").append(event.getSubject())
                    .append(" from ").append(event.getStart())
                    .append(" to ").append(event.getEnd());

            if (event.getLocation() != null) {
              System.out.println("entered null part of the if");
              if (!event.getLocation().isEmpty()) {
                System.out.println("entered empty part of the if");
                sb.append(" at ").append(event.getLocation());
              }

            }
            sb.append("\n");
          }
          view.renderMessage(sb.toString());
        }

      } else if (rangeStart != null && rangeStart.equals(rangeEnd)) {
        // new logic for: show status on <datetime>
        boolean isBusy = model.isBusyAt(rangeStart);
        view.renderMessage(isBusy ? "busy" : "available");

      } else {
        // original logic for: print events from <start> to <end>
        List<ROIEvent> events = model.getEventsBetween(rangeStart, rangeEnd);
        if (events.isEmpty()) {
          view.renderMessage("No events found between " + rangeStart + " and " + rangeEnd);
        } else {
          StringBuilder sb = new StringBuilder("Events from " + rangeStart + " to "
                  + rangeEnd + ":\n");
          for (ROIEvent event : events) {
            sb.append("- ").append(event.getSubject())
                    .append(" from ").append(event.getStart())
                    .append(" to ").append(event.getEnd());

            if (event.getLocation() != null && !event.getLocation().isEmpty()) {
              sb.append(" at ").append(event.getLocation());
            }
            sb.append("\n");
          }
          view.renderMessage(sb.toString());
        }
      }
    } catch (IOException e) {
      throw new CommandExecutionException("Failed to render query results", e);
    } catch (Exception e) {
      throw new CommandExecutionException("Failed to query events: " + e.getMessage(), e);
    }
  }


  /**
   * Return a string representation of the output for debugging.
   *
   * @return the string representation of the query result
   */
  @Override
  public String toString() {
    if (queryDate != null) {
      return "QueryEventsCommand{date=" + queryDate + "}";
    } else {
      return "QueryEventsCommand{range=" + rangeStart + " to " + rangeEnd + "}";
    }
  }
}