// Dreshta Boghra & Aaron Zhou
// CS3500 HW4

package controller;

import org.junit.Test;

/**
 * Test class for the EditEventCommand.
 * This suite verifies that the command correctly modifies existing calendar events,
 * handling edge cases such as partial edits, invalid edits, and editing non-existent events.
 */
public class EditEventCommandTest {

  /**
   * Verifies that an existing event can be edited with new values.
   */
  @Test
  public void testEditEventSuccessfully() {
    // TODO: Implement
  }

  /**
   * Ensures that only provided fields are updated, and others are left unchanged.
   */
  @Test
  public void testEditPartialFields() {
    // TODO: Implement
  }

  /**
   * Verifies that an exception is thrown when attempting to edit a non-existent event.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testEditNonExistentEventThrowsException() {
    // TODO: Implement
  }

  /**
   * Confirms that edits do not result in time conflicts with other existing events.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testEditCausesConflict() {
    // TODO: Implement
  }

  /**
   * Tests that an edit with an invalid time range (end before start) is rejected.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testEditWithInvalidTimeRange() {
    // TODO: Implement
  }

  /**
   * Verifies that the success message is properly displayed after a valid edit.
   */
  @Test
  public void testEditSuccessMessageDisplayed() {
    // TODO: Implement
  }

  /**
   * Confirms that the command’s string representation contains the event subject.
   */
  @Test
  public void testToStringContainsSubject() {
    // TODO: Implement
  }
}