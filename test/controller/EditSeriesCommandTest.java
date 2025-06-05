// Dreshta Boghra & Aaron Zhou
// CS3500 HW4

package controller;

import org.junit.Test;

/**
 * Test class for the EditSeriesCommand.
 * This class verifies the correct behavior when editing recurring event series,
 * including recurrence fields, edge conditions, and error handling.
 */
public class EditSeriesCommandTest {

  /**
   * Verifies that the series is updated with new details such as subject and time.
   */
  @Test
  public void testEditSeriesFieldsSuccessfully() {
    // TODO: Implement
  }

  /**
   * Verifies that only recurrence details are updated without altering other fields.
   */
  @Test
  public void testEditSeriesRecurrenceOnly() {
    // TODO: Implement
  }

  /**
   * Ensures that edits to a series do not interfere with individual event instances.
   */
  @Test
  public void testEditSeriesDoesNotAffectInstances() {
    // TODO: Implement
  }

  /**
   * Confirms that an invalid recurrence update (e.g., empty repeat days) is rejected.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testInvalidRecurrenceThrowsException() {
    // TODO: Implement
  }

  /**
   * Verifies that editing a non-existent series results in appropriate error handling.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testEditNonExistentSeriesFails() {
    // TODO: Implement
  }

  /**
   * Checks that the view is notified with a message summarizing the series edit.
   */
  @Test
  public void testEditSeriesSuccessMessage() {
    // TODO: Implement
  }

  /**
   * Ensures the string representation of the command describes the target series.
   */
  @Test
  public void testToStringDescribesSeries() {
    // TODO: Implement
  }
}