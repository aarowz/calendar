// Dreshta Boghra & Aaron Zhou
// CS3500 HW5

package controller;

import exceptions.InvalidCommandException;
import model.CalendarMulti;
import model.DelegatorImpl;
import model.IDelegator;
import view.CalendarView;
import view.IView;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.time.ZoneId;

import static org.junit.Assert.assertTrue;

/**
 * Test class for the CommandParser.
 * Verifies correct parsing of valid commands, recognition of syntax rules,
 * and proper rejection of invalid or malformed input.
 */
public class CommandParserTest {

  /**
   * Tests parsing of a valid single event creation command.
   * Ensures correct command type and content.
   */
  @Test
  public void testParseCreateEventCommand() throws Exception {
    IDelegator model = new DelegatorImpl(new CalendarMulti());
    model.createCalendar("testcal", ZoneId.of("America/New_York"));
    model.useCalendar("testcal");

    String input = "create event Meeting from 2025-06-03T10:00 to 2025-06-03T11:00";
    ICommand cmd = CommandParser.parse(model, input);

    assertTrue(cmd instanceof CreateEventCommand);
    assertTrue(cmd.toString().toLowerCase().contains("meeting"));
  }

  /**
   * Tests parsing of a recurring event creation command with weekday and repetition count.
   */
  @Test
  public void testParseRecurringEventCommand() throws Exception {
    IDelegator model = new DelegatorImpl(new CalendarMulti());
    model.createCalendar("testcal", ZoneId.of("America/New_York"));
    model.useCalendar("testcal");

    String input = "create event Standup from 2025-06-02T09:00 to 2025-06-02T09:30 repeats " +
            "MWF for 3 times";
    ICommand cmd = CommandParser.parse(model, input);

    assertTrue(cmd instanceof CreateEventCommand);
    assertTrue(cmd.toString().toLowerCase().contains("standup"));
  }

  /**
   * Tests that an unknown command throws an InvalidCommandException.
   */
  @Test(expected = InvalidCommandException.class)
  public void testInvalidCommandThrows() throws InvalidCommandException {
    IDelegator model = new DelegatorImpl(new CalendarMulti());
    model.createCalendar("testcal", ZoneId.of("America/New_York"));
    model.useCalendar("testcal");

    CommandParser.parse(model, "launch rocket now");
  }

  /**
   * Tests that invalid weekday characters are rejected when parsing a recurring event command.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testRejectInvalidWeekdayCharacters() throws IllegalArgumentException,
          InvalidCommandException {
    IDelegator model = new DelegatorImpl(new CalendarMulti());
    model.createCalendar("testcal", ZoneId.of("America/New_York"));
    model.useCalendar("testcal");

    String input = "create event Practice from 2025-06-02T08:00 to 2025-06-02T09:00 " +
            "repeats MZ for 3 times";
    CommandParser.parse(model, input);
  }

  /**
   * Tests that missing required fields in a command causes an InvalidCommandException.
   */
  @Test(expected = InvalidCommandException.class)
  public void testMissingRequiredFields() throws InvalidCommandException {
    IDelegator model = new DelegatorImpl(new CalendarMulti());
    model.createCalendar("testcal", ZoneId.of("America/New_York"));
    model.useCalendar("testcal");

    CommandParser.parse(model, "create event Lunch");
  }

  /**
   * Tests that unknown command keywords are correctly rejected.
   */
  @Test(expected = InvalidCommandException.class)
  public void testUnknownCommandFails() throws InvalidCommandException {
    IDelegator model = new DelegatorImpl(new CalendarMulti());
    model.createCalendar("testcal", ZoneId.of("America/New_York"));
    model.useCalendar("testcal");

    CommandParser.parse(model, "explode event on 2025-06-06");
  }

  /**
   * Tests that a command with a start time after the end time is rejected.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testRejectFromAfterToDates() throws IllegalArgumentException,
          InvalidCommandException {
    IDelegator model = new DelegatorImpl(new CalendarMulti());
    model.createCalendar("testcal", ZoneId.of("America/New_York"));
    model.useCalendar("testcal");

    String input = "create event InvalidEvent from 2025-06-10T15:00 to 2025-06-10T14:00";
    CommandParser.parse(model, input);
  }

  /**
   * Tests that quoted arguments for event names are parsed and preserved correctly.
   */
  @Test
  public void testQuotedArgumentPreserved() throws InvalidCommandException {
    IDelegator model = new DelegatorImpl(new CalendarMulti());
    model.createCalendar("testcal", ZoneId.of("America/New_York"));
    model.useCalendar("testcal");

    String input = "create event \"Daily Check In\" from 2025-06-01T08:00 to 2025-06-01T08:15";
    ICommand cmd = CommandParser.parse(model, input);

    assertTrue(cmd instanceof CreateEventCommand);
    assertTrue(cmd.toString().contains("Daily Check In"));
  }

  /**
   * Tests that an empty or whitespace-only command input is rejected.
   */
  @Test(expected = InvalidCommandException.class)
  public void testEmptyInputFails() throws InvalidCommandException {
    IDelegator model = new DelegatorImpl(new CalendarMulti());
    model.createCalendar("testcal", ZoneId.of("America/New_York"));
    model.useCalendar("testcal");

    CommandParser.parse(model, " ");
  }

  /**
   * Tests controller behavior for a script containing both valid and invalid commands.
   * Ensures that valid commands are executed and invalid ones are reported.
   */
  @Test
  public void testInvalidCommandInScriptIsHandledAndReported() {
    String input = String.join("\n",
            "create calendar --name testcal --timezone America/New_York",
            "use calendar --name testcal",
            "create event Meeting from 2025-06-06T09:00 to 2025-06-06T10:00",
            "explode event on 2025-06-06",
            "exit"
    );

    ByteArrayInputStream byteInput = new ByteArrayInputStream(input.getBytes());
    Readable inputReader = new java.io.InputStreamReader(byteInput);

    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    PrintWriter writer = new PrintWriter(outputStream, true);

    IDelegator model = new DelegatorImpl(new CalendarMulti());
    IView view = new CalendarView.Builder().setOutput(writer).build();

    CalendarController controller = new CalendarController(model, view, inputReader);
    controller.run();

    String output = outputStream.toString().toLowerCase();

    assertTrue(output.contains("created calendar"));
    assertTrue(output.contains("now using calendar"));
    assertTrue(output.contains("event created"));
  }
}