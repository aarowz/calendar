// Dreshta Boghra & Aaron Zhou
// CS3500 HW4

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
   * Tests parsing a standard create-event command with date/time range.
   */
  @Test
  public void testParseCreateEvent() throws InvalidCommandException {
    ICommand cmd = CommandParser.parse("create event \"Team Meeting\" from 2025-06-10T14:00 to 2025-06-10T15:00");
    assertTrue(cmd instanceof CreateEventCommand);
  }

  /**
   * Tests parsing a create-event command for an all-day event.
   */
  @Test
  public void testParseCreateAllDayEvent() throws InvalidCommandException {
    ICommand cmd = CommandParser.parse("create event \"Hackathon\" on 2025-06-15");
    assertTrue(cmd instanceof CreateEventCommand);
  }

  /**
   * Tests parsing a command to edit a single calendar event.
   */
  @Test
  public void testParseEditEvent() throws InvalidCommandException {
    ICommand cmd = CommandParser.parse("edit event subject \"Team Meeting\" from 2025-06-10T14:00 to 2025-06-10T15:00 with \"Project Sync\"");
    assertTrue(cmd instanceof EditEventCommand);
  }

  /**
   * Tests parsing a command to edit future events in a series starting from a given date.
   */
  @Test
  public void testParseEditEvents() throws InvalidCommandException {
    ICommand cmd = CommandParser.parse("edit events location \"Yoga\" from 2025-06-01T08:00 with \"Gym\"");
    assertTrue(cmd instanceof EditEventsCommand);
  }

  /**
   * Tests parsing a command to edit all events in a series.
   */
  @Test
  public void testParseEditSeries() throws InvalidCommandException {
    ICommand cmd = CommandParser.parse("edit series status \"Yoga\" from 2025-06-01T08:00 with \"private\"");
    assertTrue(cmd instanceof EditSeriesCommand);
  }

  /**
   * Tests parsing a command to query events scheduled on a specific date.
   */
  @Test
  public void testParseQueryEventsOn() throws InvalidCommandException {
    ICommand cmd = CommandParser.parse("print events on 2025-06-01");
    assertTrue(cmd instanceof QueryEventsCommand);
  }

  /**
   * Tests parsing a command to query events within a date/time range.
   */
  @Test
  public void testParseQueryEventsInRange() throws InvalidCommandException {
    ICommand cmd = CommandParser.parse("print events from 2025-06-01T00:00 to 2025-06-07T23:59");
    assertTrue(cmd instanceof QueryEventsCommand);
  }

  /**
   * Tests parsing a command to show user availability at a specific time.
   */
  @Test
  public void testParseShowStatus() throws InvalidCommandException {
    ICommand cmd = CommandParser.parse("show status on 2025-06-01T12:00");
    assertTrue(cmd instanceof QueryEventsCommand);
  }

  /**
   * Tests parsing the exit command.
   */
  @Test
  public void testParseExit() throws InvalidCommandException {
    ICommand cmd = CommandParser.parse("exit");
    assertTrue(cmd instanceof ExitCommand);
  }

  /**
   * Tests that a command missing necessary fields throws an exception.
   */
  @Test(expected = InvalidCommandException.class)
  public void testParseInvalidCommand_MissingFields() throws InvalidCommandException {
    CommandParser.parse("create event from 2025-06-01T12:00");
  }

  /**
   * Tests that an unknown command keyword throws an exception.
   */
  @Test(expected = InvalidCommandException.class)
  public void testParseUnknownCommand() throws InvalidCommandException {
    CommandParser.parse("delete event");
  }

  /**
   * Tests that a malformed timestamp throws an exception.
   */
  @Test(expected = InvalidCommandException.class)
  public void testParseMalformedTimestamp() throws InvalidCommandException {
    CommandParser.parse("create event \"Bad Date\" from 2025-0610T14:00 to 2025-06-10T15:00");
  }

  /**
   * Tests that an invalid weekday abbreviation throws an exception.
   */
  @Test(expected = InvalidCommandException.class)
  public void testParseInvalidWeekdays() throws InvalidCommandException {
    CommandParser.parse("create event \"Lecture\" from 2025-06-01T10:00 to 2025-06-01T11:00 repeats XYZ for 5 times");
  }

  /**
   * Tests that an empty input string throws an exception.
   */
  @Test(expected = InvalidCommandException.class)
  public void testParseEmptyCommand() throws InvalidCommandException {
    CommandParser.parse("");
  }
}