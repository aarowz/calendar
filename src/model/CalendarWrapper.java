// Dreshta Boghra & Aaron Zhou
// CS3500 HW5

package model;

import java.time.ZoneId;
import java.util.Objects;

/**
 * Represents metadata and logic for a single calendar instance.
 * Wraps an ICalendar with its name and timezone.
 */
public class CalendarWrapper {
  private final String name;
  private final ZoneId timeZone;
  private final ICalendar calendar;

  /**
   * Constructor that wraps the calendar name and timezone along wth all the other fields we
   * previously defined in assignment four.
   */
  public CalendarWrapper(String name, ZoneId timeZone, ICalendar calendar) {
    this.name = Objects.requireNonNull(name, "calendar name cannot be null");
    this.timeZone = Objects.requireNonNull(timeZone, "time zone cannot be null");
    this.calendar = Objects.requireNonNull(calendar, "calendar cannot be null");
  }

  /**
   * Get the name of the calendar.
   *
   * @return the name of the calendar
   */
  public String getName() {
    return name;
  }

  /**
   * Get the time zone of the calendar.
   *
   * @return the time zone of the calendar.
   */
  public ZoneId getTimeZone() {
    return timeZone;
  }

  /**
   * Get the calendar.
   *
   * @return the calendar
   */
  public ICalendar getCalendar() {
    return calendar;
  }

  @Override
  public String toString() {
    return String.format("Calendar[name=%s, zone=%s]", name, timeZone);
  }
}
