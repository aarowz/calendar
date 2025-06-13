// Dreshta Boghra & Aaron Zhou
// CS3500 HW5

package util;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import model.CalendarEvent;
import model.EventStatus;
import model.IEvent;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Testing class for the TimeZoneUtil class.
 */
public class TimeZoneUtilTest {

  @Before
  public void setUp() {
    // Setup logic if needed
  }

  // NOTE: We used big headers to split up the tests. The last self evaluation had 38 questions.
  // If we don't split the tests up into categories, we can't complete the self evaluations on
  // time.

  // ==============================
  // Core Time Conversion Tests
  // ==============================

  /**
   * Tests conversion from LocalDateTime to ZonedDateTime.
   */
  @Test
  public void testToZonedDateTime() {
    LocalDateTime local = LocalDateTime.of(2025, 3, 10, 14, 30);
    ZoneId zone = ZoneId.of("America/New_York");

    ZonedDateTime zoned = TimeZoneUtil.toZonedDateTime(local, zone);

    assertEquals(local, zoned.toLocalDateTime());
    assertEquals(zone, zoned.getZone());
  }

  /**
   * Tests conversion from ZonedDateTime to LocalDateTime.
   */
  @Test
  public void testToLocalDateTime() {
    ZoneId zone = ZoneId.of("America/Los_Angeles");
    LocalDateTime expectedLocal = LocalDateTime.of(2025, 11, 5, 9, 0);
    ZonedDateTime zoned = expectedLocal.atZone(zone);

    LocalDateTime result = TimeZoneUtil.toLocalDateTime(zoned);

    assertEquals(expectedLocal, result);
  }

  /**
   * Tests that converting to ZonedDateTime and back yields the original value for the same zone.
   */
  @Test
  public void testRoundTripSameZoneConversion() {
    LocalDateTime original = LocalDateTime.of(2025, 10, 15, 18, 45);
    ZoneId zone = ZoneId.of("Europe/London");

    ZonedDateTime zoned = TimeZoneUtil.toZonedDateTime(original, zone);
    LocalDateTime result = TimeZoneUtil.toLocalDateTime(zoned);

    assertEquals(original, result);
  }

  // ==============================
  // Timezone Validation Tests
  // ==============================

  /**
   * Tests that a valid timezone string returns true.
   */
  @Test
  public void testIsValidZone_ValidZone() {
    assertTrue(TimeZoneUtil.isValidZone("America/New_York"));
    assertTrue(TimeZoneUtil.isValidZone("Europe/Paris"));
    assertTrue(TimeZoneUtil.isValidZone("Asia/Tokyo"));
  }

  /**
   * Tests that an invalid timezone string returns false.
   */
  @Test
  public void testIsValidZone_InvalidZone() {
    assertFalse(TimeZoneUtil.isValidZone("New_York"));         // missing "America/"
    assertFalse(TimeZoneUtil.isValidZone("Europe"));           // too vague
    assertFalse(TimeZoneUtil.isValidZone("Fake/Zone"));        // not real
    assertFalse(TimeZoneUtil.isValidZone(""));                 // empty string
    assertFalse(TimeZoneUtil.isValidZone("america/new_york")); // case-sensitive
  }

  /**
   * Tests that timezones are case-sensitive and must match IANA format exactly.
   */
  @Test
  public void testIsValidZone_CaseSensitivity() {
    // correct case should pass
    Assert.assertTrue(TimeZoneUtil.isValidZone("America/Chicago"));

    // incorrect case should fail
    Assert.assertFalse(TimeZoneUtil.isValidZone("america/chicago"));
    Assert.assertFalse(TimeZoneUtil.isValidZone("AMERICA/CHICAGO"));
    Assert.assertFalse(TimeZoneUtil.isValidZone("America/chicago"));
  }

  /**
   * Tests that null and empty timezone strings are rejected.
   */
  @Test
  public void testIsValidZone_NullOrEmpty() {
    Assert.assertFalse(TimeZoneUtil.isValidZone(null));
    Assert.assertFalse(TimeZoneUtil.isValidZone(""));
    Assert.assertFalse(TimeZoneUtil.isValidZone("   ")); // whitespace only
  }

  // ==============================
  // Event Cloning & Time Shifting
  // ==============================

  /**
   * Tests that an event is correctly cloned with new start and end times.
   */
  @Test
  public void testCopyWithNewTimes() {
    // original times
    LocalDateTime originalStart = LocalDateTime.of(2025, 6, 20, 10, 0);
    LocalDateTime originalEnd = LocalDateTime.of(2025, 6, 20, 11, 0);

    // new times for the clone
    LocalDateTime newStart = LocalDateTime.of(2025, 6, 21, 14, 0);
    LocalDateTime newEnd = LocalDateTime.of(2025, 6, 21, 15, 0);

    // build the original event using your Builder
    IEvent original = new CalendarEvent.Builder()
            .subject("Workshop")
            .start(originalStart)
            .end(originalEnd)
            .description("Project prep")
            .location("Zoom")
            .status(EventStatus.PUBLIC)
            .build();

    // clone with updated times
    IEvent copied = TimeZoneUtil.copyWithNewTimes(original, newStart, newEnd);

    // verify it's a different object
    Assert.assertNotSame(original, copied);

    // all fields should match except the start/end times
    Assert.assertEquals("Workshop", copied.getSubject());
    Assert.assertEquals("Project prep", copied.getDescription());
    Assert.assertEquals("Zoom", copied.getLocation());
    Assert.assertEquals(EventStatus.PUBLIC, copied.getStatus());
    Assert.assertEquals(newStart, copied.getStart());
    Assert.assertEquals(newEnd, copied.getEnd());
  }

  /**
   * Tests converting a list of events from one timezone to another.
   */
  @Test
  public void testConvertEventsToZone() {
    ZoneId sourceZone = ZoneId.of("America/New_York"); // UTC-4 during DST
    ZoneId targetZone = ZoneId.of("Europe/London");    // UTC+1 during DST

    LocalDateTime sourceStart = LocalDateTime.of(2025, 7, 1, 10, 0);
    LocalDateTime sourceEnd = LocalDateTime.of(2025, 7, 1, 11, 0);

    IEvent original = new CalendarEvent.Builder()
            .subject("Morning Meeting")
            .start(sourceStart)
            .end(sourceEnd)
            .description("Team sync")
            .location("Zoom")
            .status(EventStatus.PRIVATE)
            .build();

    List<IEvent> converted = TimeZoneUtil.convertEventsToZone(
            List.of(original),
            sourceZone,
            targetZone
    );

    // validate the result
    Assert.assertEquals(1, converted.size());

    IEvent result = converted.get(0);

    // the subject and other fields should remain the same
    Assert.assertEquals("Morning Meeting", result.getSubject());
    Assert.assertEquals("Team sync", result.getDescription());
    Assert.assertEquals("Zoom", result.getLocation());
    Assert.assertEquals(EventStatus.PRIVATE, result.getStatus());

    // compute expected converted time using ZonedDateTime logic
    ZonedDateTime originalZdt = sourceStart.atZone(sourceZone);
    ZonedDateTime expectedZdt = originalZdt.withZoneSameInstant(targetZone);
    LocalDateTime expectedStart = expectedZdt.toLocalDateTime();

    ZonedDateTime originalEndZdt = sourceEnd.atZone(sourceZone);
    LocalDateTime expectedEnd = originalEndZdt.withZoneSameInstant(targetZone).toLocalDateTime();

    Assert.assertEquals(expectedStart, result.getStart());
    Assert.assertEquals(expectedEnd, result.getEnd());
  }

  /**
   * Tests conversion of events scheduled exactly on the hour (e.g., 14:00).
   */
  @Test
  public void testConvertEventsToZone_ExactHour() {
    // time zones
    ZoneId sourceZone = ZoneId.of("Asia/Tokyo");       // UTC+9
    ZoneId targetZone = ZoneId.of("America/Los_Angeles"); // UTC-7 (PDT in summer)

    // event scheduled exactly on the hour
    LocalDateTime sourceStart =
            LocalDateTime.of(2025, 8, 10, 14, 0); // 2:00 PM JST
    LocalDateTime sourceEnd =
            LocalDateTime.of(2025, 8, 10, 15, 0); // 3:00 PM JST

    // build original event
    IEvent original = new CalendarEvent.Builder()
            .subject("Code Review")
            .start(sourceStart)
            .end(sourceEnd)
            .description("Weekly engineering sync")
            .location("Meeting Room 3")
            .status(EventStatus.PUBLIC)
            .build();

    List<IEvent> result =
            TimeZoneUtil.convertEventsToZone(List.of(original), sourceZone, targetZone);

    Assert.assertEquals(1, result.size());

    IEvent converted = result.get(0);

    // check unchanged metadata
    Assert.assertEquals("Code Review", converted.getSubject());
    Assert.assertEquals("Weekly engineering sync", converted.getDescription());
    Assert.assertEquals("Meeting Room 3", converted.getLocation());
    Assert.assertEquals(EventStatus.PUBLIC, converted.getStatus());

    // compute expected times
    ZonedDateTime expectedStartZdt =
            sourceStart.atZone(sourceZone).withZoneSameInstant(targetZone);
    ZonedDateTime expectedEndZdt =
            sourceEnd.atZone(sourceZone).withZoneSameInstant(targetZone);

    Assert.assertEquals(expectedStartZdt.toLocalDateTime(), converted.getStart());
    Assert.assertEquals(expectedEndZdt.toLocalDateTime(), converted.getEnd());
  }

  /**
   * Tests conversion of events that start or end exactly at midnight.
   */
  @Test
  public void testConvertEventsToZone_MidnightBoundary() {
    // define source and target zones
    ZoneId sourceZone = ZoneId.of("UTC");
    ZoneId targetZone = ZoneId.of("America/Los_Angeles"); // PDT (UTC-7 in summer)

    // event starts at midnight UTC
    LocalDateTime sourceStart =
            LocalDateTime.of(2025, 7, 12, 0, 0);  // 00:00 UTC
    LocalDateTime sourceEnd =
            LocalDateTime.of(2025, 7, 12, 1, 0);  // 01:00 UTC

    // create original event
    IEvent original = new CalendarEvent.Builder()
            .subject("Midnight Maintenance")
            .start(sourceStart)
            .end(sourceEnd)
            .description("System restart window")
            .location("Data Center")
            .status(EventStatus.PRIVATE)
            .build();

    // convert event to target zone
    List<IEvent> converted =
            TimeZoneUtil.convertEventsToZone(List.of(original), sourceZone, targetZone);
    Assert.assertEquals(1, converted.size());

    IEvent result = converted.get(0);

    // validate unchanged fields
    Assert.assertEquals("Midnight Maintenance", result.getSubject());
    Assert.assertEquals("System restart window", result.getDescription());
    Assert.assertEquals("Data Center", result.getLocation());
    Assert.assertEquals(EventStatus.PRIVATE, result.getStatus());

    // compute expected converted times
    ZonedDateTime expectedStart = sourceStart.atZone(sourceZone).withZoneSameInstant(targetZone);
    ZonedDateTime expectedEnd = sourceEnd.atZone(sourceZone).withZoneSameInstant(targetZone);

    Assert.assertEquals(expectedStart.toLocalDateTime(), result.getStart());
    Assert.assertEquals(expectedEnd.toLocalDateTime(), result.getEnd());
  }

  // ==============================
  // Daylight Saving Time (DST) Tests
  // ==============================

  /**
   * Tests converting an event that falls during the spring-forward DST shift.
   */
  @Test
  public void testConvertAcrossDST_StartSpringForward() {
    // spring-forward in US: March 9, 2025, at 2:00 AM → clocks jump to 3:00 AM

    // observes DST
    ZoneId sourceZone = ZoneId.of("America/New_York");

    // also observes DST, but different rules
    ZoneId targetZone = ZoneId.of("Europe/London");

    // schedule event at 2:30 AM local (which doesn't exist)
    LocalDateTime springStart =
            LocalDateTime.of(2025, 3, 9, 2, 30); // non-existent local time
    LocalDateTime springEnd = LocalDateTime.of(2025, 3, 9, 3, 30);

    // build original event — this will auto-adjust forward internally
    IEvent original = new CalendarEvent.Builder()
            .subject("DST Jump Event")
            .start(springStart)
            .end(springEnd)
            .description("Handles spring-forward boundary")
            .location("Remote")
            .status(EventStatus.PUBLIC)
            .build();

    // convert event
    List<IEvent> converted =
            TimeZoneUtil.convertEventsToZone(List.of(original), sourceZone, targetZone);
    Assert.assertEquals(1, converted.size());

    IEvent result = converted.get(0);

    // validate unchanged metadata
    Assert.assertEquals("DST Jump Event", result.getSubject());
    Assert.assertEquals("Handles spring-forward boundary", result.getDescription());
    Assert.assertEquals("Remote", result.getLocation());
    Assert.assertEquals(EventStatus.PUBLIC, result.getStatus());

    // check expected adjusted times
    ZonedDateTime expectedStartZdt =
            springStart.atZone(sourceZone).withZoneSameInstant(targetZone);
    ZonedDateTime expectedEndZdt = springEnd.atZone(sourceZone).withZoneSameInstant(targetZone);

    Assert.assertEquals(expectedStartZdt.toLocalDateTime(), result.getStart());
    Assert.assertEquals(expectedEndZdt.toLocalDateTime(), result.getEnd());
  }

  /**
   * Tests converting an event that falls during the fall-back DST shift.
   */
  @Test
  public void testConvertAcrossDST_EndFallBack() {
    // DST ends in New York on Nov 2, 2025 at 2:00 AM — clocks go back to 1:00 AM
    ZoneId sourceZone = ZoneId.of("America/New_York");
    ZoneId targetZone = ZoneId.of("Europe/Berlin");

    // schedule an event during the ambiguous hour (1:30 AM local time, which occurs twice)
    LocalDateTime fallbackStart = LocalDateTime.of(2025, 11, 2, 1, 30);
    LocalDateTime fallbackEnd = LocalDateTime.of(2025, 11, 2, 2, 30);

    // build the original event
    IEvent original = new CalendarEvent.Builder()
            .subject("DST Fall-Back Event")
            .start(fallbackStart)
            .end(fallbackEnd)
            .description("Handles fall-back boundary")
            .location("Remote")
            .status(EventStatus.PRIVATE)
            .build();

    // convert event
    List<IEvent> converted =
            TimeZoneUtil.convertEventsToZone(List.of(original), sourceZone, targetZone);
    Assert.assertEquals(1, converted.size());

    IEvent result = converted.get(0);

    // validate unchanged metadata
    Assert.assertEquals("DST Fall-Back Event", result.getSubject());
    Assert.assertEquals("Handles fall-back boundary", result.getDescription());
    Assert.assertEquals("Remote", result.getLocation());
    Assert.assertEquals(EventStatus.PRIVATE, result.getStatus());

    // validate adjusted times
    ZonedDateTime expectedStartZdt =
            fallbackStart.atZone(sourceZone).withZoneSameInstant(targetZone);
    ZonedDateTime expectedEndZdt = fallbackEnd.atZone(sourceZone).withZoneSameInstant(targetZone);

    Assert.assertEquals(expectedStartZdt.toLocalDateTime(), result.getStart());
    Assert.assertEquals(expectedEndZdt.toLocalDateTime(), result.getEnd());
  }
}