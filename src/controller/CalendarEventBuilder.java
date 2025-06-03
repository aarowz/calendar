// Dreshta Boghra & Aaron Zhou
// CS3500 HW4

package controller;

import model.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * A builder for constructing {@link CalendarEvent} instances step-by-step.
 * This builder allows optional fields to be set fluently and enforces that
 * required fields (subject, start, and end) are present before building.
 * <p>
 * This class is used only in the controller layer to encapsulate model object
 * creation while preserving MVC design principles.
 */
public class CalendarEventBuilder {
  private String subject;
  private LocalDateTime start;
  private LocalDateTime end;
  private String description = "";
  private EventLocation location = null;
  private EventStatus status = EventStatus.PUBLIC;

  /**
   * Sets the subject of the event.
   *
   * @param subject the event subject/title
   * @return this builder
   */
  public CalendarEventBuilder setSubject(String subject) {
    this.subject = subject;
    return this;
  }

  /**
   * Sets the start time of the event.
   *
   * @param start the event start time
   * @return this builder
   */
  public CalendarEventBuilder setStart(LocalDateTime start) {
    this.start = start;
    return this;
  }

  /**
   * Sets the end time of the event.
   *
   * @param end the event end time
   * @return this builder
   */
  public CalendarEventBuilder setEnd(LocalDateTime end) {
    this.end = end;
    return this;
  }

  /**
   * Sets the optional event description.
   *
   * @param description a longer description of the event
   * @return this builder
   */
  public CalendarEventBuilder setDescription(String description) {
    this.description = description;
    return this;
  }

  /**
   * Sets the event location, if any.
   *
   * @param locationStr the physical or virtual location of the event
   * @return this builder
   */
  public CalendarEventBuilder setLocation(String locationStr) {
    this.location = locationStr == null ? null : new EventLocation(locationStr);
    return this;
  }

  /**
   * Sets the event visibility status (public/private).
   *
   * @param status the status of the event
   * @return this builder
   */
  public CalendarEventBuilder setStatus(EventStatus status) {
    this.status = status;
    return this;
  }

  /**
   * Builds the final {@link CalendarEvent} instance.
   *
   * @return a new CalendarEvent
   * @throws IllegalStateException if required fields are missing
   */
  public IEvent build() {
    if (subject == null || start == null || end == null) {
      throw new IllegalStateException("Subject, start, and end must not be null");
    }
    return new CalendarEvent(subject, start, end, description, location, status);
  }
}