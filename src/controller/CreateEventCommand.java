// Dreshta Boghra & Aaron Zhou
// CS3500 HW4

package controller;

import model.*;
import view.IView;
import exceptions.CommandExecutionException;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Represents a command to create a calendar event or recurring event series.
 * Uses builder classes to construct events and event series.
 */
public class CreateEventCommand implements ICommand {

  // fields for all event details
  private final String subject;
  private final LocalDateTime start;
  private final LocalDateTime end;
  private final String description;
  private final String location;
  private final EventStatus status;
  private final List<Character> repeatDays;
  private final Integer repeatCount;
  private final LocalDate repeatUntil;

  /**
   * Constructs a command to create either a single event or a recurring series.
   *
   * @param subject     the event subject
   * @param start       the start time
   * @param end         the end time
   * @param description optional description
   * @param location    optional location
   * @param status      public or private status
   * @param repeatDays  days of week to repeat on (null/empty if not recurring)
   * @param repeatCount number of times to repeat (nullable)
   * @param repeatUntil date to stop repeating (nullable)
   */
  public CreateEventCommand(String subject,
                            LocalDateTime start,
                            LocalDateTime end,
                            String description,
                            String location,
                            EventStatus status,
                            List<Character> repeatDays,
                            Integer repeatCount,
                            LocalDate repeatUntil) {
    // initialize all fields
    this.subject = subject;
    this.start = start;
    this.end = end;
    this.description = description;
    this.location = location;
    this.status = status;
    this.repeatDays = repeatDays;
    this.repeatCount = repeatCount;
    this.repeatUntil = repeatUntil;
  }

  /**
   * Executes the creation command on the given calendar model.
   *
   * @param calendar the calendar model
   * @param view     the view to display messages
   * @throws CommandExecutionException if the event cannot be created
   */
  @Override
  public void execute(ICalendar calendar, IView view) throws CommandExecutionException {
    try {
      // if repeatDays is empty, this is just a one-off event
      if (repeatDays == null || repeatDays.isEmpty()) {
        // build the calendar event using the builder pattern
        CalendarEvent.Builder builder = new CalendarEvent.Builder();
        builder.subject(subject); // set subject
        builder.start(start); // set start time
        builder.end(end); // set end time
        builder.description(description); // set optional description
        builder.location(location); // set optional location
        builder.status(status); // set public/private

        // add event to calendar
        calendar.addEvent(builder.build());

        // notify user
        view.renderMessage("Event created: " + subject);
      } else {
        // otherwise we're dealing with a recurring series
        CalendarEventSeries.Builder seriesBuilder = new CalendarEventSeries.Builder();

        // set the recurring event series fields
        seriesBuilder.subject(subject); // set subject
        seriesBuilder.start(start); // set start time
        seriesBuilder.end(end); // set end time
        seriesBuilder.description(description); // optional description
        seriesBuilder.location(location); // optional location
        seriesBuilder.status(status); // status setting
        seriesBuilder.setRepeatDays(repeatDays); // set which days it repeats
        seriesBuilder.setRepeatCount(repeatCount); // set how many times
        seriesBuilder.setRepeatUntil(repeatUntil); // set last possible repeat date

        // add series to calendar
        calendar.addEventSeries(seriesBuilder.build());

        // notify user
        view.renderMessage("Recurring event series created: " + subject);
      }
    } catch (Exception e) {
      // if something goes wrong, wrap and rethrow with message
      throw new CommandExecutionException("Failed to create event: " + e.getMessage(), e);
    }
  }

  /**
   * Returns a short description of what this command will do.
   *
   * @return string representation
   */
  @Override
  public String toString() {
    // just print the command name and subject
    return "CreateEventCommand{" + subject + "}";
  }
}