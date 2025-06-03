// Dreshta Boghra & Aaron Zhou
// CS3500 HW4

package model;

import java.time.LocalDateTime;

/**
 * Immutable implementation of a calendar event.
 * Represents a single scheduled event with a subject, start and end time,
 * optional description and location, and a visibility status.
 * <p>
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

  private CalendarEvent(Builder builder) {
    this.subject = builder.subject;
    this.start = builder.start;
    this.end = builder.end;
    this.description = builder.description;
    this.status = builder.status;
    this.location = builder.location;
  }

  public String getSubject() {
    return subject;
  }

  public LocalDateTime getStart() {
    return start;
  }

  public LocalDateTime getEnd() {
    return end;
  }

  public String getDescription() {
    return description;
  }

  public EventStatus getStatus() {
    return status;
  }

  public String getLocation() {
    return location;
  }

  //finish this method
  @Override
  public boolean overlapsWith(IEvent other) {
    return false;
  }

  public static class Builder {
    private String subject;
    private LocalDateTime start;
    private LocalDateTime end;
    private String description;
    private EventStatus status;
    private String location;


    /**
     * Required: subject
     */
    public Builder subject(String subject) {
      this.subject = subject;
      return this;
    }

    /**
     * Required: start
     */
    public Builder start(LocalDateTime start) {
      this.start = start;
      return this;
    }

    /**
     * Optional: end (default is midnight if not set)
     */
    public Builder end(LocalDateTime end) {
      this.end = end;
      return this;
    }

    /**
     * Optional: description (default is "" if not set)
     */
    public Builder description(String description) {
      this.description = description;
      return this;
    }

    /**
     * Optional: status (default is null if not set)
     */
    public Builder status(EventStatus status) {
      this.status = status;
      return this;
    }

    /**
     * Optional: location (default is "" if not set)
     */
    public Builder location(String location) {
      this.location = location;
      return this;
    }

    // 3) The build() method checks any invariants you want, then calls Person's private constructor
    public CalendarEvent build() {
      if (subject == null || start == null) {
        throw new IllegalStateException("subject and start are required");
      }
      if (end == null) {
        end = start.plusHours(1);
      } else if (end != null && end.isBefore(start)) {
        throw new IllegalStateException("End time cannot be before start time.");
      }

      if (description == null) {
        description = "";
      }
      if (location == null) {
        location = "";
      }
      if (status == null) {
        status = EventStatus.PUBLIC;
      }

      return new CalendarEvent(this);
    }

  }
}