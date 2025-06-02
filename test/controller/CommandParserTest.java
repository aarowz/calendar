// Dreshta Boghra & Aaron Zhou
// CS3500 HW4

package controller;

import exceptions.InvalidCommandException;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Unit tests for the CommandParser class to ensure valid command parsing
 * and appropriate error handling for malformed inputs.
 */
public class CommandParserTest {

  /**
   * Verifies that a valid create-event command is parsed into a CreateEventCommand object.
   */
  @Test
  public void testParseCreateCommandValid() throws InvalidCommandException {
    String input = "create-event 2025-06-10T14:00 Midterm Review";
    ICommand cmd = CommandParser.parse(input);
    assertTrue(cmd instanceof CreateEventCommand);
  }

  /**
   * Verifies that a valid edit-event command is parsed into an EditEventCommand object.
   */
  @Test
  public void testParseEditCommandValid() throws InvalidCommandException {
    String input = "edit-event 2025-06-10T14:00 Updated Title 2025-06-10T15:00";
    ICommand cmd = CommandParser.parse(input);
    assertTrue(cmd instanceof EditEventCommand);
  }

  /**
   * Verifies that a valid query-events command is parsed into a QueryEventsCommand object.
   */
  @Test
  public void testParseQueryCommandValid() throws InvalidCommandException {
    String input = "query-events 2025-06-01T00:00 2025-06-30T23:59";
    ICommand cmd = CommandParser.parse(input);
    assertTrue(cmd instanceof QueryEventsCommand);
  }

  /**
   * Verifies that the 'exit' command is parsed into an ExitCommand object.
   */
  @Test
  public void testParseExitCommandValid() throws InvalidCommandException {
    String input = "exit";
    ICommand cmd = CommandParser.parse(input);
    assertTrue(cmd instanceof ExitCommand);
  }

  /**
   * Verifies that an unrecognized command results in an InvalidCommandException.
   */
  @Test(expected = InvalidCommandException.class)
  public void testParseUnknownCommandThrows() throws InvalidCommandException {
    CommandParser.parse("dance-party 2025-06-10T20:00");
  }

  /**
   * Verifies that a create-event command missing a title throws an InvalidCommandException.
   */
  @Test(expected = InvalidCommandException.class)
  public void testParseCreateCommandMissingArgs() throws InvalidCommandException {
    CommandParser.parse("create-event 2025-06-10T14:00"); // Missing title
  }

  /**
   * Verifies that an edit-event command missing the new time throws an InvalidCommandException.
   */
  @Test(expected = InvalidCommandException.class)
  public void testParseEditCommandMissingArgs() throws InvalidCommandException {
    CommandParser.parse("edit-event 2025-06-10T14:00 Updated Title"); // Missing new time
  }

  /**
   * Verifies that a query-events command missing the end time throws an InvalidCommandException.
   */
  @Test(expected = InvalidCommandException.class)
  public void testParseQueryCommandMissingArgs() throws InvalidCommandException {
    CommandParser.parse("query-events 2025-06-01T00:00"); // Missing end time
  }

  /**
   * Verifies that passing null input throws an InvalidCommandException.
   */
  @Test(expected = InvalidCommandException.class)
  public void testParseNullInput() throws InvalidCommandException {
    CommandParser.parse(null);
  }

  /**
   * Verifies that passing an empty string as input throws an InvalidCommandException.
   */
  @Test(expected = InvalidCommandException.class)
  public void testParseEmptyInput() throws InvalidCommandException {
    CommandParser.parse("");
  }
}