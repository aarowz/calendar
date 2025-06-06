// Dreshta Boghra & Aaron Zhou
// CS3500 HW4

package model;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Interface representing a single calendar event.
 * Provides methods to access core event properties and determine temporal conflicts.
 */
public interface IEvent extends ROIEvent {
  /**
   * Determines if this event overlaps in time with another event.
   *
   * @param other the other event to compare with
   * @return true if the two events overlap in time, false otherwise
   */
  boolean overlapsWith(IEvent other);

  /**
   * Edits a single event.
   *
   * @param newSubject     the updated subject for the event
   * @param newStart       the updated start date and time
   * @param newEnd         the updated end date and time
   * @param newDescription the updated description
   * @param newStatus      the updated public/private status
   * @param newLocation    the updated location
   * @return a new IEvent instance with the updated fields
   */
  IEvent editEvent(String newSubject, LocalDateTime newStart, LocalDateTime newEnd,
                   String newDescription, EventStatus newStatus, String newLocation);

  /**
   * Returns the series ID if this event is part of a recurring series.
   * May return null if it's a standalone event.
   *
   * @return the series UUID or null
   */
  UUID getSeriesId();

  /**
   * Overrides the built-in toString method.
   *
   * @return a String
   */
  String toString();
}