// Dreshta Boghra & Aaron Zhou
// CS3500 HW4

package controller;

import model.ICalendar;
import model.IEvent;
import model.IEventSeries;
import model.EventStatus;
import view.IView;
import exceptions.CommandExecutionException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Represents a command to create a calendar event or recurring event series.
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
   * @param start       the start date/time
   * @param end         the end date/time (nullable for all-day)
   * @param description optional description
   * @param location    optional location
   * @param status      public or private status
   * @param repeatDays  list of weekday chars (M, T, W...), null or empty if non-recurring
   * @param repeatCount how many times to repeat (nullable)
   * @param repeatUntil until what date to repeat (nullable)
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
   * @param view     the view used to output success or error messages
   * @throws CommandExecutionException if execution fails (e.g., due to duplicate event)
   */
  @Override
  public void execute(ICalendar calendar, IView view) throws CommandExecutionException {
    // TODO: if repeatDays is empty/null, create single event and add to model
    // TODO: otherwise, create series and add to model
    // TODO: report success/failure to the view
  }

  /**
   * Optional: returns a string representation of the command.
   */
  @Override
  public String toString() {
    // TODO: return a short description of what this command will do
    return "CreateEventCommand{" + subject + "}";
  }
}