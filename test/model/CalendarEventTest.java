// Dreshta Boghra & Aaron Zhou
// CS3500 HW4

package model;

import java.time.LocalDateTime;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Unit tests for the CalendarEvent class.
 * Validates immutability, field access, overlaps, and invalid input scenarios.
 */
public class CalendarEventTest {

  /**
   * Tests valid event getters return expected data.
   */
  @Test
  public void testGetMethods() {
    // TODO: Implement
  }

  /**
   * Tests that overlapping events correctly return true or false.
   */
  @Test
  public void testOverlapsWith() {
    // TODO: Implement
  }

  /**
   * Tests that building an event with a null subject throws an exception.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testMissingSubjectThrows() {
    // TODO: Implement
  }

  /**
   * Tests that building an event with a null start time throws an exception.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testMissingStartThrows() {
    // TODO: Implement
  }

  /**
   * Tests that supplying optional fields without required subject throws an error.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testOptionalWithoutSubject() {
    // TODO: Implement
  }

  /**
   * Tests that supplying optional fields without required start time throws an error.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testOptionalWithoutStart() {
    // TODO: Implement
  }

  /**
   * Tests that an event with invalid date range (end before start) is rejected.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testInvalidTimeRangeThrows() {
    // TODO: Implement
  }

  /**
   * Tests that two events with identical subject and start time are treated as duplicates.
   */
  @Test
  public void testDuplicateEventEquality() {
    // TODO: Implement
  }

  /**
   * Tests that equals() and hashCode() are consistent for equal events.
   */
  @Test
  public void testEqualsAndHashCodeConsistency() {
    // TODO: Implement
  }

  /**
   * Tests immutability of CalendarEvent after creation.
   */
  @Test
  public void testEventIsImmutable() {
    // TODO: Implement
  }

  /**
   * Tests the with-methods correctly return updated event copies.
   */
  @Test
  public void testWithMethodsReturnModifiedCopy() {
    // TODO: Implement
  }

  /**
   * Tests that all-day event correctly defaults to 8am-5pm when end time is omitted.
   */
  @Test
  public void testAllDayEventDefaultTiming() {
    // TODO: Implement
  }
}