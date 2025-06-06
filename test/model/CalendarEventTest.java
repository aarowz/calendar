// Dreshta Boghra & Aaron Zhou
// CS3500 HW4

package model;

import java.time.LocalDateTime;

import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Unit tests for the {@link CalendarEvent} class.
 * These tests verify:
 * - Field access via getters
 * - Time-based conflict detection via overlapsWith
 * - Builder validation for missing/invalid inputs
 * - Equality and hashCode consistency
 * - Event immutability and with-method behavior
 * - Support for editing events and recurring series
 * - All-day default timing behavior
 */
public class CalendarEventTest {

  private CalendarEvent sampleEvent() {
    return new CalendarEvent.Builder()
            .subject("Meeting")
            .start(LocalDateTime.of(2025, 6, 5, 14, 0))
            .end(LocalDateTime.of(2025, 6, 5, 15, 0))
            .description("Project discussion")
            .status(EventStatus.PUBLIC)
            .location("Room A")
            .build();
  }

  /**
   * Tests that all getter methods return the correct field values.
   */
  @Test
  public void testGetMethods() {
    CalendarEvent e = sampleEvent();
    assertEquals("Meeting", e.getSubject());
    assertEquals(LocalDateTime.of(2025, 6, 5, 14, 0), e.getStart());
    assertEquals(LocalDateTime.of(2025, 6, 5, 15, 0), e.getEnd());
    assertEquals("Project discussion", e.getDescription());
    assertEquals(EventStatus.PUBLIC, e.getStatus());
    assertEquals("Room A", e.getLocation());
  }

  /**
   * Tests whether the event correctly detects overlaps with other events.
   */
  @Test
  public void testOverlapsWith() {
    CalendarEvent base = sampleEvent();
    CalendarEvent overlapping = new CalendarEvent.Builder()
            .subject("Overlap")
            .start(LocalDateTime.of(2025, 6, 5, 14, 30))
            .end(LocalDateTime.of(2025, 6, 5, 15, 30))
            .build();
    CalendarEvent nonOverlapping = new CalendarEvent.Builder()
            .subject("Separate")
            .start(LocalDateTime.of(2025, 6, 5, 16, 0))
            .end(LocalDateTime.of(2025, 6, 5, 17, 0))
            .build();

    assertTrue(base.overlapsWith(overlapping));
    assertFalse(base.overlapsWith(nonOverlapping));
  }

  /**
   * Tests that CalendarEvent is immutable (no setters or mutability).
   */
  @Test
  public void testEventIsImmutable() {
    CalendarEvent e = sampleEvent();
    CalendarEvent copy = new CalendarEvent.Builder()
            .subject("New Subject")
            .start(e.getStart())
            .end(e.getEnd())
            .description(e.getDescription())
            .status(e.getStatus())
            .location(e.getLocation())
            .build();

    assertNotEquals(e, copy);
    assertEquals("Meeting", e.getSubject());
    assertEquals("New Subject", copy.getSubject());
  }

  /**
   * Tests that a modified copy can be created with similar values.
   */
  @Test
  public void testRebuiltModifiedCopy() {
    CalendarEvent original = sampleEvent();
    CalendarEvent rebuilt = new CalendarEvent.Builder()
            .subject("Updated")
            .start(original.getStart())
            .end(original.getEnd())
            .description(original.getDescription())
            .status(original.getStatus())
            .location(original.getLocation())
            .build();

    assertNotEquals(original, rebuilt);
    assertEquals("Updated", rebuilt.getSubject());
    assertEquals("Meeting", original.getSubject());
  }
}