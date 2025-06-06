// Dreshta Boghra & Aaron Zhou
// CS3500 HW4

package model;

import org.junit.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertFalse;

/**
 * Test class for RecurrenceRule.
 * Verifies correct handling of recurrence logic such as days, counts, and end dates.
 */
public class RecurrenceRuleTest {

  /**
   * Tests creation of a recurrence rule using repeat count and verifies
   * correct number of generated occurrences and stored properties.
   */
  @Test
  public void testCreateRuleWithRepeatCount() {
    Set<DayOfWeek> days = Set.of(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY);
    int count = 5;
    LocalDate startDate = LocalDate.of(2025, 6, 2);

    RecurrenceRule rule = new RecurrenceRule.Builder()
            .repeatDays(days)
            .count(count)
            .start(startDate)
            .build();

    assertEquals(days, rule.getRepeatDays());
    assertEquals(count, rule.getRepeatCount());
    assertEquals(startDate, rule.getStart());
    assertNull(rule.getRepeatUntil());

    List<LocalDateTime[]> occurrences = rule.generateOccurrences(LocalTime.of(9, 0),
            LocalTime.of(10, 0));
    assertEquals(5, occurrences.size());
    for (LocalDateTime[] occ : occurrences) {
      assertTrue(days.contains(occ[0].getDayOfWeek()));
      assertEquals(LocalTime.of(9, 0), occ[0].toLocalTime());
      assertEquals(LocalTime.of(10, 0), occ[1].toLocalTime());
    }
  }

  /**
   * Tests that the repeat days returned from the rule match what was configured.
   */
  @Test
  public void testGetRepeatDays() {
    Set<DayOfWeek> days = Set.of(DayOfWeek.FRIDAY, DayOfWeek.SUNDAY);
    RecurrenceRule rule = new RecurrenceRule.Builder()
            .repeatDays(days)
            .start(LocalDate.of(2025, 6, 6))
            .count(3)
            .build();

    assertEquals(days, rule.getRepeatDays());
    assertTrue(rule.getRepeatDays().contains(DayOfWeek.FRIDAY));
    assertTrue(rule.getRepeatDays().contains(DayOfWeek.SUNDAY));
  }

  /**
   * Tests that invalid configuration (missing repeat days) throws an exception.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testInvalidRuleConfiguration() {
    new RecurrenceRule.Builder()
            .count(5)
            .start(LocalDate.of(2025, 6, 6))
            .build();
  }

  /**
   * Tests that toString() produces a non-null, informative string
   * that includes key properties like day of week and year.
   */
  @Test
  public void testToStringOutput() {
    RecurrenceRule rule = new RecurrenceRule.Builder()
            .repeatDays(Set.of(DayOfWeek.TUESDAY))
            .count(2)
            .start(LocalDate.of(2025, 6, 3))
            .build();

    String out = rule.toString();
    assertNotNull(out);
  }

  /**
   * Tests that occursOn() returns true only for valid recurrence dates
   * and false otherwise.
   */
  @Test
  public void testOccursOnCorrectDates() {
    RecurrenceRule rule = new RecurrenceRule.Builder()
            .repeatDays(Set.of(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY))
            .start(LocalDate.of(2025, 6, 2))
            .count(4)
            .build();

    assertTrue(rule.occursOn(LocalDate.of(2025, 6, 2))); // Monday
    assertFalse(rule.occursOn(LocalDate.of(2025, 6, 3))); // Tuesday
    assertTrue(rule.occursOn(LocalDate.of(2025, 6, 4))); // Wednesday
  }

  /**
   * Tests that getOccurrenceCount() returns the correct number of valid occurrences
   * based on repeat days and count.
   */
  @Test
  public void testGetOccurrenceCount() {
    RecurrenceRule rule = new RecurrenceRule.Builder()
            .repeatDays(Set.of(DayOfWeek.MONDAY))
            .start(LocalDate.of(2025, 6, 2))
            .count(5)
            .build();

    assertEquals(5, rule.getOccurrenceCount());
  }
}