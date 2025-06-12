package model;

import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class CalendarMultiTest {

  private ICalendarMulti multi;

  @Before
  public void setUp() {
    multi = new CalendarMulti();
  }

  // createCalendar

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
   * Tests creating a calendar with an invalid timezone.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testCreateCalendarInvalidZone() {
    multi.createCalendar("BadZone", ZoneId.of("Fake/Zone"));
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
    multi.createCalendar(null,
            ZoneId.of("UTC"));
  }

  /**
   * Tests creating a calendar with a null timezone.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testCreateCalendarNullZone() {
    multi.createCalendar("NullZone",
            null);
  }

  // editCalendar

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
  @Test(expected = IllegalArgumentException.class)
  public void testEditCalendarNullProperty() {
    multi.createCalendar("X", ZoneId.of("UTC"));
    multi.editCalendar("X", null, "test");
  }

  /**
   * Tests editing a calendar with a null property value.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testEditCalendarNullValue() {
    multi.createCalendar("X", ZoneId.of("UTC"));
    multi.editCalendar("X", "name", null);
  }

  // useCalendar

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

  // === getCurrentCalendar ===

  /**
   * Tests getCurrentCalendar throws if no calendar is in use.
   */
  @Test(expected = IllegalStateException.class)
  public void testGetCurrentCalendarUnset() {
    multi.getCurrentCalendar();
  }
  // copyEvent

  /**
   * Tests copying a valid event to another calendar.
   */
  @Test
  public void testCopyEventValid() {
    multi.createCalendar("Source", ZoneId.of("UTC"));
    multi.useCalendar("Source");
    ICalendar src = multi.getCurrentCalendar();
    src.createEvent("Event",
            LocalDateTime.of(2025, 6, 1, 10, 0),
            LocalDateTime.of(2025, 6, 1, 11, 0),
            "Desc", EventStatus.PUBLIC, "Room");

    multi.createCalendar("Dest", ZoneId.of("America/Chicago"));
    multi.copyEvent("Event",
            LocalDateTime.of(2025, 6, 1, 10, 0),
            "Dest",
            LocalDateTime.of(2025, 6, 2, 9, 0));

    multi.useCalendar("Dest");
    List<IEvent> events = new ArrayList<>();
    for (ROIEvent e : multi.getCurrentCalendar().getEventsOn(LocalDate.of(2025,
            6, 2))) {
      events.add((IEvent) e);
    }

    assertEquals(1, events.size());
    assertEquals("Event", events.get(0).getSubject());
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
   * Tests copying an event with a null event name.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testCopyEventNullName() {
    multi.copyEvent(null, LocalDateTime.now(), "X", LocalDateTime.now());
  }

  /**
   * Tests copying an event with a null target time.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testCopyEventNullTargetTime() {
    multi.copyEvent("A", LocalDateTime.now(), "X", null);
  }

  // copyEventsOn

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

  // copyEventsBetween

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
}
