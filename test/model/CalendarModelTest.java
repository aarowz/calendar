// Dreshta Boghra & Aaron Zhou
// CS3500 HW4

package model;

import org.junit.Before;
import org.junit.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for the CalendarModel implementation using the ICalendar interface.
 * Verifies behavior for single and recurring event creation, querying, duplication,
 * busy checks, and immutability of returned data.
 */
public class CalendarModelTest {
  private ICalendar model;

  @Before
  public void setup() {
    model = new CalendarModel();
  }

  /**
   * Tests that a single event is created and retrieved correctly on its date.
   */
  @Test
  public void testCreateSingleEvent() {
    model.createEvent("Team Meeting",
            LocalDateTime.of(2025, 6, 5, 14, 0),
            LocalDateTime.of(2025, 6, 5, 15, 0),
            "Weekly sync",
            EventStatus.PUBLIC,
            "Room A");

    List<ROIEvent> events = model.getEventsOn(LocalDate.of(2025, 6, 5));
    assertEquals(1, events.size());
    ROIEvent event = events.get(0);
    assertEquals("Team Meeting", event.getSubject());
  }

  /**
   * Tests creation of a recurring event series and verifies the correct count.
   */
  @Test
  public void testCreateRecurringEventSeries() {
    model.createEventSeries("Yoga",
            "Morning yoga class",
            "Studio",
            EventStatus.PUBLIC,
            LocalDate.of(2025, 6, 2),
            null,
            LocalTime.of(7, 0),
            LocalTime.of(8, 0),
            Set.of(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY),
            5);

    int total = 0;
    for (int i = 0; i < 14; i++) {
      LocalDate date = LocalDate.of(2025, 6, 2).plusDays(i);
      total += model.getEventsOn(date).size();
    }
    assertEquals(5, total);
  }

  /**
   * Tests that isBusyAt returns true during an event and false outside of it.
   */
  @Test
  public void testBusyStatusDuringEvent() {
    model.createEvent("1-on-1",
            LocalDateTime.of(2025, 6, 6, 10, 0),
            LocalDateTime.of(2025, 6, 6, 10, 30),
            "Check-in",
            EventStatus.PRIVATE,
            "Office");

    assertTrue(model.isBusyAt(LocalDateTime.of(2025, 6, 6, 10,
            15)));
    assertFalse(model.isBusyAt(LocalDateTime.of(2025, 6, 6, 9,
            59)));
  }

  /**
   * Tests that creating a duplicate event with the same subject, start,
   * and end throws an exception.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testRejectDuplicateEvent() {
    LocalDateTime start = LocalDateTime.of(2025, 6, 7, 9, 0);
    LocalDateTime end = LocalDateTime.of(2025, 6, 7, 10, 0);

    model.createEvent("Standup", start, end, "Daily check-in",
            EventStatus.PUBLIC, "Zoom");
    model.createEvent("Standup", start, end, "Daily check-in",
            EventStatus.PUBLIC, "Zoom");
  }

  /**
   * Tests that events are correctly retrieved only on their assigned days.
   */
  @Test
  public void testGetEventsOnMultipleDays() {
    model.createEvent("Workshop",
            LocalDateTime.of(2025, 6, 8, 11, 0),
            LocalDateTime.of(2025, 6, 8, 13, 0),
            "Skill session",
            EventStatus.PUBLIC,
            "Lab");

    model.createEvent("Lunch",
            LocalDateTime.of(2025, 6, 9, 12, 0),
            LocalDateTime.of(2025, 6, 9, 13, 0),
            "Team meal",
            EventStatus.PUBLIC,
            "Cafeteria");

    assertEquals(1, model.getEventsOn(LocalDate.of(2025, 6,
            8)).size());
    assertEquals(1, model.getEventsOn(LocalDate.of(2025, 6,
            9)).size());
    assertEquals(0, model.getEventsOn(LocalDate.of(2025, 6,
            10)).size());
  }

  /**
   * Tests that isDuplicate detects existing events and distinguishes non-matching times.
   */
  @Test
  public void testIsDuplicateDetection() {
    LocalDateTime start = LocalDateTime.of(2025, 6, 12, 10, 0);
    LocalDateTime end = LocalDateTime.of(2025, 6, 12, 11, 0);
    model.createEvent("Pitch", start, end, "Presenting", EventStatus.PUBLIC,
            "Main Hall");
    assertTrue(model.isDuplicate("Pitch", start, end));
    assertFalse(model.isDuplicate("Pitch", start.minusHours(1), end));
  }
}