// Dreshta Boghra & Aaron Zhou
// CS3500 HW4

package model;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Immutable, read-only copy of a CalendarEvent.
 */
public class ROCalendarEvent implements ROIEvent {
  private final String subject;
  private final LocalDateTime start;
  private final LocalDateTime end;
  private final String description;
  private final EventStatus status;
  private final String location;
  private final UUID seriesId;

  public ROCalendarEvent(String subject,
                         LocalDateTime start,
                         LocalDateTime end,
                         String description,
                         EventStatus status,
                         String location,
                         UUID seriesId) {
    this.subject = subject;
    this.start = start != null ? LocalDateTime.from(start) : null;
    this.end = end != null ? LocalDateTime.from(end) : null;
    this.description = description != null ? description : "";
    this.status = status != null ? status : EventStatus.PUBLIC;
    this.location = location != null ? location : "";
    this.seriesId = seriesId != null ? UUID.fromString(seriesId.toString()) : new UUID(0L, 0L);
  }

  @Override
  public String getSubject() {
    return subject;
  }

  @Override
  public LocalDateTime getStart() {
    return LocalDateTime.from(start);
  }

  @Override
  public LocalDateTime getEnd() {
    return LocalDateTime.from(end);
  }

  @Override
  public String getDescription() {
    return description;
  }

  @Override
  public EventStatus getStatus() {
    return status;
  }

  @Override
  public String getLocation() {
    return location;
  }

  @Override
  public UUID getSeriesId() {
    return UUID.fromString(seriesId.toString());
  }
}