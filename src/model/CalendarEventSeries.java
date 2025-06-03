// Dreshta Boghra & Aaron Zhou
// CS3500 HW4

package model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Immutable implementation of a recurring event series.
 * Uses a recurrence rule to generate individual CalendarEvent instances.
 * Supports rules such as repeating on weekdays and ending after a count or by a date.
 */
public class CalendarEventSeries implements IEventSeries {
  String subject;
  LocalDateTime start;
  LocalDateTime end;
  String longerDescription;
  String location;
  EventStatus status;

  @Override
  public List<IEvent> getAllOccurrences() {
    return List.of();
  }

  @Override
  public UUID getSeriesId() {
    return null;
  }
}