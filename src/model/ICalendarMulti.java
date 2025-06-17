// Dreshta Boghra & Aaron Zhou
// CS3500 HW5

package model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

/**
 * Interface representing the calendar system capable of managing multiple calendars.
 * Handles calendar creation, editing, switching, and event copying across calendars.
 */
public interface ICalendarMulti {
  /**
   * Creates a new calendar with the given name and timezone.
   * Calendar names must be unique.
   *
   * @param name     the name of the new calendar
   * @param timezone the timezone in IANA format (e.g., "America/New_York")
   * @throws IllegalArgumentException if the calendar name is not unique or the timezone is invalid
   */
  void createCalendar(String name, ZoneId timezone);

  /**
   * Edits the property of a calendar with the given name.
   * Supported properties include "name" and "timezone".
   *
   * @param name     the name of the calendar to edit
   * @param property the property to modify ("name" or "timezone")
   * @param newValue the new value for the specified property
   * @throws IllegalArgumentException if the calendar does not exist, the property is invalid,
   *                                  or the new value is invalid in the context of the property
   */
  void editCalendar(String name, String property, String newValue);

  /**
   * Sets the currently active calendar context by name.
   * All event-level commands operate on this calendar once set.
   *
   * @param name the name of the calendar to activate
   * @throws IllegalArgumentException if the calendar does not exist
   */
  void useCalendar(String name);

  /**
   * Returns the calendar currently in use.
   *
   * @return the active calendar
   * @throws IllegalStateException if no calendar is currently active
   */
  ICalendar getCurrentCalendar();

  /**
   * Copies a single event from the active calendar to the target calendar.
   * The copied event will start at the given target date/time.
   * Timezones will be converted appropriately.
   *
   * @param eventName      the name of the event to copy
   * @param sourceStart    the start time of the event in the source calendar
   * @param targetCalendar the name of the destination calendar
   * @param targetStart    the desired start time in the target calendar’s timezone
   * @throws IllegalArgumentException if the event, source calendar, or target calendar is invalid
   */
  void copyEvent(String eventName, LocalDateTime sourceStart,
                 String targetCalendar, LocalDateTime targetStart);

  /**
   * Copies all events scheduled on the given date from the active calendar
   * to the target calendar. Times will be converted across timezones.
   *
   * @param date           the date in the active calendar to copy from
   * @param targetCalendar the name of the destination calendar
   * @param targetDate     the date in the target calendar to copy to
   * @throws IllegalArgumentException if the active or target calendar is invalid
   */
  void copyEventsOn(LocalDate date, String targetCalendar, LocalDate targetDate);

  /**
   * Copies all events in the specified date range (inclusive) from the active calendar
   * to the target calendar. Events will be shifted to begin at the target date.
   * Timezones will be converted, and recurring series will be preserved where applicable.
   *
   * @param start           the start date of the source interval (inclusive)
   * @param end             the end date of the source interval (inclusive)
   * @param targetCalendar  the name of the destination calendar
   * @param targetStartDate the start date in the target calendar’s timezone
   * @throws IllegalArgumentException if the calendars are invalid or range is malformed
   */
  void copyEventsBetween(LocalDate start, LocalDate end,
                         String targetCalendar, LocalDate targetStartDate);

  /**
   * Returns a list of the calendar names.
   */
  List<String> getCalendarNames();
}