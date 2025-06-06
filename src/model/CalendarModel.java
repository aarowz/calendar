// Dreshta Boghra & Aaron Zhou
// CS3500 HW4

package model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Implementation of the ICalendar interface.
 * Manages single events and recurring event series, providing logic for querying,
 * updating, and detecting conflicts across the calendar.
 */
public class CalendarModel implements ICalendar {
  private final List<IEvent> events; // instance-level field for calendar events
  private static List<IEvent> globalEventsReference; // static field for shared access

  /**
   * Initiate default model constructor.
   */
  public CalendarModel() {
    this.events = new ArrayList<>();
    CalendarModel.globalEventsReference = this.events;
  }

  /**
   * Returns the current global list of calendar events.
   *
   * @return the list of all IEvent instances in the calendar
   */
  public static List<IEvent> getAllEvents() {
    return globalEventsReference;
  }

  /**
   * creates and adds a one-time calendar event.
   */
  @Override
  public void createEvent(String subject, LocalDateTime start, LocalDateTime end,
                          String description, EventStatus status, String location) {
    // create a new event using the builder
    CalendarEvent event = new CalendarEvent.Builder()
            .subject(subject)
            .start(start)
            .end(end)
            .description(description)
            .status(status)
            .location(location)
            .build();

    // check for event duplication before adding
    if (isDuplicate(subject, start, end)) {
      throw new IllegalArgumentException("Duplicate event not allowed.");
    }

    // add the new event to the calendar
    events.add(event);
  }

  /**
   * creates and adds a recurring event series.
   */
  @Override
  public void createEventSeries(String subject, String description, String location,
                                EventStatus status,
                                LocalDate startDate, LocalDate endDate,
                                java.time.LocalTime startTime, java.time.LocalTime endTime,
                                Set<java.time.DayOfWeek> repeatDays, int count) {
    // build the base event (template) used to generate the series
    CalendarEvent baseEvent = new CalendarEvent.Builder()
            .subject(subject)
            .start(startDate.atTime(startTime))
            .end(startDate.atTime(endTime))
            .description(description)
            .status(status)
            .location(location)
            .build();

    // build recurrence rule
    RecurrenceRule rule = new RecurrenceRule.Builder()
            .repeatDays(repeatDays)
            .count(count)
            .start(startDate)
            .repeatUntil(endDate)
            .build();

    // generate the full series using base event and recurrence rule
    CalendarEventSeries series = new CalendarEventSeries(baseEvent, rule);

    // check for duplicates in generated series
    for (IEvent event : series.getAllOccurrences()) {
      if (isDuplicate(event.getSubject(), event.getStart(), event.getEnd())) {
        throw new IllegalArgumentException("Duplicate event in series not allowed.");
      }
    }

    // add all series events to the calendar
    events.addAll(series.getAllOccurrences());
  }

  @Override
  public void editEvent(String subject, LocalDateTime originalStart,
                        String newSubject, LocalDateTime newStart, LocalDateTime newEnd,
                        String newDescription, EventStatus newStatus, String newLocation) {
    // for each event
    for (int i = 0; i < events.size(); i++) {
      IEvent e = events.get(i);
      // if the event contains a subject and start
      if (e.getSubject().equals(subject) && e.getStart().equals(originalStart)) {
        // edit the event to the given new fields
        events.set(i, e.editEvent(newSubject, newStart, newEnd, newDescription,
                newStatus, newLocation));
        return;
      }
    }
    // otherwise throw exception
    throw new IllegalArgumentException("Event to edit not found.");
  }


  /**
   * Edits all events from a series starting from the given one onward.No new list
   *
   * @param subject        the subject to identify the event
   * @param originalStart  the start time of the event to match
   * @param newSubject     the new subject, or null if unchanged
   * @param newStart       the new start time, or null if unchanged
   * @param newEnd         the new end time, or null if unchanged
   * @param newDescription the new description, or null if unchanged
   * @param newStatus      the new status, or null if unchanged
   * @param newLocation    the new location, or null if unchanged
   */
  @Override
  public void editEvents(String subject, LocalDateTime originalStart,
                         String newSubject, LocalDateTime newStart, LocalDateTime newEnd,
                         String newDescription, EventStatus newStatus, String newLocation) {
    // set series ID to null
    UUID theSeriesID = getUuid(subject, originalStart);
    List<IEvent> seriesEvents = new ArrayList<>();

    // for each event
    for (IEvent e : events) {
      // if the series ID matches
      if (theSeriesID.equals(e.getSeriesId())) {
        // add the event
        seriesEvents.add(e);
      }
    }

    // otherwise indicate that there was no event found
    if (seriesEvents.isEmpty()) {
      throw new IllegalArgumentException("No events found for that series.");
    }

    // return the updated instance of the series
    CalendarEventSeries series = new CalendarEventSeries(seriesEvents);
    List<IEvent> updated = series.editEvents(originalStart, newSubject, newStart, newEnd,
            newDescription, newStatus, newLocation);

    // sanity check lol
    events.removeAll(updated);
    events.addAll(updated);
  }

  /**
   * Extracts the UUID given a subject and a start time. This is an abstracted method to keep our
   * methods within readable length.
   *
   * @param subject       the given subject
   * @param originalStart the given original start time
   * @return the extracted UUID
   */
  private UUID getUuid(String subject, LocalDateTime originalStart) {
    // set initial null ID
    UUID theSeriesID = null;

    // for each event within the events
    for (IEvent e : events) {
      // if the subject and start time exist given the nanosecond time adjustment
      if (e.getSubject().equals(subject) && e.getStart().withNano(0)
              .equals(originalStart.withNano(0))) {
        // and the series ID is null
        if (e.getSeriesId() == null) {
          // throw an exception to indicate that the event is an outcast >:(
          throw new IllegalArgumentException("This event is not part of a series.");
        }
        // otherwise update the ID
        theSeriesID = e.getSeriesId();
        break;
      }
    }
    // if the ID is nonexistent
    if (theSeriesID == null) {
      // throw an exception
      throw new IllegalArgumentException("No matching series found.");
    }
    return theSeriesID;
  }


  /**
   * Edits all events in the series that this event belongs to.
   */
  @Override
  public void editEventSeries(String subject, LocalDateTime originalStart,
                              String newSubject, LocalDateTime newStart, LocalDateTime newEnd,
                              String newDescription, EventStatus newStatus, String newLocation) {
    // extract the series ID
    UUID theSeriesId = getUuid(subject, originalStart);
    List<IEvent> seriesEvents = new ArrayList<>();

    // for each event
    for (IEvent e : events) {
      // if the series ID matches
      if (theSeriesId.equals(e.getSeriesId())) {
        // add the series to the events
        seriesEvents.add(e);
      }
    }

    // throw an exception if the series does not exist
    if (seriesEvents.isEmpty()) {
      throw new IllegalArgumentException("No events found for that series.");
    }

    // create temporary CalendarEventSeries (auto-sorts and sets baseEvent)
    CalendarEventSeries series = new CalendarEventSeries(seriesEvents);
    LocalDateTime earliestStart = series.getBaseEvent().getStart();

    // get all updated events (i.e., full series)
    List<IEvent> updated = series.editEvents(
            earliestStart, newSubject, newStart, newEnd, newDescription, newStatus, newLocation);

    // replace entire original series
    events.removeAll(seriesEvents);
    events.addAll(updated);
  }

  /**
   * Gets all events that happen on a specific date.
   */
  @Override
  public List<ROIEvent> getEventsOn(LocalDate date) {
    List<ROIEvent> result = new ArrayList<>();
    // check each event's date
    for (IEvent e : events) {
      if (e.getStart().toLocalDate().equals(date)) {
        result.add(e);  // add to results
      }
    }
    return result;
  }

  /**
   * Gets all events between two date times.
   */
  @Override
  public List<ROIEvent> getEventsBetween(LocalDateTime from, LocalDateTime to) {
    List<ROIEvent> result = new ArrayList<>();
    // add events that overlap with the specified interval
    for (IEvent e : events) {
      if (!e.getEnd().isBefore(from) && !e.getStart().isAfter(to)) {
        result.add(e);
      }
    }
    return result;
  }

  /**
   * returns true if any event is ongoing at the given time.
   */
  @Override
  public boolean isBusyAt(LocalDateTime time) {
    for (IEvent e : events) {
      if (!time.isBefore(e.getStart()) && !time.isAfter(e.getEnd())) {
        return true;  // found a match; calendar is busy
      }
    }
    return false;  // no matches; user is free
  }

  /**
   * returns true if an event with the same subject and time already exists.
   */
  @Override
  public boolean isDuplicate(String subject, LocalDateTime start, LocalDateTime end) {
    for (IEvent e : events) {
      if (e.getSubject().equals(subject) &&
              e.getStart().equals(start) &&
              e.getEnd().equals(end)) {
        return true;  // duplicate found
      }
    }
    return false;  // no duplicates
  }
}