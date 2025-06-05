// Dreshta Boghra & Aaron Zhou
// CS3500 HW4

package controller;

import org.junit.Test;

import exceptions.CommandExecutionException;

/**
 * Test class for the CreateEventCommand.
 * This suite verifies behavior for creating both single and recurring events,
 * ensuring proper interactions with the calendar model and expected side effects.
 */
public class CreateEventCommandTest {

  /**
   * Verifies that executing the command creates a single, non-recurring event.
   */
  @Test
  public void testExecuteCreatesSingleEvent() {
    // TODO: Implement
  }

  /**
   * Verifies creation of a recurring event series using a repeat count.
   */
  @Test
  public void testExecuteCreatesRecurringEventWithCount() {
    // TODO: Implement
  }

  /**
   * Verifies creation of a recurring event series using a repeat-until date.
   */
  @Test
  public void testExecuteCreatesRecurringEventWithUntilDate() {
    // TODO: Implement
  }

  /**
   * Verifies correct handling when optional fields like location and description are omitted.
   */
  @Test
  public void testExecuteWithMissingOptionalFields() {
    // TODO: Implement
  }

  /**
   * Verifies that invalid inputs result in a CommandExecutionException.
   */
  @Test(expected = CommandExecutionException.class)
  public void testExecuteFailsGracefullyOnModelError() {
    // TODO: Implement
  }

  /**
   * Ensures that events with invalid time ranges (end before start) are not accepted.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testExecuteRejectsInvalidTimeRange() {
    // TODO: Implement
  }

  /**
   * Verifies the output message after successful single event creation.
   */
  @Test
  public void testSuccessMessageSingleEvent() {
    // TODO: Implement
  }

  /**
   * Verifies the output message after successful series creation.
   */
  @Test
  public void testSuccessMessageRecurringSeries() {
    // TODO: Implement
  }

  /**
   * Confirms that the toString method reflects the event subject.
   */
  @Test
  public void testToStringDisplaysSubject() {
    // TODO: Implement
  }
}