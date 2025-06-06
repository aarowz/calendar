// Dreshta Boghra & Aaron Zhou
// CS3500 HW4

package controller;

import model.EventStatus;
import model.ICalendar;
import view.IView;
import exceptions.CommandExecutionException;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
  private final List<Character> repeatDays; // ['M', 'W', 'F']
  private final Integer repeatCount;
  private final LocalDate repeatUntil;

  /**
   * Builder input for the creation command.
   *
   * @param subject     the given subject
   * @param start       the given start time
   * @param end         the given end time
   * @param description the given description
   * @param location    the given location
   * @param status      the given status
   * @param repeatDays  the number of repeat days
   * @param repeatCount the repeat count
   * @param repeatUntil the time repeated until
   */
  public CreateEventCommand(String subject, LocalDateTime start, LocalDateTime end,
                            String description, String location, EventStatus status,
                            List<Character> repeatDays, Integer repeatCount,
                            LocalDate repeatUntil) {
    this.subject = subject;
    this.start = start;
    this.end = end;
    this.description = description != null ? description : "";
    this.location = location != null ? location : "";
    this.status = status != null ? status : EventStatus.PUBLIC;
    this.repeatDays = repeatDays;
    this.repeatCount = repeatCount != null ? repeatCount : 0;
    this.repeatUntil = repeatUntil;
  }

  /**
   * Executes the create event command by adding either a single event
   * or a recurring event series to the calendar model, and rendering a success message.
   *
   * @param calendar the calendar model to update
   * @param view     the view used to display feedback
   * @throws CommandExecutionException if event creation fails
   */
  @Override
  public void execute(ICalendar calendar, IView view) throws CommandExecutionException {
    try {
      // use the specified start and end time unless the end is null (all-day event)
      LocalDateTime actualStart = this.start;
      LocalDateTime actualEnd = this.end;

      // if no end time is provided, assume it's an all-day event (8am–5pm)
      if (actualEnd == null) {
        actualStart = start.with(LocalTime.of(8, 0));
        actualEnd = start.with(LocalTime.of(17, 0));
      }

      // if not a recurring event, create a single calendar event
      if (repeatDays == null || repeatDays.isEmpty()) {
        calendar.createEvent(subject, actualStart, actualEnd, description, status, location);
        view.renderMessage("Event created: " + subject + "\n");
      } else {
        // otherwise, extract recurrence info and create a series
        LocalDate startDate = actualStart.toLocalDate();
        LocalTime seriesStartTime = actualStart.toLocalTime();
        LocalTime seriesEndTime = actualEnd.toLocalTime();
        Set<DayOfWeek> days = convertToDayOfWeekSet(repeatDays);

        calendar.createEventSeries(subject, description, location, status,
                startDate, repeatUntil, seriesStartTime, seriesEndTime, days, repeatCount);

        view.renderMessage("Recurring event series created: " + subject + "\n");
      }

    } catch (Exception e) {
      // wrap and rethrow any exception that occurs during execution
      throw new CommandExecutionException("Failed to create event: " + e.getMessage(), e);
    }
  }

  /**
   * Converts a list of weekday characters (e.g., 'M', 'W') into a Set of DayOfWeek enums.
   */
  private Set<DayOfWeek> convertToDayOfWeekSet(List<Character> chars) {
    Set<DayOfWeek> result = new HashSet<>();

    // apply logic to each week character
    for (char c : chars) {
      switch (Character.toUpperCase(c)) {
        case 'M':
          result.add(DayOfWeek.MONDAY);
          break;
        case 'T':
          result.add(DayOfWeek.TUESDAY);
          break;
        case 'W':
          result.add(DayOfWeek.WEDNESDAY);
          break;
        case 'R':
          result.add(DayOfWeek.THURSDAY);
          break;
        case 'F':
          result.add(DayOfWeek.FRIDAY);
          break;
        case 'S':
          result.add(DayOfWeek.SATURDAY);
          break;
        case 'U':
          result.add(DayOfWeek.SUNDAY);
          break;
        default:
          throw new IllegalArgumentException("Invalid repeat day: " + c);
      }
    }
    return result;
  }

  /**
   * Returns a string representation of the command for logging/debugging.
   */
  @Override
  public String toString() {
    return "CreateEventCommand{" + "subject='" + subject + '\'' + ", start=" + start +
            ", end=" + end + ", repeatDays=" + repeatDays + ", repeatCount=" + repeatCount +
            ", repeatUntil=" + repeatUntil + '}';
  }
}