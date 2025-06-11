// Dreshta Boghra & Aaron Zhou
// CS3500 HW5

package model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import util.TimeZoneUtil;

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
  public CalendarMulti() {
    this.calendars = new HashMap<>();
    this.activeCalendarName = null;
  }

  // =======================
  // Create calendar command
  // =======================

  @Override
  public void createCalendar(String name, ZoneId timeZone) {
    // if duplicate name
    if (calendars.containsKey(name)) {
      // calendar cannot be created
      throw new IllegalArgumentException("Calendar with name '" + name + "' already exists.");
    }

    // add the calendar
    ICalendar calendar = new CalendarModel();
    CalendarWrapper wrapper = new CalendarWrapper(name, timeZone, calendar);
    calendars.put(name, wrapper);
  }

  // ==================================
  // Edit calendar command with helpers
  // ==================================

  @Override
  public void editCalendar(String name, String property, String newValue) {
    // look up the calendar to be edited
    CalendarWrapper oldWrapper = calendars.get(name);
    if (oldWrapper == null) {
      throw new IllegalArgumentException("No calendar found with name '" + name + "'.");
    }

    CalendarWrapper updated;

    // determine which property to edit and delegate to a helper
    switch (property.toLowerCase()) {
      case "name":
        // create a new CalendarWrapper with the updated name and remove the old one
        updated = updateCalendarName(name, oldWrapper, newValue);
        break;

      case "timezone":
        // create a new CalendarWrapper with the updated timezone
        updated = updateCalendarTimeZone(oldWrapper, newValue);
        break;

      default:
        // reject unsupported property keys
        throw new IllegalArgumentException("Unknown calendar property: '" + property + "'");
    }

    // put the updated calendar in the map (under new or same key depending on edit)
    calendars.put(updated.getName(), updated);
  }

  /**
   * Rename an existing calendar by creating a new CalendarWrapper with the new name.
   * This method also updates the active calendar name if it was the one renamed.
   *
   * @param oldName    The current name of the calendar to rename.
   * @param oldWrapper The existing CalendarWrapper instance.
   * @param newName    The new name to assign to the calendar.
   * @return A new CalendarWrapper instance with the updated name.
   * @throws IllegalArgumentException If a calendar with the new name already exists.
   */
  private CalendarWrapper updateCalendarName(String oldName, CalendarWrapper oldWrapper,
                                             String newName) {
    // check if the new name is already taken
    if (calendars.containsKey(newName)) {
      throw new IllegalArgumentException("Calendar name '" + newName + "' already exists.");
    }

    // remove the calendar with the old name
    calendars.remove(oldName);

    // create a new wrapper with the new name but same timezone and model
    CalendarWrapper renamed = new CalendarWrapper(
            newName,
            oldWrapper.getTimeZone(),
            oldWrapper.getCalendar()
    );

    // if the renamed calendar was the active one, update the active name
    if (oldName.equals(activeCalendarName)) {
      activeCalendarName = newName;
    }

    // return the new wrapper to be reinserted in the map
    return renamed;
  }

  /**
   * Update the timezone of an existing calendar by creating a new CalendarWrapper
   * with the same name and model but a new ZoneId.
   *
   * @param oldWrapper The existing CalendarWrapper whose timezone should be updated.
   * @param newZoneStr The new timezone string in IANA format (e.g. "America/New_York").
   * @return A new CalendarWrapper instance with the updated timezone.
   * @throws IllegalArgumentException If the provided timezone string is invalid.
   */
  private CalendarWrapper updateCalendarTimeZone(CalendarWrapper oldWrapper, String newZoneStr) {
    // try parsing the timezone string
    ZoneId newZone;
    try {
      newZone = ZoneId.of(newZoneStr);
    } catch (Exception e) {
      throw new IllegalArgumentException("Invalid timezone: '" + newZoneStr + "'");
    }

    // create a new wrapper with the same name and calendar but new timezone
    return new CalendarWrapper(
            oldWrapper.getName(),
            newZone,
            oldWrapper.getCalendar()
    );
  }

  // ====================
  // Use calendar command
  // ====================

  @Override
  public void useCalendar(String name) {
    // check if the requested calendar exists
    if (!calendars.containsKey(name)) {
      throw new IllegalArgumentException("No calendar found with name '" + name + "'.");
    }

    // set the active calendar name
    activeCalendarName = name;
  }

  // ============================
  // Get current calendar command
  // ============================

  @Override
  public ICalendar getCurrentCalendar() {
    // check if a calendar is currently in use
    if (activeCalendarName == null) {
      throw new IllegalStateException("No calendar is currently selected.");
    }

    // retrieve the calendar wrapper and return its model
    CalendarWrapper wrapper = calendars.get(activeCalendarName);
    if (wrapper == null) {
      throw new IllegalStateException("Active calendar reference is invalid.");
    }

    return wrapper.getCalendar();
  }

  // ===============================
  // Copy event command with helpers
  // ===============================

  @Override
  public void copyEvent(String eventName, LocalDateTime sourceStart, String targetCalendar,
                        LocalDateTime targetStart) {
    // check if a calendar is currently selected
    if (activeCalendarName == null) {
      throw new IllegalStateException("No calendar is currently selected.");
    }

    // get source and target calendar wrappers
    CalendarWrapper source = getRequiredCalendar(activeCalendarName);
    CalendarWrapper target = getRequiredCalendar(targetCalendar);

    // create a timezone-adjusted copy of the event
    IEvent copy = copyEventBetweenCalendars(eventName, sourceStart, source, target);

    // add the copied event to the target calendar
    target.getCalendar().addEvent(copy);
  }

  /**
   * Creates a deep copy of an event from the source calendar, adjusting its start time
   * to match the equivalent instant in the target calendar's timezone.
   *
   * @param eventName   the subject of the event
   * @param sourceStart the local start time of the event in the source calendar
   * @param source      the source calendar wrapper
   * @param target      the target calendar wrapper
   * @return a copied IEvent with adjusted start and end times
   * @throws IllegalArgumentException if the event is not found
   */
  private IEvent copyEventBetweenCalendars(String eventName, LocalDateTime sourceStart,
                                           CalendarWrapper source, CalendarWrapper target) {
    // retrieve the original event from the source calendar
    IEvent original = source.getCalendar().getSpecificEvent(eventName, sourceStart);
    if (original == null) {
      throw new IllegalArgumentException("No event named '" + eventName + "' starting at "
              + sourceStart);
    }

    // get the source and target timezones
    ZoneId sourceZone = source.getTimeZone();
    ZoneId targetZone = target.getTimeZone();

    // convert the source start time to the target calendar's timezone
    LocalDateTime adjustedStart = TimeZoneUtil
            .toZonedDateTime(sourceStart, sourceZone)
            .withZoneSameInstant(targetZone)
            .toLocalDateTime();

    // return a copied event with the adjusted start time
    return prepareCopiedEvent(original, adjustedStart);
  }

  /**
   * Creates a deep copy of an event with a new start time, preserving all other fields.
   * End time is automatically adjusted to preserve original duration.
   *
   * @param original the original event to copy
   * @param newStart the new start time for the copied event
   * @return a new CalendarEvent with updated times
   */
  private CalendarEvent prepareCopiedEvent(IEvent original, LocalDateTime newStart) {
    long minutes = java.time.Duration.between(original.getStart(), original.getEnd()).toMinutes();

    return new CalendarEvent.Builder()
            .subject(original.getSubject())
            .start(newStart)
            .end(newStart.plusMinutes(minutes))
            .description(original.getDescription())
            .status(original.getStatus())
            .location(original.getLocation())
            .seriesId(original.getSeriesId()) // if applicable
            .build();
  }

  /**
   * Look up a calendar by name and return its wrapper.
   * Throws an exception if the calendar does not exist.
   *
   * @param name the name of the calendar to retrieve
   * @return the CalendarWrapper associated with the given name
   * @throws IllegalArgumentException if no calendar with the given name exists
   */
  private CalendarWrapper getRequiredCalendar(String name) {
    // retrieve the calendar wrapper from the map
    CalendarWrapper wrapper = calendars.get(name);

    // throw if not found
    if (wrapper == null) {
      throw new IllegalArgumentException("Calendar '" + name + "' does not exist.");
    }

    // return the valid wrapper
    return wrapper;
  }

  // ===================================
  // Copy events on command with helpers
  // ===================================

  @Override
  public void copyEventsOn(LocalDate date, String targetCalendar, LocalDate targetDate) {
    if (activeCalendarName == null) {
      throw new IllegalStateException("No calendar is currently selected.");
    }

    // store source and target calendars
    CalendarWrapper source = getRequiredCalendar(activeCalendarName);
    CalendarWrapper target = getRequiredCalendar(targetCalendar);

    // format and copy the events to the target
    List<IEvent> events = resolveEventsOnDate(source, date);
    copyEventsToTarget(events, target, targetDate);
  }

  /**
   * Resolves a list of full IEvent objects occurring on the given date from the source calendar.
   *
   * @param source the source calendar wrapper
   * @param date   the date to retrieve events from
   * @return a list of full IEvent objects
   */
  private List<IEvent> resolveEventsOnDate(CalendarWrapper source, LocalDate date) {
    List<ROIEvent> readOnlyEvents = source.getCalendar().getEventsOn(date);
    List<IEvent> fullEvents = new ArrayList<>();

    // for each event
    for (ROIEvent e : readOnlyEvents) {
      // get the specific event
      IEvent full = source.getCalendar().getSpecificEvent(e.getSubject(), e.getStart());
      if (full == null) {
        // throw an exception if there are no events
        throw new IllegalStateException("Underlying event not found: " + e.getSubject());
      }
      fullEvents.add(full);
    }

    return fullEvents;
  }

  /**
   * Copies a list of events to the target calendar, offsetting them to the given target date.
   *
   * @param events     the list of source events
   * @param target     the destination calendar
   * @param targetDate the new calendar date to assign the events to
   */
  private void copyEventsToTarget(List<IEvent> events, CalendarWrapper target,
                                  LocalDate targetDate) {
    // get the timezones for the source (active) and target calendars
    ZoneId sourceZone = getRequiredCalendar(activeCalendarName).getTimeZone();
    ZoneId targetZone = target.getTimeZone();

    // for each event to be copied
    for (IEvent event : events) {
      // convert the event's time to match the target calendar's timezone and date
      LocalDateTime newStart = convertToTargetZoneDate(event.getStart(), targetDate, sourceZone,
              targetZone);

      // create a deep copy of the event with the adjusted start time
      IEvent copy = prepareCopiedEvent(event, newStart);

      // check if the event already exists in the target calendar
      if (target.getCalendar().isDuplicate(copy.getSubject(), copy.getStart(), copy.getEnd())) {
        throw new IllegalArgumentException("Duplicate event detected: " + copy.getSubject());
      }

      // add the event to the target calendar
      target.getCalendar().addEvent(copy);
    }
  }

  /**
   * Converts a local datetime from the source calendar's timezone to a matching datetime
   * on the target calendar's timezone using the same time-of-day from the target date.
   *
   * @param sourceTime the original event start time (from the source calendar)
   * @param targetDate the target calendar's intended date
   * @param sourceZone the timezone of the source calendar
   * @param targetZone the timezone of the target calendar
   * @return the adjusted local datetime in the target calendar's timezone
   */
  private LocalDateTime convertToTargetZoneDate(LocalDateTime sourceTime, LocalDate targetDate,
                                                ZoneId sourceZone, ZoneId targetZone) {
    // convert source time to the target zone, preserving the same instant
    ZonedDateTime converted = TimeZoneUtil
            .toZonedDateTime(sourceTime, sourceZone)
            .withZoneSameInstant(targetZone);

    // replace the date with the targetDate while keeping the same time-of-day
    return targetDate.atTime(converted.toLocalTime());
  }

  // ========================================
  // Copy events between command with helpers
  // ========================================

  @Override
  public void copyEventsBetween(LocalDate start, LocalDate end, String targetCalendar,
                                LocalDate targetStartDate) {
    // check if a calendar is currently selected
    if (activeCalendarName == null) {
      throw new IllegalStateException("No calendar is currently selected.");
    }

    // get the source (active) and target calendar wrappers
    CalendarWrapper source = getRequiredCalendar(activeCalendarName);
    CalendarWrapper target = getRequiredCalendar(targetCalendar);

    // get all full events from the source calendar between the given dates
    List<IEvent> events = resolveEventsBetween(source, start, end);

    // calculate how many days to shift the copied events by
    long dayOffset = java.time.temporal.ChronoUnit.DAYS.between(start, targetStartDate);

    // copy each event to the target calendar with the adjusted date offset
    copyShiftedEventsToTarget(events, target, dayOffset);
  }

  /**
   * Resolves all full IEvent objects occurring between two dates from the source calendar.
   *
   * @param source the source calendar wrapper
   * @param start  the start date (inclusive)
   * @param end    the end date (inclusive)
   * @return a list of full IEvent objects
   */
  private List<IEvent> resolveEventsBetween(CalendarWrapper source, LocalDate start,
                                            LocalDate end) {
    // define the inclusive datetime range
    LocalDateTime from = start.atStartOfDay();
    LocalDateTime to = end.atTime(23, 59, 59);

    // retrieve read-only events in the given datetime range
    List<ROIEvent> readOnly = source.getCalendar().getEventsBetween(from, to);
    List<IEvent> full = new ArrayList<>();

    // convert each read-only event to its full editable form
    for (ROIEvent e : readOnly) {
      IEvent event = source.getCalendar().getSpecificEvent(e.getSubject(), e.getStart());
      if (event == null) {
        throw new IllegalStateException("Underlying event not found: " + e.getSubject());
      }
      full.add(event);
    }

    return full;
  }

  /**
   * Copies a list of events to the target calendar, shifting each event's time by the given
   * number of days.
   *
   * @param events    the list of source events to copy
   * @param target    the target calendar wrapper
   * @param dayOffset the number of days to shift each event's time by
   */
  private void copyShiftedEventsToTarget(List<IEvent> events, CalendarWrapper target,
                                         long dayOffset) {
    ZoneId sourceZone = getRequiredCalendar(activeCalendarName).getTimeZone();
    ZoneId targetZone = target.getTimeZone();

    for (IEvent event : events) {
      // convert original start time to equivalent instant in target zone
      ZonedDateTime converted = TimeZoneUtil
              .toZonedDateTime(event.getStart(), sourceZone)
              .withZoneSameInstant(targetZone)
              .plusDays(dayOffset); // shift the real-world moment

      LocalDateTime newStart = converted.toLocalDateTime();

      IEvent copy = prepareCopiedEvent(event, newStart);

      // detect duplicate event
      if (target.getCalendar().isDuplicate(copy.getSubject(), copy.getStart(), copy.getEnd())) {
        throw new IllegalArgumentException("Duplicate event detected: " + copy.getSubject());
      }

      target.getCalendar().addEvent(copy);
    }
  }
}