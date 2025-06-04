// Dreshta Boghra & Aaron Zhou
// CS3500 HW4

//add a series id to this and leave it null or empty if its not in a series but in the eventSeries class, we set this field once it gets added to a series

package model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

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
  private final UUID seriesId;

  private CalendarEvent(Builder builder) {
    this.subject = builder.subject;
    this.start = builder.start;
    this.end = builder.end;
    this.description = builder.description;
    this.status = builder.status;
    this.location = builder.location;
    this.seriesId = builder.seriesId;
  }

  private CalendarEvent(String subject, LocalDateTime start, LocalDateTime end,
                        String description, EventStatus status, String location, UUID seriesId) {
    this.subject = subject;
    this.start = start;
    this.end = end;
    this.description = description;
    this.status = status;
    this.location = location;
    this.seriesId = seriesId;
  }

  //finish this method
  @Override
  public boolean overlapsWith(IEvent other) {
    return false;
  }

  @Override
  public IEvent editEvent() {
    return null;
  }

  @Override
  public List<IEvent> editEvents() {
    return null;
  }

  @Override
  public List<IEventSeries> editSeries() {
    return null;
  }

  public static class Builder {
    private String subject;
    private LocalDateTime start;
    private LocalDateTime end;
    private String description;
    private EventStatus status;
    private String location;
    private UUID seriesId;

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

    /**
     * Will set an ID if an event is a pert of series.
     */
    public Builder seriesId(UUID seriesId) {
      this.seriesId = seriesId;
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
      if (seriesId == null) {
        seriesId = new UUID(0L, 0L);
      }
      return new CalendarEvent(this);
    }
  }
}
