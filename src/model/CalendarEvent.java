// Dreshta Boghra & Aaron Zhou
// CS3500 HW4

package model;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Immutable implementation of a calendar event.
 * Represents a single scheduled event with a subject, start and end time,
 * optional description and location, and a visibility status.
 * This class enforces immutability and should be constructed using a
 * full-arguments constructor. Fields should not be modified after creation.
 */
public class CalendarEvent implements IEvent {
  private final String subject;
  private final LocalDateTime start;
  private final LocalDateTime end;
  private final String description;
  private final EventStatus status;
  private final String location;
  private final UUID seriesId;

  /**
   * Private constructor to initiate the builder pattern.
   *
   * @param builder the given builder input
   */
  private CalendarEvent(Builder builder) {
    this.subject = builder.subject;
    this.start = builder.start;
    this.end = builder.end;
    this.description = builder.description;
    this.status = builder.status;
    this.location = builder.location;
    this.seriesId = builder.seriesId;
  }

  /**
   * Determines if this event overlaps in time with another event.
   * Two events overlap if their time intervals intersect.
   *
   * @param other the other event to compare against
   * @return true if the events overlap, false otherwise
   */
  @Override
  public boolean overlapsWith(IEvent other) {
    return !this.end.isBefore(other.getStart()) && !this.start.isAfter(other.getEnd());
  }


  @Override
  public IEvent editEvent(String newSubject, LocalDateTime newStart, LocalDateTime newEnd,
                          String newDescription, EventStatus newStatus, String newLocation) {

    // if the start time changed, remove the seriesId (event is no longer part of the original
    // series)
    boolean startChanged = !this.start.equals(newStart);

    // construct the builder based on the state of each field
    CalendarEvent.Builder builder = new CalendarEvent.Builder()
            .subject(newSubject != null ? newSubject : this.subject)
            .start(newStart != null ? newStart : this.start)
            .end(newEnd != null ? newEnd : this.end)
            .description(newDescription != null ? newDescription : this.description)
            .status(newStatus != null ? newStatus : this.status)
            .location(newLocation != null ? newLocation : this.location);

    // if the start time has not changed and the event is a series
    if (!startChanged && this.seriesId != null) {
      // preserve series membership
      builder.seriesId(this.seriesId);
    }

    // initiate the builder
    return builder.build();
  }


  // read-only property accessors inherited from ROIEvent interface
  @Override
  public String getSubject() {
    return subject;
  }

  @Override
  public LocalDateTime getStart() {
    return start;
  }

  @Override
  public LocalDateTime getEnd() {
    return end;
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
    return this.seriesId == null ? null : UUID.fromString(this.seriesId.toString());
  }

  /**
   * Builder class for constructing CalendarEvent instances.
   */
  public static class Builder {
    private String subject;
    private LocalDateTime start;
    private LocalDateTime end;
    private String description;
    private EventStatus status;
    private String location;
    private UUID seriesId;

    /**
     * Required: subject.
     */
    public Builder subject(String subject) {
      this.subject = subject;
      return this;
    }

    /**
     * Required: start.
     */
    public Builder start(LocalDateTime start) {
      this.start = start;
      return this;
    }

    /**
     * Optional: end (default is 1 hour after start if not set).
     */
    public Builder end(LocalDateTime end) {
      this.end = end;
      return this;
    }

    /**
     * Optional: description (default is "" if not set).
     */
    public Builder description(String description) {
      this.description = description;
      return this;
    }

    /**
     * Optional: status (default is PUBLIC if not set).
     */
    public Builder status(EventStatus status) {
      this.status = status;
      return this;
    }

    /**
     * Optional: location.
     */
    public Builder location(String location) {
      this.location = location;
      return this;
    }

    /**
     * Optional: seriesId — only set if part of a series.
     */
    public Builder seriesId(UUID seriesId) {
      this.seriesId = seriesId;
      return this;
    }

    /**
     * Builds and returns an immutable CalendarEvent.
     * Validates required fields and applies defaults for optional ones.
     *
     * @return a fully constructed CalendarEvent instance
     * @throws IllegalStateException if required fields are missing or invalid
     */
    public CalendarEvent build() {
      // check required fields: subject and start must be provided
      if (subject == null || start == null) {
        throw new IllegalStateException("subject and start are required");
      }

      // if no end time is provided, default to 1 hour after start
      if (end == null) {
        end = start.plusHours(1);
      }
      // ensure end is not before start
      else if (end.isBefore(start)) {
        throw new IllegalStateException("End time cannot be before start time.");
      }

      // default empty string for optional description if not set
      if (description == null) {
        description = "";
      }

      // default empty string for optional location if not set
      if (location == null) {
        location = "";
      }

      // default to PUBLIC status if not set
      if (status == null) {
        status = EventStatus.PUBLIC;
      }

      // seriesId is optional — leave as null if not part of a series
      return new CalendarEvent(this);
    }
  }

  /**
   * String representation of the model logic for debugging.
   *
   * @return any debugging information
   */
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();

    sb.append(subject)
            .append(" from ").append(start)
            .append(" to ").append(end);

    if (location != null && !location.isEmpty()) {
      sb.append(" at ").append(location);
    }

    return sb.toString();
  }
}