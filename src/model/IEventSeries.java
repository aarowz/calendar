// Dreshta Boghra & Aaron Zhou
// CS3500 HW4

package model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Interface representing a recurring series of calendar events.
 * Provides a method to generate individual event instances from recurrence rules.
 */
public interface IEventSeries {

  /**
   * Returns a list of individual calendar events that make up this recurring series.
   * These events are typically generated from a base event and a recurrence rule.
   *
   * @return a list of IEvent instances representing each occurrence in the series
   */
  List<IEvent> getAllOccurrences();

  /**
   * Returns a unique identifier for this event series.
   * This ID is used to associate individual CalendarEvents with the series they belong to.
   *
   * @return the series ID
   */
  UUID getSeriesId();
}
