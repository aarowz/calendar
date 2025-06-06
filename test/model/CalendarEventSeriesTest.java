// Dreshta Boghra & Aaron Zhou
// CS3500 HW4

package model;

import org.junit.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for {@link CalendarEventSeries}.
 * Verifies correct recurrence generation and rule validation.
 */
public class CalendarEventSeriesTest {

  private CalendarEvent baseEvent() {
    return new CalendarEvent.Builder()
            .subject("Yoga")
            .start(LocalDateTime.of(2025, 6, 2, 7, 0))
            .end(LocalDateTime.of(2025, 6, 2, 8, 0))
            .description("Morning session")
            .status(EventStatus.PUBLIC)
            .location("Studio")
            .build();
  }

  /**
   * Tests that a series created with a repeat count generates the expected number of events.
   */
  @Test
  public void testSeriesWithRepeatCount() {
    RecurrenceRule rule = new RecurrenceRule.Builder()
            .repeatDays(Set.of(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY))
            .count(4)
            .start(LocalDate.of(2025, 6, 2))
            .build();

    CalendarEventSeries series = new CalendarEventSeries(baseEvent(), rule);
    List<IEvent> events = series.getAllOccurrences();
    assertEquals(4, events.size());
  }

  /**
   * Tests that a series created with a repeat-until date stops on or before that date.
   */
  @Test
  public void testSeriesWithRepeatUntilDate() {
    RecurrenceRule rule = new RecurrenceRule.Builder()
            .repeatDays(Set.of(DayOfWeek.TUESDAY))
            .start(LocalDate.of(2025, 6, 3))
            .repeatUntil(LocalDate.of(2025, 6, 24))
            .build();

    CalendarEventSeries series = new CalendarEventSeries(baseEvent(), rule);
    List<IEvent> events = series.getAllOccurrences();
    assertEquals(4, events.size());
  }

  /**
   * Tests that the recurrence rule stores and applies specified days of the week correctly.
   */
  @Test
  public void testRepeatDaysStoredCorrectly() {
    Set<DayOfWeek> days = Set.of(DayOfWeek.FRIDAY);
    RecurrenceRule rule = new RecurrenceRule.Builder()
            .repeatDays(days)
            .count(2)
            .start(LocalDate.of(2025, 6, 6))
            .build();

    CalendarEventSeries series = new CalendarEventSeries(baseEvent(), rule);
    List<IEvent> events = series.getAllOccurrences();
    assertEquals(2, events.size());
    assertEquals(DayOfWeek.FRIDAY, events.get(0).getStart().getDayOfWeek());
  }

  /**
   * Tests that invalid recurrence rules (e.g., negative count, empty days) throw exceptions.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testInvalidRepeatConfiguration() {
    new RecurrenceRule.Builder()
            .repeatDays(Set.of())
            .count(-1)
            .start(LocalDate.of(2025, 6, 1))
            .build();
  }

  /**
   * Tests that the series correctly expands into a list of IEvent instances.
   */
  @Test
  public void testExpandToEvents() {
    RecurrenceRule rule = new RecurrenceRule.Builder()
            .repeatDays(Set.of(DayOfWeek.MONDAY))
            .count(3)
            .start(LocalDate.of(2025, 6, 2))
            .build();

    CalendarEventSeries series = new CalendarEventSeries(baseEvent(), rule);
    List<IEvent> events = series.getAllOccurrences();
    assertEquals(3, events.size());
    for (IEvent e : events) {
      assertEquals("Yoga", e.getSubject());
    }
  }

  /**
   * Tests that the list of occurrences returned from a series is immutable.
   */
  @Test(expected = UnsupportedOperationException.class)
  public void testEventListImmutability() {
    RecurrenceRule rule = new RecurrenceRule.Builder()
            .repeatDays(Set.of(DayOfWeek.THURSDAY))
            .count(1)
            .start(LocalDate.of(2025, 6, 5))
            .build();

    CalendarEventSeries series = new CalendarEventSeries(baseEvent(), rule);
    List<IEvent> events = series.getAllOccurrences();
    events.add(baseEvent());
  }
}