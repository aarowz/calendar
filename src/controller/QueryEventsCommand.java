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
        handleEventsOnDate(model, view);
      } else if (rangeStart != null && rangeStart.equals(rangeEnd)) {
        handleBusyStatus(model, view);
      } else {
        handleEventsInRange(model, view);
      }
    } catch (IOException e) {
      throw new CommandExecutionException("Failed to render query results", e);
    } catch (Exception e) {
      throw new CommandExecutionException("Failed to query events: " + e.getMessage(), e);
    }
  }

  /**
   * Handles the "print events on date" command.
   * Queries and renders events on a specific date.
   *
   * @param model the calendar model
   * @param view  the view for rendering messages
   * @throws IOException if rendering fails
   */
  private void handleEventsOnDate(IDelegator model, IView view) throws IOException {
    List<ROIEvent> events = model.getEventsOn(queryDate);
    if (events.isEmpty()) {
      view.renderMessage("No events found on " + queryDate);
    } else {
      StringBuilder sb = new StringBuilder("Events on " + queryDate + ":\n");
      for (ROIEvent event : events) {
        appendEventDetails(sb, event);
      }
      view.renderMessage(sb.toString());
    }
  }

  /**
   * Handles the "show status on datetime" command.
   * Displays whether the user is busy at a specific time.
   *
   * @param model the calendar model
   * @param view  the view for rendering messages
   * @throws IOException if rendering fails
   */
  private void handleBusyStatus(IDelegator model, IView view) throws IOException {
    boolean isBusy = model.isBusyAt(rangeStart);
    view.renderMessage(isBusy ? "busy" : "available");
  }

  /**
   * Handles the "print events from start to end" command.
   * Queries and renders all events in the given datetime range.
   *
   * @param model the calendar model
   * @param view  the view for rendering messages
   * @throws IOException if rendering fails
   */
  private void handleEventsInRange(IDelegator model, IView view) throws IOException {
    List<ROIEvent> events = model.getEventsBetween(rangeStart, rangeEnd);
    if (events.isEmpty()) {
      view.renderMessage("No events found between " + rangeStart + " and " + rangeEnd);
    } else {
      StringBuilder sb = new StringBuilder("Events from " + rangeStart + " to " +
              rangeEnd + ":\n");
      for (ROIEvent event : events) {
        appendEventDetails(sb, event);
      }
      view.renderMessage(sb.toString());
    }
  }

  /**
   * Appends formatted event details to a StringBuilder.
   *
   * @param sb    the StringBuilder to append to
   * @param event the event whose details are to be formatted
   */
  private void appendEventDetails(StringBuilder sb, ROIEvent event) {
    sb.append("- ").append(event.getSubject())
            .append(" from ").append(event.getStart())
            .append(" to ").append(event.getEnd());

    if (event.getLocation() != null && !event.getLocation().isEmpty()) {
      sb.append(" at ").append(event.getLocation());
    }
    sb.append("\n");
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