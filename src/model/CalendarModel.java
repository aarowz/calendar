// Dreshta Boghra & Aaron Zhou
// CS3500 HW4

package model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Implementation of the ICalendar interface.
 * Manages single events and recurring event series, providing logic for querying,
 * updating, and detecting conflicts across the calendar.
 */
public class CalendarModel implements ICalendar {
  List<IEvent> events;//events and series cannot overlap
  List<IEventSeries> series;

  @Override
  public void addEvent(IEvent event) throws IllegalArgumentException {

  }

  @Override
  public void addEventSeries(IEventSeries series) throws IllegalArgumentException {

  }

  @Override
  public void editEvent(EventProperty prop, String subject, LocalDateTime start, LocalDateTime end, String newValue) throws IllegalArgumentException {

  }

  @Override
  public List<IEvent> getEventsOn(LocalDate date) {
    return List.of();
  }

  @Override
  public List<IEvent> getEventsBetween(LocalDateTime from, LocalDateTime to) {
    return List.of();
  }

  @Override
  public boolean isBusyAt(LocalDateTime time) {
    return false;
  }
  // optional — or generate events on series creation and store all as IEvent
}
