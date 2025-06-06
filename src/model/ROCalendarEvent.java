// Dreshta Boghra & Aaron Zhou
// CS3500 HW4

package model;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * Immutable, read-only implementation of a calendar event.
 * Used to safely expose event data without allowing any modifications.
 */
public class ROCalendarEvent implements ROIEvent {
  private final String subject;
  private final LocalDateTime start;
  private final LocalDateTime end;
  private final String description;
  private final EventStatus status;
  private final String location;
  private final UUID seriesId;

  /**
   * Constructs a new read-only event from its components.
   * Fields are copied to ensure immutability.
   *
   * @param subject     event subject/title
   * @param start       event start time
   * @param end         event end time
   * @param description event description
   * @param status      event status (public or private)
   * @param location    event location
   * @param seriesId    unique ID of the series, if any
   */
  public ROCalendarEvent(String subject,
                         LocalDateTime start,
                         LocalDateTime end,
                         String description,
                         EventStatus status,
                         String location,
                         UUID seriesId) {
    this.subject = subject;

    // copy start time if not null
    if (start != null) {
      this.start = LocalDateTime.from(start);
    } else {
      this.start = null;
    }

    // copy end time if not null
    if (end != null) {
      this.end = LocalDateTime.from(end);
    } else {
      this.end = null;
    }

    // default description to empty if null
    this.description = Objects.requireNonNullElse(description, "");

    // default status to PUBLIC if null
    this.status = Objects.requireNonNullElse(status, EventStatus.PUBLIC);

    // default location to empty if null
    this.location = Objects.requireNonNullElse(location, "");

    // assign a safe default UUID if null
    if (seriesId != null) {
      this.seriesId = UUID.fromString(seriesId.toString());
    } else {
      this.seriesId = new UUID(0L, 0L);
    }
  }

  /**
   * Returns the subject/title of the event.
   */
  @Override
  public String getSubject() {
    return subject;
  }

  /**
   * Returns the start date and time of the event.
   */
  @Override
  public LocalDateTime getStart() {
    return LocalDateTime.from(start); // return a defensive copy
  }

  /**
   * Returns the end date and time of the event.
   */
  @Override
  public LocalDateTime getEnd() {
    return LocalDateTime.from(end); // return a defensive copy
  }

  /**
   * Returns the event's description.
   */
  @Override
  public String getDescription() {
    return description;
  }

  /**
   * Returns the visibility status of the event (PUBLIC or PRIVATE).
   */
  @Override
  public EventStatus getStatus() {
    return status;
  }

  /**
   * Returns the event's location.
   */
  @Override
  public String getLocation() {
    return location;
  }

  /**
   * Returns the ID of the series this event belongs to (or a zero UUID if none).
   */
  @Override
  public UUID getSeriesId() {
    return UUID.fromString(seriesId.toString());
  }
}