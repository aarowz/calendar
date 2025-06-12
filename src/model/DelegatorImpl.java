// Dreshta Boghra & Aaron Zhou
// CS3500 HW5

package model;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Set;

/**
 * Implementation of IDelegator.
 * The DelegatorImpl class functions as a calendar operations manager. It maintains access
 * to all available calendars, tracks the active one in use, and handles user-facing functionality
 * such as creating, editing, querying, and copying events. It encapsulates the complexity of
 * operating on both single and multiple calendar systems.
 */
public class DelegatorImpl implements IDelegator {
  private final ICalendarMulti calendarSystem;

  /**
   * Constructor for the delegator.
   *
   * @param calendarSystem the given calendar system
   */
  public DelegatorImpl(ICalendarMulti calendarSystem) {
    this.calendarSystem = calendarSystem;
  }

  // ==========================================================
  // Methods dealing with a single calendar that existed in HW4
  // ==========================================================

  @Override
  public void createEvent(String subject, LocalDateTime start, LocalDateTime end,
                          String description, EventStatus status, String location) {
    calendarSystem.getCurrentCalendar().createEvent(subject, start, end, description, status,
            location);
  }

  @Override
  public void createEventSeries(String subject, String description, String location,
                                EventStatus status, LocalDate startDate, LocalDate endDate,
                                LocalTime startTime, LocalTime endTime, Set<DayOfWeek> repeatDays,
                                int count) {
    calendarSystem.getCurrentCalendar().createEventSeries(subject, description, location, status,
            startDate, endDate, startTime, endTime, repeatDays, count);
  }

  @Override
  public void editEvent(String subject, LocalDateTime originalStart, String newSubject,
                        LocalDateTime newStart, LocalDateTime newEnd, String newDescription,
                        EventStatus newStatus, String newLocation) {
    calendarSystem.getCurrentCalendar().editEvent(subject, originalStart, newSubject, newStart,
            newEnd, newDescription, newStatus, newLocation);
  }

  @Override
  public void editEvents(String subject, LocalDateTime originalStart, String newSubject,
                         LocalDateTime newStart, LocalDateTime newEnd, String newDescription,
                         EventStatus newStatus, String newLocation) {
    calendarSystem.getCurrentCalendar().editEvents(subject, originalStart, newSubject, newStart,
            newEnd, newDescription, newStatus, newLocation);
  }

  @Override
  public void editEventSeries(String subject, LocalDateTime originalStart, String newSubject,
                              LocalDateTime newStart, LocalDateTime newEnd, String newDescription,
                              EventStatus newStatus, String newLocation) {
    calendarSystem.getCurrentCalendar().editEventSeries(subject, originalStart, newSubject,
            newStart, newEnd, newDescription, newStatus, newLocation);
  }

  @Override
  public List<ROIEvent> getEventsOn(LocalDate date) {
    return calendarSystem.getCurrentCalendar().getEventsOn(date);
  }

  @Override
  public List<ROIEvent> getEventsBetween(LocalDateTime from, LocalDateTime to) {
    return calendarSystem.getCurrentCalendar().getEventsBetween(from, to);
  }

  @Override
  public boolean isBusyAt(LocalDateTime time) {
    return calendarSystem.getCurrentCalendar().isBusyAt(time);
  }

  @Override
  public boolean isDuplicate(String subject, LocalDateTime start, LocalDateTime end) {
    return calendarSystem.getCurrentCalendar().isDuplicate(subject, start, end);
  }

  // ========================================================
  // Methods dealing with a single calendar were added in HW5
  // ========================================================

  @Override
  public IEvent getSpecificEvent(String subject, LocalDateTime start) {
    return calendarSystem.getCurrentCalendar().getSpecificEvent(subject, start);
  }

  @Override
  public void addEvent(IEvent event) {
    calendarSystem.getCurrentCalendar().addEvent(event);
  }

  // =======================================
  // Methods dealing with multiple calendars
  // =======================================

  @Override
  public void createCalendar(String name, ZoneId timezone) {
    calendarSystem.createCalendar(name, timezone);
  }

  @Override
  public void editCalendar(String name, String property, String newValue) {
    calendarSystem.editCalendar(name, property, newValue);
  }

  @Override
  public void useCalendar(String name) {
    calendarSystem.useCalendar(name);
  }

  @Override
  public ICalendar getCurrentCalendar() {
    return calendarSystem.getCurrentCalendar();
  }

  @Override
  public void copyEvent(String eventName, LocalDateTime sourceStart, String targetCalendar,
                        LocalDateTime targetStart) {
    calendarSystem.copyEvent(eventName, sourceStart, targetCalendar, targetStart);
  }

  @Override
  public void copyEventsOn(LocalDate date, String targetCalendar, LocalDate targetDate) {
    calendarSystem.copyEventsOn(date, targetCalendar, targetDate);
  }

  @Override
  public void copyEventsBetween(LocalDate start, LocalDate end, String targetCalendar,
                                LocalDate targetStartDate) {
    calendarSystem.copyEventsBetween(start, end, targetCalendar, targetStartDate);
  }
}