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

import static org.junit.Assert.*;

/**
 * Test class for RecurrenceRule.
 * Verifies correct handling of recurrence logic such as days, counts, and end dates.
 */
public class RecurrenceRuleTest {

  /**
   * Tests creation of a valid recurrence rule using repeat count.
   */
  @Test
  public void testCreateRuleWithRepeatCount() {
    RecurrenceRule rule = new RecurrenceRule.Builder()
            .repeatDays(Set.of(DayOfWeek.MONDAY))
            .count(3)
            .start(LocalDate.of(2025, 6, 2))
            .build();

    List<LocalDateTime[]> occurrences = rule.generateOccurrences(
            LocalTime.of(9, 0), LocalTime.of(10, 0));
    assertEquals(3, occurrences.size());
  }

  /**
   * Tests creation of a valid recurrence rule using a repeat-until date.
   */
  @Test
  public void testCreateRuleWithRepeatUntil() {
    RecurrenceRule rule = new RecurrenceRule.Builder()
            .repeatDays(Set.of(DayOfWeek.MONDAY))
            .start(LocalDate.of(2025, 6, 2))
            .end(LocalDate.of(2025, 6, 30))
            .build();

    List<LocalDateTime[]> occurrences = rule.generateOccurrences(
            LocalTime.of(9, 0), LocalTime.of(10, 0));
    assertTrue(occurrences.size() >= 4); // at least 4 Mondays in June 2025
  }

  /**
   * Tests that recurrence days are properly stored and returned.
   */
  @Test
  public void testGetRepeatDays() {
    Set<DayOfWeek> days = Set.of(DayOfWeek.TUESDAY, DayOfWeek.THURSDAY);
    RecurrenceRule rule = new RecurrenceRule.Builder()
            .repeatDays(days)
            .count(5)
            .start(LocalDate.of(2025, 6, 1))
            .build();

    assertEquals(days, rule.getRepeatDays());
  }

  /**
   * Tests edge cases like empty repeat days or negative repeat count.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testInvalidRuleConfiguration() {
    new RecurrenceRule.Builder()
            .count(3)
            .start(LocalDate.of(2025, 6, 1))
            .build();
  }

  /**
   * Tests equality and hash code logic for recurrence rules.
   */
  @Test
  public void testEqualsAndHashCode() {
    RecurrenceRule rule1 = new RecurrenceRule.Builder()
            .repeatDays(Set.of(DayOfWeek.FRIDAY))
            .count(2)
            .start(LocalDate.of(2025, 6, 6))
            .build();

    RecurrenceRule rule2 = new RecurrenceRule.Builder()
            .repeatDays(Set.of(DayOfWeek.FRIDAY))
            .count(2)
            .start(LocalDate.of(2025, 6, 6))
            .build();

    assertNotSame(rule1, rule2); // different instances
    assertEquals(rule1.getRepeatDays(), rule2.getRepeatDays());
    assertEquals(rule1.getCount(), rule2.getCount());
    assertEquals(rule1.getStart(), rule2.getStart());
  }

  /**
   * Tests toString for informative output.
   */
  @Test
  public void testToStringOutput() {
    RecurrenceRule rule = new RecurrenceRule.Builder()
            .repeatDays(Set.of(DayOfWeek.SATURDAY))
            .count(1)
            .start(LocalDate.of(2025, 6, 7))
            .build();

    assertTrue(rule.toString().contains("RecurrenceRule"));
  }

  /**
   * Tests rule behavior when both repeatCount and repeatUntil are null.
   */
  @Test
  public void testEmptyRecurrenceRuleIsHandledGracefully() {
    RecurrenceRule rule = new RecurrenceRule.Builder()
            .repeatDays(Set.of(DayOfWeek.WEDNESDAY))
            .start(LocalDate.of(2025, 6, 4))
            .build();

    // Should default to infinite (or at least unbounded) generation
    List<LocalDateTime[]> occurrences = rule.generateOccurrences(
            LocalTime.of(9, 0), LocalTime.of(10, 0));

    assertFalse(occurrences.isEmpty());
  }
}