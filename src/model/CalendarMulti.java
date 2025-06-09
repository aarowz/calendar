// Dreshta Boghra & Aaron Zhou
// CS3500 HW5

package model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;

/**
 * Implementation of ICalendarManager.
 * Maintains a map of named calendars, tracks the active calendar,
 * and delegates to them for per-calendar event operations.
 */
public class CalendarMulti implements ICalendarMulti {
  private final Map<String, CalendarWrapper> calendars;
  private String activeCalendarName;

  /**
   * Constructor for all model methods that handles logic for multiple calendars.
   */
  public CalendarMulti(Map<String, CalendarWrapper> calendars, String activeCalendarName) {
    this.calendars = new HashMap<>();
    this.activeCalendarName = null;
  }

  @Override
  public void createCalendar(String name, ZoneId timeZone) {
    if (calendars.containsKey(name)) {
      throw new IllegalArgumentException("Calendar with name '" + name + "' already exists.");
    }
  }

  @Override
  public void editCalendar(String name, String property, String newValue) {

  }

  @Override
  public void useCalendar(String name) {

  }

  @Override
  public ICalendar getCurrentCalendar() {
    return null;
  }

  @Override
  public void copyEvent(String eventName, LocalDateTime sourceStart, String targetCalendar,
                        LocalDateTime targetStart) {

  }

  @Override
  public void copyEventsOn(LocalDate date, String targetCalendar, LocalDate targetDate) {

  }

  @Override
  public void copyEventsBetween(LocalDate start, LocalDate end, String targetCalendar,
                                LocalDate targetStartDate) {

  }
}
