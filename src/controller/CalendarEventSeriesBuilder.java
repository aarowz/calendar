// Dreshta Boghra & Aaron Zhou
// CS3500 HW4

package controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import model.CalendarEventSeries;
import model.EventLocation;
import model.EventStatus;
import model.IEventSeries;

/**
 * A builder for constructing {@link CalendarEventSeries} instances.
 * Supports setting repeat rules and shared properties for all events in the series.
 * Used exclusively by the controller to centralize series creation logic.
 */
public class CalendarEventSeriesBuilder {
  private String subject;
  private LocalDateTime start;
  private LocalDateTime end;
  private String description = "";
  private EventLocation location = null;
  private EventStatus status = EventStatus.PUBLIC;
  private List<Character> repeatDays;
  private Integer repeatCount;
  private LocalDate repeatUntil;

  /**
   * Sets the subject of the series.
   *
   * @param subject the event subject/title
   * @return this builder
   */
  public CalendarEventSeriesBuilder setSubject(String subject) {
    this.subject = subject;
    return this;
  }

  /**
   * Sets the start time of each event in the series.
   *
   * @param start the event start time
   * @return this builder
   */
  public CalendarEventSeriesBuilder setStart(LocalDateTime start) {
    this.start = start;
    return this;
  }

  /**
   * Sets the end time of each event in the series.
   *
   * @param end the event end time
   * @return this builder
   */
  public CalendarEventSeriesBuilder setEnd(LocalDateTime end) {
    this.end = end;
    return this;
  }

  /**
   * Sets the description of the event series.
   *
   * @param description longer description of the events
   * @return this builder
   */
  public CalendarEventSeriesBuilder setDescription(String description) {
    this.description = description;
    return this;
  }

  /**
   * Sets the location for the events in the series.
   *
   * @param locationStr the physical or virtual location
   * @return this builder
   */
  public CalendarEventSeriesBuilder setLocation(String locationStr) {
    this.location = locationStr == null ? null : new EventLocation(locationStr);
    return this;
  }

  /**
   * Sets the status of the event series (public/private).
   *
   * @param status the visibility of the series
   * @return this builder
   */
  public CalendarEventSeriesBuilder setStatus(EventStatus status) {
    this.status = status;
    return this;
  }

  /**
   * Sets the days of the week on which the event series repeats.
   *
   * @param repeatDays list of weekday characters (e.g., M, W, F)
   * @return this builder
   */
  public CalendarEventSeriesBuilder setRepeatDays(List<Character> repeatDays) {
    this.repeatDays = repeatDays;
    return this;
  }

  /**
   * Sets the number of times the event series repeats.
   *
   * @param repeatCount number of occurrences
   * @return this builder
   */
  public CalendarEventSeriesBuilder setRepeatCount(Integer repeatCount) {
    this.repeatCount = repeatCount;
    return this;
  }

  /**
   * Sets the end date for the event series.
   *
   * @param repeatUntil the final date to repeat through
   * @return this builder
   */
  public CalendarEventSeriesBuilder setRepeatUntil(LocalDate repeatUntil) {
    this.repeatUntil = repeatUntil;
    return this;
  }

  /**
   * Builds the final {@link CalendarEventSeries} instance.
   *
   * @return a new CalendarEventSeries
   * @throws IllegalStateException if required fields are missing
   */
  public IEventSeries build() {
    if (subject == null || start == null || end == null || repeatDays == null) {
      throw new IllegalStateException("Missing required fields for event series");
    }
    return new CalendarEventSeries(subject, start, end, description, location,
            status, repeatDays, repeatCount, repeatUntil);
  }
}