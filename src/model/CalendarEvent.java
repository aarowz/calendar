// Dreshta Boghra & Aaron Zhou
// CS3500 HW4

package model;

import java.time.LocalDateTime;

/**
 * Immutable implementation of a calendar event.
 * Represents a single scheduled event with a subject, start and end time,
 * optional description and location, and a visibility status.
 *
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
  // Constructor and methods like getSubject(), getStart(), etc. should be implemented below,
  // preserving immutability (no setters, and all fields marked final).

  public CalendarEvent() {

  }

  @Override
  public String getSubject() {
    return "";
  }

  @Override
  public LocalDateTime getStart() {
    return null;
  }

  @Override
  public LocalDateTime getEnd() {
    return null;
  }

  @Override
  public String getDescription() {
    return "";
  }

  @Override
  public EventStatus getStatus() {
    return null;
  }

  @Override
  public String getLocation() {
    return "";
  }

  @Override
  public boolean overlapsWith(IEvent other) {
    return false;
  }

}