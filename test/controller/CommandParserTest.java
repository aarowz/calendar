// Dreshta Boghra & Aaron Zhou
// CS3500 HW4

package controller;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test class for the CommandParser.
 * This class includes tests to verify correct parsing of valid commands,
 * handling of invalid input formats, recognition of known commands, and
 * validation of required arguments and edge cases.
 */
public class CommandParserTest {

  /**
   * Verifies that a well-formed 'create-event' command is parsed successfully.
   */
  @Test
  public void testParseCreateEventCommand() {
    // TODO: Implement
  }

  /**
   * Verifies correct parsing of a valid 'edit-event' command.
   */
  @Test
  public void testParseEditEventCommand() {
    // TODO: Implement
  }

  /**
   * Verifies that a valid 'remove-event' command is parsed without error.
   */
  @Test
  public void testParseRemoveEventCommand() {
    // TODO: Implement
  }

  /**
   * Ensures that a recurring event creation command with repeat parameters
   * is parsed as expected.
   */
  @Test
  public void testParseRecurringEventCommand() {
    // TODO: Implement
  }

  /**
   * Verifies that invalid commands result in an appropriate exception.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testInvalidCommandThrows() {
    // TODO: Implement
  }

  /**
   * Ensures the parser reports an error when required fields are missing.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testMissingRequiredFields() {
    // TODO: Implement
  }

  /**
   * Confirms that unknown commands are rejected properly.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testUnknownCommandFails() {
    // TODO: Implement
  }

  /**
   * Checks whether the parser handles extra whitespace and formatting consistently.
   */
  @Test
  public void testWhitespaceNormalization() {
    // TODO: Implement
  }

  /**
   * Verifies that quoted strings are correctly preserved during parsing.
   */
  @Test
  public void testQuotedArgumentPreserved() {
    // TODO: Implement
  }

  /**
   * Ensures that empty or null inputs result in a parsing error.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testEmptyInputFails() {
    // TODO: Implement
  }
}