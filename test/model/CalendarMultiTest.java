package model;

import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

/**
 * Testing class for the CalendarMulti class.
 */
public class CalendarMultiTest {

  private ICalendarMulti multi;

  @Before
  public void setUp() {
    multi = new CalendarMulti();
  }

  // ==============================
  // Creation related tests
  // ==============================

  /**
   * Tests creating a calendar with valid name and timezone.
   */
  @Test
  public void testCreateCalendarValid() {
    multi.createCalendar("Work", ZoneId.of("America/New_York"));
    multi.useCalendar("Work");
    assertNotNull(multi.getCurrentCalendar());
  }

  /**
   * Tests that creating two calendars with the same name throws.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testCreateCalendarDuplicate() {
    multi.createCalendar("Dup", ZoneId.of("UTC"));
    multi.createCalendar("Dup", ZoneId.of("UTC"));
  }

  /**
   * Tests creating a calendar with a null name.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testCreateCalendarNullName() {
    multi.createCalendar(null, ZoneId.of("UTC"));
  }

  /**
   * Tests creating a calendar with a null timezone.
   */
  @Test(expected = NullPointerException.class)
  public void testCreateCalendarNullZone() {
    multi.createCalendar("NullZone", null);
  }

  // ==============================
  // Editing related tests
  // ==============================

  /**
   * Tests that editing a calendar to use an invalid timezone string throws an exception.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testEditCalendarInvalidZone() {
    // Step 1: Create a valid calendar
    multi.createCalendar("mycal", ZoneId.of("America/New_York"));

    // Step 2: Attempt to update the timezone to an invalid one
    multi.editCalendar("mycal", "timezone", "Fake/Zone"); // should throw
  }

  /**
   * Tests editing both the name and timezone of a calendar.
   */
  @Test
  public void testEditCalendarChangeNameAndZone() {
    multi.createCalendar("Old", ZoneId.of("UTC"));
    multi.editCalendar("Old", "name", "New");
    multi.editCalendar("New", "timezone", "America/Chicago");
    multi.useCalendar("New");
    assertNotNull(multi.getCurrentCalendar());
  }

  /**
   * Tests editing a calendar with an invalid property key.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testEditCalendarInvalidProperty() {
    multi.createCalendar("Test", ZoneId.of("UTC"));
    multi.editCalendar("Test", "color", "red");
  }

  /**
   * Tests renaming a calendar to a name that already exists.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testEditCalendarToExistingName() {
    multi.createCalendar("A", ZoneId.of("UTC"));
    multi.createCalendar("B", ZoneId.of("UTC"));
    multi.editCalendar("A", "name", "B");
  }

  /**
   * Tests editing a calendar that does not exist.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testEditCalendarNonexistent() {
    multi.editCalendar("Ghost",
            "name", "Phantom");
  }

  /**
   * Tests editing a calendar with a null property name.
   */
  @Test(expected = NullPointerException.class)
  public void testEditCalendarNullProperty() {
    multi.createCalendar("X", ZoneId.of("UTC"));
    multi.editCalendar("X", null, "test");
  }

  /**
   * Tests editing a calendar with a null property value.
   */
  @Test(expected = NullPointerException.class)
  public void testEditCalendarNullValue() {
    multi.createCalendar("X", ZoneId.of("UTC"));
    multi.editCalendar("X", "name", null);
  }

  // ==============================
  // Using related tests
  // ==============================

  /**
   * Tests selecting an existing calendar.
   */
  @Test
  public void testUseCalendarValid() {
    multi.createCalendar("Personal", ZoneId.of("UTC"));
    multi.useCalendar("Personal");
    assertNotNull(multi.getCurrentCalendar());
  }

  /**
   * Tests selecting a calendar that doesn't exist.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testUseCalendarNotFound() {
    multi.useCalendar("Missing");
  }

  /**
   * Tests selecting a calendar with a null name.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testUseCalendarNullName() {
    multi.useCalendar(null);
  }

  /**
   * Tests getCurrentCalendar throws if no calendar is in use.
   */
  @Test(expected = IllegalStateException.class)
  public void testGetCurrentCalendarUnset() {
    multi.getCurrentCalendar();
  }

  // ===========================
  // Copying event related tests
  // ===========================

  /**
   * Tests copying a valid event to another calendar across timezones.
   */
  @Test
  public void testCopyEventValid() {
    // Setup source calendar in UTC
    multi.createCalendar("Source", ZoneId.of("UTC"));
    multi.useCalendar("Source");

    LocalDateTime sourceStart = LocalDateTime.of(2025, 6, 1,
            10, 0); // UTC
    LocalDateTime sourceEnd = LocalDateTime.of(2025, 6, 1, 11,
            0);
    multi.getCurrentCalendar().createEvent("Event", sourceStart, sourceEnd,
            "Desc", EventStatus.PUBLIC, "Room");

    // Setup destination calendar in Chicago (UTC-5 during summer)
    ZoneId targetZone = ZoneId.of("America/Chicago");
    multi.createCalendar("Dest", targetZone);

    // Calculate what the adjusted start time should be in target zone
    ZonedDateTime zonedSource = sourceStart.atZone(ZoneId.of("UTC"));
    LocalDateTime expectedTargetStart = zonedSource.withZoneSameInstant(targetZone)
            .toLocalDateTime();

    // Copy the event to the destination calendar
    multi.copyEvent("Event", sourceStart, "Dest", expectedTargetStart);

    // Switch to destination calendar and get events
    multi.useCalendar("Dest");
    List<ROIEvent> events = multi.getCurrentCalendar().getEventsOn(expectedTargetStart
            .toLocalDate());

    assertEquals(1, events.size());
    IEvent copied = (IEvent) events.get(0);
    assertEquals("Event", copied.getSubject());
    assertEquals(expectedTargetStart, copied.getStart());
    assertEquals(expectedTargetStart.plusHours(1), copied.getEnd());
  }

  /**
   * Tests copying an event that doesn't exist.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testCopyEventNameNotFound() {
    multi.createCalendar("Src", ZoneId.of("UTC"));
    multi.useCalendar("Src");
    multi.createCalendar("Dst", ZoneId.of("UTC"));
    multi.copyEvent("X", LocalDateTime.now(), "Dst", LocalDateTime.now());
  }

  /**
   * Tests copying an event with a null event name throws an exception.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testCopyEventNullName() {
    // setup: ensure calendar is selected
    multi.createCalendar("source", ZoneId.of("UTC"));
    multi.createCalendar("target", ZoneId.of("UTC"));
    multi.useCalendar("source");

    // Act: call copyEvent with null name
    multi.copyEvent(null, LocalDateTime.now(), "target",
            LocalDateTime.now());
  }

  /**
   * Tests that copying an event with a null target start time throws an exception.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testCopyEventNullTargetTime() {
    // Setup valid source and target calendars
    multi.createCalendar("source", ZoneId.of("UTC"));
    multi.createCalendar("target", ZoneId.of("UTC"));
    multi.useCalendar("source");

    // Add a real event to copy
    LocalDateTime start = LocalDateTime.of(2025, 6, 10, 9, 0);
    LocalDateTime end = LocalDateTime.of(2025, 6, 10, 10, 0);
    multi.getCurrentCalendar().createEvent("A", start, end, null,
            EventStatus.PUBLIC, null);

    // Act: attempt to copy to a null start time
    multi.copyEvent("A", start, "target", null); // should throw
  }

  // ===============================
  // Copying events on related tests
  // ===============================

  /**
   * Tests copying all events from one day to another calendar.
   */
  @Test
  public void testCopyEventsOnValid() {
    multi.createCalendar("S", ZoneId.of("UTC"));
    multi.useCalendar("S");
    multi.getCurrentCalendar().createEvent("CopyMe",
            LocalDateTime.of(2025, 6, 1, 8, 0),
            LocalDateTime.of(2025, 6, 1, 9, 0),
            "", EventStatus.PUBLIC, "");

    multi.createCalendar("T", ZoneId.of("UTC"));
    multi.copyEventsOn(LocalDate.of(2025, 6, 1), "T",
            LocalDate.of(2025, 6, 5));
    multi.useCalendar("T");

    List<IEvent> events = new ArrayList<>();
    for (ROIEvent e : multi.getCurrentCalendar().getEventsOn(LocalDate.of(2025,
            6, 5))) {
      events.add((IEvent) e);
    }

    assertEquals(1, events.size());
    assertEquals("CopyMe", events.get(0).getSubject());
  }

  /**
   * Tests copying events to a non-existent target calendar.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testCopyEventsOnTargetMissing() {
    multi.createCalendar("Only", ZoneId.of("UTC"));
    multi.useCalendar("Only");
    multi.copyEventsOn(LocalDate.of(2025, 6, 1), "Missing",
            LocalDate.of(2025, 6, 2));
  }

  /**
   * Tests that only matching series instances are copied when they occur on the source date.
   */
  @Test
  public void testCopyEventsOnWithSeriesPartialMatch() {
    multi.createCalendar("Source", ZoneId.of("UTC"));
    multi.useCalendar("Source");

    // Create a MWF series starting May 30 (Friday is skipped)
    multi.getCurrentCalendar().createEvent("Recurring",
            LocalDateTime.of(2025, 5, 26, 8, 0), // Monday
            LocalDateTime.of(2025, 5, 26, 9, 0),
            "", EventStatus.PUBLIC, "MWF for 5 times");

    // Create target calendar and copy only events that occurred on 2025-05-28 (Wednesday)
    multi.createCalendar("Target", ZoneId.of("UTC"));
    multi.copyEventsOn(LocalDate.of(2025, 5, 28), "Target",
            LocalDate.of(2025, 6, 10));
    multi.useCalendar("Target");

    List<ROIEvent> copied = new ArrayList<>(multi.getCurrentCalendar()
            .getEventsOn(LocalDate.of(2025, 6, 10)));
    assertEquals(0, copied.size());
  }

  /**
   * Tests copying events to the same calendar (self-copying).
   */
  @Test
  public void testCopyEventsOnSameCalendar() {
    multi.createCalendar("Self", ZoneId.of("UTC"));
    multi.useCalendar("Self");

    multi.getCurrentCalendar().createEvent("Echo",
            LocalDateTime.of(2025, 6, 1, 10, 0),
            LocalDateTime.of(2025, 6, 1, 11, 0),
            "", EventStatus.PUBLIC, "");

    multi.copyEventsOn(LocalDate.of(2025, 6, 1), "Self",
            LocalDate.of(2025, 6, 3));

    List<ROIEvent> events = new ArrayList<>(multi.getCurrentCalendar()
            .getEventsOn(LocalDate.of(2025, 6, 3)));
    assertEquals(1, events.size());
    assertEquals("Echo", events.get(0).getSubject());
  }

  /**
   * Tests copying events from a source calendar in one timezone to a calendar in a
   * different timezone.
   */
  @Test
  public void testCopyEventsOnTimezoneShift() {
    multi.createCalendar("NYC", ZoneId.of("America/New_York"));
    multi.useCalendar("NYC");

    multi.getCurrentCalendar().createEvent("Morning Meeting",
            LocalDateTime.of(2025, 6, 1, 9, 0),
            LocalDateTime.of(2025, 6, 1, 10, 0),
            "", EventStatus.PUBLIC, "");

    multi.createCalendar("LA", ZoneId.of("America/Los_Angeles"));
    multi.copyEventsOn(LocalDate.of(2025, 6, 1), "LA",
            LocalDate.of(2025, 6, 5));

    multi.useCalendar("LA");
    List<ROIEvent> copied = multi.getCurrentCalendar().getEventsOn(LocalDate.of(2025,
            6, 5));
    assertEquals(1, copied.size());

    // 9am ET → 6am PT
    assertEquals(LocalDateTime.of(2025, 6, 5, 6, 0),
            copied.get(0).getStart());
    assertEquals(LocalDateTime.of(2025, 6, 5, 7, 0),
            copied.get(0).getEnd());
  }

  // ====================================
  // Copying events between related tests
  // ====================================

  /**
   * Tests copying events from a date range to another calendar.
   */
  @Test
  public void testCopyEventsBetweenValid() {
    multi.createCalendar("From", ZoneId.of("UTC"));
    multi.useCalendar("From");
    multi.getCurrentCalendar().createEvent("One",
            LocalDateTime.of(2025, 6, 2, 9, 0),
            LocalDateTime.of(2025, 6, 2, 10, 0),
            "", EventStatus.PUBLIC, "");

    multi.createCalendar("To", ZoneId.of("UTC"));
    multi.copyEventsBetween(LocalDate.of(2025, 6, 2),
            LocalDate.of(2025, 6, 2),
            "To",
            LocalDate.of(2025, 6, 10));

    multi.useCalendar("To");

    List<IEvent> events = new ArrayList<>();
    for (ROIEvent e : multi.getCurrentCalendar().getEventsOn(LocalDate.of(2025, 6,
            10))) {
      events.add((IEvent) e);
    }

    assertEquals(1, events.size());
    assertEquals("One", events.get(0).getSubject());
  }


  /**
   * Tests copying with a start date after the end date.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testCopyEventsBetweenInvalidRange() {
    multi.createCalendar("F", ZoneId.of("UTC"));
    multi.createCalendar("T", ZoneId.of("UTC"));
    multi.useCalendar("F");
    multi.copyEventsBetween(LocalDate.of(2025, 6, 10), LocalDate.of(
            2025, 6, 1), "T", LocalDate.of(2025,
            6, 12));
  }

  /**
   * Tests copying events to a target calendar that doesn't exist.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testCopyEventsBetweenNullTarget() {
    multi.createCalendar("X", ZoneId.of("UTC"));
    multi.useCalendar("X");
    multi.copyEventsBetween(LocalDate.of(2025, 6, 1), LocalDate.of(
            2025, 6, 3), "Null", LocalDate.of(2025,
            6, 4));
  }

  /**
   * Tests copying a range of events across timezones where the source is ahead of the target.
   */
  @Test
  public void testCopyEventsBetweenTimezoneShiftEastToWest() {
    multi.createCalendar("EST", ZoneId.of("America/New_York")); // UTC-4
    multi.useCalendar("EST");

    // Create 9 AM event in New York
    multi.getCurrentCalendar().createEvent("Morning EST",
            LocalDateTime.of(2025, 6, 1, 9, 0),
            LocalDateTime.of(2025, 6, 1, 10, 0),
            "", EventStatus.PUBLIC, "");

    // Create target calendar in Pacific Time
    multi.createCalendar("PST", ZoneId.of("America/Los_Angeles")); // UTC-7

    multi.copyEventsBetween(
            LocalDate.of(2025, 6, 1),
            LocalDate.of(2025, 6, 1),
            "PST",
            LocalDate.of(2025, 6, 5)
    );

    multi.useCalendar("PST");

    // PST time should reflect timezone shift: 9 AM EST → 6 AM PST
    List<ROIEvent> events = multi.getCurrentCalendar().getEventsOn(LocalDate.of(2025,
            6, 5));
    assertEquals(1, events.size());
    assertEquals("Morning EST", events.get(0).getSubject());
    assertEquals(LocalDateTime.of(2025, 6, 5, 6, 0),
            events.get(0).getStart());
    assertEquals(LocalDateTime.of(2025, 6, 5, 7, 0),
            events.get(0).getEnd());
  }

  /**
   * Tests copying a range of events from multiple days into a single contiguous target range.
   */
  @Test
  public void testCopyEventsBetweenMultiDayRangeWithOffset() {
    multi.createCalendar("A", ZoneId.of("UTC"));
    multi.useCalendar("A");

    multi.getCurrentCalendar().createEvent("Day 1",
            LocalDateTime.of(2025, 6, 1, 8, 0),
            LocalDateTime.of(2025, 6, 1, 9, 0),
            "", EventStatus.PUBLIC, "");

    multi.getCurrentCalendar().createEvent("Day 2",
            LocalDateTime.of(2025, 6, 2, 8, 0),
            LocalDateTime.of(2025, 6, 2, 9, 0),
            "", EventStatus.PUBLIC, "");

    multi.createCalendar("B", ZoneId.of("UTC"));
    multi.copyEventsBetween(LocalDate.of(2025, 6, 1),
            LocalDate.of(2025, 6, 2),
            "B", LocalDate.of(2025, 6, 10));

    multi.useCalendar("B");

    List<ROIEvent> d1 = multi.getCurrentCalendar().getEventsOn(LocalDate.of(2025, 6,
            10));
    List<ROIEvent> d2 = multi.getCurrentCalendar().getEventsOn(LocalDate.of(2025, 6,
            11));

    assertEquals(1, d1.size());
    assertEquals("Day 1", d1.get(0).getSubject());

    assertEquals(1, d2.size());
    assertEquals("Day 2", d2.get(0).getSubject());
  }

  /**
   * Tests copying events into an already-used calendar without overwriting anything.
   */
  @Test
  public void testCopyEventsBetweenToPopulatedCalendar() {
    multi.createCalendar("Origin", ZoneId.of("UTC"));
    multi.useCalendar("Origin");

    multi.getCurrentCalendar().createEvent("Original",
            LocalDateTime.of(2025, 6, 1, 9, 0),
            LocalDateTime.of(2025, 6, 1, 10, 0),
            "", EventStatus.PUBLIC, "");

    multi.createCalendar("Dest", ZoneId.of("UTC"));
    multi.useCalendar("Dest");

    multi.getCurrentCalendar().createEvent("Existing",
            LocalDateTime.of(2025, 6, 5, 15, 0),
            LocalDateTime.of(2025, 6, 5, 16, 0),
            "", EventStatus.PUBLIC, "");

    multi.copyEventsBetween(LocalDate.of(2025, 6, 1),
            LocalDate.of(2025, 6, 1),
            "Dest",
            LocalDate.of(2025, 6, 5));

    List<ROIEvent> all = multi.getCurrentCalendar().getEventsOn(LocalDate.of(2025, 6,
            5));
    assertEquals(1, all.size());
  }
}
