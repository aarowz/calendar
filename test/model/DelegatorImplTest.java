// Dreshta Boghra & Aaron Zhou
// CS3500 HW5

package model;

import controller.CreateEventCommand;

import org.junit.Before;
import org.junit.Test;

import java.time.*;
import java.util.List;

import static org.junit.Assert.*;

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

  /**
   * Tests that creating and using a calendar enables successful event creation.
   */
  @Test
  public void testCreateAndUseCalendarAllowsEventOperations() throws Exception {
    delegator.createCalendar("testcal", ZoneId.of("America/New_York"));
    delegator.useCalendar("testcal");

    CreateEventCommand create = new CreateEventCommand(
            "TeamSync",
            LocalDateTime.of(2025, 6, 15, 9, 0),
            LocalDateTime.of(2025, 6, 15, 10, 0),
            null, null, null,
            null, null, null
    );
    create.execute(delegator, null);

    List<ROIEvent> events = delegator.getEventsOn(LocalDate.of(2025, 6,
            15));
    assertEquals(1, events.size());
    assertEquals("TeamSync", events.get(0).getSubject());
  }

  /**
   * Tests that trying to use a non-existent calendar throws an exception.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testUseInvalidCalendarThrows() {
    delegator.useCalendar("nonexistent");
  }

  /**
   * Tests that getEventsOn delegates correctly.
   */
  @Test
  public void testGetEventsOnDelegatesCorrectly() throws Exception {
    delegator.createCalendar("c1", ZoneId.of("America/New_York"));
    delegator.useCalendar("c1");

    CreateEventCommand cmd = new CreateEventCommand(
            "Meeting",
            LocalDateTime.of(2025, 6, 10, 10, 0),
            LocalDateTime.of(2025, 6, 10, 11, 0),
            null, null, null,
            null, null, null
    );
    cmd.execute(delegator, null);

    List<ROIEvent> events = delegator.getEventsOn(LocalDate.of(2025, 6,
            10));
    assertEquals(1, events.size());
    assertEquals("Meeting", events.get(0).getSubject());
  }

  /**
   * Tests that isBusyAt returns true for overlapping events.
   */
  @Test
  public void testIsBusyAtDelegatesCorrectly() throws Exception {
    delegator.createCalendar("work", ZoneId.of("America/New_York"));
    delegator.useCalendar("work");

    CreateEventCommand cmd = new CreateEventCommand(
            "Call",
            LocalDateTime.of(2025, 6, 12, 13, 0),
            LocalDateTime.of(2025, 6, 12, 14, 0),
            null, null, null,
            null, null, null
    );
    cmd.execute(delegator, null);

    assertTrue(delegator.isBusyAt(LocalDateTime.of(2025, 6, 12, 13,
            30)));
    assertFalse(delegator.isBusyAt(LocalDateTime.of(2025, 6, 12, 15,
            0)));
  }

  /**
   * Tests copying an event from one calendar to another with different timezones.
   */
  @Test
  public void testCopyEventBetweenCalendars() throws Exception {
    delegator.createCalendar("source", ZoneId.of("America/New_York"));
    delegator.createCalendar("target", ZoneId.of("America/Chicago"));

    delegator.useCalendar("source");
    LocalDateTime start = LocalDateTime.of(2025, 7, 1, 9, 0);
    LocalDateTime end = LocalDateTime.of(2025, 7, 1, 10, 0);
    delegator.createEvent("Kickoff", start, end, null, EventStatus.PUBLIC,
            null);

    LocalDateTime targetStart = LocalDateTime.of(2025, 8, 1, 9,
            0);
    delegator.copyEvent("Kickoff", start, "target", targetStart);

    delegator.useCalendar("target");
    List<ROIEvent> copied = delegator.getEventsOn(LocalDate.of(2025, 8,
            1));
    assertEquals(1, copied.size());
    assertEquals("Kickoff", copied.get(0).getSubject());
    assertEquals(targetStart, copied.get(0).getStart());
  }
}