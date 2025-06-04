// Dreshta Boghra & Aaron Zhou
// CS3500 HW4

package model;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Interface representing a single calendar event.
 * Provides methods to access core event properties and determine temporal conflicts.
 */
public interface IEvent {
  /**
   * Determines if this event overlaps in time with another event.
   *
   * @param other the other event to compare with
   * @return true if the two events overlap in time, false otherwise
   */
  boolean overlapsWith(IEvent other);

  /**
   * Edits a single event.
   */
  IEvent editEvent();

  /**
   * Changes the property of the given event (irrespective of whether it is single or part of a
   * series).
   */
  List<IEvent> editEvents();

  /**
   * Identifies the event that has the given subject and starts at the given date and time and
   * edit its property. If this event is part of a series then the properties of all events in
   * that series that start at or after the given date and time should be changed. If this event
   * is not part of a series then this has the same effect as the command above.
   */
  List<IEventSeries> editSeries();
}
