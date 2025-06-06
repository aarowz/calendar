package model;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

/**
 * Interface for a calendar model that stores and manages events.
 * This interface defines all allowable operations for interacting with the calendar,
 * and should be the only model interface exposed to the controller.
 * The controller may call methods on this interface but should never have direct
 * access to any model-level implementation (e.g., IEvent, CalendarEvent, etc.).
 */
public interface ICalendar {

  /**
   * Creates and adds a new one-time calendar event.
   *
   * @param subject     the subject or title of the event
   * @param start       the start time of the event
   * @param end         the end time of the event
   * @param description a description of the event
   * @param status      visibility status of the event (e.g. PUBLIC or PRIVATE)
   * @param location    the location where the event takes place
   * @throws IllegalArgumentException if an event with the same subject,
   *                                  start, and end already exists
   */
  void createEvent(String subject, LocalDateTime start, LocalDateTime end,
                   String description, EventStatus status, String location);

  /**
   * Creates and adds a new recurring event series to the calendar.*,
   *
   * @param subject     the subject/title of the event,
   * @param description a short description of the event,
   * @param location    where the event takes place,
   * @param status      visibility status (PUBLIC or PRIVATE),
   * @param startDate   the first date of the recurrence window,
   * @param endDate     the optional end date of the recurrence window (null if not used),
   * @param startTime   the time of day each event starts,
   * @param endTime     the time of day each event ends,
   * @param repeatDays  the days of the week to repeat the event on,
   * @param count       how many events to create (0 = unlimited within window),
   * @throws IllegalArgumentException if any event overlaps with an existing event
   */

  void createEventSeries(String subject, String description, String location, EventStatus status,
                         LocalDate startDate, LocalDate endDate,
                         java.time.LocalTime startTime, java.time.LocalTime endTime,
                         Set<DayOfWeek> repeatDays, int count);

  /**
   * Edits a single calendar event with a matching subject and start time.
   *
   * @param subject        the subject of the original event
   * @param originalStart  the original start time of the event
   * @param newSubject     the updated subject
   * @param newStart       the new start time
   * @param newEnd         the new end time
   * @param newDescription the new description
   * @param newStatus      the new visibility status
   * @param newLocation    the new location
   * @throws IllegalArgumentException if no matching event is found
   */
  void editEvent(String subject, LocalDateTime originalStart,
                 String newSubject, LocalDateTime newStart, LocalDateTime newEnd,
                 String newDescription, EventStatus newStatus, String newLocation);

  /**
   * Edits all events in the series with the given date and onwards.
   * Identifies the event by the given subject and start date/time
   *
   * @param subject        the subject of the events to edit
   * @param originalStart  the original start time of the event to identify the group
   * @param newSubject     the updated subject
   * @param newStart       the new start time
   * @param newEnd         the new end time
   * @param newDescription the new description
   * @param newStatus      the new visibility status
   * @param newLocation    the new location
   * @throws IllegalArgumentException if no matching events are found
   */
  void editEvents(String subject, LocalDateTime originalStart,
                  String newSubject, LocalDateTime newStart, LocalDateTime newEnd,
                  String newDescription, EventStatus newStatus, String newLocation);

  /**
   * Edits all events in a series.
   *
   * @param subject        the subject of the series
   * @param originalStart  the starting point in time to begin editing
   * @param newSubject     the updated subject
   * @param newStart       the new start time
   * @param newEnd         the new end time
   * @param newDescription the new description
   * @param newStatus      the new visibility status
   * @param newLocation    the new location
   * @throws IllegalArgumentException if no matching series or events are found
   */
  void editEventSeries(String subject, LocalDateTime originalStart,
                       String newSubject, LocalDateTime newStart, LocalDateTime newEnd,
                       String newDescription, EventStatus newStatus, String newLocation);

  /**
   * Retrieves all events occurring on a specific calendar day.
   *
   * @param date the date to query
   * @return a list of read-only representations of the events on that date
   */
  List<ROIEvent> getEventsOn(LocalDate date);

  /**
   * Retrieves all events occurring between the given start and end times.
   *
   * @param from the start of the interval (inclusive)
   * @param to   the end of the interval (inclusive)
   * @return a list of read-only representations of the events in the range
   */
  List<ROIEvent> getEventsBetween(LocalDateTime from, LocalDateTime to);

  /**
   * Returns true if at least one event is occurring at the given moment.
   *
   * @param time the moment to check
   * @return true if the calendar has any overlapping event at this time, false otherwise
   */
  boolean isBusyAt(LocalDateTime time);

  /**
   * Checks if the given event (defined by subject, start, and end) would be a duplicate.
   * A duplicate is an event that has the same subject, start, and end time as an existing one.
   *
   * @param subject the subject of the event to check
   * @param start   the start time of the event
   * @param end     the end time of the event
   * @return true if the event would be a duplicate, false otherwise
   */
  boolean isDuplicate(String subject, LocalDateTime start, LocalDateTime end);
}