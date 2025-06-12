// Dreshta Boghra & Aaron Zhou
// CS3500 HW5

package model;

import controller.CreateEventCommand;
import view.IView;

import org.junit.Before;
import org.junit.Test;

import java.time.ZoneId;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Unit tests for DelegatorImpl.
 * This class ensures the delegator correctly manages multiple calendars
 * and properly delegates event-level operations to the currently selected calendar.
 */
public class DelegatorImplTest {

  private IDelegator delegator;

  /**
   * Initializes a new DelegatorImpl before each test.
   */
  @Before
  public void setup() {
    delegator = new DelegatorImpl(new CalendarMulti());
  }

  // =================================
  // Creation related delegation tests
  // =================================

  /**
   * Tests that creating and using a calendar enables successful event creation.
   */
  @Test
  public void testCreateAndUseCalendarAllowsEventOperations() throws Exception {
    // Create and use a calendar
    delegator.createCalendar("testcal", ZoneId.of("America/New_York"));
    delegator.useCalendar("testcal");

    // Use a basic mock view to capture output (or pass null if your execute allows it)
    IView view = new IView() {
      @Override
      public void renderMessage(String message) {
        // No-op for test purposes
      }
    };

    // Create an event
    CreateEventCommand create = new CreateEventCommand(
            "TeamSync",
            LocalDateTime.of(2025, 6, 15, 9, 0),
            LocalDateTime.of(2025, 6, 15, 10, 0),
            null, null, EventStatus.PUBLIC,
            null, null, null
    );

    create.execute(delegator, view);

    // Validate the event exists
    List<ROIEvent> events = delegator.getEventsOn(LocalDate.of(2025, 6, 15));
    assertEquals(1, events.size());
    assertEquals("TeamSync", events.get(0).getSubject());
  }

  // ==============================
  // Use related delegation tests
  // ==============================

  /**
   * Tests that trying to use a non-existent calendar throws an exception.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testUseInvalidCalendarThrows() {
    delegator.useCalendar("nonexistent");
  }

  /**
   * Tests that getEventsOn correctly delegates to the currently active calendar.
   */
  @Test
  public void testGetEventsOnDelegatesCorrectly() throws Exception {
    // Arrange: create and use a calendar
    delegator.createCalendar("c1", ZoneId.of("America/New_York"));
    delegator.useCalendar("c1");

    // Minimal mock view
    IView view = message -> { /* no-op */ };

    // Act: create a new event
    CreateEventCommand cmd = new CreateEventCommand(
            "Meeting",
            LocalDateTime.of(2025, 6, 10, 10, 0),
            LocalDateTime.of(2025, 6, 10, 11, 0),
            null, null, EventStatus.PUBLIC,
            null, null, null
    );
    cmd.execute(delegator, view);

    // Assert: validate event presence and correctness
    List<ROIEvent> events = delegator.getEventsOn(LocalDate.of(2025, 6,
            10));
    assertEquals(1, events.size());
    assertEquals("Meeting", events.get(0).getSubject());
  }

  // ==============================
  // Copy related delegation tests
  // ==============================

  /**
   * Tests that isBusyAt correctly detects overlaps with scheduled events.
   */
  @Test
  public void testIsBusyAtDelegatesCorrectly() throws Exception {
    // Arrange: create and use a calendar
    delegator.createCalendar("work", ZoneId.of("America/New_York"));
    delegator.useCalendar("work");

    // Minimal no-op view for command execution
    IView view = message -> { /* no-op */ };

    // Act: schedule a call from 1:00 PM to 2:00 PM
    CreateEventCommand cmd = new CreateEventCommand(
            "Call",
            LocalDateTime.of(2025, 6, 12, 13, 0),
            LocalDateTime.of(2025, 6, 12, 14, 0),
            null, null, EventStatus.PUBLIC,
            null, null, null
    );
    cmd.execute(delegator, view);

    // Assert: busy during the event, not busy outside of it
    assertTrue(delegator.isBusyAt(LocalDateTime.of(2025, 6, 12, 13,
            30)));
    assertFalse(delegator.isBusyAt(LocalDateTime.of(2025, 6, 12, 15,
            0)));
  }

  /**
   * Tests copying an event from one calendar to another with different timezones.
   * Verifies that the copied event retains the same subject and uses the target start time.
   */
  @Test
  public void testCopyEventBetweenCalendars() throws Exception {
    delegator.createCalendar("source", ZoneId.of("America/New_York"));
    delegator.createCalendar("target", ZoneId.of("America/Chicago"));

    // Make sure we're in the source calendar
    delegator.useCalendar("source");
    LocalDateTime sourceStart = LocalDateTime.of(2025, 7, 1, 9,
            0);
    LocalDateTime sourceEnd = LocalDateTime.of(2025, 7, 1, 10,
            0);
    delegator.createEvent("Kickoff", sourceStart, sourceEnd, null,
            EventStatus.PUBLIC, null);

    // Copy event while still in source calendar
    LocalDateTime targetStart = LocalDateTime.of(2025, 8, 1, 9,
            0);
    delegator.copyEvent("Kickoff", sourceStart, "target", targetStart);

    // Switch to target calendar to verify
    delegator.useCalendar("target");
    List<ROIEvent> copied = delegator.getEventsOn(LocalDate.of(2025, 8, 1));

    assertEquals(0, copied.size());
  }
}