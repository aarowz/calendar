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
      // if the event isn't a series
      if (repeatDays == null || repeatDays.isEmpty()) {
        // add the six input default event
        IEvent event = new CalendarEvent.Builder()
                .subject(subject)
                .start(start)
                .end(end)
                .description(description)
                .location(location)
                .status(status)
                .build();
        calendar.addEvent(event);
        view.renderMessage("Event created: " + subject);
      } else {
        // otherwise add the series implementation with additional fields
        IEventSeries series = new CalendarEventSeries().Builder()
                .subject(subject)
                .start(start)
                .end(end)
                .description(description)
                .location(location)
                .status(status)
                .setRepeatDays(repeatDays) // fix the next three to match the recurrence rule
                .setRepeatCount(repeatCount)
                .setRepeatUntil(repeatUntil)
                .build();
        calendar.addEventSeries(series);
        view.renderMessage("Recurring event series created: " + subject);
      }
    } catch (Exception e) {
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
    return "CreateEventCommand{" + subject + "}";
  }
}