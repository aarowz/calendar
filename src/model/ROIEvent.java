// Dreshta Boghra & Aaron Zhou
// CS3500 HW4

package model;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Read-only interface for a CalendarEvent.
 * Provides access to all properties without allowing modification.
 */
public interface ROIEvent {
  /**
   * Returns the subject of the event.
   */
  String getSubject();

  /**
   * Returns the start time of the event.
   */
  LocalDateTime getStart();

  /**
   * Returns the end time of the event.
   */
  LocalDateTime getEnd();

  /**
   * Returns the description of the event.
   */
  String getDescription();

  /**
   * Returns the status (e.g., PUBLIC or PRIVATE) of the event.
   */
  EventStatus getStatus();

  /**
   * Returns the location of the event.
   */
  String getLocation();

  /**
   * Returns the UUID of the event's series, or a null/empty UUID if it does not belong to a series.
   */
  UUID getSeriesId();
}