// Dreshta Boghra & Aaron Zhou
// CS3500 HW4

package model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Interface for a calendar model that stores and manages events.
 * Supports adding, querying, editing, and updating both single and recurring events.
 */
public interface ICalendar {

  /**
   * Adds a new event to the calendar.
   *
   * @param event to add to the calendar
   * @throws IllegalArgumentException if an event with the same subject, start, and end time already exists
   */
  void createEvent(IEvent event) throws IllegalArgumentException;

  /**
   * Adds a new recurring event series to the calendar.
   *
   * @param series the recurring event series to add
   * @throws IllegalArgumentException if any generated event conflicts with an existing event
   */
  void createEventSeries(IEventSeries series) throws IllegalArgumentException;

  /**
   * Edits the specified property of an existing event.
   *
   * @param subject       the subject of the event to identify it
   * @param originalStart the original start time of the event
   * @param newEvent      the new value for the specified property
   * @throws IllegalArgumentException if no matching event is found with the given details
   */
  void editEvent(String subject, LocalDateTime originalStart, CalendarEvent newEvent)
          throws IllegalArgumentException;

  /**
   * Retrieves all events scheduled on a specific date.
   *
   * @param date the date to search
   * @return a list of events occurring on that date
   */
  List<IEvent> getEventsOn(LocalDate date);

  /**
   * Retrieves all events scheduled within a given time range.
   *
   * @param from the start of the time range (inclusive)
   * @param to   the end of the time range (inclusive)
   * @return a list of events within the specified interval
   */
  List<IEvent> getEventsBetween(LocalDateTime from, LocalDateTime to);

  /**
   * Checks whether the calendar is busy at the given date and time.
   *
   * @param time the date and time to check
   * @return true if there is at least one event overlapping the given time, false otherwise
   */
  boolean isBusyAt(LocalDateTime time);

  /**
   * Adds the given event to this calendar.
   *
   * @param event the given event
   */
  void addEvent(IEvent event);

  /**
   * Adds the given event series to this calendar.
   *
   * @param series the given series
   */
  void addEventSeries(IEventSeries series);

  /**
   * Checks if the event has the same subject start time and end time.
   *
   * @param event the given event
   */
  boolean isDuplicate(IEvent event);
  // we're going to check if it's a duplicate by adding this event to the set and seeing if the
  // size of the set changed
}