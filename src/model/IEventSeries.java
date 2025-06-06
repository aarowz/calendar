// Dreshta Boghra & Aaron Zhou
// CS3500 HW4

package model;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Interface representing a recurring series of calendar events.
 * Provides a method to generate individual event instances from recurrence rules.
 */
public interface IEventSeries {

  /**
   * Identifies the event that has the given subject and starts at the given date and time and
   * edit its property. If this event is part of a series then the properties of all events in
   * that series that start at or after the given date and time should be changed.
   *
   * @param fromDate       the start date to apply edits from
   * @param newSubject     updated subject or null
   * @param newStart       updated start or null
   * @param newEnd         updated end or null
   * @param newDescription updated description or null
   * @param newStatus      updated status or null
   * @param newLocation    updated location or null
   * @return list of edited events
   */
  List<IEvent> editEvents(LocalDateTime fromDate,
                          String newSubject,
                          LocalDateTime newStart,
                          LocalDateTime newEnd,
                          String newDescription,
                          EventStatus newStatus,
                          String newLocation);

  /**
   * Returns a list of individual calendar events that make up this recurring series.
   * These events are typically generated from a base event and a recurrence rule.
   *
   * @return a list of IEvent instances representing each occurrence in the series
   */
  List<IEvent> getAllOccurrences();


  /**
   * Returns the base event of the object.
   *
   * @return an IEvent
   */
  IEvent getBaseEvent();
}