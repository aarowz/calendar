// Dreshta Boghra & Aaron Zhou
// CS3500 HW4

package model;

import java.time.LocalDateTime;

/**
 * Interface representing a single calendar event.
 * Provides methods to access core event properties and determine temporal conflicts.
 */
public interface IEvent {

  /**
   * Returns the subject or title of the event.
   * @return the event's subject
   */
  String getSubject();

  /**
   * Returns the start date and time of the event.
   * @return the start time of the event
   */
  LocalDateTime getStart();

  /**
   * Returns the end date and time of the event.
   * @return the end time of the event
   */
  LocalDateTime getEnd();

  /**
   * Returns the longer description of the event.
   * @return the event's description
   */
  String getDescription();

  /**
   * Returns the visibility status of the event (e.g., public or private).
   * @return the event's status
   */
  EventStatus getStatus();

  /**
   * Returns the location of the event.
   * Can be a physical address or an online meeting link.
   * @return the event's location
   */
  String getLocation();

  /**
   * Determines if this event overlaps in time with another event.
   * @param other the other event to compare with
   * @return true if the two events overlap in time, false otherwise
   */
  boolean overlapsWith(IEvent other);
}
